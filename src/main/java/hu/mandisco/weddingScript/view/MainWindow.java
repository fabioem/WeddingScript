package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.create.ScriptCreateWindow;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

	private BorderPane layout = new BorderPane();
	private TableView<Script> scriptTable;
	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyv készítő");

		TableList tableList = new TableList();
		scriptTable = tableList.getScriptList();


		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Script> scriptItems = scriptTable.getItems();
			ScriptCreateWindow window = new ScriptCreateWindow(scriptItems);
			window.display();
		});
		// TODO: gombok eseménykezelése
		Button copyButton = new Button("Másolat készítése");
		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Script selectedItem = scriptTable.getSelectionModel().getSelectedItem();
			scriptTable.getItems().remove(selectedItem);
			weddingScriptController.removeScript(selectedItem);
		});

		toolBar.getItems().addAll(newButton, copyButton, deleteButton);
		topMenu.getChildren().add(toolBar);

		layout.setTop(topMenu);

		// CENTER
		layout.setCenter(scriptTable);

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}