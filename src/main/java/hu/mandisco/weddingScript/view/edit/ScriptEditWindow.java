package hu.mandisco.weddingScript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;
import hu.mandisco.weddingScript.view.TableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
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
		// TableView<Program> programsTable =
		// tableList.getProgramListOfScript(script);
		TableView<Program> programsTable = new TableView<Program>();

		programsTable.setEditable(true);

		TableColumn<Program, String> programNameCol = new TableColumn<Program, String>("Név");
		programNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programTimeCol = new TableColumn<Program, LocalDateTime>("Idő");
		programTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("time"));
		programTimeCol.setCellFactory(column -> {
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

		// TODO handle time editing
		// timeCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		programTimeCol.setOnEditCommit(new EventHandler<CellEditEvent<Program, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Program, LocalDateTime> t) {
				((Program) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTime(t.getNewValue());
				int scriptId = script.getScriptId();
				int programId = t.getRowValue().getProgId();
				LocalDateTime newTime = t.getNewValue();
				weddingScriptController.editScriptProgramTime(scriptId, programId, newTime);
			}
		});

		programsTable.getColumns().add(programNameCol);
		programsTable.getColumns().add(programTimeCol);

		// Test area START
		// Need this to be able to edit cells
		// DateTimeFormatter formatter =
		// DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_TIME);
		// TableColumn<Program, String> ldtCol = new TableColumn<Program,
		// String>("LDTime");
		// ldtCol.setCellValueFactory(foo -> new
		// SimpleStringProperty(foo.getValue().getTime().format(formatter)));
		// ldtCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		// ldtCol.setOnEditCommit(new EventHandler<CellEditEvent<Program,
		// String>>() {
		// @Override
		// public void handle(CellEditEvent<Program, String> t) {
		// ((Program)
		// t.getTableView().getItems().get(t.getTablePosition().getRow())).setTime(t.getNewValue());
		// int scriptId = script.getScriptId();
		// int programId = t.getRowValue().getProgId();
		// LocalDateTime newTime = t.getNewValue();
		// weddingScriptController.editScriptProgramTime(scriptId, programId,
		// newTime);
		// }
		// });
		//
		// table.getColumns().add(ldtCol);
		// Test area END

		List<Program> scriptPrograms = weddingScriptController.getScriptPrograms(script);
		programsTable.getItems().addAll(scriptPrograms);

		// TODO sorting doesn't work
		/*
		 * https://stackoverflow.com/questions/38045546/formatting-an-
		 * objectpropertylocaldatetime-in-a-tableview-column Alternatively, you
		 * can have a DateTimeFormatter to convert the LocalDateTime into a
		 * String, but in this case table sorting will not work (will use string
		 * ordering). Thanks @JFValdes to point that out.
		 */

		// Sort by time
		ObservableList<Program> programData = FXCollections.observableArrayList();
		SortedList<Program> sortedProgramData = new SortedList<>(programData);
		sortedProgramData.comparatorProperty().bind(programsTable.comparatorProperty());
		programsTable.setItems(sortedProgramData);
		programsTable.getSortOrder().add(programTimeCol);
		// table.getSortOrder().add(ldtCol);
		programData.addAll(scriptPrograms);

		programsTable.setEditable(true);

		// 3. Service
		TableView<Service> servicesTable = new TableView<Service>();
		servicesTable.setEditable(true);

		TableColumn<Service, String> scriptsNameCol = new TableColumn<Service, String>("Szolgáltatás");
		scriptsNameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		servicesTable.getColumns().add(scriptsNameCol);

		List<Service> services = weddingScriptController.getServicesOfScript(script);
		servicesTable.getItems().addAll(services);

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
		TableView<Program> programAntiTable = new TableView<Program>();

		programAntiTable.setEditable(true);

		TableColumn<Program, String> programAntiNameCol = new TableColumn<Program, String>("Név");
		programAntiNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programAntiDefaultTimeCol = new TableColumn<Program, LocalDateTime>(
				"Alapértelmezett időpont");
		programAntiDefaultTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		programAntiDefaultTimeCol.setCellFactory(column -> {
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

		programAntiTable.getColumns().add(programAntiNameCol);
		programAntiTable.getColumns().add(programAntiDefaultTimeCol);

		List<Program> antiPrograms = weddingScriptController.getProgramsNotInScript(script);

		programAntiTable.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program rowData = row.getItem();
					antiPrograms.remove(rowData);

					// Handle SortedList
					ObservableList<Program> programAntiData = FXCollections.observableArrayList();
					SortedList<Program> sortedProgramAntiData = new SortedList<>(programAntiData);
					sortedProgramAntiData.comparatorProperty().bind(programAntiTable.comparatorProperty());
					programAntiData.addAll(antiPrograms);
					programAntiTable.setItems(sortedProgramAntiData);

					weddingScriptController.addProgramToScript(script, rowData);
					programData.setAll(weddingScriptController.getScriptPrograms(script));

				}
			});
			return row;
		});

		programAntiTable.getItems().addAll(antiPrograms);

		// Sort by default time
		ObservableList<Program> data = FXCollections.observableArrayList();
		SortedList<Program> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(programAntiTable.comparatorProperty());
		programAntiTable.setItems(sortedData);
		programAntiTable.getSortOrder().add(programAntiDefaultTimeCol);
		data.addAll(antiPrograms);
		GridPane.setConstraints(programsTable, 0, 0);
		GridPane.setHgrow(programsTable, Priority.ALWAYS);
		GridPane.setConstraints(programAntiTable, 1, 0);
		GridPane.setHgrow(programAntiTable, Priority.ALWAYS);

		// Script services
		TableView<Service> servicesAntiTable = tableList.getServiceListNotInScript(script, servicesTable);
		GridPane.setConstraints(servicesTable, 0, 0);
		GridPane.setHgrow(servicesTable, Priority.ALWAYS);
		GridPane.setConstraints(servicesAntiTable, 1, 0);
		GridPane.setHgrow(servicesAntiTable, Priority.ALWAYS);

		// Center grid adding
		attributesCenterGrid.getChildren().addAll(attributesTable, attributeAntiTable);
		attributesTab.setContent(attributesCenterGrid);

		programsCenterGrid.getChildren().addAll(programsTable, programAntiTable);
		programsTab.setContent(programsCenterGrid);

		// TODO services tab
		servicesCenterGrid.getChildren().addAll(servicesTable, servicesAntiTable);
		servicesTab.setContent(servicesCenterGrid);

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
