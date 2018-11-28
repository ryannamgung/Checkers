import java.io.*;
import java.util.*;

/**
 * This class serves as a GUI for a checkers game,
 * which works with several instances of a class 
 * called Piece to enforce the rules of checkers.
 * 
 * Your task is to finish implementing the processInput
 * method and the Piece class to enforce the rules. 
 * The current behavior of the application allows users 
 * to take turns moving a piece to any available slot. 
 * The rules about legal moves, including capturing pieces, 
 * are not enforced.
 * 
 * The rest of this documentation overviews the code
 * that is here.
 * 
 * Instance variables: The GUI maintains a board (2D array 
 * of Piece objects), current player, and current piece. 
 * 
 * Construction: The Checkers class with a main method 
 * calls the GUI constructor, which initializes ivars 
 * and constructs 12 pieces per player and puts them 
 * on the board at the starting locations. 
 * (See setupPieces() and Piece constructor.) 
 * 
 * Run: When run() begins, various 1-time calls to static  
 * StdDraw methods configure the canvas (See initialize().) 
 * The initial configuration is displayed, and then a loop 
 * controlling the event-driven behavior of the game begins. 
 * When a user clicks the mouse, processInput() and then 
 * drawConfiguration() are called, and then gameOver() is 
 * checked to see whether to terminate the program.
 * 
 * Process input: Called whenever a mouse is clicked, this
 * method translates the click coordinates into board indices.
 * You should modify the if-else block to satisfy the 
 * selecting/moving requirements of the assignment handout. 
 * 
 * Draw configuration: Draws 32 alternating black tiles and  
 * a yellow banner at the top or bottom indicating whose 
 * turn it is, and it iterates through all the pieces on the
 * board calling draw (implemented in the Piece class).
 * 
 * Game over: This method currently just returns false. 
 * Implement it as an extension!
 */
public class CheckersGame {

	/** Display constants */
	private static final int CANVAS_SIZE = 600; // number of pixels
	private static final double BANNER = 0.1; // proportion of tile for border
	private static final int PAUSE_TIME = 16; // milliseconds 
	private char key; //last key pressed

	/** State of the application */
	private final Piece[][] board; 
	private boolean currentPlayer;
	private Piece currentPiece;

	/**
	 * Constructor to initialize instance variables.
	 */
	public CheckersGame() {
		// fill board with pieces
		board = new Piece[8][8];
		currentPlayer = true; // dark ("true") player starts
		currentPiece = null; // nothing selected yet
	}

	/**
	 * Initialization of the GUI for the game - Setup GUI canvas, scale, and
	 * double buffering.
	 */
	public void initialize() {
		// Set dimensions (in pixels) for canvas
		StdDraw.setCanvasSize(CANVAS_SIZE, CANVAS_SIZE);
		// Scale canvas coordinate system to easily  
		// translate into array indices (0 to 7).
		// Lower left corner is (-0.1,-0.1)
		StdDraw.setXscale(-BANNER, 8+BANNER);
		StdDraw.setYscale(-BANNER, 8+BANNER);
		StdDraw.enableDoubleBuffering();
	}

	/**
	 * Basic game loop (process input, update, draw, show). This is like the real
	 * main function.
	 */
	public void run() {
		initialize();
		welcomeThenConstructPieces();
		drawConfiguration();
		StdDraw.show();

		while (true) {
			//check for click (mouse down and up without any movement between)
			if(StdDraw.hasNextMouseClicked()) {
				processInput(); // select/deselect/move
				StdDraw.clear();
				drawConfiguration(); // only need to redraw after click
				StdDraw.show();
				if(StdDraw.hasNextKeyTyped()){
					key = StdDraw.nextKeyTyped(); 
					if(key == 'S' ||key == 's'){
						saveGame();
						System.exit(0);
					}
				}
				if (gameOver()) break;
			}
			StdDraw.pause(PAUSE_TIME);
		}
	}

	private void welcomeThenConstructPieces() {
		// draw gray game button on the top half of the screen
		StdDraw.setPenColor(StdDraw.LIGHT_GRAY);
		StdDraw.filledRectangle(4, 6, 4, 2-BANNER/2);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(4, 6, "Start new game");
		
		//draw red load game button on bottom half of the screen
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.filledRectangle(4, 2, 4, 2-BANNER/2);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.text(4, 2, "Load saved game");
		
		//display drawn components
		StdDraw.show();
		
		//wait for click
		while(!StdDraw.hasNextMouseClicked()){
			StdDraw.pause(PAUSE_TIME);
		}
		
		//respond to click
		if(StdDraw.nextMouseClicked().getY() > 4){
			//if user clicks in top half of canvas
			//set up pieces in starting configuration
			setupPieces();
		}else{
			//else load pieces from files
			loadSavedGame();
		}
		
		//clear canvas before drawing configuration for the first time
		StdDraw.clear();
	}
	
	//loads the instance of a saved game if the button load saved is clicked
	private void loadSavedGame() {
		try {
			Scanner input = new Scanner(new File("game.txt"));
			String curPlayer = input.nextLine();
			if(curPlayer == "true"){
				currentPlayer = true;
			}else{
				currentPlayer = false;
			}
			
			while(input.hasNextLine()){
				String gamePiece = input.nextLine();
				//how to set the values once we read in the string
				//must initialize values
				
			}
			
			input.close();
			} catch (FileNotFoundException e) {
				System.out.println(e);
				System.exit(1);
			}
	}

	private void saveGame() {
		try {
			PrintWriter output = new PrintWriter("game.txt");
				/* write to the file */
				if(currentPlayer){
					output.println(currentPlayer);
				}else{
					output.println(currentPlayer);
				}
				
				//in a while or for loop call upon all the instances of current Piece
				output.println(currentPiece.toString());
				
			
			output.close();
			} catch (FileNotFoundException e) {
				System.out.println(e);
				System.exit(1);
			}
	}

	/**
	 * Constructs new pieces for both teams and stores 
	 * them in the board array at initial locations.
	 * Called once right after initializing canvas.
	 */
	public void setupPieces() {	
		// dark pieces at the bottom
		for (int row=0; row<3; row++) {
			for (int col=row%2; col<8; col+=2) {
				board[row][col] = new Piece(true,board,row,col);
			}
		}
		// light pieces at the top
		for (int row=5; row<8; row++) {
			for (int col=row%2; col<8; col+=2) {
				board[row][col] = new Piece(false,board,row,col);
			}
		}
	}

	/** Reports whether the current player has any moves */
	public boolean gameOver() {
		return false;
	}

	/**
	 * Process mouse and key presses - user input.
	 * Note how to use mouse here in different ways. Click actions are defined by
	 * a press and release of the left mouse button without moving the mouse in
	 * between. Mouse press events can be used to determine if the left mouse
	 * button is down. Mouse movement can also be tracked without pressing the
	 * button. Keyboard button presses are also shown. Here you write code
	 * corresponding to altering the state of the game based upon these actions.
	 */
	public void processInput() {
		// Get the mouse click and its coordinates
		StdDraw.MouseClick m = StdDraw.nextMouseClicked();
		double x = m.getX(), y = m.getY();

		// translate coordinates and get selected piece
		int row = (int)y, col = (int)x;
		Piece curPiece = board[row][col];
		//code that selects the piece
		if(row < 0 || col < 0 || row > 7 || col > 7){
			return;
		}else if (currentPiece == null) {
			// select new piece if it belongs to the correct player
			Piece newPiece = board[row][col]; 
			if (newPiece != null && newPiece.player == currentPlayer && newPiece.hasValidMove()) {
				newPiece.select();
				currentPiece = newPiece;
			}
		}else if(curPiece == currentPiece){
				curPiece.deselect();
				currentPiece = null;
		}else if(currentPiece.isValidMove(row,col)){ 
			// move the current piece to the clicked location
			currentPiece.move(row,col);
			//should write a better check because we keep alternating without actually moving *******
			if(currentPiece.capturing() == false){
			// deselect and get ready for next player's turn
				currentPiece.deselect();
				currentPiece = null;
				currentPlayer = !currentPlayer;
			}
		}
	}

	/**
	 * Draw the configuration of the pieces.
	 */
	public void drawConfiguration() {
		// draw the tiles of the board
		StdDraw.setPenColor(StdDraw.BLACK);
		for (int x=0; x<8; x++) {
			for (int y=0; y<8; y++) {
				if ((x+y)%2==0)
					StdDraw.filledRectangle(x+0.5,y+0.5,0.5,0.5);
			}
		}

		// draw yellow banner indicating current player
		StdDraw.setPenColor(StdDraw.YELLOW);
		if (currentPlayer) {
			StdDraw.filledRectangle(4, -BANNER/2, 4+BANNER, BANNER/2);
		} else {
			StdDraw.filledRectangle(4, 8+BANNER/2, 4+BANNER, BANNER/2);			
		}

		// tell pieces to draw themselves
		for (int y=0; y<8; y++) {
			for (int x=0; x<8; x++) {
				Piece piece = board[y][x];
				if (piece!=null) {
					piece.draw();
				}
			}
		}
	}
	
}