package ViewPackage;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
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
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class BoardController {
	@FXML
	GridPane board;
	@FXML
	GridPane player;
	@FXML
	BorderPane borderPane;
	
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
							//star.scaleXProperty().bind(square.widthProperty().divide(80));
							//star.scaleYProperty().bind(square.heightProperty().divide(80));
							star.scaleXProperty().bind(square.widthProperty().divide(100));
							star.scaleYProperty().bind(square.heightProperty().divide(100));
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
                	stackPane.getChildren().addAll(square, text, star);
                	//StackPane.setAlignment(star, Pos.CENTER);
                	StackPane.setAlignment(square, Pos.CENTER);
                }
                final int colum = col;
                final int rows=row;
                board.add(stackPane, col, row);
                stack[col][row] = stackPane;


                //stackPane.setOnMousePressed(evt -> this.mouseDown(evt));
                //stackPane.setOnDragDropped(e->e.acceptTransferModes(TransferMode.ANY));
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
	public void mouseDown(MouseEvent me) {
		//(!mousePress) {
			//mousePress=true;
			//System.out.println("push");
		//}
		
		Node n1 = (Node)me.getSource();
		Integer col = board.getColumnIndex(n1);
		Integer row = board.getRowIndex(n1);
		System.out.println("row" + row +"column" + col);
		
		// create a label
        Label label = new Label("This is a Popup");
   
        // create a popup
        Popup popup = new Popup();
   
        // set background
        label.setStyle(" -fx-background-color: white;");
   
        // add the label
        popup.getContent().add(label);
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        Button button = new Button("Submit");
        button.setTranslateX(250);
        button.setTranslateY(75);
        //Creating labels
        Label label1 = new Label("Name: ");
        Label label2 = new Label("Email: ");
        
      //Setting the message with read data
        Text text = new Text("");
        //Setting font to the label
        Font font = Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 10);
        text.setFont(font);
        text.setTranslateX(15);
        text.setTranslateY(125);
        text.setFill(Color.BROWN);
        text.maxWidth(580);
        text.setWrappingWidth(580);
        
        button.setOnAction(e -> {
            //Retrieving data
            String name = textField1.getText();
            String email = textField2.getText();
            text.setText("Hello "+name+"Welcome to Tutorialspoint. From now, we will communicate with you at"+email);
         });
        //Adding labels for nodes
        HBox box = new HBox(5);
        box.setPadding(new Insets(25, 5 , 5, 50));
        box.getChildren().addAll(label1, textField1, label2, textField2);
        Group root = new Group(box, button, text);
        
        Scene scene = new Scene(root, 595, 150, Color.BEIGE);
        stage.setTitle("Text Field Example");
        
        popup.getContent().add(box);
        // set size of label
        label.setMinWidth(80);
        label.setMinHeight(50);
        
        popup.show(this.stage);
	}
	public void mouseUp(MouseEvent me) {
		if(mousePress) {
			mousePress=false;
			System.out.println("up");
		}
	}
	
}
