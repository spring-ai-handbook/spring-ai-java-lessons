package com.example.springai.lesson14.controller;

import com.example.springai.lesson14.dto.Lesson14Dtos.ImportResponse;
import com.example.springai.lesson14.dto.Lesson14Dtos.ReadResponse;
import com.example.springai.lesson14.dto.Lesson14Dtos.SearchRequest;
import com.example.springai.lesson14.dto.Lesson14Dtos.SearchResult;
import com.example.springai.lesson14.dto.Lesson14Dtos.SplitResponse;
import com.example.springai.lesson14.service.Lesson14DocumentReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Lesson14 PDF DocumentReader 教学接口。
 *
 * <p>Controller 只负责接收 HTTP 请求，
 * 所有 PDF 校验、解析、切片和向量入库工作都交给 Service。</p>
 */
@RestController
@RequestMapping("/lesson14")
@RequiredArgsConstructor
public class Lesson14DocumentReaderController {

    private final Lesson14DocumentReaderService documentReaderService;

    /**
     * PDF → Document。
     */
    @PostMapping(
        value = "/pdf/read",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ReadResponse readPdf(
        @RequestParam("file") MultipartFile file) {

        return documentReaderService.readPdf(file);
    }

    /**
     * PDF → Document → Chunk。
     */
    @PostMapping(
        value = "/pdf/split",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public SplitResponse splitPdf(
        @RequestParam("file") MultipartFile file) {

        return documentReaderService.splitPdf(file);
    }

    /**
     * PDF → Document → Chunk → VectorStore。
     */
    @PostMapping(
        value = "/pdf/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ImportResponse importPdf(
        @RequestParam("file") MultipartFile file) {

        return documentReaderService.importPdf(file);
    }

    /**
     * 搜索已经导入 VectorStore 的 PDF 内容。
     */
    @PostMapping("/search")
    public SearchResult search(
        @RequestBody SearchRequest request) {

        return documentReaderService.search(request);
    }
}
