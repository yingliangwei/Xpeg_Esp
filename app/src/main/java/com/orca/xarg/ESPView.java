package com.orca.xarg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.view.View;


public class ESPView extends View implements Runnable {
    private static final int[] OTH_NAME = new int[]{R.drawable.ic_clear_pro, R.drawable.ic_clear_noob, R.drawable.ic_warn_pro, R.drawable.ic_warn_noob, R.drawable.ic_warning, R.drawable.ic_boot};
    private static int Pos;
    private static int Size;
    private static int itemPosition;
    private static int itemSize;
    private static int setStroke;
    private static int setStrokeSkel;
    static long sleepTime;
    Bitmap bitmap;
    Paint mFPSText;
    Paint mFilledPaint;
    Paint mFilledPaint2;
    Paint mStrokePaint;
    Paint mTextPaint;
    Bitmap out;
    Paint p;
    private int mFPS = 0;
    private int mFPSCounter = 0;
    private long mFPSTime = 0;
    Bitmap[] OTHER = new Bitmap[6];
    Thread mThread = new Thread(this);

    public static void ChangeStrokeLine(int i) {
        setStroke = i;
    }

    public static void ChangeStrokeSkelton(int i) {
        setStrokeSkel = i;
    }

    public static void ChangeFps(int i) {
        sleepTime = 1000 / i;
    }

    public ESPView(Context context) {
        super(context, null, 0);
        InitializePaints(context);
        setFocusableInTouchMode(false);
        setBackgroundColor(0);
        this.mThread.start();
        AssetManager assets = context.getAssets();
        this.mTextPaint.setTypeface(Typeface.createFromAsset(assets, "fonts/font.ttf"));
        this.mFPSText.setTypeface(Typeface.createFromAsset(assets, "fonts/digital.ttf"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (canvas != null && getVisibility() == View.VISIBLE) {
            ClearCanvas(canvas);
            Overlay.DrawOn(this, canvas);
        }
    }

    @Override
    public void run() {
        while (this.mThread.isAlive() && !this.mThread.isInterrupted()) {
            try {
                long currentTimeMillis = System.currentTimeMillis();
                postInvalidate();
                Thread.sleep(Math.max(Math.min(0, sleepTime - (System.currentTimeMillis() - currentTimeMillis)), sleepTime));
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void InitializePaints(Context context) {
        this.mStrokePaint = new Paint();
        this.mStrokePaint.setStyle(Paint.Style.STROKE);
        this.mStrokePaint.setAntiAlias(true);
        this.mStrokePaint.setColor(Color.rgb(0, 0, 0));
        this.mStrokePaint.setTextAlign(Paint.Align.CENTER);
        this.mFilledPaint = new Paint();
        this.mFilledPaint.setStyle(Paint.Style.FILL);
        this.mFilledPaint.setAntiAlias(true);
        this.mFilledPaint.setColor(Color.rgb(0, 0, 0));
        this.mFilledPaint2 = new Paint();
        this.mFilledPaint2.setStyle(Paint.Style.FILL);
        this.mFilledPaint2.setAntiAlias(true);
        this.mFilledPaint2.setColor(Color.rgb(0, 0, 0));
        this.mTextPaint = new Paint();
        this.mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mTextPaint.setAntiAlias(true);
        this.mTextPaint.setColor(Color.rgb(0, 0, 0));
        this.mStrokePaint.setStrokeWidth(0.5f);
        this.mTextPaint.setTextAlign(Paint.Align.CENTER);
        this.mFPSText = new Paint();
        this.mFPSText.setStyle(Paint.Style.FILL_AND_STROKE);
        this.mFPSText.setAntiAlias(true);
        this.mFPSText.setColor(Color.rgb(0, 0, 0));
        this.mFPSText.setTextAlign(Paint.Align.CENTER);
        this.mFPSText.setStrokeWidth(0.1f);
        this.mFPSText.setShadowLayer(10, 1, 1, -16777216);
        this.p = new Paint();
        int length = this.OTHER.length;
        for (int i = 0; i < length; i++) {
            this.OTHER[i] = BitmapFactory.decodeResource(context.getResources(), OTH_NAME[i]);
            if (i == 4) {
                this.OTHER[i] = scale(this.OTHER[i], 400, 35);
            } else if (i == 5) {
                this.OTHER[i] = scale(this.OTHER[i], 22, 22);
            } else {
                this.OTHER[i] = scale(this.OTHER[i], 80, 80);
            }
        }
    }

    public void ClearCanvas(Canvas canvas) {
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }

    public void DrawLine(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, float f5) {
        this.mStrokePaint.setColor(Color.rgb(i2, i3, i4));
        this.mStrokePaint.setAlpha(i);
        this.mStrokePaint.setStrokeWidth(f);
        canvas.drawLine(f2, f3, f4, f5, this.mStrokePaint);
    }

    public void DrawLinePlayer(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, float f5) {
        this.mStrokePaint.setColor(Color.rgb(i2, i3, i4));
        this.mStrokePaint.setAlpha(i);
        this.mStrokePaint.setStrokeWidth((setStroke / 3) + f);
        canvas.drawLine(f2, f3, f4, f5, this.mStrokePaint);
    }

    public void DrawSkelton(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, float f5) {
        this.mStrokePaint.setColor(Color.rgb(i2, i3, i4));
        this.mStrokePaint.setAlpha(i);
        this.mStrokePaint.setStrokeWidth((setStrokeSkel / 3) + f);
        canvas.drawLine(f2, f3, f4, f5, this.mStrokePaint);
    }

    public void DrawRect(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4, float f5) {
        this.mStrokePaint.setStrokeWidth(f);
        this.mStrokePaint.setColor(Color.rgb(i2, i3, i4));
        this.mStrokePaint.setAlpha(i);
        canvas.drawRoundRect(new RectF(f2, f3, f4, f5), 5, 5, this.mStrokePaint);
    }

    public void DrawFilledRect(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
        this.mFilledPaint2.setColor(Color.rgb(i2, i3, i4));
        this.mFilledPaint2.setAlpha(i);
        canvas.drawRoundRect(new RectF(f, f2, f3, f4), 5, 5, this.mFilledPaint2);
    }

    public void DebugText(String str) {
        System.out.println(str);
    }



    public void DrawTextName(Canvas canvas, int i, int i2, int i3, int i4, String str, float f, float f2, float f3) {
        this.mFPSText.setARGB(i, i2, i3, i4);
        this.mFPSText.setTextSize(f3);
        if (SystemClock.uptimeMillis() - this.mFPSTime > 1000) {
            this.mFPSTime = SystemClock.uptimeMillis();
            this.mFPS = this.mFPSCounter;
            this.mFPSCounter = 0;
        } else {
            this.mFPSCounter++;
        }
        canvas.drawText("" + new StringBuffer().append("").append(this.mFPS).toString(), f, f2, this.mFPSText);
    }

    public void DrawText(Canvas canvas, int i, int i2, int i3, int i4, String str, float f, float f2, float f3) {
        this.mTextPaint.setARGB(i, i2, i3, i4);
        this.mTextPaint.setTextSize(f3);
        canvas.drawText(str, f, f2, this.mTextPaint);
    }

    @SuppressLint("DefaultLocale")
    public void DrawWeapon(Canvas canvas, int i, int i2, int i3, int i4, int i5, int i6, float f, float f2, float f3) {
        this.mTextPaint.setARGB(i, i2, i3, i4);
        this.mTextPaint.setTextSize(f3);
        String weapon = getWeapon(i5);
        canvas.drawText(String.format("%s%d", weapon + ": ", i6), f, f2, this.mTextPaint);
    }

    public void DrawEnemyCount(Canvas canvas, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{0, Color.rgb(i2, i3, i4), 0});
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setGradientRadius(120.0f);
        gradientDrawable.setBounds(new Rect(i5, i6, i7, i8));
        canvas.save();
        gradientDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        gradientDrawable.draw(canvas);
        canvas.restore();
    }

    public void DrawName(Canvas canvas, int i, int i2, int i3, int i4, String str, int i5, float f, float f2, float f3) {
        String[] split = str.split(":");
        char[] cArr = new char[split.length];
        for (int i6 = 0; i6 < split.length; i6++) {
            cArr[i6] = (char) Integer.parseInt(split[i6]);
        }
        String str2 = new String(cArr);
        this.mTextPaint.setARGB(i, i2, i3, i4);
        this.mTextPaint.setTextSize(f3);
        canvas.drawText(str2, f, f2, this.mTextPaint);
    }

    public void DrawTimID(Canvas canvas, int i, int i2, int i3, int i4, String str, int i5, float f, float f2, float f3) {
        String[] split = str.split(":");
        char[] cArr = new char[split.length];
        for (int i6 = 0; i6 < split.length; i6++) {
            cArr[i6] = (char) Integer.parseInt(split[i6]);
        }
        new String(cArr);
        this.mTextPaint.setARGB(i, i2, i3, i4);
        canvas.drawText("" + i5, f, f2 - Pos, this.mTextPaint);
    }

    public void DrawItems(Canvas canvas, String str, float f, float f2, float f3, float f4) {
        String itemName = getItemName(str);
        this.mTextPaint.setTextSize(f4);
        if (itemName != null && !itemName.equals("")) {
            if (Overlay.getConfig("Icon") || Overlay.getConfig("Icon Distance") || Overlay.getConfig("Icon Distance Name")) {
                switch (itemName) {
                    case "M416":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m416);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "SCAR-L":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scar_l);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "AKM":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.akm);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "QBZ":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qbz);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "M16A-4":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m164a);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "M249":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m249);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Groza":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.groza);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Mk47 Mutant":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mk47);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "AUG":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aug);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "M762":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m762);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "G36C":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.g36c);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "DP28":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dp);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "ASM AR":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ams);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "MG3 Machine Gun":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mg3);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "FAMAS":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.famas);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "UMP":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ump);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "MP5K":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mp5k);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "UZI":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.uzi);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Vector":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vector);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "TommyGun":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tommy);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "P90":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.p90);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Bizon":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bizon);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "AWM":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.awm);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "QBU":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qbu);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "M24":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m24);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Kar98k":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kar98k);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Mini14":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mini14);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "SKS":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sks);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "SLR":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.slr);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "MK14":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mk14);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "MK12":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mk12);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "VSS":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vss);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "CrossBow":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.crossbow);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Mosin":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mosin);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Win94":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.win94);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "8x":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s8x);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "2x":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s2x);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Red Dot":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.reddot);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "3X":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s3x);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Hollow Sight":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hollow);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "6x":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s6x);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "4x":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s4x);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Canted Sight":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.canted);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Bag lvl 3":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bag3);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Bag lvl 2":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bag2);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Bag lvl 1":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bag1);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Vest lvl 3":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vest3);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Vest lvl 2":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vest2);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Vest lvl 1":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.vest1);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Helmet lvl 3":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.helmet3);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Helmet lvl 2":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.helmet2);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Helmet lvl 1":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.helmet1);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 67, false);
                        canvas.drawBitmap(this.out, f2 - 40, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "45ACP":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammocp);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "9mm":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo9mm);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "12 Gauge":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo12uage);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "7.62mm":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo762);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "5.56mm":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo556);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Arrow":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aarrow);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "300Magnum":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ammo300magnum);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "DBS":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dbs);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "S686":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s686);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "S12K":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s12k);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "M1014":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.m1014);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "S1897":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s1897);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "SawedOff":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sawed);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Grenade":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.frag_nad);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Smoke":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.smoke);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Stun":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stun);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Molotov":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.molotov);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "FirstAid":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.first_aid);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "PainKiller":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pain_killer);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Bandage":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bandage);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Injection":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.injection);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Energy Drink":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.energy_drink);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Medkit":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.medkit);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 60, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Pan":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pan);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Machete":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.machete);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 50, false);
                        canvas.drawBitmap(this.out, f2 - 24, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Crowbar":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.crow);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 50, false);
                        canvas.drawBitmap(this.out, f2 - 25, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Sickle":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.sickle);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 50, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "R1895":
                    case "P92":
                    case "Scorpion":
                    case "P18C":
                    case "R45":
                    case "P1911":
                    case "DesertEagle":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pistol);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 50, false);
                        canvas.drawBitmap(this.out, f2 - 25, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Crate":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.crate);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 69, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "AirDrop":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.airdrop);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 60, 71, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "DropPlane":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.plane);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 85) - itemPosition, (Paint) null);
                        break;
                    case "Flare Gun":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flare_gun);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 90, 48, false);
                        canvas.drawBitmap(this.out, f2 - 50, (f3 - 70) - itemPosition, (Paint) null);
                        break;
                    case "Gas Can":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.gas_can);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 52, 72, false);
                        canvas.drawBitmap(this.out, f2 - 30, (f3 - 90) - itemPosition, (Paint) null);
                        break;
                }
                if (Overlay.getConfig("Icon Distance"))
                    canvas.drawText("" + Math.round(f) + "m", f2, f3 - itemPosition, this.mTextPaint);
                if (Overlay.getConfig("Icon Distance Name")) {
                    canvas.drawText(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(itemName).append(" (").toString()).append(Math.round(f)).toString()).append("m)").toString(), f2, f3 - itemPosition, this.mTextPaint);
                    return;
                }
                return;
            }
            canvas.drawText(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(itemName).append(" (").toString()).append(Math.round(f)).toString()).append("m)").toString(), f2, f3 - itemPosition, this.mTextPaint);
        }
    }

    public void DrawVehicles(Canvas canvas, String str, float f, float f2, float f3, float f4) {
        String vehicleName = getVehicleName(str);
        this.mTextPaint.setColor(-1);
        this.mTextPaint.setTextSize(f4);
        this.mStrokePaint.setTextSize(f4);
        if (!vehicleName.equals("")) {
            if (Overlay.getConfig("Icon") || Overlay.getConfig("Icon Distance") || Overlay.getConfig("Icon Distance Name")) {
                switch (vehicleName) {
                    case "Buggy":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.buggy);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "UAZ":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.uaz);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Trike":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.motocycle_cart);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Bike":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.motocycle);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Dacia":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.dacia);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "UTV":
                    case "ATV":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.utv);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, null);
                        break;
                    case "Jet":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.aquarail);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, null);
                        break;
                    case "Boat":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_boat);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, null);
                        break;
                    case "MiniBus":
                    case "Bus":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mini_bus);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "CoupeRB":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mirado_open);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Motorglider":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.motorglider);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Mirado":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.mirado);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Snowbike":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snowbike);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Snowmobile":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.snow_mobile);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Tempo":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tuktuk);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Rony":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rony);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Truck":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pickup);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "BRDM":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.brdm);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "LadaNiva":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lada_niva);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Scooter":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scooter);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                    case "Monster Truck":
                        this.bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pickup);
                        this.out = Bitmap.createScaledBitmap(this.bitmap, 50, 58, false);
                        canvas.drawBitmap(this.out, f2 - 20, (f3 - 80) - itemPosition, (Paint) null);
                        break;
                }
                if (Overlay.getConfig("Icon Distance")) {
                    canvas.drawText(new StringBuffer().append(new StringBuffer().append("").append(Math.round(f)).toString()).append("m").toString(), f2, f3 - itemPosition, this.mTextPaint);
                }
                if (Overlay.getConfig("Icon Distance Name")) {
                    canvas.drawText(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(vehicleName).append(" (").toString()).append(Math.round(f)).toString()).append("m)").toString(), f2, f3 - itemPosition, this.mTextPaint);
                    return;
                }
                return;
            }
            canvas.drawText(new StringBuffer().append(new StringBuffer().append(new StringBuffer().append(vehicleName).append(" (").toString()).append(Math.round(f)).toString()).append("m)").toString(), f2, f3 - itemPosition, this.mTextPaint);
        }
    }

    public void DrawCircle(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3, float f4) {
        this.mStrokePaint.setARGB(i, i2, i3, i4);
        this.mStrokePaint.setStrokeWidth(f4);
        canvas.drawCircle(f, f2, f3, this.mStrokePaint);
    }

    public void DrawFilledCircle(Canvas canvas, int i, int i2, int i3, int i4, float f, float f2, float f3) {
        this.mFilledPaint.setColor(Color.rgb(i2, i3, i4));
        this.mFilledPaint.setAlpha(i);
        canvas.drawCircle(f, f2, f3, this.mFilledPaint);
    }

    public void DrawOTH(Canvas canvas, int i, float f, float f2) {
        canvas.drawBitmap(this.OTHER[i], f, f2, this.p);
    }

    private String getWeapon(int i) {
        if (i == 101006) {
            return "AUG";
        }
        if (i == 101008) {
            return "M762";
        }
        if (i == 101003) {
            return "SCAR-L";
        }
        if (i == 101004) {
            return "M416";
        }
        if (i == 101002) {
            return "M16A4";
        }
        if (i == 101009) {
            return "MK47";
        }
        if (i == 101010) {
            return "G36C";
        }
        if (i == 101007) {
            return "QBZ";
        }
        if (i == 101001) {
            return "AKM";
        }
        if (i == 101005) {
            return "Groza";
        }
        if (i == 102005) {
            return "Bizon";
        }
        if (i == 102004) {
            return "TommyGun";
        }
        if (i == 102007) {
            return "MP5K";
        }
        if (i == 102002) {
            return "UMP";
        }
        if (i == 102003) {
            return "Vector";
        }
        if (i == 102001) {
            return "Uzi";
        }
        if (i == 105002) {
            return "DP28";
        }
        if (i == 105001) {
            return "M249";
        }
        if (i == 103003) {
            return "AWM";
        }
        if (i == 103010) {
            return "QBU";
        }
        if (i == 103009) {
            return "SLR";
        }
        if (i == 103004) {
            return "SKS";
        }
        if (i == 103006) {
            return "Mini14";
        }
        if (i == 103002) {
            return "M24";
        }
        if (i == 103001) {
            return "Kar98k";
        }
        if (i == 103005) {
            return "VSS";
        }
        if (i == 103008) {
            return "Win94";
        }
        if (i == 103007) {
            return "Mk14";
        }
        if (i == 104003) {
            return "S12K";
        }
        if (i == 104004) {
            return "DBS";
        }
        if (i == 104001) {
            return "S686";
        }
        if (i == 104002) {
            return "S1897";
        }
        if (i == 108003) {
            return "Sickle";
        }
        if (i == 108001) {
            return "Machete";
        }
        if (i == 108002) {
            return "Crowbar";
        }
        if (i == 107001) {
            return "CrossBow";
        }
        if (i == 108004) {
            return "Pan";
        }
        if (i == 106006) {
            return "SawedOff";
        }
        if (i == 106003) {
            return "R1895";
        }
        if (i == 106008) {
            return "Vz61";
        }
        if (i == 106001) {
            return "P92";
        }
        if (i == 106004) {
            return "P18C";
        }
        if (i == 106005) {
            return "R45";
        }
        if (i == 106002) {
            return "P1911";
        }
        if (i == 106010) {
            return "DesertEagle";
        }
        if (i == 103011) {
            return "Mosin";
        }
        if (i == 107005) {
            return "PanzerFaust";
        }
        return "";
    }

    private String getItemName(String str) {
        if (str.contains("MZJ_8X") && Overlay.getConfig("8x")) {
            this.mTextPaint.setARGB(255, 247, 99, 245);
            return "8x";
        } else if (str.contains("MZJ_2X") && Overlay.getConfig("2x")) {
            this.mTextPaint.setARGB(255, 230, 172, 226);
            return "2x";
        } else if (str.contains("MZJ_HD") && Overlay.getConfig("Red Dot")) {
            this.mTextPaint.setARGB(255, 230, 172, 226);
            return "Red Dot";
        } else if (str.contains("MZJ_3X") && Overlay.getConfig("3x")) {
            this.mTextPaint.setARGB(255, 247, 99, 245);
            return "3X";
        } else if (str.contains("MZJ_QX") && Overlay.getConfig("Hollow")) {
            this.mTextPaint.setARGB(255, 153, 75, 152);
            return "Hollow Sight";
        } else if (str.contains("MZJ_6X") && Overlay.getConfig("6x")) {
            this.mTextPaint.setARGB(255, 247, 99, 245);
            return "6x";
        } else if (str.contains("MZJ_4X") && Overlay.getConfig("4x")) {
            this.mTextPaint.setARGB(255, 247, 99, 245);
            return "4x";
        } else if (str.contains("MZJ_SideRMR") && Overlay.getConfig("Canted")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "Canted Sight";
        } else if (str.contains("AUG") && Overlay.getConfig("AUG")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "AUG";
        } else if (str.contains("M762") && Overlay.getConfig("M762")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "M762";
        } else if (str.contains("SCAR") && Overlay.getConfig("SCAR-L")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "SCAR-L";
        } else if (str.contains("FAMAS") && Overlay.getConfig("FAMAS")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "FAMAS";
        } else if (str.contains("MG3") && Overlay.getConfig("MG3 Machine Gun")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "MG3 Machine Gun";
        } else if (str.contains("AN94") && Overlay.getConfig("ASM AR")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "ASM AR";
        } else if (str.contains("M416") && Overlay.getConfig("M416")) {
            this.mTextPaint.setARGB(255, 0, 250, 250);
            return "M416";
        } else if (str.contains("M16A4") && Overlay.getConfig("M16A4")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "M16A-4";
        } else if (str.contains("Mk47") && Overlay.getConfig("MK47")) {
            this.mTextPaint.setARGB(255, 247, 99, 245);
            return "Mk47 Mutant";
        } else if (str.contains("G36") && Overlay.getConfig("G36C")) {
            this.mTextPaint.setARGB(255, 116, 227, 123);
            return "G36C";
        } else if (str.contains("QBZ") && Overlay.getConfig("QBZ")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "QBZ";
        } else if (str.contains("AKM") && Overlay.getConfig("AKM")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "AKM";
        } else if (str.contains("Groza") && Overlay.getConfig("Groza")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "Groza";
        } else if (str.contains("PP19") && Overlay.getConfig("Bizon")) {
            this.mTextPaint.setARGB(255, 255, 246, 0);
            return "Bizon";
        } else if (str.contains("TommyGun") && Overlay.getConfig("TommyGun")) {
            this.mTextPaint.setARGB(255, 0, 0, 207);
            return "TommyGun";
        } else if (str.contains("MP5K") && Overlay.getConfig("MP5K")) {
            this.mTextPaint.setARGB(255, 0, 0, 207);
            return "MP5K";
        } else if (str.contains("UMP9") && Overlay.getConfig("UMP")) {
            this.mTextPaint.setARGB(255, 0, 0, 207);
            return "UMP";
        } else if (str.contains("Vector") && Overlay.getConfig("Vector")) {
            this.mTextPaint.setARGB(255, 233, 0, 207);
            return "Vector";
        } else if (str.contains("MachineGun_Uzi") && Overlay.getConfig("UZI")) {
            this.mTextPaint.setARGB(255, 233, 0, 207);
            return "UZI";
        } else if (str.contains("MachineGun_P90") && Overlay.getConfig("P90")) {
            this.mTextPaint.setARGB(255, 233, 0, 207);
            return "P90";
        } else if (str.contains("DP28") && Overlay.getConfig("DP28")) {
            this.mTextPaint.setARGB(255, 255, 165, 0);
            return "DP28";
        } else if (str.contains("M249") && Overlay.getConfig("M249")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "M249";
        } else if (str.contains("AWM") && Overlay.getConfig("AWM")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "AWM";
        } else if (str.contains("QBU") && Overlay.getConfig("QBU")) {
            this.mTextPaint.setARGB(255, 207, 207, 207);
            return "QBU";
        } else if (str.contains("SLR") && Overlay.getConfig("SLR")) {
            this.mTextPaint.setARGB(255, 43, 26, 28);
            return "SLR";
        } else if (str.contains("SKS") && Overlay.getConfig("SKS")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "SKS";
        } else if (str.contains("Mini14") && Overlay.getConfig("Mini14")) {
            this.mTextPaint.setARGB(255, 0, 255, 0);
            return "Mini14";
        } else if (str.contains("Sniper_M24") && Overlay.getConfig("M24")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "M24";
        } else if (str.contains("Kar98k") && Overlay.getConfig("Kar98k")) {
            this.mTextPaint.setARGB(255, 153, 165, 0);
            return "Kar98k";
        } else if (str.contains("VSS") && Overlay.getConfig("VSS")) {
            this.mTextPaint.setARGB(255, 255, 43, 0);
            return "VSS";
        } else if (str.contains("Win94") && Overlay.getConfig("Win94")) {
            this.mTextPaint.setARGB(255, 207, 207, 207);
            return "Win94";
        } else if (str.contains("Mk14") && Overlay.getConfig("MK14")) {
            this.mTextPaint.setARGB(255, 153, 0, 0);
            return "MK14";
        } else if (str.contains("Sniper_Mosin") && Overlay.getConfig("Mosin")) {
            this.mTextPaint.setARGB(255, 153, 0, 0);
            return "Mosin";
        } else if (str.contains("Sniper_MK12") && Overlay.getConfig("MK12")) {
            this.mTextPaint.setARGB(255, 153, 0, 0);
            return "MK12";
        } else if (str.contains("S12K") && Overlay.getConfig("S12K")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "S12K";
        } else if (str.contains("DBS") && Overlay.getConfig("DBS")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "DBS";
        } else if (str.contains("SawedOff") && Overlay.getConfig("Sawed-Off")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "SawedOff";
        } else if (str.contains("M1014") && Overlay.getConfig("M1014")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "M1014";
        } else if (str.contains("DP12") && Overlay.getConfig("DBS")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "DBS";
        } else if (str.contains("S686") && Overlay.getConfig("S686")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "S686";
        } else if (str.contains("S1897") && Overlay.getConfig("S1897")) {
            this.mTextPaint.setARGB(255, 153, 109, 109);
            return "S1897";
        } else if (str.contains("Sickle") && Overlay.getConfig("Sickle")) {
            this.mTextPaint.setARGB(255, 102, 74, 74);
            return "Sickle";
        } else if (str.contains("Machete") && Overlay.getConfig("Machete")) {
            this.mTextPaint.setARGB(255, 102, 74, 74);
            return "Machete";
        } else if (str.contains("Cowbar") && Overlay.getConfig("Crowbar")) {
            this.mTextPaint.setARGB(255, 102, 74, 74);
            return "Crowbar";
        } else if (str.contains("CrossBow") && Overlay.getConfig("Crossbow")) {
            this.mTextPaint.setARGB(255, 102, 74, 74);
            return "CrossBow";
        } else if (str.contains("Pan") && Overlay.getConfig("Pan")) {
            this.mTextPaint.setARGB(255, 102, 74, 74);
            return "Pan";
        } else if (str.contains("R1895") && Overlay.getConfig("R1895")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "R1895";
        } else if (str.contains("Vz61") && Overlay.getConfig("Scorpion")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "Scorpion";
        } else if (str.contains("P92") && Overlay.getConfig("P92")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "P92";
        } else if (str.contains("P18C") && Overlay.getConfig("P18C")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "P18C";
        } else if (str.contains("R45") && Overlay.getConfig("R45")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "R45";
        } else if (str.contains("P1911") && Overlay.getConfig("P1911")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "P1911";
        } else if (str.contains("DesertEagle") && Overlay.getConfig("Dessert Eagle")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "DesertEagle";
        } else if (str.contains("Ammo_762mm") && Overlay.getConfig("7.62mm")) {
            this.mTextPaint.setARGB(255, 92, 36, 28);
            return "7.62mm";
        } else if (str.contains("Ammo_45AC") && Overlay.getConfig("45ACP")) {
            this.mTextPaint.setColor(-3355444);
            return "45ACP";
        } else if (str.contains("Ammo_556mm") && Overlay.getConfig("5.56mm")) {
            this.mTextPaint.setColor(-16711936);
            return "5.56mm";
        } else if (str.contains("Ammo_9mm") && Overlay.getConfig("9mm")) {
            this.mTextPaint.setColor(-256);
            return "9mm";
        } else if (str.contains("Ammo_300Magnum") && Overlay.getConfig("300Magnum")) {
            this.mTextPaint.setColor(-16777216);
            return "300Magnum";
        } else if (str.contains("Ammo_12Guage") && Overlay.getConfig("12Gauge")) {
            this.mTextPaint.setARGB(255, 156, 91, 81);
            return "12 Gauge";
        } else if (str.contains("Ammo_Bolt") && Overlay.getConfig("Arrow")) {
            this.mTextPaint.setARGB(255, 156, 113, 81);
            return "Arrow";
        } else if (str.contains("Bag_Lv3") && Overlay.getConfig("Bag L3")) {
            this.mTextPaint.setARGB(255, 36, 83, 255);
            return "Bag lvl 3";
        } else if (str.contains("Bag_Lv1") && Overlay.getConfig("Bag L1")) {
            this.mTextPaint.setARGB(255, 127, 154, 250);
            return "Bag lvl 1";
        } else if (str.contains("Bag_Lv2") && Overlay.getConfig("Bag L2")) {
            this.mTextPaint.setARGB(255, 77, 115, 255);
            return "Bag lvl 2";
        } else if (str.contains("Armor_Lv2") && Overlay.getConfig("Vest L2")) {
            this.mTextPaint.setARGB(255, 77, 115, 255);
            return "Vest lvl 2";
        } else if (str.contains("Armor_Lv1") && Overlay.getConfig("Vest L1")) {
            this.mTextPaint.setARGB(255, 127, 154, 250);
            return "Vest lvl 1";
        } else if (str.contains("Armor_Lv3") && Overlay.getConfig("Vest L3")) {
            this.mTextPaint.setARGB(255, 36, 83, 255);
            return "Vest lvl 3";
        } else if (str.contains("Helmet_Lv2") && Overlay.getConfig("Helmet L2")) {
            this.mTextPaint.setARGB(255, 77, 115, 255);
            return "Helmet lvl 2";
        } else if (str.contains("Helmet_Lv1") && Overlay.getConfig("Helmet L1")) {
            this.mTextPaint.setARGB(255, 127, 154, 250);
            return "Helmet lvl 1";
        } else if (str.contains("Helmet_Lv3") && Overlay.getConfig("Helmet L3")) {
            this.mTextPaint.setARGB(255, 36, 83, 255);
            return "Helmet lvl 3";
        } else if (str.contains("Pills") && Overlay.getConfig("PainKiller")) {
            this.mTextPaint.setARGB(255, 227, 91, 54);
            return "PainKiller";
        } else if (str.contains("Injection") && Overlay.getConfig("Injection")) {
            this.mTextPaint.setARGB(255, 204, 193, 190);
            return "Injection";
        } else if (str.contains("Drink") && Overlay.getConfig("EnergyDrink")) {
            this.mTextPaint.setARGB(255, 54, 175, 227);
            return "Energy Drink";
        } else if (str.contains("Firstaid") && Overlay.getConfig("FirstAid")) {
            this.mTextPaint.setARGB(255, 194, 188, 109);
            return "FirstAid";
        } else if (str.contains("Bandage") && Overlay.getConfig("Bandage")) {
            this.mTextPaint.setARGB(255, 43, 189, 48);
            return "Bandage";
        } else if (str.contains("FirstAidbox") && Overlay.getConfig("MedKit")) {
            this.mTextPaint.setARGB(255, 0, 171, 6);
            return "Medkit";
        } else if (str.contains("Grenade_Stun") && Overlay.getConfig("Stun")) {
            this.mTextPaint.setARGB(255, 204, 193, 190);
            return "Stun";
        } else if (str.contains("Grenade_Shoulei") && Overlay.getConfig("Grenade")) {
            this.mTextPaint.setARGB(255, 2, 77, 4);
            return "Grenade";
        } else if (str.contains("Grenade_Smoke") && Overlay.getConfig("Smoke")) {
            this.mTextPaint.setColor(-1);
            return "Smoke";
        } else if (str.contains("Grenade_Burn") && Overlay.getConfig("Molotov")) {
            this.mTextPaint.setARGB(255, 230, 175, 64);
            return "Molotov";
        } else if (str.contains("Large_FlashHider") && Overlay.getConfig("Flash Hider Ar")) {
            this.mTextPaint.setARGB(255, 255, 213, 130);
            return "Flash Hider Ar";
        } else if (str.contains("QK_Large_C") && Overlay.getConfig("Ar Compensator")) {
            this.mTextPaint.setARGB(255, 255, 213, 130);
            return "Ar Compensator";
        } else if (str.contains("Mid_FlashHider") && Overlay.getConfig("Flash Hider SMG")) {
            this.mTextPaint.setARGB(255, 255, 213, 130);
            return "Flash Hider SMG";
        } else if (str.contains("QT_A_") && Overlay.getConfig("Tactical Stock")) {
            this.mTextPaint.setARGB(255, 158, 222, 195);
            return "Tactical Stock";
        } else if (str.contains("DuckBill") && Overlay.getConfig("Duckbill")) {
            this.mTextPaint.setARGB(255, 158, 222, 195);
            return "DuckBill";
        } else if (str.contains("Sniper_FlashHider") && Overlay.getConfig("Flash Hider Snp")) {
            this.mTextPaint.setARGB(255, 158, 222, 195);
            return "Flash Hider Sniper";
        } else if (str.contains("Mid_Suppressor") && Overlay.getConfig("Suppressor SMG")) {
            this.mTextPaint.setARGB(255, 158, 222, 195);
            return "Suppressor SMG";
        } else if (str.contains("HalfGrip") && Overlay.getConfig("Half Grip")) {
            this.mTextPaint.setARGB(255, 155, 189, 222);
            return "Half Grip";
        } else if (str.contains("Choke") && Overlay.getConfig("Choke")) {
            this.mTextPaint.setARGB(255, 155, 189, 222);
            return "Choke";
        } else if (str.contains("QT_UZI") && Overlay.getConfig("Stock Micro UZI")) {
            this.mTextPaint.setARGB(255, 155, 189, 222);
            return "Stock Micro UZI";
        } else if (str.contains("QK_Sniper") && Overlay.getConfig("SniperCompensator")) {
            this.mTextPaint.setARGB(255, 60, 127, 194);
            return "Sniper Compensator";
        } else if (str.contains("Sniper_Suppressor") && Overlay.getConfig("Sup Sniper")) {
            this.mTextPaint.setARGB(255, 60, 127, 194);
            return "Suppressor Sniper";
        } else if (str.contains("Large_Suppressor") && Overlay.getConfig("Suppressor Ar")) {
            this.mTextPaint.setARGB(255, 60, 127, 194);
            return "Suppressor Ar";
        } else if (str.contains("Sniper_EQ_") && Overlay.getConfig("Ex.Qd.Sniper")) {
            this.mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.Sniper";
        } else if (str.contains("Mid_Q_") && Overlay.getConfig("Qd.SMG")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.SMG";
        } else if (str.contains("Mid_E_") && Overlay.getConfig("Ex.SMG")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.SMG";
        } else if (str.contains("Sniper_Q_") && Overlay.getConfig("Qd.Sniper")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.Sniper";
        } else if (str.contains("Sniper_E_") && Overlay.getConfig("Ex.Sniper")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.Sniper";
        } else if (str.contains("Large_E_") && Overlay.getConfig("Ex.Ar")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Ex.Ar";
        } else if (str.contains("Large_EQ_") && Overlay.getConfig("Ex.Qd.Ar")) {
            this.mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.Ar";
        } else if (str.contains("Large_Q_") && Overlay.getConfig("Qd.Ar")) {
            this.mTextPaint.setARGB(255, 193, 163, 209);
            return "Qd.Ar";
        } else if (str.contains("Mid_EQ_") && Overlay.getConfig("Ex.Qd.SMG")) {
            this.mTextPaint.setARGB(255, 193, 140, 222);
            return "Ex.Qd.SMG";
        } else if (str.contains("Crossbow_Q") && Overlay.getConfig("Quiver CrossBow")) {
            this.mTextPaint.setARGB(255, 148, 121, 163);
            return "Quiver CrossBow";
        } else if (str.contains("ZDD_Sniper") && Overlay.getConfig("Bullet Loop")) {
            this.mTextPaint.setARGB(255, 148, 121, 163);
            return "Bullet Loop";
        } else if (str.contains("ThumbGrip") && Overlay.getConfig("Thumb Grip")) {
            this.mTextPaint.setARGB(255, 148, 121, 163);
            return "Thumb Grip";
        } else if (str.contains("Lasersight") && Overlay.getConfig("Laser Sight")) {
            this.mTextPaint.setARGB(255, 148, 121, 163);
            return "Laser Sight";
        } else if (str.contains("Angled") && Overlay.getConfig("Angled Grip")) {
            this.mTextPaint.setARGB(255, 219, 219, 219);
            return "Angled Grip";
        } else if (str.contains("LightGrip") && Overlay.getConfig("Light Grip")) {
            this.mTextPaint.setARGB(255, 219, 219, 219);
            return "Light Grip";
        } else if (str.contains("Vertical") && Overlay.getConfig("Vertical Grip")) {
            this.mTextPaint.setARGB(255, 219, 219, 219);
            return "Vertical Grip";
        } else if (str.contains("GasCan") && Overlay.getConfig("Gas Can")) {
            this.mTextPaint.setARGB(255, 255, 143, 203);
            return "Gas Can";
        } else if (str.contains("Mid_Compensator") && Overlay.getConfig("Compensator SMG")) {
            this.mTextPaint.setARGB(255, 219, 219, 219);
            return "Compensator SMG";
        } else if (str.contains("Flare") && Overlay.getConfig("FlareGun")) {
            this.mTextPaint.setARGB(255, 242, 63, 159);
            return "Flare Gun";
        } else if (str.contains("Ghillie") && Overlay.getConfig("Ghillie Suit")) {
            this.mTextPaint.setARGB(255, 139, 247, 67);
            return "Ghillie Suit";
        } else if (str.contains("CheekPad") && Overlay.getConfig("CheekPad")) {
            this.mTextPaint.setARGB(255, 112, 55, 55);
            return "CheekPad";
        } else if (str.contains("PickUpListWrapperActor") && Overlay.getConfig("Crate")) {
            this.mTextPaint.setARGB(200, 255, 255, 255);
            return "Crate";
        } else if (str.contains("AirDropPlane") && Overlay.getConfig("DropPlane")) {
            this.mTextPaint.setARGB(255, 224, 177, 224);
            return "DropPlane";
        } else if (!str.contains("PlayerDeadInventoryBox") || !Overlay.getConfig("AirDrop")) {
            return null;
        } else {
            this.mTextPaint.setARGB(255, 255, 10, 255);
            return "AirDrop";
        }
    }

    private String getVehicleName(String str) {
        if (str.contains("Buggy") && Overlay.getConfig("Buggy")) {
            return "Buggy";
        }
        if (str.contains("UAZ") && Overlay.getConfig("UAZ")) {
            return "UAZ";
        }
        if (str.contains("MotorcycleC") && Overlay.getConfig("Trike")) {
            return "Trike";
        }
        if (str.contains("Motorcycle") && Overlay.getConfig("Bike")) {
            return "Bike";
        }
        if (str.contains("Dacia") && Overlay.getConfig("Dacia")) {
            return "Dacia";
        }
        if (str.contains("AquaRail") && Overlay.getConfig("Jet")) {
            return "Jet";
        }
        if (str.contains("PG117") && Overlay.getConfig("Boat")) {
            return "Boat";
        }
        if (str.contains("MiniBus") && Overlay.getConfig("Bus")) {
            return "Bus";
        }
        if (str.contains("Mirado") && Overlay.getConfig("Mirado")) {
            return "Mirado";
        }
        if (str.contains("Scooter") && Overlay.getConfig("Scooter")) {
            return "Scooter";
        }
        if (str.contains("Rony") && Overlay.getConfig("Rony")) {
            return "Rony";
        }
        if (str.contains("Snowbike") && Overlay.getConfig("Snowbike")) {
            return "Snowbike";
        }
        if (str.contains("Snowmobile") && Overlay.getConfig("Snowmobile")) {
            return "Snowmobile";
        }
        if (str.contains("Tuk") && Overlay.getConfig("Tempo")) {
            return "Tempo";
        }
        if (str.contains("UTV") && Overlay.getConfig("UTV")) {
            return "UTV";
        }
        if (str.contains("ATV") && Overlay.getConfig("ATV")) {
            return "ATV";
        }
        if (str.contains("PickUp") && Overlay.getConfig("Truck")) {
            return "Truck";
        }
        if (str.contains("BRDM") && Overlay.getConfig("BRDM")) {
            return "BRDM";
        }
        if (str.contains("LadaNiva") && Overlay.getConfig("LadaNiva")) {
            return "LadaNiva";
        }
        if (str.contains("Bigfoot") && Overlay.getConfig("MonsterTruck")) {
            return "Monster Truck";
        }
        if (str.contains("CoupeRB") && Overlay.getConfig("CoupeRB")) {
            return "CoupeRB";
        }
        if (!str.contains("Motorglider") || !Overlay.getConfig("Motorglider")) {
            return "";
        }
        return "Motorglider";
    }

    public static Bitmap scale(Bitmap bitmap, int i, int i2) {
        if (bitmap.getWidth() / i >= bitmap.getHeight() / i2) {
            i2 = (i / bitmap.getWidth()) * bitmap.getHeight();
            if (i2==0){
                System.out.println(bitmap.getWidth());
                i2=50;
            }
        } else {
            i = (i2 / bitmap.getHeight()) * bitmap.getWidth();
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        float width = i / bitmap.getWidth();
        float height = i2 / bitmap.getHeight();
        float f = i / 2.0f;
        float f2 = i2 / 2.0f;
        Matrix matrix = new Matrix();
        matrix.setScale(width, height, f, f2);
        Canvas canvas = new Canvas(createBitmap);
        canvas.setMatrix(matrix);
        canvas.drawBitmap(bitmap, f - (bitmap.getWidth() / 2), f2 - (bitmap.getHeight() / 2), new Paint(2));
        return createBitmap;
    }
}

