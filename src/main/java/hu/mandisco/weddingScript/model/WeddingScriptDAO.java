package hu.mandisco.weddingScript.model;

import java.time.LocalDateTime;
import java.util.List;

import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.model.bean.Program;
import hu.mandisco.weddingScript.model.bean.Script;
import hu.mandisco.weddingScript.model.bean.Service;
import javafx.collections.ObservableList;

public interface WeddingScriptDAO {

	public final String DATEFORMAT_DATETIME = "yyyy.MM.dd HH:mm:ss";
	public final String DATEFORMAT_TIME = "HH:mm";
	public final String DATEFORMAT_DATE = "yyyy.MM.dd";

	public List<Program> getPrograms();

	public List<Script> getScripts();

	public ObservableList<Service> getServices();

	public ObservableList<AttributeType> getAttributeTypes();

	public List<Program> getProgramsOfScript(Script script);

	public List<Attribute> getAttributesOfProgram(Program program);

	public List<Program> getProgramsNotInScript(Script script);

	public boolean addScript(Script script);

	public boolean removeScript(Script script);

	public boolean addProgramToScript(Script script, Program program);

	public boolean removeProgram(Program program);

	public boolean addProgram(Program program);

	public boolean editProgram(Program program);

	public boolean removeAttribute(Attribute attribute);

	public boolean addAttribute(Attribute attribute);

	public List<Attribute> getAttributes();

	public List<Attribute> getAttributesOfAttribute(Attribute mainAttribute);

	public boolean addAttributeToAttribute(Attribute mainAttribute, Attribute subAttribute);

	public List<Attribute> getAttributesOfScript(Script script);

	public List<Attribute> getAttributesNotInScript(Script script);

	public boolean addAttributeToScript(Script script, Attribute attribute);

	public List<Service> getServicesOfScript(Script script);

	public boolean addServiceToScript(Script script, Service rowData);

	public List<Service> getServicesNotInScript(Script script);

	public List<Attribute> getAttributesNotInProgram(Program program);

	public boolean addAttributeToProgram(Program program, Attribute attribute);

	public boolean editScriptAttributeValue(int scriptId, int attributeId, String newAttrValue);

	public boolean editScriptProgramTime(int scriptId, int programId, LocalDateTime newTime);

}
