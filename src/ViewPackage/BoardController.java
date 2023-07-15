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
	Stage primaryStage;
	public void setStage(Stage stage){
		this.primaryStage=stage;
		}
	
	
	@SuppressWarnings("static-access")
	public void paint() {
		mousePress = false;
	    stack = new StackPane[15][15]; // Initialize the stack array
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
     //   DataFormat StackPaneFormat = new DataFormat("StackPane");

		for (int row = 0; row < 15; row++) {				//filling board with squares
            for (int col = 0; col < 15; col++) {
            	Rectangle square = new Rectangle();
                Color color = null;
                Text text = null;
                switch(boardData[row][col]) {
                	case 0: color = Color.GREEN;
        					text = new Text(" ");
                		break;
                	case 1: color = Color.CYAN;
                			text = new Text("double\n letter");
                		break;
                	case 2: color = Color.YELLOW;
        					text = new Text("double\n word");
                		break;
                	case 3: color = Color.DODGERBLUE;
							text = new Text("triple\n letter");
                		break;
                	case 4: color = Color.ORANGERED;
							text = new Text("triple\n word");
                		break;
                	case 5: color = Color.YELLOW;
							text = new Text("");
							star.scaleXProperty().bind(square.widthProperty().divide(80));
							star.scaleYProperty().bind(square.heightProperty().divide(80));
							//star.scaleXProperty().bind(square.widthProperty().divide(100));
							//star.scaleYProperty().bind(square.heightProperty().divide(100));
                		break;
                }
                square.setFill(color);
                square.widthProperty().bind(board.widthProperty().divide(15));
                square.heightProperty().bind(board.heightProperty().divide(15));
                square.setStroke(Color.BLACK); // Add black border
                square.setStrokeWidth(1); // Set border width
                StackPane stackPane = new StackPane();
                if(boardData[row][col]!=5) {
                	stackPane.getChildren().addAll(square,text);
                }
                else {
                	stackPane.getChildren().addAll(square, star);
                	square.setTranslateX(-12);
                	star.setTranslateX(-12);

                }
                final int colum = col;
                final int rows=row;
                board.add(stackPane, col, row);
                stack[col][row] = stackPane;

                /**
                 * view model get  array of letter of player
                 */


                stackPane.setOnMousePressed(evt -> this.mouseDown(evt));
                //stackPane.setOnDragDropped(e->e.acceptTransferModes(TransferMode.ANY));
               /* stackPane.setOnDragDetected(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        /* drag was detected, start a drag-and-drop gesture*/
                        /* allow any transfer mode */
                /*
                        Dragboard db = stackPane.startDragAndDrop(TransferMode.ANY);
                        
                        /* Put a string on a dragboard */
    /*                    ClipboardContent content = new ClipboardContent();
                        //content.putString(stackPane.getText());
                        content.put(StackPaneFormat, stackPane);
                        db.setContent(content);
                        
                        event.consume();
                    }
                });
                stackPane.setOnDragDropped(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data dropped */
                        /* if there is a StackPane on the dragboard, read it and use it */
      /*                  Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasContent(StackPaneFormat)) {
                            StackPane droppedStackPane = (StackPane) db.getContent(StackPaneFormat);
                            
                            // Remove the dropped StackPane from its original location
                            GridPane sourceGridPane = (GridPane) droppedStackPane.getParent();
                            sourceGridPane.getChildren().remove(droppedStackPane);
                            
                            // Add the dropped StackPane to the target location
                            GridPane targetGridPane = (GridPane) stackPane.getParent();
                            int targetColumnIndex = GridPane.getColumnIndex(stackPane);
                            int targetRowIndex = GridPane.getRowIndex(stackPane);
                            targetGridPane.add(droppedStackPane, targetColumnIndex, targetRowIndex);
                            
                            success = true;
                        }
                        /* let the source know whether the StackPane was successfully transferred and used */
      /*                  event.setDropCompleted(success);
                        
                        event.consume();
                    }
                });
                
                stackPane.setOnDragOver(new EventHandler<DragEvent>() {
                    public void handle(DragEvent event) {
                        /* data is dragged over the target */
                        /* accept it only if it is not dragged from the same node 
                         * and if it has a StackPane data */
       /*                 if (event.getGestureSource() != stackPane &&
                                event.getDragboard().hasContent(StackPaneFormat)) {
                            /* allow for moving */
      /*                      event.acceptTransferModes(TransferMode.MOVE);
                        }
                        
                        event.consume();
                    }
                });
        */        
                
                
                
                }
            
		}
		board.setGridLinesVisible(true);
		player.setGridLinesVisible(true);






		//board.setHgap(5);
		//board.setVgap(5);
	
		/*
		for(int j=0;j<7;j++) {
			
			Rectangle square = new Rectangle();
			Color color = Color.ANTIQUEWHITE;
	        Text letterText = new Text(Character.toString('A'));
	        Text scoreText = new Text(Integer.toString(5));
			letterText.setFont(Font.font("BN Matan", FontWeight.BOLD, 20));
			scoreText.setFont(Font.font("BN Matan", FontWeight.BOLD, 16));
	        //Font f = new Font(score);
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
	        
	      
			player.add(temp, j, 0);
		}
		
		*/
		updateTiles();
		time();
	}
	

    public void time() {
       
    }
	// In your BoardController class
	
	public void updateTiles() {
	    for (int i=0;i<userTiles.length;i++) {
	    	char letter = userTiles[i].getValue();
	        int value = userTilesScore[i].getValue();
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
			player.add(temp, i, 0);
	    }
	}

	public void mouseEntered(MouseEvent me) {
		Node n1 = (Node)me.getSource();
		Integer col = board.getColumnIndex(n1);
		Integer row = board.getRowIndex(n1);
		System.out.println("row" + row +"column" + col);
		
		
        }
	@SuppressWarnings("static-access")
	public void mouseDown(MouseEvent me) {
		//(!mousePress) {
			//mousePress=true;
			//System.out.println("push");
		//}
		
		

        Dialog<String> inputDialog = new Dialog<>();
        inputDialog.setTitle("Input Dialog");
        inputDialog.setHeaderText("Enter your inputs:");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        TextField wordField = new TextField();
        wordField.setPromptText("Word");

        ToggleGroup group = new ToggleGroup();
        RadioButton verticalButton = new RadioButton("Vertical");
        verticalButton.setToggleGroup(group);
        verticalButton.setSelected(true);
        RadioButton horizontalButton = new RadioButton("Horizontal");
        horizontalButton.setToggleGroup(group);

        HBox orientationBox = new HBox(10, verticalButton, horizontalButton);

        grid.add(new Label("Word:"), 0, 0);
        grid.add(wordField, 1, 0);
        grid.add(new Label("Orientation:"), 0, 1);
        grid.add(orientationBox, 1, 1);

        inputDialog.getDialogPane().setContent(grid);
        inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        inputDialog.showAndWait();
        String word = wordField.getText();
        //put the thin
        this.isvertical.set(verticalButton.isSelected());
        Node n1 = (Node)me.getSource();
		//Integer col = board.getColumnIndex(n1);
		//Integer row = board.getRowIndex(n1);
		//System.out.println("row" + row +"column" + col);
        this.row.set(board.getRowIndex(n1));
        this.col.set(board.getColumnIndex(n1));
        this.word = new char[word.length()];
        this.score = new int[word.length()];
        for(int i=0;i<word.length();i++) {
            this.word[i]=word.charAt(i);
            for(int j=0;j<userTiles.length;j++) {
    				if (this.word[i]==userTiles[j].get()) {
    					this.score[i]=userTilesScore[j].getValue();
    					break;
    				}
        this.wordFromUser.set(word);
        
    			
            
            
        }
        
        
	}
        boolean isVertical = verticalButton.isSelected();
        System.out.println("User entered: " + word + ", " + (isVertical ? "Vertical" : "Horizontal"));
	}
	
	public void mouseUp(MouseEvent me) {
		if(mousePress) {
			mousePress=false;
			System.out.println("up");
		}
	}

	@Override
	public void update(java.util.Observable o, Object arg) {

		System.out.println("barakaaaaaaaaaaa");
		//if (o==vm) {
			if(isvalid.getValue()) {
				updateTiles();
				redraw();
		//	}

		}		
	}

	public void pauseScreen() {

        
      
        
		
		// Wait for players to join
		//while (numberOfPlayers.getValue() < 2) {
			Dialog<String> inputDialog = new Dialog<>();
	        inputDialog.setTitle("waiting zone");

	        GridPane grid = new GridPane();
	        grid.setHgap(10);
	        grid.setVgap(10);
	        grid.setPadding(new Insets(20, 150, 10, 10));
	        

	  

	        grid.add(new Label("Waiting for players..."), 0, 0);
	        
	        inputDialog.getDialogPane().setContent(grid);
	        inputDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

	        inputDialog.showAndWait();
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
	
}
