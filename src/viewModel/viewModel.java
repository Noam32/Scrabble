package viewModel;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ModelPackage.GameState;
import ModelPackage.Model;
import ModelPackage.Player;
import ViewPackage.BoardController;
import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
import baseScrabble.Word;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@SuppressWarnings("deprecation")
public class viewModel extends Observable implements Observer {

	public StringProperty wordFromUser;
	public BooleanProperty isvertical,isvalid,isHost;
	public IntegerProperty row,col;
	public volatile IntegerProperty numberOfPlayers;
	public MapProperty<StringProperty, IntegerProperty> userScore;
	public ObjectProperty<Character>[] userTiles = new ObjectProperty[7];
	public IntegerProperty[] userTilesScore = new IntegerProperty[7];



	
	Word w;
	Model m;
	GameState gameState;
	String myName;
	
	//public StringProperty s;
	//public DoubleProperty d;
	
	public viewModel(Model m,String my_name) {
		this.myName=my_name;
		this.m = m;
        if (m instanceof Observable) {
            ((Observable) m).addObserver(this);
        }
        
		this.wordFromUser = new SimpleStringProperty();
		//this.userTiles = new SimpleStringProperty();
		this.isvertical = new SimpleBooleanProperty();
		this.isHost = new SimpleBooleanProperty();
		this.isvalid = new SimpleBooleanProperty();
		this.userScore = new SimpleMapProperty<>();
		this.row = new SimpleIntegerProperty();
		this.col=new SimpleIntegerProperty();
		this.numberOfPlayers = new SimpleIntegerProperty();
		gameState = m.getGameState();

		numberOfPlayers.set(gameState.listOfPlayers.size());
		for (int i = 0; i < userTiles.length; i++) {
			userTiles[i] = new SimpleObjectProperty<>();
		}

		for (int i = 0; i < userTilesScore.length; i++) {
			userTilesScore[i] = new SimpleIntegerProperty();
		}
		//need to create new word? for try place or in the model it work on it
				wordFromUser.addListener((o,ov,nv)->{
					try {
						m.placeWordOnBoard(createWord(nv));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
        /*Thread hostThread = new Thread(() -> {
        while(gameState.listOfPlayers.size()<2) {
        	try {
        		gameState = m.getGameState();
        		numberOfPlayers.set(gameState.listOfPlayers.size());
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }*/
	
		getTiles();
		
		
        //});
        //hostThread.start();
	}

	
	public void getTiles() {
		Tile[] tiles = new Tile[7];
		gameState = m.getGameState();
		gameState.listOfPlayers.size();
		
		//int index = gameState.getIndexOfCurrentTurnPlayer();
		Player p1 = gameState.getPlayerWithName(myName);
		ArrayList<Tile> userTile=p1.getMyTiles();
		for(int i=0;i<tiles.length;i++) {
			userTiles[i].set(userTile.get(i).letter);
			userTilesScore[i].set(userTile.get(i).score);
		}
		
	}
	
	public Word createWord(String word){
		Tile[] tiles = new Tile[word.length()];
		gameState = m.getGameState();
		int index = gameState.getIndexOfCurrentTurnPlayer();
		Player p1 = gameState.listOfPlayers.get(index);
		ArrayList<Tile> userTile=p1.getMyTiles();
		for(int i=0;i<word.length();i++) {
			char temp= word.charAt(i);
			for (int j=0;j<userTile.size();j++) {
				if (temp==userTile.get(j).letter) {
					tiles[i]=userTile.get(j);
					break;
				}
			}
		}
		return new Word(tiles, this.row.getValue(), this.col.getValue(), this.isvertical.getValue());
	}
	
	public void func() {
		m.addAplayer(null); //////////////////////////
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (o==m) {
			isvalid.set(m.wasLastPlacementSuccessful());
			getTiles();
			setChanged();
			notifyObservers();
		}
		
	}

}

