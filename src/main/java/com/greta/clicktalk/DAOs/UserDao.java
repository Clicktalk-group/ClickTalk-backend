package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.User;
import com.greta.clicktalk.excetions.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, _) -> new User(
            rs.getInt("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("role")
    );

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public boolean save(User user) {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getRole());
        return rowsAffected > 0;
    }

    public ResponseEntity<String> updatePassword(String email, String password) {

        if(!existsByEmail(email)) {
            throw new ResourceNotFoundException("user not found");
        }

        String sql = "UPDATE users SET password = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(sql, password, email);

        return rowsAffected > 0 ? ResponseEntity.ok("Password updated successfully") : ResponseEntity.internalServerError().body("Password update failed");
    }

    public ResponseEntity<String> deleteUserById(long id ) {
        if(!existsById(id)) {
             throw new ResourceNotFoundException("user not found");
        }
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0? ResponseEntity.noContent().build() : ResponseEntity.internalServerError().body("Error while deleting user");
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer rowsAffected = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return rowsAffected != null && rowsAffected > 0;
    }

    private boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer rowsAffected = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowsAffected != null && rowsAffected > 0;
    }


}