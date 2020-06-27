package hu.mandisco.weddingscript.view.pdf;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.UnitValue;

import hu.mandisco.weddingscript.controller.WeddingScriptController;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ScriptPdf {

	private static final Logger LOGGER = LogManager.getLogger();
	private static final WeddingScriptController weddingScriptController = new WeddingScriptController();

	public static void createPdf(Script script) throws IOException {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		String fileName = "results/"
				+ (script.getDate() == null ? "" : script.getDate().format(dateFormatter) + " ")
				+ script.getName() + ".pdf";

		// Initialize PDF writer
		PdfWriter writer = new PdfWriter(fileName);

		// Initialize PDF document
		PdfDocument pdfDoc = new PdfDocument(writer);

		// Initialize document
		Document document = new Document(pdfDoc);

		// Initialize needed variables
		Rectangle pageSize = pdfDoc.getDefaultPageSize();
		float width = pageSize.getWidth() - document.getLeftMargin() - document.getRightMargin();

		// Header
		addCenteredParagraph(document, width, "Név: " + script.getName() + " - " + script.getComment());
		if (script.getDate() != null) {
			addCenteredParagraph(document, width, "Dátum: " + script.getDate().format(dateFormatter));
		}

		// Services
		document.add(new Paragraph("Szolgáltatások:"));
		List<Service> scriptServices = weddingScriptController.getServicesOfScript(script);
		com.itextpdf.layout.element.List servicesList = new com.itextpdf.layout.element.List()
				.setSymbolIndent(12).setListSymbol("\u2022");

		for (Service service : scriptServices) {
			servicesList.add(new ListItem(service.getName()));
		}

		document.add(servicesList);

		// Script attributes
		List<Attribute> scriptAttributes = weddingScriptController.getScriptAttributes(script);
		for (Attribute attribute : scriptAttributes) {
			Paragraph p = new Paragraph(attribute.getName() + ": " + attribute.getValue());
			document.add(p);
		}

		// Script programs
		document.add(new Paragraph("Programok:"));
		List<Program> scriptPrograms = weddingScriptController.getScriptPrograms(script);
		Table table = new Table(UnitValue.createPercentArray(new float[] { 1, 6 })).useAllAvailableWidth();
		for (Program program : scriptPrograms) {
			table.addCell(new Cell().add(new Paragraph(program.getTime().format(timeFormatter))));
			table.addCell(new Cell().add(new Paragraph(program.getName())));
			List<Attribute> scriptProgramAttributes = weddingScriptController
					.getScriptProgramAttributes(script, program);
			for (Attribute attribute : scriptProgramAttributes) {
				table.addCell(new Cell().add(new Paragraph()));
				table.addCell(
						new Cell().add(new Paragraph(attribute.getName() + ": " + attribute.getValue())));
			}
		}
		document.add(table);

		// Close document
		document.close();

		Alert alert = new Alert(AlertType.INFORMATION, "A PDF sikeresen elkészült.", ButtonType.OK);
		alert.setHeaderText("A PDF elkészült");
		alert.showAndWait();
	}

	private static void addCenteredParagraph(Document document, float width, String text) {
		PdfFont timesNewRomanBold = null;
		try {
			timesNewRomanBold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
		} catch (IOException e) {
			LOGGER.error("Failed to create Times New Roman Bold font.");
			LOGGER.error(e);
		}

		List<TabStop> tabStops = new ArrayList<>();

		// Create a TabStop at the middle of the page
		tabStops.add(new TabStop(width / 2, TabAlignment.CENTER));

		// Create a TabStop at the end of the page
		tabStops.add(new TabStop(width, TabAlignment.LEFT));

		Paragraph p = new Paragraph().addTabStops(tabStops).setFontSize(14);
		if (timesNewRomanBold != null) {
			p.setFont(timesNewRomanBold);
		}
		p.add(new Tab()).add(text).add(new Tab());
		document.add(p);
	}
}
