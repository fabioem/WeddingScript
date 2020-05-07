package hu.mandisco.weddingscript.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import hu.mandisco.weddingscript.model.WeddingScriptDAO;
import hu.mandisco.weddingscript.model.WeddingScriptDAOSQLite;
import hu.mandisco.weddingscript.model.bean.Attribute;
import hu.mandisco.weddingscript.model.bean.AttributeType;
import hu.mandisco.weddingscript.model.bean.Program;
import hu.mandisco.weddingscript.model.bean.Script;
import hu.mandisco.weddingscript.model.bean.Service;
import javafx.collections.ObservableList;

public class WeddingScriptController {
	private WeddingScriptDAO dao = new WeddingScriptDAOSQLite();

	public ObservableList<Script> getScripts() {
		return dao.getScripts();
	}

	public ObservableList<Program> getPrograms() {
		return dao.getPrograms();
	}

	public ObservableList<Service> getServices() {
		return dao.getServices();
	}

	public ObservableList<AttributeType> getAttributeTypes() {
		return dao.getAttributeTypes();
	}

	public boolean addScript(Script script) {
		boolean succeed = dao.addScript(script);
		List<Program> defaultPrograms = dao.getDefaultPrograms();
		for (Program program : defaultPrograms) {
			dao.addProgramToScript(script, program);
		}
		return succeed;
	}

	public List<Program> getDefaultPrograms() {
		return dao.getDefaultPrograms();
	}

	public boolean addProgramToScript(Script script, Program program) {
		program.setTime(program.getDefaultTime());
		updateScriptLastEdited(script);
		return dao.addProgramToScript(script, program);
	}

	public boolean removeScript(Script script) {
		return dao.removeScript(script);
	}

	public List<Attribute> getScriptAttributes(Script script) {
		return dao.getAttributesOfScript(script);
	}

	public ObservableList<Program> getScriptPrograms(Script script) {
		return dao.getProgramsOfScript(script);
	}

	public ObservableList<Program> getProgramsNotInScript(Script script) {
		return dao.getProgramsNotInScript(script);
	}

	public List<Attribute> getProgramAttributes(Program program) {
		return dao.getAttributesOfProgram(program);
	}

	public boolean removeProgram(Program program) {
		return dao.removeProgram(program);
	}

	public boolean addProgram(Program program) {
		return dao.addProgram(program);
	}

	public boolean setProgram(Program program) {
		return dao.setProgram(program);
	}

	public boolean removeAttribute(Attribute attribute) {
		return dao.removeAttribute(attribute);
	}

	public boolean addAttribute(Attribute attribute) {
		return dao.addAttribute(attribute);
	}

	public ObservableList<Attribute> getAttributes() {
		return dao.getAttributes();
	}

	public ObservableList<Attribute> getAttributesOfScript(Script script) {
		return dao.getAttributesOfScript(script);
	}

	public ObservableList<Attribute> getAttributesNotInScript(Script script) {
		return dao.getAttributesNotInScript(script);
	}

	public boolean addAttributeToScript(Script script, Attribute attribute) {
		updateScriptLastEdited(script);
		attribute.setValue(attribute.getDefaultValue());
		return dao.addAttributeToScript(script, attribute);
	}

	public ObservableList<Service> getServicesOfScript(Script script) {
		return dao.getServicesOfScript(script);
	}

	public boolean addServiceToScript(Script script, Service service) {
		updateScriptLastEdited(script);
		List<Attribute> attributesOfService = dao.getAttributesOfService(service);
		for (Attribute attribute : attributesOfService) {
			dao.addAttributeToScript(script, attribute);
		}
		return dao.addServiceToScript(script, service);
	}

	public ObservableList<Service> getServicesNotInScript(Script script) {
		return dao.getServicesNotInScript(script);
	}

	public List<Attribute> getAttributesNotInProgram(Program program) {
		return dao.getAttributesNotInProgram(program);
	}

	public boolean addAttributeToProgram(Program program, Attribute attribute) {
		attribute.setValue(attribute.getDefaultValue());
		return dao.addAttributeToProgram(program, attribute);
	}

	public boolean setScriptAttributeValue(Script script, Attribute attribute, String newAttrValue) {
		updateScriptLastEdited(script);
		return dao.setScriptAttributeValue(script, attribute, newAttrValue);
	}

	public boolean setScriptProgramTime(Script script, Program program, LocalDateTime newTime) {
		updateScriptLastEdited(script);
		return dao.setScriptProgramTime(script, program, newTime);
	}

	public boolean removeAttributeFromProgram(Program program, Attribute attribute) {
		return dao.removeAttributeFromProgram(program, attribute);
	}

	public boolean setAttribute(Attribute attribute) {
		return dao.setAttribute(attribute);
	}

	public boolean setScript(Script script) {
		updateScriptLastEdited(script);
		return dao.setScript(script);
	}

	public boolean removeAttributeFromScript(Script script, Attribute attribute) {
		updateScriptLastEdited(script);
		return dao.removeAttributeFromScript(script, attribute);
	}

	public ObservableList<Attribute> getScriptProgramAttributes(Script script, Program program) {
		return dao.getAttributesOfScriptProgram(script, program);
	}

	public ObservableList<Attribute> getScriptAttributesNotInProgram(Script script, Program program) {
		return dao.getAttributesNotInScriptProgram(script, program);
	}

	public boolean removeAttributeFromScriptProgram(Script script, Program program, Attribute attribute) {
		updateScriptLastEdited(script);
		return dao.removeAttributeFromScriptProgram(script, program, attribute);
	}

	public boolean addAttributeToScriptProgram(Script script, Program program, Attribute attribute) {
		updateScriptLastEdited(script);
		return dao.addAttributeToScriptProgram(script, program, attribute);
	}

	public boolean setScriptProgramAttributeValue(Script script, Program program, Attribute attribute,
			String newAttributeValue) {
		updateScriptLastEdited(script);
		return dao.setScriptProgramAttributeValue(script, program, attribute, newAttributeValue);
	}

	public boolean addService(Service service) {
		return dao.addService(service);
	}

	public boolean setService(Service service) {
		return dao.setService(service);
	}

	public boolean removeService(Service service) {
		return dao.removeService(service);
	}

	public boolean addAttributeToService(Service service, Attribute attribute) {
		return dao.addAttributeToService(service, attribute);
	}

	public boolean removeAttributeFromService(Service service, Attribute attribute) {
		return dao.removeAttributeFromService(service, attribute);
	}

	public List<Attribute> getAttributesNotInService(Service service) {
		return dao.getAttributesNotInService(service);
	}

	public List<Attribute> getAttributesOfService(Service service) {
		return dao.getAttributesOfService(service);
	}

	public boolean setServiceAttributeValue(Service service, Attribute attribute, String newAttributeValue) {
		return dao.setServiceAttributeValue(service, attribute, newAttributeValue);
	}

	public boolean setProgramAttributeValue(Program program, Attribute attribute, String newAttributeValue) {
		return dao.setProgramAttributeValue(program, attribute, newAttributeValue);
	}

	public boolean removeServiceFromScript(Script script, Service service) {
		updateScriptLastEdited(script);
		return dao.removeServiceFromScript(script, service);
	}

	public boolean updateScriptLastEdited(Script script) {
		script.updateLastEdited(LocalDateTime.now(ZoneId.of("Europe/Budapest")));
		return dao.updateScriptLastEdited(script);
	}
}
