package com.morsekeys;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ClipEditActivity extends Activity {
    @Override protected void onCreate(Bundle s) {
        super.onCreate(s);
        final String old = getIntent().getStringExtra("t");
        if (old == null) {
            finish();
            return;
        }
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 40, 40, 40);
        final EditText e = new EditText(this);
        e.setText(old);
        e.setMinLines(3);
        e.setGravity(Gravity.TOP | Gravity.START);
        root.addView(e, new LinearLayout.LayoutParams(-1, -2));
        LinearLayout row = new LinearLayout(this);
        Button ok = new Button(this);
        ok.setText("\u062D\u0641\u0638");
        ok.setOnClickListener(v -> {
            Clips.replace(this, old, e.getText().toString());
            finish();
        });
        Button no = new Button(this);
        no.setText("\u0625\u0644\u063A\u0627\u0621");
        no.setOnClickListener(v -> finish());
        row.addView(ok, new LinearLayout.LayoutParams(0, -2, 1));
        row.addView(no, new LinearLayout.LayoutParams(0, -2, 1));
        root.addView(row, new LinearLayout.LayoutParams(-1, -2));
        setContentView(root);
    }
}
