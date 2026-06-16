// Global variables
const API_BASE_URL = 'http://localhost:8080/api';
let currentUser = null;
let complaints = [];

// DOM Elements
const authModal = document.getElementById('authModal');
const reportModal = document.getElementById('reportModal');
const loginBtn = document.getElementById('loginBtn');
const reportBtn = document.getElementById('reportBtn');
const logoutBtn = document.getElementById('logoutBtn');
const profileBtn = document.getElementById('profileBtn');
const gpsBtn = document.getElementById('gpsBtn');
const loginForm = document.getElementById('loginForm');
const registerForm = document.getElementById('registerForm');
const reportForm = document.getElementById('reportForm');
const complaintsList = document.getElementById('complaintsList');
const profileContent = document.getElementById('profileContent');
const dashboardSection = document.getElementById('dashboard');
const profileSection = document.getElementById('profile');

// Modal close buttons
const closeButtons = document.querySelectorAll('.close');

// ==================== Modal Management ====================
function openModal(modal) {
    modal.style.display = 'block';
}

function closeModal(modal) {
    modal.style.display = 'none';
}

// Close modal when clicking close button
closeButtons.forEach(btn => {
    btn.addEventListener('click', function() {
        this.closest('.modal').style.display = 'none';
    });
});

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    if (event.target === authModal) {
        closeModal(authModal);
    }
    if (event.target === reportModal) {
        closeModal(reportModal);
    }
});

// ==================== Auth Tab Switching ====================
const tabButtons = document.querySelectorAll('.tab-btn');
tabButtons.forEach(btn => {
    btn.addEventListener('click', function() {
        const tabName = this.dataset.tab;
        
        // Remove active class from all tabs
        document.querySelectorAll('.tab-btn').forEach(b => b.classList.remove('active'));
        document.querySelectorAll('.tab-content').forEach(t => t.classList.remove('active'));
        
        // Add active class to clicked tab
        this.classList.add('active');
        document.getElementById(tabName + '-tab').classList.add('active');
    });
});

// Toggle between login and register
const toggleAuthLinks = document.querySelectorAll('.toggle-auth');
toggleAuthLinks.forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        const tabBtn = document.querySelector('.tab-btn:not(.active)');
        tabBtn.click();
    });
});

// ==================== Navigation ====================
loginBtn.addEventListener('click', () => {
    if (currentUser) {
        openModal(reportModal);
    } else {
        openModal(authModal);
    }
});

reportBtn.addEventListener('click', () => {
    if (currentUser) {
        openModal(reportModal);
    } else {
        openModal(authModal);
    }
});

profileBtn.addEventListener('click', () => {
    if (currentUser) {
        loadUserProfile();
        dashboardSection.classList.remove('active');
        profileSection.classList.add('active');
    }
});

// ==================== Authentication ====================
loginForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const email = loginForm.querySelector('input[type="email"]').value;
    const password = loginForm.querySelector('input[type="password"]').value;
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, password })
        });
        
        if (response.ok) {
            const data = await response.json();
            currentUser = data;
            localStorage.setItem('user', JSON.stringify(data));
            localStorage.setItem('token', data.token);
            updateUIAfterLogin();
            closeModal(authModal);
            alert('Login successful!');
        } else {
            alert('Login failed. Please check your credentials.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});

registerForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const formData = new FormData(registerForm);
    const fullName = registerForm.querySelector('input[placeholder="Full Name"]').value;
    const email = registerForm.querySelector('input[type="email"]').value;
    const mobileNumber = registerForm.querySelector('input[type="tel"]').value;
    const governmentId = registerForm.querySelector('input[placeholder="Government ID (Optional)"]').value;
    const password = registerForm.querySelectorAll('input[type="password"]')[0].value;
    const confirmPassword = registerForm.querySelectorAll('input[type="password"]')[1].value;
    
    if (password !== confirmPassword) {
        alert('Passwords do not match!');
        return;
    }
    
    try {
        const response = await fetch(`${API_BASE_URL}/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                fullName,
                email,
                mobileNumber,
                governmentId,
                password
            })
        });
        
        if (response.ok) {
            const data = await response.json();
            currentUser = data;
            localStorage.setItem('user', JSON.stringify(data));
            localStorage.setItem('token', data.token);
            updateUIAfterLogin();
            closeModal(authModal);
            alert('Registration successful!');
        } else {
            alert('Registration failed. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});

logoutBtn.addEventListener('click', () => {
    currentUser = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    updateUIAfterLogout();
});

// ==================== Report Issue ====================
gpsBtn.addEventListener('click', () => {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
            (position) => {
                const latitude = position.coords.latitude;
                const longitude = position.coords.longitude;
                
                document.getElementById('latitude').value = latitude;
                document.getElementById('longitude').value = longitude;
                
                // Reverse geocoding to get address (simple implementation)
                document.getElementById('location').value = `${latitude}, ${longitude}`;
            },
            (error) => {
                alert('Error getting location: ' + error.message);
            }
        );
    } else {
        alert('Geolocation is not supported by your browser.');
    }
});

reportForm.addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const category = document.getElementById('category').value;
    const description = document.getElementById('description').value;
    const location = document.getElementById('location').value;
    const latitude = document.getElementById('latitude').value;
    const longitude = document.getElementById('longitude').value;
    const photoInput = document.getElementById('photo');
    
    if (!currentUser) {
        alert('Please login first!');
        return;
    }
    
    try {
        const formData = new FormData();
        formData.append('category', category);
        formData.append('description', description);
        formData.append('location', location);
        formData.append('latitude', latitude);
        formData.append('longitude', longitude);
        formData.append('userId', currentUser.id);
        
        if (photoInput.files.length > 0) {
            formData.append('photo', photoInput.files[0]);
        }
        
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/complaints/create`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`
            },
            body: formData
        });
        
        if (response.ok) {
            const data = await response.json();
            alert(`Issue reported successfully! Complaint ID: ${data.complaintId}`);
            reportForm.reset();
            closeModal(reportModal);
            loadComplaints();
        } else {
            alert('Failed to report issue. Please try again.');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
});

// ==================== Load Complaints ====================
async function loadComplaints() {
    if (!currentUser) return;
    
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/complaints/user/${currentUser.id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            complaints = await response.json();
            displayComplaints();
            dashboardSection.classList.add('active');
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function displayComplaints() {
    complaintsList.innerHTML = '';
    
    if (complaints.length === 0) {
        complaintsList.innerHTML = '<p>No complaints yet. <a href="#" onclick="document.getElementById(\'reportBtn\').click()">Report one now</a></p>';
        return;
    }
    
    complaints.forEach(complaint => {
        const card = document.createElement('div');
        card.className = 'complaint-card';
        
        const statusClass = `status-${complaint.status.toLowerCase().replace(' ', '-')}`;
        
        card.innerHTML = `
            <div class="complaint-header">
                <div>
                    <div class="complaint-id">Complaint ID: ${complaint.id}</div>
                    <div><strong>Category:</strong> ${capitalizeFirstLetter(complaint.category)}</div>
                    <div><strong>Location:</strong> ${complaint.location}</div>
                </div>
                <div class="complaint-status ${statusClass}">${complaint.status}</div>
            </div>
            
            <div class="complaint-content">
                <p><strong>Description:</strong> ${complaint.description}</p>
                <p><strong>Reported on:</strong> ${new Date(complaint.createdAt).toLocaleDateString()}</p>
                ${complaint.assignedDepartment ? `<p><strong>Assigned to:</strong> ${complaint.assignedDepartment}</p>` : ''}
            </div>
            
            <div class="status-timeline">
                <h4>Status Timeline</h4>
                ${generateTimeline(complaint)}
            </div>
        `;
        
        complaintsList.appendChild(card);
    });
}

function generateTimeline(complaint) {
    const statuses = ['Submitted', 'Verified', 'Assigned', 'In Progress', 'Resolved'];
    const currentStatusIndex = statuses.findIndex(s => s.toLowerCase().replace(' ', '-') === complaint.status.toLowerCase().replace(' ', '-'));
    
    return statuses.map((status, index) => {
        const isCompleted = index <= currentStatusIndex;
        return `
            <div class="timeline-step ${isCompleted ? 'completed' : ''}">
                <div class="timeline-icon">${isCompleted ? '✓' : '○'}</div>
                <span>${status}</span>
            </div>
        `;
    }).join('');
}

// ==================== Load User Profile ====================
async function loadUserProfile() {
    if (!currentUser) return;
    
    try {
        const token = localStorage.getItem('token');
        const response = await fetch(`${API_BASE_URL}/users/${currentUser.id}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });
        
        if (response.ok) {
            const user = await response.json();
            displayUserProfile(user);
        }
    } catch (error) {
        console.error('Error:', error);
    }
}

function displayUserProfile(user) {
    profileContent.innerHTML = `
        <div class="profile-field">
            <label>Full Name</label>
            <p>${user.fullName}</p>
        </div>
        <div class="profile-field">
            <label>Email</label>
            <p>${user.email}</p>
        </div>
        <div class="profile-field">
            <label>Mobile Number</label>
            <p>${user.mobileNumber}</p>
        </div>
        <div class="profile-field">
            <label>Member Since</label>
            <p>${new Date(user.createdAt).toLocaleDateString()}</p>
        </div>
        <div class="profile-field">
            <label>Total Complaints</label>
            <p>${complaints.length}</p>
        </div>
    `;
}

// ==================== UI Updates ====================
function updateUIAfterLogin() {
    loginBtn.textContent = 'Report Issue';
    logoutBtn.classList.remove('hidden');
    profileBtn.classList.remove('hidden');
    loadComplaints();
}

function updateUIAfterLogout() {
    loginBtn.textContent = 'Login / Register';
    logoutBtn.classList.add('hidden');
    profileBtn.classList.add('hidden');
    dashboardSection.classList.remove('active');
    profileSection.classList.remove('active');
    complaintsList.innerHTML = '';
}

// ==================== Utility Functions ====================
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

// ==================== Check for existing session ====================
window.addEventListener('load', () => {
    const savedUser = localStorage.getItem('user');
    if (savedUser) {
        currentUser = JSON.parse(savedUser);
        updateUIAfterLogin();
    }
});
