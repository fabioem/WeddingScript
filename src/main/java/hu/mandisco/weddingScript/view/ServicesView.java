package hu.mandisco.weddingScript.view;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Service;
import hu.mandisco.weddingScript.view.create.ServiceCreateWindow;
import hu.mandisco.weddingScript.view.edit.ServiceEditWindow;
import javafx.collections.FXCollections;
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

		// TOOLBAR
		Button newButton = new Button("Új");
		newButton.setOnAction(e -> {
			ObservableList<Service> serviceItems = serviceTable.getItems();
			ServiceCreateWindow window = new ServiceCreateWindow(serviceItems);
			window.display();
		});

		Button deleteButton = new Button("Törlés");
		deleteButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				serviceTable.getItems().remove(selectedItem);
				weddingScriptController.removeService(selectedItem);
			}
		});

		Button editButton = new Button("Szerkesztés");
		editButton.setOnAction(e -> {
			Service selectedItem = serviceTable.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				ServiceEditWindow window = new ServiceEditWindow();
				Stage stage = new Stage();
				window.display(stage, selectedItem);
			}
		});

		toolBar.getItems().addAll(newButton, deleteButton, editButton);
		topMenu.getChildren().add(toolBar);

		serviceTable = new TableView<Service>();
		serviceTable.setEditable(true);
		serviceTable.setRowFactory(tv -> {
			TableRow<Service> row = new TableRow<>();
			row.setOnMouseClicked(event -> {

				Stage stage = new Stage();
				stage.setOnHiding(new EventHandler<WindowEvent>() {

					@Override
					public void handle(WindowEvent paramT) {
						ObservableList<Service> data = FXCollections.observableArrayList();
						data.addAll(weddingScriptController.getServices());
						SortedList<Service> sortedData = new SortedList<>(data);
						sortedData.comparatorProperty().bind(serviceTable.comparatorProperty());
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

		TableColumn<Service, String> nameCol = new TableColumn<Service, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));

		serviceTable.getColumns().add(nameCol);

		ObservableList<Service> services = weddingScriptController.getServices();

		serviceTable.setItems(services);

		this.setTop(topMenu);

		// CENTER
		this.setCenter(serviceTable);
	}

}
