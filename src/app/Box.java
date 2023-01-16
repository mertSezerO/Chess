package app;

public class Box {
	private int x;
	private int y;
	private String color;
	private boolean hasPiece;
	private Piece piece;
	private boolean highlighted;
	private boolean rockHighlight;
	private boolean consumeHighlight;
	private boolean checkHighlight;
	
	public Box(int x, int y,String color) {
		this.x= x;
		this.y = y;
		this.color = color;
		hasPiece = false;
		highlighted = false;
	}
	public String getColor() {
		return color;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean hasPiece() {
		return hasPiece;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public void setHasPiece(boolean tf) {
		hasPiece = tf;
	}
	public Piece getPiece() {
		return piece;
	}
	public void setPiece(Piece piece) {
		this.piece = piece;
	}
	public boolean getIsHighlighted() {
		return highlighted;
	}
	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}
	public boolean getIsRockHighlight() {
		return rockHighlight;
	}
	public void setRockHighlight(boolean rockHighlight) {
		this.rockHighlight = rockHighlight;
	}
	public boolean getIsConsumeHighlight() {
		return consumeHighlight;
	}
	public void setConsumeHighlight(boolean consumeHighlight) {
		this.consumeHighlight = consumeHighlight;
	}
	public boolean getIsCheckHighlight() {
		return checkHighlight;
	}
	public void setCheckHighlight(boolean checkHighlight) {
		this.checkHighlight = checkHighlight;
	}
}
