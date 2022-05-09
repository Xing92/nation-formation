package com.xing.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.model.Kingdom;
import com.xing.main.model.User;
import com.xing.main.repository.KingdomRepository;
import com.xing.main.repository.UserRepository;

@Controller
@RequestMapping(path = "/api/kingdom")
public class KingdomController {
	@Autowired
	private KingdomRepository kingdomRepository;
	@Autowired
	private UserRepository userRepository;

	@PostMapping(path = "/add")
	public @ResponseBody String addNewKingdom(@RequestParam String name, Authentication auth) {

		User user = userRepository.findByUsername(auth.getName());
		if (user.getKingdom() != null) {
			return "This User already has a Kingdom";
		}

		Kingdom kingdom = new Kingdom();
		kingdom.setName(name);
		kingdom.setCurrentTurn(1);
		kingdom.setMaxTurns(10);
		kingdom.setPopulation(100);
		kingdom.setPopulationUnassigned(100);
//		kingdom.setUser(user);
		kingdom = kingdomRepository.save(kingdom);
		user.setKingdom(kingdom);
		user = userRepository.save(user);
		return "Created Kingdom:" + kingdom;
	}

	@GetMapping(path = "/{name}/turn")
	public @ResponseBody Kingdom playKingdomTurn(@PathVariable String name) {
		Kingdom kingdom = getSingleKingdom(name);
		int currentTurn = kingdom.getCurrentTurn();
		if (currentTurn >= kingdom.getMaxTurns()) {
			return kingdom;
		}

		int gold = kingdom.getGold();
		int miners = kingdom.getMiners();
		int population = kingdom.getPopulation();
		gold += miners * 10 - population;
		kingdom.setGold(gold);
		kingdom.setCurrentTurn(++currentTurn);
		kingdomRepository.save(kingdom);

		return kingdom;
	}

	@GetMapping(path = "/{name}")
	public @ResponseBody Kingdom getKingdom(@PathVariable String name) {
		Kingdom kingdom = getSingleKingdom(name);
		return kingdom;
	}

	@PostMapping(path = "/{name}/changeMiners")
	public @ResponseBody String changeMiners(@PathVariable String name, @RequestParam int minersDelta) {
		Kingdom kingdom = getSingleKingdom(name);
		int minersBefore = kingdom.getMiners();
		int populationUnassigned = kingdom.getPopulationUnassigned();
		if (minersDelta > populationUnassigned) {
			minersDelta = populationUnassigned;
		}
		if (minersDelta < -minersBefore) {
			minersDelta = -minersBefore;
		}

		kingdom.setMiners(minersBefore + minersDelta);
		kingdom.setPopulationUnassigned(populationUnassigned - minersDelta);
		kingdomRepository.save(kingdom);
		return "Changed Miners by:" + minersDelta;
	}

	@GetMapping(path = "/")
	public @ResponseBody Iterable<Kingdom> getAllKingdoms() {
		return kingdomRepository.findAll();
	}

	private Kingdom getSingleKingdom(String kingdomName) {
		List<Kingdom> kingdoms = kingdomRepository.findByName(kingdomName);
		if (kingdoms.size() == 0) {
			return null;
		}
		Kingdom kingdom = kingdoms.get(0);
		return kingdom;
	}

}