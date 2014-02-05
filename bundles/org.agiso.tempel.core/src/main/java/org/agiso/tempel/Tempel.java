/* org.agiso.tempel.Tempel (14-09-2012)
 * 
 * Tempel.java
 * 
 * Copyright 2012 agiso.org
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
package org.agiso.tempel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
import org.agiso.tempel.api.internal.IExpressionEvaluator;
import org.agiso.tempel.api.internal.IParamReader;
import org.agiso.tempel.api.internal.ITemplateExecutor;
import org.agiso.tempel.api.internal.ITemplateProvider;
import org.apache.commons.lang.LocaleUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
public class Tempel implements ITempel {
	private static final Logger logger = LogUtils.getLogger(Tempel.class);

	private final Map<String, Object> systemProperties;

//	--------------------------------------------------------------------------
	private ITemplateProvider templateProvider;
	private ITemplateExecutor templateExecutor;

	@Autowired
	private IExpressionEvaluator expressionEvaluator;

//	--------------------------------------------------------------------------
	public Tempel() {
		// Odczytujemy właściwości systemowe i zapamiętujemy je w mapie systemProperties:
		Properties properties = System.getProperties();
		Map<String, Object> map = new HashMap<String, Object>(properties.size());
		for(String key : properties.stringPropertyNames()) {
			map.put(key.replace('.', '_'), properties.getProperty(key));
		}
		systemProperties = Collections.unmodifiableMap(map);
	}

//	--------------------------------------------------------------------------
	@Autowired
	public void setTemplateProvider(ITemplateProvider templateProvider) {
		this.templateProvider = templateProvider;
	}

	@Autowired
	public void setTemplateExecutor(ITemplateExecutor templateExecutor) {
		this.templateExecutor = templateExecutor;
	}

//	--------------------------------------------------------------------------
	@Override
	public void setParamReader(IParamReader paramReader) {
		templateExecutor.setParamReader(paramReader);
	}

	/**
	 * 
	 */
	@Override
	public void startTemplate(String name, Map<String, String> params, String workDir) throws Exception {
		Map<String, Object> properties = new TreeMap<String, Object>();

		// Wypełniamy mapę properties dodając do niej parametry poszczególnych
		// poziomów tak, że parametry wyższego poziomu przykrywają parametry
		// niższego poziomu (przedefiniowywanie parametrów):

		// Parametry systemowe wywołania maszyny wirtualnej Java (1):
		properties.putAll(systemProperties);
		properties.put("SYSTEM", systemProperties);

		// Parametry ustawień globalnych (2), użytkownika (3) i lokalnych (4):
		templateProvider.initialize(properties);

		// Parametry uruchomieniowe (5):
		properties.putAll(params);

		// W oparciu o parametry użytkownika budujemy parametry uruchomienia:
		properties.putAll(addRuntimeProperties(properties));

		// Po zbudowaniu mapy properties przeglądamy ją i rozwijamy parametry:
		for(String key : properties.keySet()) {
			Object value = properties.get(key);
			if(value instanceof String) {
				String oldValue = (String)value;
				String newValue = expressionEvaluator.evaluate(oldValue, properties);
				if(!oldValue.equals(newValue)) {
					logger.debug("Property {}: {} <-- {}",
							key, oldValue, newValue
					);
					properties.put(key, newValue);
				}
			}
		}

		properties = Collections.unmodifiableMap(properties);

		// Po rozwinięciu parametrów inicjalizujemy provider'a szablonów:
		templateProvider.configure(properties);

		// Uruchamianie procesu generacji w oparciu o wskazany szablon:
		templateExecutor.executeTemplate(name, properties, workDir);
	}

//	--------------------------------------------------------------------------
//	Obsługa parametrów szablonów.
//	RP_ - Parametry linii poleceń wykonania programu. W oparciu o ich wartości
//	      wyznaczane są parametry TP_ wykorzystywane w szablonach.
//	UP_ - Parametry konfiguracyjne (użytkownika). Określają sposób wyznaczania
//	      parametrów TP_ wykorzystywanych w szablonach.
//	TP_ - Parametry szablonów. Przeznaczone do bezpośredniego uzycia w treści
//	      szablonów.
//	--------------------------------------------------------------------------
	public static final String RP_DATE = "date";
	public static final String RP_DATE_FORMAT = "date_format";

	public static final String UP_DATE_LOCALE = "date_locale";
	public static final String UP_DATE_FORMAT_SHORT  = "date_format_short";
	public static final String UP_DATE_FORMAT_MEDIUM = "date_format_medium";
	public static final String UP_DATE_FORMAT_LONG   = "date_format_long";
	public static final String UP_DATE_FORMAT_FULL   = "date_format_full";
	public static final String UP_TIME_FORMAT_SHORT  = "time_format_short";
	public static final String UP_TIME_FORMAT_MEDIUM = "time_format_medium";
	public static final String UP_TIME_FORMAT_LONG   = "time_format_long";
	public static final String UP_TIME_FORMAT_FULL   = "time_format_full";

	public static final String TP_YEAR  = "year";
	public static final String TP_MONTH = "month";
	public static final String TP_DAY   = "day";
	public static final String TP_DATE_SHORT  = "date_short";
	public static final String TP_DATE_MEDIUM = "date_medium";
	public static final String TP_DATE_LONG   = "date_long";
	public static final String TP_DATE_FULL   = "date_full";
	public static final String TP_TIME_SHORT  = "time_short";
	public static final String TP_TIME_MEDIUM = "time_medium";
	public static final String TP_TIME_LONG   = "time_long";
	public static final String TP_TIME_FULL   = "time_full";

	/**
	 * 
	 * 
	 * @param properties
	 * @throws Exception 
	 */
	private Map<String, Object> addRuntimeProperties(Map<String, Object> properties) throws Exception {
		Map<String, Object> props = new HashMap<String, Object>();

		// Określanie lokalizacji daty/czasu używanej do wypełnienia paramtrów szablonów
		// zawierających datę/czas w formatach DateFormat.SHORT, .MEDIUM, .LONG i .FULL:
		Locale date_locale;
		if(properties.containsKey(UP_DATE_LOCALE)) {
			date_locale = LocaleUtils.toLocale((String)properties.get(UP_DATE_LOCALE));
		} else {
			date_locale = Locale.getDefault();
		}

		// Wyznaczanie daty, na podstawie której zostaną wypełnione parametry szablonów
		// przechowujące datę/czas w formatach DateFormat.SHORT, .MEDIUM, .LONG i .FULL.
		// Odbywa się w oparciu o wartości parametrów 'date_format' i 'date'. Parametr
		// 'date_format' definiuje format używany do parsowania łańcucha reprezentującego
		// datę określoną parametrem 'date'. Parametr 'date_format' może nie być określony.
		// W takiej sytuacji użyty jest format DateFormat.LONG aktywnej lokalizacji (tj.
		// systemowej, która może być przedefiniowana przez parametr 'date_locale'), który
		// może być przedefiniowany przez parametry 'date_format_long' i 'time_format_long':
		Calendar calendar = Calendar.getInstance(date_locale);
		if(properties.containsKey(RP_DATE)) {
			String date_string = (String)properties.get(RP_DATE);
			if(properties.containsKey(RP_DATE_FORMAT)) {
				String date_format = (String)properties.get(RP_DATE_FORMAT);
				DateFormat formatter = new SimpleDateFormat(date_format);
				calendar.setTime(formatter.parse(date_string));
			} else if(properties.containsKey(UP_DATE_FORMAT_LONG) && properties.containsKey(UP_TIME_FORMAT_LONG)) {
				// TODO: Założenie, że format data-czas jest złożony z łańcucha daty i czasu rozdzelonych spacją:
				// 'UP_DATE_FORMAT_LONG UP_TIME_FORMAT_LONG'
				DateFormat formatter = new SimpleDateFormat(
						(String)properties.get(UP_DATE_FORMAT_LONG) + " " +
						(String)properties.get(UP_TIME_FORMAT_LONG), date_locale);
				calendar.setTime(formatter.parse(date_string));
			} else {
				DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.LONG,
						DateFormat.LONG, date_locale);
				calendar.setTime(formatter.parse(date_string));
			}
		}

		// Jeśli nie określono, wypełnianie parametrów przechowujących poszczególne
		// składniki daty, tj. rok, miesiąc i dzień:
		if(!properties.containsKey(TP_YEAR)) {
			props.put(TP_YEAR, calendar.get(Calendar.YEAR));
		}
		if(!properties.containsKey(TP_MONTH)) {
			props.put(TP_MONTH, calendar.get(Calendar.MONTH));
		}
		if(!properties.containsKey(TP_DAY)) {
			props.put(TP_DAY, calendar.get(Calendar.DAY_OF_MONTH));
		}

		// Jeśli nie określono, wypełnianie parametrów przechowujących datę i czas w
		// formatach SHORT, MEDIUM, LONG i FULL (na podstawie wyznaczonej lokalizacji):
		Date date = calendar.getTime();
		if(!properties.containsKey(TP_DATE_SHORT)) {
			DateFormat formatter;
			if(properties.containsKey(UP_DATE_FORMAT_SHORT)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_DATE_FORMAT_SHORT), date_locale
				);
			} else {
				formatter = DateFormat.getDateInstance(DateFormat.SHORT, date_locale);
			}
			props.put(TP_DATE_SHORT, formatter.format(date));
		}
		if(!properties.containsKey(TP_DATE_MEDIUM)) {
			DateFormat formatter;
			if(properties.containsKey(UP_DATE_FORMAT_MEDIUM)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_DATE_FORMAT_MEDIUM), date_locale
				);
			} else {
				formatter = DateFormat.getDateInstance(DateFormat.MEDIUM, date_locale);
			}
			props.put(TP_DATE_MEDIUM, formatter.format(date));
		}
		if(!properties.containsKey(TP_DATE_LONG)) {
			DateFormat formatter;
			if(properties.containsKey(UP_DATE_FORMAT_LONG)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_DATE_FORMAT_LONG), date_locale
				);
			} else {
				formatter = DateFormat.getDateInstance(DateFormat.LONG, date_locale);
			}
			props.put(TP_DATE_LONG, formatter.format(date));
		}
		if(!properties.containsKey(TP_DATE_FULL)) {
			DateFormat formatter;
			if(properties.containsKey(UP_DATE_FORMAT_FULL)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_DATE_FORMAT_FULL), date_locale
				);
			} else {
				formatter = DateFormat.getDateInstance(DateFormat.FULL, date_locale);
			}
			props.put(TP_DATE_FULL, formatter.format(date));
		}

		if(!properties.containsKey(TP_TIME_SHORT)) {
			DateFormat formatter;
			if(properties.containsKey(UP_TIME_FORMAT_SHORT)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_TIME_FORMAT_SHORT), date_locale
				);
			} else {
				formatter = DateFormat.getTimeInstance(DateFormat.SHORT, date_locale);
			}
			props.put(TP_TIME_SHORT, formatter.format(date));
		}
		if(!properties.containsKey(TP_TIME_MEDIUM)) {
			DateFormat formatter;
			if(properties.containsKey(UP_TIME_FORMAT_MEDIUM)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_TIME_FORMAT_MEDIUM), date_locale
				);
			} else {
				formatter = DateFormat.getTimeInstance(DateFormat.MEDIUM, date_locale);
			}
			props.put(TP_TIME_MEDIUM, formatter.format(date));
		}
		if(!properties.containsKey(TP_TIME_LONG)) {
			DateFormat formatter;
			if(properties.containsKey(UP_TIME_FORMAT_LONG)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_TIME_FORMAT_LONG), date_locale
				);
			} else {
				formatter = DateFormat.getTimeInstance(DateFormat.LONG, date_locale);
			}
			props.put(TP_TIME_LONG, formatter.format(date));
		}
		if(!properties.containsKey(TP_TIME_FULL)) {
			DateFormat formatter;
			if(properties.containsKey(UP_TIME_FORMAT_FULL)) {
				formatter = new SimpleDateFormat(
						(String)properties.get(UP_TIME_FORMAT_FULL), date_locale
				);
			} else {
				formatter = DateFormat.getTimeInstance(DateFormat.FULL, date_locale);
			}
			props.put(TP_TIME_FULL, formatter.format(date));
		}

		return props;
	}
}
