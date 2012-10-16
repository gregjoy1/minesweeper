package com.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

/**This class makes a user defined button
 * 
 * @author Greg Joy
 *
 */

public class Button {

	/*Declaration of the x position coordinate
	 *Declaration of the y position coordinate
	 *Declaration of the button width
	 *Declaration of the button height
	 *
	 *Declaration of all the paint classes responsible 
	 *for rendering the line, fill, buttong click state fill
	 *
	 *The string of text displayed on the button
	 *
	 *booleans for whether the button is a toggle button, is enabled, is down
	 */
	int x,y,width,height;
	int[] line,fill,click;
	Paint paintLine,paintFill,paintClick,temp;
	String text;
	boolean toggle,enabled,down;
	
	/**The button class constructor
	 * 
	 * @param x x position coordinate
	 * @param y y position coordinate
	 * @param width button width
	 * @param height button height
	 * @param text button text
	 * @param line button line color
	 * @param fill button fill color
	 * @param click button onclick color
	 * @param toggle whether the button is a toggle button or a standard button
	 */
	public Button(int x, int y, int width, int height, String text, int[] line, int[] fill, int[] click, boolean toggle){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.line = line;
		this.fill = fill;
		this.click = click;
		this.toggle = toggle;
		
		paintLine = new Paint();
		paintLine.setColor(Color.rgb(line[0], line[1], line[2]));
		
		paintFill = new Paint();
		paintFill.setColor(Color.rgb(fill[0], fill[1], fill[2]));
		
		paintClick = new Paint();
		paintClick.setColor(Color.rgb(click[0], click[1], click[2]));
	}

	/**Method responsible for parsing the input in relation to the button
	 * 
	 * @param me MotionEvent argument that carries all the input information
	 * @return return true if button is pressed
	 */
	public boolean inputParser(MotionEvent me){
		if((int) me.getX()>=this.x&&(int) me.getX()<=this.x+width&&(int) me.getY()>=this.y&&(int) me.getY()<=this.y+height){
			//calls the toggleHandler method to handle the toggle action of the button
			toggleHandler(me);
			Log.d("Button", "Pressed");
			return true;
		}
		else{
			
			return false;
		}
	}
	
	/**Method for setting the toggle
	 * 
	 * @param bool the boolean value for toggle
	 */
	public void toggle(boolean bool){
		
		enabled = bool;
		down = bool;
	}
	
	/**Method that handles the toggle action of the button
	 * 
	 * @param me MotionEvent object containing all the input information
	 */
	private void toggleHandler(MotionEvent me){
		//if a toggle button, toggles the button
		if(toggle){
			if(enabled)
				enabled = false;
			else
				enabled = true;
			down = enabled;
		}
		//if a normal button, then inherit standard button functionality
		else{
			if(me.getAction()==MotionEvent.ACTION_DOWN)
				down = true;
			else if(me.getAction()==MotionEvent.ACTION_UP)
				down = false;
			else
				down = false;
			
		}
	}
	
	/**Method for determining that a button is enabled
	 * 
	 * @return true if enabled, false is not
	 */
	public boolean isEnabled(){
		return enabled;
	}
	
	/**Render method responsible for Rendering the button
	 * 
	 * @param canvas The instance of Canvas for rendering
	 */
	public void render(Canvas canvas){
		
		//if the user has their finger down on the button, change the color
		if(down)
			temp = paintClick;
		else
			temp = paintFill;
		
		canvas.drawRect(x, y, x+width, y+height, paintLine);
		canvas.drawRect(x+1, y+1, x+width-1, y+height-1, temp);
		canvas.drawText(text, x+(width/3), y+(height/2), paintLine);
	}
}
