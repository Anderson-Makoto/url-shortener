package com.anderson.url_shortener.FunctionalTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
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
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        String query = "SELECT email FROM users where email='test@user.com'";
        String userEmail = this.entityManager.createNativeQuery(query, String.class).getSingleResult().toString();
        assertEquals(user.getEmail(), userEmail);
        
        query = "SELECT COUNT(*) FROM users";
        Integer usersCount = (Integer)this.entityManager.createNativeQuery(query, Integer.class).getSingleResult();
        assertEquals(1, usersCount);
    }

    @Test
    void testSaveRepeatedUserShouldReturn400() {
        
    }
}
