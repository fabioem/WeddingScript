package hu.mandisco.weddingScript.view.edit;

import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AttributeEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private BorderPane layout = new BorderPane();

	public void display(Attribute attribute) {
		Stage window = new Stage();

		// Block events to other windows
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Attribútum szerkesztése");
		window.setMinWidth(250);

		// CENTER
		// CENTER - TOP
		BorderPane centerLayout = new BorderPane();
		GridPane topGrid = new GridPane();
		topGrid.setPadding(new Insets(10, 10, 10, 10));
		topGrid.setVgap(8);
		topGrid.setHgap(10);

		// Name - constrains use (child, column, row)
		Label nameLabel = new Label(attribute.getName());
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// value
		Label valueLabel = new Label(attribute.getValue());
		GridPane.setConstraints(valueLabel, 0, 1);
		GridPane.setHalignment(valueLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		topGrid.getChildren().addAll(nameLabel, valueLabel);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER
		TableView<Attribute> attributeTable = new TableView<Attribute>();
		attributeTable.setEditable(true);

		List<Attribute> attributes = weddingScriptController.getAttributes();
		attributeTable.getItems().addAll(attributes);

		VBox tablesBox = new VBox();
		tablesBox.getChildren().addAll(attributeTable);

		centerLayout.setCenter(tablesBox);
		layout.setCenter(centerLayout);

		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}

}
