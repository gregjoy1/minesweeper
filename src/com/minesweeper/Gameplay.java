package com.minesweeper;

import android.view.MotionEvent;
import android.view.View;
import android.content.Context;
import android.graphics.Canvas;

/**This is the gameplay class, it handles the root elements of the game
 * i.e. the input, drawing, initialisation etc.
 * 
 * @author Greg Joy
 *
 */

public class Gameplay extends View{

	
	public GameActivityStart game;
	/*Declares the ready boolean to determine when the adequete information has been established
	 *Also declaredc is the invalidatorloop boolean that maintains the loop in the invalidator thread. */
	boolean ready=false,invalidatorLoop=true;
	//Declare instance of the Game class.
	Game gameContainer;
	int width, height, difficulty;
	double mineDensity;
	
	/**The constructer for the GamePlay class, this is responsible for finalising
	 * the transition between the GameActivityStart intent to the actual game Class.
	 * 
	 * @param context The current app context
	 * @param difficulty The game difficulty
	 * @param mineDensity The game minedensity
	 */
	public Gameplay(Context context, int difficulty, double mineDensity){
		super(context);
		game = (GameActivityStart) context;
		this.difficulty = difficulty;
		this.mineDensity = mineDensity;
		//Handles the focusing for the gameplay class
		setFocusable(true);
		setFocusableInTouchMode(true);
		gameContainer = new Game();
	}
	
	/**View's abstract render method
	 * 
	 * @param canvas This is the instance of the canvas class
	 */
	@Override
	public void onDraw(Canvas canvas){
		//Gathers important information for the game to render properly. i.e. screensizes etc
		if(!ready){
			width = canvas.getWidth();
			height = canvas.getHeight();
			//initialises the Game class with the information obtained.
			gameContainer.init(width, height, difficulty, mineDensity, getContext());
			//sets the ready variable to true, so the screen size isnt queried everytime
			ready = true;
			//Starts the invalidator thread
			invalidateUpdate.start();
		}
		//passes the instance of canvas down to all the child classes.
		gameContainer.render(canvas);
	}
	
	/**Views abstract input handler method
	 * 
	 * @param event This is the instance of the MotionEvent class
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event){
		//Passes the MotionEvent class down to the relevent child classes
		gameContainer.inputHandler(event);
		//Refreshes the display everytime the user interfaces with the app
		invalidate();
		return true;
		
	}
	
	/**This is the timer thread, it also invalidates the display everytime it
	 * updates the time count.
	 * 
	 */
	Thread invalidateUpdate = new Thread(){

		@Override
		public void run(){
			while(invalidatorLoop){
				try {
					//Checks to see if the user has started interfacing with the game and starts count
					if(gameContainer.UI.gameStarted){
						gameContainer.UI.count++;
						//This refreshes the display
						postInvalidate();
					}
					//Sleeps for a second
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					stop();
				}
			}
		}
	};
}
