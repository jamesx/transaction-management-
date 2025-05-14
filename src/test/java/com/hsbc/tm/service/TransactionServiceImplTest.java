package com.hsbc.tm.service;

import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.exception.TransactionException;
import com.hsbc.tm.model.Transaction;
import com.hsbc.tm.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 交易服务实现类的单元测试类
 * 测试服务层的业务逻辑功能
 * 
 * @author 薛鹏
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest validRequest;
    private Transaction transaction;
    private String transactionId;

    /**
     * 测试前的准备工作
     * 初始化测试所需的请求和交易对象
     * 
     * @author 薛鹏
     */
    @BeforeEach
    void setUp() {
        validRequest = new TransactionRequest();
        validRequest.setAmount(new BigDecimal("100.00"));
        validRequest.setDescription("Test Transaction");
        validRequest.setType("DEPOSIT");
        validRequest.setCurrency("USD");

        transaction = new Transaction(
                new BigDecimal("100.00"),
                "Test Transaction",
                "DEPOSIT",
                "USD"
        );
        transactionId = transaction.getId();
    }

    /**
     * 测试创建有效交易
     * 验证服务能够正确处理有效的创建交易请求
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should create transaction when request is valid")
    void createTransaction_ValidRequest_ReturnsTransactionResponse() {
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionResponse response = transactionService.createTransaction(validRequest);

        assertNotNull(response);
        assertEquals(transaction.getId(), response.getId());
        assertEquals(validRequest.getAmount(), response.getAmount());
        assertEquals(validRequest.getDescription(), response.getDescription());
        assertEquals(validRequest.getType(), response.getType());
        assertEquals(validRequest.getCurrency(), response.getCurrency());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    /**
     * 测试创建无效金额交易
     * 验证服务能够正确处理金额为负数的无效交易请求
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should throw exception when creating transaction with negative amount")
    void createTransaction_NegativeAmount_ThrowsException() {
        TransactionRequest invalidRequest = new TransactionRequest();
        invalidRequest.setAmount(new BigDecimal("-100.00"));
        invalidRequest.setType("DEPOSIT");
        invalidRequest.setCurrency("USD");

        assertThrows(TransactionException.InvalidTransactionException.class,
                () -> transactionService.createTransaction(invalidRequest));

        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    /**
     * 测试获取已存在的交易
     * 验证服务能够正确获取已存在交易的详情
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should return transaction when ID exists")
    void getTransaction_ExistingId_ReturnsTransaction() {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        TransactionResponse response = transactionService.getTransaction(transactionId);

        assertNotNull(response);
        assertEquals(transaction.getId(), response.getId());
        assertEquals(transaction.getAmount(), response.getAmount());

        verify(transactionRepository, times(1)).findById(transactionId);
    }

    /**
     * 测试获取不存在的交易
     * 验证服务能够正确处理交易不存在的情况
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should throw exception when transaction ID does not exist")
    void getTransaction_NonexistentId_ThrowsException() {
        String nonExistentId = "non-existent-id";
        when(transactionRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        assertThrows(TransactionException.TransactionNotFoundException.class,
                () -> transactionService.getTransaction(nonExistentId));

        verify(transactionRepository, times(1)).findById(nonExistentId);
    }

    /**
     * 测试获取所有交易
     * 验证服务能够正确获取所有交易的列表
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should return all transactions")
    void getAllTransactions_ReturnsAllTransactions() {
        Transaction transaction2 = new Transaction(
                new BigDecimal("200.00"),
                "Another Transaction",
                "WITHDRAWAL",
                "EUR"
        );
        when(transactionRepository.findAll()).thenReturn(Arrays.asList(transaction, transaction2));

        List<TransactionResponse> responses = transactionService.getAllTransactions();

        assertNotNull(responses);
        assertEquals(2, responses.size());
        
        assertEquals(transaction.getId(), responses.get(0).getId());
        assertEquals(transaction.getAmount(), responses.get(0).getAmount());
        
        assertEquals(transaction2.getId(), responses.get(1).getId());
        assertEquals(transaction2.getAmount(), responses.get(1).getAmount());

        verify(transactionRepository, times(1)).findAll();
    }

    /**
     * 测试更新已存在的交易
     * 验证服务能够正确处理更新交易的请求
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should update transaction when ID exists")
    void updateTransaction_ExistingId_ReturnsUpdatedTransaction() {
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        doNothing().when(transactionRepository).update(any(Transaction.class));

        TransactionRequest updateRequest = new TransactionRequest();
        updateRequest.setAmount(new BigDecimal("150.00"));
        updateRequest.setDescription("Updated Transaction");
        updateRequest.setType("TRANSFER");
        updateRequest.setCurrency("EUR");

        TransactionResponse response = transactionService.updateTransaction(transactionId, updateRequest);

        assertNotNull(response);
        assertEquals(transactionId, response.getId());
        assertEquals(updateRequest.getAmount(), response.getAmount());
        assertEquals(updateRequest.getDescription(), response.getDescription());
        assertEquals(updateRequest.getType(), response.getType());
        assertEquals(updateRequest.getCurrency(), response.getCurrency());

        verify(transactionRepository, times(1)).findById(transactionId);
        verify(transactionRepository, times(1)).update(any(Transaction.class));
    }

    /**
     * 测试删除已存在的交易
     * 验证服务能够正确处理删除交易的请求
     * 
     * @author 薛鹏
     */
    @Test
    @DisplayName("Should delete transaction when ID exists")
    void deleteTransaction_ExistingId_DeletesTransaction() {
        doNothing().when(transactionRepository).deleteById(transactionId);

        transactionService.deleteTransaction(transactionId);

        verify(transactionRepository, times(1)).deleteById(transactionId);
    }
} 