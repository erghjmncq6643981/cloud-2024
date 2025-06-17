/*
 * chandler-shcdule-test
 * 2025/5/19 14:05
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.schedule.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/5/19 14:05
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
@Component
public class ScheduleTest implements InitializingBean {
    private final static ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduledExecutor.scheduleAtFixedRate(()->{
                    try {
                        Thread.sleep(60*1000L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    log.info("测试");
                },
                0,
                1,
                TimeUnit.SECONDS);
    }
}