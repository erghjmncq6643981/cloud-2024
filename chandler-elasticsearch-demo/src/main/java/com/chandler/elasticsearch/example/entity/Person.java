package com.chandler.elasticsearch.example.entity;

import lombok.*;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2019/5/17下午2:02
 * @since 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String name;
    private String sex;
    private Integer age;
    private List<ChargeItem> items;
}
