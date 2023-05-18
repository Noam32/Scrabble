package ModelPackage;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;

import baseScrabble.Tile;
import baseScrabble.Word;
import server.ClientHandler;
import server.MyServer;


// This class is the model for the local host - who stores the game state and does the changes :

public class ModelHost extends Observable implements Model {
	private  GameState gamestate ;
	public MyServer localServer;//server that servers the guest players
	
	private static boolean wasLastPlacementSuccessful=false;
	
	public static final int playerIdNotFoundCode=-1;
	
	
	public ModelHost(){
		this.gamestate=new GameState();
		initLocalServer();
	}
	
	//*******************************
	//Get data method :
	//*******************************
	
	@Override
	//Is this problematic?
	public GameState getGameState() {
		return this.gamestate;
	}

	@Override
	//Since I am the host :
	//If playerId is not found returns -1;
	public int getNumOfPointsForPlayer(int playerId) {
		int numOfPlayers = this.gamestate.listOfPlayers.size();
		for (int i = 0; i < numOfPlayers; i++) {
			if(this.gamestate.listOfPlayers.get(i).playerId==playerId)
				return this.gamestate.listOfPlayers.get(i).numOfPoints;
		}
		return playerIdNotFoundCode;
	}

	@Override
	public int getNumOfPointsForPlayer(String name) {
		int numOfPlayers = this.gamestate.listOfPlayers.size();
		for (int i = 0; i < numOfPlayers; i++) {
			if(this.gamestate.listOfPlayers.get(i).name.equals(name))
				return this.gamestate.listOfPlayers.get(i).numOfPoints;
		}
		return playerIdNotFoundCode;
	}

	@Override
	public ArrayList<Tile> getTilesForPlayer(int playerId) {
		int numOfPlayers = this.gamestate.listOfPlayers.size();
		for (int i = 0; i < numOfPlayers; i++) {
			if(this.gamestate.listOfPlayers.get(i).playerId==playerId)
				return this.gamestate.listOfPlayers.get(i).getMyTiles();
		}
		return null;
	}

	@Override
	public ArrayList<Tile> getTilesForPlayer(String name) {
		int numOfPlayers = this.gamestate.listOfPlayers.size();
		for (int i = 0; i < numOfPlayers; i++) {
			if(this.gamestate.listOfPlayers.get(i).name.equals(name))
				return this.gamestate.listOfPlayers.get(i).getMyTiles();
		}
		return null;
	}

	@Override
	public Player WhoseTurnIsIt() {
		int index =gamestate.indexOfCurrentTurnPlayer;
		return gamestate.listOfPlayers.get(index);
		 
	}

	@Override
	public int WhoseTurnIsIt_Id() {
		return gamestate.indexOfCurrentTurnPlayer;
	}

	@Override
	public boolean wasLastPlacementSuccessful() {
		return wasLastPlacementSuccessful;
	}

	//*******************************
	//Change data method :
	//*******************************
	
	public void addAplayer(String name) {
		Player p1=new Player(name);
		this.gamestate.listOfPlayers.add(p1);
		
	}

	@Override
	//this method should only be called once at the host:
	public void initGame() {
		decideOnOrderOfPlayers();
		giveAllPlayersSevenTiles();
		
	}
	
	//helper method to initGame - changes the order of the players in the array list according to a draw /
	private void decideOnOrderOfPlayers() {
		int numOfplayers=this.gamestate.listOfPlayers.size();
		//ArrayList<Player> tilesForDraw=(ArrayList<Player>) this.gamestate.listOfPlayers.clone();
		Random rand =new Random();
		//randomly sorting the listOfplayers twice:
		this.gamestate.listOfPlayers.sort((Player p1,Player p2)->rand.nextInt(2)-1);
		//randomly sorting the listOfplayers twice:
		this.gamestate.listOfPlayers.sort((Player p1,Player p2)->rand.nextInt(2)-1);

	}
	
	//helper method to initGame - we give each player 7 tiles:
	private void giveAllPlayersSevenTiles() {
		int howManyTilesToDraw=GameState.numOfTilesForPlayer;
		//We iterate over all players:
		for(int i=0;i<this.gamestate.listOfPlayers.size();i++) {
			//We draw 7 tiles for each player:
			for(int j=0;j<howManyTilesToDraw;j++) {
				Tile t1=this.gamestate.bag.getRand();
				this.gamestate.listOfPlayers.get(i).addTile(t1);
			}
		}
	}
	
	@Override
	public void givePlayerOneTile(int playerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeWordOnBoard(Word w) {
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
	
	
	/// Create a tcp/ip Server that will listen to guests looking to connect to the game:
	//should we create another class ? with client handler interface and etc ..?
	//
	
	
	
	
	public void initLocalServer() {
		int maxNumOfPorts=4;
		int portTolistenTo=8080;
		

		
		this.localServer=new MyServer(portTolistenTo, null, maxNumOfPorts);
		
	}
	
	//afterwards create a with threadpool? (or as a queue) a client handler 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
