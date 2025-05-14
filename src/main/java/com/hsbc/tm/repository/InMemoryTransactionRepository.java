package com.hsbc.tm.repository;

import com.hsbc.tm.exception.TransactionException;
import com.hsbc.tm.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 交易仓库接口的内存实现
 * 使用内存数据结构存储交易数据，适用于开发和测试环境
 * 
 * @author 薛鹏
 * @version 1.0
 */
@Repository
public class InMemoryTransactionRepository implements TransactionRepository {
    private final Map<String, Transaction> transactionStore = new ConcurrentHashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    /**
     * 保存交易到内存存储
     * 
     * @author 薛鹏
     * @param transaction 要保存的交易实体
     * @return 返回保存后的交易实体
     * @throws IllegalArgumentException 当交易ID为空时抛出异常
     * @throws TransactionException.DuplicateTransactionException 当已存在相同ID的交易时抛出异常
     */
    @Override
    public Transaction save(Transaction transaction) {
        if (transaction.getId() == null) {
            throw new IllegalArgumentException("Transaction ID cannot be null");
        }
        
        if (existsById(transaction.getId())) {
            throw new TransactionException.DuplicateTransactionException(transaction.getId());
        }
        
        transactionStore.put(transaction.getId(), transaction);
        counter.incrementAndGet();
        return transaction;
    }

    /**
     * 根据ID查找交易
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 返回包含交易的Optional对象，如果不存在则为空
     */
    @Override
    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactionStore.get(id));
    }

    /**
     * 查找所有交易
     * 
     * @author 薛鹏
     * @return 返回所有交易的列表
     */
    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactionStore.values());
    }

    /**
     * 查找分页的交易列表
     * 
     * @author 薛鹏
     * @param page 页码，从0开始
     * @param size 每页的交易数量
     * @return 返回指定页的交易列表
     * @throws IllegalArgumentException 当页码或大小参数无效时抛出异常
     */
    @Override
    public List<Transaction> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size parameters");
        }
        
        int skip = page * size;
        return transactionStore.values().stream()
                .skip(skip)
                .limit(size)
                .collect(Collectors.toList());
    }

    /**
     * 检查指定ID的交易是否存在
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 如果交易存在返回true，否则返回false
     */
    @Override
    public boolean existsById(String id) {
        return transactionStore.containsKey(id);
    }

    /**
     * 根据ID删除交易
     * 
     * @author 薛鹏
     * @param id 要删除的交易ID
     * @throws TransactionException.TransactionNotFoundException 当指定ID的交易不存在时抛出异常
     */
    @Override
    public void deleteById(String id) {
        if (!existsById(id)) {
            throw new TransactionException.TransactionNotFoundException(id);
        }
        transactionStore.remove(id);
        counter.decrementAndGet();
    }

    /**
     * 更新交易信息
     * 
     * @author 薛鹏
     * @param transaction 包含更新信息的交易实体
     * @throws TransactionException.TransactionNotFoundException 当指定ID的交易不存在时抛出异常
     */
    @Override
    public void update(Transaction transaction) {
        if (!existsById(transaction.getId())) {
            throw new TransactionException.TransactionNotFoundException(transaction.getId());
        }
        transactionStore.put(transaction.getId(), transaction);
    }

    /**
     * 获取交易总数
     * 
     * @author 薛鹏
     * @return 返回系统中的交易总数
     */
    @Override
    public long count() {
        return counter.get();
    }
} 