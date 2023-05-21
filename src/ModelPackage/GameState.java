package ModelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;

//Data object that contains all the data about the game in it's current state:
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public static final int numOfTilesForPlayer=7; // game rule - 7 tiles for each player!
	final Bag bag;
	ArrayList<Player> listOfPlayers;
	Board gameBoard;
	private int indexOfCurrentTurnPlayer;
	
	
	
	public int getIndexOfCurrentTurnPlayer() {
		return indexOfCurrentTurnPlayer;
	}

	public GameState() {
		this.listOfPlayers= new ArrayList<Player>();
		bag=new Bag();
		gameBoard=new Board();
		indexOfCurrentTurnPlayer=0;
	}
	//increments the value of indexOfCurrentTurnPlayer - if we reached the end of the array -we return to zero(first player)
	protected void inc_indexOfCurrentTurnPlayer(){
		indexOfCurrentTurnPlayer++;
		if(indexOfCurrentTurnPlayer>=listOfPlayers.size()) {
			indexOfCurrentTurnPlayer=0;
		}
	}
	
	protected int getCurrentNumOfPlayers() {
		return this.listOfPlayers.size();
	}


}
