package com.anderson.url_shortener.helpers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHelper {
    public static String hashPassword(String plainPassword) {
        BCryptPasswordEncoder passEncoder = new BCryptPasswordEncoder();
        return passEncoder.encode(plainPassword);
    }
}
