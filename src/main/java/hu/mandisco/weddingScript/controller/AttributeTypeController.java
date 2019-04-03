package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.AttributeType;

public class AttributeTypeController {
	AttributeType model;

	public AttributeTypeController(AttributeType model) {
		super();
		this.model = model;
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
		return model.getAttrTypeId();
	}

	public void setTypeId(int typeId) {
		model.setAttrTypeId(typeId);
	}
}
