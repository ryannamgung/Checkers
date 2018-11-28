/**
 * This class encapsulates the core data and functionality 
 * of the main objects in a checkers game -- the pieces.
 * Each piece keeps track of the player it belongs to, its
 * location, and whether it is selected, capturing, or crowned.
 * Each piece can also see the board instance variable 
 * maintained by the GUI to check for move validity. 
 * A piece knows how to draw itself (implemented for you)
 * and toggle its selected boolean. 
 * 
 * Your main task with this class is to implement a move method
 * that respects the rules of the game. You'll probably want
 * to implement other public and private methods along the way.
 */
public class Piece {
	// initialized by constructor and never changed
	public final boolean player; // true="dark" team (red)
	private final Piece[][] board;

	// state variables
	private int row;
	private int col;
	private boolean selected;
	private boolean capturing;
	private boolean king;

	/**
	 * Construct a new piece object with specified player identifier,
	 * board, location, and default starting states.
	 * @param player
	 * @param board
	 * @param row
	 * @param col
	 */
	//initial constructor
	public Piece(boolean player, Piece[][] board, int row, int col) {
		this.player = player;
		this.board = board;
		this.row = row;
		this.col = col;
		selected = false;
		capturing = false;
		king = false;
	}
	
	//total saved constructor
	public Piece(boolean player, Piece[][] board, int row, int col, boolean selected, boolean capturing, boolean king){
		this.player = player;
		this.board = board;
		this.row = row;
		this.col = col;
		this.selected = selected;
		this.capturing = capturing;
		this.king = false;
	}

	/**
	 * Draw correctly-colored circle at correct coordinates.
	 * Circle should be outlined in yellow if selected 
	 * or magenta if jumping.
	 */
	public void draw() {
		double y = row+.5, x = col+.5; // add .5 to center
		if (selected) { // draw slightly larger yellow circle underneath
			StdDraw.setPenColor(StdDraw.YELLOW);
			if (capturing) StdDraw.setPenColor(StdDraw.MAGENTA);
			StdDraw.filledCircle(x,y,.45);						
		}
		// draw a circle at the correct place with correct color
		StdDraw.setPenColor(player?StdDraw.RED:StdDraw.LIGHT_GRAY);
		StdDraw.filledCircle(x,y,.4);

		// draw image over kings
		if (king) 
			StdDraw.picture(x,y,"crown.png",.5,.5);

	}

	public String toString(){
		String all = new String();
		all += Boolean.toString(player);
		all += " ";
		all += Integer.toString(row);
		all += " ";
		all += Integer.toString(col);
		all += " ";
		all += Boolean.toString(selected);
		all += " ";
		all += Boolean.toString(capturing);
		all += " ";
		all += Boolean.toString(king);
		
		return all;
	}
	//simply determines whether we are in the scope of the board
	private boolean isLegit(int newRow, int newCol){
		if(newRow <= 7 && newRow >= 0){
			if(newCol <= 7 && newCol >= 0){
				return true;
			}
		}
		return false;
	}

	//isValid is a helper method that determines whether or not the space we want to move to
	//or capture is empty or not
	private boolean isValid(int rowDel, int colDel){
		if(board[row + rowDel][col + colDel] == null){
			return true;
		}else if(board[row+rowDel][col+colDel].player == !player){
			if(isLegit(row+(2*rowDel), col+(2*colDel))){
				if(board[row+(2*rowDel)][col+(2*colDel)] == null){
					return true;
				}
			}
		}
		return false;
	}

	//hasValidMove is a method that checks that the piece we selected is one that can be moved
	//i.e. it has a capture available, or it is allowed to move foward/backwards
	public boolean hasValidMove(){
		//checks if the piece that you selected is a king and moves accordingly
		int rowDelta1 = 1;
		int rowDelta2 = -1;
		int colDelta1 = 1;
		int colDelta2 = -1;
		if(king){

			if(isLegit(row + rowDelta1, col + colDelta1)){
				if(isValid(rowDelta1, colDelta1)){
					return true;
				}
			}

			if(isLegit(row + rowDelta1, col + colDelta2)){
				if(isValid(rowDelta1, colDelta2)){
					return true;
				}
			}

			if(isLegit(row + rowDelta2, col + colDelta1)){
				if(isValid(rowDelta2, colDelta1)){
					return true;
				}
			}

			if(isLegit(row + rowDelta2, col + colDelta2)){
				if(isValid(rowDelta2, colDelta2)){
					return true;
				}
			}
		}
		//checks for possible moves that the red team may make
		else if(player){
			if(isLegit(row+rowDelta1,col + colDelta1)){
				if(isValid(rowDelta1, colDelta1)){
					return true;
				}	
			}

			if(isLegit(row+rowDelta1, col + colDelta2)){
				if(isValid(rowDelta1, colDelta2)){
					return true;
				}
			}
			//checks for possible moves that the black team may make
		}else if(player == false){
			if(isLegit(row + rowDelta2,col + colDelta1)){
				if(isValid(rowDelta2, colDelta1)){
					return true;
				}
			}

			if(isLegit(row+rowDelta2, col + colDelta2)){
				if(isValid(rowDelta2, colDelta2)){
					return true;
				}
			}
		}

		return false;

	}
	/** 
	 * Stub method for moving:  check whether the target
	 * spot is available, and if so, update location and
	 * possibly become a king.   
	 */

	//confirms that the movement we make will be a valid one by checking the move
	//case that is not a capture
	private boolean confirmMove(int newRow, int newCol){
		if(king){
			if(newRow == row + 1 || newRow == row - 1){
				if(newCol == col + 1 || newCol == col - 1){
					return true;
				}
			}
		}
		if(player){
			if(newRow == row + 1){
				if(newCol == col + 1 || newCol == col - 1){
					return true;
				}
			}
		}else if(!player){
			if(newRow == row - 1){
				if(newCol == col + 1 || newCol == col - 1){
					return true;
				}
			}
		}
		return false;
	}


	//confirms whether or not there is a piece at the specific point on the board so 
	//that we may capture it

	private int hasCapture(int newRow, int newCol){
		int rowDelta1 = 1;
		int rowDelta2 = -1;
		int colDelta1 = 1;
		int colDelta2 = -1;
		if(king){
			if(isLegit(newRow, newCol)){
				if(newRow == row + (2*rowDelta1) && newCol == col + (2*colDelta1)){
					if(board[row + rowDelta1][col + colDelta1].player == !player)
						return 1;
				}else if(newRow == row + (2*rowDelta1) && newCol == col + (2*colDelta2)){
					if(board[row + rowDelta1][col + colDelta2].player == !player)
						return 2;
				}else if(newRow == row + (2*rowDelta2) && newCol == col + (2*colDelta1)){
					if(board[row + rowDelta2][col + colDelta1].player == !player)
						return 3;
				}else if(newRow == row + (2*rowDelta2) && newCol == col + (2*colDelta2)){
					if(board[row + rowDelta2][col + colDelta2].player == !player)
						return 4;
				}
			}
			//we are checking the values between our selected new row & column and seeing if they
			//have an opposing piece, if they do we return true
		}
		else if(player){
			//sees whether or not there is a piece available for capture if player red
			if(isLegit(newRow, newCol)){
				if(newRow == row + (2*rowDelta1) && newCol == col + (2*colDelta1)){
					if(board[row + rowDelta1][col + colDelta1].player == !player)
						return 1;
				}else if(newRow == row + (2*rowDelta1) && newCol == col + (2*colDelta2)){
					if(board[row + rowDelta1][col + colDelta2].player == !player)
						return 2;
				}
			}
		}else if(!player){
			//sees whether or not there is a piece available for capture if player red
			if(isLegit(newRow, newCol)){
				if(newRow == row + (2*rowDelta2) && newCol == col + (2*colDelta1)){
					if(board[row + rowDelta2][col + colDelta1].player == !player)
						return 3;
				}else if(newRow == row + (2*rowDelta2) && newCol == col + (2*colDelta2)){
					if(board[row + rowDelta2][col + colDelta2].player == !player)
						return 4;
				}
			}
		}
		//if we cant find such a piece we will return 0
		return 0;
	}

	//confirms that we move 2 spaces to a valid point
	private boolean twoSpace(int newRow, int newCol){
		if(newRow == row + 2 || newRow == row - 2){
			if(newCol == col + 2 || newCol == col -2){
				return true;
			}
		}
		return false;
	}

	//capture method
	private void capture(int newRow, int newCol){
		int rowDelta1 = 1;
		int rowDelta2 = -1;
		int colDelta1 = 1;
		int colDelta2 = -1;
		if(hasCapture(newRow, newCol) == 1){
			board[row + rowDelta1][col + colDelta1] = null;
		}else if(hasCapture(newRow, newCol) == 2){
			board[row + rowDelta1][col + colDelta2] = null;
		}else if(hasCapture(newRow, newCol) == 3){
			board[row + rowDelta2][col + colDelta1] = null;
		}else if(hasCapture(newRow, newCol) == 4){
			board[row + rowDelta2][col + colDelta2] = null;
		}
		board[newRow][newCol] = board[row][col];
		board[row][col] = null;
		row = newRow;
		col = newCol;
	}


	//main move stub
	public void move(int newRow, int newCol) {
		capturing = false;
		if (board[newRow][newCol] == null) {
			if(confirmMove(newRow, newCol)){
				// move from current to new spot on the board
				board[newRow][newCol] = board[row][col];
				board[row][col] = null;
				// update location instance variables
				row = newRow;
				col = newCol;
			}else if(twoSpace(newRow, newCol)){
				// become king if reach far side
				if(hasCapture(newRow, newCol) != 0){
					capture(newRow, newCol);
					if (captureAvail())
						capturing = true;
				}
			}
			//what is this
			if (newRow==(player?7:0)) 
				king = true;
		}
	}

	//determines whether or not that the point we clicked is null and 2 spaces apart
	//then it also determines whether or not that the space we want to capture holds an
	//enemy piece
	private boolean isCapturable(int rowDelta, int colDelta){
		if(isLegit(row+(2*rowDelta), col+(2*colDelta))){
			if(board[row+(2*rowDelta)][col+(2*colDelta)] == null){
				if(board[row+rowDelta][col + colDelta] == null){
					return false;
				}else if(board[row+rowDelta][col + colDelta].player == !player){
					return true;
				}
			}
		}
		return false;
	}
	
	//switches our boolean value to true if we are able to perform another capture
	private boolean captureAvail() {
		int rowDelta1 = 1;
		int rowDelta2 = -1;
		int colDelta1 = 1;
		int colDelta2 = -1;
		if(king){
			if(isCapturable(rowDelta1, colDelta1)){
				return true;
			}
			if(isCapturable(rowDelta1, colDelta2)){
				return true;
			}
			if(isCapturable(rowDelta2, colDelta1)){
				return true;
			}
			if(isCapturable(rowDelta2, colDelta2)){
				return true;
			}
		}else if(player){
			if(isCapturable(rowDelta1, colDelta1)){
				return true;
			}
			if(isCapturable(rowDelta1, colDelta2)){
				return true;
			}
		}else if(!player){
			if(isCapturable(rowDelta2, colDelta1)){
				return true;
			}
			if(isCapturable(rowDelta2, colDelta2)){
				return true;
			}
		}
		return false;
	}

	//way to access capturing within processInput
	public boolean capturing(){
		return capturing;
	}

	//determines if the move we are looking for is at a null space then this is valid
	public boolean legal(int newRow, int newCol, int rowDelta, int colDelta){
		if(newRow == row + rowDelta && newCol == col + colDelta){
			if(board[newRow][newCol] == null && board[row][col].player == player)
				return true;
		}
		return false;
	}
	
	//if this point that we clicked on is valid then we will be able to move
	public boolean isValidMove(int newRow, int newCol){
		int rowDelta1 = 1;
		int rowDelta2 = -1;
		int colDelta1 = 1;
		int colDelta2 = -1;
		if(king){
			//if king then all these moves are valid
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta1, colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta1, colDelta2))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta2, colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta2, colDelta2))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta1, 2*colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta1, 2*colDelta2))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta2, 2*colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta2, 2*colDelta2))
					return true;
			}
		}else if(player){
			//case for if we are player is this then a valid move?
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta1, colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta1, colDelta2))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta1, 2*colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta1, 2*colDelta2))
					return true;
			}
		}else if(!player){
			//case for if we are !player is this then a valid move?
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta2, colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, rowDelta2, colDelta2))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta2, 2*colDelta1))
					return true;
			}
			if(isLegit(newRow, newCol)){
				if(legal(newRow, newCol, 2*rowDelta2, 2*colDelta2))
					return true;
			}
		}
		return false;
	}


	/** Select the piece */
	public void select() {
		selected = true;
	}

	/** Deselect the piece */
	public void deselect() {
		selected = false;
	}

}

