# Bright Sparks Backend

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+

### Installation Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/THANIVAZHI/bright-sparks.git
   cd bright-sparks/backend
   ```

2. **Set up the database**
   ```bash
   mysql -u root -p < ../database/schema.sql
   ```

3. **Configure application properties**
   Edit `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/bright_sparks
   spring.datasource.username=root
   spring.datasource.password=your_password
   jwt.secret=your_secret_key_here
   ```

4. **Build the project**
   ```bash
   mvn clean install
   ```

5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - Login user
- `POST /api/auth/verify-otp` - Verify OTP

### Complaints (CRUD)
- `POST /api/complaints/create` - Create complaint
- `GET /api/complaints/user/{userId}` - Get user complaints
- `GET /api/complaints/{complaintId}` - Get complaint details
- `PUT /api/complaints/{complaintId}` - Update complaint
- `PUT /api/complaints/{complaintId}/status` - Update complaint status
- `DELETE /api/complaints/{complaintId}` - Delete complaint
- `GET /api/complaints/by-category/{category}` - Get complaints by category
- `GET /api/complaints/by-department/{department}` - Get complaints by department

### Users
- `GET /api/users/{userId}` - Get user profile
- `PUT /api/users/{userId}` - Update user profile

## Features

### Smart Routing
Complaints are automatically routed to departments based on category:
- **Streetlight** → Electricity Department
- **Garbage** → Sanitation Department
- **Pothole/Road** → Public Works Department
- **Water** → Water Supply Department
- **Dumping** → Environmental Department

### Status Tracking
- Submitted → Verified → Assigned → In Progress → Resolved

### Security
- JWT-based authentication
- Password encryption with BCrypt
- CORS configuration
- Request validation

## Project Structure
```
backend/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/brightsparks/
│       │       ├── BrightSparksApplication.java
│       │       ├── controller/
│       │       │   ├── AuthController.java
│       │       │   ├── ComplaintController.java
│       │       │   └── UserController.java
│       │       ├── service/
│       │       │   ├── UserService.java
│       │       │   └── ComplaintService.java
│       │       ├── model/
│       │       │   ├── User.java
│       │       │   └── Complaint.java
│       │       ├── repository/
│       │       │   ├── UserRepository.java
│       │       │   └── ComplaintRepository.java
│       │       ├── dto/
│       │       ├── util/
│       │       ├── config/
│       │       └── exception/
│       └── resources/
│           └── application.properties
├── pom.xml
└── README.md
```

## Technologies Used
- Spring Boot 2.7.14
- Spring Data JPA
- Spring Security
- JWT (JSON Web Tokens)
- MySQL
- Maven
