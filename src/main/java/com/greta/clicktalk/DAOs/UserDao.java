package com.greta.clicktalk.DAOs;

import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.greta.clicktalk.entities.User;
import com.greta.clicktalk.exceptions.ResourceNotFoundException;

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
            rs.getString("role"));

    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        return jdbcTemplate.query(sql, userRowMapper, email)
                .stream()
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    public User save(User user) {
        String sql = "INSERT INTO users (email, password, role) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            var ps = con.prepareStatement(sql, new String[] { "id" });
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
        }

        return user;
    }

    public ResponseEntity<String> updatePassword(String email, String password) {

        if (!existsByEmail(email)) {
            throw new ResourceNotFoundException("user not found");
        }

        String sql = "UPDATE users SET password = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(sql, password, email);

        return rowsAffected > 0 ? ResponseEntity.ok("Password updated successfully")
                : ResponseEntity.internalServerError().body("Password update failed");
    }

    public ResponseEntity<String> deleteUserById(long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("user not found");
        }
        String sql = "DELETE FROM users WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);

        // reset the auto_increment for users table
        jdbcTemplate.update("ALTER TABLE users AUTO_INCREMENT = 0;");

        return rowsAffected > 0 ? ResponseEntity.noContent().build()
                : ResponseEntity.internalServerError().body("Error while deleting user");
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        Integer usersNumber = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return usersNumber != null && usersNumber > 0;
    }

    public long getUserIdFromAuth(Authentication auth) {
        if (auth == null) {
            throw new IllegalStateException("the user is not logged in");
        }
        String email = auth.getName();
        User user = findByEmail(email);
        return user.getId();
    }

    private boolean existsById(long id) {
        String sql = "SELECT COUNT(*) FROM users WHERE id = ?";
        Integer usersNumber = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return usersNumber != null && usersNumber > 0;
    }

}