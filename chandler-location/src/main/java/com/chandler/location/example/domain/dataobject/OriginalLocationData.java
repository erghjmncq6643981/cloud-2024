package com.chandler.location.example.domain.dataobject;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;

import javax.persistence.Table;

/**
 * original_location_data
 */
@Table(name="original_location_data")
@Data
public class OriginalLocationData implements Serializable {
    private Long id;

    private String lpn;

    private String lonAndLat;

    private LocalDateTime reportTime;

    private String direction;

    private Integer speed;

    private Integer sourceType;

    private Integer status;
    private Integer distrustLevel;

    private static final long serialVersionUID = 1L;
}