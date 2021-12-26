package com.clowderin.Codingile.controllers;

import com.clowderin.Codingile.models.request.CompileRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/compile")
@Slf4j
public class CodingController {
//    @GetMapping("/python")
//    public void compilePython(CompileRequest compileRequest){
//        return null;
//    }

    @GetMapping("/gcc")
    public void compileGcc(CompileRequest compileRequest) throws IOException {
        String programId = java.util.UUID.randomUUID().toString();
        File tempDirectory = new File(System.getProperty("user.dir"));
        File programFile = new File(tempDirectory+"/temp", programId+".c");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(tempDirectory+"/temp",programId+"input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        String scriptPath = System.getProperty("user.dir") + "/scripts";
        String programPath = programFile.getPath();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash",scriptPath+"/gcc_runner.sh",programPath,inputFile.toString(),programId);
        Process process = builder.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(System.getProperty("line.separator"));
        }
        String result = stringBuilder.toString();
        inputFile.delete();
        programFile.delete();

        System.out.println("Result: " + result);

    }
}

