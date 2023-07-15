package ModelPackage;

import baseScrabble.Tile.Bag;

public class GameStateTest {

	
	public static void main (String [] args) {
		GameState gs=new GameState();
		Bag b1=gs.bag;
		ConnectedBoard board=gs.gameBoard;
		Player p1 =new Player("moshe");
		gs.listOfPlayers.add(p1);
		int index =gs.getIndexOfCurrentTurnPlayer();
		System.out.println("getIndexOfCurrentTurnPlayer return = "+index);
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println(board.toString());
		System.out.println("player"+gs.listOfPlayers.get(index));
		Player p2 =new Player("yossi");
		gs.listOfPlayers.add(p1);
		gs.inc_indexOfCurrentTurnPlayer();
		System.out.println("after increment :");
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println("test ended");
		
		
	}
	
}
