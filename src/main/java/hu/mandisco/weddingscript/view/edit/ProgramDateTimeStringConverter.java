package hu.mandisco.weddingscript.view.edit;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.Locale;

import hu.mandisco.weddingscript.view.Labels;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.util.converter.LocalDateTimeStringConverter;

public class ProgramDateTimeStringConverter extends LocalDateTimeStringConverter {

	private void showAlert(Exception e) {
		Alert alert = new Alert(AlertType.ERROR, "Az időpont hibás formátumú!", ButtonType.OK);
		alert.setHeaderText("Hibás időpont");
		alert.showAndWait();
	}

	@Override
	public String toString(LocalDateTime object) {
		if (object == null) {
			return "ERROR";
		}
		return object.format(new DateTimeFormatterBuilder().appendPattern(Labels.DATEFORMAT_TIME_FOR_PROGRAM)
				.parseDefaulting(ChronoField.YEAR, 1970).toFormatter(Locale.ENGLISH));
	}

	@Override
	public LocalDateTime fromString(String arg0) {
		try {
			DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
					.appendPattern(Labels.DATEFORMAT_TIME_FOR_PROGRAM).parseDefaulting(ChronoField.YEAR, 1970)
					.toFormatter(Locale.ENGLISH);
			return LocalDateTime.parse(arg0, dateTimeFormatter);
		} catch (DateTimeParseException e) {
			showAlert(e);
		}
		//return LocalDateTime.of(0, 1, 1, 0, 0, 0);
		return null;
	}

}
