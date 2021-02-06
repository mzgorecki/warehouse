package com.demo.warehouse.model.entity;

import static com.demo.Assertions.isNotNull;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@MappedSuperclass
public abstract class EntityUUID implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	private final UUID value;

	protected EntityUUID() {
		this.value = null;
	}

	public EntityUUID(UUID value) {
		isNotNull(value, "UUID cannot be null");
		this.value = value;
	}
}
