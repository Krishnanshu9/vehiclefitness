package com.vfcrs.service;

import com.vfcrs.dto.ApplicationRequest;
import com.vfcrs.dto.ApplicationResponse;
import com.vfcrs.entity.Application;
import com.vfcrs.entity.User;
import com.vfcrs.repository.ApplicationRepository;
import com.vfcrs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ApplicationResponse> getUserApplications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return applicationRepository.findByUserEmail(email).stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    public ApplicationResponse createApplication(ApplicationRequest request) {
        // 1) Get current user's email from the JWT-authenticated context
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        // 2) Load the full User entity
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Authenticated user not found"));

        // 3) Build and save the Application with the User reference
        Application application = new Application();
        application.setVehicleNumber(request.getVehicleNumber());
        application.setChassisNumber(request.getChassisNumber());
        application.setRegistrationDate(request.getRegistrationDate());
        application.setDocumentUrl(request.getDocumentUrl());
        application.setStatus("PENDING");
        application.setUser(user);         // ← Associate the User entity
        application.setUserEmail(email);   // ← Keep for quick lookup if needed

        Application saved = applicationRepository.save(application);

        return mapToResponse(saved);
    }

    private ApplicationResponse mapToResponse(Application app) {
        ApplicationResponse resp = new ApplicationResponse();
        resp.setVehicleNumber(app.getVehicleNumber());
        resp.setChassisNumber(app.getChassisNumber());
        resp.setStatus(app.getStatus());
        resp.setCreatedAt(app.getCreatedAt());
        resp.setUpdatedAt(app.getUpdatedAt());
        return resp;
    }
}
