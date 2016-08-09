package com.msi.quutzi;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Paddle {
    private Paint paint = new Paint();
    private float x, y;
    private float lineLength;
    private float lineHeighth;

    public Paddle() {

        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

    }

    public void setX(float lineX) {
        x = lineX;
    }

    public void setY(float lineY) {
        y = lineY;
    }

    public void setLength(float length) {
        lineLength = length;
    }

    public void setHeighth(float heighth) {
        lineHeighth = heighth;
        paint.setStrokeWidth(heighth);
    }

    public float getLength() {
        return lineLength;
    }

    public float getHeighth() {
        return lineHeighth;
    }

    public float getX() {
        return x;
    }

    public void draw(Canvas canvas) {

        canvas.drawLine(x - (lineLength / 2), y, x + (lineLength / 2), y, paint);
    }
}
