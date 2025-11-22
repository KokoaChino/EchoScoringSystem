package gateway.filter;

import com.common.constant.Constant;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        // 白名单：放行认证和支付回调
        if ("/api/pay/notify".equals(path)) {
            return chain.filter(exchange);
        }
        if (path.startsWith("/api/auth/")) {
            if ("/api/auth/logout".equals(path) || "/api/auth/get-user-vip".equals(path) || "/api/auth/signout".equals(path)) {
                // 不放行，继续走认证逻辑
            } else {
                return chain.filter(exchange);
            }
        }
        // 从 Header 获取 token
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return handleUnauthorized(exchange, "缺少或无效的授权标头");
        }
        String token = authHeader.substring(7);
        try {
            // 验证 JWT
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(Constant.SECRET_BYTES))
                    .build()
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            String username = claims.getSubject();
            String encodedUsername = Base64.getEncoder().encodeToString(username.getBytes(StandardCharsets.UTF_8));
            String expected = Hex.encodeHexString(
                    new HmacUtils(HmacAlgorithms.HMAC_SHA_256, Constant.SECRET_BYTES)
                            .hmac(username.getBytes(StandardCharsets.UTF_8))
            );
            ServerHttpRequest newRequest = exchange.getRequest().mutate()
                    .header("X-Username-B64", encodedUsername)
                    .header("X-Signature", expected)
                    .build();
            return chain.filter(exchange.mutate().request(newRequest).build());
        } catch (ExpiredJwtException e) {
            return handleUnauthorized(exchange, "令牌已过期");
        } catch (JwtException e) {
            return handleUnauthorized(exchange, "无效令牌：" + e.getMessage());
        }
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"code\":401,\"message\":\"%s\"}", message);
        return exchange.getResponse().writeWith(
                Mono.just(exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8)))
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}