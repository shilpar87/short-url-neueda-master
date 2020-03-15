package com.shorturl.resources;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.shorturl.domain.ShortUrlStatistic;
import com.shorturl.domain.ShortUrl;
import com.shorturl.dto.ShortUrlDTO;
import com.shorturl.services.ShortUrlService;
import com.shorturl.services.StatisticService;
import com.shorturl.util.Constants;

@RestController
@RequestMapping(value = "/urls")
public class ShortUrlResources {
	Logger logger = LoggerFactory.getLogger(ShortUrlResources.class);

	@Autowired
	private ShortUrlService service;

	@Autowired
	private StatisticService statisticService;

	@GetMapping(path = "/{code}")
	public ResponseEntity<ShortUrl> findAndRedirect(@PathVariable String code,
			@RequestHeader Map<String, String> headersMap) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.FINDING_URL_FOR_REDIRECTING, code);

		ShortUrl url = service.find(code);

		ShortUrlStatistic statistic = statisticService.mapFrom(headersMap, url);
		statisticService.create(statistic);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(url.getLongUrl()));

		return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
	}

	@GetMapping(path = "/{code}/longUrl")
	public ResponseEntity<ShortUrl> find(@PathVariable String code) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.FINDING_LONG_URL, code);

		ResponseEntity<ShortUrl> responseEntity;

		ShortUrl url = service.find(code);
		responseEntity = ResponseEntity.ok().body(url);

		return responseEntity;
	}

	@PostMapping(value = "/")
	public ResponseEntity<ShortUrl> findOrCreate(@Valid @RequestBody ShortUrlDTO urlDto) {
		logger.info(Constants.FINDING_OR_CREATING_URL, urlDto);

		ShortUrl url = service.fromDTO(urlDto);
		url = service.findOrCreate(url);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{code}").buildAndExpand(url.getCode())
				.toUri();

		return ResponseEntity.created(uri).build();
	}

	@GetMapping(path = "/urldetails")
	public ResponseEntity<List<ShortUrl>> findAllUrls() {
		ResponseEntity<List<ShortUrl>> responseEntity;
		List<ShortUrl> urls = service.findAllUrls();
		responseEntity = ResponseEntity.ok().body(urls);
		return responseEntity;
	}

	@PutMapping(value = "/{code}")
	public ResponseEntity<ShortUrl> update(@Valid @RequestBody ShortUrlDTO urlDto, @PathVariable String code) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.UPDATING_URL, urlDto);

		urlDto.setCode(code);
		ShortUrl url = service.fromDTO(urlDto);

		service.update(url);

		return ResponseEntity.noContent().build();
	}

	@DeleteMapping(value = "/{code}")
	public ResponseEntity<ShortUrl> delete(@PathVariable String code) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.REMOVING_URL, code);

		service.remove(code);

		return ResponseEntity.noContent().build();
	}
}