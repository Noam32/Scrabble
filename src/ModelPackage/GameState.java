package ModelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;

//Data object that contains all the data about the game in it's current state:
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public static final int numOfTilesForPlayer=7; // game rule - 7 tiles for each player!
	public final Bag bag;
	public ArrayList<Player> listOfPlayers;
	public ConnectedBoard gameBoard;
	private int indexOfCurrentTurnPlayer;
	
	public HashMap<String,Integer> hashmap_name_to_id;
	
	
	public ConnectedBoard getBoard() {
		return this.gameBoard;
	}
	
	public int getIndexOfCurrentTurnPlayer() {
		return indexOfCurrentTurnPlayer;
	}

	public GameState() {
		this.listOfPlayers= new ArrayList<Player>();
		hashmap_name_to_id=new HashMap<>();
		bag=new Bag();
		gameBoard=new ConnectedBoard();
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
	protected int getIdOfCurrentTurnPlayer() {
		int indexInList=this.indexOfCurrentTurnPlayer;
		int id=this.listOfPlayers.get(indexInList).playerId;
		return id;
	}
	
	public int getIndexOfPlayerWithId(int id) {
		int numOfPlayers =this.listOfPlayers.size();

		for (int i = 0; i < numOfPlayers; i++) {
			   if(this.listOfPlayers.get(i).playerId==id) {
				   return i;
			   }
		}
		return -1; // if not found in players list.
	}

	
	public Player getPlayerWithName(String name) {
		//Player p1;
		for(Player p : this.listOfPlayers) {
			if(p!=null&&p.name.equals(name)) {
				return p;
			}
		}
		return null;
	}
	
	public void addAPlayer(String name) {
		Player p1=new Player(name);
		this.listOfPlayers.add(p1);
		//Adding to the hashmap
		this.hashmap_name_to_id.put(name, p1.playerId);
	}
	public int getIdofPlayerName(String playerName) {
		Integer id=this.hashmap_name_to_id.get(playerName);
		return (int)id;
	}
	//returns a string showing each player's name and number of points.
	//also we mark Whose turn is it now
	public String[] getStringOfScoreBoard() {
		String [] strArr=new String[this.listOfPlayers.size()];
		for(int i=0;i<strArr.length;i++) {
			Player currPlayer=this.listOfPlayers.get(i);
			strArr[i]=currPlayer.name+":"+currPlayer.numOfPoints;
			if(i==indexOfCurrentTurnPlayer) {
				strArr[i]=strArr[i]+"(playing)"; //indicating Whose turn is it now.
			}
		}
		return strArr;
	}

}
