/*
 * chandler-location
 * 2024/4/8 11:21
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/8 11:21
 * @since 1.8
 */
@Data
public class RawLocationData {
    private String lpn;

    private String lonAndLat;

    private LocalDateTime reportTime;

    private String direction;

    private Integer speed;

    private Boolean status;
    private Integer distrustLevel;

    public void updateDistrustLevel(Integer level) {
        this.distrustLevel = level > 0 ? level : 0;
    }
}