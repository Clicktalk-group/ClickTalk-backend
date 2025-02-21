package com.greta.clicktalk.contollers;

import com.greta.clicktalk.DAOs.UserDao;
import com.greta.clicktalk.DTOs.UpdatePasswordRequestDTO;
import com.greta.clicktalk.entities.User;
import com.greta.clicktalk.serveces.JwtUtil;
import com.greta.clicktalk.serveces.PasswordUpdateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final PasswordUpdateService passwordUpdateService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtils;

    public AuthController(AuthenticationManager authenticationManager, UserDao userDao, PasswordUpdateService passwordUpdateService, PasswordEncoder encoder, JwtUtil jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userDao = userDao;
        this.passwordUpdateService = passwordUpdateService;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        boolean alreadyExists = userDao.existsByEmail(user.getEmail());
        if (alreadyExists) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

    User newUser = new User(
            user.getEmail(),
            encoder.encode(user.getPassword()),
            "USER"
    );

        boolean isUserExists = userDao.save(newUser);

        if(isUserExists) {
            String jwtToken = jwtUtils.generateToken(newUser.getEmail());
            return ResponseEntity.ok("User registered successfully! your Token: " + jwtToken);
        }
        return ResponseEntity.badRequest().body("Error: user registration failed!");
    }


    @PostMapping("/login")
    public String authenticateUser( @RequestBody User user) {
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(
                       user.getEmail(),
                       user.getPassword()
               )
       );
       UserDetails userDetails = (UserDetails) authentication.getPrincipal();
       return jwtUtils.generateToken(userDetails.getUsername());
    }

    @PutMapping("update-password")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdatePasswordRequestDTO updatePasswordRequestDTO,Authentication authentication) {
    return passwordUpdateService.updatePassword(updatePasswordRequestDTO, authentication);
    }

    @DeleteMapping("delete")
    public ResponseEntity<String> deleteUser(Authentication authentication) {
     User currentUser = userDao.findByEmail(authentication.getName());
     return userDao.deleteUserById(currentUser.getId());
    }

}