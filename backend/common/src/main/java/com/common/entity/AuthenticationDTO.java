package com.common.entity;

import com.common.constant.Constant;
import lombok.Data;
import org.springframework.util.DigestUtils;
import java.nio.charset.StandardCharsets;


@Data
public class AuthenticationDTO {

    private String username;
    private Object extra;

    private String sig;
    private Long ts = System.currentTimeMillis();
    private static final String SECRET = Constant.SECRET;

    public void setUsername(String username) {
        this.username = username;
        this.sig = generateSignature();
    }

    private String generateSignature() {
        String data = username + "|" + ts + "|" + SECRET;
        return DigestUtils.md5DigestAsHex(data.getBytes(StandardCharsets.UTF_8));
    }

    public void verify() {
        if (username == null || username.isBlank()) {
            throw new SecurityException("用户名为空");
        }
        if (ts <= 0 || sig == null) {
            throw new SecurityException("请求非法（缺少签名信息）");
        }
        if (System.currentTimeMillis() - ts > 5 * 60 * 1000L) {
            throw new SecurityException("请求已过期");
        }
        if (!sig.equals(generateSignature())) {
            throw new SecurityException("签名无效");
        }
    }

    public static AuthenticationDTO getBaseDTO(String username) {
        AuthenticationDTO dto = new AuthenticationDTO();
        dto.setUsername(username);
        return dto;
    }
}