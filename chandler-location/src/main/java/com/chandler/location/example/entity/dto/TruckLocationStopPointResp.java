package com.chandler.location.example.entity.dto;

import lombok.Data;

/**
 * @author hongguang.lai
 * @date 2023/12/13 9:52
 */
@Data
public class TruckLocationStopPointResp {

  /** 停留开始时间 */
  private String beginTime;

  /** 停留结束时间 */
  private String endTime;

  /** 停留持续时间，中文描述 */
  private String stopTime;

  /** 停留持续分钟 */
  private Long stopMinutes;

  /** 停留位置 */
  private String lonAndLat;
}
