package com.msi.quutzi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.Random;


class QuutziPlay {
    private float screenW, screenH;
    private float xMax, yMax, xMin, yMin;
    private float ballX, ballY;
    private float ballSpeedX, ballSpeedY;
    private float deltaSpeed;
    private Paint backPaint = new Paint();
    private boolean boomed;
    private boolean playAgain;
    private Context _context;
    private View _parent;
    private int level = 0;
    private int highLevel = 0;
    private int score = 0;
    private int highScore = 0;
    private Paddle paddle;
    private Ball ball;
    private Random rand = new Random();

    QuutziPlay(View parent, Context context) {
        _parent = parent;
        _context = context;
        backPaint.setColor(Color.BLUE);
        paddle = new Paddle(context);
        ball = new Ball(context);

    }

    void setSizes(int _w, int _h, int _oldw, int _oldh) {

        xMin = 0;
        yMin = 0;
        screenW = _w;
        screenH = _h;
        xMax = screenW - 1;
        yMax = screenH - 1;
        setupGame();
    }

    void draw(Canvas canvas) {
        if (playAgain) {
            if (!boomed) {
                updateGame(canvas);

            } else {
                wannaPlayAgain();
            }
        } else {
            showScore(canvas);
        }
    }

    private void updateGame(Canvas canvas) {
        canvas.drawRect(0, 0, screenW, screenH, backPaint);
        paddle.draw(canvas);
        ball.draw(canvas);
        updateBallPosition(canvas);
        _parent.invalidate();
    }

    // Detect collision and update the position of the ball.
    private void updateBallPosition(Canvas canvas) {
        float ballRadius = ball.getRadius();
        // Get new (x,y) position
        ballX += ballSpeedX;
        ballY += ballSpeedY;
        // Detect collision and react
        if (ballX + ballRadius >= xMax) {
            ballSpeedX = -ballSpeedX;
            ballX = xMax - ballRadius;
        } else if (ballX - ballRadius <= xMin) {
            ballSpeedX = -ballSpeedX;
            ballX = xMin + ballRadius;
        }
        float paddleTop = paddle.getTop();
        float paddleLeft = paddle.getLeft();
        float paddleRight = paddle.getRight();

        if (((ballY + ballRadius) >= paddleTop)
                && ((ballX > paddleLeft)
                && (ballX < paddleRight))) {
            ballSpeedY = -ballSpeedY;
            ballY = paddleTop - ballRadius;
            score += 1;
            if (score % 5 == 0) {
                nextLevel();
            }
        } else if (ballY + ballRadius >= yMax) {
            ball.boom(canvas);
            boomed = true;
        } else if (ballY - ballRadius < yMin) {
            ballSpeedY = -ballSpeedY;
            ballY = yMin + ballRadius;
        }
        ball.setCoordinates(ballY, ballX);
    }

    void touchEvent(float currentX) {
        if (currentX < paddle.getCenter()) {
            paddle.setX(paddle.getCenter());
        } else if (currentX > (screenW - paddle.getCenter())) {
            paddle.setX(screenW - paddle.getCenter());
        } else {
            paddle.setX(currentX);
        }
    }

    private void setupGame() {
        final float lineGap = 20;

        boomed = false;
        deltaSpeed = 0.5f;
        ballSpeedX = -(1.8f + rand.nextFloat());
        ballSpeedY = -1.5f;
        score = 0;
        playAgain = true;
        level = 0;
        paddle.setY(yMax - lineGap);
        paddle.setX(screenW / 2);
        ballX = screenW / 2;
        ballY = screenH / 4;
        ball.setCoordinates(ballY, ballX);
    }

    private void nextLevel() {
        level += 1;
        if (ballSpeedX > 0) {
            ballSpeedX += deltaSpeed;
        } else {
            ballSpeedX -= deltaSpeed;
        }
        if (ballSpeedY > 0) {
            ballSpeedY += deltaSpeed;
        } else {
            ballSpeedY -= deltaSpeed;
        }

    }


    private void wannaPlayAgain() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage("Do you want to play (0.7) again?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    if (score > highScore) {
                        highScore = score;
                    }
                    if (level > highLevel) {
                        highLevel = level;
                    }
                    setupGame();
                    nextLevel();
                    _parent.invalidate();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    playAgain = false;
                    if (score > highScore) {
                        highScore = score;
                    }
                    if (level > highLevel) {
                        highLevel = level;
                    }
                    _parent.invalidate();
                    break;
            }
        }
    };

    private void showScore(Canvas canvas) {
        Paint paint = new Paint();

        paint.setTextSize(20f);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setSubpixelText(true);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(Color.RED);
        String highScoreText = "High score is : " + Integer.toString(highScore);
        canvas.drawText(highScoreText, 10, 40, paint);
        String highLevelText = "Your top level is : " + Integer.toString(highLevel);
        canvas.drawText(highLevelText, 10, 65, paint);

        SharedPreferences settings = _context.getApplicationContext().getSharedPreferences("quutsi", 0);
        SharedPreferences.Editor editor = settings.edit();
        int topScore = settings.getInt("topScore", 0);
        int topLevel = settings.getInt("topLevel", 0);
        if (topScore < highScore) {
            topScore = highScore;
            editor.putInt("topScore", highScore);
            editor.apply();
        }
        if (topLevel < highLevel) {
            topLevel = highLevel;
            editor.putInt("topLevel", topLevel);
            editor.commit();
        }
        String topScoreText = "All time high score is : " + Integer.toString(topScore);
        canvas.drawText(topScoreText, 10, 90, paint);
        String topLevelText = "All time top level is : " + Integer.toString(topLevel);
        canvas.drawText(topLevelText, 10, 115, paint);
    }
}
