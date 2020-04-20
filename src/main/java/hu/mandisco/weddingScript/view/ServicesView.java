package hu.mandisco.weddingscript.view;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Service;
import hu.mandisco.weddingscript.view.create.ServiceCreateWindow;
import hu.mandisco.weddingscript.view.edit.ServiceAttributesEditWindow;
import hu.mandisco.weddingscript.view.edit.ServiceEditWindow;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

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
		SortedList<Service> sortedData = new SortedList<>(services);
		sortedData.comparatorProperty().bind(serviceTable.comparatorProperty());

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ServiceCreateWindow window = new ServiceCreateWindow(services);
			window.display();
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				services.remove(selectedItem);
				serviceTable.setItems(sortedData);
				weddingScriptController.removeService(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ServiceEditWindow window = new ServiceEditWindow();
				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						services.clear();
						services.addAll(weddingScriptController.getServices());
						serviceTable.setItems(sortedData);
					}
				});
				window.display(stage, selectedItem);
			}
		});

		Button attributesButton = new Button("Attribútumok");
		attributesButton.setOnAction(e -> {
			Stage stage = new Stage();
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ServiceAttributesEditWindow window = new ServiceAttributesEditWindow();
				window.display(stage, selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton, attributesButton);
		topMenu.getChildren().add(toolBar);

		serviceTable.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {
				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {
					@Override
					public void handle(WindowEvent paramT) {
						services.clear();
						services.addAll(weddingScriptController.getServices());
						serviceTable.setItems(sortedData);
					}
				});

				if (event.getClickCount() == 2 && (!row.isEmpty())) {
					Service selectedService = row.getItem();
					ServiceEditWindow window = new ServiceEditWindow();
					window.display(stage, selectedService);
				}
			});
			return row;
		});

		TableColumn<Service, String> nameCol = new TableColumn<>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		serviceTable.getColumns().add(nameCol);

		serviceTable.setItems(services);
		serviceTable.getSortOrder().add(nameCol);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(serviceTable);
	}

}
