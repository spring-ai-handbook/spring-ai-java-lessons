package com.example.aidemo1.lesson09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientMessageAggregator;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * Lesson09：自定义 AI 日志 Advisor。
 *
 * <p>同时实现 CallAdvisor 和 StreamAdvisor，用于统一记录：</p>
 *
 * <ul>
 *     <li>用户 Prompt</li>
 *     <li>AI 最终响应</li>
 *     <li>一次完整调用耗时</li>
 * </ul>
 */
@Component
public class Lesson09LoggingAdvisor implements CallAdvisor, StreamAdvisor {

    /**
     * 日志对象。
     */
    private static final Logger log = LoggerFactory.getLogger(Lesson09LoggingAdvisor.class);

    /**
     * 拦截普通同步 .call() 调用。
     *
     * @param request 当前 ChatClient 请求
     * @param chain 后续 CallAdvisor 调用链
     * @return 最终 ChatClientResponse
     */
    @Override
    public ChatClientResponse adviseCall(
            ChatClientRequest request,
            CallAdvisorChain chain) {
        long startTime = System.nanoTime();
        log.info("[Lesson09][CALL][Request] {}", getUserPrompt(request));

        try {
            ChatClientResponse response = chain.nextCall(request);
            long cost = elapsedMillis(startTime);

            log.info("[Lesson09][CALL][Response] {}", getResponseText(response));
            log.info("[Lesson09][CALL][Cost] {} ms", cost);
            return response;
        }
        catch (RuntimeException exception) {
            log.error(
                    "[Lesson09][CALL][Error] cost={} ms, message={}",
                    elapsedMillis(startTime),
                    exception.getMessage(),
                    exception
            );
            throw exception;
        }
    }

    /**
     * 拦截流式 .stream() 调用。
     *
     * <p>Streaming 会产生多个 ChatClientResponse chunk，
     * 因此使用 ChatClientMessageAggregator 在不阻塞原始 Flux 的前提下
     * 旁路聚合出完整 AI 响应并记录日志。</p>
     *
     * @param request 当前 ChatClient 请求
     * @param chain 后续 StreamAdvisor 调用链
     * @return 原始流式响应
     */
    /**
     * 拦截流式请求并在流结束后记录聚合响应与耗时。
     *
     * @param request ChatClient 请求
     * @param chain 流式 Advisor 调用链
     * @return 原始流式响应
     */
    @Override
    public Flux<ChatClientResponse> adviseStream(
            ChatClientRequest request,
            StreamAdvisorChain chain) {
        long startTime = System.nanoTime();
        log.info("[Lesson09][STREAM][Request] {}", getUserPrompt(request));

        Flux<ChatClientResponse> responses = chain.nextStream(request);

        return new ChatClientMessageAggregator()
                .aggregateChatClientResponse(
                        responses,
                        aggregatedResponse -> {
                            log.info(
                                    "[Lesson09][STREAM][Response] {}",
                                    getResponseText(aggregatedResponse)
                            );
                            log.info(
                                    "[Lesson09][STREAM][Cost] {} ms",
                                    elapsedMillis(startTime)
                            );
                        }
                )
                .doOnError(exception -> log.error(
                        "[Lesson09][STREAM][Error] cost={} ms, message={}",
                        elapsedMillis(startTime),
                        exception.getMessage(),
                        exception
                ));
    }

    /**
     * 获取当前请求的用户文本。
     *
     * @param request ChatClient 请求
     * @return 用户 Prompt 文本
     */
    private String getUserPrompt(ChatClientRequest request) {
        if (request.prompt().getUserMessage() == null) {
            return null;
        }
        return request.prompt().getUserMessage().getText();
    }

    /**
     * 从 ChatClientResponse 中提取最终 Assistant 文本。
     *
     * @param response ChatClient 响应
     * @return AI 文本；没有文本时返回 null
     */
    private String getResponseText(ChatClientResponse response) {
        if (response == null
                || response.chatResponse() == null
                || response.chatResponse().getResult() == null
                || response.chatResponse().getResult().getOutput() == null) {
            return null;
        }

        return response
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();
    }

    /**
     * 计算纳秒起点到当前时间的毫秒耗时。
     *
     * @param startTime 开始时的 System.nanoTime()
     * @return 毫秒耗时
     */
    private long elapsedMillis(long startTime) {
        return (System.nanoTime() - startTime) / 1_000_000;
    }

    /**
     * Advisor 名称。
     *
     * @return Advisor 名称
     */
    @Override
    public String getName() {
        return "Lesson09LoggingAdvisor";
    }

    /**
     * 日志 Advisor 放在较外层，以便统计完整调用链总耗时。
     *
     * @return Advisor 执行顺序
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 100;
    }
}
