/*
 * chandler-file-test
 * 2024/12/10 21:11
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.folder;

import com.chandler.test.example.folder.entity.FileInfo;
import org.apache.commons.compress.utils.Lists;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/12/10 21:11
 * @version 1.0.0
 * @since 1.8
 */
public class ReadFile {

    public static FileInfo getFileInfo(File file){
        if (!file.exists()) {
            return null;
        }
        String className = file.getName();
        List<String> mappers = Lists.newArrayList();
        List<String> daos = Lists.newArrayList();
        List<String> services = Lists.newArrayList();
        FileInfo info = new FileInfo();
        //java文件才需要解析
        if(file.getName().contains(".java")){
            info.setName(file.getName().split("\\.")[0]);
            info.setLength(file.length()/1000);
            try (FileInputStream fis = new FileInputStream(file);
                 InputStreamReader isr = new InputStreamReader(fis);
                 BufferedReader br = new BufferedReader(isr)) {

                String line;
                while ((line = br.readLine()) != null) {
                    //@Service @Component 才需要解析
                    if (line.contains("@Service")||line.contains("@Component")) {
                        Optional<String> optional = Arrays.stream(line.split(" ")).filter(str -> str.contains("@")).findAny();
                        info.setAnnotation(optional.get());
                    }
                    if (line.startsWith("package ")) {
                        Optional<String> optional = Arrays.stream(line.split(" ")).filter(str -> str.contains("com.eshipping")).findAny();
                        info.setPath(optional.get().replace(";", ""));
                        className = info.getPath() + "." + className;
                        info.setClassName(className);
                    }
                    if ((line.contains("Autowired") || line.contains("Resource")) && !line.contains("//")) {
                        line = br.readLine();
                        if (line.contains("Service")) {
                            Optional<String> optional = Arrays.stream(line.split(" ")).filter(str -> str.contains("Service")).findAny();
                            optional.ifPresent(services::add);
                        } else if (line.contains("Mapper")) {
                            Optional<String> optional = Arrays.stream(line.split(" ")).filter(str -> str.contains("Mapper")).findAny();
                            optional.ifPresent(mappers::add);
                        } else if (line.contains("Dao")) {
                            Optional<String> optional = Arrays.stream(line.split(" ")).filter(str -> str.contains("Dao")).findAny();
                            optional.ifPresent(daos::add);
                        }
                    }
                }
                info.setMappers(mappers);
                info.setMapperNumber(mappers.size());
                info.setServices(services);
                info.setServiceNumber(services.size());
                info.setDaos(daos);
                info.setDaoNumber(daos.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       return info;
    }
    public static void main(String[] args) {
        File file = new File("/Users/chandler/Documents/repository/gitlab-yazuishou/harbour-server/src/main/java/com/eshipping/order/service/OrderService.java");
        getFileInfo(file);
    }
}