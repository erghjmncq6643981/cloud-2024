/*
 * chandler-location
 * 2024/3/18 13:41
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.test;

import com.chandler.location.example.entity.dto.TruckHistoryLocationResp;
import com.chandler.location.example.util.TruckHistoryLocationUtil;

import java.text.ParseException;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/18 13:41
 * @since 1.8
 */
public class Test01 {
    public static void main(String[] args) throws ParseException {
        TruckHistoryLocationResp resp = TruckHistoryLocationUtil.queryTruckHistoryLocation("沪EG6292",
                "2024-03-06 18:10:27", "2024-03-09 18:10:27", "ttp-user-merge");
        resp.getLocations().forEach(l-> System.out.println(l));
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date startTime;
//        startTime=sdf.parse("2024-03-08 18:10:27");
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime.getTime()), ZoneId.systemDefault());
//        System.out.println(localDateTime.minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}