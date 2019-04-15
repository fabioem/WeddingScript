package hu.mandisco.weddingScript.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {

	private BorderPane layout = new BorderPane();
	private TopMenu topMenu = new TopMenu();
	private ScriptList scriptList = new ScriptList();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyv készítő");

		// TOP
		layout.setTop(topMenu);

		// CENTER
		layout.setCenter(scriptList.getScriptList());

		// END
		Scene scene = new Scene(layout, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}