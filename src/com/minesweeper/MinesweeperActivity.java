package com.minesweeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

public class MinesweeperActivity extends Activity {
	
	/**This Is the Main activity class, this handles the menu and all the variables being passed to the game intent.
	 *
	 *@author Gregory Joy 
	 *@Version 1.0
	 *
	 */
	
	Button startGameButton, quitGameButton;
	RadioButton difficultyEasyRadio, difficultyMediumRadio, difficultyHardRadio;
	Spinner mineDensityPicker;

	ArrayAdapter<String> spinnerAdapter;
	SpinnerItemSelectListener spinnerListener;
	Intent gameStart;
	
	int difficulty = 0;
	double mineDensity = 0.10;

	//String array declaration for the spinner
	String[] mineDensityArray = {"10% Mine Density", "20% Mine Density", "30% Mine Density", "40% Mine Density", "50% Mine Density", "60% Mine Density", "70% Mine Density"};
	
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Declares main game intent
        gameStart = new Intent(this, GameActivityStart.class);

        //Declares and initialises all the proprietary controls
    	startGameButton = (Button) findViewById(R.id.startgameButton);
    	quitGameButton = (Button) findViewById(R.id.exitgameButton);
    	
    	difficultyEasyRadio = (RadioButton) findViewById(R.id.difficultyeasy);
    	difficultyMediumRadio = (RadioButton) findViewById(R.id.difficultymedium);
    	difficultyHardRadio = (RadioButton) findViewById(R.id.difficultyhard);

    	//Sets the default option to easy    	
    	difficultyEasyRadio.setChecked(true);

    	//Initialises the arrayAdaptor for the spinner
    	spinnerAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, mineDensityArray);
    	
    	mineDensityPicker = (Spinner) findViewById(R.id.minedensity);	
    	mineDensityPicker.setAdapter(spinnerAdapter);
    	mineDensityPicker.setOnItemSelectedListener(spinnerListener = new SpinnerItemSelectListener());
    	
    	startGameButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				/*
				 * starts the game with the difficulty selected and the mine
				 * density gained from the SpinnerItemSelectListener class
				 */
				startGame(difficulty,spinnerListener.getResult());
			}
    		
    	});
    	
    	quitGameButton.setOnClickListener(new OnClickListener(){
    		
			@Override
			public void onClick(View v) {
				//Calls the garbage collector and closes program
				finish();
				System.exit(0);
			}
    		
    	});
    	
    	difficultyEasyRadio.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setDifficulty(0);
			}
    		
    	});
    	
    	difficultyMediumRadio.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setDifficulty(1);
			}
    		
    	});
    	
    	difficultyHardRadio.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				setDifficulty(2);
			}
    		
    	});
    }
    
	/**This method parses the difficulty provided and outputs the appropriate toast message.
	 * 
	 * @param difficulty The difficulty integer 0-easy 1-medium 2-hard
	 */
    public void setDifficulty(int difficulty){
    	
    	String message = "Difficulty set to ";

		this.difficulty = difficulty;
    	
    	if(difficulty==0)
    		message = message + "easy.";
    	else if(difficulty==1)
    		message = message + "medium.";
    	else if(difficulty==2)
    		message = message + "hard.";
    	
    	Toast toast = Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT);
    	toast.show();
    }
    
    /**Starts the game intent and loads all the addition information into it
     * 
     * @param difficulty The difficulty variable being passed to the game intent
     * @param minedensity The mine desnity variable being passed to the game intent
     */
    public void startGame(int difficulty, double minedensity){
    	gameStart.putExtra("difficulty", difficulty);
    	gameStart.putExtra("minedensity", minedensity);
    	startActivityForResult(gameStart, 1);
    	finish();
    }
    
    
}