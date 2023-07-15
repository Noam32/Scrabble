package modelPackage;

import baseScrabble.Tile;
import baseScrabble.Word;
import baseScrabble.Tile.Bag;

import java.util.ArrayList;

public class ConnectedBoardTest {
    
	public static void main(String[] args) throws Exception {
        System.out.println("Running tests for ConnectedBoardTest - this assumes server is running and listenning on port 8000");
		ConnectedBoard Cb1 = new ConnectedBoard();
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
        int score = Cb1.tryPlaceWord(w1);
        System.out.println("First try(good word):");
        if (score!=0){
            System.out.println("the placing of the word succeeded!");
        }
        else{
            System.out.println("the placing of the word didn't succeed!");
        }
        
        System.out.println("the board after placement of \"ship\" at (7,7) is:");
        System.out.println(Cb1.toString());

        ConnectedBoard Cb2 = new ConnectedBoard();
        ArrayList<Tile> Tiles2 = new ArrayList<Tile>();
        t = myBag.getTile('A');
        Tiles2.add((t));
        t = myBag.getTile('D');
        Tiles2.add((t));
        t = myBag.getTile('D');
        Tiles2.add((t));
        t = myBag.getTile('D');
        Tiles2.add((t));
        Word w2 = new Word(Tiles2, 7, 7, true);
        score = Cb2.tryPlaceWord(w2);
        System.out.println("Second try(wrong word):");
        if (score!=0){
            System.out.println("the placing of the word succeeded!");
        }
        else{
            System.out.println("the placing of the word didn't succeed!");
        }

    }
}
