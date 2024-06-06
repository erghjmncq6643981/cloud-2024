/*
 * chandler-location
 * 2024/3/22 13:24
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

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/22 13:24
 * @since 1.8
 */
@Data
public class Cluster {
    private GeoPoint corePoint;
    private List<GeoPoint> samePoints = new ArrayList<>();

    public Cluster(GeoPoint point) {
        this.corePoint = point;
    }
}