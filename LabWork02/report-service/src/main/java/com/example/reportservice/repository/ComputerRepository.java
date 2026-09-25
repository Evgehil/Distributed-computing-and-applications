package com.example.reportservice.repository;

import com.example.reportservice.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Long> {
    List<Computer> findByStatus(Computer.ComputerStatus status);
    long countByStatus(Computer.ComputerStatus status);
}