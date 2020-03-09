package com.shorturl.resources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shorturl.dto.StatisticsSummaryDTO;
import com.shorturl.services.StatisticService;
import com.shorturl.util.Constants;

@RestController
@RequestMapping(value = "/statistics")
public class ShortUrlStatisticResources {
	Logger logger = LoggerFactory.getLogger(ShortUrlStatisticResources.class);

	@Autowired
	private StatisticService service;

	@GetMapping(path = "/summary")
	public ResponseEntity<StatisticsSummaryDTO> getSummary() {
		logger.info(Constants.GETTING_STATISTICS_SUMMARY);

		StatisticsSummaryDTO summary = service.getStatisticsSummary();

		return ResponseEntity.ok().body(summary);
	}

	@GetMapping(path = "/summary/{code}")
	public ResponseEntity<StatisticsSummaryDTO> getSummaryByCode(@PathVariable String code) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");
		
		logger.info(Constants.GETTING_STATISTICS_SUMMARY_BY_CODE, code);

		StatisticsSummaryDTO summary = service.getStatisticsSummaryByCode(code);

		return ResponseEntity.ok().body(summary);
	}

}