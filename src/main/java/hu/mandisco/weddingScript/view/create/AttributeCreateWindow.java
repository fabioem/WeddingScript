package hu.mandisco.weddingscript.view.create;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.AttributeType;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AttributeCreateWindow {

	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	private ObservableList<Attribute> attributeItems;
	private Boolean serviceAttrTypeSelected = false;

	public AttributeCreateWindow(ObservableList<Attribute> attributeItems) {
		this.attributeItems = attributeItems;
	}

	public void display() {
		Stage window = new Stage();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Új attribútum");
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

		// Default Value Label - constrains use (child, column, row)
		Label defValueLabel = new Label("Alapértelmezett érték:");
		GridPane.setConstraints(defValueLabel, 0, 1);

		// Default Value Input
		TextField defValueInput = new TextField();
		GridPane.setConstraints(defValueInput, 1, 1);
		defValueInput.setPromptText("Alapértelmezett érték");

		// Attribute Type Label
		Label attrTypeLabel = new Label("Attribútum típus:");
		GridPane.setConstraints(attrTypeLabel, 0, 2);

		// Attribute Type ComboBox
		ObservableList<AttributeType> attrTypeOptions = weddingScriptController.getAttributeTypes();
		ComboBox<AttributeType> attrTypeComboBox = new ComboBox<>(attrTypeOptions);
		attrTypeComboBox.setValue(attrTypeOptions.get(0));
		GridPane.setConstraints(attrTypeComboBox, 1, 2);

		// isDefault Label
		Label isMandatoryLabel = new Label("Kötelező");
		GridPane.setConstraints(isMandatoryLabel, 0, 3);

		// Default Value Input
		CheckBox isMandatoryInput = new CheckBox();
		GridPane.setConstraints(isMandatoryInput, 1, 3);

		// Service Label
		Label serviceLabel = new Label("Szolgáltatás:");
		GridPane.setConstraints(serviceLabel, 0, 4);
		serviceLabel.setDisable(true);

		// Service ComboBox
		ObservableList<Service> serviceOptions = weddingScriptController.getServices();
		ComboBox<Service> serviceComboBox = new ComboBox<>(serviceOptions);
		GridPane.setConstraints(serviceComboBox, 1, 4);
		serviceComboBox.setDisable(true);
		serviceComboBox.setValue(serviceOptions.get(0));

		// Event handling
		attrTypeComboBox.setOnAction((e) -> {
			serviceAttrTypeSelected = attrTypeComboBox.getValue().getName().equals("Service");
			serviceLabel.setDisable(!serviceAttrTypeSelected);
			serviceComboBox.setDisable(!serviceAttrTypeSelected);
		});

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 5);
		saveButton.setDefaultButton(true);
		saveButton.setOnAction(e -> {

			Attribute attribute = new Attribute();
			attribute.setName(nameInput.getText());
			attribute.setDefaultValue(defValueInput.getText());
			attribute.setAttrType(attrTypeComboBox.getValue());
			attribute.setMandatory(isMandatoryInput.isPressed());
			if (serviceAttrTypeSelected) {
				attribute.setServiceId(serviceComboBox.getValue().getServiceId());
			}

			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();
			} else {
				weddingScriptController.addAttribute(attribute);
				attributeItems.clear();
				attributeItems.addAll(weddingScriptController.getAttributes());
				window.close();
			}

		});

		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 5);

		//ESC button
		window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
	        if (KeyCode.ESCAPE == event.getCode()) {
	        	window.close();
	        }
	    });

		// Add everything to grid
		grid.getChildren().addAll(nameLabel, nameInput, defValueLabel, defValueInput, serviceLabel,
				serviceComboBox, isMandatoryLabel, isMandatoryInput, attrTypeLabel,
				attrTypeComboBox, saveButton, closeButton);

		Scene scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();

	}

}
