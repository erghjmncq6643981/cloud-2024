/*
 * chandler-file-test
 * 2025/3/1 09:28
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.fluent.Response;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicHeader;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 批量修改月账单状态
 *
 * @author 钱丁君-chandler 2025/3/1 09:28
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class MonthlyBillTest {
    public static void main(String[] args) {
        // 文件路径
        String fileName = "/Users/chandler/Downloads/执行结果4.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1); // 0表示第一个sheet的索引，也可以使用sheet的名称作为参数
        String template = "{\n" +
                "    \"id\": %s,\n" +
                "    \"status\": \"%s\"\n" +
                "}";
        List<String> jsonStrings = Lists.newArrayList();
        // 输出读取到的数据
        for (List<Object> row : read) {
            String jsonString = String.format(template, row.get(0), row.get(10));
            if (!"WAIT_MAKE_BILL".equals(row.get(10))) {
                System.out.println(jsonString);
                jsonStrings.add(jsonString);
            }
        }

        // 关闭reader
        reader.close();
        jsonStrings.forEach(jsonString -> {
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
        });

    }
}