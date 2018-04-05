package com.lonchi.andrej.mathio;

import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

/**
 * Created by andre on 18.1.2018.
 */

public class FragmentGame extends android.support.v4.app.Fragment {
    private static final int GAME_LENGTH = 5000;        //  Hra trva bezne 5 sekund (5000 milisekund)

    TextView textScore, textQuestion;
    Button btnTrue, btnFalse;

    ProgressBar timeBar;
    CountDownTimer mCountDownTimer;
    int Q_TIME, IO_score, extraTime, remainingTime, themeId;
    boolean RESULT_IS, PAUSED;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        Random rnd = new Random();
        Q_TIME = GAME_LENGTH;                   //  Q_TIME = time for this question

        //  Get Bundle
        IO_score = 0;
        extraTime = 0;
        themeId = 0;
        Bundle importBundle = this.getArguments();
        if (importBundle != null) {
            IO_score = importBundle.getInt("score", 0);
            extraTime = importBundle.getInt("extraTime", 0);
            themeId = importBundle.getInt("themeId", 0);
        }


        //  STEP1:  Generovanie čísiel
        int IO_number1 = rnd.nextInt(9) +1;
        int IO_number2 = rnd.nextInt(9) +1;
        int resultReal = 0;
        int IO_resultPrint = 0;

        /*
        //  Nechapem na co to tu je
        while( number1 < number2 ){
            number1 = rnd.nextInt(9) +1;
            number2 = rnd.nextInt(9) +1;
        }
        */


        //  STEP2:  Generovanie matematickej operácie
        String IO_operation = "";
        switch( (rnd.nextInt(5) ) ){
            case 0:
                IO_operation = " + ";
                resultReal = IO_number1 + IO_number2;
                getContext().getTheme().applyStyle(R.style.AppTheme1, true);
                getActivity().setTheme(R.style.AppTheme1);
                themeId = 1;
                break;

            case 1:
                IO_operation = " - ";
                resultReal = IO_number1 - IO_number2;
                getContext().getTheme().applyStyle(R.style.AppTheme2, true);
                getActivity().setTheme(R.style.AppTheme2);
                themeId = 2;
                break;

            case 2:
                IO_operation = " x ";
                resultReal = IO_number1 * IO_number2;
                getContext().getTheme().applyStyle(R.style.AppTheme3, true);
                getActivity().setTheme(R.style.AppTheme3);
                themeId = 3;
                break;

            case 3:
                IO_operation = " / ";
                resultReal = IO_number1 / IO_number2;
                getContext().getTheme().applyStyle(R.style.AppTheme4, true);
                getActivity().setTheme(R.style.AppTheme4);
                themeId = 4;
                break;

            case 4:
                IO_operation = " % ";
                resultReal = IO_number1 % IO_number2;
                getContext().getTheme().applyStyle(R.style.AppTheme5, true);
                getActivity().setTheme(R.style.AppTheme5);
                themeId = 5;
                break;
        }


        //  STEP3:  Po zmenení témy, treba donastaviť farbu StatusBar
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getActivity().getTheme();
        theme.resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        @ColorInt int colorPrimaryDark = typedValue.data;
        if (Build.VERSION.SDK_INT >= 21) {
            getActivity().getWindow().setNavigationBarColor( colorPrimaryDark );
            getActivity().getWindow().setStatusBarColor( colorPrimaryDark );
        }


        //  STEP4:  Priradenie layoutu, komponentov, nastavenie fontov (kvoli themeId)
        View viewGame = inflater.inflate(R.layout.fragment_game, null);
        getUIElements(viewGame);
        setFonts();


        //  STEP5:  Urcit ci bude vysledok TRUE alebo FALSE
        IO_resultPrint = resultTrueOrFalse(rnd, resultReal, IO_resultPrint);


        //  STEP6:  Generovanie či sa bude výraz "rovnať ==" alebo "nerovnať !="
        String IO_EONE = questionEqualOrNotEqual( rnd );


        //  STEP7:  Nastavi texty (vypis vygenerovaneho vyrazu)
        setTextViews(IO_score, IO_number1, IO_number2, IO_resultPrint, IO_operation, IO_EONE);


        //  STEP8:  Spusti odpocet
        startCountDown(extraTime);

        return viewGame;
    }

    @Override
    public void onStart() {
        super.onStart();

        btnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evalUserAnswer( true );
            }
        });

        btnFalse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evalUserAnswer( false );
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();

        //  Pouzivatel moze chciet ziskat cas nazvys
        PAUSED = true;
    }

    @Override
    public void onResume() {
        super.onResume();

        //  Pouzivatel sa vrati spat, no hra sa ukonci, pre upodozrenie z podvadzania
        if( PAUSED ){
            PAUSED = false;
            gameOver("PAUSED");
        }
    }

    private void getUIElements(View viewGame){
        //  Priradi vyuzivane UI elementy

        textScore = viewGame.findViewById(R.id.game_tv_score);
        textQuestion = viewGame.findViewById(R.id.game_tv_question);
        btnTrue = viewGame.findViewById(R.id.game_btn_true);
        btnFalse = viewGame.findViewById(R.id.game_btn_false);
        timeBar = viewGame.findViewById(R.id.game_progress_bar);
    }

    private void setFonts(){
        //  Natavi font pre UI elementy

        Typeface fontArciform = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Arciform.otf");
        textQuestion.setTypeface(fontArciform);
        textScore.setTypeface(fontArciform);
        btnTrue.setTypeface(fontArciform);
        btnFalse.setTypeface(fontArciform);
    }

    private int resultTrueOrFalse(Random rnd, int resultReal, int resultPrint){
        //  Rozhoduje o správnosti výsledku, bude správny button TRUE alebo FALSE ?

        switch( (rnd.nextInt(2)) ){
            case 0:
                RESULT_IS = false;
                int c = rnd.nextInt(9) +1;

                if( resultReal == 0 ){
                    resultPrint = resultReal + c;
                    break;
                }

                //  Generovanie, náhodná hodnota "c" (c € <1,5>) sa "pričíta +" alebo "odčíta -" ?
                switch( (rnd.nextInt(2)) ){
                    case 0:
                        resultPrint = resultReal + c;
                        break;

                    case 1:
                        //  Opatrenie, pretože by "resultPrint" mohol ísť do záporných čísiel
                        while( c > resultReal ){
                            c = rnd.nextInt( resultReal ) +1;
                        }
                        resultPrint = resultReal - c;
                        break;
                }
                break;

            case 1:
                RESULT_IS = true;
                resultPrint = resultReal;
                break;
        }

        return resultPrint;
    }

    private String questionEqualOrNotEqual(Random rnd){
        //  Rozhoduje o tom, ci sa otazka:
        //      ( == )  bude rovnat
        //      ( != )  nebude rovnat (...v tomto pripade zneguje spravnost vysledku RESULT_IS)
        //  EONE = EqualOrNotEqual
        String EONE = "";

        switch( (rnd.nextInt(2)) ){
            case 0:
                EONE = " == ";
                break;

            case 1:
                RESULT_IS = !(RESULT_IS);
                EONE = " != ";
                break;
        }

        return EONE;
    }

    private void startCountDown(int extraTime){
        //  Zadefinuje odcitanie

        Q_TIME += extraTime;                 // Pripocita zvysny cas z predsolej hry
        timeBar.setMax(Q_TIME);
        timeBar.setProgress(Q_TIME);

        mCountDownTimer = new CountDownTimer(Q_TIME, 5) {

            public void onTick(long millisUntilFinished) {
                //  TODO
                //  Nastavuje progres iba do polovice...ale ked je setProgress(0) tak zmizne...
                remainingTime = (int) millisUntilFinished;
                timeBar.setProgress( (int) millisUntilFinished );
            }

            public void onFinish() {
                //  Cas vyprsal, uzivatel prehral
                timeBar.setProgress(0);
                gameOver("TIME");
            }

        };

        mCountDownTimer.start();
    }

    private void setTextViews(int score, int number1, int number2, int resultPrint, String operation, String EONE){
        //  Nastavi spravne texty na vypis

        textScore.setText(String.valueOf(score));
        textQuestion.setText( String.valueOf(number1) + operation + String.valueOf( number2 )
                + EONE + String.valueOf( resultPrint ));
    }

    private void evalUserAnswer(boolean userAnswer){
        //  Vyhodnoti uzivatelovu odpoved

        if( userAnswer == RESULT_IS ){
            //  Uzivatel odpovedal spravne
            mCountDownTimer.cancel();
            disableButtons();

            //  Maximalna mozna prenositelnost je jedna 1 sec (1000 millis)
            int nextExtraTime;
            if( remainingTime < 1000 ){
                nextExtraTime = remainingTime;
            }else{
                nextExtraTime = 1000;
            }

            //  Zabalenie informacii odosielajucich novemu fragmentu
            Bundle exportBundle = new Bundle();
            exportBundle.putInt("score", (IO_score+1) );
            exportBundle.putInt("extraTime", nextExtraTime);
            exportBundle.putInt("themeId", themeId);

            //  Spustenie noveho fragmentu
            FragmentGame nextQuestion = new FragmentGame();
            FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
            nextQuestion.setArguments( exportBundle );
            mFragmentTransaction.replace(R.id.game_container, nextQuestion).commit();

        }else{
            //  Uzivatel odpovedal nespravne
            gameOver("MISTAKE");
        }
    }

    private void gameOver(String gameOverWay) {
        disableButtons();
        mCountDownTimer.cancel();

        if ( gameOverWay.equals("MISTAKE") ){
            Toast.makeText(getContext(), "GAME OVER\nYou made a mistake", Toast.LENGTH_LONG).show();
        }else if( gameOverWay.equals("PAUSED") ){
            Toast.makeText(getContext(), "GAME OVER\nYou paused a game", Toast.LENGTH_LONG).show();
        }else if( gameOverWay.equals("TIME") ){
            Toast.makeText(getContext(), "GAME OVER\nYour time is down", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getContext(), "GAME OVER\nJust game over", Toast.LENGTH_LONG).show();
        }

        /*
        //  Zabalenie informacii pre vypis Game Over
        Bundle bundle = new Bundle();
        bundle.putInt("score", IO_score );
        bundle.putInt("theme", themeId);
        bundle.putString("gameOverWay", gameOverWay);

        Fragment_GameOver mGameOverFragment = new Fragment_GameOver();
        FragmentManager mFragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        mGameOverFragment.setArguments(bundle);
        mFragmentTransaction.replace(R.id.rl_question_panel, mGameOverFragment).commit();
        */

        //tv_expression.setText("GAME OVER");
        //checkHighScore();
    }

    private void disableButtons(){
        //  Deaktivuje klikatelnost tlacidiel

        btnTrue.setClickable(false);
        btnFalse.setClickable(false);
    }
}
