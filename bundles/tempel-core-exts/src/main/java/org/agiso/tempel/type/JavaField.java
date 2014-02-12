package org.agiso.tempel.type;

import org.agiso.tempel.Temp;

public class JavaField {
	private String fieldName;
	private String fieldAccessName;
	private String fieldType;
	private String fieldColumnName;
	private String getter;
	private String setter;

	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		if(!Temp.StringUtils_isEmpty(fieldName)) {
			this.fieldAccessName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			setter = "set" + fieldAccessName;
		}
	}

	public String getFieldAccessName() {
		return fieldAccessName;
	}

	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
		if(fieldType != null && fieldType.equals("boolean")){
			getter = "is" + fieldAccessName;
		} else {
			getter = "get" + fieldAccessName;
		}
	}

	public String getFieldColumnName() {
		return fieldColumnName;
	}
	public void setFieldColumnName(String fieldColumnName) {
		this.fieldColumnName = fieldColumnName;
	}

	public String getGetter() {
		return getter;
	}
	public String getSetter() {
		return setter;
	}
}
