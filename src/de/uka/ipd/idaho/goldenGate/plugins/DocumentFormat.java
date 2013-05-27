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
 *     * Neither the name of the Universität Karlsruhe (TH) nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY UNIVERSITÄT KARLSRUHE (TH) / KIT AND CONTRIBUTORS 
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
package de.uka.ipd.idaho.goldenGate.plugins;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.DocumentRoot;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;

/**
 * Document formats load documents by decoding the data from input streams and
 * readers into GAMTA documents, and save documents by outputting GAMTA
 * documents to output streams or readers. This class provides functionality for
 * recognizing and interpreting byte order marks and character encodings, to be
 * used in situations when the character encoding of text based input is not
 * known.
 * 
 * @author sautter
 */
public abstract class DocumentFormat extends FileFilter implements Resource {
	
	/**	constant for UTF-16LE encoding*/
	protected static final String UTF_16_LE_ENCODING_NAME = "UTF-16LE";
	
	/**	the first int of the UTF-16LE byte order mark*/
	protected static final int UTF_16_LE_FIRST_BYTE = 255;
	
	/**	the second int of the UTF-16LE byte order mark*/
	protected static final int UTF_16_LE_SECOND_BYTE = 254;
	
	
	/**	constant for UTF-16BE encoding*/
	protected static final String UTF_16_BE_ENCODING_NAME = "UTF-16BE";
	
	/**	the firts int of the UTF-16BE byte order mark*/
	protected static final int UTF_16_BE_FIRST_BYTE = 254;
	
	/**	the second int of the UTF-16BE byte order mark*/
	protected static final int UTF_16_BE_SECOND_BYTE = 255;
	
	
	/**	constant for UTF-8 encoding*/
	protected static final String UTF_8_ENCODING_NAME = "UTF-8";
	
	/**	the first int of the UTF-8 byte order mark*/
	protected static final int UTF_8_FIRST_BYTE = 239;
	
	/**	the second int of the UTF-8 byte order mark*/
	protected static final int UTF_8_SECOND_BYTE = 187;
	
	/**	the third int of the UTF-8 byte order mark*/
	protected static final int UTF_8_THIRD_BYTE = 191;
	
	
	/**	the noise int typical for both UTF-16LE and UTF-16BE, namely 0, which appears in odd positions in UTF-16LE and in even ones in UTF-16-BE*/
	protected static final int UTF_16_TYPICAL = 0;
	
	/**	the noise int typical for UTF-8, namely 195, which appears as the first int of many two-int encoded characters*/
	protected static final int UTF_8_TYPICAL = 195;
	
	/**	the lookahead into a given stream for guessing the encoding based on typical bytes*/
	protected static final int CHARSET_GUESS_LOOKAHEAD = 8192; // can use this much, it's the default buffer size of BufferedInputStream anyway
	
	
	/**	the name of the setting for the default encoding*/
	public static final String DEFAULT_ENCODING_SETTING_NAME = "DEFAULT_ENCODING";
	
	/**	the name of the setting for the default format for loading documents (for pre-setting in selections)*/
	public static final String DEFAULT_LOAD_FORMAT_SETTING_NAME = "DEFAULT_LOAD_FORMAT";
	
	/**	the name of the setting for the default format for saving documents (for pre-setting in selections)*/
	public static final String DEFAULT_SAVE_FORMAT_SETTING_NAME = "DEFAULT_SAVE_FORMAT";
	
	/**	the name of the setting specifying if the default save format should be pre-selected even if a document was loaded with a different format*/
	public static final String ENFORCE_SAVE_FORMAT_SETTING_NAME = "ENFORCE_SAVE_FORMAT";
	
	/**	the name of the encoding attribute for documents, remove to use default*/
	public static final String ENCODING_ATTRIBUTE = "ENCODING";
	
	private static String[] encodingNames = null;
	private static String platformDefaultEncodingName;
	private static String defaultEncodingName;
	
	private static String defaultLoadFormatName;
	private static String defaultSaveFormatName;
	private static boolean enforceSaveFormat = false;
	
	
	/** initialize document formats
	 * @param	settings	the Settings object holding the custom settings
	 */
	public static void init(Settings settings) {
		initEncodingNames();
		defaultEncodingName = settings.getSetting(DEFAULT_ENCODING_SETTING_NAME, defaultEncodingName);
		
		defaultLoadFormatName = settings.getSetting(DEFAULT_LOAD_FORMAT_SETTING_NAME);
		defaultSaveFormatName = settings.getSetting(DEFAULT_SAVE_FORMAT_SETTING_NAME);
		enforceSaveFormat = ENFORCE_SAVE_FORMAT_SETTING_NAME.equals(settings.getSetting(ENFORCE_SAVE_FORMAT_SETTING_NAME));
	}
	
	private static void initEncodingNames() {
		if (encodingNames != null) return;
		
		//	we need a catch block here, as on Windows 7 getting the char sets sometimes causes an error ...
		Map availableCharSets = new HashMap();
		try {
			availableCharSets.putAll(Charset.availableCharsets());
		}
		catch (Throwable t) {
			System.out.println("Error getting char sets: " + t.getMessage());
			t.printStackTrace(System.out);
		}
		ArrayList encodingList = new ArrayList(availableCharSets.keySet());
		Collections.sort(encodingList);
		encodingNames = ((String[]) encodingList.toArray(new String[encodingList.size()]));
		
		try {
			InputStreamReader isr = new InputStreamReader(new PipedInputStream());
			String platformDefaultEncoding = isr.getEncoding();
			isr.close();
			
			Charset charSet = Charset.forName(platformDefaultEncoding);
			Set aliasSet = charSet.aliases();
			
			String supportedDefaultEncoding = null;
			for (int e = 0; e < encodingNames.length; e++) {
				charSet = ((Charset) availableCharSets.get(encodingNames[e]));
				Set encodingAliases = new HashSet(charSet.aliases());
				encodingAliases.add(encodingNames[e]);
				Iterator iter = encodingAliases.iterator();
				while (iter.hasNext()) {
					Object alias = iter.next();
					if (aliasSet.contains(alias))
						supportedDefaultEncoding = encodingNames[e];
				}
			}
			if (supportedDefaultEncoding != null)
				platformDefaultEncoding = supportedDefaultEncoding;
			
			if (platformDefaultEncoding != null) {
				platformDefaultEncodingName = platformDefaultEncoding;
				defaultEncodingName = platformDefaultEncoding;
			}
			
			if (DEBUG_CHAR_ENCODING) System.out.println("Determined default character set: " + platformDefaultEncoding);
		} catch (Exception e) {}
	}
	
	/** store document format settings
	 * @param	settings	the Settings object to store the custom settings to
	 */
	public static void storeSettings(Settings settings) {
		if (defaultEncodingName != null)
			settings.setSetting(DEFAULT_ENCODING_SETTING_NAME, defaultEncodingName);
		
		if (defaultLoadFormatName != null)
			settings.setSetting(DEFAULT_LOAD_FORMAT_SETTING_NAME, defaultLoadFormatName);
		if (defaultSaveFormatName != null)
			settings.setSetting(DEFAULT_SAVE_FORMAT_SETTING_NAME, defaultSaveFormatName);
		if (enforceSaveFormat)
			settings.setSetting(ENFORCE_SAVE_FORMAT_SETTING_NAME, ENFORCE_SAVE_FORMAT_SETTING_NAME);
	}
	
	/** @return the names of all character encodings available on this computer
	 */
	public static String[] getEncodingNames() {
		initEncodingNames();
		
		String[] copyEncodingNames = new String[encodingNames.length];
		System.arraycopy(encodingNames, 0, copyEncodingNames, 0, encodingNames.length);
		return copyEncodingNames;
	}
	
	/** @return the name of the default encoding to use for loading and saving documents, if not specified otherwise
	 */
	public static String getDefaultEncodingName() {
		return defaultEncodingName;
	}
	
	/** @return the name of the default format for loading documents (for pre-setting in selections)
	 */
	public static String getDefaultLoadFormatName() {
		return defaultLoadFormatName;
	}
	
	/** @return the name of the default format for saving documents (for pre-setting in selections)
	 */
	public static String getDefaultSaveFormatName() {
		return defaultSaveFormatName;
	}
	
	/** @return enforce the default save format (pre-selected it even if a document was loaded with a different format)?
	 */
	public static boolean enforceDefaultSaveFormat() {
		return enforceSaveFormat;
	}
	
	/**	obtain a panel for editing general document format settings
	 * @param	host	the GoldenGATE main instance (necessary due to static context)
	 * @return	a settings panel for editing the default settings in the GoldenGATE configuration
	 */
	public static SettingsPanel getSettingsPanel(GoldenGATE host) {
		initEncodingNames();
		return new DocumentFormatSettingsPanel(host);
	}
	
	private static class DocumentFormatSettingsPanel extends SettingsPanel {
		private JComboBox encodingChooser;
		
		private JComboBox loadFormatChooser;
		
		private JComboBox saveFormatChooser;
		private JCheckBox enforceSaveFormat = new JCheckBox("Enforce Save Format (always pre-select it)?");
		
		private DocumentFormatSettingsPanel(GoldenGATE host) {
			super("Document Formats", "Configure document formats and related settings here.");
			
			DocumentFormatProvider[] formatters = host.getDocumentFormatProviders();
			ArrayList loadFormatList = new ArrayList();
			ArrayList saveFormatList = new ArrayList();
			for (int d = 0; d < formatters.length; d++) if (formatters[d] != this) {
				DocumentFormat[] loadFormats = formatters[d].getLoadFileFilters();
				for (int f = 0; f < loadFormats.length; f++)
					loadFormatList.add(loadFormats[f]);
				
				DocumentFormat[] saveFormats = formatters[d].getSaveFileFilters();
				for (int f = 0; f < saveFormats.length; f++)
					saveFormatList.add(saveFormats[f]);
			}
			DocumentFormat[] loadFormats = ((DocumentFormat[]) loadFormatList.toArray(new DocumentFormat[loadFormatList.size()]));
			DocumentFormat[] saveFormats = ((DocumentFormat[]) saveFormatList.toArray(new DocumentFormat[saveFormatList.size()]));
			
			this.encodingChooser = new JComboBox(encodingNames);
			this.encodingChooser.setBorder(BorderFactory.createLoweredBevelBorder());
			this.encodingChooser.setEditable(false);
			this.encodingChooser.setSelectedItem(defaultEncodingName);
			
			this.loadFormatChooser = new JComboBox(loadFormats);
			this.loadFormatChooser.setBorder(BorderFactory.createLoweredBevelBorder());
			this.loadFormatChooser.setEditable(false);
			if (defaultLoadFormatName != null)
				for (int f = 0; f < loadFormats.length; f++)
					if (defaultLoadFormatName.equals(loadFormats[f].getDescription()))
						this.loadFormatChooser.setSelectedItem(loadFormats[f]);
			
			this.saveFormatChooser = new JComboBox(saveFormats);
			this.saveFormatChooser.setBorder(BorderFactory.createLoweredBevelBorder());
			this.saveFormatChooser.setEditable(false);
			if (defaultSaveFormatName != null)
				for (int f = 0; f < saveFormats.length; f++)
					if (defaultSaveFormatName.equals(saveFormats[f].getDescription()))
						this.saveFormatChooser.setSelectedItem(saveFormats[f]);
			
			this.enforceSaveFormat.setSelected(DocumentFormat.enforceSaveFormat);
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			this.add(new JLabel("Default Load Format"), gbc.clone());
			gbc.gridx++;
			gbc.weightx = 1;
			this.add(this.loadFormatChooser, gbc.clone());
			gbc.gridx++;
			gbc.weightx = 0;
			this.add(new JLabel("Default Encoding"), gbc.clone());
			gbc.gridx++;
			gbc.weightx = 1;
			this.add(this.encodingChooser, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			this.add(new JLabel("Default Save Format"), gbc.clone());
			gbc.gridx++;
			gbc.weightx = 1;
			this.add(this.saveFormatChooser, gbc.clone());
			gbc.gridx++;
			gbc.gridwidth = 2;
			gbc.weightx = 2;
			this.add(this.enforceSaveFormat, gbc.clone());
		}
		
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			defaultEncodingName = this.encodingChooser.getSelectedItem().toString();
			defaultLoadFormatName = this.loadFormatChooser.getSelectedItem().toString();
			defaultSaveFormatName = this.saveFormatChooser.getSelectedItem().toString();
			DocumentFormat.enforceSaveFormat = this.enforceSaveFormat.isSelected();
		}
	}
	
	/** load a document from an InputStream, the encoding of which is determined automatically
	 * @param	source	the InputStream to read the document from
	 * @return the document that was just read
	 * @throws IOException if any occurs while reading the specified InputStream
	 */
	public MutableAnnotation loadDocument(InputStream source) throws IOException {
		InputStreamReader isr = this.getInputStreamReader(source);
		MutableAnnotation doc = this.loadDocument(isr);
		if (doc != null) doc.setAttribute(ENCODING_ATTRIBUTE, isr.getEncoding());
		return doc;
	}
	
	/** load a document from a Reader
	 * @param	source	the Reader to read the document from
	 * @return the document that was just read
	 * @throws IOException if any occurs while reading the specified Reader
	 */
	public abstract MutableAnnotation loadDocument(Reader source) throws IOException;
	
	/**	determine the encoding of the data provided by an InputStream, and create an appropriate Reader
	 * @param	source	the InputStream to deal with
	 * @return an InputStreamReader decoding the data from the specified InputStream
	 * @throws IOException if any occurs
	 */
	protected InputStreamReader getInputStreamReader(InputStream source) throws IOException {
		if (DEBUG_CHAR_ENCODING) System.out.println("Determining character encoding ...");
		BufferedInputStream bis = new BufferedInputStream(source);
		
		//	too littel data for determining encoding
		if (bis.available() < 3) {
			if (DEBUG_CHAR_ENCODING) System.out.print("  - stream too short for determining encoding, ");
			
			//	use configured default encoding
			if (defaultEncodingName != null) {
				if (DEBUG_CHAR_ENCODING) System.out.println("using configured default encoding.");
				return new InputStreamReader(bis, defaultEncodingName);
			}
			
			//	use platform default encoding
			else {
				if (DEBUG_CHAR_ENCODING) System.out.println("using platform default encoding.");
				return new InputStreamReader(bis);
			}
		}
		
		//	check byte order marks
		if (DEBUG_CHAR_ENCODING) System.out.println("  - checking BOMs");
		bis.mark(3);
		int first = bis.read();
		if (DEBUG_CHAR_ENCODING) System.out.println("    - first byte is " + first);
		
		//	byte order mark for UTF-16 little endian
		if (UTF_16_LE_FIRST_BYTE == first) {
			int second = bis.read();
			if (DEBUG_CHAR_ENCODING) System.out.println("    - second byte is " + second);
			if (UTF_16_LE_SECOND_BYTE == second) {
				if (DEBUG_CHAR_ENCODING) System.out.println("  - recognized BOM of UTF-16 little endian.");
				this.cutBOMs(bis, 2);
				return new InputStreamReader(bis, "UTF-16LE");
			}
		}
		
		//	byte order mark for UTF-16 big endian
		else if (UTF_16_BE_FIRST_BYTE == first) {
			int second = bis.read();
			if (DEBUG_CHAR_ENCODING) System.out.println("    - second byte is " + second);
			if (UTF_16_BE_SECOND_BYTE == second) {
				if (DEBUG_CHAR_ENCODING) System.out.println("  - recognized BOM of UTF-16 big endian.");
				this.cutBOMs(bis, 2);
				return new InputStreamReader(bis, "UTF-16BE");
			}
		}
		
		//	byte order mark for UTF-8
		else if (UTF_8_FIRST_BYTE == first) {
			int second = bis.read();
			if (DEBUG_CHAR_ENCODING) System.out.println("    - second byte is " + second);
			if (UTF_8_SECOND_BYTE == second) {
				int third = bis.read();
				if (DEBUG_CHAR_ENCODING) System.out.println("    - third byte is " + third);
				if (UTF_8_THIRD_BYTE == third) {
					if (DEBUG_CHAR_ENCODING) System.out.println("  - recognized BOM of UTF-8.");
					this.cutBOMs(bis, 3);
					return new InputStreamReader(bis, "UTF-8");
				}
			}
		}
		
		else if (DEBUG_CHAR_ENCODING) {
			int second = bis.read();
			System.out.println("    - second byte is " + second);
			int third = bis.read();
			System.out.println("    - third byte is " + third);
		}
		bis.reset();
		
		//	read some bytes to guess encoding
		bis.mark(CHARSET_GUESS_LOOKAHEAD);
		
		//	read lookahead bytes
		int lookahead = Math.min(CHARSET_GUESS_LOOKAHEAD, bis.available());
		if (DEBUG_CHAR_ENCODING) System.out.println("  - lookahead is " + lookahead);
		int[] lookaheadBytes = new int[lookahead];
		for (int l = 0; l < lookahead; l++)
			lookaheadBytes[l] = bis.read();
		bis.reset();
		if (DEBUG_CHAR_ENCODING) System.out.println("  - input stream reset, available bytes: " + bis.available());
		
		//	check for SGML encoding specifications
		int firstChar = 0;
		while ((firstChar < lookaheadBytes.length) && ((lookaheadBytes[firstChar] < 32) || (lookaheadBytes[firstChar] > 126)))
			firstChar++;
		
		//	file seems to start with an HTML or XML tag
		if ((firstChar < lookaheadBytes.length) && (lookaheadBytes[firstChar] == '<')) {
			StringBuffer tagBuffer = new StringBuffer();
			int i = 0;
			while ((firstChar + i) < lookaheadBytes.length) {
				char c = ((char) lookaheadBytes[firstChar + i]);
				tagBuffer.append(c);
				if (c == '>') {
					firstChar = (firstChar + i + 1);
					i = lookaheadBytes.length;
				}
				else i++;
			}
			String tag = tagBuffer.toString();
			if (DEBUG_CHAR_ENCODING) System.out.println("  - read first tag: " + tag);
			
			//	jump over DOCTYPE declaration
			if (tag.startsWith("<!")) {
				
				//	find start of next tag
				while ((firstChar < lookaheadBytes.length) && (lookaheadBytes[firstChar] != '<'))
					firstChar++;
				
				//	read tag to end
				tagBuffer = new StringBuffer();
				i = 0;
				while ((firstChar + i) < lookaheadBytes.length) {
					char c = ((char) lookaheadBytes[firstChar + i]);
					tagBuffer.append(c);
					if (c == '>') {
						firstChar = (firstChar + i + 1);
						i = lookaheadBytes.length;
					}
					else i++;
				}
				tag = tagBuffer.toString();
				if (DEBUG_CHAR_ENCODING) System.out.println("    - jumped to next tag: " + tag);
			}
			
			//	html file, have to read header <meta http-equiv="content-type" content="text/html; charset=UTF-8"> or <meta charset="UTF-8">
			if (tag.toLowerCase().startsWith("<html")) {
				
				//	proceed until (a) encoding found, (b) end of HTML header reached, or (c) lookahead consumed
				while (firstChar < lookaheadBytes.length) {
					
					//	find start of next tag
					while ((firstChar < lookaheadBytes.length) && (lookaheadBytes[firstChar] != '<'))
						firstChar++;
					
					//	read tag to end
					tagBuffer = new StringBuffer();
					i = 0;
					while ((firstChar + i) < lookaheadBytes.length) {
						char c = ((char) lookaheadBytes[firstChar + i]);
						tagBuffer.append(c);
						if (c == '>') {
							firstChar = (firstChar + i + 1);
							i = lookaheadBytes.length;
						}
						else i++;
					}
					tag = tagBuffer.toString();
					if (DEBUG_CHAR_ENCODING) System.out.println("    - read next tag: " + tag);
					
					//	found what we're looking for?
					if (tag.toLowerCase().startsWith("<meta")) {
						int split = tag.indexOf("charset");
						if (split != -1) {
							String encoding = tag.substring(split + "charset".length());
							split = encoding.indexOf('=');
							if (split != -1) {
								encoding = encoding.substring(split + 1);
								split = encoding.indexOf('"');
								if (split == 0) {
									encoding = encoding.substring(split + 1);
									split = encoding.indexOf('"');
								}
								if (split != -1) {
									encoding = encoding.substring(0, encoding.indexOf('"'));
									if (DEBUG_CHAR_ENCODING) System.out.println("    - parsed encoding: " + encoding);
									return new InputStreamReader(bis, encoding);
								}
							}
						}
					}
					
					//	any hope remaining to find something?
					else if (tag.toLowerCase().equals("</head>"))
						firstChar = lookaheadBytes.length;
				}
			}
			
			//	XML processing instruction <?xml version="1.0" encoding="UTF-8"?>
			else if (tag.startsWith("<?")) {
				int split = tag.indexOf("encoding");
				if (split != -1) {
					String encoding = tag.substring(split + "encoding".length());
					split = encoding.indexOf('"');
					if (split != -1) {
						encoding = encoding.substring(split + 1);
						split = encoding.indexOf('"');
						if (split != -1) {
							encoding = encoding.substring(0, encoding.indexOf('"'));
							if (DEBUG_CHAR_ENCODING) System.out.println("  - parsed encoding: " + encoding);
							return new InputStreamReader(bis, encoding);
						}
					}
				}
			}
			
			//	document XML file <document ENCODING="UTF-8">
			else if (tag.toLowerCase().startsWith("<" + DocumentRoot.DOCUMENT_TYPE.toLowerCase())) {
				int split = tag.indexOf(ENCODING_ATTRIBUTE);
				if (split != -1) {
					String encoding = tag.substring(split + ENCODING_ATTRIBUTE.length());
					split = encoding.indexOf('"');
					if (split != -1) {
						encoding = encoding.substring(split + 1);
						split = encoding.indexOf('"');
						if (split != -1) {
							encoding = encoding.substring(0, encoding.indexOf('"'));
							if (DEBUG_CHAR_ENCODING) System.out.println("  - parsed encoding: " + encoding);
							return new InputStreamReader(bis, encoding);
						}
					}
				}
			}
		}
		
		//	check indicative (less reliable) evidence
		int oddZeros = 0;
		int evenZeros = 0;
		int highPairs = 0;
		int highBytes = 0;
		for (int b = 0; b < lookahead; b++) {
			if (lookaheadBytes[b] == 0) {
				if ((b & 1) == 0)
					evenZeros++;
				else oddZeros++;
			}
			else if (((lookaheadBytes[b] == 194) || (lookaheadBytes[b] == 195)) && ((b+1) < lookaheadBytes.length) && (lookaheadBytes[b+1] > 127)) {
				highPairs++;
				b++;
			}
			else if (lookaheadBytes[b] > 127)
				highBytes++;
		}
		if (DEBUG_CHAR_ENCODING)  {
			System.out.println("  - got evidence:");
			System.out.println("    - high pairs (UTF-8): " + highPairs);
			System.out.println("    - high bytes (ISO): " + highBytes);
			System.out.println("    - even zeros (UTF-16BE): " + evenZeros);
			System.out.println("    - odd zeros (UTF-16LE): " + oddZeros);
		}
		
		//	found many bytes typical for UTF-16 little endian
		if ((oddZeros * 3) > lookaheadBytes.length) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - guess UTF-16LE.");
			return new InputStreamReader(bis, "UTF-16LE");
		}
		
		//	found many bytes typical for UTF-16 big endian
		else if ((evenZeros * 3) > lookaheadBytes.length) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - guess UTF-16BE.");
			return new InputStreamReader(bis, "UTF-16BE");
		}
		
		//	found bytes typical for UTF-8
		else if (highPairs > highBytes) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - guess UTF-8.");
			return new InputStreamReader(bis, "UTF-8");
		}
		
		//	found bytes typical for ISO (windows encodings, use Western European (Windows) for now)
		else if (highBytes > 0) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - guess ISO-8859-1.");
			return new InputStreamReader(bis, "ISO-8859-1");
		}
		
		//	use configured default encoding
		else if (defaultEncodingName != null) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using configured default encoding (" + defaultEncodingName + ").");
			return new InputStreamReader(bis, defaultEncodingName);
		}
		
		//	use platform default encoding
		else {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using platform default encoding.");
			return new InputStreamReader(bis);
		}
	}
	
	private void cutBOMs(BufferedInputStream bis, int byteCount) throws IOException {
		int[] bytes = new int[byteCount];
		while (true) {
			bis.mark(byteCount);
			for (int b = 0; b < byteCount; b++)
				bytes[b] = bis.read();
			if ((byteCount == 3) && (bytes[0] == UTF_8_FIRST_BYTE) && (bytes[1] == UTF_8_SECOND_BYTE) && (bytes[2] == UTF_8_THIRD_BYTE)) {
				if (DEBUG_CHAR_ENCODING) System.out.println("  - skipped obsolete additional BOM for UTF-8.");
				continue;
			}
			if ((byteCount == 2) && (bytes[0] == UTF_16_LE_FIRST_BYTE) && (bytes[1] == UTF_16_LE_SECOND_BYTE)) {
				if (DEBUG_CHAR_ENCODING) System.out.println("  - skipped obsolete additional BOM for UTF-16LE.");
				continue;
			}
			if ((byteCount == 2) && (bytes[0] == UTF_16_BE_FIRST_BYTE) && (bytes[1] == UTF_16_BE_SECOND_BYTE)) {
				if (DEBUG_CHAR_ENCODING) System.out.println("  - skipped obsolete additional BOM for UTF-16BE.");
				continue;
			}
			bis.reset();
			return;
		}
	}
	
	protected static final boolean DEBUG_CHAR_ENCODING = true;
	
	/** save a document to an OutputStream
	 * @param	data	the document to save
	 * @param	out		the OutputStream to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified OutputStream
	 */
	public boolean saveDocument(QueriableAnnotation data, OutputStream out) throws IOException {
		String encoding = ((String) data.getAttribute(ENCODING_ATTRIBUTE));
		if (!UTF_8_ENCODING_NAME.equals(encoding) && !UTF_16_LE_ENCODING_NAME.equals(encoding) && !UTF_16_BE_ENCODING_NAME.equals(encoding))
			encoding = null;
		return this.saveDocument(data, this.getOutputStreamWriter(out, encoding));
	}
	
	/** save the document in a DocumentEditor to an OutputStream
	 * @param	data	the DocumentEditor whose content to save
	 * @param	out		the OutputStream to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified OutputStream
	 */
	public boolean saveDocument(DocumentEditor data, OutputStream out) throws IOException {
		String encoding = ((String) data.getContent().getAttribute(ENCODING_ATTRIBUTE));
		if (!UTF_8_ENCODING_NAME.equals(encoding) && !UTF_16_LE_ENCODING_NAME.equals(encoding) && !UTF_16_BE_ENCODING_NAME.equals(encoding))
			encoding = null;
		return this.saveDocument(data, this.getOutputStreamWriter(out, encoding));
	}
	
	/** save a document to an OutputStream, using the settings of a DocumentEditor 
	 * @param	data	the DocumentEditor providing settings
	 * @param	doc		the document to write
	 * @param	out		the OutputStream to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified OutputStream
	 */
	public boolean saveDocument(DocumentEditor data, QueriableAnnotation doc, OutputStream out) throws IOException {
		String encoding = ((String) doc.getAttribute(ENCODING_ATTRIBUTE));
		if (!UTF_8_ENCODING_NAME.equals(encoding) && !UTF_16_LE_ENCODING_NAME.equals(encoding) && !UTF_16_BE_ENCODING_NAME.equals(encoding))
			encoding = null;
		return this.saveDocument(data, doc, this.getOutputStreamWriter(out, encoding));
	}
	
	/** save a document to a Writer (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the document to save
	 * @param	out		the Writer to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(QueriableAnnotation data, Writer out) throws IOException;
	
	/** save the document in a DocumentEditor to a Writer (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the DocumentEditor whose content to save
	 * @param	out		the Writer to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(DocumentEditor data, Writer out) throws IOException;
	
	/** save a document to a Writer, using the settings of a DocumentEditor (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the DocumentEditor providing settings
	 * @param	doc		the document to write
	 * @param	out		the Writer to write the document to
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(DocumentEditor data, QueriableAnnotation doc, Writer out) throws IOException;
	
	/**	produce an OutputStreamWriter to write to the specified OutputStream in the specified encoding (for UTF-8, UTF-16LE, and UTF-16BE, this method writes the byte order mark to the specified OutputStream before creating the OutputStreamWriter)
	 * @param	out			the OutputStrem to write to
	 * @param 	encoding	the name of the encoding to use
	 * @return an OutputStreamWriter wrapping the specified OutputStream
	 * @throws IOException
	 */
	protected Writer getOutputStreamWriter(OutputStream out, String encoding) throws IOException {
		if (DEBUG_CHAR_ENCODING) System.out.println("Building writer for '" + encoding + "'");
		
		//	write byte order mark for UTF-8 & return respective writer
		if (UTF_8_ENCODING_NAME.equals(encoding)) {
			out.write(UTF_8_FIRST_BYTE);
			out.write(UTF_8_SECOND_BYTE);
			out.write(UTF_8_THIRD_BYTE);
			out.flush();
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using " + UTF_8_ENCODING_NAME);
			return new OutputStreamWriter(new EncodingCatcherOutputStream(out, UTF_8_ENCODING_NAME), UTF_8_ENCODING_NAME);
		}
		
		//	write byte order mark for UTF-16 little endian & return respective writer
		else if (UTF_16_LE_ENCODING_NAME.equals(encoding)) {
			out.write(UTF_16_LE_FIRST_BYTE);
			out.write(UTF_16_LE_SECOND_BYTE);
			out.flush();
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using " + UTF_16_LE_ENCODING_NAME);
			return new OutputStreamWriter(new EncodingCatcherOutputStream(out, UTF_16_LE_ENCODING_NAME), UTF_16_LE_ENCODING_NAME);
		}
		
		//	write byte order mark for UTF-16 big endian & return respective writer
		else if (UTF_16_BE_ENCODING_NAME.equals(encoding)) {
			out.write(UTF_16_BE_FIRST_BYTE);
			out.write(UTF_16_BE_SECOND_BYTE);
			out.flush();
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using " + UTF_16_BE_ENCODING_NAME);
			return new OutputStreamWriter(new EncodingCatcherOutputStream(out, UTF_16_BE_ENCODING_NAME), UTF_16_BE_ENCODING_NAME);
		}
		
		//	use specified encoding TODOne figure out if this makes sense ==> DOES MAKE SENSE WITH ACCEPTING ONLY UTF-8 AND UTF-16 by default
		else if (encoding != null) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using custom specified (" + encoding + ")");
			return new OutputStreamWriter(new EncodingCatcherOutputStream(out, encoding), encoding);
		}
		
		//	check format default encoding
		String defaultEncoding = this.getFormatDefaultEncodingName();
		if (defaultEncoding != null) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using format default (" + defaultEncoding + ")");
			return this.getOutputStreamWriter(out, defaultEncoding);
		}
		
		//	check system default encoding
		else if (defaultEncodingName != null) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using system default (" + defaultEncodingName + ")");
			return this.getOutputStreamWriter(out, defaultEncodingName);
		}
		
		//	check platform default encoding
		else if (platformDefaultEncodingName != null) {
			if (DEBUG_CHAR_ENCODING) System.out.println("  - using platform default (" + platformDefaultEncodingName + ")");
			return this.getOutputStreamWriter(out, platformDefaultEncodingName);
		}
		
		//	last resort
		else return new OutputStreamWriter(out);
	}
	
	private static class EncodingCatcherOutputStream extends FilterOutputStream {
		private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		private byte[] encodingBytes;
		EncodingCatcherOutputStream(OutputStream out, String encoding) {
			super(out);
			this.encodingBytes = new byte[encoding.length()];
			for (int b = 0; b < encoding.length(); b++)
				this.encodingBytes[b] = ((byte) encoding.charAt(b));
		}
		public void close() throws IOException {
			this.checkEncoding(true);
			super.close();
		}
		public void flush() throws IOException {
			this.checkEncoding(true);
			super.flush();
		}
		public void write(byte[] b, int off, int len) throws IOException {
			if (this.buffer == null)
				super.write(b, off, len);
			else {
				this.buffer.write(b, off, len);
				this.checkEncoding(false);
			}
		}
		public void write(int b) throws IOException {
			if (this.buffer == null)
				super.write(b);
			else {
				this.buffer.write(b);
				this.checkEncoding(false);
			}
		}
		private final void checkEncoding(boolean force) throws IOException {
			if (this.buffer == null)
				return;
			
			byte[] buffer = this.buffer.toByteArray();
			force = (force || (buffer.length > maxBufferLevel));
			
			int gxesIndex = indexOf(buffer, genericXmlEncodingStart, 0);
			int xesIndex = indexOf(buffer, xmlEncodingStart, 0);
			int hesIndex = indexOf(buffer, htmlEncodingStart, 0);
			int essIndex = -1;
			int esIndex = -1;
			
			if (gxesIndex != -1) {
				essIndex = indexOf(buffer, genericXmlEncodingSpecifyer, gxesIndex);
				esIndex = essIndex + genericXmlEncodingSpecifyer.length;
			}
			else if (xesIndex != -1) {
				essIndex = indexOf(buffer, xmlEncodingSpecifyer, xesIndex);
				esIndex = essIndex + xmlEncodingSpecifyer.length;
			}
			else if (hesIndex != -1) {
				essIndex = indexOf(buffer, htmlEncodingSpecifyer, hesIndex);
				esIndex = essIndex + htmlEncodingSpecifyer.length;
			}
			
			if (essIndex == -1) {
				if (force) {
					this.buffer = null;
					this.write(buffer);
					if (DEBUG_CHAR_ENCODING) System.out.println("EncodingChecker: encoding not detected");
				}
				return;
			}
			
			int eeIndex = indexOf(buffer, encodingTerminator, esIndex);
			if (eeIndex == -1) {
				if (force) {
					this.buffer = null;
					this.write(buffer);
					if (DEBUG_CHAR_ENCODING) System.out.println("EncodingChecker: encoding not detected");
				}
				return;
			}
			
			if (((eeIndex - esIndex) == this.encodingBytes.length) && startsWith(buffer, this.encodingBytes, esIndex)) {
				this.buffer = null;
				this.write(buffer);
				if (DEBUG_CHAR_ENCODING) System.out.println("EncodingChecker: encoding OK");
			}
			else {
				this.buffer = null;
				this.write(buffer, 0, esIndex);
				this.write(this.encodingBytes);
				this.write(buffer, eeIndex, (buffer.length - eeIndex));
				if (DEBUG_CHAR_ENCODING) System.out.println("EncodingChecker: encoding corrected");
			}
			
			//	catch the following:
			//	<html><head><meta http-equiv="content-type" content="text/html; charset=UTF-8">
			//	<?xml version="1.0" encoding="UTF-8"?>
			//	<document ENCODING="UTF-8">
		}
		
		private static boolean startsWith(byte[] bytes, byte[] prefix, int start) {
			if (prefix.length == 0)
				return true;
			if (((bytes.length - start) < prefix.length))
				return false;
			for (int b = 0; b < prefix.length; b++) {
				if (bytes[start+b] != prefix[b])
					return false;
			}
			return true;
		}
		
		private static int indexOf(byte[] bytes, byte[] infix, int start) {
			if (infix.length == 0)
				return start;
			if (((bytes.length - start) < infix.length))
				return -1;
			for (int b = start; b < bytes.length; b++) {
				if ((bytes[b] == infix[0]) && startsWith(bytes, infix, b))
					return b;
			}
			return -1;
		}
		
		private static final int maxBufferLevel = 8192;
		
		private static final byte[] genericXmlEncodingStart = {
			(byte) '<',
			(byte) 'd',
			(byte) 'o',
			(byte) 'c',
			(byte) 'u',
			(byte) 'm',
			(byte) 'e',
			(byte) 'n',
			(byte) 't',
		};
		private static final byte[] genericXmlEncodingSpecifyer = {
			(byte) 'E',
			(byte) 'N',
			(byte) 'C',
			(byte) 'O',
			(byte) 'D',
			(byte) 'I',
			(byte) 'N',
			(byte) 'G',
			(byte) '=',
			(byte) '"',
		};
		
		private static final byte[] xmlEncodingStart = {
			(byte) '<',
			(byte) '?',
			(byte) 'x',
			(byte) 'm',
			(byte) 'l',
		};
		private static final byte[] xmlEncodingSpecifyer = {
			(byte) 'e',
			(byte) 'n',
			(byte) 'c',
			(byte) 'o',
			(byte) 'd',
			(byte) 'i',
			(byte) 'n',
			(byte) 'g',
			(byte) '=',
			(byte) '"',
		};
		
		private static final byte[] htmlEncodingStart = {
			(byte) '<',
			(byte) 'h',
			(byte) 't',
			(byte) 'm',
			(byte) 'l',
		};
		private static final byte[] htmlEncodingSpecifyer = {
			(byte) 'c',
			(byte) 'h',
			(byte) 'a',
			(byte) 'r',
			(byte) 's',
			(byte) 'e',
			(byte) 't',
			(byte) '=',
		};
		
		private static final byte[] encodingTerminator = {(byte) '"'};
	}
	
	/**
	 * Retrieve the name of the default character encoding for this document
	 * format. Note: This mehtod returns the same as getDefaultEncodingName() by
	 * default. Subclasses are welcome to override this method as needed.
	 * @return the name of the default character encoding for this document
	 *         format
	 */
	public String getFormatDefaultEncodingName() {
		return defaultEncodingName;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	public boolean accept(File file) {
		return ((file != null) && (file.isDirectory() || this.accept(file.getName())));
	}
	
	/**
	 * Test if the filter accepts a given file name. This method is a
	 * replacement for accept(File) intended to prevent problems in
	 * circumstances where File objects cannot be used for security reasons, eg
	 * in an applet.
	 * @param fileName the file name to test
	 * @return true is the filter accepts a file with the given name.
	 */
	public abstract boolean accept(String fileName);
	
	/**	@return	the default file extension for documents saved in this DocumentFormat (a file having this extension should be accepted by the FileFilter.accept() method) 
	 */
	public abstract String getDefaultSaveFileExtension();
	
	/** @see java.lang.Object#toString()
	 */
	public String toString() {
		//	return description so the DocumentFormat is displayed in FileChooser and JComboBox in the same way
		return this.getDescription();
	}
	
	/** @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (o instanceof DocumentFormat) return this.equals((DocumentFormat) o);
		else return super.equals(o);
	}
	
	/** @see java.lang.Object#equals(java.lang.Object)
	 */
	public abstract boolean equals(DocumentFormat format);
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.Resource#getTypeLabel()
	 */
	public String getTypeLabel() {
		return "Document Format";
	}
//	
//	//	for test purposes only
//	public static void main(String[] args) throws Exception {
//		initEncodingNames();
////		defaultEncodingName = "UTF-8";
//		TestDocumentFormat tdf = new TestDocumentFormat();
//		MutableAnnotation doc = tdf.loadDocument(new ByteArrayInputStream("<html><head><meta http-equiv=\"content-type\" content=\"text/html; charset=Cp1252\"/><script type=\"text/javascript\">var x = 0;</script></head><body>test</body></html>".getBytes()));
////		doc.removeAttribute(ENCODING_ATTRIBUTE);
//		de.uka.ipd.idaho.gamta.util.AnnotationFilter.removeAnnotations(doc, "html");
//		tdf.saveDocument(doc, System.out);
//	}
//	private static class TestDocumentFormat extends DocumentFormat {
//		public String getProviderClassName() {
//			return "";
//		}
//		public MutableAnnotation loadDocument(Reader source) throws IOException {
//			return SgmlDocumentReader.readDocument(source);
//		}
//		public String getDefaultSaveFileExtension() {
//			return null;
//		}
//		public String getFormatDefaultEncodingName() {
//			return "windows-1252";
//		}
//		public boolean saveDocument(DocumentEditor data, QueriableAnnotation doc, Writer out) throws IOException {
//			AnnotationUtils.writeXML(doc, out);
//			return true;
//		}
//		public boolean saveDocument(DocumentEditor data, Writer out) throws IOException {
//			AnnotationUtils.writeXML(data.getContent(), out);
//			return true;
//		}
//		public boolean saveDocument(QueriableAnnotation data, Writer out) throws IOException {
////			out.write("<?xml version=\"1.0\" encoding=\"" + data.getAttribute(ENCODING_ATTRIBUTE) + "\"?>");
////			out.write((int) '\n');
//			AnnotationUtils.writeXML(data, out);
//			return true;
//		}
//		public String getName() {
//			return "test";
//		}
//		public String getDescription() {
//			return "test";
//		}
//		public boolean equals(DocumentFormat format) {
//			return (format instanceof TestDocumentFormat);
//		}
//		public boolean accept(String fileName) {
//			return false;
//		}
//	}
}
