/*
 * chandler-location
 * 2024/3/18 17:22
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.service;

import com.chandler.location.example.domain.dataobject.Location;
import com.chandler.location.example.domain.dataobject.Task;
import com.chandler.location.example.domain.mapper.LocationMapper;
import com.chandler.location.example.domain.mapper.StoppingPointMapper;
import com.chandler.location.example.domain.mapper.TaskMapper;
import com.chandler.location.example.entity.*;
import com.chandler.location.example.entity.dto.AmapGeoRespDTO;
import com.chandler.location.example.exception.BusinessException;
import com.chandler.location.example.util.DateUtil;
import com.chandler.location.example.util.FileUtil;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/3/18 17:22
 * @since 1.8
 */
@Slf4j
@Service
public class TaskService implements InitializingBean {
    private String startTime = "2024-03-19 00:00:00";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<List<LocalDateTime>> times = Lists.newArrayList();
    ScheduledExecutorService scheduledExecutor = new ScheduledThreadPoolExecutor(1);
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private LocationMapper locationMapper;
    @Resource
    private StoppingPointMapper stoppingPointMapper;

    public void addTasks() {
        Workbook wb = null;
        Sheet sheet = null;
        Row row = null;
        String filePath = "/Users/chandler/Downloads/甬舟提空进重清单.xlsx";
        wb = FileUtil.readExcel(filePath);
        if (wb != null) {
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rowNum = sheet.getPhysicalNumberOfRows();
            //获取第一行
//            row = sheet.getRow(0);
            //获取最大列数
            for (int i = 1; i < rowNum; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    Cell c1 = row.getCell(1);
                    Object v1 = FileUtil.getCellFormatValue(c1);
                    Cell c2 = row.getCell(2);
                    Object v2 = FileUtil.getCellFormatValue(c2);
                    Cell c3 = row.getCell(3);
                    Object v3 = FileUtil.getCellFormatValue(c3);
                    Cell c4 = row.getCell(4);
                    Object v4 = FileUtil.getCellFormatValue(c4);
                    Task t = new Task();
                    t.setLpn(String.valueOf(v1));
                    t.setSecondLpn(String.valueOf(v2));
                    t.setStartTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(DateUtil.stringToDay(String.valueOf(v3)).getTime()), ZoneId.systemDefault()));
                    t.setEndTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(DateUtil.stringToDay(String.valueOf(v4)).getTime()), ZoneId.systemDefault()));
                    t.setStatus("start");
                    if (!t.getLpn().equalsIgnoreCase(t.getSecondLpn())) {
                        t.setStatus("disabled");
                    }
                    Task old = taskMapper.selectExsit(t);
                    if (Objects.isNull(old)) {
                        taskMapper.insert(t);
                    } else {
                        old.setSecondLpn(String.valueOf(v2));
                        if (!old.getLpn().equalsIgnoreCase(old.getSecondLpn())) {
                            old.setStatus("disabled");
                        }
                        taskMapper.updateByPrimaryKey(old);
                    }
                }
            }
        }
    }
    private RestTemplate restTemplate = new RestTemplate();
    String url = "https://restapi.amap.com/v3/geocode/regeo?key=%s&location=%s";

    private String getLocationFromGeo(String location) {
        String key = "9b319af76c651cb8087461fd9883ca20";
        LinkedMultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        HttpEntity<Object> httpEntity = new HttpEntity<>(param);
        ResponseEntity<AmapGeoRespDTO> response =
                restTemplate.exchange(
                        String.format(url, key, location),
                        HttpMethod.GET,
                        httpEntity,
                        new ParameterizedTypeReference<>() {
                        });
        return response.getBody().getRegeocode().getAddress();
    }

//    private void taskHandle() {
//        Task task = taskMapper.selectTask();
//        TruckHistoryLocationResp resp = TruckHistoryLocationUtil.queryTruckHistoryLocation(task.getLpn(),
//                task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
//                task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "ttp-user-merge");
//        log.info("处理task:{}", task);
//        if (Boolean.FALSE.equals(CollectionUtils.isEmpty(resp.getLocations()))) {
////            List<Location> locations=
//            resp.getLocations()
//                    .stream()
//                    .map(l -> {
//                        Location location = new Location();
//                        location.setLpn(task.getLpn());
//                        location.setLatitude(String.valueOf(l.toLocation().getLatitude()));
//                        location.setLongitude(String.valueOf(l.toLocation().getLongitude()));
//                        try {
//                            location.setTime(sdf.parse(l.getTime()));
//                        } catch (ParseException e) {
//                            throw new RuntimeException(e);
//                        }
//                        location.setDirection(l.getDirection());
//
//                        com.chandler.location.example.entity.Location demo = new com.chandler.location.example.entity.Location();
//                        demo.setLongitude(Double.parseDouble(location.getLongitude()));
//                        demo.setLatitude(Double.parseDouble(location.getLatitude()));
//                        Boolean r = cache.isInPolygon(demo);
//                        location.setInPolygon(Boolean.TRUE.equals(r) ? 1 : 2);
////                        log.info("获取location:{}",location);
//                        return location;
//                    })
//                    .filter(l -> 1 == l.getInPolygon())
//                    .forEach(l -> locationMapper.insert(l));
////            locationMapper.batchSave(locations);
//        }
//        task.setStatus("done");
//        taskMapper.updateByPrimaryKey(task);
//    }
//
//    @Resource
//    private LocationCache cache;
//
//    private void parseLocation() {
//        try {
//            List<Location> locations = locationMapper.selectTarget(0);
//            locations.forEach(l -> {
//                com.chandler.location.example.entity.Location demo = new com.chandler.location.example.entity.Location();
//                demo.setLongitude(Double.parseDouble(l.getLongitude()));
//                demo.setLatitude(Double.parseDouble(l.getLatitude()));
//                Boolean r = cache.isInPolygon(demo);
//                l.setInPolygon(Boolean.TRUE.equals(r) ? 1 : 2);
////                log.info("{} {}中山市", l, Boolean.TRUE.equals(r) ? "在" : "不在");
//                locationMapper.updateByPrimaryKey(l);
//            });
//            log.info("已处理{}条", locations.size());
//        } catch (Exception e) {
//            log.error("发生异常", e);
//        }
//    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        Date first;
//        first = sdf.parse(startTime);
//        LocalDateTime start = LocalDateTime.ofInstant(Instant.ofEpochMilli(first.getTime()), ZoneId.systemDefault());
//        for (int i = 0; i < 60; i++) {
//            List<LocalDateTime> duration = Lists.newArrayList();
//            duration.add(depCopy(start));
//            LocalDateTime end = start.minusDays(3);
//            duration.add(depCopy(end));
//            start = end;
//            times.add(duration);
//        }
//        初始化车牌数据
//        addTasks();
//        scheduledExecutor.scheduleAtFixedRate(()->{
//            try {
//                addTasks();
//            }catch (Exception e){
//                log.error("Error while scheduling",e);
//            }
//        }, 3, 30, TimeUnit.SECONDS);

    }

    public List<Cluster> test(Integer fetchSize, Integer eps, Integer minPts) {
        List<Location> locations = locationMapper.selectALL(fetchSize);
        List<GeoPoint> geoPoints = locations.stream().map(l -> {
            GeoPoint p = new GeoPoint();
            p.setLatitude(Double.parseDouble(l.getLatitude()));
            p.setLongitude(Double.parseDouble(l.getLongitude()));
            return p;
        }).collect(Collectors.toList());
        DBSCAN d = new DBSCAN(geoPoints, eps, minPts);
        List<Cluster> clusterList = d.dbscan();
        return clusterList;
    }

    public static <T> T depCopy(T src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(src);

            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream inStream = new ObjectInputStream(byteIn);
            return (T) inStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new BusinessException(e);
        }
    }
}