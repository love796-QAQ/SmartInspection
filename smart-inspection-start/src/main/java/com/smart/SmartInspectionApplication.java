package com.smart;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.smart.system.mapper", "com.smart.biz.mapper"})
public class SmartInspectionApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartInspectionApplication.class, args);
    }
}
