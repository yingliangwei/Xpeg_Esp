package com.orca.xarg;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;

import java.io.DataOutputStream;
import java.io.IOException;

public class Overlay extends Service {
    private static Overlay Instance;
    static Context ctx;
    View mainView;
    ESPView overlayView;
    Process process;
    WindowManager windowManager;

    private native void Close();

    private void DrawCanvas() {
        int c;
        if (Build.VERSION.SDK_INT >= 26) {
            c = 2038;
        } else {
            c = 2006;
        }
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 0, getNavigationBarHeight(), c, 1304, 1);
        if (Build.VERSION.SDK_INT >= 28)
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        this.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.windowManager.addView(this.overlayView, layoutParams);
    }

    public static native void DrawOn(ESPView paramESPView, Canvas paramCanvas);

    public static void Stop(Context paramContext) {
        try {
            Class<?> clazz = Class.forName("com.orca.xarg.Overlay");
            paramContext.stopService(new Intent(paramContext, clazz));
            try {
                clazz = Class.forName("com.orca.xarg.FloatingActivity");
                paramContext.stopService(new Intent(paramContext, clazz));
            } catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    static boolean getConfig(String paramString) {
        return ctx.getSharedPreferences("espValue", 0).getBoolean(paramString, false);
    }


    static native boolean getReady(int paramInt);


    private int getNavigationBarHeight() {
        boolean hasPermanentMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();
        int identifier = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (identifier <= 0 || hasPermanentMenuKey) {
            return 0;
        }
        return getResources().getDimensionPixelSize(identifier);
    }

    public void Shell(String str) {
        DataOutputStream dataOutputStream = null;
        try {
            this.process = Runtime.getRuntime().exec(str);
        } catch (IOException e) {
            e.printStackTrace();
            this.process = null;
        }
        if (this.process != null) {
            dataOutputStream = new DataOutputStream(this.process.getOutputStream());
        }
        try {
            dataOutputStream.flush();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            this.process.waitFor();
        } catch (InterruptedException e3) {
            e3.printStackTrace();
        }
    }

    public void Start(Context paramContext, int paramInt1, int paramInt2) {
        if (Instance == null) {
            new Thread(() -> Overlay.getReady(paramInt1)).start();
            new Thread(() -> {
                long l = 100L;
                try {
                    Thread.sleep(l);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
                StartDaemon(paramContext, paramInt2);
            }).start();
        }
    }

    public void StartDaemon(Context paramContext, int paramInt) {
        Shell(FloatingActivity.socket);
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ctx = this;
        Start(ctx, 0, 1);
        this.windowManager = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
        this.overlayView = new ESPView(ctx);
        DrawCanvas();
    }

    public void onDestroy() {
        super.onDestroy();
        Close();
        if (this.overlayView != null) {
            ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).removeView(this.overlayView);
            this.overlayView = null;
        }
        this.process.destroy();
    }


    static {
        System.loadLibrary("native");
    }
}
