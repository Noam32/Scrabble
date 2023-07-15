package modelPackage;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
import baseScrabble.Word;

import java.io.IOException;
import java.util.ArrayList;

//this test has to create a host and a guest, and communicate between them.
//It has to show that the guest can request the host to do some operations of the game, and get the result back.
// IMportant : this test only runs if the RunServer is active (just run the RunServer.java and then this) 
public class ModelGuestTest {


    public static void main(String[] args) {
 
    	ModelHost myHost =new ModelHost();
        ModelGuest myGuest=new ModelGuest("Moshe");
        try {
            Thread.sleep(500);}
         catch (InterruptedException e) {throw new RuntimeException(e);}
        System.out.println(myHost.getGameState().listOfPlayers);

        //test for getNumOfPointsForPlayer(String)
        int numOfPoints = myGuest.getNumOfPointsForPlayer("Moshe");
        System.out.println("the num of points =:"+numOfPoints);

        //test for getNumOfPointsForPlayer(int)
        int numOfPoints2 = myGuest.getNumOfPointsForPlayer(1);
        System.out.println("the num of points =:"+numOfPoints);

        //test for wasLastPlacementSuccessful
        boolean b1 = myGuest.wasLastPlacementSuccessful();
        System.out.println("wasLastPlacementSuccessful returned ="+b1);
        
      //test for addAplayer
        myGuest.addAplayer("Yossi");
        System.out.println(myHost.getGameState().listOfPlayers);
        
        
        //test for WhoseTurnIsIt_Id
        int turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's"+" the player with id = "+turn+" "+"turn");
        
        
      //test for WhoseTurnIsIt
        Player p1 = myGuest.WhoseTurnIsIt();
        if (p1==null){
            System.out.println("null was returned");
        }
        else {
            System.out.println("it's the turn of:"+p1);
        }
        System.out.println("\n**********Void methods ***********");
        //test endPlayerTurn()
        myGuest.endPlayerTurn();
        turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's" + " the player with id = " + turn + " " + "turn");

        //test skipPlayerTurn()
        myGuest.skipPlayerTurn();
        turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's" + " the player with id = " + turn + " " + "turn");
      //test skipPlayerTurn() again to see that the first player gets his turn back:
        myGuest.skipPlayerTurn();
        turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's" + " the player with id = " + turn + " " + "turn");
        
        
        

        //test for getTilesForPlayer(int)
        ArrayList<Tile> myTiles =  myGuest.getTilesForPlayer(1);
        if (myTiles==null){
            System.out.println("null was returned");
        }
        else {
            System.out.println("my Tiles are:"+myTiles);
        }

        //test for getTilesForPlayer(String)
        ArrayList<Tile> myTiles2 =  myGuest.getTilesForPlayer("Yossi");
        if (myTiles2==null){
            System.out.println("null was returned");
        }
        else {
            System.out.println("my Tiles are:"+myTiles2);
        }

        //test for getGameState
       GameState gs = myGuest.getGameState();
        if (gs==null){
            System.out.println("null was returned");
        }
        else {
           printGameState(gs);
        }
        
        
        //test for placeWordOnBoard
        Word w1 = buildingNewWord();
        try {
			myGuest.placeWordOnBoard(w1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //seeing the changes in Board  getGameState
        System.out.println("Changes in board after placing \"Ship\" :");
         gs = myGuest.getGameState();
         if (gs==null){
             System.out.println("null was returned");
         }
         else {
            printGameState(gs);
         }
        
       //test for placeWordOnBoard - with an illegal word
          w1 = buildingNewIllegalWord();
         try {
 			myGuest.placeWordOnBoard(w1);
 		} catch (Exception e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		}
         
       //seeing that there are no  changes in Board  getGameState
         System.out.println("Changes in board after placing illegal \"ADDD\" :");
          gs = myGuest.getGameState();
          if (gs==null){
              System.out.println("null was returned");
          }
          else {
             printGameState(gs);
          }
        
        
        
        
        //Closing
        myHost.localServer.close();
 
        System.out.println("test ended successfully!");
    }
 
    

    
    public static void printGameState(GameState game) {
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
   
    
    
    private static Word buildingNewWord() {
        ArrayList<Tile> Tiles = new ArrayList<Tile>();
        Bag myBag = new Bag();
        Tile t = myBag.getTile('S');
        Tiles.add((t));
        t = myBag.getTile('H');
        Tiles.add((t));
        t = myBag.getTile('I');
        Tiles.add((t));
        t = myBag.getTile('P');
        Tiles.add((t));
        Word w1 = new Word(Tiles, 7, 7, true);
        return w1;
    }
    
    private static Word buildingNewIllegalWord() {
    	 Bag myBag = new Bag(); 
    	 ArrayList<Tile> Tiles2 = new ArrayList<Tile>();
         Tile t = myBag.getTile('A');
         Tiles2.add((t));
         t = myBag.getTile('D');
         Tiles2.add((t));
         t = myBag.getTile('D');
         Tiles2.add((t));
         t = myBag.getTile('D');
         Tiles2.add((t));
         Word w2 = new Word(Tiles2, 1, 1, true);
         return w2;
    }
    
    
    
    
    /*
        	Thread clientThread = new Thread(() -> {
			anotherTest();
		});
    	clientThread.start();
    	
    	try {Thread.sleep(30000);} catch (InterruptedException e1) {e1.printStackTrace();}
    	
     */
    
    
    /* public static void  anotherTest() {
	ModelGuest myGuest=new ModelGuest("Moshe");
	myGuest.addAplayer("hi");
	int res =myGuest.getNumOfPointsForPlayer("Moshe");
	System.out.println("res is..."+res);
	
    try {
        Thread.sleep(5000);}
     catch (InterruptedException e) {throw new RuntimeException(e);}
    
}*/
   
   
    
}