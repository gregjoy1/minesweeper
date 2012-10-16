package com.minesweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class Game {

	/**declaration of the width, height, minedensity, and difficulty variables*/
	int width, height, difficulty;
	double mineDensity;
	//Declarion of an instance of the UI class
	Userinterface UI;
	
	
	public Game(){
		
	}
	
	/**This is the initialisation method
	 *  for the game, it handles all the game parameters
	 * like the width, height, difficulty, and minedensity
	 * 
	 * @param width The screen width
	 * @param height The screen height
	 * @param difficulty The game difficulty
	 * @param mineDensity The game mine density
	 * @param context The game context
	 */
	public void init(int width, int height, int difficulty, double mineDensity, Context context){
		this.width = width;
		this.height = height;
		UI = new Userinterface(0,0,width,height, difficulty, mineDensity, context);
	}
	
	/**This is the render method that passes the instance of canvas down to the appropriate child classes
	 * 
	 * @param canvas The instance of canvas to be utilised by the child classes.
	 */
	public void render(Canvas canvas){
		UI.render(canvas);
	}
	
	/**This is the inputhandler method, it passes the MotionEvent down to the child classes to be parsed appropriately.
	 * 
	 * @param event The instance of MotionEvent that holds all the input information
	 */
	public void inputHandler(MotionEvent event){
		
		UI.inputParser(event);

	}
}
