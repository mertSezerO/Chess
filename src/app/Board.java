package app;

import java.util.ArrayList;

public class Board {
	private Box[][] gametable= new Box[8][8];
	ArrayList<Piece> consumed_pieces = new ArrayList<Piece>();
	private Piece moving_piece;
	
	public Board() {
		for(int i=0;i<8;i++)
			for(int j=0;j<8;j++) {
				gametable[i][j] = ((i+j)%2 == 0) ? new Box(i,j,"white"):new Box(i,j,"black") ;
				gametable[i][j].setX(i);
				gametable[i][j].setY(j);
			}
			this.setPieces();
	}
	
	private void setPieces() {
		//place Rocks
		gametable[0][0].setPiece(new Rock(0,0,this,"black"));
		gametable[0][7].setPiece(new Rock(0,7,this,"black"));
		gametable[7][7].setPiece(new Rock(7,7,this,"white"));
		gametable[7][0].setPiece(new Rock(7,0,this,"white"));
			
		//place Knights
		gametable[0][1].setPiece(new Knight(0,1,this,"black"));
		gametable[0][6].setPiece(new Knight(0,6,this,"black"));
		gametable[7][1].setPiece(new Knight(7,1,this,"white"));
		gametable[7][6].setPiece(new Knight(7,6,this,"white"));
			
		//place Bishops
		gametable[0][2].setPiece(new Bishop(0,2,this,"black"));
		gametable[0][5].setPiece(new Bishop(0,5,this,"black"));
		gametable[7][2].setPiece(new Bishop(7,2,this,"white"));
		gametable[7][5].setPiece(new Bishop(7,5,this,"white"));
		
		//place Queens
		gametable[0][3].setPiece(new Queen(0,3,this,"black"));
		gametable[7][3].setPiece(new Queen(7,3,this,"white"));
		
		//place Kings
		gametable[0][4].setPiece(new King(0,4,this,"black"));
		gametable[7][4].setPiece(new King(7,4,this,"white"));
		//place Pawns
		String color = "";
		for(int i=1;i<7;i+=5) {
			color = (i==1) ? "black" : "white" ;
			for(int j=0;j<8;j++) {
				gametable[i][j].setPiece(new Pawn(i,j,this,color));
			}
		}
	}
	public void move(int dest_x, int dest_y) {
		if(gametable[dest_x][dest_y].getPiece() != null) 
			consumed_pieces.add(gametable[dest_x][dest_y].getPiece());
		gametable[moving_piece.getX()][moving_piece.getY()].setHasPiece(false);
		gametable[moving_piece.getX()][moving_piece.getY()].setPiece(null);
		gametable[dest_x][dest_y].setPiece(moving_piece);
		moving_piece.setX(dest_x);
		moving_piece.setY(dest_y);
		gametable[moving_piece.getX()][moving_piece.getY()].setHasPiece(true);
		if(gametable[dest_x][dest_y].getPiece() instanceof Pawn)
			((Pawn)gametable[dest_x][dest_y].getPiece()).setIsFirst(false);
	}
	
	public void rock(int king_x,int king_y,int fin_x,int fin_y) {
		int short_or_long = (fin_y<king_y) ? 1 : -1;
		int whichRock = (fin_y<king_y) ? 0 : 7;
		gametable[king_x][king_y].setHasPiece(false);
		gametable[king_x][king_y].setPiece(null);
		gametable[fin_x][fin_y].setHasPiece(true);
		gametable[fin_x][fin_y].setPiece(moving_piece);

		gametable[fin_x][fin_y+short_or_long].setPiece(gametable[king_x][whichRock].getPiece());
		gametable[fin_x][fin_y+short_or_long].setHasPiece(true);
		gametable[fin_x][fin_y+short_or_long].getPiece().setX(fin_x);
		gametable[fin_x][fin_y+short_or_long].getPiece().setY(fin_y+short_or_long);

		gametable[king_x][whichRock].setPiece(null);
		gametable[king_x][whichRock].setHasPiece(false);

		((King)gametable[fin_x][fin_y].getPiece()).setIsFirst(false);
		((Rock)gametable[fin_x][fin_y+short_or_long].getPiece()).setHasMoved(true);
	}
	
	//highlighting possible options
	public void highlight(int x, int y) {
		moving_piece=gametable[x][y].getPiece();
		gametable[x][y].setPiece(null);
		gametable[x][y].setHasPiece(false);
		if(!passiveCheck(moving_piece.getColor())){
			gametable[x][y].setPiece(moving_piece);
			gametable[x][y].setHasPiece(true);
			moving_piece.canGo();
		}
		else{
			gametable[x][y].setPiece(moving_piece);
			gametable[x][y].setHasPiece(true);
			gametable[x][y].setCheckHighlight(true);
		}
			
			
	}
	
	public boolean passiveCheck(String moving) {
		String lastMoved = (moving.equals("white")) ? "black" : "white";
		for(int x=0; x<64;x++){
			if(gametable[x/8][x%8].hasPiece()){
				if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Pawn){
					continue;
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Knight){
					continue;	
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Rock){
					Rock rock = (Rock)gametable[x/8][x%8].getPiece();
					for(int i=0;i<4;i++){
						for(int j=1;j<9;j++){
							if(rock.getX()+rock.getXVector(i)*j>=0 && rock.getX()+rock.getXVector(i)*j<8 && rock.getY()+rock.getYVector(i)*j>=0 && rock.getY()+rock.getYVector(i)*j<8) {
								if(this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j).hasPiece()) {
									Box box = this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j);
									if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
										return true;
									else
										break;
								}
								
							}
						}
					}
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Bishop){
					Bishop bishop = (Bishop)gametable[x/8][x%8].getPiece();
					for(int i=0;i<4;i++){
						for(int j=1;j<9;j++){
							if(bishop.getX()+bishop.getXVector(i)*j>=0 && bishop.getX()+bishop.getXVector(i)*j<8 && bishop.getY()+bishop.getYVector(i)*j>=0 && bishop.getY()+bishop.getYVector(i)*j<8) {
								if(this.getBox(bishop.getX()+bishop.getXVector(i)*j, bishop.getY()+bishop.getYVector(i)*j).hasPiece()) {
									Box box = this.getBox(bishop.getX()+bishop.getXVector(i)*j, bishop.getY()+bishop.getYVector(i)*j);
									if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
										return true;
									else
										break;
								}
								
							}
						}
					}
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Queen){
					Queen queen = (Queen)gametable[x/8][x%8].getPiece();
					for(int i=0;i<7;i++){
						for(int j=1;j<9;j++){
							if(queen.getX()+queen.getXVector(i)*j>=0 && queen.getX()+queen.getXVector(i)*j<8 && queen.getY()+queen.getYVector(i)*j>=0 && queen.getY()+queen.getYVector(i)*j<8) {
								if(this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j).hasPiece()) {
									Box box = this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j);
									if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
										return true;
									else
										break;
								}
								
							}
						}
					}
				}
			}
		}
		return false;
	}

	public boolean activeCheck(){
		return true;
	}

	//after move remove highlights
	public void normalize() {
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				gametable[i][j].setConsumeHighlight(false);
				gametable[i][j].setHighlighted(false);
				gametable[i][j].setRockHighlight(false);
				gametable[i][j].setCheckHighlight(false);
			}
		}
	}
	
	public Box getBox(int x,int y) {
		return gametable[x][y];
	}
	
	public ArrayList<Piece> getConsumed_Pieces(){
		return consumed_pieces;
	}

	public Piece getMovingPiece() {
		return moving_piece;
	}

	public void setMovingPiece(Piece moving_piece) {
		this.moving_piece = moving_piece;
	}
}
