package com.clowderin.Codingile.services;

import com.clowderin.Codingile.models.request.CompileRequest;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class CompileService {
    public String gccService(CompileRequest compileRequest) throws IOException {
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
        }
        String result = stringBuilder.toString();
        inputFile.delete();
        programFile.delete();

        System.out.println("Result: " + result);
        return result;
    }

    public String pythonService(CompileRequest compileRequest) throws IOException {
        String programId = java.util.UUID.randomUUID().toString();
        File tempDirectory = new File(System.getProperty("user.dir"));
        File programFile = new File(tempDirectory+"/temp", programId+".py");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(tempDirectory+"/temp",programId+"input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        String scriptPath = System.getProperty("user.dir") + "/scripts";
        String programPath = programFile.getPath();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash",scriptPath+"/python_runner.sh",programPath,inputFile.toString(),programId);
        Process process = builder.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String result = stringBuilder.toString();
        inputFile.delete();
        programFile.delete();

        System.out.println("Result: " + result);
        return result;
    }

    public String javaService(CompileRequest compileRequest) throws IOException {
        String programId = java.util.UUID.randomUUID().toString();
        File tempDirectory = new File(System.getProperty("user.dir"));
        var programDir = tempDirectory+"/temp/"+programId;
        var a = new File(programDir).mkdirs();
        File programFile = new File(programDir, "Main.java");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(programDir,programId+"input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        String scriptPath = System.getProperty("user.dir") + "/scripts";
        String programPath = programFile.getPath();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash",scriptPath+"/java_runner.sh",programPath,inputFile.toString(),programId);
        Process process = builder.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String result = stringBuilder.toString();
        inputFile.delete();
        programFile.delete();

        System.out.println("Result: " + result);
        return result;
    }
}
