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

import static org.agiso.tempel.Temp.AnsiUtils.*;
import static org.agiso.tempel.Temp.AnsiUtils.AnsiElement.*;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.agiso.core.logging.Logger;
import org.agiso.core.logging.util.LogUtils;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Klasa startowa obsługująca uruchamianie aplikacji z linii komend.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
@Configuration @EnableAutoConfiguration
@ImportResource("classpath*:/META-INF/spring/tempel-context.xml")
public class Bootstrap implements CommandLineRunner {
	private static final Logger logger = LogUtils.getLogger(Bootstrap.class);

	private static IParamReader PARAM_READER = null;

	@Autowired
	private ITempel tempel;

//	--------------------------------------------------------------------------
	public static void setParamReader(IParamReader paramReader) {
		PARAM_READER = paramReader;
	}

//	--------------------------------------------------------------------------
	public static void main(String[] args) throws Exception {
		main(PARAM_READER, args);
	}
	public static void main(IParamReader paramReader, String[] args) throws Exception {
		// TODO: Utworzyć interfejs IMappingAnsiProcessor
		setAnsiProcessor(new IWrappingAnsiProcessor() {
//			private static final Map<Object, Object> elementMappings;
//			static {
//				elementMappings = new HashMap<Object, Object>();
//				elementMappings.put(NORMAL, org.springframework.boot.ansi.AnsiElement.NORMAL);
//				// ...
//			}

			@Override
			public String ansiString(Object... elements) {
				return AnsiOutput.toString(elements);
			}
		})	.withAnsiNormal(org.springframework.boot.ansi.AnsiElement.NORMAL)
			.withAnsiBold(org.springframework.boot.ansi.AnsiElement.BOLD)
			.withAnsiFaint(org.springframework.boot.ansi.AnsiElement.FAINT)
			.withAnsiItalic(org.springframework.boot.ansi.AnsiElement.ITALIC)
			.withAnsiUnderline(org.springframework.boot.ansi.AnsiElement.UNDERLINE)
			.withAnsiBlack(org.springframework.boot.ansi.AnsiElement.BLACK)
			.withAnsiRed(org.springframework.boot.ansi.AnsiElement.RED)
			.withAnsiGreen(org.springframework.boot.ansi.AnsiElement.GREEN)
			.withAnsiYellow(org.springframework.boot.ansi.AnsiElement.YELLOW)
			.withAnsiBlue(org.springframework.boot.ansi.AnsiElement.BLUE)
			.withAnsiMagenta(org.springframework.boot.ansi.AnsiElement.MAGENTA)
			.withAnsiCyan(org.springframework.boot.ansi.AnsiElement.CYAN)
			.withAnsiWhite(org.springframework.boot.ansi.AnsiElement.WHITE)
			.withAnsiDefault(org.springframework.boot.ansi.AnsiElement.DEFAULT);

		// Obsługa wywołania bezargumentowego:
		if(args.length == 0) {
			printTempelInfo();
			System.exit(0);
		}

		setParamReader(paramReader);

		SpringApplication application = new SpringApplication(Bootstrap.class);
//		application.addInitializers(new LoggingInitializer());
		application.setShowBanner(false);
		application.run(args);
	}

//	--------------------------------------------------------------------------
	@Override
	public void run(String... args) throws Exception {
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
		logger.info("Running template {}",
				ansiString(GREEN, templateName)
		);

		if(PARAM_READER != null) {
			tempel.setParamReader(PARAM_READER);
		}
		tempel.startTemplate(templateName, params, workDir);

		logger.info("Template {} executed successfully",
				ansiString(GREEN, templateName)
		);
	}

//	--------------------------------------------------------------------------
	private static void printTempelInfo() {
		System.out.println(ansiString(RED, "Agiso Tempel",
				GREEN, "0.0.1.BUILD-SNAPSHOT"));
		System.out.println("Copyright 2014 agiso.org");
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

		Option help = new Option("h", "help", false,
				"print this help message");
		options.addOption(help);

		Option version = new Option("v", "version", false,
				"print the version information and exit");
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

		@SuppressWarnings("static-access")
		Option debug = OptionBuilder.withLongOpt("debug")
				.withDescription("run Tempel in debug mode")
				.create();
		options.addOption(debug);

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
				System.err.println("Working directory does not exist: " +
						ansiString(RED, workDir.getPath())
				);
				System.exit(-2);
			} else if(!workDir.isDirectory()) {
				System.err.println("Incorrect working directory: " +
						ansiString(RED, workDir.getPath())
				);
				System.exit(-3);
			}
		} else {
			workDir = new File(".");
		}

		logger.debug("Setting working directory to {}",
				ansiString(GREEN, workDir.getPath())
		);
		return workDir.getCanonicalPath();
	}
}
