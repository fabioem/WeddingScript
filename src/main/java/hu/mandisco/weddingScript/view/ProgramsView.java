package hu.mandisco.weddingscript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.view.create.ProgramCreateWindow;
import hu.mandisco.weddingscript.view.edit.ProgramAttributesEditWindow;
import hu.mandisco.weddingscript.view.edit.ProgramEditWindow;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
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

		programTable = new TableView<>();
		programTable.setEditable(true);

		ObservableList<Program> programs = weddingScriptController.getPrograms();
		SortedList<Program> sortedData = new SortedList<>(programs);
		sortedData.comparatorProperty().bind(programTable.comparatorProperty());

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			Stage stage = new Stage();
			stage.setOnHiding(new EventHandler<WindowEvent>() {

				@Override
				public void handle(WindowEvent paramT) {
					programs.clear();
					programs.addAll(weddingScriptController.getPrograms());

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
				programs.remove(selectedItem);
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
					programs.clear();
					programs.addAll(weddingScriptController.getPrograms());

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

		programTable.setRowFactory(tv -> {
			TableRow<Program> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent paramT) {
						programs.addAll(weddingScriptController.getPrograms());

						programTable.setItems(sortedData);
					}
				});

				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Program selectedProgram = row.getItem();
					ProgramEditWindow window = new ProgramEditWindow();
					window.display(stage, selectedProgram);
				}
			});
			return row;
		});

		TableColumn<Program, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> defaultTimeCol = new TableColumn<>("Idő");
		defaultTimeCol.setCellValueFactory(
				new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		defaultTimeCol.setCellFactory(column ->

		new TableCell<Program, LocalDateTime>() {
			@Override
			protected void updateItem(LocalDateTime item, boolean empty) {
				super.updateItem(item, empty);
				if (item == null || empty) {
					setText(null);
				} else {
					setText(item.format(DateTimeFormatter.ofPattern(Labels.DATEFORMAT_TIME)));
				}
			}
		});

		programTable.getColumns().add(nameCol);
		programTable.getColumns().add(defaultTimeCol);

		// Sort by time
		programTable.setItems(sortedData);
		programTable.getSortOrder().add(defaultTimeCol);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(programTable);
	}

}
