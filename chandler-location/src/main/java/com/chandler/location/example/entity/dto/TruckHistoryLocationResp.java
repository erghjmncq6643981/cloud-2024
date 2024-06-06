package com.chandler.location.example.entity.dto;

import cn.hutool.core.collection.CollectionUtil;
import com.chandler.location.example.enu.CoordsEnum;
import com.chandler.location.example.enu.LocationSourceEnum;
import lombok.Data;

import java.util.List;

/**
 * @author hongguang.lai
 * @date 2023/5/12 10:34
 */
@Data
public class TruckHistoryLocationResp {

  /** 车牌号 */
  private String truckPlateNumber;

  /** 坐标系 */
  private CoordsEnum coords;

  /** 行驶里程 */
  private String drivingMileage;

  /** 轨迹数据 */
  private List<TruckLocationDTO> locations;

  /** 轨迹停留点 */
  private List<TruckLocationStopPointResp> stopPoints;

  private LocationSourceEnum locationSource;

  private int locationSize;

  public int getLocationSize() {
    return CollectionUtil.isEmpty(locations) ? 0 : locations.size();
  }
}
