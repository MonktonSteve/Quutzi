
package com.msi.quutzi;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

class BullsEye {
    private float ballRadius;
    private float centerX, centerY;
    private float bullsEyeAngle = 0;
    private Paint bullsEyePaint = new Paint();

    BullsEye() {
        bullsEyePaint.setColor(Color.BLACK);
        bullsEyePaint.setStrokeWidth(3);
    }

    void setRadius(float radius) {
        ballRadius = radius;
    }


    void setCoordinates(float x, float y) {
        centerX = x;
        centerY = y;
    }

    void rotate() {
        if (bullsEyeAngle < 360) {
            bullsEyeAngle += 1;
        } else {
            bullsEyeAngle = 0;
        }
    }

    void draw(Canvas canvas) {
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
        float xStart = centerX - (ballRadius * xF);
        float xEnd = centerX + (ballRadius * xF);
        float yStart = centerY - (ballRadius * yF);
        float yEnd = centerY + (ballRadius * yF);
        canvas.drawLine(xStart, yStart, xEnd, yEnd, bullsEyePaint);
    }
}

