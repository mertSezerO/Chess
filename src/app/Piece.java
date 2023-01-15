package app;

public abstract class Piece {
	private String tag;
	private String color;
	private int x;
	private int y;
	private Board board;
	
	public Piece(String value,int x,int y,Board board,String color) {
		this.tag = value;
		this.x = x;
		this.y = y;
		this.board = board;
		this.color =color;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getColor() {
		return color;
	}
	public Board getBoard() {
		return board;
	}
	public void setBoard(Board board) {
		this.board = board;
	}
	public abstract void canGo();
}

class Pawn extends Piece{
	private boolean is_first;
	
	public Pawn(int x,int y,Board board,String color) {
		super("Pawn",x,y,board,color);
		is_first = true;
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	public void canGo() {
		Board board = this.getBoard();

		if(this.getColor() == "white") {
			
			//Movement for white pawns
			if(this.is_first) {
				for(int i=0;i<2;i++) {
					if(this.getX()-1<0)
						break;
					if(board.getBox(this.getX()-i-1, this.getY()).getPiece() != null) 
						break;
					else
						board.getBox(this.getX()-i-1, this.getY()).setHighlighted(true);
				}
			}
			else {
				if(board.getBox(this.getX()-1, this.getY()).getPiece() == null && this.getX() != 1)
					board.getBox(this.getX()-1, this.getY()).setHighlighted(true);
				else
					convert();
			}
			
			//Consuming enemy for white pawns
			for(int i=-1;i<2;i+=2) {
				if(this.getX()<1)
					break;
				if(board.getBox(this.getX()-1, getY()+i).hasPiece()) 
					if(board.getBox(this.getX()-1, getY()+i).getPiece().getColor().equals(this.getColor())) 
						board.getBox(this.getX()-1, getY()+i).setConsumeHighlight(true);
			}
		}
		else {
			
			//Movement for black pawns
			if(this.is_first) {
				for(int i=0;i<2;i++) {
					if(board.getBox(this.getX()+i+1, this.getY()).getPiece() != null) 
						break;
					else
						board.getBox(this.getX()+i+1, this.getY()).setHighlighted(true);
				}
			}
			else {
				if(board.getBox(this.getX()+1, this.getY()).getPiece() == null)
					board.getBox(this.getX()+1, this.getY()).setHighlighted(true);
			}
			
			//Consuming enemy for black pawns
			for(int i=-1;i<2;i+=2) {
				if(this.getX()>6)
					break;
				if(board.getBox(this.getX()+1, getY()+i).hasPiece()) 
					if(board.getBox(this.getX()+1, getY()+i).getPiece().getColor().equals(this.getColor())) 
						board.getBox(this.getX()+1, getY()+i).setConsumeHighlight(true);
			}
		}
	}
	public void convert() {
		
	}
	public void passByTaken() {
		
	}
}

class Rock extends Piece{
	private final int[] X_VECTOR = {-1,0,1,0};
	private final int[] Y_VECTOR = {0,1,0,-1};
	private boolean hasMoved = false;
	
	public Rock(int x,int y,Board board,String color) {
		super("Rock",x,y,board,color);
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	public void canGo() {
		Board board = this.getBoard();
		
		for(int i=0;i<4;i++) {
			for(int j=1;j<9;j++) {
				if(this.getX()+X_VECTOR[i]*j>=0 && this.getX()+X_VECTOR[i]*j<8 && this.getY()+Y_VECTOR[i]*j>=0 && this.getY()+Y_VECTOR[i]*j<8) {
					if(board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).hasPiece()) {
						if(!board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).getPiece().getColor().equals(this.getColor())) 
							board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setConsumeHighlight(true);
						break;
					}
					else
						board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setHighlighted(true);
				}
				else
					break;
			}
		}
	}
	
	public boolean getHasMoved()
	{
		return hasMoved;
	}
	public void setHasMoved(boolean b)
	{
		this.hasMoved = b;
	}
	
}
class Knight extends Piece{
	private final int[] X_VECTOR = {-2,-2,-1,1,2,2,1,-1};
	private final int[] Y_VECTOR = {-1,1,2,2,1,-1,-2,-2};
	
	public Knight(int x,int y,Board board,String color) {
		super("Knight",x,y,board,color);
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	public void canGo() {
		Board board = this.getBoard();
		
		for(int i=0;i<8;i++) {
			if(this.getX()+X_VECTOR[i]>=0 && this.getX()+X_VECTOR[i]<8 && this.getY()+Y_VECTOR[i]>=0 && this.getY()+Y_VECTOR[i]<8) {
				if(board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).hasPiece()) {
					if(!board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).getPiece().getColor().equals(this.getColor()))
						board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).setConsumeHighlight(true);
				}
				else
					board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).setHighlighted(true);
			}
		}
	}
}
class Bishop extends Piece{
	private final int[] X_VECTOR = {-1,1,1,-1};
	private final int[] Y_VECTOR = {1,1,-1,-1};
	
	public Bishop(int x,int y,Board board,String color) {
		super("Bishop",x,y,board,color);
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	public void canGo() {
		Board board = this.getBoard();
		
		for(int i=0;i<4;i++) {
			for(int j=1;j<9;j++) {
				if(this.getX()+X_VECTOR[i]*j>=0 && this.getX()+X_VECTOR[i]*j<8 && this.getY()+Y_VECTOR[i]*j>=0 && this.getY()+Y_VECTOR[i]*j<8) {
					if(board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).hasPiece()) {
						if(!board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).getPiece().getColor().equals(this.getColor())) 
							board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setConsumeHighlight(true);
						break;
					}
					else
						board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setHighlighted(true);
				}
				else
					break;
			}
		}
	}	
}
class Queen extends Piece{
	private final int[] X_VECTOR = {-1,-1,0,1,1,1,0,-1};
	private final int[] Y_VECTOR = {0,1,1,1,0,-1,-1,-1};
		
	public Queen(int x, int y,Board board,String color) {
		super("Queen",x,y,board,color);
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	public void canGo() {
		Board board = this.getBoard();
		
		for(int i=0;i<8;i++) {
			for(int j=1;j<9;j++) {
				if(this.getX()+X_VECTOR[i]*j>=0 && this.getX()+X_VECTOR[i]*j<8 && this.getY()+Y_VECTOR[i]*j>=0 && this.getY()+Y_VECTOR[i]*j<8) {
					if(board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).hasPiece()) {
						if(!board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).getPiece().getColor().equals(this.getColor()))
							board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setConsumeHighlight(true);
						break;
					}
					else
						board.getBox(this.getX()+X_VECTOR[i]*j, this.getY()+Y_VECTOR[i]*j).setHighlighted(true);
				}
				else
					break;
			}
		}
	}
}
class King extends Piece{
	private boolean is_first;
	private final int[] X_VECTOR = {-1,-1,0,1,1,1,0,-1};
	private final int[] Y_VECTOR = {0,1,1,1,0,-1,-1,-1};
	
	public King(int x,int y,Board board,String color) {
		super("King",x,y,board,color);
		this.getBoard().getBox(x,y).setHasPiece(true);
	}
	
	public void canGo() {
		Board board = this.getBoard();
		
		for(int i=0;i<8;i++) {
			if(this.getX()+X_VECTOR[i]>=0 && this.getX()+X_VECTOR[i]<8 && this.getY()+Y_VECTOR[i]>=0 && this.getY()+Y_VECTOR[i]<8) {
				if(board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).hasPiece()) {
					if(!board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).getPiece().getColor().equals(this.getColor()))
						board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).setConsumeHighlight(true);
				}
				else
					board.getBox(this.getX()+X_VECTOR[i], this.getY()+Y_VECTOR[i]).setHighlighted(true);
			}
		}
		
		//To check the rock chance
				if(this.getColor().equals("white")) {
					if(this.getIsFirst() && board.getBox(7, 0).getPiece().getTag().equals("Rock") && !((Rock)board.getBox(7, 0).getPiece()).getHasMoved()) {
						if(!board.getBox(this.getX(), this.getY()-1).hasPiece() && !board.getBox(this.getX(), this.getY()-2).hasPiece() && !board.getBox(this.getX(), this.getY()-3).hasPiece())
							board.getBox(this.getX(), this.getY()-2).setRockHighlight(true);
					}
					if(this.getIsFirst() && board.getBox(7, 7).getPiece().getTag().equals("Rock") && !((Rock)board.getBox(7, 7).getPiece()).getHasMoved()) {
						if(!board.getBox(this.getX(), this.getY()+1).hasPiece() && !board.getBox(this.getX(), this.getY()+2).hasPiece())
							board.getBox(this.getX(), this.getY()+2).setRockHighlight(true);
					}
				}
				else {
					if(this.getIsFirst() && board.getBox(0, 0).getPiece().getTag().equals("Rock") && !((Rock)board.getBox(0, 0).getPiece()).getHasMoved()) {
						if(!board.getBox(this.getX(), this.getY()-1).hasPiece() && !board.getBox(this.getX(), this.getY()-2).hasPiece() && !board.getBox(this.getX(), this.getY()-3).hasPiece())
							board.getBox(this.getX(), this.getY()-2).setRockHighlight(true);
					}
					if(this.getIsFirst() && board.getBox(0, 7).getPiece().getTag().equals("Rock") && !((Rock)board.getBox(0, 7).getPiece()).getHasMoved()) {
						if(!board.getBox(this.getX(), this.getY()+1).hasPiece() && !board.getBox(this.getX(), this.getY()+2).hasPiece())
							board.getBox(this.getX(), this.getY()+2).setRockHighlight(true);
					}
				}
					
			}
	
	public boolean getIsFirst() {
		return is_first;
	}
	public void setIsFirst(boolean is_first) {
		this.is_first = is_first;
	}
}