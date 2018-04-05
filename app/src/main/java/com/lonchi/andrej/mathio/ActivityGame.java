package com.lonchi.andrej.mathio;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by andre on 18.1.2018.
 */

public class ActivityGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //  Ziskanie prenasanej informacie (id temy)
        Bundle importBundle = getIntent().getExtras();
        int themeId = importBundle.getInt("themeId", 0);
        //  Nastavenie temy
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
            default:
                setTheme(R.style.AppTheme1);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //  Odovzdanie prenasanej informacie (skore, cas nazvys, tema)
        Bundle exportBundle = new Bundle();
        exportBundle.putInt("score", 0);
        exportBundle.putInt("extraTime", 0);
        exportBundle.putInt("themeId", themeId);

        //  Otvorenie fragmentu s odovzdanim informacii
        FragmentManager gameFragmentManager = this.getSupportFragmentManager();
        FragmentTransaction gameFragmentTransaction = gameFragmentManager.beginTransaction();
        FragmentGame myFragmentGame = new FragmentGame();
        myFragmentGame.setArguments( exportBundle );
        gameFragmentTransaction.replace(R.id.game_container, myFragmentGame ).commit();

    }

}