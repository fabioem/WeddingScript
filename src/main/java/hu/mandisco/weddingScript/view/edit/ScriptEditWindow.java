package hu.mandisco.weddingScript.view.edit;

import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.view.TableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ScriptEditWindow {

	private BorderPane layout = new BorderPane();

	public void display(Script script) {
		Stage window = new Stage();

		TableList tableList = new TableList();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Forgatókönyv szerkesztése");
		window.setMinWidth(250);

		// CENTER
		GridPane topGrid = new GridPane();
		topGrid.setPadding(new Insets(10, 10, 10, 10));
		topGrid.setVgap(8);
		topGrid.setHgap(10);

		// Name - constrains use (child, column, row)
		Label nameLabel = new Label(script.getName());
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);

		// Comment
		Label commentLabel = new Label(script.getComment());
		GridPane.setConstraints(commentLabel, 0, 1);
		GridPane.setHalignment(commentLabel, HPos.CENTER);

		topGrid.getChildren().addAll(nameLabel, commentLabel);
		layout.setTop(topGrid);

		// RIGHT
		layout.setRight(tableList.getProgramList());

		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}
}
