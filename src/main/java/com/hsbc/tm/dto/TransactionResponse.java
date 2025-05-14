package com.hsbc.tm.dto;

import com.hsbc.tm.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 交易响应数据传输对象
 * 用于服务器向客户端返回交易数据
 * 
 * @author 薛鹏
 * @version 1.0
 */
public class TransactionResponse {
    /**
     * 交易ID
     */
    private String id;
    
    /**
     * 交易金额
     */
    private BigDecimal amount;
    
    /**
     * 交易描述
     */
    private String description;
    
    /**
     * 交易类型
     * 如: "DEPOSIT", "WITHDRAWAL", "TRANSFER"等
     */
    private String type;
    
    /**
     * 交易时间戳
     */
    private LocalDateTime timestamp;
    
    /**
     * 交易货币
     */
    private String currency;
    
    /**
     * 交易状态
     * 如: "COMPLETED", "PENDING", "FAILED"等
     */
    private String status;

    /**
     * 默认构造函数
     * 
     * @author 薛鹏
     */
    public TransactionResponse() {
    }

    /**
     * 从交易实体构造响应对象
     * 
     * @author 薛鹏
     * @param transaction 交易实体对象
     */
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.type = transaction.getType();
        this.timestamp = transaction.getTimestamp();
        this.currency = transaction.getCurrency();
        this.status = transaction.getStatus();
    }

    /**
     * 获取交易ID
     * 
     * @author 薛鹏
     * @return 返回交易ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置交易ID
     * 
     * @author 薛鹏
     * @param id 交易ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取交易金额
     * 
     * @author 薛鹏
     * @return 返回交易金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置交易金额
     * 
     * @author 薛鹏
     * @param amount 交易金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取交易描述
     * 
     * @author 薛鹏
     * @return 返回交易描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置交易描述
     * 
     * @author 薛鹏
     * @param description 交易描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取交易类型
     * 
     * @author 薛鹏
     * @return 返回交易类型
     */
    public String getType() {
        return type;
    }

    /**
     * 设置交易类型
     * 
     * @author 薛鹏
     * @param type 交易类型
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取交易时间戳
     * 
     * @author 薛鹏
     * @return 返回交易时间戳
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * 设置交易时间戳
     * 
     * @author 薛鹏
     * @param timestamp 交易时间戳
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * 获取交易货币
     * 
     * @author 薛鹏
     * @return 返回交易货币
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * 设置交易货币
     * 
     * @author 薛鹏
     * @param currency 交易货币
     */
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    /**
     * 获取交易状态
     * 
     * @author 薛鹏
     * @return 返回交易状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置交易状态
     * 
     * @author 薛鹏
     * @param status 交易状态
     */
    public void setStatus(String status) {
        this.status = status;
    }
} 