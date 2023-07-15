package OpenScreen;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class testmain2 extends Application {
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
