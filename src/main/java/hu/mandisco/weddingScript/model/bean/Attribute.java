package hu.mandisco.weddingScript.model.bean;

public class Attribute {
	private int attrId;
	private String name;
	private int typeId;
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
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
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
