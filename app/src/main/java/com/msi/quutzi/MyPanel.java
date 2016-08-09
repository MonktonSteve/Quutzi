package com.msi.quutzi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.View;


public class MyPanel extends View {
    float w, h;
    float ballRadius;
    float xMax, yMax, xMin, yMin;
    float ballX, ballY;
    float ballSpeedX, ballSpeedY;
    float bullsEyeAngle;
    Paint bullsEyePaint = new Paint();
    Paint circlePaint = new Paint();
    Paint backPaint = new Paint();
    //MediaPlayer mp1, mp2;
    boolean boomed;
    boolean yesno = false;
    boolean play_again = true;
    Context _context;
    int level = 0;
    int highLevel = 0;
    int score = 0;
    int highScore = 0;
    BullsEye bullsEye = new BullsEye();
    Paddle paddle = new Paddle();

    public MyPanel(Context context) {
        super(context);
        _context = context;
        backPaint.setColor(Color.BLUE);
        bullsEyePaint.setColor(Color.BLACK);
        bullsEyePaint.setStrokeWidth(3);
        circlePaint.setStyle(Paint.Style.FILL);

        bullsEye.setPaint(bullsEyePaint);

        //mp1 = MediaPlayer.create(context, R.raw.lock);
        //mp1.setLooping(false);
        //mp2 = MediaPlayer.create(context, R.raw.dock);
        //mp2.setLooping(false);
    }

    private void _init() {
        ballX = w / 2;
        ballY = h / 2;
        paddle.setX(w / 2);

        bullsEyeAngle = 0;
        boomed = false;
        if (level == 0) {
            ballSpeedX = -2;
            ballSpeedY = -3;
        } else if (level == 1) {
            ballSpeedX = -3;
            ballSpeedY = -4;
        } else if (level == 2) {
            ballSpeedX = -3;
            ballSpeedY = -4;
        } else {
            ballSpeedX -= 1;
            ballSpeedY -= 1;
        }
    }

    @Override
    public void onSizeChanged(int _w, int _h, int _oldw, int _oldh) {
        w = _w;
        h = _h;
        xMax = _w - 1;
        yMax = _h - 1;
        paddle.setY(yMax - 20);
        paddle.setX(w / 2);
        paddle.setLength(w / 4);
        paddle.setHeighth(12);
        xMin = 0;
        yMin = 0;
        ballX = w / 2;
        ballY = h / 2;

        ballSpeedX = -2;
        ballSpeedY = -3;
        bullsEyeAngle = 0;
        score = 0;
        if (w > h) {
            ballRadius = h / 5;
        } else {
            ballRadius = w / 5;
        }
        circlePaint.setAntiAlias(true);
        boomed = false;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (!yesno) {
            if (!boomed) {
                updateGame(canvas);
                invalidate();
            } else {
                playAgain();
            }
        }
        if (!play_again) {
            showScore(canvas);
        }
    }

    private void updateGame(Canvas canvas) {
        canvas.drawRect(0, 0, w, h, backPaint);
        drawCircle(canvas);
        paddle.draw(canvas);
        updateCircle(canvas);
    }

    private void drawCircle(Canvas canvas) {
        int[] circleColors = {Color.RED,
                Color.MAGENTA,
                Color.GREEN,
                Color.BLUE,
                Color.BLACK,
                Color.YELLOW,
                Color.CYAN,
                Color.MAGENTA,
                Color.GREEN,
                Color.RED};
        float[] circlePositions = {0f, 0.15f, 0.25f, 0.35f, 0.45F, 0.55f, 0.65f, 0.75f, 0.85f, 1f};
        //   	circlePaint.setShader(new SweepGradient(
        //   	        ballX,
        //   	        ballY,
        //   	        circleColors,
        //   	        circlePositions));
        circlePaint.setShader(new RadialGradient(
                ballX,
                ballY,
                ballRadius,
                circleColors,
                circlePositions,
                Shader.TileMode.CLAMP));
        canvas.drawCircle(ballX, ballY, ballRadius, circlePaint);
        drawBullsEye(canvas);
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
        if (ballX + ballRadius > xMax) {
            ballSpeedX = -ballSpeedX;
            ballX = xMax - ballRadius;
        } else if (ballX - ballRadius < xMin) {
            ballSpeedX = -ballSpeedX;
            ballX = xMin + ballRadius;
        }
        float lineHeighth = paddle.getHeighth();
        float lineX = paddle.getX();
        float lineLength = paddle.getLength();

        if (((ballY + ballRadius) >= (yMax - (20 + lineHeighth)))
                && ((ballX > (lineX - (lineLength / 2)))
                && (ballX < (lineX + (lineLength / 2))))) {
            ballSpeedY = -ballSpeedY;
            ballY = (yMax - (20 + lineHeighth)) - ballRadius;
            score += 1;
            if (score % 5 == 0) {
                level += 1;
                _init();
            }
            //mp1.start();
        } else if (ballY + ballRadius > yMax) {
            boom(canvas);
        } else if (ballY - ballRadius < yMin) {
            ballSpeedY = -ballSpeedY;
            ballY = yMin + ballRadius;
        }
    }

    // Touch-input handler
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float currentX = event.getX();
        float currentY = event.getY();
        float deltaX, deltaY;
        float scalingFactor = 5.0f / ((xMax > yMax) ? yMax : xMax);
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // Modify rotational angles according to movement
                //deltaX = currentX - previousX;
                //deltaY = currentY - previousY;
                //ballSpeedX += deltaX * scalingFactor;
                //ballSpeedY += deltaY * scalingFactor;
            case MotionEvent.ACTION_DOWN:
                // Modify rotational angles according to movement
                paddle.setX(currentX);
        }
        // Save current x, y
        //previousX = currentX;
        //previousY = currentY;
        return true;  // Event handled
    }

    private void boom(Canvas canvas) {
        int[] circleColors = {Color.RED, Color.RED};
        float[] circlePositions = {0f, 1f};

        circlePaint.setShader(new SweepGradient(
                ballX,
                ballY,
                circleColors,
                circlePositions));
        canvas.drawCircle(ballX, ballY, ballRadius, circlePaint);
        boomed = true;
    }

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

    private void playAgain() {
        //mp2.start();
        yesno = true;
        AlertDialog.Builder builder = new AlertDialog.Builder(_context);
        builder.setMessage("Do you want to play (0.3) again?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    play_again = true;
                    yesno = false;
                    if (score > highScore) {
                        highScore = score;
                    }
                    if (level > highLevel) {
                        highLevel = level;
                    }
                    level = 0;
                    score = 0;
                    _init();
                    invalidate();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    play_again = false;
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
}
