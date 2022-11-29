package com.orca.xarg;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class Prefs {
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;
    private static final double DEFAULT_DOUBLE_VALUE = -1.0D;
    private static final float DEFAULT_FLOAT_VALUE = -1.0F;
    private static final int DEFAULT_INT_VALUE = -1;
    private static final long DEFAULT_LONG_VALUE = -1L;
    private static final String DEFAULT_STRING_VALUE = "";
    private static final String LENGTH = "_length";
    private static Prefs prefsInstance;
    private SharedPreferences sharedPreferences;

    Prefs(Context paramContext) {
        this.sharedPreferences = paramContext.getApplicationContext().getSharedPreferences(paramContext.getPackageName() + "_preferences", 0);
    }

    Prefs(Context paramContext, String paramString) {
        this.sharedPreferences = paramContext.getApplicationContext().getSharedPreferences(paramString, 0);
    }

    public static Prefs with(Context paramContext) {
        if (prefsInstance == null) prefsInstance = new Prefs(paramContext);
        return prefsInstance;
    }

    public static Prefs with(Context paramContext, String paramString) {
        if (prefsInstance == null) prefsInstance = new Prefs(paramContext, paramString);
        return prefsInstance;
    }

    public static Prefs with(Context paramContext, String paramString, boolean paramBoolean) {
        if (paramBoolean) prefsInstance = new Prefs(paramContext, paramString);
        return prefsInstance;
    }

    public static Prefs with(Context paramContext, boolean paramBoolean) {
        if (paramBoolean) prefsInstance = new Prefs(paramContext);
        return prefsInstance;
    }

    public void clear() {
        this.sharedPreferences.edit().clear().apply();
    }

    public boolean contains(String paramString) {
        return this.sharedPreferences.contains(paramString);
    }

    public Set<String> getStringSet(String paramString, Set<String> paramSet) {
        return this.sharedPreferences.getStringSet(paramString, paramSet);
    }

    public void putStringSet(String paramString, Set<String> paramSet) {
        this.sharedPreferences.edit().putStringSet(paramString, paramSet).apply();
    }

    public String read(String paramString) {
        return this.sharedPreferences.getString(paramString, "");
    }

    public String read(String paramString1, String paramString2) {
        return this.sharedPreferences.getString(paramString1, paramString2);
    }

    public boolean readBoolean(String paramString) {
        return readBoolean(paramString, false);
    }

    public boolean readBoolean(String paramString, boolean paramBoolean) {
        return this.sharedPreferences.getBoolean(paramString, paramBoolean);
    }

    public double readDouble(String paramString) {
        return !contains(paramString) ? -1.0D : Double.longBitsToDouble(readLong(paramString));
    }

    public double readDouble(String paramString, double paramDouble) {
        return !contains(paramString) ? paramDouble : Double.longBitsToDouble(readLong(paramString));
    }

    public float readFloat(String paramString) {
        return this.sharedPreferences.getFloat(paramString, -1.0F);
    }

    public float readFloat(String paramString, float paramFloat) {
        return this.sharedPreferences.getFloat(paramString, paramFloat);
    }

    public int readInt(String paramString) {
        return this.sharedPreferences.getInt(paramString, -1);
    }

    public int readInt(String paramString, int paramInt) {
        return this.sharedPreferences.getInt(paramString, paramInt);
    }

    public long readLong(String paramString) {
        return this.sharedPreferences.getLong(paramString, -1L);
    }

    public long readLong(String paramString, long paramLong) {
        return this.sharedPreferences.getLong(paramString, paramLong);
    }

    public void remove(String paramString) {
        if (contains(paramString + "_length")) {
            int i = readInt(paramString + "_length");
            if (i >= 0) {
                this.sharedPreferences.edit().remove(paramString + "_length").apply();
                int j = 0;
                while (true) {
                    if (j < i) {
                        this.sharedPreferences.edit().remove(paramString + "[" + j + "]").apply();
                        j++;
                        continue;
                    }
                    this.sharedPreferences.edit().remove(paramString).apply();
                    return;
                }
            }
        }
        this.sharedPreferences.edit().remove(paramString).apply();
    }

    public void write(String paramString1, String paramString2) {
        this.sharedPreferences.edit().putString(paramString1, paramString2).apply();
    }

    public void writeBoolean(String paramString, boolean paramBoolean) {
        this.sharedPreferences.edit().putBoolean(paramString, paramBoolean).apply();
    }

    public void writeDouble(String paramString, double paramDouble) {
        writeLong(paramString, Double.doubleToRawLongBits(paramDouble));
    }

    public void writeFloat(String paramString, float paramFloat) {
        this.sharedPreferences.edit().putFloat(paramString, paramFloat).apply();
    }

    public void writeInt(String paramString, int paramInt) {
        this.sharedPreferences.edit().putInt(paramString, paramInt).apply();
    }

    public void writeLong(String paramString, long paramLong) {
        this.sharedPreferences.edit().putLong(paramString, paramLong).apply();
    }
}
