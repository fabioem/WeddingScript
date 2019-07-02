package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.create.ScriptCreateWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ScriptsView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private TableView<Script> scriptTable;

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public ScriptsView() {
		super();

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Script> scriptItems = scriptTable.getItems();
			ScriptCreateWindow window = new ScriptCreateWindow(scriptItems);
			window.display();
		});
		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Script selectedItem = scriptTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				scriptTable.getItems().remove(selectedItem);
				weddingScriptController.removeScript(selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton);
		topMenu.getChildren().add(toolBar);

		TableList tableList = new TableList();
		scriptTable = tableList.getScriptList();

		this.setTop(topMenu);

		// CENTER
		this.setCenter(scriptTable);
	}

}
