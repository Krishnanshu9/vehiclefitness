package com.vfcrs.dto;

import lombok.Data;
import java.util.List;

@Data
public class DashboardResponse {
    private int totalApplications;
    private List<ApplicationResponse> recentApplications;
    private List<ExpiringCertificateResponse> expiringCertificates;
}
