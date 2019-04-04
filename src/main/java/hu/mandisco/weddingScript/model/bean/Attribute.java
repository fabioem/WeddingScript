package hu.mandisco.weddingScript.model.bean;

public class Attribute {
	private int attrId;
	private String name;
	private int attrTypeId;
	private int serviceId;
	private boolean mandatory;

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

	public int getTypeId() {
		return attrTypeId;
	}

	public void setTypeId(int typeId) {
		this.attrTypeId = typeId;
	}

	public int getAttrTypeId() {
		return attrTypeId;
	}

	public void setAttrTypeId(int attrTypeId) {
		this.attrTypeId = attrTypeId;
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
