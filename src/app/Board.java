package app;

import java.util.ArrayList;
import java.util.Scanner;


public class Board {
	private Box[][] gametable= new Box[8][8];
	ArrayList<Piece> consumedWhitePieces = new ArrayList<Piece>();
	ArrayList<Piece> consumedBlackPieces = new ArrayList<Piece>();
	private Piece movingPiece;
	private String nextPlayer;
	private boolean isCheck;
	private boolean isFinished;
	
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
		if(gametable[dest_x][dest_y].hasPiece()){
			if(gametable[dest_x][dest_y].getPiece().getColor().equals("white"))
				consumedWhitePieces.add(gametable[dest_x][dest_y].getPiece());
			else
				consumedBlackPieces.add(gametable[dest_x][dest_y].getPiece()) ;
		}
		int previousX = movingPiece.getX();
		gametable[movingPiece.getX()][movingPiece.getY()].setHasPiece(false);
		gametable[movingPiece.getX()][movingPiece.getY()].setPiece(null);
		gametable[dest_x][dest_y].setPiece(movingPiece);
		gametable[dest_x][dest_y].setHasPiece(true);
		movingPiece.setX(dest_x);
		movingPiece.setY(dest_y);
		if(movingPiece instanceof Pawn){
			if(dest_x-previousX == 2 || dest_x-previousX == -2)
				((Pawn)movingPiece).setMovedTwoBoxes(true);
			else
				((Pawn)movingPiece).setMovedTwoBoxes(false);
			if(dest_x == 7 || dest_x ==0)
				convert(dest_x, dest_y, movingPiece.getColor());
			if(((Pawn)movingPiece).passBy()){
				int where = movingPiece.getColor().equals("white") ? 1: -1;
				gametable[dest_x+where][dest_y].setPiece(null);
				gametable[dest_x+where][dest_y].setHasPiece(false);
			}
			((Pawn)movingPiece).setIsFirst(false);
		}
		else if(movingPiece instanceof Rock)
			((Rock)movingPiece).setHasMoved(true);
		else if(movingPiece instanceof King)
			((King)movingPiece).setIsFirst(false);
		nextPlayer = (movingPiece.getColor().equals("white")) ? "black" : "white";
		Piece activeCheck = check(nextPlayer);
		if(activeCheck != null){
			for(int i=0;i<64 ;i++){
				if(gametable[i/8][i%8].hasPiece())
					if(gametable[i/8][i%8].getPiece() instanceof King && !gametable[i/8][i%8].getPiece().getColor().equals(movingPiece.getColor()))
						gametable[i/8][i%8].setCheckHighlight(true);
			}
			isCheck = true;
		}
		else
			isCheck = false;
		if(checkMate())
			isFinished = true;
	}
	
	public void rock(int king_x,int king_y,int fin_x,int fin_y) {
		int short_or_long = (fin_y<king_y) ? 1 : -1;
		int whichRock = (fin_y<king_y) ? 0 : 7;
		gametable[king_x][king_y].setHasPiece(false);
		gametable[king_x][king_y].setPiece(null);

		gametable[fin_x][fin_y].setHasPiece(true);
		gametable[fin_x][fin_y].setPiece(movingPiece);
		gametable[fin_x][fin_y].getPiece().setX(fin_x);
		gametable[fin_x][fin_y].getPiece().setY(fin_y);

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
		movingPiece=gametable[x][y].getPiece();
		gametable[x][y].setPiece(null);
		gametable[x][y].setHasPiece(false);
		Piece threat = check(movingPiece.getColor());
		gametable[x][y].setPiece(movingPiece);
		gametable[x][y].setHasPiece(true);
		if(isCheck || movingPiece instanceof King){
			movingPiece.canGo();
			gametable[x][y].setPiece(null);
			gametable[x][y].setHasPiece(false);
			desolveCheck(movingPiece.getColor(), movingPiece);
			gametable[x][y].setPiece(movingPiece);
			gametable[x][y].setHasPiece(true);
		}
		else if(threat==null){
			movingPiece.canGo();
		}
		else{
			gametable[x][y].setCheckHighlight(true);
			if(removeThreat(threat))
				gametable[threat.getX()][threat.getY()].setConsumeHighlight(true);
		}	
	}

	public void desolveCheck(String color, Piece mPiece){
		for(int i=0;i<64;i++){
			//when defender could consume, this method is problematic
			//movingPiece en son hareket eden taşı tuttuğundan checkMate için kullanılamaz
			if(gametable[i/8][i%8].getIsHighlighted() || gametable[i/8][i%8].getIsConsumeHighlight()){
				Piece piece = gametable[i/8][i%8].getPiece();
				boolean b = gametable[i/8][i%8].hasPiece();
				gametable[i/8][i%8].setHasPiece(true);
				gametable[i/8][i%8].setPiece(mPiece);
				Piece possible = check(color);
				if(possible != null){
					gametable[i/8][i%8].setHighlighted(false);
					gametable[i/8][i%8].setConsumeHighlight(false);
				}
				gametable[i/8][i%8].setHasPiece(b);
				gametable[i/8][i%8].setPiece(piece);
			}
		}
	}
	
	public Piece check(String moving) {
		String lastMoved = (moving.equals("white")) ? "black" : "white";
		for(int x=0; x<64;x++){
			if(gametable[x/8][x%8].hasPiece()){
				if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Pawn){
					Pawn pawn = (Pawn)gametable[x/8][x%8].getPiece();
					int blackOrWhite = pawn.getColor().equals("white") ? -1 : 1;
					for(int i=-1;i<2;i+=2) {
						if(pawn.getX()>6 || pawn.getX()<1)
							break;
						if(pawn.getY()+i<0 || pawn.getY()+i>7)
							continue;
						if(this.getBox(pawn.getX()+blackOrWhite, pawn.getY()+i).hasPiece()) {
							Box box = this.getBox(pawn.getX()+blackOrWhite, pawn.getY()+i);
							if(box.getPiece() instanceof King && !box.getPiece().getColor().equals(pawn.getColor())) 
								return gametable[x/8][x%8].getPiece();
						}
					}
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Knight){
					Knight knight = (Knight)gametable[x/8][x%8].getPiece();
					for(int i=0;i<8;i++) {
						if(knight.getX()+knight.getXVector(i)>=0 && knight.getX()+knight.getXVector(i)<8 && knight.getY()+knight.getYVector(i)>=0 && knight.getY()+knight.getYVector(i)<8) {
							if(this.getBox(knight.getX()+knight.getXVector(i), knight.getY()+knight.getYVector(i)).hasPiece()) {
								Box box = this.getBox(knight.getX()+knight.getXVector(i), knight.getY()+knight.getYVector(i));
								if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
									return gametable[x/8][x%8].getPiece();
							}
						}
					}
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Rock){
					Rock rock = (Rock)gametable[x/8][x%8].getPiece();
					for(int i=0;i<4;i++){
						for(int j=1;j<9;j++){
							if(rock.getX()+rock.getXVector(i)*j>=0 && rock.getX()+rock.getXVector(i)*j<8 && rock.getY()+rock.getYVector(i)*j>=0 && rock.getY()+rock.getYVector(i)*j<8) {
								if(this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j).hasPiece()) {
									Box box = this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j);
									if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
										return gametable[x/8][x%8].getPiece();
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
										return gametable[x/8][x%8].getPiece();
									else
										break;
								}
								
							}
						}
					}
				}
				else if(gametable[x/8][x%8].getPiece().getColor().equals(lastMoved) && gametable[x/8][x%8].getPiece() instanceof Queen){
					Queen queen = (Queen)gametable[x/8][x%8].getPiece();
					for(int i=0;i<8;i++){
						for(int j=1;j<9;j++){
							if(queen.getX()+queen.getXVector(i)*j>=0 && queen.getX()+queen.getXVector(i)*j<8 && queen.getY()+queen.getYVector(i)*j>=0 && queen.getY()+queen.getYVector(i)*j<8) {
								if(this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j).hasPiece()) {
									Box box = this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j);
									if(box.getPiece().getColor().equals(moving) && box.getPiece().getTag().equals("King"))
										return gametable[x/8][x%8].getPiece();
									else
										break;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	public boolean removeThreat(Piece threat){
		if(movingPiece instanceof Pawn){
			Pawn pawn = (Pawn)movingPiece;
			if(pawn.getColor().equals("white")){
				for(int i=-1;i<2;i+=2){
					if(pawn.getX()<1)
						break;
					if(pawn.getY()+i<0 || pawn.getY()+i>7)
						continue;
					if(this.getBox(pawn.getX()-1, pawn.getY()+i).hasPiece()){
						Box box = this.getBox(pawn.getX()-1, pawn.getY()+i);
						if(box.getPiece() == threat)
							return true;
					}
				}
			}
			else{
				for(int i=-1;i<2;i+=2){
					if(pawn.getX()>6)
						break;
					if(pawn.getY()+i<0 || pawn.getY()+i>7)
						continue;
					if(this.getBox(pawn.getX()+1, pawn.getY()+i).hasPiece()){
						Box box = this.getBox(pawn.getX()+1, pawn.getY()+i);
						if(box.getPiece() == threat)
							return true;
					}
				}
			}
		}
		else if(movingPiece instanceof Rock){
			Rock rock = (Rock)movingPiece;
			for(int i=0;i<4;i++){
				for(int j=1;j<9;j++){
					if(rock.getX()+rock.getXVector(i)*j>=0 && rock.getX()+rock.getXVector(i)*j<8 && rock.getY()+rock.getYVector(i)*j>=0 && rock.getY()+rock.getYVector(i)*j<8) {
						if(this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j).hasPiece()) {
							Box box = this.getBox(rock.getX()+rock.getXVector(i)*j, rock.getY()+rock.getYVector(i)*j);
							if(box.getPiece() == threat)
								return true;
							else
								break;
						}
						
					}
				}
			}
		}
		else if(movingPiece instanceof Bishop){
			Bishop bishop = (Bishop)movingPiece;
			for(int i=0;i<4;i++){
				for(int j=1;j<9;j++){
					if(bishop.getX()+bishop.getXVector(i)*j>=0 && bishop.getX()+bishop.getXVector(i)*j<8 && bishop.getY()+bishop.getYVector(i)*j>=0 && bishop.getY()+bishop.getYVector(i)*j<8) {
						if(this.getBox(bishop.getX()+bishop.getXVector(i)*j, bishop.getY()+bishop.getYVector(i)*j).hasPiece()) {
							Box box = this.getBox(bishop.getX()+bishop.getXVector(i)*j, bishop.getY()+bishop.getYVector(i)*j);
							if(box.getPiece() == threat)
								return true;
							else
								break;
						}
						
					}
				}
			}
		}
		else if(movingPiece instanceof Knight){
			Knight knight = (Knight)movingPiece;
			for(int i=0;i<8;i++) {
				if(knight.getX()+knight.getXVector(i)>=0 && knight.getX()+knight.getXVector(i)<8 && knight.getY()+knight.getYVector(i)>=0 && knight.getY()+knight.getYVector(i)<8) {
					if(this.getBox(knight.getX()+knight.getXVector(i), knight.getY()+knight.getYVector(i)).hasPiece()) {
						Box box = this.getBox(knight.getX()+knight.getXVector(i), knight.getY()+knight.getYVector(i));
						if(box.getPiece() == threat)
							return true;
					}
				}
			}
		}
		else if(movingPiece instanceof Queen){
			Queen queen = (Queen)movingPiece;
			for(int i=0;i<8;i++){
				for(int j=1;j<9;j++){
					if(queen.getX()+queen.getXVector(i)*j>=0 && queen.getX()+queen.getXVector(i)*j<8 && queen.getY()+queen.getYVector(i)*j>=0 && queen.getY()+queen.getYVector(i)*j<8) {
						if(this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j).hasPiece()) {
							Box box = this.getBox(queen.getX()+queen.getXVector(i)*j, queen.getY()+queen.getYVector(i)*j);
							if(box.getPiece() == threat)
								return true;
							else
								break;
						}
						
					}
				}
			}
		}
		return false;
	}

	public void convert(int x,int y,String color) {
		Scanner input = new Scanner(System.in);
		System.out.print("Select the piece you want to acquire: ");
		String i = input.next();
		switch(i){
			case "Rock": this.getBox(x,y).setPiece(new Rock(x,y,this,color));
				break;
			case "Bishop": this.getBox(x,y).setPiece(new Bishop(x,y,this,color));
				break;
			case "Knight": this.getBox(x,y).setPiece(new Knight(x,y,this,color));
				break;
			case "Queen": this.getBox(x,y).setPiece(new Queen(x,y,this,color));
				break;
		}
		input.close();
	}

	public boolean checkMate(){
		int count=0;
		ArrayList<Piece> list = (nextPlayer.equals("white")) ? consumedWhitePieces : consumedBlackPieces;
		for(int i=0;i<64;i++){
			if(count == 16 - list.size())
				break;
			if(gametable[i/8][i%8].hasPiece()){
				Box box = gametable[i/8][i%8];
				if(box.getPiece().getColor().equals(nextPlayer)){
					Piece piece = box.getPiece();
					normalize();
					piece.canGo();
					desolveCheck(piece.getColor(), piece);
					for(int j=0;j<64;j++){
						if(gametable[j/8][j%8].getIsConsumeHighlight() || gametable[j/8][j%8].getIsHighlighted() ||
							gametable[j/8][j%8].getIsRockHighlight())
							return false;
					}
					count++;
				}
			}
		}
		return true;
	}

	//after move remove highlights
	public void normalize() {
		for(int i=0;i<8;i++) {
			for(int j=0;j<8;j++) {
				gametable[i][j].setConsumeHighlight(false);
				gametable[i][j].setHighlighted(false);
				gametable[i][j].setRockHighlight(false);
				if(!(gametable[i][j].getPiece() instanceof King))
					gametable[i][j].setCheckHighlight(false);
				else{
					if(!isCheck){
						gametable[i][j].setCheckHighlight(false);
					}
				}
			}
		}
	}
	
	public Box getBox(int x,int y) {
		return gametable[x][y];
	}
	
	public ArrayList<Piece> getconsumedWhitePieces(){
		return consumedWhitePieces;
	}

	public ArrayList<Piece> getconsumeBlackhitePieces(){
		return consumedBlackPieces;
	}

	public Piece getMovingPiece() {
		return movingPiece;
	}

	public void setMovingPiece(Piece movingPiece) {
		this.movingPiece = movingPiece;
	}

	public boolean isCheck(){
		return isCheck;
	}

	public boolean isFinished(){
		return isFinished;
	}
}
