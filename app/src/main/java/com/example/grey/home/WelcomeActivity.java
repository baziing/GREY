package com.example.grey.home;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.grey.R;
import com.example.grey.sensor.ChangeOrientationHandler;
import com.example.grey.sensor.OrientationSensorListener;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        TextView hideView=findViewById(R.id.welcome);

        hideView.animate()
                .alpha(0f)
                .setDuration(1000)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });

        final Intent intent=new Intent(WelcomeActivity.this, DrawerActivity.class);
        Timer timer=new Timer();
        TimerTask tast=new TimerTask() {
            @Override
            public void run() {
                startActivity(intent);
                WelcomeActivity.this.finish();
            }
        };
        timer.schedule(tast, 1000);
    }
}
