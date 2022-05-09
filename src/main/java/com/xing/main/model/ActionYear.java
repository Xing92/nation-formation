package com.xing.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ActionYear {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@ManyToOne
	private Year year;

	@ManyToOne
	private Kingdom kingdomOrigin;

	@ManyToOne
	private Kingdom kingdomDestination;

	public Year getYear() {
		return year;
	}

	public void setYear(Year year) {
		this.year = year;
	}

	public Kingdom getKingdomOrigin() {
		return kingdomOrigin;
	}

	public void setKingdomOrigin(Kingdom kingdomOrigin) {
		this.kingdomOrigin = kingdomOrigin;
	}

	public Kingdom getKingdomDestination() {
		return kingdomDestination;
	}

	public void setKingdomDestination(Kingdom kingdomDestination) {
		this.kingdomDestination = kingdomDestination;
	}

	public Integer getId() {
		return id;
	}

}
