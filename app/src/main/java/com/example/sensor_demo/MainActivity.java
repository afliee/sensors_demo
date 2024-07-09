package com.example.sensor_demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sensor_demo.sensors.Accelerometer;
import com.example.sensor_demo.sensors.Gyroscope;

public class MainActivity extends AppCompatActivity {
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    private ImageView imgFirstDice, imgSecondDice;
    private TextView tvScore;

    private int score = 1;
    private int dice1Score = 0;
    private int dice2Score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Initialize and mapping the views
        init();

//        Create animation for the dice
        Animation animFirstDice =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake
                );
        Animation animSecondDice =
                AnimationUtils.loadAnimation(
                        this,
                        R.anim.shake
                );

        Animation.AnimationListener animationListener =
                new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        score = randomDiceValue();

//               Get the resource id of the dice image
                        int res1 = getResources().getIdentifier(
                                "dice" + score,
                                "drawable",
                                "com.example.sensor_demo"
                        );
                        if (animation == animFirstDice) {
                            dice1Score = score;
                            imgFirstDice.setImageResource(res1);
                        } else if (animation == animSecondDice) {
                            dice2Score = score;
                            imgSecondDice.setImageResource(res1);
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                };

        animFirstDice.setAnimationListener(animationListener);
        animSecondDice.setAnimationListener(animationListener);

        accelerometer.setListener(new Accelerometer.Listener() {
            @Override
            public void onTranslation(float x, float y, float z) {
                if (x > 1) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                           Set the score to the text view after the animation is done
                            tvScore.setText("Score: " + (dice1Score + dice2Score));
                        }
                    }, 1000);

//                   start the animation when the accelerometer detects the shake
                    imgFirstDice.startAnimation(animFirstDice);
                    imgSecondDice.startAnimation(animSecondDice);
                }
            }
        });

        gyroscope.setListener(new Gyroscope.Listener() {
            @Override
            public void onRotation(float x, float y, float z) {
                if (x > 1.0f) {
                    getWindow()
                            .getDecorView()
                            .setBackgroundColor(Color.CYAN);
                } else if (x < -1.0f) {
                    getWindow()
                            .getDecorView()
                            .setBackgroundColor(Color.GREEN);
                }
                if (x == 0) {
                    getWindow()
                            .getDecorView()
                            .setBackgroundColor(Color.WHITE);
                }
            }
        });
    }

    private void init() {
        accelerometer = new Accelerometer(this);
        gyroscope = new Gyroscope(this);

        imgFirstDice = findViewById(R.id.imgFirstDice);
        imgSecondDice = findViewById(R.id.imgSecondDice);
        tvScore = findViewById(R.id.tvScore);
        tvScore.setText("Score: " + (dice1Score + dice2Score));
    }


    public int randomDiceValue() {
        return (int) (Math.random() * 6 + 1);
    }

    @Override
    protected void onResume() {
        super.onResume();

        accelerometer.register();
        gyroscope.register();
    }

    @Override
    protected void onPause() {
        super.onPause();

        accelerometer.unregister();
        gyroscope.unregister();
    }
}