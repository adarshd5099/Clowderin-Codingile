package com.clowderin.Codingile.controllers;

import com.clowderin.Codingile.models.request.CompileRequest;
import com.clowderin.Codingile.services.CompileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/compile")
@Slf4j
public class CodingController {

    @Autowired
    private CompileService compileService;

    @GetMapping("/gcc")
    public Map<String,String> compileGcc(CompileRequest compileRequest) throws IOException {
        var result = compileService.gccService(compileRequest);
        Map<String,String> response = new LinkedHashMap<>();
        response.put("data",result);
        response.put("error",null);
        return response;
    }

    @GetMapping("/python")
    public Map<String,String> compilePython(CompileRequest compileRequest) throws IOException {
        var result = compileService.pythonService(compileRequest);
        Map<String,String> response = new LinkedHashMap<>();
        response.put("data",result);
        response.put("error",null);
        return response;
    }

    @GetMapping("/java")
    public Map<String,String> compileJava(CompileRequest compileRequest) throws IOException {
        var result = compileService.javaService(compileRequest);
        Map<String,String> response = new LinkedHashMap<>();
        response.put("data",result);
        response.put("error",null);
        return response;
    }

}

