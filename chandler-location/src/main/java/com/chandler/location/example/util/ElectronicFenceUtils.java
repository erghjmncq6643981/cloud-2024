package com.chandler.location.example.util;

import cn.hutool.core.collection.CollectionUtil;
import com.chandler.location.example.entity.Location;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2023/12/4 17:29
 * @since 1.8
 */
@UtilityClass
public class ElectronicFenceUtils {
    /** 地球半径 */
    public static final double EARTH_RADIUS = 6378137D;

    /**
     * 判断定位点是否在圆内
     *
     * @param radius 半径（单位/米）
     * @param location 圆心坐标
     * @param circleCenterPoint 判断点坐标
     * @return true:在圆内, false:在圆外
     */
    public boolean isInCircle(double radius, Location location, Location circleCenterPoint) {
        double radLat1 = Math.toRadians(location.getLatitude());
        double radLat2 = Math.toRadians(circleCenterPoint.getLatitude());
        double a = radLat1 - radLat2;
        double b = Math.toRadians(location.getLongitude()) - Math.toRadians(circleCenterPoint.getLongitude());
        double s =
                2
                        * Math.asin(
                        Math.sqrt(
                                Math.pow(Math.sin(a / 2), 2)
                                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        double distance = Math.round(s * EARTH_RADIUS * 10000D) / 10000D;
        return distance <= radius;
    }

    /**
     * 使用射线穿过算法，判断定位点是否在多边形范围内
     *
     * @param location 定位点
     * @param locations 多边形经纬度
     * @return true:在多边形范围内，false: 不在多边形范围内。
     */
    public boolean isInPolygon(Location location, List<Location> locations) {
        if (CollectionUtil.isEmpty(locations)) {
            return false;
        }

        // 交叉点数量
        int intersectCount = 0;

        // 浮点类型计算时候与0比较时候的容差
        double precision = 2e-10;

        // 临近顶点
        Location p1 = locations.get(0), p2;

        for (int i = 1; i <= locations.size(); ++i) {
            if (location.equals(p1)) {
                return true;
            }

            p2 = locations.get(i % locations.size());
            if (location.getLongitude() < Math.min(p1.getLongitude(), p2.getLongitude())
                    || location.getLongitude() > Math.max(p1.getLongitude(), p2.getLongitude())) {
                p1 = p2;
                continue;
            }

            if (location.getLongitude() > Math.min(p1.getLongitude(), p2.getLongitude())
                    && location.getLongitude() < Math.max(p1.getLongitude(), p2.getLongitude())) {
                if (location.getLatitude() <= Math.max(p1.getLatitude(), p2.getLatitude())) {
                    if (p1.getLongitude() == p2.getLongitude()
                            && location.getLatitude() >= Math.min(p1.getLatitude(), p2.getLatitude())) {
                        return true;
                    }

                    if (p1.getLatitude() == p2.getLatitude()) {
                        if (p1.getLatitude() == location.getLatitude()) {
                            return true;
                        } else {
                            ++intersectCount;
                        }
                    } else {
                        double xInters =
                                (location.getLongitude() - p1.getLongitude())
                                        * (p2.getLatitude() - p1.getLatitude())
                                        / (p2.getLongitude() - p1.getLongitude())
                                        + p1.getLatitude();
                        if (Math.abs(location.getLatitude() - xInters) < precision) {
                            return true;
                        }

                        if (location.getLatitude() < xInters) {
                            ++intersectCount;
                        }
                    }
                }
            } else {
                if (location.getLongitude() == p2.getLongitude()
                        && location.getLatitude() <= p2.getLatitude()) {
                    Location p3 = locations.get((i + 1) % locations.size());
                    if (location.getLongitude() >= Math.min(p1.getLongitude(), p3.getLongitude())
                            && location.getLongitude() <= Math.max(p1.getLongitude(), p3.getLongitude())) {
                        ++intersectCount;
                    } else {
                        intersectCount += 2;
                    }
                }
            }
            p1 = p2;
        }
        return intersectCount % 2 != 0;
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param prevLocation 上个点经纬度
     * @param currLocation 当前点经纬度
     * @return 经纬度距离
     */
    public static Integer calLonLatDistance(Location prevLocation, Location currLocation) {
        if (prevLocation == null || currLocation == null) {
            return 0;
        }
        double prevLon = prevLocation.getLongitude();
        double prevLat = prevLocation.getLatitude();
        double currLon = currLocation.getLongitude();
        double currLat = currLocation.getLatitude();
        return calLonLatDistance(prevLon, prevLat, currLon, currLat);
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param lon1 经度1
     * @param lat1 纬度1
     * @param lon2 经度2
     * @param lat2 维度2
     * @return 距离 单位:米
     */
    public static Integer calLonLatDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lon1) - Math.toRadians(lon2);
        double s =
                2
                        * Math.asin(
                        Math.sqrt(
                                Math.pow(Math.sin(a / 2), 2)
                                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return BigDecimal.valueOf(s).setScale(0, RoundingMode.UP).intValue();
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }

}
