package com.hsbc.tm.controller;

import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易管理的REST控制器
 * 提供交易相关的所有HTTP端点
 * 
 * @author 薛鹏
 * @version 1.0
 */
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * 构造函数，通过依赖注入获取TransactionService实例
     * 
     * @author 薛鹏
     * @param transactionService 交易服务接口的实现
     */
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * 创建新交易
     * HTTP POST /api/transactions
     * 
     * @author 薛鹏
     * @param request 包含交易详情的请求体
     * @return 返回201状态码和创建的交易详情
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 根据ID获取单个交易
     * HTTP GET /api/transactions/{id}
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 返回交易详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransaction(@PathVariable String id) {
        TransactionResponse response = transactionService.getTransaction(id);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取所有交易，支持分页
     * HTTP GET /api/transactions?page=0&size=10
     * 
     * @author 薛鹏
     * @param page 页码，从0开始，默认为0
     * @param size 每页数量，默认为10
     * @return 返回交易列表
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        List<TransactionResponse> transactions = transactionService.getAllTransactions(page, size);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 更新现有交易
     * HTTP PUT /api/transactions/{id}
     * 
     * @author 薛鹏
     * @param id 要更新的交易ID
     * @param request 包含更新信息的请求体
     * @return 返回更新后的交易详情
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable String id,
            @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * 删除交易
     * HTTP DELETE /api/transactions/{id}
     * 
     * @author 薛鹏
     * @param id 要删除的交易ID
     * @return 返回删除成功的消息
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Transaction deleted successfully");
        return ResponseEntity.ok(response);
    }

    /**
     * 获取交易总数
     * HTTP GET /api/transactions/count
     * 
     * @author 薛鹏
     * @return 返回交易总数
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countTransactions() {
        long count = transactionService.countTransactions();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }
} 