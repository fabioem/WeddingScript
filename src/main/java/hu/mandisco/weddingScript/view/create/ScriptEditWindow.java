package hu.mandisco.weddingScript.view.create;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ScriptEditWindow {

	// private BorderPane layout = new BorderPane();

	public static void display() {
		Stage window = new Stage();
		Scene scene = new Scene(new GridPane(), 300, 200);
		window.setScene(scene);
		window.showAndWait();
	}
}
