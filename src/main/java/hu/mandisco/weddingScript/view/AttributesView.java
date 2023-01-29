package hu.mandisco.weddingscript.view;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.view.create.AttributeCreateWindow;
import hu.mandisco.weddingscript.view.edit.AttributeEditWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AttributesView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public AttributesView() {
		super();

		ObservableList<Attribute> attributes = weddingScriptController.getAttributes();
		TableView<Attribute> attributeTable = new TableView<>();
		attributeTable.setEditable(true);

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			AttributeCreateWindow window = new AttributeCreateWindow();
			window.display(attributes);
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Attribute selectedItem = attributeTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				weddingScriptController.removeAttribute(selectedItem);
				attributes.remove(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Attribute selectedAttribute = attributeTable.getSelectionModel().getSelectedItem();
			if (selectedAttribute != null) {
				AttributeEditWindow window = new AttributeEditWindow();
				window.display(attributes, selectedAttribute);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton);
		topMenu.getChildren().add(toolBar);

		attributeTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute selectedAttribute = row.getItem();
					AttributeEditWindow window = new AttributeEditWindow();
					window.display(attributes, selectedAttribute);
				}
			});
			return row;
		});

		TableColumn<Attribute, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> defValueCol = new TableColumn<>("Alapértelmezett érték");
		defValueCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		TableColumn<Attribute, Boolean> isMandatoryCol = new TableColumn<>("Kötelező");
		isMandatoryCol.setCellValueFactory(new PropertyValueFactory<Attribute, Boolean>("mandatory"));
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
				} else {
					setText(null);
				}

			}
		});

		TableColumn<Attribute, String> attrTypeCol = new TableColumn<>("Attribútum típus");
		attrTypeCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("attrType"));

		attributeTable.getColumns().add(nameCol);
		attributeTable.getColumns().add(defValueCol);
		attributeTable.getColumns().add(isMandatoryCol);
		attributeTable.getColumns().add(attrTypeCol);

		attributeTable.setItems(attributes);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(attributeTable);
	}

}
