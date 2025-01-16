/*
 * chandler-mybatisplus
 * 2025/1/16 14:33
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.instance.test.domain.dataobject;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/1/16 14:33
 * @version 1.0.0
 * @since 1.8
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BaseDO extends AuditDO {

    @TableLogic(value = "0", delval = "1")
    private Boolean logicDelete;

}