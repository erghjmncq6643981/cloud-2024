/*
 * chandler-file-test
 * 2025/6/16 10:27
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
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 商车与人车之间的 接单人信息比对
 *
 * @author 钱丁君-chandler 2025/6/16 10:27
 * @version 1.0.0
 * @since 1.8
 */
public class TransportDriverInfoRepairTest {
    public static void main(String[] args) {
        // 文件路径
        String fileName = "/Users/chandler/Downloads/执行结果10.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<TransportDriverInfo> data = Lists.newArrayList();
        for (List<Object> row : read) {
            String id = (String) row.get(0);
            String memberShipId = (String) row.get(1);
            String name = (String) row.get(7);
            String phoneNumber = (String) row.get(8);
            String cardId = (String) row.get(9);
            String isActive = (String) row.get(10);
            String cardIdFrontUrl = (String) row.get(11);
            String cardIdBackUrl = (String) row.get(12);
            if (isActive.equals("1") && StringUtils.isNotBlank(cardId)) {
                TransportDriverInfo driverInfo = TransportDriverInfo.builder()
                        .id(id)
                        .memberShipId(memberShipId)
                        .name(name)
                        .phoneNumber(phoneNumber)
                        .cardId(cardId)
                        .isActive(Boolean.getBoolean(isActive))
                        .cardIdFrontUrl(cardIdFrontUrl)
                        .cardIdBackUrl(cardIdBackUrl)
                        .build();
                data.add(driverInfo);
//                System.out.println(driverInfo);
            }
        }

        String template1 = "update transport_capacity_member\n" +
                "set name='%s', mobile_phone='%s', id_card_number='%s', id_card_front_page='%s', id_card_back_page='%s', updated_time=now()\n" +
                "where membership_id='%s';";
//        String template2 = "update transport_driver\n" +
//                "set name='%s', mobile_phone='%s', id_card_number='%s', updated_time=now()\n" +
//                "where membership_id='%s';";
        data.forEach(di -> {
            System.out.println(String.format(template1, di.getName(), di.getPhoneNumber(), di.getCardId(), di.getCardIdFrontUrl(), di.getCardIdBackUrl(),
                    di.getMemberShipId()));
        });
//        System.out.println("<--------->");
//        data.forEach(di->{
//            System.out.println(String.format(template2,di.getName(),di.getPhoneNumber(),di.getCardId(),
//                    di.getMemberShipId()));
//        });
    }
}