/* org.agiso.tempel.core.validator.DefaultParamValidator (12-12-2013)
 * 
 * DefaultParamValidator.java
 * 
 * Copyright 2013 agiso.org
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
package org.agiso.tempel.core.validator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.agiso.tempel.api.ITemplateParamValidator;
import org.agiso.tempel.core.model.beans.BeanProperty;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class DefaultParamValidator implements ITemplateParamValidator<Object> {
	public static final Map<String, IConstraintChecker> checkers;

	static {
		checkers = new HashMap<String, IConstraintChecker>();
		checkers.put("notNull", new NotNullChecker());
		checkers.put("notBlank", new NotBlankChecker());
	}

	public Collection<BeanProperty> constraints;

//	--------------------------------------------------------------------------
	public DefaultParamValidator(Collection<BeanProperty> constraints) {
		this.constraints = constraints;
	}

//	--------------------------------------------------------------------------
	@Override
	public boolean validate(Object value) {
		if(constraints == null) {
			return false;
		}

		IConstraintChecker checker;
		for(BeanProperty constraint : constraints) {
			checker = checkers.get(constraint.getPropertyName());
			if(checker == null) {
				throw new RuntimeException("No checker found for constraint "
						+ "'" + constraint.getPropertyName() + "'"
				);
			}
			checker.check(value, constraint);
		}
		return true;
	}

//	--------------------------------------------------------------------------
	public interface IConstraintChecker {
		public void check(Object value, BeanProperty constraint);
	}
}

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class NotNullChecker implements DefaultParamValidator.IConstraintChecker {
	@Override
	public void check(Object value, BeanProperty constraint) {
		if(value == null) {
			throw new RuntimeException(constraint.getAttribute("message"));
		}
	}
}

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
class NotBlankChecker implements DefaultParamValidator.IConstraintChecker {
	@Override
	public void check(Object value, BeanProperty constraint) {
		if(value == null || value.toString().isEmpty()) {
			throw new RuntimeException(constraint.getAttribute("message"));
		}
	}
}