# Bright Sparks - Civic Issue Reporting Platform

![Bright Sparks](https://img.shields.io/badge/status-active-success)
![Java](https://img.shields.io/badge/Java-11+-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![License](https://img.shields.io/badge/license-MIT-blue)

A comprehensive full-stack web platform that enables citizens to report civic issues (potholes, garbage, streetlights, water leaks, etc.) with automatic routing to relevant government authorities and real-time status tracking.

## 🎯 Features

### 1. User Registration & Authentication
- ✅ Multi-channel registration (Email, Mobile Number, Government ID)
- ✅ OTP verification for genuine users
- ✅ JWT-based secure authentication
- ✅ Password encryption with BCrypt
- ✅ Session management

### 2. Report Civic Issues (Full CRUD)
- ✅ **Create**: Submit issues with photos, GPS location, and category
- ✅ **Read**: View all submitted complaints and details
- ✅ **Update**: Modify complaint information or status
- ✅ **Delete**: Remove complaints
- ✅ Auto-generated unique Complaint IDs
- ✅ File upload support

### 3. Smart Routing System
Automatic department assignment based on issue category:
- 🔌 **Streetlight** → Electricity Department
- 🗑️ **Garbage** → Sanitation Department
- 🛣️ **Pothole/Road** → Public Works Department
- 💧 **Water Leakage** → Water Supply Department
- ♻️ **Illegal Dumping** → Environmental Department

### 4. Citizen Tracking System
Real-time status updates:
```
Submitted → Verified → Assigned → In Progress → Resolved → Feedback
```

### 5. Additional Features
- GPS auto-location capture
- Photo/Video upload capability
- User profile management
- Department-wise complaint filtering
- Category-based complaint search
- Responsive UI design

## 🏗️ Technology Stack

### Frontend
- **HTML5** - Structure and semantic markup
- **CSS3** - Modern styling with animations
- **JavaScript (Vanilla)** - DOM manipulation and API integration
- **Responsive Design** - Mobile-friendly interface

### Backend
- **Java 11+** - Programming language
- **Spring Boot 2.7** - Application framework
- **Spring Data JPA** - ORM and database access
- **Spring Security** - Authentication and authorization
- **JWT** - Secure token-based authentication

### Database
- **MySQL 8.0** - Relational database
- **Hibernate** - ORM framework
- **JDBC** - Database connectivity

### Tools & Libraries
- **Maven** - Build and dependency management
- **Lombok** - Reduce boilerplate code
- **Postman** - API testing

## 📋 Prerequisites

- Java 11 or higher
- Maven 3.6+
- MySQL 8.0+
- Web Browser (Chrome, Firefox, Edge, Safari)
- Git

## 🚀 Quick Start

### 1. Clone Repository
```bash
git clone https://github.com/THANIVAZHI/bright-sparks.git
cd bright-sparks
```

### 2. Database Setup
```bash
mysql -u root -p < database/schema.sql
```

### 3. Backend Configuration
```bash
cd backend
cp src/main/resources/application.properties src/main/resources/application.properties.local
```

Edit `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/bright_sparks
spring.datasource.username=root
spring.datasource.password=your_password
jwt.secret=your_secret_key_here
```

### 4. Build and Run Backend
```bash
mvn clean install
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 5. Run Frontend
```bash
cd ../frontend
# Option 1: Open index.html directly
open index.html

# Option 2: Use Python server
python -m http.server 3000
```

Frontend runs on: `http://localhost:3000` (if using Python server)

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api
```

### Authentication Endpoints

#### Register User
```http
POST /auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "governmentId": "ABC123456",
  "password": "SecurePassword123"
}

Response: 201 Created
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Registration successful"
}
```

#### Login User
```http
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "SecurePassword123"
}

Response: 200 OK
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "message": "Login successful"
}
```

### Complaint Endpoints

#### Create Complaint
```http
POST /complaints/create
Content-Type: multipart/form-data
Authorization: Bearer {token}

Form Parameters:
- category: pothole
- description: Large pothole causing accidents
- location: Main Street, City Center
- latitude: 12.9352
- longitude: 77.6245
- userId: 1
- photo: [file]

Response: 201 Created
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "category": "pothole",
  "description": "Large pothole causing accidents",
  "location": "Main Street, City Center",
  "latitude": 12.9352,
  "longitude": 77.6245,
  "status": "Verified",
  "assignedDepartment": "Public Works Department",
  "createdAt": "2024-06-16T14:00:00"
}
```

#### Get User Complaints
```http
GET /complaints/user/{userId}
Authorization: Bearer {token}

Response: 200 OK
[
  {
    "id": 1,
    "complaintId": "COMPLAINT-1718543400000-ABC123DE",
    "category": "pothole",
    "status": "In Progress",
    "assignedDepartment": "Public Works Department",
    ...
  }
]
```

#### Get Complaint by ID
```http
GET /complaints/{complaintId}
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "category": "pothole",
  "status": "Verified",
  ...
}
```

#### Update Complaint Status
```http
PUT /complaints/{complaintId}/status?status=In Progress
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "complaintId": "COMPLAINT-1718543400000-ABC123DE",
  "status": "In Progress",
  ...
}
```

#### Get Complaints by Category
```http
GET /complaints/by-category/{category}
Authorization: Bearer {token}

Response: 200 OK
[
  { /* complaint objects */ }
]
```

#### Get Complaints by Department
```http
GET /complaints/by-department/{department}
Authorization: Bearer {token}

Response: 200 OK
[
  { /* complaint objects */ }
]
```

#### Delete Complaint
```http
DELETE /complaints/{complaintId}
Authorization: Bearer {token}

Response: 200 OK
{
  "success": true,
  "message": "Complaint deleted successfully"
}
```

### User Endpoints

#### Get User Profile
```http
GET /users/{userId}
Authorization: Bearer {token}

Response: 200 OK
{
  "id": 1,
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "governmentId": "ABC123456",
  "isVerified": true,
  "createdAt": "2024-06-16T13:00:00"
}
```

#### Update User Profile
```http
PUT /users/{userId}
Content-Type: application/json
Authorization: Bearer {token}

{
  "fullName": "Jane Doe",
  "mobileNumber": "9876543211",
  "governmentId": "XYZ789123"
}

Response: 200 OK
{
  "id": 1,
  "fullName": "Jane Doe",
  ...
}
```

## 📁 Project Structure

```
bright-sparks/
├── frontend/
│   ├── index.html
│   ├── css/
│   │   └── style.css
│   └── js/
│       └── main.js
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/brightsparks/
│   │   │   │       ├── BrightSparksApplication.java
│   │   │   │       ├── controller/
│   │   │   │       │   ├── AuthController.java
│   │   │   │       │   ├── ComplaintController.java
│   │   │   │       │   └── UserController.java
│   │   │   │       ├── service/
│   │   │   │       │   ├── UserService.java
│   │   │   │       │   └── ComplaintService.java
│   │   │   │       ├── model/
│   │   │   │       │   ├── User.java
│   │   │   │       │   └── Complaint.java
│   │   │   │       ├── repository/
│   │   │   │       │   ├── UserRepository.java
│   │   │   │       │   └── ComplaintRepository.java
│   │   │   │       ├── dto/
│   │   │   │       ├── util/
│   │   │   │       ├── config/
│   │   │   │       └── exception/
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   ├── pom.xml
│   └── README.md
├── database/
│   └── schema.sql
├── docs/
│   ├── API_DOCUMENTATION.md
│   └── ARCHITECTURE.md
├── SETUP_GUIDE.md
├── README.md
├── .gitignore
└── LICENSE
```

## 🔐 Security Features

- ✅ JWT-based authentication
- ✅ BCrypt password hashing
- ✅ CORS configuration
- ✅ Input validation
- ✅ SQL injection prevention (JPA Parameterized Queries)
- ✅ Authorization checks on all endpoints
- ✅ Secure file upload handling

## 🧪 Testing

### Manual Testing Workflow

1. **Register a new citizen**
   - Click "Login/Register"
   - Fill in all required fields
   - Submit registration

2. **Report an issue**
   - Click "Report an Issue"
   - Select issue category
   - Add description and location
   - Click "Get Current Location" for GPS
   - Upload photo (optional)
   - Submit complaint

3. **Track complaint**
   - Go to Dashboard
   - View complaint status timeline
   - Check assigned department

4. **View profile**
   - Click "Profile"
   - View user information

### API Testing with Postman

1. Import the API collection
2. Set up environment variables (token, baseUrl)
3. Run test scenarios

## 🐛 Troubleshooting

### Database Connection Error
```
Solution:
1. Verify MySQL is running
2. Check credentials in application.properties
3. Ensure database exists: mysql> SHOW DATABASES;
```

### CORS Error in Browser
```
Solution:
- CORS is already configured in SecurityConfig
- Check browser console for specific error
- Verify backend is running on correct port
```

### File Upload Error
```
Solution:
1. Create ./uploads directory
2. Check directory permissions
3. Verify file size < 10MB
4. Check disk space
```

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

## 🚧 Future Enhancements

- [ ] Push notifications for status updates
- [ ] Email notifications
- [ ] Admin dashboard with analytics
- [ ] Department-specific dashboards
- [ ] Advanced reporting and analytics
- [ ] Mobile app (iOS/Android)
- [ ] Real-time updates with WebSocket
- [ ] Map view for complaints
- [ ] Complaint verification by authorities
- [ ] Public feedback system

## 📞 Support

For issues, questions, or suggestions:
1. Create an [issue](https://github.com/THANIVAZHI/bright-sparks/issues)
2. Contact the development team
3. Check existing documentation

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 👤 Author

**THANIVAZHI**
- GitHub: [@THANIVAZHI](https://github.com/THANIVAZHI)

## 🙏 Acknowledgments

- Spring Boot Documentation
- MySQL Documentation
- Community contributions and feedback

---

**Made with ❤️ for better civic infrastructure reporting**
