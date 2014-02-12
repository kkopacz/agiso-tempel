/* org.agiso.tempel.type.JavaField (12-02-2014)
 * 
 * JavaField.java
 * 
 * Copyright 2014 agiso.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.agiso.tempel.type;

import org.agiso.core.lang.util.StringUtils;

/**
 * 
 * 
 * @author Michał Klin
 * @since 1.0
 */
public class JavaField {
	private String fieldName;
	private String fieldType;

	// FIXME: Do usunięcia. Nie występuje w stnadardowych POJO.
	@Deprecated private String fieldColumnName;

	@Deprecated private String fieldAccessName;

	@Deprecated private String getter;
	@Deprecated private String setter;

//	--------------------------------------------------------------------------
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		if(!StringUtils.isEmpty(fieldName)) {
			this.fieldAccessName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			setter = "set" + fieldAccessName;
		}
	}

	public String getFieldType() {
		return fieldType;
	}
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
		if(fieldType != null && fieldType.equals("boolean")) {
			getter = "is" + fieldAccessName;
		} else {
			getter = "get" + fieldAccessName;
		}
	}

	@Deprecated
	public String getFieldColumnName() {
		return fieldColumnName;
	}
	@Deprecated
	public void setFieldColumnName(String fieldColumnName) {
		this.fieldColumnName = fieldColumnName;
	}

	@Deprecated
	public String getFieldAccessName() {
		return fieldAccessName;
	}

	public String getGetter() {
		return getter;
	}
	public String getSetter() {
		return setter;
	}
}
