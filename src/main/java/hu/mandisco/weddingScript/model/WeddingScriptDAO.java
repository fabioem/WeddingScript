package hu.mandisco.weddingScript.model;

import java.util.List;

import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;

public interface WeddingScriptDAO {

	public List<Program> getPrograms();
	public List<Script> getScripts();
	public List<Service> getServices();
	public List<AttributeType> getAttributeTypes();

}
