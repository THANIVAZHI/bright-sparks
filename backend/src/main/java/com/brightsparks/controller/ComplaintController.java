package com.brightsparks.controller;

import com.brightsparks.dto.ApiResponse;
import com.brightsparks.model.Complaint;
import com.brightsparks.model.User;
import com.brightsparks.service.ComplaintService;
import com.brightsparks.service.UserService;
import com.brightsparks.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ComplaintController {
    private final ComplaintService complaintService;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final String UPLOAD_DIR = "./uploads/complaints";

    @PostMapping("/create")
    public ResponseEntity<?> createComplaint(
            @RequestParam String category,
            @RequestParam String description,
            @RequestParam String location,
            @RequestParam Double latitude,
            @RequestParam Double longitude,
            @RequestParam Long userId,
            @RequestParam(required = false) MultipartFile photo,
            @RequestHeader("Authorization") String token) {
        try {
            // Validate token
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Optional<User> user = userService.findById(userId);
            if (user.isEmpty()) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "User not found"),
                        HttpStatus.NOT_FOUND
                );
            }

            Complaint complaint = new Complaint();
            complaint.setUser(user.get());
            complaint.setCategory(category);
            complaint.setDescription(description);
            complaint.setLocation(location);
            complaint.setLatitude(latitude);
            complaint.setLongitude(longitude);

            // Handle photo upload
            if (photo != null && !photo.isEmpty()) {
                String photoPath = saveFile(photo);
                complaint.setPhotoPath(photoPath);
            }

            Complaint savedComplaint = complaintService.createComplaint(complaint);

            return new ResponseEntity<>(
                    new ApiResponse(true, "Complaint created successfully", savedComplaint),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to create complaint: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserComplaints(
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

            List<Complaint> complaints = complaintService.getUserComplaints(userId);
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to fetch complaints: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<?> getComplaintById(
            @PathVariable Long complaintId,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Optional<Complaint> complaint = complaintService.getComplaintById(complaintId);
            if (complaint.isPresent()) {
                return new ResponseEntity<>(complaint.get(), HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "Complaint not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to fetch complaint: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{complaintId}")
    public ResponseEntity<?> updateComplaint(
            @PathVariable Long complaintId,
            @RequestBody Complaint updatedComplaint,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Optional<Complaint> existingComplaint = complaintService.getComplaintById(complaintId);
            if (existingComplaint.isPresent()) {
                Complaint complaint = existingComplaint.get();
                complaint.setStatus(updatedComplaint.getStatus());
                complaint.setDescription(updatedComplaint.getDescription());
                Complaint saved = complaintService.updateComplaint(complaint);
                return new ResponseEntity<>(saved, HttpStatus.OK);
            }
            return new ResponseEntity<>(
                    new ApiResponse(false, "Complaint not found"),
                    HttpStatus.NOT_FOUND
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to update complaint: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @PutMapping("/{complaintId}/status")
    public ResponseEntity<?> updateComplaintStatus(
            @PathVariable Long complaintId,
            @RequestParam String status,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            Complaint complaint = complaintService.updateComplaintStatus(complaintId, status);
            return new ResponseEntity<>(complaint, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to update status: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @DeleteMapping("/{complaintId}")
    public ResponseEntity<?> deleteComplaint(
            @PathVariable Long complaintId,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            complaintService.deleteComplaint(complaintId);
            return new ResponseEntity<>(
                    new ApiResponse(true, "Complaint deleted successfully"),
                    HttpStatus.OK
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to delete complaint: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/by-category/{category}")
    public ResponseEntity<?> getByCategory(
            @PathVariable String category,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            List<Complaint> complaints = complaintService.getComplaintsByCategory(category);
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to fetch complaints: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    @GetMapping("/by-department/{department}")
    public ResponseEntity<?> getByDepartment(
            @PathVariable String department,
            @RequestHeader("Authorization") String token) {
        try {
            String tokenValue = token.replace("Bearer ", "");
            if (!jwtTokenProvider.validateToken(tokenValue)) {
                return new ResponseEntity<>(
                        new ApiResponse(false, "Invalid or expired token"),
                        HttpStatus.UNAUTHORIZED
                );
            }

            List<Complaint> complaints = complaintService.getComplaintsByDepartment(department);
            return new ResponseEntity<>(complaints, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new ApiResponse(false, "Failed to fetch complaints: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);

        return UPLOAD_DIR + "/" + fileName;
    }
}