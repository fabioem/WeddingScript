package hu.mandisco.weddingscript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.view.create.ScriptCreateWindow;
import hu.mandisco.weddingscript.view.edit.ScriptEditWindow;
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

public class ScriptsView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public ScriptsView() {
		super();

		TableView<Script> scriptTable = new TableView<>();
		ObservableList<Script> scripts = weddingScriptController.getScripts();
		scriptTable.setEditable(true);

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ScriptCreateWindow window = new ScriptCreateWindow();
			window.display(scripts);
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Script selectedItem = scriptTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				scripts.remove(selectedItem);
				weddingScriptController.removeScript(selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton);
		topMenu.getChildren().add(toolBar);

		scriptTable.setRowFactory(tv -> {
			TableRow<Script> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Script selectedScript = row.getItem();
					ScriptEditWindow window = new ScriptEditWindow();
					window.display(scripts, selectedScript);
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
		lastEditedCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("lastEdited"));
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

		scriptTable.getColumns().add(nameCol);
		scriptTable.getColumns().add(dateCol);
		scriptTable.getColumns().add(commentCol);
		scriptTable.getColumns().add(lastEditedCol);
		scriptTable.getColumns().add(createdCol);

		scriptTable.setItems(scripts);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(scriptTable);
	}

}
