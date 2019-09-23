package hu.mandisco.weddingScript.model.bean;

import javafx.collections.ObservableList;

public class Service {
	private String name;
	private int serviceId;
	ObservableList<Program> programList;
	ObservableList<Attribute> attributeList;

	@Override
	public String toString() {
		return name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

}
