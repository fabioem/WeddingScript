package hu.mandisco.weddingscript.view.create;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Service;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServiceCreateWindow {

	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	public void display(ObservableList<Service> serviceItems) {
		Stage window = new Stage();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Új szolgáltatás");
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

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 2);
		saveButton.setDefaultButton(true);
		saveButton.setOnAction(e -> {
			Service service = new Service(nameInput.getText());
			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();
			} else if (weddingScriptController.serviceExistsByName(nameInput.getText())) {
				Alert alert = new Alert(AlertType.ERROR, "Ilyen nevű szolgáltatás már van.", ButtonType.OK);
				alert.setHeaderText("Léteő szolgáltatás");
				alert.showAndWait();
			} else {
				weddingScriptController.addService(service);
				serviceItems.add(service);
				window.close();
			}

		});

		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 2);

		// ESC button
		window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				window.close();
			}
		});

		// Add everything to grid
		grid.getChildren().addAll(nameLabel, nameInput, saveButton, closeButton);

		Scene scene = new Scene(grid, 300, 200);
		window.setScene(scene);
		window.showAndWait();
	}
}
