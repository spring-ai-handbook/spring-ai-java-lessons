package com.example.springai.lesson14.service;

import com.example.springai.lesson14.dto.Lesson14Dtos.DocumentItem;
import com.example.springai.lesson14.dto.Lesson14Dtos.ImportResponse;
import com.example.springai.lesson14.dto.Lesson14Dtos.ReadResponse;
import com.example.springai.lesson14.dto.Lesson14Dtos.SearchRequest;
import com.example.springai.lesson14.dto.Lesson14Dtos.SearchResult;
import com.example.springai.lesson14.dto.Lesson14Dtos.SplitResponse;
import com.example.springai.lesson14.support.Lesson14PdfFileValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest.Builder;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Lesson14 PDF DocumentReader 核心业务服务。
 *
 * <p>负责完整 PDF 知识入库流程：</p>
 *
 * <pre>
 * MultipartFile
 * ↓
 * Resource
 * ↓
 * PagePdfDocumentReader
 * ↓
 * Document
 * ↓
 * TokenTextSplitter
 * ↓
 * Chunk
 * ↓
 * VectorStore
 * </pre>
 */
@Service
@RequiredArgsConstructor
public class Lesson14DocumentReaderService {

    /**
     * 业务侧统一文件名 metadata。
     */
    private static final String META_FILE_NAME = "fileName";

    /**
     * 文档来源。
     */
    private static final String META_SOURCE = "source";

    /**
     * 文件 MIME 类型。
     */
    private static final String META_CONTENT_TYPE = "contentType";

    /**
     * Chunk 对应的父 Document ID。
     */
    private static final String META_PARENT_DOCUMENT_ID = "parent_document_id";

    /**
     * 当前 Chunk 序号。
     */
    private static final String META_CHUNK_INDEX = "chunk_index";

    /**
     * 父 Document 总 Chunk 数。
     */
    private static final String META_TOTAL_CHUNKS = "total_chunks";

    private static final int DEFAULT_TOP_K = 4;

    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.0D;

    /**
     * Lesson11 已经配置好的 VectorStore。
     */
    private final VectorStore vectorStore;

    /**
     * PDF 上传文件统一校验组件。
     *
     * <p>所有文件校验都集中到该组件，
     * 当前 Service 不再自己重复编写文件校验逻辑。</p>
     */
    private final Lesson14PdfFileValidator pdfFileValidator;

    /**
     * 功能1：读取 PDF。
     *
     * <p>这里只执行：</p>
     *
     * <pre>
     * PDF
     * ↓
     * DocumentReader
     * ↓
     * Document
     * </pre>
     *
     * <p>不会进行 Embedding。</p>
     */
    public ReadResponse readPdf(MultipartFile file) {

        pdfFileValidator.validate(file);

        String fileName = pdfFileValidator.getSafeFileName(file);

        List<Document> documents = readPdfDocuments(file, fileName);

        return new ReadResponse(
            fileName,
            documents.size(),
            toDocumentItems(documents)
        );
    }

    /**
     * 功能2：读取 PDF 并切片。
     *
     * <p>该阶段属于 Extract + Transform，
     * 仍然不会调用 EmbeddingModel。</p>
     */
    public SplitResponse splitPdf(MultipartFile file) {

        pdfFileValidator.validate(file);

        String fileName = pdfFileValidator.getSafeFileName(file);

        List<Document> documents = readPdfDocuments(file, fileName);

        List<Document> chunks = splitDocuments(documents);

        return new SplitResponse(
            fileName,
            documents.size(),
            chunks.size(),
            toDocumentItems(chunks)
        );
    }

    /**
     * 功能3：完整导入 PDF 到 VectorStore。
     *
     * <p>真正触发 Embedding 的位置是：</p>
     *
     * <pre>
     * vectorStore.add(chunks)
     * </pre>
     */
    public ImportResponse importPdf(MultipartFile file) {

        pdfFileValidator.validate(file);

        String fileName = pdfFileValidator.getSafeFileName(file);

        /*
         * Extract：
         * PDF → Document。
         */
        List<Document> documents = readPdfDocuments(file, fileName);

        /*
         * Transform：
         * Document → Chunk。
         */
        List<Document> chunks = splitDocuments(documents);

        /*
         * Load：
         * Chunk → Embedding → VectorStore。
         */
        vectorStore.add(chunks);

        return new ImportResponse(
            fileName,
            documents.size(),
            chunks.size(),
            chunks.size(),
            "PDF 导入完成"
        );
    }

    /**
     * 功能4：搜索刚导入的 PDF Chunk。
     *
     * <p>这里只做 Retrieval，不调用 DeepSeek。</p>
     */
    public SearchResult search(SearchRequest request) {

        if (request == null || !StringUtils.hasText(request.query())) {
            throw new IllegalArgumentException("query 不能为空");
        }

        int topK = request.topK() == null
            ? DEFAULT_TOP_K
            : request.topK();

        if (topK < 1 || topK > 20) {
            throw new IllegalArgumentException("topK 必须在 1～20 之间");
        }

        double threshold = request.similarityThreshold() == null
            ? DEFAULT_SIMILARITY_THRESHOLD
            : request.similarityThreshold();

        if (threshold < 0.0D || threshold > 1.0D) {
            throw new IllegalArgumentException(
                "similarityThreshold 必须在 0.0～1.0 之间"
            );
        }

        Builder builder = org.springframework.ai.vectorstore.SearchRequest.builder()
            .query(request.query().trim())
            .topK(topK)
            .similarityThreshold(threshold);

        /*
         * 如果指定 fileName，
         * 就只搜索该 PDF 的 Chunk。
         */
        if (StringUtils.hasText(request.fileName())) {

            FilterExpressionBuilder filter =
                new FilterExpressionBuilder();

            builder.filterExpression(
                filter.eq(
                    META_FILE_NAME,
                    request.fileName().trim()
                ).build()
            );
        }

        List<Document> documents =
            vectorStore.similaritySearch(builder.build());

        return new SearchResult(
            request.query(),
            documents.size(),
            toDocumentItems(documents)
        );
    }

    /**
     * 使用 PagePdfDocumentReader 把 PDF 转成 Document。
     *
     * <p>属于 ETL 中的 Extract。</p>
     */
    private List<Document> readPdfDocuments(
        MultipartFile file,
        String fileName) {

        Resource resource = file.getResource();

        PdfDocumentReaderConfig config =
            PdfDocumentReaderConfig.builder()
                .withPagesPerDocument(1)
                .withPageTopMargin(0)
                .withPageBottomMargin(0)
                .build();

        PagePdfDocumentReader reader =
            new PagePdfDocumentReader(resource, config);

        List<Document> documents = reader.read();

        for (Document document : documents) {

            /*
             * PagePdfDocumentReader 本身会保存：
             *
             * file_name
             * page_number
             * end_page_number
             *
             * 这里增加业务侧 metadata。
             */
            document.getMetadata().put(
                PagePdfDocumentReader.METADATA_FILE_NAME,
                fileName
            );

            document.getMetadata().put(
                META_FILE_NAME,
                fileName
            );

            document.getMetadata().put(
                META_SOURCE,
                "multipart-upload:" + fileName
            );

            document.getMetadata().put(
                META_CONTENT_TYPE,
                pdfFileValidator.getContentType(file)
            );
        }

        return documents;
    }

    /**
     * 对 Reader 产生的 Document 进行切片。
     *
     * <p>属于 ETL 中的 Transform。</p>
     */
    private List<Document> splitDocuments(
        List<Document> documents) {

        TokenTextSplitter splitter =
            TokenTextSplitter.builder()
                .withChunkSize(500)
                .withMinChunkSizeChars(100)
                .withMinChunkLengthToEmbed(5)
                .withMaxNumChunks(10000)
                .withKeepSeparator(true)
                .build();

        List<Document> allChunks = new ArrayList<>();

        for (Document document : documents) {

            List<Document> chunks =
                splitter.split(document);

            int totalChunks = chunks.size();

            for (int i = 0; i < totalChunks; i++) {

                Document chunk = chunks.get(i);

                /*
                 * 确保原始 Document 的：
                 *
                 * file_name
                 * page_number
                 * end_page_number
                 * fileName
                 * source
                 *
                 * 等 metadata 跟随 Chunk 进入 VectorStore。
                 */
                chunk.getMetadata().putAll(
                    document.getMetadata()
                );

                chunk.getMetadata().put(
                    META_PARENT_DOCUMENT_ID,
                    document.getId()
                );

                chunk.getMetadata().put(
                    META_CHUNK_INDEX,
                    i
                );

                chunk.getMetadata().put(
                    META_TOTAL_CHUNKS,
                    totalChunks
                );

                allChunks.add(chunk);
            }
        }

        return allChunks;
    }

    /**
     * 将 Spring AI Document 转成接口 DTO。
     */
    private List<DocumentItem> toDocumentItems(
        List<Document> documents) {

        return documents.stream()
            .map(document -> new DocumentItem(
                document.getId(),
                document.getText(),
                new LinkedHashMap<>(
                    document.getMetadata()
                ),
                document.getScore()
            ))
            .toList();
    }
}
