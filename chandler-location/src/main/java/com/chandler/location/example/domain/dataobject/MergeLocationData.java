package com.chandler.location.example.domain.dataobject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

import javax.persistence.Table;

/**
 * merge_location_data
 */
@Table(name="merge_location_data")
@Data
public class MergeLocationData implements Serializable {
    private Long id;

    private String lpn;

    private String lonAndLat;

    private LocalDateTime reportTime;

    private String direction;

    private Integer speed;

    private Integer sourceType;

    private Integer status;

    private static final long serialVersionUID = 1L;
}