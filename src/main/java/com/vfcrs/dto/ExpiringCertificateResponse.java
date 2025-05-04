package com.vfcrs.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ExpiringCertificateResponse {
    private String vehicleNumber;
    private LocalDate expiryDate;
}
