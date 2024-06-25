/*
 * chandler-elasticsearch-demo
 * 2024/6/24 14:44
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.elasticsearch.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/6/24 14:44
 * @version 1.0.0
 * @since 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChargeItem {
    String chargeItermCode;
    String chargeIterm;
    Double price;
}