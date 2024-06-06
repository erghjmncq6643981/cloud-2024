/*
 * chandler-location
 * 2024/3/22 10:26
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.entity;

import com.chandler.location.example.util.ElectronicFenceUtils;
import lombok.Data;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/22 10:26
 * @since 1.8
 */
@Data
public class GeoPoint {
    double latitude;
    double longitude;
    boolean visited=false;
    int clusterId = 0;

    // 计算两个数据点之间的球面距离
    public Integer distanceTo(GeoPoint other) {
        return ElectronicFenceUtils.calLonLatDistance(this.longitude,this.latitude,other.longitude,other.latitude);
    }
}