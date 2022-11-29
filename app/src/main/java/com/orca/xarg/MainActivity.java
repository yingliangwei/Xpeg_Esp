package com.orca.xarg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends Activity {
    public static int REQUEST_OVERLAY_PERMISSION = 5469;

    static int gameType;

    static boolean vercheck;

    private ImageView logo;

    private TextView modeexc;

    private ProgressBar progressBar;

    private static native String AboutTitle();

    private static native String Dev();

    private void ExecuteElf(String paramString) {
        try {
            Runtime.getRuntime().exec(paramString, null, null);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void GoToDonate() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(donate())));
    }

    private void GoToTeleOwn() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(owntele())));
    }

    private void GoToTelegram() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Tele())));
    }

    private void GoToTiktok() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(tiktok())));
    }

    private boolean MoveAssets(String paramString1, String paramString2) {
        File file = new File(paramString1);
        if (!file.exists() && !file.mkdirs()) {
            Log.e("--Method--", "copyAssetsSingleFile: cannot create directory.");
            return false;
        }
        try {
            InputStream inputStream = getAssets().open(paramString2);
            FileOutputStream fileOutputStream = new FileOutputStream(new File(file, paramString2));
            byte[] arrayOfByte = new byte[1024];
            while (true) {
                int i = inputStream.read(arrayOfByte);
                if (-1 == i) {
                    inputStream.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
                fileOutputStream.write(arrayOfByte, 0, i);
            }
        } catch (IOException iOException) {
            iOException.printStackTrace();
            return false;
        }
    }

    private static native String Tele();

    private static native String donate();

    public static boolean isRootAvailable() {
        String[] arrayOfString = System.getenv("PATH").split(":");
        for (int i = 0; ; i++) {
            if (i >= arrayOfString.length)
                return false;
            if ((new File(arrayOfString[i], "su")).exists())
                return true;
        }
    }

    public static boolean isRootGiven() {
        if (isRootAvailable()) {
            try {
                Process process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String readLine;
                while ((readLine = bufferedReader.readLine()) != null) {
                    if (readLine.toLowerCase().contains("uid=0")) {
                        process.destroy();
                        return true;
                    }
                }
            } catch (Exception e2) {
                return false;
            }
        }
        return false;
    }


    private void loadMain() {
        MoveAssets(getFilesDir() + "/", "T");
        MoveAssets(getFilesDir() + "/", "gl");
        MoveAssets(getFilesDir() + "/", "kr");
        MoveAssets(getFilesDir() + "/", "vn");
        MoveAssets(getFilesDir() + "/", "tw");
        MoveAssets(getFilesDir() + "/", "bgmi");
    }

    private static native String owntele();

    private void permissionWindows() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder((Context) this);
            builder.setMessage("This application requires window overlays access permission, please allow first.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface param1DialogInterface, int param1Int) {
                    Intent intent = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, MainActivity.REQUEST_OVERLAY_PERMISSION);
                }
            });
            builder.setCancelable(false);
            builder.show();
        }
    }

    private void startFloating() {
        try {
            Class<?> clazz = Class.forName("com.orca.xarg.FloatingActivity");
            startService(new Intent(this, clazz));
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    private void stopFloating() {
        try {
            Class<?> clazz = Class.forName("com.orca.xarg.Overlay");
            stopService(new Intent(this, clazz));
            try {
                clazz = Class.forName("com.orca.xarg.FloatingActivity");
                stopService(new Intent(this, clazz));
                try {
                    clazz = Class.forName("com.orca.xarg.AimFloat");
                    stopService(new Intent(this, clazz));
                    FloatingActivity.floataim = false;
                } catch (ClassNotFoundException classNotFoundException) {
                    throw new NoClassDefFoundError(classNotFoundException.getMessage());
                }
            } catch (ClassNotFoundException classNotFoundException) {
                throw new NoClassDefFoundError(classNotFoundException.getMessage());
            }
        } catch (ClassNotFoundException classNotFoundException) {
            throw new NoClassDefFoundError(classNotFoundException.getMessage());
        }
    }

    private static native String tiktok();

    public void Execute(String paramString) {
        try {
            ExecuteElf("chmod 777 " + getFilesDir() + paramString);
            ExecuteElf(getFilesDir() + paramString);
            ExecuteElf("su -c chmod 777 " + getFilesDir() + paramString);
            ExecuteElf("su -c " + getFilesDir() + paramString);
        } catch (Exception exception) {
        }
    }


    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4, linearLayout5, linearLayout6;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.layout_main);
        loadMain();
        permissionWindows();
        this.modeexc = findViewById(R.id.modeexc);
        ((TextView) findViewById(R.id.dev)).setText(Dev());
        ((TextView) findViewById(R.id.abouttitle)).setText(AboutTitle());
        TextView textView2 = findViewById(R.id.txtstart);
        TextView textView4 = findViewById(R.id.txtstop);
        textView2.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"));
        textView4.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font1.ttf"));
        linearLayout1 = findViewById(R.id.hackmenu);
        linearLayout2 = findViewById(R.id.toolsmenu);
        linearLayout3 = findViewById(R.id.layhome);
        linearLayout4 = findViewById(R.id.laytools);
        linearLayout5 = findViewById(R.id.aboutmenu);
        linearLayout6 = findViewById(R.id.layabout);
        linearLayout3.setBackgroundColor(Color.argb(120, 20, 20, 20));
        linearLayout3.setOnClickListener(new View.OnClickListener() {
            private final LinearLayout val$aboutmenu = linearLayout1;

            private final LinearLayout val$hackmenu = linearLayout2;

            private final LinearLayout val$layabout = linearLayout5;

            private final LinearLayout val$layhome = linearLayout3;

            private final LinearLayout val$laytools = linearLayout4;

            private final LinearLayout val$toolsmenu = linearLayout6;

            @Override
            public void onClick(View param1View) {
                val$aboutmenu.setVisibility(View.VISIBLE);
                val$hackmenu.setVisibility(View.GONE);
                val$layabout.setVisibility(View.GONE);
                val$layhome.setBackgroundColor(Color.argb(120, 20, 20, 20));
                val$laytools.setBackgroundColor(0);
                val$toolsmenu.setBackgroundColor(0);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {
            private final LinearLayout val$aboutmenu = linearLayout1;

            private final LinearLayout val$hackmenu = linearLayout2;

            private final LinearLayout val$layabout = linearLayout5;

            private final LinearLayout val$layhome = linearLayout3;

            private final LinearLayout val$laytools = linearLayout4;

            private final LinearLayout val$toolsmenu = linearLayout6;

            @Override
            public void onClick(View param1View) {
                val$aboutmenu.setVisibility(View.GONE);
                val$hackmenu.setVisibility(View.VISIBLE);
                val$layabout.setVisibility(View.GONE);
                val$layhome.setBackgroundColor(0);
                val$laytools.setBackgroundColor(Color.argb(120, 20, 20, 20));
                val$toolsmenu.setBackgroundColor(0);
            }
        });
        linearLayout6.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$aboutmenu = linearLayout1;

            private final LinearLayout val$hackmenu = linearLayout2;

            private final LinearLayout val$layabout = linearLayout5;

            private final LinearLayout val$layhome = linearLayout3;

            private final LinearLayout val$laytools = linearLayout4;

            private final LinearLayout val$toolsmenu = linearLayout6;

            @Override
            public void onClick(View param1View) {
                val$aboutmenu.setVisibility(View.GONE);
                val$hackmenu.setVisibility(View.GONE);
                val$layabout.setVisibility(View.VISIBLE);
                val$layhome.setBackgroundColor(0);
                val$laytools.setBackgroundColor(0);
                val$toolsmenu.setBackgroundColor(Color.argb(120, 20, 20, 20));
            }
        });
        linearLayout1 = (LinearLayout) findViewById(R.id.listgl);
        linearLayout2 = (LinearLayout) findViewById(R.id.listkr);
        linearLayout3 = (LinearLayout) findViewById(R.id.listvn);
        linearLayout4 = (LinearLayout) findViewById(R.id.listtw);
        linearLayout5 = (LinearLayout) findViewById(R.id.listbgmi);
        linearLayout6 = (LinearLayout) findViewById(R.id.tgl);
        LinearLayout linearLayout7 = (LinearLayout) findViewById(R.id.tkr);
        LinearLayout linearLayout8 = (LinearLayout) findViewById(R.id.tvn);
        LinearLayout linearLayout9 = (LinearLayout) findViewById(R.id.ttw);
        LinearLayout linearLayout10 = (LinearLayout) findViewById(R.id.tbgmi);
        TextView textView5 = (TextView) findViewById(R.id.resetgl);
        TextView textView6 = (TextView) findViewById(R.id.resetkr);
        TextView textView7 = (TextView) findViewById(R.id.resetvn);
        TextView textView8 = (TextView) findViewById(R.id.resettw);
        TextView textView9 = (TextView) findViewById(R.id.resetbgmi);
        TextView textView10 = (TextView) findViewById(R.id.fcgl);
        TextView textView11 = (TextView) findViewById(R.id.fckr);
        TextView textView12 = (TextView) findViewById(R.id.fcvn);
        TextView textView13 = (TextView) findViewById(R.id.fctw);
        TextView textView14 = (TextView) findViewById(R.id.fcbgmi);
        linearLayout1.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$tbgmi = linearLayout6;

            private final LinearLayout val$tgl = linearLayout7;

            private final LinearLayout val$tkr = linearLayout8;

            private final LinearLayout val$ttw = linearLayout9;

            private final LinearLayout val$tvn = linearLayout10;

            @Override
            public void onClick(View param1View) {
                this.val$tbgmi.setVisibility(View.VISIBLE);
                this.val$tgl.setVisibility(View.GONE);
                this.val$tkr.setVisibility(View.GONE);
                this.val$ttw.setVisibility(View.GONE);
                this.val$tvn.setVisibility(View.GONE);
            }
        });
        linearLayout2.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$tbgmi = linearLayout6;

            private final LinearLayout val$tgl = linearLayout7;

            private final LinearLayout val$tkr = linearLayout8;

            private final LinearLayout val$ttw = linearLayout9;

            private final LinearLayout val$tvn = linearLayout10;

            @Override
            public void onClick(View param1View) {
                this.val$tbgmi.setVisibility(View.GONE);
                this.val$tgl.setVisibility(View.VISIBLE);
                this.val$tkr.setVisibility(View.GONE);
                this.val$ttw.setVisibility(View.GONE);
                this.val$tvn.setVisibility(View.GONE);
            }
        });
        linearLayout3.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$tbgmi = linearLayout6;

            private final LinearLayout val$tgl = linearLayout7;

            private final LinearLayout val$tkr = linearLayout8;

            private final LinearLayout val$ttw = linearLayout9;

            private final LinearLayout val$tvn = linearLayout10;

            @Override
            public void onClick(View param1View) {
                this.val$tbgmi.setVisibility(View.GONE);
                this.val$tgl.setVisibility(View.GONE);
                this.val$tkr.setVisibility(View.VISIBLE);
                this.val$ttw.setVisibility(View.GONE);
                this.val$tvn.setVisibility(View.GONE);
            }
        });
        linearLayout4.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$tbgmi = linearLayout6;

            private final LinearLayout val$tgl = linearLayout7;

            private final LinearLayout val$tkr = linearLayout8;

            private final LinearLayout val$ttw = linearLayout9;

            private final LinearLayout val$tvn = linearLayout10;

            @Override
            public void onClick(View param1View) {
                this.val$tbgmi.setVisibility(View.GONE);
                this.val$tgl.setVisibility(View.GONE);
                this.val$tkr.setVisibility(View.GONE);
                this.val$ttw.setVisibility(View.VISIBLE);
                this.val$tvn.setVisibility(View.GONE);
            }
        });
        linearLayout5.setOnClickListener(new View.OnClickListener() {

            private final LinearLayout val$tbgmi = linearLayout6;

            private final LinearLayout val$tgl = linearLayout7;

            private final LinearLayout val$tkr = linearLayout8;

            private final LinearLayout val$ttw = linearLayout9;

            private final LinearLayout val$tvn = linearLayout10;

            @Override
            public void onClick(View param1View) {
                this.val$tbgmi.setVisibility(View.GONE);
                this.val$tgl.setVisibility(View.GONE);
                this.val$tkr.setVisibility(View.GONE);
                this.val$ttw.setVisibility(View.GONE);
                this.val$tvn.setVisibility(View.VISIBLE);
            }
        });
        textView5.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.this$0);
                builder.setTitle("GLOBAL");
                builder.setMessage("Do you want to reset guest?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        ProgressDialog progressDialog = new ProgressDialog(this$0);
                        new Handler(Looper.myLooper()) {

                            @Override
                            public void handleMessage(Message param3Message) {
                                super.handleMessage(param3Message);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        progressDialog.setMessage("Reseting Guest...");
                        progressDialog.show();
                        (new Thread(new Runnable() {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if (progressDialog.getProgress() > progressDialog.getMax())
                                            return;
                                        Thread.sleep(1000L);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                }
                            }
                        })).start();
                        this$0.Execute("/gl");
                        Toast.makeText((Context) this$0, "[GL] Reset Succesfully!", 1).show();
                    }
                });
                builder.setNegativeButton("Cancel", (param2DialogInterface, param2Int) -> {
                    Toast.makeText(this$0.getApplicationContext(), "Canceled", 0).show();
                    param2DialogInterface.cancel();
                });
                builder.show();
            }
        });
        textView10.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                ProgressDialog progressDialog = new ProgressDialog((Context) this.this$0);
                new Handler(Looper.myLooper()) {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void handleMessage(Message param2Message) {
                        super.handleMessage(param2Message);
                        progressDialog.incrementProgressBy(1);
                    }
                };
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                (new Thread(new Runnable() {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if (progressDialog.getProgress() > progressDialog.getMax())
                                    return;
                                Thread.sleep(200L);
                                progressDialog.dismiss();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    }
                })).start();
                Execute("/T 1");
                Toast.makeText((Context) this.this$0, "[GL] Fix Crash Succesfully!", 1).show();
            }
        });
        textView6.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.this$0);
                builder.setTitle("KOREA");
                builder.setMessage("Do you want to reset guest?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        ProgressDialog progressDialog = new ProgressDialog((Context) this$0);
                        new Handler(Looper.myLooper()) {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void handleMessage(Message param3Message) {
                                super.handleMessage(param3Message);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        progressDialog.setMessage("Reseting Guest...");
                        progressDialog.show();
                        (new Thread(new Runnable() {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if (progressDialog.getProgress() > progressDialog.getMax())
                                            return;
                                        Thread.sleep(1000L);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    return;
                                }
                            }
                        })).start();
                        this$0.Execute("/kr");
                        Toast.makeText((Context) this$0, "[KR] Reset Succesfully!", 1).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        Toast.makeText(this$0.getApplicationContext(), "Canceled", 0).show();
                        param2DialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        textView11.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                ProgressDialog progressDialog = new ProgressDialog((Context) this.this$0);
                new Handler(Looper.myLooper()) {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void handleMessage(Message param2Message) {
                        super.handleMessage(param2Message);
                        progressDialog.incrementProgressBy(1);
                    }
                };
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                (new Thread(new Runnable() {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if (progressDialog.getProgress() > progressDialog.getMax())
                                    return;
                                Thread.sleep(200L);
                                progressDialog.dismiss();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    }
                })).start();
                Execute("/T 2");
                Toast.makeText((Context) this.this$0, "[KR] Fix Crash Succesfully!", 1).show();
            }
        });
        textView7.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.this$0);
                builder.setTitle("VIETNAM");
                builder.setMessage("Do you want to reset guest?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        ProgressDialog progressDialog = new ProgressDialog((Context) this$0);
                        new Handler(Looper.myLooper()) {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void handleMessage(Message param3Message) {
                                super.handleMessage(param3Message);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        progressDialog.setMessage("Reseting Guest...");
                        progressDialog.show();
                        (new Thread(new Runnable() {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if (progressDialog.getProgress() > progressDialog.getMax())
                                            return;
                                        Thread.sleep(1000L);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    return;
                                }
                            }
                        })).start();
                        this$0.Execute("/vn");
                        Toast.makeText((Context) this$0, "[VN] Reset Succesfully!", 1).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        Toast.makeText(this$0.getApplicationContext(), "Canceled", 0).show();
                        param2DialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        textView12.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                ProgressDialog progressDialog = new ProgressDialog((Context) this.this$0);
                new Handler(Looper.myLooper()) {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void handleMessage(Message param2Message) {
                        super.handleMessage(param2Message);
                        progressDialog.incrementProgressBy(1);
                    }
                };
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                (new Thread(new Runnable() {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if (progressDialog.getProgress() > progressDialog.getMax())
                                    return;
                                Thread.sleep(200L);
                                progressDialog.dismiss();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    }
                })).start();
                Execute("/T 3");
                Toast.makeText((Context) this.this$0, "[VN] Fix Crash Succesfully!", 1).show();
            }
        });
        textView8.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.this$0);
                builder.setTitle("TAIWAN");
                builder.setMessage("Do you want to reset guest?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        ProgressDialog progressDialog = new ProgressDialog((Context) this$0);
                        new Handler(Looper.myLooper()) {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void handleMessage(Message param3Message) {
                                super.handleMessage(param3Message);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        progressDialog.setMessage("Reseting Guest...");
                        progressDialog.show();
                        (new Thread(new Runnable() {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if (progressDialog.getProgress() > progressDialog.getMax())
                                            return;
                                        Thread.sleep(1000L);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    return;
                                }
                            }
                        })).start();
                        this$0.Execute("/tw");
                        Toast.makeText((Context) this$0, "[TW] Reset Succesfully!", 1).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        Toast.makeText(this$0.getApplicationContext(), "Canceled", 0).show();
                        param2DialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        textView13.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                ProgressDialog progressDialog = new ProgressDialog((Context) this.this$0);
                new Handler(Looper.myLooper()) {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void handleMessage(Message param2Message) {
                        super.handleMessage(param2Message);
                        progressDialog.incrementProgressBy(1);
                    }
                };
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                (new Thread(new Runnable() {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if (progressDialog.getProgress() > progressDialog.getMax())
                                    return;
                                Thread.sleep(200L);
                                progressDialog.dismiss();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    }
                })).start();
                Execute("/T 4");
                Toast.makeText((Context) this.this$0, "[TW] Fix Crash Succesfully!", 1).show();
            }
        });
        textView9.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                AlertDialog.Builder builder = new AlertDialog.Builder((Context) this.this$0);
                builder.setTitle("BGMI");
                builder.setMessage("Do you want to reset guest?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        ProgressDialog progressDialog = new ProgressDialog((Context) this$0);
                        new Handler(Looper.myLooper()) {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void handleMessage(Message param3Message) {
                                super.handleMessage(param3Message);
                                progressDialog.incrementProgressBy(1);
                            }
                        };
                        progressDialog.setMessage("Reseting Guest...");
                        progressDialog.show();
                        (new Thread(new Runnable() {
                            private final MainActivity this$0 = MainActivity.this;


                            @Override
                            public void run() {
                                try {
                                    while (true) {
                                        if (progressDialog.getProgress() > progressDialog.getMax())
                                            return;
                                        Thread.sleep(1000L);
                                        progressDialog.dismiss();
                                    }
                                } catch (Exception exception) {
                                    exception.printStackTrace();
                                    return;
                                }
                            }
                        })).start();
                        this$0.Execute("/bgmi");
                        Toast.makeText((Context) this$0, "[BGMI] Reset Succesfully!", 1).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    private final MainActivity this$0 = MainActivity.this;

                    public void onClick(DialogInterface param2DialogInterface, int param2Int) {
                        Toast.makeText(this$0.getApplicationContext(), "Canceled", 0).show();
                        param2DialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        textView14.setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                ProgressDialog progressDialog = new ProgressDialog((Context) this.this$0);
                new Handler(Looper.myLooper()) {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void handleMessage(Message param2Message) {
                        super.handleMessage(param2Message);
                        progressDialog.incrementProgressBy(1);
                    }
                };
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();
                (new Thread(new Runnable() {
                    private final MainActivity this$0 = MainActivity.this;


                    @Override
                    public void run() {
                        try {
                            while (true) {
                                if (progressDialog.getProgress() > progressDialog.getMax())
                                    return;
                                Thread.sleep(200L);
                                progressDialog.dismiss();
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            return;
                        }
                    }
                })).start();
                Execute("/T 5");
                Toast.makeText((Context) this.this$0, "[BGMI] Fix Crash Succesfully!", 1).show();
            }
        });
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.translate_scale_rotate);
        overridePendingTransition(0, 0);
        this.logo = findViewById(R.id.logo);
        this.logo.startAnimation(animation);
        if (isRootGiven()) {
            this.modeexc.setText("Root Mode");
        } else {
            this.modeexc.setText("Virtual Mode");
        }
        TextView textView1 = (TextView) findViewById(R.id.vertext);
        TextView textView3 = (TextView) findViewById(R.id.verandro);
        textView1.setText(Build.MODEL);
        textView3.setText("Android " + Build.VERSION.RELEASE);
        ((LinearLayout) findViewById(R.id.telegram)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                GoToTelegram();
            }
        });
        ((Button) findViewById(R.id.tiktok)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                GoToTiktok();
            }
        });
        ((Button) findViewById(R.id.teleown)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                GoToTeleOwn();
            }
        });
        ((Button) findViewById(R.id.donasi)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                GoToDonate();
            }
        });
        ((LinearLayout) findViewById(R.id.startButton)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                startFloating();
            }
        });
        ((LinearLayout) findViewById(R.id.stopButton)).setOnClickListener(new View.OnClickListener() {
            private final MainActivity this$0 = MainActivity.this;

            @Override
            public void onClick(View param1View) {
                stopFloating();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finishAffinity();
    }

    static {
        System.loadLibrary("native");
    }
}
