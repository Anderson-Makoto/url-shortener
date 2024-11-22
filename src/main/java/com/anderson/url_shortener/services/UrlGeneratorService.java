package com.anderson.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;

import com.anderson.url_shortener.Exceptions.InvalidUrlException;
import com.anderson.url_shortener.dtos.UrlDTO;
import com.anderson.url_shortener.dtos.UserDTO;
import com.anderson.url_shortener.repositories.UrlRepository;

public class UrlGeneratorService {
    @Autowired
    private UrlRepository urlRepository;

    public UrlDTO generateUrl(String originalUrl, UserDTO userDTO) throws Exception {
        if (!this.isOriginalUrlValid(originalUrl)) {
            throw new InvalidUrlException();
        }

    }

    private boolean isOriginalUrlValid(String originalUrl) {
        String regex = "<^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]>";
        return originalUrl.matches(regex);
    }

    private boolean doesOriginalUrlExistsForUser(String originalUrl, UserDTO userDTO) {
        this.urlRepository.findByOriginalUrlAndUser(originalUrl, userDTO.toEntity());
    }
}
