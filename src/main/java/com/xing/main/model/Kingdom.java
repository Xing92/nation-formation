package com.xing.main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Kingdom {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(unique = true)
	private String name;
	private int population;
	private int populationUnassigned;
	private int miners;
	private int gold;
	private int currentTurn;
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

	public void setPopulation(int population) {
		this.population = population;
	}

	public void setMiners(int miners) {
		this.miners = miners;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public void setMaxTurns(int maxTurns) {
		this.maxTurns = maxTurns;
	}

	public int getPopulation() {
		return population;
	}

	public int getMiners() {
		return miners;
	}

	public int getGold() {
		return gold;
	}

	public int getCurrentTurn() {
		return currentTurn;
	}

	public int getMaxTurns() {
		return maxTurns;
	}

	public int getPopulationUnassigned() {
		return populationUnassigned;
	}

	public void setPopulationUnassigned(int populationUnassigned) {
		this.populationUnassigned = populationUnassigned;
	}

}