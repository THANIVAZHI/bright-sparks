package com.brightsparks.controller;

import com.brightsparks.dto.ApiResponse;
import com.brightsparks.dto.LoginRequest;
import com.brightsparks.dto.LoginResponse;
import com.brightsparks.dto.RegisterRequest;
import com.brightsparks.model.User;
import com.brightsparks.service.UserService;
import com.brightsparks.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setMobileNumber(request.getMobileNumber());
            user.setGovernmentId(request.getGovernmentId());
            user.setPassword(request.getPassword());

            User savedUser = userService.registerUser(user);
            String token = jwtTokenProvider.generateToken(savedUser.getId(), savedUser.getEmail());

            LoginResponse response = new LoginResponse();
            response.setId(savedUser.getId());
            response.setFullName(savedUser.getFullName());
            response.setEmail(savedUser.getEmail());
            response.setMobileNumber(savedUser.getMobileNumber());
            response.setToken(token);
            response.setMessage("Registration successful");

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Registration failed: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            Optional<User> user = userService.findByEmail(request.getEmail());
            if (user.isPresent()) {
                User foundUser = user.get();
                if (userService.validatePassword(request.getPassword(), foundUser.getPassword())) {
                    String token = jwtTokenProvider.generateToken(foundUser.getId(), foundUser.getEmail());

                    LoginResponse response = new LoginResponse();
                    response.setId(foundUser.getId());
                    response.setFullName(foundUser.getFullName());
                    response.setEmail(foundUser.getEmail());
                    response.setMobileNumber(foundUser.getMobileNumber());
                    response.setToken(token);
                    response.setMessage("Login successful");

                    return new ResponseEntity<>(response, HttpStatus.OK);
                }
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "Invalid email or password"),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Login failed: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        try {
            Optional<User> user = userService.findByEmail(email);
            if (user.isPresent()) {
                User foundUser = user.get();
                foundUser.setIsVerified(true);
                userService.updateUser(foundUser);

                return new ResponseEntity<>(
                        new ApiResponse(true, "OTP verified successfully"),
                        HttpStatus.OK
                );
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "OTP verification failed: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}