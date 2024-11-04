package com.anderson.url_shortener.dtos;

// import org.springframework.security.core.userdetails.User;

import com.anderson.url_shortener.entities.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO implements IDTO {
    private Integer id;
    private String name;
    private String email;
    private String password;

    public UserEntity toEntity() {
        return new UserEntity(
                this.id,
                this.name,
                this.email,
                this.password);
    }
}
