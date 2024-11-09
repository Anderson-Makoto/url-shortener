package com.anderson.url_shortener.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.anderson.url_shortener.entities.UrlEntity;

public interface UrlRepository extends MongoRepository<UrlEntity, String> {
}
