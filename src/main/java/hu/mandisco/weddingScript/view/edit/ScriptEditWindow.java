package hu.mandisco.weddingScript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.TableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private BorderPane layout = new BorderPane();

	public void display(Script script) {
		Stage window = new Stage();

		TableList tableList = new TableList();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Forgatókönyv szerkesztése");
		window.setMinWidth(250);

		// CENTER
		// CENTER - TOP
		BorderPane centerLayout = new BorderPane();
		GridPane topGrid = new GridPane();
		topGrid.setPadding(new Insets(10, 10, 10, 10));
		topGrid.setVgap(8);
		topGrid.setHgap(10);

		// Name - constrains use (child, column, row)
		Label nameLabel = new Label(script.getName() + " - "
				+ script.getDate().format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATE)));
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// Comment
		Label commentLabel = new Label(script.getComment());
		GridPane.setConstraints(commentLabel, 0, 1);
		GridPane.setHalignment(commentLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		topGrid.getChildren().addAll(nameLabel, commentLabel);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER
		// 1. Script attributes
		TableView<Attribute> scriptTable = new TableView<Attribute>();
		scriptTable.setEditable(true);
		//TODO Script attributes

		// 2. Program
		TableView<Program> programTable = new TableView<Program>();
		programTable.setEditable(true);
		TableColumn<Program, LocalDateTime> timeCol = new TableColumn<Program, LocalDateTime>("Idő");
		timeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		timeCol.setCellFactory(column -> {
			TableCell<Program, LocalDateTime> cell = new TableCell<Program, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_TIME)));
					}
				}
			};

			return cell;
		});

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Program");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		programTable.getColumns().add(timeCol);
		programTable.getColumns().add(nameCol);

		List<Program> programs = weddingScriptController.getScriptPrograms(script);
		programTable.getItems().addAll(programs);

		// 2.1 Program attributes
		TableView<Attribute> attributeTable = new TableView<Attribute>();

		// 2.1.1 Program attributes' attributes

		VBox tablesBox = new VBox();
		tablesBox.getChildren().addAll(scriptTable, programTable, attributeTable);

		centerLayout.setCenter(tablesBox);
		layout.setCenter(centerLayout);

		// RIGHT
		layout.setRight(tableList.getProgramListNotInScript(script, programTable));

		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}
}
