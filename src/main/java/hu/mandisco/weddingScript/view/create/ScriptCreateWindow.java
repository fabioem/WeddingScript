package hu.mandisco.weddingScript.view.create;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Script;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptCreateWindow {

	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	private ObservableList<Script> scriptItems;

	public ScriptCreateWindow(ObservableList<Script> scriptItems) {
		this.scriptItems = scriptItems;
	}

	public void display() {
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

		// Date Label
		Label dateLabel = new Label("Dátum:");
		GridPane.setConstraints(dateLabel, 0, 1);

		// Date Input
		DatePicker dateInput = new DatePicker();
		GridPane.setConstraints(dateInput, 1, 1);

		// Comment Label
		Label commentLabel = new Label("Komment:");
		GridPane.setConstraints(commentLabel, 0, 2);

		// Comment Input
		TextField commentInput = new TextField();
		commentInput.setPromptText("Komment");
		GridPane.setConstraints(commentInput, 1, 2);

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 3);
		saveButton.setOnAction(e -> {
			Script script = new Script(nameInput.getText(),
					dateInput.getValue() == null ? null : dateInput.getValue().atStartOfDay(), commentInput.getText());
			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();

			} else {
				weddingScriptController.addScript(script);
				//scriptItems.add(script);
				scriptItems.clear();
				scriptItems.addAll(weddingScriptController.getScripts());
				window.close();
			}

		});

		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 3);

		// Add everything to grid
		grid.getChildren().addAll(nameLabel, nameInput, commentLabel, commentInput, dateLabel, dateInput, saveButton,
				closeButton);

		Scene scene = new Scene(grid, 300, 200);
		window.setScene(scene);
		window.showAndWait();
	}
}
