package com.common.filter;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.common.client.feign.AuthClient;
import com.common.constant.Constant;
import com.common.context.UserContext;
import com.common.entity.AuthenticationDTO;
import com.common.entity.User;
import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;


@Component
public class UserContextFilter implements Filter {

    @Resource
    private AuthClient authClient;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        if (request instanceof HttpServletRequest httpRequest) {
            try {
                String xUsernameB64 = httpRequest.getHeader("X-Username-B64");
                String xSignature = httpRequest.getHeader("X-Signature");
                if (xUsernameB64 == null || xSignature == null) {
                    chain.doFilter(request, response);
                    return;
                }
                String xUsername = new String(Base64.getDecoder().decode(xUsernameB64), StandardCharsets.UTF_8);
                String expected = Hex.encodeHexString(new HmacUtils(HmacAlgorithms.HMAC_SHA_256, Constant.SECRET_BYTES)
                        .hmac(xUsername.getBytes(StandardCharsets.UTF_8)));
                if (!expected.equals(xSignature)) {
                    throw new SecurityException("签名无效");
                }
                String cacheKey = Constant.USER_CONTEXT_CACHE_KEY_PREFIX + xUsername;
                String cacheValue = redisTemplate.opsForValue().get(cacheKey);
                User user;
                if (cacheValue != null) {
                    user = JSON.parseObject(cacheValue, User.class);
                } else {
                    user = authClient.getUser(AuthenticationDTO.getBaseDTO(xUsername)).getData();
                    if (user != null && user.getId() != 0) {
                        redisTemplate.opsForValue().set(
                                cacheKey,
                                JSONObject.toJSONString(user),
                                Duration.ofMinutes(30)
                        );
                    } else {
                        redisTemplate.opsForValue().set(
                                cacheKey,
                                "{}",
                                Duration.ofMinutes(2)
                        );
                        throw new SecurityException("用户不存在");
                    }
                }
                UserContext.set(user);
                chain.doFilter(request, response);
            } finally {
                UserContext.clear();
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}