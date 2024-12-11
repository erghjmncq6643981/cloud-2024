/*
 * chandler-file-test
 * 2024/12/10 21:00
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.folder;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.chandler.test.example.folder.entity.FileInfo;
import org.apache.commons.compress.utils.Lists;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/10 21:00
 * @version 1.0.0
 * @since 1.8
 */
public class ReadFolder {
    public static void getFiles(File folder,List<FileInfo> infos){
        File[] files = folder.listFiles(); // 获取文件夹下的所有文件
        for (File file : files) {
            if (file.isFile()) {
                FileInfo info=ReadFile.getFileInfo(file);
                if(Objects.nonNull(info)&&(Objects.equals("@Service",info.getAnnotation())||Objects.equals("@Component",info.getAnnotation()))){
                    infos.add(info);
                    System.out.println(String.format("File:%s ; length: %s KB" ,file.getName(), file.length()/1000));
                }
            } else if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                getFiles(file,infos);
            }
        }
    }

    public static void main(String[] args) {
        File folder = new File("/Users/chandler/Documents/repository/gitlab-yazuishou/harbour-server/src/main/java/com/eshipping"); // 指定文件夹路径
        List<FileInfo> infos=Lists.newArrayList();
        getFiles(folder, infos);
        infos=infos.stream().filter(i->!i.getServices().isEmpty()).sorted(Comparator.comparing(FileInfo::getLength).reversed()).toList();
        try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
            excelWriter.write(infos);
            FileOutputStream fos =
                    new FileOutputStream("/Users/chandler/Downloads/service的依赖信息.xlsx");
            excelWriter.flush(fos);
            fos.close();
        } catch (Exception e) {
            System.out.println("发生异常！");
        }
        List<FileInfo> finalInfos = infos;
        List<String> dependInfos=Lists.newArrayList();
        infos.stream().filter(i->!i.getServices().isEmpty()).forEach(i->{
            List<String> dependInfo= finalInfos.stream()
                    .filter(other->!other.getName().equals(i.getName())&&other.getServices().contains(i.getName()))
                    .map(FileInfo::getName)
                    .toList();
            i.getServices().stream().filter(dependInfo::contains).forEach(d-> dependInfos.add(String.format("循环依赖！A：%s；B：%s%n",i.getName(),d)));
        });
        dependInfos.forEach(System.out::println);
        System.out.println(dependInfos.size());
    }
}