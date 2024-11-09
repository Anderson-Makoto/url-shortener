package com.anderson.url_shortener.dtos;

import java.time.LocalDateTime;
import com.anderson.url_shortener.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO implements IDTO {
    private Integer id = null;
    private String name = null;
    private String email = null;
    private String password = null;
    private LocalDateTime createdAt = null;
    private String token = null;

    public UserEntity toEntity() {
        return new UserEntity(
                this.id,
                this.name,
                this.email,
                this.password,
                this.createdAt);
    }
}
