package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.view.create.AttributeCreateWindow;
import hu.mandisco.weddingScript.view.edit.AttributeEditWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class AttributesView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private TableView<Attribute> attributeTable;

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public AttributesView() {
		super();

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Attribute> attributeItems = attributeTable.getItems();
			AttributeCreateWindow window = new AttributeCreateWindow(attributeItems);
			window.display();
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Attribute selectedItem = attributeTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				attributeTable.getItems().remove(selectedItem);
				weddingScriptController.removeAttribute(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Attribute selectedItem = attributeTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				AttributeEditWindow window = new AttributeEditWindow();
				window.display(selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton);
		topMenu.getChildren().add(toolBar);

		TableList tableList = new TableList();
		attributeTable = tableList.getAttributeList();

		this.setTop(topMenu);

		// CENTER
		this.setCenter(attributeTable);
	}

}
