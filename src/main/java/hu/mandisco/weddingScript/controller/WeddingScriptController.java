package hu.mandisco.weddingScript.controller;

import java.util.List;

import hu.mandisco.weddingScript.model.WeddingScriptDAO;
import hu.mandisco.weddingScript.model.WeddingScriptDAOSQLite;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;

public class WeddingScriptController {
	private WeddingScriptDAO dao = new WeddingScriptDAOSQLite();

	public List<Script> getScripts() {
		return dao.getScripts();
	}

	public List<Program> getPrograms() {
		return dao.getPrograms();
	}

	public List<Service> getServices() {
		return dao.getServices();
	}

	public List<AttributeType> getAttributeTypes() {
		return dao.getAttributeTypes();
	}

	public boolean addScript(Script script) {
		return dao.addScript(script);
	}

	public boolean removeScript(Script script) {
		return dao.removeScript(script);
	}

}
