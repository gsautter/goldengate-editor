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
package de.uka.ipd.idaho.goldenGate;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.uka.ipd.idaho.easyIO.help.DynamicHelpChapter;
import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.AnnotationListener;
import de.uka.ipd.idaho.gamta.AnnotationUtils;
import de.uka.ipd.idaho.gamta.AttributeUtils;
import de.uka.ipd.idaho.gamta.CharSequenceListener;
import de.uka.ipd.idaho.gamta.DocumentRoot;
import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.gamta.MutableTokenSequence;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.Token;
import de.uka.ipd.idaho.gamta.MutableCharSequence.CharSequenceEvent;
import de.uka.ipd.idaho.gamta.util.ImmutableAnnotation;
import de.uka.ipd.idaho.gamta.util.swing.AnnotationDisplayDialog;
import de.uka.ipd.idaho.goldenGate.observers.AnnotationObserver;
import de.uka.ipd.idaho.goldenGate.observers.DisplayObserver;
import de.uka.ipd.idaho.goldenGate.observers.DocumentObserver;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilter;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSource;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSourceManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentEditorExtension;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentFormatProvider;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentSaveOperation;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentSaver;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer;
import de.uka.ipd.idaho.goldenGate.plugins.MonitorableDocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.Resource;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceSplashScreen;
import de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel;
import de.uka.ipd.idaho.goldenGate.util.AttributeEditorDialog;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.EditFontsButton;
import de.uka.ipd.idaho.goldenGate.util.FontEditable;
import de.uka.ipd.idaho.goldenGate.util.FontEditorPanel;
import de.uka.ipd.idaho.stringUtils.StringIndex;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Main editor for an individual document, managing layout, annotation nesting,
 * undo management, and application of DocumentProcessors and AnnotationSources
 * 
 * @author sautter
 */
public class DocumentEditor extends JPanel implements FontEditable, GoldenGateConstants {
	
	private static final String TEXT_FONT_NAME_SETTING = "TEXT_FONT_NAME";
	private static final String TEXT_FONT_SIZE_SETTING = "TEXT_FONT_SIZE";
	private static final String TEXT_FONT_COLOR_RED = "TEXT_FONT_RED";
	private static final String TEXT_FONT_COLOR_GREEN = "TEXT_FONT_GREEN";
	private static final String TEXT_FONT_COLOR_BLUE = "TEXT_FONT_BLUE";
	
	private static final String TAG_FONT_NAME_SETTING = "TAG_FONT_NAME";
	private static final String TAG_FONT_SIZE_SETTING = "TAG_FONT_SIZE";
	private static final String TAG_FONT_COLOR_RED = "TAG_FONT_RED";
	private static final String TAG_FONT_COLOR_GREEN = "TAG_FONT_GREEN";
	private static final String TAG_FONT_COLOR_BLUE = "TAG_FONT_BLUE";
	
	private static final String ANNOTATION_NESTING_ORDER_SETTING = "ANO";
	
	private static final String TARGET_ANNOTATION_ID = "TARGET_ANNOTATION_ID";
	private static final String UNDO_ACTION_NAME = "UNDO_ACTION_NAME";
	
	private static String TEXT_FONT_NAME = "Verdana";
	private static int TEXT_FONT_SIZE = 12;
	private static Color TEXT_FONT_COLOR = Color.BLACK;
	
	private static String TAG_FONT_NAME = "Courier New";
	private static int TAG_FONT_SIZE = 12;
	private static Color TAG_FONT_COLOR = Color.BLUE;
	
	/** @return the default font color for displaying XML tags
	 */
	public static Color getDefaultTagFontColor() {
		return TAG_FONT_COLOR;
	}
	
	/** set the default font color for displaying XML tags
	 * @param	tagFontColor	the new default font color for displaying XML tags
	 */
	public static void setDefaultTagFontColor(Color tagFontColor) {
		if (tagFontColor != null) TAG_FONT_COLOR = tagFontColor;
	}
	
	/** @return the name of the default font for displaying XML tags
	 */
	public static String getDefaultTagFontName() {
		return TAG_FONT_NAME;
	}
	
	/** set the default font for displaying XML tags
	 * @param	tagFontName		the name of the new default font for displaying XML tags
	 */
	public static void setDefaultTagFontName(String tagFontName) {
		if (tagFontName != null) TAG_FONT_NAME = tagFontName;
	}
	
	/** @return the default font size for displaying XML tags
	 */
	public static int getDefaultTagFontSize() {
		return TAG_FONT_SIZE;
	}
	
	/** set the default font size for displaying XML tags
	 * @param	tagFontSize		the new default font size for displaying XML tags
	 */
	public static void setDefaultTagFontSize(int tagFontSize) {
		if (tagFontSize > 0) TAG_FONT_SIZE = tagFontSize;
	}
	
	/** @return the default font color for displaying document text
	 */
	public static Color getDefaultTextFontColor() {
		return TEXT_FONT_COLOR;
	}
	
	/** set the default font color for displaying document text
	 * @param	textFontColor	the new default font color for displaying document text
	 */
	public static void setDefaultTextFontColor(Color textFontColor) {
		if (textFontColor != null) TEXT_FONT_COLOR = textFontColor;
	}
	
	/** @return the name of the default font for displaying document text
	 */
	public static String getDefaultTextFontName() {
		return TEXT_FONT_NAME;
	}
	
	/** set the default font color for displaying document text
	 * @param	textFontName	the name of the new default font for displaying document text
	 */
	public static void setDefaultTextFontName(String textFontName) {
		if (textFontName != null) TEXT_FONT_NAME = textFontName;
	}
	
	/** @return the default font size for displaying document text
	 */
	public static int getDefaultTextFontSize() {
		return TEXT_FONT_SIZE;
	}
	
	/** set the default font size for displaying document text
	 * @param	textFontSize	the new default font size for displaying document text
	 */
	public static void setDefaultTextFontSize(int textFontSize) {
		if (textFontSize > 0) TEXT_FONT_SIZE = textFontSize;
	}
	
	private static DocumentEditor activeInstance = null;
	private static LinkedList activeInstanceStack = new LinkedList();
	
	/** @return the font color for displaying XML tags
	 */
	public static Color getActiveTagFontColor() {
		return ((activeInstance == null) ? TAG_FONT_COLOR : activeInstance.getTagFontColor());
	}
	
	/** @return the name of the font for displaying XML tags
	 */
	public static String getActiveTagFontName() {
		return ((activeInstance == null) ? TAG_FONT_NAME : activeInstance.getTagFontName());
	}
	
	/** @return the font size for displaying XML tags
	 */
	public static int getActiveTagFontSize() {
		return ((activeInstance == null) ? TAG_FONT_SIZE : activeInstance.getTagFontSize());
	}
	
	/** @return the font color for displaying document text
	 */
	public static Color getActiveTextFontColor() {
		return ((activeInstance == null) ? TEXT_FONT_COLOR : activeInstance.getTextFontColor());
	}
	
	/** @return the name of the font for displaying document text
	 */
	public static String getActiveTextFontName() {
		return ((activeInstance == null) ? TEXT_FONT_NAME : activeInstance.getTextFontName());
	}
	
	/** @return the font size for displaying document text
	 */
	public static int getActiveTextFontSize() {
		return ((activeInstance == null) ? TEXT_FONT_SIZE : activeInstance.getTextFontSize());
	}
	
	/**	@return all annotation types that are selected for highlighting the value
	 */
	public static String[] getActiveHighlightAnnotationTypes() {
		return ((activeInstance == null) ? new String[0] : activeInstance.getHighlightAnnotationTypes());
	}
	
	/**	@return all annotation types that are selected for showing tags
	 */
	public static String[] getActiveTaggedAnnotationTypes() {
		return ((activeInstance == null) ? new String[0] : activeInstance.getTaggedAnnotationTypes());
	}
	
	/**	check the color of the tags and value highlights for some annotation type
	 * @param 	annotationType	the Annotation type to get the color for
	 * @return the color used highlights and tags of annotations with the specified type
	 */
	public static Color getActiveAnnotationColor(String annotationType) {
		return ((activeInstance == null) ? AnnotationEditorPanel.getDefaultAnnotationColor(annotationType) : activeInstance.getAnnotationColor(annotationType));
	}
	
	/**	@return	a set containing the Annotation types currently selected in the AnnotationEditor
	 */
	public static Set getActiveSelectedTags() {
		return ((activeInstance == null) ? new HashSet() : activeInstance.getSelectedTags());
	}
	
	/** @return all Annotation types present in the content document of this DocumentEditor	
	 * @see de.uka.ipd.idaho.gamta.QueriableAnnotation#getAnnotationTypes()
	 */
	public static String[] getActiveAnnotationTypes() {
		return ((activeInstance == null) ? new String[0] : activeInstance.getAnnotationTypes());
	}
	
	/**	@return	all AnnotationFilters available for this DocumentEditor
	 */
	public static AnnotationFilter[] getActiveAnnotationFilters() {
		return ((activeInstance == null) ? new AnnotationFilter[0] : activeInstance.getAnnotationFilters());
	}
	
	/**
	 * Retrieve the annotation types that are used for marking up a document's
	 * structure, rather than semantical details. Which annotation types are
	 * listed here is subject to configuration of AnnotationEditor.
	 * @return an array holding the annotation types defined to be structural
	 */
	public static String[] getStructuralAnnotationTypes() {
		return AnnotationEditorPanel.getStructuralTypes();
	}
	
	/**	obtain suggestions for annotation types
	 * @return an array of annotation type suggestions, sorted lexicographically
	 */
	public static String[] getAnnotationTypeSuggestions() {
		return AnnotationEditorPanel.getAnnotationTypeSuggestions();
	}
	
	/**	obtain suggestions for attributes of annotations of a specific type
	 * @param	type	the annotation type (specifying null will return all attribute suggestions for all annotation types)
	 * @return an array of suggestions for attributes of annotations of the specified type, sorted lexicographically
	 */
	public static String[] getAnnotationAttributeSuggestions(String type) {
		return AnnotationEditorPanel.getAnnotationAttributeSuggestions(type);
	}
	
	/**	obtain suggestions for the value of an attribute of an annotation of a specific type
	 * @param	type		the annotation type (specifying null will return all value suggestions for the specified attribute, for all annotation types)
	 * @param	attribute	the attribute to obtain value suggestions for
	 * @return an array of suggestions for the value of the specified attribute in the scope of annotations of the specified type, sorted lexicographically
	 */
	public static String[] getAttributeValueSuggestions(String type, String attribute) {
		return AnnotationEditorPanel.getAttributeValueSuggestions(type, attribute);
	}
	
	static void init(Settings settings) {
		
		setDefaultTextFontName(settings.getSetting(TEXT_FONT_NAME_SETTING));
		try {
			setDefaultTextFontSize(Integer.parseInt(settings.getSetting(TEXT_FONT_SIZE_SETTING, "0")));
		} catch (Exception e) {}
		try {
			int r = Integer.parseInt(settings.getSetting(TEXT_FONT_COLOR_RED, ""));
			int g = Integer.parseInt(settings.getSetting(TEXT_FONT_COLOR_GREEN, ""));
			int b = Integer.parseInt(settings.getSetting(TEXT_FONT_COLOR_BLUE, ""));
			Color c = new Color(r, g, b);
			setDefaultTextFontColor(c);
		} catch (Exception e) {}
		
		setDefaultTagFontName(settings.getSetting(TAG_FONT_NAME_SETTING));
		try {
			setDefaultTagFontSize(Integer.parseInt(settings.getSetting(TAG_FONT_SIZE_SETTING, "0")));
		} catch (Exception e) {}
		try {
			int r = Integer.parseInt(settings.getSetting(TAG_FONT_COLOR_RED, ""));
			int g = Integer.parseInt(settings.getSetting(TAG_FONT_COLOR_GREEN, ""));
			int b = Integer.parseInt(settings.getSetting(TAG_FONT_COLOR_BLUE, ""));
			Color c = new Color(r, g, b);
			setDefaultTagFontColor(c);
		} catch (Exception e) {}
		
		Gamta.setAnnotationNestingOrder(settings.getSetting(ANNOTATION_NESTING_ORDER_SETTING));
		
		layoutSettings = settings.getSubset(LAYOUT_SETTINGS_PREFIX);
		
		try {
			undoHistoryMaxItemCount = Integer.parseInt(settings.getSetting(UNDO_HISTORY_MAX_ITEM_COUNT, ("" + undoHistoryMaxItemCount)));
			undoHistoryMaxItemAge = (Integer.parseInt(settings.getSetting(UNDO_HISTORY_MAX_ITEM_AGE, ("" + undoHistoryMaxItemAge))) * 1000);
			undoHistorySavedMaxItemCount = Integer.parseInt(settings.getSetting(UNDO_HISTORY_SAVED_MAX_ITEM_AGE, ("" + undoHistorySavedMaxItemCount)));
			undoHistorySavedMaxItemCount = (Integer.parseInt(settings.getSetting(UNDO_HISTORY_SAVED_MAX_ITEM_AGE, ("" + undoHistorySavedMaxItemAge))) * 1000);
		} catch (Exception e) {}
	}
	
	static void storeSettings(Settings settings) {
		settings.setSetting(TEXT_FONT_NAME_SETTING, getDefaultTextFontName());
		settings.setSetting(TEXT_FONT_SIZE_SETTING, ("" + getDefaultTextFontSize()));
		
		Color textColor = getDefaultTextFontColor();
		settings.setSetting(TEXT_FONT_COLOR_RED, ("" + textColor.getRed()));
		settings.setSetting(TEXT_FONT_COLOR_GREEN, ("" + textColor.getGreen()));
		settings.setSetting(TEXT_FONT_COLOR_BLUE, ("" + textColor.getBlue()));
		
		settings.setSetting(TAG_FONT_NAME_SETTING, getDefaultTagFontName());
		settings.setSetting(TAG_FONT_SIZE_SETTING, ("" + getDefaultTextFontSize()));
		
		Color tagColor = getDefaultTagFontColor();
		settings.setSetting(TAG_FONT_COLOR_RED, ("" + tagColor.getRed()));
		settings.setSetting(TAG_FONT_COLOR_GREEN, ("" + tagColor.getGreen()));
		settings.setSetting(TAG_FONT_COLOR_BLUE, ("" + tagColor.getBlue()));
		
		String annotationNestingOrder = Gamta.getAnnotationNestingOrder();
		if (!DocumentRoot.DEFAULT_ANNOTATION_NESTING_ORDER.equals(annotationNestingOrder))
			settings.setSetting(ANNOTATION_NESTING_ORDER_SETTING, annotationNestingOrder);
		
		settings.getSubset(LAYOUT_SETTINGS_PREFIX).setSettings(layoutSettings);
		
		settings.setSetting(UNDO_HISTORY_MAX_ITEM_COUNT, ("" + undoHistoryMaxItemCount));
		settings.setSetting(UNDO_HISTORY_MAX_ITEM_AGE, ("" + (undoHistoryMaxItemAge / 1000)));
		settings.setSetting(UNDO_HISTORY_SAVED_MAX_ITEM_COUNT, ("" + undoHistorySavedMaxItemCount));
		settings.setSetting(UNDO_HISTORY_SAVED_MAX_ITEM_AGE, ("" + (undoHistorySavedMaxItemAge / 1000)));
	}
	
	/**	obtain a panel for editing general document format settings
	 * @param	host	the GoldenGATE main instance (necessary due to static context)
	 * @return	a settings panel for editing the default settings in the GoldenGATE configuration
	 */
	public static SettingsPanel getSettingsPanel(GoldenGATE host) {
		return new DocumentEditorSettingsPanel(host);
	}
	
	private static class DocumentEditorSettingsPanel extends SettingsPanel {
		private SettingsPanel annotationEditorSettingsPanel;
		
		private JTextArea structuralTypes = new JTextArea();
		
		private JTextArea annotationNestingOrder = new JTextArea();
		
		private FontEditorPanel textFont = new FontEditorPanel("Text Font", getDefaultTextFontName(), getDefaultTextFontSize(), getDefaultTextFontColor());
		private FontEditorPanel tagFont = new FontEditorPanel("Tag Font ", getDefaultTagFontName(), getDefaultTagFontSize(), getDefaultTagFontColor());
		
		private SplitLayoutPanel layoutRoot;
		
		private DocumentEditorSettingsPanel(GoldenGATE host) {
			super("Document Editor", "Configure layout and behavior of document editor here.");
			
			this.annotationEditorSettingsPanel = AnnotationEditorPanel.getSettingsPanel(host);
			
			StringVector structuralTypeStringBuilder = new StringVector();
			structuralTypeStringBuilder.addContentIgnoreDuplicates(AnnotationEditorPanel.getStructuralTypes());
			structuralTypeStringBuilder.sortLexicographically(false, false);
			this.structuralTypes.setText(structuralTypeStringBuilder.concatStrings("\n"));
			this.structuralTypes.setPreferredSize(new Dimension(200, 200));
			JScrollPane structuralTypesBox = new JScrollPane(this.structuralTypes);
			structuralTypesBox.setPreferredSize(new Dimension(200, 200));
			
			StringVector annotationNestingOrderStringBuilder = new StringVector();
			annotationNestingOrderStringBuilder.parseAndAddElements(Gamta.getAnnotationNestingOrder(), " ");
			annotationNestingOrderStringBuilder.removeAll("");
			this.annotationNestingOrder.setText(annotationNestingOrderStringBuilder.concatStrings("\n"));
			this.annotationNestingOrder.setPreferredSize(new Dimension(200, 200));
			JScrollPane annotationNestingOrderBox = new JScrollPane(this.annotationNestingOrder);
			annotationNestingOrderBox.setPreferredSize(new Dimension(200, 200));
			
			JPanel settingsPanel = new JPanel(new GridBagLayout(), true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridy = 0;
			
			gbc.gridwidth = 4;
			settingsPanel.add(this.textFont, gbc.clone());
			
			gbc.gridy++;
			settingsPanel.add(this.tagFont, gbc.clone());
			gbc.gridy++;
			
			gbc.gridwidth = 2;
			settingsPanel.add(new JLabel("Structural Annotation Types (one per line)", JLabel.CENTER), gbc.clone());
			gbc.gridx = 2;
			settingsPanel.add(new JLabel("Annotation Type Nesting Order (one per line)", JLabel.CENTER), gbc.clone());
			gbc.gridy++;
			
			gbc.weighty = 1;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			settingsPanel.add(structuralTypesBox, gbc.clone());
			gbc.gridx = 2;
			settingsPanel.add(annotationNestingOrderBox, gbc.clone());
			gbc.gridy++;
			
			gbc.weighty = 0;
			gbc.gridx = 0;
			gbc.gridwidth = 4;
			settingsPanel.add(this.annotationEditorSettingsPanel, gbc.clone());
			
			StringVector components = new StringVector();
			components.addContent(FIX_COMPONENTS);
			DocumentEditorExtension[] dees = host.getDocumentEditorExtensions();
			for (int e = 0; e < dees.length; e++)
				components.addElement(dees[e].getPluginName());
			
			String layoutRootContent = layoutSettings.getSetting(MAIN_COMPONENT_NAME, ANNOTATION_EDITOR);
			if (layoutRootContent.startsWith(SPLIT_PREFIX)) {
				this.layoutRoot = new SplitLayoutPanel(null, components.toStringArray(), null);
				this.renderSplitForEdit(layoutSettings, layoutRootContent, this.layoutRoot);
			}
			else this.layoutRoot = new SplitLayoutPanel(null, components.toStringArray(), layoutRootContent);
			
			JTabbedPane tabs = new JTabbedPane();
			tabs.addTab("Settings", null, settingsPanel, "Edit the behavior of the document editor here.");
			tabs.addTab("Layout", null, this.layoutRoot, "Edit the layout and extensions of the document editor here.");
			
			this.setLayout(new BorderLayout());
			this.add(tabs, BorderLayout.CENTER);
		}
		
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			this.annotationEditorSettingsPanel.commitChanges();
			
			if (this.textFont.isDirty()) {
				setDefaultTextFontName(this.textFont.getFontName());
				setDefaultTextFontSize(this.textFont.getFontSize());
				setDefaultTextFontColor(this.textFont.getFontColor());
			}
			
			if (this.tagFont.isDirty()) {
				setDefaultTagFontName(this.tagFont.getFontName());
				setDefaultTagFontSize(this.tagFont.getFontSize());
				setDefaultTagFontColor(this.tagFont.getFontColor());
			}
			
			StringVector structuralTypeLineParser = new StringVector();
			structuralTypeLineParser.parseAndAddElements(this.structuralTypes.getText(), "\n");
			StringVector structuralTypeStringParser = new StringVector();
			for (int l = 0; l < structuralTypeLineParser.size(); l++)
				structuralTypeStringParser.parseAndAddElements(structuralTypeLineParser.get(l), " ");
			structuralTypeStringParser.removeAll("");
			AnnotationEditorPanel.setStructuralTypes(structuralTypeStringParser.toStringArray());
			
			StringVector annotationNestingOrderLineParser = new StringVector();
			annotationNestingOrderLineParser.parseAndAddElements(this.annotationNestingOrder.getText(), "\n");
			annotationNestingOrderLineParser.removeAll("");
			Gamta.setAnnotationNestingOrder(annotationNestingOrderLineParser.concatStrings(" "));
			
			layoutSettings.clear();
			if (layoutRoot.split == null)
				layoutSettings.setSetting(MAIN_COMPONENT_NAME, layoutRoot.selector.getSelectedItem().toString());
			else {
				int splitNumber = 1;
				String splitName = (SPLIT_PREFIX + splitNumber);
				layoutSettings.setSetting(MAIN_COMPONENT_NAME, splitName);
				splitNumber = layoutRoot.writeSplitSettings(layoutSettings, layoutSettings.getSubset(splitName), splitNumber);
			}
			layoutInstances();
		}
		
		private static final String VOID = "<Select Content>";
		private static final String LR_SPLIT = "LR-Split";
		private static final String TB_SPLIT = "TB-Split";
		
		private static final String[] FIX_COMPONENTS = {VOID, LR_SPLIT, TB_SPLIT, ANNOTATION_EDITOR, DISPLAY_CONTROL, CUSTOM_FUNCTIONS, RECENT_ACTIONS};
		
		private class SplitLayoutPanel extends JPanel {
			private SplitLayoutPanel parent;
			
			private JComboBox selector = new JComboBox();
			private String[] components;
			private JLabel label = new JLabel("", JLabel.CENTER);
			
			private SplitLayoutPanel topOrLeft;
			private SplitLayoutPanel bottomOrRight;
			private JSplitPane split;
			private int splitType;
			
			private SplitLayoutPanel(SplitLayoutPanel parent, String[] components, String selected) {
				super(new BorderLayout(), true);
				this.parent = parent;
				this.components = components;
				
				if (this.parent != null)
					this.setBorder(
							BorderFactory.createCompoundBorder(
									BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2), 
									BorderFactory.createLineBorder(Color.DARK_GRAY, 3)
							)
					);
				
				this.selector = new JComboBox(components);
				if (selected != null)
					this.selector.setSelectedItem(selected);
				this.selector.setEditable(false);
				this.selector.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent ie) {
						Object selected = selector.getSelectedItem();
						
						if (LR_SPLIT.equals(selected))
							split(JSplitPane.HORIZONTAL_SPLIT);
						
						else if (TB_SPLIT.equals(selected))
							split(JSplitPane.VERTICAL_SPLIT);
						
						else unSplit();
					}
				});
				this.add(this.selector, BorderLayout.NORTH);
				
				this.label.setText(this.selector.getSelectedItem().toString());
				this.add(this.label, BorderLayout.CENTER);
			}
			
			private void split(int mode) {
				System.out.println("Splitting ...");
				if (this.split != null)
					this.remove(this.split);
				this.remove(this.label);
				
				if (this.topOrLeft == null)
					this.topOrLeft = new SplitLayoutPanel(this, this.components, null);
				if (this.bottomOrRight == null)
					this.bottomOrRight = new SplitLayoutPanel(this, this.components, null);
				this.splitType = mode;
				
				this.split = new JSplitPane(this.splitType, this.topOrLeft, this.bottomOrRight);
				this.add(this.split, BorderLayout.CENTER);
				
				this.validate();
				this.split.setDividerLocation(0.5);
			}
			
			private void unSplit() {
				System.out.println("Un-Splitting ...");
				if (this.split != null)
					this.remove(this.split);
				this.remove(this.label);
				
				this.topOrLeft = null;
				this.bottomOrRight = null;
				this.split = null;
				
				this.label.setText(this.selector.getSelectedItem().toString());
				this.add(this.label, BorderLayout.CENTER);
				
				this.validate();
				this.repaint();
			}
			
			private int writeSplitSettings(Settings set, Settings splitSet, int splitNumber) {
				if (this.split != null) {
					splitSet.setSetting(SPLIT_TYPE_NAME, ((this.splitType == JSplitPane.HORIZONTAL_SPLIT) ? LEFT_RIGHT_SPLIT_TYPE : TOP_BOTTOM_SPLIT_TYPE));
					
					double resizeWeight = (((double) this.split.getDividerLocation()) / ((this.splitType == JSplitPane.HORIZONTAL_SPLIT) ? this.split.getWidth() : this.split.getHeight()));
					if ((resizeWeight < 0.475) || (resizeWeight > 0.525))
						splitSet.setSetting(SPLIT_RATIO_NAME, ("" + ((int) (resizeWeight * 100))));
					
					if (this.topOrLeft.split == null)
						splitSet.setSetting(TOP_OR_LEFT_COMPONENT_NAME, this.topOrLeft.selector.getSelectedItem().toString());
					
					else {
						splitNumber++;
						String splitName = (SPLIT_PREFIX + splitNumber);
						splitSet.setSetting(TOP_OR_LEFT_COMPONENT_NAME, splitName);
						splitNumber = this.topOrLeft.writeSplitSettings(set, set.getSubset(splitName), splitNumber);
					}
					
					if (this.bottomOrRight.split == null)
						splitSet.setSetting(BOTTOM_OR_RIGHT_COMPONENT_NAME, this.bottomOrRight.selector.getSelectedItem().toString());
					
					else {
						splitNumber++;
						String splitName = (SPLIT_PREFIX + splitNumber);
						splitSet.setSetting(BOTTOM_OR_RIGHT_COMPONENT_NAME, splitName);
						splitNumber = this.bottomOrRight.writeSplitSettings(set, set.getSubset(splitName), splitNumber);
					}
				}
				return splitNumber;
			}
		}
		
		private void renderSplitForEdit(Settings set, String splitName, SplitLayoutPanel split) {
			Settings splitSet = set.getSubset(splitName);
			
			String type = splitSet.getSetting(SPLIT_TYPE_NAME);
			if (LEFT_RIGHT_SPLIT_TYPE.equals(type) || TOP_BOTTOM_SPLIT_TYPE.equals(type)) {
				split.selector.setSelectedItem(LEFT_RIGHT_SPLIT_TYPE.equals(type) ? LR_SPLIT : TB_SPLIT);
				
				String tol = splitSet.getSetting(TOP_OR_LEFT_COMPONENT_NAME);
				if (tol.startsWith(SPLIT_PREFIX))
					renderSplitForEdit(set, tol, split.topOrLeft);
				else split.topOrLeft.selector.setSelectedItem(tol);
				
				String bor = splitSet.getSetting(BOTTOM_OR_RIGHT_COMPONENT_NAME);
				if (bor.startsWith(SPLIT_PREFIX))
					renderSplitForEdit(set, bor, split.bottomOrRight);
				else split.bottomOrRight.selector.setSelectedItem(bor);
				
				double resizeWeight = 0.5;
				try {
					resizeWeight = (((double) Integer.parseInt(splitSet.getSetting(SPLIT_RATIO_NAME, "50"))) / 100);
				} catch (NumberFormatException nfe) {}
				split.split.setResizeWeight(resizeWeight);
			}
			else split.add(new JLabel(("Split type error in " + splitName), JLabel.CENTER), BorderLayout.CENTER);
		}
	}
	
	private static Settings layoutSettings = new Settings();
	
	private static final String LAYOUT_SETTINGS_PREFIX = "LYT";
	
	private static final String MAIN_COMPONENT_NAME = "CONTENT";
	private static final String SPLIT_PREFIX = "SPLIT-";
	private static final String TOP_OR_LEFT_COMPONENT_NAME = "TOL";
	private static final String BOTTOM_OR_RIGHT_COMPONENT_NAME = "BOR";
	private static final String SPLIT_RATIO_NAME = "RATIO";
	private static final String SPLIT_TYPE_NAME = "TYPE";
	private static final String TOP_BOTTOM_SPLIT_TYPE = "TB";
	private static final String LEFT_RIGHT_SPLIT_TYPE = "LR";
	
	private static final String ANNOTATION_EDITOR = "Annotation Editor";
	private static final String DISPLAY_CONTROL = "Display Control";
	private static final String CUSTOM_FUNCTIONS = "Custom Functions";
	private static final String RECENT_ACTIONS = "Recent Actions";
	
	private static Vector instances = new Vector();
	
	private static void layoutInstances() {
		for (int i = 0; i < instances.size(); i++)
			((DocumentEditor) instances.get(i)).layoutPanels();
	}
	
	private static JMenuItem undoMenu = new JMenu("Undo");
	
	private static final int UNDO_MENU_SIZE = 10;
	
	private static final String UNDO_HISTORY_MAX_ITEM_COUNT = "UNDO_MAX_ITEM_COUNT";
	private static final String UNDO_HISTORY_MAX_ITEM_AGE = "UNDO_MAX_ITEM_AGE";
	private static final String UNDO_HISTORY_SAVED_MAX_ITEM_COUNT = "UNDO_SAVED_MAX_ITEM_COUNT";
	private static final String UNDO_HISTORY_SAVED_MAX_ITEM_AGE = "UNDO_SAVED_MAX_ITEM_AGE";
	
	private static final int DEFAULT_UNDO_HISTORY_MAX_ITEM_COUNT = 50;
	private static final int DEFAULT_UNDO_HISTORY_MAX_ITEM_AGE = (30 * 60 * 1000);
	private static final int DEFAULT_UNDO_HISTORY_SAVED_MAX_ITEM_COUNT = 10;
	private static final int DEFAULT_UNDO_HISTORY_SAVED_MAX_ITEM_AGE = (5 * 60 * 1000);
	
	private static int undoHistoryMaxItemCount = DEFAULT_UNDO_HISTORY_MAX_ITEM_COUNT; // TODO make this configurable
	private static int undoHistoryMaxItemAge = DEFAULT_UNDO_HISTORY_MAX_ITEM_AGE; // TODO make this configurable
	private static int undoHistorySavedMaxItemCount = DEFAULT_UNDO_HISTORY_SAVED_MAX_ITEM_COUNT; // TODO make this configurable
	private static int undoHistorySavedMaxItemAge = DEFAULT_UNDO_HISTORY_SAVED_MAX_ITEM_AGE; // TODO make this configurable
	
	/**
	 * Retrieve the menu items for triggering edit operations on DocumentEditor
	 * instances in the Edit menu of the main window. The menu items returned
	 * retrieve their target DocumentEditor instance from the specified
	 * InvokationTargetProvider, and thus can be used in a shared fashion in the
	 * main window. It is up to the invokation target provider to provide the
	 * target instance, most usually the DocumentEditor instance currently
	 * selected in a main window that allows for multiple documents being opened
	 * at the same time. If only one document can be opened at the same time,
	 * the target provider may simply return the respective DocumentEditor
	 * instance. This method should be used on startup to populate a menu, not
	 * for retrieving new menu items frequently.
	 * @param targetProvider the provider of the target DocumentEditor for the
	 *            functions returned
	 * @param allowSplit indicate whether or not to include the Split Document
	 *            option in the menu
	 * @return the menu items for triggering edit operations on DocumentEditor
	 *         instances in the Edit menu of the main window
	 */
	public static JMenuItem[] getEditMenuItems(final InvokationTargetProvider targetProvider, boolean allowSplit) {
//		undoMenu = new JMenu("Undo");
		undoMenu.setEnabled(false);
		undoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.undo();
			}
		});
		return getEditMenuItems(targetProvider, allowSplit, undoMenu);
	}
	private static JMenuItem[] getEditMenuItems(final InvokationTargetProvider targetProvider, boolean allowSplit, JMenuItem undoMenuItem) {
		JMenuItem mi;
		ArrayList collector = new ArrayList();
		
		collector.add(undoMenuItem);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Edit Document Attributes");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.editDocumentAttributes();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Edit Document Properties");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.editDocumentProperties();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Edit Selection");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.editSelection();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Find / Replace");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.doFindReplace();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Annotate");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.annotate();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Annotate All");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.annotateAll();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Merge Annotations");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.mergeAnnotations();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Split Annotation");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.splitAnnotation();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Rename Annotations");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.renameAnnotations();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Remove Annotations");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.removeAnnotations();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Delete Annotations");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.deleteAnnotations();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Rename Annotation Attribute");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.renameAnnotationAttribute();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Modify Annotation Attribute");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.modifyAnnotationAttribute();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Remove Annotation Attribute");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.removeAnnotationAttribute();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		if (allowSplit) {
			mi = new JMenuItem("Split Document");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					DocumentEditor target = targetProvider.getFunctionTarget();
					if (target != null) target.splitDocument();
				}
			});
			collector.add(mi);
		}
		mi = new JMenuItem("Annotate Sentences");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.tagSentences();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Annotate Paragraphs");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.tagParagraphs();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Annotate Sections");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.tagSections();
			}
		});
		collector.add(mi);
		collector.add(GoldenGateConstants.MENU_SEPARATOR_ITEM);
		
		mi = new JMenuItem("Normalize Paragraphs");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.normalizeParagraphs();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Normalize Whitespace");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.normalizeWhitespace();
			}
		});
		collector.add(mi);
		mi = new JMenuItem("Mark Line Ends");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.markLineEnds();
			}
		});
		collector.add(mi);
		
		return ((JMenuItem[]) collector.toArray(new JMenuItem[collector.size()]));
	}
	
	private DocumentRoot content = null;
	
	private String textFontName = null;
	private int textFontSize = 0;
	private Color textFontColor = null;
	
	private String tagFontName = null;
	private int tagFontSize = 0;
	private Color tagFontColor = null;
	
	private boolean showExtensions = false;
	
	private final DocumentEditor parent;
	private final GoldenGATE host;
	
	private AnnotationEditorPanel annotationEditor;
	private boolean contentModified = false;
	
//	DocumentEditor(GoldenGATE host, MutableAnnotation content, DocumentEditor parent, String documentName, DocumentFormat format) {
//		this(host, content, parent, documentName, format, null);
//	}
//	
//	DocumentEditor(GoldenGATE host, MutableAnnotation content, DocumentEditor parent, String documentName, DocumentFormat format, DocumentSaveOperation saveOperation) {
//		super(new BorderLayout(), true);
//		
//		this.parent = parent;
//		this.host = host;
//		
//		this.documentName = documentName;
//		this.documentFormat = format;
//		this.lastSaveOperation = saveOperation;
//		
//		//	gather missing data
//		if ((this.documentFormat == null) && (this.lastSaveOperation != null))
//			this.documentFormat = this.lastSaveOperation.getDocumentFormat();
//		if ((this.documentName == null) && (this.lastSaveOperation != null))
//			this.documentName = this.lastSaveOperation.getDocumentName();
//		
//		//	copy name and format from parent if necessary and available
//		if ((this.documentName == null) && (parent != null))
//			this.documentName = (parent.documentName + "." + content.getType());
//		if ((this.documentFormat == null) && (parent != null))
//			this.documentFormat = parent.documentFormat;
//		
//		//	make sure layout is updated if coming into view after a change
//		this.addAncestorListener(new AncestorListener() {
//			public void ancestorAdded(AncestorEvent ae) {
//				displayUndoHistory();
//			}
//			public void ancestorMoved(AncestorEvent ae) {}
//			public void ancestorRemoved(AncestorEvent ae) {}
//		});
//		
//		//	initialize annotation editor
//		this.annotationEditor = new AnnotationEditorPanel(this.host, this.content, this);
//		
//		//	register to static list, but only if not a sub editor
//		if (this.parent == null) instances.addElement(this);
//		
//		//	experimental: read in logged changes (will be deleted when content set)
//		String[] changesToRepeat = ((this.parent == null) ? Logger.readChangeLog((this.documentName == null) ? content.getAnnotationID() : this.documentName) : null);
//		
//		//	display document
//		this.setContent(content);
//		
//		//	experimental: repeat logged changes
//		if ((changesToRepeat != null) && (changesToRepeat.length != 0)
//				&& (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), ("Document " + ((this.documentName == null) ? content.getAnnotationID() : this.documentName) + " has been edited before, but some changes have not been saved. Restore these changes?"), "Repeat Changes", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.OK_OPTION)
//				&& Logger.repeatChanges(changesToRepeat, this.content)
//			) this.refreshDisplay();
//	}
	
	DocumentEditor(GoldenGATE host, DocumentEditor parent) {
		super(new BorderLayout(), true);
		
		this.parent = parent;
		this.host = host;
		
		//	make sure layout is updated if coming into view after a change
		this.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent ae) {
				activeInstanceStack.addLast(DocumentEditor.this);
				activeInstance = DocumentEditor.this;
				displayUndoHistory();
			}
			public void ancestorMoved(AncestorEvent ae) {}
			public void ancestorRemoved(AncestorEvent ae) {
				activeInstanceStack.remove(DocumentEditor.this);
				activeInstance = (activeInstanceStack.isEmpty() ? null : ((DocumentEditor) activeInstanceStack.getLast()));
			}
		});
		
		//	copy style
		if (this.parent != null) {
			this.textFontName = this.parent.textFontName;
			this.textFontSize = this.parent.textFontSize;
			this.textFontColor = this.parent.textFontColor;
			this.tagFontName = this.parent.tagFontName;
			this.tagFontSize = this.parent.tagFontSize;
			this.tagFontColor = this.parent.tagFontColor;
		}
		else if (activeInstance != null) {
			this.textFontName = activeInstance.textFontName;
			this.textFontSize = activeInstance.textFontSize;
			this.textFontColor = activeInstance.textFontColor;
			this.tagFontName = activeInstance.tagFontName;
			this.tagFontSize = activeInstance.tagFontSize;
			this.tagFontColor = activeInstance.tagFontColor;
		}
		
		//	initialize annotation editor
		this.annotationEditor = new AnnotationEditorPanel(this.host, this, ((this.parent == null) ? null : this.parent.annotationEditor));
		
		//	register to static list
		instances.addElement(this);
	}
	
	/**
	 * Show document editor extensions or not. If not showing extensions, the
	 * document editor has the annotation editor as its only child, with custom
	 * functions, recent actions and display control displaying as configured.
	 * If the document editor is set to show extensions, all the configured
	 * display elements are lain out as configured.
	 * @param showExtensions show custom extensions?
	 */
	public void setShowExtensions(boolean showExtensions) {
		if (showExtensions == this.showExtensions)
			return;
		
		this.showExtensions = showExtensions;
		this.layoutPanels();
	}
	
	private void layoutPanels() {
		this.annotationEditor.prepareLayoutPanels();
		this.removeAll();
		
		//	add all the fency stuff
		if (this.showExtensions) {
			String rootComponentName = layoutSettings.getSetting(MAIN_COMPONENT_NAME, ANNOTATION_EDITOR);
			
			JComponent rootComponent;
			if (rootComponentName.startsWith(SPLIT_PREFIX))
				rootComponent = this.getSplitComponent(layoutSettings, rootComponentName);
			else rootComponent = this.getPanel(rootComponentName);
			
			this.add(((rootComponent == null) ? this.annotationEditor : rootComponent), BorderLayout.CENTER);
		}
		
		//	use main editor only
		else this.add(this.annotationEditor, BorderLayout.CENTER);
		
		//	layout annotation editor
		this.annotationEditor.layoutPanels();
	}
	
	private JComponent getSplitComponent(Settings set, String splitName) {
		Settings splitSet = set.getSubset(splitName);
		String splitType = splitSet.getSetting(SPLIT_TYPE_NAME);
		
		//	try to alyout split
		if (LEFT_RIGHT_SPLIT_TYPE.equals(splitType) || TOP_BOTTOM_SPLIT_TYPE.equals(splitType)) {
			
			String topOrLeft = splitSet.getSetting(TOP_OR_LEFT_COMPONENT_NAME);
			JComponent topOrLeftPanel;
			if (topOrLeft.startsWith(SPLIT_PREFIX))
				topOrLeftPanel = getSplitComponent(set, topOrLeft);
			
			else topOrLeftPanel = this.getPanel(topOrLeft);
			
			String bottomOrRight = splitSet.getSetting(BOTTOM_OR_RIGHT_COMPONENT_NAME);
			JComponent bottomOrRightPanel;
			if (bottomOrRight.startsWith(SPLIT_PREFIX))
				bottomOrRightPanel = getSplitComponent(set, bottomOrRight);
			else bottomOrRightPanel = this.getPanel(bottomOrRight);
			
			//	both components not available
			if ((topOrLeftPanel == null) && (bottomOrRightPanel == null))
				return null;
			
			//	one component not available, return other one
			else if (topOrLeftPanel == null)
				return bottomOrRightPanel;
			else if (bottomOrRightPanel == null)
				return topOrLeftPanel;
			
			//	both components available, build split pane
			else {
				JSplitPane split = new JSplitPane(LEFT_RIGHT_SPLIT_TYPE.equals(splitType) ? JSplitPane.HORIZONTAL_SPLIT : JSplitPane.VERTICAL_SPLIT);
				
				if (LEFT_RIGHT_SPLIT_TYPE.equals(splitType)) {
					split.setLeftComponent(topOrLeftPanel);
					split.setRightComponent(bottomOrRightPanel);
				}
				else {
					split.setTopComponent(topOrLeftPanel);
					split.setBottomComponent(bottomOrRightPanel);
				}
				
				double resizeWeight = 0.5;
				try {
					resizeWeight = (((double) Integer.parseInt(splitSet.getSetting(SPLIT_RATIO_NAME, "50"))) / 100);
				} catch (NumberFormatException nfe) {}
				split.setResizeWeight(resizeWeight);
				
				return split;
			}
		}
		
		//	split not possible
		else return null;
	}
	
	private HashMap panelCache = new HashMap();
	
	private JPanel getPanel(String name) {
		if (ANNOTATION_EDITOR.equals(name))
			return this.annotationEditor;
		
		else if (DISPLAY_CONTROL.equals(name))
			return this.annotationEditor.getDisplayControlPanel();
		
		else if (CUSTOM_FUNCTIONS.equals(name))
			return this.annotationEditor.getCustomFunctionPanel();
		
		else if (RECENT_ACTIONS.equals(name))
			return this.annotationEditor.getActionPanel();
		
		else if (this.panelCache.containsKey(name))
			return ((JPanel) this.panelCache.get(name));
		
		else {
			DocumentEditorExtension[] dees = this.host.getDocumentEditorExtensions();
			for (int e = 0; e < dees.length; e++) {
				if (dees[e].getPluginName().equals(name)) {
					JPanel panel = dees[e].getExtensionPanel(this);
					this.panelCache.put(name, panel);
					return panel;
				}
			}
			return null;
		}
	}
	
	boolean close() {
		if (this.isContentModified()) {
			int i = (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), (this.getTitle() + " has been modified. Save Changes?"), "Save Changes", JOptionPane.YES_NO_CANCEL_OPTION));
			
			//	closing cancelled
			if ((i == JOptionPane.CANCEL_OPTION) || (i == JOptionPane.CLOSED_OPTION))
				return false;
			
			//	chosen to save, and saving cancelled or failed
			else if	((i == JOptionPane.YES_OPTION) && !this.saveContent())
				return false;
		}
		
		this.writeLog("Document Closed");
		this.cleanup();
		System.gc();
		return true;
	}
	
	private void cleanup() {
		if (preview != null) {
			preview.dispose();
			preview = null;
		}
		instances.remove(this);
		
		this.annotationEditor.close();
		this.undoHistory.clear();
		this.undoAtoms.clear();
		this.writeThroughAtoms.clear();
		
		if (this.lastSaveOperation != null) {
			this.lastSaveOperation.documentClosed();
			this.lastSaveOperation = null;
		}
	}
	
	private JMenu localUndoMenu = null;
	
	private void displayUndoHistory() {
		if (this.localUndoMenu == null) {
			undoMenu.removeAll();
			undoMenu.setEnabled(this.undoHistory.size() != 0);
//			for (int u = 0; u < Math.min(this.undoHistory.size(), UNDO_MENU_SIZE); u++) {
//				final UndoAction ua = ((UndoAction) this.undoHistory.get(u));
//				final int count = u+1;
//				JMenuItem mi = new JMenuItem(ua.title);
//				mi.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent ae) {
//						undo(count);
//						displayUndoHistory();
//					}
//				});
//				undoMenu.add(mi);
//			}
			int u = 0;
			for (Iterator uait = this.undoHistory.iterator(); uait.hasNext();) {
				final UndoAction ua = ((UndoAction) uait.next());
				final int count = (u++) + 1;
				JMenuItem mi = new JMenuItem(ua.title);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						undo(count);
						displayUndoHistory();
					}
				});
				undoMenu.add(mi);
				if (u >= UNDO_MENU_SIZE)
					break;
			}
		}
		else {
			this.localUndoMenu.removeAll();
			this.localUndoMenu.setEnabled(this.undoHistory.size() != 0);
//			for (int u = 0; u < Math.min(this.undoHistory.size(), UNDO_MENU_SIZE); u++) {
//				final UndoAction ua = ((UndoAction) this.undoHistory.get(u));
//				final int count = u+1;
//				JMenuItem mi = new JMenuItem(ua.title);
//				mi.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent ae) {
//						undo(count);
//						displayUndoHistory();
//					}
//				});
//				this.localUndoMenu.add(mi);
//			}
			int u = 0;
			for (Iterator uait = this.undoHistory.iterator(); uait.hasNext();) {
				final UndoAction ua = ((UndoAction) uait.next());
				final int count = (u++) + 1;
				JMenuItem mi = new JMenuItem(ua.title);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						undo(count);
						displayUndoHistory();
					}
				});
				this.localUndoMenu.add(mi);
				if (u >= UNDO_MENU_SIZE)
					break;
			}
		}
	}
	
	private boolean truncateUndoHistory(int maxRetainSize, long maxRetainAge) {
		//	we cannot truncate the history if we might have to write it through later
		if (this.originalContent != null)
			return false;
		
		//	remove oldest items from undo history
		long minStorageTime = (System.currentTimeMillis() - maxRetainAge);
		boolean removed = false;
		while (this.undoHistory.size() > 0) {
			
			//	this one's too much
			if (this.undoHistory.size() > maxRetainSize) {
				this.undoHistory.removeLast();
				removed = true;
				continue;
			}
			
			//	this one's too old
			UndoAction ua = ((UndoAction) this.undoHistory.getLast());
			if (ua.storageTime < minStorageTime) {
				this.undoHistory.removeLast();
				removed = true;
				continue;
			}
			
			//	neither too large nor too old, we're done here
			break;
		}
		
		//	finally ...
		return removed;
	}
	
	private CharSequenceListener atomRecorder = null;
	private ArrayList undoAtoms = new ArrayList();
	private ArrayList writeThroughAtoms = new ArrayList();
	private boolean wasCleared = false;
	
//	private Vector undoHistory = new Vector();
	private LinkedList undoHistory = new LinkedList();
	private UndoAction undoAction = null;
	
	void enqueueRestoreContentUndoAction(String title) {
		this.enqueueRestoreContentUndoAction(title, 0, this.content.size());
	}
	
	void enqueueRestoreContentUndoAction(String title, int startIndex, int size) {
		this.enqueueRestoreMarkupUndoAction(null, title, startIndex, size);
		this.startModification(startIndex, size);
	}
	
	void enqueueRestoreMarkupUndoAction(String title) {
		this.enqueueRestoreMarkupUndoAction(null, title);
	}
	
	void enqueueRestoreMarkupUndoAction(String type, String title) {
		this.enqueueRestoreMarkupUndoAction(type, title, 0, this.content.size());
	}
	
	void enqueueRestoreMarkupUndoAction(String title, int startIndex, int size) {
		this.enqueueRestoreMarkupUndoAction(null, title, startIndex, size);
	}
	
	void enqueueRestoreMarkupUndoAction(final String type, final String title, final int startIndex, final int size) {
		
		//	collect affected Annotations
		final int endIndex = startIndex + size;
		Annotation[] annotations = this.content.getAnnotations(type);
//		ArrayList affectedAnnotationList = new ArrayList();
		final TreeMap affectedAnnotations = new TreeMap();
		for (int a = 0; a < annotations.length; a++) {
			int si = annotations[a].getStartIndex();
			int ei = annotations[a].getEndIndex();
			if ((((si >= startIndex) && (si <= endIndex)) || ((ei >= startIndex) && (ei <= endIndex))) && !DocumentRoot.DOCUMENT_TYPE.equals(annotations[a].getType())) {
				Annotation backup = Gamta.newAnnotation(this.content, annotations[a]);
				backup.setAttribute(Annotation.ANNOTATION_ID_ATTRIBUTE, annotations[a].getAnnotationID());
				affectedAnnotations.put(getAnnotationKey(backup), backup);
//				affectedAnnotationList.add(Gamta.newAnnotation(this.content, annotations[a]));
			}
		}
		
		//	store token attributes
		final TreeMap affectedTokens = new TreeMap();
		for (int t = startIndex; t < (startIndex + size); t++) {
			String[] tans = content.tokenAt(t).getAttributeNames();
			if (tans.length != 0) {
				Annotation ta = Gamta.newAnnotation(content, Token.TOKEN_ANNOTATION_TYPE, t, 1);
				ta.copyAttributes(content.tokenAt(t));
				affectedTokens.put(new Integer(t), ta);
			}
		}
		
		//	build arrays
//		final Annotation[] affectedAnnotations = ((Annotation[]) affectedAnnotationList.toArray(new Annotation[affectedAnnotationList.size()]));
		final boolean wasContentModified = this.contentModified;
		this.undoAction = new UndoAction(new UndoRedoAction() {
			public void run() {
				
				//	restore token attributes
				for (int t = 0; t < content.size(); t++) {
					content.tokenAt(t).clearAttributes();
					Annotation ta = ((Annotation) affectedTokens.get(new Integer(t)));
					if (ta != null)
						content.tokenAt(t).copyAttributes(ta);
				}
				
				//	remove/restore modified markup
				Annotation[] annotations = content.getAnnotations(type);
				for (int a = 0; a < annotations.length; a++) {
					int si = annotations[a].getStartIndex();
					int ei = annotations[a].getEndIndex();
					if (((si >= startIndex) && (si <= endIndex)) || ((ei >= startIndex) && (ei <= endIndex))) {
						Annotation original = ((Annotation) affectedAnnotations.remove(getAnnotationKey(annotations[a])));
						
						//	this annotation did not exist before
						if (original == null)
							content.removeAnnotation(annotations[a]);
						
						//	annotation existed before, check attributes
						else {
							annotations[a].clearAttributes();
							annotations[a].copyAttributes(original);
						}
//						content.removeAnnotation(annotations[a]);
					}
				}
				
				//	add remaining original markup
//				for (int a = 0; a < affectedAnnotations.length; a++)
//					content.addAnnotation(affectedAnnotations[a]);
				for (Iterator ait = affectedAnnotations.values().iterator(); ait.hasNext();) {
					Annotation original = ((Annotation) ait.next());
					Annotation restored = content.addAnnotation(original);
					restored.copyAttributes(original);
					restored.setAttribute(Annotation.ANNOTATION_ID_ATTRIBUTE, original.getAnnotationID());
				}
				
				notifyDocumentMarkupModified();
				notifyDocumentTextModified();
				contentModified = wasContentModified;
			}
			void dissolve() {
				affectedTokens.clear();
				affectedAnnotations.clear();
			}
		}, title);
	}
	
	private String getAnnotationKey(Annotation annotation) {
		return (annotation.getAnnotationID() + "-" + annotation.getType() + "-" + annotation.getStartIndex() + "-" + annotation.size());
	}
	
	private void startModification(final int startIndex, final int size) {
		
		//	clean up after older undo actions
		if (this.atomRecorder != null) {
			this.content.removeCharSequenceListener(this.atomRecorder);
			this.atomRecorder = null;
		}
		this.undoAtoms.clear();
		this.writeThroughAtoms.clear();
		
		//	copy tokens
		final MutableTokenSequence copyTokens = this.content.getMutableSubsequence(startIndex, size);
		for (int t = startIndex; t < (startIndex + size); t++) {
			Token originalToken = this.content.tokenAt(t);
			Token copyToken = copyTokens.tokenAt(t - startIndex);
			copyToken.copyAttributes(originalToken);
		}
		
		//	build token checker
		this.undoAtoms.add(new Runnable() {
			public void run() {
				for (int t = startIndex; (t < (startIndex + size)) && (t < content.size()); t++) {
					Token copyToken = copyTokens.tokenAt(t - startIndex);
					Token originalToken = content.tokenAt(t);
					originalToken.copyAttributes(copyToken);
				}
			}
		});
		
		//	pay attention to token level changes
		this.atomRecorder = new AtomRecorder();
		this.content.addCharSequenceListener(this.atomRecorder);
	}
	
	private void modificationComplete() {
		this.content.removeCharSequenceListener(this.atomRecorder);
		this.atomRecorder = null;
		
		//	store undo atoms
		Collections.reverse(this.undoAtoms);
		this.undoAtoms.add(this.undoAction.undoAction);
		final Runnable[] undoAtoms = ((Runnable[]) this.undoAtoms.toArray(new Runnable[this.undoAtoms.size()]));
		this.undoAtoms.clear();
		this.undoAction.undoAction = new UndoRedoAction() {
				public void run() {
					for (int r = 0; r < undoAtoms.length; r++)
						undoAtoms[r].run();
				}
				void dissolve() {
					Arrays.fill(undoAtoms, null);
				}
			};
		
		//	store write through atoms
		final Runnable[] writeThroughAtoms = ((Runnable[]) this.writeThroughAtoms.toArray(new Runnable[this.undoAtoms.size()]));
		this.writeThroughAtoms.clear();
		this.undoAction.writeThroughAction = new UndoRedoAction() {
				public void run() {
					for (int r = 0; r < writeThroughAtoms.length; r++)
						writeThroughAtoms[r].run();
				}
				void dissolve() {
					Arrays.fill(writeThroughAtoms, null);
				}
			};
	}
	
	private class AtomRecorder implements CharSequenceListener {
		public void charSequenceChanged(CharSequenceEvent change) {
			final int offset = change.offset;
			final CharSequence inserted = change.inserted;
			final CharSequence removed = change.removed;
			undoAtoms.add(new Runnable() {
				public void run() {
					if (inserted.length() == 0)
						content.insertChars(removed, offset);
					else if (removed.length() == 0)
						content.removeChars(offset, inserted.length());
					else content.setChars(removed, offset, inserted.length());
				}
			});
			if (originalContent != null) {
				writeThroughAtoms.add(new Runnable() {
					public void run() {
						if (inserted.length() == 0)
							originalContent.removeChars(offset, removed.length());
						else if (removed.length() == 0)
							originalContent.insertChars(inserted, offset);
						else originalContent.setChars(inserted, offset, removed.length());
					}
				});
			}
		}
	}
	
	/**	add the last enqueued undo action to the undo history
	 */
	void storeUndoAction() {
		if (this.undoAction != null) {
			if (this.atomRecorder != null)
				this.modificationComplete();
//			this.undoHistory.insertElementAt(this.undoAction, 0);
			this.undoAction.storageTime = System.currentTimeMillis();
			this.undoHistory.addFirst(this.undoAction);
			this.undoAction = null;
			this.truncateUndoHistory(undoHistoryMaxItemCount, undoHistoryMaxItemAge);
			this.displayUndoHistory();
		}
	}
	
	private void undo() {
		this.undo(1);
	}
	
	private void undo(int count) {
		int actionCount = Math.min(count, this.undoHistory.size());
		final UndoRedoAction[] undoActions = new UndoRedoAction[actionCount];
		int start = 0;
		for (int u = 0; u < actionCount; u++) {
//			UndoAction ua = ((UndoAction) this.undoHistory.remove(0));
			UndoAction ua = ((UndoAction) this.undoHistory.removeFirst());
			undoActions[u] = ua.undoAction;
			this.writeLog("Undo '" + ua.title + "'");
		}
		final int undoStart = start;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int r = undoStart; r < undoActions.length; r++) {
					undoActions[r].run();
					undoActions[r].dissolve();
				}
				refreshDisplay();
			}
		});
		this.displayUndoHistory();
	}
	
	private static final boolean DEBUG_WRITETHROUGH = true;
	
	/**
	 * Write the changes made to a working copy of a content mutable annotation
	 * through to this very annotation. If the content MutableAnnotation of this
	 * document editor is a DocumentRoot, invokation of this method does not
	 * have any effect. If not, the document editor will write all changes made
	 * since the last invokation of either one of setContent() or writeChanges()
	 * through to the content MutableAnnotation. This approach is in order to
	 * facilitate UNDO functionality. Invokation of this method will clear the
	 * local UNDO history in order to prevent writing the same changes twice.
	 */
	public void writeChanges() {
		if ((this.originalContent == null) || !this.isContentModified())
			return;
		
		if (DEBUG_WRITETHROUGH) System.out.println("WRITING CHANGES");
		
		//	replace tokens
		if (this.wasCleared) {
			this.wasCleared = false;
			
			//	keep first token in order to avoid markup anomalies
			this.originalContent.removeTokensAt(1, (this.originalContent.size() - 1));
			
			//	add new content
			this.originalContent.addTokens(this.content);
			
			//	remove first token
			this.originalContent.removeTokensAt(0, 1);
		}
		
		//	transform tokens
		else {
			
			//	redo recorded modifications
//			Collections.reverse(this.undoHistory);
//			for (int r = 0; r < this.undoHistory.size(); r++) {
//				UndoAction mod = ((UndoAction) this.undoHistory.get(r));
//				if ((mod != null) && (mod.writeThroughAction != null))
//					mod.writeThroughAction.run();
//			}
			while (this.undoHistory.size() > 0) {
				UndoAction mod = ((UndoAction) this.undoHistory.removeLast());
				if (mod.writeThroughAction != null)
					mod.writeThroughAction.run();
				mod.dissolve();
			}
			
			//	adjust token attributes
			for (int t = 0; t < this.content.size(); t++) {
				Token originalToken = this.originalContent.tokenAt(t);
				originalToken.clearAttributes();
				originalToken.copyAttributes(this.content.tokenAt(t));
			}
		}
		
		//	clear undo history
		this.undoHistory.clear();
		
		//	get annotations from both original content and working copy
		Annotation[] originalAnnotations = this.originalContent.getAnnotations();
		Annotation[] annotations = this.content.getAnnotations();
		
		//	sort out annotations that didn't change
		int oai = 0;
		for (int a = 0; a < annotations.length; a++) {
			
			//	find next original content annotation
			while ((oai < originalAnnotations.length) && (originalAnnotations[oai].getStartIndex() < annotations[a].getStartIndex()))
				oai++;
			
			//	sort out unchanged annotations
			if ((oai < originalAnnotations.length) && AnnotationUtils.equals(originalAnnotations[oai], annotations[a], true)) {
				if (DEBUG_WRITETHROUGH) System.out.println(" - retaining unchanged annotation " + originalAnnotations[oai].getType() + "-Annotation: '" + originalAnnotations[oai].getValue() + "'");
				
				//	adjust attributes if necessary
				if (!AttributeUtils.hasEqualAttributes(originalAnnotations[oai], annotations[a])) {
					originalAnnotations[oai].clearAttributes();
					originalAnnotations[oai].copyAttributes(annotations[a]);
				}
				
				//	annotations done, remove them from arrays
				originalAnnotations[oai++] = null;
				annotations[a] = null;
			}
		}
		
		//	remove old markup that was modified
		for (int oa = 0; oa < originalAnnotations.length; oa++) {
			if ((originalAnnotations[oa] != null) && !AnnotationUtils.equals(this.originalContent, originalAnnotations[oa])) {
				if (DEBUG_WRITETHROUGH) System.out.println(" - removing old " + originalAnnotations[oa].getType() + "-Annotation: '" + originalAnnotations[oa].getValue() + "'");
				this.originalContent.removeAnnotation(originalAnnotations[oa]);
			}
		}
		
		//	copy new markup from working copy
		for (int a = 0; a < annotations.length; a++) {
			if ((annotations[a] != null) && !DocumentRoot.DOCUMENT_TYPE.equals(annotations[a].getType())) {
				if (AnnotationUtils.equals(this.originalContent, annotations[a])) {
					if (DEBUG_WRITETHROUGH) System.out.println(" - retaining content " + annotations[a].getType() + "-Annotation: '" + annotations[a].getValue() + "'");
					this.originalContent.clearAttributes();
					this.originalContent.copyAttributes(annotations[a]);
				}
				else {
					if (DEBUG_WRITETHROUGH) System.out.println(" - adding modified " + annotations[a].getType() + "-Annotation: '" + annotations[a].getValue() + "'");
					this.originalContent.addAnnotation(annotations[a]);
				}
			}
		}
	}
	
	/**	Representation of undo actions
	 */
	private static class UndoAction {
		final String title;
		UndoRedoAction undoAction;
		UndoRedoAction writeThroughAction = null;
		long storageTime = System.currentTimeMillis();
		UndoAction(UndoRedoAction undoAction, String title) {
			this.undoAction = undoAction;
			this.title = title;
		}
		void dissolve() {
			this.undoAction.dissolve();
			if (this.writeThroughAction != null)
				this.writeThroughAction.dissolve();
		}
	}
	
	/**	Representation of atomic undo or redo actions
	 */
	private static abstract class UndoRedoAction implements Runnable {
		abstract void dissolve();
	}
	
	//	if the content of this DocumentEditor is a non-root MutableAnnotation, perform changes on a copy and write them through when this editor is closed
	private MutableAnnotation originalContent = null;
	
	//	listener for annotation changes, and pointer to resource responsible for the changes 
	private AnnotationListener annotationNotifyer = null;
	private Resource annotationModifyer = null;
	
	void setContent(MutableAnnotation content, String documentName, DocumentFormat format, DocumentSaveOperation saveOperation) {
		
		this.documentName = documentName;
		this.documentFormat = format;
		this.lastSaveOperation = saveOperation;
		
		//	gather missing data
		if ((this.documentFormat == null) && (this.lastSaveOperation != null))
			this.documentFormat = this.lastSaveOperation.getDocumentFormat();
		if ((this.documentName == null) && (this.lastSaveOperation != null))
			this.documentName = this.lastSaveOperation.getDocumentName();
		
		//	display document
		this.setContent(content);
	}
	
	/**
	 * Set the content of this document editor. If the specified
	 * MutableAnnotation is a DocumentRoot, all changes made to it will apply
	 * directly. If not, the document editor will create a working copy in order
	 * to facilitate UNDO functionality. Changes apply to the specified
	 * MutableAnnotation only after writeChanges() has been invoked.
	 * 
	 * @param content the mutable annotation to edit in this annotation editor
	 */
	public void setContent(MutableAnnotation content) {
		
		//	stop listening for annotation actions on old content
		if (this.content != null)
			this.content.removeAnnotationListener(this.annotationNotifyer);
		
		//	enable editor
		this.annotationEditor.setEnabled(content != null);
		DocumentRoot newContent;
		
		//	empty content
		if (content == null) {
			newContent = Gamta.newDocument(this.host.getTokenizer());
			this.originalContent = null;
		}
		
		//	got root of content hierarchy, no need for working copy
		else if (content instanceof DocumentRoot) {
			newContent = ((DocumentRoot) content);
			this.originalContent = null;
		}
		
		//	only view on document, create working copy
		else {
			this.originalContent = content;
			newContent = Gamta.copyDocument(content);
		}
		
		//	clear memory
		this.undoHistory.clear();
		this.displayUndoHistory();
		this.contentModified = false;
		
		//	set content
		this.content = newContent;
		
		//	listen to annotation changes
		this.annotationNotifyer = new AnnotationNotifyer();
		this.content.addAnnotationListener(this.annotationNotifyer);
		
		//	dispplay new content in annotation editor
		this.annotationEditor.setContent(this.content);
		
		//	refresh extensions
		this.panelCache.clear();
		this.layoutPanels();
	}
	
	/**
	 * the annotation listener implementation looping annotation actions through to observers registered at host
	 * 
	 * @author sautter
	 */
	private class AnnotationNotifyer implements AnnotationListener {
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.gamta.AnnotationListener#annotationAdded(de.uka.ipd.idaho.gamta.QueriableAnnotation, de.uka.ipd.idaho.gamta.Annotation)
		 */
		public void annotationAdded(QueriableAnnotation doc, Annotation annotation) {
			if (!TEMP_ANNOTATION_TYPE.equals(annotation.getType()))
				notifyAnnotationAdded(doc, annotation, annotationModifyer);
		}
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.gamta.AnnotationListener#annotationRemoved(de.uka.ipd.idaho.gamta.QueriableAnnotation, de.uka.ipd.idaho.gamta.Annotation)
		 */
		public void annotationRemoved(QueriableAnnotation doc, Annotation annotation) {
			if (!TEMP_ANNOTATION_TYPE.equals(annotation.getType()))
				notifyAnnotationRemoved(doc, annotation, annotationModifyer);
		}
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.gamta.AnnotationListener#annotationTypeChanged(de.uka.ipd.idaho.gamta.QueriableAnnotation, de.uka.ipd.idaho.gamta.Annotation, java.lang.String)
		 */
		public void annotationTypeChanged(QueriableAnnotation doc, Annotation annotation, String oldType) {
			//	type changed to temp, treat as removal
			if (TEMP_ANNOTATION_TYPE.equals(annotation.getType()))
				notifyAnnotationRemoved(doc, Gamta.newAnnotation(doc, oldType, annotation.getStartIndex(), annotation.size()), annotationModifyer);
			
			//	old type was temp, treat as added
			else if (TEMP_ANNOTATION_TYPE.equals(oldType))
				notifyAnnotationAdded(doc, Gamta.newAnnotation(doc, oldType, annotation.getStartIndex(), annotation.size()), annotationModifyer);
			
			//	other type change
			else notifyAnnotationTypeChanged(doc, annotation, oldType, annotationModifyer);
		}
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.gamta.AnnotationListener#annotationAttributeChanged(de.uka.ipd.idaho.gamta.QueriableAnnotation, de.uka.ipd.idaho.gamta.Annotation, java.lang.String, java.lang.Object)
		 */
		public void annotationAttributeChanged(QueriableAnnotation doc, Annotation annotation, String attributeName, Object oldValue) {
			if (!TEMP_ANNOTATION_TYPE.equals(annotation.getType()))
				notifyAnnotationAttributeChanged(doc, annotation, attributeName, oldValue, annotationModifyer);
		}
	}
	
	private Vector annotationObservers = new Vector();
	
	/**	register an AnnotationObserver so it is notified whenever an Annotation is modified manually in the document in this DocumentEditor
	 * @param	ao	the AnnotationObserver to register
	 */
	public void addAnnotationObserver(AnnotationObserver ao) {
		if (ao != null) this.annotationObservers.add(ao);
	}
	
	/**	unregister an AnnotationObserver so it is not notified any more whenever an Annotation is modified manually in the document in this DocumentEditor
	 * @param	ao	the AnnotationObserver to unregister
	 */
	public void removeAnnotationObserver(AnnotationObserver ao) {
		this.annotationObservers.remove(ao);
	}
	
	private void notifyAnnotationAdded(QueriableAnnotation doc, Annotation added, Resource source) {
		QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
		Annotation addedAnnotation = ((added instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) added) : added);
		for (int l = 0; l < this.annotationObservers.size(); l++) {
			try {
				((AnnotationObserver) this.annotationObservers.get(l)).annotationAdded(docAnnotation, addedAnnotation, source);
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation added.");
				t.printStackTrace(System.out);
			}
		}
		
		this.host.notifyAnnotationAdded(docAnnotation, addedAnnotation, source);
	}
	
	private void notifyAnnotationRemoved(QueriableAnnotation doc, Annotation removed, Resource source) {
		QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
		Annotation removedAnnotation = ((removed instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) removed) : removed);
		for (int l = 0; l < this.annotationObservers.size(); l++) {
			try {
				((AnnotationObserver) this.annotationObservers.get(l)).annotationRemoved(docAnnotation, removedAnnotation, source);
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation removed.");
				t.printStackTrace(System.out);
			}
		}
		
		this.host.notifyAnnotationRemoved(docAnnotation, removedAnnotation, source);
	}
	
	private void notifyAnnotationTypeChanged(QueriableAnnotation doc, Annotation reTyped, String oldType, Resource source) {
		QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
		Annotation reTypedAnnotation = ((reTyped instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) reTyped) : reTyped);
		for (int l = 0; l < this.annotationObservers.size(); l++) {
			try {
				((AnnotationObserver) this.annotationObservers.get(l)).annotationTypeChanged(docAnnotation, reTypedAnnotation, oldType, source);
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation type change.");
				t.printStackTrace(System.out);
			}
		}
		
		this.host.notifyAnnotationTypeChanged(docAnnotation, reTypedAnnotation, oldType, source);
	}
	
	private void notifyAnnotationAttributeChanged(QueriableAnnotation doc, Annotation target, String attributeName, Object oldValue, Resource source) {
		QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
		Annotation targetAnnotation = ((target instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) target) : target);
		for (int l = 0; l < this.annotationObservers.size(); l++) {
			try {
				((AnnotationObserver) this.annotationObservers.get(l)).annotationAttributeChanged(docAnnotation, targetAnnotation, attributeName, oldValue, source);
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation attribute change.");
				t.printStackTrace(System.out);
			}
		}
		
		this.host.notifyAnnotationAttributeChanged(docAnnotation, targetAnnotation, attributeName, oldValue, source);
	}
	
	/**	@return	a read only view on the content of this DocumentEditor. The read only behavior is to facilitate UNDO functionality
	 */
	public QueriableAnnotation getContent() {
		return new ImmutableAnnotation(this.content);
	}
	
	/** @see de.goldenGate.DocumentDisplayPanel#refreshDisplay()
	 */
	synchronized void refreshDisplay() {
		this.annotationEditor.refreshDisplay();
	}
	
	/**	@return	true if and only if the document displayed in this DocumentEditor has been modified 
	 */
	public boolean isContentModified() {
		return (((this.lastSaveOperation == null) && (this.originalContent == null)) || this.contentModified);
	}
	
	private void resetContentModified() {
		this.contentModified = false;
	}
	
	
	private Vector displayObservers = new Vector();
	
	/**	register a display observer with this DocumentEditor so it is notified of scrolling activities
	 * @param	dob		the display observer to register
	 */
	public void addDisplayObserver(DisplayObserver dob) {
		if (dob != null) this.displayObservers.add(dob);
	}
	
	/**	unregister a display observer from this DocumentEditor so it is not notified of scrolling activities any more
	 * @param	dob		the display observer to unregister
	 */
	public void removeDisplayObserver(DisplayObserver dob) {
		this.displayObservers.remove(dob);
	}
	
	void notifyDisplayPositionChanged(int topTokenIndex, int bottomTokenIndex) {
		for (int o = 0; o < this.displayObservers.size(); o++) {
			try {
				((DisplayObserver) this.displayObservers.get(o)).displayPositionChanged(topTokenIndex, bottomTokenIndex);
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.displayObservers.get(o).getClass().getName() + "' of scroll position change.");
				t.printStackTrace(System.out);
			}
		}
	}
	
	private void notifyDisplayLocked() {
		for (int o = 0; o < this.displayObservers.size(); o++) {
			try {
				((DisplayObserver) this.displayObservers.get(o)).displayLocked();
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.displayObservers.get(o).getClass().getName() + "' of scroll position change.");
				t.printStackTrace(System.out);
			}
		}
	}
	
	private void notifyDisplayUnlocked() {
		for (int o = 0; o < this.displayObservers.size(); o++) {
			try {
				((DisplayObserver) this.displayObservers.get(o)).displayUnlocked();
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.displayObservers.get(o).getClass().getName() + "' of scroll position change.");
				t.printStackTrace(System.out);
			}
		}
	}
	
	/** @return the index of the first (top) Token visible in this DocumentEditor's display area
	 */
	public int getTopTokenIndex() {
		return this.annotationEditor.getTopTokenIndex();
	}
	
	/** @return the index of the last (bottom) Token visible in this DocumentEditor's display area
	 */
	public int getBottomTokenIndex() {
		return this.annotationEditor.getBottomTokenIndex();
	}
	
	/**	make the DocumentEditor's display area scroll so a specific Token becomes visible. Setting both atTop and atBottom to true will put the token with the specified index to the middle of the screen. 
	 * @param	index		the index of the Token to scroll to
	 * @param	atTop		scroll so the token is at top of the display area, not just somewhere in the visible screen
	 * @param	atBottom	scroll so the token is at bottom of the display area, not just somewhere in the visible screen
	 */
	public void scrollToToken(int index, boolean atTop, boolean atBottom) {
		this.annotationEditor.scrollToIndex(index, atTop, atBottom);
	}
	
	
	/**	show or hide the colored highlight behind annotation values
	 * @param 	annotationType	the Annotation type to change the setting for
	 * @param	visible			show or hide the highlights
	 * @return true if and only if the display changed as a result of the call to this method
	 */
	public boolean setAnnotationValueHighlightVisible(String annotationType, boolean visible) {
		if (visible)
			return this.annotationEditor.showHighlight(annotationType, true);
		else return this.annotationEditor.hideHighlight(annotationType, true);
	}
	
	/**	test if the colored highlight behind annotation values are showing
	 * @param 	annotationType	the Annotation type to check the setting for
	 * @return true if and only if the colored highlight behind values of the annotation with the specified type are showing
	 */
	public boolean isAnnotationValueHighlightVisible(String annotationType) {
		return this.annotationEditor.isHighlighted(annotationType);
	}
	
	/**	@return all annotation types that are selected for highlighting the value
	 */
	public String[] getHighlightAnnotationTypes() {
		return this.annotationEditor.getHighlightTypes();
	}
	
	/**	show or hide the tags of annotations
	 * @param 	annotationType	the Annotation type to change the setting for
	 * @param	visible			show or hide the tags
	 * @return true if and only if the display changed as a result of the call to this method
	 */
	public boolean setAnnotationTagVisible(String annotationType, boolean visible) {
		if (visible)
			return this.annotationEditor.showTags(annotationType, true);
		else return this.annotationEditor.hideTags(annotationType, true);
	}
	
	/**	test if the tags of annotation are showing
	 * @param 	annotationType	the Annotation type to check the setting for
	 * @return true if and only if the tags of the annotation with the specified type are showing
	 */
	public boolean isAnnotationTagVisible(String annotationType) {
		return this.annotationEditor.isShowingTags(annotationType);
	}
	
	/**	@return all annotation types that are selected for showing tags
	 */
	public String[] getTaggedAnnotationTypes() {
		return this.annotationEditor.getTaggedTypes();
	}
	
	/**	change the color of the tags and value highlights for some annotation type
	 * @param 	annotationType	the Annotation type to change the setting for
	 * @param	color			the color to use for highlights and tags
	 */
	public void setAnnotationColor(String annotationType, Color color) {
		this.annotationEditor.setAnnotationColor(annotationType, color);
	}
	
	/**	check the color of the tags and value highlights for some annotation type
	 * @param 	annotationType	the Annotation type to get the color for
	 * @return the color used highlights and tags of annotations with the specified type
	 */
	public Color getAnnotationColor(String annotationType) {
		return this.annotationEditor.getAnnotationHighlightColor(annotationType);
	}
	
	/**	@return	a set containing the Annotation types currently selected in the AnnotationEditor
	 */
	public Set getSelectedTags() {
		return this.annotationEditor.getSelectedTags();
	}
	
	/** @return all Annotation types present in the content document of this DocumentEditor	
	 * @see de.uka.ipd.idaho.gamta.QueriableAnnotation#getAnnotationTypes()
	 */
	public String[] getAnnotationTypes() {
		return this.getAnnotationTypes(false);
	}
	
	/** @param	recurse		recurse to the content of parent editors of this DocumentEditor 
	 * @return all Annotation types present in the content document of the root DocumentEditor of the hierarchy this DocumentEditor belongs to	
	 */
	String[] getAnnotationTypes(boolean recurse) {
		if (recurse && (this.parent != null))
			return this.parent.getAnnotationTypes(true);
		else {
			StringVector types = new StringVector();
			types.addContentIgnoreDuplicates(this.content.getAnnotationTypes());
			types.removeAll(TEMP_ANNOTATION_TYPE);
			types.sortLexicographically(false, false);
			return types.toStringArray();
		}
	}
	
	/**	@return	the name of the document displayed in this DocumentEditor
	 */
	public String getContentName() {
		return ((this.documentName != null) ? this.documentName : "Unknown");
	}
	
	/**	@return	the format of the document displayed in this DocumentEditor
	 */
	public DocumentFormat getContentFormat() {
		return this.documentFormat;
	}
	
	/**
	 * @return the title of the document editor
	 */
	public String getTitle() {
		return ((this.documentName != null) ? this.documentName : "Unknown");
	}
	
	/**
	 * @return the tooltip text of the document editor
	 */
	public String getTooltipText() {
		return ((this.documentName != null) ? this.documentName : "Unknown");
	}
	
	private static Dimension dpEditorDialogSize = new Dimension(400, 300);
	private static Point dpEditorDialogLocation = null;
	
	private void editDocumentAttributes() {
		this.writeLog("Main Menu --> Edit Document Attributes");
		if ((this.originalContent == null) && (this.content instanceof DocumentRoot)) {
			AttributeEditorDialog aed = new AttributeEditorDialog("Edit Document Attributes", this.content, this.content) {
				public void dispose() {
					dpEditorDialogSize = this.getSize();
					dpEditorDialogLocation = this.getLocation(dpEditorDialogLocation);
					super.dispose();
				}
			};
			
			//	position and show dialog
			aed.setSize(dpEditorDialogSize);
			if (dpEditorDialogLocation == null) aed.setLocationRelativeTo(this);
			else aed.setLocation(dpEditorDialogLocation);
			aed.setVisible(true);
		}
		else JOptionPane.showMessageDialog(this, "Please edit document properties in main editor.", "Cannot Edit Document Proprties", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void editDocumentProperties() {
		this.writeLog("Main Menu --> Edit Document Properties");
		if ((this.originalContent == null) && (this.content instanceof DocumentRoot)) {
			DocumentPropertyEditorDialog dped = new DocumentPropertyEditorDialog("Edit Document Properties", ((DocumentRoot) this.content)) {
				public void dispose() {
					dpEditorDialogSize = this.getSize();
					dpEditorDialogLocation = this.getLocation(dpEditorDialogLocation);
					super.dispose();
				}
			};
			
			//	position and show dialog
			dped.setSize(dpEditorDialogSize);
			if (dpEditorDialogLocation == null) dped.setLocationRelativeTo(this);
			else dped.setLocation(dpEditorDialogLocation);
			dped.setVisible(true);
		}
		else JOptionPane.showMessageDialog(this, "Please edit document properties in main editor.", "Cannot Edit Document Proprties", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * A small editor dialog for document properties
	 * 
	 * @author sautter
	 */
	private class DocumentPropertyEditorDialog extends DialogPanel {
		
//		private boolean dirty = false;
		
		private DocumentPropertyEditor documentPropertyEditor;
		private JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		private StringVector documentPropertyNameSafe = new StringVector();
		private Properties documentPropertySafe = new Properties();
		
//		/**	Constructor
//		 * @param	host	the GoldenGATE main window
//		 * @param	title	the title for the dialog
//		 * @param	data	the Document whose properties to edit
//		 */
//		DocumentPropertyEditorDialog(GoldenGATE host, String title, DocumentRoot data) {
//			super(host, title, true);
//			this.init(data);
//		}
//		
//		/**	Constructor
//		 * @param	owner	the JDialog this DocumentPropertyEditorDialog is modal to
//		 * @param	title	the title for the dialog
//		 * @param	data	the Document whose properties to edit
//		 */
//		DocumentPropertyEditorDialog(JDialog owner, String title, DocumentRoot data) {
//			super(owner, title, true);
//			this.init(data);
//		}
//		
		/**	Constructor
		 * @param	host	the GoldenGATE main window
		 * @param	title	the title for the dialog
		 * @param	data	the Document whose properties to edit
		 */
		DocumentPropertyEditorDialog(String title, DocumentRoot data) {
			super(title, true);
//			this.init(data);
//		}
//		
//		private void init(final DocumentRoot data) {
			this.documentPropertyNameSafe.addContent(data.getDocumentPropertyNames());
			for (int a = 0; a < this.documentPropertyNameSafe.size(); a++)
				this.documentPropertySafe.setProperty(this.documentPropertyNameSafe.get(a), data.getDocumentProperty(this.documentPropertyNameSafe.get(a)));
			
			//	initialize main buttons
			JButton commitButton = new JButton("OK");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(80, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
//					dirty = 
					documentPropertyEditor.writeChanges();
					DocumentPropertyEditorDialog.this.dispose();
				}
			});
			this.mainButtonPanel.add(commitButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
			cancelButton.setPreferredSize(new Dimension(80, 21));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					DocumentPropertyEditorDialog.this.dispose();
				}
			});
			this.mainButtonPanel.add(cancelButton);
			
			//	initialize editor
			this.documentPropertyEditor = new DocumentPropertyEditor(data);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.documentPropertyEditor, BorderLayout.CENTER);
			this.getContentPane().add(this.mainButtonPanel, BorderLayout.SOUTH);
			
			//	configure window
			this.setResizable(true);
		}
		
//		/**	@return	true if and only if something was changed
//		 */
//		boolean isDirty() {
//			return this.dirty;
//		}
//		
		/**
		 * editor for annotation documentPropertys
		 * 
		 * @author sautter
		 */
		private class DocumentPropertyEditor extends JPanel {
			
			private static final String DUMMY_ATTRIBUTE_NAME = "Document Property Name";
			private static final String DUMMY_ATTRIBUTE_VALUE = "Document Property Value";
			
			private JTable documentPropertyTable = new JTable();
			
			private JComboBox documentPropertyNameField = new JComboBox();
			private JComboBox documentPropertyValueField = new JComboBox();
			
			private StringVector contextDocumentPropertyNames = new StringVector();
			private HashMap contextDocumentPropertyValuesByNames = new HashMap();
			private HashMap contextDocumentPropertyValueFrequenciesByNames = new HashMap();
			
			private DocumentRoot document;
			private StringVector annotationDocumentPropertyNames = new StringVector();
			private Properties annotationDocumentPropertyValues = new Properties();
			
			private boolean nameFieldKeyPressed = false;
			private boolean valueFieldKeyPressed = false;
			
			DocumentPropertyEditor(DocumentRoot document) {
				super(new BorderLayout(), true);
				this.document = document;
				
				//	store documentPropertys of annotation being edited
				String[] documentPropertyNames = document.getDocumentPropertyNames();
				Arrays.sort(documentPropertyNames, String.CASE_INSENSITIVE_ORDER);
				for (int n = 0; n < documentPropertyNames.length; n++) {
					this.annotationDocumentPropertyNames.addElement(documentPropertyNames[n]);
					this.annotationDocumentPropertyValues.put(documentPropertyNames[n], this.document.getDocumentProperty(documentPropertyNames[n]));
				}
				
				//	initialize documentProperty editor fields
				this.documentPropertyNameField.setBorder(BorderFactory.createLoweredBevelBorder());
				this.documentPropertyNameField.setEditable(true);
				this.resetDocumentPropertyNameField();
				
				this.documentPropertyNameField.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent fe) {
						if (DUMMY_ATTRIBUTE_NAME.equals(documentPropertyNameField.getSelectedItem()))
							documentPropertyNameField.setSelectedItem("");
					}
				});
				((JTextComponent) this.documentPropertyNameField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {
						nameFieldKeyPressed = true;
					}
					public void keyReleased(KeyEvent ke) {
						nameFieldKeyPressed = false;
					}
				});
				this.documentPropertyNameField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						if (nameFieldKeyPressed && isVisible() && !documentPropertyNameField.isPopupVisible())
							documentPropertyValueField.requestFocusInWindow();
					}
				});
				this.documentPropertyNameField.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent ie) {
						resetDocumentPropertyValueField();
					}
				});
				
				
				this.documentPropertyValueField.setBorder(BorderFactory.createLoweredBevelBorder());
				this.documentPropertyValueField.setEditable(true);
				this.resetDocumentPropertyValueField();
				
				this.documentPropertyValueField.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
					public void focusGained(FocusEvent e) {
						if (DUMMY_ATTRIBUTE_NAME.equals(documentPropertyValueField.getSelectedItem())) {
							documentPropertyValueField.setSelectedItem("");
						}
					}
				});
				((JTextComponent) this.documentPropertyValueField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {
						valueFieldKeyPressed = true;
					}
					public void keyReleased(KeyEvent ke) {
						valueFieldKeyPressed = false;
					}
				});
				this.documentPropertyValueField.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						if (valueFieldKeyPressed && isVisible() && !documentPropertyValueField.isPopupVisible())
							setDocumentProperty();
					}
				});
				
				//	initialize buttons
				JButton setDocumentPropertyButton = new JButton("Add / Set DocumentProperty");
				setDocumentPropertyButton.setBorder(BorderFactory.createRaisedBevelBorder());
				setDocumentPropertyButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						setDocumentProperty();
					}
				});
				
				JButton removeDocumentPropertyButton = new JButton("Remove DocumentProperty");
				removeDocumentPropertyButton.setBorder(BorderFactory.createRaisedBevelBorder());
				removeDocumentPropertyButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						removeDocumentProperty();
					}
				});
				
				JButton clearDocumentPropertysButton = new JButton("Clear DocumentPropertys");
				clearDocumentPropertysButton.setBorder(BorderFactory.createRaisedBevelBorder());
				clearDocumentPropertysButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						clearDocumentPropertys();
					}
				});
				
				JPanel documentPropertyButtonPanel = new JPanel(new GridBagLayout(), true);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.weightx = 1;
				gbc.weighty = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				gbc.gridx = 0;
				gbc.insets.top = 5;
				gbc.insets.left = 5;
				gbc.insets.right = 5;
				gbc.insets.bottom = 5;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.anchor = GridBagConstraints.NORTH;
				
				gbc.gridy = 0;
				documentPropertyButtonPanel.add(this.documentPropertyNameField, gbc.clone());
				gbc.gridy++;
				documentPropertyButtonPanel.add(this.documentPropertyValueField, gbc.clone());
				gbc.gridy++;
				documentPropertyButtonPanel.add(setDocumentPropertyButton, gbc.clone());
				gbc.gridy++;
				documentPropertyButtonPanel.add(removeDocumentPropertyButton, gbc.clone());
				gbc.gridy++;
				documentPropertyButtonPanel.add(clearDocumentPropertysButton, gbc.clone());
				gbc.gridy++;
				gbc.weighty = 1;
				documentPropertyButtonPanel.add(new JPanel(), gbc.clone());
				
				
				//	set up documentProperty table
				this.documentPropertyTable.setModel(new DocumentPropertyEditorTableModel());
				this.documentPropertyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				this.documentPropertyTable.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getClickCount() > 1) {
							int rowIndex = documentPropertyTable.getSelectedRow();
							if (rowIndex != -1) documentPropertyNameField.setSelectedItem(annotationDocumentPropertyNames.get(rowIndex));
						}
					}
				});
				JScrollPane documentPropertyTableBox = new JScrollPane(this.documentPropertyTable);
				documentPropertyTableBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				documentPropertyTableBox.setViewportBorder(BorderFactory.createLoweredBevelBorder());
				
				//	put the whole stuff together
				this.setLayout(new BorderLayout());
				this.add(documentPropertyTableBox, BorderLayout.CENTER);
				this.add(documentPropertyButtonPanel, BorderLayout.EAST);
			}
			
			private void resetDocumentPropertyNameField() {
				this.contextDocumentPropertyNames.sortLexicographically(false, false);
				this.documentPropertyNameField.setModel(new DefaultComboBoxModel(this.contextDocumentPropertyNames.toStringArray()));
				this.documentPropertyNameField.setSelectedItem(DUMMY_ATTRIBUTE_NAME);
			}
			
			private void resetDocumentPropertyValueField() {
				Object nameItem = this.documentPropertyNameField.getSelectedItem();
				if ((nameItem == null) || DUMMY_ATTRIBUTE_NAME.equals(nameItem)) {
					this.documentPropertyValueField.setModel(new DefaultComboBoxModel(new String[0]));
					this.documentPropertyValueField.setSelectedItem(DUMMY_ATTRIBUTE_VALUE);
				} else {
					StringVector values = this.getValueList(nameItem.toString());
					this.documentPropertyValueField.setModel(new DefaultComboBoxModel(values.toStringArray()));
					String value = this.annotationDocumentPropertyValues.getProperty(nameItem.toString());
					this.documentPropertyValueField.setSelectedItem((value == null) ? DUMMY_ATTRIBUTE_VALUE : value);
				}
			}
			
			private StringVector getValueList(String documentPropertyName) {
				StringVector values = ((StringVector) this.contextDocumentPropertyValuesByNames.get(documentPropertyName));
				if (values == null) {
					values = new StringVector();
					this.contextDocumentPropertyValuesByNames.put(documentPropertyName, values);
				}
				return values;
			}
			
			private StringIndex getValueFrequencyIndex(String documentPropertyName) {
				StringIndex valueIndex = ((StringIndex) this.contextDocumentPropertyValueFrequenciesByNames.get(documentPropertyName));
				if (valueIndex == null) {
					valueIndex = new StringIndex(true);
					this.contextDocumentPropertyValueFrequenciesByNames.put(documentPropertyName, valueIndex);
				}
				return valueIndex;
			}
			
			private void setDocumentProperty() {
				Object item;
				
				String name = null;
				item = documentPropertyNameField.getSelectedItem();
				if (item != null) name = item.toString();
				
				String value = null;
				item = documentPropertyValueField.getSelectedItem();
				if (item != null) value = item.toString();
				
				if ((name != null) && (name.length() != 0) && (value != null) && !DUMMY_ATTRIBUTE_NAME.equals(name) && !DUMMY_ATTRIBUTE_VALUE.equals(value)) {
					
					//	get old value
					String oldValue = null;
					item = this.annotationDocumentPropertyValues.getProperty(name);
					if (item != null) oldValue = item.toString();
					
					//	name OK
					if (AttributeUtils.isValidAttributeName(name)) {
						
						/*
						 * not checking the value any more ... all values are valid,
						 * only require appropriate escaping
						 */
						
						//	set documentProperty
						this.annotationDocumentPropertyNames.addElementIgnoreDuplicates(name);
						this.annotationDocumentPropertyValues.setProperty(name, value);
						
						//	update value lists
						this.getValueList(name).addElementIgnoreDuplicates(value);
						this.getValueFrequencyIndex(name).add(value);
						if (oldValue != null) {
							if (this.getValueFrequencyIndex(name).remove(oldValue))
								this.getValueList(name).removeAll(oldValue);
						}
						
						//	refresh documentProperty table
						this.documentPropertyTable.revalidate();
						this.documentPropertyTable.repaint();
						
						//	refresh input fields
						this.resetDocumentPropertyNameField();
						
//						String modificationString = (this.annotationDocumentPropertyNames.contains(name) ? "set" : "add");
//						
//						//	check value
//						if (AttributeUtils.isValidAttributeValue(value)) {
//							
//							//	set documentProperty
//							this.annotationDocumentPropertyNames.addElementIgnoreDuplicates(name);
//							this.annotationDocumentPropertyValues.setProperty(name, value);
//							
//							//	update value lists
//							this.getValueList(name).addElementIgnoreDuplicates(value);
//							this.getValueFrequencyIndex(name).add(value);
//							if (oldValue != null) {
//								if (this.getValueFrequencyIndex(name).remove(oldValue))
//									this.getValueList(name).removeAll(oldValue);
//							}
//							
//							//	refresh documentProperty table
//							this.documentPropertyTable.revalidate();
//							this.documentPropertyTable.repaint();
//							
//							//	refresh input fields
//							this.resetDocumentPropertyNameField();
//						}
//						
//						//	show error message
//						else JOptionPane.showMessageDialog(this, ("Cannot " + modificationString + " documentProperty. The specified value is invalid."), "Invalid DocumentProperty Value", JOptionPane.ERROR_MESSAGE);
					}
					
					//	show error message
					else JOptionPane.showMessageDialog(this, "Cannot add documentProperty. The specified name is invalid.", "Invalid DocumentProperty Name", JOptionPane.ERROR_MESSAGE);
				}
			}

			private void removeDocumentProperty() {
				int rowIndex = documentPropertyTable.getSelectedRow();
				if (rowIndex != -1) {
					
					//	get name to remove
					String name = this.annotationDocumentPropertyNames.get(rowIndex);
					
					//	get value
					String value = null;
					Object valueItem = this.annotationDocumentPropertyValues.getProperty(name);
					if (valueItem != null) value = valueItem.toString();
					
					//	update data
					this.annotationDocumentPropertyNames.removeAll(name);
					this.annotationDocumentPropertyValues.remove(name);
					
					//	update value lists
					if (value != null) {
						if (this.getValueFrequencyIndex(name).remove(value))
							this.getValueList(name).removeAll(value);
					}
					
					//	refresh documentProperty table
					this.documentPropertyTable.revalidate();
					this.documentPropertyTable.repaint();
					
					//	refresh input fields
					this.resetDocumentPropertyNameField();
				}
			}

			private void clearDocumentPropertys() {
				if (JOptionPane.showConfirmDialog(this, "Really remove all documentPropertys?", "Confirm Clear DocumentPropertys", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					while (!this.annotationDocumentPropertyNames.isEmpty()) {
						
						//	get name to remove
						String name = this.annotationDocumentPropertyNames.lastElement();
						
						//	get value
						String value = null;
						Object valueItem = this.annotationDocumentPropertyValues.getProperty(name);
						if (valueItem != null) value = valueItem.toString();
						
						//	update data
						this.annotationDocumentPropertyNames.removeAll(name);
						this.annotationDocumentPropertyValues.remove(name);
						
						//	update value lists
						if (value != null) {
							if (this.getValueFrequencyIndex(name).remove(value))
								this.getValueList(name).removeAll(value);
						}
					}
					
					//	refresh documentProperty table
					this.documentPropertyTable.revalidate();
					this.documentPropertyTable.repaint();
					
					//	refresh input fields
					this.resetDocumentPropertyNameField();
				}
			}
			
			private boolean writeChanges() {
				boolean modified = false;
				
				StringVector oldAnnotationDocumentPropertyNames = new StringVector();
				oldAnnotationDocumentPropertyNames.addContentIgnoreDuplicates(this.document.getDocumentPropertyNames());
				
				StringVector toRemove = oldAnnotationDocumentPropertyNames.without(this.annotationDocumentPropertyNames);
				for (int r = 0; r < toRemove.size(); r++) {
					modified = true;
					this.document.removeDocumentProperty(toRemove.get(r));
				}
				
				StringVector toCheck = oldAnnotationDocumentPropertyNames.intersect(this.annotationDocumentPropertyNames);
				for (int c = 0; c < toCheck.size(); c++) {
					String name = toCheck.get(c);
					String oldValue = this.document.getDocumentProperty(name);
					String newValue = this.annotationDocumentPropertyValues.getProperty(name);
					
					if (newValue == null) {
						if (oldValue != null) {
							modified = true;
							this.document.removeDocumentProperty(name);
						}
					} else if ((oldValue == null) || !newValue.equals(oldValue)) {
						modified = true;
						this.document.setDocumentProperty(name, newValue);
					}
				}
				
				StringVector toAdd = this.annotationDocumentPropertyNames.without(oldAnnotationDocumentPropertyNames);
				for (int a = 0; a < toAdd.size(); a++) {
					String name = toAdd.get(a);
					String value = this.annotationDocumentPropertyValues.getProperty(name);
					if (value != null) {
						modified = true;
						this.document.setDocumentProperty(name, value);
					}
				}
				
				return modified;
			}
			
			/**
			 * table model for displaying document's properties
			 * 
			 * @author sautter
			 */
			private class DocumentPropertyEditorTableModel implements TableModel {
				
				/** @see javax.swing.table.TableModel#getColumnCount()
				 */
				public int getColumnCount() {
					return 2;
				}

				/** @see javax.swing.table.TableModel#getRowCount()
				 */
				public int getRowCount() {
					return annotationDocumentPropertyNames.size();
				}

				/** @see javax.swing.table.TableModel#isCellEditable(int, int)
				 */
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}

				/** @see javax.swing.table.TableModel#getColumnClass(int)
				 */
				public Class getColumnClass(int columnIndex) {
					return String.class;
				}

				/** @see javax.swing.table.TableModel#getValueAt(int, int)
				 */
				public Object getValueAt(int rowIndex, int columnIndex) {
					if ((rowIndex >= 0) && (rowIndex < annotationDocumentPropertyNames.size())) {
						String aName = annotationDocumentPropertyNames.get(rowIndex);
						return ((columnIndex == 0) ? aName : annotationDocumentPropertyValues.getProperty(aName));
					} else return null;
				}

				/** @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
				 */
				public void setValueAt(Object newValue, int rowIndex, int columnIndex) {}

				/** @see javax.swing.table.TableModel#getColumnName(int)
				 */
				public String getColumnName(int columnIndex) {
					switch (columnIndex) {
						case 0:	return "Name";
						case 1:	return "Value";
					}
					return null;
				}

				/** @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
				 */
				public void addTableModelListener(TableModelListener l) {}

				/** @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
				 */
				public void removeTableModelListener(TableModelListener l) {}
			}
		}
	}
	
	private void editSelection() {
		this.writeLog("Main Menu --> Edit Selection");
		this.annotationEditor.editSelection();
	}
	
	private void doFindReplace() {
		this.writeLog("Main Menu --> Find / Replace");
		this.annotationEditor.doFindReplace();
	}
	
	private void annotate() {
		this.writeLog("Main Menu --> Annotate");
		this.annotationEditor.annotate();
	}
	
	private void annotateAll() {
		this.writeLog("Main Menu --> Annotate All");
		this.annotationEditor.annotateAll();
	}
	
	private void mergeAnnotations() {
		this.writeLog("Main Menu --> Merge Annotations");
		this.annotationEditor.mergeAnnotations();
	}
	
	private void splitAnnotation() {
		this.writeLog("Main Menu --> Split Annotation");
		this.annotationEditor.splitAnnotation(false, false);
	}
	
	void splitDocument() {
		this.writeLog("Main Menu --> Split Document");
		SplitDocumentDialog sdd = new SplitDocumentDialog(this.content.getAnnotationTypes());
		sdd.setVisible(true);
		if (sdd.isCommitted()) {
			String partType = sdd.getSourceType();
			MutableAnnotation[] sourceParts = this.content.getMutableAnnotations(partType);
			
			if (sourceParts.length == 0) return;
			if ((sourceParts.length == 1) && sourceParts[0].equals(this.content)) return;
			
			for (int p = 0; p < sourceParts.length; p++) {
				
				//	display part in editor
				this.host.openDocument(Gamta.copyDocument(sourceParts[p]), (this.documentName + "." + partType + "[" + p + "]"), this.documentFormat);
			}
		}
	}
	
	/**
	 * dialog for gathering the parameters of a document splitting operation
	 * 
	 * @author sautter
	 */
	private class SplitDocumentDialog extends DialogPanel {
		
//		private String[] existingTypes;
		
		private JComboBox sourceTypeSelector;
		private boolean isCommitted = false;
		
		SplitDocumentDialog(String[] existingTypes) {
//			super(host, "Split Document", true);
			super("Split Document", true);
//			this.existingTypes = existingTypes;
			
			//	initialize selector
			this.sourceTypeSelector = new JComboBox(existingTypes);
			this.sourceTypeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.sourceTypeSelector.setPreferredSize(new Dimension(200, 25));
			this.sourceTypeSelector.setEditable(false);
			
			JPanel selectorPanel = new JPanel(new FlowLayout());
			selectorPanel.add(this.sourceTypeSelector);
			
			//	initialize main buttons
			JButton commitButton = new JButton();
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.setText("Split");
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton();
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.setText("Cancel");
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(250, 90));
			this.setLocationRelativeTo(DocumentEditor.this);
		}
		
		boolean isCommitted() {
			return this.isCommitted;
		}
		
		String getSourceType() {
			Object item = this.sourceTypeSelector.getSelectedItem();
			return ((item == null) ? null : item.toString());
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			if (JOptionPane.showConfirmDialog(this, "Really Split Document?", "Confirm Split Document", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
				this.isCommitted = true;
			this.dispose();
		}
	}
	
	private void tagSentences() {
		this.writeLog("Main Menu --> Tag Sentences");
		this.annotationEditor.tagSentences();
	}
	
	private void tagParagraphs() {
		this.writeLog("Main Menu --> Tag Paragraphs");
		this.annotationEditor.tagParagraphs();
	}
	
	private void tagSections() {
		this.writeLog("Main Menu --> Tag Sections");
		this.annotationEditor.tagSections();
	}
	
	private void normalizeParagraphs() {
		this.writeLog("Main Menu --> Normalize Paragraphs");
		this.annotationEditor.normalizeParagraphs();
	}
	
	private void normalizeWhitespace() {
		this.writeLog("Main Menu --> Normalize Whitespace");
		this.annotationEditor.normalizeWhitespace();
	}
	
	private void markLineEnds() {
		this.writeLog("Main Menu --> Mark Line Ends");
		this.annotationEditor.markLineEnds();
	}
	
	private void renameAnnotations() {
		this.writeLog("Main Menu --> Rename Annotations");
		this.annotationEditor.renameAnnotations();
	}
	
	private void removeAnnotations() {
		this.writeLog("Main Menu --> Remove Annotations");
		this.annotationEditor.removeAnnotations();
	}
	
	private void renameAnnotationAttribute() {
		this.writeLog("Main Menu --> Rename Annotation Attribute");
		this.annotationEditor.renameAnnotationAttribute();
	}
	
	private void modifyAnnotationAttribute() {
		this.writeLog("Main Menu --> Modify Annotation Attribute");
		this.annotationEditor.modifyAnnotationAttribute();
	}
	
	private void removeAnnotationAttribute() {
		this.writeLog("Main Menu --> Remove Annotation Attribute");
		this.annotationEditor.removeAnnotationAttribute();
	}
	
	private void deleteAnnotations() {
		this.writeLog("Main Menu --> Delete Annotations");
		this.annotationEditor.deleteAnnotations();
	}
	
	/**
	 * Retrieve the menu items for triggering edit operations on this
	 * DocumentEditor instance from some menu. The menu items returned are bound
	 * to the DocumentEditor instance they were retrieved from.
	 * @param allowSplit indicate whether or not to include the Split Document
	 *            option in the menu
	 * @return the menu items for triggering edit operations on the
	 *         DocumentEditor instance they are retrieved from
	 */
	public JMenuItem[] getEditMenuItems(boolean allowSplit) {
		InvokationTargetProvider itp = new InvokationTargetProvider() {
			public DocumentEditor getFunctionTarget() {
				return DocumentEditor.this;
			}
		};
		this.localUndoMenu = new JMenu("Undo");
		this.localUndoMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				undo();
			}
		});
		return getEditMenuItems(itp, allowSplit, this.localUndoMenu);
	}
	
	/**	@return	all AnnotationFilters available for this DocumentEditor (not static due to type filters)
	 */
	public AnnotationFilter[] getAnnotationFilters() {
		return this.annotationEditor.getFilters(false);
	}
	
	/* TODO re-work document processort application:
	 * - open splash screen BEFORE loading actual DP (protects UI from further clicks if DP takes some time to load)
	 * - allow specifying label for undo action (e.g., custom function can submit its label, easier to grasp for user than pipeline name)
	 * - add DP manager as argument (obsoletes getting it from registry)
	 */
	
	/**
	 * Make this DocumentEditor apply a DocumentProcessor
	 * @param providerClassName the class name of the DocumentProcessorProvider
	 *            to call
	 * @param processorName the name of the DocumentProcessor to apply
	 *            (specifying null will probably open a selector dialog)
	 */
	public void applyDocumentProcessor(String providerClassName, String processorName) {
		this.applyDocumentProcessor(providerClassName, processorName, null, null);
	} // this one's used internally, in AnalyzerManager, GScriptManager, and MarkupConverterManager
	
	void applyDocumentProcessor(String providerClassName, String processorName, String annotationId, String undoActionName) {
		DocumentProcessorManager dpm = this.host.getDocumentProcessorProvider(providerClassName);
		if (dpm != null) {
			Properties parameters = new Properties();
			parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
			if (annotationId != null)
				parameters.setProperty(TARGET_ANNOTATION_ID, annotationId);
			if (undoActionName != null)
				parameters.setProperty(UNDO_ACTION_NAME, undoActionName);
			dpm.applyDocumentProcessor(processorName, this, parameters);
		}
	}
	
	/**
	 * Apply a DocumentProcessor to the document contained in this
	 * DocumentEditor
	 * @param processor the DocumentProcessor to apply
	 * @param splashScreen the spash screen blocking the main editor for the
	 *            time the processor is running (to allow
	 *            DocumentProcessorManagers to provide a custom SpalshScreen for
	 *            their specific DocumentProcessors)
	 * @param parameters execution parameters for the processor
	 */
	public void applyDocumentProcessor(final DocumentProcessor processor, final ResourceSplashScreen splashScreen, final Properties parameters) {
		if (processor == null)
			return;
		
		//	gather undo information
		DocumentProcessorManager dpm = this.host.getDocumentProcessorProvider(processor.getProviderClassName());
		String undoActionName = parameters.getProperty(UNDO_ACTION_NAME);
		if (undoActionName == null) {
			if (dpm == null)
				undoActionName = processor.getName();
			else undoActionName = dpm.getToolsMenuLabel() + " " + dpm.getResourceTypeLabel() + " (" + processor.getName() + ")";
		}
		
		//	get target
		String targetId = parameters.getProperty(TARGET_ANNOTATION_ID);
		MutableAnnotation targetAnnotation = null;
		if (targetId != null) {
			MutableAnnotation[] targetAnnotations = this.content.getMutableAnnotations();
			for (int a = 0; a < targetAnnotations.length; a++) {
				if (targetId.equals(targetAnnotations[a].getAnnotationID())) {
					targetAnnotation = targetAnnotations[a];
					a = targetAnnotations.length;
				}
			}
		}
		final MutableAnnotation target = ((targetAnnotation == null) ? this.content : targetAnnotation);
		
		//	store undo information
		this.enqueueRestoreContentUndoAction(undoActionName, target.getStartIndex(), target.size());
		this.writeLog("Apply Document Processor: " + undoActionName);
		
		//	position splash screen
		if (splashScreen != null)
			splashScreen.setLocationRelativeTo(DialogPanel.getTopWindow());
		
		//	apply processor
		Thread dpt = new Thread() {
			private boolean needRefresh = false;
			private CharSequenceListener csl;
			private AnnotationListener al;
			public void run() {
				
				//	wait for splash screen to show
				if (splashScreen != null) {
					System.out.print("DocumentEditor: popping up splashscreen ...");
					splashScreen.popUp();
					System.out.println(" done");
					while (!splashScreen.isVisible()) try {
						System.out.println("DocumentEditor: waiting for splashscreen to show ...");
						Thread.sleep(25);
					} catch (InterruptedException ie) {}
				}
				
				//	set up listening for changes
				this.csl = new CharSequenceListener() {
					public void charSequenceChanged(CharSequenceEvent change) {
						needRefresh = true;
						contentModified = true;
					}
				};
				this.al = new AnnotationListener() {
					public void annotationAdded(QueriableAnnotation doc, Annotation annotation) {
						//	TODO: create refreshHighlightSpans() method and avoid full refresh if tags not visible
						needRefresh = true;
						contentModified = true;
					}
					public void annotationAttributeChanged(QueriableAnnotation doc, Annotation annotation, String attributeName, Object oldValue) {
						needRefresh = (needRefresh || isAnnotationTagVisible(annotation.getType()));
						contentModified = true;
					}
					public void annotationRemoved(QueriableAnnotation doc, Annotation annotation) {
						//	TODO: create refreshHighlightSpans() method and avoid full refresh if tags not visible
						needRefresh = true;
						contentModified = true;
					}
					public void annotationTypeChanged(QueriableAnnotation doc, Annotation annotation, String oldType) {
						//	TODO: create refreshHighlightSpans() method and avoid full refresh if tags not visible
						needRefresh = true;
						contentModified = true;
					}
				};
				
				//	run document processor
				try {
					System.out.print("DocumentEditor: start applying processor (" + processor.getName() + ") ...");
					annotationModifyer = processor;
					notifyDisplayLocked();
					
					target.addCharSequenceListener(this.csl);
					target.addAnnotationListener(this.al);
					
					if ((splashScreen != null) && (processor instanceof MonitorableDocumentProcessor))
						((MonitorableDocumentProcessor) processor).process(target, parameters, splashScreen);
					else processor.process(target, parameters);
					System.out.println(" finished");
				}
				catch (Throwable t) {
					System.out.println(" processor produced exception");
					t.printStackTrace(System.out);
					JOptionPane.showMessageDialog(((splashScreen == null) ? ((Component) DialogPanel.getTopWindow()) : ((Component) splashScreen)), ("Error running " + processor.getName() + ":\n" + t.getMessage()), "Error Running DocumentProcessor", JOptionPane.ERROR_MESSAGE);
				}
				finally {
					if (splashScreen != null) {
						splashScreen.setPauseResumeEnabled(false);
						splashScreen.setAbortEnabled(false);
					}
					
					System.out.print("DocumentEditor: storing undo action ...");
					storeUndoAction();
					System.out.println(" done");
					
					target.removeCharSequenceListener(this.csl);
					target.removeAnnotationListener(this.al);
					
					annotationModifyer = null;
					notifyDisplayUnlocked();
					
					if (splashScreen != null) {
						while (!splashScreen.isVisible()) try {
							System.out.println("DocumentEditor: waiting for splashscreen to show ...");
							Thread.sleep(25);
						} catch (InterruptedException ie) {}
						System.out.print("DocumentEditor: disposing splashscreen ...");
						splashScreen.dispose();
						System.out.println(" done");
					}
					
					if (this.needRefresh)
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								System.out.print("DocumentEditor: refreshing display ...");
								refreshDisplay();
								System.out.println(" done");
							}
						});
				}
			}
		};
		dpt.start();
	}
	
	/**
	 * make this DocumentEditor apply a AnnotationSource
	 * @param providerClassName the class name of the AnnotationSourceProvider
	 *            to call
	 * @param annotatorName the name of the AnnotationSource to apply
	 *            (specifying null will probably open a selector dialog)
	 */
	public void applyAnnotationSource(String providerClassName, String annotatorName) {
		this.applyAnnotationSource(providerClassName, annotatorName, null);
	}
	
	/**
	 * make this DocumentEditor apply a AnnotationSource
	 * @param providerClassName the class name of the AnnotationSourceProvider
	 *            to call
	 * @param annotatorName the name of the AnnotationSource to apply
	 *            (specifying null will probably open a selector dialog)
	 * @param annotationId the ID of the annotation to apply the
	 *            AnnotationSource to (specify null for applying the
	 *            AnnotationSource to the content document of the
	 *            DocumentEditor)
	 */
	public void applyAnnotationSource(String providerClassName, String annotatorName, String annotationId) {
		AnnotationSourceManager asm = this.host.getAnnotationSourceProvider(providerClassName);
		if (asm != null) {
			Properties parameters = new Properties();
			parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
			if (annotationId != null)
				parameters.setProperty(TARGET_ANNOTATION_ID, annotationId);
			asm.applyAnnotationSource(annotatorName, this, parameters);
		}
	}
	
	/**
	 * apply an AnnotationSource to the document contained in this
	 * DocumentEditor
	 * @param annotator the AnnotationSource to apply
	 * @param splashScreen the spash screen blocking the main editor for the
	 *            time the processor is running (to allow
	 *            AnnotationSourceManagers to provide a custom SpalshScreen for
	 *            their specific AnnotationSource)
	 * @param parameters execution parameters for the annotator
	 */
	public void applyAnnotationSource(final AnnotationSource annotator, final ResourceSplashScreen splashScreen, final Properties parameters) {
		if (annotator != null) {
			
			//	gather undo information
			AnnotationSourceManager asm = this.host.getAnnotationSourceProvider(annotator.getProviderClassName());
			String undoActionName;
			if (asm == null) undoActionName = annotator.getName();
			else undoActionName = asm.getToolsMenuLabel() + " " + asm.getResourceTypeLabel() + " (" + annotator.getName() + ")";
			
			//	get target
			String targetId = parameters.getProperty(TARGET_ANNOTATION_ID);
			MutableAnnotation targetAnnotation = null;
			if (targetId != null) {
				MutableAnnotation[] targetAnnotations = this.content.getMutableAnnotations();
				for (int a = 0; a < targetAnnotations.length; a++) {
					if (targetId.equals(targetAnnotations[a].getAnnotationID())) {
						targetAnnotation = targetAnnotations[a];
						a = targetAnnotations.length;
					}
				}
			}
			final MutableAnnotation target = ((targetAnnotation == null) ? this.content : targetAnnotation);
			
			//	store undo information
			this.enqueueRestoreMarkupUndoAction(undoActionName, target.getStartIndex(), target.size());
			this.writeLog("Apply Annotation Source: " + undoActionName);
			
			//	apply processor
			if (splashScreen != null) splashScreen.setLocationRelativeTo(DialogPanel.getTopWindow());
			new Thread() {
				private boolean annotationsAdded = false;
				public void run() {
					if (splashScreen != null) {
						System.out.print("DocumentEditor: popping up splashscreen ...");
						splashScreen.popUp();
						System.out.println(" done");
						while (!splashScreen.isVisible()) try {
							System.out.println("DocumentEditor: waiting for splashscreen to show ...");
							Thread.sleep(25);
						} catch (InterruptedException ie) {}
					}
					try {
						System.out.print("DocumentEditor: start applying annotator (" + annotator.getName() + ") ...");
						annotationModifyer = annotator;
						notifyDisplayLocked();
						
						Annotation[] annotations = annotator.annotate(target, parameters);
						if (annotations != null) {
							
							//	show Annotations to user
							Window top = DialogPanel.getTopWindow();
							AnnotationDisplayDialog add;
							
							if (top instanceof JDialog)
								add = new AnnotationDisplayDialog(((JDialog) top), ("Matches of " + annotator.getTypeLabel()), annotations);
							else if (top instanceof JFrame)
								add = new AnnotationDisplayDialog(((JFrame) top), ("Matches of " + annotator.getTypeLabel()), annotations);
							else add = new AnnotationDisplayDialog(((JFrame) null), ("Matches of " + annotator.getTypeLabel()), annotations);
							
							add.setLocationRelativeTo(top);
							add.setVisible(true);
							if (add.isCommitted()) {
								
								//	get Annotation type
								String annotationType = JOptionPane.showInputDialog(top, "Please enter the type to add the Annotations with.", "Enter Annotation Type", JOptionPane.QUESTION_MESSAGE);
								if (annotationType != null) {
									
									//	add Annotations
									annotations = add.getSelectedAnnotations();
									for (int a = 0; a < annotations.length; a++) {
										annotations[a].changeTypeTo(annotationType);
										annotations[a] = target.addAnnotation(annotations[a]);
									}
									this.annotationsAdded = (annotations.length != 0);
								}
							}
						}
						
						System.out.println(" finished");
					}
					catch (Throwable t) {
						System.out.println(" annotation source produced exception");
						t.printStackTrace(System.out);
						JOptionPane.showMessageDialog(((splashScreen == null) ? ((Component) DialogPanel.getTopWindow()) : ((Component) splashScreen)), ("Error running " + annotator.getName() + ":\n" + t.getMessage()), "Error Applying AnnotationSource", JOptionPane.ERROR_MESSAGE);
					}
					finally {
						System.out.print("DocumentEditor: storing undo action ...");
						storeUndoAction();
						System.out.println(" done");
						
						contentModified = (contentModified || this.annotationsAdded);
						annotationModifyer = null;
						notifyDisplayUnlocked();
						
						if (splashScreen != null) {
							while (!splashScreen.isVisible()) try {
								System.out.println("DocumentEditor: waiting for splashscreen to show ...");
								Thread.sleep(25);
							} catch (InterruptedException ie) {}
							System.out.print("DocumentEditor: disposing splashscreen ...");
							splashScreen.dispose();
							System.out.println(" done");
						}
						
						//	TODO: create refreshHighlightSpans() method and avoid full refresh if tags not visible
						if (this.annotationsAdded)
							SwingUtilities.invokeLater(new Runnable() {
								public void run() {
									System.out.print("DocumentEditor: refreshing display ...");
									refreshDisplay();
									System.out.println(" done");
								}
							});
					}
				}
			}.start();
		}
	}
	
	/**	make this DocumentEditor show a custom view of its content document
	 * @param	viewerClassName	the class name of the AnnotationSourceProvider to call
	 */
	public void showDocumentView(String viewerClassName) {
		DocumentViewer dv = this.host.getDocumentViewer(viewerClassName);
		if (dv != null) dv.showDocument(this);
	}
	
	private DocumentSaveOperation lastSaveOperation = null;
	private String documentName = null;
	private DocumentFormat documentFormat = null;
	private static PreviewWindow preview = null; 
	
	void preview() {
		if (preview == null) preview = new PreviewWindow();
		else if (preview.previewWindow.isVisible())
			preview.dispose();
		
		preview.showPreview(this, ((this.documentName == null) ? "Unnamed Document" : this.documentName), this.documentFormat);
		
		this.writeLog("Document Preview Opened");
	}
	
	/**
	 * window for output preview 
	 * 
	 * @author sautter
	 */
	private static Point outputPreviewPosition = null;
	private static Dimension outputPreviewSize = new Dimension(800, 600);
	private static class PreviewWindow extends JPanel {
		
		private JComboBox formatSelector = new JComboBox();
		private ItemListener formatListener = new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				refreshDisplay();
				
				DocumentFormat df = ((DocumentFormat) formatSelector.getSelectedItem());
				if (df != null) {
					String fileExtension = df.getDefaultSaveFileExtension();
					validateButton.setEnabled((fileExtension != null) && fileExtension.equalsIgnoreCase("xml"));
				}
			}
		};
		
		private JTextPane display;
		private JScrollPane displayBox;
		
		private DocumentEditor parent;
		private Window previewWindow;
		
		private static final String defaultSchemaLabelText = "<click to load or select an XML schema>";
		private JLabel schemaLabel = new JLabel(defaultSchemaLabelText, JLabel.RIGHT);
		private JButton validateButton = new JButton("Validate");
		
		PreviewWindow() {
			super(new BorderLayout(), true);
			
			this.formatSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			
			this.schemaLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectSchema();
				}
			});
			
			this.validateButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.validateButton.setPreferredSize(new Dimension(100, 21));
			this.validateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (selectedSchemaName == null) selectSchema();
					if (selectedSchemaName != null) {
						final StringVector errorCollector = new StringVector();
						Schema schema = ((Schema) schemaCache.get(selectedSchemaName));
						if (schema == null)
							errorCollector.addElement("Validation is not possible, the schema '" + selectedSchemaName + "' does not exist.");
						
						else {
						    Validator validator = schema.newValidator();
							validator.setErrorHandler(new ErrorHandler() {
								public void error(SAXParseException exception) throws SAXException {
									errorCollector.addElement("Error: " + exception.getMessage());
								}
								public void fatalError(SAXParseException exception) throws SAXException {
									errorCollector.addElement("Fatal: " + exception.getMessage());
								}
								public void warning(SAXParseException exception) throws SAXException {
									errorCollector.addElement("Warning: " + exception.getMessage());
								}
							});
						    SAXResult sRes = new SAXResult();
						    try {
						        validator.validate(new SAXSource(new InputSource(new DocumentInputStream(display.getStyledDocument()))), sRes);
						    }
						    catch (SAXException se) {
								errorCollector.addElement("Fatal: " + se.getMessage());
						    }
						    catch (IOException ioe) {
								errorCollector.addElement("Fatal: " + ioe.getMessage());
							}
						}
						if (errorCollector.isEmpty())
							JOptionPane.showMessageDialog(PreviewWindow.this, ("The output is valid against the schema '" + selectedSchemaName + "'."), "XML Schema Validation Result", JOptionPane.INFORMATION_MESSAGE);
						else JOptionPane.showMessageDialog(PreviewWindow.this, ("Some errors were found validating the document:\n  " + errorCollector.concatStrings("\n  ", 0, Math.min(errorCollector.size(), 25))), "XML Schema Validation Result", JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			
			JPanel functionPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 0;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			functionPanel.add(new JLabel("Choose output format to preview"), gbc.clone());
			gbc.gridx++;
			functionPanel.add(this.formatSelector, gbc.clone());
			gbc.gridx++;
			gbc.weightx = 1;
			functionPanel.add(this.schemaLabel, gbc.clone());
			gbc.gridx++;
			gbc.weightx = 0;
			functionPanel.add(this.validateButton, gbc.clone());
			
			this.display = new JTextPane();
			this.display.setEditable(false);
			this.display.setContentType("text/plain");
			this.displayBox = new JScrollPane(this.display);
			
			JButton closeButton = new JButton("Close");
			closeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			closeButton.setPreferredSize(new Dimension(100, 21));
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(closeButton);
			
			this.add(functionPanel, BorderLayout.NORTH);
			this.add(this.displayBox, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.SOUTH);
		}
		
		private void dispose() {
			if ((this.previewWindow != null) && this.previewWindow.isVisible()) {
				outputPreviewSize = this.previewWindow.getSize();
				outputPreviewPosition = this.previewWindow.getLocation();
				this.previewWindow.dispose();
			}
		}
		
		private void showPreview(DocumentEditor parent, String docName, DocumentFormat format) {
			if ((this.previewWindow != null) && this.previewWindow.isVisible())
				this.dispose();
			
			this.parent = parent;
			
			Window w = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
			if (w instanceof Dialog) {
				this.previewWindow = new JDialog(((Dialog) w), ("Output Preview for " + docName), false);
				((JDialog) this.previewWindow).getContentPane().add(this, BorderLayout.CENTER);
				((JDialog) this.previewWindow).setSize(outputPreviewSize);
				((JDialog) this.previewWindow).setResizable(true);
				w.addWindowListener(new WindowAdapter() {
					public void windowClosed(WindowEvent we) {
						PreviewWindow.this.dispose();
					}
				});
			}
			else {
				this.previewWindow = new JFrame("Output Preview for " + docName);
				((JFrame) this.previewWindow).setIconImage(parent.host.getGoldenGateIcon());
				((JFrame) this.previewWindow).setSize(outputPreviewSize);
				((JFrame) this.previewWindow).getContentPane().add(this, BorderLayout.CENTER);
				((JFrame) this.previewWindow).setResizable(true);
			}
			
			if (outputPreviewPosition == null)
				this.previewWindow.setLocationRelativeTo(this.parent);
			else this.previewWindow.setLocation(outputPreviewPosition);
			
			ArrayList formatList = new ArrayList();
			DocumentFormatProvider[] formatters = parent.host.getDocumentFormatProviders();
			String defaultFormatName = DocumentFormat.getDefaultSaveFormatName();
			boolean enforceDefaultFormat = DocumentFormat.enforceDefaultSaveFormat();
			DocumentFormat defaultFormat = null;
			DocumentFormat selectedFormat = null;
			DocumentFormat acceptingFormat = null;
			
			for (int fp = 0; fp < formatters.length; fp++) {
				DocumentFormat[] formats = formatters[fp].getSaveFileFilters();
				for (int f = 0; f < formats.length; f++) {
					formatList.add(formats[f]);
					if ((defaultFormatName != null) && defaultFormatName.equals(formats[f].getDescription()))
						defaultFormat = formats[f]; 
					if ((selectedFormat == null) && formats[f].equals(format))
						selectedFormat = formats[f];
					if ((acceptingFormat == null) && formats[f].accept(new File(docName)))
						acceptingFormat = formats[f];
				}
			}
			
			DocumentFormat[] formats = ((DocumentFormat[]) formatList.toArray(new DocumentFormat[formatList.size()]));
			
			this.formatSelector.removeItemListener(this.formatListener);
			
			this.formatSelector.setModel(new DefaultComboBoxModel(formats));
			
			if ((defaultFormat != null) && ((selectedFormat == null) || enforceDefaultFormat))
				this.formatSelector.setSelectedItem(defaultFormat);
			else if (selectedFormat != null)
				this.formatSelector.setSelectedItem(selectedFormat);
			else if (acceptingFormat != null)
				this.formatSelector.setSelectedItem(acceptingFormat);
			
			this.formatSelector.addItemListener(this.formatListener);
			
			this.formatListener.itemStateChanged(null);
			
			this.previewWindow.setVisible(true);
		}
		
		private Runnable refresher = null;
		private void refreshDisplay() {
			if (this.refresher != null) return;
			this.refresher = new Runnable() {
				public void run() {
					DocumentFormat format = ((DocumentFormat) formatSelector.getSelectedItem());
					if (format == null)
						JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "Cannot show output preview, there is no output format available.", "Cannot Show Preview", JOptionPane.INFORMATION_MESSAGE);
					
					else try {
						DocumentOutputStream out = new DocumentOutputStream(format.getFormatDefaultEncodingName());
						format.saveDocument(parent, out);
						out.flush();
						out.close();
						
						SimpleAttributeSet textFontStyle = new SimpleAttributeSet();
						textFontStyle.addAttribute(StyleConstants.FontConstants.Family, parent.getTextFontName());
						textFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(parent.getTextFontSize()));
						textFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, parent.getTextFontColor());
						textFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
						out.doc.setCharacterAttributes(0, out.doc.getLength(), textFontStyle, true);
						
						display.setDocument(out.doc);
						display.validate();
					}
					catch (IOException ioe) {
						System.out.println("PreviewDialog.refreshDisplay: " + ioe.getClass().getName() + " - " + ioe.getMessage());
					}
					finally {
						refresher = null;
					}
				}
			};
			SwingUtilities.invokeLater(this.refresher);
		}
		
		private static class DocumentInputStream extends InputStream {
			private StyledDocument doc;
			private int docLength;
			private int docPosition = 0;
			
			private ByteBuffer byteBuffer;
			private int bbPosition = 0;
			private Charset encoding = Charset.forName("UTF-8");
			
			public DocumentInputStream(StyledDocument doc) {
				this.doc = doc;
				this.docLength = this.doc.getLength();
				this.byteBuffer = this.encoding.encode(CharBuffer.wrap("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"));
			}
			
			public int read() throws IOException {
				if ((this.byteBuffer == null) || (this.bbPosition == this.byteBuffer.limit())) {
					boolean firstRead = (this.docPosition == 0);
					String data = this.read(1024);
					if (data == null) return -1;
					
					if (firstRead)
						data = data.substring(data.indexOf('<'));
					
					this.byteBuffer = this.encoding.encode(CharBuffer.wrap(data));
					this.bbPosition = 0;
				}
				
				if ((this.byteBuffer == null) || (this.bbPosition == this.byteBuffer.limit())) return -1;
				else return this.byteBuffer.get(this.bbPosition++);
			}
			
			private String read(int len) {
				int readableLen = Math.min(len, (this.docLength - this.docPosition));
				if (readableLen < 1) return null;
				else try {
					String data = this.doc.getText(this.docPosition, readableLen);
					this.docPosition += data.length();
					return data;
				} catch (BadLocationException e) {
					return null;
				}
			}
		}
		
		private static class DocumentOutputStream extends OutputStream {
			private static final int bufferSize = 1024;
			StyledDocument doc = new DefaultStyledDocument();
			private byte[] buffer = new byte[bufferSize];
			private int bufferLevel = 0;
			private int written = 0;
			private Charset charSet;
			public DocumentOutputStream(String encodingName) {
				try {
					this.charSet = Charset.forName(encodingName);
				} catch (IllegalArgumentException e) {}
			}
			
			/* (non-Javadoc)
			 * @see java.io.OutputStream#write(int)
			 */
			public synchronized void write(int b) throws IOException {
				
				//	buffer full, add to document
				if (this.bufferLevel == bufferSize) {
					
					//	buffer full for first time, check for byte order marks
					if (this.bufferLevel == this.written) {
						
						if ((this.buffer[0] == 239) && (this.buffer[1] == 187) && (this.buffer[2] == 191)) {
							if (this.charSet == null) try {
								this.charSet = Charset.forName("UTF-8");
							} catch (IllegalArgumentException e) {}
							System.arraycopy(this.buffer, 3, this.buffer, 0, (this.bufferLevel - 3));
							this.bufferLevel -= 3;
						}
						else if ((this.buffer[0] == 255) && (this.buffer[1] == 254)) {
							if (this.charSet == null) try {
								this.charSet = Charset.forName("UTF-16LE");
							} catch (IllegalArgumentException e) {}
							System.arraycopy(this.buffer, 2, this.buffer, 0, (this.bufferLevel - 2));
							this.bufferLevel -= 2;
							
						}
						else if ((this.buffer[0] == 254) && (this.buffer[1] == 255)) {
							if (this.charSet == null) try {
								this.charSet = Charset.forName("UTF-16BE");
							} catch (IllegalArgumentException e) {}
							System.arraycopy(this.buffer, 2, this.buffer, 0, (this.bufferLevel - 2));
							this.bufferLevel -= 2;
							
						}
						else this.flushBuffer();
					}
					else this.flushBuffer();
				}
				
				this.buffer[this.bufferLevel++] = ((byte) b);
				this.written++;
			}
			
			/* (non-Javadoc)
			 * @see java.io.OutputStream#flush()
			 */
			public void flush() throws IOException {
				this.flushBuffer();
			}
			
			private synchronized void flushBuffer() throws IOException {
				String toAdd;
				
				if (this.charSet == null) {
					StringBuffer sb = new StringBuffer(this.bufferLevel);
					for (int b = 0; b < this.bufferLevel; b++) sb.append((char) this.buffer[b]);
					toAdd = sb.toString();
				}
				else toAdd = this.charSet.decode(ByteBuffer.wrap(this.buffer, 0, this.bufferLevel)).toString();
				
				this.bufferLevel = 0;
				
				try {
					this.doc.insertString(this.doc.getLength(), toAdd, null);
				}
				catch (BadLocationException ble) {
					throw new IOException(ble.getMessage());
				}
			}
		}
		
		//	THE SCHEMA VALIDATION CODE IS EXPERIMENTAL !!!
		private SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		
		private StringVector schemaNames = new StringVector();
		private String selectedSchemaName = null;
		private HashMap schemaCache = new HashMap();
		
		private static final String[] noSchemas = {"<no schemas loaded so far>"};
		
		private void selectSchema() {
			new SchemaDialog().setVisible(true);
		}
		
		private class SchemaDialog extends DialogPanel {
			private JComboBox schemaSelector = new JComboBox();
			
			SchemaDialog() {
				super(previewWindow, "Select XML Schema", true);
				
				this.schemaSelector.setBorder(BorderFactory.createLoweredBevelBorder());
				this.schemaSelector.setEditable(false);
				this.refreshSchemaSelector(selectedSchemaName);
				
				JPanel schemaSelectorPanel = new JPanel(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.weightx = 0;
				gbc.weighty = 0;
				gbc.gridwidth = 1;
				gbc.gridheight = 1;
				gbc.insets.top = 2;
				gbc.insets.bottom = 2;
				gbc.insets.left = 3;
				gbc.insets.right = 3;
				gbc.fill = GridBagConstraints.HORIZONTAL;
				gbc.gridx = 0;
				gbc.gridy = 0;
				schemaSelectorPanel.add(new JLabel("Select XML Schema"), gbc.clone());
				gbc.gridx++;
				gbc.weightx = 1;
				schemaSelectorPanel.add(this.schemaSelector, gbc.clone());
				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
				cancelButton.setPreferredSize(new Dimension(100, 21));
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				
				JButton okButton = new JButton("OK");
				okButton.setBorder(BorderFactory.createRaisedBevelBorder());
				okButton.setPreferredSize(new Dimension(100, 21));
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Object sno = schemaSelector.getSelectedItem();
						selectedSchemaName = ((sno == null) ? null : sno.toString());
						schemaLabel.setText((sno == null) ? defaultSchemaLabelText : (selectedSchemaName + " (click to change)"));
						dispose();
					}
				});
				
				//	create button
				JButton loadFileButton = new JButton("Load File");
				loadFileButton.setBorder(BorderFactory.createRaisedBevelBorder());
				loadFileButton.setPreferredSize(new Dimension(100, 21));
				
				//	create file chooser and action listener
				try {
					final JFileChooser schemaFileChooser = new JFileChooser();
					schemaFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					schemaFileChooser.addChoosableFileFilter(new FileFilter() {
						public boolean accept(File file) {
							return ((file != null) && (file.isDirectory() || file.getName().toLowerCase().endsWith(".xsd")));
						}
						public String getDescription() {
							return "XML Schema Definition Files";
						}
					});
					schemaFileChooser.setAcceptAllFileFilterUsed(false);
					
					loadFileButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							if (schemaFileChooser.showOpenDialog(SchemaDialog.this) == JFileChooser.APPROVE_OPTION) try {
								File schemaFile = schemaFileChooser.getSelectedFile();
								InputStream is = new FileInputStream(schemaFile);
								try {
									loadSchema(is, schemaFile.getAbsolutePath());
								}
								catch (Exception e) {
									JOptionPane.showMessageDialog(SchemaDialog.this, ("An error occurred while loading the XML Schema from the specified file:\n" + e.getClass().getName() + " (" + e.getMessage() + ")"), "Error Loading Schema From File", JOptionPane.ERROR_MESSAGE);
								}
								is.close();
							}
							catch (IOException ioe) {
								JOptionPane.showMessageDialog(SchemaDialog.this, ("An error occurred while loading the XML Schema from the specified file:\n" + ioe.getClass().getName() + " (" + ioe.getMessage() + ")"), "Error Loading Schema From File", JOptionPane.ERROR_MESSAGE);
							}
						}
					});
				}
				
				//	if running in an applet, attempt of creating a JFileChooser will throw a SecurityException
				catch (SecurityException se) {
					loadFileButton.setEnabled(false);
				}
				
				JButton loadUrlButton = new JButton("Load URL");
				loadUrlButton.setBorder(BorderFactory.createRaisedBevelBorder());
				loadUrlButton.setPreferredSize(new Dimension(100, 21));
				loadUrlButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String schemaUrl = JOptionPane.showInputDialog(SchemaDialog.this, "Please enter the URL to load an XML Schema from", "Enter Schema URL", JOptionPane.QUESTION_MESSAGE);
						if (schemaUrl != null) try {
							InputStream is = new URL(schemaUrl).openStream();
							try {
								loadSchema(is, schemaUrl);
							} catch (Exception e) {
								JOptionPane.showMessageDialog(SchemaDialog.this, ("An error occurred while loading the XML Schema from the specified URL:\n" + e.getClass().getName() + " (" + e.getMessage() + ")"), "Error Loading Schema From URL", JOptionPane.ERROR_MESSAGE);
							}
							is.close();
						} catch (Exception ioe) {
							JOptionPane.showMessageDialog(SchemaDialog.this, ("An error occurred while loading the XML Schema from the specified URL:\n" + ioe.getClass().getName() + " (" + ioe.getMessage() + ")"), "Error Loading Schema From URL", JOptionPane.ERROR_MESSAGE);
						}
					}
				});
				
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
				buttonPanel.add(cancelButton);
				buttonPanel.add(okButton);
				buttonPanel.add(new JLabel("    ")); // spacer between buttons
				buttonPanel.add(loadFileButton);
				buttonPanel.add(loadUrlButton);
				
				this.getContentPane().setLayout(new BorderLayout());
				this.getContentPane().add(schemaSelectorPanel, BorderLayout.CENTER);
				this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
				
				this.setSize(500, 90);
				this.setLocationRelativeTo(PreviewWindow.this);
			}
			
			private void refreshSchemaSelector(String selSchemaName) {
				if (schemaNames.isEmpty()) {
					this.schemaSelector.setModel(new DefaultComboBoxModel(noSchemas));
					this.schemaSelector.setEnabled(false);
				}
				else {
					this.schemaSelector.setModel(new DefaultComboBoxModel(schemaNames.toStringArray()));
					this.schemaSelector.setEnabled(true);
					if (selSchemaName != null)
						this.schemaSelector.setSelectedItem(selSchemaName);
				}
			}
			
			private void loadSchema(InputStream source, String schemaName) throws Exception {
			    Source schemaFile = new StreamSource(source);
				Schema schema = factory.newSchema(schemaFile);
				
				schemaNames.addElementIgnoreDuplicates(schemaName);
				schemaNames.sortLexicographically();
				schemaCache.put(schemaName, schema);
				this.refreshSchemaSelector(schemaName);
			}
		}
	}
	
	void configureDisplay() {
		this.annotationEditor.configureDisplay();
	}
	
	/**	save the document displayed in this DocumentEditor
	 * @return true if and only if the content of this DocumentEditor was saved as a result of the call to this method
	 */
	boolean saveContent() {
		try {
			if (this.lastSaveOperation != null) {
				this.lastSaveOperation.saveDocument(this);
				this.resetContentModified();
				this.writeLog("Document Saved");
				this.host.documentSaved(this, null);
				if (this.truncateUndoHistory(undoHistorySavedMaxItemCount, undoHistorySavedMaxItemAge))
					this.displayUndoHistory();
				return true;
			}
			else return this.saveContentAs(null);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), (e.getClass().getName() + ": " + e.getMessage() + "\nwhile saving document."), "Error Saving Document", JOptionPane.ERROR_MESSAGE);
			this.lastSaveOperation = null;
			return false;
		}
	}
	
	/**	save the document displayed in this DocumentEditor
	 * @param	saveOperation	the save operation to use 
	 * @return true if and only if the content of this DocumentEditor was saved as a result of the call to this method
	 */
	public boolean saveContent(DocumentSaveOperation saveOperation) {
		String newName = saveOperation.saveDocument(this);
		if (newName != null) {
			this.documentName = newName;
			this.lastSaveOperation = saveOperation;
			this.documentFormat = saveOperation.getDocumentFormat();
			this.resetContentModified();
			this.writeLog("Document Saved As '" + this.documentName + "'");
			this.host.documentSaved(this, newName);
			if (this.truncateUndoHistory(undoHistorySavedMaxItemCount, undoHistorySavedMaxItemAge))
				this.displayUndoHistory();
			return true;
		}
		else {
			this.host.documentSaved(this, null);
			if (this.truncateUndoHistory(undoHistorySavedMaxItemCount, undoHistorySavedMaxItemAge))
				this.displayUndoHistory();
			return true;
		}
	}
	
	/**	save the document displayed in this DocumentEditor
	 * @param	documentSaver	the DocumentSaver to obtain a save operation from
	 * @return true if and only if the content of this DocumentEditor was saved as a result of the call to this method
	 */
	public boolean saveContentAs(DocumentSaver documentSaver) {
		DocumentSaver saver = documentSaver;
		if (saver == null) {
			DocumentSaver[] savers = this.host.getDocumentSavers();
			String[] saverNames = new String[savers.length];
			StringVector choosableSaverNames = new StringVector();
			for (int s = 0; s < savers.length; s++) {
				JMenuItem mi = savers[s].getSaveDocumentMenuItem();
				if (mi == null) saverNames[s] = "";
				else {
					saverNames[s] = mi.getText();
					choosableSaverNames.addElement(saverNames[s]);
				}
			}
			Object o = JOptionPane.showInputDialog(this, "Please select how to save the document.", "Select Saving Method", JOptionPane.QUESTION_MESSAGE, null, choosableSaverNames.toStringArray(), null);
			for (int s = 0; s < savers.length; s++)
				if (saverNames[s].equals(o)) saver = savers[s];
		}
		
		if (saver == null)
			return false;
		
		DocumentSaveOperation saveOperation;
		if (this.lastSaveOperation == null)
			saveOperation = saver.getSaveOperation(this.documentName, this.documentFormat);
		else saveOperation = saver.getSaveOperation(this.lastSaveOperation);
		if (saveOperation != null)
			return this.saveContent(saveOperation);
		else return false;
	}
	
	/**	save individual parts of the document displayed in this DocumentEditor
	 * @param	documentSaver	the DocumentSaver to use for saving
	 */
	public void saveContentParts(DocumentSaver documentSaver) {
		DocumentSaver saver = documentSaver;
		if (saver == null) {
			DocumentSaver[] savers = this.host.getDocumentSavers();
			Object o = JOptionPane.showInputDialog(this, "Please select how to save the document.", "Select Saving Method", JOptionPane.QUESTION_MESSAGE, null, savers, null);
			if (o != null) saver = ((DocumentSaver) o);
		}
		if (saver != null) saver.saveDocumentParts(this, this.documentFormat, this.content.getType());
	}
	
	/**	write some entry to the program log
	 * @param	entry	the text to write
	 */
	void writeLog(String entry) {
		this.host.writeLog(this.getTitle() + ": " + entry);
	}
	
	//	the store for observers
	private Vector documentObservers = new Vector();
	
	/** add a DocumentObserver to this DocumentEditor so it is notified if the document changes
	 * @param	observer	the DocumentObserver to add	
	 */
	public void addDocumentObserver(DocumentObserver observer) {
		if (observer != null) this.documentObservers.addElement(observer);
	}
	
	/** remove a DocumentObserver from this DocumentEditor so it is no mor notified if the document changes
	 * @param	observer	the DocumentObserver to add	
	 */
	public void removeDocumentObserver(DocumentObserver observer) {
		this.documentObservers.remove(observer);
	}
	
	void notifyDocumentTextModified() {
		this.contentModified = true;
		for (int l = 0; l < this.documentObservers.size(); l++)
			((DocumentObserver) this.documentObservers.get(l)).documentTextModified();
	}
	
	void notifyDocumentMarkupModified() {
		this.contentModified = true;
		for (int l = 0; l < this.documentObservers.size(); l++)
			((DocumentObserver) this.documentObservers.get(l)).documentMarkupModified();
	}
	
	/** @return the font color for displaying XML tags
	 */
	public Color getTagFontColor() {
//		return ((this.parent == null) ? ((this.tagFontColor == null) ? TAG_FONT_COLOR : this.tagFontColor) : this.parent.getTagFontColor());
		return ((this.tagFontColor == null) ? TAG_FONT_COLOR : this.tagFontColor);
	}
	
	/** set the font color for displaying XML tags
	 * @param	tagFontColor	the new font color for displaying XML tags
	 */
	public void setTagFontColor(Color tagFontColor) {
//		if (this.parent == null) {
//			this.tagFontColor = tagFontColor;
//			this.refreshDisplay();
//		}
//		else this.parent.setTagFontColor(tagFontColor);
		this.tagFontColor = tagFontColor;
		this.refreshDisplay();
	}
	
	/** @return the name of the font for displaying XML tags
	 */
	public String getTagFontName() {
//		return ((this.parent == null) ? ((this.tagFontName == null) ? TAG_FONT_NAME : this.tagFontName) : this.parent.getTagFontName());
		return ((this.tagFontName == null) ? TAG_FONT_NAME : this.tagFontName);
	}
	
	/** set the font for displaying XML tags
	 * @param	tagFontName		the name of the new font for displaying XML tags
	 */
	public void setTagFontName(String tagFontName) {
//		if (this.parent == null) {
//			this.tagFontName = tagFontName;
//			this.refreshDisplay();
//		}
//		else this.parent.setTagFontName(tagFontName);
		this.tagFontName = tagFontName;
		this.refreshDisplay();
	}
	
	/** @return the font size for displaying XML tags
	 */
	public int getTagFontSize() {
//		return ((this.parent == null) ? ((this.tagFontSize == 0) ? TAG_FONT_SIZE : this.tagFontSize) : this.parent.getTagFontSize());
		return ((this.tagFontSize == 0) ? TAG_FONT_SIZE : this.tagFontSize);
	}
	
	/** set the font size for displaying XML tags
	 * @param	tagFontSize		the new font size for displaying XML tags
	 */
	public void setTagFontSize(int tagFontSize) {
//		if (this.parent == null) {
//			this.tagFontSize = tagFontSize;
//			this.refreshDisplay();
//		}
//		else this.parent.setTagFontSize(tagFontSize);
		this.tagFontSize = tagFontSize;
		this.refreshDisplay();
	}
	
	/** @return the font color for displaying document text
	 */
	public Color getTextFontColor() {
//		return ((this.parent == null) ? ((this.textFontColor == null) ? TEXT_FONT_COLOR : this.textFontColor) : this.parent.getTextFontColor());
		return ((this.textFontColor == null) ? TEXT_FONT_COLOR : this.textFontColor);
	}
	
	/** set the font color for displaying document text
	 * @param	textFontColor	the new font color for displaying document text
	 */
	public void setTextFontColor(Color textFontColor) {
//		if (this.parent == null) {
//			this.textFontColor = textFontColor;
//			this.refreshDisplay();
//		}
//		else this.parent.setTextFontColor(textFontColor);
		this.textFontColor = textFontColor;
		this.refreshDisplay();
	}
	
	/** @return the name of the font for displaying document text
	 */
	public String getTextFontName() {
//		return ((this.parent == null) ? ((this.textFontName == null) ? TEXT_FONT_NAME : this.textFontName) : this.parent.getTextFontName());
		return ((this.textFontName == null) ? TEXT_FONT_NAME : this.textFontName);
	}
	
	/** set the font color for displaying document text
	 * @param	textFontName	the name of the new font for displaying document text
	 */
	public void setTextFontName(String textFontName) {
//		if (this.parent == null) {
//			this.textFontName = textFontName;
//			this.refreshDisplay();
//		}
//		else this.parent.setTextFontName(textFontName);
		this.textFontName = textFontName;
		this.refreshDisplay();
	}
	
	/** @return the font size for displaying document text
	 */
	public int getTextFontSize() {
//		return ((this.parent == null) ? ((this.textFontSize == 0) ? TEXT_FONT_SIZE : this.textFontSize) : this.parent.getTextFontSize());
		return ((this.textFontSize == 0) ? TEXT_FONT_SIZE : this.textFontSize);
	}
	
	/** set the font size for displaying document text
	 * @param	textFontSize	the new font size for displaying document text
	 */
	public void setTextFontSize(int textFontSize) {
//		if (this.parent == null) {
//			this.textFontSize = textFontSize;
//			this.refreshDisplay();
//		}
//		else this.parent.setTextFontSize(textFontSize);
		this.textFontSize = textFontSize;
		this.refreshDisplay();
	}
	
	public JButton getEditFontsButton() {
		return this.getEditFontsButton(null, null, null);
	}
	
	public JButton getEditFontsButton(String text) {
		return this.getEditFontsButton(text, null, null);
	}
	
	public JButton getEditFontsButton(Dimension dimension) {
		return this.getEditFontsButton(null, dimension, null);
	}
	
	public JButton getEditFontsButton(Border border) {
		return this.getEditFontsButton(null, null, border);
	}
	
	public JButton getEditFontsButton(String text, Dimension dimension, Border border) {
//		return ((this.parent == null) ? new EditFontsButton(this, text, dimension, border) : this.parent.getEditFontsButton(text, dimension, border));
		return new EditFontsButton(this, text, dimension, border);
	}
	
	public boolean editFonts() {
//		if (this.parent == null) {
//			FontDialog fd = new FontDialog();
//			fd.setVisible(true);
//			if (fd.committed) {
//				boolean dirty = false;
//				if (fd.useTextDefaults.isSelected()) {
//					dirty = dirty || (this.getTextFontName() != TEXT_FONT_NAME);
//					this.setTextFontName(null);
//					this.setTextFontSize(0);
//					this.setTextFontColor(null);
//				}
//				else if (fd.textFont.isDirty()) {
//					this.setTextFontName(fd.textFont.getFontName());
//					this.setTextFontSize(fd.textFont.getFontSize());
//					this.setTextFontColor(fd.textFont.getFontColor());
//					dirty = true;
//				}
//				if (fd.useTextDefaults.isSelected()) {
//					dirty = dirty || (this.getTagFontName() != TAG_FONT_NAME);
//					this.setTagFontName(null);
//					this.setTagFontSize(0);
//					this.setTagFontColor(null);
//				}
//				else if (fd.tagFont.isDirty()) {
//					this.setTagFontName(fd.tagFont.getFontName());
//					this.setTagFontSize(fd.tagFont.getFontSize());
//					this.setTagFontColor(fd.tagFont.getFontColor());
//					dirty = true;
//				}
//				if (dirty) {
//					this.refreshDisplay();
//					return true;
//				}
//			}
//			return false;
//		}
//		else if (this.parent.editFonts()) {
//			this.refreshDisplay();
//			return true;
//		}
//		else return false;
		FontDialog fd = new FontDialog();
		fd.setVisible(true);
		if (fd.committed) {
			boolean dirty = false;
			if (fd.useTextDefaults.isSelected()) {
				dirty = dirty || (this.getTextFontName() != TEXT_FONT_NAME);
				this.setTextFontName(null);
				this.setTextFontSize(0);
				this.setTextFontColor(null);
			}
			else if (fd.textFont.isDirty()) {
				this.setTextFontName(fd.textFont.getFontName());
				this.setTextFontSize(fd.textFont.getFontSize());
				this.setTextFontColor(fd.textFont.getFontColor());
				dirty = true;
			}
			if (fd.useTextDefaults.isSelected()) {
				dirty = dirty || (this.getTagFontName() != TAG_FONT_NAME);
				this.setTagFontName(null);
				this.setTagFontSize(0);
				this.setTagFontColor(null);
			}
			else if (fd.tagFont.isDirty()) {
				this.setTagFontName(fd.tagFont.getFontName());
				this.setTagFontSize(fd.tagFont.getFontSize());
				this.setTagFontColor(fd.tagFont.getFontColor());
				dirty = true;
			}
			if (dirty) {
				this.refreshDisplay();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * dialog for editing display layout 
	 * 
	 * @author sautter
	 */
	private class FontDialog extends DialogPanel {
		
		FontEditorPanel textFont;
		JCheckBox useTextDefaults = new JCheckBox("Use Default Text Layout Settings");
		FontEditorPanel tagFont;
		JCheckBox useTagDefaults = new JCheckBox("Use Default Tag Layout Settings");
		
		boolean committed = false;
		
		FontDialog() {
			super("Edit Fonts", true);
			
			//	initialize main buttons
			JButton commitButton = new JButton("OK");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 25));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					committed = true;
					dispose();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 25));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	initialize editors
			this.textFont = new FontEditorPanel("Text Font", getTextFontName(), getTextFontSize(), getTextFontColor());
			this.useTextDefaults.setSelected(getTextFontName() == DocumentEditor.getDefaultTextFontName());
			this.tagFont = new FontEditorPanel("Tag Font", getTagFontName(), getTagFontSize(), getTagFontColor());
			this.useTagDefaults.setSelected(getTagFontName() == DocumentEditor.getDefaultTagFontName());
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new GridLayout(0, 1));
			
			this.getContentPane().add(this.textFont);
			this.getContentPane().add(this.useTextDefaults);
			
			this.getContentPane().add(this.tagFont);
			this.getContentPane().add(this.useTagDefaults);
			
			this.getContentPane().add(mainButtonPanel);
			
			this.setResizable(false);
			this.setSize(new Dimension(400, 200));
			this.setLocationRelativeTo(DocumentEditor.this);
		}
	}
	
	/**
	 * get help
	 * @param dataBaseUrl the path where external help files are located
	 * @return a hirarchy of HelpChapters explaining the functionality of a
	 *         DocumentEditor
	 */
	public static HelpChapter getHelp(String dataBaseUrl) {
		if (!dataBaseUrl.endsWith("/")) dataBaseUrl += "/";
		
		HelpChapter help = new DynamicHelpChapter("Document Editor", (dataBaseUrl + "DocumentEditor.html"));
		help.addSubChapter(AnnotationEditorPanel.getHelp(dataBaseUrl));
		return help;
	}
}
