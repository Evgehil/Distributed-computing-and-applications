package com.example.reportservice.controller;

import com.example.reportservice.model.Computer;
import com.example.reportservice.repository.ComputerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ComputerRepository repository;

    @GetMapping("/computers")
    @Transactional(readOnly = true)
    public List<Computer> getAllComputers() {
        return repository.findAll();
    }

    @GetMapping("/available")
    @Transactional(readOnly = true)
    public List<Computer> getAvailable() {
        return repository.findByStatus(Computer.ComputerStatus.AVAILABLE);
    }

    @GetMapping("/sold")
    @Transactional(readOnly = true)
    public List<Computer> getSold() {
        return repository.findByStatus(Computer.ComputerStatus.SOLD);
    }

    @GetMapping("/summary")
    @Transactional(readOnly = true)
    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("total", repository.count());
        summary.put("available", repository.countByStatus(Computer.ComputerStatus.AVAILABLE));
        summary.put("sold", repository.countByStatus(Computer.ComputerStatus.SOLD));
        return summary;
    }
}