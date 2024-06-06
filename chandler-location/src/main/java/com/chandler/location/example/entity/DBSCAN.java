/*
 * chandler-location
 * 2024/3/22 10:29
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
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/22 10:29
 * @since 1.8
 */
@Slf4j
@Data
public class DBSCAN {
    private List<Cluster> clusterList = new ArrayList<>();
    private List<GeoPoint> data;
    private int eps;
    private int minPts;

    public DBSCAN(List<GeoPoint> data, int eps, int minPts) {
        this.data = data;
        this.eps = eps;
        this.minPts = minPts;
    }

    public List<Cluster> dbscan() {
        for (GeoPoint point : data) {
            if (isExistCluster(point, clusterList)) continue; //已经在簇中的点不再考虑
//            if(point.isVisited()) continue;

            List<GeoPoint> neighbors = getNeighbors(point);
            if (neighbors.size() < minPts) {
                point.clusterId = -1; // 噪声点
            } else {
                Cluster cluster = new Cluster(point);
                clusterList.add(cluster);
                cluster.setSamePoints(neighbors);
                expandCluster(cluster);
            }
        }
        List<GeoPoint> points = data.stream().filter(p -> -1 == p.clusterId).collect(Collectors.toList());
        log.info("噪点数量：{}", points.size());
        clusterList.forEach(c -> {
            log.info("核心点:{};族内点数：{}", c.getCorePoint(), c.getSamePoints().size());
        });
        return clusterList;
    }

    private boolean isExistCluster(GeoPoint point, List<Cluster> clusterList) {
        for (Cluster cluster : clusterList) {
            List<GeoPoint> pointList = cluster.getSamePoints();
            if (pointList.contains(point)) return true;
        }
        return false;
    }

    /**
     * @param
     * @return
     * @Description: 获取一个点的eps领域内所有的点集合
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/3/22 13:43
     */
    private List<GeoPoint> getNeighbors(GeoPoint point) {
        List<GeoPoint> neighbors = new ArrayList<>();
        for (GeoPoint other : data) {
            if (point.distanceTo(other) <= eps) {
                neighbors.add(other);
            }
        }
        return neighbors;
    }

    private void expandCluster(Cluster cluster) {
        cluster.getCorePoint().setVisited(Boolean.TRUE);
        List<GeoPoint> samePoints = cluster.getSamePoints();
        for (int i = 0; i < samePoints.size(); i++) {
            GeoPoint neighbor = samePoints.get(i);
            if(cluster.getCorePoint().equals(neighbor))continue;
            List<GeoPoint> neighbors = getNeighbors(neighbor);
            if (neighbors.size() >= minPts) {
                for (int j = 0; j < neighbors.size();j++){
                    if(samePoints.contains(neighbors.get(j))){
                        continue;
                    }
                    samePoints.add(neighbors.get(j));
                }
            }
            if (neighbor.clusterId == 0) {
                neighbor.setClusterId(0);
            }
        }
    }
}