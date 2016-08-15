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


public class MyPanel extends View {
    private float screenW, screenH;
    private float ballRadius;
    private float xMax, yMax, xMin, yMin;
    private float ballX, ballY;
    private float ballSpeedX, ballSpeedY;
    private float bullsEyeAngle;
    private Paint bullsEyePaint = new Paint();
    private Paint backPaint = new Paint();
    private boolean boomed;
    private boolean playAgain;
    private Context _context;
    private int level = 0;
    private int highLevel = 0;
    private int score = 0;
    private int highScore = 0;
    private BullsEye bullsEye = new BullsEye();
    private Paddle paddle;
    private Ball ball;
    private final float lineGap = 20;

    public MyPanel(Context context) {
        super(context);
        _context = context;
        backPaint.setColor(Color.BLUE);
        bullsEyePaint.setColor(Color.BLACK);
        bullsEyePaint.setStrokeWidth(3);
        paddle = new Paddle(context);
        ball = new Ball(context);
        bullsEye.setPaint(bullsEyePaint);

    }


    @Override
    public void onSizeChanged(int _w, int _h, int _oldw, int _oldh) {
        screenW = _w;
        screenH = _h;
        xMax = screenW - 1;
        yMax = screenH - 1;
        paddle.setY(yMax - lineGap);
        paddle.setX(screenW / 2);
        setupGame();
        xMin = 0;
        yMin = 0;
        ballRadius = ball.getRadius();
        ball.setY(ballY);
        ball.setX(ballX);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (playAgain) {
            if (!boomed) {
                updateGame(canvas);
                invalidate();
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
        drawBullsEye(canvas);
        updateCircle(canvas);
    }

    private void drawBullsEye(Canvas canvas) {
        if (bullsEyeAngle < 360) {
            bullsEyeAngle += 1;
        } else {
            bullsEyeAngle = 0;
        }
        bullsEye.setRadius(ballRadius);
        bullsEye.setCoordinates(ballX, ballY);
        bullsEye.setPaint(bullsEyePaint);
        bullsEye.draw(canvas, bullsEyeAngle);
    }


    // Detect collision and update the position of the ball.
    private void updateCircle(Canvas canvas) {
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
        ball.setY(ballY);
        ball.setX(ballX);
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (currentX < paddle.getCenter()) {
                    paddle.setX(paddle.getCenter());
                } else if (currentX > (screenW - paddle.getCenter())) {
                    paddle.setX(screenW - paddle.getCenter());
                } else {
                    paddle.setX(currentX);
                }
        }
        return true;  // Event handled
    }

    private void setupGame() {
        ballX = screenW / 2;
        ballY = screenH / 4;
        bullsEyeAngle = 0;
        boomed = false;
        ballSpeedX = -1.8f;
        ballSpeedY = -2.6f;
        bullsEyeAngle = 0;
        score = 0;
        playAgain = true;
        level = 0;
    }

    private void nextLevel() {
        level += 1;
        float deltaSpeed = 1;
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
        builder.setMessage("Do you want to play (0.3) again?").setPositiveButton("Yes", dialogClickListener)
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
                    invalidate();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    playAgain = false;
                    invalidate();
                    if (score > highScore) {
                        highScore = score;
                    }
                    if (level > highLevel) {
                        highLevel = level;
                    }
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
            editor.commit();
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
