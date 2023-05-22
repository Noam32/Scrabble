package ModelPackage;

import baseScrabble.Tile;

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
        System.out.println("wasLastPlacementSuccessful =:"+b1);

        //test for WhoseTurnIsIt_Id
        int turn = myGuest.WhoseTurnIsIt_Id();
        System.out.println("now it's"+" "+turn+" "+"turn");

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
            System.out.println(gs.listOfPlayers.toString());
        }
        
        myHost.localServer.close();

    }
}