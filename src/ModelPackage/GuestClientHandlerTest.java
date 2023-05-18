package ModelPackage;

import java.util.Iterator;
//import ModelPackage.GuestClientHandler;

public class GuestClientHandlerTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
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

		
		
	}

	
	private static void printStrings(String ...strings ) {
		if(strings==null)
			return;
		for(int i=0;i<strings.length;i++) {
			String str=strings[i];
			System.out.println(i+"."+str);
		}
	}
	
}
