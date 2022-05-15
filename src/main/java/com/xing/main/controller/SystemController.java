package com.xing.main.controller;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xing.main.model.Kingdom;
import com.xing.main.model.KingdomDetails;
import com.xing.main.model.Year;
import com.xing.main.repository.KingdomRepository;
import com.xing.main.repository.YearRepository;

@Controller
@RequestMapping(path = "/api/system")
public class SystemController {
	@Autowired
	private KingdomRepository kingdomRepository;
	@Autowired
	private YearRepository yearRepository;

	@GetMapping(path = "/year/latest")
	public ResponseEntity<Year> getLatestYear() {
		Year year = yearRepository.findLatestYear();
		return new ResponseEntity<Year>(year, HttpStatus.OK);
	}

	@GetMapping(path = "/year/createfirst")
	public ResponseEntity<Year> createOneYear() {
		Year year = new Year();
		year.setYearNumer(1);
		year = yearRepository.save(year);
		return new ResponseEntity<Year>(year, HttpStatus.OK);
	}

	@GetMapping(path = "/year/play")
	public @ResponseBody String playYear() {

		Year currentYear = getCurrentYear();
		Year nextYear = createNextYear(currentYear);
		List<Kingdom> kingdoms = kingdomRepository.findAll();
		computeAllKingdoms(kingdoms, nextYear);
		Year year = computeYear(currentYear);

		return "Played year number:" + year.getYearNumer();
	}

	private Year createNextYear(Year currentYear) {
		Year nextYear = new Year();
		nextYear.setYearNumer(currentYear.getYearNumer() + 1);
		yearRepository.save(nextYear);
		return nextYear;
	}

	private Year computeYear(Year currentYear) {
		// TODO: Compute attacks
		// TODO: Add attacks
		return null;
	}

	private void computeAllKingdoms(List<Kingdom> kingdoms, Year nextYear) {
		for (Kingdom kingdom : kingdoms) {
			KingdomDetails kingdomDetails = kingdom.getKingdomDetails();
			kingdomDetails.setYear(nextYear);
			kingdomDetails.setTurn(1);
		}
		
		kingdomRepository.saveAll(kingdoms);
	}

	private Year getCurrentYear() {
		Iterable<Year> years = yearRepository.findAll();
		Stream<Year> yearsStream = StreamSupport.stream(years.spliterator(), false);
		Year year = yearsStream.sorted((o1, o2) -> {
			if (o1.getYearNumer() > o2.getYearNumer())
				return 1;
			if (o1.getYearNumer() < o2.getYearNumer())
				return -1;
			return 0;
		}).findFirst().get();
		return year;
	}

}
