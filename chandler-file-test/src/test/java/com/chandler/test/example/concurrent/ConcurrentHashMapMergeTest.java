/*
 * chandler-file-test
 * 2025/3/31 13:33
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/3/31 13:33
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class ConcurrentHashMapMergeTest {

    public static void main(String[] args) throws InterruptedException {
        // 初始化ConcurrentHashMap
        ConcurrentHashMap<String, DataCache> map = new ConcurrentHashMap<>();

        // 创建固定线程池（3个线程并发执行）
        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 提交3个线程并发更新"apple"键的值
        for (int i = 0; i < 3; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 使用merge方法原子性累加
                    map.merge("apple", DataCache.builder()
                            .id((long) j)
                            .dataId(5L)
                            .build(), (oldValue,newValue)->{
                        if (newValue.getId() > oldValue.getId()) { // 严格大于
                            log.info(
                                    "{}[{}] id[{}] > cacheId[{}] 本次更新有效！",
                                    "table",
                                    5,
                                    newValue.getId(),
                                    oldValue.getId());
                            return newValue;
                        } else {
                            log.warn(
                                    "{}[{}] id[{}] < cacheId[{}] 忽略本次更新！",
                                    "table",
                                    5,
                                    newValue.getId(),
                                    oldValue.getId());
                            return oldValue;
                        }
                    });
                }
            });
        }

        // 关闭线程池并等待所有任务完成
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // 验证最终结果
        System.out.println("Final Apple count: " + map.get("apple")); // 预期输出：3000
    }
}