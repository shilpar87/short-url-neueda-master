package com.shorturl.test.resources;

import static com.shorturl.util.Constants.URL_CODE_SIZE;
import static com.shorturl.util.Constants.URL_NOT_FOUND_FOR_CODE;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.shorturl.domain.ShortUrl;
import com.shorturl.dto.ShortUrlDTO;
import com.shorturl.helpers.UrlShortnerHelper;
import com.shorturl.services.ShortUrlService;
import com.shorturl.services.StatisticService;
import com.shorturl.services.exceptions.ShortUrlNotFoundException;

@RunWith(SpringRunner.class)
@WebMvcTest
public class UrlResourcesTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StatisticService statisticService;
	
	@MockBean
	private ShortUrlService urlService;

	@Test
	public void whenUrlCodeExistsReturnsExistingUrl() throws Exception {
		// Given
		String existingCode = "3077yW";

		ShortUrl url = new ShortUrl(existingCode, "http://www.neueda.com");

		given(urlService.find(existingCode)).willReturn(url);

		// When and Then
		this.mockMvc.perform(get("/urls/" + existingCode + "/longUrl")).andExpect(status().isOk())
				.andExpect(content().json("{'code': '3077yW','longUrl': 'http://www.neueda.com'}"));
	}

	@Test
	public void whenUrlCodeDoesNotExistReturnsNotFound() throws Exception {
		// Given
		String notExistingCode = "2YpwKFJ";

		given(urlService.find(notExistingCode))
				.willThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		// When and Then
		this.mockMvc.perform(get("/urls/" + notExistingCode + "/longUrl")).andExpect(status().isNotFound());
	}

	@Test
	public void whenUrlCodeExistsItRedirectsToExistingUrl() throws Exception {
		// Given
		String existingCode = "3077yW";

		ShortUrl url = new ShortUrl(existingCode, "http://www.neueda.com");

		given(urlService.find(existingCode)).willReturn(url);

		// When and Then
		this.mockMvc.perform(get("/urls/" + existingCode)).andExpect(status().is3xxRedirection())
				.andExpect(header().string(HttpHeaders.LOCATION, equalTo(url.getLongUrl())));

	}

	@Test
	public void whenUrlCodeDoesNotExistItDoesNotRedirectAndReturnsNotFound() throws Exception {
		// Given
		String notExistingCode = "2YpwKFJ";

		given(urlService.find(notExistingCode))
				.willThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		// When and Then
		this.mockMvc.perform(get("/urls/" + notExistingCode)).andExpect(status().isNotFound());
	}

	@Test
	public void whenLongUrlDoesNotExistItDoesNotRedirectAndReturnsNotFound() throws Exception {
		// Given
		String notExistingCode = "2YpwKFJ";

		given(urlService.find(notExistingCode))
				.willThrow(new ShortUrlNotFoundException(URL_NOT_FOUND_FOR_CODE + notExistingCode));

		// When and Then
		this.mockMvc.perform(get("/urls/" + notExistingCode)).andExpect(status().isNotFound());
	}

	@Test
	public void whenLongUrlDoesNotExistSaveItAndReturnNewUrlCode() throws Exception {
		// Given
		String code = null;
		String notExistingLongUrl = "http://www.google.com";
		ShortUrlDTO urlDtoToCreate = new ShortUrlDTO(code, notExistingLongUrl);

		int startIndex = 0;
		int endIndex = startIndex + URL_CODE_SIZE - 1;
		String newCode = UrlShortnerHelper.generateShortURL(notExistingLongUrl, startIndex, endIndex);

		ShortUrl urlToCreate = new ShortUrl(code, notExistingLongUrl);
		given(urlService.fromDTO(urlDtoToCreate)).willReturn(urlToCreate);

		ShortUrl newUrl = new ShortUrl(newCode, notExistingLongUrl);
		given(urlService.findOrCreate(urlToCreate)).willReturn(newUrl);

		String inputJson = "{\"longUrl\":\"http://www.google.com\"}";

		// When and Then
		this.mockMvc.perform(post("/urls/").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/urls/" + newCode));

	}

	@Test
	public void whenLongUrlExistsThenReturnExistingCode() throws Exception {
		// Given
		String code = null;
		String existingLongUrl = "http://www.neueda.com";
		ShortUrlDTO urlDtoToFind = new ShortUrlDTO(code, existingLongUrl);

		int startIndex = 0;
		int endIndex = startIndex + URL_CODE_SIZE - 1;
		String existingCode = UrlShortnerHelper.generateShortURL(existingLongUrl, startIndex, endIndex);

		ShortUrl urlToFind = new ShortUrl(code, existingLongUrl);
		given(urlService.fromDTO(urlDtoToFind)).willReturn(urlToFind);

		ShortUrl existingUrl = new ShortUrl(existingCode, existingLongUrl);
		given(urlService.findOrCreate(urlToFind)).willReturn(existingUrl);

		String inputJson = "{\"longUrl\":\"http://www.neueda.com\"}";

		// When and Then
		this.mockMvc.perform(post("/urls/").contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/urls/" + existingCode));

	}

	@Test
	public void whenUpdatingAnUrlReturnsStatusNoContent() throws Exception {
		// Given
		String updatingLongUrl = "http://www.neueda.com/";
		String existingCode = "3077yW";

		ShortUrlDTO urlDtoToFind = new ShortUrlDTO(existingCode, updatingLongUrl);

		ShortUrl urlToFind = new ShortUrl(existingCode, updatingLongUrl);
		given(urlService.fromDTO(urlDtoToFind)).willReturn(urlToFind);

		String inputJson = "{\"code\":\"3077yW\", \"longUrl\":\"http://www.neueda.com\"}";

		// When and Then
		this.mockMvc
				.perform(put("/urls/" + existingCode).contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson))
				.andExpect(status().isNoContent());
	}

	@Test
	public void whenRemovingAnUrlReturnsStatusNoContent() throws Exception {
		// Given
		String existingCode = "3077yW";

		// When and Then
		this.mockMvc.perform(delete("/urls/" + existingCode)).andExpect(status().isNoContent());
	}
}