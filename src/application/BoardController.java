package application;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Observer;

import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import viewModel.viewModel;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;

@SuppressWarnings("deprecation")
public class BoardController implements Observer{
	
	viewModel vm;
	public StringProperty wordFromUser,userTiles;
	public BooleanProperty isvertical,isvalid,isHost;
	public IntegerProperty userScore,row,col;
	@FXML
	GridPane board;
	@FXML
	GridPane player;
	@FXML
	BorderPane borderPane;
	
	public BoardController() {
		this.wordFromUser = new SimpleStringProperty();
		this.userTiles = new SimpleStringProperty();
		this.isvertical = new SimpleBooleanProperty();
		this.isHost = new SimpleBooleanProperty();
		this.isvalid = new SimpleBooleanProperty();
		this.userScore = new SimpleIntegerProperty();
		this.row = new SimpleIntegerProperty();
		this.col=new SimpleIntegerProperty();
	}
	
	void init(viewModel vm) {
		this.vm=vm;
		vm.wordFromUser.bind(wordFromUser);
		vm.isvertical.bind(isvertical);
		vm.isHost.bind(isHost);
		vm.isvalid.bind(isvalid);
		vm.row.bind(row);
		vm.col.bind(col);
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
	@SuppressWarnings("static-access")
	public void redraw() {
		char letter = 'A';
		
		int score =5;
		if (!map.containsKey(letter)) {
			Rectangle square = new Rectangle(40,40);
			Color color = Color.ANTIQUEWHITE;
            Text letterText = new Text(Character.toString(letter));
            Text scoreText = new Text(Integer.toString(score));
    		letterText.setFont(Font.font("BN Matan", FontWeight.BOLD, 20));
    		scoreText.setFont(Font.font("BN Matan", FontWeight.BOLD, 16));
            //Font f = new Font(score);
            square.setFill(color);
            StackPane temp = new StackPane(square,letterText,scoreText);
            square.widthProperty().bind(board.widthProperty().divide(15));
            square.heightProperty().bind(board.heightProperty().divide(15));
            temp.setAlignment(scoreText, Pos.BOTTOM_RIGHT);
            map.put(letter, temp);
		}
		
		board.add(map.get(letter), 7, 7);
		//f.bi
        //f.heightProperty().bind(board.heightProperty().divide(15));
		
	}
	Stage stage;
	public void setStage(Stage stage){
		this.stage=stage;
		}
	
	
	@SuppressWarnings("static-access")
	public void initialize() {
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
        DataFormat StackPaneFormat = new DataFormat("StackPane");

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
         double W = board.getWidth();
         double H = board.getHeight();
         double w = W / boardData[0].length;
         double h = H / boardData.length;
		 for (int row = 0; row <= 15; row++) {
		        for (int col = 0; col <= 15; col++) {
		            // Adding horizontal lines
		            Line horizontalLine = new Line();
		            horizontalLine.setStroke(Color.BLACK);
		            horizontalLine.setStartX(0);
		            horizontalLine.setEndX(W);
		            horizontalLine.setStartY(h * row);
		            horizontalLine.setEndY(h * row);
		            board.getChildren().add(horizontalLine);

		            // Adding vertical lines
		            Line verticalLine = new Line();
		            verticalLine.setStroke(Color.BLACK);
		            verticalLine.setStartX(w * col);
		            verticalLine.setEndX(w * col);
		            verticalLine.setStartY(0);
		            verticalLine.setEndY(H);
		            board.getChildren().add(verticalLine);
		        }
	    }*/
		//redraw();
		
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
        this.wordFromUser.set(wordField.getText());
        this.isvertical.set(verticalButton.isSelected());
        Node n1 = (Node)me.getSource();
		//Integer col = board.getColumnIndex(n1);
		//Integer row = board.getRowIndex(n1);
		//System.out.println("row" + row +"column" + col);
        this.row.set(board.getRowIndex(n1));
        this.col.set(board.getColumnIndex(n1));
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
		// TODO Auto-generated method stub
		
	}
	
}
