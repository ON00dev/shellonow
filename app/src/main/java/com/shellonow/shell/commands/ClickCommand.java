package com.shellonow.shell.commands;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class ClickCommand extends AccessibilityCommand {
    
    public ClickCommand(Context context, AccessibilityService service) {
        super(context, service);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void execute(String[] args) {
        validateArgs(args, 2);
        
        int x = parseCoordinate(args[0]);
        int y = parseCoordinate(args[1]);
        
        Path clickPath = new Path();
        clickPath.moveTo(x, y);
        
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(clickPath, 0, 100));
        
        accessibilityService.dispatchGesture(builder.build(), null, null);
    }
} 