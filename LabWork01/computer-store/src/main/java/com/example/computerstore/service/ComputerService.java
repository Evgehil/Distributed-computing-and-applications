package com.example.computerstore.service;

import com.example.computerstore.model.Computer;
import com.example.computerstore.repository.ComputerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComputerService {

    private final ComputerRepository repository;

    @Transactional(readOnly = true)
    public List<Computer> getAllComputers() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Computer> getComputersByStatus(Computer.ComputerStatus status) {
        return repository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Computer getComputerById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Computer not found with id: " + id));
    }

    @Transactional
    public Computer createComputer(Computer computer) {
        if (repository.findAll().stream().anyMatch(c -> c.getSerialNumber().equals(computer.getSerialNumber()))) {
            throw new RuntimeException("Serial number already exists");
        }
        computer.setStatus(Computer.ComputerStatus.AVAILABLE);
        return repository.save(computer);
    }

    @Transactional
    public Computer updateComputer(Long id, Computer updatedComputer) {
        Computer existing = getComputerById(id);
        existing.setBrand(updatedComputer.getBrand());
        existing.setModel(updatedComputer.getModel());
        existing.setSerialNumber(updatedComputer.getSerialNumber());
        existing.setPrice(updatedComputer.getPrice());
        return repository.save(existing);
    }

    @Transactional
    public Computer sellComputer(Long id) {
        Computer computer = getComputerById(id);
        if (computer.getStatus() == Computer.ComputerStatus.SOLD) {
            throw new RuntimeException("Computer is already sold");
        }
        computer.setStatus(Computer.ComputerStatus.SOLD);
        return repository.save(computer);
    }

    @Transactional(readOnly = true) 
    public Map<Computer.ComputerStatus, Long> getReport() {
    Map<Computer.ComputerStatus, Long> result = new EnumMap<>(Computer.ComputerStatus.class);
    for (Computer.ComputerStatus status : Computer.ComputerStatus.values()) {
        result.put(status, 0L);
    }
    for (Object[] row : repository.countByStatus()) {
        result.put((Computer.ComputerStatus) row[0], (Long) row[1]);
    }
    return result;
    }
}