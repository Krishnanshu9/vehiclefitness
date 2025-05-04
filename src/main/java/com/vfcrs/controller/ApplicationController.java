package com.vfcrs.controller;

import com.vfcrs.dto.ApplicationRequest;
import com.vfcrs.dto.ApplicationResponse;
import com.vfcrs.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    @Autowired
    private ApplicationService applicationService;

    @GetMapping("/status")
    public ResponseEntity<List<ApplicationResponse>> getApplications() {
        return ResponseEntity.ok(applicationService.getUserApplications());
    }

    @PostMapping("/apply")
    public ResponseEntity<ApplicationResponse> submitApplication(
        @RequestBody ApplicationRequest request
    ) {
        return ResponseEntity.ok(applicationService.createApplication(request));
    }
}