package com.example.e_commerce.service;

import com.example.e_commerce.dao.UserDao;
import com.example.e_commerce.dto.RequestDto.RegisterRequest;
import com.example.e_commerce.dto.RequestDto.UpdateUserRequest;
import com.example.e_commerce.dto.ResponseDto.UserDTO;
import com.example.e_commerce.enums.UserRole;
import com.example.e_commerce.model.User;
import com.example.e_commerce.utils.exceptions.NotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Service
@AllArgsConstructor
public class UserService {
    private final UserDao userDao;

    public void deleteUser(int id){
        int rows = userDao.deleteUser(id);
        if (rows == 0) {
            throw new NotFoundException("User not found");
        }
    }

    public void register (RegisterRequest user) {
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);

        userDao.registerUser(user);

    }

    public List<UserDTO> getAllUsers () {

        List<User> users = userDao.getAllUsers();
        return users.stream().map(this::convertToDto).collect(Collectors.toList());

    }

    public UserDTO getUserById (int user_id) {
        User user = userDao.getUserById(user_id)
                .orElseThrow(() -> new NotFoundException("User with ID " + user_id + " not found"));;
        return  convertToDto(user);
    }

    public UserDTO updateUser (int id,UpdateUserRequest user){
        int res = userDao.updateUser(id,user);
        if (res == 0) {
            throw new NotFoundException("User not found");
        }
        return getUserById(id);
    }


    private UserDTO convertToDto (User user) {
        UserDTO dto = new UserDTO();
        dto.setUser_id(user.getUser_id());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(UserRole.valueOf(user.getRole()));

        return dto;
    }

}
