package openScreen;

import java.io.IOException;
import java.util.Optional;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import modelPackage.GameState;
import modelPackage.Model;
import modelPackage.ModelGuest;
import modelPackage.ModelHost;
import mongodbCommunicationPackage.MongoDbMethods;
import viewModel.ViewModel;
import viewPackage.BoardController;

public class OpenScreenController {

	public static String optionSelected="start";
	public static GameState resumeGameChoosen;
	Stage primaryStage;
	@FXML
	Button startButton;
	@FXML
	Button joinButton;
	@FXML
	Button resumeButton;
	@FXML
	Button joinSavedButton;
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
	    startButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.1));
	    resumeButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    resumeButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.1));
	    joinButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    joinButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.1));
	    joinSavedButton.prefWidthProperty().bind(anchorPane.widthProperty().multiply(0.4));
	    joinSavedButton.prefHeightProperty().bind(anchorPane.heightProperty().multiply(0.1));
	}
	
	public void startGame() {
		System.out.println("start");
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
		//primaryStage.setScene(scene);

	    // Code to run the ModelHost in a separate thread
		Model m = new ModelHost();
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ViewModel vm =new ViewModel(m,"HostPlayer");
		System.out.println("viewmodel");
		view.init(vm);
		view.setBoardScene(scene);
		view.pauseScreen();
		primaryStage.setTitle("HostPlayer");
		view.paint();
	}
	
	public void joinGame() {
		System.out.println("join");
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
		//primaryStage.setScene(scene);

		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Join Game");
	    dialog.setHeaderText("Enter your game name:");
	    Optional<String> result = dialog.showAndWait();
	    // Code to run the ModelHost in a separate thread
		Model m = new ModelGuest(result.get());
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ViewModel vm =new ViewModel(m,result.get());
		System.out.println("viewmodel");
		view.init(vm);
		view.setBoardScene(scene);
		view.pauseScreen();
		primaryStage.setTitle("Scrabble-"+result.get());
		view.paint();
	}

	public void resumeGame() {
		optionSelected="resume";
		String saves = MongoDbMethods.getAllNamesOfGameSaves();
		int playerCounter = 0;
		String[] saveNames = saves.split("\n");
		ChoiceBox<String> saveChoiceBox = new ChoiceBox<>();
		saveChoiceBox.getItems().addAll(saveNames);
		saveChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
		    if (newSelection != null) {
		    	
		    		 resumeGameChoosen =MongoDbMethods.getGameSaveFromMongo(newSelection);
		    		 startGame();
		    		    
		    	}
		    });

	    // Create a new dialog
	    Dialog<String> dialog = new Dialog<>();
	    dialog.setTitle("Resume Game");
	    dialog.setHeaderText("Choose a saved game to resume:");

	    // Set the content of the dialog to the ChoiceBox
	    dialog.getDialogPane().setContent(saveChoiceBox);

	    // Add a button to the dialog
	    ButtonType resumeButtonType = new ButtonType("Resume", ButtonData.OK_DONE);
	    dialog.getDialogPane().getButtonTypes().addAll(resumeButtonType, ButtonType.CANCEL);

	    // Show the dialog and wait for the user to close it
	    dialog.showAndWait();
	    //Optional<String> result = dialog.showAndWait();
	    //String choice = result.get();
	    // Handle the result of the dialog
	    //if (result.isPresent() && result.get().equals("Resume")) {
	        // The user clicked the Resume button
	       // String selectedSave = saveChoiceBox.getSelectionModel().getSelectedItem();
	        // Load the selected saved game
	    //}
		System.out.println("resume");
	}
	
	public void joinSavedGame() {
		System.out.println("resumeJoin");
		optionSelected="resumeGuest";
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
		//primaryStage.setScene(scene);

		TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Join Game");
	    dialog.setHeaderText("Enter your game name:");
	    Optional<String> result = dialog.showAndWait();
	    // Code to run the ModelHost in a separate thread
		Model m = new ModelGuest(result.get());
		/*try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		ViewModel vm =new ViewModel(m,result.get());
		System.out.println("viewmodel");
		view.init(vm);
		view.setBoardScene(scene);
		view.pauseScreen();
		primaryStage.setTitle("Scrabble-"+result.get());
		view.paint();
	}

}
