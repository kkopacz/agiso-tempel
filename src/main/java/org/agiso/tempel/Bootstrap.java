/* org.agiso.tempel.Bootstrap (02-10-2012)
 * 
 * Bootstrap.java
 * 
 * Copyright 2012 agiso.org
 */
package org.agiso.tempel;

import java.io.File;
import java.util.HashMap;

import org.agiso.tempel.core.DefaultTemplateExecutor;
import org.agiso.tempel.core.RecursiveTemplateVerifier;
import org.agiso.tempel.core.XStreamTemplateProvider;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * Klasa startowa obsługująca uruchamianie aplikacji z linii komend.
 * 
 * @author <a href="mailto:kkopacz@agiso.org">Karol Kopacz</a>
 */
public class Bootstrap {
	public static void main(String[] args) throws Exception {
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

		// Określanie katalogu roboczego:
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

		// FIXME: Określanie katalogu repozytorium szablonów:
		File repoDir = new File("./src/test/resources/repository");

		// Pobieranie nazwy szablonu do wykonania:
		String templateName;
		if(cmd.getArgList().size() != 1) {
			System.err.println("Incorrect params. Use \"tpl --help\" for help.");
			System.exit(-1);
		}
		templateName = String.valueOf(cmd.getArgList().get(0));

		// Uruchamianie generatora dla określonego szablonu:
		Tempel tempel = new Tempel(workDir, repoDir);
		tempel.setTemplateProvider(new XStreamTemplateProvider());
		tempel.setTemplateVerifier(new RecursiveTemplateVerifier());
		tempel.setTemplateExecutor(new DefaultTemplateExecutor());
		tempel.startTemplate(templateName, new HashMap<String, String>());
	}

//	--------------------------------------------------------------------------
	private static void printTempelInfo() {
		System.out.println("Agiso Tempel - version 0.0.1.BUILD-SNAPSHOT");
		System.out.println("Copyright 2012 agiso.org");
		System.out.println();
		System.out.println("usage: tpl template [options]");
		System.out.println("   or: tpl --help");
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

	private static void printTempelHelp(Options options) {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("tpl template [options]", options);
	}
}
