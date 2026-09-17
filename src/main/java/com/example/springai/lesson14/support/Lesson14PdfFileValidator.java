package com.example.springai.lesson14.support;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Locale;

/**
 * Lesson14 PDF 上传文件统一校验组件。
 *
 * <p>该组件专门负责 PDF 上传文件的基础校验，
 * 避免 Controller 或 Service 中反复编写相同的：</p>
 *
 * <ul>
 *     <li>文件是否为空</li>
 *     <li>文件名是否合法</li>
 *     <li>扩展名是否为 PDF</li>
 *     <li>Content-Type 是否为 application/pdf</li>
 * </ul>
 *
 * <p>以后 Lesson14 内所有 PDF 上传入口只需要调用：</p>
 *
 * <pre>
 * pdfFileValidator.validate(file);
 * </pre>
 *
 * <p>正式企业项目还可以继续在这里统一增加：
 * 文件大小限制、Magic Number 文件头校验、
 * 加密 PDF 检测、病毒扫描等。</p>
 */
@Component
public class Lesson14PdfFileValidator {

    /**
     * 校验上传文件是否可以作为 PDF 处理。
     *
     * @param file 用户上传的文件
     */
    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("PDF 文件不能为空");
        }

        String fileName = getSafeFileName(file);

        boolean pdfExtension = fileName
            .toLowerCase(Locale.ROOT)
            .endsWith(".pdf");

        boolean pdfContentType = MediaType.APPLICATION_PDF_VALUE
            .equalsIgnoreCase(file.getContentType());

        /*
         * Demo 阶段允许以下任意一种成立：
         *
         * 1. 文件扩展名是 .pdf
         * 2. Content-Type 是 application/pdf
         *
         * 正式项目应该进一步校验 PDF 文件头，
         * 不能只相信扩展名或客户端 Content-Type。
         */
        if (!pdfExtension && !pdfContentType) {
            throw new IllegalArgumentException("当前接口只允许上传 PDF 文件");
        }
    }

    /**
     * 获取已经清理过的安全文件名。
     *
     * <p>MultipartFile.getOriginalFilename() 是客户端提供的数据，
     * 不能直接拿来作为服务器文件路径。</p>
     *
     * @param file 上传文件
     * @return 清理后的文件名
     */
    public String getSafeFileName(MultipartFile file) {

        String originalFilename = file.getOriginalFilename();

        if (!StringUtils.hasText(originalFilename)) {
            throw new IllegalArgumentException("无法获取 PDF 文件名");
        }

        String cleaned = StringUtils.cleanPath(originalFilename)
            .replace('\\', '/');

        if (cleaned.contains("..")) {
            throw new IllegalArgumentException("非法文件名：" + originalFilename);
        }

        int index = cleaned.lastIndexOf('/');

        String fileName = index >= 0
            ? cleaned.substring(index + 1)
            : cleaned;

        if (!StringUtils.hasText(fileName)) {
            throw new IllegalArgumentException("非法文件名：" + originalFilename);
        }

        return fileName;
    }

    /**
     * 获取上传文件 MIME 类型。
     *
     * @param file 上传文件
     * @return Content-Type
     */
    public String getContentType(MultipartFile file) {

        return StringUtils.hasText(file.getContentType())
            ? file.getContentType()
            : MediaType.APPLICATION_PDF_VALUE;
    }
}
