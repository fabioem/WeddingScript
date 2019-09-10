package hu.mandisco.weddingScript.model.bean;

public class AttributeType {
	private int attrTypeId;
	private String name;

	@Override
	public String toString() {
		return name;
	}

	public int getAttrTypeId() {
		return attrTypeId;
	}

	public void setAttrTypeId(int tId) {
		attrTypeId = tId;
	}

	public String getName() {
		return name;
	}

	public void setName(String nam) {
		name = nam;
	}
}
