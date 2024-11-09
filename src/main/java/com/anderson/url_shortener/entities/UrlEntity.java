package com.anderson.url_shortener.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.anderson.url_shortener.dtos.UrlDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "urls")
@AllArgsConstructor
@Getter
@Setter
public class UrlEntity implements IEntity {

    @Id
    @Field(name = "short_url")
    private String shortUrl;

    @Field(name = "original_url")
    private String originalUrl;

    @Field(name = "created_at")
    private LocalDateTime createdAt;

    @Field(name = "expires_at")
    private LocalDateTime expiresAt;

    @Field(name = "user_id")
    private Integer userId;

    @Field(name = "click_count")
    private Integer clickCount;

    public UrlDTO toDTO() {
        return new UrlDTO(
                this.shortUrl,
                this.originalUrl,
                this.createdAt,
                this.expiresAt,
                this.userId,
                this.clickCount);
    }

}