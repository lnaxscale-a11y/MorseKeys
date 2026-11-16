package com.morsekeys;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.View;
import java.util.Random;

public class MatrixView extends View {
    static final String CH = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()+=<>?/:;[]{}~";
    Paint p = new Paint();
    Random r = new Random();
    int cw, ch, cols, rows;
    char[][] g;
    float[] head;
    float[] spd;
    boolean running = false;

    public MatrixView(Context c) {
        super(c);
        float d = c.getResources().getDisplayMetrics().density;
        cw = (int) (12 * d);
        ch = (int) (16 * d);
        p.setTextSize(13 * d);
        p.setTypeface(Typeface.MONOSPACE);
    }

    @Override protected void onSizeChanged(int w, int h, int ow, int oh) {
        cols = Math.max(1, w / cw);
        rows = Math.max(1, h / ch + 1);
        g = new char[cols][rows];
        head = new float[cols];
        spd = new float[cols];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) g[i][j] = CH.charAt(r.nextInt(CH.length()));
            head[i] = -r.nextInt(rows);
            spd[i] = 0.25f + r.nextFloat() * 0.6f;
        }
    }

    @Override protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        running = true;
        invalidate();
    }

    @Override protected void onDetachedFromWindow() {
        running = false;
        super.onDetachedFromWindow();
    }

    @Override protected void onWindowVisibilityChanged(int v) {
        super.onWindowVisibilityChanged(v);
        running = (v == View.VISIBLE);
        if (running) invalidate();
    }

    @Override protected void onDraw(Canvas cv) {
        cv.drawColor(Color.BLACK);
        if (g == null) return;
        int trail = 14;
        for (int i = 0; i < cols; i++) {
            int hr = (int) head[i];
            for (int t = 0; t < trail; t++) {
                int row = hr - t;
                if (row < 0 || row >= rows) continue;
                if (t == 0) {
                    p.setColor(Color.argb(200, 200, 255, 200));
                } else {
                    int a = (int) (Math.max(25, 255 - t * 18) * 0.55f);
                    p.setColor(Color.argb(a, 0, 255, 70));
                }
                cv.drawText(g[i], row, 1, i * cw, (row + 1) * ch, p);
            }
            head[i] += spd[i];
            if (head[i] - trail > rows) {
                head[i] = -r.nextInt(rows);
                spd[i] = 0.25f + r.nextFloat() * 0.6f;
            }
            g[i][r.nextInt(rows)] = CH.charAt(r.nextInt(CH.length()));
        }
        if (running) postInvalidateDelayed(50);
    }
}
