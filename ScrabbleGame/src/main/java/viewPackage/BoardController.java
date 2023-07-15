package viewPackage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import static javafx.beans.binding.Bindings.bindBidirectional;
import java.util.Observer;
import java.util.Optional;

import baseScrabble.Tile;
import baseScrabble.Tile.Bag;
import javafx.animation.Animation;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.MapProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import viewModel.ViewModel;
import waitScreen.WaitScreenController;

@SuppressWarnings("deprecation")
public class BoardController implements Observer {
	
	ViewModel vm;
	private StringProperty wordFromUser,saveFromUser;
	private BooleanProperty isvertical,isvalid,isHost,skipPush,endPush,savePush;
	private IntegerProperty row,col,numberOfPlayers,currentPlayerIndex;
	@SuppressWarnings("unchecked")
	public ObjectProperty<Character>[] userTiles = new ObjectProperty[7];
	public IntegerProperty[] userTilesScore = new IntegerProperty[7];
	public IntegerProperty[] userScore;
	public StringProperty[] userScorename;
	public Scene boardScene;
	

	StackPane selectedTileStackPane = null;
	char selectedTileLetter=' ';
	int selectedTileScore=0,selectedTileIndex;
	List<int[]> placedTiles = new ArrayList<>();
	List<String> playerNames;
	
	@FXML
	GridPane board;
	@FXML
	GridPane player;
	@FXML
	GridPane players_score;
	@FXML
	BorderPane borderPane;
	@FXML
    Label timerLabel;
    @FXML
    Button skipButton;
    @FXML
    Button endButton;

	
    
    
   
	
	/*	This array holds values for representing the board, including special cells. 
	 * 	Legend: 0 = regular cell, green
	 * 			1 = double letter, cyan
	 * 			2 = double word, yellow
	 * 			3 = triple letter, blue
	 * 			4 = triple word, red
	 * 			5 = STAR
	 */
	int[][] boardData={
            {4,0,0,1,0,0,0,4,0,0,0,1,0,0,4},
            {0,2,0,0,0,3,0,0,0,3,0,0,0,2,0},
            {0,0,2,0,0,0,1,0,1,0,0,0,2,0,0},
            {1,0,0,2,0,0,0,1,0,0,0,2,0,0,1},
            {0,0,0,0,2,0,0,0,0,0,2,0,0,0,0},
            {0,3,0,0,0,3,0,0,0,3,0,0,0,3,0},
            {0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
            {4,0,0,1,0,0,0,5,0,0,0,1,0,0,4},	//row 8, middle
            {0,0,1,0,0,0,1,0,1,0,0,0,1,0,0},
            {0,3,0,0,0,3,0,0,0,3,0,0,0,3,0},
            {0,0,0,0,2,0,0,0,0,0,2,0,0,0,0},
            {1,0,0,2,0,0,0,1,0,0,0,2,0,0,1},
            {0,0,2,0,0,0,1,0,1,0,0,0,2,0,0},
            {0,2,0,0,0,3,0,0,0,3,0,0,0,2,0},
            {4,0,0,1,0,0,0,4,0,0,0,1,0,0,4}
    };
	
	StackPane[][] stack = new StackPane[15][15];
	StackPane[] tiles = new StackPane[26];
	boolean mousePress;
	LinkedHashMap<Character, StackPane> map = new  LinkedHashMap<Character, StackPane>();

	
	private void updateTime() {
		
	    for (int i = 60; i > 0; i--) {
	        final int time = i;
	        Platform.runLater(() -> timerLabel.setText(String.valueOf(time)));
	        try {
	            Thread.sleep(1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	    }
	}


	    
	    /*
		  Function name: BoardController
		  Input: None
		  Output: None
		  Functionality: Constructor for the BoardController class. Initializes various properties and arrays.
	     */
		public BoardController() {
			playerNames = new ArrayList<>();
			this.wordFromUser = new SimpleStringProperty();
			this.saveFromUser= new SimpleStringProperty();
			this.isvertical = new SimpleBooleanProperty();
			this.isHost = new SimpleBooleanProperty();
			this.isvalid = new SimpleBooleanProperty();
			this.row = new SimpleIntegerProperty();
			this.col=new SimpleIntegerProperty();
			this.currentPlayerIndex=new SimpleIntegerProperty();
			this.numberOfPlayers = new SimpleIntegerProperty();
            this.skipPush = new SimpleBooleanProperty();
            this.endPush = new SimpleBooleanProperty();
            this.savePush = new SimpleBooleanProperty();
			for (int i = 0; i < userTiles.length; i++) {
				userTiles[i] = new SimpleObjectProperty<>();
				userTilesScore[i] = new SimpleIntegerProperty();

			}
		}
		
		/*
		 Function name: init
		 Input: viewModel vm - the view model object
		 Output: None
		 Functionality: Initializes the BoardController with the provided view model and binds various properties.
		 */
		public void init(ViewModel vm) {
			this.vm=vm;
			vm.addObserver(this);
			vm.wordFromUser.bind(wordFromUser);
			vm.saveFromUser.bind(saveFromUser);
			vm.isvertical.bind(isvertical);
			vm.isHost.bind(isHost);
			vm.row.bind(row);
			vm.col.bind(col);
			currentPlayerIndex.bind(vm.currentPlayerIndex);
			isvalid.bind(vm.isvalid);
            skipPush.bindBidirectional(vm.skipPush);
            endPush.bindBidirectional(vm.endPush);
            savePush.bindBidirectional(vm.savePush);
            /*
			numberOfPlayers.bind(vm.numberOfPlayers);
            this.userScore = new IntegerProperty[numberOfPlayers.getValue()];
    		this.userScorename = new SimpleStringProperty[numberOfPlayers.getValue()];
    		
    		for(int i=0;i<userScore.length;i++) {
    			userScorename[i] = new SimpleStringProperty();
    			userScore[i] = new SimpleIntegerProperty();
    			userScore[i].bind(vm.userScore[i]);
    			userScorename[i].bind(vm.userScorename[i]);
			}
			for(int i=0;i<userTiles.length;i++) {
				userTiles[i].bind(vm.userTiles[i]);
				userTilesScore[i].bind(vm.userTilesScore[i]);
			}*/
			
		}
		
	/*
	Function name: redraw
	Input: None
	Output: None
	Functionality: Redraws the board with the current word and scores.
	*/
	
	public void redraw(Tile[][] tilesBoard) {
		for (int i = 0; i < tilesBoard.length; i++) {
            for (int j = 0; j < tilesBoard[i].length; j++) {
            	if(tilesBoard[i][j]==null)continue;
            	char letter = tilesBoard[i][j].letter;
     	        int value = tilesBoard[i][j].score;
     	        final int row = i;
     	        final int col = j;

				ObservableList<Node> children = board.getChildren();
				
				// Remove all StackPanes in the specified cell
				children.removeIf(node -> node instanceof StackPane && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col);
				Rectangle square = new Rectangle();
				Color color = Color.ANTIQUEWHITE;
				Text letterText = new Text(Character.toString(letter));
			    letterText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
			    Text scoreText = new Text(Integer.toString(value));
			    scoreText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));

		        square.setFill(color);
		        StackPane temp = new StackPane(square,letterText,scoreText);
		     // Bind square size to cell size
		        square.widthProperty().bind(board.widthProperty().divide(15));
		        square.heightProperty().bind(board.heightProperty().divide(15));
		        square.setStroke(Color.BLACK); // Add black border
		        square.setStrokeWidth(1); // Set border width
		        
		        // Bind text size to square size
		        letterText.fontProperty().bind(Bindings.createObjectBinding(() ->
		                Font.font("Comic Sans MS", FontWeight.BOLD, square.getWidth() / 3), square.widthProperty()));
		        scoreText.fontProperty().bind(Bindings.createObjectBinding(() ->
		                Font.font("Comic Sans MS", FontWeight.BOLD, square.getWidth() / 4), square.widthProperty()));

		        temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);

		        scoreText.setTranslateX(-5);
		        scoreText.setTranslateY(-5);
				board.add(temp, col, row);
		    	}
			
            }
        }
	
	/*
	Function name: setStage
	Input: Stage stage - the primary stage
	Output: None
	Functionality: Sets the primary stage for the application.
	*/
	Stage primaryStage;
	public void setStage(Stage stage){
		this.primaryStage=stage;
	}
	
	public void setBoardScene(Scene scene) {
		this.boardScene=scene;
	}
	
	
	/*
	 * name: paint
	 * input: none
	 * output: none
	 * functionality: Initializes the stack array, creates a star polygon, sets the color and text of each square on the board based on its value in boardData, adds mouse event handling, and updates tiles and time.
	 */
	@SuppressWarnings("static-access")
	public void paint() {
		
		
		stack = new StackPane[15][15]; // Initialize the stack array

	    // Create a star polygon
	    Polygon star = new Polygon();
	    star.getPoints().addAll(new Double[]{ 
	    		0.0, 0.0,
	  	        10.0, 30.0,
	  	        40.0, 30.0,
	  	        15.0, 50.0,
	  	        25.0, 80.0,
	  	        0.0, 60.0,
	  	       -25.0, 80.0,
	  	       -15.0, 50.0,
	  	       -40.0, 30.0,
	  	       -10.0, 30.0
	  	    });
	    star.setFill(Color.GOLD);

	    // Loop through rows and columns of the board
	    for (int row = 0; row < 15; row++) {
	        for (int col = 0; col < 15; col++) {
	            Rectangle square = new Rectangle();
	            Color color = null;
	            Text text = null;

	            // Set color and text based on boardData value
	            switch (boardData[row][col]) {
	                case 0:
	                    color = Color.GREEN;
	                    text = new Text(" ");
	                    break;
	                case 1:
	                    color = Color.CYAN;
	                    text = new Text("double\n letter");
	                    break;
	                case 2:
	                    color = Color.YELLOW;
	                    text = new Text("double\n word");
	                    break;
	                case 3:
	                    color = Color.DODGERBLUE;
	                    text = new Text("triple\n letter");
	                    break;
	                case 4:
	                    color = Color.ORANGERED;
	                    text = new Text("triple\n word");
	                    break;
	                case 5:
	                    color = Color.YELLOW;
	                    text = new Text("");
	                    star.scaleXProperty().bind(square.widthProperty().divide(80));
	                    star.scaleYProperty().bind(square.heightProperty().divide(80));
	                    break;
	            }

	            square.setFill(color);
	            square.widthProperty().bind(board.widthProperty().divide(15));
	            square.heightProperty().bind(board.heightProperty().divide(15));
	            square.setStroke(Color.BLACK);
	            square.setStrokeWidth(1);

	            StackPane stackPane = new StackPane();
	            if (boardData[row][col] != 5) {
	                stackPane.getChildren().addAll(square, text);
	            } else {
	                stackPane.getChildren().addAll(square, star);
	                square.setTranslateX(-12);
	                star.setTranslateX(-12);
	            }

	            final int column = col;
	            final int rows = row;

	            board.add(stackPane, col, row);
	            stack[col][row] = stackPane;

	            // Add mouse event handling
	            stackPane.setOnMousePressed(evt -> this.mouseDown(evt));
	            stackPane.setOnMouseClicked(event -> {
	                // Check if a tile is selected
	                if (selectedTileLetter != ' ') {
	                    // Move the selected tile to the clicked cell on the game board
	                    // Remove the selected tile from the user tile GridPane
	                    ObservableList<Node> children = player.getChildren();
	                    children.removeIf(node -> node instanceof StackPane && GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == selectedTileIndex);
	                    // Add the selected tile to the clicked cell on the game board
	                    Rectangle square1 = new Rectangle();
	                    square.setFill(Color.ANTIQUEWHITE);
	                    square.widthProperty().bind(board.widthProperty().divide(15));
	                    square.heightProperty().bind(board.heightProperty().divide(15));
	                    square.setStroke(Color.BLACK);
	                    square.setStrokeWidth(1);
	                    Text letterText = new Text(Character.toString(selectedTileLetter));
	                    letterText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
	                    Text scoreText = new Text(Integer.toString(selectedTileScore));
	                    scoreText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));
	                    StackPane temp = new StackPane(square1, letterText, scoreText);
	                    temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);
	                    scoreText.setTranslateX(-5);
	                    scoreText.setTranslateY(-5);
	                    board.add(temp, column, rows);
	                    placedTiles.add(new int[]{column, rows, (int) selectedTileLetter, selectedTileScore});
	                    // Reset the selected tile
	                    selectedTileLetter = ' ';
	                    selectedTileScore=0;
	                }
	            });
	        }
	    }

	    board.setGridLinesVisible(true);
	    player.setGridLinesVisible(true);
	    //new Thread(() -> updateTime()).start();
	    //updateTiles();
	    //initialPlayersName();
	}


	


	/*
	 * name: updateTiles
	 * input: none
	 * output: none
	 * functionality: Updates the tiles on the player board by setting the color, letter, and score of each tile based on the user's tiles and scores.
	 */
	public void updateTiles() {
	    // Loop through the user's tiles
	    for (int i = 0; i < userTiles.length; i++) {
	        // Get the letter and value of the current tile
	        char letter = userTiles[i].getValue();
	        int value = userTilesScore[i].getValue();
	        ObservableList<Node> children = player.getChildren();
			final int col = i; 
			// Remove all StackPanes in the specified cell
			children.removeIf(node -> node instanceof StackPane && GridPane.getRowIndex(node) == 0 && GridPane.getColumnIndex(node) == col);

	        // Create a new square and set its color
	        Rectangle square = new Rectangle();
	        Color color = Color.ANTIQUEWHITE;

	        // Create text for the letter and score
	        Text letterText = new Text(Character.toString(letter));
	        letterText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 20));
	        Text scoreText = new Text(Integer.toString(value));
	        scoreText.setFont(Font.font("Comic Sans MS", FontWeight.BOLD, 16));

	        // Set the square's fill color and create a new StackPane with the square, letterText, and scoreText
	        square.setFill(color);
	        StackPane temp = new StackPane(square, letterText, scoreText);

	        // Bind square size to cell size
	        square.widthProperty().bind(board.widthProperty().divide(15));
	        square.heightProperty().bind(board.heightProperty().divide(15));
	        square.setStroke(Color.BLACK); // Add black border
	        square.setStrokeWidth(1); // Set border width

	        // Bind text size to square size
	        letterText.fontProperty().bind(Bindings.createObjectBinding(() ->
	                Font.font("Comic Sans MS", FontWeight.BOLD, square.getWidth() / 3), square.widthProperty()));
	        scoreText.fontProperty().bind(Bindings.createObjectBinding(() ->
	                Font.font("Comic Sans MS", FontWeight.BOLD, square.getWidth() / 4), square.widthProperty()));

	        // Set the alignment of the scoreText to bottom right
	        temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);

	        // Translate the scoreText to be slightly inside the square
	        scoreText.setTranslateX(-5);
	        scoreText.setTranslateY(-5);

	        temp.setOnMouseClicked(event -> {
	        	

	            // Check if the clicked StackPane is already selected
	            if (temp == selectedTileStackPane) {
	                // Deselect the clicked StackPane
	                selectedTileStackPane.setBorder(Border.EMPTY);
	                selectedTileStackPane = null;
	                selectedTileLetter = ' ';
	                selectedTileScore = 0;
	            } else {
	                // Reset the border of the previously selected tile StackPane
	                if (selectedTileStackPane != null) {
	                    selectedTileStackPane.setBorder(Border.EMPTY);
	                }
	             // Get the tile associated with the clicked StackPane
		            int c1 = GridPane.getColumnIndex(temp);
		            char tileLetter = userTiles[c1].getValue();
		            int tileScore = userTilesScore[c1].getValue();
		            
		            // Set the selected tile
		            selectedTileLetter = tileLetter;
		            selectedTileScore = tileScore;
		            selectedTileIndex=c1;
	                selectedTileStackPane = temp;
	                // Set the border of the selected tile StackPane
	                temp.setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
	            }
	            
	            
	        });
	        // Add the StackPane to the player board
	        player.add(temp, i, 0);
	    }
	}

	/*
	 * name: mouseDown
	 * input: MouseEvent me
	 * output: none
	 * functionality: Handles mouse down events by displaying an input dialog for the user to enter a word and its orientation, and sets the word, score, row, column, and orientation based on the user's input.
	 */
	@SuppressWarnings("static-access")
	public void mouseDown(MouseEvent me) {
	    // Create a new input dialog
	    Dialog<String> inputDialog = new Dialog<>();
	    inputDialog.setTitle("Input Dialog");
	    inputDialog.setHeaderText("Enter your inputs:");

	    // Create a GridPane for the dialog's content
	    GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(20, 150, 10, 10));

	    // Create a TextField for entering the word
	    TextField wordField = new TextField();
	    wordField.setPromptText("Word");

	    // Create radio buttons for selecting the word orientation
	    ToggleGroup group = new ToggleGroup();
	    RadioButton verticalButton = new RadioButton("Vertical");
	    verticalButton.setToggleGroup(group);
	    verticalButton.setSelected(true);
	    RadioButton horizontalButton = new RadioButton("Horizontal");
	    horizontalButton.setToggleGroup(group);

	    // Create an HBox for the radio buttons
	    HBox orientationBox = new HBox(10, verticalButton, horizontalButton);

	    // Add labels and controls to the grid
	    grid.add(new Label("Word:"), 0, 0);
	    grid.add(wordField, 1, 0);
	    grid.add(new Label("Orientation:"), 0, 1);
	    grid.add(orientationBox, 1, 1);

	    // Set the dialog's content and button types
	    inputDialog.getDialogPane().setContent(grid);
	    inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	    // Show the dialog and wait for user input
	    inputDialog.showAndWait();
	    String word = wordField.getText();

	    // Set the word orientation based on the selected radio button
	    this.isvertical.set(verticalButton.isSelected());

	    Node n1 = (Node) me.getSource();

	    // Set the row and column based on the source of the mouse event
	    this.row.set(board.getRowIndex(n1));
	    this.col.set(board.getColumnIndex(n1));

	    
	    //Tile[][] b=new Tile[15][15];
	    //Bag bag = new Bag();
	   // b[7][7] = bag.getRand();
	    //b[7][8]=bag.getRand();
	    //redraw(b);
        this.wordFromUser.set(word.toUpperCase());
	}

    public void skipHandler(){
        this.skipPush.set(true);
	}
	
	public void endHandler(){
	        this.endPush.set(true);
	}
	
	public void saveButton(){
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Save");
	    dialog.setHeaderText("Enter save name:");
	    Optional<String> result = dialog.showAndWait();
	    if (result.isPresent()) {
	        saveFromUser.set(result.get());
	        // store saveName in a String property
	    }
		this.savePush.set(true);
	}

	
	public void mouseUp(MouseEvent me) {
		if(mousePress) {
			mousePress=false;
			System.out.println("up");
		}
	}
	

	public void initialPlayersName() {
		
		for (int i=0;i<userScore.length;i++) {
	    String playerName = userScorename[i].getValue();
	    playerNames.add(playerName);
	    int playerScore = userScore[i].getValue();
	    Label turnIndicator;
	    if(i==0) {
	    	turnIndicator = new Label("->");
	    }
	    else {
	    	turnIndicator = new Label();
	    }
	    Label playerNameLabel = new Label(playerName);
	    Label playerScoreLabel = new Label(String.valueOf(playerScore));

	    players_score.add(turnIndicator, 0, i);
	    players_score.add(playerNameLabel, 1, i);
	    players_score.add(playerScoreLabel, 2, i);
		}
	}


	// Update the turn indicator when the current player changes
	private void updateTurnIndicator() {
		int previousPlayerIndex = currentPlayerIndex.getValue()-1;
		if(previousPlayerIndex<0)previousPlayerIndex=numberOfPlayers.getValue()-1;
		final int row=previousPlayerIndex;
        ObservableList<Node> children = players_score.getChildren();
		
		// Remove all StackPanes in the specified cell
		children.removeIf(node -> node instanceof Label && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == 0);
	    Label turnIndicator = new Label("->");

		final int rownew=currentPlayerIndex.getValue();
		players_score.add(turnIndicator, 0, rownew);
	}
	
	private void updatePlayerScore() {
		int previousPlayerIndex = currentPlayerIndex.getValue()-1;
		if(previousPlayerIndex<0)previousPlayerIndex=numberOfPlayers.getValue()-1;
		final int row=previousPlayerIndex;
		int playerScore = userScore[previousPlayerIndex].getValue();

	        ObservableList<Node> children = players_score.getChildren();
			
			// Remove all StackPanes in the specified cell
			children.removeIf(node -> node instanceof Label && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == 2);
		    
			Label playerScoreLabel = new Label(String.valueOf(playerScore));
			players_score.add(playerScoreLabel, 2, row);
	}
	

	
	// Add a button to allow the user to submit their move
	public void submitButton(){
	    // Check if any tiles have been placed on the game board
	    if (!placedTiles.isEmpty()) {
	        // Sort the placed tiles by row and column
	    	placedTiles.sort(Comparator.comparingInt((int[] a) -> a[0]).thenComparingInt((int[] a) -> a[1]));
	        // Get the start row and column
	        int startRow = placedTiles.get(0)[0];
	        int startCol = placedTiles.get(0)[1];
	        // Check if the tiles are in a horizontal or vertical line
	        boolean isVertical = true;
	        for (int i = 1; i < placedTiles.size(); i++) {
	            if (placedTiles.get(i)[0] != startRow) {
	                isVertical = false;
	                break;
	            }
	        }
	        
	        boolean isHorizontal = true;
	        for (int i = 1; i < placedTiles.size(); i++) {
	            if (placedTiles.get(i)[1] != startCol) {
	                isHorizontal = false;
	                break;
	            }
	        }
	        
	        // Check if the tiles form a valid word
	        StringBuilder wordBuilder = new StringBuilder();
	        if (isVertical||isHorizontal) {
	            for (int[] tile : placedTiles) {
	                wordBuilder.append((char) tile[2]);
	            }
	        } else {
	            // TODO: Handle case where tiles are not in a horizontal or vertical line
	        }
	        String word = wordBuilder.toString();
	    }
	}


	public void clearTableContent() {
	    for (Node node : players_score.getChildren()) {
	        if (node instanceof Label) {
	            ((Label) node).setText("");
	        }
	    }
	}

	/*
	 * name: update
	 * input: java.util.Observable o, Object arg
	 * output: none
	 * functionality: Updates the tiles and redraws the board if the isvalid property is true.
	 */
	@Override
	public void update(java.util.Observable o, Object arg) {
		if(arg.equals("start")) {
			numberOfPlayers.bind(vm.numberOfPlayers);
            this.userScore = new IntegerProperty[numberOfPlayers.getValue()];
    		this.userScorename = new SimpleStringProperty[numberOfPlayers.getValue()];
    		
    		for(int i=0;i<userScore.length;i++) {
    			userScorename[i] = new SimpleStringProperty();
    			userScore[i] = new SimpleIntegerProperty();
    			userScore[i].bind(vm.userScore[i]);
    			userScorename[i].bind(vm.userScorename[i]);
			}
			for(int i=0;i<userTiles.length;i++) {
				userTiles[i].bind(vm.userTiles[i]);
				userTilesScore[i].bind(vm.userTilesScore[i]);
			}
			updateTiles();
			initialPlayersName();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    primaryStage.setScene(boardScene);
		    
		}
		else if(arg.equals("resume")) {
			clearTableContent();
			updateTiles();
			initialPlayersName();
			updatePlayerScore();
		    updateTurnIndicator();
		}

		else {
			 // Check if the isvalid property is true
			//if(isvalid.getValue()) {
			    redraw((Tile[][]) arg);
			    updateTiles();
			//}
		    updatePlayerScore();
		    updateTurnIndicator();
		    //new Thread(() -> updateTime()).start();
		}
	   
	}

	/*
	 * name: pauseScreen
	 * input: none
	 * output: none
	 * functionality: Displays a dialog with a message indicating that the game is waiting for players to join.
	 */
	public void pauseScreen() {
		FXMLLoader fxmlLoader = new FXMLLoader();
		AnchorPane root = null;
		try {
			root = fxmlLoader.load(getClass().getResource("/waitScreen/WaitScreen.fxml").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		WaitScreenController view = fxmlLoader.getController();
		view.setStage(primaryStage);
		Scene scene = new Scene(root,1200,800);
		scene.getStylesheets().add(getClass().getResource("/waitScreen/application.css").toExternalForm());
		primaryStage.setScene(scene);

	}
}