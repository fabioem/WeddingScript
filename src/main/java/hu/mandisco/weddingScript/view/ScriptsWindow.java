package hu.mandisco.weddingScript.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Script;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ScriptsWindow extends Application {
	private WeddingScriptController weddingScriptController = new WeddingScriptController();
	private static TopMenu topMenu = new TopMenu();

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyvek");

		BorderPane layout = new BorderPane();

		// TOP
		layout.setTop(topMenu);

		// CENTER
		TableView<Script> table = new TableView<Script>();
		table.setEditable(true);

		TableColumn<Script, String> nameCol = new TableColumn<Script, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Script, String>("name"));

		TableColumn<Script, Date> dateCol = new TableColumn<Script, Date>("Dátum");
		dateCol.setCellValueFactory(new PropertyValueFactory<Script, Date>("date"));

		TableColumn<Script, String> commentCol = new TableColumn<Script, String>("Komment");
		commentCol.setCellValueFactory(new PropertyValueFactory<Script, String>("comment"));

		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
		TableColumn<Script, Date> lastEditedCol = new TableColumn<Script, Date>("Utolsó módosítás");
		lastEditedCol.setCellValueFactory(new PropertyValueFactory<Script, Date>("lastEdited"));
		lastEditedCol.setCellFactory(column -> {
			TableCell<Script, Date> cell = new TableCell<Script, Date>() {
				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(dateTimeFormat.format(item));
					}
				}
			};

			return cell;
		});

		TableColumn<Script, Date> createdCol = new TableColumn<Script, Date>("Létrehozva");
		createdCol.setCellValueFactory(new PropertyValueFactory<Script, Date>("created"));
		createdCol.setCellFactory(column -> {
			TableCell<Script, Date> cell = new TableCell<Script, Date>() {

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(dateTimeFormat.format(item));
					}
				}
			};

			return cell;
		});

		table.getColumns().addAll(nameCol, dateCol, commentCol, lastEditedCol, createdCol);

		List<Script> scripts = weddingScriptController.getScripts();
		table.getItems().addAll(scripts);

		layout.setCenter(table);

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}