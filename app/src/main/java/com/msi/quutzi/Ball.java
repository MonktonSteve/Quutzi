package com.msi.quutzi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;

class Ball {
    private float leftX, topY;
    private float centerX;
    private float centerY;
    private float ballWidth;
    private float ballHeight;
    private float ballRadius;
    private Bitmap bitmap;
    private Paint circlePaint;
    private BullsEye bullsEye = new BullsEye();

    Ball(Context context) {
        Bitmap imBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_ball);
        bitmap = imBitmap.copy(Bitmap.Config.ARGB_8888, true);
        ballWidth = bitmap.getWidth();
        ballHeight = bitmap.getHeight();
        ballRadius = ballWidth / 2;
        if (ballWidth != ballHeight) {
            System.out.print("oops");
        }
        bullsEye.setRadius(ballRadius);
        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

    }

    void setCoordinates(float _centerY, float _centerX) {
        centerX = _centerX;
        leftX = centerX - ballRadius;
        centerY = _centerY;
        topY = centerY - ballRadius;
        bullsEye.setCoordinates(_centerX, _centerY);
    }

    float getTop() {
        return topY;
    }

    float getRight() {
        return leftX + ballWidth;
    }

    float getRadius() {
        return ballRadius;
    }

    void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, leftX, topY, null);
        drawBullsEye(canvas);

    }

    private void drawBullsEye(Canvas canvas) {
        bullsEye.rotate();
        bullsEye.draw(canvas);
    }

    void boom(Canvas canvas) {
        int[] circleColors = {Color.RED, Color.RED};
        float[] circlePositions = {0f, 1f};

        circlePaint.setShader(new SweepGradient(
                centerX,
                centerY,
                circleColors,
                circlePositions));

        topY = centerY - (ballHeight / 2);
    }
}
