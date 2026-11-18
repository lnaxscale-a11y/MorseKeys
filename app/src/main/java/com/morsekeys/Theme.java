package com.morsekeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;

public class Theme {
    public static final int[] ACCENT = {0xFFC62828, 0xFF2E9B4E, 0xFF9EA3A8, 0xFF6B6B6B};
    public static final int[] KEYFILL = {0xFF2A0A0A, 0xFF08200F, 0xFF1C1D1F, 0xFF000000};
    public static final int[] BG = {0xFF000000, 0xFF050A1A, 0xFF03140C, 0xFF1A1A1A, 0xFF1A110A};

    static SharedPreferences p(Context c) {
        return c.getSharedPreferences("morse", Context.MODE_PRIVATE);
    }

    public static int theme(Context c) { return Math.min(p(c).getInt("theme", 0), ACCENT.length - 1); }
    public static int bg(Context c) { return p(c).getInt("bg", 0); }
    public static int radius(Context c) { return p(c).getInt("radius", 24); }
    public static int height(Context c) { return p(c).getInt("height", 50); }
    public static String secret(Context c) { return p(c).getString("k", ""); }

    public static GradientDrawable box(Context c, boolean active) {
        int t = theme(c);
        GradientDrawable g = new GradientDrawable();
        g.setColor(active ? ((ACCENT[t] & 0x00FFFFFF) | 0x66000000) : KEYFILL[t]);
        g.setStroke(3, 0xFFFFFFFF);
        g.setCornerRadius(radius(c));
        return g;
    }
}
