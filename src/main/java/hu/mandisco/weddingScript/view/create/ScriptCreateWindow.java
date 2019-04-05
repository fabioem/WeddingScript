package hu.mandisco.weddingScript.view.create;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptCreateWindow {

	public static void display() {
		Stage window = new Stage();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Új forgatókönyv");
		window.setMinWidth(250);

		// GridPane with 10px padding around edge
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		// Name Label - constrains use (child, column, row)
		Label nameLabel = new Label("Név:");
		GridPane.setConstraints(nameLabel, 0, 0);

		// Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 0);
		nameInput.setPromptText("Név");

		// Password Label
		Label dateLabel = new Label("Dátum:");
		GridPane.setConstraints(dateLabel, 0, 1);

		// Password Input
		TextField dateInput = new TextField();
		dateInput.setPromptText("Dátum");
		GridPane.setConstraints(dateInput, 1, 1);

		// Login
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 2);

		Button closeButton = new Button("Close this window");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 2);

		// Add everything to grid
		grid.getChildren().addAll(nameLabel, nameInput, dateLabel, dateInput, saveButton, closeButton);

		Scene scene = new Scene(grid, 300, 200);
		window.setScene(scene);
		window.showAndWait();
	}
}
