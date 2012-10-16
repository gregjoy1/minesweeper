package com.minesweeper;

import java.util.Random;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;

/**Class responsible for creating a grid
 * 
 * @author Greg Joy
 *
 */
public class Grid {

	/*Declaration of x position variable and the starting x position
	 *Declaration of y position variable and the starting y position
	 *Declaration of the number of tiles wide and high of the grid
	 *Declaration of the difficulty, width and height per tile
	 *Also the screen width and height for scaling purposes
	 */
	int x, y, startX, startY, width, height, difficulty, sizeWidth, cellWidth,
			cellHeight, screenWidth, screenHeight, mineCount = 0;
	//Declaration of a 2d array of tile objects
	Tile[][] grid;
	/*boolean ready for ensuring the initialisation process is finished before
	 *starting functionality - to stop potential null pointer errors
	 * boolean firstTime is for indicating first input for generating the game
	 * refresh boolean is for successfully handling user input in terms of panning
	 * lost boolean indicates whether the game is lost
	 */
	boolean ready = false, firstTime = true;
	boolean refresh = true, lost;
	//all coord variables for ensuring the grid is correctly panning
	int x1 = 0, x2, y1 = 0, y2, x0 = x, y0 = y;

	/**Grid class constructer
	 * 
	 * @param x the x position coordinate
	 * @param y the y position coordinate
	 * @param sizeWidth the width size coordinate
	 * @param difficulty the game difficulty
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 * @param mineDensity the games mine density
	 */
	public Grid(int x, int y, int sizeWidth, int difficulty, int screenWidth,
			int screenHeight, double mineDensity) {
		this.x = x;
		this.y = y;
		this.startX = x;
		this.startY = y;
		this.difficulty = difficulty;
		this.sizeWidth = sizeWidth;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		lost = false;
		
		//This determines the grid size in regards to game difficulty
		switch (difficulty) {
		case 0:
			this.width = 16;
			this.height = 16;
			break;
		case 1:
			this.width = 24;
			this.height = 24;
			break;
		case 2:
			this.width = 32;
			this.height = 32;
			break;
		default:
			this.width = 16;
			this.height = 16;
			break;
		}
		
		//this works out what proportion of the tiles are mines
		mineCount = (int) ((height * width) * mineDensity);

		//this determines the height and width of each cell
		cellWidth = sizeWidth / width;
		cellHeight = sizeWidth / height;
		
		//initialises the tile object array in regards to the difficulty
		grid = new Tile[height][width];
	}

	/**This method initialises the grid and constructs each tile object in the array
	 * 
	 */
	public void initialise() {

		for (int yInc = 0; yInc < height; yInc++) {
			for (int xInc = 0; xInc < width; xInc++) {
				//purely initialising the tiles as blanks
				grid[yInc][xInc] = new Tile(x + (xInc * cellWidth), y
						+ (yInc * cellHeight), cellWidth, cellHeight);
			}
		}
		//Sets ready to true to indicate that the array has been set up
		ready = true;
		Log.d("Grid", "ready ");
	}

	/**This method handles the input
	 * 
	 * @param event The MotionEvent object containing the input details
	 * @param pan Boolean indicating that the grid is responding to panning
	 * @param press Boolean indicating that the grid is responding to pressing
	 * @param flag Boolean indicating that the grid is responding to flagging
	 */
	
	public void inputParser(MotionEvent event, boolean pan, boolean press,
			boolean flag) {

		//Determines whether input is relevent to the grid and is panning
		if (pan && (int) event.getX() >= (screenWidth / 8)
				&& (int) event.getX() <= (screenWidth - (screenWidth / 8))
				&& (int) event.getY() >= (screenWidth / 8)
				&& (int) event.getY() < screenHeight) {
			Log.d("Grid", "DOWN");

			//stores the grid position when the user first touches the grid
			if (this.refresh) {
				x1 = (int) event.getX();
				y1 = (int) event.getY();
				setPan(true);
				Log.d("should only show once 2", "yes " + x1 + " " + y1);
				refresh = false;
			}

			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				//stores the grid position when the user first touches the grid
				if (this.refresh) {
					x1 = (int) event.getX();
					y1 = (int) event.getY();
					setPan(true);
					Log.d("should only show once 2", "yes " + x1 + " " + y1);
					refresh = false;}
				break;
					
			//if the user moves their finger
			case MotionEvent.ACTION_MOVE:
				x2 = (int) event.getX();
				y2 = (int) event.getY();
				//calls move method with the differences in positions
				move(x2 - x1, y2 - y1);
				break;
			
			//When the user lifts their finger
			case MotionEvent.ACTION_UP:
				//Sets refresh to true
				refresh = true;
				//stops panning
				setPan(false);
				Log.d("Grid", "refresh");
				break;
			}
		}
		//This if block handles the user pressing on the grid
		else if (press && (int) event.getX() >= (screenWidth / 8)
				&& (int) event.getX() <= (screenWidth - (screenWidth / 8))
				&& (int) event.getY() >= (screenWidth / 8)
				&& (int) event.getY() < screenHeight) {
			/*Checks if its a first time so it can generate the grid and ensure 
			 *the user doesnt hit a mine on the first go. It also clears alot of 
			 *the blank spaces.
			 */
			if (firstTime) {
				int[] coords = select((int) event.getX(), (int) event.getY(),false);
				generate(width, height, coords[0], coords[1]);
				checkSurround(coords[0], coords[1]);
				Log.d("gen", "done");
				firstTime = false;
			}
			//gets the array coordinates of the tile the user pressed on
			int[] coords = select((int) event.getX(), (int) event.getY(),false);
			//utilises those coordinates to check if the tile is a mine 
			if(grid[coords[1]][coords[0]].isMine())
				this.gameLost();
			//Clears all the blank tiles
			else if(grid[coords[1]][coords[0]].getType()==0)
				checkSurround(coords[0], coords[1]);
		}
		
		//This if block handles the flagging input
		else if (flag && (int) event.getX() >= (screenWidth / 8)
				&& (int) event.getX() <= (screenWidth - (screenWidth / 8))
				&& (int) event.getY() >= (screenWidth / 8)
				&& (int) event.getY() < screenHeight) {
			if(!firstTime){
				if(refresh){
					refresh=false;
					select((int) event.getX(), (int) event.getY(),true);
					//Checks if all the mines are flagged and exposes all the tiles if true
					if(checkWin())
						gameLost();
				}
			}
		}
		//This prevents a constant loop of toggle on and off
		if(event.getAction()==MotionEvent.ACTION_UP){
			refresh=true;
			Log.d("refresh", "refrshed");
		}

	}

	/**Method for setting pan on all the tiles
	 * 
	 * @param toggle to toggle pan on or off
	 */
	public void setPan(boolean toggle) {
		for (int yInc = 0; yInc < height; yInc++) {
			for (int xInc = 0; xInc < width; xInc++) {
				grid[yInc][xInc].setPan(toggle);
			}
		}
	}

	/**This method is responsible for moving all the tiles
	 * 
	 * @param x x increment to move the grid
	 * @param y y increment to move the grid
	 */
	public void move(int x, int y) {
		this.x = grid[0][0].x;
		this.y = grid[0][0].y;
		//moves every tile in the array
		for (int yInc = 0; yInc < height; yInc++) {
			for (int xInc = 0; xInc < width; xInc++) {
				grid[yInc][xInc].move(x, y);
			}
		}
	}

	/**This method is responsible for rendering the grid
	 * 
	 * @param canvas The canvas instance 
	 */
	public void render(Canvas canvas) {
		//prevents null pointer
		if (ready) {
			for (int yInc = 0; yInc < height; yInc++) {
				for (int xInc = 0; xInc < width; xInc++) {
					grid[yInc][xInc].render(canvas);
				}
			}
		}
	}

	/**This method handles zooming the grid
	 * 
	 * @param zoom the zoom level to zoom by
	 */
	public void zoom(int zoom) {
		Log.d("zoom", "" + zoom);
		//Works out the adjusted tile size
		this.sizeWidth = ((screenWidth - (screenWidth / 8)) * zoom);

		cellWidth = sizeWidth / width;
		cellHeight = sizeWidth / height;

		for (int yInc = 0; yInc < height; yInc++) {
			for (int xInc = 0; xInc < width; xInc++) {
				//Resizes each tile
				grid[yInc][xInc].reset((x*zoom)+(xInc * cellWidth), (y*zoom)
						+ (yInc * cellHeight), cellWidth, cellHeight);
			}
		}
	}

	/**This method generates the game
	 * 
	 * @param xSize the width of the grid
	 * @param ySize the height of the grid
	 * @param xSel the x value of the tile being first selected
	 * @param ySel the y value of the tile being first selected
	 */
	public void generate(int xSize, int ySize, int xSel, int ySel) {
		int mineInc = 0, x, y;
		Random ran = new Random();

		//Randomly allocates the predefined number of mines to the grid
		while (mineInc < mineCount) {
			x = ran.nextInt(this.width);
			y = ran.nextInt(this.height);
			if ((x != xSel) && (y != ySel)) {
				grid[y][x].setType(9);
				mineInc++;
				Log.d("mine", "" + mineInc);
			}
		}
		//It then counts the tile for mines 
		for (y = 0; y < this.height; y++) {
			for (x = 0; x < this.width; x++) {
				//if the tile isnt a mine count it
				if (grid[y][x].getType()!=9) {								
					grid[y][x].setType(countMine(x,y));
				}
			}
		}
		

	}
	
	/**This method recursively checks the surrounding mines and exposes them if they
	 * are blank.
	 * 
	 * @param x the x value of count starting point
	 * @param y the y value of count starting point
	 */
	public void checkSurround(int x, int y){
		for(int yCheck=y-1;yCheck<=y+1;yCheck++){
			for(int xCheck=x-1;xCheck<=x+1;xCheck++){
				//Checks for array out of bounds error
				if(!(xCheck<0||xCheck>=width||yCheck<0||yCheck>=height||xCheck==x&&yCheck==y)){
					//ensures tile isnt exposed and checks
					if(grid[yCheck][xCheck].getType()==0&&grid[yCheck][xCheck].exposed==false){
						grid[yCheck][xCheck].expose();
						checkSurround(xCheck,yCheck);
					}
					//if the tile isnt a mine then expose it
					else if(grid[yCheck][xCheck].getType()!=9)
						grid[yCheck][xCheck].expose();
				}
			}
		}
	}
	
	/**This method counts the number of mines surrounding a tile
	 * 
	 * @param x the x increment in the tile array to be counted
	 * @param y the y increment in the tile array to be counted
	 * @return returns the number of mines surrounding the tile
	 */
	public int countMine(int x, int y){
		int count=0;
		
		if((x-1>=0)&&(y-1>=0)){
			if(grid[y-1][x-1].getType()==9){
				count++;
				Log.d("mine1","x "+x+" y "+y+" count "+count);
			}
		}
		if((x-1>=0)&&(y+1<=(height-1))){
			if(grid[y+1][x-1].getType()==9){
				count++;
				Log.d("mine2","x "+x+" y "+y+" count "+count);				
			}
		}
		if((x+1<=(width-1))&&(y-1>=0)){
			if(grid[y-1][x+1].getType()==9){
				count++;
				Log.d("mine3","x "+x+" y "+y+" count "+count);
			}
		}
		if((x+1<=(width-1))&&(y+1<=(height-1))){
			if(grid[y+1][x+1].getType()==9){
				count++;
				Log.d("mine4","x "+x+" y "+y+" count "+count);
			}
		}
		if(x-1>=0){
			if(grid[y][x-1].getType()==9){
				count++;
				Log.d("mine5","x "+x+" y "+y+" count "+count);
			}
		}
		if(x+1<=(width-1)){
			if(grid[y][x+1].getType()==9){
				count++;
				Log.d("mine6","x "+x+" y "+y+" count "+count);
			}
		}
		if(y-1>=0){
			if(grid[y-1][x].getType()==9){
				count++;
				Log.d("mine7","x "+x+" y "+y+" count "+count);
			}
		}
		if(y+1<=(height-1)){
			if(grid[y+1][x].getType()==9){
				count++;
				Log.d("mine8","x "+x+" y "+y+" count "+count);
			}
		}

		return count;
	}

	/**This method returns the tile position of the tile that has been selected
	 * 
	 * @param x the screen x coordinate to be checked
	 * @param y the screen y coordinate to be checked
	 * @param flag whether the selection is flagging or not
	 * @return returns an array of coordinates [0] = x [1] = y
	 */
	public int[] select(int x, int y, boolean flag) {
		int[] out = new int[2];
		if (x >= (screenWidth / 8) && x <= (screenWidth - (screenWidth / 8))
				&& y >= (screenWidth / 8) && y < screenHeight) {
			//Searches the array of tiles for the selected tile
			for (int yInc = 0; yInc < height; yInc++) {
				for (int xInc = 0; xInc < width; xInc++) {
					grid[yInc][xInc].isSelected = false;
					if (grid[yInc][xInc].isSelected(x, y)) {
						//This selects the tile as a flag
						if(flag)
							grid[yInc][xInc].select(false,true);
						//This selects the tile as a standard selection
						else
							grid[yInc][xInc].select(true,false);
						out[0] = xInc;
						out[1] = yInc;
					}
				}
			}
		}

		return out;
	}
	
	/**This method reveals all the tiles
	 * 
	 */
	public void gameLost(){
		for(int yInc=0; yInc<height;yInc++){
			for(int xInc=0; xInc<width;xInc++){
				grid[yInc][xInc].exposed = true;
			}
		}
		lost = true;
	}
	
	/**This method checks if the game has been won by checking all mines for flags
	 * 
	 * @return returns true is won, false is not
	 */
	public boolean checkWin(){
		int mineInt=0, winCount=0;
		boolean out=false;
		//Searches array for mine tiles
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++){
				//if the tile is a mine and is flagged
				if(grid[y][x].isMine()&&grid[y][x].flag){
					mineInt++;
					winCount++;
				}
				else if(grid[y][x].isMine())
					mineInt++;
				//If the number of flagged mines matches the number of mines then return true
				if(winCount==mineCount)
					out = true;
				else if(winCount<mineCount&&mineInt==mineCount)
					out = false;
			}
		}
		return out;
	}
}
