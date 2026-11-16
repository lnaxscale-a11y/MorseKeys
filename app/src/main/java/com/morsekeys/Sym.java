package com.morsekeys;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

public class Sym {
    static final String[] SYM = {
        "\u25A1","\u25A3","\u25A4","\u25A5","\u25A6","\u25A7","\u25A8","\u25A9",
        "\u25B3","\u25B7","\u25BD","\u25C1","\u25C6","\u25C7","\u25C8","\u25CB",
        "\u25CE","\u25CF","\u25D0","\u25D1","\u25D2","\u25D3","\u25EC","\u25ED",
        "\u25EE","\u2318","\u2316","\u2317","\u2726","\u2727","\u2605","\u2606"
    };

    public static CharSequence label(String k, int n) {
        String sym = SYM[n % SYM.length];
        SpannableString s = new SpannableString(k + "\n" + sym);
        int flag = android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
        s.setSpan(new RelativeSizeSpan(0.55f), k.length() + 1, s.length(), flag);
        s.setSpan(new ForegroundColorSpan(0xFF9A9A9A), k.length() + 1, s.length(), flag);
        return s;
    }
}
