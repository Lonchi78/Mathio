package com.lonchi.andrej.mathio;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by andre on 18.1.2018.
 */

public class UserPreferences {
    public static final String USER_PREF = "userPreferences";

    private SharedPreferences userSharedPreferences;
    private SharedPreferences.Editor userEditor;

    public UserPreferences(Context context) {
        this.userSharedPreferences = context.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE);
        this.userEditor = this.userSharedPreferences.edit();
    }

    //////////////////////////////////          SETTERS            /////////////////////////////////

    public void setHighScore( int value ) {
        //  1. argument - zachovávaný údaj
        //  2.argument - ukladaná hodnota
        this.userEditor.putInt( "HighScore", value );
        this.userEditor.commit();
    }

    public void setGamesPlayed( int value ) {
        this.userEditor.putInt( "GamesPlayed", value );
        this.userEditor.commit();
    }

    public void setUserName( String value ) {
        this.userEditor.putString( "UserName", value );
        this.userEditor.commit();
    }

    //////////////////////////////////          GETTERS            /////////////////////////////////

    public int getHighScore() {
        //  1.argument - zachovávaný údaj, ktorý chceš získať
        //  2.argument - defaultná hodnota, v prípade, že je prázdna alebo neexistuje
        return this.userSharedPreferences.getInt( "HighScore", 0 );
    }

    public int getGamesPlayed() {
        return this.userSharedPreferences.getInt( "GamesPlayed", 0);
    }

    public String getUserName() {
        return  this.userSharedPreferences.getString( "UserName", "NOBODY" );
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void remove(String key) {
        this.userEditor.remove(key);
        this.userEditor.commit();
    }

    public void clear() {
        this.userEditor.clear();
        this.userEditor.commit();
    }
}