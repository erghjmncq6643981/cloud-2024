/*
 * chandler-tomcat-jersey-demo
 * 2025/1/22 16:57
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package org.example.chandlertomcatjerseydemo;

import org.example.chandlertomcatjerseydemo.resource.HelloResource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/1/22 16:57
 * @version 1.0.0
 * @since 1.8
 */
@SpringBootApplication
public class BootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }

//    @Bean
    public MyApplication myApplication(){
        MyApplication myApplication=new MyApplication();
        myApplication.register(HelloResource.class);
        return myApplication;
    }
}