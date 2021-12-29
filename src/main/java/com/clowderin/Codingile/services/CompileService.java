package com.clowderin.Codingile.services;

import com.clowderin.Codingile.models.request.CompileRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CompileService {

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ServletContext context;

    public  Map<String, String> gccService(CompileRequest compileRequest) throws IOException {
        String programId = java.util.UUID.randomUUID().toString();
        File tempDirectory = new File(System.getProperty("user.dir"));
        var programDir = tempDirectory+"/temp/"+programId;
        var a = new File(programDir).mkdirs();
        File programFile = new File(programDir, programId+".c");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(programDir,programId+"input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
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
        FileUtils.deleteDirectory(new File(programDir));

        BufferedReader error =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorString = new StringBuilder();
        while ((line = error.readLine()) != null) {
            errorString.append(line);
        }

        Map<String, String> response = new LinkedHashMap<>();
        if(validationService.validateOutputSize(result)) {
            response.put("data", errorString + result);
            response.put("error", null);
        }
        else {
            response.put("data", errorString + result);
            response.put("error","Your Code Produced unusually large output");
        }
        return response;
    }

    public Map<String, String> pythonService(CompileRequest compileRequest,int pyVer) throws IOException {
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
        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
        String programPath = programFile.getPath();
        String scriptName = "/python_runner.sh";
        if(pyVer == 0)
            scriptName = "/python3_runner.sh";

        ProcessBuilder builder = new ProcessBuilder("/bin/bash",scriptPath+scriptName,programPath,inputFile.toString(),programId);
        Process process = builder.start();

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        while ( (line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        String result = stringBuilder.toString();

        BufferedReader error =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorString = new StringBuilder();
        while ((line = error.readLine()) != null) {
            errorString.append(line);
        }

        inputFile.delete();
        programFile.delete();

        Map<String, String> response = new LinkedHashMap<>();
        if(validationService.validateOutputSize(result)) {
            response.put("data", errorString + result);
            response.put("error", null);
        }
        else {
            response.put("data", errorString + result);
            response.put("error","Your Code Produced unusually large output");
        }
       return response;

    }

    public  Map<String, String> javaService(CompileRequest compileRequest) throws IOException {
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

        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
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

        BufferedReader error =
                new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorString = new StringBuilder();
        while ((line = error.readLine()) != null) {
            errorString.append(line);
        }

        FileUtils.deleteDirectory(new File(programDir));
        Map<String, String> response = new LinkedHashMap<>();
        if(validationService.validateOutputSize(result)) {
            response.put("data", errorString + result);
            response.put("error", null);
        }
        else {
            response.put("data", errorString + result);
            response.put("error","Your Code Produced unusually large output");
        }
        return response;
    }
}
