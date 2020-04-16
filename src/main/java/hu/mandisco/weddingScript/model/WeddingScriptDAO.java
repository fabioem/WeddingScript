package hu.mandisco.weddingscript.model;

import java.time.LocalDateTime;
import java.util.List;

import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.AttributeType;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import javafx.collections.ObservableList;

public interface WeddingScriptDAO {

	public ObservableList<Program> getPrograms();

	public ObservableList<Script> getScripts();

	public ObservableList<Service> getServices();

	public ObservableList<AttributeType> getAttributeTypes();

	public ObservableList<Program> getProgramsOfScript(Script script);

	public List<Attribute> getAttributesOfProgram(Program program);

	public ObservableList<Program> getProgramsNotInScript(Script script);

	public boolean addScript(Script script);

	public boolean removeScript(Script script);

	public boolean addProgramToScript(Script script, Program program);

	public boolean removeProgram(Program program);

	public boolean addProgram(Program program);

	public boolean setProgram(Program program);

	public boolean removeAttribute(Attribute attribute);

	public boolean addAttribute(Attribute attribute);

	public List<Attribute> getAttributes();

	public ObservableList<Attribute> getAttributesOfScript(Script script);

	public ObservableList<Attribute> getAttributesNotInScript(Script script);

	public boolean addAttributeToScript(Script script, Attribute attribute);

	public List<Service> getServicesOfScript(Script script);

	public boolean addServiceToScript(Script script, Service rowData);

	public ObservableList<Service> getServicesNotInScript(Script script);

	public List<Attribute> getAttributesNotInProgram(Program program);

	public boolean addAttributeToProgram(Program program, Attribute attribute);

	public boolean setScriptAttributeValue(Script script, Attribute attribute, String newAttrValue);

	public boolean setScriptProgramTime(Script script, Program program, LocalDateTime newTime);

	public boolean removeAttributeFromProgram(Program program, Attribute attribute);

	public boolean setAttribute(Attribute attribute);

	public boolean setScript(Script script);

	public boolean removeAttributeFromScript(Script script, Attribute attribute);

	public ObservableList<Attribute> getAttributesOfScriptProgram(Script script, Program program);

	public ObservableList<Attribute> getAttributesNotInScriptProgram(Script script,
			Program program);

	public boolean removeAttributeFromScriptProgram(Script script, Program program,
			Attribute attribute);

	public boolean addAttributeToScriptProgram(Script script, Program program, Attribute attribute);

	public boolean setScriptProgramAttributeValue(Script script, Program program,
			Attribute attribute, String newAttributeValue);

	public boolean addService(Service service);

	public boolean setService(Service service);

	public boolean removeService(Service selectedItem);

	public List<Program> getDefaultPrograms();

	public boolean addAttributeToService(Service service, Attribute attribute);

	public boolean removeAttributeFromService(Service service, Attribute attribute);

	public List<Attribute> getAttributesNotInService(Service service);

	public List<Attribute> getAttributesOfService(Service service);

	public boolean setServiceAttributeValue(Service service, Attribute attribute,
			String newAttributeValue);

}
