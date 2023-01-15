package app;

import displayers.*;

public class Play {
    static final int BOX_SIZE = 64;
	
	public static void main(String[] args) {
		Board board = new Board();
		GameDisplayer gameDisplayer = new GameDisplayer(board);
	}
}
