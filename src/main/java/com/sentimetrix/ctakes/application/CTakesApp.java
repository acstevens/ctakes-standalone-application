package com.sentimetrix.ctakes.application;

//import org.apache.log4j.Logger;
import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;
import org.apache.uima.jcas.JCas;

import com.sentimetrix.ctakes.io.CtakesFileWriter;
import com.sentimetrix.ctakes.pipeline.PipelineEnum;
import com.sentimetrix.ctakes.service.ClinicalEntityRecognizer;
import com.sentimetrix.ctakes.service.OutputEnum;

public class CTakesApp 
{
//	private static final Logger LOGGER = Logger.getLogger(CTakesApp.class);
	private static final String CMD_OPT_HELP				= "help";
	private static final String CMD_OPT_INPUT_FILE			= "inputfile";
	private static final String CMD_OPT_TEXT				= "text";
	private static final String CMD_OPT_OUTPUT_FILE			= "outputfile";
	private static final String CMD_OPT_OUTPUT_FORMAT		= "outputformat";
	private static final String CMD_OPT_PIPELINE_TYPE		= "pipeline";
	
	protected static void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("run", options, true);
	}

	protected static Options getOptions() {
		Options options = new Options();
		
		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_HELP)
				.withDescription("Print help")
				.create("h")
		);
		
		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_INPUT_FILE)
				.withDescription("Input File")
				.hasArg()
				.withArgName("file")
				.create("if")
		);

		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_TEXT)
				.withDescription("Text")
				.hasArg()
				.withArgName("text")
				.create("t")
		);

		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_OUTPUT_FORMAT)
				.withDescription("Output Format")
				.hasArg()
				.withArgName("format")
				.create("of")
		);

		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_PIPELINE_TYPE)
				.withDescription("Pipeline type")
				.hasArg()
				.withArgName("pipeline")
				.create("p")
		);

		options.addOption(
			OptionBuilder
				.withLongOpt(CMD_OPT_OUTPUT_FILE)
				.withDescription("Output File")
				.hasArg()
				.withArgName("outfile")
				.create("of")
		);

		return options;
	}

	protected static void processOptions(CommandLine cmd) throws Exception {
	}

    public static void main( String[] args ) throws Exception
    {
		Options options = getOptions();
		CommandLine cmd;
		try {
			CommandLineParser parser = new GnuParser();
			cmd = parser.parse(options, args);
			if (cmd.hasOption("help")) {
				printHelp(options);
				return;
			}
			processOptions(cmd);
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			printHelp(options);
			return;
		}
		
		String text = "";
		if (cmd.hasOption(CMD_OPT_INPUT_FILE)) {
			File file = new File(cmd.getOptionValue(CMD_OPT_INPUT_FILE));
			text = FileUtils.readFileToString(file);
		}
		else if (cmd.hasOption(CMD_OPT_TEXT)) {
        	text = cmd.getOptionValue(CMD_OPT_TEXT);
        }
        
        String pipelineType = "fast";
        if (cmd.hasOption(CMD_OPT_PIPELINE_TYPE)) {
            pipelineType = cmd.getOptionValue(CMD_OPT_PIPELINE_TYPE);
        }

        String outputFormat = "sentence";
        if (cmd.hasOption(CMD_OPT_OUTPUT_FORMAT)) {
        	outputFormat = cmd.getOptionValue(CMD_OPT_OUTPUT_FORMAT);
        }

        String outfilename = null;
        if (cmd.hasOption(CMD_OPT_OUTPUT_FILE)) {
            outfilename = cmd.getOptionValue(CMD_OPT_OUTPUT_FILE);
            if("json".equals(outputFormat)) {
                outfilename += ".json";
            } else {
                outfilename += ".txt";
            }
        }


        if (!text.isEmpty()) {
            ClinicalEntityRecognizer cer = new ClinicalEntityRecognizer(PipelineEnum.valueOf(pipelineType.toUpperCase()));
	        JCas jcas = cer.process(text);
	        String results = cer.format(OutputEnum.valueOf(outputFormat.toUpperCase()), jcas);
	        if (outfilename == null) {
	            System.out.println(results);
	        } else {
	            CtakesFileWriter writer = new CtakesFileWriter(outfilename);
	            writer.write(results);
	        }
        } else {
        	System.err.println("no input text supplied");
        }
    }
}
