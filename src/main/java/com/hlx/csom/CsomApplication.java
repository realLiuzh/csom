package com.hlx.csom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.hlx.csom.mapper")
//public class CsomApplication  {
@EnableScheduling
public class CsomApplication  {
//public class CsomApplication extends SpringBootServletInitializer {
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//        return application.sources(CsomApplication.class);
//    }

    public static void main(String[] args) {
        SpringApplication.run(CsomApplication.class, args);
    }

}
