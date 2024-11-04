package com.anderson.url_shortener.controllers;

import lombok.AllArgsConstructor;
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

    private UserService userService;

    @PostMapping("/save")
    public UserDTO saveUser(@RequestBody UserDTO userBody) {
        UserDTO userResponse = this.userService.saveUser(userBody);

        return userResponse;
    }
}
