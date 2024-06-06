/*
 * chandler-location
 * 2024/4/8 09:08
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.location.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.chandler.location.example.domain.dataobject.OriginalLocationData;
import com.chandler.location.example.domain.dataobject.Task;
import com.chandler.location.example.domain.mapper.OriginalLocationDataMapper;
import com.chandler.location.example.entity.Location;
import com.chandler.location.example.entity.OriginalLocationParam;
import com.chandler.location.example.entity.RawLocationData;
import com.chandler.location.example.entity.dto.LocationDataDTO;
import com.chandler.location.example.entity.dto.TruckDrivingResp;
import com.chandler.location.example.enu.LocationSourceEnum;
import com.chandler.location.example.util.ElectronicFenceUtils;
import com.chandler.location.example.util.FileUtil;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2024/4/8 09:08
 * @since 1.8
 */
@Slf4j
@Service
public class DataNoiseHandler implements InitializingBean {
    //100KM/h
    private static final int NORMAL_DRIVING_METRE_PER_SECONDS = 28;
    @Resource
    private OriginalLocationDataMapper originalLocationDataMapper;
    /**
     * @param lpn 车牌号
     * @param sourceType  数据源类型：车厂，北斗
     * @param filePath 文件path
     * {@code @Description} 读取轨迹点文件
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/4/11 15:06
     */
    public void readFile(String lpn, String sourceType, String filePath) {
        Workbook wb = FileUtil.readExcel(filePath);
        if (Objects.isNull(wb)) {
            throw new RuntimeException("File is empty!filePath:" + filePath + "!");
        }

        LocationSourceEnum type = LocationSourceEnum.getLocationSourceFromName(sourceType);
        if (Objects.isNull(type)) {
            throw new RuntimeException("sourceType is null!sourceType:" + sourceType + "!");
        } else if (Boolean.FALSE.equals(LocationSourceEnum.innerLocationSource(type))) {
            throw new RuntimeException("sourceType is not inner LocationSource!sourceType:" + sourceType + "!");
        }
        //获取第一个sheet
        Sheet sheet = wb.getSheetAt(0);
        //获取最大行数
        int rowNum = sheet.getPhysicalNumberOfRows();
        //获取第一行
//            row = sheet.getRow(0);
        List<OriginalLocationData> locations = Lists.newArrayList();
        //获取最大列数
        for (int i = 1; i < rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell c1 = row.getCell(0);
                LocalDateTime r1 = c1.getLocalDateTimeCellValue();
                Cell c2 = row.getCell(1);
                Object r2 = FileUtil.getCellFormatValue(c2);
                Cell c3 = row.getCell(2);
                Object r3 = FileUtil.getCellFormatValue(c3);
                Cell c4 = row.getCell(3);
                Object r4 = FileUtil.getCellFormatValue(c4);
                OriginalLocationData l = new OriginalLocationData();
                l.setLpn(lpn);
                l.setLonAndLat((String) r4);
                l.setReportTime(r1);
                OriginalLocationData old = originalLocationDataMapper.selectOne(l);
                if (Objects.isNull(old)) {
                    l.setSourceType(Integer.valueOf(type.getSource()));
                    l.setDirection((String) r3);
                    l.setSpeed(Double.valueOf((String) r2).intValue());
                    locations.add(l);
                }
            }
        }
        originalLocationDataMapper.batchSave(locations);
        log.info("读取{}的{}条轨迹点数据！", lpn, locations.size());
    }

    /**
     * @param param 轨迹点查询条件
     * @return
     * {@code @Description} 解析轨迹点
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/4/11 15:07
     */
    public TruckDrivingResp parseLocationData(OriginalLocationParam param) {
        List<OriginalLocationData> points = originalLocationDataMapper.selectData(param);
        List<RawLocationData> cleanPoints = Lists.newArrayList();
        List<RawLocationData> samples = Lists.newArrayList();
        for (int i = 0; i < points.size(); i++) {
            OriginalLocationData d = points.get(i);
            RawLocationData raw = new RawLocationData();
            BeanUtils.copyProperties(d, raw);
            judgeLocation(samples, raw, cleanPoints);
        }
        cleanPoints.addAll(samples);
        cleanPoints.forEach(p -> log.info("cleanPoints:{}", p));
        TruckDrivingResp r = new TruckDrivingResp();
        List<LocationDataDTO> trustPoints = cleanPoints.stream()
                .filter(p -> p.getDistrustLevel() <= param.getDistrustLevel())
                .map(p -> {
                    LocationDataDTO l = new LocationDataDTO();
                    BeanUtils.copyProperties(p, l);
                    l.setTime(p.getReportTime());
                    return l;
                })
                .toList();
        long drivingMileage = 0;
        for (int i = 1; i < trustPoints.size(); i++) {
            //距离
            Location firstLocation = new Location(trustPoints.get(i - 1).getLonAndLat());
            Location secondLocation = new Location(trustPoints.get(i).getLonAndLat());
            int distance = ElectronicFenceUtils.calLonLatDistance(
                    firstLocation.getLongitude(), firstLocation.getLatitude(),
                    secondLocation.getLongitude(), secondLocation.getLatitude());
            drivingMileage = drivingMileage + distance;
        }
        r.setLocationSize(trustPoints.size());
        r.setDrivingMileage(new BigDecimal(drivingMileage).divide(BigDecimal.valueOf(1000), 2, RoundingMode.HALF_UP));
        r.setLocations(trustPoints);
        return r;
    }

    /**
     * @param samples 样本数量=5
     * @return raw 待判断数据
     * {@code @Description}
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/4/8 16:16
     */
    private void judgeMiniSamples(List<RawLocationData> samples, RawLocationData raw) {
        if (samples.isEmpty()) {
            return;
        }
        if (raw.getLonAndLat().equalsIgnoreCase(samples.getLast().getLonAndLat())) {
            if (1 < raw.getSpeed()) {
                //速度变化，经纬度不变
                raw.updateDistrustLevel(raw.getDistrustLevel() + 20);
            }
            return;
        }
        for (RawLocationData sample : samples) {
            if (computeDistanceIsReasonable(sample, raw)) {
                raw.updateDistrustLevel(raw.getDistrustLevel() - 10);
            } else {
                Duration duration = Duration.between(samples.getLast().getReportTime(), raw.getReportTime());
                if (duration.getSeconds() > 5 * 60) {
                    raw.updateDistrustLevel(raw.getDistrustLevel() + 50);
                } else {
                    raw.updateDistrustLevel(raw.getDistrustLevel() + 20);
                }
            }
        }
    }

    /**
     * @param noiseSamples 噪点样本
     * @return raw 待判断数据
     * {@code @Description}
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/4/9 09:30
     */
    private void judgeNoiseSamples(List<RawLocationData> noiseSamples, RawLocationData sample) {
        for (RawLocationData noiseSample : noiseSamples) {
            if (sample.getLonAndLat().equalsIgnoreCase(noiseSample.getLonAndLat())) {
                if (1 < sample.getSpeed()) {
                    noiseSample.updateDistrustLevel(noiseSample.getDistrustLevel() + 20);
                }
                //经纬度相同，权重为01
            } else {
                boolean isReasonable = computeDistanceIsReasonable(noiseSample, sample);
                //时间越近，权重越高
                Duration duration = Duration.between(noiseSample.getReportTime(), sample.getReportTime());
                long durationSecond = Math.abs(duration.getSeconds());
                if (isReasonable && sample.getDistrustLevel() <= 100) {
                    int distrustLevel = 0;
                    if (durationSecond > 0 && durationSecond < 10 * 60) {
                        distrustLevel = 10;
                    } else if (durationSecond >= 10 * 60 && durationSecond < 30 * 60) {
                        distrustLevel = 5;
                    } else if (durationSecond >= 30 * 60 && durationSecond < 60 * 60) {
                        distrustLevel = 2;
                    }
                    noiseSample.updateDistrustLevel(noiseSample.getDistrustLevel() - distrustLevel);
                } else {
                    int distrustLevel = 0;
                    if (durationSecond > 0 && durationSecond < 10 * 60) {
                        distrustLevel = (isReasonable) ? 10 : 50;
                    } else if (durationSecond >= 10 * 60 && durationSecond < 30 * 60) {
                        distrustLevel = (isReasonable) ? 2 : 10;
                    } else if (durationSecond >= 30 * 60 && durationSecond < 60 * 60) {
                        distrustLevel = (isReasonable) ? 1 : 5;
                    }
                    //时间相差越大，系数越低
                    noiseSample.updateDistrustLevel(noiseSample.getDistrustLevel() + distrustLevel);
                }
            }
        }
    }


    public void judgeLocation(List<RawLocationData> samples,
                              RawLocationData raw,
                              List<RawLocationData> cleanPoints) {
        if (samples.size() < 5) {//rawPoint 90
            raw.updateDistrustLevel(90);
        } else if (samples.size() == 5) {
            for (int i = samples.size() - 1; i > 0; i--) {
                int finalI = i;
                List<RawLocationData> data = new java.util.ArrayList<>(samples.stream()
                        .filter(s -> Boolean.FALSE.equals(s == samples.get(finalI)))
                        .toList());
                data.add(raw);
                judgeMiniSamples(data, samples.get(i));
            }
        } else if (samples.size() < 10) {
            judgeMiniSamples(samples, raw);
        } else {
            //转移到已处理的集合中
            if (samples.size() >= 100) {
                RawLocationData cleanLocation = samples.getFirst();
                samples.removeFirst();
                if (!cleanPoints.isEmpty()) {
                    Duration firstDuration = Duration.between(cleanPoints.getLast().getReportTime(), cleanLocation.getReportTime());
                    Duration secondDuration = Duration.between(cleanLocation.getReportTime(), samples.getFirst().getReportTime());
                    //与前后点的上报时间都相差超过1分钟
                    if (firstDuration.getSeconds() >= 60 && secondDuration.getSeconds() >= 60) {
                        cleanLocation.setDistrustLevel(cleanLocation.getDistrustLevel() + Long.valueOf((firstDuration.getSeconds() + secondDuration.getSeconds()) / 6).intValue());
                    }
                    boolean firstDistance = computeDistanceIsReasonable(cleanPoints.getLast(), cleanLocation);
                    boolean secondDistance = computeDistanceIsReasonable(samples.getFirst(), cleanLocation);
                    //与前后点都偏差很大
                    if (Boolean.FALSE.equals(firstDistance) && Boolean.FALSE.equals(secondDistance)) {
                        cleanLocation.setDistrustLevel(cleanLocation.getDistrustLevel() + 200);
                    }
                }
                cleanPoints.add(cleanLocation);
            }
            //与前5个怀疑值低于100的点进行判断，确定raw的评分
            List<RawLocationData> data = Lists.newArrayList();
            for (int i = samples.size() - 1; data.size() < 5 && i >= 0; i--) {
                if (samples.get(i).getDistrustLevel() < 100) {
                    data.add(samples.get(i));
                }
            }
            if (Boolean.FALSE.equals(samples.isEmpty())) {
                judgeMiniSamples(data, raw);
            }
            //与疑似噪点的数据进行判断，
            judgeNoiseSamples(samples.stream()
                    .filter(s -> s.getDistrustLevel() > 0)
                    .filter(s -> s.getDistrustLevel() < 250)
                    .toList(), raw);
        }
        samples.add(raw);
    }

    /**
     * @param first  第一个点
     * @param second 第二个点
     * @return true:合理，false:不合理
     * {@code @Description} 判断两点之间的距离是否合理
     * <p>
     * {@code @Author} chandler
     * {@code @create} 2024/4/8 11:42
     */
    private boolean computeDistanceIsReasonable(RawLocationData first, RawLocationData second) {
        //时间间隔
        long intervalSeconds = DateUtil.between(Date.from(first.getReportTime().atZone(ZoneId.systemDefault()).toInstant()),
                Date.from(second.getReportTime().atZone(ZoneId.systemDefault()).toInstant()), DateUnit.SECOND);
        //距离
        Location firstLocation = new Location(first.getLonAndLat());
        Location secondLocation = new Location(second.getLonAndLat());
        int distance = ElectronicFenceUtils.calLonLatDistance(
                firstLocation.getLongitude(), firstLocation.getLatitude(),
                secondLocation.getLongitude(), secondLocation.getLatitude());
        return distance <= intervalSeconds * NORMAL_DRIVING_METRE_PER_SECONDS;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}