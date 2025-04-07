/*
 * chandler-file-test
 * 2025/3/1 09:32
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.vehicle.excel;

import lombok.Data;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/3/1 09:32
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class MonthlyBill {
    Long id;
    String create_by;
    String create_time;
    String last_update_by;
    String last_update_time;
    String trace_id;
    String logic_delete;
    String object_version;
    String taker_id;
    String bill_month;
    String bill_status;
    String unfinished_reason;
    String transport_company_id;
    String transport_company_name;
    String unsettled_amount;
}