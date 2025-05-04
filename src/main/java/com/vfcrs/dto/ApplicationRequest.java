package com.vfcrs.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicationRequest {
    private String vehicleNumber;
    private String chassisNumber;
    private LocalDate registrationDate;
    private String documentUrl;
}