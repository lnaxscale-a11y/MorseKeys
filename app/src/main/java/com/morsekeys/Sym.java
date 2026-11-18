package com.morsekeys;

import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

public class Sym {
    static final String[] SYM = {
        "\u25A1","\u25A3","\u25A4","\u25A5","\u25A6","\u25A7","\u25A8","\u25A9",
        "\u25B3","\u25B7","\u25BD","\u25C1","\u25C6","\u25C7","\u25C8","\u25CB",
        "\u25CE","\u25CF","\u25D0","\u25D1","\u25D2","\u25D3","\u25EC","\u25ED",
        "\u25EE","\u2318","\u2316","\u2317","\u2726","\u2727","\u2605","\u2606"
    };

    static String norm(String c) {
        switch (c) {
            case "\u0629": return "\u0647";
            case "\u0649": return "\u064A";
            case "\u0624": return "\u0648";
            case "\u0621": return "\u0627";
            default: return c;
        }
    }

    static CharSequence make(String k, String sub) {
        SpannableString s = new SpannableString(k + "\n" + sub);
        int flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s.setSpan(new RelativeSizeSpan(0.65f), k.length() + 1, s.length(), flag);
        s.setSpan(new ForegroundColorSpan(0xFF9A9A9A), k.length() + 1, s.length(), flag);
        return s;
    }

    public static CharSequence label(String k, int n) {
        return make(k, SYM[n % SYM.length]);
    }

    public static CharSequence label(Context c, String k, int n) {
        boolean ar = k.length() > 0 && k.charAt(0) >= 0x0600;
        String m = MorseCodec.encode(norm(k), ar, Theme.secret(c));
        if (m.isEmpty()) return label(k, n);
        return make(k, m.replace(".", "\u2022").replace("-", "\u2013"));
    }
}
