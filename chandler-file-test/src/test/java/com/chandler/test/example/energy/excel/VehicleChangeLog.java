/*
 * chandler-file-test
 * 2025/5/28 10:44
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.energy.excel;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/5/28 10:44
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class VehicleChangeLog {
    String truckPlateNumber;
    String carRegistration;
    String oilType;
    LocalDateTime bindingTime;
    LocalDateTime unbindingTime;
    String axlesNumber;
}