package com.chandler.location.example.enu;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 坐标系枚举类
 *
 * @author hongguang.lai
 * @date 2022/11/2 15:57
 */
@Getter
@AllArgsConstructor
public enum CoordsEnum {

  /** 地球坐标系 */
  wgs84("wgs84", "地球坐标系"),

  /** 火星坐标系 */
  gcj02("gcj02", "火星坐标系"),

  bd09("bd09", "百度坐标系");

  private final String coords;

  private final String coordsDesc;

  private static final Map<String, CoordsEnum> COORDS = new HashMap<>(values().length);

  static {
    CoordsEnum[] coordsEnums = values();
    for (CoordsEnum coordsEnum : coordsEnums) {
      COORDS.put(coordsEnum.getCoords(), coordsEnum);
    }
  }


  public boolean isWgs84() {
    return wgs84.getCoords().equalsIgnoreCase(coords);
  }

  public boolean isGcj02() {
    return gcj02.getCoords().equalsIgnoreCase(coords);
  }

  public boolean isBd09() {
    return bd09.getCoords().equalsIgnoreCase(coords);
  }
}
