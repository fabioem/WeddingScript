package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.create.ScriptCreateWindow;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow extends Application {

	private BorderPane layout = new BorderPane();
	private TableView<Script> scriptTable;
	private MenuBar mainMenu = new MenuBar();
	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyv készítő");

		TableList tableList = new TableList();
		scriptTable = tableList.getScriptList();

		// TOP MENU
		// Create and add the "File" sub-menu options.
		Menu file = new Menu("Fájl");
		MenuItem newItem = new MenuItem("Új");
		MenuItem openItem = new MenuItem("Megnyitás");
		MenuItem exitAppItem = new MenuItem("Kilépés");
		file.getItems().addAll(newItem, openItem, exitAppItem);

		// Create and add the "Edit" sub-menu options.
		Menu edit = new Menu("Szerkesztés");
		MenuItem propertiesItem = new MenuItem("Tulajdonságok");
		edit.getItems().add(propertiesItem);

		// Create and add the "Help" sub-menu options.
		Menu help = new Menu("Súgó");
		MenuItem visitWebsiteItem = new MenuItem("Weboldalunk");
		help.getItems().add(visitWebsiteItem);

		mainMenu.getMenus().addAll(file, edit, help);

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Script> scriptItems = scriptTable.getItems();
			ScriptCreateWindow window = new ScriptCreateWindow(scriptItems);
			window.display();
		});
		// newButton.setText(Elusive.FILE_NEW.getCode() + "");
		// TODO: gombok eseménykezelése
		Button copyButton = new Button("Másolat készítése");
		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Script selectedItem = scriptTable.getSelectionModel().getSelectedItem();
			scriptTable.getItems().remove(selectedItem);
			weddingScriptController.removeScript(selectedItem);
		});

		toolBar.getItems().addAll(newButton, copyButton, deleteButton);
		topMenu.getChildren().add(mainMenu);
		topMenu.getChildren().add(toolBar);

		layout.setTop(topMenu);

		// CENTER
		layout.setCenter(scriptTable);

		// RIGHT
		// layout.setRight(tableList.getProgramList());

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}