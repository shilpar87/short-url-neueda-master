package com.shorturl.test.services;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;

import com.shorturl.domain.ShortUrlStatistic;
import com.shorturl.domain.ShortUrl;
import com.shorturl.dto.ShortUrlStatisticsDTO;
import com.shorturl.dto.StatisticsSummaryDTO;
import com.shorturl.repositories.StatisticRepository;
import com.shorturl.services.StatisticService;

public class StatisticServiceTest {

	@InjectMocks
	private StatisticService service;

	@Mock
	private StatisticRepository repository;

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void whenCreatingAStatisticVerifyThatRepositorySaveIsCalled() {

		// Given
		ShortUrl url = new ShortUrl("3077yW", "http://www.neueda.com");
		ShortUrlStatistic statistic = new ShortUrlStatistic("Firefox 7", "Computer", "Windows XP", url);
		
		// When
		service.create(statistic);

		// Then
		Mockito.verify(repository).save(statistic);
	}
	
	@Test
	public void whenMappingFromHeadersAndStatisticReturnsStatistic() {
		// Given
		ShortUrl url = new ShortUrl("3077yW", "http://www.neueda.com");
		
		Map<String, String> headers = new HashMap<>();
		headers.put(HttpHeaders.USER_AGENT.toLowerCase(), "Mozilla/5.0 (Windows NT 5.1; rv:7.0.1) Gecko/20100101 Firefox/7.0.1");
		
		//When
		ShortUrlStatistic statistic = service.mapFrom(headers, url);
		
		//Then
		Assert.assertEquals("Firefox 7", statistic.getBrowser());
		Assert.assertEquals("Computer", statistic.getDeviceType());
		Assert.assertEquals("Windows XP", statistic.getOperatingSystem());
		Assert.assertEquals(url, statistic.getUrl());
	}
	
	@Test
	public void whenGettingStatisticsSummaryReturnsResultsFromRepository() {
		Long numberOfHits = 3L;
		
		ShortUrlStatisticsDTO firefox = new ShortUrlStatisticsDTO("Firefox", 1L);
		ShortUrlStatisticsDTO chrome = new ShortUrlStatisticsDTO("Chrome", 2L);
		List<ShortUrlStatisticsDTO> browsers = Arrays.asList(new ShortUrlStatisticsDTO[] { firefox, chrome});

		ShortUrlStatisticsDTO computer = new ShortUrlStatisticsDTO("Computer", 1L);
		ShortUrlStatisticsDTO mobile = new ShortUrlStatisticsDTO("Mobile", 1L);
		ShortUrlStatisticsDTO tablet = new ShortUrlStatisticsDTO("Tablet", 1L);
		List<ShortUrlStatisticsDTO> deviceTypes = Arrays.asList(new ShortUrlStatisticsDTO[] { computer, mobile, tablet});

		Long totalOfLinuxHits = 3L;
		ShortUrlStatisticsDTO linux = new ShortUrlStatisticsDTO("Linux", totalOfLinuxHits);
		List<ShortUrlStatisticsDTO> operationSystems = Arrays.asList(new ShortUrlStatisticsDTO[] { linux });
		
		// Given
		Mockito.when(repository.getNumberOfHits()).thenReturn(numberOfHits);
		Mockito.when(repository.getBrowsers()).thenReturn(browsers);
		Mockito.when(repository.getDevicesTypes()).thenReturn(deviceTypes);
		Mockito.when(repository.getOperatingSystems()).thenReturn(operationSystems);

		//When
		StatisticsSummaryDTO statisticsSummary = service.getStatisticsSummary();
		
		//Then
		Assert.assertEquals(numberOfHits, statisticsSummary.getNumberOfHits());
		Assert.assertEquals(2, statisticsSummary.getBrowsers().size());
		Assert.assertEquals(3, statisticsSummary.getDevicesTypes().size());
		Assert.assertEquals(1, statisticsSummary.getOperatingSystems().size());
		Assert.assertEquals(totalOfLinuxHits, statisticsSummary.getOperatingSystems().get(0).getTotal());
	}
	
	@Test
	public void whenGettingStatisticsSummaryByCodeReturnsResultsFromRepository() {
		String code = "3077yW";
		Long numberOfHits = 3L;
		
		ShortUrlStatisticsDTO firefox = new ShortUrlStatisticsDTO("Firefox", 1L);
		ShortUrlStatisticsDTO chrome = new ShortUrlStatisticsDTO("Chrome", 2L);
		List<ShortUrlStatisticsDTO> browsers = Arrays.asList(new ShortUrlStatisticsDTO[] { firefox, chrome});

		ShortUrlStatisticsDTO computer = new ShortUrlStatisticsDTO("Computer", 1L);
		ShortUrlStatisticsDTO mobile = new ShortUrlStatisticsDTO("Mobile", 1L);
		ShortUrlStatisticsDTO tablet = new ShortUrlStatisticsDTO("Tablet", 1L);
		List<ShortUrlStatisticsDTO> deviceTypes = Arrays.asList(new ShortUrlStatisticsDTO[] { computer, mobile, tablet});

		Long totalOfLinuxHits = 2L;
		ShortUrlStatisticsDTO linux = new ShortUrlStatisticsDTO("Linux", totalOfLinuxHits);
		ShortUrlStatisticsDTO windows = new ShortUrlStatisticsDTO("Windows", 1L);
		List<ShortUrlStatisticsDTO> operationSystems = Arrays.asList(new ShortUrlStatisticsDTO[] { linux, windows });
		
		// Given
		Mockito.when(repository.getNumberOfHitsByCode(code)).thenReturn(numberOfHits);
		Mockito.when(repository.getBrowsersByCode(code)).thenReturn(browsers);
		Mockito.when(repository.getDevicesTypesByCode(code)).thenReturn(deviceTypes);
		Mockito.when(repository.getOperatingSystemsByCode(code)).thenReturn(operationSystems);

		//When
		StatisticsSummaryDTO statisticsSummary = service.getStatisticsSummaryByCode(code);
		
		//Then
		Assert.assertEquals(numberOfHits, statisticsSummary.getNumberOfHits());
		Assert.assertEquals(2, statisticsSummary.getBrowsers().size());
		Assert.assertEquals(3, statisticsSummary.getDevicesTypes().size());
		Assert.assertEquals(2, statisticsSummary.getOperatingSystems().size());
		Assert.assertEquals(totalOfLinuxHits, statisticsSummary.getOperatingSystems().get(0).getTotal());
	}

}
