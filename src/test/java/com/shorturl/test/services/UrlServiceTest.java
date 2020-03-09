package com.shorturl.test.services;

import static com.shorturl.util.Constants.URL_CODE_SIZE;
import static com.shorturl.util.Constants.URL_NOT_FOUND_FOR_CODE;
import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.shorturl.domain.ShortUrl;
import com.shorturl.dto.ShortUrlDTO;
import com.shorturl.helpers.UrlShortnerHelper;
import com.shorturl.repositories.ShortUrlRepository;
import com.shorturl.services.ShortUrlService;
import com.shorturl.services.exceptions.ShortUrlNotFoundException;

public class UrlServiceTest {

	@InjectMocks
	private ShortUrlService service;

	@Mock
	private ShortUrlRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void whenCodeExistsReturnsUrl() {
		// Given
		String existingCode = "3077yW";

		ShortUrl existingUrl = new ShortUrl(existingCode, "http://www.neueda.com");
		Optional<ShortUrl> optional = Optional.of(existingUrl);
		Mockito.when(repository.findById(existingCode)).thenReturn(optional);

		// When
		ShortUrl url = service.find(existingCode);

		// Then
		Assert.assertEquals(existingUrl, url);
	}

	@Test(expected = ShortUrlNotFoundException.class)
	public void whenCodeDoesNotExistThrowsUrlNotFoundException() {
		// Given
		String notExistingCode = "2YpwKFJ";

		Mockito.when(repository.findById(notExistingCode))
				.thenThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		// When
		service.find(notExistingCode);
	}

	@Test
	public void whenLongUrlDoesNotExistSaveItAndReturnNewUrlCode() {
		// Given
		String code = null;
		String notExistingLongUrl = "http://www.neueda.com";
		ShortUrl urlToCreate = new ShortUrl(code, notExistingLongUrl);

		int startIndex = 0;
		int endIndex = startIndex + URL_CODE_SIZE - 1;
		String notExistingCode = UrlShortnerHelper.generateShortURL(notExistingLongUrl, startIndex, endIndex);

		Mockito.when(repository.findById(notExistingCode))
				.thenThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		ShortUrl url = new ShortUrl(notExistingCode, notExistingLongUrl);
		Mockito.when(repository.save(url)).thenReturn(url);

		// When
		ShortUrl newUrl = service.findOrCreate(urlToCreate);

		// Then
		Assert.assertEquals(url, newUrl);
	}

	@Test
	public void whenLongUrlExistsFindItAndReturnExistingUrlCode() {
		// Given
		String code = null;
		String existingLongUrl = "http://www.neueda.com";
		ShortUrl urlToCreate = new ShortUrl(code, existingLongUrl);

		int startIndex = 0;
		int endIndex = startIndex + URL_CODE_SIZE - 1;
		String existingCode = UrlShortnerHelper.generateShortURL(existingLongUrl, startIndex, endIndex);

		ShortUrl existingUrl = new ShortUrl(existingCode, existingLongUrl);
		Optional<ShortUrl> optional = Optional.of(existingUrl);
		Mockito.when(repository.findById(existingCode)).thenReturn(optional);

		// When
		ShortUrl newUrl = service.findOrCreate(urlToCreate);

		// Then
		assertEquals(existingUrl, newUrl);
	}

	@Test
	public void whenLongUrlDoesNotExistButGeneratedCodeExistsThenSaveANewCodeAndReturnIt() {
		// Given
		String code = null;
		String notExistingLongUrl = "http://www.google.com";
		ShortUrl urlToCreate = new ShortUrl(code, notExistingLongUrl);

		int startIndex = 0;
		int endIndex = startIndex + URL_CODE_SIZE - 1;
		String existingCode = UrlShortnerHelper.generateShortURL(notExistingLongUrl, startIndex, endIndex);

		ShortUrl existingUrl = new ShortUrl(existingCode, "http://www.neueda.com");
		Optional<ShortUrl> optional = Optional.of(existingUrl);
		Mockito.when(repository.findById(existingCode)).thenReturn(optional);

		startIndex = startIndex + 1;
		endIndex = endIndex + 1;
		String notExistingCode = UrlShortnerHelper.generateShortURL(notExistingLongUrl, startIndex, endIndex);

		Mockito.when(repository.findById(notExistingCode))
				.thenThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		ShortUrl url = new ShortUrl(notExistingCode, notExistingLongUrl);
		Mockito.when(repository.save(url)).thenReturn(url);

		// When
		ShortUrl newUrl = service.findOrCreate(urlToCreate);

		// Then
		Assert.assertEquals(url, newUrl);
	}

	@Test
	public void whenUpdatinAnUrlVerifyThatRepositorySaveIsCalled() {
		// Given
		String existingCode = "3077yW";
		String existingLongUrl = "http://www.neueda.com";
		ShortUrl existingUrl = new ShortUrl(existingCode, existingLongUrl);

		Optional<ShortUrl> optional = Optional.of(existingUrl);
		Mockito.when(repository.findById(existingCode)).thenReturn(optional);
		
		// When
		service.update(existingUrl);

		// Then
		Mockito.verify(repository).save(existingUrl);
	}
	
	@Test
	public void whenDeletingAnUrlVerifyThatRepositoryDeleteByIdIsCalled() {
		// Given
		String existingCode = "3077yW";

		ShortUrl existingUrl = new ShortUrl(existingCode, "http://www.neueda.com");
		Optional<ShortUrl> optional = Optional.of(existingUrl);
		Mockito.when(repository.findById(existingCode)).thenReturn(optional);
		
		// When
		service.remove(existingCode);

		// Then
		Mockito.verify(repository).deleteById(existingCode);
	}
	
	@Test
	public void whenBuildingUrlFromDtoReturnAnUrlWithSameCodeAndLongUrl() {
		// Given
		String existingCode = "3077yW";
		String existingLongUrl = "http://www.neueda.com";

		// When
		ShortUrlDTO urlDto = new ShortUrlDTO(existingCode, existingLongUrl);
		ShortUrl url = service.fromDTO(urlDto);

		// Then
		ShortUrl expectedUrl = new ShortUrl(existingCode, existingLongUrl);
		assertEquals(expectedUrl, url);
	}
}