package ViewPackage;
	
import ModelPackage.Model;
import ModelPackage.ModelGuest;
import ModelPackage.ModelHost;
import javafx.application.Application;
import javafx.stage.Stage;
import viewModel.viewModel;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class BoardRun2 extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		

            

			//BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Board.fxml"));
			
			//FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Board.fxml"));
            //Parent root = (Parent) fxmlLoader.load();
			//((BoardController) fxmlLoader.getController()).setStage(primaryStage);
			FXMLLoader fxmlLoader = new FXMLLoader();
			BorderPane root = fxmlLoader.load(getClass().getResource("Board.fxml").openStream());
			BoardController view = fxmlLoader.getController();
			view.setStage(primaryStage);
			Scene scene = new Scene(root,800,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			
		
			    // Code to run the ModelHost in a separate thread
				Model m = new ModelGuest("xxx");
				m.getGameState();
				viewModel vm =new viewModel(m,"xxx");
				view.init(vm);
				//view.pauseScreen();
				view.paint();
				primaryStage.show();
				
		
			//view.updateTiles();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
