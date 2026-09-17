package com.example.springai.lesson10.service;

import com.example.springai.lesson10.dto.Lesson10EmbeddingResponse;
import com.example.springai.lesson10.dto.Lesson10SimilarityRequest;
import com.example.springai.lesson10.dto.Lesson10SimilarityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Lesson10 Embedding 学习服务。
 *
 * <p>
 * 本 Service 主要演示 Spring AI 的 {@link EmbeddingModel}：
 * </p>
 *
 * <ul>
 *     <li>将单段文本转换为向量</li>
 *     <li>批量将多段文本转换为向量</li>
 *     <li>使用向量计算文本语义相似度</li>
 * </ul>
 *
 * <p>
 * 当前业务代码只依赖 Spring AI 的 EmbeddingModel 抽象，
 * 而不直接依赖某一个具体厂商的 EmbeddingModel 实现。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class Lesson10EmbeddingService {

    /**
     * Spring AI 提供的 Embedding 模型统一抽象。
     *
     * <p>
     * Spring Boot 会根据 application.yml 中的模型配置，
     * 自动创建对应的 EmbeddingModel Bean。
     * </p>
     *
     * <p>
     * 当前课程实际调用的是百炼 Embedding 模型，
     * 但业务代码不需要知道底层模型厂商。
     * </p>
     */
    private final EmbeddingModel embeddingModel;

    /**
     * 将单段文本转换为 Embedding 向量。
     *
     * <p>
     * 核心流程：
     * </p>
     *
     * <pre>
     * String
     * ↓
     * EmbeddingModel.embed(...)
     * ↓
     * float[]
     * </pre>
     *
     * @param text 需要进行向量化的文本
     * @return Embedding 处理结果
     */
    public Lesson10EmbeddingResponse embed(String text) {

        /*
         * 调用 EmbeddingModel 将文本转换成向量。
         *
         * 返回值是 float[]，
         * 数组中的每一个数字代表向量空间中的一个维度。
         */
        float[] vector = embeddingModel.embed(text);

        /*
         * 为了避免接口输出完整的 1024 个浮点数，
         * 这里仅截取前 10 位用于学习观察。
         *
         * 真正业务计算仍然使用完整 vector。
         */
        List<Float> vectorPreview = createVectorPreview(vector, 10);

        return new Lesson10EmbeddingResponse(
            text,
            vector.length,
            vectorPreview
        );
    }

    /**
     * 比较两段文本之间的语义相似度。
     *
     * <p>
     * 首先通过 EmbeddingModel 将两段文本分别转换成向量，
     * 然后使用余弦相似度进行比较。
     * </p>
     *
     * <pre>
     * leftText
     * ↓
     * Vector A
     *
     * rightText
     * ↓
     * Vector B
     *
     * Vector A + Vector B
     * ↓
     * Cosine Similarity
     * ↓
     * similarity
     * </pre>
     *
     * @param request 相似度比较请求
     * @return 文本语义相似度
     */
    public Lesson10SimilarityResponse similarity(
        Lesson10SimilarityRequest request) {

        /*
         * EmbeddingModel 支持批量向量化。
         *
         * 相比连续调用两次 embed(String)，
         * 批量调用在真实模型服务中通常更加合理，
         * 也为后面批量处理 Document 打基础。
         */
        List<float[]> vectors = embeddingModel.embed(
            List.of(
                request.leftText(),
                request.rightText()
            )
        );

        /*
         * 返回顺序和输入文本顺序对应。
         */
        float[] leftVector = vectors.get(0);

        float[] rightVector = vectors.get(1);

        /*
         * 使用两个完整向量计算余弦相似度。
         */
        double similarity = cosineSimilarity(
            leftVector,
            rightVector
        );

        return new Lesson10SimilarityResponse(
            request.leftText(),
            request.rightText(),
            similarity
        );
    }

    /**
     * 创建向量预览数据。
     *
     * <p>
     * 这里只用于接口展示，不参与真实相似度计算。
     * </p>
     *
     * @param vector 完整向量
     * @param limit  最多返回多少个向量元素
     * @return 向量预览列表
     */
    private List<Float> createVectorPreview(
        float[] vector,
        int limit) {

        /*
         * 防止 limit 大于实际向量长度。
         */
        int previewSize = Math.min(
            vector.length,
            limit
        );

        List<Float> preview =
            new ArrayList<>(previewSize);

        /*
         * 只截取向量前 previewSize 个数据。
         */
        for (int i = 0; i < previewSize; i++) {
            preview.add(vector[i]);
        }

        return preview;
    }

    /**
     * 计算两个向量之间的余弦相似度。
     *
     * <p>
     * 余弦相似度主要比较两个向量的方向是否接近。
     * </p>
     *
     * <p>
     * 计算公式：
     * </p>
     *
     * <pre>
     *              A · B
     * cos(A,B) = ----------
     *             |A| |B|
     * </pre>
     *
     * @param leftVector  第一个向量
     * @param rightVector 第二个向量
     * @return 两个向量的余弦相似度
     */
    private double cosineSimilarity(
        float[] leftVector,
        float[] rightVector) {

        /*
         * 两个向量必须处于相同维度的向量空间，
         * 否则不能进行正确比较。
         */
        if (leftVector.length != rightVector.length) {
            throw new IllegalArgumentException(
                "两个 Embedding 向量维度不一致，无法计算相似度"
            );
        }

        /*
         * A · B
         *
         * 表示两个向量的点积。
         */
        double dotProduct = 0.0D;

        /*
         * |A|²
         */
        double leftNorm = 0.0D;

        /*
         * |B|²
         */
        double rightNorm = 0.0D;

        /*
         * 遍历每一个向量维度。
         */
        for (int i = 0; i < leftVector.length; i++) {

            dotProduct +=
                leftVector[i] * rightVector[i];

            leftNorm +=
                leftVector[i] * leftVector[i];

            rightNorm +=
                rightVector[i] * rightVector[i];
        }

        /*
         * 防止出现零向量导致除零异常。
         */
        if (leftNorm == 0.0D || rightNorm == 0.0D) {
            throw new IllegalArgumentException(
                "Embedding 向量模长为 0，无法计算相似度"
            );
        }

        /*
         * 根据余弦相似度公式返回最终结果。
         */
        return dotProduct
            / (
            Math.sqrt(leftNorm)
                * Math.sqrt(rightNorm)
        );
    }
}
