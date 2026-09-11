package com.example.computerstore.repository;

import com.example.computerstore.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComputerRepository extends JpaRepository<Computer, Long> {
    List<Computer> findByStatus(Computer.ComputerStatus status);
    
    @Query("SELECT c.status, COUNT(c) FROM Computer c GROUP BY c.status")
    List<Object[]> countByStatus();
}