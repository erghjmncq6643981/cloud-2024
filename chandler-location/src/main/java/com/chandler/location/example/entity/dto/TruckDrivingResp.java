/*
 * chandler-location
 * 2024/4/10 09:05
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.entity.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/10 09:05
 * @since 1.8
 */
@Data
public class TruckDrivingResp {
    private BigDecimal drivingMileage;
    private Integer locationSize;
    private List<LocationDataDTO> locations;
}