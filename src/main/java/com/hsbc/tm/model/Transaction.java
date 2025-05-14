package com.hsbc.tm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    /**
     *
     * 交易的唯一标识符
     * 自动使用UUID生成
     * 用于查询、更新和删除操作
     *
     */
    private String id;
    /**
     * 交易金额
     */
    private BigDecimal amount;

    /**
     *交易描述
     * 提供交易的附加信息或备注
     */
    private String description;
    /**
     * 交易类型
     * 例如："DEPOSIT"（存款）、"WITHDRAWAL"（取款）、"TRANSFER"（转账）、"PAYMENT"（支付）
     * 用于分类和筛选交易
     *
     */
    private String type; // 例如："DEPOSIT", "WITHDRAWAL", "TRANSFER"
    /**
     *交易时间戳
     * 记录交易创建的确切时间
     * 自动设置为当前时间
     *
     */
    private LocalDateTime timestamp;

    /**
     *交易货币
     * 例如："USD"、"EUR"、"GBP"等
     * 支持多币种交易记录
     *
     */
    private String currency;

    /**
     * 交易状态
     * 例如："PENDING"（待处理）、"COMPLETED"（已完成）、"FAILED"（失败）
     * 默认为"COMPLETED"
     * 用于跟踪交易处理流程
     *
     */
    private String status; // 例如："PENDING", "COMPLETED", "FAILED"

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
        this.status = "COMPLETED"; // 默认状态
    }

    public Transaction(BigDecimal amount, String description, String type, String currency) {
        this();
        this.amount = amount;
        this.description = description;
        this.type = type;
        this.currency = currency;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 