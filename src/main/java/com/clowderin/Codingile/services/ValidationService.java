package com.clowderin.Codingile.services;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ValidationService {
    public boolean validateOutputSize(String output){
        return output.getBytes(StandardCharsets.UTF_8).length <= 512;
    }
}
