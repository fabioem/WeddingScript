package hu.mandisco.weddingScript.view.create;

import java.time.LocalDateTime;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Program;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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

		// Time Label
		Label defaultTimeLabel = new Label("Időpont:");
		GridPane.setConstraints(defaultTimeLabel, 0, 1);

		// Time Input
		HBox defaultTimeHBox = new HBox();
		defaultTimeHBox.setSpacing(10);
		defaultTimeHBox.setAlignment(Pos.CENTER_LEFT);

		// Spinner constants

		// Day
		Label dayLabel = new Label("Nap: ");
		Spinner<Integer> daySpinner = new Spinner<Integer>();
		daySpinner.setEditable(true);
		SpinnerValueFactory<Integer> daySpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1, 0);
		daySpinner.setValueFactory(daySpinnerFactory);
		daySpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				daySpinner.increment(0);
			}
		});

		// Hour
		Label hourLabel = new Label("Óra: ");
		Spinner<Integer> hourSpinner = new Spinner<Integer>();
		hourSpinner.setEditable(true);
		SpinnerValueFactory<Integer> hourSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
		hourSpinner.setValueFactory(hourSpinnerFactory);
		hourSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				hourSpinner.increment(0);
			}
		});

		// Minute
		Label minLabel = new Label("Perc: ");
		Spinner<Integer> minSpinner = new Spinner<Integer>();
		minSpinner.setEditable(true);
		SpinnerValueFactory<Integer> minuteSpinnerFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59,
				0);
		minSpinner.setValueFactory(minuteSpinnerFactory);
		minSpinner.focusedProperty().addListener((observable, oldValue, newValue) -> {
			if (!newValue) {
				minSpinner.increment(0);
			}
		});

		defaultTimeHBox.getChildren().addAll(dayLabel, daySpinner, hourLabel, hourSpinner, minLabel, minSpinner);
		GridPane.setConstraints(defaultTimeHBox, 1, 1);

		// IsDefault Label
		Label isDefaultLabel = new Label("Alapértelmezett:");
		GridPane.setConstraints(isDefaultLabel, 0, 2);

		// IsDefault Input
		CheckBox isDefaultBox = new CheckBox();
		GridPane.setConstraints(isDefaultBox, 1, 2);

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 3);
		saveButton.setOnAction(e -> {
			Program program = new Program();
			program.setName(nameInput.getText());
			LocalDateTime defaultTime = LocalDateTime.of(0, 1, daySpinner.getValue() + 1, hourSpinner.getValue(),
					minSpinner.getValue(), 0);
			program.setDefaultTime(defaultTime);
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
		grid.getChildren().addAll(nameLabel, nameInput, defaultTimeLabel, defaultTimeHBox, isDefaultLabel, isDefaultBox,
				saveButton, closeButton);

		Scene scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}

}
