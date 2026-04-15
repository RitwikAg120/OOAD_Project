package com.medicinetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String token;
    private UserDTO user;

    public LoginResponse(boolean success, String message, String token, UserDTO user) {
        this.success = success;
        this.message = message;
        this.token = token;
        this.user = user;
    }
}
