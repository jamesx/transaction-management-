package com.hsbc.tm.repository;

import com.hsbc.tm.model.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * 交易仓库接口
 * 定义交易数据访问层的操作方法
 * 
 * @author 薛鹏
 * @version 1.0
 */
public interface TransactionRepository {
    /**
     * 保存交易
     * 
     * @author 薛鹏
     * @param transaction 要保存的交易实体
     * @return 返回保存后的交易实体
     */
    Transaction save(Transaction transaction);
    
    /**
     * 根据ID查找交易
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 返回包含交易的Optional对象，如果不存在则为空
     */
    Optional<Transaction> findById(String id);
    
    /**
     * 查找所有交易
     * 
     * @author 薛鹏
     * @return 返回所有交易的列表
     */
    List<Transaction> findAll();
    
    /**
     * 查找分页的交易列表
     * 
     * @author 薛鹏
     * @param page 页码，从0开始
     * @param size 每页的交易数量
     * @return 返回指定页的交易列表
     */
    List<Transaction> findAll(int page, int size);
    
    /**
     * 检查指定ID的交易是否存在
     * 
     * @author 薛鹏
     * @param id 交易的唯一标识符
     * @return 如果交易存在返回true，否则返回false
     */
    boolean existsById(String id);
    
    /**
     * 根据ID删除交易
     * 
     * @author 薛鹏
     * @param id 要删除的交易ID
     */
    void deleteById(String id);
    
    /**
     * 更新交易信息
     * 
     * @author 薛鹏
     * @param transaction 包含更新信息的交易实体
     */
    void update(Transaction transaction);
    
    /**
     * 获取交易总数
     * 
     * @author 薛鹏
     * @return 返回系统中的交易总数
     */
    long count();
} 