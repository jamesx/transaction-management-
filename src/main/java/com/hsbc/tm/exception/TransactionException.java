package com.hsbc.tm.exception;

/**
 * 交易异常基类
 * 定义交易处理过程中可能出现的各种异常
 * 
 * @author 薛鹏
 * @version 1.0
 */
public class TransactionException extends RuntimeException {
    private final String code;

    /**
     * 构造函数
     * 
     * @author 薛鹏
     * @param code 异常代码
     * @param message 异常消息
     */
    public TransactionException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 获取异常代码
     * 
     * @author 薛鹏
     * @return 返回异常代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 交易不存在异常
     * 当尝试访问不存在的交易时抛出
     * 
     * @author 薛鹏
     */
    public static class TransactionNotFoundException extends TransactionException {
        /**
         * 构造函数
         * 
         * @author 薛鹏
         * @param id 不存在的交易ID
         */
        public TransactionNotFoundException(String id) {
            super("TRX_NOT_FOUND", "Transaction with id " + id + " not found");
        }
    }

    /**
     * 交易重复异常
     * 当尝试创建ID已存在的交易时抛出
     * 
     * @author 薛鹏
     */
    public static class DuplicateTransactionException extends TransactionException {
        /**
         * 构造函数
         * 
         * @author 薛鹏
         * @param id 重复的交易ID
         */
        public DuplicateTransactionException(String id) {
            super("TRX_DUPLICATE", "Transaction with id " + id + " already exists");
        }
    }

    /**
     * 交易无效异常
     * 当交易数据不符合业务规则时抛出
     * 
     * @author 薛鹏
     */
    public static class InvalidTransactionException extends TransactionException {
        /**
         * 构造函数
         * 
         * @author 薛鹏
         * @param message 详细的错误消息
         */
        public InvalidTransactionException(String message) {
            super("TRX_INVALID", message);
        }
    }
} 