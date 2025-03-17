package gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class RtGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().toString();
        long start = System.currentTimeMillis();
        log.info("请求 {} 开始，时间：{}", uri, start);
        // ============================== 以上是前置逻辑 ==============================
        // ============================== 以下是后置逻辑 ==============================
        return chain.filter(exchange).doFinally((result) -> {
            long end = System.currentTimeMillis();
            log.info("请求 {} 结束，时间：{}，耗时：{}ms", uri, end, end - start);
        });
    }

    @Override
    public int getOrder() {
        return 0;
    }
}