package com.hsbc.tm.dto;

import java.math.BigDecimal;

/**
 * 交易请求数据传输对象
 * 用于客户端向服务器发送创建或更新交易的请求
 * 
 * @author 薛鹏
 * @version 1.0
 */
public class TransactionRequest {
    /**
     * 交易金额
     * 必须为正数
     */
    private BigDecimal amount;
    
    /**
     * 交易描述
     * 可选字段，提供交易的附加信息
     */
    private String description;
    
    /**
     * 交易类型
     * 如: "DEPOSIT", "WITHDRAWAL", "TRANSFER"等
     * 必填字段
     */
    private String type;
    
    /**
     * 交易货币
     * 如: "USD", "EUR", "CNY"等
     * 必填字段
     */
    private String currency;

    // Getters and Setters
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
} 