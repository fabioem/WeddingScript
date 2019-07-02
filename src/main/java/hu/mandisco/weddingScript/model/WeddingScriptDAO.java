package hu.mandisco.weddingScript.model;

import java.util.List;

import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;

public interface WeddingScriptDAO {

	public final String DATEFORMAT_DATETIME = "yyyy.MM.dd HH:mm:ss";
	public final String DATEFORMAT_TIME = "HH:mm";
	public final String DATEFORMAT_DATE = "yyyy.MM.dd";

	public List<Program> getPrograms();

	public List<Script> getScripts();

	public List<Service> getServices();

	public List<AttributeType> getAttributeTypes();

	public List<Attribute> getScriptAttributes(Script script);

	public List<Program> getScriptPrograms(Script script);

	public List<Attribute> getProgramAttributes(Program program);

	public List<Program> getScriptProgramsInverse(Script script);

	public boolean addScript(Script script);

	public boolean removeScript(Script script);

	public boolean addProgramToScript(Script script, Program program);

}
