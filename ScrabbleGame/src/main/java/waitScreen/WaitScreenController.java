package waitScreen;

import java.io.IOException;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelPackage.Model;
import modelPackage.ModelHost;
import viewModel.ViewModel;
import viewPackage.BoardController;

public class WaitScreenController {

	Stage primaryStage;
	@FXML
	Button backButton;
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private Label titleLabel;
	public void setStage(Stage primaryStage) {
		// TODO Auto-generated method stub
		this.primaryStage=primaryStage;
	}
	
	public void paint() {
	   
	    backButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    backButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.2));
	    
	   
	
	}
	

	public void backGame() {
		System.out.println("back");
		
		FXMLLoader fxmlLoader = new FXMLLoader();
		BorderPane root = null;
		try {
			root = fxmlLoader.load(getClass().getResource("/viewPackage/Board.fxml").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BoardController view = fxmlLoader.getController();
		view.setStage(primaryStage);
		Scene scene = new Scene(root,1200,800);
		scene.getStylesheets().add(getClass().getResource("/viewPackage/application.css").toExternalForm());
		primaryStage.setScene(scene);
	}
}
