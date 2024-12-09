package com.example.controller;

import com.example.entity.RestBean;
import com.example.mapper.UserMapper;
import com.example.service.AuthorizeService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated // 启用请求参数校验
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController { // 处理与身份认证和授权相关的控制器

    @Resource
    UserMapper mapper;

    @Resource
    AuthorizeService service; // 授权服务，用于处理注册、验证等业务逻辑

    private final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$"; // 有效邮箱地址的正则表达式
    private final String USERNAME_REGEX = "^[a-zA-Z0-9一-龥]+$"; // 有效用户名的正则表达式

    @PostMapping("/valid-register-email")
    public RestBean<String> validateRegisterEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                                  HttpSession session) { // 发送验证邮件
        String s = service.sendValidateEmail(email, session.getId(), false);
        if (s == null) return RestBean.success("邮件已发送，请注意查收");
        else return RestBean.failure(400, s);
    }

    @PostMapping("/valid-reset-email")
    public RestBean<String> validateResetEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                               HttpSession session) { // 发送重置邮件
        String s = service.sendValidateEmail(email, session.getId(), true);
        if (s == null) return RestBean.success("邮件已发送，请注意查收");
        else return RestBean.failure(400, s);
    }

    @PostMapping("/register")
    public RestBean<String> registerUser(@Pattern(regexp = USERNAME_REGEX) @Length(min = 2, max = 8) @RequestParam("username") String username,
                                         @Length(min = 6, max = 16) @RequestParam("password") String password,
                                         @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                         @Length(min = 6, max = 6) @RequestParam("code") String code,
                                         HttpSession session) { // 进行用户注册
        String s = service.validateAndRegister(username, password, email, code, session.getId());
        if (s == null) return RestBean.success("注册成功");
        else return RestBean.failure(400, s);
    }

    @PostMapping("/start-reset")
    public RestBean<String> startReset(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session) { // 进行邮箱及验证码的校验
        String s = service.validateOnly(email, code, session.getId());
        if (s == null) {
            session.setAttribute("reset-password", email);
            return RestBean.success();
        } else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/validate-email")
    public RestBean<String> changeEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email,
                                       @Length(min = 6, max = 6) @RequestParam("code") String code,
                                       HttpSession session) { // 进行邮箱及验证码的校验
        String s = service.validateOnlyFalse(email, code, session.getId());
        if (s == null) {
            session.setAttribute("reset-password", email);
            return RestBean.success();
        } else {
            return RestBean.failure(400, s);
        }
    }

    @PostMapping("/do-reset")
    public RestBean<String> resetPassword(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                          HttpSession session) { // 进行重置密码
        String email = (String) session.getAttribute("reset-password");
        if (email == null) {
            return RestBean.failure(401, "请先完成邮箱验证");
        } else if (service.resetPassword(password, email)) {
            session.removeAttribute("reset-password");
            return RestBean.success("密码重置成功");
        } else { // 密码重置失败
            return RestBean.failure(500, "密码重置失败，请联系作者：'星开祈灵'");
        }
    }

    @PostMapping("/change-username")
    public RestBean<String> changeUsername(@Length(min = 2, max = 8) @RequestParam("username") String username,
                                           @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email) { // 进行重置用户名
        if (service.changeUsername(username, email)) {
            return RestBean.success("用户名重置成功");
        } else {
            return RestBean.failure(500, "用户名重置失败，请联系作者：'星开祈灵'");
        }
    }

    @PostMapping("/change-password")
    public RestBean<String> changePassword(@Length(min = 6, max = 16) @RequestParam("password") String password,
                                           @Pattern(regexp = EMAIL_REGEX) @RequestParam("email") String email) { // 进行重置密码
        if (service.resetPassword(password, email)) {
            return RestBean.success("密码重置成功");
        } else {
            return RestBean.failure(500, "密码重置失败，请联系作者：'星开祈灵'");
        }
    }

    @PostMapping("/change-email")
    public RestBean<String> changeEmail(@Pattern(regexp = EMAIL_REGEX) @RequestParam("oldEmail") String oldEmail,
                                        @Pattern(regexp = EMAIL_REGEX) @RequestParam("newEmail") String newEmail) { // 进行重置邮箱
        if (service.changeEmail(oldEmail, newEmail)) {
            return RestBean.success("邮箱重置成功");
        } else {
            return RestBean.failure(500, "邮箱重置失败，请联系作者：'星开祈灵'");
        }
    }

    @PostMapping("/signout")
    public void signout(@RequestBody String username) { // 注销用户
        username = username.substring(1, username.length() - 1);
        for (String tableName : mapper.findAllTables()) {
            mapper.deleteAccountByUsername(tableName, username);
        }
    }
}