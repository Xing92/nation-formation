package com.xing.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.model.Kingdom;
import com.xing.main.model.KingdomDetails;
import com.xing.main.model.User;
import com.xing.main.repository.KingdomDetailsRepository;
import com.xing.main.repository.KingdomRepository;
import com.xing.main.repository.UserRepository;
import com.xing.main.repository.YearRepository;

@Controller
@RequestMapping(path = "/api/kingdom")
public class KingdomController {

	@Autowired
	private YearRepository yearRepository;
	@Autowired
	private KingdomRepository kingdomRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private KingdomDetailsRepository kingdomDetailsRepository;

	@PostMapping(path = "/create")
	public ResponseEntity<Kingdom> createNewKingdom(@RequestParam String name) {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(userName);
		if (user.getKingdom() != null) {
			return new ResponseEntity<Kingdom>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		KingdomDetails kingdomDetails = new KingdomDetails();
		kingdomDetails.setTurn(1);
		kingdomDetails.setPopulation(1000);
		kingdomDetails.setPopulationUnassigned(1000);
		kingdomDetails.setSize(100);
		kingdomDetails.setYear(yearRepository.findLatestYear());
		kingdomDetails = kingdomDetailsRepository.save(kingdomDetails);

		Kingdom kingdom = new Kingdom();
		kingdom.setName(name);
		kingdom.setMaxTurns(10);
		kingdom.setKingdomDetails(kingdomDetails);
		kingdom = kingdomRepository.save(kingdom);
		user.setKingdom(kingdom);
		user = userRepository.save(user);
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@GetMapping(path = "/{name}/turn")
	public @ResponseBody Kingdom playKingdomTurn(@PathVariable String name) {
		Kingdom kingdom = kingdomRepository.findByName(name);
//		int currentTurn = kingdom.getCurrentTurn();
//		if (currentTurn >= kingdom.getMaxTurns()) {
//			return kingdom;
//		}
//
//		int gold = kingdom.getGold();
//		int miners = kingdom.getMiners();
//		int population = kingdom.getPopulation();
//		gold += miners * 10 - population;
//		kingdom.setGold(gold);
//		kingdom.setCurrentTurn(++currentTurn);
//		kingdomRepository.save(kingdom);

		return kingdom;
	}

	@GetMapping(path = "/{name}")
	public ResponseEntity<Kingdom> getKingdom(@PathVariable String name) {
		Kingdom kingdom = kingdomRepository.findByName(name);
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@GetMapping(path = "/me")
	public ResponseEntity<Kingdom> getKingdomMe() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(userName);
		if (user.getKingdom() != null) {
			return new ResponseEntity<Kingdom>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Kingdom kingdom = kingdomRepository.findByName(user.getUsername());
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@PostMapping(path = "/{name}/changeMiners")
	public @ResponseBody String changeMiners(@PathVariable String name, @RequestParam int minersDelta) {
		Kingdom kingdom = kingdomRepository.findByName(name);
//		int minersBefore = kingdom.getMiners();
//		int populationUnassigned = kingdom.getPopulationUnassigned();
//		if (minersDelta > populationUnassigned) {
//			minersDelta = populationUnassigned;
//		}
//		if (minersDelta < -minersBefore) {
//			minersDelta = -minersBefore;
//		}
//
//		kingdom.setMiners(minersBefore + minersDelta);
//		kingdom.setPopulationUnassigned(populationUnassigned - minersDelta);
		kingdomRepository.save(kingdom);
		return "Changed Miners by:" + minersDelta;
	}

	@GetMapping(path = "/all")
	public @ResponseBody Iterable<Kingdom> getAllKingdoms() {
		return kingdomRepository.findAll();
	}

}