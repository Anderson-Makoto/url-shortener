package com.anderson.url_shortener.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.anderson.url_shortener.dtos.UserDTO;
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

    public UserDTO saveUser(UserDTO userDTO) {

        if (this.userRepository.findByEmail(userDTO.getEmail()) != null) {
            return null;
        }

        String passEncrypted = PasswordHelper.hashPassword(userDTO.getPassword());
        userDTO.setPassword(passEncrypted);

        UserEntity userEntity = userRepository.save(userDTO.toEntity());

        userDTO = userEntity.toDTO();
        userDTO.setPassword(null);

        return userDTO;
    }

    public UserDTO login(UserDTO userDTO) {
        var userPass = new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword());

        var auth = this.authenticationManager.authenticate(userPass);
        String token = this.jwtTokenService.generateToken((UserEntity) auth.getPrincipal());
        userDTO.setToken(token);

        return userDTO;
    }
}
