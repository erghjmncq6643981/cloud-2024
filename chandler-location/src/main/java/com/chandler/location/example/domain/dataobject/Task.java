package com.chandler.location.example.domain.dataobject;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * task
 */
@Data
@Table(name="task")
public class Task implements Serializable {
    @Id
    private Long id;

    private String lpn;

    private String secondLpn;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String status;

    private String parseStatus;
    private String drivingMileage;

    private static final long serialVersionUID = 1L;
}