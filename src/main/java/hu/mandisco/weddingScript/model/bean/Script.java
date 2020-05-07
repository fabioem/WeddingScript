package hu.mandisco.weddingscript.model.bean;

import java.time.LocalDateTime;

import javafx.collections.ObservableList;

public class Script {
	private int scriptId;
	private String name;
	private LocalDateTime date;
	private String comment;
	private LocalDateTime lastEdited;
	private LocalDateTime created;
	ObservableList<Attribute> attributeList;
	ObservableList<Program> programList;
	ObservableList<Service> serviceList;

	public Script() {
		super();
	}

	public Script(String name, LocalDateTime date, String comment) {
		super();
		this.name = name;
		this.date = date;
		this.comment = comment;
	}

	public ObservableList<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(ObservableList<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public ObservableList<Attribute> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(ObservableList<Attribute> attributeList) {
		this.attributeList = attributeList;
	}

	public ObservableList<Program> getProgramList() {
		return programList;
	}

	public void setProgramList(ObservableList<Program> programList) {
		this.programList = programList;
	}

	public ObservableList<Attribute> getAttributes() {
		return attributeList;
	}

	public void setAttributes(ObservableList<Attribute> attributes) {
		this.attributeList = attributes;
	}

	public int getScriptId() {
		return scriptId;
	}

	public void setScriptId(int scriptId) {
		this.scriptId = scriptId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public LocalDateTime getLastEdited() {
		return lastEdited;
	}

	public void updateLastEdited(LocalDateTime lastEdited) {
		this.lastEdited = lastEdited;
	}

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

}
