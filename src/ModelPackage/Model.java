package ModelPackage;

import java.util.ArrayList;

import baseScrabble.Tile;
import baseScrabble.Word;

public interface Model {
	
	//*******************************
	//get data :
	//*******************************
	public GameState getGameState();
	public int getNumOfPointsForPlayer(int playerId);
	
	public int getNumOfPointsForPlayer(String name); //assuming name is unique
	public ArrayList<Tile> getTilesForPlayer(int playerId);
	public ArrayList<Tile> getTilesForPlayer(String playerId);//assuming name is unique
	public Player WhoseTurnIsIt(); // returns which player has his turn now(returns player object)
	public int WhoseTurnIsIt_Id(); //returns which player has his turn now (integer - players Id) 
	//After the view Model calls placeWordOnBoard() - waits to be notified and then checks if the placement succeeded
	public boolean wasLastPlacementSuccessful();
	
	//*******************************
	//change data:
	//*******************************
	//---start of game: ---
	public void addAplayer(String name); // adds player object to game state
	//public void decideOnOrderOfPlayer(); //logic - draw random numbers and decide on turns - in the array list.
	//public void giveAllPlayersSevenTiles();//At the start of the game
	public void initGame(); //decideOnOrderOfPlayer() and giveAllPlayersSevenTiles().
	
	//---During the game - turn by turn : ---
	public void givePlayerOneTile(int playerId);
	//We need to send notification to let the model view know if the placement succeeded
	public void placeWordOnBoard(Word w , Player currPlayer);
	//sums the points - updates game's state-gives the player tiles (until it has 7 tiles) and moves to the next player.
	public void endPlayerTurn(); 
	//if a player skips his turn - we just give the turn to the next player:
	public void skipPlayerTurn(); //
	
	
	
}
