/*
 * chandler-file-test
 * 2025/3/1 09:55
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
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/3/1 09:55
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class HttpClient {
    public static void main(String[] args) {
        String jsonString = "{\n" +
                "    \"id\": 37275,\n" +
                "    \"status\": \"RECORDED\"\n" +
                "}";
        Request request = null;
        try {
            URI uri =
                    new URIBuilder(new URI("https://new.carrierglobe.com/ws-truck/driver/monthly/bill/updateMonthlyBill"))
                            .build();
            request =
                    Request.post(uri)
                            .setHeader(new BasicHeader("Content-Type", "application/json"))
                            .setHeader(new BasicHeader("Accept", "application/json; charset=UTF-8"))
                            .setHeader(new BasicHeader("X-Requested-By", "1"))
                            .bodyString(jsonString, ContentType.parse("UTF-8"));
            Response response = request.execute();
            String resultContent = response.returnContent().asString();
            log.info("调用成功！jsonString:{},resultContent:{}",jsonString, resultContent);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            log.error(
                    "httpclient failure！request：{}； ", request, e);
        }
    }
}