package com.example.springai.lesson12.config;

import com.knuddels.jtokkit.api.EncodingType;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Lesson12 TextSplitter 配置类。
 *
 * <p>
 * 本课程重点学习 Spring AI 中的 Document 与 TextSplitter。
 * 其中 {@link TokenTextSplitter} 是最常用的拆分器，
 * 会按照 token 数把一个长文本 Document 切分成多个短文本 Document。
 * </p>
 *
 * <p>
 * 这里显式声明一个默认 Bean，
 * 是为了在不依赖 Spring AI 自动配置默认值的前提下，
 * 集中管理课程里使用的 chunk 大小、保留分隔符、标点符号等参数，
 * 这样业务代码只依赖 {@link TextSplitter} 接口即可。
 * </p>
 */
@Configuration
public class Lesson12TextSplitterConfig {

    /**
     * Lesson12 默认文本拆分器。
     *
     * <p>
     * 为了方便初学者观察拆分行为，这里使用相对较小的 chunk size，
     * 这样一段普通说明文通常就能被切成多个 chunk。
     * </p>
     *
     * <p>
     * 使用 {@code CL100K_BASE} 编码，主要是为了和 OpenAI 系列模型保持兼容；
     * 在做中文文本教学时，可以根据实际模型替换为更合适的编码。
     * </p>
     *
     * @return Lesson12 默认的 {@link TokenTextSplitter}
     */
    @Bean("lesson12DefaultTextSplitter")
    public TokenTextSplitter lesson12DefaultTextSplitter() {
        return TokenTextSplitter.builder()
            .withEncodingType(EncodingType.CL100K_BASE)
            .withChunkSize(200)
            .withMinChunkSizeChars(50)
            .withMinChunkLengthToEmbed(5)
            .withMaxNumChunks(1000)
            .withKeepSeparator(true)
            .withPunctuationMarks(List.of(
                '。', '！', '？', '；', '，',
                '\n', '.', '!', '?', ';', ','
            ))
            .build();
    }
}