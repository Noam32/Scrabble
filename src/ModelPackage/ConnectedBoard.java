package ModelPackage;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Word;

import java.io.Serializable;
import java.util.ArrayList;

//This class implements the board which now has tcp/ip connectivity :

public class ConnectedBoard extends Board implements Serializable {
	private static final boolean isTestMode_ALL_WORDS_LEGAL=false;
	private static final long serialVersionUID = 1L;
	public boolean dictionaryLegal(Word w) throws Exception{
        String word = w.createSimpleString();
        Boolean queryRes = ModelHost.runClientToDictionaryServer(8000,'C',word);
        Boolean queryRes_lowerCase = ModelHost.runClientToDictionaryServer(8000,'C',word.toLowerCase());
        //we try for both lower and upper case:
        return (queryRes||queryRes_lowerCase|| isTestMode_ALL_WORDS_LEGAL);
    }
    //Overriding the methods used for word checking to include the new methods
    public int tryPlaceWord(Word w) throws Exception {

        Tile[] ts = w.getTiles();
        int row=w.getRow();
        int col=w.getCol();
        for(int i=0;i<ts.length;i++) {
            if(ts[i]==null)
                ts[i]=tiles[row][col];
            if(w.isVertical()) row++; else col++;
        }

        Word test=new Word(ts, w.getRow(), w.getCol(), w.isVertical());

        int sum=0;
        if(boardLegal(test) ) {
            ArrayList<Word> newWords=getWords(test);
            for(Word nw : newWords) {
                if(dictionaryLegal(nw)){
                    sum+=getScore(nw);}
                else{return 0;}
            }
        }

        // the placement
        row=w.getRow();
        col=w.getCol();
        if(sum!=0) {
        	for(Tile t : w.getTiles() ) {
                tiles[row][col]=t;
                if(w.isVertical()) row++; else col++;
            }
        }
        
        if(isEmpty) {
            isEmpty=false;
            bonus[7][7]=0;
        }
        return sum;
    }

}
