/*
 * chandler-file-test
 * 2025/6/16 10:29
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.vehicle.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/6/16 10:29
 * @version 1.0.0
 * @since 1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransportDriverInfo {
    String id;
    String memberShipId;
    String name;
    String phoneNumber;
    String cardId;
    String cardIdFrontUrl;
    String cardIdBackUrl;
    Boolean isActive;
}