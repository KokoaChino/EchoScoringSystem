package com.echo.external.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
public class KujiequApiHttpUtil {

    private static final int MAX_RETRIES = 3;
    private static final long BASE_RETRY_DELAY_MS = 1000;

    @Resource
    private OkHttpClient okHttpClient;

    @Resource
    private ObjectMapper objectMapper;

    public JsonNode postAndGetBusinessData(String url, FormBody.Builder formBuilder) {
        Request request = new Request.Builder()
                .url(url)
                .post(formBuilder.build())
                .addHeader("wiki_type", "9")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .build();
        Exception lastException = null;
        for (int retry = 1; retry <= MAX_RETRIES; retry++) {
            try (Response response = okHttpClient.newCall(request).execute()) {
                String body = response.body() == null ? "" : response.body().string();
                if (!response.isSuccessful()) {
                    log.error("HTTP 请求失败 {}：{} | URL：{} | Response：{}",
                            response.code(), response.message(), url, body);
                    lastException = new IOException("HTTP " + response.code());
                    continue;
                }
                JsonNode root = objectMapper.readTree(body);
                int code = root.path("code").asInt();
                String msg = root.path("msg").asText();
                if (code != 200) {
                    log.error("库街区 API 业务失败：code={}, msg={} | URL：{} | Response：{}",
                            code, msg, url, body);
                    throw new RuntimeException("库街区 API 业务失败");
                }
                JsonNode data = root.path("data");
                if (data.isMissingNode() || data.isNull()) {
                    throw new RuntimeException("库街区 API 返回 data 为空：" + body);
                }
                return data;
            } catch (IOException e) {
                lastException = e;
            }
            if (retry < MAX_RETRIES) {
                long delay = BASE_RETRY_DELAY_MS * (1L << (retry - 1));
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断：", ie);
                }
            }
        }
        String errMsg = "POST 请求失败，已重试 " + MAX_RETRIES + " 次：" + url;
        log.error(errMsg, lastException);
        throw new RuntimeException(errMsg, lastException);
    }

    public JsonNode getBusinessData(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Exception lastException = null;
        for (int retry = 1; retry <= MAX_RETRIES; retry++) {
            try (Response response = okHttpClient.newCall(request).execute()) {
                String body = response.body() == null ? "" : response.body().string();
                if (!response.isSuccessful()) {
                    log.error("HTTP GET 请求失败 {}：{} | URL：{} | Response：{}",
                            response.code(), response.message(), url, body);
                    lastException = new IOException("HTTP " + response.code());
                    continue;
                }
                JsonNode root = objectMapper.readTree(body);
                int code = root.path("code").asInt();
                String msg = root.path("message").asText();
                if (code != 200) {
                    log.error("库街区 API 业务失败：code={}, message={} | URL：{} | Response：{}",
                            code, msg, url, body);
                    throw new RuntimeException("业务 API 失败：" + msg);
                }
                JsonNode data = root.path("data");
                if (data.isMissingNode() || data.isNull()) {
                    throw new RuntimeException("返回 data 为空：" + body);
                }
                return data;
            } catch (IOException e) {
                lastException = e;
            }
            if (retry < MAX_RETRIES) {
                long delay = BASE_RETRY_DELAY_MS * (1L << (retry - 1));
                try {
                    TimeUnit.MILLISECONDS.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("重试被中断", ie);
                }
            }
        }
        String errMsg = "GET 请求失败，已重试 " + MAX_RETRIES + " 次：" + url;
        log.error(errMsg, lastException);
        throw new RuntimeException(errMsg, lastException);
    }
}