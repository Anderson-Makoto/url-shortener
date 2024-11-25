package com.anderson.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anderson.url_shortener.Exceptions.UrlNotFoundException;
import com.anderson.url_shortener.entities.UrlEntity;
import com.anderson.url_shortener.repositories.UrlRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class UrlRedirectService {
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private EntityManager entityManager;

    public String redirect(String shortUrl) throws Exception {
        String query = "SELECT * from urls AS WHERE short_url = :shortUrl";
        try {
            UrlEntity urlEntity = this.entityManager
                    .createQuery(query, UrlEntity.class)
                    .setParameter("shortUrl", shortUrl)
                    .getSingleResult();

            urlEntity = this.countClick(urlEntity);
            urlEntity = this.urlRepository.save(urlEntity);

            return urlEntity.getOriginalUrl();
        } catch (NoResultException e) {
            throw new UrlNotFoundException();
        }

    }

    private UrlEntity countClick(UrlEntity urlEntity) {
        urlEntity.setClickCount(urlEntity.getClickCount() + 1);

        return urlEntity;
    }
}
