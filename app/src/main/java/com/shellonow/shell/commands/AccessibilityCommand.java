package com.shellonow.shell.commands;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import com.shellonow.shell.Command;

public abstract class AccessibilityCommand implements Command {
    protected Context context;
    protected AccessibilityService accessibilityService;

    public AccessibilityCommand(Context context, AccessibilityService service) {
        this.context = context;
        this.accessibilityService = service;
    }

    protected void validateArgs(String[] args, int expectedCount) {
        if (args == null || args.length != expectedCount) {
            throw new IllegalArgumentException(
                String.format("Comando requer %d argumentos, mas recebeu %d", 
                expectedCount, args == null ? 0 : args.length));
        }
    }

    protected int parseCoordinate(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Coordenada inválida: " + value);
        }
    }
} 