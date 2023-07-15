package modelPackage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Random;
import java.util.Scanner;

import baseScrabble.Tile;
import baseScrabble.Word;
import server.ClientHandler;
import server.MyServer;


// This class is the model for the local host - who stores the game state and does the changes :

public class ModelHost extends Observable implements Model {
	private  GameState gamestate ;
	public MyServer localServer;//server that servers the guest players  
	private static boolean errorInLast_communication=false;//was there an error in the last TCP/IP communication
	private static boolean wasLastPlacementSuccessful=false;
	private final static int Num_Of_Players=2;
	public static final int playerIdNotFoundCode=-1;
	public boolean hasGameEnded=false;
	public Player myPlayer; //the player controlled by this Model
	public static int Host_PortFor_Communicating_With_Guests =8080;// Communicate
	private boolean hasGameStarted=false;

	
	public ModelHost(){
		this.gamestate=new GameState();
		this.addAplayer("HostPlayer");
		this.myPlayer=this.gamestate.listOfPlayers.get(0);
		initLocalServer();
		
	}
	
	//use this method when resuming a save game
	@Override
	public void resumeGame(GameState game) {
		this.gamestate=game;
		myPlayer=game.listOfPlayers.get(0);//0 is the host BY DEFAULT
		setChanged();
		this.notifyObservers("resume");
		//TODO wait for player to connect:
		//init local server ...
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
		int index =gamestate.getIndexOfCurrentTurnPlayer();
		return gamestate.listOfPlayers.get(index); //returns Player object
		 
	}

	@Override
	public int WhoseTurnIsIt_Id() {
		int index=gamestate.getIndexOfCurrentTurnPlayer();
		int id=gamestate.listOfPlayers.get(index).playerId;
		return id; //returns integer id.
	}

	@Override
	public boolean wasLastPlacementSuccessful() {
		return wasLastPlacementSuccessful;
	}
	@Override
	public boolean wasThereAnErrorAtLastCommunication() {
		return errorInLast_communication;
	}
	
	//*******************************
	//Change data method :
	//*******************************
	
	public void addAplayer(String name) {
		//Player p1=new Player(name);
		//this.gamestate.listOfPlayers.add(p1);
		//Changed the method to be inside the game state object:
		this.gamestate.addAPlayer(name);
		
	}

	@Override
	//this method should only be called once at the host:
	public void initGame() {
		decideOnOrderOfPlayers();
		giveAllPlayersSevenTiles();
		this.hasGameStarted=true;
		
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
		setChanged();
		this.notifyObservers("start");
	}
	
	@Override
	public void givePlayerOneTile(int playerId) {
		 int numOfPlayers = this.gamestate.listOfPlayers.size();
		  Tile t1=this.gamestate.bag.getRand();
		  for (int i = 0; i < numOfPlayers; i++) {
		   if(this.gamestate.listOfPlayers.get(i).playerId==playerId &&t1!=null)
		    this.gamestate.listOfPlayers.get(i).addTile(t1);
		  }
		
	}

	//Unfinished!
	@Override
	public void placeWordOnBoard(Word w) throws Exception {
		int numOfPoints = 0;
		try {
			numOfPoints = this.gamestate.gameBoard.tryPlaceWord(w);
		} catch (Exception e) {
			//take care of the Exception:if there was an exception it means that we failed to connect to the remote server.
			//Therefore we notify the user of the program that there was a communication error:
			String error_msg="error communicating with Dictionary server -cannot complete placeWordOnBoard() action";
			throw (new Exception (error_msg));
			
		}
		if (numOfPoints == 0) {
			wasLastPlacementSuccessful = false;
		}
		else {
			wasLastPlacementSuccessful = true;
			//update player's points
			int indexOfCurrentTurnPlayer = this.gamestate.getIndexOfCurrentTurnPlayer();
			Player CurrentTurnPlayer = this.gamestate.listOfPlayers.get(indexOfCurrentTurnPlayer);
			CurrentTurnPlayer.numOfPoints+=numOfPoints;
			//update his tiles array after place on board.
			ArrayList<Tile> MyTilesInHand = CurrentTurnPlayer.getMyTiles();
			Tile [] MyWordTiles = w.getTiles();
			for(int i=0;i<MyWordTiles.length;i++){
				MyTilesInHand.remove(MyWordTiles[i]);
			}
		}
		endPlayerTurn();
	}

	@Override
	public void endPlayerTurn() {
		// making sure that the player has 7 tiles:
		fillPlayersTileListToSeven(this.gamestate.getIdOfCurrentTurnPlayer());
		//Advancing the turn to the next player
		this.gamestate.inc_indexOfCurrentTurnPlayer();
		//notify every one that the turn is over .
		//change data:
		setChanged();
		this.notifyObservers();
	}

	
	@Override
	public void skipPlayerTurn() {
		// TODO Auto-generated method stub
		this.gamestate.inc_indexOfCurrentTurnPlayer();
		setChanged();
		this.notifyObservers();
	}
	
	
	/// Create a tcp/ip Server that will listen to guests looking to connect to the game:
	//should we create another class ? with client handler interface and etc ..?
	//
	
	private void fillPlayersTileListToSeven(int player_Id) {
		int indexInList=this.gamestate.getIndexOfPlayerWithId(player_Id);
		Player p1=this.gamestate.listOfPlayers.get(indexInList);
		int currNumOftile=p1.getMyTiles().size();
		int numOfTilesToAdd=7-currNumOftile;
		for(int i=0;i<numOfTilesToAdd;i++) {
			givePlayerOneTile(player_Id);
		}
		
	}
	
	//initializing the server that will handle requests from the guest players:
	public void initLocalServer() {
		int maxNumOfPorts=4;
		int portTolistenTo=Host_PortFor_Communicating_With_Guests;
		
		GuestClientHandler clientHandler_ForModelHost= new GuestClientHandler(this,Num_Of_Players);
		//passing the handler to handle string requests from the guests:
		this.localServer=new MyServer(portTolistenTo, clientHandler_ForModelHost, maxNumOfPorts);
		this.localServer.start();
		
	}
	
	
	//This method creates a client that will communicate with the Dictionary 'remote' server.
	//Q_or_C = 'Q' for query and 'C' for challenge
	//throws exception if connection to the DictionaryServer failed !
	public static Boolean runClientToDictionaryServer(int port,char Q_or_C ,String stringTosearch) throws Exception{
		String bookNames="mobydick.txt"+","+"alice_in_wonderland.txt"+","+"Frank Herbert - Dune.txt"+","+"Harray Potter.txt";
		try {
			Socket server=new Socket("localhost",port);
			PrintWriter out=new PrintWriter(server.getOutputStream());
			Scanner in=new Scanner(server.getInputStream());
			//template is : "Q,bookNames1,bookName2,...,stringTosearch"
			String stringToSend=Q_or_C+","+bookNames+","+stringTosearch;
			System.out.println("runClientToDictionaryServer:sending \""+ stringToSend+"\"");
			//We send 2 string - one is all upper case - and one is all lower case 
			//- if one of the challenges returns true-we return true:
			out.println(stringToSend);//here we are sending the query/challenge string to the Client handler server
			out.flush();
			//System.out.println("in.hasNext()= "  +in.hasNext());
			String res=in.next();//here we are receiving the query/challenge *result* string from the Client handler server
			Boolean boolRes=Boolean.parseBoolean(res);
			//closing :
			in.close();
			out.close();
			server.close();
			///returning :
			return boolRes;
		} catch (IOException e) {
			System.out.println("your code ran into an IOException ");
			e.printStackTrace();
			System.out.println("is port available = " +isTcpPortAvailable(port) );
			throw e;//query failed
		}
	}
	
	private static boolean isTcpPortAvailable(int port) {
		try (ServerSocket serverSocket = new ServerSocket()) {
			// setReuseAddress(false) is required only on macOS,
			// otherwise the code will not work correctly on that platform
			serverSocket.setReuseAddress(false);
			serverSocket.bind(new InetSocketAddress(InetAddress.getByName("localhost"), port), 1);
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	
	public boolean hasGameStarted() {
		return this.hasGameStarted;
	}
	
	//afterwards create a with threadpool? (or as a queue) a client handler 
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}