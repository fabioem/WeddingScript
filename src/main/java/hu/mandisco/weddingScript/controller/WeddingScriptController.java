package hu.mandisco.weddingScript.controller;

import java.util.List;

import hu.mandisco.weddingScript.model.WeddingScriptDAO;
import hu.mandisco.weddingScript.model.WeddingScriptDAOSQLite;
import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;
import javafx.collections.ObservableList;

public class WeddingScriptController {
	private WeddingScriptDAO dao = new WeddingScriptDAOSQLite();

	public final String DATEFORMAT_DATETIME = "yyyy.MM.dd HH:mm:ss";
	public final String DATEFORMAT_TIME = "HH:mm";
	public final String DATEFORMAT_DATE = "yyyy.MM.dd";

	public List<Script> getScripts() {
		return dao.getScripts();
	}

	public List<Program> getPrograms() {
		return dao.getPrograms();
	}

	public List<Service> getServices() {
		return dao.getServices();
	}

	public ObservableList<AttributeType> getAttributeTypes() {
		return dao.getAttributeTypes();
	}

	public boolean addScript(Script script) {
		return dao.addScript(script);
	}

	public boolean addProgramToScript(Script script, Program program) {
		return dao.addProgramToScript(script, program);
	}

	public boolean removeScript(Script script) {
		return dao.removeScript(script);
	}

	public List<Attribute> getScriptAttributes(Script script) {
		return dao.getScriptAttributes(script);
	};

	public List<Program> getScriptPrograms(Script script) {
		return dao.getScriptPrograms(script);
	};

	public List<Program> getProgramsNotInScript(Script script) {
		return dao.getProgramsNotInScript(script);
	};

	public List<Attribute> getProgramAttributes(Program program) {
		return dao.getProgramAttributes(program);
	}

	public boolean removeProgram(Program program) {
		return dao.removeProgram(program);
	}

	public boolean addProgram(Program program) {
		return dao.addProgram(program);
	}

	public boolean editProgram(Program program) {
		return dao.editProgram(program);
	}

	public boolean removeAttribute(Attribute attribute) {
		return dao.removeAttribute(attribute);

	}

	public boolean addAttribute(Attribute attribute) {
		return dao.addAttribute(attribute);
	}

	public List<Attribute> getAttributes() {
		return dao.getAttributes();
	};

	public List<Attribute> getAttributesAttributes(Attribute mainAttribute){
		return dao.getAttributesAttributes(mainAttribute);
	}

	public boolean addAttributeToAttribute(Attribute mainAttribute, Attribute subAttribute) {
		return dao.addAttributeToAttribute(mainAttribute, subAttribute);
	}

	public List<Attribute> getAttributesOfScript(Script script) {
		return dao.getAttributesOfScript(script);
	}

	public List<Attribute> getAttributesNotInScript(Script script) {
		return dao.getAttributesNotInScript(script);
	}

	public boolean addAttributeToScript(Script script, Attribute attribute) {
		return dao.addAttributeToScript(script, attribute);
	}
}
