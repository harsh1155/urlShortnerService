package com.urlShortner.urlShortnerService.controller;

import com.urlShortner.urlShortnerService.model.Url;
import com.urlShortner.urlShortnerService.model.UrlDto;
import com.urlShortner.urlShortnerService.model.UrlErrorResponseDto;
import com.urlShortner.urlShortnerService.model.UrlResponseDto;
import com.urlShortner.urlShortnerService.service.UrlService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
public class UrlController {
    @Autowired
    private UrlService urlService;

    @PostMapping("/generate")
    public ResponseEntity<?> genrateUrl(@RequestBody UrlDto urlDto){
        Url urlToReturn = urlService.generateUrl(urlDto);
        if(urlToReturn != null)
        {
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
    @GetMapping("/{shortLink}")
    public ResponseEntity<?> redirectToOriginal(@PathVariable String shortLink, HttpServletResponse response) throws IOException {
        if(StringUtils.isEmpty(shortLink))
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("400");
            urlErrorResponseDto.setError("Invalid Url");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }
        Url returnUrl = urlService.getEncodedUrl(shortLink);
        if(returnUrl == null)
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("400");
            urlErrorResponseDto.setError("Url Does Not Exsists");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.NOT_FOUND);
        }
        if(returnUrl.getExpirationDate().isBefore(LocalDateTime.now()))
        {
            UrlErrorResponseDto urlErrorResponseDto = new UrlErrorResponseDto();
            urlErrorResponseDto.setStatus("200");
            urlErrorResponseDto.setError("Url expired. Please generate new ");
            return new ResponseEntity<UrlErrorResponseDto>(urlErrorResponseDto, HttpStatus.OK);
        }
        response.sendRedirect(returnUrl.getOriginalLink());
        return null;
    }
}
