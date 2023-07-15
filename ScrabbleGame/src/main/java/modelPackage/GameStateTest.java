package modelPackage;

import baseScrabble.Tile.Bag;
import org.mongojack.JacksonMongoCollection;
import org.mongojack.MongoCollection;
public class GameStateTest {


	public static void main (String [] args) {
		GameState gs=new GameState();
		Bag b1=gs.bag;
		ConnectedBoard board=gs.gameBoard;
		//Player p1 =new Player();
		gs.addAPlayer("moshe");
		//Player p2 =new Player("Danit");
		gs.addAPlayer("Danit");
		System.out.println(gs.listOfPlayers.toString());
		int index =gs.getIndexOfCurrentTurnPlayer();
		System.out.println("getIndexOfCurrentTurnPlayer return = "+index);
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println(board.toString());
		System.out.println("player"+"index is "+index+" ."+gs.listOfPlayers.get(index));
		//Player p3 =new Player("yossi");
		gs.addAPlayer("yossi");
		gs.inc_indexOfCurrentTurnPlayer();
		System.out.println("after increment :");
		index= gs.getIndexOfPlayerWithId(1);
		System.out.println("getIndexOfPlayerWithId return = "+index);
		System.out.println(gs.getPlayerWithName("moshe").toString());

		System.out.println("test the hashmap:");
		System.out.println(gs.hashmap_name_to_id);
		System.out.println(gs.hashmap_name_to_id.get("moshe"));
		System.out.println(gs.hashmap_name_to_id.get("Danit"));
		System.out.println(gs.hashmap_name_to_id.get("yossi"));
		System.out.println("gs.getIdofPlayerName(\"moshe\")="+gs.getIdofPlayerName("moshe"));
		System.out.println("\ntest the getStringOfScoreBoard");
		gs.listOfPlayers.get(0).numOfPoints=77;
		String [] out=gs.getStringOfScoreBoard();
		for (String str:out) {
			System.out.println(str);
		}
		gs.inc_indexOfCurrentTurnPlayer();
		System.out.println("");
		out=gs.getStringOfScoreBoard();
		for (String str:out) {
			System.out.println(str);
		}

		System.out.println("\n Testing the equals method");
		GameState gs_copy=gs;
		System.out.println("testing equals with the same object -should be true:");
		System.out.println(gs_copy.equals(gs));
		System.out.println("testing equals with a differnt gameState- should be false:");
		GameState other_gs=new GameState();
		other_gs.addAPlayer("david");
		System.out.println(gs_copy.equals(other_gs));
		System.out.println();

		System.out.println("test ended");


	}

}