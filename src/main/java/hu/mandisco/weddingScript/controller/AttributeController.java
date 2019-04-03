package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.Attribute;
import hu.mandisco.weddingScript.view.AttributeView;

public class AttributeController {

	Attribute model;
	AttributeView view;

	public AttributeController(Attribute model, AttributeView view) {
		super();
		this.model = model;
		this.view = view;
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
