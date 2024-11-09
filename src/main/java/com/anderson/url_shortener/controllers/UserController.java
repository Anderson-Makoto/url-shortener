package com.anderson.url_shortener.controllers;

import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.anderson.url_shortener.dtos.UserDTO;
import com.anderson.url_shortener.services.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        userDTO = this.userService.login(userDTO);

        return ResponseEntity.ok(userDTO);
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userBody) {
        UserDTO userDTO = this.userService.saveUser(userBody);

        return ResponseEntity.ok(userDTO);
    }
}
