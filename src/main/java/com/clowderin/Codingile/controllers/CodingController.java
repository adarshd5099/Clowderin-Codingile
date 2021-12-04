package com.clowderin.Codingile.controllers;

import com.clowderin.Codingile.models.request.CompileRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class CodingController {
    @GetMapping("/python")
    public ResponseBody compilePython(CompileRequest compileRequest){
        return null;
    }
}

