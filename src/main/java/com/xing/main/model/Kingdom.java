package com.xing.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Kingdom {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@OneToOne
	private KingdomDetails kingdomDetails;

	@Column(unique = true)
	private String name;
	private int maxTurns;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMaxTurns(int maxTurns) {
		this.maxTurns = maxTurns;
	}

	public int getMaxTurns() {
		return maxTurns;
	}

	public KingdomDetails getKingdomDetails() {
		return kingdomDetails;
	}

	public void setKingdomDetails(KingdomDetails kingdomDetails) {
		this.kingdomDetails = kingdomDetails;
	}

}