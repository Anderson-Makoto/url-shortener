package com.anderson.url_shortener.UnitTests;

import org.springframework.boot.test.context.SpringBootTest;

import com.anderson.url_shortener.services.UrlGeneratorService;

@SpringBootTest
public class UrlGeneratorServiceTest {
    private UrlGeneratorService urlGeneratorService;

    public void testGenerateValidUrlShouldReturn200() throws Exception {

    }

    public void testGenerateExistingUrlFromOtherUserShouldReturn200() throws Exception {

    }

    public void testGenerateInvalidUrlShouldReturn401() {

    }

    public void testGenerateExistingUrlFromSameUserShouldReturn401() {

    }
}
