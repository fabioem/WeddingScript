package hu.mandisco.weddingScript.model.bean;

import java.util.List;

public class AttributeType {
	private static int attrTypeId;
	private static String name;
	private static List<AttributeType> attributeTypeList;

	public static List<AttributeType> getAttributeTypeList() {
		return attributeTypeList;
	}

	public static void setAttributeTypeList(List<AttributeType> attributeTypeList) {
		AttributeType.attributeTypeList = attributeTypeList;
	}

	public int getAttrTypeId() {
		return attrTypeId;
	}

	public void setAttrTypeId(int typeId) {
		AttributeType.attrTypeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		AttributeType.name = name;
	}
}
