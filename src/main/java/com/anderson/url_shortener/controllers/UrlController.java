package com.anderson.url_shortener.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.anderson.url_shortener.Handlers.HttpResponseHandler;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.services.UrlGeneratorService;
import com.anderson.url_shortener.services.UrlRedirectService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/url")
public class UrlController {

    @Autowired
    private UrlGeneratorService urlGeneratorService;
    @Autowired
    private UrlRedirectService urlRedirectService;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody UrlEntity urlEntity) {
        try {
            urlEntity = this.urlGeneratorService.saveUrl(urlEntity);
            return ResponseEntity.ok(urlEntity);
        } catch (Exception e) {
            return HttpResponseHandler.handleHttpResponse(e);
        }
    }

    @GetMapping("/redirectUrl")
    public RedirectView redirect(@RequestParam String shortUrl) {
        try {
            String originalUrl = this.urlRedirectService.redirect(shortUrl);
            return new RedirectView(originalUrl);
        } catch (Exception e) {
            return new RedirectView();
        }
    }

}
