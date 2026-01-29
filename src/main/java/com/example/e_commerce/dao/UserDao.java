package com.example.e_commerce.dao;

import com.example.e_commerce.dto.RequestDto.RegisterRequest;
import com.example.e_commerce.dto.RequestDto.UpdateUserRequest;
import com.example.e_commerce.model.User;
import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class UserDao {
     private final JdbcTemplate jdbcTemplate;
     private static final RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role")
            );

     public void registerUser (RegisterRequest user) {
             jdbcTemplate.update("INSERT INTO \"User\" (username,email,password,role) VALUES (?,?,?, CAST (? AS user_role))",
                     user.getUsername(),
                     user.getEmail(),
                     user.getPassword(),
                     user.getRole().toString()
             );

     }

     public List<User> getAllUsers () {
        return jdbcTemplate.query("SELECT * FROM \"User\" ORDER BY username", USER_ROW_MAPPER);
     }

     public Optional<User> getUserById (int userId) {
         try {
             User user = jdbcTemplate.queryForObject("SELECT * FROM \"User\" WHERE user_id = ?",USER_ROW_MAPPER,userId );
             return Optional.ofNullable(user);
         } catch (EmptyResultDataAccessException e) {
             return Optional.empty();
         }

     }


     public int deleteUser (int userId){
         return  jdbcTemplate.update("DELETE FROM \"User\" WHERE user_id = ?",userId);

     }

    public int updateUser(int userId, UpdateUserRequest user) {
        StringBuilder sql = new StringBuilder("UPDATE \"User\" SET ");
        List<Object> params = new ArrayList<>();

        if (user.getUsername() != null) {
            sql.append("username = ?, ");
            params.add(user.getUsername());
        }

        if (user.getRole() != null) {
            sql.append("role =  CAST (? AS user_role), ");
            params.add(user.getRole().name());
        }

        sql.setLength(sql.length() - 2); // remove trailing comma
        sql.append(" WHERE user_id = ?");
        params.add(userId);

        return  jdbcTemplate.update(sql.toString(), params.toArray());

    }


}
