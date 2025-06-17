/*
 * chandler-file-test
 * 2025/3/30 10:41
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.vehicle;

import com.chandler.instance.client.example.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/3/30 10:41
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class DataSyncClient {
    private static final String prefix = "https://api.carrierglobe.com/duckbill-data-query-server";
    private static ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private static ScheduledExecutorService scheduledExecutor2 = new ScheduledThreadPoolExecutor(1);
    private static Integer delay1 = 0;
    private static Integer delay2 = 0;

    private static boolean scheduleTask(String suffix, String startTime, String endTime) {
        Request request = null;
        try {
            URI uri =
                    new URIBuilder(new URI(prefix + suffix))
                            .addParameter("startTime", startTime)
                            .addParameter("endTime", endTime)
                            .build();
            request = Request.get(uri);
            Response response = request.execute();
            String resultContent = response.returnContent().asString();
            return Boolean.TRUE;
        } catch (URISyntaxException e) {
            log.error("httpclient failure!", e);
        } catch (IOException e) {
            log.error(
                    "httpclient failure！request：{}； ", request, e);
        }
        return Boolean.FALSE;
    }

    public static void main(String[] args) {
//        scheduledExecutor.scheduleAtFixedRate(() -> {
//            if (delay1 >= checkTimeRange()) {
//                log.info("orderDocument调用开始");
//                LocalDate localDate = LocalDate.now();
//                String startTime = String.format("%s %s", DateUtil.getTodayDate(localDate), "00:00:00");
//                String endTime = String.format("%s %s", DateUtil.getTodayDate(localDate.plusDays(1)), "00:00:00");
//                if (scheduleTask("/workBench/compensate/orderDocument", startTime, endTime)) {
//                    log.info("orderDocument调用成功！startTime:{};endTime:{}", startTime, endTime);
//                }
//                delay1 = 0;
//            }
//            delay1++;
//        }, 0, 15, TimeUnit.MINUTES);

        scheduledExecutor2.scheduleAtFixedRate(() -> {
            if (delay2 >= checkTimeRange()) {
                log.info("调度面板，调用开始");
                LocalDate localDate = LocalDate.now();
                String startTime = String.format("%s %s", DateUtil.getTodayDate(localDate), "00:00:00");
                String endTime = String.format("%s %s", DateUtil.getTodayDate(localDate.plusDays(2)), "00:00:00");
                if (scheduleTask("/task/compensate/dispatch", startTime, endTime)) {
                    log.info("调度面板，调用成功！startTime:{};endTime:{}", startTime, endTime);
                }
                try {
                    Thread.sleep(30*1000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                String nextMonthTime = String.format("%s %s", DateUtil.getTodayDate(localDate.plusDays(2).plusMonths(1)), "00:00:00");
                if (scheduleTask("/task/compensate/dispatch", endTime, nextMonthTime)) {
                    log.info("调度面板，调用成功！startTime:{};endTime:{}", endTime, nextMonthTime);
                }
                delay2 = 0;
            }
            delay2++;
        }, 1, 10, TimeUnit.MINUTES);
    }

    public static int checkTimeRange() {
        // 获取当前小时（24小时制，0-23）
        int currentHour = LocalTime.now().getHour();

        // 判断时间段
        if (currentHour >= 19 || currentHour <= 8) {   // 19:00-次日08:59
            return 4;
        } else if ((currentHour >= 9 && currentHour <= 11) || (currentHour >= 13 && currentHour <= 16)) {
            return 2;  // 09:00-11:59 或 13:00-16:59
        } else {
            return 3;  // 其他时间（12:00-12:59 和 17:00-18:59）
        }
    }
}