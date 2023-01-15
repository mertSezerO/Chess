package app;

import displayers.*;
/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import displayers.*;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;*/

public class main{
	static final int BOX_SIZE = 64;
	
	public static void main(String[] args) {
		Board board = new Board();
		GameDisplayer gameDisplayer = new GameDisplayer(board);
	}

}

