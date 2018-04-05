package com.lonchi.andrej.mathio;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int themeId;
    ImageButton iconPlay;

    //  Celkovy koren realtime databazy
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    //  Odkaz ku premennej "condition", ktora je pod mRootRef
    DatabaseReference mConditionRef = mRootRef.child("condition");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //  Generovanie temy (resp. farebneho prevedenia)
        Random generator = new Random();
        themeId = generator.nextInt(5);
        switch (themeId){
            case 0:
                setTheme(R.style.AppTheme1);
                break;
            case 1:
                setTheme(R.style.AppTheme2);
                break;
            case 2:
                setTheme(R.style.AppTheme3);
                break;
            case 3:
                setTheme(R.style.AppTheme4);
                break;
            case 4:
                setTheme(R.style.AppTheme5);
                break;
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        TextView textQuote = findViewById(R.id.main_tv_quote);
        TextView textHello = findViewById(R.id.main_tv_hello);
        TextView textScore = findViewById(R.id.main_tv_score);

        Typeface fontArciform = Typeface.createFromAsset(getAssets(), "fonts/Arciform.otf");
        textQuote.setTypeface(fontArciform);
        textHello.setTypeface(fontArciform);
        textScore.setTypeface(fontArciform);
    }

    @Override
    protected void onStart() {
        super.onStart();

        iconPlay = findViewById(R.id.main_ib_play);
        iconPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Spustenie hry - nova aktivita
                Intent intentGameAct = new Intent(MainActivity.this, ActivityGame.class);

                //  Animacie, deklaracia
                final ActivityOptions[] activityOptions = new ActivityOptions[1];

                //  Priradenie ...  (.this, ...('id animovaneho objektu'), 'nazov objektu transitionName')
                activityOptions[0] = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,
                        new Pair<>(findViewById(R.id.main_iv_mathio), "transition_mathio_name"));

                //  Informacie dodatocne (id temy)
                intentGameAct.putExtra("themeId", themeId);

                startActivity(intentGameAct, activityOptions[0].toBundle());
            }
        });

        //  TODO
        //  Ikona highscore

        //  TODO
        //  Odkaz na Google Play

        //  TODO
        //  User profile...aktualizacia + dialog

        //  Zachytava kazdu zmenu hodnoty u "condition"
        /*
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String text = dataSnapshot.getValue(String.class);
                mTextView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        */

        //  Po klilknuti nastavi hodnotu na "Rainy"
        /*
        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Rainy");
            }
        });

        //  Po kliknuti nastavi hodnotu na "Sunny"
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConditionRef.setValue("Sunny");
            }
        });
        */
    }

    private void startAnimations() {

        //  Elementy plus aplikovanie animacii
        LinearLayout layoutMain = findViewById(R.id.main_ll_main);
        LinearLayout layoutTop = findViewById(R.id.main_ll_top);
        RelativeLayout layoutMiddle = findViewById(R.id.main_rl_middle);
        LinearLayout layoutBottom = findViewById(R.id.main_ll_bottom);
        Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation upToDown = AnimationUtils.loadAnimation(this,R.anim.up_to_down);
        Animation downToUp = AnimationUtils.loadAnimation(this, R.anim.down_to_up);

        //  Aplikovanie 1. animacie (zosvetlenie pozadia)
        alpha.reset();
        layoutMain.clearAnimation();
        layoutMain.startAnimation(alpha);

        //  Aplikovanie animacie pre vypis pouzivatelskych udajov (zosvetlenie)
        alpha.reset();
        layoutMiddle.clearAnimation();
        layoutMiddle.startAnimation(alpha);

        //  Aplikovanie 2. animacie (horna pola - nabeh zhora dole)
        upToDown.reset();
        layoutTop.clearAnimation();
        layoutTop.startAnimation(upToDown);

        //  Aplikovanie 3. animacie (dolna polka - nabeh zdola hore)
        downToUp.reset();
        layoutBottom.clearAnimation();
        layoutBottom.startAnimation(downToUp);

    }

}
