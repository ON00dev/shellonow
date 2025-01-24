package com.shellonow.shell;

public class Config {
    // Exemplo de como usar: monkey -p com.example.app -c android.intent.category.LAUNCHER 1
    public static String formatMonkeyCommand(String packageName) {
        return String.format("monkey -p %s -c android.intent.category.LAUNCHER 1", packageName);
    }

    // Exemplo de como usar: am start -n com.example.app/.MainActivity
    public static String formatStartActivityCommand(String packageName, String activityName) {
        return String.format("am start -n %s/%s", packageName, activityName);
    }

    // Exemplo de como usar: am force-stop com.example.app
    public static String formatStopAppCommand(String packageName) {
        return String.format("am force-stop %s", packageName);
    }

    // Exemplo de como usar: pm clear com.example.app
    public static String formatClearAppDataCommand(String packageName) {
        return String.format("pm clear %s", packageName);
    }
} 