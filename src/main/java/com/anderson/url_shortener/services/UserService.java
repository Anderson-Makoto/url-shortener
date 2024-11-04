package com.anderson.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.anderson.url_shortener.dtos.UserDTO;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.helpers.PasswordHelper;
import com.anderson.url_shortener.repositories.UserRepository;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRespository) {
        this.userRepository = userRespository;
    }

    public UserDTO saveUser(UserDTO userDTO) {

        if (!this.userRepository.findByEmail(userDTO.getEmail()).isEmpty()) {
            return null;
        }

        String passEncrypted = PasswordHelper.hashPassword(userDTO.getPassword());
        userDTO.setPassword(passEncrypted);

        userRepository.save(userDTO.toEntity());

        return userDTO;
    }
}
