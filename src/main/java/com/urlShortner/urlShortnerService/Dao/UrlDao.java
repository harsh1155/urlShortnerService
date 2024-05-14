package com.urlShortner.urlShortnerService.Dao;

import com.urlShortner.urlShortnerService.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlDao extends JpaRepository<Url, Long>{

        public Url findByShortLink(String shortLink);
}
