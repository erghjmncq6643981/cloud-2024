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

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
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
    private static ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    private static ScheduledExecutorService scheduledExecutor2 = new ScheduledThreadPoolExecutor(1);

    public static void main(String[] args) {
//        scheduledExecutor.scheduleAtFixedRate(()->{
//            log.info("orderDocument调用开始");
//            Request request = null;
//            try {
//                URI uri =
//                        new URIBuilder(new URI("https://api.carrierglobe.com/duckbill-data-query-server/workBench/compensate/orderDocument"))
//                                .addParameter("startTime","2025-05-15 00:00:00")
//                                .addParameter("endTime","2025-05-17 00:00:00")
//                                .build();
//                request = Request.get(uri);
//                Response response = request.execute();
//                String resultContent = response.returnContent().asString();
//                log.info("orderDocument调用成功！resultContent:{}", resultContent);
//            } catch (URISyntaxException e) {
//                log.error("httpclient failure!", e);
//                throw new RuntimeException(e);
//            }catch (IOException e) {
//                log.error(
//                        "httpclient failure！request：{}； ", request, e);
//            }
//        }, 0, 5, TimeUnit.MINUTES);

        scheduledExecutor2.scheduleAtFixedRate(()->{
            log.info("调度面板，调用开始");
            Request request = null;
            try {
                URI uri =
                        new URIBuilder(new URI("https://api.carrierglobe.com/duckbill-data-query-server/task/compensate/dispatch"))
                                .addParameter("startTime","2025-05-16 00:00:00")
                                .addParameter("endTime","2025-05-18 00:00:00")
                                .build();
                request = Request.get(uri);
                Response response = request.execute();
                String resultContent = response.returnContent().asString();
                log.info("调度面板，调用成功！resultContent:{}", resultContent);
            } catch (URISyntaxException e) {
                log.error("httpclient failure!", e);
                throw new RuntimeException(e);
            }catch (IOException e) {
                log.error(
                        "httpclient failure！request：{}； ", request, e);
            }
            try {
                URI uri =
                        new URIBuilder(new URI("https://api.carrierglobe.com/duckbill-data-query-server/task/compensate/dispatch"))
                                .addParameter("startTime","2025-05-18 00:00:00")
                                .addParameter("endTime","2025-06-18 00:00:00")
                                .build();
                request = Request.get(uri);
                Response response = request.execute();
                String resultContent = response.returnContent().asString();
                log.info("调度面板，调用成功！resultContent:{}", resultContent);
            } catch (URISyntaxException e) {
                log.error("httpclient failure!", e);
                throw new RuntimeException(e);
            }catch (IOException e) {
                log.error(
                        "httpclient failure！request：{}； ", request, e);
            }
        }, 1, 20, TimeUnit.MINUTES);
    }
}