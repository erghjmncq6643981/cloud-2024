/*
 * chandler-file-test
 * 2025/5/28 10:27
 *
 * Please contact chandler
 * if you need additional information or have any questions.
 * Please contact chandler Corporation or visit:
 * https://www.jianshu.com/u/117796446366
 * @author 钱丁君-chandler
 * @version 1.0
 */
package com.chandler.test.example.energy;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.chandler.instance.client.example.util.DateUtil;
import com.chandler.test.example.energy.excel.*;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类功能描述
 *
 * @author 钱丁君-chandler 2025/5/28 10:27
 * @version 1.0.0
 * @since 1.8
 */
@Slf4j
public class EnergyConsumptionRecord {
    public static void main(String[] args) {
        Map<String, List<VehicleOilDetail>> vehicleOilDetailMap = getVehicleOilDetail();

        Map<String, List<VehicleChangeLog>> vehicleChangeLogMap = getVehicleChangeLog();
        Map<String, List<DriverChangeLog>> driverChangeLogMap = getDriverChangeLog();
        Map<String, List<PortChangeLog>> portChangeLogMap = getPortChangeLog();
        //比对所需的EXCEL
        List<TruckEnergyConsumptionRecord> data = Lists.newArrayList();
        List<String> unHaveRecords = Lists.newArrayList();
        vehicleOilDetailMap.forEach((number, oilDetails) ->
        {
            List<TruckEnergyConsumptionRecord> consumptionRecords = getPairTimes(number, vehicleChangeLogMap.get(number), driverChangeLogMap.get(number), portChangeLogMap.get(number));
            if (consumptionRecords.isEmpty()) {
                unHaveRecords.add(number);
                log.warn("没有生成能耗记录！车牌:{},加注数据的数量：{}", number, oilDetails.size());
                return;
            }
            for (TruckEnergyConsumptionRecord consumptionRecord : consumptionRecords) {
                List<VehicleOilDetail> filterOilDetails =
                        oilDetails.stream()
                                .filter(
                                        od ->
                                                consumptionRecord.getBeginDate().isBefore(od.getRecordDate())
                                                        && (Objects.isNull(consumptionRecord.getEndDate())
                                                        || consumptionRecord.getEndDate().isAfter(od.getRecordDate())))
                                .sorted(Comparator.comparing(VehicleOilDetail::getRecordDate).reversed())
                                .collect(Collectors.toList());
                List<VehicleOilDetail> OtherOilDetails =
                        oilDetails.stream()
                                .filter(
                                        od ->
                                                od.getRecordDate().isBefore(consumptionRecord.getBeginDate()))
                                .sorted(Comparator.comparing(VehicleOilDetail::getRecordDate).reversed())
                                .collect(Collectors.toList());
                updateData(filterOilDetails, OtherOilDetails, consumptionRecord);
                consumptionRecord.setPortId(getPortName(consumptionRecord.getPortId()));
                if(!filterOilDetails.isEmpty()){
                    data.add(consumptionRecord);
                }
            }
        });
        System.out.println("没有生成能耗记录的数量:" + unHaveRecords.size());
        System.out.println(unHaveRecords);
        //初始化所需的body
        List<String> needInit = vehicleOilDetailMap.keySet().stream().filter(k -> !unHaveRecords.contains(k)).toList();
        StringBuffer stringBuffer = new StringBuffer("{\n");
        String body = needInit.stream().map(k -> String.format("\"%s\": \"all\"", k))
                .collect(Collectors.joining(",\n"));
        stringBuffer.append(body);
        stringBuffer.append("\n");
        stringBuffer.append("}");
        System.out.println("需要生成能耗记录的数量:" + needInit.size());
        System.out.println(stringBuffer);
        try (ExcelWriter excelWriter = ExcelUtil.getBigWriter()) {
            excelWriter.write(data);
            FileOutputStream fos =
                    new FileOutputStream("/Users/chandler/Downloads/能耗记录.xlsx");
            excelWriter.flush(fos);
            fos.close();
        } catch (Exception e) {
            System.out.println("发生异常！");
        }
    }

    private static Map<String, List<VehicleChangeLog>> getVehicleChangeLog() {
        // 文件路径
        String fileName = "/Users/chandler/Documents/鸭嘴兽/商车科技/能耗/能耗记录初始化/车挂绑定记录.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<VehicleChangeLog> vehicleChangeLogs = Lists.newArrayList();
        for (List<Object> row : read) {
            VehicleChangeLog vehicleChangeLog = new VehicleChangeLog();
            String truckPlateNumber = (String) row.get(0);
            vehicleChangeLog.setTruckPlateNumber(truckPlateNumber);
            String carRegistration = (String) row.get(1);
            vehicleChangeLog.setCarRegistration(carRegistration);
            String oilType = (String) row.get(2);
            vehicleChangeLog.setOilType(oilType);
            String bindingTime = (String) row.get(3);
            vehicleChangeLog.setBindingTime(LocalDateTime.parse(bindingTime, DateUtil.YMD_HMS_DF));
            String unbindingTime = (String) row.get(4);
            if (StringUtils.isNotBlank(unbindingTime)) {
                vehicleChangeLog.setUnbindingTime(LocalDateTime.parse(unbindingTime, DateUtil.YMD_HMS_DF));
            }
            String axlesNumber = (String) row.get(5);
            vehicleChangeLog.setAxlesNumber(axlesNumber);
            vehicleChangeLogs.add(vehicleChangeLog);
//            System.out.printf("%s-%s-%s-%s-%s-%s %n", truckPlateNumber, carRegistration, oilType, bindingTime, unbindingTime, axlesNumber);
        }
        return vehicleChangeLogs.stream().collect(Collectors.groupingBy(VehicleChangeLog::getTruckPlateNumber));
    }

    private static Map<String, List<DriverChangeLog>> getDriverChangeLog() {
        // 文件路径
        String fileName = "/Users/chandler/Documents/鸭嘴兽/商车科技/能耗/能耗记录初始化/司机绑定记录.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<DriverChangeLog> driverChangeLogs = Lists.newArrayList();
        for (List<Object> row : read) {
            DriverChangeLog driverChangeLog = new DriverChangeLog();
            String port = (String) row.get(0);
            String truckPlateNumber = (String) row.get(1);
            String name = (String) row.get(2);
            String idCard = (String) row.get(3);
            String mobilePhone = (String) row.get(4);
            driverChangeLog.setPort(port);
            driverChangeLog.setTruckPlateNumber(truckPlateNumber);
            driverChangeLog.setName(name);
            driverChangeLog.setIdCard(idCard);
            driverChangeLog.setMobilePhone(mobilePhone);
            String bindingTime = (String) row.get(5);
            driverChangeLog.setBindingTime(LocalDateTime.parse(bindingTime, DateUtil.YMD_HMS_DF));
            String unbindingTime = (String) row.get(6);
            if (StringUtils.isNotBlank(unbindingTime)) {
                driverChangeLog.setUnbindingTime(LocalDateTime.parse(unbindingTime, DateUtil.YMD_HMS_DF));
            }
            driverChangeLogs.add(driverChangeLog);
        }
        return driverChangeLogs.stream().collect(Collectors.groupingBy(DriverChangeLog::getTruckPlateNumber));
    }

    private static Map<String, List<PortChangeLog>> getPortChangeLog() {
        // 文件路径
        String fileName = "/Users/chandler/Documents/鸭嘴兽/商车科技/能耗/能耗记录初始化/港口绑定记录.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<PortChangeLog> portChangeLogs = Lists.newArrayList();
        for (List<Object> row : read) {
            PortChangeLog portChangeLog = new PortChangeLog();
            String truckPlateNumber = (String) row.get(0);
            String port = (String) row.get(1);
            String bindingTime = (String) row.get(2);
            String unbindingTime = (String) row.get(3);
            portChangeLog.setPort(port);
            portChangeLog.setTruckPlateNumber(truckPlateNumber);
            portChangeLog.setBindingTime(LocalDateTime.parse(bindingTime, DateUtil.YMD_HMS_DF));
            if (StringUtils.isNotBlank(unbindingTime)) {
                portChangeLog.setUnbindingTime(LocalDateTime.parse(unbindingTime, DateUtil.YMD_HMS_DF));
            }
            portChangeLogs.add(portChangeLog);
        }
        return portChangeLogs.stream().collect(Collectors.groupingBy(PortChangeLog::getTruckPlateNumber));
    }

    private static Map<String, List<VehicleOilDetail>> getVehicleOilDetail() {
        // 文件路径
        String fileName = "/Users/chandler/Documents/鸭嘴兽/商车科技/能耗/能耗记录初始化/加注数据.xlsx";
        // 使用Hutool的ExcelUtil工具类来读取Excel文件
        ExcelReader reader = ExcelUtil.getReader(fileName);
        // 读取第一个sheet
        List<List<Object>> read = reader.read(1);
        List<VehicleOilDetail> vehicleOilDetails = Lists.newArrayList();
        for (List<Object> row : read) {
            VehicleOilDetail vehicleOilDetail = new VehicleOilDetail();
            String truckPlateNumber = (String) row.get(0);
            String energyType = (String) row.get(1);
            String recordDate = (String) row.get(2);
            String oilLitres = (String) row.get(3);
            String isFull = (String) row.get(4);
            String mileage = (String) row.get(5);
            vehicleOilDetail.setEnergyType(energyType);
            vehicleOilDetail.setTruckPlateNumber(truckPlateNumber);
            vehicleOilDetail.setRecordDate(LocalDateTime.parse(recordDate, DateUtil.YMD_HMS_DF));
            vehicleOilDetail.setOilLitres(new BigDecimal(oilLitres));
            vehicleOilDetail.setIsFull(Integer.parseInt(isFull));
            if (StringUtils.isNotBlank(mileage) && energyType.equals("柴油")) {
                vehicleOilDetail.setMileage(new BigDecimal(mileage));
                vehicleOilDetails.add(vehicleOilDetail);
            }
        }
        return vehicleOilDetails.stream()
                .collect(Collectors.groupingBy(VehicleOilDetail::getTruckPlateNumber));
    }

    private static List<TruckEnergyConsumptionRecord> getPairTimes(String plateNumber,
                                                                   List<VehicleChangeLog> vehicleChangeLogs,
                                                                   List<DriverChangeLog> driverChangeLogs,
                                                                   List<PortChangeLog> portChangeLogs) {
        if (Objects.isNull(driverChangeLogs) ||
                driverChangeLogs.isEmpty()) {
            return Lists.newArrayList();
        }

        // 司机绑定关系、车辆更换港口记录、车头车挂绑定记录
        LocalDateTime endTime = null;
        Set<LocalDateTime> times = Sets.newHashSet();
        List<VehicleChangeLog> cleanVehicleChanges = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(vehicleChangeLogs)) {
            List<VehicleChangeLog> sortVehicleChanges =
                    vehicleChangeLogs.stream()
                            .filter(v -> Objects.nonNull(v.getBindingTime()))
                            .sorted(Comparator.comparing(VehicleChangeLog::getBindingTime))
                            .toList();

            for (VehicleChangeLog vc : sortVehicleChanges) {
                if (cleanVehicleChanges.isEmpty()) {
                    cleanVehicleChanges.add(vc);
                } else {
                    VehicleChangeLog last = cleanVehicleChanges.get(cleanVehicleChanges.size() - 1);
                    // 轴数相同合并
                    if (Objects.nonNull(last.getAxlesNumber())
                            && Objects.equals(last.getAxlesNumber(), vc.getAxlesNumber())) {
                        last.setUnbindingTime(vc.getUnbindingTime());
                    } else {
                        cleanVehicleChanges.add(vc);
                    }
                }
            }

            endTime = cleanVehicleChanges.get(cleanVehicleChanges.size() - 1).getUnbindingTime();
            cleanVehicleChanges.forEach(
                    t -> {
                        times.add(t.getBindingTime());
                        if (Objects.nonNull(t.getUnbindingTime())) {
                            times.add(t.getUnbindingTime());
                        }
                    });
        }
        // driver
        List<DriverChangeLog> sortedDriverChanges =
                driverChangeLogs.stream()
                        .filter(d -> Objects.nonNull(d.getBindingTime()))
                        .sorted(Comparator.comparing(DriverChangeLog::getBindingTime))
                        .toList();
        LocalDateTime driverEndTime = sortedDriverChanges.get(sortedDriverChanges.size() - 1).getUnbindingTime();
        if ((Objects.isNull(endTime) && Objects.nonNull(driverEndTime))
                || (Objects.nonNull(endTime)
                && Objects.nonNull(driverEndTime)
                && driverEndTime.isBefore(endTime))) {
            endTime = driverEndTime;
        }
        List<DriverChangeLog> cleanDriverChanges = Lists.newArrayList();
        for (DriverChangeLog dc : sortedDriverChanges) {
            if (cleanDriverChanges.isEmpty()) {
                cleanDriverChanges.add(dc);
            } else {
                DriverChangeLog last = cleanDriverChanges.get(cleanDriverChanges.size() - 1);
                // 相同合并
                if (Objects.nonNull(last.getIdCard())
                        && Objects.equals(last.getIdCard(), dc.getIdCard())) {
                    last.setUnbindingTime(dc.getUnbindingTime());
                } else {
                    cleanDriverChanges.add(dc);
                }
            }
        }
        cleanDriverChanges.forEach(
                t -> {
                    times.add(t.getBindingTime());
                    if (Objects.nonNull(t.getUnbindingTime())) {
                        times.add(t.getUnbindingTime());
                    }
                });
        // port
        List<PortChangeLog> cleanPortChanges = com.google.common.collect.Lists.newArrayList();
        if (!CollectionUtils.isEmpty(portChangeLogs)) {
            List<PortChangeLog> sortPortChanges =
                    portChangeLogs.stream()
                            .filter(p -> Objects.nonNull(p.getBindingTime()))
                            .sorted(Comparator.comparing(PortChangeLog::getBindingTime))
                            .toList();
            for (PortChangeLog pc : sortPortChanges) {
                if (cleanPortChanges.isEmpty()) {
                    cleanPortChanges.add(pc);
                } else {
                    PortChangeLog last = cleanPortChanges.get(cleanPortChanges.size() - 1);
                    // 相同合并
                    if (Objects.nonNull(last.getPort()) && Objects.equals(last.getPort(), pc.getPort())) {
                        last.setUnbindingTime(pc.getUnbindingTime());
                    } else {
                        cleanPortChanges.add(pc);
                    }
                }
            }
            LocalDateTime portEndTime =
                    cleanPortChanges.get(cleanPortChanges.size() - 1)
                            .getUnbindingTime();
            if ((Objects.isNull(endTime) && Objects.nonNull(portEndTime))
                    || (Objects.nonNull(endTime)
                    && Objects.nonNull(portEndTime)
                    && portEndTime.isBefore(endTime))) {
                endTime = portEndTime;
            }

            cleanPortChanges.forEach(
                    t -> {
                        times.add(t.getBindingTime());
                        if (Objects.nonNull(t.getUnbindingTime())) {
                            times.add(t.getUnbindingTime());
                        }
                    });
        }

        List<Pair<LocalDateTime, LocalDateTime>> timePairs = new ArrayList<>();
        List<LocalDateTime> sortedTimes = times.stream().sorted().toList();
        for (int i = 1; i < sortedTimes.size(); i++) {
            timePairs.add(Pair.of(sortedTimes.get(i - 1), sortedTimes.get(i)));
            if (i == sortedTimes.size() - 1 && Objects.isNull(endTime)) {
                timePairs.add(Pair.of(sortedTimes.get(i), endTime));
            }
        }
        List<TruckEnergyConsumptionRecord> records = Lists.newArrayList();
        timePairs.forEach(
                pair -> {
                    LocalDateTime recordDate = pair.getKey().plusSeconds(1);
                    Optional<VehicleChangeLog> vehicleChange =
                            cleanVehicleChanges.stream()
                                    .filter(
                                            dc ->
                                                    Objects.nonNull(dc)
                                                            && (dc.getBindingTime().isBefore(recordDate)
                                                            || dc.getBindingTime().equals(recordDate))
                                                            && (Objects.isNull(dc.getUnbindingTime())
                                                            || dc.getUnbindingTime().isAfter(recordDate)))
                                    .findAny();
                    Optional<DriverChangeLog> driverChange =
                            cleanDriverChanges.stream()
                                    .filter(
                                            dc ->
                                                    Objects.nonNull(dc)
                                                            && (dc.getBindingTime().isBefore(recordDate)
                                                            || dc.getBindingTime().equals(recordDate))
                                                            && (Objects.isNull(dc.getUnbindingTime())
                                                            || dc.getUnbindingTime().isAfter(recordDate)))
                                    .findAny();
                    Optional<PortChangeLog> portChange =
                            cleanPortChanges.stream()
                                    .filter(
                                            dc ->
                                                    Objects.nonNull(dc)
                                                            && (dc.getBindingTime().isBefore(recordDate)
                                                            || dc.getBindingTime().equals(recordDate))
                                                            && (Objects.isNull(dc.getUnbindingTime())
                                                            || dc.getUnbindingTime().isAfter(recordDate)))
                                    .findAny();
                    if (driverChange.isPresent()) {
                        TruckEnergyConsumptionRecord record = new TruckEnergyConsumptionRecord();
                        record.setPortId(portChange.stream().map(PortChangeLog::getPort).findAny().orElse(""));
                        record.setTruckPlateNumber(vehicleChange.stream().map(VehicleChangeLog::getTruckPlateNumber).findAny().orElse(plateNumber));
                        record.setAxlesNumber(vehicleChange.stream().map(VehicleChangeLog::getAxlesNumber).findAny().orElse(""));
                        record.setBeginDate(pair.getLeft());
                        record.setEndDate(pair.getRight());
                        record.setName(String.format("%s(%s-%s)", driverChange.get().getName(), driverChange.get().getMobilePhone(), driverChange.get().getIdCard()));
                        long workDay =
                                ChronoUnit.DAYS.between(
                                        pair.getKey().toLocalDate(),
                                        Objects.nonNull(pair.getValue()) ? pair.getValue() : LocalDateTime.now());
                        workDay = workDay + 1;
                        record.setWorkDay((int) workDay);
                        record.setIntervalMileage(BigDecimal.ZERO);
                        record.setIntervalOilLitres(BigDecimal.ZERO);
                        record.setIntervalHundredConsumption(BigDecimal.ZERO);
                        records.add(record);
//                        log.info("{}车辆识别到的绑定记录时间段,{}", plateNumber, record);
                    } else {
                        log.warn("{}车辆{}时间段，未生成能耗记录！", plateNumber, timePairs);
                    }
                });
        return records;
    }


    private static void updateData(
            List<VehicleOilDetail> oilDetails, List<VehicleOilDetail> lastOilDetails, TruckEnergyConsumptionRecord consumptionRecord) {
        // 检查前面是否存在未加满的数据，如果存在就丢弃
        Optional<VehicleOilDetail> firstFullRecord =
                oilDetails.stream().filter(r -> r.getIsFull() == 1).findFirst();
        if (firstFullRecord.isPresent()) {
            oilDetails =
                    oilDetails.stream()
                            .filter(
                                    od ->
                                            Objects.equals(od.getRecordDate(), firstFullRecord.get().getRecordDate())
                                                    || od.getRecordDate().isBefore(firstFullRecord.get().getRecordDate()))
                            .collect(Collectors.toList());
        } else {
            log.info(
                    "没有加注数据,beginDate:{};endDate:{}",
                    consumptionRecord.getBeginDate(),
                    consumptionRecord.getEndDate());
            return;
        }
        VehicleOilDetail oilDetailFirst = oilDetails.get(0);
        VehicleOilDetail oilDetailLast = oilDetails.get(oilDetails.size() - 1);
        Optional<VehicleOilDetail> lastFullRecord =
                lastOilDetails.stream().filter(r -> r.getIsFull() == 1).findFirst();

        BigDecimal intervalOilLitres =
                oilDetails.stream()
                        .map(VehicleOilDetail::getOilLitres)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal intervalHundredConsumption = BigDecimal.ZERO;
        BigDecimal intervalMileage = oilDetailFirst.getMileage().subtract(BigDecimal.ZERO);
        if (lastFullRecord.isPresent()) {
            intervalOilLitres =
                    intervalOilLitres.add(
                            lastOilDetails.stream()
                                    .filter(r -> r.getRecordDate().isAfter(lastFullRecord.get().getRecordDate()))
                                    .map(VehicleOilDetail::getOilLitres)
                                    .filter(Objects::nonNull)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            intervalMileage = oilDetailFirst.getMileage().subtract(lastFullRecord.get().getMileage());
        } else {
            intervalOilLitres =
                    intervalOilLitres.add(
                            lastOilDetails.stream()
                                    .map(VehicleOilDetail::getOilLitres)
                                    .filter(Objects::nonNull)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add));
            if (oilDetails.size() > 1) {
                intervalMileage = oilDetailFirst.getMileage().subtract(oilDetailLast.getMileage());
            }
        }
        if (Boolean.FALSE.equals(intervalMileage.compareTo(BigDecimal.ZERO) == 0)) {
            intervalHundredConsumption =
                    new BigDecimal("100.00")
                            .multiply(intervalOilLitres.divide(intervalMileage, 4, RoundingMode.HALF_UP));
        }
        consumptionRecord.setIntervalHundredConsumption(intervalHundredConsumption);
        consumptionRecord.setIntervalOilLitres(intervalOilLitres);
        consumptionRecord.setIntervalMileage(intervalMileage);
//        log.info("能耗记录更新，{}", consumptionRecord);
    }

    private static String getPortName(String portId) {
        switch (portId) {
            case "ANJI":
                return "安吉港";
            case "CHANGSHU":
                return "常熟港";
            case "CNCAN":
                return "广州港";
            case "CNJIA":
                return "江阴港";
            case "CNXMN":
                return "厦门港";
            case "CNZHE":
                return "镇江港";
            case "DUSHAN":
                return "独山港";
            case "HEFEI":
                return "合肥港";
            case "HUZHOUTIELU":
                return "湖州铁路港";
            case "JIAXING":
                return "嘉兴港";
            case "LIANYUN":
                return "连云港";
            case "NANJING":
                return "南京港";
            case "NANTONG":
                return "南通港";
            case "NINGBO":
                return "宁波港";
            case "QINGDAO":
                return "青岛港";
            case "SHANGHAI":
                return "上海港";
            case "SHANTOU":
                return "汕头港";
            case "SHENZHEN":
                return "深圳港";
            case "TAICANG":
                return "苏州港";
            case "TIANJIN":
                return "太仓港";
            case "WUHU":
                return "天津港";
            case "ZHANGJIA":
                return "芜湖港";
            case "DEQING":
                return "德清港";
            case "YANGZHOU":
                return "扬州港";
            case "YIWU":
                return "义乌港";
            case "HUANGHUA":
                return "张家港";
            case "TAIZHOU":
                return "台州港";
            case "QINZHOU":
                return "青州港";
            case "JIANSHAN":
                return "尖山港";
            default:
                return portId;
        }
    }
}