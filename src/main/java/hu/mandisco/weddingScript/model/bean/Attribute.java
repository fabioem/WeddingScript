package hu.mandisco.weddingScript.model.bean;

import javafx.collections.ObservableList;

public class Attribute {
	private int attrId;
	private String name;
	private String value;
	private String defaultValue;
	private AttributeType attrType;
	ObservableList<Attribute> attributes;
	private int serviceId;
	private boolean mandatory;

	public Attribute() {
		super();
	}

	public ObservableList<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(ObservableList<Attribute> attributes) {
		this.attributes = attributes;
	}

	public AttributeType getAttrType() {
		return attrType;
	}

	public void setAttrType(AttributeType attrType) {
		this.attrType = attrType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public int getAttrId() {
		return attrId;
	}

	public void setAttrId(int attrId) {
		this.attrId = attrId;
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

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

}
