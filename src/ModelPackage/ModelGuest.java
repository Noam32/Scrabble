package ModelPackage;
import java.util.ArrayList;
import java.util.Observable;
import baseScrabble.Tile;
import baseScrabble.Word;


// This class is the model for the remote client - who doesn't have the game state - therefore it has to send requests 
//(currently TcpId to the host model!!)  

public class ModelGuest extends Observable implements Model {

	//*******************************
	//Get data method :
	//*******************************
	
	@Override
	public GameState getGameState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//Since I am the host 
	public int getNumOfPointsForPlayer(int playerId) {
		
		return 0;
	}

	@Override
	public int getNumOfPointsForPlayer(String name) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<Tile> getTilesForPlayer(int playerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Tile> getTilesForPlayer(String playerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Player WhoseTurnIsIt() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int WhoseTurnIsIt_Id() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean wasLastPlacementSuccessful() {
		// TODO Auto-generated method stub
		return false;
	}

	
	//*******************************
	//Change data method :
	//*******************************
	
	@Override
	public void addAplayer(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void givePlayerOneTile(int playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeWordOnBoard(Word w, Player currPlayer) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endPlayerTurn() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void skipPlayerTurn() {
		// TODO Auto-generated method stub
		
	}
	
}