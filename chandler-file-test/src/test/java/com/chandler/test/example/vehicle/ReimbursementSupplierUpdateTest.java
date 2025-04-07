/*
 * chandler-file-test
 * 2025/4/3 10:41
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
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/4/3 10:41
 * @version 1.0.0
 * @since 1.8
 */
public class ReimbursementSupplierUpdateTest {
    public static void main(String[] args) {
        // 文件路径
        String fileName = "/Users/chandler/Downloads/特殊费用脏数据处理.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1); // 0表示第一个sheet的索引，也可以使用sheet的名称作为参数
        String template = "update supplier_bill \n" +
                "set `bank_card_num` ='%s',`deposit_bank` ='%s',`last_update_time` =NOW() \n" +
                "where id=%s and `settlement_id` ='%s';";
        List<String> jsonStrings = Lists.newArrayList();
        // 输出读取到的数据
        for (List<Object> row : read) {
            String sql=template.formatted(row.get(3),row.get(5),row.get(0),row.get(1));
            System.out.println(sql);
        }
    }
}