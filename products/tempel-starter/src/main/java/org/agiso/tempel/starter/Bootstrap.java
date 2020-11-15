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

import static org.agiso.core.i18n.util.I18nUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.*;
import static org.agiso.core.lang.util.AnsiUtils.AnsiElement.*;
import static org.agiso.tempel.ITempel.*;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;

import org.agiso.core.i18n.annotation.I18n;
import org.agiso.core.i18n.support.reflections.AnnotationMessageProvider;
import org.agiso.core.i18n.support.spring.MessageSourceMessageProvider;
import org.agiso.core.i18n.util.I18nUtils;
import org.agiso.core.i18n.util.I18nUtils.I18nId;
import org.agiso.core.lang.util.AnsiUtils.IWrappingAnsiProcessor;
import org.agiso.core.logging.I18nLogger;
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
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;

/**
 * Klasa startowa obsługująca uruchamianie aplikacji z linii komend.
 * 
 * @author Karol Kopacz
 * @since 1.0
 */
@Configuration @EnableAutoConfiguration
@PropertySource("classpath:git.properties")
@ImportResource("classpath*:/META-INF/spring/tempel-context.xml")
public class Bootstrap implements CommandLineRunner {
	static {
		LogManager.getLogManager().reset();
		SLF4JBridgeHandler.install();

		final ResourceBundleMessageSource messageSource
				= new ResourceBundleMessageSource();
		messageSource.setBasename("messages");

		I18nUtils.setMessageProviders(
				new MessageSourceMessageProvider(messageSource),
				new AnnotationMessageProvider("org.agiso.tempel")
		);
	}

	private static final I18nLogger<Logs> starterLogger = LogUtils.getLogger(LOGGER_STARTER);
	private static enum Logs implements I18nId {
		@I18n(def = "Processing template {0}")
		LOG_01,

		@I18n(def = "Template {0} processed successfully")
		LOG_02,

		@I18n(def = "Setting working directory to {0}")
		LOG_03,

		@I18n(def = "Working directory {0} does not exist")
		LOG_04,

		@I18n(def = "Working directory {0} is not correct")
		LOG_05,

		@I18n(def = "Error: {0}")
		LOG_06,
	}
	private static enum Messages implements I18nId {
		@I18n(def = "Copyright 2014-2015 agiso.org")
		COPYRIGHT,

		@I18n(def = "usage: tpl template [options]"
				+ "\n   or: tpl --help")
		USAGE_INFO,

		@I18n(def = "Incorrect params. Use \"tpl --help\" for help.")
		USAGE_ERROR,

		@I18n(def = "tpl template [options]")
		HELP_USAGE,

		@I18n(def = "print this help message")
		HELP_HELP,

		@I18n(def = "print the version information and exit")
		HELP_VERSION,

		@I18n(def = "create resources in defined directory")
		HELP_DIRECTORY,

		@I18n(def = "DIRECTORY")
		HELP_DIRECTORY_ARG,

		@I18n(def = "use value for given property")
		HELP_DEFINE,

		@I18n(def = "property=value")
		HELP_DEFINE_ARG,

		@I18n(def = "run Tempel in debug mode")
		HELP_DEBUG,
	}

	private static IParamReader PARAM_READER = null;

//	--------------------------------------------------------------------------
	@Autowired
	private ITempel tempel;

//	--------------------------------------------------------------------------
	public static void setParamReader(IParamReader paramReader) {
		PARAM_READER = paramReader;
	}

//	--------------------------------------------------------------------------
	public static void main(String[] args) throws Exception {
		// java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);

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

		setParamReader(paramReader);

		SpringApplication application = new SpringApplication(Bootstrap.class);
//		application.addInitializers(new LoggingInitializer());
		application.setShowBanner(false);
		application.run(args);
	}

//	--------------------------------------------------------------------------
	@Override
	public void run(String... args) throws Exception {
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
			System.err.println(getMessage(Messages.USAGE_ERROR));
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
		starterLogger.info(Logs.LOG_01, ansiString(GREEN, templateName));

		try {
			if(PARAM_READER != null) {
				tempel.setParamReader(PARAM_READER);
			}
			tempel.startTemplate(templateName, params, workDir);
			starterLogger.info(Logs.LOG_02, ansiString(GREEN, templateName));
		} catch(Exception e) {
			starterLogger.error(e, Logs.LOG_06, ansiString(RED, e.getMessage()));
			System.exit(-4);
		}
	}

//	--------------------------------------------------------------------------
	@Value("${git.commit.id}")
	private String commitId;
	@Value("${git.build.version}")
	private String buildVersion;

	private void printTempelInfo() {
		System.out.println(ansiString(RED, "Agiso Tempel", " ",
				GREEN, buildVersion + " (" + commitId + ")"));
		System.out.println(getMessage(Messages.COPYRIGHT));
		System.out.println();
		System.out.println(getMessage(Messages.USAGE_INFO));
	}

	private void printTempelHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(getMessage(Messages.HELP_USAGE), options);
	}

	private static Options configureTempelOptions() {
		Options options = new Options();

		Option help = new Option("h", "help", false,
				getMessage(Messages.HELP_HELP));
		options.addOption(help);

		Option version = new Option("v", "version", false,
				getMessage(Messages.HELP_VERSION));
		options.addOption(version);

		@SuppressWarnings("static-access")
		Option directory = OptionBuilder.withLongOpt("directory")
				.withDescription(getMessage(Messages.HELP_DIRECTORY))
				.hasArg()
				.withArgName(getMessage(Messages.HELP_DIRECTORY_ARG))
				.create("d");
		options.addOption(directory);

		@SuppressWarnings("static-access")
		Option property = OptionBuilder.withLongOpt("define")
				.withDescription(getMessage(Messages.HELP_DEFINE))
				.hasArgs(2)
				.withArgName(getMessage(Messages.HELP_DEFINE_ARG))
				.withValueSeparator()
				.create("D");
		options.addOption(property);

		@SuppressWarnings("static-access")
		Option debug = OptionBuilder.withLongOpt("debug")
				.withDescription(getMessage(Messages.HELP_DEBUG))
				.create();
		options.addOption(debug);

		return options;
	}

	private CommandLine parseTempelCommandArgs(Options options, String[] args) {
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
				starterLogger.error(Logs.LOG_04, ansiString(RED, workDir.getPath()));
				System.exit(-2);
			} else if(!workDir.isDirectory()) {
				starterLogger.error(Logs.LOG_05, ansiString(RED, workDir.getPath()));
				System.exit(-3);
			}
		} else {
			workDir = new File(".");
		}

		starterLogger.debug(Logs.LOG_03, ansiString(GREEN, workDir.getPath()));
		return workDir.getCanonicalPath();
	}
}
