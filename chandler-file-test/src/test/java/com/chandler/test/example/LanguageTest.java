/*
 * chandler-file-test
 * 2025/6/10 18:24
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/6/10 18:24
 * @version 1.0.0
 * @since 1.8
 */
public class LanguageTest {
    public static void main(String[] args) {
        // 设置越南语环境
        Locale vietnamese = new Locale("vi", "VN");
        ResourceBundle viBundle = ResourceBundle.getBundle("messages", vietnamese);
        Locale chinese = new Locale("cn", "CN");
        ResourceBundle chBundle = ResourceBundle.getBundle("messages", chinese);

        // 获取翻译文本
        String welcome = viBundle.getString("河内");
        System.out.println(welcome); // 输出: Xin chào

        System.out.println(chBundle.getString(welcome));
    }
}