package com.morsekeys;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

public class MorseIME extends InputMethodService {
    boolean on = false, arabic = true, emoji = false;
    LinearLayout root;

    static final String[][] AR_ROWS = {
        {"\u0636","\u0635","\u062B","\u0642","\u0641","\u063A","\u0639","\u0647","\u062E","\u062D","\u062C"},
        {"\u0634","\u0633","\u064A","\u0628","\u0644","\u0627","\u062A","\u0646","\u0645","\u0643","\u0637"},
        {"\u0630","\u0621","\u0624","\u0631","\u0649","\u0629","\u0648","\u0632","\u0638","\u062F"}
    };
    static final String[][] EN_ROWS = {
        {"q","w","e","r","t","y","u","i","o","p"},
        {"a","s","d","f","g","h","j","k","l"},
        {"z","x","c","v","b","n","m"}
    };
    static final String[] EMOJI = {
        "\uD83D\uDE00","\uD83D\uDE02","\uD83D\uDE0D","\uD83D\uDE0E","\uD83D\uDE22","\uD83D\uDE21","\uD83D\uDC4D","\uD83D\uDE4F",
        "\u2764\uFE0F","\uD83D\uDD25","\uD83C\uDF89","\uD83D\uDE18","\uD83D\uDE0A","\uD83E\uDD14","\uD83D\uDE2D","\uD83D\uDE31",
        "\uD83D\uDC4B","\uD83D\uDC4F","\uD83D\uDCAA","\u2728","\uD83C\uDF39","\u2705","\u274C","\uD83D\uDE34"
    };

    LinearLayout.LayoutParams rowLp(int dp) {
        int h = (int) (dp * getResources().getDisplayMetrics().density);
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
        b.setBackground(Theme.box(this, false));
        b.setOnClickListener(v -> a.run());
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, -1, w);
        p.setMargins(3, 3, 3, 3);
        b.setLayoutParams(p);
        return b;
    }

    @Override public View onCreateInputView() {
        root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(6, 10, 6, 10);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        build();
        return root;
    }

    @Override public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);
        emoji = false;
        if (root != null) build();
    }

    void build() {
        root.setBackgroundColor(Theme.BG[Theme.bg(this)]);
        root.removeAllViews();
        toolbar();
        if (emoji) { emojiPanel(); return; }
        numbers();
        letters();
        bottom();
    }

    void toolbar() {
        LinearLayout t = new LinearLayout(this);
        t.addView(key("\u25BC", 1, () -> requestHideSelf(0)));
        t.addView(key("\u2699\uFE0F", 1, this::openApp));
        t.addView(key("\uD83D\uDE0A", 1, () -> { emoji = !emoji; build(); }));
        t.addView(key("\uD83D\uDCCB", 1, this::paste));
        t.addView(key("\uD83C\uDF10", 1, this::picker));
        t.addView(key(arabic ? "EN" : "\u0639", 1, () -> { arabic = !arabic; build(); }));
        root.addView(t, rowLp(40));
    }

    void numbers() {
        LinearLayout r = new LinearLayout(this);
        for (String d : new String[]{"1","2","3","4","5","6","7","8","9","0"}) r.addView(key(d, 1, () -> raw(d)));
        root.addView(r, rowLp(Theme.height(this)));
    }

    void letters() {
        String[][] rows = arabic ? AR_ROWS : EN_ROWS;
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            for (String k : rows[i]) row.addView(key(k, 1, () -> type(k)));
            if (i == 2) row.addView(key("\u232B", 1.5f, this::back));
            root.addView(row, rowLp(Theme.height(this)));
        }
    }

    void bottom() {
        LinearLayout last = new LinearLayout(this);
        String enc = "\u062A\u0634\u0641\u064A\u0631: ";
        Button bOn = key(on ? enc + "ON" : enc + "OFF", 2, () -> { on = !on; build(); });
        bOn.setBackground(Theme.box(this, on));
        last.addView(bOn);
        last.addView(key("\u0645\u0633\u0627\u0641\u0629", 4, this::space));
        last.addView(key(".", 1, () -> raw(".")));
        last.addView(key("\u21B5", 1, this::enter));
        root.addView(last, rowLp(Theme.height(this)));
    }

    void emojiPanel() {
        for (int r = 0; r < 3; r++) {
            LinearLayout row = new LinearLayout(this);
            for (int c = 0; c < 8; c++) {
                String e = EMOJI[r * 8 + c];
                row.addView(key(e, 1, () -> raw(e)));
            }
            root.addView(row, rowLp(Theme.height(this)));
        }
        LinearLayout last = new LinearLayout(this);
        last.addView(key("ABC", 2, () -> { emoji = false; build(); }));
        last.addView(key("\u0645\u0633\u0627\u0641\u0629", 5, () -> raw(" ")));
        last.addView(key("\u232B", 2, this::back));
        root.addView(last, rowLp(Theme.height(this)));
    }

    void openApp() {
        Intent i = new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    void paste() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm == null || !cm.hasPrimaryClip()) return;
        ClipData d = cm.getPrimaryClip();
        if (d == null || d.getItemCount() == 0) return;
        CharSequence s = d.getItemAt(0).coerceToText(this);
        if (s != null) raw(s.toString());
    }

    void picker() {
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) im.showInputMethodPicker();
    }

    String norm(String c) {
        switch (c) {
            case "\u0629": return "\u0647";
            case "\u0649": return "\u064A";
            case "\u0624": return "\u0648";
            case "\u0621": return "\u0627";
            default: return c;
        }
    }

    void type(String c) {
        InputConnection ic = getCurrentInputConnection();
        if (ic == null) return;
        if (on) {
            String m = MorseCodec.encode(norm(c), arabic, Theme.secret(this));
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
