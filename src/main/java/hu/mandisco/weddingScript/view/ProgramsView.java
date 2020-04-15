package hu.mandisco.weddingscript.view;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.view.create.ProgramCreateWindow;
import hu.mandisco.weddingscript.view.edit.ProgramAttributesEditWindow;
import hu.mandisco.weddingscript.view.edit.ProgramEditWindow;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
			Stage stage = new Stage();
			stage.setOnHiding(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent paramT) {
					ObservableList<Program> data = FXCollections.observableArrayList();
					data.addAll(weddingScriptController.getPrograms());
					SortedList<Program> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(programTable.comparatorProperty());
					programTable.setItems(sortedData);
				}
			});
			ProgramCreateWindow window = new ProgramCreateWindow();
			window.display(stage);

		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Program selectedItem = programTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ObservableList<Program> data = FXCollections.observableArrayList();
				SortedList<Program> sortedData = new SortedList<>(data);
				sortedData.comparatorProperty().bind(programTable.comparatorProperty());
				data.addAll(programTable.getItems());
				data.remove(selectedItem);
				programTable.setItems(sortedData);
				weddingScriptController.removeProgram(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {

			Stage stage = new Stage();
			stage.setOnHiding(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent paramT) {
					ObservableList<Program> data = FXCollections.observableArrayList();
					data.addAll(weddingScriptController.getPrograms());
					SortedList<Program> sortedData = new SortedList<>(data);
					sortedData.comparatorProperty().bind(programTable.comparatorProperty());
					programTable.setItems(sortedData);
				}
			});

			Program selectedItem = programTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ProgramEditWindow window = new ProgramEditWindow();
				window.display(stage, selectedItem);
			}
		});

		Button attributesButton = new Button("Attribútumok");
		attributesButton.setOnAction(e -> {

			Stage stage = new Stage();

			Program selectedItem = programTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ProgramAttributesEditWindow window = new ProgramAttributesEditWindow();
				window.display(stage, selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton, attributesButton);
		topMenu.getChildren().add(toolBar);

		TableList tableList = new TableList();
		programTable = tableList.getProgramList();

		this.setTop(topMenu);

		// CENTER
		this.setCenter(programTable);
	}

}
