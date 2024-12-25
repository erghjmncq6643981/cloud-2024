package com.chandler.jpa.jdk8.example.domain.dataobject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@MappedSuperclass
public class BaseVersionEntity extends BaseEntity{

    @Column(name = "version")
    @Version
    protected Long version;

}
