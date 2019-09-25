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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptEditWindow {

	// TODO show and select services in a new tab
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
        String nameString = script.getName();
        if (script.getDate() != null){
        	nameString.concat(" - " + script.getDate().format(DateTimeFormatter.ofPattern(weddingScriptController.DATEFORMAT_DATE)));
        }
        Label nameLabel = new Label(nameString);
        GridPane.setConstraints(nameLabel, 0, 0);
        GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

        // Comment
        Label commentLabel = new Label(script.getComment());
        GridPane.setConstraints(commentLabel, 0, 1);
        GridPane.setHalignment(commentLabel, HPos.CENTER);
        GridPane.setHgrow(commentLabel, Priority.ALWAYS);

        topGrid.getChildren().addAll(nameLabel, commentLabel);
        centerLayout.setTop(topGrid);
        // CENTER - CENTER
        // 1. Script attributes
        TableView<Attribute> scriptAttributesTable = tableList.getAttributeListOfScript(script);
        scriptAttributesTable.setEditable(true);
        centerLayout.setCenter(scriptAttributesTable);

        // 2. Program
        TableView<Program> scriptProgramsTable = new TableView<Program>();
        scriptProgramsTable.setEditable(true);
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

        scriptProgramsTable.getColumns().add(timeCol);
        scriptProgramsTable.getColumns().add(nameCol);

        List<Program> programs = weddingScriptController.getScriptPrograms(script);
        scriptProgramsTable.getItems().addAll(programs);

        // Center Grid
        GridPane centerGrid = new GridPane();

        // Script Attributes
        GridPane.setConstraints(scriptAttributesTable, 0, 0);
        TableView<Attribute> attributeAntiTable = tableList.getAttributeListNotInScript(script, scriptAttributesTable);
        GridPane.setConstraints(attributeAntiTable, 1, 0);

        // Script programs
        GridPane.setConstraints(scriptProgramsTable, 0, 1);
        TableView<Program> programAntiTable = tableList.getProgramListNotInScript(script, scriptProgramsTable);
        GridPane.setConstraints(programAntiTable, 1, 1);

        // Center grid adding
        centerGrid.getChildren().addAll(scriptAttributesTable, attributeAntiTable, scriptProgramsTable, programAntiTable);
        GridPane.setHgrow(scriptAttributesTable, Priority.ALWAYS);
        GridPane.setHgrow(attributeAntiTable, Priority.ALWAYS);
        GridPane.setHgrow(scriptProgramsTable, Priority.ALWAYS);
        GridPane.setHgrow(programAntiTable, Priority.ALWAYS);
        GridPane.setHgrow(centerGrid, Priority.ALWAYS);
        centerLayout.setCenter(centerGrid);

        // Main center adding
        layout.setCenter(centerLayout);

        // Stuff
        Scene scene = new Scene(layout, 700, 500);
        window.setScene(scene);
        window.showAndWait();
    }

}
