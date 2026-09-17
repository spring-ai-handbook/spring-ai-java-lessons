package com.example.springai.lesson12.controller;

import com.example.springai.lesson12.dto.Lesson12DocumentResult;
import com.example.springai.lesson12.dto.Lesson12SplitRequest;
import com.example.springai.lesson12.service.Lesson12DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Lesson12 Document / TextSplitter REST 接口。
 *
 * <p>
 * 提供文本切片预览以及切片后写入 VectorStore 两个功能，
 * 用于观察知识在进入向量数据库之前的预处理过程。
 * </p>
 */
@RestController
@RequestMapping("/lesson12")
@RequiredArgsConstructor
public class Lesson12DocumentController {

    /**
     * Lesson12 文档处理服务。
     */
    private final Lesson12DocumentService documentService;

    /**
     * 对一段原始文本执行切片，并直接返回所有 Chunk。
     *
     * <p>
     * 此接口只执行：
     * Document → TextSplitter，
     * 不进行 Embedding，也不会调用 OpenAI。
     * </p>
     *
     * @param request 原始文本
     * @return 切片后的 Document 集合
     */
    @PostMapping("/split")
    public List<Lesson12DocumentResult> split(
        @RequestBody Lesson12SplitRequest request) {

        return documentService.split(request);
    }

    /**
     * 将原始文本切片后写入 VectorStore。
     *
     * <p>
     * 此接口会进一步触发：
     * VectorStore → EmbeddingModel → OpenAI Embedding。
     * </p>
     *
     * @param request 原始知识文本
     * @return 实际生成并写入的 Chunk 数量
     */
    @PostMapping("/split-and-store")
    public Map<String, Object> splitAndStore(
        @RequestBody Lesson12SplitRequest request) {

        int chunkCount =
            documentService.splitAndStore(request);

        return Map.of(
            "chunkCount", chunkCount,
            "message", "文档切片并写入 VectorStore 成功"
        );
    }
}
