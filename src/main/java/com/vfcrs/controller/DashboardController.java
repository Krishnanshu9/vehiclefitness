package com.vfcrs.controller;

import com.vfcrs.dto.DashboardResponse;
import com.vfcrs.dto.ExpiringCertificateResponse;
import com.vfcrs.dto.ApplicationResponse;
import com.vfcrs.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/dashboard")
    public ResponseEntity<DashboardResponse> getDashboard() {
        // 1) Fetch all applications for the user
        List<ApplicationResponse> recent = applicationService.getUserApplications();

        // 2) Calculate total count
        int total = recent.size();

        // 3) For now, return no expiring certificates
        //    (Or implement your own logic based on registrationDate + validity)
        List<ExpiringCertificateResponse> expiring = Collections.emptyList();

        // 4) Package into the DTO
        DashboardResponse resp = new DashboardResponse();
        resp.setTotalApplications(total);
        resp.setRecentApplications(recent);
        resp.setExpiringCertificates(expiring);

        return ResponseEntity.ok(resp);
    }
}
