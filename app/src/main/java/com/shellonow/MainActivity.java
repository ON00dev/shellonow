package com.shellonow;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import com.shellonow.shell.VirtualShell;

public class MainActivity extends AppCompatActivity {
    
    private static final int PERMISSION_REQUEST_CODE = 123;
    private static final int PICK_SCRIPT_FILE = 456;
    
    private EditText commandInput;
    private TextView outputText;
    private String selectedScriptPath;
    private VirtualShell virtualShell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        commandInput = findViewById(R.id.commandInput);
        outputText = findViewById(R.id.outputText);
        
        Button executeButton = findViewById(R.id.executeButton);
        Button selectFileButton = findViewById(R.id.selectFileButton);
        Button executeScriptButton = findViewById(R.id.executeScriptButton);

        executeButton.setOnClickListener(v -> executeCommand());
        selectFileButton.setOnClickListener(v -> selectScript());
        executeScriptButton.setOnClickListener(v -> executeSelectedScript());

        checkPermissions();
        virtualShell = new VirtualShell(this);
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, 
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void executeCommand() {
        String command = commandInput.getText().toString();
        if (!command.isEmpty()) {
            virtualShell.executeCommand(command);
        } else {
            Toast.makeText(this, "Digite um comando", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectScript() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_SCRIPT_FILE);
    }

    private void executeSelectedScript() {
        if (selectedScriptPath != null) {
            virtualShell.executeScript(selectedScriptPath);
        } else {
            Toast.makeText(this, "Selecione um arquivo .sh primeiro", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_SCRIPT_FILE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            selectedScriptPath = uri.getPath();
            Toast.makeText(this, "Arquivo selecionado: " + selectedScriptPath, Toast.LENGTH_SHORT).show();
        }
    }
} 