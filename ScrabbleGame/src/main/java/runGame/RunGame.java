package runGame;


import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import openScreen.OpenScreenController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class RunGame extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			  // Screen 1: Opening screen
            FXMLLoader openingLoader = new FXMLLoader();
            AnchorPane openingRoot = null;
            try { 
            	openingRoot = openingLoader.load(getClass().getResource("/openScreen/OpenScreen.fxml").openStream());
            }catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
            OpenScreenController openingController = openingLoader.getController();
            openingController.paint();
            Scene openingScene = new Scene(openingRoot, 1200, 800);
            openingController.setStage(primaryStage);
            primaryStage.setScene(openingScene);
			
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}

