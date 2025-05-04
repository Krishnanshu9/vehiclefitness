package com.vfcrs.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ApplicationResponse {
    private String vehicleNumber;
    private String chassisNumber;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}