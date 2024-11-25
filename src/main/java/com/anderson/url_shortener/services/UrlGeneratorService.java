package com.anderson.url_shortener.services;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anderson.url_shortener.Exceptions.InvalidUrlException;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.repositories.UrlRepository;

@Service
public class UrlGeneratorService {
    @Autowired
    private UrlRepository urlRepository;

    public UrlEntity saveUrl(UrlEntity urlEntity) throws Exception {
        String originalUrl = urlEntity.getOriginalUrl();
        UserEntity userEntity = urlEntity.getUser();
        if (!this.isOriginalUrlValid(originalUrl)) {
            throw new InvalidUrlException();
        }

        if (this.doesOriginalUrlExistsForUser(originalUrl, userEntity)) {
            throw new InvalidUrlException();
        }

        String shortUrl = this.generateShortUrl(originalUrl);
        urlEntity.setShortUrl(shortUrl);

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
