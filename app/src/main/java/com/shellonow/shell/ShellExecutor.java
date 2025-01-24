package com.shellonow.shell;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class ShellExecutor {
    private static final int TIMEOUT = 15000; // 15 segundos

    public static String execute(String command) {
        StringBuilder output = new StringBuilder();
        Process process = null;
        try {
            // Tenta executar com root primeiro
            try {
                process = Runtime.getRuntime().exec("su");
            } catch (Exception e) {
                // Se falhar, tenta sem root
                process = Runtime.getRuntime().exec("sh");
            }

            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            
            // Se for um comando monkey, ajusta o formato
            if (command.contains("monkey")) {
                command = command.replace("adb shell ", "");
                // Garante que o monkey está rodando em modo não interativo
                if (!command.contains("--ignore-crashes")) {
                    command += " --ignore-crashes --ignore-timeouts --ignore-security-exceptions --monitor-native-crashes";
                }
            }
            
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            while ((line = errorReader.readLine()) != null) {
                output.append("ERROR: ").append(line).append("\n");
            }

            process.waitFor(TIMEOUT, TimeUnit.MILLISECONDS);
            
        } catch (IOException | InterruptedException e) {
            output.append("Exception: ").append(e.getMessage());
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
        return output.toString();
    }

    public static String executeScript(String scriptPath) {
        return execute("sh " + scriptPath);
    }

    public static boolean hasRootAccess() {
        try {
            Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            process.waitFor();
            return line != null && line.contains("uid=0");
        } catch (Exception e) {
            return false;
        }
    }
} 