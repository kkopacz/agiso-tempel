/* org.agiso.tempel.starter.Bootstrap (02-10-2012)
 * 
 * Bootstrap.java
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
package org.agiso.tempel.starter;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.agiso.tempel.ITempel;
import org.agiso.tempel.api.internal.IParamReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Klasa startowa obsługująca uruchamianie aplikacji z linii komend.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class Bootstrap {
	private static IParamReader PARAM_READER = null;

//	--------------------------------------------------------------------------
	public static void setParamReader(IParamReader paramReader) {
		PARAM_READER = paramReader;
	}

//	--------------------------------------------------------------------------
	public static void main(String[] args) throws Exception {
		main(PARAM_READER, args);
	}
	public static void main(IParamReader paramReader, String[] args) throws Exception {
		// Obsługa wywołania bezargumentowego:
		if(args.length == 0) {
			printTempelInfo();
			System.exit(0);
		}

		// Konfiguracja opcji i parsowanie argumentów:
		Options options = configureTempelOptions();

		// Parsowanie parametrów wejściowych:
		CommandLine cmd = parseTempelCommandArgs(options, args);

		// Wyświetlanie pomocy dla wywołania z parametrem 'help':
		if(cmd.hasOption('h')) {
			printTempelHelp(options);
			System.exit(0);
		}

		// Określanie katalogu roboczego i katalogu repozytorium:
		String workDir = determineWorkDir(cmd);

		// Pobieranie nazwy szablonu do wykonania:
		String templateName;
		if(cmd.getArgList().size() != 1) {
			System.err.println("Incorrect params. Use \"tpl --help\" for help.");
			System.exit(-1);
		}
		templateName = String.valueOf(cmd.getArgList().get(0));

		// Budowanie mapy parametrów dodatkowych (określanych przez -Dkey=value):
		Map<String, String> params = new HashMap<String, String>();
		Properties properties = cmd.getOptionProperties("D");
		Enumeration<?> propertiesEnumeration = properties.propertyNames();
		while(propertiesEnumeration.hasMoreElements()) {
			String key = (String)propertiesEnumeration.nextElement();
			params.put(key, properties.getProperty(key));
		}

		// Uruchamianie generatora dla określonego szablonu:
		System.out.println("--- Uruchamianie szablonu " + templateName + " ---");

		ITempel tempel = createTempelInstance();
		if(paramReader != null) {
			tempel.setParamReader(paramReader);
		}
		tempel.startTemplate(templateName, params, workDir);

		System.out.println("--- Zakonczono pomyslnie ---");
	}

	private static ITempel createTempelInstance() {
		ClassPathXmlApplicationContext ctx = null;
		try {
			ctx = new ClassPathXmlApplicationContext(
					"classpath*:/META-INF/spring/tempel-context.xml"
			);

			return ctx.getBean(ITempel.class);
		} finally {
			if(ctx != null) {
				try {
					ctx.close();
				} catch(Exception e) {
				}
			}
		}
	}

//	--------------------------------------------------------------------------
	private static void printTempelInfo() {
		System.out.println("Agiso Tempel - version 0.0.1.BUILD-SNAPSHOT");
		System.out.println("Copyright 2013 agiso.org");
		System.out.println();
		System.out.println("usage: tpl template [options]");
		System.out.println("   or: tpl --help");
	}

	private static void printTempelHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("tpl template [options]", options);
	}

	private static Options configureTempelOptions() {
		Options options = new Options();

		Option help = new Option("h", "help", false, "print this message");
		options.addOption(help);

		Option version = new Option("v", "version", false, "print the version information and exit");
		options.addOption(version);

		@SuppressWarnings("static-access")
		Option directory = OptionBuilder.withLongOpt("directory")
				.withDescription("create resources in defined directory")
				.hasArg()
				.withArgName("DIRECTORY")
				.create("d");
		options.addOption(directory);

		@SuppressWarnings("static-access")
		Option property = OptionBuilder.withLongOpt("define")
				.withArgName("property=value")
				.hasArgs(2)
				.withValueSeparator()
				.withDescription("use value for given property")
				.create("D");
		options.addOption(property);

		return options;
	}

	private static CommandLine parseTempelCommandArgs(Options options, String[] args) {
		CommandLine cmd = null;

		CommandLineParser parser = new PosixParser();
		try {
			cmd = parser.parse(options, args);
		} catch(ParseException e) {
			throw new RuntimeException(e);
		}

		return cmd;
	}

	/**
	 * @param cmd
	 * @return
	 * @throws IOException
	 */
	private static String determineWorkDir(CommandLine cmd) throws IOException {
		File workDir;
		if(cmd.hasOption('d')) {
			workDir = new File(cmd.getOptionValue('d').trim());
			if(!workDir.exists()) {
				System.err.println("Working directory does not exist: " + workDir.getPath());
				System.exit(-2);
			} else if(!workDir.isDirectory()) {
				System.err.println("Incorrect working directory: " + workDir.getPath());
				System.exit(-3);
			}
		} else {
			workDir = new File(".");
		}
		System.out.println("Katalog roboczy: " + workDir.getCanonicalPath());
		return workDir.getCanonicalPath();
	}
}
