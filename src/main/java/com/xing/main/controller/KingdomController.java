package com.xing.main.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.exception.WrongDataException;
import com.xing.main.model.ActionTurn;
import com.xing.main.model.ActionType;
import com.xing.main.model.Kingdom;
import com.xing.main.model.KingdomDetails;
import com.xing.main.model.User;
import com.xing.main.repository.ActionTurnRepository;
import com.xing.main.repository.KingdomDetailsRepository;
import com.xing.main.repository.KingdomRepository;
import com.xing.main.repository.UserRepository;
import com.xing.main.repository.YearRepository;
import com.xing.main.util.Log;

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
	@Autowired
	private ActionTurnRepository actionTurnRepository;

	@PostMapping(path = "/create")
	public ResponseEntity<Kingdom> createNewKingdom() {

		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(userName);
		if (user.getKingdom() != null) {
			return new ResponseEntity<Kingdom>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		KingdomDetails kingdomDetails = new KingdomDetails();
		kingdomDetails.setTurn(1);
		kingdomDetails.setPopulation(1000);
		kingdomDetails.setSize(100);
		kingdomDetails.setYear(yearRepository.findLatestYear());
		kingdomDetails = kingdomDetailsRepository.save(kingdomDetails);

		Kingdom kingdom = new Kingdom();
		kingdom.setName(userName);
		kingdom.setMaxTurns(10);
		kingdom.setKingdomDetails(kingdomDetails);
		kingdom = kingdomRepository.save(kingdom);
		user.setKingdom(kingdom);
		user = userRepository.save(user);
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@GetMapping(path = "/me/turn")
	public ResponseEntity<Kingdom> playKingdomTurn() {

		Kingdom kingdom = getMeKingdom();
		KingdomDetails kingdomDetails = kingdom.getKingdomDetails();
		if (kingdomDetails.getTurn() >= kingdom.getMaxTurns()) {
			return new ResponseEntity<Kingdom>(kingdom, HttpStatus.NOT_ACCEPTABLE);
		}

		int currentTurn = kingdomDetails.getTurn();
		int gold = kingdomDetails.getGold();
		int miners = kingdomDetails.getMiners();
		int population = kingdomDetails.getPopulation();

		gold += miners * 10 - population;
		if (gold < 0) {
			population += (gold / 10);
			gold = 0;
		} else {
			population += (kingdomDetails.getSize() * 10 - population) / 2;
		}
		kingdomDetails.setPopulation(population);
		kingdomDetails.setGold(gold);
		kingdomDetails.setTurn(currentTurn + 1);
		kingdomDetails = kingdomDetailsRepository.save(kingdomDetails);
		kingdom.setKingdomDetails(kingdomDetails);
		kingdom = kingdomRepository.save(kingdom);
		Log.info("Turn played for kingdom:" + kingdom.getName());

		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@GetMapping(path = "/{name}")
	public ResponseEntity<Kingdom> getKingdom(@PathVariable String name) {
		Kingdom kingdom = kingdomRepository.findByName(name);
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@GetMapping(path = "/me")
	public ResponseEntity<Kingdom> getKingdomMe() {
		Kingdom kingdom = getMeKingdom();
		return new ResponseEntity<Kingdom>(kingdom, HttpStatus.OK);
	}

	@PostMapping(path = "/me/action")
	public ResponseEntity<String> doAction(@RequestBody ActionTurn actionTurn) {
		Kingdom kingdom = getMeKingdom();
		ActionType actionType = actionTurn.getActionType();
		KingdomDetails kingdomDetails = kingdom.getKingdomDetails();
		if (kingdomDetails.getTurn() != actionTurn.getTurn()) {
			return new ResponseEntity<String>(
					String.format("Turn from request does not match turn from ActionTurn. Actual:%s, Requested:%s",
							kingdomDetails.getTurn(), actionTurn.getTurn()),
					HttpStatus.BAD_REQUEST);
		}
		actionTurn.setKingdom(kingdom);
		try {
			switch (actionType) {
			case CHANGE_MINERS:
				kingdomDetails = changeMiners(kingdomDetails, actionTurn.getDelta());
				break;
			case CHANGE_ARMY:
				kingdomDetails = changeArmy(kingdomDetails, actionTurn.getDelta());
				break;
			case CHANGE_SIZE:
				kingdomDetails = changeSize(kingdomDetails, actionTurn.getDelta());
				break;
			}
		} catch (WrongDataException wde) {
			Log.error("Didn't manage to execute ActionTurn.", wde);
			return new ResponseEntity<String>(wde.getMessage(), HttpStatus.BAD_REQUEST);
		}

		kingdomDetailsRepository.save(kingdomDetails);
		actionTurnRepository.save(actionTurn);
		return new ResponseEntity<String>("ActionTurn SUCCESS", HttpStatus.OK);
	}

	private KingdomDetails changeSize(KingdomDetails kingdomDetails, int delta) {

		if (delta <= 0) {
			throw new WrongDataException("Size change request can't be less or equal to 0.");
		}

		int startSize = kingdomDetails.getSize();
		int endSize = startSize + delta;
		double geometricR = 1.01;
		double geometricA = 10;
		double sumBig = geometricA * (Math.pow(geometricR, endSize) - 1) / (geometricR - 1);
		double sumSmall = geometricA * (Math.pow(geometricR, startSize) - 1) / (geometricR - 1);
		double sumFinal = sumBig - sumSmall;

		if (kingdomDetails.getGold() < sumFinal) {
			throw new WrongDataException(
					"Too little gold. Actual:" + kingdomDetails.getGold() + ", Required:" + sumFinal);
		}

		kingdomDetails.setGold(kingdomDetails.getGold() - (int) sumFinal);
		kingdomDetails.setSize(kingdomDetails.getSize() + delta);
		return kingdomDetails;
	}

	private KingdomDetails changeArmy(KingdomDetails kingdomDetails, int delta) {

		if (getUnassignedPopulation(kingdomDetails) < delta) {
			throw new WrongDataException("Too little unassigned population. Actual:"
					+ getUnassignedPopulation(kingdomDetails) + ", Requested:" + delta);
		}
		if (delta < 0 && kingdomDetails.getArmy() + delta < 0) {
			throw new WrongDataException(
					"Too little current army. Actual:" + kingdomDetails.getArmy() + ", Requested:" + delta);
		}
		if (kingdomDetails.getGold() < delta * 100) {
			throw new WrongDataException(
					"Too little gold. Actual:" + kingdomDetails.getGold() + ", Required:" + delta * 100);
		}

		kingdomDetails.setArmy(kingdomDetails.getArmy() + delta);
		kingdomDetails.setGold(kingdomDetails.getGold() - delta * 100);
		return kingdomDetails;
	}

	private KingdomDetails changeMiners(KingdomDetails kingdomDetails, int delta) {

		if (getUnassignedPopulation(kingdomDetails) < delta) {
			throw new WrongDataException("Too little unassigned population. Actual:"
					+ getUnassignedPopulation(kingdomDetails) + ", Requested:" + delta);
		}
		if (delta < 0 && kingdomDetails.getMiners() + delta < 0) {
			throw new WrongDataException(
					"Too little current miners. Actual:" + kingdomDetails.getMiners() + ", Requested:" + delta);
		}

		kingdomDetails.setMiners(kingdomDetails.getMiners() + delta);
		return kingdomDetails;
	}

	private int getUnassignedPopulation(KingdomDetails kingdomDetails) {
		return kingdomDetails.getPopulation() - kingdomDetails.getMiners() - kingdomDetails.getArmy();
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

	private Kingdom getMeKingdom() {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepository.findByUsername(userName);
		return user.getKingdom();
	}
}