package hu.mandisco.weddingscript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import hu.mandisco.weddingscript.view.edit.AttributeEditWindow;
import hu.mandisco.weddingscript.view.edit.ScriptEditWindow;
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

	public TableView<Script> getScriptList() {

		TableView<Script> scriptListTable = new TableView<>();
		ObservableList<Script> scripts = weddingScriptController.getScripts();

		scriptListTable.setEditable(true);

		scriptListTable.setRowFactory(tv -> {
			TableRow<Script> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent paramT) {
						scripts.addAll(weddingScriptController.getScripts());
						SortedList<Script> sortedData = new SortedList<>(scripts);
						sortedData.comparatorProperty().bind(scriptListTable.comparatorProperty());
						scriptListTable.setItems(sortedData);
					}
				});

				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Script selectedScript = row.getItem();
					ScriptEditWindow window = new ScriptEditWindow();
					window.display(stage, selectedScript);
				}
			});
			return row;
		});

		TableColumn<Script, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Script, String>("name"));

		TableColumn<Script, LocalDateTime> dateCol = new TableColumn<>("Dátum");
		dateCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("date"));
		dateCol.setCellFactory(column -> new TableCell<Script, LocalDateTime>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.format(DateTimeFormatter.ofPattern(Labels.DATEFORMAT_DATE)));
				}
			}
		});

		TableColumn<Script, String> commentCol = new TableColumn<>("Komment");
		commentCol.setCellValueFactory(new PropertyValueFactory<Script, String>("comment"));

		TableColumn<Script, LocalDateTime> lastEditedCol = new TableColumn<>("Utolsó módosítás");
		lastEditedCol
				.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("lastEdited"));
		lastEditedCol.setCellFactory(column -> new TableCell<Script, LocalDateTime>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.format(DateTimeFormatter.ofPattern(Labels.DATEFORMAT_DATETIME)));
				}
			}
		});

		TableColumn<Script, LocalDateTime> createdCol = new TableColumn<>("Létrehozva");
		createdCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("created"));
		createdCol.setCellFactory(column -> new TableCell<Script, LocalDateTime>() {

			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.format(DateTimeFormatter.ofPattern(Labels.DATEFORMAT_DATETIME)));
				}
			}
		});

		scriptListTable.getColumns().add(nameCol);
		scriptListTable.getColumns().add(dateCol);
		scriptListTable.getColumns().add(commentCol);
		scriptListTable.getColumns().add(lastEditedCol);
		scriptListTable.getColumns().add(createdCol);

		scriptListTable.getItems().addAll(scripts);

		return scriptListTable;
	}

	public TableView<Attribute> getAttributeList() {

		TableView<Attribute> attributeListTable = new TableView<>();

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

		TableColumn<Attribute, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> defValueCol = new TableColumn<>("Alapértelmezett érték");
		defValueCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		TableColumn<Attribute, Boolean> isMandatoryCol = new TableColumn<>("Kötelező");
		isMandatoryCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, Boolean>("mandatory"));
		isMandatoryCol.setCellFactory(tc -> new TableCell<Attribute, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item.booleanValue()) {
						setText("igen");
					} else {
						setText("nem");
					}
				}

			}
		});

		TableColumn<Attribute, String> attrTypeCol = new TableColumn<>("Attribútum típus");
		attrTypeCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("attrType"));

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(defValueCol);
		attributeListTable.getColumns().add(isMandatoryCol);
		attributeListTable.getColumns().add(attrTypeCol);

		List<Attribute> attributes = weddingScriptController.getAttributes();
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

	public TableView<Service> getServiceListNotInScript(Script script,
			TableView<Service> servicesTable) {

		TableView<Service> table = new TableView<>();
		ObservableList<Service> services = weddingScriptController.getServicesNotInScript(script);

		table.setEditable(true);

		TableColumn<Service, String> nameCol = new TableColumn<>("Szolgáltatás");
		nameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		table.getColumns().add(nameCol);

		table.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service rowData = row.getItem();
					services.remove(rowData);

					// Handle SortedList
					SortedList<Service> sortedData = new SortedList<>(services);
					sortedData.comparatorProperty().bind(table.comparatorProperty());
					table.setItems(sortedData);

					weddingScriptController.addServiceToScript(script, rowData);
					servicesTable.getItems().clear();
					servicesTable.getItems()
							.addAll(weddingScriptController.getServicesOfScript(script));

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

}
