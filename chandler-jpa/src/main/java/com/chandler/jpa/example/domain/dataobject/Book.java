/*
 * chandler-jpa
 * 2024/12/25 15:04
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.jpa.example.domain.dataobject;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/25 15:04
 * @version 1.0.0
 * @since 1.8
 */
@Entity
@Data
public class Book extends BaseVersionEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
}