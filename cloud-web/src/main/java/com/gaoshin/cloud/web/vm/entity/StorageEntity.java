package com.gaoshin.cloud.web.vm.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class StorageEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
