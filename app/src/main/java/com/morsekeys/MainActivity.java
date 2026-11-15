package com.morsekeys;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    boolean encrypt = true, arabic = true;
    Button bEnc, bDec, bLang;
    EditText key, in;
    TextView out;

    GradientDrawable box(int fill) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setStroke(4, 0xFFA855F7);
        g.setCornerRadius(40);
        return g;
    }

    Button btn(String t) {
        Button b = new Button(this);
        b.setText(t);
        b.setTextColor(Color.WHITE);
        b.setTextSize(16);
        b.setAllCaps(false);
        return b;
    }

    EditText edit(String hint, int lines) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(0xFF777777);
        e.setTextColor(Color.WHITE);
        e.setBackground(box(0xFF0A0014));
        e.setPadding(30, 30, 30, 30);
        e.setMinLines(lines);
        e.setGravity(Gravity.TOP | Gravity.START);
        return e;
    }

    LinearLayout.LayoutParams lp(float w) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(w > 0 ? 0 : -1, -2, w);
        p.setMargins(8, 16, 8, 16);
        return p;
    }

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setPadding(40, 80, 40, 40);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        TextView title = new TextView(this);
        title.setText("لوحة مفاتيح مورس");
        title.setTextColor(Color.WHITE);
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        root.addView(title, lp(0));

        key = edit("المفتاح السري (اختياري)", 1);
        root.addView(key, lp(0));

        LinearLayout row = new LinearLayout(this);
        bEnc = btn("تشفير");
        bDec = btn("فك الشيفرة");
        row.addView(bEnc, lp(1));
        row.addView(bDec, lp(1));
        root.addView(row, lp(0));

        bLang = btn("اللغة: عربي");
        root.addView(bLang, lp(0));

        in = edit("اكتب نصك هنا...", 4);
        root.addView(in, lp(0));

        Button go = btn("تحويل");
        go.setBackground(box(0xFF5B21B6));
        root.addView(go, lp(0));

        out = new TextView(this);
        out.setTextColor(Color.WHITE);
        out.setTextSize(20);
        out.setTextIsSelectable(true);
        out.setMinHeight(200);
        out.setPadding(30, 30, 30, 30);
        out.setBackground(box(0xFF0A0014));
        root.addView(out, lp(0));

        Button copy = btn("نسخ النتيجة");
        copy.setBackground(box(0xFF0A0014));
        root.addView(copy, lp(0));
        Button act = btn("1) تفعيل لوحة المفاتيح");
        act.setBackground(box(0xFF0A0014));
        root.addView(act, lp(0));
        act.setOnClickListener(v -> startActivity(new android.content.Intent(android.provider.Settings.ACTION_INPUT_METHOD_SETTINGS)));
        Button pick = btn("2) اختيار لوحة المفاتيح");
        pick.setBackground(box(0xFF0A0014));
        root.addView(pick, lp(0));
        pick.setOnClickListener(v -> ((android.view.inputmethod.InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker());
        key.setText(getSharedPreferences("morse", MODE_PRIVATE).getString("k", ""));
        key.addTextChangedListener(new android.text.TextWatcher() {
            public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            public void onTextChanged(CharSequence s, int a, int b, int c) {}
            public void afterTextChanged(android.text.Editable e) {
                getSharedPreferences("morse", MODE_PRIVATE).edit().putString("k", e.toString()).apply();
            }
        });

        bEnc.setOnClickListener(v -> { encrypt = true; refresh(); });
        bDec.setOnClickListener(v -> { encrypt = false; refresh(); });
        bLang.setOnClickListener(v -> { arabic = !arabic; refresh(); });
        go.setOnClickListener(v -> {
            String k = key.getText().toString();
            String t = in.getText().toString();
            out.setText(encrypt ? MorseCodec.encode(t, arabic, k) : MorseCodec.decode(t, arabic, k));
        });
        copy.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("morse", out.getText()));
            Toast.makeText(this, "تم النسخ", Toast.LENGTH_SHORT).show();
        });

        refresh();
        ScrollView sv = new ScrollView(this);
        sv.setFillViewport(true);
        sv.setBackgroundColor(Color.BLACK);
        sv.addView(root);
        setContentView(sv);
    }

    void refresh() {
        bEnc.setBackground(box(encrypt ? 0xFF5B21B6 : 0xFF0A0014));
        bDec.setBackground(box(encrypt ? 0xFF0A0014 : 0xFF5B21B6));
        bLang.setText(arabic ? "اللغة: عربي" : "Language: English");
        bLang.setBackground(box(0xFF0A0014));
    }
}
