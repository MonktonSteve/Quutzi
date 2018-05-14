package com.msi.quutzi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

class Paddle {
    private float leftX, topY;
    private float barLength;
    private float barHeight;
    private Bitmap bitmap;

    Paddle(Context context) {
        Bitmap imBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_bar);
        bitmap = imBitmap.copy(Bitmap.Config.ARGB_8888, true);
        barLength = bitmap.getWidth();
        barHeight = bitmap.getHeight();

    }

    void setX(float barX) {
        leftX = barX - (barLength / 2);
    }

    void setY(float bottomY) {
        topY = bottomY - barHeight;
    }

    float getLeft() {
        return leftX;
    }

    float getTop() {
        return topY;
    }

    float getRight() {
        return leftX + barLength;
    }

    float getCenter() {
        return (barLength / 2);
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, leftX, topY, null);
    }
}
