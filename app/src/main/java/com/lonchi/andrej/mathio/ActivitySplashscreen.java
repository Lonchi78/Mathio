package com.lonchi.andrej.mathio;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.ActionMode;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by andre on 17.1.2018.
 */

public class ActivitySplashscreen extends Activity {

    Thread splashTread;
    private final int SPLASH_DISPLAY_LENGTH = 3500;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        startAnimations();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        //  Aplikovanie fontu
        TextView textQm = findViewById(R.id.splashscreen_tv_qm);
        TextView textCr = findViewById(R.id.splashscreen_tv_cr);
        Typeface fontArciform = Typeface.createFromAsset(getAssets(), "fonts/Arciform.otf");
        textQm.setTypeface(fontArciform);
        textCr.setTypeface(fontArciform);
    }

    private void startAnimations() {

        //  Elementy plus aplikovanie animacii
        LinearLayout layoutMain = findViewById(R.id.splashscreen_ll_main);
        LinearLayout layoutTop = findViewById(R.id.splashscreen_ll_top);
        LinearLayout layoutBottom = findViewById(R.id.splashscreen_ll_bottom);
        final ImageView ivMathio = findViewById(R.id.splashscreen_iv_mathio);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        Animation downToUp = AnimationUtils.loadAnimation(this, R.anim.down_to_up);

        //  Aplikovanie 1. animacie (zosvetlenie pozadia)
        alpha.reset();
        layoutMain.clearAnimation();
        layoutMain.startAnimation(alpha);

        //  Aplikovanie 2. animacie (horna pola - nabeh zhora dole)
        upToDown.reset();
        layoutTop.clearAnimation();
        layoutTop.startAnimation(upToDown);

        //  Aplikovanie 3. animacie (dolna polka - nabeh zdola hore)
        downToUp.reset();
        layoutBottom.clearAnimation();
        layoutBottom.startAnimation(downToUp);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                final ActivityOptions[] activityOptions = new ActivityOptions[1];
                activityOptions[0] = ActivityOptions.makeSceneTransitionAnimation(ActivitySplashscreen.this,
                        new Pair<>(findViewById(R.id.splashscreen_iv_mathio), "transition_mathio_name"));
                Intent mainIntent = new Intent(ActivitySplashscreen.this, MainActivity.class);
                ActivitySplashscreen.this.startActivity(mainIntent, activityOptions[0].toBundle());
                ActivitySplashscreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);

        /*
        //  Trvanie splashscreenu
        splashTread = new Thread() {
            @Override
            public void run() {
                try{
                    //  'finalDelay' je cas v milis, kolko bude splashscreen
                    int delay = 0, finalDelay = 3500;
                    while(delay < finalDelay){
                        sleep(100);
                        delay += 100;
                    }

                    //  Presun ku 'MainAcitivity'
                    final Intent intentMainActivity = new Intent(ActivitySplashscreen.this, MainActivity.class);

                    final ActivityOptions[] activityOptions = new ActivityOptions[1];

                    //activityOptions[0] = ActivityOptions.makeSceneTransitionAnimation(ActivitySplashscreen.this,
                    //        new Pair<>(findViewById(R.id.splashscreen_iv_mathio), "transition_mathio_name"));

                    ActivitySplashscreen.this.runOnUiThread(new Runnable() {
                        public void run() {
                            activityOptions[0] = ActivityOptions.makeSceneTransitionAnimation(ActivitySplashscreen.this,
                                    new Pair<>(findViewById(R.id.splashscreen_iv_mathio), "transition_mathio_name"));

                            intentMainActivity.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                            startActivity(intentMainActivity, activityOptions[0].toBundle());
                        }
                    });



                    //startActivity(intentMainActivity, activityOptions[0].toBundle());

                    ActivitySplashscreen.this.finish();

                } catch(InterruptedException e){
                    //  Nothing
                } finally {
                    ActivitySplashscreen.this.finish();
                }
            }
        };
        splashTread.start();
        */
    }
}