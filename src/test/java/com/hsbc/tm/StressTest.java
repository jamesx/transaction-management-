package com.hsbc.tm;

import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 交易管理系统的压力测试类
 * 测试系统在高并发情况下的性能和稳定性
 * 
 * @author 薛鹏
 * @version 1.0
 */
@SpringBootTest
public class StressTest {

    @Autowired
    private TransactionService transactionService;
    
    private final String[] TRANSACTION_TYPES = {"DEPOSIT", "WITHDRAWAL", "TRANSFER", "PAYMENT"};
    private final String[] CURRENCIES = {"USD", "EUR", "GBP", "JPY", "CNY"};

    /**
     * 测试系统处理并发交易的能力
     * 创建多个线程同时提交大量交易请求，验证系统的并发处理能力和稳定性
     * 
     * @author 薛鹏
     * @throws InterruptedException 当等待线程执行完成被中断时抛出
     */
    @Test
    @DisplayName("Should handle multiple concurrent transactions")
    void testConcurrentTransactions() throws InterruptedException {
        // 线程数
        int numThreads = 100;
        // 每个线程交易数量
        int transactionsPerThread = 100;
        
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);
        List<String> transactionIds = new ArrayList<>();
        
        long startTime = System.currentTimeMillis();
        
        // 启动线程
        for (int i = 0; i < numThreads; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < transactionsPerThread; j++) {
                        try {
                            // 创建请求
                            TransactionRequest request = new TransactionRequest();
                            request.setAmount(new BigDecimal(getRandomAmount()));
                            request.setDescription("Stress test transaction - Thread " + threadId + " - Tx " + j);
                            request.setType(getRandomTransactionType());
                            request.setCurrency(getRandomCurrency());
                            
                            // 创建交易
                            TransactionResponse response = transactionService.createTransaction(request);
                            synchronized (transactionIds) {
                                transactionIds.add(response.getId());
                            }
                            
                            // 增加成功计数
                            successCount.incrementAndGet();
                        } catch (Exception e) {
                            // 增加失败计数
                            failureCount.incrementAndGet();
                            System.err.println("Error in thread " + threadId + ": " + e.getMessage());
                        }
                    }
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有线程完成或超时
        boolean completed = latch.await(2, TimeUnit.MINUTES);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        executor.shutdown();
        
        System.out.println("-------- Stress Test Results --------");
        System.out.println("Completed: " + completed);
        System.out.println("Successful Transactions: " + successCount.get());
        System.out.println("Failed Transactions: " + failureCount.get());
        System.out.println("Duration: " + duration + " ms");
        System.out.println("Transaction Rate: " + (successCount.get() * 1000.0 / duration) + " transactions/sec");
        
        assertEquals(numThreads * transactionsPerThread, successCount.get() + failureCount.get(), 
                "Total transactions should match expected");
        assertTrue(successCount.get() > 0, "Should have some successful transactions");
        
        // 验证已创建的交易是否可以被查询
        if (!transactionIds.isEmpty()) {
            String testId = transactionIds.get(0);
            TransactionResponse retrievedTransaction = transactionService.getTransaction(testId);
            assertEquals(testId, retrievedTransaction.getId(), "Retrieved transaction ID should match");
        }
    }
    
    /**
     * 生成随机的交易类型
     * 
     * @author 薛鹏
     * @return 返回随机选择的交易类型
     */
    private String getRandomTransactionType() {
        return TRANSACTION_TYPES[(int) (Math.random() * TRANSACTION_TYPES.length)];
    }
    
    /**
     * 生成随机的交易货币
     * 
     * @author 薛鹏
     * @return 返回随机选择的交易货币
     */
    private String getRandomCurrency() {
        return CURRENCIES[(int) (Math.random() * CURRENCIES.length)];
    }
    
    /**
     * 生成随机的交易金额
     * 生成1.00到1000.00之间的随机金额，保留两位小数
     * 
     * @author 薛鹏
     * @return 返回随机生成的金额字符串
     */
    private String getRandomAmount() {
        return String.format("%.2f", 1.0 + (Math.random() * 999.0));
    }
} 