package com.chandler.location.example.domain.dataobject;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * location
 */
@Data
@Table(name="location")
public class Location implements Serializable {
    @Id
    private Long id;

    private Long taskId;
    private String longitude;

    private String latitude;

    private String lpn;

    private Date time;

    /** 定位方向编码 范围：0-360 */
    private String direction;

//    private Integer inPolygon;

    private static final long serialVersionUID = 1L;
}