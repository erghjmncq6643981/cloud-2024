/*
 * chandler-location
 * 2024/4/2 11:44
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
import com.chandler.location.example.domain.dataobject.StoppingPoint;
import com.chandler.location.example.domain.dataobject.Task;
import com.chandler.location.example.domain.mapper.LocationMapper;
import com.chandler.location.example.domain.mapper.StoppingPointMapper;
import com.chandler.location.example.domain.mapper.TaskMapper;
import com.chandler.location.example.entity.dto.AmapGeoRespDTO;
import com.chandler.location.example.entity.dto.TruckLocationStopPointResp;
import com.chandler.location.example.util.DateUtil;
import com.chandler.location.example.util.ElectronicFenceUtils;
import com.chandler.location.example.util.FileUtil;
import com.chandler.location.example.util.TruckHistoryLocationUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/2 11:44
 * @since 1.8
 */
@Slf4j
@Service
public class DoorPointService implements InitializingBean {
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
                    t.setStatus(t.getLpn().equalsIgnoreCase(t.getSecondLpn()) ? "start" : "disabled");
                    Task old = taskMapper.selectExsit(t);
                    if (Objects.isNull(old)) {
                        taskMapper.insert(t);
                    } else {
                        if (Objects.isNull(old.getSecondLpn())) {
                            old.setSecondLpn(t.getSecondLpn());
                            taskMapper.updateByPrimaryKey(old);
                        }
                    }
                }
            }
        }
    }

    public void taskHandle(Task task) {
        List<Task> tasks = taskMapper.selectTasks(task);
        tasks.forEach(this::getTruckHistoryLocation);
    }

    private void getTruckHistoryLocation(Task task) {
        if ("done".equalsIgnoreCase(task.getStatus())) {
            return;
        } else if ("start".equalsIgnoreCase(task.getStatus())) {
            //修改状态
            task.setStatus("doing");
            taskMapper.updateByPrimaryKey(task);
        }
        CompletableFuture.supplyAsync(() -> TruckHistoryLocationUtil.queryTruckHistoryLocation(task.getLpn(),
                        task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                        task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), "ttp-user-merge"))
                .thenApply(r -> {
                            //停留点
                            List<TruckLocationStopPointResp> stopPoints = r.getStopPoints();
                            if (Boolean.FALSE.equals(CollectionUtils.isEmpty(stopPoints))) {
                                stoppingPointMapper.batchSave(stopPoints.stream().map(sp -> {
                                    StoppingPoint p = new StoppingPoint();
                                    BeanUtils.copyProperties(sp, p);
                                    p.setTaskId(task.getId());
                                    p.setLonAndLat(sp.getLonAndLat());
                                    p.setStartTime(DateUtil.stringToDay(sp.getBeginTime()));
                                    p.setEndTime(DateUtil.stringToDay(sp.getEndTime()));
                                    return p;
                                }).toList());
                            }
                            return r;
                        }
                )
                .thenApply(r -> {
                    //GPS点
                    List<Location> locations = r.getLocations()
                            .stream()
                            .map(l -> {
                                Location location = new Location();
                                location.setTaskId(task.getId());
                                location.setLpn(task.getLpn());
                                location.setLatitude(String.valueOf(l.toLocation().getLatitude()));
                                location.setLongitude(String.valueOf(l.toLocation().getLongitude()));
                                location.setTime(DateUtil.stringToDay(l.getTime()));
                                location.setDirection(l.getDirection());
                                return location;
                            }).toList();
                    locationMapper.batchSave(locations);
                    task.setDrivingMileage(r.getDrivingMileage());
                    task.setStatus("done");
                    log.info("Task done:{}", task);
                    return task;
                }).thenApply(t -> taskMapper.updateByPrimaryKey(t));
    }

    public void parseStoppingPoint(Long taskId) {
        if (Objects.isNull(taskId)) {
            Task t = taskMapper.selectTask();
            if (Objects.isNull(t)) {
                return;
            }
            taskId = t.getId();
        }
        Task t = new Task();
        t.setId(taskId);
        t = taskMapper.selectOne(t);
        if (Objects.isNull(t) || "done".equalsIgnoreCase(t.getParseStatus())) {
            return;
        }
        //修改状态
        t.setStatus("doing");
        taskMapper.updateByPrimaryKey(t);

        Long finalTaskId = taskId;
        Task finalT = t;
        CompletableFuture.supplyAsync(() -> stoppingPointMapper.selectPoints(finalTaskId))
                .thenApply(points -> {
                    //提空点
                    Location first = locationMapper.selectFirst(finalTaskId);
                    Location end = locationMapper.selectEnd(finalTaskId);
                    points.stream()
                            .filter(p -> Objects.isNull(p.getDistance()) || 0 == p.getDistance() ||
                                    Objects.isNull(p.getDistance2()) || 0 == p.getDistance2())
                            .forEach(p -> {
                                String longitude = p.getLonAndLat().split(",")[0];
                                String latitude = p.getLonAndLat().split(",")[1];
                                p.setDistance(ElectronicFenceUtils.calLonLatDistance(Double.parseDouble(longitude),
                                        Double.parseDouble(latitude),
                                        Double.parseDouble(first.getLongitude()),
                                        Double.parseDouble(first.getLatitude())));
                                p.setDistance2(ElectronicFenceUtils.calLonLatDistance(Double.parseDouble(longitude),
                                        Double.parseDouble(latitude),
                                        Double.parseDouble(end.getLongitude()),
                                        Double.parseDouble(end.getLatitude())));
                            });
                    return points;
                }).thenApply(points -> {
                    points.stream()
                            .filter(p -> StringUtils.isEmpty(p.getAddress()) ||
                                    StringUtils.isEmpty(p.getTownship()))
                            .forEach(p -> {
                                AmapGeoRespDTO.Address r = getLocationFromGeo(p.getLonAndLat());
                                p.setAddress(r.getAddress());
                                p.setProvince(r.getAddressComponent().getProvince());
                                p.setCity(r.getAddressComponent().getCity());
                                p.setDistrict(r.getAddressComponent().getDistrict());
                                p.setTownship(r.getAddressComponent().getTownship());
                            });
                    return points;
                }).thenAccept(points -> {
                    List<StoppingPoint> sortedPoints = points.stream().sorted(Comparator.comparing(StoppingPoint::getDistance).reversed()).toList();
                    for (int i = 0; i < sortedPoints.size(); i++) {
                        StoppingPoint p = sortedPoints.get(i);
                        p.setDoorPoint(i + 1);
                        stoppingPointMapper.updateByPrimaryKey(p);
                    }

                    finalT.setParseStatus("done");
                    taskMapper.updateByPrimaryKey(finalT);
                    log.info("Task parse:{}", finalT);
                });
    }

    private RestTemplate restTemplate = new RestTemplate();
    String url = "https://restapi.amap.com/v3/geocode/regeo?key=%s&location=%s";

    private AmapGeoRespDTO.Address getLocationFromGeo(String location) {
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
        return response.getBody().getRegeocode();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
//        addTasks();
//        scheduledExecutor.scheduleAtFixedRate(() -> {
//            CompletableFuture.supplyAsync(() -> {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                return UUID.randomUUID();
//            }).thenAccept(u -> log.info("随机数：{}", u));
//            log.info("任务开始");
//        }, 3, 5, TimeUnit.SECONDS);
    }
}