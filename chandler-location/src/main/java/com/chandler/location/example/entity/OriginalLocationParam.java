/*
 * chandler-location
 * 2024/4/10 08:53
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/10 08:53
 * @since 1.8
 */
@Data
public class OriginalLocationParam {
    private String lpn;

    private Integer sourceType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeFrom;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeTo;
    private Integer distrustLevel;
}