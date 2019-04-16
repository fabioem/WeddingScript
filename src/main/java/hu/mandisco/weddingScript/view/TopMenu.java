package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.create.ScriptCreateWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class TopMenu extends VBox {
	private MenuBar mainMenu = new MenuBar();
	private ToolBar toolBar = new ToolBar();

	public TopMenu(BorderPane layout) {
		super();
		this.getChildren().add(mainMenu);
		this.getChildren().add(toolBar);

		// MENU
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
			ObservableList<Script> scriptItems = ((TableView<Script>)layout.getCenter()).getItems();
			ScriptCreateWindow.display(scriptItems);
		});
		// newButton.setText(Elusive.FILE_NEW.getCode() + "");
		//TODO: gombok eseménykezelése
		Button copyButton = new Button("Másolat készítése");
		Button deleteButton = new Button("Törlés");

		toolBar.getItems().addAll(newButton, copyButton, deleteButton);

	}

}
