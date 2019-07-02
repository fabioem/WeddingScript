package hu.mandisco.weddingScript.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainWindow extends Application {

	private BorderPane scriptLayout = new ScriptsView();
	private BorderPane programLayout = new ProgramsView();

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Forgatókönyv kezelő");

		TabPane tabPane = new TabPane();

		Tab scriptsTab = new Tab("Forgatókönyvek");
		scriptsTab.setClosable(false);
		tabPane.getTabs().add(scriptsTab);

		Tab programsTab = new Tab("Programok");
		programsTab.setClosable(false);
		tabPane.getTabs().add(programsTab);

		Tab attributesTab = new Tab("Attribútumok");
		attributesTab.setClosable(false);
		tabPane.getTabs().add(attributesTab);

		scriptsTab.setContent(scriptLayout);
		programsTab.setContent(programLayout);

		// END
		Scene scene = new Scene(tabPane, 800, 600);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

}