package com.greta.clicktalk.controllers;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.greta.clicktalk.DAOs.SettingDao;
import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.DTOs.UpdatePasswordRequestDTO;
import com.greta.clicktalk.entities.User;
import com.greta.clicktalk.services.JwtUtil;
import com.greta.clicktalk.services.PasswordUpdateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
        private final AuthenticationManager authenticationManager;
        private final SettingDao settingDao;
        private final PasswordUpdateService passwordUpdateService;
        private final UserDao userDao;
        private final PasswordEncoder encoder;
        private final JwtUtil jwtUtils;

        public AuthController(AuthenticationManager authenticationManager, UserDao userDao, SettingDao settingDao,
                        PasswordUpdateService passwordUpdateService, PasswordEncoder encoder, JwtUtil jwtUtils) {
                this.authenticationManager = authenticationManager;
                this.userDao = userDao;
                this.settingDao = settingDao;
                this.passwordUpdateService = passwordUpdateService;
                this.encoder = encoder;
                this.jwtUtils = jwtUtils;
        }

        @PostMapping("/register")
        @Operation(summary = "Register a new user", description = "Register a new user with email and password.", responses = {
                        @ApiResponse(responseCode = "200", description = "User registered successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "User registered successfully! your Token: <token>"))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(examples = @ExampleObject(name = "Invalid request", value = "Error: Email is already in use!")))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Example user", value = "{\"email\":\"user@example.com\",\"password\":\"password123\"}")))
        public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
                boolean alreadyExists = userDao.existsByEmail(user.getEmail());
                if (alreadyExists) {
                        return ResponseEntity.badRequest().body("Error: Email is already in use!");
                }
                User newUser = new User(
                                user.getEmail(),
                                encoder.encode(user.getPassword()),
                                "USER");
                newUser = userDao.save(newUser);

                if (newUser != null) {
                        // add the initial setting
                        settingDao.addSetting("dark", newUser.getId());
                        String jwtToken = jwtUtils.generateToken(newUser.getEmail());
                        return ResponseEntity.ok(Map.of(
                                        "access_token", jwtToken,
                                        "token_type", "Bearer",
                                        "expires_in", 3600));
                }
                return ResponseEntity.badRequest().body("Error: user registration failed!");
        }

        @PostMapping("/login")
        @Operation(summary = "Authenticate user", description = "Authenticate user with email and password.", responses = {
                        @ApiResponse(responseCode = "200", description = "User authenticated successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "<token>"))),
                        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(examples = @ExampleObject(name = "Unauthorized", value = "Invalid email or password")))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Example user", value = "{\"email\":\"user@example.com\",\"password\":\"password123\"}")))
        public ResponseEntity<?> authenticateUser(@RequestBody User user) {
                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                user.getEmail(),
                                                user.getPassword()));
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                String token = jwtUtils.generateToken(userDetails.getUsername());

                return ResponseEntity.ok(Map.of(
                                "access_token", token,
                                "token_type", "Bearer",
                                "expires_in", 3600));
        }

        @PutMapping("update-password")
        @Operation(summary = "Update password", description = "Update password for the authenticated user.", responses = {
                        @ApiResponse(responseCode = "200", description = "Password updated successfully", content = @Content(examples = @ExampleObject(name = "Example response", value = "Password updated successfully"))),
                        @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(examples = @ExampleObject(name = "Invalid request", value = "Error: your password is not correct!")))
        })
        @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(examples = @ExampleObject(name = "Example password update", value = "{\"currentPassword\":\"oldPassword123\",\"newPassword\":\"newPassword123\"}")))
        public ResponseEntity<String> updatePassword(
                        @Valid @RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,
                        Authentication authentication) {
                return passwordUpdateService.updatePassword(updatePasswordRequestDTO, authentication);
        }

        @DeleteMapping("delete")
        @Operation(summary = "Delete user", description = "Delete the authenticated user.", responses = {
                        @ApiResponse(responseCode = "204", description = "User deleted successfully", content = @Content(examples = @ExampleObject(name = "User deleted", value = ""))),
                        @ApiResponse(responseCode = "404", description = "User not found", content = @Content(examples = @ExampleObject(name = "User not found", value = "Error: user not found"))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(examples = @ExampleObject(name = "Internal server error", value = "Error while deleting user")))
        })
        public ResponseEntity<String> deleteUser(Authentication authentication) {
                User currentUser = userDao.findByEmail(authentication.getName());
                return userDao.deleteUserById(currentUser.getId());
        }
}