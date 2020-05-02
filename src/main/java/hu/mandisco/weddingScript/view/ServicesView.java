package hu.mandisco.weddingscript.view;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Service;
import hu.mandisco.weddingscript.view.create.ServiceCreateWindow;
import hu.mandisco.weddingscript.view.edit.ServiceAttributesEditWindow;
import hu.mandisco.weddingscript.view.edit.ServiceEditWindow;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class ServicesView extends BorderPane {

	private ToolBar toolBar = new ToolBar();
	private VBox topMenu = new VBox();
	private TableView<Service> serviceTable;

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	public ServicesView() {
		super();

		serviceTable = new TableView<>();
		serviceTable.setEditable(true);
		ObservableList<Service> services = weddingScriptController.getServices();

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ServiceCreateWindow window = new ServiceCreateWindow();
			window.display(services);
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				services.remove(selectedItem);
				weddingScriptController.removeService(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ServiceEditWindow window = new ServiceEditWindow();
				window.display(services, selectedItem);
			}
		});

		Button attributesButton = new Button("Attribútumok");
		attributesButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ServiceAttributesEditWindow window = new ServiceAttributesEditWindow();
				window.display(selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton, attributesButton);
		topMenu.getChildren().add(toolBar);

		serviceTable.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service selectedService = row.getItem();
					ServiceEditWindow window = new ServiceEditWindow();
					window.display(services, selectedService);
				}
			});
			return row;
		});

		// Columns
		TableColumn<Service, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		serviceTable.getColumns().add(nameCol);
		serviceTable.getSortOrder().add(nameCol);

		serviceTable.setItems(services);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(serviceTable);
	}

}
