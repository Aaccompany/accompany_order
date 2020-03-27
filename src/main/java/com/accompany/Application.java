package com.accompany;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.accompany.order"})
@EnableAsync(proxyTargetClass = true)
@MapperScan("com.accompany.order.service")
@EnableTransactionManagement
public class Application {

    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		log.debug("启动成功");
	}
}
