package com.clowderin.codingile.controllers;

import com.clowderin.codingile.models.request.CompileRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/compile")
public class CodingController {
    @GetMapping("/python")
    public void compilePython(@Valid @RequestBody CompileRequest compileRequest) {
        var a = compileRequest;

    }
}

