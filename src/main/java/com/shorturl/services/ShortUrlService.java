package com.shorturl.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shorturl.domain.ShortUrl;
import com.shorturl.dto.ShortUrlDTO;
import com.shorturl.helpers.UrlShortnerHelper;
import com.shorturl.repositories.ShortUrlRepository;
import com.shorturl.services.exceptions.ShortUrlNotFoundException;
import com.shorturl.util.Constants;

@Service
public class ShortUrlService {

	Logger logger = LoggerFactory.getLogger(ShortUrlService.class);

	@Autowired
	private ShortUrlRepository repository;

	public ShortUrl find(String urlCode) {

		final String code = urlCode.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.FINDING_URL_BY_CODE, code);

		Optional<ShortUrl> optional = repository.findById(code);
		return optional.orElseThrow(() -> new ShortUrlNotFoundException(Constants.URL_NOT_FOUND_FOR_CODE + code));
	}

	private ShortUrl recursiveInsert(String longUrl, int startIndex, int endIndex) {

		longUrl = longUrl.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.RECURSIVE_INSERT, longUrl);

		String code = UrlShortnerHelper.generateShortURL(longUrl, startIndex, endIndex);

		ShortUrl url;
		try {
			url = find(code);

			if (!url.getLongUrl().equals(longUrl)) {
				logger.info(Constants.FOUND_DIFFERENT_URLS_FOR_SAME_CODE, code);

				url = recursiveInsert(longUrl, startIndex + 1, endIndex + 1);
			}
		} catch (ShortUrlNotFoundException e) {
			logger.warn(Constants.URL_NOT_FOUND_CREATING_NEW_ONE, code, e);

			url = repository.save(new ShortUrl(code, longUrl));
		}

		return url;
	}

	public ShortUrl findOrCreate(ShortUrl url) {
		String longUrl = url.getLongUrl();

		logger.info(Constants.FINDING_OR_CREATING_URL, longUrl);

		int startIndex = 0;
		int endIndex = startIndex + Constants.URL_CODE_SIZE - 1;

		return recursiveInsert(longUrl, startIndex, endIndex);
	}

	public ShortUrl update(ShortUrl url) {
		logger.info(Constants.UPDATING_URL, url);

		ShortUrl foundUrl = find(url.getCode());
		url.setCreatedAt(foundUrl.getCreatedAt());

		return repository.save(url);
	}

	public void remove(String code) {

		code = code.replaceAll(Constants.PATTERN_BREAKING_CHARACTERS, "_");

		logger.info(Constants.REMOVING_URL, code);

		find(code);
		repository.deleteById(code);
	}

	public ShortUrl fromDTO(ShortUrlDTO urlDto) {
		return new ShortUrl(urlDto.getCode(), urlDto.getLongUrl());
	}

	public List<ShortUrl> findAllUrls() {
		List<ShortUrl> urlList = new ArrayList<ShortUrl>();
		urlList =repository.findAll();
		return urlList;
	}
}
