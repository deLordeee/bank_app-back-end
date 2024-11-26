package com.example.Banking.App.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, HttpServletRequest request, Model model) {
        String referrer = request.getHeader("Referer");
        // Clean the URL by removing existing error parameters
        String baseUrl = referrer != null ? referrer.split("\\?")[0] : "/dashboard";

        return "redirect:" + baseUrl + "?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
    }
}