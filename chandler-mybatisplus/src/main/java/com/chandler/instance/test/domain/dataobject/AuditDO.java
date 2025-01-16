/*
 * chandler-mybatisplus
 * 2025/1/16 14:32
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.instance.test.domain.dataobject;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/1/16 14:32
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class AuditDO implements Serializable {

//    @TableField(fill = FieldFill.INSERT)
    private String createBy;

//    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String lastUpdateBy;

//    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime lastUpdateTime;

    @Version
    private Integer objectVersion;
}