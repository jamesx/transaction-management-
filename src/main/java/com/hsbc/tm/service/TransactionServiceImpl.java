package com.hsbc.tm.service;

import com.hsbc.tm.dto.TransactionRequest;
import com.hsbc.tm.dto.TransactionResponse;
import com.hsbc.tm.exception.TransactionException;
import com.hsbc.tm.model.Transaction;
import com.hsbc.tm.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 交易服务接口的实现类
 * 提供交易管理的具体业务逻辑实现
 * 
 * @author 薛鹏
 * @version 1.0
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    
    /**
     * 构造函数，通过依赖注入获取TransactionRepository实例
     * 
     * @author 薛鹏
     * @param transactionRepository 交易数据访问层接口的实现
     */
    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * 创建新交易
     * 验证交易请求数据的有效性并保存到存储库
     * 
     * @author 薛鹏
     * @param request 包含交易详情的请求对象
     * @return 返回创建的交易响应
     * @throws TransactionException.InvalidTransactionException 当请求数据无效时抛出异常
     */
    @Override
    public TransactionResponse createTransaction(TransactionRequest request) {
        validateTransactionRequest(request);
        
        Transaction transaction = new Transaction(
                request.getAmount(),
                request.getDescription(),
                request.getType(),
                request.getCurrency()
        );
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        return new TransactionResponse(savedTransaction);
    }

    /**
     * 根据ID获取交易
     * 使用Spring缓存机制减少数据库访问
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 返回找到的交易响应
     * @throws TransactionException.TransactionNotFoundException 当交易不存在时抛出异常
     */
    @Override
    @Cacheable(value = "transaction", key = "#id")
    public TransactionResponse getTransaction(String id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionException.TransactionNotFoundException(id));
        
        return new TransactionResponse(transaction);
    }

    /**
     * 获取所有交易
     * 
     * @author 薛鹏
     * @return 返回所有交易的列表
     */
    @Override
    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 获取分页的交易列表
     * 
     * @author 薛鹏
     * @param page 页码，从0开始
     * @param size 每页的交易数量
     * @return 返回指定页的交易列表
     */
    @Override
    public List<TransactionResponse> getAllTransactions(int page, int size) {
        return transactionRepository.findAll(page, size).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    /**
     * 更新现有交易
     * 验证更新数据的有效性，并在成功时清除缓存
     * 
     * @author 薛鹏
     * @param id 要更新的交易ID
     * @param request 包含更新信息的请求对象
     * @return 返回更新后的交易响应
     * @throws TransactionException.TransactionNotFoundException 当交易不存在时抛出异常
     * @throws TransactionException.InvalidTransactionException 当请求数据无效时抛出异常
     */
    @Override
    @CacheEvict(value = "transaction", key = "#id")
    public TransactionResponse updateTransaction(String id, TransactionRequest request) {
        validateTransactionRequest(request);
        
        Transaction existingTransaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionException.TransactionNotFoundException(id));
        
        existingTransaction.setAmount(request.getAmount());
        existingTransaction.setDescription(request.getDescription());
        existingTransaction.setType(request.getType());
        existingTransaction.setCurrency(request.getCurrency());
        
        transactionRepository.update(existingTransaction);
        
        return new TransactionResponse(existingTransaction);
    }

    /**
     * 删除交易
     * 删除后清除交易缓存
     * 
     * @author 薛鹏
     * @param id 要删除的交易ID
     */
    @Override
    @CacheEvict(value = "transaction", key = "#id")
    public void deleteTransaction(String id) {
        transactionRepository.deleteById(id);
    }

    /**
     * 获取交易总数
     * 
     * @author 薛鹏
     * @return 返回系统中的交易总数
     */
    @Override
    public long countTransactions() {
        return transactionRepository.count();
    }
    
    /**
     * 验证交易请求数据的有效性
     * 检查金额、类型和货币是否有效
     * 
     * @author 薛鹏
     * @param request 要验证的交易请求
     * @throws TransactionException.InvalidTransactionException 当验证失败时抛出异常
     */
    private void validateTransactionRequest(TransactionRequest request) {
        if (request.getAmount() == null || request.getAmount().doubleValue() <= 0) {
            throw new TransactionException.InvalidTransactionException("Transaction amount must be positive");
        }
        
        if (request.getType() == null || request.getType().trim().isEmpty()) {
            throw new TransactionException.InvalidTransactionException("Transaction type is required");
        }
        
        if (request.getCurrency() == null || request.getCurrency().trim().isEmpty()) {
            throw new TransactionException.InvalidTransactionException("Transaction currency is required");
        }
    }
} 