package OpenScreen;

import java.io.IOException;

import ModelPackage.Model;
import ModelPackage.ModelHost;
import ViewPackage.BoardController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import viewModel.viewModel;

public class OpenScreenController {

	Stage primaryStage;
	@FXML
	Button startButton;
	@FXML
	Button resumeButton;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label titleLabel;
	public void setStage(Stage primaryStage) {
		// TODO Auto-generated method stub
		this.primaryStage=primaryStage;
	}
	
	public void paint() {
	    startButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    startButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.2));
	    resumeButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    resumeButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.2));
	    
	   
	
	}
	
	public void startGame() {
		System.out.println("start");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		BorderPane root = null;
		try {
			root = fxmlLoader.load(getClass().getResource("/ViewPackage/Board.fxml").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BoardController view = fxmlLoader.getController();
		view.setStage(primaryStage);
		Scene scene = new Scene(root,1200,800);
		scene.getStylesheets().add(getClass().getResource("/ViewPackage/application.css").toExternalForm());
		//primaryStage.setScene(scene);

	    // Code to run the ModelHost in a separate thread
		Model m = new ModelHost();
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		viewModel vm =new viewModel(m,"HostPlayer");
		System.out.println("viewmodel");
		view.init(vm);
		view.setBoardScene(scene);
		view.pauseScreen();
		primaryStage.setTitle("HostPlayer");
		view.paint();
	}

	public void resumeGame() {
		System.out.println("resume");
	}
}
