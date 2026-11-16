package com.morsekeys;

import android.graphics.Color;
import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;

public class MorseIME extends InputMethodService {
    boolean on = false, arabic = true;
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

    LinearLayout.LayoutParams rowLp() {
        int h = (int) (Theme.height(this) * getResources().getDisplayMetrics().density);
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
        if (root != null) build();
    }

