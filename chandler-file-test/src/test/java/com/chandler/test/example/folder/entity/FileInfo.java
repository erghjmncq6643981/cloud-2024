/*
 * chandler-file-test
 * 2024/12/10 21:09
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.folder.entity;

import lombok.Data;

import java.util.List;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/10 21:09
 * @version 1.0.0
 * @since 1.8
 */
@Data
public class FileInfo {
    private String name;
    private long length;
    private String path;
    private String annotation;
    private String className;
    private Integer serviceNumber;
    private Integer mapperNumber;
    private Integer daoNumber;
    private List<String> services;
    private List<String> mappers;
    private List<String> daos;

}