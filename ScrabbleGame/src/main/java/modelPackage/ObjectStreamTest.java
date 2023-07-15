package modelPackage;

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
		System.out.println("\n*******This is the ObjectStreamTest*******");
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
       //Important:starting the streams in reverse order in server and client.
        //server:in then out.client:out then in
        objStream.initInputStream();
        objStream.initOutputStreams();
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
        	System.out.println("GameState receieved by server is : ");
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
        System.out.println("******************%%%%%");
        System.out.println("\nserver says : trying to read String object");
        String stringFromStream= objStream.readString();
        System.out.println("String(object) received by server is :"+stringFromStream.toString());
        
        System.out.println("\n*********!!!!!!!*********%%%%%");
        System.out.println("\nserver says : trying to read tile[][] object");
        Tile[][] tileArr= (Tile[][])objStream.readObject();
        System.out.println("Tile[][](object) received by server is :");
        System.out.println(printdArr(tileArr));
        
        /// cahnging the board inside gameState and sending it
        
        objStream.closeInputStream();
        System.out.println("***End of test :ObjectStreamTest***");
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
        objStream.initInputStream();
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
        System.out.println("client says : sending String (object) to server:");
        String str="test for string sending through object stream \nsecond line\nthirdlind";
        objStream.sendString(str);
        
        System.err.println("client says : sending Tile[][] (object) to server:");
        Tile [][]tilesArr=new Tile[15][15];
        Tile t1=new Tile('S',1);
        tilesArr[7][7]=t1;
        t1=new Tile('H',1);
        tilesArr[8][7]=t1;
        t1=new Tile('I',1);
        tilesArr[9][7]=t1;
        t1=new Tile('P',1);
        tilesArr[10][7]=t1;
        System.err.println("client says : Im sending this arr +"+printdArr(tilesArr));
        objStream.writeObjectOut(tilesArr);
        
        
         objStream.closeOutputStreams();
        
	}
	
	
	private static Word CreateWord() {
		 Bag b1=new Bag();
		 ArrayList<Tile> listOfTiles=new ArrayList<Tile>();
		 listOfTiles.add(b1.getRand());
		 listOfTiles.add(b1.getRand());
		 listOfTiles.add(b1.getRand());
		 Tile [] tileArr=Tile.arrayList_Tile_To_Arr(listOfTiles);
		 Word word=new Word(tileArr, 0, 0, false);
		 System.out.println("word before sending ="+word.getString());
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
        try {Thread.sleep(2000);} catch (InterruptedException e1) {e1.printStackTrace();}
        clientThread.start();
        
	}

	
	public static String printdArr(Tile [] []tiles) {
		String str="{The Board 2d tile array:\n";
		for(int i=0;i<15;i++) {
			for(int j=0;j<15;j++) {
				Tile currTile=tiles[i][j];
				if(currTile==null) {
					str+="{null}";
				}
				else {
					str+=currTile.toString();
				}
			}
			str+="\n";
		}
		str+="}";
		return str;
	}
	

}
