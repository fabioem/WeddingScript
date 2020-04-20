package hu.mandisco.weddingscript.view;

import java.util.List;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.view.edit.AttributeEditWindow;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableList {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public TableView<Attribute> getAttributeList() {

		TableView<Attribute> attributeListTable = new TableView<>();

		attributeListTable.setEditable(true);

		attributeListTable.setRowFactory(tv -> {
			TableRow<Attribute> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Attribute selectedAttribute = row.getItem();
					AttributeEditWindow window = new AttributeEditWindow();
					window.display(selectedAttribute);
				}
			});
			return row;
		});

		TableColumn<Attribute, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("name"));

		TableColumn<Attribute, String> defValueCol = new TableColumn<>("Alapértelmezett érték");
		defValueCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, String>("defaultValue"));

		TableColumn<Attribute, Boolean> isMandatoryCol = new TableColumn<>("Kötelező");
		isMandatoryCol
				.setCellValueFactory(new PropertyValueFactory<Attribute, Boolean>("mandatory"));
		isMandatoryCol.setCellFactory(tc -> new TableCell<Attribute, Boolean>() {
			@Override
			protected void updateItem(Boolean item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item.booleanValue()) {
						setText("igen");
					} else {
						setText("nem");
					}
				}

			}
		});

		TableColumn<Attribute, String> attrTypeCol = new TableColumn<>("Attribútum típus");
		attrTypeCol.setCellValueFactory(new PropertyValueFactory<Attribute, String>("attrType"));

		attributeListTable.getColumns().add(nameCol);
		attributeListTable.getColumns().add(defValueCol);
		attributeListTable.getColumns().add(isMandatoryCol);
		attributeListTable.getColumns().add(attrTypeCol);

		List<Attribute> attributes = weddingScriptController.getAttributes();
		attributeListTable.getItems().addAll(attributes);

		return attributeListTable;
	}

}
