
package com.chandler.location.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 模板工程
 *
 * @version 1.0
 * @author 钱丁君-chandler 4/2/21 10:05 PM
 * @since 1.8
 */
@MapperScan("com.chandler.location.example.domain.mapper")
@SpringBootApplication
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
