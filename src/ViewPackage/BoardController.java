package ViewPackage;

import java.util.HashMap;
import java.util.LinkedHashMap;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.shape.Line;
import javafx.scene.control.TextArea;

public class BoardController {
	@FXML
	GridPane board;
	
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
    		letterText.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
    		scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
            Font f = new Font(score);
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
	
	public void initialize() {
	    stack = new StackPane[15][15]; // Initialize the stack array
		for (int row = 0; row < 15; row++) {				//filling board with squares
            for (int col = 0; col < 15; col++) {
            	Rectangle square = new Rectangle(40,40);
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
							text = new Text(" ");
                		break;
                }
                square.setFill(color);
                StackPane stackPane = new StackPane(square, text);
                board.add(stackPane, col, row);
                square.widthProperty().bind(board.widthProperty().divide(15));
                square.heightProperty().bind(board.heightProperty().divide(15));
                stack[col][row] = stackPane;
            }
		}
		board.setGridLinesVisible(true);
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
		redraw();
	}
	
	public void mouseDown(MouseEvent me) {
		
	}
}
