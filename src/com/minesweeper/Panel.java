package com.minesweeper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**A simple class that creates a basic panel
 * 
 * @author Greg Joy
 *
 */
public class Panel {

	/*Standard declaration of the x, y, width, and height of the panel
	 * Also consisting of colours
	 */
	int x,y,width,height;
	int[] fill;
	Paint paintFill;
	
	/**This is the class constructor that constructs the panel
	 * 
	 * @param x the x position of the panel
	 * @param y the y position of the panel
	 * @param width the width of the panel
	 * @param height the height of the panel
	 * @param fill the fill color of the panle
	 */
	public Panel(int x, int y, int width, int height, int[] fill){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.fill = fill;
		paintFill = new Paint();
		paintFill.setColor(Color.rgb(fill[0], fill[1], fill[2]));
	}
	
	/**The render method
	 * 
	 * @param canvas The instance of canvas to render with
	 */
	public void render(Canvas canvas){
		canvas.drawRect(x, y, x+width, y+height, paintFill);
	}
}
