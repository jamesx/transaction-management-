package com.hsbc.tm.service;

import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.model.Transaction;

import java.util.List;

/**
 * 交易服务接口
 * 定义交易管理系统的核心业务逻辑
 * 
 * @author 薛鹏
 * @version 1.0
 */
public interface TransactionService {
    /**
     * 创建新交易
     * 
     * @author 薛鹏
     * @param request 包含交易详情的请求对象
     * @return 返回创建的交易响应
     */
    TransactionResponse createTransaction(TransactionRequest request);
    
    /**
     * 根据ID获取交易
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 返回找到的交易响应
     */
    TransactionResponse getTransaction(String id);
    
    /**
     * 获取所有交易
     * 
     * @author 薛鹏
     * @return 返回所有交易的列表
     */
    List<TransactionResponse> getAllTransactions();
    
    /**
     * 获取分页的交易列表
     * 
     * @author 薛鹏
     * @param page 页码，从0开始
     * @param size 每页的交易数量
     * @return 返回指定页的交易列表
     */
    List<TransactionResponse> getAllTransactions(int page, int size);
    
    /**
     * 更新现有交易
     * 
     * @author 薛鹏
     * @param id 要更新的交易ID
     * @param request 包含更新信息的请求对象
     * @return 返回更新后的交易响应
     */
    TransactionResponse updateTransaction(String id, TransactionRequest request);
    
    /**
     * 删除交易
     * 
     * @author 薛鹏
     * @param id 要删除的交易ID
     */
    void deleteTransaction(String id);
    
    /**
     * 获取交易总数
     * 
     * @author 薛鹏
     * @return 返回系统中的交易总数
     */
    long countTransactions();
} 