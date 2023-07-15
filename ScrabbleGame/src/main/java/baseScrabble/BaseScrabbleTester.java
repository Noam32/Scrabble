package baseScrabble;

import baseScrabble.Tile.Bag;

public class BaseScrabbleTester {
	
	public static void main(String []args) {
		//Word w1=new Word();
		Bag b1;
		b1=new Bag();
		Tile t1=b1.getRand();
		System.out.println(t1.letter);
		
		
	}

}
