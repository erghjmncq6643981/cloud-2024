package com.chandler.location.example.entity.dto;

import com.alibaba.fastjson2.annotation.JSONField;
import com.chandler.location.example.entity.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author hongguang.lai
 * @date 2023/5/12 10:30
 */
@Data
public class TruckLocationDTO {

  /** 经纬度 坐标系取决于入参指定的坐标系，默认为{@link com.duckbillscm.ttp.enums.CoordsEnum#gcj02} */
  private String lonAndLat;

  /** 速度 单位:KM/H */
  private String speed;

  /** 定位时间 */
  private String time;

  /** 定位地址，只有查最后位置时有值。 */
  private String address;

  /** 定位方向编码 范围：0-360 */
  private String direction;

  /** 定位方向描述 */
  private String directionDesc;

  /** ACC状态(关机:0,开机:1) */
  private String accStatus;

  @JSONField(serialize = false)
  @JsonIgnore
  public Location toLocation() {
    Location location = new Location();
    String longitude = getLonAndLat().split(",")[0];
    String latitude = getLonAndLat().split(",")[1];
    location.setLongitude(Double.parseDouble(longitude));
    location.setLatitude(Double.parseDouble(latitude));
    return location;
  }
}
