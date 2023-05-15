package ModelPackage;

import java.util.ArrayList;
import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;

//Data object that contains all the data about the game in it's current state:
public class GameState {
	final Bag bag;
	ArrayList<Player> listOfPlayers;
	Board gameBoard;
	int indexOfCurrentTurnPlayer;
	
	
	public GameState() {
		ArrayList<Player> listOfPlayers= new ArrayList<Player>();
		bag=new Bag();
		gameBoard=new Board();
		indexOfCurrentTurnPlayer=0;
	}

}
