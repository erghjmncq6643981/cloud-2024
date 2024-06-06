package com.chandler.location.example.enu;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * 车辆位置数据源
 *
 * @author hongguang.lai
 * @date 2023/5/18 15:43
 */
@Getter
@AllArgsConstructor
public enum LocationSourceEnum {

  /** 北斗 */
  Gps("1", "北斗"),

  /** 车厂 */
  Factory("2", "车厂"),

  /** 北斗、车厂合并之后的数据 */
  Merge("3", "融合"),

  /** 龙船 */
  DragonBoat("4", "龙船"),

  /** 中交智运 */
  ZhiYun("5", "智运");

  private final String source;

  private final String desc;

  private static final String COMMA = StrUtil.COMMA;

  /** 合法的数据源组合 */
  private static final Set<String> LEGAL_GROUP = new HashSet<>();

  private static final Set<LocationSourceEnum> INNER_LOCATION_SOURCES = new HashSet<>();

  static {
    Arrays.stream(values()).map(LocationSourceEnum::getSource).forEach(LEGAL_GROUP::add);

    INNER_LOCATION_SOURCES.add(Gps);
    INNER_LOCATION_SOURCES.add(Factory);
    INNER_LOCATION_SOURCES.add(Merge);
  }

  public static boolean includeZhiYun(String source) {
    return include(source, ZhiYun.source);
  }

  public static boolean include(String source, String target) {
    return source.contains(target);
  }

  public static boolean innerLocationSource(LocationSourceEnum locationSource) {
    return INNER_LOCATION_SOURCES.contains(locationSource);
  }

  public static LocationSourceEnum getLocationSource(String source) {
    for (LocationSourceEnum sourceEnum : values()) {
      if (sourceEnum.getSource().equals(source)) {
        return sourceEnum;
      }
    }
    return null;
  }

  public static LocationSourceEnum getLocationSourceFromName(String name) {
    for (LocationSourceEnum sourceEnum : values()) {
      if (sourceEnum.getDesc().equals(name)) {
        return sourceEnum;
      }
    }
    return null;
  }
}
