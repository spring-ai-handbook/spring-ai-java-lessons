package com.example.springai.lesson19;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.audio.transcription.TranscriptionModel;
import org.springframework.ai.audio.tts.TextToSpeechModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * Lesson19 Spring AI Audio 快速示例。
 *
 * <p>本课程只演示两个最核心能力：</p>
 *
 * <pre>
 * TTS：
 * 文字 → TextToSpeechModel → 语音
 *
 * STT：
 * 语音 → TranscriptionModel → 文字
 * </pre>
 */
@RestController
@RequestMapping("/lesson19")
@RequiredArgsConstructor
@ConditionalOnBean({TextToSpeechModel.class, TranscriptionModel.class})
public class Lesson19AudioController {

    /**
     * Spring AI 统一的文字转语音模型接口。
     *
     * <p>当前 application.yml 配置为 OpenAI，
     * 所以实际底层使用 OpenAI TTS 模型。</p>
     */
    private final TextToSpeechModel textToSpeechModel;

    /**
     * Spring AI 统一的语音转文字模型接口。
     *
     * <p>当前 application.yml 配置为 OpenAI，
     * 所以实际底层使用 OpenAI Transcription 模型。</p>
     */
    private final TranscriptionModel transcriptionModel;

    /**
     * TTS：把文字转换成 MP3 语音。
     *
     * <p>核心链路：</p>
     *
     * <pre>
     * String
     * ↓
     * TextToSpeechModel
     * ↓
     * byte[]
     * ↓
     * MP3
     * </pre>
     *
     * @param text 要转换成语音的文字
     * @return MP3 音频字节
     */
    @GetMapping("/speech")
    public ResponseEntity<byte[]> speech(
        @RequestParam String text) {

        if (!StringUtils.hasText(text)) {
            throw new IllegalArgumentException(
                "text 不能为空"
            );
        }

        /*
         * Spring AI 2.0.1 TextToSpeechModel
         * 提供了最简单的 call(String text) 方法。
         *
         * 返回值直接就是音频 byte[]。
         */
        byte[] audio =
            textToSpeechModel.call(text);

        /*
         * 告诉浏览器：
         *
         * 当前返回内容是 MP3 音频。
         */
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                "inline; filename=\"lesson19.mp3\""
            )
            .contentType(
                MediaType.parseMediaType(
                    "audio/mpeg"
                )
            )
            .body(audio);
    }

    /**
     * STT：把上传的语音文件转换成文字。
     *
     * <p>核心链路：</p>
     *
     * <pre>
     * MultipartFile
     * ↓
     * Resource
     * ↓
     * TranscriptionModel
     * ↓
     * String
     * </pre>
     *
     * @param file 用户上传的音频文件
     * @return 语音识别后的文字
     */
    @PostMapping(
        value = "/transcription",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public String transcription(
        @RequestParam("file") MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                "音频文件不能为空"
            );
        }

        /*
         * Spring AI 2.0.1 的 TranscriptionModel
         * 提供 transcribe(Resource) 便捷方法。
         *
         * MultipartFile.getResource()
         * 可以直接转换为 Spring Resource。
         */
        return transcriptionModel.transcribe(
            file.getResource()
        );
    }
}
