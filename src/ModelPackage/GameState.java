package ModelPackage;

import java.util.ArrayList;
import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;

//Data object that contains all the data about the game in it's current state:
public class GameState {
	public static final int numOfTilesForPlayer=7; // game rule - 7 tiles for each player!
	final Bag bag;
	ArrayList<Player> listOfPlayers;
	Board gameBoard;
	int indexOfCurrentTurnPlayer;
	
	
	
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
