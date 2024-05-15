package com.urlShortner.urlShortnerService.Dao;

import com.urlShortner.urlShortnerService.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlDao extends JpaRepository<Url, Long>{

     //   public Url findByShortLink(String shortLink);
        @Query("SELECT u FROM Url u WHERE u.shortLink = :shortLink")
        public Url findByShortLink(@Param("shortLink") String shortLink);
}
