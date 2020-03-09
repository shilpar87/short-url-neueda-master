package com.shorturl.test.resources;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.shorturl.dto.ShortUrlStatisticsDTO;
import com.shorturl.dto.StatisticsSummaryDTO;
import com.shorturl.services.ShortUrlService;
import com.shorturl.services.StatisticService;

@RunWith(SpringRunner.class)
@WebMvcTest
public class StatisticResourcesTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StatisticService statisticService;

	@MockBean
	private ShortUrlService urlService;

	@Test
	public void whenGettingStatisticsReturnsTheExistingOnes() throws Exception {
		// Given
		Long numberOfHits = 3L;

		ShortUrlStatisticsDTO firefox = new ShortUrlStatisticsDTO("Firefox", 1L);
		ShortUrlStatisticsDTO chrome = new ShortUrlStatisticsDTO("Chrome", 2L);
		List<ShortUrlStatisticsDTO> browsers = Arrays.asList(new ShortUrlStatisticsDTO[] { firefox, chrome });

		ShortUrlStatisticsDTO computer = new ShortUrlStatisticsDTO("Computer", 3L);
		List<ShortUrlStatisticsDTO> devicesTypes = Arrays.asList(new ShortUrlStatisticsDTO[] { computer });

		ShortUrlStatisticsDTO linux = new ShortUrlStatisticsDTO("Linux", 3L);
		List<ShortUrlStatisticsDTO> operatingSystems = Arrays.asList(new ShortUrlStatisticsDTO[] { linux });

		StatisticsSummaryDTO statisticsSummary = new StatisticsSummaryDTO();
		statisticsSummary.setNumberOfHits(numberOfHits);
		statisticsSummary.setBrowsers(browsers);
		statisticsSummary.setDevicesTypes(devicesTypes);
		statisticsSummary.setOperatingSystems(operatingSystems);
		
		given(statisticService.getStatisticsSummary()).willReturn(statisticsSummary);

		// When and Then
		this.mockMvc.perform(get("/statistics/summary")).andExpect(status().isOk()).andExpect(content().json(
				"{'numberOfHits':3,'browsers':[{'name':'Firefox','total':1},{'name':'Chrome','total':2}],'devicesTypes':[{'name':'Computer','total':3}],'operatingSystems':[{'name':'Linux','total':3}]}"));
	}

	@Test
	public void whenGettingStatisticsByCodeReturnsTheExistingOnes() throws Exception {
		// Given
		String code = "3077yW";
		Long numberOfHits = 3L;

		ShortUrlStatisticsDTO firefox = new ShortUrlStatisticsDTO("Firefox", 1L);
		ShortUrlStatisticsDTO chrome = new ShortUrlStatisticsDTO("Chrome", 2L);
		List<ShortUrlStatisticsDTO> browsers = Arrays.asList(new ShortUrlStatisticsDTO[] { firefox, chrome });

		ShortUrlStatisticsDTO computer = new ShortUrlStatisticsDTO("Computer", 3L);
		List<ShortUrlStatisticsDTO> devicesTypes = Arrays.asList(new ShortUrlStatisticsDTO[] { computer });

		ShortUrlStatisticsDTO linux = new ShortUrlStatisticsDTO("Linux", 3L);
		List<ShortUrlStatisticsDTO> operatingSystems = Arrays.asList(new ShortUrlStatisticsDTO[] { linux });

		StatisticsSummaryDTO statisticsSummary = new StatisticsSummaryDTO();
		statisticsSummary.setNumberOfHits(numberOfHits);
		statisticsSummary.setBrowsers(browsers);
		statisticsSummary.setDevicesTypes(devicesTypes);
		statisticsSummary.setOperatingSystems(operatingSystems);
		
		given(statisticService.getStatisticsSummaryByCode(code)).willReturn(statisticsSummary);

		// When and Then
		this.mockMvc.perform(get("/statistics/summary/" + code)).andExpect(status().isOk()).andExpect(content().json(
				"{'numberOfHits':3,'browsers':[{'name':'Firefox','total':1},{'name':'Chrome','total':2}],'devicesTypes':[{'name':'Computer','total':3}],'operatingSystems':[{'name':'Linux','total':3}]}"));
	}
}