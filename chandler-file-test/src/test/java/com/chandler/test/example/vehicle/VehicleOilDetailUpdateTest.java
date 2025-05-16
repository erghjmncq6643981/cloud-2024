/*
 * chandler-file-test
 * 2025/4/18 13:53
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.vehicle;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.chandler.test.example.vehicle.excel.HitchBridgeData;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.math3.util.Pair;
import org.springframework.boot.test.util.TestPropertyValues;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/4/18 13:53
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class VehicleOilDetailUpdateTest {
    public static void main(String[] args) {
        // 文件路径
        String fileName = "/Users/chandler/Downloads/油耗异常数据处理.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1); // 0表示第一个sheet的索引，也可以使用sheet的名称作为参数
        Set<String> plateNumbers= Sets.newConcurrentHashSet();

        List<HitchBridgeData> data=Lists.newArrayList();
        for (List<Object> row : read) {
            String id= (String) row.get(0);
            String plateNumber= (String) row.get(2);
            String driverName= (String) row.get(3);
            String driverType= (String) row.get(6);
            String hitchBridge= (String) row.get(7);
            Pair<String,String> pair=get(driverType,hitchBridge);
           String tractorSpecModel= (String) row.get(9);
            String axlesNumber= (String) row.get(10);
           if(!pair.getFirst().equals(tractorSpecModel)){
               plateNumbers.add(plateNumber);
               System.out.println(String.format("车辆型号不一致！ID：%s；司机姓名：%s；车牌：%s；数据库中数据：%s;正确数据：%s",id,driverName,plateNumber,tractorSpecModel,pair.getFirst()));
               data.add(HitchBridgeData.builder()
                               .id(id)
                               .tractorSpecModel(pair.getFirst())
                               .axlesNumber(pair.getSecond())
                       .build());
           }
        }
        System.out.println(String.format("一共%s条数据，涉及%s辆车辆!plateNumbers:%s",data.size(),plateNumbers.size(),plateNumbers));

        String template = "update vehicle_oil_detail \n" +
                "set `tractor_spec_model` ='%s',`axles_number` ='%s',`last_update_time` =NOW() \n" +
                "where id in (%s) ;";
        Map<String,List<HitchBridgeData>> bridgeMap= data.stream().collect(Collectors.groupingBy(HitchBridgeData::getTractorSpecModel));
        bridgeMap.forEach((k,v)->{
            String tractorSpecModel=v.get(0).getTractorSpecModel();
            String axlesNumber=v.get(0).getAxlesNumber();
            String ids=v.stream().map(HitchBridgeData::getId).collect(Collectors.joining(","));
            System.out.println(String.format(template,tractorSpecModel,axlesNumber,ids));
            System.out.println(String.format("整车型号：%s；轴数：%s；修正数据的数量：%s",tractorSpecModel,axlesNumber,v.size()));
        });


    }

    private static Pair<String,String> get(String driverType,String hitchBridge){
        String tractorSpecModel = "";
        String axlesNumber="";
        switch (hitchBridge) {
            case "63":
                if (driverType.equals("SINGLE_DRIVE")) {
                    tractorSpecModel = "一拖一";
                    axlesNumber = "三轴车";
                } else if (driverType.equals("DOUBLE_DRIVE")) {
                    tractorSpecModel = "二拖一";
                    axlesNumber = "四轴车";
                }
                break;
            case "64":
                if (driverType.equals("SINGLE_DRIVE")) {
                    tractorSpecModel = "一拖二";
                    axlesNumber = "四轴车";
                } else if (driverType.equals("DOUBLE_DRIVE")) {
                    tractorSpecModel = "二拖二";
                    axlesNumber = "五轴车";
                }
                break;
            case "65":
                if (driverType.equals("SINGLE_DRIVE")) {
                    tractorSpecModel = "一拖三";
                    axlesNumber = "五轴车";
                } else if (driverType.equals("DOUBLE_DRIVE")) {
                    tractorSpecModel = "二拖三";
                    axlesNumber = "六轴车";
                }
                break;
        }
        return Pair.create(tractorSpecModel,axlesNumber);
    }
}