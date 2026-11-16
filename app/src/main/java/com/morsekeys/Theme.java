package com.morsekeys;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;

public class Theme {
    public static final int[] ACCENT = {0xFFA855F7, 0xFF2ECC71, 0xFF3B82F6, 0xFFEF4444, 0xFFF59E0B};
    public static final int[] KEYFILL = {0xFF1A0B2E, 0xFF0D2B1F, 0xFF0B1B33, 0xFF2B0D0D, 0xFF2B2010};
    public static final int[] BG = {0xFF000000, 0xFF050A1A, 0xFF03140C, 0xFF1A1A1A, 0xFF1A110A};

    static SharedPreferences p(Context c) {
        return c.getSharedPreferences("morse", Context.MODE_PRIVATE);
    }

    public static int theme(Context c) { return p(c).getInt("theme", 0); }
    public static int bg(Context c) { return p(c).getInt("bg", 0); }
    public static int radius(Context c) { return p(c).getInt("radius", 24); }
    public static int height(Context c) { return p(c).getInt("height", 50); }
    public static String secret(Context c) { return p(c).getString("k", ""); }

    public static GradientDrawable box(Context c, boolean active) {
        int t = theme(c);
        GradientDrawable g = new GradientDrawable();
        g.setColor(active ? ((ACCENT[t] & 0x00FFFFFF) | 0x66000000) : KEYFILL[t]);
        g.setStroke(3, ACCENT[t]);
        g.setCornerRadius(radius(c));
        return g;
    }
}
