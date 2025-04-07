/*
 * duckbill-data-sync-server-backend
 * 2025/3/29 12:16
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.concurrent;

import lombok.*;

/**
 * 类功能描述
 *
 * @version 1.0.0
 * @author 钱丁君-chandler 2025/3/29 12:16
 * @since 1.8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DataCache {
    Long dataId;
    Long id;
    Long cacheTime;
}
