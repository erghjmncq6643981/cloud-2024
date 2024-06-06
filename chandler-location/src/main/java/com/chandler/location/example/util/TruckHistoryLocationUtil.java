/*
 * chandler-location
 * 2024/3/18 11:05
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.util;

import cn.hutool.core.collection.CollectionUtil;
import com.chandler.location.example.entity.dto.SingleResponse;
import com.chandler.location.example.entity.dto.TruckHistoryLocationResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/18 11:05
 * @since 1.8
 */
@Slf4j
public class TruckHistoryLocationUtil {
    private static final String HISTORY_LOCATION_URL =
            "https://api.carrierglobe.com/ttp-web/front/truck/location/history?truckPlateNumber=%s&beginTime=%s&endTime=%s&needStopPoint=%s";

    public static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJ2ZXJzaW9uIjoiVjMwIiwiYWxnIjoiSFM1MTIifQ.eyJzdWIiOiIyMzI4NTIwODE0NTE2MjI0NiIsImp0aSI6IjI0MTg1NjcxNjYxMTE2OTMwNyIsImlzcyI6Ijk1MTg1NjY2NTExMTk2NDMiLCJpYXQiOjE3MTIwMTg2NjQsImV4cCI6MTcxMjA1NDY2NCwiYXVkIjoiMTI2NDQ4MjYxMDEyNTE0ODM0IiwiciI6IuW1vVx1RDg2OVx1REM4Q1x1RDg0QVx1REVCOOaHgyIsIm5pY2siOiLpkrHkuIHlkJsiLCJpbmRlbnRpdHkiOjIzMjg1MjA4MTQ1MTYyMjQ4LCJvcmdSb290SWQiOjEwMjk5NzIyNDE4MDY5NTA0LCJodWlkIjoicWRqMTM5OSIsImNsaWVudCI6IiJ9.51cxklbcMNauGlD1g56a0FRLwieErNgdAjYzJnrFbXUzjFFLzPcpuGXKbSp-4YH0yzJFHxzvRC-cQ6mxReu-Yg";

    private static final RestTemplate restTemplate = new RestTemplate();

    public static TruckHistoryLocationResp queryTruckHistoryLocation(
            String lpn, String beginTime, String endTime, String appId) {
        try {
            LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
            param.add("X_APP_ID", appId);
            param.add("Authorization", TOKEN);

            String historyLocationRequestUrl =
                    String.format(HISTORY_LOCATION_URL, lpn, beginTime, endTime,Boolean.TRUE);
            HttpEntity<Object> httpEntity = new HttpEntity<>(param);
            ResponseEntity<SingleResponse<TruckHistoryLocationResp>> response =
                    restTemplate.exchange(
                            historyLocationRequestUrl,
                            HttpMethod.GET,
                            httpEntity,
                            new ParameterizedTypeReference<SingleResponse<TruckHistoryLocationResp>>() {});
            if (response.getStatusCode().is2xxSuccessful()) {
                if (response.getBody() == null) {
                    throw new IllegalStateException("响应体为空");
                }
                if (response.getBody().isSuccess()
                        && Objects.nonNull(response.getBody().getData())
                        && CollectionUtil.isNotEmpty(response.getBody().getData().getLocations())) {
                    return response.getBody().getData();
                }
                log.info(
                        "车牌号:{}, 起止时间:{}, appId:{}, 查询失败, 原因:{}",
                        lpn,
                        beginTime + "~" + endTime,
                        appId,
                        response.getBody().getErrMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TruckHistoryLocationResp resp = new TruckHistoryLocationResp();
        resp.setLocations(Collections.emptyList());
        return resp;
    }

}