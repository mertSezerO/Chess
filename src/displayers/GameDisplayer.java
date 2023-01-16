package displayers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


import java.util.*;

import app.*;

public class GameDisplayer{
	private Board board;
	
	private Color lightTileColor = new Color(254,240,187);
	private Color darkTileColor = new Color(17,12,12);
	private Color consumeHighlight = Color.RED;
	private Color rockHighlight = Color.GREEN;
	private Color possibleTilesHighlight = Color.BLUE;

	
	private final JFrame gameFrame;
	private final BoardPanel boardPanel;
	
	private Box sourceTile;
	private Box destinationTile;
	
	private static final Dimension BOARD_SCALE = new Dimension(400,350);
	private static final Dimension SCALE = new Dimension(600,600);
	private static final Dimension TILE_SCALE = new Dimension(10,10);
	
	public GameDisplayer(Board board) {
		this.board = board;
		this.gameFrame = new JFrame("Chess");
		this.gameFrame.setLayout(new BorderLayout());
		this.gameFrame.setSize(SCALE);
		
		this.boardPanel = new BoardPanel();
		this.gameFrame.add(boardPanel,BorderLayout.CENTER);
		this.gameFrame.setVisible(true);
	}
	
	public void highlight(int x, int y) {
		board.highlight(x, y);
		boardPanel.removeAll();
		boardPanel.setTable();
	}
	
	public void startOver() {
		board.normalize();
		boardPanel.removeAll();
		boardPanel.setTable();
	}
	
	//helper method to visualize board
	public void updateTable(int dest_x, int dest_y) {
		board.move(dest_x,dest_y);
		startOver();
	}
	
	//overloaded method
	public void updateTable(int king_x,int king_y,int fin_x,int fin_y) {
		board.rock(king_x, king_y, fin_x, fin_y);
		startOver();
	}
	
	
	private class BoardPanel extends JPanel{

		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8,8));
			this.boardTiles = new ArrayList<>();
			setTable();
		}
		
		private void setTable() {
			for(int i=0;i<64;i++) {
				final TilePanel tilePanel = new TilePanel(this,i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			setPreferredSize(BOARD_SCALE);
			validate();
		}
		
	}
	
	private class TilePanel extends JPanel {
		private int locationX;
		private int locationY;
		
		TilePanel(final BoardPanel boardPanel,int tileId) {
			super(new GridBagLayout());
			this.locationX = tileId/8;
			this.locationY = tileId-locationX*8;
			assignTileColor();
			assignPieceImages(board);
			setPreferredSize(TILE_SCALE);
			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if(javax.swing.SwingUtilities.isRightMouseButton(e)) {
						if(sourceTile != null) {
							sourceTile = null;
							startOver();
						}
					}
					else if(javax.swing.SwingUtilities.isLeftMouseButton(e)) {
						if(sourceTile == null) {
							sourceTile = board.getBox(locationX, locationY);
							if(!sourceTile.hasPiece())
								sourceTile = null;
							else
								highlight(locationX, locationY);
						}
						else {
							destinationTile= board.getBox(locationX, locationY);
							if(destinationTile.getPiece() != null && destinationTile.getPiece().getColor().equals(sourceTile.getPiece().getColor())){
								sourceTile = destinationTile;
								startOver();
								highlight(sourceTile.getX(), sourceTile.getY());
							}
							else{
								if(destinationTile.getIsConsumeHighlight() || destinationTile.getIsHighlighted()){
									updateTable(locationX, locationY);
									sourceTile = null;
								}
								else if(destinationTile.getIsRockHighlight()){
									updateTable(sourceTile.getX() , sourceTile.getY(), destinationTile.getX(), destinationTile.getY());
									sourceTile = null;
								}
								else
									destinationTile = null;
							}
						}
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					
					
				}
			});
			validate();
		}

		private void assignTileColor() {
			if(board.getBox(locationX, locationY).getIsConsumeHighlight()) {
				setBackground(consumeHighlight);
			}
			else if(board.getBox(locationX, locationY).getIsHighlighted()) {
				setBackground(possibleTilesHighlight);
			}
			else if(board.getBox(locationX, locationY).getIsRockHighlight()) {
				setBackground(rockHighlight);
			}
			else
				setBackground(((locationX+locationY)%2 == 0) ? lightTileColor : darkTileColor);
		}
		
		private void assignPieceImages(Board board) {
			this.removeAll();
			Piece piece = board.getBox(locationX, locationY).getPiece();
			if(piece != null) {
				try
				{
					BufferedImage image = ImageIO.read(new File("Images/" + piece.getColor() + " "+ piece.getTag() + ".gif"));
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					System.out.println("CATCHED");
				}
			}
		}
		
	}
}
