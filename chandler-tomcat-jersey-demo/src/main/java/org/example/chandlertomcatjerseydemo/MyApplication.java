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

import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.web.filter.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/1/22 10:53
 * @version 1.0.0
 * @since 1.8
 */
//@ApplicationPath("/jersey")
public class MyApplication extends ResourceConfig {
    public MyApplication() {
        // 初始化Resource，以指定包的形式初始化，多个包之间以分号隔开
        packages("org.example.chandlertomcatjerseydemo.resource");
        packages("org.glassfish.jersey.examples.multipart");

        // 注册spring filter
        register(RequestContextFilter.class);

        // 注册数据转换器，支持传参和返回信息json格式与bean之间的自动转换
        register(JacksonJsonProvider.class);

        // 注册支持multipart-formdata格式的请求
        register(org.glassfish.jersey.media.multipart.MultiPartFeature.class);
    }
}