package com.urlShortner.urlShortnerService.service;

import com.urlShortner.urlShortnerService.model.Url;
import com.urlShortner.urlShortnerService.model.UrlDto;
import org.springframework.stereotype.Service;

@Service
public interface UrlService {

    public Url generateUrl (UrlDto urlDto);
    public Url persistShortLink(Url url);
    public Url getEncodedUrl(String url);
    public  void deleteShortLink(Url url);
}
