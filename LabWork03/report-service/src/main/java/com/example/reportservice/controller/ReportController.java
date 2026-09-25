package com.example.reportservice.controller;

import com.example.reportservice.client.ComputerClient;
import com.example.reportservice.model.Computer;
import lombok.RequiredArgsConstructor;
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

    private final ComputerClient client;

    @GetMapping("/computers")
    public List<Computer> getAll() {
        return client.getAllComputers();
    }

    @GetMapping("/available")
    public List<Computer> getAvailable() {
        return client.getByStatus(Computer.ComputerStatus.AVAILABLE);
    }

    @GetMapping("/sold")
    public List<Computer> getSold() {
        return client.getByStatus(Computer.ComputerStatus.SOLD);
    }

    @GetMapping("/summary")
    public Map<String, Long> getSummary() {
        List<Computer> all = client.getAllComputers();

        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("total", (long) all.size());
        summary.put("available", all.stream()
                .filter(c -> c.getStatus() == Computer.ComputerStatus.AVAILABLE).count());
        summary.put("sold", all.stream()
                .filter(c -> c.getStatus() == Computer.ComputerStatus.SOLD).count());
        return summary;
    }
}