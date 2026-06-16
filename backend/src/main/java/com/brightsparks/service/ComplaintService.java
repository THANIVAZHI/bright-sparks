package com.brightsparks.service;

import com.brightsparks.model.Complaint;
import com.brightsparks.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepository;

    public Complaint createComplaint(Complaint complaint) {
        complaint.setComplaintId(generateComplaintId());
        complaint.setStatus("Submitted");
        assignToDepartment(complaint);
        return complaintRepository.save(complaint);
    }

    public Optional<Complaint> getComplaintById(Long id) {
        return complaintRepository.findById(id);
    }

    public Optional<Complaint> getByComplaintId(String complaintId) {
        return complaintRepository.findByComplaintId(complaintId);
    }

    public List<Complaint> getUserComplaints(Long userId) {
        return complaintRepository.findByUserId(userId);
    }

    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(status);
    }

    public List<Complaint> getComplaintsByCategory(String category) {
        return complaintRepository.findByCategory(category);
    }

    public List<Complaint> getComplaintsByDepartment(String department) {
        return complaintRepository.findByAssignedDepartment(department);
    }

    public Complaint updateComplaintStatus(Long id, String status) {
        Optional<Complaint> complaint = complaintRepository.findById(id);
        if (complaint.isPresent()) {
            Complaint c = complaint.get();
            c.setStatus(status);
            return complaintRepository.save(c);
        }
        throw new RuntimeException("Complaint not found");
    }

    public void deleteComplaint(Long id) {
        complaintRepository.deleteById(id);
    }

    public Complaint updateComplaint(Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    // Generate unique complaint ID
    private String generateComplaintId() {
        return "COMPLAINT-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    // Smart routing to assign complaints to departments based on category
    private void assignToDepartment(Complaint complaint) {
        switch (complaint.getCategory().toLowerCase()) {
            case "streetlight":
                complaint.setAssignedDepartment("Electricity Department");
                break;
            case "garbage":
                complaint.setAssignedDepartment("Sanitation Department");
                break;
            case "pothole":
            case "road":
                complaint.setAssignedDepartment("Public Works Department");
                break;
            case "water":
                complaint.setAssignedDepartment("Water Supply Department");
                break;
            case "dumping":
                complaint.setAssignedDepartment("Environmental Department");
                break;
            default:
                complaint.setAssignedDepartment("General Services");
        }
        complaint.setStatus("Verified");
    }
}