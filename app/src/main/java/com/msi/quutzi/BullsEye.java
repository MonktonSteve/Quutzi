
package com.msi.quutzi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BullsEye {
    private Paint bullsEyePaint;
    private float ballRadius;
    private float ballX, ballY;

    public BullsEye() {

    }

    public void setPaint(Paint paint) {
        bullsEyePaint = paint;
    }

    public void setRadius(float radius) {
        ballRadius = radius;
    }

    public void setCoordinates(float x, float y) {
        ballX = x;
        ballY = y;
    }

    public void draw(Canvas canvas, float bullsEyeAngle) {
        drawOneLine(canvas, bullsEyeAngle);
        bullsEyeAngle = bullsEyeAngle + 90;
        if (bullsEyeAngle > 360) {
            bullsEyeAngle -= 360;
        }
        drawOneLine(canvas, bullsEyeAngle);
    }

    private void drawOneLine(Canvas canvas, float bullsEyeAngle) {
        float xF = (float) Math.sin(Math.toRadians(bullsEyeAngle));
        float yF = (float) Math.cos(Math.toRadians(bullsEyeAngle));
        float xStart = ballX - (ballRadius * xF);
        float xEnd = ballX + (ballRadius * xF);
        float yStart = ballY - (ballRadius * yF);
        float yEnd = ballY + (ballRadius * yF);
        canvas.drawLine(xStart, yStart, xEnd, yEnd, bullsEyePaint);
    }
}

