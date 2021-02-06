package com.demo.warehouse.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;

import com.demo.warehouse.model.entity.UUIDAggregateRoot;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity(name = "ad_entry")
public class AdEntry extends UUIDAggregateRoot<AdEntryID> {

	@Column(name = "data_source", length = 32, nullable = false)
	private String dataSource;

	@Column(nullable = false)
	private String campaign;

	@Column(nullable = false)
	private LocalDate daily;

	@Column(nullable = false)
	private Long clicks;

	@Column(nullable = false)
	private Long impressions;

	@Formula("round(clicks::numeric / impressions::numeric * 100, 2)")
	private Double ctr;

	AdEntry() {
	}

	public AdEntry(AdEntryID id, String dataSource, String campaign, LocalDate daily, Long clicks, Long impressions) {
		super(id);
		this.dataSource = dataSource;
		this.campaign = campaign;
		this.daily = daily;
		this.clicks = clicks;
		this.impressions = impressions;
	}

	@Transient
	public Double getCtr() {
		return ctr;
	}
}