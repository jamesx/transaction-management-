package com.hsbc.tm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 交易管理系统的主应用程序类
 * 负责启动Spring Boot应用程序
 * 
 * @author 薛鹏
 * @version 1.0
 */
@SpringBootApplication
public class TmApplication {

	/**
	 * 应用程序入口点
	 * 启动Spring Boot应用程序
	 * 
	 * @author 薛鹏
	 * @param args 命令行参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(TmApplication.class, args);
	}

}
