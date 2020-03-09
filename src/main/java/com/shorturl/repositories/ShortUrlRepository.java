package com.shorturl.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shorturl.domain.ShortUrl;

@Repository
public interface ShortUrlRepository extends JpaRepository<ShortUrl, String>{

}
