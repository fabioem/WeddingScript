package hu.mandisco.weddingScript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

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
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;

public class ScriptEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private BorderPane layout = new BorderPane();

	public void display(Stage window, Script script) {
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

		// Name Label - constrains use (child, column, row)
		Label nameLabel = new Label("Név:");
		GridPane.setConstraints(nameLabel, 0, 0);

		// Name Input
		TextField nameInput = new TextField();
		GridPane.setConstraints(nameInput, 1, 0);
		nameInput.setText(script.getName());

		// Date Label
		Label dateLabel = new Label("Dátum:");
		GridPane.setConstraints(dateLabel, 0, 1);

		// Date Input
		DatePicker dateInput = new DatePicker();
		GridPane.setConstraints(dateInput, 1, 1);
		if (script.getDate() != null) {
			dateInput.setValue(script.getDate().toLocalDate());
		}

		// Comment Label
		Label commentLabel = new Label("Komment:");
		GridPane.setConstraints(commentLabel, 0, 2);

		// Comment Input
		TextField commentInput = new TextField();
		commentInput.setText(script.getComment());
		GridPane.setConstraints(commentInput, 1, 2);

		topGrid.getChildren().addAll(nameLabel, nameInput, commentLabel, commentInput, dateLabel,
				dateInput);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER

		/*
		 *
		 * ATTRIBUTES
		 *
		 */
		TableView<Attribute> attributesTable = new TableView<Attribute>();
		TableView<Attribute> attributeAntiTable = new TableView<Attribute>();

		attributesTable.setEditable(true);

		attributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute selectedAttribute = row.getItem();
					weddingScriptController.removeAttributeFromScript(script, selectedAttribute);
					attributesTable.getItems().clear();
					attributesTable.getItems()
							.addAll(weddingScriptController.getAttributesOfScript(script));

					// attributeAntiTable.getItems().add(selectedAttribute);
					attributeAntiTable.getItems().clear();
					attributeAntiTable.getItems()
							.addAll(weddingScriptController.getAttributesNotInScript(script));
					// TODO: anti table refresh
				}
			});
			return row;
		});

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueCol = new TableColumn<Attribute, String>("Érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));
		valueCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		valueCol.setOnEditCommit(new EventHandler<CellEditEvent<Attribute, String>>() {
			@Override
			public void handle(CellEditEvent<Attribute, String> t) {
				((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setValue(t.getNewValue());
				int scriptId = script.getScriptId();
				int attributeId = t.getRowValue().getAttrId();
				String newAttrValue = t.getNewValue();
				weddingScriptController.editScriptAttributeValue(scriptId, attributeId,
						newAttrValue);
			}
		});

		attributesTable.getColumns().add(nameCol);
		attributesTable.getColumns().add(valueCol);

		ObservableList<Attribute> attributes = weddingScriptController
				.getAttributesOfScript(script);
		attributesTable.getItems().addAll(attributes);
		attributesTable.setEditable(true);

		/*
		 * ANTI ATTRIBUTES
		 */

		attributeAntiTable.setEditable(true);

		TableColumn<Attribute, String> nameAntiCol = new TableColumn<Attribute, String>("Név");
		nameAntiCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueAntiCol = new TableColumn<Attribute, String>(
				"Alap érték");
		valueAntiCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		attributeAntiTable.getColumns().add(nameAntiCol);
		attributeAntiTable.getColumns().add(valueAntiCol);

		ObservableList<Attribute> antiAttributes = weddingScriptController
				.getAttributesNotInScript(script);

		attributeAntiTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute rowData = row.getItem();
					antiAttributes.remove(rowData);
					attributeAntiTable.setItems(antiAttributes);

					weddingScriptController.addAttributeToScript(script, rowData);
					attributesTable.getItems().clear();
					attributesTable.getItems()
							.addAll(weddingScriptController.getScriptAttributes(script));

				}
			});
			return row;
		});

		attributeAntiTable.getItems().addAll(antiAttributes);

		// Sort by default time
		ObservableList<Attribute> data = FXCollections.observableArrayList();
		SortedList<Attribute> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(attributeAntiTable.comparatorProperty());
		attributeAntiTable.setItems(sortedData);
		attributeAntiTable.getSortOrder().add(nameAntiCol);
		data.addAll(antiAttributes);

		GridPane.setConstraints(attributesTable, 0, 0);
		GridPane.setHgrow(attributesTable, Priority.ALWAYS);
		GridPane.setConstraints(attributeAntiTable, 1, 0);
		GridPane.setHgrow(attributeAntiTable, Priority.ALWAYS);

		/*
		 * 2. PROGRAM
		 */

		TableView<Program> programsTable = new TableView<Program>();

		programsTable.setEditable(true);

		TableColumn<Program, String> programNameCol = new TableColumn<Program, String>("Név");
		programNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programTimeCol = new TableColumn<Program, LocalDateTime>(
				"Idő");
		programTimeCol.setEditable(true);
		programTimeCol
				.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("time"));

		DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder()
				.appendPattern("D'. nap' HH:mm").parseDefaulting(ChronoField.YEAR, 1970)
				.toFormatter(Locale.ENGLISH);

		programTimeCol.setCellFactory(TextFieldTableCell
				.forTableColumn(new LocalDateTimeStringConverter(parseFormatter, null)));

		programTimeCol.setOnEditCommit(new EventHandler<CellEditEvent<Program, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Program, LocalDateTime> t) {
				((Program) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setTime(t.getNewValue());
				int scriptId = script.getScriptId();
				int programId = t.getRowValue().getProgId();
				LocalDateTime newTime = t.getNewValue();
				weddingScriptController.editScriptProgramTime(scriptId, programId, newTime);
			}
		});

		programsTable.getColumns().add(programNameCol);
		programsTable.getColumns().add(programTimeCol);

		List<Program> scriptPrograms = weddingScriptController.getScriptPrograms(script);
		programsTable.getItems().addAll(scriptPrograms);

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

		TableColumn<Service, String> scriptsNameCol = new TableColumn<Service, String>(
				"Szolgáltatás");
		scriptsNameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		servicesTable.getColumns().add(scriptsNameCol);

		List<Service> services = weddingScriptController.getServicesOfScript(script);
		servicesTable.getItems().addAll(services);

		// Center Grid
		GridPane attributesCenterGrid = new GridPane();
		GridPane programsCenterGrid = new GridPane();
		GridPane servicesCenterGrid = new GridPane();

		// Script programs
		TableView<Program> programAntiTable = new TableView<Program>();

		programAntiTable.setEditable(true);

		TableColumn<Program, String> programAntiNameCol = new TableColumn<Program, String>("Név");
		programAntiNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programAntiDefaultTimeCol = new TableColumn<Program, LocalDateTime>(
				"Alapértelmezett időpont");
		programAntiDefaultTimeCol.setCellValueFactory(
				new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		programAntiDefaultTimeCol.setCellFactory(column -> {
			TableCell<Program, LocalDateTime> cell = new TableCell<Program, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter
								.ofPattern(weddingScriptController.DATEFORMAT_TIME)));
					}
				}
			};

			return cell;
		});

		programAntiTable.getColumns().add(programAntiNameCol);
		programAntiTable.getColumns().add(programAntiDefaultTimeCol);

		ObservableList<Program> antiPrograms = weddingScriptController
				.getProgramsNotInScript(script);

		programAntiTable.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program rowData = row.getItem();
					antiPrograms.remove(rowData);

					// Handle SortedList
					ObservableList<Program> programAntiData = FXCollections.observableArrayList();
					SortedList<Program> sortedProgramAntiData = new SortedList<>(programAntiData);
					sortedProgramAntiData.comparatorProperty()
							.bind(programAntiTable.comparatorProperty());
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
		ObservableList<Program> antiData = FXCollections.observableArrayList();
		SortedList<Program> sortedAntiData = new SortedList<>(antiData);
		sortedAntiData.comparatorProperty().bind(programAntiTable.comparatorProperty());
		programAntiTable.setItems(sortedAntiData);
		programAntiTable.getSortOrder().add(programAntiDefaultTimeCol);
		antiData.addAll(antiPrograms);
		GridPane.setConstraints(programsTable, 0, 0);
		GridPane.setHgrow(programsTable, Priority.ALWAYS);
		GridPane.setConstraints(programAntiTable, 1, 0);
		GridPane.setHgrow(programAntiTable, Priority.ALWAYS);

		// Script services
		TableView<Service> servicesAntiTable = tableList.getServiceListNotInScript(script,
				servicesTable);
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

		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 0);
		saveButton.setOnAction(e -> {
			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();
			} else {
				script.setComment(commentInput.getText());
				script.setName(nameInput.getText());
				script.setDate(dateInput.getValue().atStartOfDay());

				weddingScriptController.setScript(script);
				window.close();
			}

		});

		// Close
		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 0);

		GridPane bottomGrid = new GridPane();
		bottomGrid.setPadding(new Insets(10, 10, 10, 10));
		bottomGrid.setVgap(8);
		bottomGrid.setHgap(10);
		bottomGrid.getChildren().addAll(saveButton, closeButton);

		layout.setBottom(bottomGrid);

		// Stuff
		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}

}
