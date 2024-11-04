package com.anderson.url_shortener.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.anderson.url_shortener.dtos.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Document(collection = "users")
@Getter
@Setter
@AllArgsConstructor
public class UserEntity implements IEntity {
    @Id
    private Integer id;
    private String name;
    private String email;
    private String password;

    public UserDTO toDTO() {
        return new UserDTO(
                this.getId(),
                this.getName(),
                this.getEmail(),
                this.getPassword());
    }
}
