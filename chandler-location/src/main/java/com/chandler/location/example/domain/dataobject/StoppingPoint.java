package com.chandler.location.example.domain.dataobject;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * stopping_point
 */
@Data
@Table(name="stopping_point")
public class StoppingPoint implements Serializable {
    @Id
    private Long id;

    private Long taskId;

    private Long stopMinutes;

    private Date startTime;

    private Date endTime;

    private String stopTime;

    private String address;

    private String lonAndLat;

    private Integer distance;

    private Integer distance2;

    private Integer doorPoint;
    private String province;
    private String city;
    private String district;
    private String township;

    private static final long serialVersionUID = 1L;
}