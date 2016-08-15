package com.msi.quutzi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;

public class Ball {
    private float leftX, topY;
    private float centerX;
    private float centerY;
    private float ballWidth;
    private float ballHeight;
    private float ballRadius;
    Bitmap bitmap;
    private Paint circlePaint;

    public Ball(Context context) {
        Bitmap imBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_ball);
        bitmap = imBitmap.copy(Bitmap.Config.ARGB_8888, true);
        ballWidth = bitmap.getWidth();
        ballHeight = bitmap.getHeight();
        ballRadius = ballWidth / 2;
        if (ballWidth != ballHeight) {
            System.out.print("oops");
        }
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);
    }

    public void setX(float _centerX) {
        centerX = _centerX;
        leftX = centerX - (ballWidth / 2);
    }

    public void setY(float _centerY) {
        centerY = _centerY;
        topY = centerY - (ballHeight / 2);
    }

    public float getLeft() {
        return leftX;
    }

    public float getTop() {
        return topY;
    }

    public float getRight() {
        return leftX + ballWidth;
    }

    public float getRadius() {
        return ballRadius;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, leftX, topY, null);
    }

    public void boom(Canvas canvas) {
        int[] circleColors = {Color.RED, Color.RED};
        float[] circlePositions = {0f, 1f};

        circlePaint.setShader(new SweepGradient(
                centerX,
                centerY,
                circleColors,
                circlePositions));
        canvas.drawCircle(centerX, centerY, ballRadius, circlePaint);
    }
}
