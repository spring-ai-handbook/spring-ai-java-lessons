package com.example.springai.lesson14.dto;

import java.util.List;
import java.util.Map;

/**
 * Lesson14 使用的全部 DTO。
 *
 * <p>为了课程代码不过度拆文件，
 * 本课将几个简单 DTO 统一放在一个类中。</p>
 */
public final class Lesson14Dtos {

    private Lesson14Dtos() {
    }

    /**
     * Document 展示对象。
     *
     * @param id       Document ID
     * @param text     文本内容
     * @param metadata 元数据
     * @param score    相似度分数，普通读取时通常为空
     */
    public record DocumentItem(
        String id,
        String text,
        Map<String, Object> metadata,
        Double score
    ) {
    }

    /**
     * PDF 原始读取结果。
     */
    public record ReadResponse(
        String fileName,
        int documentCount,
        List<DocumentItem> documents
    ) {
    }

    /**
     * PDF 切片预览结果。
     */
    public record SplitResponse(
        String fileName,
        int originalDocumentCount,
        int chunkCount,
        List<DocumentItem> chunks
    ) {
    }

    /**
     * PDF 导入结果。
     */
    public record ImportResponse(
        String fileName,
        int originalDocumentCount,
        int chunkCount,
        int importedChunkCount,
        String message
    ) {
    }

    /**
     * PDF 知识检索请求。
     *
     * @param query               查询内容
     * @param topK                最大返回数量
     * @param similarityThreshold 最低相似度
     * @param fileName            可选，只检索指定 PDF
     */
    public record SearchRequest(
        String query,
        Integer topK,
        Double similarityThreshold,
        String fileName
    ) {
    }

    /**
     * PDF 知识检索结果。
     */
    public record SearchResult(
        String query,
        int resultCount,
        List<DocumentItem> documents
    ) {
    }
}
