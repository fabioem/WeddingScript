package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.view.create.ProgramCreateWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ProgramsView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private TableView<Program> programTable;

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public ProgramsView() {
		super();

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Program> programItems = programTable.getItems();
			ProgramCreateWindow window = new ProgramCreateWindow(programItems);
			window.display();
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Program selectedItem = programTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				programTable.getItems().remove(selectedItem);
				weddingScriptController.removeProgram(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Program selectedItem = programTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				weddingScriptController.editProgram(selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton);
		topMenu.getChildren().add(toolBar);

		TableList tableList = new TableList();
		programTable = tableList.getProgramList();

		this.setTop(topMenu);

		// CENTER
		this.setCenter(programTable);
	}

}
