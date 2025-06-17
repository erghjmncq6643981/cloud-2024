/*
 * chandler-file-test
 * 2025/5/28 13:29
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/5/28 13:29
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class VehicleOilDetail {
    /**
     * 车牌号
     */
    private String truckPlateNumber;
    /**
     * 能源类型
     */
    private String energyType;

    /**
     * 记录时间
     */
    private LocalDateTime recordDate;

    /**
     * 加油升数
     */
    private BigDecimal oilLitres;
    /**
     * 里程数
     */
    private BigDecimal mileage;

    /**
     * 是否加满 默认加满1
     */
    private Integer isFull;
}