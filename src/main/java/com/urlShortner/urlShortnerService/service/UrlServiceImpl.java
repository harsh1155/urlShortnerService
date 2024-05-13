package com.urlShortner.urlShortnerService.service;

import com.google.common.hash.Hashing;
import com.urlShortner.urlShortnerService.Dao.UrlDao;
import com.urlShortner.urlShortnerService.model.Url;
import com.urlShortner.urlShortnerService.model.UrlDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlDao urlDao;

    @Override
    public Url generateUrl(UrlDto urlDto) {
        // checking is exisitng url is empty or not
        if(StringUtils.isNotEmpty(urlDto.getUrl())){
           String encodedUrl = encodedUrl(urlDto.getUrl()); // calling encode url method
           Url urlToPersist = new Url();
           urlToPersist.setCreationDate(LocalDateTime.now());
           urlToPersist.setOriginalLink(urlDto.getUrl());
           urlToPersist.setShortLink(encodedUrl);
           urlToPersist.setExpirationDate(getExpiration(urlDto.getExpirationDate(),
                   urlToPersist.getCreationDate()));
           Url urlToReturn = persistShortLink(urlToPersist);
           if(urlToReturn != null){
               return urlToReturn;
           }
            return null;
        }

        return null;
    }
    // checking is Date Blank or not, if blank make active for 60 sec.
    private LocalDateTime getExpiration(String expirationDate, LocalDateTime creationDate)
    {

        if(StringUtils.isBlank(expirationDate)){
            return creationDate.plusSeconds(60);
        }
        LocalDateTime returnExpiration = LocalDateTime.parse(expirationDate);
        return returnExpiration;
    }

    private String encodedUrl(String url) {
        String encodedUrl ="";
        LocalDateTime time = LocalDateTime.now();
        encodedUrl = Hashing.murmur3_32().   //  murmur3_32() have hashing functionality in it.
                hashString(url.concat(time.toString()), StandardCharsets.UTF_8).
                toString();
        return encodedUrl;
    }

    @Override
    public Url persistShortLink(Url url) {
        Url urltoReturn = urlDao.save(url);
        return urltoReturn;
    }

    @Override
    public Url getEncodedUrl(String url) {
        Url urltoReturn = urlDao.findByShortLink(url);
        return urltoReturn;
    }

    @Override
    public void deleteShortLink(Url url) {
       urlDao.delete(url);
    }
}
