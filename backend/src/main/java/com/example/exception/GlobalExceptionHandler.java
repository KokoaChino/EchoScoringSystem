package com.example.exception;

import org.springframework.security.web.authentication.rememberme.CookieTheftException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CookieTheftException.class)
    public String handleCookieTheftException() {
        System.out.println("ppp");
        return "redirect:/login?error=cookie-theft";
    }
}