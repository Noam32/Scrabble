package ModelPackage;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;

import java.io.IOException;
import java.util.ArrayList;

//this test has to create a host and a guest, and communicate between them.
//It has to show that the guest can request the host to do some operations of the game, and get the result back.
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

        //test for WhoseTurnIsIt_Id
        int turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's"+" the player with id = "+turn+" "+"turn");

        //until here works.
        //test for WhoseTurnIsIt
        Player p1 = myGuest.WhoseTurnIsIt();
        if (p1==null){
            System.out.println("null was returned");
        }
        else {
            System.out.println("it's the turn of:"+p1);
        }

        //test for addAplayer
        myGuest.addAplayer("Yossi");
        System.out.println(myHost.getGameState().listOfPlayers);

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
        
        
        
        
        
        
        
        
        
        
        //Closing
        myHost.localServer.close();
 
        System.out.println("test ended successfully!");
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