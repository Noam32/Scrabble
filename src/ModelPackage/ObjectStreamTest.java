package ModelPackage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Word;
import baseScrabble.Tile.Bag;


public class ObjectStreamTest {

	public static void main(String[] args) {
		testObjectSerialazation();
	}
	
	
	
	public static  void startServer() throws IOException, ClassNotFoundException{
		ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Connected to server. ");
        System.out.println("Server started. Waiting for client...listening to port 1234");
        Socket clientSocket = serverSocket.accept();
        System.out.println("Server says: client connected to server.");
        //Create the ObjectStream:
        ObjectStream objStream=new ObjectStream(clientSocket);
        objStream.initInputStream();
        System.out.println("******************");
        System.out.println("server says : trying to read ArrayList<Tile> object");
        Object obj=objStream.readObject();
        if(obj instanceof ArrayList ) {
        	System.out.println("obj is of type ArrayList");
        	ArrayList<Tile> tiles=(ArrayList<Tile>)obj;
        	System.out.println("tiles receieved by server is : "+tiles.toString());
        }
        System.out.println("******************");
        //objStream.closeInputStream();
        //objStream.initInputStream();
        System.out.println("******************");
        System.out.println("server says : trying to read word object");
        obj=objStream.readObject();
        if(obj instanceof Word ) {
        	System.out.println("obj is of type Word");
        	Word w=(Word)obj;
        	System.out.println("Word receieved by server is : "+w.getString()+" or just:"+w.createSimpleString());
        }
        System.out.println("******************");
        
        //
        System.out.println("******************");
        System.out.println("\nserver says : trying to read GameSate object");
        obj=objStream.readObject();
        if(obj instanceof GameState ) {
        	System.out.println("obj is of type GameState");
        	GameState state=(GameState)obj;
        	System.out.println("GameState receieved by server is :");
        	printGameState(state);
        }
        System.out.println("******************");
        
        //
        System.out.println("******************");
        System.out.println("\nserver says : trying to read Player object");
        obj=objStream.readObject();
        if(obj instanceof Player ) {
        	System.out.println("obj is of type Player");
        	Player p1=(Player)obj;
        	System.out.println("Player received by server is :"+p1.toString());
        }
        System.out.println("******************");
        
        objStream.closeInputStream();
	}
	
	public static void startClient() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException {
		//GameState gamestate=new GameState();
		Socket server=new Socket("localhost",1234);//connect
		System.out.println("Clientsays:Connected to server.");
        // Receive and deserialize the object from the server
        ArrayList<Tile> list=new ArrayList<Tile>();
        Bag bag=new Bag();
        list.add(bag.getTile('A'));
        list.add(bag.getTile('B'));
        //creating stream object to send:
        ObjectStream objStream=new ObjectStream(server);
        objStream.initOutputStreams();
        System.out.println("client says : sending ArrayList<Tile> to server:");
        objStream.writeObjectOut(list);
        //
        System.out.println("client says : sending Word to server:");
        Word w=CreateWord();
        //Thread.sleep(1000);
        objStream.writeObjectOut(w);
        
        System.out.println("client says : sending GameState to server:");
        //creating game state:
        GameState state=new GameState();
        Player p1=new Player("Joe");
        p1.myTiles.add(state.bag.getTile('C'));
        p1.myTiles.add(state.bag.getTile('D'));
        state.listOfPlayers.add(p1);
        objStream.writeObjectOut(state);
        
        System.out.println("client says : sending Player to server:");
        Player p2=new Player("Moshiko");
        objStream.writeObjectOut(p2);
        //closing
         objStream.closeOutputStreams();
        
	}
	
	
	private static Word CreateWord() {
		
		 ModelHost model=new ModelHost();
		 model.addAplayer("player1");
		 model.addAplayer("player2");
		 model.initGame();
		 GameState gameState= model.getGameState();
		 Player player=gameState.listOfPlayers.get(0);
		 ArrayList<Tile> listOfTiles=player.getMyTiles();
		 Tile [] tileArr=Tile.arrayList_Tile_To_Arr(listOfTiles);
		 Word word=new Word(tileArr, 0, 0, false);
		 System.out.println("word before sending ="+word.getString());
		 model.localServer.close();
		 return word;
	}
	
	 private static void printGameState(GameState game) {
		 Bag bag=game.bag;
		 ArrayList<Player> listOfPlayers=game.listOfPlayers;
		 Board board=game.gameBoard;
		 int indexOfCurrentTurnPlayer=game.getIndexOfCurrentTurnPlayer();
		 System.out.println("-------------------------------------");
		 System.out.println("bag :"+bag.toString());
		 System.out.println("listOfPlayers :"+listOfPlayers.toString());
		 System.out.println("board"+board.toString());
		 System.out.println("indexOfCurrentTurnPlayer: "+indexOfCurrentTurnPlayer);
		 System.out.println("-------------------------------------");
		 
		
	 }
	
	
	public static GameState readGameState(ObjectStream objStream) throws IOException, ClassNotFoundException, InterruptedException{
        Object obj=objStream.readObject();
        GameState state=null;
        if(obj instanceof GameState ) {
        	System.out.println("obj is of type GameState");
        	 state=(GameState)obj;
        	System.out.println("GameState recieved by server is :");
        	printGameState(state);
        }
        return state;
	}
	
	
	public static void testObjectSerialazation() {
		
		Thread serverThread = new Thread(() -> {
			try {
				startServer();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        Thread clientThread = new Thread(() -> {
			try {
				startClient();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
        serverThread.start();
        clientThread.start();
        
	}

	

}