package com.shellonow;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ShellExecutor {
    
    public static String execute(String command) {
        StringBuilder output = new StringBuilder();
        Process process = null;
        
        // Primeiro tenta executar com root
        try {
            process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            process.waitFor();
            
            // Se conseguiu executar com root, retorna o resultado
            if (process.exitValue() == 0) {
                return output.toString();
            }
            
        } catch (IOException | InterruptedException e) {
            // Ignora erro e tenta sem root
        }
        
        // Se não conseguiu com root, tenta sem root
        output = new StringBuilder();
        try {
            process = Runtime.getRuntime().exec("sh");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));
            
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Captura também erros
            BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));
                
            while ((line = errorReader.readLine()) != null) {
                output.append("Erro: ").append(line).append("\n");
            }

            process.waitFor();
            
        } catch (IOException | InterruptedException e) {
            return "Erro ao executar comando: " + e.getMessage();
        }
        
        return output.toString();
    }

    public static String executeScript(String scriptPath) {
        // Verifica se o arquivo existe e tem permissão de execução
        StringBuilder output = new StringBuilder();
        
        try {
            // Primeiro tenta dar permissão de execução
            execute("chmod +x " + scriptPath);
            
            // Tenta executar o script
            return execute("sh " + scriptPath);
            
        } catch (Exception e) {
            return "Erro ao executar script: " + e.getMessage();
        }
    }
    
    // Método para verificar se tem acesso root
    public static boolean hasRootAccess() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }
} 