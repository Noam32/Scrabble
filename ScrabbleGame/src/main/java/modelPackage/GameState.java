package modelPackage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
import org.bson.Document;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoException;
import org.bson.Document;
import com.mongodb.client.model.Filters;


//Data object that contains all the data about the game in its current state:
public class GameState implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public static final int numOfTilesForPlayer=7; // game rule - 7 tiles for each player!
	public  Bag bag;
	public ArrayList<Player> listOfPlayers;
	public ConnectedBoard gameBoard;
	private int indexOfCurrentTurnPlayer;
	
	public HashMap<String,Integer> hashmap_name_to_id;
	public String gameSaveName="placeHoldergameSaveName";
	
	
	public ConnectedBoard getBoard() {
		return this.gameBoard;
	}
	
	public int getIndexOfCurrentTurnPlayer() {
		return indexOfCurrentTurnPlayer;
	}

	private void setIndexOfCurrentTurnPlayer(int index) {
		this.indexOfCurrentTurnPlayer=index;
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
	
	//These methods compare a GameState object to another game state object
	//if there is a change in any of the variables - we return true
	//otherwise we return false
	public boolean equals(GameState previousState) {
		boolean res =true;
		//checking changes in the tiles Bag:
		int []bagQuatities_original =bag.getQuantities();
		int []bagQuatities_other =previousState.bag.getQuantities();
		for(int i=0;i<bagQuatities_original.length;i++) {
			if(bagQuatities_original[i]!=bagQuatities_other[i]) {
				return false;
			}
		}
		//checking changes in the listOfPlayers:
		ArrayList<Player> listOfPlayer_other=previousState.listOfPlayers;
		if(listOfPlayer_other.size()!=this.listOfPlayers.size()) {
			return false;
		}
		for(int i=0;i<listOfPlayers.size();i++) {
			Player currPlayer=this.listOfPlayers.get(i);
			Player currPlayer_other=listOfPlayer_other.get(i);
			if(!currPlayer.equals(currPlayer_other)) {
				return false;
			}
		}
		//checking changes in the gameBoard:
		ConnectedBoard currBoard=this.gameBoard;
		ConnectedBoard otherBoard=previousState.gameBoard;
		if(!currBoard.equals(otherBoard)) {
			return false;
		}
		if(this.indexOfCurrentTurnPlayer!=previousState.indexOfCurrentTurnPlayer) {
			return false;
		}
		return res;
	}

	// methods to connect to Mongo DB send /retrieve :
	// Method to convert GameState to a MongoDB document
	// Method to convert GameState to a MongoDB document
	public Document toDocument() {
		Document document = new Document();
		document.append("bag", bag.toDocument());
		document.append("listOfPlayers", embbeddedDocPlayers());
		document.append("gameBoard", gameBoard.toDocument());
		document.append("indexOfCurrentTurnPlayer", indexOfCurrentTurnPlayer);
		document.append("hashmap_name_to_id", hashmap_name_to_id);
		document.append("gameSaveName", gameSaveName);
		//document.append("fieldname",document);
		return document;
	}


	public Document embbeddedDocPlayers() {
		Document document = new Document();
		for(int i=0;i<this.listOfPlayers.size();i++) {
			document.append(""+i, this.listOfPlayers.get(i).toDocument());
		}
		return document;
	}

	// Method to save the GameState document to MongoDB collection(table)
	public void saveToMongoDB(MongoCollection<Document> collection) {
		Document document = this.toDocument();
		collection.insertOne(document);
	}
	// Method to retrieve the GameState from a MongoDB document
	public static GameState  readGameStatefromDocument(Document game_document) {
		GameState game = new GameState();
		//getting the simple fields
		game.indexOfCurrentTurnPlayer=game_document.getInteger("indexOfCurrentTurnPlayer");
		game.gameSaveName=game_document.getString("gameSaveName");
		//getting the complex object fields
		try {
			// game.hashmap_name_to_id=(HashMap<String,Integer>)game_document.get("hashmap_name_to_id", HashMap.class);
			//game.hashmap_name_to_id = new HashMap<String,Integer>(game_document.get("hashmap_name_to_id", Document.class));
			Document hashMapDoc=game_document.get("hashmap_name_to_id",Document.class);
			game.hashmap_name_to_id=getHashmapFromDocument(hashMapDoc);
			//bag:
			Document bag_document=game_document.get("bag",Document.class);
			game.bag=Bag.fromDocument(bag_document);
			//listOfplayers:
			Document playerListDoc=game_document.get("listOfPlayers", Document.class);
			game.listOfPlayers=getListOfPlayersFromDocument(playerListDoc);
			//gameBoard:
			Document gameBoardDoc=game_document.get("gameBoard", Document.class);
			game.gameBoard=ConnectedBoard.fromDocument(gameBoardDoc);



		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return game;

	}

	//reading the list of players from document one by one and creating the arraylist :
	private static ArrayList<Player> getListOfPlayersFromDocument(Document playerListDoc) {
		ArrayList<Player> list_of_players=new ArrayList<Player>();
		int maxNumOfplayers=4;
		boolean endLoopFlag=false;
		//iterating over the indices and getting the inner documents each representing a player:
		for(int i=0;i<maxNumOfplayers&& !endLoopFlag;i++) {
			Document currDocument =playerListDoc.get(""+i, Document.class);
			if(currDocument==null) {
				endLoopFlag=true;
			}
			else {
				list_of_players.add(Player.fromDocument(currDocument));
			}
		}
		return list_of_players;
	}

	private static HashMap<String,Integer> getHashmapFromDocument(Document hashmapDocument){
		HashMap<String,Integer> map=new HashMap<>();

		if (hashmapDocument != null) {
			for (String key : hashmapDocument.keySet()) {
				Integer value = hashmapDocument.getInteger(key);
				map.put(key, value);
			}
		}
		return map;

	}



}
