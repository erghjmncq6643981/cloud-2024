package com.chandler.instance.client.example.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2019/5/17下午2:02
 * @since 1.8
 */
@Getter
@Setter
@Builder
public class Person {
    private String name;
    private String age;
    private String sex;
}
