package com.example.e_commerce.dto.ResponseDto;

import com.example.e_commerce.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int user_id;
    private String username;
    private String email;
    private UserRole role;
}
