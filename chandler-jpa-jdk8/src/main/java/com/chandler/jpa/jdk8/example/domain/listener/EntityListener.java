package com.chandler.jpa.jdk8.example.domain.listener;

import com.chandler.jpa.jdk8.example.domain.dataobject.BaseEntity;
import javax.persistence.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

public class EntityListener {

	@PrePersist
	void onPrePersist(BaseEntity o) {
		if (null==o.getCreationTime()) {
			o.setCreationTime(new Date());
		}
		if (null==o.getLastUpdateTime()) {
			o.setLastUpdateTime(new Date());
		}
		if (StringUtils.isEmpty(o.getCreatedBy())) {
			o.setCreatedBy(getUser());
		}
		if (StringUtils.isEmpty(o.getUpdatedBy())) {
			o.setUpdatedBy(getUser());
		}
	}

	@PostPersist
	void onPostPersist(BaseEntity o) {
	}

	@PostLoad
	void onPostLoad(BaseEntity o) {
	}

	@PreUpdate
	void onPreUpdate(BaseEntity o) {
		o.setLastUpdateTime(new Date());
		if (StringUtils.isNotEmpty(getUser())) {
			o.setUpdatedBy(getUser());
		}
	}

	@PostUpdate
	void onPostUpdate(BaseEntity o) {
	}

	@PreRemove void onPreRemove(BaseEntity o) {
	}

	@PostRemove void onPostRemove(BaseEntity o) {
	}

	String getUser(){
		return "";
	}
}
