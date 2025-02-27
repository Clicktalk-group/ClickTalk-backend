package com.greta.clicktalk.DAOs;

import com.greta.clicktalk.entities.Setting;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SettingDao {
    private final JdbcTemplate jdbcTemplate;

    public SettingDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Setting> rowMapper = (rs,_)->new Setting(
            rs.getLong("id"),
            rs.getLong("user_id"),
            rs.getString("theme")
    );

    public ResponseEntity<Setting> getUserSetting(long userId) {
        String sql = "SELECT * FROM settings WHERE user_id=?";
        Setting setting = jdbcTemplate.queryForObject(sql, rowMapper, userId);
        return setting != null ? ResponseEntity.ok(setting) : ResponseEntity.notFound().build();
    }

    public void addSetting(String theme, long userId) {
        if (existSetting(userId)) {
            return;
        }
        String sql = "INSERT INTO settings(user_id,theme) VALUES(?,?)";
        jdbcTemplate.update(sql, userId, theme);
    }

    public ResponseEntity<String> updateUserSetting(long userId, Setting setting) {
        String sql = "UPDATE settings set theme=? WHERE  user_id=?";
        int rowAffected = jdbcTemplate.update(sql, setting.getTheme(), userId);
        return rowAffected > 0 ? ResponseEntity.ok("Setting updated successfully") : ResponseEntity.internalServerError().body("Error while updating setting");
    }

    private boolean existSetting(long userId) {
        String sql = "SELECT COUNT(*) FROM settings WHERE user_id=?";

        Integer settingsCount= jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return settingsCount != null && settingsCount > 0;

    }
}
