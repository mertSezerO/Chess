package engine;

public class Player {
    private boolean turn;
    private String color;

    public Player(String color){
        this.color = color;
        turn = (color.equals("white")) ? true : false;
    }

    public boolean getIsTurn(){
        return turn;
    }

    public void setIsTurn(boolean tf){
        turn = tf;
    }

    public String getColour(){
        return color;
    }
}
