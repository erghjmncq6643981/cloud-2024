/*
 * chandler-location
 * 2024/3/18 15:25
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.test;

import com.chandler.location.example.util.FileUtil;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/18 15:25
 * @since 1.8
 */
public class Test02 {
    public static void main(String[] args) {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        List<String> list = Lists.newArrayList();
        String filePath = "/Users/chandler/Downloads/20240101-0317-中山市作业车辆.xlsx";
        wb = FileUtil.readExcel(filePath);
        if (wb != null) {
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            //获取第一行
//            row = sheet.getRow(0);
            //获取最大列数
            for (int i = 1; i < rowNum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    Cell c=row.getCell(0);
                    Object v=FileUtil.getCellFormatValue(c);
                    list.add(String.valueOf(v));
                    System.out.println(v);
                }
            }
        }
    }
}