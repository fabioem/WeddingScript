package hu.mandisco.weddingScript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.edit.AttributeEditWindow;
import hu.mandisco.weddingScript.view.edit.ProgramEditWindow;
import hu.mandisco.weddingScript.view.edit.ScriptEditWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TableList {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public TableView<Program> getProgramList() {
		TableView<Program> table = new TableView<Program>();

		table.setEditable(true);

		table.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent paramT) {
						ObservableList<Program> data = FXCollections.observableArrayList();
						data.addAll(weddingScriptController.getPrograms());
						SortedList<Program> sortedData = new SortedList<>(data);
						sortedData.comparatorProperty().bind(table.comparatorProperty());
						table.setItems(sortedData);
					}
				});

				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program selectedProgram = row.getItem();
					ProgramEditWindow window = new ProgramEditWindow();
					window.display(stage, selectedProgram);
				}
			});
			return row;
		});

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> defaultTimeCol = new TableColumn<Program, LocalDateTime>("Idő");
		defaultTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		defaultTimeCol.setCellFactory(column -> {
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

		table.getColumns().add(nameCol);
		table.getColumns().add(defaultTimeCol);

		List<Program> programs = weddingScriptController.getPrograms();

		// TODO maybe next line is unnecessary
		// table.getItems().addAll(programs);

		// SORT BY TIME
		ObservableList<Program> data = FXCollections.observableArrayList();
		SortedList<Program> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		table.getSortOrder().add(defaultTimeCol);
		data.addAll(programs);

		return table;
	}

	public TableView<Program> getProgramListOfScript(Script script) {
		TableView<Program> table = new TableView<Program>();

		table.setEditable(true);

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> timeCol = new TableColumn<Program, LocalDateTime>("Idő");
		timeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("time"));
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

		TableColumn<Program, Attribute> attrCol = new TableColumn<Program, Attribute>("Attribútumok");
		attrCol.setCellValueFactory(new PropertyValueFactory<Program, Attribute>("attrId"));

		table.getColumns().add(nameCol);
		table.getColumns().add(timeCol);
		table.getColumns().add(attrCol);

		List<Program> programs = weddingScriptController.getScriptPrograms(script);
		table.getItems().addAll(programs);

		// TODO sorting doesn't work
		// SORT BY TIME
		ObservableList<Program> data = FXCollections.observableArrayList();
		SortedList<Program> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		table.getSortOrder().add(timeCol);
		data.addAll(programs);

		return table;
	}

	public TableView<Attribute> getAttributeListOfProgram(Program program) {
		// TODO: not used maybe?
		TableView<Attribute> table = new TableView<Attribute>();

		table.setEditable(true);

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> attrCol = new TableColumn<Attribute, String>("Érték");
		attrCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));

		table.getColumns().add(nameCol);
		table.getColumns().add(attrCol);

		List<Attribute> attributes = weddingScriptController.getProgramAttributes(program);
		table.getItems().addAll(attributes);

		return table;

	}

	public TableView<Program> getProgramListNotInScript(Script script, TableView<Program> programTable) {
		TableView<Program> table = new TableView<Program>();

		table.setEditable(true);

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> defaultTimeCol = new TableColumn<Program, LocalDateTime>("Idő");
		defaultTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		defaultTimeCol.setCellFactory(column -> {
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

		table.getColumns().add(nameCol);
		table.getColumns().add(defaultTimeCol);

		List<Program> programs = weddingScriptController.getScriptProgramsInverse(script);

		table.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program rowData = row.getItem();
					programs.remove(rowData);

					// Handle SortedList
					ObservableList<Program> data = FXCollections.observableArrayList();
					SortedList<Program> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(table.comparatorProperty());
					table.setItems(sortedData);
					data.addAll(programs);

					// table.getItems().clear();
					// table.getItems().addAll(programs);
					weddingScriptController.addProgramToScript(script, rowData);
					programTable.getItems().clear();
					programTable.getItems().addAll(weddingScriptController.getScriptPrograms(script));

				}
			});
			return row;
		});

		table.getItems().addAll(programs);

		// SORT BY DEFAULT TIME
		ObservableList<Program> data = FXCollections.observableArrayList();
		SortedList<Program> sortedData = new SortedList<>(data);
		// this ensures the sortedData is sorted according to the sort columns
		// in the table:
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		// programmatically set a sort column:
		table.getSortOrder().add(defaultTimeCol);
		// note that you should always manipulate the underlying list, not the
		// sortedList:
		data.addAll(programs);

		return table;
	}

	public TableView<Script> getScriptList() {

		TableView<Script> scriptListTable = new TableView<Script>();

		scriptListTable.setEditable(true);

		scriptListTable.setRowFactory(tv -> {
			TableRow<Script> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Script selectedScript = row.getItem();
					ScriptEditWindow window = new ScriptEditWindow();
					window.display(selectedScript);
				}
			});
			return row;
		});

		TableColumn<Script, String> nameCol = new TableColumn<Script, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Script, String>("name"));

		TableColumn<Script, LocalDateTime> dateCol = new TableColumn<Script, LocalDateTime>("Dátum");
		dateCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("date"));
		dateCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATE)));
					}
				}
			};

			return cell;
		});

		TableColumn<Script, String> commentCol = new TableColumn<Script, String>("Komment");
		commentCol.setCellValueFactory(new PropertyValueFactory<Script, String>("comment"));

		TableColumn<Script, LocalDateTime> lastEditedCol = new TableColumn<Script, LocalDateTime>("Utolsó módosítás");
		lastEditedCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("lastEdited"));
		lastEditedCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATETIME)));
					}
				}
			};

			return cell;
		});

		TableColumn<Script, LocalDateTime> createdCol = new TableColumn<Script, LocalDateTime>("Létrehozva");
		createdCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("created"));
		createdCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATETIME)));
					}
				}
			};

			return cell;
		});

		scriptListTable.getColumns().add(nameCol);
		scriptListTable.getColumns().add(dateCol);
		scriptListTable.getColumns().add(commentCol);
		scriptListTable.getColumns().add(lastEditedCol);
		scriptListTable.getColumns().add(createdCol);

		List<Script> scripts = weddingScriptController.getScripts();
		scriptListTable.getItems().addAll(scripts);

		return scriptListTable;
	}

	public TableView<Attribute> getAttributeList() {

		TableView<Attribute> attributeListTable = new TableView<Attribute>();

		attributeListTable.setEditable(true);

		attributeListTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute selectedAttribute = row.getItem();
					AttributeEditWindow window = new AttributeEditWindow();
					window.display(selectedAttribute);
				}
			});
			return row;
		});

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> defValueCol = new TableColumn<Attribute, String>("Alapértelmezett érték");
		defValueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		TableColumn<Attribute, Boolean> isMandatoryCol = new TableColumn<Attribute, Boolean>("Kötelező");
		isMandatoryCol.setCellValueFactory(new PropertyValueFactory<Attribute, Boolean>("mandatory"));
		isMandatoryCol.setCellFactory(tc -> new TableCell<Attribute, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? null : item.booleanValue() ? "igen" : "nem");
			}
		});

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(defValueCol);
		attributeListTable.getColumns().add(isMandatoryCol);

		List<Attribute> attributes = weddingScriptController.getAttributes();
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

}
