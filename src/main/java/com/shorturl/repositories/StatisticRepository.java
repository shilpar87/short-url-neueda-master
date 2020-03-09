package com.shorturl.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.shorturl.domain.ShortUrlStatistic;
import com.shorturl.dto.ShortUrlStatisticsDTO;

@Repository
public interface StatisticRepository extends JpaRepository<ShortUrlStatistic, Long> {
    @Query("select count(s.id) from Statistic s")
	Long getNumberOfHits();
	
    @Query("select count(s.id) from Statistic s where s.url.code = :code")
	Long getNumberOfHitsByCode(@Param("code") String code);
	
    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.browser, count(s)) from Statistic s group by s.browser")
    List<ShortUrlStatisticsDTO> getBrowsers();

    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.deviceType, count(s)) from Statistic s group by s.deviceType")
    List<ShortUrlStatisticsDTO> getDevicesTypes();

    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.operatingSystem, count(s)) from Statistic s group by s.operatingSystem")
    List<ShortUrlStatisticsDTO> getOperatingSystems();

    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.browser, count(s)) from Statistic s where s.url.code = :code group by s.browser")
    List<ShortUrlStatisticsDTO> getBrowsersByCode(@Param("code") String code);

    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.deviceType, count(s)) from Statistic s where s.url.code = :code group by s.deviceType")
    List<ShortUrlStatisticsDTO> getDevicesTypesByCode(@Param("code") String code);

    @Query("select new com.shorturl.dto.ShortUrlStatisticsDTO(s.operatingSystem, count(s)) from Statistic s where s.url.code = :code group by s.operatingSystem")
	List<ShortUrlStatisticsDTO> getOperatingSystemsByCode(@Param("code") String code);
}
