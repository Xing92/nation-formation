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
	

	@GetMapping(path = "/year/create")
	public ResponseEntity<Year> createOneYear() {
		Year year = new Year();
		year.setYearNumer(1);
		year = yearRepository.save(year);
		return new ResponseEntity<Year>(year, HttpStatus.OK);
	}

	@GetMapping(path = "/year/play")
	public @ResponseBody String playYear() {

		Year currentYear = getCurrentYear();
		List<Kingdom> kingdoms = getAllKingdoms();
		computeAllKingdoms(kingdoms);
		Year year = computeYear(currentYear);
		Year nextYear = createNextYear(currentYear);

		return "Played year number:" + year.getYearNumer();
	}

	@PostMapping(path = "/year/addKingdom")
	public @ResponseBody String addKingdomToYear(@RequestParam Kingdom kingdom) {

		Year year = getCurrentYear();
//		year.addKingdom(kingdom);
		yearRepository.save(year);
		return "Kingdom added:" + kingdom.getName() + ", to year:" + year.getYearNumer();
	}

	private Year createNextYear(Year currentYear) {
		Year newYear = new Year();
		newYear.setYearNumer(currentYear.getYearNumer() + 1);
		yearRepository.save(newYear);
		return newYear;
	}

	private Year computeYear(Year currentYear) {
		// TODO Auto-generated method stub
		return null;
	}

	private void computeAllKingdoms(List<Kingdom> kingdoms) {
		// TODO Auto-generated method stub
	}

	private List<Kingdom> getAllKingdoms() {
		Iterable<Kingdom> kingdoms = kingdomRepository.findAll();
		List<Kingdom> kingdomList = StreamSupport.stream(kingdoms.spliterator(), false).collect(Collectors.toList());
		return kingdomList;
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
