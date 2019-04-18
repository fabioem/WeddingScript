package hu.mandisco.weddingScript.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import hu.mandisco.weddingScript.controller.WeddingScriptController;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableList {

	private WeddingScriptController weddingScriptController = new WeddingScriptController();

	private static final String DATEFORMAT_DATETIME = "yyyy.MM.dd HH:mm:ss";
	private static final String DATEFORMAT_TIME = "HH:mm:ss";
	private static final String DATEFORMAT_DATE = "yyyy.MM.dd";

	public TableView<Program> getProgramList() {
		TableView<Program> table = new TableView<Program>();

		table.setEditable(true);

		TableColumn<Program, String> nameCol = new TableColumn<Program, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Program, String>("name"));

		TableColumn<Program, LocalDateTime> defaultTimeCol = new TableColumn<Program, LocalDateTime>("Idő");
		defaultTimeCol.setCellValueFactory(new PropertyValueFactory<Program, LocalDateTime>("defaultTime"));
		defaultTimeCol.setCellFactory(column -> {
			TableCell<Program, LocalDateTime> cell = new TableCell<Program, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(DATEFORMAT_TIME)));
					}
				}
			};

			return cell;
		});

		table.getColumns().addAll(nameCol, defaultTimeCol);

		List<Program> programs = weddingScriptController.getPrograms();
		table.getItems().addAll(programs);

		return table;
	}

	public TableView<Script> getScriptList() {

		TableView<Script> table = new TableView<Script>();

		table.setEditable(true);

		TableColumn<Script, String> nameCol = new TableColumn<Script, String>("Név");
		nameCol.setCellValueFactory(new PropertyValueFactory<Script, String>("name"));

		TableColumn<Script, LocalDateTime> dateCol = new TableColumn<Script, LocalDateTime>("Dátum");
		dateCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("date"));
		dateCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(DATEFORMAT_DATE)));
					}
				}
			};

			return cell;
		});

		TableColumn<Script, String> commentCol = new TableColumn<Script, String>("Komment");
		commentCol.setCellValueFactory(new PropertyValueFactory<Script, String>("comment"));

		TableColumn<Script, LocalDateTime> lastEditedCol = new TableColumn<Script, LocalDateTime>("Utolsó módosítás");
		lastEditedCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("lastEdited"));
		lastEditedCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {
				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME)));
					}
				}
			};

			return cell;
		});

		TableColumn<Script, LocalDateTime> createdCol = new TableColumn<Script, LocalDateTime>("Létrehozva");
		createdCol.setCellValueFactory(new PropertyValueFactory<Script, LocalDateTime>("created"));
		createdCol.setCellFactory(column -> {
			TableCell<Script, LocalDateTime> cell = new TableCell<Script, LocalDateTime>() {

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if (item == null || empty) {
						setText(null);
					} else {
						setText(item.format(DateTimeFormatter.ofPattern(DATEFORMAT_DATETIME)));
					}
				}
			};

			return cell;
		});

		table.getColumns().addAll(nameCol, dateCol, commentCol, lastEditedCol, createdCol);

		List<Script> scripts = weddingScriptController.getScripts();
		table.getItems().addAll(scripts);

		return table;
	}

}
