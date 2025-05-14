package com.hsbc.tm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Web前端页面控制器
 * 负责处理页面跳转请求，返回相应的视图
 */
@Controller
public class WebController {

    /**
     * 访问首页
     * 当用户访问根路径时，返回index视图
     * 该方法将请求映射到templates/index.html模板
     * 
     * @return 返回"index"视图名称，由Thymeleaf模板引擎解析
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
} 