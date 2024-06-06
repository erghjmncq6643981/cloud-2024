package com.chandler.location.example.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 高德地图GEO接口响应
 *
 * @author hongguang.lai
 * @date 2023/1/5 9:26
 */
@Data
public class AmapGeoRespDTO {

  private static final String SUCCESS = "1";

  private String status;

  private String info;

  private Address regeocode;

  public boolean isSuccess() {
    return SUCCESS.equals(status);
  }

  @Data
  public static class Address {
    @JsonProperty("formatted_address")
    private String address;
    @JsonProperty("addressComponent")
    private AddressComponent addressComponent;
  }

  @Data
  public static class AddressComponent {
    @JsonProperty("province")
    private String province;
    @JsonProperty("city")
    private String city;
    @JsonProperty("district")
    private String district;
    @JsonProperty("township")
    private String township;
  }
}
