# Bright Sparks - API Documentation

## Overview
This document provides detailed API endpoints and usage examples for the Bright Sparks platform.

## Base URL
```
http://localhost:8080/api
```

## Authentication
All endpoints (except auth) require JWT token in Authorization header:
```
Authorization: Bearer {token}
```

## Response Format
All responses are in JSON format.

### Success Response
```json
{
  "success": true,
  "message": "Operation successful",
  "data": { /* response data */ }
}
```

### Error Response
```json
{
  "success": false,
  "message": "Error description"
}
```

## HTTP Status Codes
- `200 OK` - Successful request
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request parameters
- `401 Unauthorized` - Missing or invalid token
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## Endpoints

### Authentication

#### 1. Register User
**Method:** `POST`  
**Path:** `/auth/register`  
**Authentication:** No

**Request Body:**
```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "governmentId": "ABC123456",
  "password": "SecurePassword123"
}
```

**Response (201):**
```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Registration successful"
}
```

#### 2. Login User
**Method:** `POST`  
**Path:** `/auth/login`  
**Authentication:** No

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Response (200):**
```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Login successful"
}
```

#### 3. Verify OTP
**Method:** `POST`  
**Path:** `/auth/verify-otp`  
**Authentication:** No

**Query Parameters:**
- `email` - User email
- `otp` - OTP code

**Response (200):**
```json
{
  "success": true,
  "message": "OTP verified successfully"
}
```

### Complaints

#### 1. Create Complaint
**Method:** `POST`  
**Path:** `/complaints/create`  
**Authentication:** Yes (Bearer Token)  
**Content-Type:** `multipart/form-data`

**Form Parameters:**
- `category` (string) - Issue category
- `description` (string) - Issue description
- `location` (string) - Location
- `latitude` (number) - Latitude coordinate
- `longitude` (number) - Longitude coordinate
- `userId` (number) - User ID
- `photo` (file) - Photo/Video file (optional)

**Response (201):**
```json
{
  "success": true,
  "message": "Complaint created successfully",
  "data": {
    "id": 1,
    "complaintId": "COMPLAINT-1718543400000-ABC123DE",
    "category": "pothole",
    "description": "Large pothole",
    "location": "Main Street",
    "latitude": 12.9352,
    "longitude": 77.6245,
    "status": "Verified",
    "assignedDepartment": "Public Works Department",
    "createdAt": "2024-06-16T14:00:00"
  }
}
```

#### 2. Get User Complaints
**Method:** `GET`  
**Path:** `/complaints/user/{userId}`  
**Authentication:** Yes

**Response (200):**
```json
[
  {
    "id": 1,
    "complaintId": "COMPLAINT-1718543400000-ABC123DE",
    "category": "pothole",
    "description": "Large pothole",
    "status": "In Progress",
    "assignedDepartment": "Public Works Department",
    "createdAt": "2024-06-16T14:00:00"
  }
]
```

#### 3. Get Complaint Details
**Method:** `GET`  
**Path:** `/complaints/{complaintId}`  
**Authentication:** Yes

**Response (200):**
```json
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "category": "pothole",
  "description": "Large pothole",
  "location": "Main Street",
  "latitude": 12.9352,
  "longitude": 77.6245,
  "photoPath": "./uploads/complaints/abc123.jpg",
  "status": "In Progress",
  "assignedDepartment": "Public Works Department",
  "createdAt": "2024-06-16T14:00:00",
  "updatedAt": "2024-06-16T14:30:00"
}
```

#### 4. Update Complaint
**Method:** `PUT`  
**Path:** `/complaints/{complaintId}`  
**Authentication:** Yes

**Request Body:**
```json
{
  "description": "Updated description",
  "status": "In Progress"
}
```

**Response (200):**
```json
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "status": "In Progress",
  "description": "Updated description",
  "updatedAt": "2024-06-16T14:30:00"
}
```

#### 5. Update Complaint Status
**Method:** `PUT`  
**Path:** `/complaints/{complaintId}/status`  
**Authentication:** Yes

**Query Parameters:**
- `status` - New status (Submitted, Verified, Assigned, In Progress, Resolved)

**Response (200):**
```json
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "status": "Resolved",
  "updatedAt": "2024-06-16T15:00:00"
}
```

#### 6. Delete Complaint
**Method:** `DELETE`  
**Path:** `/complaints/{complaintId}`  
**Authentication:** Yes

**Response (200):**
```json
{
  "success": true,
  "message": "Complaint deleted successfully"
}
```

#### 7. Get Complaints by Category
**Method:** `GET`  
**Path:** `/complaints/by-category/{category}`  
**Authentication:** Yes

**Response (200):**
```json
[
  { /* complaint objects */ }
]
```

#### 8. Get Complaints by Department
**Method:** `GET`  
**Path:** `/complaints/by-department/{department}`  
**Authentication:** Yes

**Response (200):**
```json
[
  { /* complaint objects */ }
]
```

### Users

#### 1. Get User Profile
**Method:** `GET`  
**Path:** `/users/{userId}`  
**Authentication:** Yes

**Response (200):**
```json
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "governmentId": "ABC123456",
  "isVerified": true,
  "createdAt": "2024-06-16T13:00:00",
  "updatedAt": "2024-06-16T13:00:00"
}
```

#### 2. Update User Profile
**Method:** `PUT`  
**Path:** `/users/{userId}`  
**Authentication:** Yes

**Request Body:**
```json
{
  "fullName": "Jane Doe",
  "mobileNumber": "9876543211",
  "governmentId": "XYZ789123"
}
```

**Response (200):**
```json
{
  "id": 1,
  "fullName": "Jane Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543211",
  "governmentId": "XYZ789123",
  "isVerified": true,
  "updatedAt": "2024-06-16T14:00:00"
}
```

## Error Codes

| Code | Message | Solution |
|------|---------|----------|
| 400 | Invalid request | Check request parameters |
| 401 | Invalid or expired token | Login again to get new token |
| 404 | Resource not found | Verify the resource ID |
| 500 | Internal server error | Contact support |

## Rate Limiting
- No rate limiting currently implemented
- Recommended: Implement rate limiting in production

## Pagination
- Currently no pagination implemented
- Recommended: Add pagination for large datasets

## Versioning
- Current API version: v1
- Future versions will use `/api/v2/` prefix
