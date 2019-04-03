package hu.mandisco.weddingScript.controller;

import hu.mandisco.weddingScript.model.bean.AttributeType;
import hu.mandisco.weddingScript.view.AttributeTypeView;

public class AttributeTypeController {
	AttributeType model;
	AttributeTypeView view;

	public AttributeTypeController(AttributeType model, AttributeTypeView view) {
		super();
		this.model = model;
		this.view = view;
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
