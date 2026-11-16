package com.morsekeys;
// PAGES-V2

import android.app.Activity;
import android.content.*;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.text.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;

public class MainActivity extends Activity {
    static final String[] NAMES = {"bnfsjy", ">xDr", ">zrq", ">Hmr", "*hby"};
    static final String[] BGN = {">swd", "kHly", ">xDr gAmq", "rmAdy", "bny"};
    int cur = 0;
    boolean encrypt = true, arabic = true;
    TextView stEnabled, stSelected;
    LinearLayout previewBox;
    SharedPreferences sp;

    static String ar(String s) {
        String from = "AbtvjHxd*rzs$SDTZEgfqklmnhwYyp'}><|&";
        String to = "\u0627\u0628\u062A\u062B\u062C\u062D\u062E\u062F\u0630\u0631\u0632\u0633\u0634\u0635\u0636\u0637\u0638\u0639\u063A\u0641\u0642\u0643\u0644\u0645\u0646\u0647\u0648\u0649\u064A\u0629\u0621\u0626\u0623\u0625\u0622\u0624";
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            int i = from.indexOf(c);
            sb.append(i >= 0 ? to.charAt(i) : c);
        }
        return sb.toString();
    }

    int dp(int v) { return (int) (v * getResources().getDisplayMetrics().density); }
    int acc() { return Theme.ACCENT[Theme.theme(this)]; }

    LinearLayout.LayoutParams lp() {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1, -2);
        p.setMargins(dp(4), dp(8), dp(4), dp(8));
        return p;
    }

    TextView tv(String t, int size, int color) {
        TextView v = new TextView(this);
        v.setText(t);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    LinearLayout card() {
        LinearLayout c = new LinearLayout(this);
        c.setOrientation(LinearLayout.VERTICAL);
        c.setPadding(dp(16), dp(16), dp(16), dp(16));
        GradientDrawable g = new GradientDrawable();
        g.setColor(0xFF08080C);
        g.setStroke(2, acc());
        g.setCornerRadius(dp(20));
        c.setBackground(g);
        return c;
    }

    Button btn(String t, int fill, int stroke, int text) {
        Button b = new Button(this);
        b.setText(t);
        b.setAllCaps(false);
        b.setTextSize(16);
        b.setTextColor(text);
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setStroke(3, stroke);
        g.setCornerRadius(dp(30));
        b.setBackground(g);
        b.setLayoutParams(lp());
        return b;
    }

    Button btn(String t, boolean filled) {
        return btn(t, filled ? acc() : 0x00000000, acc(), filled ? Color.BLACK : Color.WHITE);
    }

    EditText edit(String hint, int lines) {
        EditText e = new EditText(this);
        e.setHint(hint);
        e.setHintTextColor(0xFF777777);
        e.setTextColor(Color.WHITE);
        e.setMinLines(lines);
        e.setGravity(Gravity.TOP | Gravity.START);
        e.setPadding(dp(14), dp(14), dp(14), dp(14));
        GradientDrawable g = new GradientDrawable();
        g.setColor(0xFF0A0A10);
        g.setStroke(3, acc());
        g.setCornerRadius(dp(18));
        e.setBackground(g);
        return e;
    }

    TextView pk(String s, float w) {
        TextView t = tv(s, 15, Color.WHITE);
        t.setGravity(Gravity.CENTER);
        t.setBackground(Theme.box(this, false));
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0, -1, w);
        p.setMargins(2, 2, 2, 2);
        t.setLayoutParams(p);
        return t;
    }

    View preview() {
        LinearLayout box = new LinearLayout(this);
        box.setOrientation(LinearLayout.VERTICAL);
        box.setBackgroundColor(Theme.BG[Theme.bg(this)]);
        box.setPadding(dp(4), dp(6), dp(4), dp(6));
        box.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        int h = dp(Theme.height(this));
        for (String[] r : MorseIME.AR_ROWS) {
            LinearLayout row = new LinearLayout(this);
            for (String k : r) row.addView(pk(k, 1));
            box.addView(row, new LinearLayout.LayoutParams(-1, h));
        }
        LinearLayout last = new LinearLayout(this);
        last.addView(pk(ar("msAfp"), 5));
        last.addView(pk("\u21B5", 1));
        box.addView(last, new LinearLayout.LayoutParams(-1, h));
        return box;
    }

    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        sp = getSharedPreferences("morse", MODE_PRIVATE);
        render();
    }

    @Override protected void onResume() {
        super.onResume();
        updateStatus();
    }

    void updateStatus() {
        if (stEnabled == null) return;
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        boolean en = false;
        for (InputMethodInfo i : im.getEnabledInputMethodList()) {
            if (i.getPackageName().equals(getPackageName())) en = true;
        }
        String def = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        boolean sel = def != null && def.startsWith(getPackageName() + "/");
        stEnabled.setText(en ? "\u2705 " + ar("mfEl") : "\u26A0\uFE0F " + ar("gyr mfEl"));
        stSelected.setText(sel ? "\u2705 " + ar("tm AxtyArh") : "\u26A0\uFE0F " + ar("lm txtrh bEd"));
    }

    void render() {
        stEnabled = null;
        stSelected = null;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.BLACK);
        root.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        ScrollView sv = new ScrollView(this);
        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(14), dp(40), dp(14), dp(14));
        switch (cur) {
            case 0: home(page); break;
            case 1: designs(page); break;
            case 2: backgrounds(page); break;
            case 3: shape(page); break;
            default: settings(page);
        }
        sv.addView(page);
        root.addView(sv, new LinearLayout.LayoutParams(-1, 0, 1));
        LinearLayout nav = new LinearLayout(this);
        nav.setBackgroundColor(0xFF08080C);
        String[] icons = {"\uD83C\uDFE0", "\uD83C\uDFA8", "\uD83D\uDDBC\uFE0F", "\u2328\uFE0F", "\u2699\uFE0F"};
        String[] names = {"Alr}ysyp", "AltSAmym", "Alxlfyp", "Al$kl", "Al<EdAdAt"};
        for (int i = 0; i < 5; i++) {
            final int idx = i;
            TextView t = tv(icons[i] + "\n" + ar(names[i]), 12, i == cur ? acc() : 0xFF8A8A8A);
            t.setGravity(Gravity.CENTER);
            t.setPadding(0, dp(8), 0, dp(8));
            t.setOnClickListener(v -> { cur = idx; render(); });
            nav.addView(t, new LinearLayout.LayoutParams(0, -2, 1));
        }
        root.addView(nav, new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
        updateStatus();
    }

    void home(LinearLayout p) {
        TextView t = tv(ar("lwHp mfAtyH mwrs"), 26, acc());
        t.setGravity(Gravity.CENTER);
        p.addView(t, lp());
        TextView s = tv(ar("kybwrd b$yfrp mwrs"), 14, 0xFF9A9A9A);
        s.setGravity(Gravity.CENTER);
        p.addView(s, lp());
        LinearLayout c1 = card();
        c1.addView(tv(ar("HAlp Alkybwrd"), 18, Color.WHITE));
        stEnabled = tv("", 16, Color.WHITE);
        stSelected = tv("", 16, Color.WHITE);
        c1.addView(stEnabled);
        c1.addView(stSelected);
        Button b1 = btn("\u0661) " + ar("tfEyl Alkybwrd"), true);
        b1.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_INPUT_METHOD_SETTINGS)));
        Button b2 = btn("\u0662) " + ar("AxtyAr Alkybwrd"), false);
        b2.setOnClickListener(v -> ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).showInputMethodPicker());
        c1.addView(b1);
        c1.addView(b2);
        p.addView(c1, lp());
        LinearLayout c2 = card();
        c2.addView(tv(ar("mEAynp mbA$rp"), 18, Color.WHITE));
        c2.addView(preview(), lp());
        c2.addView(tv(ar("gyr Al$kl mn SfHAt AltSAmym wAlxlfyp wAl$kl."), 13, 0xFF9A9A9A));
        p.addView(c2, lp());
    }

    void designs(LinearLayout p) {
        p.addView(tv(ar("AltSAmym"), 24, acc()), lp());
        LinearLayout c = card();
        for (int i = 0; i < NAMES.length; i++) {
            final int idx = i;
            String mark = Theme.theme(this) == i ? "\u2713 " : "";
            Button b = btn(mark + ar(NAMES[i]), Theme.KEYFILL[i], Theme.ACCENT[i], Color.WHITE);
            b.setOnClickListener(v -> { sp.edit().putInt("theme", idx).apply(); render(); });
            c.addView(b);
        }
        p.addView(c, lp());
    }

    void backgrounds(LinearLayout p) {
        p.addView(tv(ar("Alxlfyp"), 24, acc()), lp());
        LinearLayout c = card();
        for (int i = 0; i < BGN.length; i++) {
            final int idx = i;
            String mark = Theme.bg(this) == i ? "\u2713 " : "";
            Button b = btn(mark + ar(BGN[i]), Theme.BG[i], acc(), Color.WHITE);
            b.setOnClickListener(v -> { sp.edit().putInt("bg", idx).apply(); render(); });
            c.addView(b);
        }
        p.addView(c, lp());
    }

    void seek(SeekBar s, final String key, final int offset) {
        s.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar b, int v, boolean user) {
                sp.edit().putInt(key, v + offset).apply();
                previewBox.removeAllViews();
                previewBox.addView(preview());
            }
            public void onStartTrackingTouch(SeekBar b) {}
            public void onStopTrackingTouch(SeekBar b) {}
        });
    }

    void shape(LinearLayout p) {
        p.addView(tv(ar("Al$kl"), 24, acc()), lp());
        LinearLayout c = card();
        previewBox = new LinearLayout(this);
        previewBox.setOrientation(LinearLayout.VERTICAL);
        previewBox.addView(preview());
        c.addView(tv(ar("AstdArp Al>zrAr"), 16, Color.WHITE));
        SeekBar r = new SeekBar(this);
        r.setMax(60);
        r.setProgress(Theme.radius(this));
        seek(r, "radius", 0);
        c.addView(r);
        c.addView(tv(ar("ArtfAE Al>zrAr"), 16, Color.WHITE));
        SeekBar h = new SeekBar(this);
        h.setMax(30);
        h.setProgress(Theme.height(this) - 40);
        seek(h, "height", 40);
        c.addView(h);
        c.addView(previewBox, lp());
        p.addView(c, lp());
    }

    void settings(LinearLayout p) {
        p.addView(tv(ar("Al<EdAdAt"), 24, acc()), lp());
        LinearLayout c = card();
        c.addView(tv(ar("AlmftAH Alsry"), 18, Color.WHITE));
        EditText key = edit(ar("AlmftAH Alsry (AxtyAry)"), 1);
        key.setText(Theme.secret(this));
        key.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence x, int a, int b, int d) {}
            public void onTextChanged(CharSequence x, int a, int b, int d) {}
            public void afterTextChanged(Editable e) {
                sp.edit().putString("k", e.toString()).apply();
            }
        });
        c.addView(key, lp());
        c.addView(tv(ar("lAzm ykwn nfs AlmftAH End Sdyqk HtY yfk Al$yfrp."), 13, 0xFF9A9A9A));
        p.addView(c, lp());
        LinearLayout t = card();
        t.addView(tv(ar(">dAp AltHwyl"), 18, Color.WHITE));
        final Button bm = btn(ar("AlwDE: t$fyr"), false);
        bm.setOnClickListener(v -> {
            encrypt = !encrypt;
            bm.setText(encrypt ? ar("AlwDE: t$fyr") : ar("AlwDE: fk Al$yfrp"));
        });
        final Button bl = btn(ar("Alllgp: Erby"), false);
        bl.setOnClickListener(v -> {
            arabic = !arabic;
            bl.setText(arabic ? ar("Alllgp: Erby") : "Language: English");
        });
        final EditText in = edit(ar("Aktb nSk hnA..."), 3);
        final TextView out = tv("", 18, Color.WHITE);
        out.setTextIsSelectable(true);
        out.setMinHeight(dp(60));
        Button go = btn(ar("tHwyl"), true);
        go.setOnClickListener(v -> {
            String k = Theme.secret(this);
            String s = in.getText().toString();
            out.setText(encrypt ? MorseCodec.encode(s, arabic, k) : MorseCodec.decode(s, arabic, k));
        });
        Button cp = btn(ar("nsx Alntyjp"), false);
        cp.setOnClickListener(v -> {
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            cm.setPrimaryClip(ClipData.newPlainText("morse", out.getText()));
            Toast.makeText(this, ar("tm Alnsx"), Toast.LENGTH_SHORT).show();
        });
        t.addView(bm);
        t.addView(bl);
        t.addView(in, lp());
        t.addView(go);
        t.addView(out, lp());
        t.addView(cp);
        p.addView(t, lp());
    }
}
