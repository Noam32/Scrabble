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
