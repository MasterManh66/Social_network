package com.social_luvina.social_dev8.modules.models.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String password;

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }

    public String getPassword(){
        return password;
    }
}


