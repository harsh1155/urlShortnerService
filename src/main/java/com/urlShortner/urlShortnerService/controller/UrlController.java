package com.urlShortner.urlShortnerService.controller;

import com.urlShortner.urlShortnerService.model.Url;
import com.urlShortner.urlShortnerService.model.UrlDto;
import com.urlShortner.urlShortnerService.model.UrlErrorResponseDto;
import com.urlShortner.urlShortnerService.model.UrlResponseDto;
import com.urlShortner.urlShortnerService.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> genrateUrl(@RequestBody UrlDto urlDto){
        Url urlToReturn = urlService.generateUrl(urlDto);
        if(urlToReturn!= null){
            UrlResponseDto urlResponseDto = new UrlResponseDto();
            urlResponseDto.setOriginalUrl(urlToReturn.getOriginalLink());
            urlResponseDto.setExpirationDate(urlToReturn.getExpirationDate());
            urlResponseDto.setShortLink(urlToReturn.getShortLink());
            return new ResponseEntity<UrlResponseDto>(urlResponseDto, HttpStatus.OK);

        }
        UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
        urlErrorResponseDto.setStatus("400");
        urlErrorResponseDto.setError("There is error while making this request");
        return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
    }

}
