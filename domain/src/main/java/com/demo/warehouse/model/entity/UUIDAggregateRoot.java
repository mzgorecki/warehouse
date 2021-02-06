package com.demo.warehouse.model.entity;

import static com.demo.Assertions.isNotNull;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(exclude = { "version" })
@MappedSuperclass
public abstract class UUIDAggregateRoot<U extends EntityUUID> {

	@Id
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "id")) })
	private U id;

	@Version
	@Column(name = "OPTLOCK")
	private Integer version;

	protected UUIDAggregateRoot() {
	}

	protected UUIDAggregateRoot(U id) {
		isNotNull(id, "ID cannot be null");
		this.id = id;
	}

	public U getId() {
		return this.getUuid();
	}

	public U getUuid() {
		isNotNull(id, "ID cannot be null");
		return this.id;
	}

	protected Integer getVersion() {
		return this.version;
	}
}
