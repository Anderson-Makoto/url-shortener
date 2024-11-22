package com.anderson.url_shortener.entities;

import java.time.LocalDateTime;
import com.anderson.url_shortener.dtos.UrlDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "urls")
@AllArgsConstructor
@Getter
@Setter
public class UrlEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "short_url", nullable = false, unique = true)
    private String shortUrl;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "click_count", nullable = false)
    private Integer clickCount;

    public UrlDTO toDTO() {
        return new UrlDTO(
                this.id,
                this.shortUrl,
                this.originalUrl,
                this.createdAt,
                this.expiresAt,
                this.user.toDTO(),
                this.clickCount);
    }

}