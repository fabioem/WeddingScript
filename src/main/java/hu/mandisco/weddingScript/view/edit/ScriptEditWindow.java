package hu.mandisco.weddingScript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.TableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private BorderPane layout = new BorderPane();

	public void display(Script script) {
		Stage window = new Stage();

		TabPane tabPane = new TabPane();

		Tab attributesTab = new Tab("Forgatókönyv");
		attributesTab.setClosable(false);
		tabPane.getTabs().add(attributesTab);

		Tab programsTab = new Tab("Programok");
		programsTab.setClosable(false);
		tabPane.getTabs().add(programsTab);

		Tab servicesTab = new Tab("Szolgáltatások");
		servicesTab.setClosable(false);
		tabPane.getTabs().add(servicesTab);

		TableList tableList = new TableList();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Forgatókönyv szerkesztése");
		window.setMinWidth(250);

		// CENTER
		// CENTER - TOP
		BorderPane centerLayout = new BorderPane();
		GridPane topGrid = new GridPane();
		topGrid.setPadding(new Insets(10, 10, 10, 10));
		topGrid.setVgap(8);
		topGrid.setHgap(10);

		// Name - constrains use (child, column, row)
		String nameString = script.getName();
		if (script.getDate() != null) {
			nameString.concat(" - "
					+ script.getDate().format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATE)));
		}
		Label nameLabel = new Label(nameString);
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// Comment
		Label commentLabel = new Label(script.getComment());
		GridPane.setConstraints(commentLabel, 0, 1);
		GridPane.setHalignment(commentLabel, HPos.CENTER);
		GridPane.setHgrow(commentLabel, Priority.ALWAYS);

		topGrid.getChildren().addAll(nameLabel, commentLabel);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER
		// 1. Script attributes
		TableView<Attribute> attributesTable = tableList.getAttributeListOfScript(script);
		attributesTable.setEditable(true);

		// 2. Program
		TableView<Program> scriptProgramsTable = new TableView<Program>();
		scriptProgramsTable.setEditable(true);
		TableColumn<Program, LocalDateTime> timeCol = new TableColumn<Program, LocalDateTime>("Idő");
		timeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		timeCol.setCellFactory(column -> {
			TableCell<Program, LocalDateTime> cell = new TableCell<Program, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_TIME)));
					}
				}
			};

			return cell;
		});

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Program");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		scriptProgramsTable.getColumns().add(timeCol);
		scriptProgramsTable.getColumns().add(nameCol);

		List<Program> programs = weddingScriptController.getScriptPrograms(script);
		scriptProgramsTable.getItems().addAll(programs);

		// Center Grid
		GridPane attributesCenterGrid = new GridPane();
		GridPane programsCenterGrid = new GridPane();
		GridPane servicesCenterGrid = new GridPane();

		// Script Attributes
		TableView<Attribute> attributeAntiTable = tableList.getAttributeListNotInScript(script, attributesTable);
		GridPane.setConstraints(attributesTable, 0, 0);
		GridPane.setHgrow(attributesTable, Priority.ALWAYS);
		GridPane.setConstraints(attributeAntiTable, 1, 0);
		GridPane.setHgrow(attributeAntiTable, Priority.ALWAYS);

		// Script programs
		TableView<Program> programAntiTable = tableList.getProgramListNotInScript(script, scriptProgramsTable);
		GridPane.setConstraints(scriptProgramsTable, 0, 0);
		GridPane.setHgrow(scriptProgramsTable, Priority.ALWAYS);
		GridPane.setConstraints(programAntiTable, 1, 0);
		GridPane.setHgrow(programAntiTable, Priority.ALWAYS);

		// Center grid adding
		attributesCenterGrid.getChildren().addAll(attributesTable, attributeAntiTable);
		attributesTab.setContent(attributesCenterGrid);

		programsCenterGrid.getChildren().addAll(scriptProgramsTable, programAntiTable);
		programsTab.setContent(programsCenterGrid);

		/*
		 * TODO services tab
		 * servicesCenterGrid.getChildren().addAll();
		 * servicesTab.setContent();
		 */

		GridPane.setHgrow(attributesCenterGrid, Priority.ALWAYS);
		centerLayout.setCenter(tabPane);

		// Main center adding
		layout.setCenter(centerLayout);

		// Stuff
		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}

}
