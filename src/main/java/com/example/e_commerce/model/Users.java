package com.example.e_commerce.model;

import com.example.e_commerce.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Users {
    private Long user_id;
    private String username;
    private String email;
    private String password;
    private Role role;

}
