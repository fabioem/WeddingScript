package hu.mandisco.weddingScript.view;

import java.sql.Date;
import java.util.List;

import hu.mandisco.weddingScript.controller.ScriptController;
import hu.mandisco.weddingScript.model.WeddingScriptDAO;
import hu.mandisco.weddingScript.model.WeddingScriptDAOSQLite;
import hu.mandisco.weddingScript.model.bean.Script;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ScriptsWindow extends Application {
	ScriptController scriptController = new ScriptController();

	public static void main(String[] args) {
		launch(args);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyvek");

		BorderPane layout = new BorderPane();

		// TOP
		HBox horizontal = new HBox();
		horizontal.setPadding(new Insets(15, 12, 15, 12));
		horizontal.setSpacing(10);

		Button newButton = new Button("Új");
		Button deleteButton = new Button("Törlés");
		horizontal.getChildren().addAll(newButton, deleteButton);

		layout.setTop(horizontal);

		// CENTER
		TableView<Script> table = new TableView<Script>();
		table.setEditable(true);

		TableColumn<Script, String> nameCol = new TableColumn<Script, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Script, String>("name"));

		TableColumn<Script, Date> dateCol = new TableColumn<Script, Date>("Dátum");
		dateCol.setCellValueFactory(new PropertyValueFactory<Script, Date>("date"));
		table.getColumns().addAll(nameCol, dateCol);

		WeddingScriptDAO dao = new WeddingScriptDAOSQLite();
		List<Script> scripts = dao.getScripts();
		table.getItems().addAll(scripts);
		// table.getItems().add(new Script(5, "uj script", new Date(0)));

		layout.setCenter(table);

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}