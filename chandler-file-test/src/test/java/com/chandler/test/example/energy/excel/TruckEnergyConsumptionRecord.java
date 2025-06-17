/*
 * chandler-file-test
 * 2025/5/28 13:27
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
 * @author 钱丁君-chandler 2025/5/28 13:27
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class TruckEnergyConsumptionRecord {
    private LocalDateTime beginDate;

    /** 结束时间 */
    private LocalDateTime endDate;

    /** 天数 */
    private Integer workDay;

    /** 车辆作业港口 */
    private String portId;

    /** 车牌 */
    private String truckPlateNumber;

    /** 整车轴数 */
    private String axlesNumber;

    /** 司机姓名 */
    private String name;

    /** 区间行驶里程(KM) */
    private BigDecimal intervalMileage;

    /** 区间加注量 */
    private BigDecimal intervalOilLitres;

    /** 区间百公里能耗 */
    private BigDecimal intervalHundredConsumption;
}