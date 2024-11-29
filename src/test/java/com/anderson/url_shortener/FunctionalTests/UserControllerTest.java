package com.anderson.url_shortener.FunctionalTests;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.repositories.UrlRepository;
import com.anderson.url_shortener.repositories.UserRepository;

import jakarta.persistence.EntityManager;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UrlRepository urlRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    void testSaveValidUserShouldReturn200() {
        UserEntity user = new UserEntity();
        user.setName("test user");
        user.setEmail("test@user.com");
        user.setPassword("123123");

        ResponseEntity<UserEntity> response = this.testRestTemplate.postForEntity("/api/user/save", user, UserEntity.class);
        String query = "SELECT email FROM User where email='test@user.com'";
        UserEntity userEntity = this.entityManager.createQuery(query, UserEntity.class).getSingleResult();
        System.out.println(userEntity.getEmail());
        
    }
}
