package com.anderson.url_shortener.dtos;

import java.time.LocalDateTime;

import com.anderson.url_shortener.entities.UrlEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UrlDTO implements IDTO {

    private String shortUrl;
    private String originalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Integer userId;
    private Integer clickCount;

    public UrlEntity toEntity() {
        return new UrlEntity(
                this.shortUrl,
                this.originalUrl,
                this.createdAt,
                this.expiresAt,
                this.userId,
                this.clickCount);
    }
}
