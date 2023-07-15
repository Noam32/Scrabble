package modelPackage;

import baseScrabble.Board;
import baseScrabble.Tile;
import baseScrabble.Word;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import org.bson.Document;

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
    
    public boolean equals(ConnectedBoard other) {
    	 Tile[][] other_tiles=other.tiles;
    	 //boolean isEmpty;
    	 if(this.isEmpty!=other.isEmpty) {
    		 return false;
    	 }
    	 int rowLen=15;
    	 int colLen=15;
    	 for(int i=0;i<rowLen;i++) {
    		 for(int j=0;j<colLen;j++) {
    			 if(this.tiles[i][j]!=null &&other_tiles[i][j]==null) {
    				 return false;
    			 }
    			  if(this.tiles[i][j]==null &&other_tiles[i][j]!=null) {
    				 return false;
    			 }
    			  if(this.tiles[i][j]!=null &&other_tiles[i][j]!=null) {
    				  if(this.tiles[i][j].letter!=other_tiles[i][j].letter) {
    	    				 return false;
    	    			 }
    			  }
    			  
    		 }
    	 }
    	
    	return true;
    }

    public Document toDocument() {
        Document document=new Document();
        document.append("isEmpty", this.isEmpty);
        Document embedded_document=new Document();//for 2d array
        for(int i=0;i<this.tiles[0].length;i++) {
            //System.out.println("len ="+this.tiles[i].length);
            ArrayList<Tile> list=new ArrayList<Tile>(Arrays.asList(tiles[i]));
            embedded_document.append(""+i, embedded_TilesDoc( list));
        }
        document.append("tiles", embedded_document);
        return document;

    }
    public Document embedded_TilesDoc(ArrayList<Tile> list) {
        Document document= new Document();
        for(int i=0;i<list.size();i++ ) {
            if(list.get(i)==null)
                document.append(""+i,null);
            else
                document.append(""+i, list.get(i).toDocument());
        }
        return document;
    }
    //converts mongoDB document that contains the object to ConnectedBoard object
    public static ConnectedBoard fromDocument (Document document_ConnectedBoard) {
        ConnectedBoard board =new ConnectedBoard();
        //now we need to update the isEmpty and the tile 2d array:
        boolean isEmpty=(boolean)document_ConnectedBoard.getBoolean("isEmpty");
        board.isEmpty=isEmpty;
        //update 2d tile arr:
        Tile [][] tiles=board.tiles;
        int rows=15;
        int cols=15;
        Document document_2d_tiles_arr=document_ConnectedBoard.get("tiles", Document.class);
        for(int i=0;i<rows;i++) {
            for(int j=0;j<cols;j++) {
                Tile t=getTileFromDocument(i,j,document_2d_tiles_arr);
                tiles[i][j]=t;
            }
        }
        board.tiles=tiles;
        return board;
    }
    //untested!!! maybe  getList will not work?
    public static Tile getTileFromDocument(int i ,int j,Document document_2d_tiles_arr ) {
        Tile t1;
        Document rowOfTileDocument=document_2d_tiles_arr.get(""+i, Document.class);
        //getting a list of 15 document representing one Tile each:
        Document tileDocumnet=rowOfTileDocument.get(""+j,Document.class);//getting the specific document
        if(tileDocumnet==null) {
            return null;
        }
        t1=Tile.fromDocument(tileDocumnet);
        return t1;
    }
}
