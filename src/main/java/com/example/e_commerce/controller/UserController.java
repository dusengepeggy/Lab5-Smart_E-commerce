package com.example.e_commerce.controller;

import com.example.e_commerce.dto.RequestDto.RegisterRequest;
import com.example.e_commerce.dto.JsonResponseDto.SuccessResponse;
import com.example.e_commerce.dto.RequestDto.UpdateUserRequest;
import com.example.e_commerce.dto.ResponseDto.UserDTO;
import com.example.e_commerce.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity getAllUsers () {
        SuccessResponse<String> res = new SuccessResponse("Users fetched successfully", userService.getAllUsers());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }

    @PostMapping("/register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest user) {
        userService.register(user);
        SuccessResponse<String> res = new SuccessResponse("User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(res);
    }

    @DeleteMapping
    public  ResponseEntity deleteUser (@RequestParam int id ){
        userService.deleteUser(id);
        SuccessResponse<String> res = new SuccessResponse("User account deleted successfully");
        return  ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @GetMapping("/user")
    public ResponseEntity getUserById (@RequestParam int id) {
        UserDTO user = userService.getUserById(id);
        SuccessResponse<UserDTO> res = new SuccessResponse<>("User retrieved successfully",user);
        return  ResponseEntity.status(HttpStatus.OK).body(res);

    }

    @PutMapping
    public ResponseEntity updateUser (@RequestParam int id, @Valid @RequestBody UpdateUserRequest request) {
        UserDTO user = userService.updateUser(id,request);
        SuccessResponse<UserDTO> res = new SuccessResponse<>("User account updated successfully",user);
        return  ResponseEntity.status(HttpStatus.OK).body(res);

    }

}
