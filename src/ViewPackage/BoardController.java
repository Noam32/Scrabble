package ViewPackage;

import java.util.LinkedHashMap;
import java.util.Observer;

import javafx.animation.Animation;
import javafx.animation.Timeline;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
import viewModel.viewModel;

@SuppressWarnings("deprecation")
public class BoardController implements Observer {
	
	viewModel vm;
	private StringProperty wordFromUser;
	private BooleanProperty isvertical,isvalid,isHost;
	private IntegerProperty row,col,numberOfPlayers;
	private MapProperty<StringProperty, IntegerProperty> userScore;
	public ObjectProperty<Character>[] userTiles = new ObjectProperty[7];
	public IntegerProperty[] userTilesScore = new IntegerProperty[7];
	char[] word;
	int[] score;
	
	@FXML
	GridPane board;
	@FXML
	GridPane player;
	@FXML
	BorderPane borderPane;
	@FXML
    private Label timerLabel;
	private static final int STARTTIME = 0;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME);

    private Timeline timeline;
	
    
    
   
	
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
	        // increment seconds
	        int seconds = timeSeconds.get();
	        timeSeconds.set(seconds + 1);
	    }

	    public void handle(ActionEvent event) {
	     //   timeline = new Timeline(new KeyFrame(Duration.seconds(1), evt -> updateTime()));
	        timeline.setCycleCount(Animation.INDEFINITE); // repeat over and over again
	        timeSeconds.set(STARTTIME);
	        timeline.play();
	    }
	    
	    /*
		  Function name: BoardController
		  Input: None
		  Output: None
		  Functionality: Constructor for the BoardController class. Initializes various properties and arrays.
	     */
		public BoardController() {
			this.wordFromUser = new SimpleStringProperty();
			this.isvertical = new SimpleBooleanProperty();
			this.isHost = new SimpleBooleanProperty();
			this.isvalid = new SimpleBooleanProperty();
			this.userScore = new SimpleMapProperty<>();
			this.row = new SimpleIntegerProperty();
			this.col=new SimpleIntegerProperty();
			this.numberOfPlayers = new SimpleIntegerProperty();

			for (int i = 0; i < userTiles.length; i++) {
				userTiles[i] = new SimpleObjectProperty<>();
			}

			for (int i = 0; i < userTilesScore.length; i++) {
				userTilesScore[i] = new SimpleIntegerProperty();
			}
		}
		
		/*
		 Function name: init
		 Input: viewModel vm - the view model object
		 Output: None
		 Functionality: Initializes the BoardController with the provided view model and binds various properties.
		 */
		void init(viewModel vm) {
			this.vm=vm;
			vm.addObserver(this);
			vm.wordFromUser.bind(wordFromUser);
			vm.isvertical.bind(isvertical);
			vm.isHost.bind(isHost);
			vm.row.bind(row);
			vm.col.bind(col);
			userScore.bind(vm.userScore);
			isvalid.bind(vm.isvalid);
			numberOfPlayers.bind(vm.numberOfPlayers);
			for(int i=0;i<userTiles.length;i++) {
				userTiles[i].bind(vm.userTiles[i]);
				userTilesScore[i].bind(vm.userTilesScore[i]);
			}
			
		}
		
	/*
	Function name: redraw
	Input: None
	Output: None
	Functionality: Redraws the board with the current word and scores.
	*/
	
	public void redraw() {
		if(this.word!=null) {
	    for (int i=0;i<this.word.length;i++) {
	    	char letter = this.word[i];
	        int value = this.score[i];
	    	Rectangle square = new Rectangle();
			Color color = Color.ANTIQUEWHITE;
			Text letterText = new Text(Character.toString(letter));
		    letterText.setFont(Font.font("BN Matan", FontWeight.BOLD, 20));
		    Text scoreText = new Text(Integer.toString(value));
		    scoreText.setFont(Font.font("BN Matan", FontWeight.BOLD, 16));

	        square.setFill(color);
	        StackPane temp = new StackPane(square,letterText,scoreText);
	     // Bind square size to cell size
	        square.widthProperty().bind(board.widthProperty().divide(15));
	        square.heightProperty().bind(board.heightProperty().divide(15));
	        square.setStroke(Color.BLACK); // Add black border
	        square.setStrokeWidth(1); // Set border width
	        
	        // Bind text size to square size
	        letterText.fontProperty().bind(Bindings.createObjectBinding(() ->
	                Font.font("BN Matan", FontWeight.BOLD, square.getWidth() / 3), square.widthProperty()));
	        scoreText.fontProperty().bind(Bindings.createObjectBinding(() ->
	                Font.font("BN Matan", FontWeight.BOLD, square.getWidth() / 4), square.widthProperty()));

	        temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);

	        scoreText.setTranslateX(-5);
	        scoreText.setTranslateY(-5);    
			player.add(temp, this.col.getValue(), this.row.getValue());
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
	
	
	
	/*
	 * name: paint
	 * input: none
	 * output: none
	 * functionality: Initializes the stack array, creates a star polygon, sets the color and text of each square on the board based on its value in boardData, adds mouse event handling, and updates tiles and time.
	 */
	@SuppressWarnings("static-access")
	public void paint() {
	    mousePress = false;
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
	        }
	    }

	    board.setGridLinesVisible(true);
	    player.setGridLinesVisible(true);

	    updateTiles();
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

	        // Create a new square and set its color
	        Rectangle square = new Rectangle();
	        Color color = Color.ANTIQUEWHITE;

	        // Create text for the letter and score
	        Text letterText = new Text(Character.toString(letter));
	        letterText.setFont(Font.font("BN Matan", FontWeight.BOLD, 20));
	        Text scoreText = new Text(Integer.toString(value));
	        scoreText.setFont(Font.font("BN Matan", FontWeight.BOLD, 16));

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
	                Font.font("BN Matan", FontWeight.BOLD, square.getWidth() / 3), square.widthProperty()));
	        scoreText.fontProperty().bind(Bindings.createObjectBinding(() ->
	                Font.font("BN Matan", FontWeight.BOLD, square.getWidth() / 4), square.widthProperty()));

	        // Set the alignment of the scoreText to bottom right
	        temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);

	        // Translate the scoreText to be slightly inside the square
	        scoreText.setTranslateX(-5);
	        scoreText.setTranslateY(-5);

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

	    this.word = new char[word.length()];
	    this.score = new int[word.length()];

	    // Set the word and score arrays based on the user's input
	    for (int i = 0; i < word.length(); i++) {
	        this.word[i] = word.charAt(i);
	        for (int j = 0; j < userTiles.length; j++) {
	            if (this.word[i] == userTiles[j].get()) {
	                this.score[i] = userTilesScore[j].getValue();
	                break;
	            }
	            this.wordFromUser.set(word);
	        }
	    }
	}

	
	public void mouseUp(MouseEvent me) {
		if(mousePress) {
			mousePress=false;
			System.out.println("up");
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
	    // Check if the isvalid property is true
	    if (isvalid.getValue()) {
	        // Update the tiles and redraw the board
	        updateTiles();
	        redraw();
	    }
	}

	/*
	 * name: pauseScreen
	 * input: none
	 * output: none
	 * functionality: Displays a dialog with a message indicating that the game is waiting for players to join.
	 */
	public void pauseScreen() {
	    // Create a new input dialog
	    Dialog<String> inputDialog = new Dialog<>();
	    inputDialog.setTitle("waiting zone");

	    // Create a GridPane for the dialog's content
	    GridPane grid = new GridPane();
	    grid.setHgap(10);
	    grid.setVgap(10);
	    grid.setPadding(new Insets(20, 150, 10, 10));

	    // Add a label to the grid with a message indicating that the game is waiting for players to join
	    grid.add(new Label("Waiting for players..."), 0, 0);

	    // Set the dialog's content and button types
	    inputDialog.getDialogPane().setContent(grid);
	    inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	    // Show the dialog and wait for user input
	    inputDialog.showAndWait();

	    // Sleep for 5 seconds
	    try {
	        Thread.sleep(5000);
	    } catch (InterruptedException e) {
	        e.printStackTrace();
	    }
	}
}