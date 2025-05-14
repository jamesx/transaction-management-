package com.hsbc.tm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 * 统一处理系统中抛出的各种异常，将其转换为标准的HTTP响应
 * 
 * @author 薛鹏
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理交易相关异常
     * 根据异常类型设置相应的HTTP状态码
     * 
     * @author 薛鹏
     * @param ex 捕获的交易异常
     * @return 返回包含错误详情的HTTP响应
     */
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<Map<String, Object>> handleTransactionException(TransactionException ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("code", ex.getCode());
        errorResponse.put("message", ex.getMessage());

        HttpStatus status;
        if (ex instanceof TransactionException.TransactionNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof TransactionException.DuplicateTransactionException) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return new ResponseEntity<>(errorResponse, status);
    }

    /**
     * 处理所有其他类型的异常
     * 将其转换为500 Internal Server Error响应
     * 
     * @author 薛鹏
     * @param ex 捕获的一般异常
     * @return 返回包含错误详情的HTTP响应
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("code", "INTERNAL_ERROR");
        errorResponse.put("message", ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
} 