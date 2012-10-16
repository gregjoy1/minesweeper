package com.minesweeper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/**
 * This class holds and handles the UI elements i.e. buttons, grids, dialogs and
 * handles the input associated with them.
 * 
 * @author Greg Joy
 * 
 */
public class Userinterface {

	// Declares the gameStarted boolean
	boolean gameStarted = false;
	/*
	 * Declares the x and y values for basepoint positioning ScreenWidth and
	 * screenheight for scaling zoomLevel to determine the level of zoom count
	 * to determine the time elapsed difficulty to determine the grid size
	 */
	int x, y, screenWidth, screenHeight, zoomLevel = 1, count = 0, difficulty;
	double mineDensity;
	// This a crude way to declare RGB colours in a integer array
	int[] line = { 0, 0, 0 }, fill = { 183, 183, 183 },
			click = { 121, 188, 114 };
	// Declaration of the button classes
	Button buttonZoomUp, buttonZoomDown, buttonPan, buttonFlag, buttonPress,
			buttonReset;
	// Declaration of the panel classes
	Panel panelLeft, panelRight, panelTop;
	// Declaration of the Grid classes
	Grid grid;
	// The app context
	Context context;
	// Declaration of the paint class that handles drawing the count timer
	Paint text = new Paint();
	// Declaration of the dialog class
	Dialog resetConfirm;
	// Declaration of an array of the Bitmap class that contains the relevent
	// smileys
	Bitmap[] smileys = new Bitmap[2];

	/**The class constructor to initialise the UI class
	 * 
	 * @param x The x coord to position the UI
	 * @param y The y coord to position the UI
	 * @param screenWidth The screen width
	 * @param screenHeight The screen height
	 * @param difficulty The game difficulty
	 * @param mineDensity The game mineDensity
	 * @param context The apps context
	 */

	public Userinterface(int x, int y, int screenWidth, int screenHeight,
			int difficulty, double mineDensity, Context context) {
		this.x = x;
		this.y = y;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.difficulty = difficulty;
		this.mineDensity = mineDensity;
		this.context = context;
		init();
	}

	/**The Initialisation method, this initialises the class
	 * 
	 */
	
	public void init() {
		//This loads the smileys into the smileys array to be displayed appropriately
		smileys[0] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.smileyhappy);
		smileys[1] = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.smileysad);

		//This sets up the reset confirmation dialog
		resetConfirm = new Dialog(screenWidth / 8, screenHeight / 4,
				screenWidth - ((screenWidth / 8) * 2), screenHeight / 2,
				"Please confirm resetting the game.", line, fill, click);

		//This sets the color to the text paint to black and the size to 1/16th of the screenheight
		text.setColor(Color.BLACK);
		text.setTextSize(screenHeight / 16);

		//calls the initialisegrid method
		initialiseGrid();
		
		//initialises the panel on the left of the screen
		panelLeft = new Panel(0, screenHeight / 8, screenWidth / 8,
				screenHeight - (screenHeight / 8), fill);
		//initialises the panel on the right of the screen
		panelRight = new Panel(screenWidth - (screenWidth / 8),
				screenHeight / 8, screenWidth / 8, screenHeight
						- (screenHeight / 8), fill);
		//initialises the panel on the top of the screen
		panelTop = new Panel(0, 0, screenWidth, screenHeight / 8, fill);

		//initialises the button for pressing on tiles
		buttonPress = new Button(5, screenHeight / 8, (screenWidth / 8) - 10,
				(screenHeight / 3) - screenHeight / 8, "Press", line, fill,
				click, true);
		//initialises the button for panning the grid
		buttonPan = new Button(5, screenHeight / 8 + (screenHeight / 3)
				- screenHeight / 8, (screenWidth / 8) - 10, (screenHeight / 3)
				- screenHeight / 8, "Pan", line, fill, click, true);
		//Initialises the button for putting flags on the grid
		buttonFlag = new Button(
				5,
				(screenHeight / 8 + ((screenHeight / 3) - screenHeight / 8) * 2),
				(screenWidth / 8) - 10, (screenHeight / 3) - screenHeight / 8,
				"Flag", line, fill, click, true);
		//Initialises the button for resetting the game
		buttonReset = new Button((screenWidth / 8) * 7 + 5, screenHeight / 8,
				(screenWidth / 8) - 10, (screenHeight / 3) - screenHeight / 8,
				"Reset", line, fill, click, true);
		//Initialises the button for zooming out of the grid
		buttonZoomUp = new Button((screenWidth / 8) * 7 + 5, screenHeight / 8
				+ (screenHeight / 3) - screenHeight / 8,
				(screenWidth / 8) - 10, (screenHeight / 3) - screenHeight / 8,
				"Zoom out", line, fill, click, false);
		//Initialises the button for zooming into the grid
		buttonZoomDown = new Button(
				(screenWidth / 8) * 7 + 5,
				(screenHeight / 8 + ((screenHeight / 3) - screenHeight / 8) * 2),
				(screenWidth / 8) - 10, (screenHeight / 3) - screenHeight / 8,
				"Zoom in", line, fill, click, false);
	}

	/**This method initialises and sets up the grid
	 * 
	 */
	public void initialiseGrid() {
		grid = new Grid(screenWidth / 8, screenHeight / 8, screenWidth
				- ((screenWidth / 8) * 2), difficulty, screenWidth,
				screenHeight, mineDensity);
		grid.initialise();
		grid.zoom(zoomLevel);
	}

	/**This method handles all the input in regards to the UI and passes
	 * down the appropriate instance of event to all the child classes
	 * 
	 * @param event The MotionEvent class that carries all the input information
	 */
	public void inputParser(MotionEvent event) {

		//Checks if the users finger is down
		if (event.getAction() == MotionEvent.ACTION_DOWN) {

			
			/*This groups the press, pan, and flag buttons together
			 * and in effect turns them into radio buttons.
			 * It also turns gameStarted boolean to true so
			 * the timer thread can start
			 */
			if (buttonFlag.inputParser(event)) {
				buttonPan.toggle(false);
				buttonPress.toggle(false);
				if (!gameStarted)
					gameStarted = true;
			} else if (buttonPan.inputParser(event)) {
				buttonPress.toggle(false);
				buttonFlag.toggle(false);
				if (!gameStarted)
					gameStarted = true;
			} else if (buttonPress.inputParser(event)) {
				buttonFlag.toggle(false);
				buttonPan.toggle(false);
				if (!gameStarted)
					gameStarted = true;
			}

			//Checks if the zoomout button was pressed and calls the zoomOut() method
			if (buttonZoomDown.inputParser(event))
				zoomOut();
			//Checks if the zoomin button was pressed and calls the zoomIn() method
			if (buttonZoomUp.inputParser(event))
				zoomIn();
			//Checks if the reset button was pressed and shows the reset confirmation dialog
			if (buttonReset.inputParser(event)) {
				resetConfirm.show();
			}

		//Checks to see if the user has lifted their finger
		} else if (event.getAction() == MotionEvent.ACTION_UP) {

			//Sends the motion event to the all the appropriate button 
			//classes to respond to the user lifting their finger
			buttonZoomDown.inputParser(event);
			buttonZoomUp.inputParser(event);
			buttonReset.inputParser(event);

			resetConfirm.dialogOk.inputParser(event);
			resetConfirm.dialogCancel.inputParser(event);
		}
		//If the reset dialog is showing it pauses the game and stops input 
		//to the grid to avoid unwanted input
		if (resetConfirm.show) {

			buttonFlag.toggle(false);
			buttonPress.toggle(false);
			buttonPan.toggle(false);
			gameStarted = false;

			//If the user presses the ok button in the dialog then reinitialise the grid
			if (resetConfirm.dialogOk.inputParser(event)) {
				resetConfirm.show = false;
				initialiseGrid();
				count = 0;
			//If the user presses the cancel button in the dialog then close the dialog
			} else if (resetConfirm.dialogCancel.inputParser(event))
				resetConfirm.show = false;
		//If the dialog isnt showing then the motion event is sent to the grid class to be handled
		} else {
			grid.inputParser(event, buttonPan.isEnabled(),
					buttonPress.isEnabled(), buttonFlag.isEnabled());
			if (!gameStarted)
				gameStarted = true;
		}
	}

	/**This method renders the UI by passing the appropriate instance of canvas to all
	 * its child classes.
	 * 
	 * @param canvas The instance of canvas class to be redistributed
	 */
	public void render(Canvas canvas) {
		grid.render(canvas);
		panelLeft.render(canvas);
		panelRight.render(canvas);
		panelTop.render(canvas);
		buttonPress.render(canvas);
		buttonPan.render(canvas);
		buttonFlag.render(canvas);
		buttonZoomUp.render(canvas);
		buttonZoomDown.render(canvas);
		buttonReset.render(canvas);
		resetConfirm.render(canvas);
		
		//Draws the timer in the top left
		canvas.drawText("Time taken:" + this.count, 10,
				(screenHeight / 32) * 3, text);
		//Checks if the game is lost and displays a sad smiley
		if (grid.lost)
			canvas.drawBitmap(smileys[1], screenWidth / 2, 10, null);
		//Else if game isnt lost then show smiley smiley
		else
			canvas.drawBitmap(smileys[0], screenWidth / 2, 10, null);
	}

	/**This method handles zooming in*/
	public void zoomIn() {
		if (zoomLevel != 1) {
			zoomLevel--;
			grid.zoom(zoomLevel);
		}
	}

	/**This method handles zooming out*/
	public void zoomOut() {
		if (zoomLevel != 5) {
			zoomLevel++;
			grid.zoom(zoomLevel);
		}
	}

}
