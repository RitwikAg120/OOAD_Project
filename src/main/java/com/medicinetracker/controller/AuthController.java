package com.medicinetracker.controller;

import com.medicinetracker.dto.LoginRequest;
import com.medicinetracker.dto.LoginResponse;
import com.medicinetracker.dto.UserDTO;
import com.medicinetracker.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    // In-memory user store (in real app, use database)
    private static final Map<String, User> USERS = new HashMap<>();
    private static final Map<String, String> SESSIONS = new HashMap<>();

    static {
        // Demo users
            USERS.put("ritwik@admin.com", new User(1, "Ritwik", "ritwik@admin.com", 
                Integer.toHexString("1234".hashCode()), "ADMIN"));
        USERS.put("rishi@home.com", new User(2, "Rishikesh", "rishi@home.com",
                Integer.toHexString("1234".hashCode()), "USER"));
        USERS.put("rohit@home.com", new User(3, "Rohit", "rohit@home.com",
                Integer.toHexString("rohit789".hashCode()), "USER"));
        USERS.put("ritesh@home.com", new User(4, "Ritesh", "ritesh@home.com",
                Integer.toHexString("ritesh000".hashCode()), "USER"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        User user = USERS.get(request.getEmail());
        
        if (user != null && user.passwordHash.equals(Integer.toHexString(request.getPassword().hashCode()))) {
            String token = UUID.randomUUID().toString();
            SESSIONS.put(token, request.getEmail());
            
            UserDTO userDTO = new UserDTO(user.userId, user.name, user.email, user.role);
            LoginResponse response = new LoginResponse(true, "Login successful", token, userDTO);
            return ResponseEntity.ok(response);
        }
        
        return ResponseEntity.status(401).body(new LoginResponse(false, "Invalid credentials", null, null));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestHeader("Authorization") String token) {
        SESSIONS.remove(token);
        return ResponseEntity.ok(new ApiResponse(true, "Logout successful"));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse> validateToken(@RequestHeader("Authorization") String token) {
        if (SESSIONS.containsKey(token)) {
            return ResponseEntity.ok(new ApiResponse(true, "Token valid"));
        }
        return ResponseEntity.status(401).body(new ApiResponse(false, "Invalid token"));
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> getCurrentUser(@RequestHeader("Authorization") String token) {
        String email = SESSIONS.get(token);
        if (email != null) {
            User user = USERS.get(email);
            return ResponseEntity.ok(new UserDTO(user.userId, user.name, user.email, user.role));
        }
        return ResponseEntity.status(401).build();
    }

    // Helper class
    private static class User {
        int userId;
        String name;
        String email;
        String passwordHash;
        String role;

        User(int userId, String name, String email, String passwordHash, String role) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.passwordHash = passwordHash;
            this.role = role;
        }
    }
}
