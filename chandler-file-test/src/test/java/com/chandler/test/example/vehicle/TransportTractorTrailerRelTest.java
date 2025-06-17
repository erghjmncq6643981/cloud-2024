/*
 * chandler-file-test
 * 2025/6/16 14:16
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
import com.chandler.test.example.vehicle.excel.TransportDriverInfo;
import com.chandler.test.example.vehicle.excel.TransportTractorTrailerRel;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/6/16 14:16
 * @version 1.0.0
 * @since 1.8
 */
public class TransportTractorTrailerRelTest {
    public static void main(String[] args) {
        // 文件路径
        String fileName = "/Users/chandler/Downloads/缺少车挂绑定关系的车辆信息.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<TransportTractorTrailerRel> data = Lists.newArrayList();
        for (List<Object> row : read) {
            String tractorId = (String) row.get(0);
            String trailerId = (String) row.get(2);
            String createdTime = (String) row.get(5);
            TransportTractorTrailerRel rel = TransportTractorTrailerRel.builder()
                    .tractorId(tractorId)
                    .trailerId(trailerId)
                    .createdTime(createdTime.substring(0, createdTime.length() - 2))
                    .build();
            data.add(rel);
//            System.out.println(rel);

        }
        String template = "insert into transport_tractor_trailer_rel\n" +
                "(tractor_id,trailer_id,binding_time,created_by,creation_time,updated_by,last_update_time)\n" +
                "values";
        String template1 = "(%s,%s,'%s','钱丁君',now(),'钱丁君',now())";
        StringBuilder builder = new StringBuilder(template);
        for (int i = 0; i < 600; i++) {
            builder.append(String.format(template1, data.get(i).getTractorId(), data.get(i).getTrailerId(), data.get(i).getCreatedTime()));
            builder.append(",\n");
        }
        System.out.println(builder);

        builder = new StringBuilder(template);
        for (int i = 600; i < 1200; i++) {
            builder.append(String.format(template1, data.get(i).getTractorId(), data.get(i).getTrailerId(), data.get(i).getCreatedTime()));
            builder.append(",\n");
        }
        System.out.println(builder.toString());
        builder = new StringBuilder(template);
        for (int i = 600; i < data.size(); i++) {
            builder.append(String.format(template1, data.get(i).getTractorId(), data.get(i).getTrailerId(), data.get(i).getCreatedTime()));
            builder.append(",\n");
        }
        System.out.println(builder.toString());
    }
}