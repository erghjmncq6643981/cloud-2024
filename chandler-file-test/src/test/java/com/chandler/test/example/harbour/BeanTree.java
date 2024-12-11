/*
 * chandler-file-test
 * 2024/12/5 10:39
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.harbour;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.chandler.test.example.harbour.entity.BeanInfoBO;
import com.chandler.test.example.harbour.excel.BeanInfoExcel;
import com.chandler.test.example.harbour.excel.BeanTreeInfoExcel;
import com.chandler.test.example.harbour.excel.BeanUsedInfoExcel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/5 10:39
 * @version 1.0.0
 * @since 1.8
 */
public class BeanTree {
    public static <T> T readJsonFile(String path, Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(path), valueType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<BeanInfoBO> readListJsonFile(String path) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(new File(path), new TypeReference<List<BeanInfoBO>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
//        BeanInfoBO info = readJsonFile("/Users/chandler/Documents/鸭嘴兽/harbour/Bean依赖分析/forwarderService的依赖.json", BeanInfoBO.class);
        List<BeanInfoBO> infoList = readListJsonFile("/Users/chandler/Documents/鸭嘴兽/harbour/Bean依赖分析/harbour启动初始化bean（activityFinishedConsumer优化）.json");
        List<BeanTreeInfoExcel> beanExcels = Lists.newArrayList();
        Map<String, BeanUsedInfoExcel> usedExcelMap= Maps.newHashMap();
        for (BeanInfoBO info : infoList) {
            if (Objects.nonNull(info.getTags().get("class")) && Boolean.FALSE.equals(info.getTags().get("class").startsWith("com.eshipping"))) {
                continue;
            }
            List<BeanInfoExcel> excels = Lists.newArrayList();
            Integer level = 1;
            analyze(level, info, excels,usedExcelMap);
            excels.forEach(child -> System.out.println(child));
            try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
                excelWriter.write(excels);
                FileOutputStream fos =
                        new FileOutputStream(String.format("/Users/chandler/Documents/鸭嘴兽/harbour/Bean依赖分析/分析结果（activityFinishedConsumer优化）/%s的Bean依赖分析结果.xlsx", info.getName()));
                excelWriter.flush(fos);
                fos.close();
            } catch (Exception e) {
                System.out.println("发生异常！");
            }
            BeanTreeInfoExcel excel = new BeanTreeInfoExcel();
            BeanUtils.copyProperties(info, excel);
            excel.setClassloader(info.getTags().get("classloader"));
            excel.setClassName(info.getTags().get("class"));
            excel.setThreadName(info.getTags().get("threadName"));
            excel.setHasChildren(Boolean.FALSE.equals(info.getChildren().isEmpty()));
            excel.setChildrenNumber(info.getChildren().size());
            excel.setDeep(excels.get(0).getDeep());
            beanExcels.add(excel);
        }
        try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
            excelWriter.write(usedExcelMap.values().stream().sorted(Comparator.comparing(BeanUsedInfoExcel::getUsedTimes)).toList());
            FileOutputStream fos =
                    new FileOutputStream(String.format("/Users/chandler/Documents/鸭嘴兽/harbour/Bean依赖分析/bean的整体分析/%s的使用情况分析（activityFinishedConsumer优化）.xlsx", "业务bean"));
            excelWriter.flush(fos);
            fos.close();
        } catch (Exception e) {
            System.out.println("发生异常！");
        }

        try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
            excelWriter.write(beanExcels.stream().sorted(Comparator.comparing(BeanTreeInfoExcel::getDeep).reversed()).toList());
            FileOutputStream fos =
                    new FileOutputStream(String.format("/Users/chandler/Documents/鸭嘴兽/harbour/Bean依赖分析/bean的整体分析/%s的整体分析结果（activityFinishedConsumer优化）.xlsx", "业务bean"));
            excelWriter.flush(fos);
            fos.close();
        } catch (Exception e) {
            System.out.println("发生异常！");
        }
    }

    public static Integer analyze(Integer level, BeanInfoBO info, List<BeanInfoExcel> excels,Map<String, BeanUsedInfoExcel> usedExcelMap) {
        BeanInfoExcel excel = new BeanInfoExcel();
        BeanUtils.copyProperties(info, excel);
        excel.setClassloader(info.getTags().get("classloader"));
        excel.setClassName(info.getTags().get("class"));
        excel.setThreadName(info.getTags().get("threadName"));
        excel.setHasChildren(Boolean.FALSE.equals(info.getChildren().isEmpty()));
        excel.setChildrenNumber(info.getChildren().size());
        excel.setLevel(level);
        excels.add(excel);

        if (info.getChildren().isEmpty()) {
            excel.setDeep(0);
            return level;
        } else {
            Optional<Integer> maxO = info.getChildren().stream().map(child -> analyze(level + 1, child, excels,usedExcelMap)).max(Comparator.naturalOrder());
            excel.setDeep(maxO.get() - level);
            String parentMessage=String.format("父bean：%s;最大深度：%s;当前深度：%s;",excel.getName(),excel.getDeep(),level);
            info.getChildren().forEach(c->{
                BeanUsedInfoExcel usedInfo= usedExcelMap.get(c.getName());
                if(Objects.isNull(usedInfo)){
                    usedInfo=new BeanUsedInfoExcel();
                    usedInfo.setName(c.getName());
                    usedInfo.setClassName(c.getTags().get("class"));
                    usedInfo.setUsedTimes(1);
                    usedInfo.setParents(Lists.newArrayList());
                    usedInfo.getParents().add(parentMessage);
                    usedExcelMap.put(usedInfo.getName(),usedInfo);
                }else {
                    usedInfo.setUsedTimes(usedInfo.getUsedTimes()+1);
                    usedInfo.getParents().add(parentMessage);
                }
            });
            return maxO.get();
        }
    }
}