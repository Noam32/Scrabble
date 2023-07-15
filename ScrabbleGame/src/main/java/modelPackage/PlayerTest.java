package modelPackage;

import baseScrabble.Tile.Bag;

public class PlayerTest {

	public static void main(String []args) {
		Player p1=new Player("moshiko");
		p1.numOfPoints=19;
		Bag b1=new Bag();
		//adding tiles:
		p1.addTile(b1.getRand());
		p1.addTile(b1.getRand());
		p1.addTile(b1.getRand());
		p1.addTile(b1.getRand());
		System.out.println("player's tiles are :");
		System.out.println(p1.getMyTiles().toString());
		System.out.println(p1.toString());
		
	}
}
