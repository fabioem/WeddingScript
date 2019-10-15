package hu.mandisco.weddingScript.view.edit;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.view.TableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProgramAttributesEditWindow {

	private static WeddingScriptController weddingScriptController = new WeddingScriptController();

	public void display(Stage window, Program program) {
		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Program attribútumok szerkesztése");
		window.setMinWidth(250);

		GridPane grid = new GridPane();
		grid.setPadding(new Insets(10, 10, 10, 10));
		grid.setVgap(8);
		grid.setHgap(10);

		int rowCount = 0;

		// Row 1 - name
		Label nameLabel = new Label(program.getName());
		GridPane.setConstraints(nameLabel, 0, rowCount++);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// Row 2 - table
		GridPane tableGrid = new GridPane();
		GridPane.setConstraints(tableGrid, 0, rowCount++);
		TableList tableList = new TableList();

		// Program Attributes
		TableView<Attribute> attributesTable = tableList.getAttributeListOfProgram(program);
		GridPane.setConstraints(attributesTable, 0, 0);
		GridPane.setHgrow(attributesTable, Priority.ALWAYS);

		TableView<Attribute> attributesAntiTable = tableList.getAttributeListNotInProgram(program, attributesTable);
		GridPane.setConstraints(attributesAntiTable, 1, 0);
		GridPane.setHgrow(attributesAntiTable, Priority.ALWAYS);

		tableGrid.getChildren().addAll(attributesTable, attributesAntiTable);


		// Row 3 - button
		Button okButton = new Button("OK");
		okButton.setOnAction(e -> window.close());
		GridPane.setConstraints(okButton, 0, rowCount++);
		GridPane.setHalignment(okButton, HPos.CENTER);
		GridPane.setHgrow(okButton, Priority.ALWAYS);


		grid.getChildren().addAll(nameLabel, tableGrid, okButton);

		// TODO

		Scene scene = new Scene(grid);
		window.setScene(scene);
		window.showAndWait();
	}

}
