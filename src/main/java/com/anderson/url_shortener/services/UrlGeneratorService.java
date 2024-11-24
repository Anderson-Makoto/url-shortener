package com.anderson.url_shortener.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import com.anderson.url_shortener.Exceptions.InvalidUrlException;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.repositories.UrlRepository;

public class UrlGeneratorService {
    @Autowired
    private UrlRepository urlRepository;

    public UrlEntity saveUrl(String originalUrl, UserEntity userEntity) throws Exception {
        if (!this.isOriginalUrlValid(originalUrl)) {
            throw new InvalidUrlException();
        }

        if (this.doesOriginalUrlExistsForUser(originalUrl, userEntity)) {
            throw new InvalidUrlException();
        }

        String shortUrl = this.generateShortUrl(originalUrl);
        UrlEntity urlEntity = new UrlEntity(null, shortUrl, originalUrl, null, null, userEntity, null);

        this.urlRepository.save(urlEntity);

        return urlEntity;
    }

    private String generateShortUrl(String originalUrl) {
        String seed = UUID.randomUUID().toString();
        String shortUrl = UUID.fromString(originalUrl + seed).toString().substring(0, 9);

        return shortUrl;
    }

    private boolean isOriginalUrlValid(String originalUrl) {
        String regex = "<^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
        return originalUrl.matches(regex);
    }

    private boolean doesOriginalUrlExistsForUser(String originalUrl, UserEntity userEntity) {
        return this.urlRepository.existsByOriginalUrlAndUser(originalUrl, userEntity);
    }
}
