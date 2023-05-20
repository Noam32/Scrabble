package ModelPackage;

import java.awt.PrintGraphics;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import baseScrabble.Board;
import baseScrabble.Tile.Bag;

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
		 gameState.indexOfCurrentTurnPlayer++;
		 System.out.println(gameState.listOfPlayers.get(0).getMyTiles().size());
		 System.out.println("game state before");
		 printGameState(gameState);
		 //Writing object:
		 ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(gameState);
	     byte[] serializedObject1 = baos.toByteArray();
	    //Reading object:
	     ByteArrayInputStream bis = new ByteArrayInputStream(serializedObject1);
	     ObjectInputStream oInputStream = new ObjectInputStream(bis);
	     GameState restoredGameState = (GameState) oInputStream.readObject();  
	     //
	     System.out.println(restoredGameState.indexOfCurrentTurnPlayer);
	     System.out.println(restoredGameState.listOfPlayers.get(0).getMyTiles().size());
	     
	     System.out.println("game state after");
		 printGameState(restoredGameState);
	     
	 }
	 
	 private static void printGameState(GameState game) {
		 Bag bag=game.bag;
		 ArrayList<Player> listOfPlayers=game.listOfPlayers;
		 Board board=game.gameBoard;
		 int indexOfCurrentTurnPlayer=game.indexOfCurrentTurnPlayer;
		 System.out.println("-------------------------------------");
		 System.out.println("bag :"+bag.toString());
		 System.out.println("listOfPlayers :"+listOfPlayers.toString());
		 System.out.println("board"+board.toString());
		 System.out.println("indexOfCurrentTurnPlayer: "+indexOfCurrentTurnPlayer);
		 System.out.println("-------------------------------------");
		 
		
	 }
	

}
