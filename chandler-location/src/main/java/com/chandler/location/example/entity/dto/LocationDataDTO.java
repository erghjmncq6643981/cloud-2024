/*
 * chandler-location
 * 2024/4/10 09:04
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

import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/10 09:04
 * @since 1.8
 */
@Data
public class LocationDataDTO {
    private String lonAndLat;

    private LocalDateTime time;

    private String direction;

    private Integer speed;
}