package com.example.computerstore.controller;

import com.example.computerstore.model.Computer;
import com.example.computerstore.service.ComputerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/computers")
@RequiredArgsConstructor
public class ComputerController {

    private final ComputerService service;

    @GetMapping
    public List<Computer> getAll(@RequestParam(required = false) Computer.ComputerStatus status) {
        if (status != null) {
            return service.getComputersByStatus(status);
        }
        return service.getAllComputers();
    }

    @GetMapping("/{id}")
    public Computer getById(@PathVariable Long id) {
        return service.getComputerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Computer create(@RequestBody Computer computer) {
        return service.createComputer(computer);
    }

    @PutMapping("/{id}")
    public Computer update(@PathVariable Long id, @RequestBody Computer computer) {
        return service.updateComputer(id, computer);
    }

    @PatchMapping("/{id}/sell")
    public Computer sell(@PathVariable Long id) {
        return service.sellComputer(id);
    }

    @GetMapping("/report")
    public Map<Computer.ComputerStatus, Long> getReport() {
        return service.getReport();
    }
}