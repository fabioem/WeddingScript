package hu.mandisco.weddingscript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import hu.mandisco.weddingscript.view.Labels;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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

		topGrid.getChildren().addAll(nameLabel, nameInput, commentLabel, commentInput, dateLabel, dateInput);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER

		/*
		 *
		 * ATTRIBUTES
		 *
		 */
		TableView<Attribute> attributesTable = new TableView<>();
		TableView<Attribute> antiAttributesTable = new TableView<>();

		// Handling double clicks
		ObservableList<Attribute> attributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedAttributeData = new SortedList<>(attributeData);
		sortedAttributeData.comparatorProperty().bind(attributesTable.comparatorProperty());

		ObservableList<Attribute> antiAttributeData = FXCollections.observableArrayList();
		SortedList<Attribute> sortedAntiAttributeData = new SortedList<>(antiAttributeData);
		sortedAntiAttributeData.comparatorProperty().bind(antiAttributesTable.comparatorProperty());

		ObservableList<Attribute> attributes = weddingScriptController.getAttributesOfScript(script);
		ObservableList<Attribute> antiAttributes = weddingScriptController.getAttributesNotInScript(script);

		attributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute selectedAttribute = row.getItem();
					selectedAttribute.setValue(selectedAttribute.getDefaultValue());
					attributes.remove(selectedAttribute);

					weddingScriptController.removeAttributeFromScript(script, selectedAttribute);
					attributeData.setAll(attributes);
					attributesTable.setItems(sortedAttributeData);

					antiAttributes.add(selectedAttribute);

					antiAttributeData.setAll(antiAttributes);
					antiAttributesTable.setItems(sortedAntiAttributeData);

				}
			});
			return row;
		});

		TableColumn<Attribute, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueCol = new TableColumn<>("Érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));
		valueCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		valueCol.setOnEditCommit(new EventHandler<CellEditEvent<Attribute, String>>() {
			@Override
			public void handle(CellEditEvent<Attribute, String> t) {
				((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setValue(t.getNewValue());
				Attribute attribute = t.getRowValue();
				String newAttrValue = t.getNewValue();
				weddingScriptController.setScriptAttributeValue(script, attribute, newAttrValue);
			}
		});

		attributesTable.getColumns().add(nameCol);
		attributesTable.getColumns().add(valueCol);

		/*
		 * ANTI ATTRIBUTES
		 */

		antiAttributesTable.setEditable(true);

		TableColumn<Attribute, String> nameAntiCol = new TableColumn<>("Név");
		nameAntiCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueAntiCol = new TableColumn<>("Alap érték");
		valueAntiCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		antiAttributesTable.getColumns().add(nameAntiCol);
		antiAttributesTable.getColumns().add(valueAntiCol);

		antiAttributesTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute rowData = row.getItem();
					antiAttributes.remove(rowData);

					antiAttributeData.setAll(antiAttributes);
					antiAttributesTable.setItems(sortedAntiAttributeData);

					weddingScriptController.addAttributeToScript(script, rowData);
					attributes.add(rowData);
					attributeData.setAll(attributes);
					attributesTable.setItems(sortedAttributeData);

				}
			});
			return row;
		});

		antiAttributesTable.getItems().addAll(antiAttributes);

		// Sort by default time
		SortedList<Attribute> sortedData = new SortedList<>(antiAttributes);
		sortedData.comparatorProperty().bind(antiAttributesTable.comparatorProperty());
		antiAttributesTable.setItems(sortedData);
		antiAttributesTable.getSortOrder().add(nameAntiCol);

		GridPane.setConstraints(attributesTable, 0, 0);
		GridPane.setHgrow(attributesTable, Priority.ALWAYS);
		GridPane.setConstraints(antiAttributesTable, 1, 0);
		GridPane.setHgrow(antiAttributesTable, Priority.ALWAYS);

		/*
		 *
		 * 2. PROGRAM
		 *
		 */

		TableView<Program> programsTable = new TableView<>();

		programsTable.setEditable(true);

		programsTable.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program rowData = row.getItem();

					Stage stage = new Stage();

					ScriptProgramAttributesEditWindow editWindow = new ScriptProgramAttributesEditWindow();
					editWindow.display(stage, rowData, script);

				}
			});
			return row;
		});

		TableColumn<Program, String> programNameCol = new TableColumn<>("Név");
		programNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programTimeCol = new TableColumn<>("Idő");
		programTimeCol.setEditable(true);
		programTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("time"));

		DateTimeFormatter parseFormatter = new DateTimeFormatterBuilder().appendPattern("D'. nap' HH:mm")
				.parseDefaulting(ChronoField.YEAR, 1970).toFormatter(Locale.ENGLISH);

		programTimeCol.setCellFactory(
				TextFieldTableCell.forTableColumn(new LocalDateTimeStringConverter(parseFormatter, null)));

		programTimeCol.setOnEditCommit(new EventHandler<CellEditEvent<Program, LocalDateTime>>() {
			@Override
			public void handle(CellEditEvent<Program, LocalDateTime> t) {
				((Program) t.getTableView().getItems().get(t.getTablePosition().getRow()))
						.setTime(t.getNewValue());
				Program program = t.getRowValue();
				LocalDateTime newTime = t.getNewValue();
				weddingScriptController.setScriptProgramTime(script, program, newTime);
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
		programData.addAll(scriptPrograms);

		programsTable.setEditable(true);

		/*
		 * ANTI PROGRAM
		 */
		TableView<Program> programAntiTable = new TableView<>();

		programAntiTable.setEditable(true);

		TableColumn<Program, String> programAntiNameCol = new TableColumn<>("Név");
		programAntiNameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> programAntiDefaultTimeCol = new TableColumn<>(
				"Alapértelmezett időpont");
		programAntiDefaultTimeCol
				.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		programAntiDefaultTimeCol.setCellFactory(column -> new TableCell<Program, LocalDateTime>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.format(DateTimeFormatter.ofPattern(Labels.DATEFORMAT_TIME)));
				}
			}
		}

		);

		programAntiTable.getColumns().add(programAntiNameCol);
		programAntiTable.getColumns().add(programAntiDefaultTimeCol);

		ObservableList<Program> antiPrograms = weddingScriptController.getProgramsNotInScript(script);

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

		/*
		 *
		 * 3. Services
		 *
		 */
		TableView<Service> servicesTable = new TableView<>();
		servicesTable.setEditable(true);

		TableColumn<Service, String> serviceNameCol = new TableColumn<>("Szolgáltatás");
		serviceNameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		servicesTable.getColumns().add(serviceNameCol);
		ObservableList<Service> services = weddingScriptController.getServicesOfScript(script);
		servicesTable.getItems().addAll(services);

		/*
		 * ANTI SERVICES
		 */
		TableView<Service> servicesAntiTable = new TableView<>();
		ObservableList<Service> antiServices = weddingScriptController.getServicesNotInScript(script);
		servicesAntiTable.setEditable(true);

		SortedList<Service> sortedServicesData = new SortedList<>(services);
		SortedList<Service> sortedAntiServicesData = new SortedList<>(antiServices);

		servicesTable.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service selectedService = row.getItem();
					services.remove(selectedService);

					// Handle SortedList
					sortedServicesData.comparatorProperty().bind(servicesTable.comparatorProperty());
					servicesTable.setItems(sortedServicesData);

					weddingScriptController.removeServiceFromScript(script, selectedService);
					antiServices.add(selectedService);
					servicesAntiTable.setItems(sortedAntiServicesData);

				}
			});
			return row;
		});

		TableColumn<Service, String> nameAntiServiceCol = new TableColumn<>("Szolgáltatás");
		nameAntiServiceCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		servicesAntiTable.getColumns().add(nameAntiServiceCol);

		servicesAntiTable.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service rowData = row.getItem();
					antiServices.remove(rowData);

					// Handle SortedList
					sortedAntiServicesData.comparatorProperty().bind(servicesAntiTable.comparatorProperty());
					servicesAntiTable.setItems(sortedAntiServicesData);

					weddingScriptController.addServiceToScript(script, rowData);
					services.add(rowData);
					servicesTable.setItems(sortedServicesData);

				}
			});
			return row;
		});

		servicesAntiTable.getItems().addAll(antiServices);

		ObservableList<Service> servicesAntiData = FXCollections.observableArrayList();
		SortedList<Service> sortedServicesAntiData = new SortedList<>(servicesAntiData);
		sortedServicesAntiData.comparatorProperty().bind(servicesAntiTable.comparatorProperty());
		servicesAntiTable.setItems(sortedServicesAntiData);
		servicesAntiData.addAll(antiServices);

		GridPane.setConstraints(servicesTable, 0, 0);
		GridPane.setHgrow(servicesTable, Priority.ALWAYS);
		GridPane.setConstraints(servicesAntiTable, 1, 0);
		GridPane.setHgrow(servicesAntiTable, Priority.ALWAYS);

		/*
		 * CENTER GRID
		 */

		GridPane attributesCenterGrid = new GridPane();
		GridPane programsCenterGrid = new GridPane();
		GridPane servicesCenterGrid = new GridPane();

		attributesCenterGrid.getChildren().addAll(attributesTable, antiAttributesTable);
		attributesTab.setContent(attributesCenterGrid);

		programsCenterGrid.getChildren().addAll(programsTable, programAntiTable);
		programsTab.setContent(programsCenterGrid);

		servicesCenterGrid.getChildren().addAll(servicesTable, servicesAntiTable);
		servicesTab.setContent(servicesCenterGrid);

		GridPane.setHgrow(attributesCenterGrid, Priority.ALWAYS);
		centerLayout.setCenter(tabPane);

		// Main center adding
		layout.setCenter(centerLayout);

		/*
		 * BUTTONS
		 */
		// Save
		Button saveButton = new Button("Mentés");
		GridPane.setConstraints(saveButton, 0, 0);
		saveButton.setDefaultButton(true);
		saveButton.setOnAction(e -> {
			if (nameInput.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR, "A név nem lehet üres!", ButtonType.OK);
				alert.setHeaderText("Üres név");
				alert.showAndWait();
			} else {
				script.setComment(commentInput.getText());
				script.setName(nameInput.getText());
				if (dateInput.getValue() != null) {
					script.setDate(dateInput.getValue().atStartOfDay());
				}

				weddingScriptController.setScript(script);
				window.close();
			}

		});

		// Close
		Button closeButton = new Button("Mégsem");
		closeButton.setOnAction(e -> window.close());
		GridPane.setConstraints(closeButton, 1, 0);

		// ESC button
		window.addEventHandler(KeyEvent.KEY_RELEASED, (KeyEvent event) -> {
			if (KeyCode.ESCAPE == event.getCode()) {
				window.close();
			}
		});

		/*
		 * BOTTOM
		 */

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
