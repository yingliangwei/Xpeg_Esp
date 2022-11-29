package com.orca.xarg;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FloatingActivity extends Service {
    private SharedPreferences editor;
    public static boolean floataim = false;
    public static String socket;
    private LinearLayout aimBtn;
    private Switch aimfloat;
    private LinearLayout carBtn;
    public String daemonPath;
    private LinearLayout espBtn;
    private LinearLayout itemBtn;
    private int mFPS = 0;
    private int mFPSCounter = 0;
    private long mFPSTime = 0L;
    private LinearLayout mainFloatView;
    private View mainView;
    private LinearLayout memenu;
    private LinearLayout memoryBtn;
    private LinearLayout menu1;
    private LinearLayout menu2;
    private LinearLayout menu3;
    private LinearLayout menu4;
    private LinearLayout menuaim;
    private LinearLayout miniFloatView;
    private WindowManager.LayoutParams paramsView;
    private LinearLayout setBtn;
    private TextView textfps;
    private WindowManager windowManager;
    private RadioButton radioButton1, radioButton3, radioButton4, radioButton5;
    private Switch switch_1, switch_3;
    private CheckBox checkBox1, checkBox2;
    private SeekBar seekBar2;
    private RadioGroup radioGroup;

    public void DrawESP() {
        if (MainActivity.isRootGiven()) {
            socket = "su -c " + this.daemonPath;
        } else {
            socket = this.daemonPath;
        }
        try {
            startService(new Intent(this, Class.forName("com.orca.xarg.Overlay")));
        } catch (ClassNotFoundException e) {
            throw new NoClassDefFoundError(e.getMessage());
        }
    }

    private void ExecuteElf(String paramString) {
        try {
            Runtime.getRuntime().exec(paramString, null, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void InitShowMainView() {
        this.miniFloatView = this.mainView.findViewById(R.id.miniFloatMenu);
        this.mainFloatView = this.mainView.findViewById(R.id.mainFloatMenu);
        ImageView closeButton = this.mainView.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(param1View -> {
            mainFloatView.setVisibility(View.GONE);
            miniFloatView.setVisibility(View.VISIBLE);
        });
        mainView.findViewById(R.id.layoutControlView).setOnTouchListener(new View.OnTouchListener() {

            private float initialTouchX;
            private float initialTouchY;
            private int initialX;
            private int initialY;

            @Override
            public boolean onTouch(View param1View, MotionEvent param1MotionEvent) {
                int i;
                int j;
                boolean bool1;
                boolean bool2 = true;
                switch (param1MotionEvent.getAction()) {
                    default:
                        return false;
                    case 0:
                        this.initialX = paramsView.x;
                        this.initialY = paramsView.y;
                        this.initialTouchX = param1MotionEvent.getRawX();
                        this.initialTouchY = param1MotionEvent.getRawY();
                        return true;
                    case 1:
                        i = (int) (param1MotionEvent.getRawX() - this.initialTouchX);
                        j = (int) (param1MotionEvent.getRawY() - this.initialTouchY);
                        bool1 = bool2;
                        if (i < 10) {
                            bool1 = bool2;
                            if (j < 10) {
                                bool1 = bool2;
                                if (isViewCollapsed()) {
                                    FloatingActivity.this.miniFloatView.setVisibility(View.GONE);
                                    FloatingActivity.this.mainFloatView.setVisibility(View.VISIBLE);
                                    return true;
                                }
                            }
                        }
                        return bool1;
                    case 2:
                        break;
                }
                paramsView.x = this.initialX + (int) (param1MotionEvent.getRawX() - this.initialTouchX);
                paramsView.y = this.initialY + (int) (param1MotionEvent.getRawY() - this.initialTouchY);
                windowManager.updateViewLayout(mainView, paramsView);
                return true;
            }
        });
    }

    private void ShowMainView() {
        this.mainView = LayoutInflater.from(this).inflate(R.layout.layout_floating, new FrameLayout(this), false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, getLayoutType(), getFlagsType(), -3);
        layoutParams.gravity = Gravity.START | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 0;
        this.paramsView = layoutParams;
        this.windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        this.windowManager.addView(this.mainView, this.paramsView);
        InitShowMainView();
    }

    private void StartAimFloat() {
        startService(new Intent(this, AimFloat.class));
    }

    private void StopAimFloat() {
        stopService(new Intent(this, AimFloat.class));
    }

    private void StopESP() {
        stopService(new Intent(this, Overlay.class));
    }

    private int getFlagsType() {
        return 8;
    }

    private static int getLayoutType() {
        return (Build.VERSION.SDK_INT >= 26) ? 2038 : ((Build.VERSION.SDK_INT >= 24) ? 2002 : ((Build.VERSION.SDK_INT >= 23) ? 2005 : 2003));
    }

    private String getType() {
        return getSharedPreferences("espValue", 0).getString("type", "1");
    }

    private boolean isViewCollapsed() {
        return !(this.mainFloatView != null && this.miniFloatView.getVisibility() != View.VISIBLE);
    }

    private void loadFunc() {
        ((Switch) this.mainView.findViewById(R.id.drawesp)).setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                DrawESP();
                return;
            }
            StopESP();
            StopAimFloat();
            FloatingActivity.floataim = false;
            aimfloat.setChecked(false);
        });
        SeekBar seekBar1 = mainView.findViewById(R.id.strokeline);
        TextView textView = this.mainView.findViewById(R.id.stroketext);
        seekBar1.setProgress(getStrokeLine());
        textView.setText(String.valueOf(getStrokeLine()));
        ESPView.ChangeStrokeLine(getStrokeLine());
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private final SeekBar seekBar = mainView.findViewById(R.id.strokeline);
            private final TextView text = mainView.findViewById(R.id.stroketext);

            @Override
            public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
                param1Int = this.seekBar.getProgress();
                setStrokeLine(param1Int);
                ESPView.ChangeStrokeLine(param1Int);
                text.setText(String.valueOf(param1Int));
            }

            @Override
            public void onStartTrackingTouch(SeekBar param1SeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar param1SeekBar) {
            }
        });
        seekBar1 = this.mainView.findViewById(R.id.strokeskelton);
        textView = this.mainView.findViewById(R.id.strokeskeltext);
        seekBar1.setProgress(getStrokeSkelton());
        textView.setText(String.valueOf(getStrokeSkelton()));
        ESPView.ChangeStrokeSkelton(getStrokeSkelton());
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private final SeekBar seekBar = mainView.findViewById(R.id.strokeskelton);
            private final TextView textView = mainView.findViewById(R.id.strokeskeltext);

            @Override
            public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
                param1Int = seekBar.getProgress();
                setStrokeSkelton(param1Int);
                ESPView.ChangeStrokeSkelton(param1Int);
                textView.setText(String.valueOf(param1Int));
            }

            @Override
            public void onStartTrackingTouch(SeekBar param1SeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar param1SeekBar) {
            }
        });
        RadioButton radioButton2 = this.mainView.findViewById(R.id.isBotWhite);
        radioButton3 = this.mainView.findViewById(R.id.isBotLBlue);
        int i = getBot();
        if (i == 1) {
            radioButton2.setChecked(true);
            SettingValue(33, true);
            SettingValue(34, false);
        } else if (i == 2) {
            radioButton3.setChecked(true);
            SettingValue(33, false);
            SettingValue(34, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(33, true);
            SettingValue(34, false);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setBot(1);
                SettingValue(33, true);
                SettingValue(34, false);
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setBot(2);
                SettingValue(33, false);
                SettingValue(34, true);
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isHealth1);
        radioButton3 = this.mainView.findViewById(R.id.isHealth2);
        i = getHealth();
        if (i == 1) {
            radioButton2.setChecked(true);
            SettingValue(19, true);
            SettingValue(20, false);
        } else if (i == 2) {
            radioButton3.setChecked(true);
            SettingValue(19, false);
            SettingValue(20, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(19, true);
            SettingValue(20, false);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setHealth(1);
                SettingValue(19, true);
                SettingValue(20, false);
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setHealth(2);
                SettingValue(19, false);
                SettingValue(20, true);
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isSkeletonRed);
        radioButton3 = this.mainView.findViewById(R.id.isSkeletonYellow);
        radioButton4 = this.mainView.findViewById(R.id.isSkeletonGreen);
        radioButton5 = this.mainView.findViewById(R.id.isSkeletonWhite);
        i = getSkeletonColor();
        if (i == 2) {
            radioButton2.setChecked(true);
            SettingValue(29, false);
            SettingValue(30, true);
            SettingValue(31, false);
            SettingValue(32, false);
        } else if (i == 3) {
            radioButton3.setChecked(true);
            SettingValue(29, false);
            SettingValue(30, false);
            SettingValue(31, true);
            SettingValue(32, false);
        } else if (i == 4) {
            radioButton4.setChecked(true);
            SettingValue(29, false);
            SettingValue(30, false);
            SettingValue(31, false);
            SettingValue(32, true);
        } else if (i == 1) {
            radioButton5.setChecked(true);
            SettingValue(29, true);
            SettingValue(30, false);
            SettingValue(31, false);
            SettingValue(32, false);
        } else {
            radioButton5.setChecked(true);
            SettingValue(29, true);
            SettingValue(30, false);
            SettingValue(31, false);
            SettingValue(32, false);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setSkeletonColor(2);
                SettingValue(29, false);
                SettingValue(30, true);
                SettingValue(31, false);
                SettingValue(32, false);
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setSkeletonColor(3);
                SettingValue(29, false);
                SettingValue(30, false);
                SettingValue(31, true);
                SettingValue(32, false);
            }
        });
        radioButton4.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setSkeletonColor(4);
                SettingValue(29, false);
                SettingValue(30, false);
                SettingValue(31, false);
                SettingValue(32, true);
            }
        });
        radioButton5.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setSkeletonColor(1);
                SettingValue(29, true);
                SettingValue(30, false);
                SettingValue(31, false);
                SettingValue(32, false);
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isBoxRed);
        radioButton3 = this.mainView.findViewById(R.id.isBoxYellow);
        radioButton4 = this.mainView.findViewById(R.id.isBoxGreen);
        radioButton5 = this.mainView.findViewById(R.id.isBoxBlue);
        i = getBoxColor();
        if (i == 1) {
            radioButton2.setChecked(true);
            SettingValue(25, true);
            SettingValue(26, false);
            SettingValue(27, false);
            SettingValue(28, false);
        } else if (i == 2) {
            radioButton3.setChecked(true);
            SettingValue(25, false);
            SettingValue(26, true);
            SettingValue(27, false);
            SettingValue(28, false);
        } else if (i == 3) {
            radioButton4.setChecked(true);
            SettingValue(25, false);
            SettingValue(26, false);
            SettingValue(27, true);
            SettingValue(28, false);
        } else if (i == 4) {
            radioButton5.setChecked(true);
            SettingValue(25, false);
            SettingValue(26, false);
            SettingValue(27, false);
            SettingValue(28, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(25, true);
            SettingValue(26, false);
            SettingValue(27, false);
            SettingValue(28, false);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setBoxColor(1);
                SettingValue(25, true);
                SettingValue(26, false);
                SettingValue(27, false);
                SettingValue(28, false);
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBoxColor(2);
                    SettingValue(25, false);
                    SettingValue(26, true);
                    SettingValue(27, false);
                    SettingValue(28, false);
                }
            }
        });
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBoxColor(3);
                    SettingValue(25, false);
                    SettingValue(26, false);
                    SettingValue(27, true);
                    SettingValue(28, false);
                }
            }
        });
        radioButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBoxColor(4);
                    SettingValue(25, false);
                    SettingValue(26, false);
                    SettingValue(27, false);
                    SettingValue(28, true);
                }
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isLineRed);
        radioButton3 = this.mainView.findViewById(R.id.isLineYellow);
        radioButton4 = this.mainView.findViewById(R.id.isLineGreen);
        radioButton5 = this.mainView.findViewById(R.id.isLineBlue);
        i = getLineColor();
        if (i == 1) {
            radioButton2.setChecked(true);
            SettingValue(21, true);
            SettingValue(22, false);
            SettingValue(23, false);
            SettingValue(24, false);
        } else if (i == 2) {
            radioButton3.setChecked(true);
            SettingValue(21, false);
            SettingValue(22, true);
            SettingValue(23, false);
            SettingValue(24, false);
        } else if (i == 3) {
            radioButton4.setChecked(true);
            SettingValue(21, false);
            SettingValue(22, false);
            SettingValue(23, true);
            SettingValue(24, false);
        } else if (i == 4) {
            radioButton5.setChecked(true);
            SettingValue(21, false);
            SettingValue(22, false);
            SettingValue(23, false);
            SettingValue(24, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(21, true);
            SettingValue(22, false);
            SettingValue(23, false);
            SettingValue(24, false);
        }
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setLineColor(1);
                    SettingValue(21, true);
                    SettingValue(22, false);
                    SettingValue(23, false);
                    SettingValue(24, false);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setLineColor(2);
                    SettingValue(21, false);
                    SettingValue(22, true);
                    SettingValue(23, false);
                    SettingValue(24, false);
                }
            }
        });
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setLineColor(3);
                    SettingValue(21, false);
                    SettingValue(22, false);
                    SettingValue(23, true);
                    SettingValue(24, false);
                }
            }
        });
        radioButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setLineColor(4);
                    SettingValue(21, false);
                    SettingValue(22, false);
                    SettingValue(23, false);
                    SettingValue(24, true);
                }
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isDisableSkelton);
        radioButton3 = this.mainView.findViewById(R.id.isSkeltonPlayer);
        radioButton4 = this.mainView.findViewById(R.id.isSkeltonPlayerBot);
        i = getSkelton();
        if (i == 1) {
            radioButton3.setChecked(true);
            SettingValue(39, true);
            SettingValue(40, false);
        } else if (i == 2) {
            radioButton4.setChecked(true);
            SettingValue(39, false);
            SettingValue(40, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(39, false);
            SettingValue(40, false);
        }
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setSkelton(3);
                    SettingValue(39, false);
                    SettingValue(40, false);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setSkelton(1);
                    SettingValue(39, true);
                    SettingValue(40, false);
                }
            }
        });
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setSkelton(2);
                    SettingValue(39, false);
                    SettingValue(40, true);
                }
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isDisableBox);
        radioButton3 = this.mainView.findViewById(R.id.isPlayerBoxStroke);
        radioButton4 = this.mainView.findViewById(R.id.isPlayerBoxFilled);
        i = getBox();
        if (i == 1) {
            radioButton3.setChecked(true);
            SettingValue(37, true);
            SettingValue(38, false);
        } else if (i == 2) {
            radioButton4.setChecked(true);
            SettingValue(37, false);
            SettingValue(38, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(37, false);
            SettingValue(38, false);
        }
        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBox(3);
                    SettingValue(37, false);
                    SettingValue(38, false);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBox(1);
                    SettingValue(37, true);
                    SettingValue(38, false);
                }
            }
        });
        radioButton4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    setBox(2);
                    SettingValue(37, false);
                    SettingValue(38, true);
                }
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isDisableLine);
        radioButton3 = this.mainView.findViewById(R.id.isTopLine);
        radioButton4 = this.mainView.findViewById(R.id.isMiddleLine);
        radioButton5 = this.mainView.findViewById(R.id.isBottomLine);
        RadioButton radioButton6 = this.mainView.findViewById(R.id.isTopBottomLine);
        i = getLine();
        if (i == 1) {
            radioButton3.setChecked(true);
            SettingValue(2, true);
            SettingValue(15, false);
            SettingValue(16, false);
            SettingValue(17, false);
        } else if (i == 2) {
            radioButton4.setChecked(true);
            SettingValue(2, false);
            SettingValue(15, true);
            SettingValue(16, false);
            SettingValue(17, false);
        } else if (i == 3) {
            radioButton5.setChecked(true);
            SettingValue(2, false);
            SettingValue(15, false);
            SettingValue(16, true);
            SettingValue(17, false);
        } else if (i == 4) {
            radioButton6.setChecked(true);
            SettingValue(2, false);
            SettingValue(15, false);
            SettingValue(16, false);
            SettingValue(17, true);
        } else {
            radioButton2.setChecked(true);
            SettingValue(2, false);
            SettingValue(15, false);
            SettingValue(16, false);
            SettingValue(17, false);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setLine(5);
                SettingValue(2, false);
                SettingValue(15, false);
                SettingValue(16, false);
                SettingValue(17, false);
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setLine(1);
                SettingValue(2, true);
                SettingValue(15, false);
                SettingValue(16, false);
                SettingValue(17, false);
            }
        });
        radioButton4.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setLine(2);
                SettingValue(2, false);
                SettingValue(15, true);
                SettingValue(16, false);
                SettingValue(17, false);
            }
        });
        radioButton5.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setLine(3);
                SettingValue(2, false);
                SettingValue(15, false);
                SettingValue(16, true);
                SettingValue(17, false);
            }
        });
        radioButton6.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setLine(4);
                SettingValue(2, false);
                SettingValue(15, false);
                SettingValue(16, false);
                SettingValue(17, true);
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.isCount1);
        radioButton3 = this.mainView.findViewById(R.id.isCount2);
        radioButton4 = this.mainView.findViewById(R.id.isCount3);
        radioButton5 = this.mainView.findViewById(R.id.isCountNone);
        i = getCount();
        if (i == 1) {
            SettingValue(12, false);
            SettingValue(13, true);
            SettingValue(14, false);
            radioButton2.setChecked(true);
        } else if (i == 2) {
            SettingValue(12, true);
            SettingValue(13, false);
            SettingValue(14, false);
            radioButton3.setChecked(true);
        } else if (i == 3) {
            SettingValue(12, false);
            SettingValue(13, false);
            SettingValue(14, true);
            radioButton4.setChecked(true);
        } else if (i == 4) {
            SettingValue(12, false);
            SettingValue(13, false);
            SettingValue(14, false);
            radioButton5.setChecked(true);
        } else {
            SettingValue(12, false);
            SettingValue(13, true);
            SettingValue(14, false);
            radioButton2.setChecked(true);
        }
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setCount(1);
                SettingValue(12, false);
                SettingValue(13, true);
                SettingValue(14, false);
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setCount(2);
                SettingValue(12, true);
                SettingValue(13, false);
                SettingValue(14, false);
            }
        });
        radioButton4.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setCount(3);
                SettingValue(12, false);
                SettingValue(13, false);
                SettingValue(14, true);
            }
        });
        radioButton5.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                setCount(4);
                SettingValue(12, false);
                SettingValue(13, false);
                SettingValue(14, false);
            }
        });
        radioButton2 = this.mainView.findViewById(R.id.dual);
        radioButton3 = this.mainView.findViewById(R.id.material);
        radioButton4 = this.mainView.findViewById(R.id.meterialdistance);
        radioButton5 = this.mainView.findViewById(R.id.textonly);
        if (getConfig((String) radioButton3.getText())) {
            radioButton3.setChecked(true);
            radioButton5.setChecked(false);
            radioButton4.setChecked(false);
            radioButton2.setChecked(false);
        } else if (getConfig((String) radioButton5.getText())) {
            radioButton3.setChecked(false);
            radioButton5.setChecked(true);
            radioButton4.setChecked(false);
            radioButton2.setChecked(false);
        } else if (getConfig((String) radioButton4.getText())) {
            radioButton3.setChecked(false);
            radioButton5.setChecked(false);
            radioButton4.setChecked(true);
            radioButton2.setChecked(false);
        } else if (getConfig((String) radioButton2.getText())) {
            radioButton3.setChecked(false);
            radioButton5.setChecked(false);
            radioButton4.setChecked(false);
            radioButton2.setChecked(true);
        } else {
            radioButton3.setChecked(false);
            radioButton5.setChecked(true);
            radioButton4.setChecked(false);
            radioButton2.setChecked(false);
        }
        radioButton5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });

        radioButton3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        radioButton4.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> setValue(String.valueOf(param1CompoundButton.getText()), param1CompoundButton.isChecked()));
        radioButton2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> setValue(String.valueOf(param1CompoundButton.getText()), param1CompoundButton.isChecked()));
        Switch switch_2 = (Switch) this.mainView.findViewById(R.id.isVisibility);
        switch_2.setChecked(getConfig((String) switch_2.getText()));
        SettingValue(18, getConfig((String) switch_2.getText()));
        switch_2.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            setValue(String.valueOf(param1CompoundButton.getText()), param1CompoundButton.isChecked());
            SettingValue(18, param1CompoundButton.isChecked());
        });
        radioButton1 = this.mainView.findViewById(R.id.fpsdefault);
        radioButton3 = this.mainView.findViewById(R.id.fps60);
        radioButton4 = this.mainView.findViewById(R.id.fps90);
        radioButton5 = this.mainView.findViewById(R.id.fps120);
        i = getFps();
        if (i == 30) {
            radioButton1.setChecked(true);
            ESPView.sleepTime = 33L;
        } else if (i == 60) {
            radioButton3.setChecked(true);
            ESPView.sleepTime = 16L;
        } else if (i == 90) {
            radioButton4.setChecked(true);
            ESPView.sleepTime = 11L;
        } else if (i == 120) {
            radioButton5.setChecked(true);
            ESPView.sleepTime = 8L;
        } else {
            radioButton1.setChecked(true);
            ESPView.sleepTime = 33L;
        }
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    radioButton3.setChecked(false);
                    radioButton4.setChecked(false);
                    radioButton5.setChecked(false);
                    setFps(30);
                    ESPView.ChangeFps(30);
                }
            }
        });
        radioButton3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                radioButton1.setChecked(false);
                radioButton4.setChecked(false);
                radioButton5.setChecked(false);
                setFps(60);
                ESPView.ChangeFps(60);
            }
        });
        radioButton4.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                radioButton3.setChecked(false);
                radioButton1.setChecked(false);
                radioButton5.setChecked(false);
                setFps(90);
                ESPView.ChangeFps(90);
            }
        });
        radioButton5.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                radioButton3.setChecked(false);
                radioButton4.setChecked(false);
                radioButton1.setChecked(false);
                setFps(120);
                ESPView.ChangeFps(120);
            }
        });
        switch_1 = (Switch) this.mainView.findViewById(R.id.isFPS);
        switch_1.setChecked(getConfig((String) switch_1.getText()));
        SettingValue(11, getConfig((String) switch_1.getText()));
        switch_1.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            setValue(String.valueOf(param1CompoundButton.getText()), param1CompoundButton.isChecked());
            SettingValue(11, param1CompoundButton.isChecked());
        });
        checkBox1 = this.mainView.findViewById(R.id.isEnemyWeapon);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(9, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(9, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isGrenadeWarning);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(10, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(10, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isHead);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(6, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(6, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isBack);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(7, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(7, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isHealth);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(4, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(4, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isTimID);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(36, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(36, compoundButton.isChecked());
            }
        });
        checkBox1 = this.mainView.findViewById(R.id.isName);
        checkBox1.setChecked(getConfig((String) checkBox1.getText()));
        SettingValue(5, getConfig((String) checkBox1.getText()));
        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(5, compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.isDist);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        SettingValue(3, getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox2;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
                SettingValue(3, compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Desert);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.m416);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QBZ);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SCARL);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.AKM);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.M16A4);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.AUG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.M249);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Groza);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MK47);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.M762);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.G36C);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.DP28);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MG3);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.FAMAS);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ASM);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.UMP);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.bizon);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MP5K);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.TommyGun);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.vector);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.P90);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.UZI);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.AWM);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QBU);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Kar98k);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.M24);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SLR);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SKS);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MK14);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Mini14);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Mosin);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.VSS);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Win94);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MK12);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.x2);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.x3);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.x4);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.x6);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.x8);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.canted);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.hollow);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.reddot);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.bag1);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.bag2);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.bag3);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.helmet1);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.helmet2);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.helmet3);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.vest1);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.vest2);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.vest3);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a9);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a7);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a5);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a300);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a45);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.arrow);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.a12);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.DBS);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.S686);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.sawed);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.M1014);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.S1897);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.S12K);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.grenade);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.molotov);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.stun);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.smoke);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.painkiller);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.medkit);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.firstaid);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.bandage);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.injection);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.energydrink);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Pan);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Crowbar);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Sickle);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Machete);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Crossbow);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.P92);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.R45);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.P18C);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.P1911);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.R1895);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Scorpion);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.CheekPad);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.CompensatorSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.FlashHiderSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.FlashHiderAr);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ArCompensator);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.TacticalStock);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Duckbill);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.FlashHiderSniper);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SuppressorSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.HalfGrip);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.StockMicroUZI);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SuppressorSniper);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.SuppressorAr);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExQdSniper);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QdSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QdSniper);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExSniper);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExAr);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExQdAr);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QdAr);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ExQdSMG);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.QuiverCrossBow);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.BulletLoop);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ThumbGrip);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.LaserSight);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.AngledGrip);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.LightGrip);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.VerticalGrip);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.GasCan);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.ATV);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.UTV);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Buggy);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.UAZ);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Trike);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Bike);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Dacia);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Jet);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Boat);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Scooter);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Bus);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Mirado);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Rony);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Snowbike);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Snowmobile);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Tempo);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Truck);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.MonsterTruck);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.BRDM);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.LadaNiva);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Motorglider);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.CoupeRB);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Crate);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.Airdrop);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.DropPlane);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        checkBox2 = this.mainView.findViewById(R.id.FlareGun);
        checkBox2.setChecked(getConfig((String) checkBox2.getText()));
        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private final CheckBox checkBox = checkBox1;

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setValue(String.valueOf(compoundButton.getText()), compoundButton.isChecked());
            }
        });
        ((Switch) this.mainView.findViewById(R.id.reducerec)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    SettingValue(35, true);
                    return;
                }
                SettingValue(35, false);
            }
        });
        ((Switch) this.mainView.findViewById(R.id.zerorec)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    SettingValue(51, true);
                    return;
                }
                SettingValue(51, false);
            }
        });
        ((Switch) this.mainView.findViewById(R.id.scross)).setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                SettingValue(44, true);
                return;
            }
            SettingValue(44, false);
        });
        ((Switch) this.mainView.findViewById(R.id.shake)).setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                SettingValue(47, true);
                return;
            }
            SettingValue(47, false);
        });
        ((Switch) this.mainView.findViewById(R.id.fastshoot)).setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            if (param1Boolean) {
                SettingValue(42, true);
                return;
            }
            SettingValue(42, false);
        });
        ((Switch) this.mainView.findViewById(R.id.scross)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    SettingValue(43, true);
                    return;
                }
                SettingValue(43, false);
            }
        });
        ((Switch) this.mainView.findViewById(R.id.bulletinstant)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    SettingValue(45, true);
                    return;
                }
                SettingValue(45, false);
            }
        });
        ((Switch) this.mainView.findViewById(R.id.ipadview)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    SettingValue(46, true);
                    return;
                }
                SettingValue(46, false);
            }
        });
        this.aimfloat = (Switch) this.mainView.findViewById(R.id.aimfloat);
        this.aimfloat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton param1CompoundButton, boolean param1Boolean) {
                if (param1Boolean) {
                    StartAimFloat();
                    FloatingActivity.floataim = true;
                    return;
                }
                StopAimFloat();
                SettingValue(50, true);
                FloatingActivity.floataim = false;
            }
        });
        seekBar2 = this.mainView.findViewById(R.id.range);
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar param1SeekBar, int param1Int, boolean param1Boolean) {
                Range(seekBar2.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar param1SeekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar param1SeekBar) {
            }
        });
        radioGroup = (RadioGroup) this.mainView.findViewById(R.id.aimby);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
                param1Int = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = mainView.findViewById(param1Int);
                AimBy(Integer.parseInt(radioButton.getTag().toString()));
            }
        });
        radioGroup = this.mainView.findViewById(R.id.aimwhen);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup param1RadioGroup, int param1Int) {
                param1Int = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = mainView.findViewById(param1Int);
                AimWhen(Integer.parseInt(radioButton.getTag().toString()));
            }
        });
        radioGroup = this.mainView.findViewById(R.id.aimbotmode);
        radioGroup.setOnCheckedChangeListener((param1RadioGroup, param1Int) -> {
            param1Int = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = mainView.findViewById(param1Int);
            Target(Integer.parseInt(radioButton.getTag().toString()));
        });

        switch_3 = this.mainView.findViewById(R.id.aimknocked);
        switch_3.setChecked(getConfig((String) switch_3.getText()));
        SettingValue(49, getConfig((String) switch_3.getText()));
        switch_3.setOnCheckedChangeListener((param1CompoundButton, param1Boolean) -> {
            setValue(String.valueOf(switch_3.getText()), switch_3.isChecked());
            SettingValue(49, switch_3.isChecked());
        });
    }


    private void setValue(String paramString, boolean paramBoolean) {
        SharedPreferences.Editor ed = editor.edit();
        ed.putBoolean(paramString, paramBoolean);
        ed.apply();
    }

    private static native String txtESP();

    private static native String txtINJ();

    public native void AimBy(int paramInt);

    public native void AimWhen(int paramInt);

    public void Execute(String paramString) {
        ExecuteElf("chmod 777 " + getFilesDir() + paramString);
        ExecuteElf(getFilesDir() + paramString);
        ExecuteElf("su -c chmod 777 " + getFilesDir() + paramString);
        ExecuteElf("su -c " + getFilesDir() + paramString);
    }

    public native void Range(int paramInt);

    public native void SettingValue(int paramInt, boolean paramBoolean);

    public native void Target(int paramInt);

    int getBot() {
        return getSharedPreferences("espValue", 0).getInt("Bot", 10);
    }

    int getBox() {
        return getSharedPreferences("espValue", 0).getInt("box", 10);
    }

    int getBoxColor() {
        return getSharedPreferences("espValue", 0).getInt("Boxcolor", 10);
    }

    boolean getConfig(String paramString) {
        return getSharedPreferences("espValue", 0).getBoolean(paramString, false);
    }

    int getCount() {
        return getSharedPreferences("espValue", 0).getInt("count", 10);
    }

    int getFps() {
        return getSharedPreferences("espValue", 0).getInt("fps", 100);
    }

    int getHealth() {
        return getSharedPreferences("espValue", 0).getInt("health", 10);
    }

    int getLine() {
        return getSharedPreferences("espValue", 0).getInt("line", 10);
    }

    int getLineColor() {
        return getSharedPreferences("espValue", 0).getInt("linecolor", 10);
    }

    int getSkeletonColor() {
        return getSharedPreferences("espValue", 0).getInt("Skeletoncolor", 10);
    }

    int getSkelton() {
        return getSharedPreferences("espValue", 0).getInt("skelton", 10);
    }

    int getStrokeLine() {
        return getSharedPreferences("espValue", 0).getInt("StrokeLine", 0);
    }

    int getStrokeSkelton() {
        return getSharedPreferences("espValue", 0).getInt("StrokeSkelton", 0);
    }

    public void loadAssets() throws IOException {
        FileOutputStream fileOutputStream = null;
        IOException e;
        FileNotFoundException e2;
        FileOutputStream fileOutputStream2;
        byte[] bArr;
        InputStream open;
        int read;
        FileOutputStream fileOutputStream3 = null;
        try {
            try {
                fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/Android/data/.tyb");
                try {
                    byte[] bytes = "DO NOT DELETE".getBytes();
                    fileOutputStream.write(bytes, 0, bytes.length);
                    fileOutputStream.close();
                } catch (FileNotFoundException e3) {
                    e2 = e3;
                    e2.printStackTrace();
                    fileOutputStream2 = new FileOutputStream(getFilesDir().toString() + "/XARG");
                    bArr = new byte[1024];
                    open = getAssets().open("XARG");
                    while (true) {
                        read = open.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                    }
                    open.close();
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                    this.daemonPath = getFilesDir().toString() + "/XARG";
                    Runtime.getRuntime().exec("chmod 777 " + this.daemonPath);
                } catch (IOException e4) {
                    e = e4;
                    e.printStackTrace();
                    fileOutputStream2 = new FileOutputStream(getFilesDir().toString() + "/XARG");
                    bArr = new byte[1024];
                    open = getAssets().open("XARG");
                    while (true) {
                        read = open.read(bArr);
                        if (read <= 0) {
                            break;
                        }
                        fileOutputStream2.write(bArr, 0, read);
                    }
                    open.close();
                    fileOutputStream2.flush();
                    fileOutputStream2.close();
                    this.daemonPath = getFilesDir().toString() + "/XARG";
                    Runtime.getRuntime().exec("chmod 777 " + this.daemonPath);
                }
            } catch (Throwable th) {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
                throw th;
            }
        } catch (Throwable e6) {
            fileOutputStream = fileOutputStream3;
        }
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close();
            } catch (IOException e8) {
                e8.printStackTrace();
            }
            fileOutputStream2 = new FileOutputStream(getFilesDir().toString() + "/XARG");
            bArr = new byte[1024];
            open = getAssets().open("XARG");
            while (true) {
                read = open.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream2.write(bArr, 0, read);
            }
            open.close();
            fileOutputStream2.flush();
            fileOutputStream2.close();
        } else {
            fileOutputStream2 = new FileOutputStream(getFilesDir().toString() + "/XARG");
            bArr = new byte[1024];
            open = getAssets().open("XARG");
            while (true) {
                read = open.read(bArr);
                if (read <= 0) {
                    break;
                }
                fileOutputStream2.write(bArr, 0, read);
            }
            open.close();
            fileOutputStream2.flush();
            fileOutputStream2.close();
        }
        this.daemonPath = getFilesDir().toString() + "/XARG";
        Runtime.getRuntime().exec("chmod 777 " + this.daemonPath);
    }

    @Override
    public IBinder onBind(Intent paramIntent) {
        return (IBinder) null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        editor = getSharedPreferences("espValue", 0);
        try {
            loadAssets();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ShowMainView();
        loadFunc();
        TextView textView = this.mainView.findViewById(R.id.dev);
        textView.setText(txtINJ());
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"));
        this.memoryBtn = this.mainView.findViewById(R.id.memory);
        this.espBtn = this.mainView.findViewById(R.id.espBtn);
        this.itemBtn = this.mainView.findViewById(R.id.itemBtn);
        this.carBtn = this.mainView.findViewById(R.id.carBtn);
        this.setBtn = this.mainView.findViewById(R.id.setBtn);
        this.menu1 = this.mainView.findViewById(R.id.menu1);
        this.menu2 = this.mainView.findViewById(R.id.menu2);
        this.menu3 = this.mainView.findViewById(R.id.menu3);
        this.menu4 = this.mainView.findViewById(R.id.menu4);
        this.memenu = this.mainView.findViewById(R.id.memenu);
        this.aimBtn = this.mainView.findViewById(R.id.aimBtn);
        this.menuaim = this.mainView.findViewById(R.id.menuaim);
        this.memoryBtn.setBackgroundResource(R.drawable.menu_round);
        this.memoryBtn.setOnClickListener(param1View -> {
            memoryBtn.setBackgroundResource(R.drawable.menu_round);
            espBtn.setBackgroundColor(0);
            itemBtn.setBackgroundColor(0);
            setBtn.setBackgroundColor(0);
            carBtn.setBackgroundColor(0);
            aimBtn.setBackgroundColor(0);
            memenu.setVisibility(View.VISIBLE);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menuaim.setVisibility(View.GONE);
            textView.setText(FloatingActivity.txtINJ());
        });
        this.itemBtn.setOnClickListener(param1View -> {
            itemBtn.setBackgroundResource(R.drawable.menu_round);
            espBtn.setBackgroundColor(0);
            memoryBtn.setBackgroundColor(0);
            carBtn.setBackgroundColor(0);
            aimBtn.setBackgroundColor(0);
            setBtn.setBackgroundColor(0);
            memenu.setVisibility(View.GONE);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.VISIBLE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menuaim.setVisibility(View.GONE);
            textView.setText(FloatingActivity.txtESP());
        });
        this.espBtn.setOnClickListener(param1View -> {
            espBtn.setBackgroundResource(R.drawable.menu_round);
            itemBtn.setBackgroundColor(0);
            memoryBtn.setBackgroundColor(0);
            carBtn.setBackgroundColor(0);
            aimBtn.setBackgroundColor(0);
            setBtn.setBackgroundColor(0);
            memenu.setVisibility(View.GONE);
            menu1.setVisibility(View.VISIBLE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menuaim.setVisibility(View.GONE);
            textView.setText(FloatingActivity.txtESP());
        });
        this.carBtn.setOnClickListener(param1View -> {
            carBtn.setBackgroundResource(R.drawable.menu_round);
            espBtn.setBackgroundColor(0);
            itemBtn.setBackgroundColor(0);
            memoryBtn.setBackgroundColor(0);
            aimBtn.setBackgroundColor(0);
            setBtn.setBackgroundColor(0);
            memenu.setVisibility(View.GONE);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.VISIBLE);
            menu4.setVisibility(View.GONE);
            menuaim.setVisibility(View.GONE);
            textView.setText(FloatingActivity.txtESP());
        });
        this.setBtn.setOnClickListener(param1View -> {
            setBtn.setBackgroundResource(R.drawable.menu_round);
            espBtn.setBackgroundColor(0);
            itemBtn.setBackgroundColor(0);
            memoryBtn.setBackgroundColor(0);
            carBtn.setBackgroundColor(0);
            aimBtn.setBackgroundColor(0);
            memenu.setVisibility(View.GONE);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.VISIBLE);
            menuaim.setVisibility(View.GONE);
            textView.setText(FloatingActivity.txtESP());
        });
        this.aimBtn.setOnClickListener(param1View -> {
            setBtn.setBackgroundColor(0);
            espBtn.setBackgroundColor(0);
            aimBtn.setBackgroundResource(R.drawable.menu_round);
            itemBtn.setBackgroundColor(0);
            memoryBtn.setBackgroundColor(0);
            carBtn.setBackgroundColor(0);
            memenu.setVisibility(View.GONE);
            menu1.setVisibility(View.GONE);
            menu2.setVisibility(View.GONE);
            menu3.setVisibility(View.GONE);
            menu4.setVisibility(View.GONE);
            menuaim.setVisibility(View.VISIBLE);
            textView.setText(FloatingActivity.txtESP());
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.mainView != null) this.windowManager.removeView(this.mainView);
    }

    void setBot(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("Bot", paramInt);
        editor.apply();
    }

    void setBox(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("box", paramInt);
        editor.apply();
    }

    void setBoxColor(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("Boxcolor", paramInt);
        editor.apply();
    }

    void setCount(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("count", paramInt);
        editor.apply();
    }

    void setFps(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("fps", paramInt);
        editor.apply();
    }

    void setHealth(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("health", paramInt);
        editor.apply();
    }

    void setLine(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("line", paramInt);
        editor.apply();
    }

    void setLineColor(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("linecolor", paramInt);
        editor.apply();
    }

    void setSkeletonColor(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("Skeletoncolor", paramInt);
        editor.apply();
    }

    void setSkelton(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("skelton", paramInt);
        editor.apply();
    }

    void setStrokeLine(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("StrokeLine", paramInt);
        editor.apply();
    }

    void setStrokeSkelton(int paramInt) {
        SharedPreferences.Editor editor = getSharedPreferences("espValue", 0).edit();
        editor.putInt("StrokeSkelton", paramInt);
        editor.apply();
    }

    static {
        System.loadLibrary("native");
    }
}
