/*
 * chandler-tomcat-jersey-demo
 * 2025/1/22 10:53
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
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/1/22 10:53
 * @version 1.0.0
 * @since 1.8
 */
@Configuration
@ApplicationPath("/jersey")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        register(HelloResource.class);
    }
}