package com.github.yt.mybatis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableYtMybatis
public class YtMybatisDemoApplication {
	public static void main(String[] args) {
		SpringApplication.run(YtMybatisDemoApplication.class, args);
	}
}
