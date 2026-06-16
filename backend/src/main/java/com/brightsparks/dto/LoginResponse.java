package com.brightsparks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private Long id;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String token;
    private String message;
}