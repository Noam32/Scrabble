package ModelPackage;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Word;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectedBoard extends Board implements Serializable {

    public boolean dictionaryLegal(Word w, ModelHost model) throws Exception{
        String word = w.createSimpleString();
        Boolean queryRes = model.runClientToDictionaryServer(8000,word);
        return queryRes;
    }

    public int tryPlaceWord(Word w, ModelHost model) throws Exception {

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
                if(dictionaryLegal(nw, model))
                    sum+=getScore(nw);
                else
                    return 0;
            }
        }

        // the placement
        row=w.getRow();
        col=w.getCol();
        for(Tile t : w.getTiles()) {
            tiles[row][col]=t;
            if(w.isVertical()) row++; else col++;
        }

        if(isEmpty) {
            isEmpty=false;
            bonus[7][7]=0;
        }
        return sum;
    }

}
