# Bright Sparks - System Architecture

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                        CLIENT LAYER                          │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Frontend (HTML, CSS, JavaScript)                    │  │
│  │  - User Interface                                    │  │
│  │  - Authentication Forms                              │  │
│  │  - Complaint Reporting Form                          │  │
│  │  - Dashboard & Tracking                              │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            |
                   (HTTP/REST API)
                            |
┌─────────────────────────────────────────────────────────────┐
│                    BACKEND LAYER (Spring Boot)               │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  REST Controllers                                    │  │
│  │  - AuthController                                   │  │
│  │  - ComplaintController                              │  │
│  │  - UserController                                   │  │
│  └──────────────────────────────────────────────────────┘  │
│                            |                                │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Services                                            │  │
│  │  - UserService                                       │  │
│  │  - ComplaintService                                  │  │
│  └──────────────────────────────────────────────────────┘  │
│                            |                                │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Data Access Layer (Repositories)                    │  │
│  │  - UserRepository                                    │  │
│  │  - ComplaintRepository                               │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Utilities & Security                                │  │
│  │  - JwtTokenProvider                                  │  │
│  │  - PasswordEncoder (BCrypt)                          │  │
│  │  - CORS Configuration                                │  │
│  │  - Exception Handler                                 │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
                            |
                   (JDBC/Hibernate)
                            |
┌─────────────────────────────────────────────────────────────┐
│                    DATA LAYER (MySQL)                       │
│  ┌──────────────────────────────────────────────────────┐  │
│  │  Database                                            │  │
│  │  - Users Table                                       │  │
│  │  - Complaints Table                                  │  │
│  │  - Indexes & Relationships                           │  │
│  └──────────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────────┘
```

## Component Details

### 1. Frontend Layer

**Technology:** HTML5, CSS3, Vanilla JavaScript

**Components:**
- **Authentication Module**
  - Login form
  - Registration form
  - OTP verification

- **Complaint Module**
  - Issue reporting form
  - Photo/video upload
  - GPS location capture
  - Category selection

- **Dashboard Module**
  - Complaint list
  - Status tracking
  - Status timeline
  - Filter options

- **User Profile Module**
  - Profile view
  - Profile update
  - Complaint count

**Key Features:**
- Responsive design
- Modal-based navigation
- Real-time form validation
- Local storage for session management
- GPS integration

### 2. Backend Layer

**Technology:** Java 11, Spring Boot 2.7

#### Controllers
```
AuthController
├── POST /auth/register
├── POST /auth/login
└── POST /auth/verify-otp

ComplaintController
├── POST /complaints/create
├── GET /complaints/user/{userId}
├── GET /complaints/{complaintId}
├── PUT /complaints/{complaintId}
├── PUT /complaints/{complaintId}/status
├── DELETE /complaints/{complaintId}
├── GET /complaints/by-category/{category}
└── GET /complaints/by-department/{department}

UserController
├── GET /users/{userId}
└── PUT /users/{userId}
```

#### Services

**UserService**
- User registration
- User authentication
- Password validation
- User retrieval and updates

**ComplaintService**
- Complaint creation with smart routing
- Complaint retrieval (by user, category, department)
- Complaint status updates
- Complaint deletion
- Unique ID generation

#### Data Models

**User Entity**
```java
- id (Primary Key)
- fullName
- email (Unique)
- mobileNumber (Unique)
- governmentId
- password (Encrypted)
- isVerified
- createdAt
- updatedAt
```

**Complaint Entity**
```java
- id (Primary Key)
- complaintId (Unique)
- user (Foreign Key)
- category
- description
- location
- latitude
- longitude
- photoPath
- status
- assignedDepartment
- createdAt
- updatedAt
```

#### Security

**Authentication:**
- JWT (JSON Web Tokens)
- Token generation on login/registration
- Token validation on protected endpoints
- Token expiration: 24 hours

**Password Security:**
- BCrypt hashing
- Salt rounds: 10
- No plain text storage

**Authorization:**
- Role-based access (future enhancement)
- User can only access own complaints
- Token required for all operations

**CORS:**
- Allow all origins (configurable)
- Allow standard HTTP methods
- Allow custom headers

### 3. Database Layer

**Technology:** MySQL 8.0

**Database Schema:**

```sql
users
├── id (BIGINT, PRIMARY KEY)
├── full_name (VARCHAR)
├── email (VARCHAR, UNIQUE)
├── mobile_number (VARCHAR, UNIQUE)
├── government_id (VARCHAR)
├── password (VARCHAR)
├── is_verified (BOOLEAN)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)

complaints
├── id (BIGINT, PRIMARY KEY)
├── complaint_id (VARCHAR, UNIQUE)
├── user_id (BIGINT, FOREIGN KEY)
├── category (VARCHAR)
├── description (LONGTEXT)
├── location (VARCHAR)
├── latitude (DECIMAL)
├── longitude (DECIMAL)
├── photo_path (VARCHAR)
├── status (VARCHAR)
├── assigned_department (VARCHAR)
├── created_at (TIMESTAMP)
└── updated_at (TIMESTAMP)
```

**Indexes:**
- Users: email, mobile_number
- Complaints: user_id, status, category, department, complaint_id, created_at

## Data Flow

### User Registration Flow
```
1. User enters registration details in frontend form
2. Frontend validates input
3. POST request to /auth/register
4. AuthController receives request
5. UserService checks for duplicate email/mobile
6. Password is hashed using BCrypt
7. User record created in database
8. JWT token generated
9. Response sent to frontend with token
10. Token stored in localStorage
11. User redirected to dashboard
```

### Complaint Creation Flow
```
1. User fills complaint form with details
2. Frontend captures GPS coordinates
3. User uploads photo (optional)
4. Form data sent to /complaints/create
5. ComplaintController validates token
6. File uploaded and saved
7. ComplaintService.createComplaint() called
8. Smart routing assigns department based on category
9. Unique complaint ID generated
10. Status set to "Verified" (after auto-assignment)
11. Complaint record created in database
12. Response sent to frontend
13. Dashboard updates with new complaint
```

### Status Tracking Flow
```
1. Complaint follows lifecycle:
   - Submitted (initial)
   - Verified (auto - after smart routing)
   - Assigned (manual - by system/department)
   - In Progress (manual - by authority)
   - Resolved (manual - by authority)
   - Feedback (manual - citizen provides feedback)

2. Frontend polls dashboard for status updates
3. User sees real-time timeline visualization
4. Department receives complaint and updates status
```

## Smart Routing Algorithm

```java
Based on Category:
- "streetlight" → "Electricity Department"
- "garbage" → "Sanitation Department"
- "pothole" or "road" → "Public Works Department"
- "water" → "Water Supply Department"
- "dumping" → "Environmental Department"
- default → "General Services"
```

## API Communication Pattern

```
Request:
1. Method: GET/POST/PUT/DELETE
2. Path: /api/endpoint
3. Headers: 
   - Content-Type: application/json (or multipart/form-data)
   - Authorization: Bearer {token}
4. Body: JSON or Form Data

Response:
1. Status Code: 200/201/400/401/404/500
2. Headers:
   - Content-Type: application/json
3. Body:
   {
     "success": boolean,
     "message": string,
     "data": object (optional)
   }
```

## File Storage

**Location:** `./uploads/complaints/`

**File Naming:** `{UUID}_{original_filename}`

**Supported Types:** Images (jpg, png, gif), Videos (mp4, avi, mov)

**Size Limit:** 10MB per file

## Performance Considerations

1. **Database Indexes**
   - Added on frequently queried columns
   - Improves search performance

2. **Connection Pooling**
   - Spring Data HikariCP
   - Manages database connections

3. **Lazy Loading**
   - JPA lazy loading for relationships
   - Reduces memory overhead

4. **Query Optimization**
   - Repository method names for auto-query generation
   - Custom queries where needed

## Scalability

**Future Enhancements:**
1. Caching layer (Redis)
2. Message queue (RabbitMQ)
3. Load balancing
4. Database replication
5. Microservices architecture

## Deployment

**Production Deployment:**
1. Use production-grade database
2. Enable SSL/TLS
3. Configure environment variables
4. Enable logging and monitoring
5. Use CI/CD pipeline
6. Run tests before deployment
7. Use reverse proxy (Nginx)

## Monitoring & Logging

**Current Setup:**
- Console logging
- Log level: INFO (configurable)

**Future Setup:**
- ELK Stack (Elasticsearch, Logstash, Kibana)
- Application Performance Monitoring (APM)
- Error tracking (Sentry)

## Backup & Recovery

**Database Backups:**
```bash
mysqldump -u root -p bright_sparks > backup.sql
mysql -u root -p bright_sparks < backup.sql
```

**File Backups:**
- Regular backups of `./uploads/` directory
- Cloud storage integration (future)
