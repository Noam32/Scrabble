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
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
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
import javafx.collections.FXCollections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.util.Duration;

@SuppressWarnings("deprecation")
public class viewModel extends Observable implements Observer {

	public StringProperty wordFromUser;
	public BooleanProperty isvertical,isvalid,isHost, skipPush, endPush;
	public IntegerProperty[] userScore;
	public StringProperty[] userScorename;
	public IntegerProperty row,col,currentPlayerIndex,numberOfPlayers;
	public ObjectProperty<Character>[] userTiles = new ObjectProperty[7];
	public IntegerProperty[] userTilesScore = new IntegerProperty[7];



	
	Word w;
	Model m;
	GameState gameState;
	String myName;
	
	
	/*
	 * name: viewModel
	 * input: Model m, String my_name
	 * output: none
	 * functionality: Constructor for the viewModel class. Initializes properties,
	 *  adds an observer to the model if it is an instance of Observable, 
	 *  and sets up a listener for the wordFromUser property.
	 */
	public viewModel(Model m, String my_name) {
	    // Set the myName and m properties
	    this.myName = my_name;
	    this.m = m;

	    // Add an observer to the model if it is an instance of Observable
	    if (m instanceof Observable) {
	        ((Observable) m).addObserver(this);
	    }

	    // Initialize properties
	    this.wordFromUser = new SimpleStringProperty();
	    this.isvertical = new SimpleBooleanProperty();
	    this.isHost = new SimpleBooleanProperty();
	    this.isvalid = new SimpleBooleanProperty();
	    this.row = new SimpleIntegerProperty();
	    this.col = new SimpleIntegerProperty();
	    this.numberOfPlayers = new SimpleIntegerProperty();
        this.skipPush = new SimpleBooleanProperty();
        this.endPush = new SimpleBooleanProperty();
		this.currentPlayerIndex=new SimpleIntegerProperty();
	    gameState = m.getGameState();
	    numberOfPlayers.set(gameState.listOfPlayers.size());
		this.userScore = new IntegerProperty[numberOfPlayers.getValue()];
		this.userScorename = new SimpleStringProperty[numberOfPlayers.getValue()];
		
	    //gameState = m.getGameState();

	    // Set the numberOfPlayers property based on the size of the listOfPlayers in the gameState
	    numberOfPlayers.set(gameState.listOfPlayers.size());

	    for (int i = 0; i < userScore.length; i++) {
	    	userScorename[i] = new SimpleStringProperty();
	    	userScore[i] = new SimpleIntegerProperty();

	    }

	    // Initialize the userTiles array
	    for (int i = 0; i < userTiles.length; i++) {
	        userTiles[i] = new SimpleObjectProperty<>();
	        userTilesScore[i] = new SimpleIntegerProperty();

	    }

	   

	    // Add a listener to the wordFromUser property that calls the placeWordOnBoard 
	    //method of the model when the property changes
	    wordFromUser.addListener((o, ov, nv) -> {
	        try {
	            m.placeWordOnBoard(createWord(nv));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });


        // Add a listener to the skipPush property that calls the skipPlayerTurn
        //method of the model when the property changes
        skipPush.addListener((o, ov, nv) ->{
                try {
                		if(nv==true) {
	                        m.skipPlayerTurn();
	                        this.skipPush.set(false);
	                        System.out.println("this is the listener of skipPush");
                		}
                } catch (Exception e) {
                        e.printStackTrace();
                }
        });
        
        endPush.addListener((o, ov, nv) ->{
                try {
                		if(nv==true) {
	                        m.endPlayerTurn();
	                        this.endPush.set(false);
	                        System.out.println("this is the listener of endPush");
                		}
                } catch (Exception e) {
                        e.printStackTrace();
                }
                
        });
	    
        getUserScore();
	    // Get tiles from the model
	    getTiles();
	    
	    /*
	    Task<Void> task = new Task<Void>() {
	        @Override
	        protected Void call() throws Exception {
	            // Call the checkboard() method on the background thread
	            checkboard();

	            return null;
	        }
	    };*/

		Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), e -> checkboard()));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

	   // new Thread(task).start();
	}

	public void getUserScore() {
		gameState = m.getGameState();
	    for(int i=0;i<gameState.listOfPlayers.size();i++) {
	    	
	    	userScorename[i].set(gameState.listOfPlayers.get(i).getName());
	    	userScore[i].set(gameState.listOfPlayers.get(i).getNumOfPoints());
	    }
	}

	/*
	 * name: getTiles
	 * input: none
	 * output: none
	 * functionality: Gets the tiles from the model and sets the userTiles and
	 *  userTilesScore properties based on the tiles of the player with the same name as myName.
	 */
	public void getTiles() {
	    Tile[] tiles = new Tile[7];
	    gameState = m.getGameState();

	    // Get the player with the same name as myName
	    Player p1 = gameState.getPlayerWithName(myName);

	    // Get the player's tiles
	    ArrayList<Tile> userTile = p1.getMyTiles();

	    // Set the userTiles and userTilesScore properties based on the player's tiles
	    for (int i = 0; i < tiles.length; i++) {
	        userTiles[i].set(userTile.get(i).letter);
	        userTilesScore[i].set(userTile.get(i).score);
	    }
	}

	/*
	 * name: createWord
	 * input: String word
	 * output: Word
	 * functionality: Creates a new Word object based on the given word, row, column, and orientation.
	 */
	public Word createWord(String word) {
	    Tile[] tiles = new Tile[word.length()];
	    gameState = m.getGameState();
	    int index = gameState.getIndexOfCurrentTurnPlayer();
	    Player p1 = gameState.listOfPlayers.get(index);
	    ArrayList<Tile> userTile = p1.getMyTiles();

	    // Set the tiles array based on the given word and the player's tiles
	    for (int i = 0; i < word.length(); i++) {
	        char temp = word.charAt(i);
	        for (int j = 0; j < userTile.size(); j++) {
	            if (temp == userTile.get(j).letter) {
	                tiles[i] = userTile.get(j);
	                break;
	            }
	        }
	    }

	    // Create and return a new Word object with the given row, column, and orientation
	    return new Word(tiles, this.row.getValue(), this.col.getValue(), this.isvertical.getValue());
	}

	/*
	 * name: func
	 * input: none
	 * output: none
	 * functionality: Calls the addAplayer method of the model with a null argument.
	 */
	public void func() {
	    m.addAplayer(null);
	}

	/*
	 * name: update
	 * 
	 * input: Observable o, Object arg
	 * output: none
	 * functionality: Updates the isvalid property based on whether the last placement was successful, 
	 * gets tiles from the model, and notifies observers.
	 */
	@Override
	public void update(Observable o, Object arg) {
	    if (o == m) {
	        // Set the isvalid property based on whether the last placement was successful
	        isvalid.set(m.wasLastPlacementSuccessful());
	        System.out.println(this.myName);
	        //isvalid.getValue()
			    gameState = m.getGameState();//update the game state
			    this.currentPlayerIndex.set(gameState.getIndexOfCurrentTurnPlayer());
			    for(int i=0;i<numberOfPlayers.getValue();i++) {
			    userScore[i].set(gameState.listOfPlayers.get(i).getNumOfPoints());
			    }
			    //userScore[this.currentPlayerIndex-1];
		        getTiles();
		        setChanged();
		        Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                notifyObservers(gameState.getBoard().getTiles());//get the board
		            }
		        });
	        
	        // Get tiles from the model
	    }
	}
	
	public void checkboard() {
	    GameState gamenew = m.getGameState();//update the game state
	    if(!gameState.equals(gamenew)){//if true do nothing
	        isvalid.set(m.wasLastPlacementSuccessful());
	        //if(isvalid.getValue()) {

		        gameState = m.getGameState();//update the game state
		        this.currentPlayerIndex.set(gameState.getIndexOfCurrentTurnPlayer());
		        for(int i=0;i<numberOfPlayers.getValue();i++) {
		            userScore[i].set(gameState.listOfPlayers.get(i).getNumOfPoints());
		        }
		        //userScore[this.currentPlayerIndex-1];
		        getTiles();
		        setChanged();
	
		        // Update the user interface on the JavaFX Application Thread
		        Platform.runLater(new Runnable() {
		            @Override
		            public void run() {
		                notifyObservers(gameState.getBoard().getTiles());//get the board
		            }
		        });
	       // }
	    }
	}


}

