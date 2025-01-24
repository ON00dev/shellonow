package com.shellonow.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

public class AutomationService extends AccessibilityService {
    private static AutomationService instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // Implementar se necessário monitorar eventos
    }

    @Override
    public void onInterrupt() {
        // Implementar se necessário tratar interrupções
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // Configurações adicionais do serviço, se necessário
    }

    public static AutomationService getInstance() {
        return instance;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        instance = null;
        return super.onUnbind(intent);
    }

    public void clickOnScreen(float x, float y) {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 100));
        dispatchGesture(builder.build(), null, null);
    }

    public void scroll(float startX, float startY, float endX, float endY) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        GestureDescription.Builder builder = new GestureDescription.Builder();
        builder.addStroke(new GestureDescription.StrokeDescription(path, 0, 500));
        dispatchGesture(builder.build(), null, null);
    }

    public void launchApp(String packageName) {
        Intent intent = getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
} 