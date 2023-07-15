package modelPackage;

import java.util.ArrayList;
import java.util.Iterator;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
//import ModelPackage.GuestClientHandler;

public class GuestClientHandlerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("testing all GuestClientHandler . testing createCommandStrings() and getMethodName");
		String[] allMethodNames= {"getGameState", "getNumOfPointsForPlayer", "getTilesForPlayer", "WhoseTurnIsIt",
				"WhoseTurnIsIt_Id", "wasLastPlacementSuccessful", "addAplayer", "initGame", "givePlayerOneTile",
				"placeWordOnBoard", "endPlayerTurn", "skipPlayerTurn"};
		//1
		String []command=GuestClientHandler.createCommandStrings("WhoseTurnIsIt_Id");
		printStrings(command);
		System.out.println("the getMethodName() string is="+GuestClientHandler.getMethodName(command[0]));
		System.out.println();
		//2
		command=GuestClientHandler.createCommandStrings("getNumOfPointsForPlayer","String","player1");
		printStrings(command);
		System.out.println("the getMethodName() string is="+GuestClientHandler.getMethodName(command[0]));
		System.out.println();
		//3
		command=GuestClientHandler.createCommandStrings("getNumOfPointsForPlayer","int","1");
		printStrings(command);
		System.out.println("the getMethodName() string is="+GuestClientHandler.getMethodName(command[0]));
		System.out.println();
		Bag b1=new Bag();
		ArrayList<Tile> tileList=new ArrayList<Tile>();
		tileList.add(b1.getTile('A'));
		tileList.add(b1.getTile('D'));
		tileList.add(b1.getTile('D'));
		System.out.println("the list is "+tileList.toString());
		System.out.println("t1 is :"+b1.getRand().toString());
		
		System.out.println("get class of Bag is "+b1.getClass());
		System.out.println("get class of tileList "+tileList.getClass());
		System.out.println("get class of tileList "+tileList.get(0).getClass());
		
		
		
	}

	
	public static void printStrings(String ...strings ) {
		if(strings==null)
			return;
		for(int i=0;i<strings.length;i++) {
			String str=strings[i];
			System.out.println(i+"."+str);
		}
	}
	
}
