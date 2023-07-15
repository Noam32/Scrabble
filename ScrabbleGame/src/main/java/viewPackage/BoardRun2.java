package viewPackage;
	
import javafx.application.Application;
import javafx.stage.Stage;
import modelPackage.Model;
import modelPackage.ModelGuest;
import modelPackage.ModelHost;
import viewModel.ViewModel;
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
			Scene scene = new Scene(root,1200,800);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);

			
		
			    // Code to run the ModelHost in a separate thread
				Model m = new ModelGuest("ModelGuest");
				m.getGameState();
				ViewModel vm =new ViewModel(m,"ModelGuest");
				view.init(vm);
				//view.pauseScreen();
				view.paint();
				primaryStage.setTitle("ModelGuest");
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
