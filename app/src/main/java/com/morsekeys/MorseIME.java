package com.morsekeys;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class MorseIME extends InputMethodService {
    boolean on = false, arabic = true;
    LinearLayout root;

    static final String[][] AR_ROWS = {
        {"ض","ص","ث","ق","ف","غ","ع","ه","خ","ح","ج"},
        {"ش","س","ي","ب","ل","ا","ت","ن","م","ك","ط"},
        {"ذ","ء","ؤ","ر","ى","ة","و","ز","ظ","د"}
    };
    static final String[][] EN_ROWS = {
        {"q","w","e","r","t","y","u","i","o","p"},
        {"a","s","d","f","g","h","j","k","l"},
        {"z","x","c","v","b","n","m"}
    };

    GradientDrawable box(int fill) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setStroke(3, 0xFFA855F7);
        g.setCornerRadius(24);
        return g;
    }

    LinearLayout.LayoutParams rowLp() {
        int h = (int) (50 * getResources().getDisplayMetrics().density);
        return new LinearLayout.LayoutParams(-1, h);
    }

    Button key(String t, float w, Runnable a) {
        Button b = new Button(this);
        b.setText(t);
        b.setTextColor(Color.WHITE);
        b.setTextSize(18);
        b.setAllCaps(false);
        b.setPadding(0, 0, 0, 0);
        b.setMinWidth(0);
        b.setMinimumWidth(0);
        b.setMinHeight(0);
        b.setMinimumHeight(0);
        b.setBackground(box(0xFF0A0014));
        b.setOnClickListener(v -> a.run());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, -1, w);
        p.setMargins(3, 3, 3, 3);
        b.setLayoutParams(p);
        return b;
    }

    @Override public View onCreateInputView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setPadding(6, 10, 6, 10);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        build();
        return root;
    }

    void build() {
        root.removeAllViews();
        String[][] rows = arabic ? AR_ROWS : EN_ROWS;
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            for (String k : rows[i]) row.addView(key(k, 1, () -> type(k)));
            if (i == 2) row.addView(key("⌫", 1.5f, this::back));
            root.addView(row, rowLp());
        }
        LinearLayout last = new LinearLayout(this);
        Button bOn = key(on ? "تشفير: ON" : "تشفير: OFF", 2, () -> { on = !on; build(); });
        bOn.setBackground(box(on ? 0xFF5B21B6 : 0xFF0A0014));
        last.addView(bOn);
        last.addView(key(arabic ? "EN" : "ع", 1, () -> { arabic = !arabic; build(); }));
        last.addView(key("مسافة", 3, this::space));
        last.addView(key(".", 1, () -> raw(".")));
        last.addView(key("↵", 1, this::enter));
        root.addView(last, rowLp());
    }

    String secret() {
        return getSharedPreferences("morse", MODE_PRIVATE).getString("k", "");
    }

    String norm(String c) {
        switch (c) {
            case "ة": return "ه";
            case "ى": return "ي";
            case "ؤ": return "و";
            case "ء": return "ا";
            default: return c;
        }
    }

    void type(String c) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        if (on) {
            String m = MorseCodec.encode(norm(c), arabic, secret());
            if (!m.isEmpty()) { ic.commitText(m + " ", 1); return; }
        }
        ic.commitText(c, 1);
    }

    void raw(String s) {
        InputConnection ic = getCurrentInputConnection();
        if (ic != null) ic.commitText(s, 1);
    }

    void space() {
        raw(on ? "/ " : " ");
    }

    void enter() {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
        ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
    }

    void back() {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        if (on) {
            CharSequence b = ic.getTextBeforeCursor(15, 0);
            if (b != null && b.length() > 0 && b.charAt(b.length() - 1) == ' ') {
                String s = b.toString();
                int end = s.length();
                int i = end - 1;
                while (i > 0 && s.charAt(i - 1) != ' ') i--;
                ic.deleteSurroundingText(end - i, 0);
                return;
            }
        }
        ic.deleteSurroundingText(1, 0);
    }
}
