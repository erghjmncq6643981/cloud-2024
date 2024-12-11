/*
 * chandler-file-test
 * 2024/12/3 19:16
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.harbour;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.chandler.test.example.harbour.excel.JarAnalyzeExcel;
import com.chandler.test.example.harbour.entity.JarAnalyzeBO;
import org.apache.commons.compress.utils.Lists;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/3 19:16
 * @version 1.0.0
 * @since 1.8
 */
public class MavenTree {


    public static void main(String[] args) {
        String filePath = "/Users/chandler/Downloads/tree.txt";

        List<JarAnalyzeBO> bos = Lists.newArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            Integer seq = 0;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("[INFO] +") || line.startsWith("[INFO] |")) {
                    seq++;
                    String block = line.substring("[INFO] ".length());
                    String[] strs = block.split("- ");
                    //jar名称和作用域
                    String suffix = strs[1];
                    String[] suffixStrs = suffix.split(":");
                    String groupId = suffixStrs[0];
                    //jar名称
                    String name = suffixStrs[1];
                    //类型
                    String type = suffixStrs[2];
                    //版本
                    String version = suffixStrs[3];
                    //作用域
                    String scope = suffixStrs[4];

                    JarAnalyzeBO jarAnalyze = new JarAnalyzeBO();
                    jarAnalyze.setSeq(seq);
                    jarAnalyze.setName(name);
                    jarAnalyze.setGroupId(groupId);
                    jarAnalyze.setType(type);
                    jarAnalyze.setVersion(version);
                    jarAnalyze.setScope(scope);
                    //表示层级
                    String prefix = strs[0];
                    jarAnalyze.setPrefix(prefix);
                    Integer parent = 0;
                    Integer level;
                    Boolean haveChildren = Boolean.FALSE;
                    if (prefix.equals("+")) {
                        //+表示第一级
                        level = 1;
                    } else {
                        //表示这是最后一个依赖的jar，
                        // 与上一个数据层级相同
                        // 与上一个数据层级不相同
                        level = prefix.substring(1, prefix.length() - 1).length() / 2+1;
                        JarAnalyzeBO last = bos.get(bos.size() - 1);
                        if (level.equals(last.getLevel())) {
                            parent = last.getParent();
                        } else {
                            parent = last.getSeq();
                            last.setHaveChildren(Boolean.TRUE);
                        }
                    }
                    jarAnalyze.setParent(parent);
                    jarAnalyze.setLevel(level);
                    jarAnalyze.setHaveChildren(haveChildren);
                    bos.add(jarAnalyze);
                }
            }
            bos.forEach(a -> System.out.println(a));
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<JarAnalyzeExcel> analyzeBOS = bos.stream().map(b->{
            JarAnalyzeExcel analyze=new JarAnalyzeExcel();
            analyze.set序号(b.getSeq());
            if(0!=b.getParent()){
                analyze.set父JAR(bos.stream().filter(p->p.getSeq().equals(b.getParent())).findFirst().get().getName());
            }
            analyze.set层级(b.getLevel());
            analyze.set是否有子JAR(b.getHaveChildren());
            analyze.setJAR名称(b.getName());
            analyze.setGroupId(b.getGroupId());
            analyze.setJAR类型(b.getType());
            analyze.set版本(b.getVersion());
            analyze.setScope(b.getScope());
            return analyze;
        }).collect(Collectors.toList());
        try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
            excelWriter.write(analyzeBOS);
            FileOutputStream fos =
                    new FileOutputStream("/Users/chandler/Downloads/harbour依赖分析结果.xlsx");
            excelWriter.flush(fos);
            fos.close();
        } catch (Exception e) {
            System.out.println("发生异常！");
        }
    }
}