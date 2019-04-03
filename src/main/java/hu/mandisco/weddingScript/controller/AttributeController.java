package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Attribute;

public class AttributeController {

	Attribute model;

	public AttributeController(Attribute model) {
		super();
		this.model = model;
	}

	// AttrId
	public int getAttrId() {
		return model.getAttrId();
	}

	public void setAttrId(int attrId) {
		model.setAttrId(attrId);
	}

	// Name
	public String getName() {
		return model.getName();
	}

	public void setName(String name) {
		model.setName(name);
	}

	// TypeId
	public int getTypeId() {
		return model.getTypeId();
	}

	public void setTypeId(int typeId) {
		model.setTypeId(typeId);
	}

	// ServiceId
	public int getServiceId() {
		return model.getServiceId();
	}

	public void setServiceId(int serviceId) {
		model.setServiceId(serviceId);
	}

	// Mandatory
	public boolean isMandatory() {
		return model.isMandatory();
	}

	public void setMandatory(boolean mandatory) {
		model.setMandatory(mandatory);
	}

}
