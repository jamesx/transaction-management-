package com.hsbc.tm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.exception.TransactionException;
import com.hsbc.tm.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 交易控制器的单元测试类
 * 测试控制器层处理HTTP请求和响应的功能
 * 
 * @author 薛鹏
 * @version 1.0
 */
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private TransactionRequest validRequest;
    private TransactionResponse sampleResponse;
    private String transactionId;

    /**
     * 测试前的准备工作
     * 初始化测试所需的请求和响应对象
     * 
     * @author 薛鹏
     */
    @BeforeEach
    void setUp() {
        // 创建请求
        validRequest = new TransactionRequest();
        validRequest.setAmount(new BigDecimal("100.00"));
        validRequest.setDescription("Test Transaction");
        validRequest.setType("DEPOSIT");
        validRequest.setCurrency("USD");

        // 创建响应
        transactionId = "test-id-123";
        sampleResponse = new TransactionResponse();
        sampleResponse.setId(transactionId);
        sampleResponse.setAmount(new BigDecimal("100.00"));
        sampleResponse.setDescription("Test Transaction");
        sampleResponse.setType("DEPOSIT");
        sampleResponse.setCurrency("USD");
        sampleResponse.setTimestamp(LocalDateTime.now());
        sampleResponse.setStatus("COMPLETED");
    }

    /**
     * 测试创建交易接口
     * 验证控制器能够正确处理有效的创建交易请求，并返回201状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should create transaction and return 201 status")
    void createTransaction_ValidRequest_Returns201() throws Exception {
        when(transactionService.createTransaction(any(TransactionRequest.class))).thenReturn(sampleResponse);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(transactionId)))
                .andExpect(jsonPath("$.amount", is(100.00)))
                .andExpect(jsonPath("$.type", is("DEPOSIT")));

        verify(transactionService, times(1)).createTransaction(any(TransactionRequest.class));
    }

    /**
     * 测试获取单个交易接口
     * 验证控制器能够正确获取已存在交易的详情，并返回200状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should get transaction by ID and return 200 status")
    void getTransaction_ExistingId_Returns200() throws Exception {
        when(transactionService.getTransaction(transactionId)).thenReturn(sampleResponse);

        mockMvc.perform(get("/api/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(transactionId)))
                .andExpect(jsonPath("$.amount", is(100.00)))
                .andExpect(jsonPath("$.type", is("DEPOSIT")));

        verify(transactionService, times(1)).getTransaction(transactionId);
    }

    /**
     * 测试获取所有交易接口
     * 验证控制器能够正确获取交易列表，并返回200状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should get all transactions and return 200 status")
    void getAllTransactions_Returns200() throws Exception {
        TransactionResponse anotherResponse = new TransactionResponse();
        anotherResponse.setId("test-id-456");
        anotherResponse.setAmount(new BigDecimal("200.00"));
        anotherResponse.setDescription("Another Transaction");
        anotherResponse.setType("WITHDRAWAL");
        anotherResponse.setCurrency("EUR");
        anotherResponse.setTimestamp(LocalDateTime.now());
        anotherResponse.setStatus("COMPLETED");

        List<TransactionResponse> responses = Arrays.asList(sampleResponse, anotherResponse);
        when(transactionService.getAllTransactions(0, 10)).thenReturn(responses);

        mockMvc.perform(get("/api/transactions")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(transactionId)))
                .andExpect(jsonPath("$[1].id", is("test-id-456")));

        verify(transactionService, times(1)).getAllTransactions(0, 10);
    }

    /**
     * 测试更新交易接口
     * 验证控制器能够正确处理有效的更新交易请求，并返回200状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should update transaction and return 200 status")
    void updateTransaction_ValidRequest_Returns200() throws Exception {
        TransactionRequest updateRequest = new TransactionRequest();
        updateRequest.setAmount(new BigDecimal("150.00"));
        updateRequest.setDescription("Updated Transaction");
        updateRequest.setType("TRANSFER");
        updateRequest.setCurrency("EUR");

        TransactionResponse updatedResponse = new TransactionResponse();
        updatedResponse.setId(transactionId);
        updatedResponse.setAmount(new BigDecimal("150.00"));
        updatedResponse.setDescription("Updated Transaction");
        updatedResponse.setType("TRANSFER");
        updatedResponse.setCurrency("EUR");
        updatedResponse.setTimestamp(LocalDateTime.now());
        updatedResponse.setStatus("COMPLETED");

        when(transactionService.updateTransaction(eq(transactionId), any(TransactionRequest.class)))
                .thenReturn(updatedResponse);

        mockMvc.perform(put("/api/transactions/{id}", transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(transactionId)))
                .andExpect(jsonPath("$.amount", is(150.00)))
                .andExpect(jsonPath("$.type", is("TRANSFER")));

        verify(transactionService, times(1)).updateTransaction(eq(transactionId), any(TransactionRequest.class));
    }

    /**
     * 测试删除交易接口
     * 验证控制器能够正确处理删除交易请求，并返回200状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should delete transaction and return 200 status")
    void deleteTransaction_ExistingId_Returns200() throws Exception {
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/api/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Transaction deleted successfully")));

        verify(transactionService, times(1)).deleteTransaction(transactionId);
    }

    /**
     * 测试获取不存在的交易
     * 验证控制器能够正确处理交易不存在的情况，并返回404状态码
     * 
     * @author 薛鹏
     * @throws Exception 当请求处理过程中发生异常时抛出
     */
    @Test
    @DisplayName("Should return 404 when transaction not found")
    void getTransaction_NonExistentId_Returns404() throws Exception {
        String nonExistentId = "non-existent-id";
        when(transactionService.getTransaction(nonExistentId))
                .thenThrow(new TransactionException.TransactionNotFoundException(nonExistentId));

        mockMvc.perform(get("/api/transactions/{id}", nonExistentId))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).getTransaction(nonExistentId);
    }
} 