package com.anderson.url_shortener.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.entities.UserEntity;

public interface UrlRepository extends JpaRepository<UrlEntity, Integer> {
    List<UrlEntity> findByOriginalUrl(String url);

    List<UrlEntity> findByUser(UserEntity user);

    boolean existsByOriginalUrlAndUser(String originalUrl, UserEntity userEntity);

    boolean existsByShortUrl(String shortUrl);
}
