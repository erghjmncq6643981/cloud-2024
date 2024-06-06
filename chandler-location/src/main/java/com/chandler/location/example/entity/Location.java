package com.chandler.location.example.entity;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * @author hj
 */
@Data
public class Location {

    /**
     * 经度
     */
    @JSONField(serialize = false)
    private double longitude;

    /**
     * 纬度
     */
    @JSONField(serialize = false)
    private double latitude;

    /**
     * 是否在中国
     */
    @JSONField(serialize = false)
    private boolean isChina = true;

    public String getLonAndLat() {
        return longitude + StrUtil.COMMA + latitude;
    }

    @Override
    public String toString() {
        return longitude + StrUtil.COMMA + latitude;
    }

    public Location() {
    }

    public Location(String lonAndLat) {
        String[] data = lonAndLat.split(",");
        longitude = Double.parseDouble(data[0]);
        latitude = Double.parseDouble(data[1]);
    }
}
