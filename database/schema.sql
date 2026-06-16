-- Create Database
CREATE DATABASE IF NOT EXISTS bright_sparks;
USE bright_sparks;

-- Users Table
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    mobile_number VARCHAR(20) NOT NULL UNIQUE,
    government_id VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_mobile (mobile_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Complaints Table
CREATE TABLE IF NOT EXISTS complaints (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    complaint_id VARCHAR(100) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    category VARCHAR(100) NOT NULL,
    description LONGTEXT NOT NULL,
    location VARCHAR(500) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    photo_path VARCHAR(500),
    status VARCHAR(50) DEFAULT 'Submitted',
    assigned_department VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_category (category),
    INDEX idx_department (assigned_department),
    INDEX idx_complaint_id (complaint_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create indexes for better query performance
CREATE INDEX idx_created_at ON complaints(created_at);
CREATE INDEX idx_updated_at ON complaints(updated_at);
CREATE INDEX idx_user_complaints ON complaints(user_id, created_at);

-- Insert sample departments for reference
INSERT INTO users (full_name, email, mobile_number, password, is_verified) VALUES
('Admin User', 'admin@brightsparks.com', '9999999999', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36P4rWOy', TRUE);

-- Sample data
INSERT INTO complaints (complaint_id, user_id, category, description, location, latitude, longitude, status, assigned_department) VALUES
('COMPLAINT-1718543400000-ABC123DE', 1, 'pothole', 'Large pothole on Main Street', 'Main Street, City Center', 12.9352, 77.6245, 'Resolved', 'Public Works Department'),
('COMPLAINT-1718543500000-DEF456GH', 1, 'streetlight', 'Broken streetlight near park', 'Park Road, Downtown', 12.9256, 77.6365, 'In Progress', 'Electricity Department');
