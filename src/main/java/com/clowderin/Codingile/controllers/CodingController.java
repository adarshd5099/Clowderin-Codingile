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
@Slf4j
public class CodingController {

    @Autowired
    private CompileService compileService;
    @PostMapping("/compile")
    public Map<String, String> compile(@RequestBody CompileRequest compileRequest) throws IOException {
        var result = "";
        switch (compileRequest.getLanguageCode()) {
            case "0":
            case "1":
                return compileService.pythonService(compileRequest, Integer.parseInt(compileRequest.getLanguageCode()));
            case "8":
                return compileService.javaService(compileRequest);
            default:
                return compileService.gccService(compileRequest);
        }
    }

    @GetMapping("/hello")
    public String hello(){
        return "Iam Working yay!!";
    }

}

