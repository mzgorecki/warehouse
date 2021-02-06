package com.demo.warehouse.model;

import java.util.UUID;

import javax.persistence.Embeddable;

import com.demo.warehouse.model.entity.EntityUUID;

@Embeddable
public class AdEntryID extends EntityUUID {

	public AdEntryID() {
	}

	public AdEntryID(UUID value) {
		super(value);
	}
}
