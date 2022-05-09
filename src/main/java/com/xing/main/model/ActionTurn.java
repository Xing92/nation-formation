package com.xing.main.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ActionTurn {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private ActionType actionType;
	
	private Kingdom kingdom;
	
	private int delta;
	
	private int turn;

}
