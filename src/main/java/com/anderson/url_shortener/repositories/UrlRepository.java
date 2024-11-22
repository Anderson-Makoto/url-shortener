package com.anderson.url_shortener.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.anderson.url_shortener.entities.UrlEntity;

public interface UrlRepository extends JpaRepository<UrlEntity, Integer> {
}
