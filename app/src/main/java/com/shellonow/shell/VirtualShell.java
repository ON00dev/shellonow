package com.shellonow.shell;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class VirtualShell {
    private Context context;
    private Handler mainHandler;

    public VirtualShell(Context context) {
        this.context = context;
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void executeCommand(String command) {
        new Thread(() -> {
            String output = ShellExecutor.execute(command);
            showOutput(output);
        }).start();
    }

    public void executeScript(String scriptPath) {
        new Thread(() -> {
            String output = ShellExecutor.executeScript(scriptPath);
            showOutput(output);
        }).start();
    }

    private void showOutput(final String output) {
        mainHandler.post(() -> {
            if (output != null && !output.trim().isEmpty()) {
                Toast.makeText(context, output, Toast.LENGTH_LONG).show();
            }
        });
    }
} 