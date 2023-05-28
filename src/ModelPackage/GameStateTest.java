package ModelPackage;

import baseScrabble.Tile.Bag;

public class GameStateTest {

	
	public static void main (String [] args) {
		GameState gs=new GameState();
		Bag b1=gs.bag;
		ConnectedBoard board=gs.gameBoard;
		Player p1 =new Player("moshe");
		gs.listOfPlayers.add(p1);
		System.out.println(gs.listOfPlayers.toString());
		int index =gs.getIndexOfCurrentTurnPlayer();
		System.out.println("getIndexOfCurrentTurnPlayer return = "+index);
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println(board.toString());
		System.out.println("player"+"index is "+index+" ."+gs.listOfPlayers.get(index));
		Player p2 =new Player("yossi");
		gs.listOfPlayers.add(p2);
		gs.inc_indexOfCurrentTurnPlayer();
		System.out.println("after increment :");
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println(gs.getPlayerWithName("moshe").toString());
		System.out.println("test ended");
		
		
	}
	
}
