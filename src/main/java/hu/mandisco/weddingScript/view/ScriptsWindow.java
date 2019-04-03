package hu.mandisco.weddingScript.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ScriptsWindow extends Application {

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
		TableView<Object> table = new TableView<Object>();
        table.setEditable(true);
		TableColumn<Object, Object>nameCol = new TableColumn<Object, Object>("Név");
        TableColumn<Object, Object> dateCol = new TableColumn<Object, Object>("Dátum");
        table.getColumns().addAll(nameCol, dateCol);

        layout.setCenter(table);

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}