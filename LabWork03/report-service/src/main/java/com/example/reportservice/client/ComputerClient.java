package com.example.reportservice.client;

import com.example.reportservice.model.Computer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ComputerClient {

    private final RestTemplate restTemplate;

    @Value("${computer-store.base-url}")
    private String baseUrl;

    public List<Computer> getAllComputers() {
        Computer[] arr = restTemplate.getForObject(
                baseUrl + "/api/computers", Computer[].class);
        return arr != null ? Arrays.asList(arr) : List.of();
    }

    public List<Computer> getByStatus(Computer.ComputerStatus status) {
        Computer[] arr = restTemplate.getForObject(
                baseUrl + "/api/computers?status=" + status, Computer[].class);
        return arr != null ? Arrays.asList(arr) : List.of();
    }
}