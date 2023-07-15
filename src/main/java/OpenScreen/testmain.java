package OpenScreen;
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;


public class testmain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
			  // Screen 1: Opening screen
            FXMLLoader openingLoader = new FXMLLoader();
            AnchorPane openingRoot = openingLoader.load(getClass().getResource("OpenScreen.fxml").openStream());
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
