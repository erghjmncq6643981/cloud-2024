/*
 * chandler-location
 * 2024/4/1 13:52
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.test;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/1 13:52
 * @since 1.8
 */
public class Test06 {
    public static void main(String[] args) {
        CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("异常测试");
        }).exceptionally(e -> {
            System.out.println("任务A发生异常，信息为：：" + e.getMessage());
            return "";
        });
    }
}