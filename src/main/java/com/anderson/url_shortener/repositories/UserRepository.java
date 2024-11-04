package com.anderson.url_shortener.repositories;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.anderson.url_shortener.dtos.UserDTO;
import com.anderson.url_shortener.entities.UserEntity;

@Repository
public interface UserRepository extends MongoRepository<UserEntity, Integer> {

    List<UserDTO> findByEmail(String email);
}
