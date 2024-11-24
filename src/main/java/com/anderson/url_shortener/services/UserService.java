package com.anderson.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.helpers.PasswordHelper;
import com.anderson.url_shortener.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenService jwtTokenService;

    public UserService(UserRepository userRespository) {
        this.userRepository = userRespository;
    }

    public UserEntity saveUser(UserEntity userEntity) {

        if (this.userRepository.findByEmail(userEntity.getEmail()) != null) {
            return null;
        }

        String passEncrypted = PasswordHelper.hashPassword(userEntity.getPassword());
        userEntity.setPassword(passEncrypted);
        userEntity = userRepository.save(userEntity);
        userEntity.setPassword(null);

        return userEntity;
    }

    public UserEntity login(UserEntity userEntity) {
        var userPass = new UsernamePasswordAuthenticationToken(userEntity.getEmail(), userEntity.getPassword());

        var auth = this.authenticationManager.authenticate(userPass);
        String token = this.jwtTokenService.generateToken((UserEntity) auth.getPrincipal());
        userEntity.setToken(token);

        return userEntity;
    }
}
