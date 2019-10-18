package hu.mandisco.weddingScript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;
import hu.mandisco.weddingScript.view.edit.AttributeEditWindow;
import hu.mandisco.weddingScript.view.edit.ProgramEditWindow;
import hu.mandisco.weddingScript.view.edit.ScriptEditWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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

		// Sort by time
		ObservableList<Program> data = FXCollections.observableArrayList();
		SortedList<Program> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		table.getSortOrder().add(defaultTimeCol);
		data.addAll(programs);

		return table;
	}

	public TableView<Attribute> getAttributeListOfProgram(Program program) {
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

		TableColumn<Attribute, String> attrTypeCol = new TableColumn<Attribute, String>("Attribútum típus");
		attrTypeCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("attrType"));

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(defValueCol);
		attributeListTable.getColumns().add(isMandatoryCol);
		attributeListTable.getColumns().add(attrTypeCol);

		List<Attribute> attributes = weddingScriptController.getAttributes();
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

	public TableView<Attribute> getAttributeListOfScript(Script script) {

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

		TableColumn<Attribute, String> valueCol = new TableColumn<Attribute, String>("Érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));
		valueCol.setCellFactory(TextFieldTableCell.<Attribute>forTableColumn());
		valueCol.setOnEditCommit(new EventHandler<CellEditEvent<Attribute, String>>() {
			@Override
			public void handle(CellEditEvent<Attribute, String> t) {
				((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
				int scriptId = script.getScriptId();
				int attributeId = t.getRowValue().getAttrId();
				String newAttrValue = t.getNewValue();
				weddingScriptController.editScriptAttributeValue(scriptId, attributeId, newAttrValue);
			}
		});

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(valueCol);

		List<Attribute> attributes = weddingScriptController.getAttributesOfScript(script);
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

	public TableView<Attribute> getAttributeListNotInScript(Script script, TableView<Attribute> attributeTable) {
		TableView<Attribute> table = new TableView<Attribute>();

		table.setEditable(true);

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueCol = new TableColumn<Attribute, String>("Alap érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		table.getColumns().add(nameCol);
		table.getColumns().add(valueCol);

		List<Attribute> attributes = weddingScriptController.getAttributesNotInScript(script);

		table.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute rowData = row.getItem();
					attributes.remove(rowData);

					// Handle SortedList
					ObservableList<Attribute> data = FXCollections.observableArrayList();
					SortedList<Attribute> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(table.comparatorProperty());
					table.setItems(sortedData);
					data.addAll(attributes);

					weddingScriptController.addAttributeToScript(script, rowData);
					attributeTable.getItems().clear();
					attributeTable.getItems().addAll(weddingScriptController.getScriptAttributes(script));

				}
			});
			return row;
		});

		table.getItems().addAll(attributes);

		// Sort by default time
		ObservableList<Attribute> data = FXCollections.observableArrayList();
		SortedList<Attribute> sortedData = new SortedList<>(data);
		// this ensures the sortedData is sorted according to the sort columns
		// in the table:
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		// programmatically set a sort column:
		table.getSortOrder().add(nameCol);
		// note that you should always manipulate the underlying list, not the
		// sortedList:
		data.addAll(attributes);

		return table;
	}

	public TableView<Attribute> getAttributeListOfAttributes(Attribute attribute) {
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

		TableColumn<Attribute, String> valueCol = new TableColumn<Attribute, String>("Érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("value"));

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(valueCol);

		List<Attribute> attributes = weddingScriptController.getAttributesOfAttribute(attribute);
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

	public TableView<Service> getServiceListNotInScript(Script script, TableView<Service> servicesTable) {
		TableView<Service> table = new TableView<Service>();

		table.setEditable(true);

		TableColumn<Service, String> nameCol = new TableColumn<Service, String>("Szolgáltatás");
		nameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		table.getColumns().add(nameCol);

		List<Service> services = weddingScriptController.getServicesNotInScript(script);

		table.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service rowData = row.getItem();
					services.remove(rowData);

					// Handle SortedList
					ObservableList<Service> data = FXCollections.observableArrayList();
					SortedList<Service> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(table.comparatorProperty());
					table.setItems(sortedData);
					data.addAll(services);

					weddingScriptController.addServiceToScript(script, rowData);
					servicesTable.getItems().clear();
					servicesTable.getItems().addAll(weddingScriptController.getServicesOfScript(script));

				}
			});
			return row;
		});

		table.getItems().addAll(services);

		ObservableList<Service> data = FXCollections.observableArrayList();
		SortedList<Service> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		data.addAll(services);

		return table;
	}

	public TableView<Attribute> getAttributeListNotInProgram(Program program, TableView<Attribute> attributeTable) {
		TableView<Attribute> table = new TableView<Attribute>();

		table.setEditable(true);

		TableColumn<Attribute, String> nameCol = new TableColumn<Attribute, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> valueCol = new TableColumn<Attribute, String>("Alap érték");
		valueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		table.getColumns().add(nameCol);
		table.getColumns().add(valueCol);

		List<Attribute> attributes = weddingScriptController.getAttributesNotInProgram(program);

		table.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute rowData = row.getItem();
					attributes.remove(rowData);

					// Handle SortedList
					ObservableList<Attribute> data = FXCollections.observableArrayList();
					SortedList<Attribute> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(table.comparatorProperty());
					table.setItems(sortedData);
					data.addAll(attributes);

					weddingScriptController.addAttributeToProgram(program, rowData);
					attributeTable.getItems().clear();
					attributeTable.getItems().addAll(weddingScriptController.getProgramAttributes(program));

				}
			});
			return row;
		});

		table.getItems().addAll(attributes);

		// Sort by default time
		ObservableList<Attribute> data = FXCollections.observableArrayList();
		SortedList<Attribute> sortedData = new SortedList<>(data);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
		table.getSortOrder().add(nameCol);
		data.addAll(attributes);

		return table;

	}

}
