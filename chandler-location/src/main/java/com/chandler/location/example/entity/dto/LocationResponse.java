/*
 * chandler-location
 * 2024/3/22 17:24
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.entity.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/22 17:24
 * @since 1.8
 */
@Data
public class LocationResponse {
    @JSONField(serialize = false)
    private String longitude;

    /** 纬度 */
    @JSONField(serialize = false)
    private String latitude;

    private Integer count;
}