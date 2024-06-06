/*
 * chandler-location
 * 2024/3/20 10:39
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.test;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/20 10:39
 * @since 1.8
 */
public class Test04 {
    private static final double EPSILON = 0.5; // 半径阈值
    private static final int MIN_POINTS = 5; // 核心对象周围所需的最小点数

    public static void dbscan(List<double[]> points) {
        boolean[] visited = new boolean[points.size()];
        int[] clusterId = new int[points.size()];
        int clusterIdCounter = 0;

        for (int i = 0; i < points.size(); i++) {
            if (!visited[i]) {
                List<Integer> corePoints = new ArrayList<>();
                dfs(points.get(i), points, visited, corePoints, i, EPSILON, MIN_POINTS);

                if (corePoints.size() >= MIN_POINTS) {
                    for (int point : corePoints) {
                        clusterId[point] = ++clusterIdCounter;
                    }
                }
            }
        }

        // 输出聚类结果
        for (int i = 0; i < clusterId.length; i++) {
            System.out.println("Point " + i + " belongs to cluster: " + clusterId[i]);
        }
    }

    private static void dfs(double[] point, List<double[]> points, boolean[] visited, List<Integer> corePoints, int index, double epsilon, int minPoints) {
        if (!visited[index] && isCoreObject(point, points.get(index), epsilon)) {
            visited[index] = true;
            corePoints.add(index);

            for (int i = 0; i < points.size(); i++) {
                if (!visited[i] && isCoreObject(point, points.get(i), epsilon)) {
                    dfs(point, points, visited, corePoints, i, epsilon, minPoints);
                }
            }
        }
    }

    private static boolean isCoreObject(double[] p1, double[] p2, double epsilon) {
        return Math.sqrt(Math.pow(p1[0] - p2[0], 2) + Math.pow(p1[1] - p2[1], 2)) <= epsilon;
    }

    public static void main(String[] args) {
        // 示例数据点
        List<double[]> points = new ArrayList<>();
        points.add(new double[]{1, 1});
        points.add(new double[]{2, 1});
        points.add(new double[]{2, 2});
        points.add(new double[]{1, 2});
        points.add(new double[]{8, 8});
        points.add(new double[]{8, 9});
        points.add(new double[]{9, 8});
        points.add(new double[]{9, 9});

        dbscan(points);
    }
}