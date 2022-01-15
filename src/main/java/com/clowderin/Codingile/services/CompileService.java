package com.clowderin.Codingile.services;

import com.clowderin.Codingile.models.request.CompileRequest;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CompileService {
    @Autowired
    private ValidationService validationService;
    @Autowired
    private ServletContext context;

    public Map<String, String> gccService(CompileRequest compileRequest) throws IOException {
        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
//        String scriptPath = System.getProperty("user.dir") + "/src/main/resources/scripts";
        File tempDirectory = new File(System.getProperty("user.dir"));
        String programId = java.util.UUID.randomUUID().toString();
        var programDir = tempDirectory + "/temp/" + programId;
        var a = new File(programDir).mkdirs();
        File programFile = new File(programDir, "program.c");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(programDir, programId + "input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        File outputFile = new File(programDir, programId + ".txt");
        String programPath = programFile.getPath();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", scriptPath + "/gcc_runner.sh", programPath, inputFile.toString(), programId, outputFile.toString());
        Process process = builder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = readOutput(outputFile);
        String errorString = errorCleaner(readError(process), String.valueOf(programFile),"program.c");
        FileUtils.deleteDirectory(new File(programDir));
        return createResponse(result, errorString);
    }

    public Map<String, String> pythonService(CompileRequest compileRequest, int pyVer) throws IOException {
        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
//        String scriptPath = System.getProperty("user.dir") + "/src/main/resources/scripts";
        File tempDirectory = new File(System.getProperty("user.dir"));
        String programId = java.util.UUID.randomUUID().toString();
        var programDir = tempDirectory + "/temp/" + programId;
        var a = new File(programDir).mkdirs();
        File programFile = new File(programDir, "Main.py");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(programDir, programId + "input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        File outputFile = new File(programDir, programId + ".txt");
        String programPath = programFile.getPath();
        String scriptName = "/python_runner.sh";
        if (pyVer == 0) scriptName = "/python3_runner.sh";
        ProcessBuilder builder = new ProcessBuilder("/bin/bash", scriptPath + scriptName, programPath, inputFile.toString(), programId, outputFile.toString());
        Process process = builder.start();
        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = readOutput(outputFile);
        String errorString = errorCleaner(readError(process), String.valueOf(programFile),"Main.py");        FileUtils.deleteDirectory(new File(programDir));
        return createResponse(result, errorString);
    }

    public Map<String, String> javaService(CompileRequest compileRequest) throws IOException {
        String scriptPath = context.getRealPath("WEB-INF/classes/scripts");
        File tempDirectory = new File(System.getProperty("user.dir"));
        String programId = java.util.UUID.randomUUID().toString();
        var programDir = tempDirectory + "/temp/" + programId;
        var a = new File(programDir).mkdirs();
        File programFile = new File(programDir, "Main.java");
        FileWriter myWriter = new FileWriter(programFile);
        myWriter.write(compileRequest.getProgramBody());
        myWriter.close();

        File inputFile = new File(programDir, programId + "input");
        FileWriter inputWriter = new FileWriter(inputFile);
        inputWriter.write(compileRequest.getInput());
        inputWriter.close();

        File outputFile = new File(programDir, programId + ".txt");
        String programPath = programFile.getPath();

        ProcessBuilder builder = new ProcessBuilder("/bin/bash", scriptPath + "/java_runner.sh", programPath, inputFile.toString(), programId, outputFile.toString());
        Process process = builder.start();

        try {
            process.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = readOutput(outputFile);
        String errorString = errorCleaner(readError(process), String.valueOf(programFile),"Main.java");
        errorString = errorString.replace("NOTE: Picked up JDK_JAVA_OPTIONS: --add-opens=java.base/java.lang=ALL-UNNAMED --add-opens=java.base/java.io=ALL-UNNAMED --add-opens=java.base/java.util=ALL-UNNAMED --add-opens=java.base/java.util.concurrent=ALL-UNNAMED --add-opens=java.rmi/sun.rmi.transport=ALL-UNNAMED","");
        FileUtils.deleteDirectory(new File(programDir));
        return createResponse(result, errorString);
    }

    private Map<String, String> createResponse(String result, String errorString) {
        Map<String, String> response = new LinkedHashMap<>();
        if (validationService.validateOutputSize(result)) {
            response.put("data", result);
            response.put("error", String.valueOf(errorString));
        } else {
            response.put("data", errorString + result);
            response.put("error", "Your Code Produced unusually large output");
        }
        return response;
    }

    private StringBuilder readError(Process process) throws IOException {
        BufferedReader error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        StringBuilder errorString = new StringBuilder();
        String line;
        while ((line = error.readLine()) != null) {
            errorString.append(line);
        }
        return errorString;
    }

    private String errorCleaner(StringBuilder errorString, String programFile,String programName) {
        String error = errorString.toString();
        return error.replace(programFile, programName).trim().replaceAll(" +", " ");
    }

    private String readOutput(File outputFile) {
        String result = "";
        try (BufferedReader br = new BufferedReader(new FileReader(outputFile))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
                if (line != null) sb.append("\n");
            }
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
