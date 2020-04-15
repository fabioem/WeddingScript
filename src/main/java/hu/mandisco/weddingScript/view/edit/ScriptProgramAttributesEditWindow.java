package hu.mandisco.weddingscript.view.edit;

import java.util.List;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
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

public class ScriptProgramAttributesEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public void display(Stage window, Program program, Script script) {
		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Program attribútumok szerkesztése");
		window.setMinWidth(250);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		// Row 1 - name
		Label nameLabel = new Label(program.getName());
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// Row 2 - table
		GridPane tableGrid = new GridPane();
		GridPane.setConstraints(tableGrid, 0, 1);

		// Program Attributes
		TableView<Attribute> programAttributesTable = new TableView<>();

		programAttributesTable.setEditable(true);

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> attrCol = new TableColumn<Attribute, String>("Érték");
		attrCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));
		attrCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		attrCol.setOnEditCommit(new EventHandler<CellEditEvent<Attribute, String>>() {
			@Override
			public void handle(CellEditEvent<Attribute, String> t) {
				((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setValue(t.getNewValue());
				Attribute attribute = t.getRowValue();
				String newAttributeValue = t.getNewValue();
				weddingScriptController.setScriptProgramAttributeValue(script, program, attribute,
						newAttributeValue);
			}
		});

		programAttributesTable.getColumns().add(nameCol);
		programAttributesTable.getColumns().add(attrCol);

		ObservableList<Attribute> attributes = weddingScriptController
				.getScriptProgramAttributes(script, program);

		programAttributesTable.getItems().addAll(attributes);
		GridPane.setConstraints(programAttributesTable, 0, 0);
		GridPane.setHgrow(programAttributesTable, Priority.ALWAYS);

		TableView<Attribute> programAntiAttributesTable = new TableView<>();
		programAntiAttributesTable.setEditable(true);

		TableColumn<Attribute, String> programAttrAntiNameCol = new TableColumn<Attribute, String>(
				"Név");
		programAttrAntiNameCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> programAttrAntiValueCol = new TableColumn<Attribute, String>(
				"Alap érték");
		programAttrAntiValueCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		programAntiAttributesTable.getColumns().add(programAttrAntiNameCol);
		programAntiAttributesTable.getColumns().add(programAttrAntiValueCol);

		List<Attribute> antiAttributes = weddingScriptController
				.getScriptAttributesNotInProgram(script, program);

		programAntiAttributesTable.getItems().addAll(antiAttributes);

		// Sort by default time
		ObservableList<Attribute> programAttrAntiData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedProgramAttrAntiData = new SortedList<>(programAttrAntiData);
		sortedProgramAttrAntiData.comparatorProperty()
				.bind(programAntiAttributesTable.comparatorProperty());
		programAntiAttributesTable.setItems(sortedProgramAttrAntiData);
		programAntiAttributesTable.getSortOrder().add(programAttrAntiNameCol);
		programAttrAntiData.addAll(antiAttributes);

		GridPane.setConstraints(programAntiAttributesTable, 1, 0);
		GridPane.setHgrow(programAntiAttributesTable, Priority.ALWAYS);

		// Handling double clicks
		ObservableList<Attribute> programAttributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedProgramAttributeData = new SortedList<>(programAttributeData);
		sortedProgramAttributeData.comparatorProperty()
				.bind(programAttributesTable.comparatorProperty());

		ObservableList<Attribute> programAntiAttributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedProgramAntiAttributeData = new SortedList<>(
				programAntiAttributeData);
		sortedProgramAntiAttributeData.comparatorProperty()
				.bind(programAntiAttributesTable.comparatorProperty());

		programAttributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute attributeRowData = row.getItem();
					attributes.remove(attributeRowData);

					// Handle SortedList
					programAttributeData.setAll(attributes);
					programAttributesTable.setItems(sortedProgramAttributeData);

					weddingScriptController.removeAttributeFromScriptProgram(script, program,
							attributeRowData);
					antiAttributes.add(attributeRowData);
					programAntiAttributeData.setAll(antiAttributes);
					programAntiAttributesTable.setItems(sortedProgramAntiAttributeData);
				}
			});
			return row;
		});

		programAntiAttributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute antiAttributeRowData = row.getItem();
					antiAttributes.remove(antiAttributeRowData);

					// Handle SortedList
					programAntiAttributeData.setAll(antiAttributes);
					programAntiAttributesTable.setItems(sortedProgramAntiAttributeData);

					weddingScriptController.addAttributeToScriptProgram(script, program,
							antiAttributeRowData);
					attributes.add(antiAttributeRowData);
					programAttributeData.setAll(attributes);
					programAttributesTable.setItems(sortedProgramAttributeData);
				}
			});
			return row;
		});

		tableGrid.getChildren().addAll(programAttributesTable, programAntiAttributesTable);

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
