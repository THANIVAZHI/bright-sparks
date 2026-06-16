package com.brightsparks.controller;

import com.brightsparks.dto.ApiResponse;
import com.brightsparks.model.User;
import com.brightsparks.service.UserService;
import com.brightsparks.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserProfile(
            @PathVariable Long userId,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Optional<User> user = userService.findById(userId);
            if (user.isPresent()) {
                return new ResponseEntity<>(user.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to fetch user: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable Long userId,
            @RequestBody User updatedUser,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Optional<User> existingUser = userService.findById(userId);
            if (existingUser.isPresent()) {
                User user = existingUser.get();
                if (updatedUser.getFullName() != null) {
                    user.setFullName(updatedUser.getFullName());
                }
                if (updatedUser.getMobileNumber() != null) {
                    user.setMobileNumber(updatedUser.getMobileNumber());
                }
                if (updatedUser.getGovernmentId() != null) {
                    user.setGovernmentId(updatedUser.getGovernmentId());
                }
                User saved = userService.updateUser(user);
                return new ResponseEntity<>(saved, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "User not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to update user: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}