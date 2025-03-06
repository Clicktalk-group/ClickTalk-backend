package com.greta.clicktalk.controllers;


import com.greta.clicktalk.DAOs.SettingDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.entities.Setting;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("setting")
public class SettingController {
    private final SettingDao settingDao;
    private final UserDao userDao;

    public SettingController(SettingDao settingDao, UserDao userDao) {
        this.settingDao = settingDao;
        this.userDao = userDao;
    }

    @GetMapping
    public ResponseEntity<Setting> getUserSetting(Authentication auth) {
    long userId = userDao.getUserIdFromAuth(auth);
    return settingDao.getUserSetting(userId);
    }
}
