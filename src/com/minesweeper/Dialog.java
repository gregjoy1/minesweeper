package com.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

/**This class makes a dialog with an ok and cancel button
 * 
 * @author Greg Joy
 *
 */
public class Dialog {

	/*Declares x coordinate to position the dialog
	 *Declares y coordinate to position the dialog 
	 *Declares the width of the dialog
	 *Declares the height of the dialog
	 *Declares the cancel and ok buttons
	 *Declares the show boolean to determine whether the dialog is showing
	 *Declares the Paint classes responsible for drawing the lines and fill
	 */
	int x, y, width, height;
	String message;
	Button dialogCancel, dialogOk;
	boolean show = false;
	Paint Line, Fill;
	
	/**Dialog class contructor
	 * 
	 * @param x x axis position
	 * @param y y axis position
	 * @param width dialog width
	 * @param height dialog height
	 * @param message message to be displayed by the dialog
	 * @param line the rgb int array containing the color for the lines
	 * @param fill the rgb int array containing the color for the fill
	 * @param click the rgb int array containing the color for the button in its click state
	 */
	public Dialog(int x, int y, int width, int height, String message, int[] line, int[] fill, int[] click){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.message = message;
		//Initialises the buttons with the parameters specified
		dialogCancel = new Button(x+(width/5), y+((height/5)*3), width/5, height/5, "Cancel", line, fill, click, false);
		dialogOk = new Button(x+((width/5)*3), y+((height/5)*3), width/5, height/5, "OK", line, fill, click, false);
		Line = new Paint();
		Fill = new Paint();
		//Applies the rgb int arrays to the appropriate paint classes
		Line.setColor(Color.rgb(line[0], line[1], line[2]));
		Fill.setColor(Color.rgb(fill[0], fill[1], fill[2]));
	}
	
	/**Handles the input for the dialog ok button
	 * 
	 * @param event the MotionEvent class holding the relevent input information
	 * @return true if the button is pressed
	 */
	public boolean inputParserOK(MotionEvent event){
		boolean out = false;
		if(show){
			if((int) event.getX()>x&&(int) event.getX() <x+width&&(int) event.getY()<y&&(int) event.getY()>y+height){
				if(dialogOk.inputParser(event)){
					show = false;
					out = true;
				}
			}
		}
		else
			out = false;
		
		return out;
	}

	/**Handles the input for the dialog cancel button
	 * 
	 * @param event the MotionEvent class holding the relevent input information
	 * @return true if the button is pressed
	 */
	public boolean inputParserCancel(MotionEvent event){
		boolean out = false;
		if(show){
			if((int) event.getX()>x&&(int) event.getX() <x+width&&(int) event.getY()<y&&(int) event.getY()>y+height){
				if(dialogCancel.inputParser(event)){
					show = false;
					out = true;
				}
			}
		}
		else
			out = false;
		
		return out;
	}
	
	/**Method for showing the dialog
	 * 
	 */
	public void show(){
		show = true;
	}
		
	/**Method responsible for rendering the Dialog
	 * 
	 * @param canvas The appropriate instance of canvas
	 */
	public void render(Canvas canvas){
		//only renders if the dialog is showing
		if(show){
			canvas.drawRect(x, y, x+width, y+height, Line);
			canvas.drawRect(x+1, y+1, x+width-1, y+height-1, Fill);
			dialogCancel.render(canvas);
			dialogOk.render(canvas);
			canvas.drawText(message, x+(width/5), y+(height/5), Line);
		}
	}
}
