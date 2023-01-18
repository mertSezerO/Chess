package engine;

import app.*;
import displayers.*;

public class Game {
    private GameDisplayer gameDisplayer;
    private Player whitePlayer;
    private Player blackPlayer;

    public Game(){
        Board board = new Board();
		this.whitePlayer = new Player("white");
		this.blackPlayer = new Player("black");
        this.gameDisplayer = new GameDisplayer(board,whitePlayer,blackPlayer);
    }
    
    public GameDisplayer getGameDisplayer(){
        return this.gameDisplayer;
    }

    public Player getWhitePlayer(){
        return whitePlayer;
    }

    public Player getBlackPlayer(){
        return blackPlayer;
    }
}
