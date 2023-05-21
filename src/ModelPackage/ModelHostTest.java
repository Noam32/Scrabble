package ModelPackage;

import java.awt.PrintGraphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.stream.Stream;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
import baseScrabble.Word;

public class ModelHostTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		testInitGame();
		try {
			TestSerialization();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	 static void testInitGame() {
		//testing init game:
		ModelHost model_host=new ModelHost();
		System.out.println(model_host.getGameState().listOfPlayers);
		model_host.addAplayer("player1");
		model_host.addAplayer("player2");
		model_host.addAplayer("player3");
		model_host.addAplayer("player4");
		model_host.addAplayer("player5");
		//initialize game:
		System.out.println(model_host.getGameState().listOfPlayers); 
		model_host.initGame();
		System.out.println(model_host.getGameState().listOfPlayers); 
		//printing to verify:
		for(int i=0;i<model_host.getGameState().getCurrentNumOfPlayers();i++) {
			Player currPlayer=model_host.getGameState().listOfPlayers.get(i);
			for(int j=0;j<7;j++) {
				System.out.print(currPlayer.myTiles.get(j).letter);
			}
		}
	}
	 
	 //serialization 
	 public static void TestSerialization() throws IOException, ClassNotFoundException {
		 System.out.println("\n \n*******************\nTesting serialazation of objects");
		 //creating the objects:
		 ModelHost model=new ModelHost();
		 Player p1=new Player("player1_moshe");
		 //Player p1=new Player("player3_natan");
		 model.addAplayer("player4");
		 model.addAplayer("player2");
		 model.addAplayer("player29812");
		 GameState gameState=model.getGameState();
		 //model.givePlayerOneTile(0);
		 model.initGame();
		 gameState.inc_indexOfCurrentTurnPlayer();
		 System.out.println(gameState.listOfPlayers.get(0).getMyTiles().size());
		 System.out.println("game state before");
		 printGameState(gameState);
		 //**Writing object:
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(gameState);
	     byte[] serializedObject1 = baos.toByteArray();
	    //**Reading object:
	     ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject1);
	     ObjectInputStream oInputStream = new ObjectInputStream(bis);
	     GameState restoredGameState = (GameState) oInputStream.readObject();  
	     //
	     System.out.println(restoredGameState.getIndexOfCurrentTurnPlayer());
	     System.out.println(restoredGameState.listOfPlayers.get(0).getMyTiles().size());
	     
	     System.out.println("game state after");
		 printGameState(restoredGameState);
	     
		 //closing :
		 baos.close();
		 oos.close();
		 bis.close();//closing prev stream
		 oInputStream.close();
		 
		 
		 //Now testing  Word object Serialization:
		 //Creating the word from some players current random tiles in hand:
		 System.out.println("**Now testing  Word object Serialization**");
		 Player player=gameState.listOfPlayers.get(0);
		 ArrayList<Tile> listOfTiles=player.getMyTiles();
		 Tile [] tileArr=Tile.arrayList_Tile_To_Arr(listOfTiles);
		 Word word=new Word(tileArr, 0, 0, false);
		 System.out.println("word before sending ="+word.getString());
		 //now sending:
		 baos = new ByteArrayOutputStream();
		 oos = new ObjectOutputStream(baos);
		 oos.writeObject(word);
		 byte[] serializedWord = baos.toByteArray();
		//Reading object:
		 bis=new ByteArrayInputStream(serializedWord);
		 oInputStream = new ObjectInputStream(bis);
		 Word restoredWord=(Word)oInputStream.readObject();
		 System.out.println("word AFTER sending ="+restoredWord.getString());
		 System.out.println("restoredWord =" +restoredWord.createSimpleString());
		 if(restoredWord.createSimpleString().equals(word.createSimpleString()))
			 if(word.getRow()==restoredWord.getRow()&&word.getCol()==restoredWord.getCol()&&word.isVertical()==restoredWord.isVertical())
				 System.out.println("Sending Words worked");
		 ////closing :
		 baos.close();
		 oos.close();
		 bis.close();//closing prev stream
		 oInputStream.close();
		 
		 
		 ///Now testing  Array list Tile serialization by itself:
		 System.out.println("**Now testing  Array list Tile serialization**");
		 //sending listOfTiles :
		 
		 System.out.println("end of TestSerialization");
		 System.out.println("************************");
		 
	 }
	 
	 public static void initObjectOutStreams() {
		// baos = new ByteArrayOutputStream();
		 //oos = new ObjectOutputStream(baos);
		 
	 }
	 
	 
	 
	 public static class ObjectStream{
		 //output;
		 ByteArrayOutputStream baos;
		 ObjectOutputStream oos;
		 //input:
		 ByteArrayInputStream bis;
		 ObjectInputStream oInputStream;
		 //socket to init objects:
		 Socket mySocket;
		 
		 
		 
		 public ObjectStream(Socket s) {
			 this.mySocket=s;
		 }
		 
		 public void initOutputStreams() throws IOException {
			 baos = new ByteArrayOutputStream();
		     oos = new ObjectOutputStream(baos);
		 }
		 public void writeObjectOut(Object obj) throws IOException {
			 oos.writeObject(obj);
		 }
		 
		 
		 
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
	
//Template:
	 /*
	 try 
{
    // To String
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    ObjectOutputStream os = new ObjectOutputStream(bos);
    os.writeObject(object1);
    String serializedObject1 = bos.toString();
    os.close();

    // To Object 
    ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject1.getBytes());
    ObjectInputStream oInputStream = new ObjectInputStream(bis);
    YourObject restoredObject1 = (YourObject) oInputStream.readObject();            

    oInputStream.close();
} catch(Exception ex) {
    ex.printStackTrace();
}
	 */
}
