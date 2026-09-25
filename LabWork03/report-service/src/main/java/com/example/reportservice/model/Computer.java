package com.example.reportservice.model;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Computer {

    private Long id;

    private String brand;

    private String model;

    private String serialNumber;

    private BigDecimal price;

    private ComputerStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum ComputerStatus {
        AVAILABLE, SOLD
    }
}