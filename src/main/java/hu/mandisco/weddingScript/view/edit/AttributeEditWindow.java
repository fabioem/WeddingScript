package hu.mandisco.weddingScript.view.edit;

import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.view.TableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AttributeEditWindow {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private BorderPane layout = new BorderPane();

	public void display(Attribute mainAttribute) {
		Stage window = new Stage();

		TableList tableList = new TableList();

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

		// Name
		Label nameLabel = new Label(mainAttribute.getName());
		GridPane.setConstraints(nameLabel, 0, 0);
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		// value
		Label valueLabel = new Label(mainAttribute.getValue());
		GridPane.setConstraints(valueLabel, 0, 1);
		GridPane.setHalignment(valueLabel, HPos.CENTER);
		GridPane.setHgrow(nameLabel, Priority.ALWAYS);

		topGrid.getChildren().addAll(nameLabel, valueLabel);
		centerLayout.setTop(topGrid);
		// CENTER - CENTER
		TableView<Attribute> attributeTable = tableList.getAttributeListOfAttributes(mainAttribute);
		attributeTable.setEditable(true);

		List<Attribute> attributes = weddingScriptController.getAttributesOfAttribute(mainAttribute);
		attributeTable.getItems().addAll(attributes);

		centerLayout.setCenter(attributeTable);
		layout.setCenter(centerLayout);

		Scene scene = new Scene(layout, 700, 500);
		window.setScene(scene);
		window.showAndWait();
	}

}
