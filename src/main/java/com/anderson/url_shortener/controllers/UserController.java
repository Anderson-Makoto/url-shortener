package com.anderson.url_shortener.controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anderson.url_shortener.Handlers.HttpResponseHandler;
import com.anderson.url_shortener.entities.UserEntity;
import com.anderson.url_shortener.services.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity userEntity) {
        try {
            userEntity = this.userService.login(userEntity);

            return ResponseEntity.status(HttpStatus.OK).body(userEntity);
        } catch (Exception e) {
            return HttpResponseHandler.handleHttpResponse(e);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserEntity userEntity) {
        try {
            userEntity = this.userService.saveUser(userEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
        } catch (Exception e) {
            return HttpResponseHandler.handleHttpResponse(e);
        }
    }
}
