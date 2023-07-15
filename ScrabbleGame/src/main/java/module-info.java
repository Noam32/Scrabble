module barak1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
	requires javafx.graphics;
	requires javafx.base;
    requires org.mongodb.bson;
	requires org.mongodb.driver.sync.client;
	requires org.mongodb.driver.core;
	requires mongojack;
    
    opens viewPackage to javafx.graphics, javafx.fxml;
   	opens openScreen to javafx.graphics, javafx.fxml;
   	opens waitScreen to javafx.graphics, javafx.fxml;
   	opens runGame to javafx.graphics, javafx.fxml;
}