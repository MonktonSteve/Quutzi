package com.msi.quutzi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.msi.quutzi.Ball;
import com.msi.quutzi.Paddle;

import java.util.Random;


public class QuutziView extends View {
    QuutziPlay game;

    public QuutziView(Context context) {
        super(context);
        game = new QuutziPlay(this, context);

    }

    @Override
    public void onSizeChanged(int _w, int _h, int _oldw, int _oldh) {
        game.setSizes(_w, _h, _oldw, _oldh);
    }

    @Override
    public void onDraw(Canvas canvas) {
        game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                game.touchEvent(event.getX());
            case MotionEvent.ACTION_BUTTON_PRESS:
                return performClick();
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
