/* org.agiso.tempel.core.SystemInParamReader (13-02-2013)
 * 
 * SystemInParamReader.java
 * 
 * Copyright 2013 PPW 'ARAJ' Sp. z o. o.
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
package org.agiso.tempel.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.agiso.tempel.Temp;
import org.agiso.tempel.api.internal.IParamReader;
import org.springframework.stereotype.Component;

/**
 * 
 * 
 * @author <a href="mailto:kkopacz@araj.pl">Karol Kopacz</a>
 */
@Component
public class SystemInParamReader implements IParamReader {
	@Override
	public String getParamValue(String key, String name, String value) {
		if(Temp.StringUtils_isBlank(name)) {
			name = "Param '" + key + "'";
		}

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		try {
			if(value == null) {
				System.out.print(name + " []: ");
			} else {
				System.out.print(name + " [" + value + "]: ");
			}
			String line = br.readLine();
			if(!Temp.StringUtils_isEmpty(line)) {
				value = line;
			}
		} catch(IOException e) {
			throw new RuntimeException(e);
		}
		return value;
	}
}
