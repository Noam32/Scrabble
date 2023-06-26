module board {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
    requires org.mongodb.bson;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.driver.core;
    requires mongojack;

    opens ViewPackage to javafx.graphics, javafx.fxml;
	opens OpenScreen to javafx.graphics, javafx.fxml;
	opens WaitScreen to javafx.graphics, javafx.fxml;
	
}
