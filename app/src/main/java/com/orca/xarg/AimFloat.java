package com.orca.xarg;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AimFloat extends Service {
    public static final String LOG_TAG = new String(Base64.decode("emVjbGF5eA==", 0));
    private float downRawX;
    private float downRawY;
    public boolean isBtnChecked = false;
    private RelativeLayout layout_icon_control_view;
    private PowerManager.WakeLock mWakeLock;
    private View mainView;
    private WindowManager.LayoutParams paramsMainView;
    private WindowManager windowManagerMainView;

    static {
        System.loadLibrary("native");
    }

    @SuppressLint({"ClickableViewAccessibility", "ResourceType"})
    private void ShowMainView() {
        this.mainView = LayoutInflater.from(this).inflate(R.layout.aim_floating,new FrameLayout(this),false);
        this.layout_icon_control_view = this.mainView.findViewById(R.id.layout_icon_control_aim);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, getLayoutType(), getFlagsType(), -3);
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.x = 0;
        layoutParams.y = 0;
        this.paramsMainView = layoutParams;
        this.windowManagerMainView = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.windowManagerMainView.addView(this.mainView, this.paramsMainView);
        this.isBtnChecked = false;
        this.layout_icon_control_view.setOnTouchListener(new View.OnTouchListener() {
            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            @Override
            public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                ImageView imageView;
                int i;
                int j;
                switch (param1MotionEvent.getAction()) {
                    default:
                        return false;
                    case 0:
                        this.initialX = paramsMainView.x;
                        this.initialY = paramsMainView.y;
                        this.initialTouchX = param1MotionEvent.getRawX();
                        this.initialTouchY = param1MotionEvent.getRawY();
                        downRawX = param1MotionEvent.getRawX();
                        downRawY = param1MotionEvent.getRawY();
                        return true;
                    case 1:
                        i = (int) (param1MotionEvent.getRawX() - this.initialTouchX);
                        j = (int) (param1MotionEvent.getRawY() - this.initialTouchY);
                        if (i < 10 && j < 10 && isViewFlesed()) {
                            imageView = (ImageView) mainView.findViewById(R.id.imageview_aim);
                            if (!isBtnChecked) {
                                AimMemory(45, true);
                                imageView.setImageDrawable(getResources().getDrawable(R.drawable.a1));
                                isBtnChecked = true;
                                return true;
                            }
                        } else {
                            return true;
                        }
                        AimMemory(45, false);
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.a2));
                        isBtnChecked = false;
                        return true;
                    case 2:
                        break;
                }
                paramsMainView.x = this.initialX + (int) (param1MotionEvent.getRawX() - this.initialTouchX);
                paramsMainView.y = this.initialY + (int) (param1MotionEvent.getRawY() - this.initialTouchY);
                windowManagerMainView.updateViewLayout(mainView, paramsMainView);
                return true;
            }
        });
    }

    private int getFlagsType() {
        return 8;
    }

    private static int getLayoutType() {
        return (Build.VERSION.SDK_INT >= 26) ? 2038 : ((Build.VERSION.SDK_INT >= 24) ? 2002 : ((Build.VERSION.SDK_INT >= 23) ? 2005 : 2003));
    }

    private boolean isViewFlesed() {
        return !(this.mainView != null && this.layout_icon_control_view.getVisibility() != View.VISIBLE);
    }

    public native void AimMemory(int paramInt, boolean paramBoolean);

    @Override
    public IBinder onBind(Intent paramIntent) {
        return (IBinder) null;
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onCreate() {
        super.onCreate();

        ShowMainView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.isBtnChecked) this.isBtnChecked = false;
        if (this.mWakeLock != null) {
            this.mWakeLock.release();
            this.mWakeLock = null;
        }
        if (this.mainView != null) this.windowManagerMainView.removeView(this.mainView);
    }

    @SuppressLint({"InvalidWakeLockTag", "WakelockTimeout"})
    @Override
    public int onStartCommand(Intent paramIntent, int paramInt1, int paramInt2) {
        if (this.mWakeLock == null) {
            this.mWakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(1, LOG_TAG);
            this.mWakeLock.acquire();
        }
        return Service.START_NOT_STICKY;
    }
}
