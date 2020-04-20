package hu.mandisco.weddingscript.view.edit;

import java.util.List;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.Service;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ServiceAttributesEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public void display(Stage window, Service service) {
		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Szolgáltatás attribútumok szerkesztése");
		window.setMinWidth(250);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		// Row 1 - name
		Label nameLabel = new Label(service.getName());
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// Row 2 - table
		GridPane tableGrid = new GridPane();
		GridPane.setConstraints(tableGrid, 0, 1);

		// Service Attributes
		TableView<Attribute> serviceAttributesTable = new TableView<>();

		serviceAttributesTable.setEditable(true);

		TableColumn<Attribute, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> attrCol = new TableColumn<>("Érték");
		attrCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));
		attrCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		attrCol.setOnEditCommit(new EventHandler<CellEditEvent<Attribute, String>>() {
			@Override
			public void handle(CellEditEvent<Attribute, String> t) {
				((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setValue(t.getNewValue());
				Attribute attribute = t.getRowValue();
				String newAttributeValue = t.getNewValue();
				weddingScriptController.setServiceAttributeValue(service, attribute,
						newAttributeValue);
			}
		});

		serviceAttributesTable.getColumns().add(nameCol);
		serviceAttributesTable.getColumns().add(attrCol);

		List<Attribute> attributes = weddingScriptController.getAttributesOfService(service);

		serviceAttributesTable.getItems().addAll(attributes);
		GridPane.setConstraints(serviceAttributesTable, 0, 0);
		GridPane.setHgrow(serviceAttributesTable, Priority.ALWAYS);

		TableView<Attribute> serviceAntiAttributesTable = new TableView<>();
		serviceAntiAttributesTable.setEditable(true);

		TableColumn<Attribute, String> serviceAttrAntiNameCol = new TableColumn<>("Név");
		serviceAttrAntiNameCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> serviceAttrAntiValueCol = new TableColumn<>("Alap érték");
		serviceAttrAntiValueCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		serviceAntiAttributesTable.getColumns().add(serviceAttrAntiNameCol);
		serviceAntiAttributesTable.getColumns().add(serviceAttrAntiValueCol);

		List<Attribute> antiAttributes = weddingScriptController.getAttributesNotInService(service);

		serviceAntiAttributesTable.getItems().addAll(antiAttributes);

		// Sort by default time
		ObservableList<Attribute> serviceAttrAntiData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedServiceAttrAntiData = new SortedList<>(serviceAttrAntiData);
		sortedServiceAttrAntiData.comparatorProperty()
				.bind(serviceAntiAttributesTable.comparatorProperty());
		serviceAntiAttributesTable.setItems(sortedServiceAttrAntiData);
		serviceAntiAttributesTable.getSortOrder().add(serviceAttrAntiNameCol);
		serviceAttrAntiData.addAll(antiAttributes);

		GridPane.setConstraints(serviceAntiAttributesTable, 1, 0);
		GridPane.setHgrow(serviceAntiAttributesTable, Priority.ALWAYS);

		// Handling double clicks
		ObservableList<Attribute> serviceAttributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedServiceAttributeData = new SortedList<>(serviceAttributeData);
		sortedServiceAttributeData.comparatorProperty()
				.bind(serviceAttributesTable.comparatorProperty());

		ObservableList<Attribute> serviceAntiAttributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedServiceAntiAttributeData = new SortedList<>(
				serviceAntiAttributeData);
		sortedServiceAntiAttributeData.comparatorProperty()
				.bind(serviceAntiAttributesTable.comparatorProperty());

		serviceAttributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute attributeRowData = row.getItem();
					attributeRowData.setValue(attributeRowData.getDefaultValue());
					attributes.remove(attributeRowData);

					// Handle SortedList
					serviceAttributeData.setAll(attributes);
					serviceAttributesTable.setItems(sortedServiceAttributeData);

					weddingScriptController.removeAttributeFromService(service, attributeRowData);
					antiAttributes.add(attributeRowData);
					serviceAntiAttributeData.setAll(antiAttributes);
					serviceAntiAttributesTable.setItems(sortedServiceAntiAttributeData);
				}
			});
			return row;
		});

		serviceAntiAttributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute antiAttributeRowData = row.getItem();
					antiAttributes.remove(antiAttributeRowData);

					// Handle SortedList
					serviceAntiAttributeData.setAll(antiAttributes);
					serviceAntiAttributesTable.setItems(sortedServiceAntiAttributeData);

					weddingScriptController.addAttributeToService(service, antiAttributeRowData);
					attributes.add(antiAttributeRowData);
					serviceAttributeData.setAll(attributes);
					serviceAttributesTable.setItems(sortedServiceAttributeData);
				}
			});
			return row;
		});

		tableGrid.getChildren().addAll(serviceAttributesTable, serviceAntiAttributesTable);

		// Row 3 - button
		Button okButton = new Button("OK");
		okButton.setOnAction(e -> window.close());
		GridPane.setConstraints(okButton, 0, 2);
		GridPane.setHalignment(okButton, HPos.CENTER);
		GridPane.setHgrow(okButton, Priority.ALWAYS);

		grid.getChildren().addAll(nameLabel, tableGrid, okButton);

		// ESC button
		window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				window.close();
			}
		});

		Scene scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}

}
