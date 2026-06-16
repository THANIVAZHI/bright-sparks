# Setup and Installation Guide

## Project Overview
Bright Sparks is a comprehensive civic issue reporting platform with the following architecture:
- **Frontend**: HTML, CSS, JavaScript
- **Backend**: Java (Spring Boot)
- **Database**: MySQL

## Installation

### 1. Database Setup

```bash
# Create database and tables
mysql -u root -p < database/schema.sql
```

### 2. Backend Setup

```bash
cd backend

# Install dependencies
mvn clean install

# Configure database (edit application.properties)
# Update:
# - spring.datasource.url
# - spring.datasource.username
# - spring.datasource.password
# - jwt.secret

# Run the application
mvn spring-boot:run
```

Backend runs on: `http://localhost:8080`

### 3. Frontend Setup

```bash
cd frontend

# Open index.html in a web browser
# Or use a local server (e.g., Python)
python -m http.server 3000
```

Frontend runs on: `http://localhost:3000`

## Core Features

### 1. User Registration & Authentication
- Register with email, mobile number, or government ID
- OTP verification
- JWT-based authentication
- Secure password storage with BCrypt

### 2. Report Issues (CRUD Operations)
- **Create**: Submit civic issues with photos, location (GPS), and category
- **Read**: View submitted complaints and their status
- **Update**: Modify complaint details or status
- **Delete**: Remove complaints

### 3. Smart Routing
Automatic assignment to departments:
- Pothole → Public Works Department
- Garbage → Sanitation Department
- Streetlight → Electricity Department
- Water Leakage → Water Supply Department
- Illegal Dumping → Environmental Department

### 4. Status Tracking
Citizen can track complaint status:
- Submitted
- Verified
- Assigned
- In Progress
- Resolved
- Citizen Feedback

## API Documentation

### Base URL: `http://localhost:8080/api`

### Authentication Endpoints

#### Register
```
POST /auth/register
Content-Type: application/json

{
  "fullName": "John Doe",
  "email": "john@example.com",
  "mobileNumber": "9876543210",
  "governmentId": "ABC123456",
  "password": "password123"
}
```

#### Login
```
POST /auth/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}
```

### Complaint Endpoints

#### Create Complaint
```
POST /complaints/create
Content-Type: multipart/form-data
Authorization: Bearer {token}

Form Data:
- category: pothole
- description: Large pothole on Main Street
- location: Main Street, City
- latitude: 12.9352
- longitude: 77.6245
- userId: 1
- photo: [file]
```

#### Get User Complaints
```
GET /complaints/user/{userId}
Authorization: Bearer {token}
```

#### Update Complaint Status
```
PUT /complaints/{complaintId}/status?status=In Progress
Authorization: Bearer {token}
```

#### Get Complaints by Category
```
GET /complaints/by-category/{category}
Authorization: Bearer {token}
```

#### Get Complaints by Department
```
GET /complaints/by-department/{department}
Authorization: Bearer {token}
```

## Database Schema

### Users Table
```sql
- id (Primary Key)
- full_name
- email (Unique)
- mobile_number (Unique)
- government_id
- password (Encrypted)
- is_verified
- created_at
- updated_at
```

### Complaints Table
```sql
- id (Primary Key)
- complaint_id (Unique)
- user_id (Foreign Key)
- category
- description
- location
- latitude
- longitude
- photo_path
- status
- assigned_department
- created_at
- updated_at
```

## Testing the Application

### Manual Testing Steps

1. **Register a new user**
   - Go to `http://localhost:3000`
   - Click "Login/Register"
   - Fill in registration form
   - Click "Register"

2. **Submit a complaint**
   - Click "Report an Issue"
   - Select category (e.g., "Pothole")
   - Enter description and location
   - Click "Get Current Location" for GPS coordinates
   - Upload a photo (optional)
   - Click "Submit Report"

3. **Track complaint**
   - Go to Dashboard
   - View all submitted complaints
   - See real-time status updates
   - View assigned department

4. **View profile**
   - Click "Profile"
   - View user information and complaint count

## Troubleshooting

### Database Connection Error
- Verify MySQL is running
- Check database credentials in `application.properties`
- Ensure database schema is created

### CORS Error
- Backend CORS is configured to allow all origins
- Check browser console for specific error messages

### File Upload Issues
- Ensure `./uploads` directory exists
- Check file permissions
- Verify file size doesn't exceed 10MB

## Future Enhancements
- Push notifications for status updates
- Email notifications
- Admin dashboard
- Department-specific dashboards
- Advanced analytics and reporting
- Mobile app integration
- Real-time updates with WebSocket

## Support
For issues or questions, please create an issue in the GitHub repository.
