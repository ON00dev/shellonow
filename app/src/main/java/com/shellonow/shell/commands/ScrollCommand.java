package com.shellonow.shell.commands;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Context;
import android.graphics.Path;
import android.os.Build;
import androidx.annotation.RequiresApi;

public class ScrollCommand extends AccessibilityCommand {
    
    public ScrollCommand(Context context, AccessibilityService service) {
        super(context, service);
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void execute(String[] args) {
        validateArgs(args, 4);
        
        int startX = parseCoordinate(args[0]);
        int startY = parseCoordinate(args[1]);
        int endX = parseCoordinate(args[2]);
        int endY = parseCoordinate(args[3]);
        
        Path scrollPath = new Path();
        scrollPath.moveTo(startX, startY);
        scrollPath.lineTo(endX, endY);
        
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(scrollPath, 0, 500));
        
        accessibilityService.dispatchGesture(builder.build(), null, null);
    }
} 