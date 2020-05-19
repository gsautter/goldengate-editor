/*
 * Copyright (c) 2006-, IPD Boehm, Universitaet Karlsruhe (TH) / KIT, by Guido Sautter
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the Universitaet Karlsruhe (TH) / KIT nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY UNIVERSITAET KARLSRUHE (TH) / KIT AND CONTRIBUTORS 
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.uka.ipd.idaho.goldenGate.batch;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.AnnotationUtils;
import de.uka.ipd.idaho.gamta.DocumentRoot;
import de.uka.ipd.idaho.gamta.util.Analyzer;
import de.uka.ipd.idaho.gamta.util.GenericGamtaXML;
import de.uka.ipd.idaho.gamta.util.ProgressMonitor;
import de.uka.ipd.idaho.gamta.util.SgmlDocumentReader;
import de.uka.ipd.idaho.gamta.util.constants.LiteratureConstants;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;
import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;
import de.uka.ipd.idaho.goldenGate.configuration.FileConfiguration;
import de.uka.ipd.idaho.goldenGate.configuration.UrlConfiguration;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.MonitorableDocumentProcessor;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Batch runner utility for GoldenGATE Imagine. This command line tool fully
 * automatically converts PDF documents into IMF documents, optionally running
 * a series of Image Markup tools in between. This fully automated conversion
 * tool works best if document style templates exist for the PDF documents to
 * process.
 * 
 * @author sautter
 */
public class GoldenGateEditorBatch implements GoldenGateConstants {
	private static final String DATA_PARAMETER = "DATA";
	private static final String DATA_TYPE_PARAMETER = "DT";
	private static final String DATA_ENCODING_PARAMETER = "DE";
	private static final String OUT_PARAMETER = "OUT";
	private static final String OUT_TYPE_PARAMETER = "OT";
	private static final String HELP_PARAMETER = "HELP";
	
	private static File BASE_PATH = null;
	
	private static Settings PARAMETERS = new Settings();
	
	private static final String LOG_TIMESTAMP_DATE_FORMAT = "yyyyMMdd-HHmm";
	private static final DateFormat LOG_TIMESTAMP_FORMATTER = new SimpleDateFormat(LOG_TIMESTAMP_DATE_FORMAT);
	
	/**	the main method to run GoldenGATE Imagine as a batch application
	 */
	public static void main(String[] args) throws Exception {
		
		//	adjust basic parameters
		String basePath = "./";
		String logFileName = ("GoldenGateBatch." + LOG_TIMESTAMP_FORMATTER.format(new Date()) + ".log");
		String dataBaseName = null;
		String dataType = "X";
		String dataEncoding = "UTF-8";
		String dataOutPath = null;
		String dataOutType = "X";
		boolean printHelpImplicit = true;
		boolean printHelpExplicit = false;
		
		//	parse remaining args
		for (int a = 0; a < args.length; a++) {
			if (args[a] == null)
				continue;
			if (args[a].startsWith(BASE_PATH_PARAMETER + "="))
				basePath = args[a].substring((BASE_PATH_PARAMETER + "=").length());
			else if (args[a].startsWith(DATA_PARAMETER + "=")) {
				dataBaseName = args[a].substring((DATA_PARAMETER + "=").length());
				printHelpImplicit = false;
			}
			else if (args[a].equals(HELP_PARAMETER)) {
				printHelpExplicit = true;
				break;
			}
			else if (args[a].startsWith(DATA_TYPE_PARAMETER + "="))
				dataType = args[a].substring((DATA_TYPE_PARAMETER + "=").length());
			else if (args[a].startsWith(DATA_ENCODING_PARAMETER + "="))
				dataEncoding = args[a].substring((DATA_ENCODING_PARAMETER + "=").length());
			else if (args[a].startsWith(OUT_PARAMETER + "="))
				dataOutPath = args[a].substring((OUT_PARAMETER + "=").length());
			else if (args[a].startsWith(OUT_TYPE_PARAMETER + "="))
				dataOutType = args[a].substring((OUT_TYPE_PARAMETER + "=").length());
			else if (args[a].equals(LOG_PARAMETER + "=DOC"))
				logFileName = "DOC";
			else if (args[a].equals(LOG_PARAMETER + "=IDE") || args[a].equals(LOG_PARAMETER + "=NO"))
				logFileName = null;
			else if (args[a].startsWith(LOG_PARAMETER + "="))
				logFileName = args[a].substring((LOG_PARAMETER + "=").length());
		}
		
		//	print help and exit if asked to
		if (printHelpExplicit || printHelpImplicit) {
			System.out.println("GoldenGATE Batch can take the following parameters:");
			System.out.println("");
			System.out.println("PATH:\tthe folder to run GoldenGATE Imagine Batch in (defaults to the\r\n\tinstallation folder)");
			System.out.println("DATA:\tthe files to process:");
			System.out.println("\t- set to XML file path and name to process that file");
			System.out.println("\t- set to folder path and name to process all XML files in that folder");
			System.out.println("\t- set to TXT file to process all XML files listed in that file");
			System.out.println("DT:\tthe type of the XML files to process (defaults to 'X' for 'plain XML'):");
			System.out.println("\t- set to 'X' to indicate plain XML files");
			System.out.println("\t- set to 'G' to indicate generic GAMTA XML files");
			System.out.println("DE:\tthe character encoding of the data (defaults to 'UTF-8'):");
			System.out.println("\t- set to any valif character set name to indicate that encoding");
			System.out.println("OUT:\tthe folder to store the processed XML files in (defaults to the folder\r\n\teach individual source XML file was loaded from)");
			System.out.println("OT:\tthe way of storing the processed XML files (defaults to 'X' for 'plain XML'):");
			System.out.println("\t- set to 'G' to indicate generic GAMTA XML");
			System.out.println("LOG:\tthe name for the log files to write respective information to (file\r\n\tnames are suffixed with '.out.log' and '.err.log', set to 'IDE' or 'NO'\r\n\tto log directly to the console, or to DOC to create one log file per\r\n\tdocument, located next to the IMF)");
			System.out.println("HELP:\tprint this help text");
			System.out.println("");
			System.out.println("The file 'GoldenGateBatch.cnfg' specifies what to do to documents:");
			System.out.println("- documentProcessors: a space separated list of the Document Processors to run");
			System.out.println("- configName: the name of the GoldenGATE configuration to load the\r\n  Document Processors from");
			System.exit(0);
		}
		
		//	get list of files to process (either all PDFs in some folder, or the ones listed in some TXT file, or some already-decoded files)
		File[] dataInFiles = null;
		
		//	folder to process
		File dataInBase = new File(dataBaseName);
		if (dataInBase.isDirectory()) {
			dataInFiles = dataInBase.listFiles(new FileFilter() {
				public boolean accept(File file) {
					return (file.isFile() && file.getName().toLowerCase().endsWith(".pdf"));
				}
			});
		}
		else if (dataInBase.getName().toLowerCase().endsWith(".pdf")) {
			dataInFiles = new File[1];
			dataInFiles[0] = dataInBase;
		}
		else if (dataInBase.getName().toLowerCase().endsWith(".txt")) {
			StringVector dataInNames = StringVector.loadList(dataInBase);
			ArrayList dataInFileList = new ArrayList();
			for (int d = 0; d < dataInNames.size(); d++) {
				File dataInFile = new File(dataInNames.get(d));
				if (dataInFile.isDirectory())
					dataInFileList.addAll(Arrays.asList(dataInFile.listFiles(new FileFilter() {
						public boolean accept(File file) {
							return (file.isFile() && file.getName().toLowerCase().endsWith(".pdf"));
						}
					})));
				else if (dataInFile.getName().toLowerCase().endsWith(".pdf"))
					dataInFileList.add(dataInFile);
			}
			dataInFiles = ((File[]) dataInFileList.toArray(new File[dataInFileList.size()]));
		}
		
		//	anything to work on?
		if ((dataInFiles == null) || (dataInFiles.length == 0)) {
			System.out.println("No data specified to work with, use 'DATA' parameter:");
			System.out.println("- set to PDF file name: process that file");
			System.out.println("- set to folder name: process all PDF files in that folder");
			System.out.println("- set to TXT file: process all PDF files listed in there");
			System.exit(0);
		}
		
		//	remember program base path
		BASE_PATH = new File(basePath);
		
		//	load parameters
		System.out.println("Loading parameters");
		try {
			StringVector parameters = StringVector.loadList(new File(BASE_PATH, PARAMETER_FILE_NAME));
			for (int p = 0; p < parameters.size(); p++) try {
				String param = parameters.get(p);
				int split = param.indexOf('=');
				if (split != -1) {
					String key = param.substring(0, split).trim();
					String value = param.substring(split + 1).trim();
					if ((key.length() != 0) && (value.length() != 0))
						PARAMETERS.setSetting(key, value);
				}
			} catch (Exception e) {}
		} catch (Exception e) {}
		
		//	configure web access
		if (PARAMETERS.containsKey(PROXY_NAME)) {
			System.getProperties().put("proxySet", "true");
			System.getProperties().put("proxyHost", PARAMETERS.getSetting(PROXY_NAME));
			if (PARAMETERS.containsKey(PROXY_PORT))
				System.getProperties().put("proxyPort", PARAMETERS.getSetting(PROXY_PORT));
			
			if (PARAMETERS.containsKey(PROXY_USER) && PARAMETERS.containsKey(PROXY_PWD)) {
				//	initialize proxy authentication
			}
		}
		
		//	preserve original System.out and write major steps there
		final PrintStream systemOut = new PrintStream(System.out) {
			public void println(String str) {
				super.println(str);
				if (System.out != this.out)
					System.out.println(str);
			}
		};
		
		//	create log files if required
		File logFolder = null;
		if ((logFileName != null) && !"DOC".equals(logFileName)) try {
			File logFileOut = null;
			File logFileErr = null;
			
			//	truncate log file extension
			if (logFileName.endsWith(".log"))
				logFileName = logFileName.substring(0, (logFileName.length() - ".log".length()));
			
			//	create absolute log files
			if (logFileName.startsWith("/") || (logFileName.indexOf(':') != -1)) {
				logFileOut = new File(logFileName + ".out.log");
				logFileErr = new File(logFileName + ".err.log");
				logFolder = logFileOut.getAbsoluteFile().getParentFile();
			}
			
			//	create relative log files (the usual case)
			else {
				
				//	get log path
				String logFolderName = PARAMETERS.getSetting(LOG_PATH, LOG_FOLDER_NAME);
				if (logFolderName.startsWith("/") || (logFolderName.indexOf(':') != -1))
					logFolder = new File(logFolderName);
				else logFolder = new File(BASE_PATH, logFolderName);
				logFolder = logFolder.getAbsoluteFile();
				logFolder.mkdirs();
				
				//	create log files
				logFileOut = new File(logFolder, (logFileName + ".out.log"));
				logFileErr = new File(logFolder, (logFileName + ".err.log"));
			}
			
			//	redirect System.out
			logFileOut.getAbsoluteFile().getParentFile().mkdirs();
			logFileOut.createNewFile();
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(logFileOut)), true, "UTF-8"));
			
			//	redirect System.err
			logFileErr.getAbsoluteFile().getParentFile().mkdirs();
			logFileErr.createNewFile();
			System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(logFileErr)), true, "UTF-8"));
		}
		catch (Exception e) {
			systemOut.println("Could not create log files in folder '" + logFolder.getAbsolutePath() + "':" + e.getMessage());
			e.printStackTrace(systemOut);
		}
		
		//	load GoldenGATE Imagine specific settings
		final Settings ggiSettings = Settings.loadSettings(new File(BASE_PATH, "GoldenGateBatch.cnfg"));
		
		//	get list of image markup tools to run
		String imtNameString = ggiSettings.getSetting("documentProcessors");
		if (imtNameString == null) {
			systemOut.println("No Document Processors configured to run, check entry" +
					"\r\n'documentProcessors' in GoldenGateBatch.cnfg");
			System.exit(0);
		}
		String[] imtNames = imtNameString.split("\\s+");
		
		//	use configuration specified in settings (default to 'Default.imagine' for now)
		String ggConfigName = ggiSettings.getSetting("configName");
		
		//	open GoldenGATE Imagine window
		GoldenGateConfiguration ggConfig = null;
		
		//	local master configuration selected
		if (ggConfigName == null)
			ggConfig = new FileConfiguration("Local Master Configuration", BASE_PATH, true, true, null);
		
		//	other local configuration selected
		else if (ggConfigName.startsWith("http://") || ggConfigName.startsWith("https://"))
			ggConfig = new UrlConfiguration(ggConfigName);
		
		//	remote configuration selected
		else ggConfig = new FileConfiguration(ggConfigName, new File(new File(BASE_PATH, CONFIG_FOLDER_NAME), ggConfigName), false, true, null);
//		
//		//	if cache path set, add settings for page image and supplement cache
//		if (cacheRootPath != null) {
//			if (!cacheRootPath.endsWith("/"))
//				cacheRootPath += "/";
//			Settings set = ggConfig.getSettings();
//			set.setSetting("cacheRootFolder", cacheRootPath);
//			set.setSetting("pageImageFolder", (cacheRootPath + "PageImages"));
//			set.setSetting("supplementFolder", (cacheRootPath + "Supplements"));
//		}
		
		//	instantiate GoldenGATE Core
		GoldenGATE goldenGate = GoldenGATE.openGoldenGATE(ggConfig, false, false);
		systemOut.println("GoldenGATE core created, configuration is " + ggConfigName);
		
		//	get individual document processors
		DocumentProcessor[] dps = new DocumentProcessor[imtNames.length];
		for (int t = 0; t < imtNames.length; t++) {
			dps[t] = goldenGate.getDocumentProcessorForName(imtNames[t]);
			if (dps[t] == null) {
				systemOut.println("Document Processor '" + imtNames[t] + "' not found," +
						"\r\ncheck entry 'documentProcessors' in GoldenGateBatch.cnfg");
				System.exit(0);
			}
			else systemOut.println("Image Markup Tool '" + imtNames[t] + "' loaded");
		}
		
		//	create progress monitor forking steps to console
		ProgressMonitor pm = new ProgressMonitor() {
			public void setStep(String step) {
				systemOut.println(step);
			}
			public void setInfo(String info) {
				System.out.println(info);
			}
			public void setBaseProgress(int baseProgress) {}
			public void setMaxProgress(int maxProgress) {}
			public void setProgress(int progress) {}
		};
		
		//	process files
		PerDocLogger perDocLogger = null;
		for (int d = 0; d < dataInFiles.length; d++) try {
			
			//	determine where to store document
			String dataOutName = (dataInFiles[d].getName() + ("G".equals(dataOutType) ? ".gamta.xml" : ".xml"));
			File dataOutFile;
			if (dataOutPath == null)
				dataOutFile = new File(dataInFiles[d].getAbsoluteFile().getParentFile(), dataOutName);
			else dataOutFile = new File(dataOutPath, dataOutName);
			
			//	we've processed this one before
			if (dataOutFile.exists()) {
				systemOut.println("Document '" + dataInFiles[d].getAbsolutePath() + "' processed before, skipping");
				continue;
			}
			else systemOut.println("Processing document '" + dataInFiles[d].getAbsolutePath() + "'");
			
			//	create document specific log files if requested
			if ("DOC".equals(logFileName)) try {
				logFolder = dataOutFile.getAbsoluteFile().getParentFile();
				perDocLogger = new PerDocLogger(logFolder, dataInFiles[d].getName());
			}
			catch (Exception e) {
				systemOut.println("Could not create log files in folder '" + logFolder.getAbsolutePath() + "':" + e.getMessage());
				e.printStackTrace(systemOut);
			}
			
			//	load document
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dataInFiles[d]), dataEncoding));
			DocumentRoot doc;
			if ("G".equalsIgnoreCase(dataType))
				doc = GenericGamtaXML.readDocument(in);
			else doc = SgmlDocumentReader.readDocument(in);
			in.close();
			systemOut.println(" - document loaded");
			
			//	add document name if missing
			if (!doc.hasAttribute(LiteratureConstants.DOCUMENT_NAME_ATTRIBUTE));
				doc.setAttribute(LiteratureConstants.DOCUMENT_NAME_ATTRIBUTE, dataInFiles[d].getName());
			
			//	process document
			Properties params = new Properties();
			params.setProperty(Analyzer.ONLINE_PARAMETER, Analyzer.ONLINE_PARAMETER);
			for (int t = 0; t < dps.length; t++) {
				systemOut.println("Running Document Processor '" + dps[t].getName() + "'");
				if (dps[t] instanceof MonitorableDocumentProcessor)
					((MonitorableDocumentProcessor) dps[t]).process(doc, params, pm);
				else dps[t].process(doc, params);
			}
			
			//	create storage location
			dataOutFile.getAbsoluteFile().getParentFile().mkdirs();
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dataOutFile), dataEncoding));
			systemOut.println("Storing document to '" + dataOutFile.getAbsolutePath() + "'");
			
			//	store document as XML ...
			if ("X".equals(dataOutType))
				AnnotationUtils.writeXML(doc, out, true);
			
			//	... or GAMTA XML
			else GenericGamtaXML.storeDocument(doc, out);
			
			//	finalize storing
			out.flush();
			out.close();
			systemOut.println("Document stored");
		}
		
		//	catch and log whatever might go wrong
		catch (Throwable t) {
			systemOut.println("Error processing document '" + dataInFiles[d].getAbsolutePath() + "': " + t.getMessage());
			t.printStackTrace(systemOut);
		}
		
		//	clean up, error or not
		finally {
			
			//	close log files if logging per document
			if (perDocLogger != null)
				perDocLogger.close();
			perDocLogger = null;
			
			//	garbage collect whatever is left
			System.gc();
		}
		
		//	shut down whatever threads are left
		System.exit(0);
	}
	
	private static class PerDocLogger {
		private File logFolder;
		private String docName;
		
		private File logFileOut;
		private PrintStream logOut;
		private PrintStream sysOut;
		
		private File logFileErr;
		private PrintStream logErr;
		private PrintStream sysErr;
		
		PerDocLogger(File logFolder, String docName) throws Exception {
			this.logFolder = logFolder;
			this.docName = docName;
			
			//	create log files
			this.logFolder.mkdirs();
			this.logFileOut = new File(this.logFolder, (this.docName + ".out.log"));
			this.logFileErr = new File(this.logFolder, (this.docName + ".err.log"));
			
			//	redirect System.out
			this.logFileOut.createNewFile();
			this.sysOut = System.out;
			this.logOut = new PrintStream(new BufferedOutputStream(new FileOutputStream(this.logFileOut)), true, "UTF-8");
			System.setOut(this.logOut);
			
			//	redirect System.err
			this.logFileErr.createNewFile();
			this.sysErr = System.err;
			this.logErr = new PrintStream(new BufferedOutputStream(new FileOutputStream(this.logFileErr)), true, "UTF-8");
			System.setErr(this.logErr);

		}
		
		void close() {
			
			//	restore System.out
			System.setOut(this.sysOut);
			this.logOut.flush();
			this.logOut.close();
			
			//	restore System.err
			System.setErr(this.sysErr);
			this.logErr.flush();
			this.logErr.close();
			
			//	zip up log files
			try {
				File zipFile = new File(this.logFolder, (this.docName + ".logs.zip"));
				ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
				this.zipUp(this.logFileOut, zipOut);
				this.zipUp(this.logFileErr, zipOut);
				zipOut.flush();
				zipOut.close();
			}
			catch (Exception e) {
				System.out.println("Could not zip up log files in '" + this.logFolder.getAbsolutePath() + "':" + e.getMessage());
				e.printStackTrace(System.out);
			}
		}
		
		void zipUp(File logFile, ZipOutputStream zipOut) throws Exception {
			
			//	zip up log file (unless it's empty)
			if (logFile.length() != 0) {
				InputStream logIn = new BufferedInputStream(new FileInputStream(logFile));
				zipOut.putNextEntry(new ZipEntry(logFile.getName()));
				byte[] buffer = new byte[1024];
				for (int r; (r = logIn.read(buffer, 0, buffer.length)) != -1;)
					zipOut.write(buffer, 0, r);
				zipOut.closeEntry();
				logIn.close();
			}
			
			//	clean up plain log file
			logFile.delete();
		}
	}
}