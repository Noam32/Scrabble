module board {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
	opens ViewPackage to javafx.graphics, javafx.fxml;
	opens OpenScreen to javafx.graphics, javafx.fxml;
	opens WaitScreen to javafx.graphics, javafx.fxml;
	
}
