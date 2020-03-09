package com.shorturl.dto;

import java.io.Serializable;
import java.util.List;

public class StatisticsSummaryDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long numberOfHits;
	private List<ShortUrlStatisticsDTO> browsers;
	private List<ShortUrlStatisticsDTO> devicesTypes;
	private List<ShortUrlStatisticsDTO> operatingSystems;

	public List<ShortUrlStatisticsDTO> getBrowsers() {
		return browsers;
	}

	public void setBrowsers(List<ShortUrlStatisticsDTO> browsers) {
		this.browsers = browsers;
	}

	public List<ShortUrlStatisticsDTO> getDevicesTypes() {
		return devicesTypes;
	}

	public void setDevicesTypes(List<ShortUrlStatisticsDTO> devicesTypes) {
		this.devicesTypes = devicesTypes;
	}

	public List<ShortUrlStatisticsDTO> getOperatingSystems() {
		return operatingSystems;
	}

	public void setOperatingSystems(List<ShortUrlStatisticsDTO> operatingSystems) {
		this.operatingSystems = operatingSystems;
	}

	public Long getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits(Long numberOfHits) {
		this.numberOfHits = numberOfHits;
	}
}
