package hu.mandisco.weddingScript.view.create;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Program;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgramCreateWindow {

	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	private ObservableList<Program> programItems;

	public ProgramCreateWindow(ObservableList<Program> programItems) {
		this.programItems = programItems;
	}

	public void display() {
		Stage window = new Stage();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Új program");
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
		Label defaultTimeLabel = new Label("Alapértelmezett időpont:");
		GridPane.setConstraints(defaultTimeLabel, 0, 1);

		// Date Input
		// TimePicker

		// TODO Number Spinner D HH MM
		// DatePicker defaultTimeInput = new DatePicker();
		// GridPane.setConstraints(defaultTimeInput, 1, 1);

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 3);
		saveButton.setOnAction(e -> {
			Program program = new Program();
			program.setName(nameInput.getText());
			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();
			} else {
				weddingScriptController.addProgram(program);
				programItems.clear();
				programItems.addAll(weddingScriptController.getPrograms());
				window.close();
			}

		});

		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 3);

		// Add everything to grid
		grid.getChildren().addAll(nameLabel, nameInput, saveButton, closeButton);

		Scene scene = new Scene(grid, 300, 200);
		window.setScene(scene);
		window.showAndWait();
	}

}
