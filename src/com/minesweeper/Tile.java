package com.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

/**
 * The class handles the individual tiles in the grid
 * 
 * @author Greg Joy
 * 
 */
public class Tile {

	/*
	 * Declaration of the x and y coordinates of the tile Declaration of the x,
	 * y movement increments of the tiles. Declaration of the tile width and
	 * height, type and the text size variables
	 */
	int x, y, xOld, yOld, xIncOld, yIncOld, width, height, type = 0, textSize;
	String text = "";
	/*
	 * Boolean declarations for whether tile is exposed, is selected, is
	 * scrolling (panning), is a mine or flaggedAlso declared are the paint
	 * instances.
	 */
	boolean exposed, isSelected, scroll, mine = false, flag = false;
	Paint line, fill, selected, red, blue, green, exposedFill;

	/**
	 * Tile constructor
	 * 
	 * @param x
	 *            x axis tile position
	 * @param y
	 *            y axis tile position
	 * @param width
	 *            tile width
	 * @param height
	 *            tile height
	 */
	public Tile(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		setType(type);
		exposed = false;
		isSelected = false;
		line = new Paint();
		line.setColor(Color.BLACK);
		fill = new Paint();
		fill.setColor(Color.LTGRAY);
		exposedFill = new Paint();
		exposedFill.setColor(Color.WHITE);
		red = new Paint();
		red.setColor(Color.RED);
		red.setFakeBoldText(true);
		red.setAntiAlias(true);
		blue = new Paint();
		blue.setColor(Color.BLUE);
		blue.setFakeBoldText(true);
		blue.setAntiAlias(true);
		green = new Paint();
		green.setColor(Color.GREEN);
		green.setFakeBoldText(true);
		green.setAntiAlias(true);
		selected = new Paint();
		selected.setColor(Color.BLUE);
		textSize = (int) (line.getTextSize());
		Log.d("text", "" + textSize + " " + width);
	}

	/**
	 * Method determining whether tile is exposed
	 * 
	 * @return true if exposed
	 */
	public boolean getExposed() {
		return exposed;
	}

	/**
	 * Method exposes the tile
	 * 
	 */
	public void expose() {
		exposed = true;
	}

	/**
	 * This method handles the selection of the tile
	 * 
	 * @param select
	 *            if the selection is to select
	 * @param flag
	 *            if the selection is to flag
	 */
	public void select(boolean select, boolean flag) {
		this.isSelected = select;
		if (select)
			exposed = true;
		// toggles the flag on and off
		else if (flag)
			if (this.flag)
				setFlag(false);
			else
				setFlag(true);
	}

	/**
	 * Method determining whether tile is a mine
	 * 
	 * @return true if mine, false if isnt
	 */
	public boolean isMine() {
		if (mine)
			return true;
		else
			return false;
	}

	/**
	 * Method to resize the tile
	 * 
	 * @param width
	 *            the new width
	 * @param height
	 *            the new height
	 */
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Method to move the tile
	 * 
	 * @param x
	 *            increment movement by on x axis
	 * @param y
	 *            increment movement by on y axis
	 */
	public void move(int x, int y) {
		// Checks if the users finger is moving and moves tile correctly
		if (x != xIncOld && scroll)
			this.x = xOld + x;
		if (y != yIncOld && scroll)
			this.y = yOld + y;
	}

	/**
	 * Method to toggle the panning of the tile
	 * 
	 * @param toggle
	 *            toggle panning on or off
	 */
	public void setPan(boolean toggle) {
		if (toggle) {
			xOld = x;
			yOld = y;
		}
		scroll = toggle;
	}

	/**
	 * Method to set the type of tile
	 * 
	 * @param type the tile integer id
	 */
	public void setType(int type) {
		
		//Handles all the parameters in accordance to what type of tile it is
		this.type = type;
		if (type > 0 && type <= 8)
			text = String.valueOf(type);
		else if (type == 9) {
			text = "mine";
			mine = true;
		} else if (type == 10)
			text = "flag";
		else
			type = 0;

	}

	/**Method to return the type of tile
	 * 
	 * @return number value of tile type
	 */
	public int getType() {
		return this.type;
	}

	/**Method to set the tile flagged
	 * 
	 * @param toggle flag toggle boolean
	 */
	public void setFlag(boolean toggle) {
		flag = toggle;
	}

	/**Method responsible for rendering the tile
	 * 
	 * @param canvas instance of canvas to render with
	 */
	public void render(Canvas canvas) {
		Paint temp = new Paint();
		Paint temp2 = new Paint();
		temp2 = fill;
		//If the tile is selected then change the color
		if (isSelected){
			temp = selected;
			temp2 = exposedFill;
		}
		//handles the tile color if it is exposed		
		else if(!isSelected&&exposed)
			temp2 = exposedFill;
		//handles the flag color is the tile is flagged
		else if(flag)
			temp2 = exposedFill;
		else
			temp = line;

		
		
		canvas.drawRect(x, y, x + width, y + height, temp);
		canvas.drawRect(x + 1, y + 1, (x + width) - 1, (y + height) - 1, temp2);
		if(exposed){
			//if the tile is a mine then draw a red dot in the center of the tile
			if(mine)
				canvas.drawCircle((float) x+(width/2),(float) y+(height/2),(float) (width/4), red);
			//sets the text color of numbered tile 1 blue
			else if(!flag&&type==1)
				canvas.drawText(text, x+(width/2), y + (height / 2), blue);
			//sets the text color of numbered tile 2 green
			else if(!flag&&type==2)
				canvas.drawText(text, x+(width/2), y + (height / 2), green);
			//sets the text color of numbered tile 3 red			
			else if(!flag&&type==3)
				canvas.drawText(text, x+(width/2), y + (height / 2), red);
			//draws a red rectangle in the centre of the tile if it is a flag		
			else if(type!=0&&flag)
				canvas.drawRect(x+(width/8), y+(height/4), (x+width)-(width/8), (y+height)-(height/4), red);
		}
		//draws a red rectangle in the centre of the tile if it is a flag
		else if(!exposed&&flag)
			canvas.drawRect(x+(width/8), y+(height/4), (x+width)-(width/8), (y+height)-(height/4), red);
	}

	/**This method resets the tile to specified parameters
	 * 
	 * @param x the specified x coordinate to reset the tile too
	 * @param y the specified y coordinate to reset the tile too
	 * @param width the specified width to reset the tile too
	 * @param height the specified height to reset the tile too
	 */
	public void reset(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textSize = width / 3;
		green.setTextSize((float) textSize);
		blue.setTextSize((float) textSize);
		red.setTextSize((float) textSize);
	}

	/**This method checks if the specified x, y coordinates are within the tile bounds
	 * 
	 * @param x the specified x coordinate
	 * @param y the specified y coordinate
	 * @return whether the tile is in the specified bounds
	 */
	public boolean isSelected(int x, int y) {
		if (x > this.x && x < this.x + this.width && y > this.y
				&& y < this.y + this.height) {
			return true;
		} else {
			return false;
		}
	}
}