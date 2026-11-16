package com.morsekeys;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class Clips {
    static final String SEP = "\u001F";

    static SharedPreferences p(Context c) {
        return c.getSharedPreferences("morse", Context.MODE_PRIVATE);
    }

    static List<String> read(Context c, String key) {
        List<String> l = new ArrayList<>();
        String s = p(c).getString(key, "");
        if (s.isEmpty()) return l;
        for (String x : s.split(SEP)) {
            if (!x.isEmpty()) l.add(x);
        }
        return l;
    }

    static void write(Context c, String key, List<String> l) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < l.size(); i++) {
            if (i > 0) sb.append(SEP);
            sb.append(l.get(i));
        }
        p(c).edit().putString(key, sb.toString()).apply();
    }

    public static List<String> all(Context c) {
        List<String> l = read(c, "pins");
        l.addAll(read(c, "clips"));
        return l;
    }

    public static boolean isPinned(Context c, String t) {
        return read(c, "pins").contains(t);
    }

    public static void add(Context c, String t) {
        if (t == null) return;
        t = t.trim();
        if (t.isEmpty()) return;
        if (t.length() > 500) t = t.substring(0, 500);
        if (isPinned(c, t)) return;
        List<String> l = read(c, "clips");
        l.remove(t);
        l.add(0, t);
        while (l.size() > 30) l.remove(l.size() - 1);
        write(c, "clips", l);
    }

    public static void togglePin(Context c, String t) {
        List<String> pins = read(c, "pins");
        List<String> clips = read(c, "clips");
        if (pins.contains(t)) {
            pins.remove(t);
            clips.remove(t);
            clips.add(0, t);
        } else {
            clips.remove(t);
            pins.add(0, t);
        }
        write(c, "pins", pins);
        write(c, "clips", clips);
    }

    public static void replace(Context c, String old, String neu) {
        neu = neu.trim();
        if (neu.isEmpty() || neu.equals(old)) return;
        if (neu.length() > 500) neu = neu.substring(0, 500);
        List<String> pins = read(c, "pins");
        List<String> clips = read(c, "clips");
        int i = pins.indexOf(old);
        if (i >= 0) {
            pins.remove(neu);
            pins.set(pins.indexOf(old), neu);
        } else if (clips.indexOf(old) >= 0) {
            clips.remove(neu);
            clips.set(clips.indexOf(old), neu);
        } else {
            clips.add(0, neu);
        }
        write(c, "pins", pins);
        write(c, "clips", clips);
    }

    public static void remove(Context c, String t) {
        List<String> pins = read(c, "pins");
        List<String> clips = read(c, "clips");
        pins.remove(t);
        clips.remove(t);
        write(c, "pins", pins);
        write(c, "clips", clips);
    }

    public static void clear(Context c) {
        p(c).edit().putString("clips", "").apply();
    }
}
