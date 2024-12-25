package com.chandler.jpa.example.domain.dataobject;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

@Getter @Setter
@MappedSuperclass
public class BaseVersionEntity extends BaseEntity{

    @Column(name = "version")
    @Version
    protected Long version;

}
