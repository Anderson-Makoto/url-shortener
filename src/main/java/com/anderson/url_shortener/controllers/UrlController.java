package com.anderson.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.anderson.url_shortener.Handlers.HttpResponseHandler;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.services.UrlGeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlGeneratorService urlGeneratorService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UrlEntity urlEntity) throws Exception {
        try {
            urlEntity = this.urlGeneratorService.saveUrl(urlEntity);
            return ResponseEntity.ok(urlEntity);
        } catch (Exception e) {
            return HttpResponseHandler.handleHttpResponse(e);
        }
    }

}
