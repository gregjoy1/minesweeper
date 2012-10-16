package com.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
/**This class is the game intent, it is the bridge between the main menu and the game itself
 * 
 * @author Greg Joy
 *
 */
public class GameActivityStart extends Activity {
	//Declares the intent and the gameplay class
	Gameplay game;
	Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        //extracts the mine density and difficulty from the intent
        Log.d("intent", ""+intent.getExtras().getDouble("minedensity"));
        Log.d("intent", ""+intent.getExtras().getInt("difficulty"));        
        //Calls the constructor for the gameplay class
        game = new Gameplay(this, intent.getExtras().getInt("difficulty"),intent.getExtras().getDouble("minedensity"));
        //Set the visuals to be handled by the gameplay class
        setContentView(game);
        //Passes the focus to the gameplay class
        game.requestFocus();
    }
    
    /**This method kills the game processes and invokes the garbage collector when the game is
     * minimised.
     */
    @Override
    public void onPause(){
    	super.onPause();
    	finish();
    	System.exit(0);
    }
    
}
