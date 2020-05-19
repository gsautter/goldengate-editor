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
 *     * Neither the name of the Universitaet Karlsruhe (TH) nor the
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
package de.uka.ipd.idaho.goldenGate;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import de.uka.ipd.idaho.easyIO.help.DynamicHelpChapter;
import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.AnnotationUtils;
import de.uka.ipd.idaho.gamta.CharSequenceListener;
import de.uka.ipd.idaho.gamta.CharSequenceUtils;
import de.uka.ipd.idaho.gamta.DocumentRoot;
import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.gamta.MutableCharSequence;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.StandaloneAnnotation;
import de.uka.ipd.idaho.gamta.Token;
import de.uka.ipd.idaho.gamta.TokenSequence;
import de.uka.ipd.idaho.gamta.TokenSequenceListener;
import de.uka.ipd.idaho.gamta.TokenSequenceUtils;
import de.uka.ipd.idaho.gamta.MutableCharSequence.CharSequenceEvent;
import de.uka.ipd.idaho.gamta.MutableTokenSequence.TokenSequenceEvent;
import de.uka.ipd.idaho.gamta.util.gPath.GPath;
import de.uka.ipd.idaho.gamta.util.gPath.exceptions.GPathException;
import de.uka.ipd.idaho.gamta.util.swing.DocumentSynchronizer;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilter;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilterManager;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSourceManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.MonitorableDocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.Resource;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceSplashScreen;
import de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel;
import de.uka.ipd.idaho.goldenGate.util.AnnotationTools;
import de.uka.ipd.idaho.goldenGate.util.AttributeEditorDialog;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.stringUtils.StringVector;


/**
 * @author sautter
 */
public class AnnotationEditorPanel extends JPanel implements GoldenGateConstants {
	
	private static boolean tagNewAnnotations = false;
	private static boolean highlightNewAnnotations = true;
	private static boolean tagNewDocParts = true;
	private static boolean highlightNewDocParts = false;
	private static final String TAG_NEW_ANNOTATIONS = "TAG_ANNOTATIONS";
	private static final String HIGHLIGHT_NEW_ANNOTATIONS = "HIGHLIGHT_ANNOTATIONS";
	private static final String TAG_NEW_DOCPARTS = "TAG_DOCPARTS";
	private static final String HIGHLIGHT_NEW_DOCPARTS = "HIGHLIGHT_DOCPARTS";
	private static final String DOCPART_TYPES = "DOCPART_TYPES";
	
	private static boolean annotateAllCaseSensitive = true;
	private static final String ANNOTATE_ALL_CASE_SENSITIVE = "AA_CASE_SENSITIVE";
	
	private static boolean showAdvancedFilters = true;
	private static final String SHOW_ADVANCED_FILTERS = "SHOW_ADVANCED_FILTERS";
	
	private static boolean showRecentActions = true;
	private static final String SHOW_RECENT_ACTIONS = "SHOW_RECENT_ACTIONS";
	
	private static boolean showCustomFunctions = true;
	private static final String SHOW_CUSTOM_FUNCTIONS = "SHOW_CUSTOM_FUNCTIONS";
	
	private static boolean showDisplayControl = true;
	private static final String SHOW_DISPLAY_CONTROL = "SHOW_DISPLAY_CONTROL";
	
	private static boolean showFindReplace = true;
	private static final String SHOW_FIND_REPLACE = "SHOW_FIND_REPLACE";
	
	private static final String[] defaultStructuralTypes = {MutableAnnotation.SECTION_TYPE, MutableAnnotation.SUB_SECTION_TYPE, MutableAnnotation.SUB_SUB_SECTION_TYPE, MutableAnnotation.PARAGRAPH_TYPE, MutableAnnotation.SENTENCE_TYPE};
	private static final StringVector structuralTypes = new StringVector();
	
	private static boolean highlightNewAnnotations() {
		return highlightNewAnnotations;
	}
	
	private static void highlightNewAnnotations(boolean highlightNewAnnotations) {
		AnnotationEditorPanel.highlightNewAnnotations = highlightNewAnnotations;
	}
	
	private static boolean tagNewAnnotations() {
		return tagNewAnnotations;
	}
	
	private static void tagNewAnnotations(boolean tagNewAnnotations) {
		AnnotationEditorPanel.tagNewAnnotations = tagNewAnnotations;
	}
	
	private static boolean highlightNewDocParts() {
		return highlightNewDocParts;
	}
	
	private static void highlightNewDocParts(boolean highlightNewDocParts) {
		AnnotationEditorPanel.highlightNewDocParts = highlightNewDocParts;
	}
	
	private static boolean tagNewDocParts() {
		return tagNewDocParts;
	}
	
	private static void tagNewDocParts(boolean tagNewDocParts) {
		AnnotationEditorPanel.tagNewDocParts = tagNewDocParts;
	}
	
	static String[] getStructuralTypes() {
		structuralTypes.sortLexicographically();
		return structuralTypes.toStringArray();
	}
	
	static void setStructuralTypes(String[] structuralTypes) {
		AnnotationEditorPanel.structuralTypes.clear();
		AnnotationEditorPanel.structuralTypes.addContentIgnoreDuplicates(structuralTypes);
		AnnotationEditorPanel.structuralTypes.addContentIgnoreDuplicates(defaultStructuralTypes);
	}
	
	private static boolean annotateAllCaseSensitive() {
		return annotateAllCaseSensitive;
	}
	
	private static void annotateAllCaseSensitive(boolean caseSensitive) {
		annotateAllCaseSensitive = caseSensitive;
	}
	
	private static boolean showAdvancedFilters() {
		return showAdvancedFilters;
	}
	
	private static void showAdvancedFilters(boolean show) {
		showAdvancedFilters = show;
	}
	
	private static boolean showRecentActions() {
		return showRecentActions;
	}
	
	private static void showRecentActions(boolean show) {
		if (showRecentActions != show) {
			showRecentActions = show;
			for (int i = 0; i < instances.size(); i++) {
				AnnotationEditorPanel aep = ((AnnotationEditorPanel) instances.get(i));
				aep.layoutPanels();
			}
		}
	}
	
	private static boolean showCustomFunctions() {
		return showCustomFunctions;
	}
	
	private static void showCustomFunctions(boolean show) {
		if (showCustomFunctions != show) {
			showCustomFunctions = show;
			for (int i = 0; i < instances.size(); i++) {
				AnnotationEditorPanel aep = ((AnnotationEditorPanel) instances.get(i));
				aep.layoutPanels();
			}
		}
	}
	
	private static boolean showDisplayControl() {
		return showDisplayControl;
	}
	
	private static void showDisplayControl(boolean show) {
		if (showDisplayControl != show) {
			showDisplayControl = show;
			for (int i = 0; i < instances.size(); i++) {
				AnnotationEditorPanel aep = ((AnnotationEditorPanel) instances.get(i));
				aep.layoutPanels();
			}
		}
	}
	
	private static boolean showFindReplace() {
		return showFindReplace;
	}
	
	private static void showFindReplace(boolean show) {
		if (showFindReplace != show) {
			showFindReplace = show;
			for (int i = 0; i < instances.size(); i++) {
				AnnotationEditorPanel aep = ((AnnotationEditorPanel) instances.get(i));
				aep.layoutPanels();
			}
		}
	}
	
	/**	obtain a panel for editing general document format settings
	 * @param	host	the GoldenGATE main instance (necessary due to static context)
	 * @return	a settings panel for editing the default settings in the GoldenGATE configuration
	 */
	static SettingsPanel getSettingsPanel(GoldenGATE host) {
		return new AnnotationEditorSettingsPanel(host);
	}
	
	private static class AnnotationEditorSettingsPanel extends SettingsPanel {
		
		private JCheckBox highlightAnnotations = new JCheckBox("Mark Annotations", highlightNewAnnotations());
		private JCheckBox tagAnnotations = new JCheckBox("Tag Annotations", tagNewAnnotations());
		private JCheckBox highlightDocParts = new JCheckBox("Mark Structure", highlightNewDocParts());
		private JCheckBox tagDocParts = new JCheckBox("Tag Structure", tagNewDocParts());
		
		private JCheckBox annotateAllCaseSensitive = new JCheckBox("Annotate All Case Sensitive", annotateAllCaseSensitive());
		private JCheckBox showAdvancedFilters = new JCheckBox("Show Advanced Filters", showAdvancedFilters());
		
		private JCheckBox showRecentActions = new JCheckBox("Recent Actions", showRecentActions());
		private JCheckBox showCustomFunctions = new JCheckBox("Custom Functions", showCustomFunctions());
		private JCheckBox showFindReplace = new JCheckBox("Find / Replace", showFindReplace());
		private JCheckBox showDisplayControl = new JCheckBox("Display Control", showDisplayControl());
		
		AnnotationEditorSettingsPanel(GoldenGATE host) {
			super("Annotation Editor", "Configure layout and behavior of annotation editor here.");
			
			this.setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			this.add(this.highlightAnnotations, gbc.clone());
			gbc.gridx ++;
			this.add(this.tagAnnotations, gbc.clone());
			gbc.gridx ++;
			this.add(this.highlightDocParts, gbc.clone());
			gbc.gridx ++;
			this.add(this.tagDocParts, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.gridwidth = 2;
			this.add(this.annotateAllCaseSensitive, gbc.clone());
			gbc.gridx = 2;
			this.add(this.showAdvancedFilters, gbc.clone());
			
			gbc.gridy = 2;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			this.add(this.showRecentActions, gbc.clone());
			gbc.gridx ++;
			this.add(this.showCustomFunctions, gbc.clone());
			gbc.gridx ++;
			this.add(this.showFindReplace, gbc.clone());
			gbc.gridx ++;
			this.add(this.showDisplayControl, gbc.clone());
		}
		
		/* (non-Javadoc)
		 * @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			highlightNewAnnotations(this.highlightAnnotations.isSelected());
			tagNewAnnotations(this.tagAnnotations.isSelected());
			highlightNewDocParts(this.highlightDocParts.isSelected());
			tagNewDocParts(this.tagDocParts.isSelected());
			
			annotateAllCaseSensitive(this.annotateAllCaseSensitive.isSelected());
			showAdvancedFilters(this.showAdvancedFilters.isSelected());
			
			showFindReplace(this.showFindReplace.isSelected());
			showRecentActions(this.showRecentActions.isSelected());
			showCustomFunctions(this.showCustomFunctions.isSelected());
			showDisplayControl(this.showDisplayControl.isSelected());
		}
	}
	
	private static final String ANNOTATION_SUGGESTION_SETTINGS_PREFIX = "ASS";
	
	private static final String ATTRIBUTE_SUGGESTIONS_PREFIX = "a_t_t_r_i_b_u_t_e_s";
	private static final String VALUE_PREFIX = "v_a_l_u_e_";
	private static final String RESTRICT_TO_SUGGESTIONS = "r_e_s_t_r_i_c_t";
	
	private static Settings annotationSuggestionData = new Settings();
	
	private static StringVector typeSuggestions = new StringVector();
	
	private static StringVector globalAttributeSuggestions = new StringVector();
	private static StringVector allAttributeSuggestions = new StringVector();
	private static HashMap typeAttributeSuggestions = new HashMap();
	
	private static HashMap attributeValueSuggestions = new HashMap();
	
	private static void initAnnotationSuggestions(Settings as) {
		annotationSuggestionData.clear();
		annotationSuggestionData.setSettings(as);
		
		typeSuggestions.clear();
		globalAttributeSuggestions.clear();
		allAttributeSuggestions.clear();
		typeAttributeSuggestions.clear();
		attributeValueSuggestions.clear();
		
		typeSuggestions.addContent(annotationSuggestionData.getSubsetPrefixes());
		typeSuggestions.removeAll(ATTRIBUTE_SUGGESTIONS_PREFIX);
		typeSuggestions.removeAll(RESTRICT_TO_SUGGESTIONS);
		
		Settings globalAttributes = annotationSuggestionData.getSubset(ATTRIBUTE_SUGGESTIONS_PREFIX);
		globalAttributeSuggestions.addContent(globalAttributes.getSubsetPrefixes());
		allAttributeSuggestions.addContent(globalAttributeSuggestions);
		
		for (int a = 0 ; a < globalAttributeSuggestions.size(); a++) {
			String attribute = globalAttributeSuggestions.get(a);
			Settings attributeValues = globalAttributes.getSubset(attribute);
			
			StringVector values = ((StringVector) attributeValueSuggestions.get(ATTRIBUTE_SUGGESTIONS_PREFIX + "." + attribute));
			if (values == null) {
				values = new StringVector();
				attributeValueSuggestions.put((ATTRIBUTE_SUGGESTIONS_PREFIX + "." + attribute), values);
			}
			StringVector allValues = ((StringVector) attributeValueSuggestions.get(attribute));
			if (allValues == null) {
				allValues = new StringVector();
				attributeValueSuggestions.put(attribute, allValues);
			}
			
			for (int v = 0; v < attributeValues.size(); v++) {
				String value = attributeValues.getSetting(VALUE_PREFIX + v);
				if (value != null) {
					values.addElementIgnoreDuplicates(value);
					allValues.addElementIgnoreDuplicates(value);
				}
			}
		}
		
		for (int t = 0; t < typeSuggestions.size(); t++) {
			String type = typeSuggestions.get(t);
			Settings typeAttributes = annotationSuggestionData.getSubset(type);
			
			StringVector attributes = new StringVector();
			attributes.addContent(typeAttributes.getSubsetPrefixes());
			attributes.removeAll(VALUE_PREFIX);
			attributes.removeAll(RESTRICT_TO_SUGGESTIONS);
			
			allAttributeSuggestions.addContentIgnoreDuplicates(attributes);
			typeAttributeSuggestions.put(type, attributes);
			
			for (int a = 0 ; a < attributes.size(); a++) {
				String attribute = attributes.get(a);
				Settings attributeValues = typeAttributes.getSubset(attribute);
				
				StringVector values = ((StringVector) attributeValueSuggestions.get(type + "." + attribute));
				if (values == null) {
					values = new StringVector();
					attributeValueSuggestions.put((type + "." + attribute), values);
				}
				StringVector allValues = ((StringVector) attributeValueSuggestions.get(attribute));
				if (allValues == null) {
					allValues = new StringVector();
					attributeValueSuggestions.put(attribute, allValues);
				}
				
				for (int v = 0; v < attributeValues.size(); v++) {
					String value = attributeValues.getSetting(VALUE_PREFIX + v);
					if (value != null) {
						values.addElementIgnoreDuplicates(value);
						allValues.addElementIgnoreDuplicates(value);
					}
				}
			}
		}
		
		typeSuggestions.sortLexicographically(false, false);
		
		globalAttributeSuggestions.sortLexicographically(false, false);
		allAttributeSuggestions.sortLexicographically(false, false);
	}
	
	private static void storeAnnotationSuggestions(Settings as) {
		as.clear();
		as.setSettings(annotationSuggestionData);
	}
	
	/**	obtain suggestions for annotation types
	 * @return an array of annotation type suggestions, sorted lexicographically
	 */
	static String[] getAnnotationTypeSuggestions() {
		return typeSuggestions.toStringArray();
	}
	
	/**	obtain suggestions for attributes of annotations of a specific type
	 * @param	type	the annotation type (specifying null will return all attribute suggestions for all annotation types)
	 * @return an array of suggestions for attributes of annotations of the specified type, sorted lexicographically
	 */
	static String[] getAnnotationAttributeSuggestions(String type) {
		StringVector suggestions = new StringVector();
		
		if (type == null)
			suggestions.addContent(allAttributeSuggestions);
		
		else {
			suggestions.addContent(globalAttributeSuggestions);
			StringVector typeAttributes = ((StringVector) typeAttributeSuggestions.get(type));
			if (typeAttributes != null)
				suggestions.addContentIgnoreDuplicates(typeAttributes);
		}
		
		suggestions.sortLexicographically(false, false);
		return suggestions.toStringArray();
	}
	
	/**	obtain suggestions for the value of an attribute of an annotation of a specific type
	 * @param	type		the annotation type (specifying null will return all value suggestions for the specified attribute, for all annotation types)
	 * @param	attribute	the attribute to obtain value suggestions for
	 * @return an array of suggestions for the value of the specified attribute in the scope of annotations of the specified type, sorted lexicographically
	 */
	static String[] getAttributeValueSuggestions(String type, String attribute) {
		StringVector suggestions = new StringVector();
		StringVector attributeValues;
		
		if (type == null) {
			attributeValues = ((StringVector) attributeValueSuggestions.get(attribute));
			if (attributeValues != null)
				suggestions.addContentIgnoreDuplicates(attributeValues);
		}
		else {
			attributeValues = ((StringVector) attributeValueSuggestions.get(ATTRIBUTE_SUGGESTIONS_PREFIX + "." + attribute));
			if (attributeValues != null)
				suggestions.addContentIgnoreDuplicates(attributeValues);
			attributeValues = ((StringVector) attributeValueSuggestions.get(type + "." + attribute));
			if (attributeValues != null)
				suggestions.addContentIgnoreDuplicates(attributeValues);
		}
		
		suggestions.sortLexicographically(false, false);
		return suggestions.toStringArray();
	}
	
	/**	obtain a panel for editing annotation suggestion settings
	 * @param	host	the GoldenGATE main instance (necessary due to static context)
	 * @return	a settings panel for editing the annotation suggestion settings
	 */
	static SettingsPanel getAnnotationSuggestionSettingsPanel(GoldenGATE host) {
		return new AnnotationSuggestionSettingsPanel(host);
	}
	
	private static class AnnotationSuggestionSettingsPanel extends SettingsPanel {
		private static final String GLOBAL_ATTRIBUTES = "<Global Attributes>";
		
		private abstract class AnnotationSuggestionListModel extends AbstractListModel {
			public void fireContentsChanged() {
				super.fireContentsChanged(this, 0, this.getSize());
			}
			public void fireIntervalAdded() {
				super.fireIntervalAdded(this, 0, this.getSize());
			}
			public void fireIntervalRemoved() {
				super.fireIntervalRemoved(this, 0, this.getSize());
			}
		}
		
		private Settings content = new Settings();
		
		private StringVector types = new StringVector();
		private Settings typeAttributes = this.content.getSubset(ATTRIBUTE_SUGGESTIONS_PREFIX);
		private JList typeList = new JList();
		private AnnotationSuggestionListModel typeListModel = new AnnotationSuggestionListModel() {
			public Object getElementAt(int index) {
				return ((index == 0) ? GLOBAL_ATTRIBUTES : types.get(index - 1));
			}
			public int getSize() {
				return (types.size() + 1);
			}
		};
		private JCheckBox restrictTypes = new JCheckBox("Restrict to Specified Annotation Types?", false);
		private boolean typesDirty = false;
		private JButton addTypeButton = new JButton("Add Type");
		private JButton editTypeButton = new JButton("Edit Type");
		private JButton removeTypeButton = new JButton("Remove Type");
		
		private StringVector attributes = new StringVector();
		private Settings attributeValues;
		private JList attributeList = new JList();
		private AnnotationSuggestionListModel attributeListModel = new AnnotationSuggestionListModel() {
			public Object getElementAt(int index) {
				return attributes.get(index);
			}
			public int getSize() {
				return attributes.size();
			}
		};
		private JCheckBox restrictAttributes = new JCheckBox("Restrict to Specified Attributes?", false);
		private boolean attributesDirty = false;
		private JButton addAttributeButton = new JButton("Add Attribute");
		private JButton editAttributeButton = new JButton("Edit Attribute");
		private JButton removeAttributeButton = new JButton("Remove Attribute");
		
		private StringVector values = new StringVector();
		private JList valueList = new JList();
		private AnnotationSuggestionListModel valueListModel = new AnnotationSuggestionListModel() {
			public Object getElementAt(int index) {
				return values.get(index);
			}
			public int getSize() {
				return values.size();
			}
		};
		private JCheckBox restrictValues = new JCheckBox("Restrict to Specified Values?", false);
		private boolean valuesDirty = false;
		private JButton addValueButton = new JButton("Add Value");
		private JButton editValueButton = new JButton("Edit Value");
		private JButton removeValueButton = new JButton("Remove Value");
		
		private AnnotationSuggestionSettingsPanel(GoldenGATE host) {
			super("Annotation Suggestions", "Edit suggestions for annotation types, attributes, and values");
			this.setLayout(new GridBagLayout());
			
			//	initialize annotation type list
			this.typeList.setModel(this.typeListModel);
			this.typeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.typeList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() > 1)
						editType(typeList.getSelectedIndex());
					else selectType(typeList.getSelectedIndex());
				}
			});
			
			this.addTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.addTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addType();
				}
			});
			this.editTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.editTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editType(typeList.getSelectedIndex());
				}
			});
			this.removeTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.removeTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removeType(typeList.getSelectedIndex());
				}
			});
			JPanel typeListButtonPanel = new JPanel(new FlowLayout(), true);
			typeListButtonPanel.add(this.addTypeButton);
			typeListButtonPanel.add(this.editTypeButton);
			typeListButtonPanel.add(this.removeTypeButton);
			
			//	assemble editor for annotation types
			JPanel typeListPanel = new JPanel(new BorderLayout(), true);
			typeListPanel.add(this.restrictTypes, BorderLayout.NORTH);
			typeListPanel.add(new JScrollPane(this.typeList), BorderLayout.CENTER);
			typeListPanel.add(typeListButtonPanel, BorderLayout.SOUTH);
			
			
			//	initialize annotation attribute name list
			this.attributeList.setModel(this.attributeListModel);
			this.attributeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.attributeList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() > 1)
						editAttribute(attributeList.getSelectedIndex());
					else selectAttribute(attributeList.getSelectedIndex());
				}
			});
			
			this.addAttributeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.addAttributeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addAttribute();
				}
			});
			this.editAttributeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.editAttributeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editAttribute(attributeList.getSelectedIndex());
				}
			});
			this.removeAttributeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.removeAttributeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removeAttribute(attributeList.getSelectedIndex());
				}
			});
			JPanel attributeListButtonPanel = new JPanel(new FlowLayout(), true);
			attributeListButtonPanel.add(this.addAttributeButton);
			attributeListButtonPanel.add(this.editAttributeButton);
			attributeListButtonPanel.add(this.removeAttributeButton);
			
			//	assemble editor for annotation attributes
			JPanel attributeListPanel = new JPanel(new BorderLayout(), true);
			attributeListPanel.add(this.restrictAttributes, BorderLayout.NORTH);
			attributeListPanel.add(new JScrollPane(this.attributeList), BorderLayout.CENTER);
			attributeListPanel.add(attributeListButtonPanel, BorderLayout.SOUTH);
			
			//	initialize annotation attribute value list
			this.valueList.setModel(this.valueListModel);
			this.valueList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.valueList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() > 1)
						editValue(valueList.getSelectedIndex());
				}
			});
			
			this.addValueButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.addValueButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addValue();
				}
			});
			this.editValueButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.editValueButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editValue(valueList.getSelectedIndex());
				}
			});
			this.removeValueButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.removeValueButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removeValue(valueList.getSelectedIndex());
				}
			});
			JPanel valueListButtonPanel = new JPanel(new FlowLayout(), true);
			valueListButtonPanel.add(this.addValueButton);
			valueListButtonPanel.add(this.editValueButton);
			valueListButtonPanel.add(this.removeValueButton);
			
			//	assemble editor for annotation attribute values
			JPanel valueListPanel = new JPanel(new BorderLayout(), true);
			valueListPanel.add(this.restrictValues, BorderLayout.NORTH);
			valueListPanel.add(new JScrollPane(this.valueList), BorderLayout.CENTER);
			valueListPanel.add(valueListButtonPanel, BorderLayout.SOUTH);
			
			
			//	put the three editors together
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 5;
			gbc.insets.bottom = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			
			gbc.gridx = 0;
			this.add(typeListPanel, gbc.clone());
			gbc.gridx++;
			this.add(attributeListPanel, gbc.clone());
			gbc.gridx++;
			this.add(valueListPanel, gbc.clone());
			
			//	read data
			Settings set = new Settings();
			storeAnnotationSuggestions(set);
			this.setContent(set);
		}
		
		public void commitChanges() {
			if (this.typesDirty || this.attributesDirty || this.valuesDirty)
				initAnnotationSuggestions(this.getContent());
		}
		
		private void setContent(Settings set) {
			
			//	clear old content
			this.content.clear();
			this.types.clear();
			
			//	clear modification info
			this.typesDirty = false;
			this.attributesDirty = false;
			this.valuesDirty = false;
			
			//	copy new content
			this.content.setSettings(set);
			
			//	read restriction
			this.restrictTypes.setSelected(set.removeSetting(RESTRICT_TO_SUGGESTIONS) != null);
			
			//	read types
			this.types.addContent(this.content.getSubsetPrefixes());
			this.types.removeAll(ATTRIBUTE_SUGGESTIONS_PREFIX);
			this.types.sortLexicographically(false, false);
			this.typeListModel.fireContentsChanged();
			
			//	pre-select global attributes
			this.selectType(0);
		}
		
		private Settings getContent() {
			int index = this.typeList.getSelectedIndex();
			
			//	de-select type to force writing changes
			this.selectType(-1);
			
			//	copy content
			Settings set = new Settings();
			set.setSettings(this.content);
			
			//	remember restriction
			if (this.restrictTypes.isSelected())
				set.setSetting(RESTRICT_TO_SUGGESTIONS, RESTRICT_TO_SUGGESTIONS);
			
			//	re-select type previously selected
			this.selectType(index);
			
			//	return copy of content
			return set;
		}
		
		private void addType() {
			this.editType(-1);
		}
		
		private void editType(int index) {
			if (index == 0) return;
			String type = ((index < 0) ? null : this.types.get(index - 1));
			Object typeObject = JOptionPane.showInputDialog(this, (((type == null) ? "Create a new" : "Edit") + " annotation type ..."), (((type == null) ? "Create" : "Edit") + " Annotation Type ..."), JOptionPane.PLAIN_MESSAGE, null, null, type);
			if (typeObject != null) {
				type = typeObject.toString().trim();
				if (type.length() == 0) this.removeType(index);
				else if (index < 0) {
					if (!this.types.contains(type)) {
						this.types.addElement(type);
						this.types.sortLexicographically(false, false);
						this.typeListModel.fireIntervalAdded();
					}
					else this.typeListModel.fireContentsChanged();
					
					this.typeList.revalidate();
					this.selectType(this.types.indexOf(type) + 1);
				}
				else {
					this.types.setElementAt(type, (index - 1));
					this.typeAttributes.setPrefix(type);
					this.types.removeDuplicateElements(true);
					this.types.sortLexicographically(false, false);
					this.typeListModel.fireContentsChanged();
					
					this.typeList.revalidate();
					this.selectType(this.types.indexOf(type) + 1);
				}
				this.typesDirty = true;
			}
			this.editTypeButton.setEnabled(this.types.size() != 0);
			this.removeTypeButton.setEnabled(this.types.size() != 0);
		}
		
		private void removeType(int index) {
			if (index < 1) return;
			this.types.remove(index - 1);
			this.typeAttributes.clear();
			this.typeListModel.fireIntervalRemoved();
			this.typeList.revalidate();
			this.selectType(index - 1);
			
			this.typesDirty = true;
			this.editTypeButton.setEnabled(this.types.size() != 0);
			this.removeTypeButton.setEnabled(this.types.size() != 0);
		}
		
		private void selectType(int index) {
			this.typeList.setSelectedIndex(index);
			
			//	de-select attribute to make sure attribute and values are stored
			this.selectAttribute(-1);
			
			//	clear attribute list
			this.attributes.clear();
			
			//	add dummy attribute so type can exist without real attributes
			if ((this.typeAttributes != null) && this.typeAttributes.isEmpty())
				this.typeAttributes.setSetting(ATTRIBUTE_SUGGESTIONS_PREFIX, ATTRIBUTE_SUGGESTIONS_PREFIX);
			
			//	remember restriction
			if (this.restrictAttributes.isSelected() && (this.typeAttributes != null))
				this.typeAttributes.setSetting(RESTRICT_TO_SUGGESTIONS, RESTRICT_TO_SUGGESTIONS);
			
			//	de-select only
			if (index < 0) {
				this.typeAttributes = null;
				
				this.restrictAttributes.setSelected(false);
				this.restrictAttributes.setEnabled(false);
				
				this.editTypeButton.setEnabled(false);
				this.removeTypeButton.setEnabled(false);
				
				this.addAttributeButton.setEnabled(false);
			}
			
			//	change of selection
			else {
				
				//	read new type
				String type = ((index == 0) ? ATTRIBUTE_SUGGESTIONS_PREFIX : this.types.get(index - 1));
				
				//	get new set of attributes
				this.typeAttributes = this.content.getSubset(type);
				this.typeAttributes.removeSetting(ATTRIBUTE_SUGGESTIONS_PREFIX);
				if (index == 0) {
					this.restrictAttributes.setEnabled(false);
					this.restrictAttributes.setSelected(false);
				}
				else {
					this.restrictAttributes.setEnabled(true);
					this.restrictAttributes.setSelected(this.typeAttributes.removeSetting(RESTRICT_TO_SUGGESTIONS) != null);
				}
				this.attributes.addContent(this.typeAttributes.getSubsetPrefixes());
				this.attributes.sortLexicographically(false, false);
				
				this.editTypeButton.setEnabled(index != 0);
				this.removeTypeButton.setEnabled(index != 0);
				
				this.addAttributeButton.setEnabled(true);
			}
			
			//	update GUI
			this.attributeListModel.fireContentsChanged();
			this.attributeList.revalidate();
			this.selectAttribute(this.attributes.isEmpty() ? -1 : 0);
			
			this.editAttributeButton.setEnabled(this.attributes.size() != 0);
			this.removeAttributeButton.setEnabled(this.attributes.size() != 0);
		}
		
		private void addAttribute() {
			this.editAttribute(-1);
		}
		
		private void editAttribute(int index) {
			String attribute = ((index < 0) ? null : this.attributes.get(index));
			Object attributeObject = JOptionPane.showInputDialog(this, (((attribute == null) ? "Create a new" : "Edit") + " annotation attribute ..."), (((attribute == null) ? "Create" : "Edit") + " Annotation Attribute ..."), JOptionPane.PLAIN_MESSAGE, null, null, attribute);
			if (attributeObject != null) {
				attribute = attributeObject.toString().trim();
				if (attribute.length() == 0) this.removeAttribute(index);
				else if (index < 0) {
					if (!this.attributes.contains(attribute)) {
						this.attributes.addElement(attribute);
						this.attributes.sortLexicographically(false, false);
						this.attributeListModel.fireIntervalAdded();
					}
					else this.attributeListModel.fireContentsChanged();
					
					this.attributeList.revalidate();
					this.selectAttribute(this.attributes.indexOf(attribute));
				}
				else {
					this.attributes.setElementAt(attribute, index);
					this.attributeValues.setPrefix(attribute);
					this.attributes.removeDuplicateElements(true);
					this.attributes.sortLexicographically(false, false);
					this.attributeListModel.fireContentsChanged();
					
					this.attributeList.revalidate();
					this.selectAttribute(this.attributes.indexOf(attribute));
				}
				this.attributesDirty = true;
			}
			this.editAttributeButton.setEnabled(this.attributes.size() != 0);
			this.removeAttributeButton.setEnabled(this.attributes.size() != 0);
		}
		
		private void removeAttribute(int index) {
			if (index < 0) return;
			this.attributes.remove(index);
			this.typeAttributes.removeSubset(this.attributeValues);
			this.attributeValues = null;
			this.attributeListModel.fireIntervalRemoved();
			this.attributeList.revalidate();
			this.selectAttribute(index - 1);
			
			this.attributesDirty = true;
			this.editAttributeButton.setEnabled(this.attributes.size() != 0);
			this.removeAttributeButton.setEnabled(this.attributes.size() != 0);
		}
		
		private void selectAttribute(int index) {
			this.attributeList.setSelectedIndex(index);
			
			//	write back values of previously selected attribute (if it was not removed)
			if (this.attributeValues != null) {
				this.attributeValues.clear();
				for (int v = 0; v < this.values.size(); v++)
					this.attributeValues.setSetting((VALUE_PREFIX + v), this.values.get(v));
				
				//	add dummy value so attribute can exist without values
				if (this.attributeValues.isEmpty())
					this.attributeValues.setSetting(VALUE_PREFIX, VALUE_PREFIX);
				
				//	remember restriction
				if (this.restrictValues.isSelected())
					this.attributeValues.setSetting(RESTRICT_TO_SUGGESTIONS, RESTRICT_TO_SUGGESTIONS);
			}
			
			//	clear old selection
			this.values.clear();
			
			//	de-select only, clear value list
			if (index < 0) {
				this.attributeValues = null;
				
				this.restrictValues.setSelected(false);
				this.restrictValues.setEnabled(false);
				
				this.editAttributeButton.setEnabled(false);
				this.removeAttributeButton.setEnabled(false);
				
				this.addValueButton.setEnabled(false);
			}
			
			//	change of selection
			else {
				
				//	get new attribute
				String attribute = this.attributes.get(index);
				
				//	read existing values of new attribute
				this.attributeValues = this.typeAttributes.getSubset(attribute);
				this.restrictValues.setEnabled(true);
				this.restrictValues.setSelected(this.attributeValues.removeSetting(RESTRICT_TO_SUGGESTIONS) != null);
				this.attributeValues.removeSetting(VALUE_PREFIX);
				for (int v = 0; v < this.attributeValues.size(); v++)
					this.values.addElementIgnoreDuplicates(this.attributeValues.getSetting(VALUE_PREFIX + v));
				this.values.sortLexicographically(false, false);
				
				this.editAttributeButton.setEnabled(true);
				this.removeAttributeButton.setEnabled(true);
				
				this.addValueButton.setEnabled(true);
			}
			
			//	update GUI
			this.valueListModel.fireContentsChanged();
			this.valueList.revalidate();
			this.valueList.setSelectedIndex(this.values.isEmpty() ? -1 : 0);
			
			this.editValueButton.setEnabled(this.values.size() != 0);
			this.removeValueButton.setEnabled(this.values.size() != 0);
		}
		
		
		private void addValue() {
			this.editValue(-1);
		}
		
		private void editValue(int index) {
			String value = ((index < 0) ? null : this.values.get(index));
			Object valueObject = JOptionPane.showInputDialog(this, (((value == null) ? "Create a new" : "Edit") + " annotation attribute value ..."), (((value == null) ? "Create" : "Edit") + " Attribute Value ..."), JOptionPane.PLAIN_MESSAGE, null, null, value);
			if (valueObject != null) {
				value = valueObject.toString();
				if (value.length() == 0) this.removeValue(index);
				else if (index < 0) {
					if (!this.values.contains(value)) {
						this.values.addElement(value);
						this.values.sortLexicographically(false, false);
						this.valueListModel.fireIntervalAdded();
					}
					else this.valueListModel.fireContentsChanged();
					
					this.valueList.revalidate();
					this.valueList.setSelectedIndex(this.values.indexOf(value));
				}
				else {
					this.values.setElementAt(value, index);
					this.values.removeDuplicateElements(true);
					this.values.sortLexicographically(false, false);
					this.valueListModel.fireContentsChanged();
					
					this.valueList.revalidate();
					this.valueList.setSelectedIndex(this.values.indexOf(value));
				}
				this.valuesDirty = true;
			}
			this.editValueButton.setEnabled(this.values.size() != 0);
			this.removeValueButton.setEnabled(this.values.size() != 0);
		}
		
		private void removeValue(int index) {
			if (index < 0) return;
			this.values.remove(index);
			this.valueListModel.fireIntervalRemoved();
			this.valueList.revalidate();
			this.valueList.setSelectedIndex(index - 1);
			
			this.valuesDirty = true;
			this.editValueButton.setEnabled(this.values.size() != 0);
			this.removeValueButton.setEnabled(this.values.size() != 0);
		}
	}
	
	private static final String ANNOTATION_COLOR_SETTINGS_PREFIX = "ACS";
	
	private static HashMap defaultAnnotationColors = new HashMap();
	
	private static void initDefaultAnnotationColors(Settings set) {
		defaultAnnotationColors.clear();
		String[] types = set.getKeys();
		for (int t = 0; t < types.length; t++) {
			String rgb = set.getSetting(types[t]).toUpperCase();
			if (rgb.startsWith("#"))
				rgb = rgb.substring(1);
			Color typeColor = null;
			if (rgb.length() == 3) {
				typeColor = new Color(
						readHex(rgb.charAt(0), rgb.charAt(0)),
						readHex(rgb.charAt(1), rgb.charAt(1)),
						readHex(rgb.charAt(2), rgb.charAt(2))
					);
			}
			else if (rgb.length() == 6) {
				typeColor = new Color(
						readHex(rgb.charAt(0), rgb.charAt(1)),
						readHex(rgb.charAt(2), rgb.charAt(3)),
						readHex(rgb.charAt(4), rgb.charAt(5))
					);
			}
			if (typeColor != null)
				defaultAnnotationColors.put(types[t], typeColor);
		}
	}
	
	private static void storeDefaultAnnotationColors(Settings set) {
		set.clear();
		String[] types = ((String[]) defaultAnnotationColors.keySet().toArray(new String[defaultAnnotationColors.size()]));
		for (int t = 0; t < types.length; t++) {
			Color typeColor = getDefaultAnnotationColor(types[t]);
			if (typeColor != null) {
				String rgb = "";
				rgb += getHex(typeColor.getRed());
				rgb += getHex(typeColor.getGreen());
				rgb += getHex(typeColor.getBlue());
				set.setSetting(types[t], rgb);
			}
		}
	}
	
	private static int readHex(char high, char low) {
		int i = 0;
		if (high <= '9') i += (high - '0');
		else  i += (high - 'A' + 10);
		i = i << 4;
		if (low <= '9') i += (low - '0');
		else  i += (low - 'A' + 10);
		return i;
	}
	
	private static String getHex(int i) {
		int high = (i >>> 4) & 15;
		int low = i & 15;
		String hex = "";
		if (high < 10) hex += ("" + high);
		else hex += ("" + ((char) ('A' + (high - 10))));
		if (low < 10) hex += ("" + low);
		else hex += ("" +  ((char) ('A' + (low - 10))));
		return hex;
	}
	
	static Color getDefaultAnnotationColor(String annotationType) {
		Color typeColor = ((Color) defaultAnnotationColors.get(annotationType));
		if (typeColor == null) {
			typeColor = produceColor();
			defaultAnnotationColors.put(annotationType, typeColor);
		}
		return typeColor;
	}
	
	private static Color produceColor() {
		return new Color(Color.HSBtoRGB(((float) Math.random()), 0.5f, 1.0f));
	}
	
	/**	obtain a panel for editing annotation color settings
	 * @param	host	the GoldenGATE main instance (necessary due to static context)
	 * @return	a settings panel for editing the annotation suggestion settings
	 */
	static SettingsPanel getAnnotationColorSettingsPanel(GoldenGATE host) {
		return new AnnotationColorSettingsPanel(host);
	}
	
	private static class AnnotationColorSettingsPanel extends SettingsPanel {
		private class TypeTray {
			String type;
			Color color;
			JPanel colorDisplay = new JPanel();
			TypeTray(String type, Color color) {
				this.type = type;
				this.color = color;
			}
		}
		private HashMap typeTrays = new HashMap();
		private TypeTray typeTray = null;
		private StringVector annotationTypes = new StringVector();
		
		private class TypeTableModel extends AbstractTableModel {
			public void fireTableRowsDeleted() {
				super.fireTableRowsDeleted(0, this.getRowCount());
			}
			public void fireTableRowsInserted() {
				super.fireTableRowsInserted(0, this.getRowCount());
			}
			public void fireTableRowsUpdated() {
				super.fireTableRowsUpdated(0, this.getRowCount());
			}
			public Class getColumnClass(int column) {
				return ((column == 0) ? JPanel.class : String.class);
			}
			public String getColumnName(int column) {
				return ((column == 0) ? "Color" : "Type");
			}
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return false;
			}
			public int getColumnCount() {
				return 2;
			}
			public int getRowCount() {
				return annotationTypes.size();
			}
			public Object getValueAt(int row, int column) {
				String type = annotationTypes.get(row);
				if (column == 1) return type;
				else {
					TypeTray typeTray = ((TypeTray) typeTrays.get(type));
					typeTray.colorDisplay.setBackground(typeTray.color);
					return typeTray.colorDisplay;
				}
			}
		}
		
		private TypeTableModel typeTableModel = new TypeTableModel();
		private JTable typeTable = new JTable();
		
		private JButton addTypeButton = new JButton("Add Type");
		private JButton editTypeButton = new JButton("Edit Type");
		private JButton removeTypeButton = new JButton("Remove Type");
		
		private JColorChooser colorEditor = new JColorChooser();
		
		private AnnotationColorSettingsPanel(GoldenGATE host) {
			super("Annotation Colors", "Edit default highlight colors fot annotations & their tags");
			
			Iterator typeIterator = defaultAnnotationColors.keySet().iterator();
			while (typeIterator.hasNext()) {
				String type = typeIterator.next().toString();
				this.annotationTypes.addElement(type);
				TypeTray typeTray = new TypeTray(type, getDefaultAnnotationColor(type));
				this.typeTrays.put(type, typeTray);
			}
			this.annotationTypes.sortLexicographically(false, false);
			
			this.typeTable.setModel(this.typeTableModel);
			this.typeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.typeTable.getColumnModel().getColumn(0).setMaxWidth(40);
			this.typeTable.getColumnModel().getColumn(1).setMaxWidth(160);
			this.typeTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					if (value instanceof JComponent) return ((JComponent) value);
					else return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				}
			});
			this.typeTable.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (me.getClickCount() > 1)
						editType(typeTable.getSelectedRow());
					else selectType(typeTable.getSelectedRow());
				}
			});
			
			this.addTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.addTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addType();
				}
			});
			this.editTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.editTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editType(typeTable.getSelectedRow());
				}
			});
			this.removeTypeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.removeTypeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removeType(typeTable.getSelectedRow());
				}
			});
			JPanel typeListButtonPanel = new JPanel(new FlowLayout(), true);
			typeListButtonPanel.add(this.addTypeButton);
			typeListButtonPanel.add(this.editTypeButton);
			typeListButtonPanel.add(this.removeTypeButton);
			
			JPanel typeTablePanel = new JPanel(new BorderLayout(), true);
			typeTablePanel.add(new JScrollPane(this.typeTable), BorderLayout.CENTER);
			typeTablePanel.add(typeListButtonPanel, BorderLayout.SOUTH);
			typeTablePanel.setPreferredSize(new Dimension(200, 400));
			
			this.colorEditor.getSelectionModel().addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					if (typeTray != null) {
						typeTray.color = colorEditor.getColor();
						typeTray.colorDisplay.setBackground(typeTray.color);
						typeTable.repaint();
					}
				}
			});
			
			JPanel colorEditorPanel = new JPanel(new BorderLayout(), true);
			colorEditorPanel.add(this.colorEditor, BorderLayout.NORTH);
			
			this.setLayout(new BorderLayout());
			this.add(typeTablePanel, BorderLayout.WEST);
			this.add(colorEditorPanel, BorderLayout.CENTER);
			
			this.selectType(this.annotationTypes.isEmpty() ? -1 : 0);
		}
		
		private void addType() {
			this.editType(-1);
		}
		
		private void editType(int index) {
			String oldType = ((index < 0) ? null : this.annotationTypes.get(index));
			Object typeObject = JOptionPane.showInputDialog(this, (((oldType == null) ? "Create a new" : "Edit") + " annotation type ..."), (((oldType == null) ? "Create" : "Edit") + " Annotation Type ..."), JOptionPane.PLAIN_MESSAGE, null, null, oldType);
			if (typeObject != null) {
				String type = typeObject.toString().trim();
				if (type.equals(oldType)) return;
				else if (type.length() == 0) this.removeType(index);
				else if (index < 0) {
					if (this.annotationTypes.contains(type))
						this.typeTableModel.fireTableRowsUpdated();
					
					else {
						this.annotationTypes.addElement(type);
						this.annotationTypes.sortLexicographically(false, false);
						this.typeTableModel.fireTableRowsInserted();
					}
					
					this.typeTable.revalidate();
					this.selectType(this.annotationTypes.indexOf(type));
				}
				else {
					TypeTray typeTray = ((TypeTray) this.typeTrays.remove(oldType));
					if (typeTray != null) {
						typeTray.type = type;
						this.typeTrays.put(type, typeTray);
					}
					this.typeTray = typeTray;
					
					this.annotationTypes.setElementAt(type, index);
					this.annotationTypes.removeDuplicateElements(true);
					this.annotationTypes.sortLexicographically(false, false);
					this.typeTableModel.fireTableRowsUpdated();
					
					this.typeTable.revalidate();
					this.selectType(this.annotationTypes.indexOf(type));
				}
			}
			this.editTypeButton.setEnabled(this.annotationTypes.size() != 0);
			this.removeTypeButton.setEnabled(this.annotationTypes.size() != 0);
		}
		
		private void removeType(int index) {
			if (index < 0) return;
			String type = this.annotationTypes.remove(index);
			this.typeTrays.remove(type);
			this.typeTray = null;
			this.typeTableModel.fireTableRowsDeleted();
			this.typeTable.revalidate();
			this.selectType((index == 0) ? 0 : (index - 1));
			
			this.editTypeButton.setEnabled(this.annotationTypes.size() != 0);
			this.removeTypeButton.setEnabled(this.annotationTypes.size() != 0);
		}
		
		private void selectType(int index) {
			if (index < 0) this.typeTable.clearSelection();
			else this.typeTable.setRowSelectionInterval(index, index);
			
			//	store previous color
			if (this.typeTray != null) {
				this.typeTray.color = this.colorEditor.getColor();
				this.typeTray.colorDisplay.setBackground(this.typeTray.color);
			}
			
			//	de-select only
			if (index < 0) {
				this.editTypeButton.setEnabled(false);
				this.removeTypeButton.setEnabled(false);
				
				this.typeTray = null;
				this.colorEditor.setEnabled(false);
				this.colorEditor.setColor(Color.WHITE);
			}
			
			//	change of selection
			else {
				
				//	get new set of attributes
				this.editTypeButton.setEnabled(true);
				this.removeTypeButton.setEnabled(true);
				
				//	read new type
				String type = (this.annotationTypes.get(index));
				TypeTray typeTray = ((TypeTray) this.typeTrays.get(type));
				if (typeTray == null) {
					typeTray = new TypeTray(type, produceColor());
					this.typeTrays.put(type, typeTray);
				}
				this.typeTray = typeTray;
				
				this.colorEditor.setColor(typeTray.color);
				this.colorEditor.setEnabled(true);
			}
		}
		
		public void commitChanges() {
			this.selectType(-1);
			defaultAnnotationColors.clear();
			Iterator typeTrayIterator = this.typeTrays.values().iterator();
			while (typeTrayIterator.hasNext()) {
				TypeTray typeTray = ((TypeTray) typeTrayIterator.next());
				defaultAnnotationColors.put(typeTray.type, typeTray.color);
			}
		}
	}
	
	private MutableAnnotation content = null;
	
	private final DocumentEditor parent;
	private final GoldenGATE host;
	
	private JLabel displayInfo = new JLabel("No clicks so far, position of last click will be displayed here.", JLabel.LEFT);
	
	private JPanel findReplacePanel = new JPanel();
	
	private JPanel displayControlPanel = new JPanel();
	private JScrollPane displayControlPanelBox = new JScrollPane(this.displayControlPanel);
	private JPanel externalDisplayControlPanel = null;
	
	private HashMap annotationSettings = new HashMap();
	private HashSet highlightAnnotationTypes = new HashSet();
	private HashSet taggedAnnotationTypes = new HashSet();
	private AnnotationEditorPanel layoutParent = null;
	
	private JLabel actionLabel = new JLabel("Last Actions", JLabel.LEFT);
	private JPanel actionPanel = new JPanel();
	private JScrollPane actionPanelBox = new JScrollPane(this.actionPanel);
	private boolean actionsModified = false;
	private HashMap actionButtons = new HashMap();
	private JPanel externalActionPanel = null;
	
	private JLabel customFunctionLabel = new JLabel("Custom Functions", JLabel.LEFT);
	private JPanel customFunctionPanel = new JPanel();
	private JScrollPane customFunctionPanelBox = new JScrollPane(this.customFunctionPanel);
	private boolean customFunctionsModified = false;
//	private HashMap customFunctionButtons = new HashMap();
	private JPanel externalCustomFunctionPanel = null;
	
	private JSplitPane quickAccessPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
	
	private HashMap annotationsByID = new HashMap();
	
	private JMenuItem annotateMenuItem;
	private JMenuItem annotateAllMenuItem;
	private HashMap actionMenuItems = new HashMap();
	
	private JPopupMenu contextMenu = new JPopupMenu();
	int lastClickPosition = -1;
	
	private static TokenSequence clipboard = null;
	
//	AnnotationEditorPanel(GoldenGATE host, MutableAnnotation content, DocumentEditor parent) {
//		super(new BorderLayout(), true);
//		
//		this.content = content;
//		this.parent = parent;
//		this.host = host;
//		
//		if (parent.parent != null) {
//			this.layoutParent = ((DocumentEditor) parent.parent).annotationEditor;
//			this.taggedAnnotationTypes.addAll(((DocumentEditor) parent.parent).annotationEditor.taggedAnnotationTypes);
//			this.highlightAnnotationTypes.addAll(((DocumentEditor) parent.parent).annotationEditor.highlightAnnotationTypes);
//		}
//		this.init();
//		instances.add(this);
//	}
//	
	AnnotationEditorPanel(GoldenGATE host, DocumentEditor parent, AnnotationEditorPanel layoutParent) {
		super(new BorderLayout(), true);
		this.host = host;
		this.parent = parent;
		
		this.layoutParent = layoutParent;
		if (this.layoutParent != null) {
			this.taggedAnnotationTypes.addAll(this.layoutParent.taggedAnnotationTypes);
			this.highlightAnnotationTypes.addAll(this.layoutParent.highlightAnnotationTypes);
		}
		
		this.init();
		instances.add(this);
	}
	
	private void init() {
		
		this.displayPanel.display.setEditable(false);
		this.displayPanel.display.addKeyListener(new KeyListener() {
			private final char NULL = '\u0000';
			private char lastKey = NULL;
			private int lastKeyCode = 0;
			public void keyPressed(KeyEvent ke) {
				this.lastKey = ke.getKeyChar();
				this.lastKeyCode = ke.getKeyCode();
			}
			public void keyReleased(KeyEvent ke) {}
			public void keyTyped(KeyEvent ke) {
				handleKeyStroke(ke, this.lastKey, this.lastKeyCode, displayPanel.getCaretPosition());
				this.lastKey = NULL;
				this.lastKeyCode = 0;
			}
		});
		this.displayPanel.display.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				
				//	update position label
				try {
					lastClickPosition = displayPanel.display.viewToModel(me.getPoint());
					int blockNumber = displayPanel.display.getDocument().getDefaultRootElement().getElementIndex(lastClickPosition);
					lastClickPosition = (lastClickPosition + displayPanel.displayOffset - displayPanel.firstVisibleLine);
					displayInfo.setText("Last click was at position " + lastClickPosition + ", in block " + blockNumber + ".");
					displayInfo.validate();
				} catch (Exception e) {}
				
				//	process click
				if (me.getButton() == MouseEvent.BUTTON1) openAnnotation(me);
				else showPopupMenu(me);
			}
		});
		
		this.displayInfo.setBorder(BorderFactory.createLoweredBevelBorder());
		
		this.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(AncestorEvent ae) {
				if (actionsModified)
					layoutActions();
				if (customFunctionsModified)
					layoutCustomFunctions();
			}
			public void ancestorMoved(AncestorEvent ae) {}
			public void ancestorRemoved(AncestorEvent ae) {}
		});
		
		//	initialize font styles
		this.textFontStyle = new SimpleAttributeSet();
		this.textFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTextFontName());
		this.textFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTextFontSize()));
		this.textFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTextFontColor());
		this.textFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
		
		this.tagFontStyle = new SimpleAttributeSet();
		this.tagFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTagFontName());
		this.tagFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTagFontSize()));
		this.tagFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTagFontColor());
		this.tagFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);

		
		//	initialize buttons
		JButton previewButton = new JButton("Output Preview");
		previewButton.setBorder(BorderFactory.createRaisedBevelBorder());
		previewButton.setPreferredSize(new Dimension(100, 21));
		previewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Output Preview");
				parent.preview();
			}
		});
		
		JButton findReplaceButton = new JButton("Find / Replace ...");
		findReplaceButton.setBorder(BorderFactory.createRaisedBevelBorder());
		findReplaceButton.setPreferredSize(new Dimension(100, 21));
		findReplaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Find / Replace");
				doFindReplace();
			}
		});
		
		JButton findPreviousButton = new JButton("Find Previous");
		findPreviousButton.setBorder(BorderFactory.createRaisedBevelBorder());
		findPreviousButton.setPreferredSize(new Dimension(100, 21));
		findPreviousButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Find Previous");
				findPrevious(false);
			}
		});
		
		JButton replaceButton = new JButton("Replace");
		replaceButton.setBorder(BorderFactory.createRaisedBevelBorder());
		replaceButton.setPreferredSize(new Dimension(100, 21));
		replaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Replace");
				String replacement = JOptionPane.showInputDialog(parent, "Enter the text to replace the selection with.", "Enter Replacement", JOptionPane.QUESTION_MESSAGE);
				if (replacement != null) replaceSelection(content.getTokenizer().tokenize(replacement), true);
			}
		});
		
		JButton replaceAllButton = new JButton("Replace All");
		replaceAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
		replaceAllButton.setPreferredSize(new Dimension(100, 21));
		replaceAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Replace All");
				replaceAll(false);
			}
		});
		
		JButton findNextButton = new JButton("Find Next");
		findNextButton.setBorder(BorderFactory.createRaisedBevelBorder());
		findNextButton.setPreferredSize(new Dimension(100, 21));
		findNextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parent.writeLog("Button Panel --> Find Next");
				findNext(false);
			}
		});
		
		this.findReplacePanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets.top = 2;
		gbc.insets.bottom = 2;
		gbc.insets.left = 3;
		gbc.insets.right = 3;
		gbc.gridy = 0;
		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		
		gbc.weightx = 100;
		this.findReplacePanel.add(new JPanel(), gbc.clone());
		gbc.gridx++;
		
		gbc.weightx = 1;
		this.findReplacePanel.add(previewButton, gbc.clone());
		gbc.gridx++;
		
		JLabel spacer = new JLabel();
		spacer.setPreferredSize(new Dimension(50, 21));
		this.findReplacePanel.add(spacer, gbc.clone());
		gbc.gridx++;
		
		this.findReplacePanel.add(findReplaceButton, gbc.clone());
		gbc.gridx++;
		
		this.findReplacePanel.add(findPreviousButton, gbc.clone());
		gbc.gridx++;
		this.findReplacePanel.add(replaceButton, gbc.clone());
		gbc.gridx++;
		this.findReplacePanel.add(replaceAllButton, gbc.clone());
		gbc.gridx++;
		this.findReplacePanel.add(findNextButton, gbc.clone());
		gbc.gridx++;
		
		gbc.weightx = 100;
		this.findReplacePanel.add(new JPanel(), gbc.clone());
		
		this.displayControlPanelBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.displayControlPanelBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.displayControlPanelBox.getVerticalScrollBar().setUnitIncrement(50);
		this.displayControlPanelBox.getVerticalScrollBar().setBlockIncrement(100);
		
		this.layoutSelectionActions();
		
		this.actionPanelBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.actionPanelBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.actionPanelBox.getVerticalScrollBar().setUnitIncrement(50);
		this.actionPanelBox.getVerticalScrollBar().setBlockIncrement(100);
		this.layoutActions();
		
		this.customFunctionPanelBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		this.customFunctionPanelBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.customFunctionPanelBox.getVerticalScrollBar().setUnitIncrement(50);
		this.customFunctionPanelBox.getVerticalScrollBar().setBlockIncrement(100);
		this.layoutCustomFunctions();
		
		this.quickAccessPanel.setDividerLocation(0.5);
		this.quickAccessPanel.setResizeWeight(0.5);
		
		this.layoutPanels();
	}
	
	void prepareLayoutPanels() {
		this.externalDisplayControlPanel = null;
		this.externalActionPanel = null;
		this.externalCustomFunctionPanel = null;
	}
	
	void layoutPanels() {
		this.removeAll();
		
		this.add(this.displayPanel, BorderLayout.CENTER);
		this.add(this.displayInfo, BorderLayout.SOUTH);
		
		// show display control if configured accordingly, and if display control not on display externally
		if (showDisplayControl && (this.externalDisplayControlPanel == null)) this.add(this.displayControlPanelBox, BorderLayout.EAST);
		
		//	show find/replace toolbar if configured accordingly
		if (showFindReplace) this.add(this.findReplacePanel, BorderLayout.NORTH);
		
		//	show recent actions only if there is something to show
		boolean recentActions = (showRecentActions && !actions.isEmpty() && (this.externalActionPanel == null));
		
		//	show custom functions only if there is something to show
		boolean customFunctions = (showCustomFunctions && (this.host.getCustomFunctions().length != 0) && (this.externalCustomFunctionPanel == null));
		
		//	show recent actions and custom functions
		if (recentActions && customFunctions) {
			this.quickAccessPanel.setTopComponent(this.actionPanelBox);
			this.quickAccessPanel.setBottomComponent(this.customFunctionPanelBox);
			this.add(this.quickAccessPanel, BorderLayout.WEST);
		}
		else if (recentActions)
			this.add(this.actionPanelBox, BorderLayout.WEST);
		else if (customFunctions)
			this.add(this.customFunctionPanelBox, BorderLayout.WEST);
		
		//	make changes visible
		this.validate();
	}
	
	JPanel getCustomFunctionPanel() {
		if (this.externalCustomFunctionPanel == null) {
			this.externalCustomFunctionPanel = new JPanel(new BorderLayout());
			this.externalCustomFunctionPanel.add(this.customFunctionPanelBox, BorderLayout.CENTER);
		}
		return this.externalCustomFunctionPanel;
	}
	
	JPanel getRecentActionPanel() {
		if (this.externalActionPanel == null) {
			this.externalActionPanel = new JPanel(new BorderLayout());
			this.externalActionPanel.add(this.actionPanelBox, BorderLayout.CENTER);
		}
		return this.externalActionPanel;
	}
	
	JPanel getDisplayControlPanel() {
		if (this.externalDisplayControlPanel == null) {
			this.externalDisplayControlPanel = new JPanel(new BorderLayout());
			this.externalDisplayControlPanel.add(this.displayControlPanelBox, BorderLayout.CENTER);
		}
		return this.externalDisplayControlPanel;
	}
	
	void close() {
		if (this.findReplace != null) this.findReplace.dispose();
	}
	
	protected void finalize() throws Throwable {
		this.close();
		instances.remove(this);
		super.finalize();
	}
	
	void setContent(MutableAnnotation docPart) {
		this.content = docPart;
		this.refreshDisplay();
	}
	
	Set getSelectedTags() {
		return new HashSet(this.taggedAnnotationTypes);
	}
	
	private ScrollableTextPane displayPanel = new ScrollableTextPane();
	
	private static final boolean DEBUG_REFRESH_DISPLAY = false;
	
	synchronized void refreshDisplay() {
		setEnabled(false);
		
		//	remember token index in top left corner
		Point topLeftPoint = this.displayPanel.displayBox.getViewport().getViewPosition();
		int topLeftPosition;
		try {
			topLeftPosition = this.displayPanel.display.viewToModel(topLeftPoint);
		} catch (Exception e) {
			topLeftPosition = 0;
		}
		topLeftPosition += (this.displayPanel.displayOffset - this.displayPanel.firstVisibleLine);
		int topLeftTokenIndex = this.getTokenIndexForPosition(topLeftPosition);
		int s = 0;
		while ((topLeftTokenIndex == -1) && (this.tokenSpans.length != 0)) {
			s--;
			topLeftTokenIndex = this.getTokenIndexForPosition(topLeftPosition + s);
		}
		if (DEBUG_REFRESH_DISPLAY && (topLeftTokenIndex != -1) && (this.content.size() > 0)) {
			if (topLeftTokenIndex < this.content.size())
				System.out.println("AnnotationEditorPanel: index of top left token is " + topLeftTokenIndex + ", token is " + this.content.valueAt(topLeftTokenIndex));
			else System.out.println("AnnotationEditorPanel: index of top left token is " + topLeftTokenIndex + ", token is out of range ...");
		}
		int topLine = 0;
		
		//	clear document
		this.displayPanel.clear();
		StringBuffer line = null;
		int offset = 0;
		
		//	clear layout settings
		tagLayoutsByAnnotationType.clear();
		highlightLayoutsByAnnotationType.clear();
		
		//	clean click supporting structures
		annotationsByID.clear();
		
		//	reset span IDs if necessary
		if ((nextSpanID * 2) > Integer.MAX_VALUE) nextSpanID = 0;
		
		try {
			
			//	get Annotations
			MutableAnnotation[] nestedAnnotations = content.getMutableAnnotations();
			
			//	index Annotations by their IDs
			for (int a = 0; a < nestedAnnotations.length; a++)
				if (!DocumentRoot.DOCUMENT_TYPE.equals(nestedAnnotations[a].getType()))
					annotationsByID.put(nestedAnnotations[a].getAnnotationID(), nestedAnnotations[a]);
			
			//	get layout settings
			ArrayList tokenSpanList = new ArrayList();
			ArrayList annotationSpanList = new ArrayList();
//			HashMap startTagSpansByAnnotationID = new HashMap();
			
			//	adjust basic styles
			textFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTextFontName());
			textFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTextFontSize()));
			textFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTextFontColor());
			textFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
			
			tagFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTagFontName());
			tagFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTagFontSize()));
			tagFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTagFontColor());
			tagFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
			
			Stack stack = new Stack();
			int annotationPointer = 0;
			if ((nestedAnnotations.length != 0) && DocumentRoot.DOCUMENT_TYPE.equals(nestedAnnotations[0].getType()))
				annotationPointer ++;
			
//			//	order Annotations according to order defined in parent
//			Arrays.sort(nestedAnnotations, annotationPointer, nestedAnnotations.length, parent.getAnnoationNestingOrder());
			
			Token token = null;
			//Token lastToken;
			
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: start rendering document ...");
			long time = System.currentTimeMillis();
			for (int t = 0; t < content.size(); t++) {
				
				//	switch to next Token
				//lastToken = token;
				token = content.tokenAt(t);
				
				//	write end tags for Annotations ending before current Token
				while ((stack.size() > 0) && ((((Annotation) stack.peek()).getStartIndex() + ((Annotation) stack.peek()).size()) <= t)) {
					MutableAnnotation annotation = ((MutableAnnotation) stack.pop());
					
					if (isShowingTags(annotation.getType())) {
						
						if (line != null) {
							this.displayPanel.addLine(line.toString());
							line = null;
							offset ++;
						}
						
						int indent = 0;
						String indentString = "";
						for (int i = 0; i < stack.size(); i++) {
							if (isShowingTags(((Annotation) stack.get(i)).getType())) {
								indentString += "  ";
								offset += 2;
								indent += 2;
							}
						}
						
						String endTag = ("</" + annotation.getType() + ">");
						this.displayPanel.addLine(indentString + endTag);
						
						AnnotationTagSpan span = new AnnotationTagSpan((offset - indent), indent, annotation, getTagLayoutForAnnotationType(annotation.getType()), "E");
						span.setSize(endTag.length() + indent + 1);
						annotationSpanList.add(span);
						
//						AnnotationTagSpan partnerSpan = ((AnnotationTagSpan) startTagSpansByAnnotationID.remove(annotation.getAnnotationID()));
//						if (partnerSpan != null) {
//							partnerSpan.partnerSpan = span;
//							span.partnerSpan = partnerSpan;
//						}
						
						offset += endTag.length() + 1; // tag plus line break
					}
				}
				
				//	catch line of previous top token
				if (t == topLeftTokenIndex) topLine = this.displayPanel.getLineCount();
				
				//	write start tags for Annotations beginning at current Token
				while ((annotationPointer < nestedAnnotations.length) && (nestedAnnotations[annotationPointer].getStartIndex() == t)) {
					MutableAnnotation annotation = nestedAnnotations[annotationPointer];
					annotationPointer++;
					
					if (isShowingTags(annotation.getType())) {
						
						if (line != null) {
							this.displayPanel.addLine(line.toString());
							line = null;
							offset ++;
						}
						
						int indent = 0;
						String indentString = "";
						for (int i = 0; i < stack.size(); i++) {
							if (isShowingTags(((Annotation) stack.get(i)).getType())) {
								indentString += "  ";
								offset += 2;
								indent += 2;
							}
						}
						
						String startTag = ("<" + annotation.getType());
						String[] attributeNames = annotation.getAttributeNames();
						for (int a = 0; a < attributeNames.length; a++) {
							String aName = attributeNames[a];
							Object o = annotation.getAttribute(aName);
							if ((o != null) && (o instanceof String))
//								startTag += (" " + aName + "=\"" + o.toString() + "\"");
								startTag += (" " + aName + "=\"" + AnnotationUtils.escapeForXml(o.toString()) + "\"");
						}
						startTag += ">";
						this.displayPanel.addLine(indentString + startTag);
						
						AnnotationSpan span = new AnnotationTagSpan((offset - indent), indent, annotation, getTagLayoutForAnnotationType(annotation.getType()), "S");
						span.setSize(startTag.length() + indent + 1);
						annotationSpanList.add(span);
//						startTagSpansByAnnotationID.put(annotation.getAnnotationID(), span);
						
						offset += startTag.length() + 1; // tag plus line break
						stack.push(annotation);
					}
					
					String type = annotation.getType();
					AnnotationHighlightSpan span = new AnnotationHighlightSpan(offset, annotation, getHighlightLayoutForAnnotationType(type));
					annotationSpanList.add(span);
					span.isShowing = isHighlighted(type);
				}
				
//				//	add line break at end of paragraph
//				if ((lastToken != null) && lastToken.hasAttribute(Token.PARAGRAPH_END_ATTRIBUTE) && (line != null)) {
//					this.displayPanel.addLine(line.toString());
//					line = null;
//					offset ++;
//				}
				
				//	get whitespace before current token
				String whitespace = ((t == 0) ? "" : unifyWhitespace(this.content.getWhitespaceAfter(t-1)));
				
//				//	skip space character before unspaced punctuation (e.g. ',') or if explicitly told so
//				if (((lastToken == null) || !lastToken.hasAttribute(Token.PARAGRAPH_END_ATTRIBUTE)) && Gamta.insertSpace(lastToken, token) && (line != null)) {
//					line.append(" ");// TODOne: use normal whitespace here
//					offset ++;
//				}
				
				//	append whitespace before current token if no tags before
				if (line != null) {
					int lineSplit = whitespace.indexOf('\n');
					
					//	no line break in whitespace
					if (lineSplit == -1) {
						line.append(whitespace);
						offset += whitespace.length();
					}
					
					//	add lines
					else {
						line.append(whitespace.substring(0, lineSplit));
						offset += lineSplit;
						
						this.displayPanel.addLine(line.toString());
						line = null;
						offset ++;
						
						whitespace = whitespace.substring(lineSplit + 1);
						lineSplit = whitespace.indexOf('\n');
						
						while (lineSplit != -1) {
							this.displayPanel.addLine(whitespace.substring(0, lineSplit));
							offset += (lineSplit+1);
							if (DEBUG_REFRESH_DISPLAY) System.out.println("GOT LINE BREAK IN WHITESPACE AFTER " + t);
							whitespace = whitespace.substring(lineSplit + 1);
							lineSplit = whitespace.indexOf('\n');
						}
						
						line = new StringBuffer(whitespace);
						offset += whitespace.length();
					}
				}
				
				//	append current Token
				if (line == null) line = new StringBuffer(token.getValue());
				else line.append(token.getValue());
				tokenSpanList.add(new TokenSpan(offset, t, token));
				
				offset += token.length();
			}
			
			//	add last line (if any printable characters)
			if ((line != null) && (line.toString().trim().length() != 0)) {
				this.displayPanel.addLine(line.toString());
				line = null;
				offset ++;
			}
			
			//	write end tags for Annotations not closed so far
			while (stack.size() > 0) {
				MutableAnnotation annotation = ((MutableAnnotation) stack.pop());
				
				if (isShowingTags(annotation.getType())) {
					
					int indent = 0;
					String indentString = "";
					for (int i = 0; i < stack.size(); i++) {
						if (isShowingTags(((Annotation) stack.get(i)).getType())) {
							indentString += "  ";
							offset += 2;
							indent += 2;
						}
					}
					
					String endTag = ("</" + annotation.getType() + ">");
					this.displayPanel.addLine(indentString + endTag);
					
					AnnotationTagSpan span = new AnnotationTagSpan((offset - indent), indent, annotation, getTagLayoutForAnnotationType(annotation.getType()), "E");
					span.setSize(endTag.length() + indent + 1);
					annotationSpanList.add(span);
					
//					AnnotationTagSpan partnerSpan = ((AnnotationTagSpan) startTagSpansByAnnotationID.remove(annotation.getAnnotationID()));
//					if (partnerSpan != null) {
//						partnerSpan.partnerSpan = span;
//						span.partnerSpan = partnerSpan;
//					}
					
					offset += endTag.length() + 1; // tag plus line break
				}
			}
			
			//	sort layout attributes
			Collections.sort(annotationSpanList);
			
			//	remember spans for translation of display offsets to Tokens & Annotations 
			tokenSpans = ((TokenSpan[]) tokenSpanList.toArray(new TokenSpan[tokenSpanList.size()]));
			annotationSpans = ((AnnotationSpan[]) annotationSpanList.toArray(new AnnotationSpan[annotationSpanList.size()]));
			for (int a = 0; a < annotationSpans.length; a++) {
				if (annotationSpans[a] instanceof AnnotationHighlightSpan) {
					AnnotationHighlightSpan highlightSpan = ((AnnotationHighlightSpan) annotationSpans[a]);
					highlightSpan.startToken = ((TokenSpan) tokenSpanList.get(highlightSpan.annotation.getStartIndex()));
					highlightSpan.endToken = ((TokenSpan) tokenSpanList.get(highlightSpan.annotation.getEndIndex() - 1));
				}
			}
			annotationSpansByAnnotationID.clear();
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: document rendered in " + (System.currentTimeMillis() - time) + " ms.");
			
			//	set document
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: handing document to display ...");
			time = System.currentTimeMillis();
			this.displayPanel.render(topLine);
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: document displayed in " + (System.currentTimeMillis() - time) + " ms.");
			
			//	apply styles
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: applying spans ...");
			time = System.currentTimeMillis();
			this.applySpans(false);
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: spans applied in " + (System.currentTimeMillis() - time) + " ms.");
			
			//	layout type panels if dependent of enclosing view panel
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: laying out type panels ...");
			time = System.currentTimeMillis();
			this.layoutTypePanels();
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: type panels lain out in " + (System.currentTimeMillis() - time) + " ms.");
			
			//	validate panel
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: validating ...");
			time = System.currentTimeMillis();
			this.validate();
			if (DEBUG_REFRESH_DISPLAY) System.out.println("AnnotationEditorPanel: validated in " + (System.currentTimeMillis() - time) + " ms.");
			
		}
		catch (Exception e) {
			if (DEBUG_REFRESH_DISPLAY) {
				System.out.println("AnnotationEditorPanel.refreshDisplay(): " + e.getClass().getName() + " (" + e.getMessage() + "):");
				e.printStackTrace(System.out);
			}
		}
		finally {
			setEnabled(true);
			notifyDisplayPositionChanged();
		}
	}
	
	private static String unifyWhitespace(String whitespace) {
		String unifiedWhitespace = whitespace;
		unifiedWhitespace = unifiedWhitespace.replaceAll("\\\r\\\n", "\\\n");
		unifiedWhitespace = unifiedWhitespace.replaceAll("\\\n\\\r", "\\\n");
		unifiedWhitespace = unifiedWhitespace.replaceAll("\\\r", "\\\n");
		return unifiedWhitespace;
	}
	
	private class ScrollableTextPane extends JPanel {
		
		int minReserve = 10;
		int maxReserve = 50;
		
		int minTopReserve = minReserve;
		int maxTopReserve = maxReserve;
		int minBottomReserve = minReserve;
		int maxBottomReserve = maxReserve;
		
		Vector lines = new Vector();
		int length = 0; // the length of the virtual document
		
		JTextPane display = new JTextPane();
		StyledDocument displayDoc = null;
		JScrollPane displayBox = new JScrollPane(this.display);
		
		int firstVisibleLine = 0; // the index of the first line to be displayed
		int lastVisibleLine = 0; // the index of the last line to be displayed
		
		int displayOffset = 0; // the offset of the first visible line in the virtual document
		int displayLength = 0; // the length of the document explicitly displayed 
		
		boolean isModifying = false;
		boolean isAdjusting = false;
		boolean isScrollbarDragging = false;
		AdjustmentEvent lastAe = null;
		
		ScrollableTextPane() {
			super(new BorderLayout(), true);
			
			this.display.addFocusListener(new FocusAdapter() {
				public void focusGained(FocusEvent fe) {
					if (displayDoc == null) return;
					
					try { //  show caret
						display.getCaret().setVisible(true);
						display.getCaret().setBlinkRate(500);
					} catch (Exception e) {}
				}
			});
			this.display.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if (displayDoc == null) return;
					
					//	update position label
					if (DEBUG_REFRESH_DISPLAY) try {
						int clickPosition = display.viewToModel(me.getPoint());
						int blockNumber = display.getDocument().getDefaultRootElement().getElementIndex(clickPosition);
						System.out.println("Click was at position " + clickPosition + ", in block " + blockNumber + ".");
					} catch (Exception e) {}
					
					try { //  show caret
						display.getCaret().setVisible(true);
						display.getCaret().setBlinkRate(500);
					} catch (Exception e) {}
				}
			});
			final AdjustmentListener al = new AdjustmentListener() {
				
				public synchronized void adjustmentValueChanged(AdjustmentEvent ae) {
					
					//	do not adjust an empty display
					if (displayDoc == null) return;
					
					//	is scrollbar is being dragged, remember last AdjustmentEvent for later processing, but don't process it now. 
					if (isScrollbarDragging) {
						lastAe = ae;
						return;
					}
					
					//	do not process further events if already processing one
					if (isAdjusting || isModifying) return;
					
					isAdjusting = true;
					
					final int handleID = ((int) (Math.random() * 1000));
					if (DEBUG_REFRESH_DISPLAY) System.out.println("Document scrolling " + handleID + " ...");
					if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " scrolled to " + ae.getValue());
					
					Point topLeft = displayBox.getViewport().getViewPosition();
					int topScrollPosition = display.viewToModel(topLeft);
					final int topBlockNumber = display.getDocument().getDefaultRootElement().getElementIndex(topScrollPosition);
					if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " top left corner is at offset " + topScrollPosition + " in block " + topBlockNumber);
					
					Point bottomLeft = new Point(topLeft.x, topLeft.y + displayBox.getViewport().getExtentSize().height - 2);
					int bottomScrollPosition = display.viewToModel(bottomLeft);
					final int bottomBlockNumber = display.getDocument().getDefaultRootElement().getElementIndex(bottomScrollPosition);
					if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " bottom left corner is at offset " + bottomScrollPosition + " in block " + bottomBlockNumber);
					
					int caretPosition = display.getCaretPosition();
					int selectionStart = display.getSelectionStart();
					int selectionEnd = display.getSelectionStart();
					
					boolean changed = false;
					boolean useTopAnchor = true;
					int topExtend = 0;
					int bottomExtend = 0;
					long time = System.currentTimeMillis();
					do {
						int topReserve = (topBlockNumber - firstVisibleLine);
						int bottomReserve = (lastVisibleLine - bottomBlockNumber);
						changed = false;
						
						if (firstVisibleLine > bottomBlockNumber) {
							int extend = extendTopReserve(firstVisibleLine - bottomBlockNumber, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendTopReserve() filled in " + extend + " characters.");
							caretPosition += extend;
							topExtend += (extend + (topBlockNumber - firstVisibleLine) - topReserve);
							topReserve = (topBlockNumber - firstVisibleLine);
							changed = true;
						}
						else useTopAnchor = false;
						
						if (topReserve < 0) {
							int extend = extendTopReserve(firstVisibleLine - topBlockNumber, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendTopReserve() filled in " + extend + " characters.");
							caretPosition += extend;
							bottomScrollPosition += extend;
							topExtend += (extend + (topBlockNumber - firstVisibleLine) - topReserve);
							topReserve = (topBlockNumber - firstVisibleLine);
							changed = true;
						}
						
						if ((topReserve < minTopReserve) && (firstVisibleLine > 0)) {
							int extend = extendTopReserve(firstVisibleLine - topBlockNumber + maxTopReserve - minTopReserve, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendTopReserve() inserted " + extend + " characters.");
							topScrollPosition += extend;
							bottomScrollPosition += extend;
							caretPosition += extend;
							topExtend += (extend + (topBlockNumber - firstVisibleLine) - topReserve);
							changed = true;
						}
						
						if (lastVisibleLine < topBlockNumber) {
							useTopAnchor = false;
							int extend = extendBottomReserve(topBlockNumber - lastVisibleLine, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendBottomReserve() filled in " + extend + " characters.");
							topScrollPosition += extend;
							bottomScrollPosition += extend;
							bottomExtend += (extend + (lastVisibleLine - bottomBlockNumber) - bottomReserve);
							bottomReserve = (lastVisibleLine - bottomBlockNumber);
							changed = true;
						}
						
						if (bottomReserve < 0) {
							int extend = extendBottomReserve(bottomBlockNumber - lastVisibleLine, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendBottomReserve() filled in " + extend + " characters.");
							bottomScrollPosition += extend;
							bottomExtend += (extend + (lastVisibleLine - bottomBlockNumber) - bottomReserve);
							bottomReserve = (lastVisibleLine - bottomBlockNumber);
							changed = true;
						}
						
						if ((bottomReserve < minBottomReserve) && (lastVisibleLine < lines.size())) {
							int extend = extendBottomReserve(bottomBlockNumber - lastVisibleLine + maxBottomReserve - minBottomReserve, false);
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " extendBottomReserve() inserted " + extend + " characters.");
							bottomExtend += (extend + (lastVisibleLine - bottomBlockNumber) - bottomReserve);
							changed = true;
						}
						
						if ((topReserve > maxTopReserve) && (caretPosition > topScrollPosition) && (selectionStart > topScrollPosition) && (selectionEnd > topScrollPosition)) {
							int cut = cutTopReserve();
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " cutTopReserve() cut " + cut + " characters.");
							topScrollPosition -= cut;
							bottomScrollPosition -= cut;
							caretPosition -= cut;
							changed = true;
						}
						
						if ((bottomReserve > maxBottomReserve) && (caretPosition < bottomScrollPosition) && (selectionStart < bottomScrollPosition) && (selectionEnd < bottomScrollPosition)) {
							int cut = cutBottomReserve();
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " cutBottomReserve() cut " + cut + " characters.");
							changed = true;
						}
					} while (changed && ((topExtend + bottomExtend) > 0));
					
					if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " display text adjusted in " + (System.currentTimeMillis() - time) + " ms.");
					time = System.currentTimeMillis();
					
					if (topExtend > 0)
						applySpans(displayOffset, (displayOffset + topExtend), false);
					if (bottomExtend > 0)
						applySpans((displayOffset + displayLength - bottomExtend), (displayOffset + displayLength), false);
					
					if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " display layout adjusted in " + (System.currentTimeMillis() - time) + " ms.");
					final long extTime = System.currentTimeMillis();
					if (useTopAnchor) {
						final int newTopScrollPosition = topScrollPosition;
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								long time = System.currentTimeMillis();
								if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " start validating after " + (time - extTime) + " ms.");
								try {
									Rectangle view = display.modelToView(newTopScrollPosition);
									displayBox.getViewport().setViewPosition(new Point(0, view.getLocation().y));
									if (DEBUG_REFRESH_DISPLAY) {
										System.out.println(" - " + handleID + " set view position to " + view.getLocation() + "in " + (System.currentTimeMillis() - time) + " ms.");
										time = System.currentTimeMillis();
									}
								}
								catch (Exception e) {
									if (DEBUG_REFRESH_DISPLAY) {
										System.out.println(" - " + handleID + " " + e.getClass().getName() + "\n   " + e.getMessage());
										e.printStackTrace(System.out);
									}
								}
								finally {
									try { //  show caret
										displayPanel.display.getCaret().setVisible(true);
										displayPanel.display.getCaret().setBlinkRate(500);
									} catch (Exception e) {}
									display.validate();
									if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " view validated in " + (System.currentTimeMillis() - time) + " ms.");
									notifyDisplayPositionChanged();
									isAdjusting = false;
								}
							}
						});
					}
					else {
						final int newBottomScrollPosition = bottomScrollPosition;
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								long time = System.currentTimeMillis();
								if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " start validating after " + (time - extTime) + " ms.");
								try {
									Rectangle view = display.modelToView(newBottomScrollPosition);
									Point bottomLeft = view.getLocation();
									Point topLeft = new Point(0, Math.max(0, (bottomLeft.y - displayBox.getViewport().getExtentSize().height + 2)));
									displayBox.getViewport().setViewPosition(topLeft);
									if (DEBUG_REFRESH_DISPLAY) {
										System.out.println(" - " + handleID + " set view position to " + view.getLocation() + "in " + (System.currentTimeMillis() - time) + " ms.");
										time = System.currentTimeMillis();
									}
								}
								catch (Exception e) {
									if (DEBUG_REFRESH_DISPLAY) {
										System.out.println(" - " + handleID + " " + e.getClass().getName() + "\n   " + e.getMessage());
										e.printStackTrace(System.out);
									}
								}
								finally {
									try { //  show caret
										displayPanel.display.getCaret().setVisible(true);
										displayPanel.display.getCaret().setBlinkRate(500);
									} catch (Exception e) {}
									display.validate();
									if (DEBUG_REFRESH_DISPLAY) System.out.println(" - " + handleID + " view validated in " + (System.currentTimeMillis() - time) + " ms.");
									notifyDisplayPositionChanged();
									isAdjusting = false;
								}
							}
						});
					}
				}
			};
			
			this.displayBox.getVerticalScrollBar().addAdjustmentListener(al);
			
			this.displayBox.getVerticalScrollBar().addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (displayDoc == null) return;
					if (DEBUG_REFRESH_DISPLAY) System.out.println("Mouse pressed in ScrollBar.");
					isScrollbarDragging = true;
				}
				public void mouseReleased(MouseEvent e) {
					if (displayDoc == null) return;
					if (DEBUG_REFRESH_DISPLAY) System.out.println("Mouse released in ScrollBar.");
					isScrollbarDragging = false;
					if (lastAe != null) {
						if (DEBUG_REFRESH_DISPLAY) System.out.println(" --> Re-Firing last AdjustmentEvent ...");
						AdjustmentEvent ae = lastAe;
						lastAe = null;
						al.adjustmentValueChanged(ae);
					}
				}
			});
			
			this.display.setEditable(false);
			this.add(this.displayBox, BorderLayout.CENTER);
		}
		
		private synchronized int extendTopReserve(int lineCount, boolean layoutImmediately) {
			if (this.displayDoc == null) return 0; //	not rendered
			
			isModifying = true;
			int count = lineCount;
			int extendLength = 0;
			while ((count != 0) && (this.firstVisibleLine > 0)) {
				count --;
				if (this.firstVisibleLine < this.lines.size()) {
					this.firstVisibleLine--;
					DisplayLine dl = ((DisplayLine) this.lines.get(this.firstVisibleLine));
					try {
						this.displayDoc.insertString(this.firstVisibleLine, dl.line, null);
						extendLength += dl.line.length();
						this.displayOffset -= (dl.line.length() + 1);
						this.displayLength += (dl.line.length() + 1);
					}
					catch (BadLocationException ble) {
						if (DEBUG_REFRESH_DISPLAY) {
							System.out.println(" - " + ble.getClass().getName() + "\n  " + ble.getMessage());
							ble.printStackTrace(System.out);
						}
						return extendLength;
					}
				}
			}
			
			if (layoutImmediately)
				applySpans(this.displayOffset, (this.displayOffset + extendLength + lineCount));
			isModifying = false;
			return extendLength;
		}
		
		private synchronized int cutTopReserve() {
			if (this.displayDoc == null) return 0; //	not rendered
			
			isModifying = true;
			int count = (maxTopReserve - minTopReserve);
			int cutLength = 0;
			while ((count != 0) && (this.firstVisibleLine < this.lines.size())) {
				count --;
				DisplayLine dl = ((DisplayLine) this.lines.get(this.firstVisibleLine));
				try {
					this.displayDoc.remove(this.firstVisibleLine, dl.line.length());
					cutLength += dl.line.length();
					this.displayOffset += (dl.line.length() + 1);
					this.displayLength -= (dl.line.length() + 1);
					this.firstVisibleLine++;
				}
				catch (BadLocationException ble) {
					if (DEBUG_REFRESH_DISPLAY) {
						System.out.println(" - " + ble.getClass().getName() + "\n  " + ble.getMessage());
						ble.printStackTrace(System.out);
					}
					return cutLength;
				}
			}
			
			isModifying = false;
			return cutLength;
		}
		
		private synchronized int extendBottomReserve(int lineCount, boolean layoutImmediately) {
			if (this.displayDoc == null) return 0; //	not rendered
			
			isModifying = true;
			int count = lineCount;
			int extendLength = 0;
			while ((count != 0) && (this.lastVisibleLine < this.lines.size())) {
				count --;
				DisplayLine dl = ((DisplayLine) this.lines.get(this.lastVisibleLine));
				try {
					this.displayDoc.insertString((this.firstVisibleLine + this.displayLength), dl.line, null);
					extendLength += dl.line.length();
					this.displayLength += (dl.line.length() + 1);
					this.lastVisibleLine++;
				}
				catch (BadLocationException ble) {
					if (DEBUG_REFRESH_DISPLAY) {
						System.out.println(" - " + ble.getClass().getName() + "\n  " + ble.getMessage());
						ble.printStackTrace(System.out);
					}
					return extendLength;
				}
			}
			
			if (layoutImmediately)
				applySpans((this.displayOffset + this.displayLength - extendLength - lineCount), (this.displayOffset + this.displayLength));
			isModifying = false;
			return extendLength;
		}
		
		private synchronized int cutBottomReserve() {
			if (this.displayDoc == null) return 0; //	not rendered
			
			isModifying = true;
			int count = (maxBottomReserve - minBottomReserve);
			int cutLength = 0;
			while ((count != 0) && (this.lastVisibleLine > 0)) {
				count --;
				if (this.lastVisibleLine <= this.lines.size()) {
					this.lastVisibleLine--;
					DisplayLine dl = ((DisplayLine) this.lines.get(this.lastVisibleLine));
					try {
						this.displayLength -= (dl.line.length() + 1);
						cutLength += dl.line.length();
						this.displayDoc.remove((this.firstVisibleLine + this.displayLength), dl.line.length());
					}
					catch (BadLocationException ble) {
						if (DEBUG_REFRESH_DISPLAY) {
							System.out.println(" - " + ble.getClass().getName() + "\n  " + ble.getMessage());
							ble.printStackTrace(System.out);
						}
						return cutLength;
					}
				}
			}
			
			isModifying = false;
			return cutLength; 
		}
		
		synchronized void render(int topLine) {
			
			this.isModifying = true;
			StyledDocument newDisplayDoc = new DefaultStyledDocument();
			this.displayOffset = 0;
			this.firstVisibleLine = Math.max(0, (topLine - ((minReserve + maxReserve) / 2)));
			this.lastVisibleLine = Math.min(this.lines.size(), (topLine + maxReserve + ((minReserve + maxReserve) / 2)));
			int scrollPos = 0;
			
			for (int l = 0; l < this.lines.size(); l++) try {
				DisplayLine dl = ((DisplayLine) this.lines.get(l));
				if (l < this.firstVisibleLine) {
					newDisplayDoc.insertString(newDisplayDoc.getLength(), "\n", null);
					this.displayOffset += (dl.line.length() + 1);
				}
				else if (l < this.lastVisibleLine) {
					if (l == topLine) scrollPos = newDisplayDoc.getLength();
					newDisplayDoc.insertString(newDisplayDoc.getLength(), (dl.line + "\n"), null);
					this.displayLength = (newDisplayDoc.getLength() - this.firstVisibleLine);
				}
				else newDisplayDoc.insertString(newDisplayDoc.getLength(), "\n", null);
			}
			catch (BadLocationException ble) {
				if (DEBUG_REFRESH_DISPLAY) {
					System.out.println("render():\n  " + ble.getClass().getName() + "\n  " + ble.getMessage());
					ble.printStackTrace(System.out);
				}
			}
			
			try {
				this.displayDoc = newDisplayDoc;
				this.display.setDocument(newDisplayDoc);
			}
			catch (Exception e) {
				if (DEBUG_REFRESH_DISPLAY) {
					System.out.println("render():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
					e.printStackTrace(System.out);
				}
			}
			finally {
				final int newScrollPos = scrollPos;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							Rectangle view = display.modelToView(newScrollPos);
							displayBox.getViewport().setViewPosition(view.getLocation());
						}
						catch (Exception e) {
							if (DEBUG_REFRESH_DISPLAY) {
								System.out.println("render():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
								e.printStackTrace(System.out);
							}
						}
						finally {
							display.setEditable(false);
							notifyDisplayPositionChanged();
							isModifying = false;
							isAdjusting = false;
						}
					}
				});
			}
		}
		
		synchronized void addLine(String line) {
			isModifying = true;
			DisplayLine newDl = new DisplayLine(line, this.length);
			this.length += (newDl.line.length() + 1); // line plus line break
			boolean displayImmediately = ((this.lastVisibleLine == this.lines.size()) && ((this.lastVisibleLine - this.firstVisibleLine) < (3 * maxReserve)));
			this.lines.addElement(newDl);
			
			if (this.displayDoc != null) try {
				if (displayImmediately) {
					this.displayDoc.insertString(this.displayDoc.getLength(), (newDl.line + "\n"), null);
					this.lastVisibleLine ++;
					this.displayLength += (newDl.line.length() + 1);
				}
				else this.displayDoc.insertString(this.displayDoc.getLength(), "\n", null);
			}
			catch (BadLocationException e) {
				if (DEBUG_REFRESH_DISPLAY) {
					System.out.println("addLine():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
					e.printStackTrace(System.out);
				}
			}
			
			isModifying = false;
		}
		
		synchronized void insertLine(String line, int position) {
			if (position == this.lines.size()) {
				this.addLine(line);
				this.validate();
			}
			else if ((-1 < position) && (position < this.lines.size())) {
				isModifying = true;
				DisplayLine dl = ((DisplayLine) this.lines.get(position));
				DisplayLine newDl = new DisplayLine(line, dl.offset);
				this.length += (newDl.line.length() + 1); // line plus line break
				for (int l = position; l < this.lines.size(); l++) {
					dl = ((DisplayLine) this.lines.get(l));
					dl.offset += (newDl.line.length() + 1); // line plus line break
				}
				this.lines.insertElementAt(newDl, position);
				
				if (this.displayDoc != null) try {
					if (position < this.firstVisibleLine) {
						this.displayDoc.insertString(this.firstVisibleLine, "\n", null);
						this.firstVisibleLine ++;
						this.lastVisibleLine ++;
						this.displayOffset += (newDl.line.length() + 1);
					}
					else if (position < this.lastVisibleLine) {
						this.displayDoc.insertString((newDl.offset - this.displayOffset + this.firstVisibleLine), (newDl.line + "\n"), null);
						this.lastVisibleLine ++;
						this.displayLength += (newDl.line.length() + 1);
					}
					else this.displayDoc.insertString(this.displayDoc.getLength(), "\n", null);
					
					this.validate();
				}
				catch (BadLocationException e) {
					if (DEBUG_REFRESH_DISPLAY) {
						System.out.println("insertLine():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
						e.printStackTrace(System.out);
					}
				}
				
				isModifying = false;
			}
		}
		
		synchronized String setLine(String line, int position) {
			isModifying = true;
			DisplayLine changeDl = ((DisplayLine) this.lines.get(position));
			int shift = (line.length() - changeDl.line.length());
			
			this.length += shift;
			for (int l = (position + 1); l < this.lines.size(); l++) {
				DisplayLine dl = ((DisplayLine) this.lines.get(l));
				dl.offset += shift;
			}
			String oldLine = changeDl.line;
			changeDl.line = line;
			
			if (this.displayDoc != null) {
				if (position < this.firstVisibleLine)
					this.displayOffset += shift;
					
				else if (position < this.lastVisibleLine) {
					this.display.setSelectionStart(changeDl.offset - this.displayOffset + this.firstVisibleLine);
					this.display.setSelectionEnd(changeDl.offset - this.displayOffset + this.firstVisibleLine + oldLine.length());
					this.display.setEditable(true);
					this.display.replaceSelection(line);
					this.display.setEditable(false);
					this.displayLength += shift;
					this.validate();
				}
			}
			
			isModifying = false;
			return oldLine;
		}
		
		synchronized String removeLine(int position) {
			isModifying = true;
			DisplayLine removeDl = ((DisplayLine) this.lines.remove(position));
			this.length -= (removeDl.line.length() + 1); // line plus line break
			for (int l = position; l < this.lines.size(); l++) {
				DisplayLine dl = ((DisplayLine) this.lines.get(l));
				dl.offset -= (removeDl.line.length() + 1); // line plus line break
			}
			
			if (this.displayDoc != null) {
				if (position < this.firstVisibleLine) {
					this.displayOffset -= (removeDl.line.length() + 1);
					this.firstVisibleLine --;
					this.lastVisibleLine --;
					this.display.setSelectionStart(position);
					this.display.setSelectionEnd(position + 1);
					this.display.setEditable(true);
					this.display.replaceSelection("");
					this.display.setEditable(false);
				}
				else if (position < this.lastVisibleLine) {
					this.display.setSelectionStart(removeDl.offset - this.displayOffset + this.firstVisibleLine);
					this.display.setSelectionEnd(removeDl.offset - this.displayOffset + this.firstVisibleLine + removeDl.line.length() + 1);
					this.display.setEditable(true);
					this.display.replaceSelection("");
					this.display.setEditable(false);
					this.displayLength -= (removeDl.line.length() + 1);
					this.lastVisibleLine --;
				}
				else {
					this.display.setSelectionStart(this.displayOffset + this.displayLength);
					this.display.setSelectionEnd(this.displayOffset + this.displayLength + 1);
					this.display.setEditable(true);
					this.display.replaceSelection("");
					this.display.setEditable(false);
				}
				this.validate();
			}
			
			isModifying = false;
			return removeDl.line;
		}
		
		private class DisplayLine {
			String line;
			int offset;
			DisplayLine(String line, int offset) {
				this.line = line;
				this.offset = offset;
			}
		}
		
		synchronized int getLineCount() {
			return this.lines.size();
		}
		
		synchronized void clear() {
			this.displayDoc = null;
			
			this.lines.clear();
			this.length = 0;
			
			this.firstVisibleLine = 0;
			this.lastVisibleLine = 0;
			
			this.displayOffset = 0;
			this.displayLength = 0;
		}
		
		synchronized void setCharacterAttributes(int offset, int length, AttributeSet attributes, boolean replace) {
			if (this.displayDoc == null) return; //	not rendered
			
			if (offset > (this.displayOffset + this.displayLength)) return; // after visible range
			if ((offset + length) < this.displayOffset) return; // before visible range
			
			int effectiveStart = Math.max(offset, this.displayOffset);
			int effectiveEnd = Math.min((offset + length), (this.displayOffset + this.displayLength));
			int effectiveLength = effectiveEnd - effectiveStart;
			this.displayDoc.setCharacterAttributes((effectiveStart - this.displayOffset + this.firstVisibleLine), effectiveLength, attributes, replace);
		}
		
		synchronized int getSelectionStart() {
			return (this.display.getSelectionStart() + this.displayOffset - this.firstVisibleLine);
		}
		
		synchronized boolean setSelectionStart(int position) {
			if (this.ensurePositionDisplayed(position, false)) {
				this.display.setSelectionStart(position - this.displayOffset + this.firstVisibleLine);
				return true;
			}
			else return false;
		}
		
		synchronized int getSelectionEnd() {
			return (this.display.getSelectionEnd() + this.displayOffset - this.firstVisibleLine);
		}
		
		synchronized boolean setSelectionEnd(int position) {
			if (this.ensurePositionDisplayed(position, false)) {
				this.display.setSelectionEnd(position - this.displayOffset + this.firstVisibleLine);
				return true;
			}
			else return false;
//			System.out.println("setSelectionEnd(" + position + ")");
//			this.ensurePositionDisplayed(position, false);
//			System.out.println(" - position displayed");
//			this.display.setSelectionEnd(position - this.displayOffset + this.firstVisibleLine);
//			System.out.println(" - selection end set");
		}
		
		synchronized int getCaretPosition() {
			return (this.display.getCaretPosition() + this.displayOffset - this.firstVisibleLine);
		}
		
		synchronized void setCaretPosition(int position) {
			this.ensurePositionDisplayed(position, true);
			this.display.setCaretPosition(position - this.displayOffset + this.firstVisibleLine);
		}
		
		synchronized int getDocumentLength() {
			return this.length;
		}
		
		private synchronized int getTopScrollPosition() { // the top left position of the visible area, in terms of the actual display document
			Point topLeft = this.displayBox.getViewport().getViewPosition();
			return this.display.viewToModel(topLeft);
		}
		
		synchronized int getDisplayStart() {// the top left position of the visible area, in terms of the virtual display document
			Point topLeft = this.displayBox.getViewport().getViewPosition();
			return (this.display.viewToModel(topLeft) + this.displayOffset - this.firstVisibleLine);
		}
		
		private synchronized int getBottomScrollPosition() {// the bottom right position of the visible area, in terms of the actual display document
			Point topLeft = this.displayBox.getViewport().getViewPosition();
			Point bottomLeft = new Point(topLeft.x, topLeft.y + this.displayBox.getViewport().getExtentSize().height - 2);
			return this.display.viewToModel(bottomLeft);
		}
		
		synchronized int getDisplayEnd() {// the bottom right position of the visible area, in terms of the virtual display document
			Point topLeft = this.displayBox.getViewport().getViewPosition();
			Point bottomLeft = new Point(topLeft.x, topLeft.y + this.displayBox.getViewport().getExtentSize().height - 2);
			return (this.display.viewToModel(bottomLeft) + this.displayOffset - this.firstVisibleLine);
		}
		
//		synchronized int getLineForPosition(int position) { // the display line in which the specified position lies in the virtual document
//			for (int l = 0; l < this.lines.size(); l++) {
//				DisplayLine dl = ((DisplayLine) this.lines.get(l));
//				if ((dl.offset + dl.line.length()) >= position) return l;
//			}
//			return this.lines.size();
//		}
//		
		synchronized void scrollToPosition(final int position, boolean forceAtTop, boolean forceAtBottom) {
			if (DEBUG_REFRESH_DISPLAY) System.out.println("Scrolling to position " + position);
			this.ensurePositionDisplayed(position, true);
			this.setCaretPosition(position);
			
			final int scrollPosition = position - this.displayOffset + this.firstVisibleLine;
			
			if (forceAtTop && (scrollPosition > this.getTopScrollPosition()))
				this.extendBottomReserve(this.maxBottomReserve, true);
			
			if (forceAtBottom && (scrollPosition < this.getBottomScrollPosition()))
				this.extendTopReserve(this.maxTopReserve, true);
			
			final int smoother = (this.displayBox.getViewport().getExtentSize().height / 10);
			
			if (forceAtTop && forceAtBottom) {
				if (DEBUG_REFRESH_DISPLAY) System.out.println("Scrolling " + position + " to middle of display");
				this.isModifying = true;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - start scrolling ... ");
							Rectangle view = display.modelToView(scrollPosition);
							Point asTopLeft = view.getLocation();
							Point positionMiddleTopLeft = new Point(asTopLeft.x, Math.max(0, (asTopLeft.y - (displayBox.getViewport().getExtentSize().height / 2))));
							displayBox.getViewport().setViewPosition(positionMiddleTopLeft);
							displayBox.validate();
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - done");
						}
						catch (Exception e) {
							if (DEBUG_REFRESH_DISPLAY) {
								System.out.println("scrollToPosition():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
								e.printStackTrace(System.out);
							}
						}
						finally {
							notifyDisplayPositionChanged();
							isModifying = false;
						}
					}
				});
			}
			else if (forceAtTop || (scrollPosition < this.getTopScrollPosition())) {
				if (DEBUG_REFRESH_DISPLAY) {
					if (forceAtTop) System.out.println("Scrolling " + position + " to top of display");
					else System.out.println("Scrolling up to " + position);
				}
				this.isModifying = true;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - start scrolling ... ");
							Rectangle view = display.modelToView(scrollPosition);
							Point asTopLeft = view.getLocation();
							Point smoothedTopLeft = new Point(asTopLeft.x, Math.max(0, (asTopLeft.y - smoother)));
							displayBox.getViewport().setViewPosition(smoothedTopLeft);
							displayBox.validate();
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - done");
						}
						catch (Exception e) {
							if (DEBUG_REFRESH_DISPLAY) {
								System.out.println("scrollToPosition():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
								e.printStackTrace(System.out);
							}
						}
						finally {
							notifyDisplayPositionChanged();
							isModifying = false;
						}
					}
				});
			}
			else if (forceAtBottom || (scrollPosition > this.getBottomScrollPosition())) {
				if (DEBUG_REFRESH_DISPLAY) {
					if (forceAtBottom) System.out.println("Scrolling " + position + " to bottom of display");
					else System.out.println("Scrolling down to " + position);
				}
				this.isModifying = true;
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - start scrolling ... ");
							Rectangle view = display.modelToView(scrollPosition);
							Point asTopLeft = view.getLocation();
							Point asBottomLeft = new Point(asTopLeft.x, Math.max(0, (asTopLeft.y - displayBox.getViewport().getExtentSize().height + smoother)));
							displayBox.getViewport().setViewPosition(asBottomLeft);
							displayBox.validate();
							if (DEBUG_REFRESH_DISPLAY) System.out.println(" - done");
						}
						catch (Exception e) {
							if (DEBUG_REFRESH_DISPLAY) {
								System.out.println("scrollToPosition():\n  " + e.getClass().getName() + "\n  " + e.getMessage());
								e.printStackTrace(System.out);
							}
						}
						finally {
							notifyDisplayPositionChanged();
							isModifying = false;
						}
					}
				});
			}
		}
		
//		synchronized boolean isPositionVisible(int position) {
//			if (position > (this.displayOffset + this.displayLength)) return false; // after visible range
//			if (position < this.displayOffset) return false; // before visible range
//			
//			int scrollPosition = position - this.displayOffset + this.firstVisibleLine;
//			if (scrollPosition < this.getTopScrollPosition()) return false;
//			if (scrollPosition > this.getBottomScrollPosition()) return false;
//			
//			return true;
//		}
//		
		synchronized boolean ensurePositionDisplayed(int position, boolean allowCaretLoss) {
			if ((position < 0) || (position > this.length)) throw new IllegalArgumentException("Position index out of range: " + position);
			int ext;
			
			//	TODO: if caret loss allowed and required extension large, do not render whole document between current and required position
			
			//	extend display up to specified position
			ext = 1;
			while ((position < this.displayOffset) && (this.firstVisibleLine >= 0))
				ext = this.extendTopReserve(this.maxTopReserve - this.minTopReserve, false);
			if (ext == 0) return false;
			
			//	get display position
			Point topLeft = this.displayBox.getViewport().getViewPosition();
			int topScrollPosition = this.display.viewToModel(topLeft);
			final int topBlockNumber = this.display.getDocument().getDefaultRootElement().getElementIndex(topScrollPosition);
			int topReserve = (topBlockNumber - this.firstVisibleLine);
			
			//	make sure top reserve is large enough
			if (topReserve < 0) {
				this.extendTopReserve(this.firstVisibleLine - topBlockNumber, false);
				topReserve = (topBlockNumber - this.firstVisibleLine);
			}
			if ((topReserve < this.minTopReserve) && (this.firstVisibleLine > 0))
				this.extendTopReserve(this.firstVisibleLine - topBlockNumber + this.maxTopReserve - this.minTopReserve, false);
			
			
			//	extend display down to specified position
			ext = 1;
			while ((position >= (this.displayOffset + this.displayLength)) && (this.lastVisibleLine <= this.lines.size()) && (ext != 0))
				ext = this.extendBottomReserve(this.maxBottomReserve - this.minBottomReserve, false);
			if (ext == 0) return false;
			
			//	get display position
			Point bottomLeft = new Point(topLeft.x, topLeft.y + this.displayBox.getViewport().getExtentSize().height - 2);
			int bottomScrollPosition = this.display.viewToModel(bottomLeft);
			final int bottomBlockNumber = this.display.getDocument().getDefaultRootElement().getElementIndex(bottomScrollPosition);
			int bottomReserve = (this.lastVisibleLine - bottomBlockNumber);
			
			//	make sure bottom reserve is large enough
			if (bottomReserve < 0) {
				this.extendBottomReserve(bottomBlockNumber - this.lastVisibleLine, false);
				bottomReserve = (this.lastVisibleLine - bottomBlockNumber);
			}
			if ((bottomReserve < this.minBottomReserve) && (this.lastVisibleLine < this.lines.size()))
				this.extendBottomReserve(bottomBlockNumber - this.lastVisibleLine + this.maxBottomReserve - this.minBottomReserve, false);
			
			
			//	do detail layout
			applySpans(this.displayOffset, (this.displayOffset + this.displayLength));
			
			//	make changes visible
			this.validate();
			return true;
		}
		
		synchronized String getText(int offset, int length) throws BadLocationException {
			return this.display.getText((offset - this.displayOffset + this.firstVisibleLine), length);
		}
		
		synchronized String getSelectedText() {
			return this.display.getSelectedText();
		}
		
		synchronized void replaceSelection(String replacement) {
			String content = ((replacement == null) ? "" : replacement);
			String selection = this.display.getSelectedText();
			
			if (selection == null) {
				int caretPosition = this.display.getCaretPosition();
				int blockNumber = this.display.getDocument().getDefaultRootElement().getElementIndex(caretPosition);
				DisplayLine dl = ((DisplayLine) this.lines.get(blockNumber));
				
				int offset = caretPosition - dl.offset + this.displayOffset - this.firstVisibleLine;
				String newLine = dl.line.substring(0, offset) + content + dl.line.substring(offset);
				
				int split = newLine.indexOf('\n');
				while (split != -1) {
					this.insertLine(newLine.substring(0, split), blockNumber);
					newLine = newLine.substring(split + 1);
					split = newLine.indexOf('\n');
					blockNumber ++;
				}
				this.setLine(newLine, blockNumber);
			}
			else {
				int startOffset = Math.min(this.display.getSelectionStart(), this.display.getSelectionEnd());
				int endOffset = Math.max(this.display.getSelectionStart(), this.display.getSelectionEnd());
				
				int startBlockNumber = display.getDocument().getDefaultRootElement().getElementIndex(startOffset);
				int endBlockNumber = display.getDocument().getDefaultRootElement().getElementIndex(endOffset);
				
				if (startBlockNumber == endBlockNumber) {
					DisplayLine dl = ((DisplayLine) this.lines.get(startBlockNumber));
					
					int start = startOffset - dl.offset + this.displayOffset - this.firstVisibleLine;
					int end = endOffset - dl.offset + this.displayOffset - this.firstVisibleLine;
					String newLine = dl.line.substring(0, start) + content + dl.line.substring(end);
					
					int split = newLine.indexOf('\n');
					while (split != -1) {
						this.insertLine(newLine.substring(0, split), startBlockNumber);
						newLine = newLine.substring(split + 1);
						split = newLine.indexOf('\n');
						startBlockNumber ++;
					}
					this.setLine(newLine, startBlockNumber);
				}
				
				else {
					DisplayLine startDl = ((DisplayLine) this.lines.get(startBlockNumber));
					
					int start = startOffset - startDl.offset + this.displayOffset - this.firstVisibleLine;
					int end = endOffset - startDl.offset + this.displayOffset - this.firstVisibleLine;
					String line = startDl.line;
					for (int l = (startBlockNumber + 1); l <= endBlockNumber; l++)
						line += ("\n" + ((DisplayLine) this.lines.get(l)).line);
					
					String newLine = line.substring(0, start) + content + line.substring(end);
					
					int split = newLine.indexOf('\n');
					while (split != -1) {
						if (startBlockNumber <= endBlockNumber) this.setLine(newLine.substring(0, split), startBlockNumber);
						else this.insertLine(newLine.substring(0, split), startBlockNumber);
						newLine = newLine.substring(split + 1);
						split = newLine.indexOf('\n');
						startBlockNumber ++;
					}
					this.setLine(newLine, startBlockNumber);
					startBlockNumber ++;
					while (startBlockNumber <= endBlockNumber) {
						this.removeLine(startBlockNumber);
						endBlockNumber --;
					}
				}
			}
		}
	}
	
	private SimpleAttributeSet textFontStyle;
	private SimpleAttributeSet tagFontStyle;
	
	private TokenSpan[] tokenSpans = new TokenSpan[0];
	private AnnotationSpan[] annotationSpans = new AnnotationSpan[0];
	
	private int nextSpanID = 0;
	
	private abstract class Span implements Comparable {
		
		private int startOffset;
		private int endOffset;
		private int size = 0;
		
		final int id;
		
		Span(int startOffset) {
			this.startOffset = startOffset;
			this.endOffset = startOffset;
			this.id = nextSpanID++;
		}
		int getStartOffset() {
			return this.startOffset;
		}
		void setStartOffset(int startOffset) {
			this.startOffset = startOffset;
			this.endOffset = (this.startOffset + this.size);
		}
		int getSize() {
			return this.size;
		}
		void setSize(int size) {
			this.size = size;
			this.endOffset = this.startOffset + size;
		}
		int getEndOffset() {
			return this.endOffset;
		}
//		void setEndOffset(int endOffset) {
//			this.endOffset = endOffset;
//			this.size = (this.endOffset - this.startOffset);
//		}
		public int compareTo(Object obj) {
			Span span = ((Span) obj);
			if (span == null) return -1;
			else if (this.getStartOffset() == span.getStartOffset()) {
				if (this.getSize() == span.getSize()) return (this.id - span.id);
				else return (span.getSize() - this.getSize());
			} else return (this.getStartOffset() - span.getStartOffset());
		}
		public boolean equals(Object o) {
			return ((o instanceof Span) && (this.compareTo((Span) o) == 0));
		}
		public int hashCode() {
			return this.id;
		}
	}
	
	private abstract class AnnotationSpan extends Span {
		AttributeSet colorLayout;
		MutableAnnotation annotation;
		
		boolean isShowing = true;
		
		AnnotationSpan(int startOffset, MutableAnnotation annotation, AttributeSet colorLayout) {
			super(startOffset);
			this.colorLayout = colorLayout;
			this.annotation = annotation;
		}
	}
	
	private class AnnotationTagSpan extends AnnotationSpan {
		String tagType;
//		AnnotationTagSpan partnerSpan = null;
		int indent = 0;
//		int displayIndent = 0;
		AnnotationTagSpan(int startOffset, int indent, MutableAnnotation annotation, AttributeSet colorLayout, String tagType) {
			super(startOffset, annotation, colorLayout);
			this.indent = indent;
			this.tagType = tagType;
		}
	}
	
	private class AnnotationHighlightSpan extends AnnotationSpan {
		TokenSpan startToken;
		TokenSpan endToken;
		AnnotationHighlightSpan(int startOffset, MutableAnnotation annotation, AttributeSet colorLayout) {
			super(startOffset, annotation, colorLayout);
		}
		AnnotationHighlightSpan(TokenSpan startToken, TokenSpan endToken, MutableAnnotation annotation, AttributeSet colorLayout) {
			super(startToken.getStartOffset(), annotation, colorLayout);
			this.startToken = startToken;
			this.endToken = endToken;
		}
		int getStartOffset() {
			if (this.startToken == null) return super.getStartOffset();
			return this.startToken.getStartOffset();
		}
		int getSize() {
			return (this.getEndOffset() - this.getStartOffset());
		}
		int getEndOffset() {
			if (this.endToken == null) return super.getEndOffset();
			return this.endToken.getEndOffset();
		}
	}
	
	private class TokenSpan extends Span {
		int tokenIndex;
		Token token;
		
		TokenSpan(int startOffset, int tokenIndex, Token token) {
			super(startOffset);
			
			this.tokenIndex = tokenIndex;
			this.token = token;
			this.setSize(this.token.length());
		}
	}
	
	private void notifyDisplayPositionChanged() {
		this.parent.notifyDisplayPositionChanged(this.getTopTokenIndex(), this.getBottomTokenIndex());
	}
	
	void scrollToIndex(int index, boolean atTop, boolean atBottom) {
		if (this.displayPanel.isAdjusting || this.displayPanel.isModifying || this.displayPanel.isScrollbarDragging)
			return;
		this.displayPanel.scrollToPosition(this.tokenSpans[index].getStartOffset(), atTop, atBottom);
	}
	
	int getTopTokenIndex() {
		int start = this.displayPanel.getDisplayStart();
		
		//	use binary search to narrow interval
		int left = 0;
		int right = this.tokenSpans.length;
		int startTokenIndex = 0;
		while ((right - left) > 2) {
			startTokenIndex = ((left + right) / 2);
			if (this.tokenSpans[startTokenIndex].getEndOffset() <= start) left = startTokenIndex;
			else if (this.tokenSpans[startTokenIndex].getStartOffset() > start) right = startTokenIndex;
			else break;
		}
		
		//	scan remaining interval
		startTokenIndex = left;
		while (startTokenIndex < tokenSpans.length) {
			if (this.tokenSpans[startTokenIndex].getEndOffset() <= start) startTokenIndex++;
			else break;
		}
		
		//	return result
		return startTokenIndex;
	}
	
	int getBottomTokenIndex() {
		int end = this.displayPanel.getDisplayEnd();
		
		//	use binary search to narrow interval
		int left = 0;
		int right = this.tokenSpans.length;
		int endTokenIndex = 0;
		while ((right - left) > 2) {
			endTokenIndex = ((left + right) / 2);
			if (this.tokenSpans[endTokenIndex].getEndOffset() < end) left = endTokenIndex;
			else if (this.tokenSpans[endTokenIndex].getStartOffset() >= end) right = endTokenIndex;
			else break;
		}
		
		//	scan remaining interval
		endTokenIndex = Math.min(right, (this.tokenSpans.length - 1));
		while (endTokenIndex > -1) {
			if (this.tokenSpans[endTokenIndex].getStartOffset() >= end) endTokenIndex--;
			else break;
		}
		
		//	return result
		return endTokenIndex;
	}
	
	private StandaloneAnnotation annotateSelection(String type, boolean showError) {
		int start = this.displayPanel.getSelectionStart();
		int end = this.displayPanel.getSelectionEnd();
		
		if (start == end) {
			if (showError) JOptionPane.showMessageDialog(this, "Cannot annotate whitespace. Select at least one word to annotate.", "Invalid Annotation", JOptionPane.ERROR_MESSAGE);
			return null;
		} else if (end < start) {
			int temp = end;
			end = start;
			start = temp;
		}
		
		int left;
		int right;
		
		//	use binary search to narrow interval
		left = 0;
		right = this.tokenSpans.length;
		int startTokenIndex = 0;
		while ((right - left) > 2) {
			startTokenIndex = ((left + right) / 2);
			if (this.tokenSpans[startTokenIndex].getEndOffset() <= start) left = startTokenIndex;
			else if (this.tokenSpans[startTokenIndex].getStartOffset() > start) right = startTokenIndex;
			else break;
		}
		
		//	scan remaining interval
		startTokenIndex = left;
		while (startTokenIndex < tokenSpans.length) {
			if (this.tokenSpans[startTokenIndex].getEndOffset() <= start) startTokenIndex++;
			else break;
		}
		
		//	use binary search to narrow interval
		left = startTokenIndex;
		right = this.tokenSpans.length;
		int endTokenIndex = this.tokenSpans.length - 1;
		while ((right - left) > 2) {
			endTokenIndex = ((left + right) / 2);
			if (this.tokenSpans[endTokenIndex].getEndOffset() < end) left = endTokenIndex;
			else if (this.tokenSpans[endTokenIndex].getStartOffset() >= end) right = endTokenIndex;
			else break;
		}
		
		//	scan remaining interval
		endTokenIndex = Math.min(right, (this.tokenSpans.length - 1));
		while (endTokenIndex > -1) {
			if (this.tokenSpans[endTokenIndex].getStartOffset() >= end) endTokenIndex--;
			else break;
		}
		
		if (startTokenIndex > endTokenIndex) {
			if (showError) JOptionPane.showMessageDialog(this, "Cannot annotate whitespace. Select at least one word to annotate.", "Invalid Annotation", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return Gamta.newAnnotation(this.content, type, startTokenIndex, (endTokenIndex - startTokenIndex + 1));
	}
	
	private MutableAnnotation[] getAnnotationsForSelectedTags() {
		int start = this.displayPanel.getSelectionStart();
		int end = this.displayPanel.getSelectionEnd();
		
		if (start == end) return new MutableAnnotation[0];
		
		HashSet annotations = new HashSet();
		for (int s = 0; s < this.annotationSpans.length; s++) {
			if (this.annotationSpans[s] instanceof AnnotationTagSpan) {
				AnnotationTagSpan span = ((AnnotationTagSpan) this.annotationSpans[s]);
				if ((span.getEndOffset() > start) && (span.getStartOffset() < end))
					annotations.add(span.annotation);
			}
		}
		
		ArrayList annotationList = new ArrayList(annotations);
//		Collections.sort(annotationList, new Comparator() {
//			public int compare(Annotation a1, Annotation a2) {
//				int c = AnnotationUtils.compare(a1, a2);
//				return ((c == 0) ? ANNOTATION_TYPE_ORDER.compare(a1.getType(), a2.getType()) : c);
//			}
//		});
		Collections.sort(annotationList, new Comparator() {
			public int compare(Object o1, Object o2) {
				Annotation a1 = ((Annotation) o1);
				Annotation a2 = ((Annotation) o2);
				int c = AnnotationUtils.compare(a1, a2);
				return ((c == 0) ? ANNOTATION_TYPE_ORDER.compare(a1.getType(), a2.getType()) : c);
			}
		});
		return ((MutableAnnotation[]) annotationList.toArray(new MutableAnnotation[annotationList.size()]));
	}
	
	private Annotation getAnnotationSurrounding(int tokenIndex) {
		Annotation surrounding = null;
		for (int s = 0; s < annotationSpans.length; s++) {
			if (annotationSpans[s] instanceof AnnotationTagSpan) {
				AnnotationTagSpan ats = ((AnnotationTagSpan) annotationSpans[s]);
				if ("S".equals(ats.tagType)) {
					if ((ats.annotation.getStartIndex() <= tokenIndex) && (tokenIndex < ats.annotation.getEndIndex()))
						surrounding = annotationSpans[s].annotation;
				}
			}
		}
		return surrounding;
	}
	
	private int getTokenIndexForPosition(int position) {
		if (tokenSpans.length == 0) return -1;
		
		//	catch special cases
		if (position < tokenSpans[0].getStartOffset()) return 0;
		if (position >= tokenSpans[tokenSpans.length - 1].getEndOffset()) return this.tokenSpans[tokenSpans.length - 1].tokenIndex;
		
		//	use binary search to narrow search interval
		int left = 0;
		int right = this.tokenSpans.length;
		int tsIndex = 0;
		while ((right - left) > 2) {
			tsIndex = ((left + right) / 2);
			if (tokenSpans[tsIndex].getEndOffset() <= position) left = tsIndex;
			else if (tokenSpans[tsIndex].getStartOffset() <= position) return this.tokenSpans[tsIndex].tokenIndex;
			else right = tsIndex;
		}
		
		//	scan remaining interval
		tsIndex = left;
		while (tsIndex < tokenSpans.length) {
			if (tokenSpans[tsIndex].getEndOffset() <= position) tsIndex++;
			else if (tokenSpans[tsIndex].getStartOffset() <= position) return this.tokenSpans[tsIndex].tokenIndex;
			else tsIndex++;
		}
		if (position < tokenSpans[0].getStartOffset()) return 0;
		if (position >= tokenSpans[tokenSpans.length - 1].getEndOffset()) return this.tokenSpans[tokenSpans.length - 1].tokenIndex;
		return -1;
	}
	
	private TokenSpan getTokenSpanForPosition(int position) {
		if (tokenSpans.length == 0) return null;
		
		//	use binary search to narrow search interval
		int left = 0;
		int right = this.tokenSpans.length;
		int tsIndex = 0;
		while ((right - left) > 2) {
			tsIndex = ((left + right) / 2);
			if (tokenSpans[tsIndex].getEndOffset() <= position) left = tsIndex;
			else if (tokenSpans[tsIndex].getStartOffset() <= position) return this.tokenSpans[tsIndex];
			else right = tsIndex;
		}
		
		//	scan remaining interval
		tsIndex = left;
		while (tsIndex < tokenSpans.length) {
			if (tokenSpans[tsIndex].getEndOffset() <= position) tsIndex++;
			else if (tokenSpans[tsIndex].getStartOffset() <= position) return this.tokenSpans[tsIndex];
			else tsIndex++;
		}
		return null;
	}
	
	private AnnotationSpan[] getAnnotationSpansForPosition(int position) {
		ArrayList spanList = new ArrayList();
		
		//	do linaer scan, binary search is not applicable here because spans containing a given position are not neccessarily subsequent in span array
		for (int s = 0; s < annotationSpans.length; s++) {
			if ((annotationSpans[s].getStartOffset() <= position) && (annotationSpans[s].getEndOffset() > position) && annotationSpans[s].isShowing)
				spanList.add(annotationSpans[s]);
		}
		return ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
	}
	
	private AnnotationSpan getInmostAnnotationSpanForPosition(int position) {
		AnnotationSpan[] spans = getAnnotationSpansForPosition(position);
		if (spans.length == 0) return null;
		else return spans[spans.length - 1];
	}
	
	private AnnotationTagSpan getAnnotationTagSpanForPosition(int position) {
		AnnotationSpan[] spans = getAnnotationSpansForPosition(position);
		for (int s = 0; s < spans.length; s++)
			if ((spans[s] instanceof AnnotationTagSpan) && ((position + 1) != spans[s].getEndOffset()))
				return ((AnnotationTagSpan) spans[s]);
		return null;
	}
	
	private AnnotationSpan[] getSpansForAnnotation(Annotation annotation) {
		return this.getSpansForAnnotationID(annotation.getAnnotationID());
	}
	
	private HashMap annotationSpansByAnnotationID = new HashMap();
	private AnnotationSpan[] getSpansForAnnotationID(String annotationID) {
//		ArrayList spanList = new ArrayList();
//		//	TODOne: use Hash index
//		for (int s = 0; s < annotationSpans.length; s++) {
//			if (annotationSpans[s].annotation.getAnnotationID().equals(annotationID))
//				spanList.add(annotationSpans[s]);
//		}
//		return ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
		ArrayList spanList = ((ArrayList) this.annotationSpansByAnnotationID.get(annotationID));
		if (spanList == null) {
			spanList = new ArrayList();
			for (int s = 0; s < annotationSpans.length; s++) {
				if (annotationSpans[s].annotation.getAnnotationID().equals(annotationID))
					spanList.add(annotationSpans[s]);
			}
			this.annotationSpansByAnnotationID.put(annotationID, spanList);
		}
		return ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
	}
	
	private HashMap highlightLayoutsByAnnotationType = new HashMap();
	private HashMap tagLayoutsByAnnotationType = new HashMap();
	private HashSet expandedTypePrefixes = new HashSet();
	
	private SimpleAttributeSet getHighlightLayoutForAnnotationType(String type) {
		SimpleAttributeSet layout = ((SimpleAttributeSet) this.highlightLayoutsByAnnotationType.get(type.toLowerCase()));
		if (layout == null) {
			layout = new SimpleAttributeSet();
			layout.addAttribute(StyleConstants.ColorConstants.Background, this.getAnnotationHighlightColor(type).brighter());
			this.highlightLayoutsByAnnotationType.put(type.toLowerCase(), layout);
		}
		return layout;
	}
	
	private SimpleAttributeSet getTagLayoutForAnnotationType(String type) {
		SimpleAttributeSet layout = ((SimpleAttributeSet) this.tagLayoutsByAnnotationType.get(type.toLowerCase()));
		if (layout == null) {
			layout = new SimpleAttributeSet();
			layout.addAttribute(StyleConstants.ColorConstants.Background, this.getAnnotationHighlightColor(type));
			this.tagLayoutsByAnnotationType.put(type.toLowerCase(), layout);
		}
		return layout;
	}
	
	private TypePanel getAnnotationTypeLayoutPanel(String annotationType) {
		//	TypePanel exists
		if (this.annotationSettings.containsKey(annotationType))
			return ((TypePanel) this.annotationSettings.get(annotationType));
		
		//	build TypePanel
		TypePanel tp = null;
		
		//	get model from parent if latter exists
		if (this.layoutParent != null) {
			TypePanel model = this.layoutParent.getAnnotationTypeLayoutPanel(annotationType);
			if (model != null) tp = new TypePanel(this, model);
		}
		
		//	create TypePanel
		if (tp == null) tp = new TypePanel(this, annotationType, getDefaultAnnotationColor(annotationType));
		
		//	remember & return TypePanel
		this.annotationSettings.put(annotationType, tp);
		return tp;
	}
	
	boolean showHighlight(String annotationType, boolean refreshOnChange) {
		boolean changed = this.highlightAnnotationTypes.add(annotationType);
		if (changed) {
			this.getAnnotationTypeLayoutPanel(annotationType).highlight.setSelected(true);
			for (int s = 0; s < this.annotationSpans.length; s++) {
				AnnotationSpan span = this.annotationSpans[s];
				if ((span instanceof AnnotationHighlightSpan) && span.annotation.getType().equals(annotationType))
					span.isShowing = true;
			}
			if (refreshOnChange) this.applySpans();
		}
		return changed;
	}
	
	boolean hideHighlight(String annotationType, boolean refreshOnChange) {
		boolean changed = this.highlightAnnotationTypes.remove(annotationType);
		if (changed) {
			this.getAnnotationTypeLayoutPanel(annotationType).highlight.setSelected(false);
			for (int s = 0; s < this.annotationSpans.length; s++) {
				AnnotationSpan span = this.annotationSpans[s];
				if ((span instanceof AnnotationHighlightSpan) && span.annotation.getType().equals(annotationType))
					span.isShowing = false;
			}
			if (refreshOnChange) this.applySpans();
		}
		return changed;
	}
	
	boolean isHighlighted(String annotationType) {
		return this.highlightAnnotationTypes.contains(annotationType);
	}
	
	String[] getHighlightTypes() {
		ArrayList ttl = new ArrayList(this.highlightAnnotationTypes);
		Collections.sort(ttl, ANNOTATION_TYPE_ORDER);
		return ((String[]) ttl.toArray(new String[ttl.size()]));
	}
	
	boolean showTags(String annotationType, boolean refreshOnChange) {
		boolean changed = this.taggedAnnotationTypes.add(annotationType);
		if (changed) {
			this.getAnnotationTypeLayoutPanel(annotationType).showTag.setSelected(true);
			if (refreshOnChange) this.refreshDisplay();
		}
		return changed;
	}
	
	boolean hideTags(String annotationType, boolean refreshOnChange) {
		boolean changed = this.taggedAnnotationTypes.remove(annotationType);
		if (changed) {
			this.getAnnotationTypeLayoutPanel(annotationType).showTag.setSelected(false);
			if (refreshOnChange) this.refreshDisplay();
		}
		return changed;
	}
	
	boolean isShowingTags(String annotationType) {
		return this.taggedAnnotationTypes.contains(annotationType);
	}
	
	String[] getTaggedTypes() {
		ArrayList ttl = new ArrayList(this.taggedAnnotationTypes);
		Collections.sort(ttl, ANNOTATION_TYPE_ORDER);
		return ((String[]) ttl.toArray(new String[ttl.size()]));
	}
	
	Color getAnnotationHighlightColor(String annotationType) {
		if (this.layoutParent != null)
			return this.layoutParent.getAnnotationHighlightColor(annotationType);
		else {
			TypePanel tp = this.getAnnotationTypeLayoutPanel(annotationType);
			return tp.color;
		}
	}
	
	void setAnnotationColor(String annotationType, Color color) {
		SimpleAttributeSet highlightLayout = this.getHighlightLayoutForAnnotationType(annotationType);
		highlightLayout.addAttribute(StyleConstants.ColorConstants.Background, color.brighter());
		SimpleAttributeSet tagLayout = this.getTagLayoutForAnnotationType(annotationType);
		tagLayout.addAttribute(StyleConstants.ColorConstants.Background, color);
		
		if (instances.size() == 1) // if only instance, write color to default settings
			defaultAnnotationColors.put(annotationType, color);
	}
	
	private class TypePanel extends JPanel {
		
		private AnnotationEditorPanel target;
		private TypePanel model = null;
		private String annotationType;
		private JLabel typeLabel;
		
		JPanel colorLabel = new JPanel(); 
		JCheckBox highlight = new JCheckBox();
		JCheckBox showTag = new JCheckBox();
		Color color;
		
		TypePanel(AnnotationEditorPanel target, String annotationType, Color color) {
			super(new FlowLayout(FlowLayout.LEFT));
			this.target = target;
			this.annotationType = annotationType;
			this.color = color;
			this.init();
		}
		
		TypePanel(AnnotationEditorPanel target, TypePanel model) {
			super(new FlowLayout(FlowLayout.LEFT));
			this.target = target;
			this.model = model;
			this.annotationType = model.annotationType;
			this.highlight.setSelected(this.model.highlight.isSelected());
			this.showTag.setSelected(this.model.showTag.isSelected());
			this.color = model.color;
			this.init();
		}
		
		private void init() {
			this.highlight.setToolTipText("Highlight Annotation value.");
			this.highlight.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = highlight.isSelected();
					((DocumentEditor) target.parent).writeLog("Display Control: values of '" + annotationType + "' annotations " + (selected ? "highlighted" : "hidden"));					
					if (selected) target.showHighlight(annotationType, true);
					else target.hideHighlight(annotationType, true);
				}
			});
			this.add(this.highlight);
			
			this.showTag.setToolTipText("Show Annotation XML tags.");
			this.showTag.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					boolean selected = showTag.isSelected();
					((DocumentEditor) target.parent).writeLog("Display Control: tags of '" + annotationType + "' annotations " + (selected ? "highlighted" : "hidden"));					
					if (selected) target.showTags(annotationType, true);
					else target.hideTags(annotationType, true);
				}
			});
			this.add(this.showTag);
			
			this.colorLabel.setBackground(this.color);
			this.add(this.colorLabel);
			
			this.typeLabel = new JLabel(((this.annotationType.indexOf(':') == -1) ? "" : "  ") + this.annotationType);
			this.typeLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					changeColor();
				}
			});
			this.add(this.typeLabel);
		}
		
		private void changeColor() {
			Color newColor = JColorChooser.showDialog(this, ("Highlight Color for " + this.annotationType), this.color);
			if (newColor != null) {
				((DocumentEditor) target.parent).writeLog("Display Control: color for '" + annotationType + "' annotations changed");					
				this.setColor(newColor, true);
			}
		}
		
		private void setColor(Color color, boolean refreshOnChange) {
			this.color = color;
			this.colorLabel.setBackground(this.color);
			setAnnotationColor(this.annotationType, this.color);
			if (refreshOnChange) applySpans();
			if (this.model != null) this.model.setColor(color, refreshOnChange);
		}
	}
	
	private class TypePrefixPanel extends JPanel {
		private TypePrefixPanel(final String prefix) {
			super(new FlowLayout());
			this.add(new JLabel(prefix));
			
			//	prefix is expanded, add button for collapsing it
			if (expandedTypePrefixes.contains(prefix)) {
				JLabel actionLabel = new JLabel(" (Collapse)");
				actionLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						collapsePrefix(prefix);
					}
				});
				this.add(actionLabel);
				
			//	prefix is collapsed, add button for expanding it
			} else {
				JLabel actionLabel = new JLabel(" (Expand)");
				actionLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						expandPrefix(prefix);
					}
				});
				this.add(actionLabel);
			}
		}
	}
	
	private void expandPrefix(String prefix) {
		this.expandedTypePrefixes.add(prefix);
		this.layoutTypePanels();
		this.validate();
	}
	
	private void collapsePrefix(String prefix) {
		this.expandedTypePrefixes.remove(prefix);
		this.layoutTypePanels();
		this.validate();
	}
	
	private void layoutTypePanels() {
		this.displayControlPanel.removeAll();
		
		this.displayControlPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		if (this.content != null) {
			String[] annotationTypes = this.content.getAnnotationTypes();
			Arrays.sort(annotationTypes, ANNOTATION_TYPE_ORDER);
			int pIndex = 0;
			HashSet placeholderAdded = new HashSet();
			for (int t = 0; t < annotationTypes.length; t++) {
				if (!TEMP_ANNOTATION_TYPE.equals(annotationTypes[t])) {
					int colonIndex = annotationTypes[t].indexOf(':');
					
					//	generic type
					if (colonIndex == -1) {
						TypePanel tp = this.getAnnotationTypeLayoutPanel(annotationTypes[t]);
						gbc.gridy = pIndex++;
						this.displayControlPanel.add(tp, gbc.clone());
					}
					
					//	type with namespace prefix
					else {
						
						//	get prefix
						String prefix = annotationTypes[t].substring(0, colonIndex);
						
						//	placeholder not added yet
						if (placeholderAdded.add(prefix)) {
							gbc.gridy = pIndex++;
							this.displayControlPanel.add(new TypePrefixPanel(prefix), gbc.clone());
						}
						
						//	prefix expanded
						if (this.expandedTypePrefixes.contains(prefix)) {
							TypePanel tp = this.getAnnotationTypeLayoutPanel(annotationTypes[t]);
							gbc.gridy = pIndex++;
							this.displayControlPanel.add(tp, gbc.clone());
						}
					}
				}
			}
			gbc.gridy = pIndex++;
		}
		gbc.weighty = 1;
		this.displayControlPanel.add(new JPanel(), gbc.clone());
		
		this.displayControlPanelBox.validate();
		this.displayControlPanelBox.repaint();
	}
	
	void configureDisplay() {
		
		ConfigureDisplayDialog cdd = new ConfigureDisplayDialog();
		cdd.setVisible(true);
		
		if (cdd.committed) {
			boolean needSpans = false;
			boolean needRefresh = false;
			for (int t = 0; t < cdd.configTypePanels.size(); t++) {
				ConfigureDisplayDialog.ConfigTypePanel ctp = ((ConfigureDisplayDialog.ConfigTypePanel) cdd.configTypePanels.get(t));
				
				if (ctp.showTag.isSelected()) needRefresh = (needRefresh | this.showTags(ctp.model.annotationType, false));
				else needRefresh = (needRefresh | this.hideTags(ctp.model.annotationType, false));
				
				if (ctp.highlight.isSelected()) needSpans = (needSpans | this.showHighlight(ctp.model.annotationType, false));
				else needSpans = (needSpans | this.hideHighlight(ctp.model.annotationType, false));
				
				if (!ctp.color.equals(ctp.model.color)) {
					ctp.model.setColor(ctp.color, false);
					needSpans = true;
				}
			}
			if (needRefresh) this.refreshDisplay();
			else if (needSpans) this.applySpans();
		}
	}
	
	private class ConfigureDisplayDialog extends DialogPanel {
		private HashSet configExpandedTypePrefixes = new HashSet();
		private JPanel configTypePanelBox = new JPanel(new GridBagLayout());
		
		boolean committed = false;
		ArrayList configTypePanels = new ArrayList();
		
		private ConfigureDisplayDialog() {
			super("Display Control", true);
			this.configExpandedTypePrefixes.addAll(expandedTypePrefixes);
			
			int panelCount = this.configLayoutTypePanels();
			
			this.getContentPane().setLayout(new BorderLayout());
			JScrollPane panelBox = new JScrollPane(this.configTypePanelBox);
			panelBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			panelBox.getVerticalScrollBar().setUnitIncrement(50);
			panelBox.getVerticalScrollBar().setBlockIncrement(100);
			this.getContentPane().add(panelBox, BorderLayout.CENTER);
			
			JButton okButton = new JButton("OK");
			okButton.setBorder(BorderFactory.createRaisedBevelBorder());
			okButton.setPreferredSize(new Dimension(100, 21));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					committed = true;
					dispose();
				}
			});
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
			cancelButton.setPreferredSize(new Dimension(100, 21));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);
			this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
			//	get feedback
			this.setSize(300, Math.min(((25 * panelCount) + 70), 500));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		private int configLayoutTypePanels() {
			this.configTypePanelBox.removeAll();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			
			int panelCount = 0;
			if (content != null) {
				String[] annotationTypes = content.getAnnotationTypes();
				Arrays.sort(annotationTypes, ANNOTATION_TYPE_ORDER);
				int pIndex = 0;
				HashSet placeholderAdded = new HashSet();
				for (int t = 0; t < annotationTypes.length; t++) {
					if (!TEMP_ANNOTATION_TYPE.equals(annotationTypes[t])) {
						int colonIndex = annotationTypes[t].indexOf(':');
						
						//	generic type
						if (colonIndex == -1) {
							TypePanel modelTp = getAnnotationTypeLayoutPanel(annotationTypes[t]);
							ConfigTypePanel ctp = new ConfigTypePanel(modelTp);
							configTypePanels.add(ctp);
							gbc.gridy = pIndex++;
							this.configTypePanelBox.add(ctp, gbc.clone());
							panelCount++;
						}
						
						//	type with namespace prefix
						else {
							
							//	get prefix
							String prefix = annotationTypes[t].substring(0, colonIndex);
							
							//	placeholder not added yet
							if (placeholderAdded.add(prefix)) {
								gbc.gridy = pIndex++;
								this.configTypePanelBox.add(new ConfigTypePrefixPanel(prefix), gbc.clone());
								panelCount++;
							}
							
							//	prefix expanded
							if (this.configExpandedTypePrefixes.contains(prefix)) {
								TypePanel modelTp = getAnnotationTypeLayoutPanel(annotationTypes[t]);
								ConfigTypePanel ctp = new ConfigTypePanel(modelTp);
								configTypePanels.add(ctp);
								gbc.gridy = pIndex++;
								this.configTypePanelBox.add(ctp, gbc.clone());
							}
							panelCount++;
						}
					}
				}
				gbc.gridy = pIndex++;
			}
			gbc.weighty = 1;
			configTypePanelBox.add(new JPanel(), gbc.clone());
			return panelCount;
		}
		
		private class ConfigTypePanel extends JPanel {
			
			private TypePanel model;
			
			JPanel colorLabel = new JPanel(); 
			JCheckBox highlight = new JCheckBox();
			JCheckBox showTag = new JCheckBox();
			Color color;
			
			ConfigTypePanel(TypePanel model) {
				super(new FlowLayout(FlowLayout.LEFT));
				this.model = model;
				this.color = model.color;
				
				this.highlight.setToolTipText("Highlight Annotation value.");
				this.highlight.setSelected(model.highlight.isSelected());
				this.add(this.highlight);
				
				this.showTag.setToolTipText("Show Annotation XML tags.");
				this.showTag.setSelected(model.showTag.isSelected());
				this.add(this.showTag);
				
				this.colorLabel.setBackground(this.color);
				this.add(this.colorLabel);
				
				JLabel typeLabel = new JLabel(model.annotationType);
				typeLabel.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent e) {
						changeColor();
					}
				});
				this.add(typeLabel);
			}
			
			private void changeColor() {
				Color newColor = JColorChooser.showDialog(this, ("Highlight Color for " + this.model.annotationType), this.color);
				if (newColor != null) {
					this.color = newColor;
					this.colorLabel.setBackground(this.color);
				}
			}
		}
		
		private class ConfigTypePrefixPanel extends JPanel {
			private ConfigTypePrefixPanel(final String prefix) {
				super(new FlowLayout());
				this.add(new JLabel(prefix));
				
				//	prefix is expanded, add button for collapsing it
				if (configExpandedTypePrefixes.contains(prefix)) {
					JLabel actionLabel = new JLabel(" (Collapse)");
					actionLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							configCollapsePrefix(prefix);
						}
					});
					this.add(actionLabel);
					
				//	prefix is collapsed, add button for expanding it
				} else {
					JLabel actionLabel = new JLabel(" (Expand)");
					actionLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent e) {
							configExpandPrefix(prefix);
						}
					});
					this.add(actionLabel);
				}
			}
		}
		
		private void configExpandPrefix(String prefix) {
			this.configExpandedTypePrefixes.add(prefix);
			this.configLayoutTypePanels();
			this.validate();
		}
		
		private void configCollapsePrefix(String prefix) {
			this.configExpandedTypePrefixes.remove(prefix);
			this.configLayoutTypePanels();
			this.validate();
		}
	}
	
	private static final boolean DEBUG_APPLY_SPANS = false;
	
	private void applySpans() {
		this.applySpans(0, this.displayPanel.getDocumentLength(), true);
	}
	
	private void applySpans(boolean validateImmediately) {
		this.applySpans(0, this.displayPanel.getDocumentLength(), validateImmediately);
	}
	
	private void applySpans(int startOffset, int size) {
		this.applySpans(startOffset, size, true);
	}
	
	private void applySpans(int startOffset, int size, boolean validateImmediately) {
		
		if (DEBUG_APPLY_SPANS) System.out.println("AnnotationEditorPanel: applying spans ...");
		long time = System.currentTimeMillis();
		
		//	refresh font styles
		this.textFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTextFontName());
		this.textFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTextFontSize()));
		this.textFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTextFontColor());
		this.textFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
		
		this.tagFontStyle.addAttribute(StyleConstants.FontConstants.Family, this.parent.getTagFontName());
		this.tagFontStyle.addAttribute(StyleConstants.FontConstants.Size, new Integer(this.parent.getTagFontSize()));
		this.tagFontStyle.addAttribute(StyleConstants.ColorConstants.Foreground, this.parent.getTagFontColor());
		this.tagFontStyle.addAttribute(StyleConstants.ColorConstants.Background, Color.WHITE);
		
		//	compute affected range
		int affectStart = startOffset;
		int affectEnd = (startOffset + size);
		boolean grown = false;
		do {
			grown = false;
			for (int s = 0; s < this.annotationSpans.length; s++) {
				AnnotationSpan span = this.annotationSpans[s];
				if (span.isShowing && (((span.getStartOffset() <= affectStart) && (span.getEndOffset() > affectStart)) || ((span.getStartOffset() < affectEnd) && (span.getEndOffset() >= affectEnd) || ((span.getStartOffset() >= affectStart) && (span.getEndOffset() <= affectEnd))))) {
					grown = (grown || (span.getStartOffset() < affectStart) || (span.getEndOffset() > affectEnd));
					affectStart = Math.min(affectStart, span.getStartOffset());
					affectEnd = Math.max(affectEnd, span.getEndOffset());
				}
			}
		} while (grown);
		if (DEBUG_APPLY_SPANS) System.out.println("AnnotationEditorPanel: - range computed in " + (System.currentTimeMillis() - time) + " ms.");
		time = System.currentTimeMillis();
		
		//	set styles
		this.displayPanel.setCharacterAttributes(affectStart, (affectEnd - affectStart), this.textFontStyle, true);
		if (DEBUG_APPLY_SPANS) System.out.println("AnnotationEditorPanel: - global attributes set in " + (System.currentTimeMillis() - time) + " ms.");
		time = System.currentTimeMillis();
		
		for (int s = 0; s < this.annotationSpans.length; s++) {
			AnnotationSpan span = this.annotationSpans[s];
			if (span.isShowing && (span.getStartOffset() >= affectStart) && (span.getEndOffset() <= affectEnd)) {
				
				int spanStart = Math.max(span.getStartOffset(), startOffset);
				int spanEnd = Math.min(span.getEndOffset(), (startOffset + size));
				int spanSize = (spanEnd - spanStart);
				if (spanSize > 0) {
					
					//	highlight span
					if (span instanceof AnnotationHighlightSpan) {
						this.displayPanel.setCharacterAttributes(spanStart, spanSize, this.textFontStyle, true);
						this.displayPanel.setCharacterAttributes(spanStart, spanSize, span.colorLayout, false);
					}
					
					//	tag span
					else {
						this.displayPanel.setCharacterAttributes(spanStart, spanSize, this.tagFontStyle, true);
						
						spanStart = Math.max((span.getStartOffset() + ((AnnotationTagSpan) span).indent), startOffset);
						spanSize = (spanEnd - spanStart);
						this.displayPanel.setCharacterAttributes(spanStart, spanSize, span.colorLayout, false);
					}
				}
			}
		}
		if (DEBUG_APPLY_SPANS) System.out.println("AnnotationEditorPanel: - span attributes set in " + (System.currentTimeMillis() - time) + " ms.");
		time = System.currentTimeMillis();
		
		if (validateImmediately) {
			this.displayPanel.validate();
			if (DEBUG_APPLY_SPANS) System.out.println("AnnotationEditorPanel: - display validated in " + (System.currentTimeMillis() - time) + " ms.");
			try { //  show caret
				this.displayPanel.display.getCaret().setVisible(true);
				this.displayPanel.display.getCaret().setBlinkRate(500);
			} catch (Exception e) {}
		}
	}
	
	private static final boolean DEBUG_TYPING = true;
	
	private class TokenSequenceObserver implements TokenSequenceListener {
		TokenSequenceEvent change;
		public void tokenSequenceChanged(TokenSequenceEvent change) {
			this.change = change;
		}
	}
	
	private void handleKeyStroke(KeyEvent ke, char lastKey, int lastKeyCode, int caretPosition) {
		if (DEBUG_TYPING) System.out.println("Key typed in AnnotationEditor");
		if (DEBUG_TYPING) System.out.println(" - last key is '" + lastKey + "'");
		if (DEBUG_TYPING) System.out.println(" - char is '" + ke.getKeyChar() + "'");
		if (DEBUG_TYPING) System.out.println(" - as int " + ((int) ke.getKeyChar()));
		if (DEBUG_TYPING) System.out.println("   - shift: " + ke.isShiftDown());
		if (DEBUG_TYPING) System.out.println("   - control: " + ke.isControlDown());
		if (DEBUG_TYPING) System.out.println("   - alt: " + ke.isAltDown());
		if (DEBUG_TYPING) System.out.println("   - alt-gr: " + ke.isAltGraphDown());
		if (DEBUG_TYPING) System.out.println(" - caret at " + caretPosition);
		
		//	check if 'Ctrl' is down, chars are somehow messed up in this case, so ignore event
		if (ke.isControlDown()) return;
		
		char keyChar = ke.getKeyChar();
		
		//	check for selection (if there is a selection, keystrokes are commands)
		String selectedText = this.displayPanel.getSelectedText();
		if ((selectedText != null) && (selectedText.length() != 0)) {
			char nKeyChar = Character.toLowerCase(keyChar);
			Annotation selected = this.annotateSelection(TEMP_ANNOTATION_TYPE, false);
			MutableAnnotation[] selectedAnnotations = this.getAnnotationsForSelectedTags();
			
			//	tokens selected
			if (selected != null) {
				
				//	custom shortcut
				CustomShortcut cs = this.host.getCustomShortcut(("" + nKeyChar).toUpperCase(), ke.isAltDown(), ke.isAltGraphDown());
				if (cs != null) {
					this.executeCustomShortcut(cs, ke.isShiftDown());
					return;
				}
				
				//	Atl mask shortcut
				else if (ke.isAltDown()) {
					if ('n' == nKeyChar) {
						this.findNext(annotateAllCaseSensitive);
						return;
					}
					else if ('p' == nKeyChar) {
						this.findPrevious(annotateAllCaseSensitive);
						return;
					}
					else if ('f' == nKeyChar) {
						this.doFindReplace();
						return;
					}
				}
				
				//	plain shortcut
				else {
					if ('e' == nKeyChar) {
						this.editSelection();
						return;
					}
					else if ('a' == nKeyChar) {
						this.annotate();
						return;
					}
					else if ('s' == nKeyChar) {
						this.splitAnnotation(ke.isShiftDown(), false);
						return;
					}
					else if ((keyChar == 8) || (keyChar == 127)) {
						this.deleteTokens();
						return;
					}
				}
			}
			
			//	annotation tags selected 
			else if (selectedAnnotations.length > 1) {
				
				//	Atl mask shortcut
				if (ke.isAltDown()) {}
				
				//	plain shortcut
				else {
					if ('m' == nKeyChar) {
						this.mergeAnnotations();
						return;
					}
					else if ((keyChar == 8) || (keyChar == 127)) {
						this.deleteTokens();
						return;
					}
				}
			}
			
			//	single annotation tag selected
			else if (selectedAnnotations.length == 1) {
				boolean changed = false;
				
				//	Atl mask shortcut
				if (ke.isAltDown()) {}
				
				//	plain shortcut
				else {
					if (keyChar == ' ') changed = this.editAnnotationAttributes(selectedAnnotations[0]);
					else if ((keyChar == '\n') || (keyChar == '\r')) changed = this.editAnnotation(selectedAnnotations[0]);
					else if (nKeyChar == 'r') {
						this.renameAnnotation(selectedAnnotations[0].getAnnotationID());
						return;
					}
					else if (nKeyChar == 'e') {
						this.editSelection(selectedAnnotations[0]);
						return;
					}
					else if (keyChar == 127) {
						this.removeAnnotation(selectedAnnotations[0].getAnnotationID());
						return;
					}
					else if (keyChar == 8) {
						this.deleteTokens(selectedAnnotations[0].getAnnotationID());
						return;
					}
				}
				
				if (changed) try { //  valdate display, show caret
					this.displayPanel.setCaretPosition(caretPosition);
					this.displayPanel.display.getCaret().setVisible(true);
					this.displayPanel.display.getCaret().setBlinkRate(500);
					return;
				} catch (Exception e) {}
			}
			if (DEBUG_TYPING) System.out.println(" ==> there's a selection, which is handled token-wise ...");
			return;
		}
		
		//	map backspace to delete
		if (keyChar == 8) caretPosition--;
		
		//	check if insert or delete
		boolean isDelete = ((keyChar == 8) || (keyChar == 127));
		if (isDelete) ke.consume();
		AnnotationTagSpan aTagSpan = this.getAnnotationTagSpanForPosition(caretPosition);
		
		//	handle keystroke in tokens (in particular, one that does not affect an annotation tag)
		if (aTagSpan == null) {
			
			//	TODO prevent inserting whitespace before line break
			
			//	find offset in char sequence
			int cPosShift = 0;
			TokenSpan tSpan = this.getTokenSpanForPosition(caretPosition - cPosShift);
			while ((cPosShift < caretPosition) && (tSpan == null)) {
				cPosShift++;
				tSpan = this.getTokenSpanForPosition(caretPosition - cPosShift);
			}
			
			//	change before first token, can only be between tags, not allowed
			if (cPosShift == caretPosition) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			if (DEBUG_TYPING) System.out.println(" - token index before is " + tSpan.tokenIndex + ", start offset " + tSpan.getStartOffset());
			
			//	compute change offset in char sequence
			if (DEBUG_TYPING) {
				System.out.println(" - token is '" + tSpan.token.getValue() + "'");
				System.out.println(" - token start offset is " + tSpan.token.getStartOffset());
				System.out.println(" - caret position is " + caretPosition);
				try {
					System.out.println(" - char at caret position is " + this.displayPanel.getText(caretPosition, 1));
				}
				catch (BadLocationException ble) {
					ble.printStackTrace();
				}
				System.out.println(" - token span start offset is " + tSpan.getStartOffset());
			}
			int changeOffset = tSpan.token.getStartOffset() + (caretPosition - tSpan.getStartOffset());
			if (DEBUG_TYPING) System.out.println(" - change offset is " + changeOffset);
			
			//	whitespace appended after last token, not allowed
			if ((changeOffset == this.content.length()) && (isDelete || (keyChar < 33))) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			if (DEBUG_TYPING) System.out.println(" - char at change offset is " + this.content.charAt(changeOffset));
			
			//	count number of tokens affected at maximum
			int maxAffectedTokenCount = 1;
			
			int lastAffectedTokenShift = 0;
			int lastAffectedTokenEnd = tSpan.token.getEndOffset();
			while (((tSpan.tokenIndex + 1 + lastAffectedTokenShift) < this.tokenSpans.length) && (lastAffectedTokenEnd == this.tokenSpans[tSpan.tokenIndex + 1 + lastAffectedTokenShift].token.getStartOffset())) {
				lastAffectedTokenEnd = this.tokenSpans[tSpan.tokenIndex + 1 + lastAffectedTokenShift].token.getEndOffset();
				lastAffectedTokenShift++;
				maxAffectedTokenCount++;
			}
			int firstAffectedTokenShift = 0;
			int firstAffectedTokenStart = tSpan.token.getStartOffset();
			while (((tSpan.tokenIndex - 1 - firstAffectedTokenShift) != -1) && (firstAffectedTokenStart == this.tokenSpans[tSpan.tokenIndex - 1 - firstAffectedTokenShift].token.getEndOffset())) {
				firstAffectedTokenStart = this.tokenSpans[tSpan.tokenIndex - 1 - firstAffectedTokenShift].token.getStartOffset();
				firstAffectedTokenShift++;
				maxAffectedTokenCount++;
			}
			
			if (DEBUG_TYPING) System.out.println(" - tokens affected at max are " + maxAffectedTokenCount);
			
			//	record changes to token sequence in order to adjust spans
			TokenSequenceObserver tso = new TokenSequenceObserver();
			this.content.addTokenSequenceListener(tso);
			
			//	start recording undo
			this.parent.enqueueRestoreContentUndoAction("Typing", (tSpan.tokenIndex - firstAffectedTokenShift), maxAffectedTokenCount);
			
			//	do change & write to display
			if (!this.displayPanel.setSelectionStart(caretPosition)) {
				Toolkit.getDefaultToolkit().beep();
				return;
			}
			if (isDelete) {
				if (this.displayPanel.setSelectionEnd(caretPosition + 1)) {
					char rem = this.content.removeChar(changeOffset);
					this.displayPanel.replaceSelection("");
					if (DEBUG_TYPING) System.out.println(" - char removed '" + rem + "'");
				}
				else {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
//				char rem = this.content.removeChar(changeOffset);
//				if (DEBUG_TYPING) System.out.println(" - char removed '" + rem + "'");
//				this.displayPanel.setSelectionEnd(caretPosition + 1);
//				this.displayPanel.replaceSelection("");
			}
			else {
				if (this.displayPanel.setSelectionEnd(caretPosition)) {
					this.content.insertChar(keyChar, changeOffset);
					this.displayPanel.replaceSelection("" + keyChar);
					if (DEBUG_TYPING) System.out.println(" - char inserted '" + keyChar + "'");
				}
				else {
					Toolkit.getDefaultToolkit().beep();
					return;
				}
//				this.content.insertChar(keyChar, changeOffset);
//				if (DEBUG_TYPING) System.out.println(" - char inserted '" + keyChar + "'");
//				this.displayPanel.setSelectionEnd(caretPosition);
//				this.displayPanel.replaceSelection("" + keyChar);
			}
			
			//	action complete
			this.parent.storeUndoAction();
			
			//	stop recording
			this.content.removeTokenSequenceListener(tso);
			
			//	print info
			if (DEBUG_TYPING && (tso.change != null)) {
				System.out.println(" - token change index is " + tso.change.index);
				System.out.println(" - inserted is '" + tso.change.inserted + "'");
				System.out.println(" - removed is '" + tso.change.removed + "'");
			}
			
			//	check if token span was the one changed
			if ((tso.change != null) && (tso.change.index != tSpan.tokenIndex) && ((tso.change.inserted.size() + tso.change.removed.size()) != 0)) {
				tSpan = this.tokenSpans[tso.change.index];
				if (DEBUG_TYPING) System.out.println(" - changed affected token to '" + tSpan.token.getValue() + "'");
			}
			
			//	adjust subsequent spans
			this.adjustSpans(
						(
							(
								(keyChar < 33)
								&&
								!isDelete
								&&
								(caretPosition == tSpan.getStartOffset())
							)
								?
								tSpan.getStartOffset()
								:
								tSpan.getEndOffset()
							)
						,
						(isDelete ? -1 : 1)
					);
			
			//	no (real) change to token overlay, probably whitespace edit
			if ((tso.change == null) || ((tso.change.inserted.size() == 0) && (tso.change.removed.size() == 0))) {
				
				//	nothing to do here
			}
			
			//	change to token value
			else if ((tso.change.inserted.size() == 1) && (tso.change.removed.size() == 1)) {
				
				//	adjust affected token span
				if ((changeOffset < tSpan.token.getEndOffset()) || (!isDelete && (changeOffset == tSpan.token.getEndOffset())))
					tSpan.setSize(tSpan.getSize() + (isDelete ? -1 : 1));
			}
			
			//	something more sophisticated happened
			else {
				
				//	compute the delta in the number of tokens
				int tokenCountDelta = tso.change.inserted.size() - tso.change.removed.size();
				
				//	copy token spans before change
				TokenSpan[] newTokenSpans = new TokenSpan[this.tokenSpans.length + tokenCountDelta];
				System.arraycopy(this.tokenSpans, 0, newTokenSpans, 0, tso.change.index);
				
				//	mark token spans to be removed as invalid
				for (int s = 0; s < tso.change.removed.size(); s++)
					this.tokenSpans[tso.change.index + s].tokenIndex = -1;
				
				//	determine starting and ending positions of the change in the display
				int changeStartPos = this.tokenSpans[tso.change.index].getStartOffset();
				int changeEndPos = (changeStartPos + Math.max(tso.change.inserted.length(), tso.change.removed.length()));
				
				//	create new spans for inserted tokens
				for (int s = 0; s < tso.change.inserted.size(); s++)
					newTokenSpans[tso.change.index + s] = new TokenSpan((changeStartPos + tso.change.inserted.tokenAt(s).getStartOffset()), (tso.change.index + s), this.content.tokenAt(tso.change.index + s));
				
				//	copy remaining token spans, ignoring those belonging to removed tokens
				for (int s = (tso.change.index + tso.change.removed.size()); s < this.tokenSpans.length; s++) {
					this.tokenSpans[s].tokenIndex += tokenCountDelta;
					newTokenSpans[s + tokenCountDelta] = this.tokenSpans[s];
				}
				this.tokenSpans = newTokenSpans;
				
				//	perform sanity check after inserting new token spans
				boolean sane = true;
				int sanityCorrections = 0;
				for (int s = Math.max(0, (tso.change.index-1)); s < ((sane ? Math.min((tso.change.index + tso.change.inserted.size()), (this.tokenSpans.length - 1)) : (this.tokenSpans.length - 1))); s++) {
					System.out.println("SANITY CHECK:");
					System.out.println(this.tokenSpans[s].getStartOffset() + ": " + this.content.valueAt(this.tokenSpans[s].tokenIndex));
					System.out.println(this.tokenSpans[s+1].getStartOffset() + ": " + this.content.valueAt(this.tokenSpans[s+1].tokenIndex));
					if ((s+2) < this.tokenSpans.length)
						System.out.println("(" + this.tokenSpans[s+2].getStartOffset() + ": " + this.content.valueAt(this.tokenSpans[s+2].tokenIndex) + ")");
					if (this.tokenSpans[s].getEndOffset() <= this.tokenSpans[s+1].getStartOffset()) {
						System.out.println("CHECK PASSED: " + this.tokenSpans[s].getEndOffset() + " <= " +  this.tokenSpans[s+1].getStartOffset());
						sane = true;
					}
					else {
						System.out.print("CHECK FAILED: " + this.tokenSpans[s].getEndOffset() + " > " +  this.tokenSpans[s+1].getStartOffset());
						this.tokenSpans[s+1].setStartOffset(this.tokenSpans[s].getEndOffset());
						sane = false;
						sanityCorrections++;
						System.out.println(" CORRECTED (" + sanityCorrections + ")");
					}
				}
				
				//	force refresh if too many token spans out of sync (too risky)
				boolean needRefresh = (sanityCorrections > maxAffectedTokenCount);
				
				//	filter out affected Annotations (refresh if tagged Annotation removed)
				int removedCount = 0;
				for (int s = 0; s < this.annotationSpans.length; s++) {
					
					//	check highlight spans
					if (this.annotationSpans[s] instanceof AnnotationHighlightSpan) {
						AnnotationHighlightSpan ahs = ((AnnotationHighlightSpan) this.annotationSpans[s]);
						
						//	start or end token span invalid
						if ((ahs.startToken.tokenIndex == -1) || (ahs.endToken.tokenIndex == -1)) {
							
							//	annotation removed, remove span
							if (ahs.annotation.size() < 1) {
								removedCount ++;
								this.annotationSpansByAnnotationID.remove(this.annotationSpans[s].annotation.getAnnotationID());
								this.annotationSpans[s] = null;
								
								//	check if tags visible
								needRefresh = (needRefresh || this.isShowingTags(ahs.annotation.getType()));
							}
							
							//	update tokens of span
							else {
								ahs.startToken = this.tokenSpans[ahs.annotation.getStartIndex()];
								ahs.endToken = this.tokenSpans[ahs.annotation.getEndIndex() - 1];
							}
						}
					}
				}
				
				//	refresh if necessary
				if (needRefresh) {
					this.refreshDisplay();
					return;
				}
				
				//	update otherwise
				else {
					
					//	adjust & filter annotation spans
					if (removedCount != 0) {
						AnnotationSpan[] newAnnotationSpans = new AnnotationSpan[this.annotationSpans.length - removedCount];
						int newSpanIndex = 0;
						for (int s = 0; s < this.annotationSpans.length; s++) {
							if (this.annotationSpans[s] != null) {
								newAnnotationSpans[newSpanIndex] = this.annotationSpans[s];
								newSpanIndex ++;
							}
						}
						this.annotationSpans = newAnnotationSpans;
					}
					
					//	inserting a line break causes havoc to the display layout, apply all spans
					if (keyChar == '\n') this.applySpans(false);
					
					//	apply spans to modified part of display
					else this.applySpans(changeStartPos, (changeEndPos - changeStartPos), false);
				}
			}
			
			try { //  valdate display, show caret
				this.displayPanel.validate();
				this.displayPanel.setCaretPosition(caretPosition + (isDelete ? 0 : 1));
				this.displayPanel.display.getCaret().setVisible(true);
				this.displayPanel.display.getCaret().setBlinkRate(500);
			} catch (Exception e) {}
			
			//	notify content changed
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			
			//	we're done
			return;
		}
		
		//	handle keystroke in tag
		if (aTagSpan != null) {
			try {
				if (DEBUG_TYPING) System.out.println(" - edit Annotation Tag '" + this.displayPanel.getText(aTagSpan.getStartOffset(), aTagSpan.getSize()) + "'");
				char nKeyChar = Character.toLowerCase(keyChar);
				boolean changed = false;
				if (keyChar == ' ') changed = this.editAnnotationAttributes(aTagSpan.annotation);
				else if (keyChar == '\n') changed = this.editAnnotation(aTagSpan.annotation);
				else if (nKeyChar == 'r') {
					this.renameAnnotation(aTagSpan.annotation.getAnnotationID());
					return;
				}
				else if (nKeyChar == 't') {
					this.editSelection(aTagSpan.annotation);
					return;
				}
				else if (keyChar == 127) {
					this.removeAnnotation(aTagSpan.annotation.getAnnotationID());
					return;
				}
				else if (keyChar == 8) {
					this.deleteTokens(aTagSpan.annotation.getAnnotationID());
					return;
				}
				if (changed) try { //  valdate display, show caret
					this.displayPanel.setCaretPosition(caretPosition);
					this.displayPanel.display.getCaret().setVisible(true);
					this.displayPanel.display.getCaret().setBlinkRate(500);
				} catch (Exception e) {}
				return;
			} catch (Exception e) {}
		}
		else {
			if (DEBUG_TYPING) System.out.println(" - edit not allowed between Token and Annotation Tag");
			Toolkit.getDefaultToolkit().beep();
			return;
		}
	}
	
	private void updateAnnotationTags(Annotation annotation) {
		AnnotationSpan[] spans = this.getSpansForAnnotation(annotation);
		for (int s = 0; s < spans.length; s++) {
			if (spans[s].isShowing && (spans[s] instanceof AnnotationTagSpan)) {
				AnnotationTagSpan span = ((AnnotationTagSpan) spans[s]);
				
				String tag = "<";
				if ("S".equals(span.tagType)) {
					tag += annotation.getType();
					String[] attributeNames = annotation.getAttributeNames();
					for (int a = 0; a < attributeNames.length; a++) {
						String aName = attributeNames[a];
						Object o = annotation.getAttribute(aName);
						if ((o != null) && (o instanceof String))
//							tag += (" " + aName + "=\"" + o.toString() + "\"");
							tag += (" " + aName + "=\"" + AnnotationUtils.escapeForXml(o.toString()) + "\"");
					}
					tag += ">";
				}
				else tag += ("/" + annotation.getType() + ">");
				
				this.displayPanel.setSelectionStart(span.getStartOffset() + span.indent);
				this.displayPanel.setSelectionEnd(span.getEndOffset() - 1);
				this.displayPanel.replaceSelection(tag);
				
				int oldLength = span.getEndOffset() - span.getStartOffset() - span.indent - 1;
				this.adjustSpans(span.getEndOffset(), (tag.length() - oldLength));
				span.setSize(span.indent + tag.length() + 1);
				this.applySpans(span.getStartOffset(), span.getSize());
				this.displayPanel.validate();
			}
		}
	}
	
	private void adjustSpans(int offset, int shift) {
		if (shift == 0) return;
		
		for (int s = 0; s < this.tokenSpans.length; s++) {
			if (this.tokenSpans[s].getStartOffset() >= offset)
				this.tokenSpans[s].setStartOffset(this.tokenSpans[s].getStartOffset() + shift);
		}
		
		for (int s = 0; s < this.annotationSpans.length; s++) {
			if ((this.annotationSpans[s] instanceof AnnotationTagSpan) && this.annotationSpans[s].getStartOffset() >= offset)
				this.annotationSpans[s].setStartOffset(this.annotationSpans[s].getStartOffset() + shift);
		}
	}
	
	private MutableAnnotation getAnnotationByID(String id) {
		return ((MutableAnnotation) this.annotationsByID.get(id));
	}
	
	void editSelection() {
		Annotation selection = this.annotateSelection(null, false);
		this.editSelection(selection);
	}
	
	void editAnnotationTokens(String annotationID) {
		if (annotationID != null) {
			Annotation annotation = this.getAnnotationByID(annotationID);
			if (annotation != null) this.editSelection(annotation);
		}
	}
	
	private void editSelection(Annotation selection) {
		if (selection == null) {
			JOptionPane.showMessageDialog(this, "Cannot edit whitespace. Select at least one word to edit.", "Cannot Edit", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//	prepare undo action before temp annotation might be added
		this.parent.enqueueRestoreContentUndoAction("Edit Selection", selection.getStartIndex(), selection.size());
		
		MutableAnnotation edit;
		if (selection instanceof MutableAnnotation)
			edit = ((MutableAnnotation) selection);
		else edit = this.content.addAnnotation(TEMP_ANNOTATION_TYPE, selection.getStartIndex(), selection.size());
		
		SelectionEditor se = new SelectionEditor(edit);
		se.setVisible(true);
		
		if (se.isChanged()) {
			
			//	write log entry and apply changes to main document
			this.parent.writeLog("Edit Selection: '" + selection.getValue() + "' edited.");
			se.writeChanges();
			
			//	remove temporary editing view only after changes are written
			if (TEMP_ANNOTATION_TYPE.equals(edit.getType()))
				this.content.removeAnnotation(edit);
			
			//	store undo action and notify of canges
			this.parent.storeUndoAction();
			this.parent.notifyDocumentTextModified();
			
			//	make changes visible
			this.refreshDisplay();
		}
		
		//	even if not edited, remove temporary editing view
		else if (TEMP_ANNOTATION_TYPE.equals(edit.getType()))
			this.content.removeAnnotation(edit);
	}
	
	/**
	 * dialog for editing small portions of text (higher performance than editing in window itself)
	 * 
	 * @author sautter
	 */
	private class SelectionEditor extends DialogPanel implements DocumentListener {
		private MutableAnnotation content;
		private MutableAnnotation originalContent;
		
		private JTextArea display = new JTextArea();
		
		private boolean dirty = false;
		private boolean isCommitted = false;
		
		SelectionEditor(MutableAnnotation content) {
			super(((JFrame) null), "Edit Selection", true);
			this.originalContent = content;
			this.content = Gamta.copyDocument(content);
			try {
				AnnotationUtils.writeXML(this.content, new OutputStreamWriter(System.out));
			} catch (Exception e) {}
//			this.content.addCharSequenceListener(new AtomRecorder());
			this.content.addCharSequenceListener(new CharSequenceListener() {
				public void charSequenceChanged(CharSequenceEvent change) {
					if (SelectionEditor.this.content.size() == 0)
						redoEvents = null;
					else if (redoEvents != null)
						redoEvents.add(new RedoCharSequenceEvent(change, ((change.offset + change.inserted.length()) == SelectionEditor.this.content.length())));
				}
			});
			this.content.addTokenSequenceListener(new TokenSequenceListener() {
				public void tokenSequenceChanged(TokenSequenceEvent change) {
					System.out.println("TokenSequence: '" + change.inserted + "' inserted at " + change.index + ", removing '" + change.removed + "'");
				}
			});
			
			this.display.setLineWrap(true);
			this.display.setWrapStyleWord(true);
			this.display.setEditable(true);
			DocumentSynchronizer.synchronizeDocuments(this.content, this.display);
			this.display.getDocument().addDocumentListener(this);
			
			//	initialize main buttons
			JButton commitButton = new JButton("Edit");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
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
			JScrollPane displayBox = new JScrollPane(this.display);
			this.getContentPane().add(displayBox, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(500, 300));
			this.setLocationRelativeTo(null);
		}
		
		boolean isChanged() {
			return (this.dirty && this.isCommitted);
		}
		
		/** @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		public void changedUpdate(DocumentEvent de) {}
		
		/** @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		public void insertUpdate(DocumentEvent de) {
			try {
				System.out.println("JTextDoc: '" + this.display.getText(de.getOffset(), de.getLength()) + "' (length " + de.getLength() + ") inserted at " + de.getOffset());
			} catch (Exception e) {}
			this.dirty = true;
		}
		
		/** @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		public void removeUpdate(DocumentEvent de) {
			try {
				System.out.println("Doc: " + de.getLength() + " chars removed at " + de.getOffset());
			} catch (Exception e) {}
			this.dirty = true;
		}
		
		private void abort() {
			this.dispose();
		}
		
		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
		
		private ArrayList redoEvents = new ArrayList();
		void writeChanges() {
			System.out.println("AEP.SelectionEditor: content on closing is '" + this.content + "'");
			
			//	replace tokens
			if (this.redoEvents == null)
				this.originalContent.setChars(this.content, 0, this.originalContent.length());
				
			//	transform tokens
			else {
				
				//	reorganize document updates
				int redoEventCount;
				do {
					
					//	remember original number of events
					redoEventCount = this.redoEvents.size();
					System.out.println("Start reorganization round with " + redoEventCount + " redo events");
					
					//	annihilate insert against instant remove
					for (int r = 1; r < this.redoEvents.size(); r++) {
						RedoCharSequenceEvent re1 = ((RedoCharSequenceEvent) this.redoEvents.get(r-1));
						RedoCharSequenceEvent re2 = ((RedoCharSequenceEvent) this.redoEvents.get(r));
						if (re1.isInsert() && re2.isRemove() && (re1.offset == re2.offset) && (re1.inserted.length() == re2.removed.length())) {
							this.redoEvents.remove(r--);
							this.redoEvents.remove(r);
						}
					}
					System.out.println(" - " + this.redoEvents.size() + " redo events left after insert/delete annihilation");
					
					//	aggregate consecutive insertions (will mostly stem from typing)
					for (int r = 1; r < this.redoEvents.size(); r++) {
						RedoCharSequenceEvent re1 = ((RedoCharSequenceEvent) this.redoEvents.get(r-1));
						RedoCharSequenceEvent re2 = ((RedoCharSequenceEvent) this.redoEvents.get(r));
						if (re1.isInsert() && re2.isInsert() && (re1.offset <= re2.offset) && ((re1.offset + re1.inserted.length()) >= re2.offset)) {
							re1.inserted = (re1.inserted.substring(0, (re2.offset-re1.offset)) + re2.inserted + re1.inserted.substring(re2.offset-re1.offset));
							this.redoEvents.remove(r--);
						}
					}
					System.out.println(" - " + this.redoEvents.size() + " redo events left after insert aggregation");
					
					//	compensate insertions against deletions
					for (int r = 1; r < this.redoEvents.size(); r++) {
						RedoCharSequenceEvent re1 = ((RedoCharSequenceEvent) this.redoEvents.get(r-1));
						RedoCharSequenceEvent re2 = ((RedoCharSequenceEvent) this.redoEvents.get(r));
						if (re1.isInsert() && re2.isRemove() && (re1.offset <= re2.offset) && ((re1.offset + re1.inserted.length()) >= (re2.offset + re2.removed.length()))) {
							re1.inserted = (re1.inserted.substring(0, (re2.offset-re1.offset)) + re1.inserted.substring(re2.offset-re1.offset + re2.removed.length()));
							this.redoEvents.remove(r--);
							continue;
						}
					}
					System.out.println(" - " + this.redoEvents.size() + " redo events left after insert/delete compensation");
				}
				
				//	stop only after nothing changes or can change any more
				while ((this.redoEvents.size() > 1) && (this.redoEvents.size() < redoEventCount));
				
//				
//				//	mark end
//				this.originalContent.addChars(endMarker);
				
				//	redo recorded modifications
				for (int r = 0; r < this.redoEvents.size(); r++)
					((RedoCharSequenceEvent) this.redoEvents.get(r)).redoOn(this.originalContent);
//				
//				//	remove end marker
//				this.originalContent.removeChars(this.originalContent.length()-endMarker.length(), endMarker.length());
			}
		}
		
//		private final String endMarker = ""; // TODO_ne remove this hack
		
		private class RedoCharSequenceEvent {
			int offset;
//			boolean isAppend;
			String inserted;
			String removed;
			RedoCharSequenceEvent(CharSequenceEvent change, boolean isAppend) {
				this.offset = change.offset;
//				this.isAppend = isAppend;
				this.inserted = change.inserted.toString();
				this.removed = change.removed.toString();
				System.out.println("CharSequence: '" + change.inserted + "' inserted at " + change.offset + ", removing '" + change.removed + "'");
			}
			boolean isInsert() {
				return ((this.inserted.length() != 0) && (this.removed.length() == 0));
			}
			boolean isRemove() {
				return ((this.inserted.length() == 0) && (this.removed.length() != 0));
			}
			void redoOn(MutableCharSequence chars) {
				System.out.print("Writing change: ");
				if ((this.inserted.length() == 0) && (this.removed.length() == 0)) {
					System.out.println("VOID");
					return;
				}
				else if (this.inserted.length() == 0) {
					System.out.println("'" + this.removed + "' removed at " + this.offset);
					chars.removeChars(this.offset, this.removed.length());
				}
				else if (this.removed.length() == 0) {
					System.out.println("'" + this.inserted + "' inserted at " + this.offset);
					chars.insertChars(this.inserted, this.offset);
				}
				else {
					System.out.println("'" + this.inserted + "' inserted at " + this.offset + ", replacing '" + this.removed + "'");
					chars.setChars(this.inserted, this.offset, this.removed.length());
				}
			}
		}
	}
	
//	/**
//	 * dialog for editing small portions of text (higher performance than editing in window itself)
//	 * 
//	 * @author sautter
//	 */
//	private class SelectionEditor extends DialogPanel implements DocumentListener {
//		
//		private MutableAnnotation content;
//		private MutableAnnotation originalContent;
//		
//		private JTextArea display = new JTextArea();
//		
//		private boolean dirty = false;
//		private boolean isCommitted = false;
//		
//		SelectionEditor(MutableAnnotation content) {
//			super("Edit Selection", true);
//			this.originalContent = content;
//			this.content = Gamta.copyDocument(content);
//			this.content.addCharSequenceListener(new AtomRecorder());
//			
//			this.display.setLineWrap(true);
//			this.display.setWrapStyleWord(true);
//			this.display.setEditable(true);
//			this.display.setFont(new Font(parent.getTextFontName(), Font.PLAIN, parent.getTextFontSize()));
//			DocumentSynchronizer.synchronizeDocuments(this.content, this.display);
////			new DocumentSynchronizer(this.content, this.display);
//			this.display.getDocument().addDocumentListener(this);
//			
//			//	initialize main buttons
//			JButton commitButton = new JButton("Edit");
//			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			commitButton.setPreferredSize(new Dimension(100, 21));
//			commitButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					commit();
//				}
//			});
//			
//			JButton abortButton = new JButton("Cancel");
//			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			abortButton.setPreferredSize(new Dimension(100, 21));
//			abortButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					abort();
//				}
//			});
//			
//			JPanel mainButtonPanel = new JPanel();
//			mainButtonPanel.setLayout(new FlowLayout());
//			mainButtonPanel.add(commitButton);
//			mainButtonPanel.add(abortButton);
//			
//			//	put the whole stuff together
//			this.getContentPane().setLayout(new BorderLayout());
//			JScrollPane displayBox = new JScrollPane(this.display);
//			this.getContentPane().add(displayBox, BorderLayout.CENTER);
//			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
//			
//			this.setResizable(true);
//			this.setSize(new Dimension(500, 300));
//			this.setLocationRelativeTo(AnnotationEditorPanel.this);
//		}
//		
//		boolean isChanged() {
//			return (this.dirty && this.isCommitted);
//		}
//		
//		/** @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
//		 */
//		public void changedUpdate(DocumentEvent de) {}
//		
//		/** @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
//		 */
//		public void insertUpdate(DocumentEvent de) {
//			this.dirty = true;
//		}
//		
//		/** @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
//		 */
//		public void removeUpdate(DocumentEvent de) {
//			this.dirty = true;
//		}
//		
//		private void abort() {
//			this.dispose();
//		}
//		
//		private void commit() {
//			this.isCommitted = true;
//			this.dispose();
//		}
//		
//		private ArrayList writeThroughAtoms = new ArrayList();
//		private boolean wasCleared = false;
//		void writeChanges() {
//			System.out.println("AEP.SelectionEditor: content on closing is '" + this.content + "'");
//			
//			//	replace tokens
//			if (this.wasCleared)
//				this.originalContent.setChars(this.content, 0, this.originalContent.length());
//				
//			//	transform tokens
//			else {
//				
//				//	redo recorded modifications
//				for (int r = 0; r < this.writeThroughAtoms.size(); r++) {
//					Runnable mod = ((Runnable) this.writeThroughAtoms.get(r));
//					if (mod != null) mod.run();
//				}
//			}
//		}
//		
//		private class AtomRecorder implements CharSequenceListener {
//			public void charSequenceChanged(CharSequenceEvent change) {
//				final int offset = change.offset;
//				final CharSequence inserted = change.inserted;
//				final CharSequence removed = change.removed;
//				if (content.size() == 0)
//					wasCleared = true;
//				else writeThroughAtoms.add(new Runnable() {
//					public void run() {
//						if (inserted.length() == 0)
//							originalContent.removeChars(offset, removed.length());
//						else if (removed.length() == 0)
//							originalContent.insertChars(inserted, offset);
//						else originalContent.setChars(inserted, offset, removed.length());
//					}
//				});
//			}
//		}
//	}
	
	private FindReplaceDialog findReplace = null;
	private class FindReplaceDialog extends DialogPanel {
		
		private JTextField searchString = new JTextField();
		private JTextField replaceString = new JTextField();
		
		private JCheckBox caseSensitive = new JCheckBox("Case Sensitive", true);
		private JRadioButton backward = new JRadioButton("Back", false);
		private JRadioButton forward = new JRadioButton("Forward", true);
		
		private JButton replaceFindButton;
		
		private JLabel statusLabel = new JLabel("", JLabel.LEFT);
		
		/**	Constructor
		 * @param	owner		the Frame this FindReplaceDialog belongs to
		 * @param	display		the JTextComponent to do Find/Replace on
		 */
		public FindReplaceDialog() {
			super("Find / Replace", false);
			
			//	make sure selection is put into search String each time dialog is focussed
			this.addWindowFocusListener(new WindowFocusListener() {
				public void windowGainedFocus(WindowEvent we) {
					if (getSelectionForSearch()) replaceFindButton.setEnabled(true);
				}
				public void windowLostFocus(WindowEvent we) {}
			});
			
			//	initialize fields
			this.searchString.setBorder(BorderFactory.createLoweredBevelBorder());
			this.searchString.setPreferredSize(new Dimension(200, 23));
			this.replaceString.setBorder(BorderFactory.createLoweredBevelBorder());
			this.replaceString.setPreferredSize(new Dimension(200, 23));
			
			//	initialize buttons
			JButton findNextButton = new JButton("Find Next");
			findNextButton.setBorder(BorderFactory.createRaisedBevelBorder());
			findNextButton.setPreferredSize(new Dimension(100, 23));
			findNextButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (markNext()) {
						replaceFindButton.setEnabled(true);
						statusLabel.setText("");
					} else statusLabel.setText("String not found.");
				}
			});
			this.replaceFindButton = new JButton("Replace / Find");
			this.replaceFindButton.setBorder(BorderFactory.createRaisedBevelBorder());
			this.replaceFindButton.setPreferredSize(new Dimension(100, 23));
			this.replaceFindButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					replace();
					if (!markNext()) {
						statusLabel.setText("String not found.");
						replaceFindButton.setEnabled(false);
					}
				}
			});
			JButton replaceAllButton = new JButton("Replace All");
			replaceAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
			replaceAllButton.setPreferredSize(new Dimension(100, 23));
			replaceAllButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					FindReplaceDialog.this.replaceAll();
					replaceFindButton.setEnabled(false);
				}
			});
			JButton closeButton = new JButton("Close");
			closeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			closeButton.setPreferredSize(new Dimension(100, 23));
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
			
			//	initialize parameter fields
			ButtonGroup backwardForward = new ButtonGroup();
			backwardForward.add(this.backward);
			backwardForward.add(this.forward);
			JPanel backwardForwardPanel = new JPanel(new GridLayout(1, 2), true);
			backwardForwardPanel.add(this.backward);
			backwardForwardPanel.add(this.forward);
			
			//	initialize status label
			this.statusLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			this.statusLabel.setPreferredSize(new Dimension(200, 23));
			
			//	preset serach String with selection (if any)
			this.replaceFindButton.setEnabled(this.getSelectionForSearch());
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridwidth = 2;
			this.getContentPane().add(this.searchString, gbc.clone());
			
			gbc.gridy ++;
			this.getContentPane().add(this.replaceString, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridwidth = 1;
			gbc.gridx = 0;
			this.getContentPane().add(this.caseSensitive, gbc.clone());
			gbc.gridx = 1;
			this.getContentPane().add(backwardForwardPanel, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridx = 0;
			this.getContentPane().add(findNextButton, gbc.clone());
			gbc.gridx = 1;
			this.getContentPane().add(this.replaceFindButton, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridx = 0;
			this.getContentPane().add(replaceAllButton, gbc.clone());
			gbc.gridx = 1;
			this.getContentPane().add(closeButton, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridwidth = 2;
			gbc.gridx = 0;
			this.getContentPane().add(this.statusLabel, gbc.clone());
			
			this.setSize(250, 190);
			this.setResizable(false);
		}
		
		private boolean getSelectionForSearch() {
			String selected = getSelectedText();
			if (selected != null) {
				this.searchString.setText(selected);
				return true;
			} else return false;
		}
		
		private boolean markNext() {
			if (this.forward.isSelected()) return findNext(this.searchString.getText(), this.caseSensitive.isSelected());
			else if (this.backward.isSelected()) return findPrevious(this.searchString.getText(), this.caseSensitive.isSelected());
			else return false;
		}
		
		private boolean replace() {
			return replaceSelection(this.replaceString.getText(), this.caseSensitive.isSelected());
		}
		
		private void replaceAll() {
			int count = AnnotationEditorPanel.this.replaceAll(this.searchString.getText(), this.replaceString.getText(), this.caseSensitive.isSelected());
			statusLabel.setText(count + " occurrences replaced.");
		}
	}
	
	private static final boolean DEBUG_FIND_REPLACE = false;
	
	void doFindReplace() {
		if (this.findReplace == null) {
			this.findReplace = new FindReplaceDialog();
		}
		this.findReplace.setLocationRelativeTo(this);
		this.findReplace.setVisible(true);
	}
	
	//	returns the selected TEXT, removing any tags that might be inserted in it
	private String getSelectedText() {
		Annotation selection = this.annotateSelection(null, false);
		
		//	no text selected
		if (selection == null) return null;
		
		//	only (a part of) one token selected, no tag intermix possible
		else if (selection.size() == 1) return this.displayPanel.getSelectedText();
		
		//	text from multiple tokens selected, extract text from underlying cah sequence
		else {
			//	compute offset of selection in first selected token, correcting cases where the selection starts in a tag
			int startCut = Math.max(0, (this.displayPanel.getSelectionStart() - this.tokenSpans[selection.getStartIndex()].getStartOffset()));
			
			//	compute the offset of the chars to replace in the underlying cahr sequence to replace
			int startOffset = this.tokenSpans[selection.getStartIndex()].token.getStartOffset() + startCut;
			
			//	compute end of selection in last selected token, correcting cases where the selection ends in tags
			int endCut = Math.max(0, (this.tokenSpans[selection.getEndIndex() - 1].getEndOffset() - this.displayPanel.getSelectionEnd()));
			
			//	compute the end offset of the chars to replace in the underlying cahr sequence to replace
			int endOffset = this.tokenSpans[selection.getEndIndex() - 1].token.getEndOffset() - endCut;
			
			//	return selected text
			return this.content.subSequence(startOffset, endOffset).toString();
		}
	}
	
	private boolean replaceSelection(CharSequence replacement, boolean caseSensitive) {
		Annotation selection = this.annotateSelection(null, false);
		if (selection != null) {
			this.parent.enqueueRestoreContentUndoAction("Replace", selection.getStartIndex(), selection.size());
			String oldValue = selection.getValue();
			
			//	compute offset of selection in first selected token, correcting cases where the selection starts in a tag
			int startCut = Math.max(0, (this.displayPanel.getSelectionStart() - this.tokenSpans[selection.getStartIndex()].getStartOffset()));
			
			//	compute the offset of the chars to replace in the underlying cahr sequence to replace
			int startOffset = this.tokenSpans[selection.getStartIndex()].token.getStartOffset() + startCut;
			
			//	compute end of selection in last selected token, correcting cases where the selection ends in tags
			int endCut = Math.max(0, (this.tokenSpans[selection.getEndIndex() - 1].getEndOffset() - this.displayPanel.getSelectionEnd()));
			
			//	compute the end offset of the chars to replace in the underlying cahr sequence to replace
			int endOffset = this.tokenSpans[selection.getEndIndex() - 1].token.getEndOffset() - endCut;
			
			//	compute the length of the chars to replace in the underlying cahr sequence to replace
			int length = (endOffset - startOffset);
			
			//	do replacement with auxiliary Annotation so it does not hamper the markup
			MutableAnnotation temp = this.content.addAnnotation(selection);
			temp.setChars(replacement, startCut, length);
			String newValue = temp.getValue();
			this.content.removeAnnotation(temp);
			
			this.parent.writeLog("Replace: '" + oldValue + "' changed to '" + newValue + "'");
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
			return true;
		} else return false;
	}
	
	private int replaceAll(boolean caseSensitive) {
		String selected = this.getSelectedText();
		if (selected == null) {
			selected = JOptionPane.showInputDialog(parent, "Enter the text to replace.", "Enter Search String", JOptionPane.QUESTION_MESSAGE);
			if (selected == null) return 0;
		}
		Object replacement = JOptionPane.showInputDialog(parent, "Enter the text to replace the search string with.", "Enter Replacement", JOptionPane.QUESTION_MESSAGE, null, null, selected);
		if ((replacement != null) && (replacement instanceof String)) {
			return this.replaceAll(content.getTokenizer().tokenize(selected), content.getTokenizer().tokenize(replacement.toString()), caseSensitive);
		}
		return 0;
	}
	
	private int replaceAll(CharSequence searchChars, CharSequence replacement, boolean caseSensitive) {
		
		//	check parameters
		if (searchChars.length() == 0) return 0;
		if (tokenSpans == null) return 0;
		if (tokenSpans.length == 0) return 0;
		
		//	do replacement
		this.parent.enqueueRestoreContentUndoAction("Replace All");
		int replaceCount = 0;
		int replaceStart = 0;
		int replaceOffset = CharSequenceUtils.offsetOf(this.content, searchChars, replaceStart, caseSensitive);
		while (replaceOffset != -1) {
			replaceStart = replaceOffset + replacement.length();
			this.content.setChars(replacement, replaceOffset, searchChars.length());
			replaceCount++;
			replaceOffset = CharSequenceUtils.offsetOf(this.content, searchChars, replaceStart, caseSensitive);
		}
		
		//	propagate update
		this.parent.storeUndoAction();
		this.parent.notifyDocumentMarkupModified();
		this.parent.notifyDocumentTextModified();
		this.refreshDisplay();
		return replaceCount;
	}
	
	private boolean findPrevious(boolean caseSensitive) {
		if (this.content.size() == 0) return false;
		String selected = this.getSelectedText();
		
		//	find display position to start
		int startPosition;
		if (selected == null) {
			selected = JOptionPane.showInputDialog(parent, "Enter the text to find.", "Enter Search String", JOptionPane.QUESTION_MESSAGE);
			if (selected == null) return false;
			startPosition = this.displayPanel.getCaretPosition();
		} else startPosition = Math.min(this.displayPanel.getSelectionStart(), this.displayPanel.getSelectionEnd());
		
		//	mark last occurrence of search string before start position
		return this.markPrevious(selected, (startPosition - 1), caseSensitive);
	}
	
	private boolean findPrevious(CharSequence search, boolean caseSensitive) {
		return this.markPrevious(search, (this.displayPanel.getSelectionStart() - 1), caseSensitive);
	}
	
	private boolean markPrevious(CharSequence search, int startPosition, boolean caseSensitive) {
		if (search.length() == 0) return false;
		if (tokenSpans == null) return false;
		if (tokenSpans.length == 0) return false;
		
		//	find index of first token to search 
		int startTokenIndex;
		
		//	start before first token
		if (startPosition < this.tokenSpans[0].getStartOffset()) return false;
		
		//	start after last token
		else if (startPosition >= this.tokenSpans[this.tokenSpans.length - 1].getEndOffset()) {
			startTokenIndex = (this.tokenSpans.length - 1);
			startPosition = this.tokenSpans[startTokenIndex].getEndOffset();
			
		//	start somewhere in text
		} else {
			
			//	use binary search to narrow interval of search start token
			int left = 0;
			int right = this.tokenSpans.length;
			while ((right - left) > 2) {
				startTokenIndex = ((left + right) / 2);
				if (this.tokenSpans[startTokenIndex].getEndOffset() <= startPosition) left = startTokenIndex;
				else if (this.tokenSpans[startTokenIndex].getStartOffset() > startPosition) right = startTokenIndex;
				else break;
			}
			
			//	scan remaining interval
			startTokenIndex = left;
			while (startTokenIndex < tokenSpans.length) {
				if (this.tokenSpans[startTokenIndex].getEndOffset() <= startPosition) startTokenIndex++;
				else break;
			}
		}
		
		//	compute offset of selection in first selected token, correcting cases where the selection starts in a tag
		int startCut = (startPosition - this.tokenSpans[startTokenIndex].getStartOffset());
		
		//	negative start cut indicates start position in whitespace, try one position further up the document
		if (startCut < 0) return this.markPrevious(search, (startPosition - 1), caseSensitive);
		
		//	compute the offset of the char to start at in the underlying char sequence
		int startOffset = this.tokenSpans[startTokenIndex].token.getStartOffset() + startCut;
		
		//	find offset last occurrence of search string before start offset
		int foundOffset = CharSequenceUtils.lastOffsetOf(this.content, search, startOffset, caseSensitive);
		
		//	no occurence before start offset
		if (foundOffset == -1) return false;
		
		//	compute token index of first token in occurence and shift in token
		int foundStartTokenIndex = TokenSequenceUtils.getTokenIndexAtOffset(this.content, foundOffset);
		int foundStartCut = Math.max(0, (foundOffset - this.content.tokenAt(foundStartTokenIndex).getStartOffset()));
		
		//	compute token index of last token in occurence and shift in token
		int foundEndTokenIndex = foundStartTokenIndex;
		while ((foundEndTokenIndex < this.content.size()) && (this.content.tokenAt(foundEndTokenIndex).getEndOffset() < (foundOffset + search.length())))
			foundEndTokenIndex ++;
		int foundEndCut = ((foundEndTokenIndex == this.tokenSpans.length) ? 0 : (this.content.tokenAt(foundEndTokenIndex).getEndOffset() - (foundOffset + search.length())));
		
		//	compute start and end position of occurence in display
		int mark = this.tokenSpans[foundStartTokenIndex].getStartOffset() + foundStartCut;
		int dot = ((foundEndTokenIndex == this.tokenSpans.length) ? this.tokenSpans[this.tokenSpans.length - 1].getEndOffset() : (this.tokenSpans[foundEndTokenIndex].getEndOffset() - foundEndCut));
		
		//	mark occurence
		this.displayPanel.scrollToPosition(mark, false, false);
		this.displayPanel.setSelectionStart(mark);
		this.displayPanel.setSelectionEnd(dot);
		this.displayPanel.display.getCaret().setSelectionVisible(true);
		
		//	report success
		if (DEBUG_FIND_REPLACE) System.out.println(" - Selected " + this.displayPanel.getSelectedText());
		return true;
	}
	
	private boolean findNext(boolean caseSensitive) {
		if (this.content.size() == 0) return false;
		String selected = this.getSelectedText();
		
		//	find display position to start
		int startPosition;
		if (selected == null) {
			selected = JOptionPane.showInputDialog(parent, "Enter the text to find.", "Enter Search String", JOptionPane.QUESTION_MESSAGE);
			if (selected == null) return false;
			startPosition = this.displayPanel.getCaretPosition();
		} else startPosition = Math.min(this.displayPanel.getSelectionStart(), this.displayPanel.getSelectionEnd());
		
		//	mark next occurrence of search string after start position
		return this.markNext(selected, (startPosition + 1), caseSensitive);
	}
	
	private boolean findNext(CharSequence search, boolean caseSensitive) {
		return this.markNext(search, (this.displayPanel.getSelectionStart() + 1), caseSensitive);
	}
	
	private boolean markNext(CharSequence search, int startPosition, boolean caseSensitive) {
		if (search.length() == 0) return false;
		if (tokenSpans == null) return false;
		if (tokenSpans.length == 0) return false;
		
		//	find index of first token to search 
		int startTokenIndex;
		
		//	start before first token
		if (startPosition < this.tokenSpans[0].getStartOffset()) {
			startTokenIndex = 0;
			startPosition = this.tokenSpans[0].getStartOffset();
			
		//	start after last token
		} else if (startPosition >= this.tokenSpans[this.tokenSpans.length - 1].getEndOffset()) return false;
			
		//	start somewhere in text
		else {
			
			//	use binary search to narrow interval of search start token
			int left = 0;
			int right = this.tokenSpans.length;
			while ((right - left) > 2) {
				startTokenIndex = ((left + right) / 2);
				if (this.tokenSpans[startTokenIndex].getEndOffset() <= startPosition) left = startTokenIndex;
				else if (this.tokenSpans[startTokenIndex].getStartOffset() > startPosition) right = startTokenIndex;
				else break;
			}
			
			//	scan remaining interval
			startTokenIndex = left;
			while (startTokenIndex < tokenSpans.length) {
				if (this.tokenSpans[startTokenIndex].getEndOffset() <= startPosition) startTokenIndex++;
				else break;
			}
		}
		
		//	compute offset of selection in first selected token, correcting cases where the selection starts in a tag
//		int startCut = Math.max(0, (startPosition - this.tokenSpans[startTokenIndex].getStartOffset()));
		int startCut = (startPosition - this.tokenSpans[startTokenIndex].getStartOffset());
		
		//	negative start cut indicates start position in whitespace, try one position further down the document
		if (startCut < 0) return this.markNext(search, (startPosition + 1), caseSensitive);
		
		//	compute the offset of the char to start at in the underlying char sequence
		int startOffset = this.tokenSpans[startTokenIndex].token.getStartOffset() + startCut;
		
		//	find offset last occurrence of search string before start offset
		int foundOffset = CharSequenceUtils.offsetOf(this.content, search, startOffset, caseSensitive);
		
		//	no occurence after start offset
		if (foundOffset == -1) return false;
		
		//	compute token index of first token in occurence and shift in token
		int foundStartTokenIndex = TokenSequenceUtils.getTokenIndexAtOffset(this.content, foundOffset);
		int foundStartCut = Math.max(0, (foundOffset - this.content.tokenAt(foundStartTokenIndex).getStartOffset()));
		
		//	compute token index of last token in occurence and shift in token
		int foundEndTokenIndex = foundStartTokenIndex;
		while ((foundEndTokenIndex < this.content.size()) && (this.content.tokenAt(foundEndTokenIndex).getEndOffset() < (foundOffset + search.length())))
			foundEndTokenIndex ++;
		int foundEndCut = ((foundEndTokenIndex == this.tokenSpans.length) ? 0 : (this.content.tokenAt(foundEndTokenIndex).getEndOffset() - (foundOffset + search.length())));
		
		//	compute start and end position of occurence in display
		int mark = this.tokenSpans[foundStartTokenIndex].getStartOffset() + foundStartCut;
		int dot = ((foundEndTokenIndex == this.tokenSpans.length) ? this.tokenSpans[this.tokenSpans.length - 1].getEndOffset() : (this.tokenSpans[foundEndTokenIndex].getEndOffset() - foundEndCut));
		
		//	mark occurence
		this.displayPanel.scrollToPosition(mark, false, false);
		this.displayPanel.setSelectionStart(mark);
		this.displayPanel.setSelectionEnd(dot);
		this.displayPanel.display.getCaret().setSelectionVisible(true);
		
		//	report success
		if (DEBUG_FIND_REPLACE) System.out.println(" - Selected " + this.displayPanel.getSelectedText());
		return true;
	}
	
	AnnotationFilter[] getFilters(boolean includeAll) {
		ArrayList filters = new ArrayList();
		if (includeAll)
			filters.add(acceptAllFilter);
		if (showAdvancedFilters)
			filters.add(new GPathAnnotationFilter());
		
		String[] annotationTypes = this.content.getAnnotationTypes();
		Arrays.sort(annotationTypes, ANNOTATION_TYPE_ORDER);
		for (int t = 0; t < annotationTypes.length; t++)
			filters.add(new AnnotationTypeFilter(annotationTypes[t]));
		
		AnnotationFilterManager[] afm = this.host.getAnnotationFilterProviders();
		for (int m = 0; m < afm.length; m++) {
			String[] filterNames = afm[m].getResourceNames();
			Arrays.sort(filterNames, String.CASE_INSENSITIVE_ORDER);
			for (int n = 0; n < filterNames.length; n++) {
				AnnotationFilter filter = afm[m].getAnnotationFilter(filterNames[n]);
				if (filter != null) filters.add(new AnnotationFilterWrapper(filter));
			}
		}
		
		return ((AnnotationFilter[]) filters.toArray(new AnnotationFilter[filters.size()]));
	}
	
	private static final String acceptAllFilterName = "<All Annotations>";
	private static final AnnotationFilter acceptAllFilter = new AnnotationFilter() {
		public boolean accept(Annotation annotation) {
			return true;
		}
		public QueriableAnnotation[] getMatches(QueriableAnnotation data) {
			return data.getAnnotations();
		}
		public MutableAnnotation[] getMutableMatches(MutableAnnotation data) {
			return data.getMutableAnnotations();
		}
		public String getName() {
			return acceptAllFilterName;
		}
		public String getProviderClassName() {
			return null;
		}
		public String getTypeLabel() {
			return "AnnotationFilter";
		}
		public boolean equals(Object obj) {
			return ((obj != null) && this.toString().equals(obj.toString()));
		}
		public String toString() {
			return "<All Annotations>";
		}
	};
	
	private class AnnotationTypeFilter implements AnnotationFilter {
		private String type;
		AnnotationTypeFilter(String type) {
			this.type = type;
		}
		public boolean accept(Annotation annotation) {
			return ((this.type == null) || this.type.equals(annotation.getType()));
		}
		public QueriableAnnotation[] getMatches(QueriableAnnotation data) {
			return data.getAnnotations(this.type);
		}
		public MutableAnnotation[] getMutableMatches(MutableAnnotation data) {
			return data.getMutableAnnotations(this.type);
		}
		public String getName() {
			return ((this.type == null) ? acceptAllFilterName : this.type);
		}
		public String getProviderClassName() {
			return null;
		}
		public String getTypeLabel() {
			return "AnnotationFilter";
		}
		public boolean equals(Object obj) {
			return ((obj != null) && this.toString().equals(obj.toString()));
		}
		public String toString() {
			return ((this.type == null) ? "<All Annotations>" : this.type);
		}
	}
	
	private class AnnotationFilterWrapper implements AnnotationFilter {
		private AnnotationFilter filter;
		AnnotationFilterWrapper(AnnotationFilter filter) {
			this.filter = filter;
		}
		public boolean accept(Annotation annotation) {
			return this.filter.accept(annotation);
		}
		public QueriableAnnotation[] getMatches(QueriableAnnotation data) {
			return this.filter.getMatches(data);
		}
		public MutableAnnotation[] getMutableMatches(MutableAnnotation data) {
			return this.filter.getMutableMatches(data);
		}
		public String getName() {
			return this.filter.getName();
		}
		public String getProviderClassName() {
			return this.filter.getProviderClassName();
		}
		public String getTypeLabel() {
			return this.filter.getTypeLabel();
		}
		public boolean equals(Object obj) {
			return ((obj != null) && this.toString().equals(obj.toString()));
		}
		public String toString() {
			return (this.filter.getTypeLabel() + ": " + this.filter.getName());
		}
	}
	
	private class GPathAnnotationFilter implements AnnotationFilter {
		private String path = null;
		private GPath gPath = null;
		GPathAnnotationFilter() {}
		GPathAnnotationFilter(String path) {
			try {
				this.gPath = new GPath(path);
				this.path = path;
			} catch (Exception e) {}
		}
		public boolean accept(Annotation annotation) {
			return false;
		}
		public QueriableAnnotation[] getMatches(QueriableAnnotation data) {
			if (this.gPath == null) {
				Object path = "";
				while (path != null) {
					path = JOptionPane.showInputDialog(AnnotationEditorPanel.this, "Please enter the GPath expression to use for filtering.", "Enter GPath Expression", JOptionPane.PLAIN_MESSAGE, null, null, path);
					if (path != null) try {
						this.gPath = new GPath(path.toString());
						this.path = path.toString();
						path = null;
					} catch (Exception e) {
						if (JOptionPane.showConfirmDialog(AnnotationEditorPanel.this, "The specified GPath expression is invalid:\n" + e.getMessage() + "\nCorrect it?", "Invlaid GPath Expression", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)
							path = null;
					}
				}
			}
			if (this.gPath == null) return new QueriableAnnotation[0];
			else try {
				return this.gPath.evaluate(data, GPath.getDummyVariableResolver());
			}
			catch (GPathException gpe) {
				return new QueriableAnnotation[0];
			}
		}
		public MutableAnnotation[] getMutableMatches(MutableAnnotation data) {
			QueriableAnnotation[] matches = this.getMatches(data);
			if (matches.length == 0) return new MutableAnnotation[0];
			
			Set matchIDs = new HashSet();
			for (int m = 0; m < matches.length; m++)
				matchIDs.add(matches[m].getAnnotationID());
			MutableAnnotation[] mutableAnnotations = data.getMutableAnnotations();
			ArrayList mutableMatches = new ArrayList();
			for (int m = 0; m < mutableAnnotations.length; m++)
				if (matchIDs.contains(mutableAnnotations[m].getAnnotationID()))
					mutableMatches.add(mutableAnnotations[m]);
			return ((MutableAnnotation[]) mutableMatches.toArray(new MutableAnnotation[mutableMatches.size()]));
		}
		public String getName() {
			if (this.path == null) return "<Custom GPath Filter>";
			else return this.path;
		}
		public String getProviderClassName() {
			return "AdHocGPath";
		}
		public String getTypeLabel() {
			return "AnnotationFilter";
		}
		public boolean equals(Object obj) {
			return ((obj != null) && this.toString().equals(obj.toString()));
		}
		public String toString() {
			if (this.path == null) return "<Custom GPath Filter>";
			else return this.path;
		}
	}
	
	private void renameAnnotation(String id) {
		if (id != null) {
			Annotation annotation = this.getAnnotationByID(id);
			if (annotation != null) {
				
				RenameAnnotationDialog rad = new RenameAnnotationDialog(annotation.getType());
				rad.setVisible(true);
				
				if (rad.renameAll()) {
					String newType = rad.getTargetType();
					if (newType.trim().length() != 0)
						this.renameAnnotations(new AnnotationTypeFilter(annotation.getType()), newType);
					
				} else if (rad.typeChanged()) {
					String newType = rad.getTargetType();
					if (newType.trim().length() != 0) {
						
						this.parent.enqueueRestoreMarkupUndoAction(null, "Rename Annotation", annotation.getStartIndex(), annotation.size());
						this.parent.writeLog("Rename Annotation: '" + annotation.getType() + "' annotation renamed to '" + newType + "'");
						
						boolean needRefresh = false;
						if (this.isHighlighted(annotation.getType()))
							this.showHighlight(newType, false);
						if (this.isShowingTags(annotation.getType())) {
							needRefresh = this.showTags(newType, false);
						} else if (this.isShowingTags(newType)) needRefresh = true;
						
						annotation.changeTypeTo(newType);
						
						//	propagate modification
						this.parent.storeUndoAction();
						this.parent.notifyDocumentMarkupModified();
						if (needRefresh) this.refreshDisplay();
						else {
							this.layoutTypePanels();
							
							if (this.isShowingTags(annotation.getType()))
								this.updateAnnotationTags(annotation);
							
							AnnotationSpan changedHighlight = null;
							for (int a = 0; a < this.annotationSpans.length; a++)
								if (annotation.getAnnotationID().equals(this.annotationSpans[a].annotation.getAnnotationID()) && (this.annotationSpans[a] instanceof AnnotationHighlightSpan))
									changedHighlight = ((AnnotationHighlightSpan) this.annotationSpans[a]);
							
							if (this.isHighlighted(annotation.getType())) {
								if (changedHighlight != null) {
									changedHighlight.colorLayout = this.getHighlightLayoutForAnnotationType(annotation.getType());
									this.applySpans(changedHighlight.getStartOffset(), changedHighlight.getSize());
								}
								else this.applySpans();
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * dialog for entering the parameters of a rename operation for a single annotation
	 * 
	 * @author sautter
	 */
	private class RenameAnnotationDialog extends DialogPanel {
		
		private String sourceType;
		private JComboBox targetTypeSelector;
		
		private boolean isCommitted = false;
		private boolean renameAll = false;
		
		private boolean keyPressed = false;
		
		RenameAnnotationDialog(String sourceType) {
			super("Rename Annotation", true);
			this.sourceType = sourceType;
			
			JLabel sourceTypeLabel = new JLabel(" " + this.sourceType);
			sourceTypeLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			
			StringVector suggestions = new StringVector();
			suggestions.addContent(getAnnotationTypeSuggestions());
			suggestions.addContentIgnoreDuplicates(parent.getAnnotationTypes(true));
			String[] existingTypes = suggestions.toStringArray();
			Arrays.sort(existingTypes, ANNOTATION_TYPE_ORDER);
			
			this.targetTypeSelector = new JComboBox(existingTypes);
			this.targetTypeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.targetTypeSelector.setEditable(true);
			this.targetTypeSelector.setSelectedItem(sourceType);
			this.targetTypeSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (keyPressed && isVisible() && !targetTypeSelector.isPopupVisible()) commit();
				}
			});
			((JTextComponent) this.targetTypeSelector.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					keyPressed = true;
				}
				public void keyReleased(KeyEvent ke) {
					keyPressed = false;
				}
			});
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Current Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(sourceTypeLabel, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Rename Annotation(s) To"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.targetTypeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Rename");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton commitAllButton = new JButton("Rename All");
			commitAllButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitAllButton.setPreferredSize(new Dimension(100, 21));
			commitAllButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commitAll();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(commitAllButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 120));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		boolean typeChanged() {
			return (this.isCommitted && !this.sourceType.equals(this.getTargetType()));
		}
		
		boolean renameAll() {
			return (this.typeChanged() && this.renameAll);
		}
		
		String getTargetType() {
			Object item = this.targetTypeSelector.getSelectedItem();
			return ((item == null) ? "" : item.toString());
		}
		
		private void abort() {
			this.dispose();
		}
		
		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
		
		private void commitAll() {
			this.isCommitted = true;
			this.renameAll = true;
			this.dispose();
		}
	}
	
	private void removeAnnotation(String id) {
		if (id != null) {
			Annotation annotation = this.getAnnotationByID(id);
			if (annotation != null) {
				this.parent.enqueueRestoreMarkupUndoAction(annotation.getType(), "Remove Annotation", annotation.getStartIndex(), annotation.size());
				this.parent.writeLog("Remove Annotation: '" + annotation.getType() + "' annotation removed from '" + annotation.getValue() + "'");
				
				this.content.removeAnnotation(annotation);
				
				//	propagate modification
				this.parent.storeUndoAction();
				this.parent.notifyDocumentMarkupModified();
				if (this.isShowingTags(annotation.getType())) this.refreshDisplay();
				else {
					this.layoutTypePanels();
					ArrayList spanList = new ArrayList();
					AnnotationSpan removedHighlight = null;
					for (int a = 0; a < this.annotationSpans.length; a++)
						if (annotation.getAnnotationID().equals(this.annotationSpans[a].annotation.getAnnotationID())) {
							if (this.annotationSpans[a] instanceof AnnotationHighlightSpan)
								removedHighlight = ((AnnotationHighlightSpan) this.annotationSpans[a]);
							this.annotationSpansByAnnotationID.remove(this.annotationSpans[a].annotation.getAnnotationID());
						}
						else spanList.add(this.annotationSpans[a]);
//						if (!annotation.getAnnotationID().equals(this.annotationSpans[a].annotation.getAnnotationID()))
//							spanList.add(this.annotationSpans[a]);
//						else if (this.annotationSpans[a] instanceof AnnotationHighlightSpan)
//							removedHighlight = ((AnnotationHighlightSpan) this.annotationSpans[a]);
					this.annotationSpans = ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
					
					if (this.isHighlighted(annotation.getType())) {
						if (removedHighlight != null) this.applySpans(removedHighlight.getStartOffset(), removedHighlight.getSize());
						else this.applySpans();
					}
				}
			}
		}
	}
	
	private void removeAllAnnotations(String id) {
		if (id != null) {
			Annotation annotation = this.getAnnotationByID(id);
			if (annotation != null) {
				this.parent.enqueueRestoreMarkupUndoAction(annotation.getType(), "Remove Annotations");
				this.parent.writeLog("Remove Annotations: '" + annotation.getType() + "' annotation removed from all occurrences of '" + annotation.getValue() + "'");
				
				String value = annotation.getValue();
				Annotation[] annotations = this.content.getAnnotations(annotation.getType());
				HashSet removed = new HashSet();
				for (int a = 0; a < annotations.length; a++) {
					if (value.equals(annotations[a].getValue()) || (!annotateAllCaseSensitive() && value.equalsIgnoreCase(annotations[a].getValue()))) {
						removed.add(annotations[a].getAnnotationID());
						this.content.removeAnnotation(annotations[a]);
					}
				}
				
				//	propagate modification
				this.parent.storeUndoAction();
				this.parent.notifyDocumentMarkupModified();
				if (this.isShowingTags(annotation.getType())) this.refreshDisplay();
				else {
					this.layoutTypePanels();
					
					ArrayList spanList = new ArrayList();
					for (int a = 0; a < this.annotationSpans.length; a++)
						if (removed.contains(this.annotationSpans[a].annotation.getAnnotationID()))
							this.annotationSpansByAnnotationID.remove(this.annotationSpans[a].annotation.getAnnotationID());
						else spanList.add(this.annotationSpans[a]);
					this.annotationSpans = ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
					
					this.applySpans();
				}
			}
		}
	}
	
	private void extendAnnotation(MutableAnnotation annotation, Annotation selection) {
		if (annotation.getStartIndex() == selection.getEndIndex()) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Include Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Exclude Tokens: '" + selection.getValue() + "' included in '" + annotation.getType() + "' annotation");
			
			//	cut tokens
			MutableAnnotation temp = this.copyMutableAnnotation(selection);
			this.content.removeTokens(selection);
			
			//	insert tokens
			this.insertAt(temp, annotation, 0);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
			
		} else if (selection.getStartIndex() == annotation.getEndIndex()) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Include Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Exclude Tokens: '" + selection.getValue() + "' included in '" + annotation.getType() + "' annotation");
			
			//	cut tokens
			MutableAnnotation temp = this.copyMutableAnnotation(selection);
			this.content.removeTokens(selection);
			
			//	insert tokens
			this.insertAt(temp, annotation, annotation.size());
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void shrinkAnnotation(MutableAnnotation annotation, Annotation selection) {
		if (annotation.getStartIndex() == selection.getStartIndex()) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Exclude Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Exclude Tokens: '" + selection.getValue() + "' excluded from '" + annotation.getType() + "' annotation");
			
			//	cut tokens
			MutableAnnotation temp = this.copyMutableAnnotation(selection);
			this.content.removeTokens(selection);
			
			//	insert tokens
			int insertIndex = selection.getStartIndex();
			this.insertAt(temp, this.content, insertIndex);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
			
		} else if (selection.getEndIndex() == annotation.getEndIndex()) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Exclude Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Exclude Tokens: '" + selection.getValue() + "' excluded from '" + annotation.getType() + "' annotation");
			
			//	cut tokens
			MutableAnnotation temp = this.copyMutableAnnotation(selection);
			this.content.removeTokens(selection);
			
			//	insert tokens
			int insertIndex = selection.getStartIndex();
			this.insertAt(temp, this.content, insertIndex);
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void copyAnnotation(String id) {
		this.copyAnnotation(this.getAnnotationByID(id));
	}
	
	private void copyAnnotation(Annotation annotation) {
		if (annotation != null)
			clipboard = this.copyMutableAnnotation(annotation);
	}
	
	private void copyTokens() {
		this.copyTokens(this.annotateSelection(null, false));
	}
	
	private void copyTokens(String id) {
		this.copyTokens(this.getAnnotationByID(id));
	}
	
	private void copyTokens(Annotation selection) {
		if (selection != null)
			clipboard = this.content.getSubsequence(selection.getStartIndex(), selection.size());
	}
	
	private void cutAnnotation(String id) {
		Annotation annotation = this.getAnnotationByID(id);
		if (annotation != null) {
			
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Cut Annotation", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Cut Annotation: '" + annotation.getType() + "' annotation cut");
			
			//	copy tokens
			MutableAnnotation cutDoc = this.copyMutableAnnotation(annotation);
			
			//	cut tokens
			this.content.removeTokens(annotation);
			clipboard = cutDoc;
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void cutTokens() {
		this.cutTokens(this.annotateSelection(null, false));
	}
	
	private void cutTokens(String id) {
		Annotation annotation = this.getAnnotationByID(id);
		if (annotation != null) this.cutTokens(annotation);
	}
	
	private void cutTokens(Annotation selection) {
		if (selection != null) {
			
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Cut Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Cut Tokens: tokens '" + selection.getValue() + "' cut");
			
			//	cut tokens
			clipboard = this.content.removeTokens(selection);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void swapClipboard() {
		this.swapClipboard(this.annotateSelection(null, false));
	}
	
	private void swapClipboard(String id) {
		this.swapClipboard(this.getAnnotationByID(id));
	}
	
	private void swapClipboard(Annotation selection) {
		if ((selection != null) && (clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			int swapIndex = selection.getStartIndex();
			
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Swap Clipboard", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Swap Clipboard: '" + ((Annotation) clipboard).getType() + "' annotation inserted, '" + selection.getType() + "' annotation cut");
			
			//	cut tokens
			MutableAnnotation cutDoc = this.copyMutableAnnotation(selection);
			this.content.removeTokens(selection);
			
			//	insert tokens
			MutableAnnotation insertDoc = ((MutableAnnotation) clipboard);
			this.insertAt(insertDoc, this.content, swapIndex);
			
			//	swap clipboard
			Annotation[] cutDocAnnotations = cutDoc.getAnnotations();
			clipboard = (((cutDocAnnotations.length == 0) || DocumentRoot.DOCUMENT_TYPE.equals(cutDocAnnotations[0].getType()))
						? cutDoc.getSubsequence(0, cutDoc.size())
						: ((TokenSequence) cutDoc)
						);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		} else this.swapClipboardTokens(selection);
	}
	
	private void swapClipboardTokens() {
		this.swapClipboardTokens(this.annotateSelection(null, false));
	}
	
	private void swapClipboardTokens(String id) {
		this.swapClipboardTokens(this.getAnnotationByID(id));
	}
	
	private void swapClipboardTokens(Annotation selection) {
		if ((selection != null) && (clipboard != null)) {
			
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Swap Clipboard Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Swap Clipboard Tokens: " + clipboard.size() + " tokens inserted, " + selection.size() + " tokens cut");
			
			//	swap in document part
			if (selection instanceof MutableAnnotation) {
				MutableAnnotation swapPart = ((MutableAnnotation) selection);
				int cutSize = selection.size();
				swapPart.insertTokensAt(clipboard, 0);
				clipboard = swapPart.removeTokensAt(clipboard.size(), cutSize);
				
			//	swap in other selection
			} else {
				int swapIndex = selection.getStartIndex();
				this.content.insertTokensAt(clipboard, swapIndex);
				swapIndex += clipboard.size();
				clipboard = this.content.removeTokensAt(swapIndex, selection.size());
			}
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	private void pasteClipboard() {
		this.pasteClipboard(this.annotateSelection(null, false));
	}
	
	private void pasteClipboard(String id) {
		this.pasteClipboard(this.getAnnotationByID(id));
	}
	
	private void pasteClipboard(Annotation selection) {
		if ((clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			if (selection != null) {
				
				//	save content for undo
				this.parent.enqueueRestoreContentUndoAction("Paste Clipboard", selection.getStartIndex(), selection.size());
				this.parent.writeLog("Paste Clipboard: " + ((Annotation) clipboard).getType() + " annotation inserted, replacing " + selection.size() + " tokens");
				
				//	insert tokens
				int insertIndex = selection.getStartIndex();
				int selectionSize = selection.size();
				MutableAnnotation insertDoc = ((MutableAnnotation) clipboard);
				this.content.insertTokensAt(insertDoc, insertIndex);
				
				//	remove tokens
				this.content.removeTokensAt((insertIndex + insertDoc.size()), selectionSize);
				
				//	annotate
				MutableAnnotation temp = this.content.addAnnotation("temp", insertIndex, insertDoc.size());
				Annotation[] annotations = insertDoc.getAnnotations();
				int aStart = (((annotations.length == 0) || !DocumentRoot.DOCUMENT_TYPE.equals(annotations[0].getType())) ? 0 : 1); 
				for (int a = aStart; a < annotations.length; a++) {
					if (annotations[a] instanceof MutableAnnotation)
						temp.addAnnotation(annotations[a]);
					else temp.addAnnotation(annotations[a]);
				}
				
				//	remove helper
				this.content.removeAnnotation(temp);
				
				//	propagate update
				this.parent.storeUndoAction();
				this.parent.notifyDocumentMarkupModified();
				this.parent.notifyDocumentTextModified();
				this.refreshDisplay();
			} else {
				int selectStart = this.displayPanel.getSelectionStart();
				int insertIndex = ((this.content.size() == 0) ? 0 : this.getTokenIndexForPosition(selectStart - 1));
				if (insertIndex != -1) this.pasteClipboardAt(insertIndex);
			}
		} else pasteClipboardTokens(selection);
	}
	
	private void pasteClipboardAt(int insertTokenIndex) {
		if ((clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Paste Clipboard", insertTokenIndex, 0);
			this.parent.writeLog("Paste Clipboard: " + ((Annotation) clipboard).getType() + " annotation inserted");
			
			MutableAnnotation insertDoc = ((MutableAnnotation) clipboard);
			this.content.insertTokensAt(insertDoc, insertTokenIndex);
			
			//	annotate
			MutableAnnotation temp = this.content.addAnnotation(TEMP_ANNOTATION_TYPE, insertTokenIndex, insertDoc.size());
			Annotation[] annotations = insertDoc.getAnnotations();
			int aStart = (((annotations.length == 0) || !DocumentRoot.DOCUMENT_TYPE.equals(annotations[0].getType())) ? 0 : 1); 
			for (int a = aStart; a < annotations.length; a++) {
				if (annotations[a] instanceof MutableAnnotation)
					temp.addAnnotation(annotations[a]);
				else temp.addAnnotation(annotations[a]);
			}
			
			//	remove helper
			this.content.removeAnnotation(temp);
			
			//	propagate update
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		} else pasteClipboardTokensAt(insertTokenIndex);
	}
	
	private void pasteClipboardTokens() {
		this.pasteClipboardTokens(this.annotateSelection(null, false));
	}
	
	private void pasteClipboardTokens(String id) {
		this.pasteClipboardTokens(this.getAnnotationByID(id));
	}
	
	private void pasteClipboardTokens(Annotation selection) {
		if (clipboard != null) {
			if (selection != null) {
				
				//	save content for undo
				this.parent.enqueueRestoreContentUndoAction("Paste Clipboard Tokens", selection.getStartIndex(), selection.size());
				this.parent.writeLog("Paste Clipboard Tokens: " + clipboard.size() + " tokens inserted, replacing " + selection.size() + " tokens");
				
				//	do paste
				int pasteIndex = selection.getStartIndex();
				int selectionSize = selection.size();
				this.content.insertTokensAt(clipboard, pasteIndex);
				this.content.removeTokensAt((pasteIndex + clipboard.size()), selectionSize);
				
				//	propagate update
				this.parent.storeUndoAction();
				this.parent.notifyDocumentMarkupModified();
				this.parent.notifyDocumentTextModified();
				this.refreshDisplay();
			} else {
				int selectStart = this.displayPanel.getSelectionStart();
				int insertIndex = ((this.content.size() == 0) ? 0 : this.getTokenIndexForPosition(selectStart - 1));
				if (insertIndex != -1) this.pasteClipboardTokensAt(insertIndex);
			}
		}
	}
	
	private void pasteClipboardTokensAt(int insertTokenIndex) {
		
		//	create undo action
		this.parent.enqueueRestoreContentUndoAction("Paste Clipboard Tokens", insertTokenIndex, 0);
		this.parent.writeLog("Paste Clipboard Tokens: " + clipboard.size() + " tokens inserted");
		
		//	paste tokens
		this.content.insertTokensAt(clipboard, insertTokenIndex);
		
		//	propagate update
		this.parent.storeUndoAction();
		this.parent.notifyDocumentMarkupModified();
		this.parent.notifyDocumentTextModified();
		this.refreshDisplay();
	}
	
	private void insertBefore() {
		this.insertBefore(this.annotateSelection(null, false));
	}
	
	private void insertBefore(String id) {
		this.insertBefore(this.getAnnotationByID(id));
	}
	
	private void insertBefore(Annotation selection) {
		if ((selection != null) && (clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert Before", selection.getStartIndex(), 0);
			this.parent.writeLog("Insert Before: '" + ((Annotation) clipboard).getType() + "' annotation inserted");
			
			//	insert tokens
			int insertIndex = selection.getStartIndex();
			this.insertAt(((MutableAnnotation) clipboard), this.content, insertIndex);
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void insertTokensBefore() {
		this.insertTokensBefore(this.annotateSelection(null, false));
	}
	
	private void insertTokensBefore(String id) {
		this.insertTokensBefore(this.getAnnotationByID(id));
	}
	
	private void insertTokensBefore(Annotation selection) {
		if ((selection != null) && (clipboard != null)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert Tokens Before", selection.getStartIndex(), 0);
			this.parent.writeLog("Insert Tokens Before: " + clipboard.size() + " tokens inserted");
			
			//	insert tokens
			final int insertIndex = selection.getStartIndex();
			this.content.insertTokensAt(clipboard, insertIndex);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void insertTo(String id) {
		MutableAnnotation annotation = this.getAnnotationByID(id);
		if ((annotation != null) && (clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Insert: '" + ((Annotation) clipboard).getType() + "' annotation inserted to '" + annotation.getType() + "' annotation");
			
			//	insert tokens
			this.insertAt(((MutableAnnotation) clipboard), annotation, 0);
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void insertTokensTo(String id) {
		MutableAnnotation annotation = this.getAnnotationByID(id);
		if ((annotation != null) && (annotation instanceof MutableAnnotation) && (clipboard != null)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert Tokens", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Insert Tokens: " + clipboard.size() + " tokens inserted to '" + annotation.getType() + "' annotation");
			
			//	insert tokens
			annotation.insertTokensAt(clipboard, 0);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void insertAfter() {
		this.insertAfter(this.annotateSelection(null, false));
	}
	
	private void insertAfter(String id) {
		this.insertAfter(this.getAnnotationByID(id));
	}
	
	private void insertAfter(Annotation selection) {
		if ((selection != null) && (clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert After", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Insert After: '" + ((Annotation) clipboard).getType() + "' annotation inserted");
			
			//	insert tokens
			int insertIndex = selection.getEndIndex();
			this.insertAt(((MutableAnnotation) clipboard), this.content, insertIndex);
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void insertTokensAfter() {
		this.insertTokensAfter(this.annotateSelection(null, false));
	}
	
	private void insertTokensAfter(String id) {
		this.insertTokensAfter(this.getAnnotationByID(id));
	}
	
	private void insertTokensAfter(Annotation annotation) {
		if ((annotation != null) && (clipboard != null)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Insert Tokens After", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Insert Tokens After: " + clipboard.size() + " tokens inserted");
			
			//	insert tokens
			int insertIndex = annotation.getEndIndex();
			this.content.insertTokensAt(clipboard, insertIndex);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void appendTo(String id) {
		MutableAnnotation annotation = this.getAnnotationByID(id);
		if ((annotation != null) && (clipboard != null) && (clipboard instanceof MutableAnnotation)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Append", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Append: '" + ((Annotation) clipboard).getType() + "' annotation appended to '" + annotation.getType() + "' annotation");
			
			//	insert tokens
			int insertIndex = annotation.size();
			this.insertAt(((MutableAnnotation) clipboard), annotation, insertIndex);
			
			//	remember modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void appendTokensTo(String id) {
		MutableAnnotation annotation = this.getAnnotationByID(id);
		if ((annotation != null) && (clipboard != null)) {
			
			//	create undo action
			this.parent.enqueueRestoreContentUndoAction("Append Tokens", annotation.getStartIndex(), annotation.size());
			this.parent.writeLog("Append Tokens: " + clipboard.size() + " tokens appended to '" + annotation.getType() + "' annotation");
			
			//	append tokens
			annotation.addTokens(clipboard);
			
			//	propagate modification
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	private void deleteTokens() {
		this.deleteTokens(this.annotateSelection(null, false));
	}
	
	private void deleteTokens(String id) {
		Annotation annotation = this.getAnnotationByID(id);
		if ((annotation != null) && (JOptionPane.showConfirmDialog(this, "Really delete this Annotation and all Tokens contained in it?", "Confirm Delete Annotation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)) {
			this.deleteTokens(annotation);
		}
	}
	
	private void deleteTokens(Annotation selection) {
		if (selection != null) {
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Delete Tokens", selection.getStartIndex(), selection.size());
			this.parent.writeLog("Delete Tokens: " + selection.size() + " tokens deleted");
			
			this.content.removeTokens(selection);
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	//	methods doing all the work for annotation cut & paste
	private MutableAnnotation copyMutableAnnotation(Annotation annotation) {
		if (annotation != null) {
			
			//	copy tokens
			MutableAnnotation copyDoc = Gamta.newDocument(this.content.getMutableSubsequence(annotation.getStartIndex(), annotation.size()));
			
			//	get view
			String tempDocPartType = Gamta.getAnnotationID();
			MutableAnnotation temp = this.content.addAnnotation(tempDocPartType, annotation.getStartIndex(), annotation.size());
			
			//	copy contained Annotations
			Annotation[] annotations = temp.getAnnotations();
			for (int a = 0; a < annotations.length; a++)
				if (!tempDocPartType.equals(annotations[a].getType()))
					copyDoc.addAnnotation(annotations[a]);
			
			//	remove view if it's artificial
			if (tempDocPartType.equals(temp.getType()))
				this.content.removeAnnotation(temp);
			
			//	return copy
			return copyDoc;
		} else return null;
	}
	
	private void insertAt(MutableAnnotation data, MutableAnnotation target, int insertIndex) {
		if ((data != null) && (target != null)) {
			
			//	insert tokens
			target.insertTokensAt(data, insertIndex);
			
			//	annotate
			MutableAnnotation temp = target.addAnnotation(TEMP_ANNOTATION_TYPE, insertIndex, data.size());
			Annotation[] annotations = data.getAnnotations();
			int aStart = (((annotations.length == 0) || !DocumentRoot.DOCUMENT_TYPE.equals(annotations[0].getType())) ? 0 : 1); 
			for (int a = aStart; a < annotations.length; a++)
				temp.addAnnotation(annotations[a]);
			
			//	remove helper
			target.removeAnnotation(temp);
		}
	}
	//	end of methods doing all the work for annotation cut & paste
	
	private void openAnnotation(MouseEvent me) {
		
		//	get position of click
		Point clickPoint = me.getPoint();
		int clickPosition = this.displayPanel.display.viewToModel(clickPoint);
		
		//	get position a bit to the left of click
		Point leftOfClickPoint = new Point((clickPoint.x - 15), clickPoint.y);
		int leftOfClickPosition = this.displayPanel.display.viewToModel(leftOfClickPoint);
		
		//	check if clicked in tag or to the right of it
		if ((leftOfClickPoint.x < 0) || (clickPosition != leftOfClickPosition)) {
			AnnotationSpan span = getInmostAnnotationSpanForPosition(clickPosition + this.displayPanel.displayOffset - this.displayPanel.firstVisibleLine);
			if (span != null) this.editAnnotation(span.annotation);
		}
	}
	
	private void showPopupMenu(MouseEvent me) {
		
		final Annotation selection = this.annotateSelection(null, false);
		MutableAnnotation[] selectedAnnotations = this.getAnnotationsForSelectedTags();
		
		//	no Tokens selected
		if ((selection == null) && (selectedAnnotations.length < 2)) {
			
			//	get position of click
			Point clickPoint = me.getPoint();
			int clickPosition = this.displayPanel.display.viewToModel(clickPoint);
			
			//	check click position only if document not empty
			if (this.content.size() != 0) {
				
				//	get position a bit to the left of click
				Point leftOfClickPoint = new Point((clickPoint.x - 15), clickPoint.y);
				int leftOfClickPosition = this.displayPanel.display.viewToModel(leftOfClickPoint);
				
				//	check if clicked in tag or to the right of it
				if ((leftOfClickPoint.x >= 0) && (clickPosition == leftOfClickPosition)) return;
			}
			
			AnnotationSpan span = getInmostAnnotationSpanForPosition(clickPosition + this.displayPanel.displayOffset - this.displayPanel.firstVisibleLine);
			
			//	clicked outside any visible Annotations
			if ((span == null) && (selectedAnnotations.length == 0)) {
//				System.out.println("  --> clicked in text");
				
				final int tokenIndex = ((this.content.size() == 0) ? 0 : this.getTokenIndexForPosition(clickPosition + this.displayPanel.displayOffset - this.displayPanel.firstVisibleLine));
				if ((clipboard != null) && (tokenIndex != -1)) {
					this.contextMenu.removeAll();
					JMenuItem mi;
					
					if (clipboard instanceof MutableAnnotation) {
						mi = new JMenuItem("Paste");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Paste");					
								pasteClipboardAt(tokenIndex);
							}
						});
						this.contextMenu.add(mi);
						this.contextMenu.addSeparator();
					}
					mi = new JMenuItem("Paste Tokens");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Paste Tokens");					
							pasteClipboardTokensAt(tokenIndex);
						}
					});
					this.contextMenu.add(mi);
					this.contextMenu.show(this.displayPanel.display, me.getX(), me.getY());
				}
			}
			
			//	clicked in tag or highlight area, or selected part of tag
			else {
				final String annotationId;
				MutableAnnotation annotation;
				
				if (span == null) {
					annotation = selectedAnnotations[0];
					annotationId = annotation.getAnnotationID();
				}
				else {
					annotation = span.annotation;
					annotationId = span.annotation.getAnnotationID();
				}
				
				//	add Annotation actions
				this.contextMenu.removeAll();
				JMenuItem mi;
				
				//	assemble context menu for Annotations
				mi = new JMenuItem("Edit");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Edit");					
						editAnnotation(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Edit Attributes");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Edit Attributes");					
						editAnnotationAttributes(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Rename");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Rename");					
						renameAnnotation(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Copy");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Copy");					
						copyAnnotation(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Cut");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Cut");					
						cutAnnotation(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Remove");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Remove");					
						removeAnnotation(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Remove All");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Remove All");					
						removeAllAnnotations(annotationId);
					}
				});
				this.contextMenu.add(mi);
				this.contextMenu.addSeparator();
				
				mi = new JMenuItem("Edit Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Edit Tokens");					
						editAnnotationTokens(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Copy Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Copy Tokens");					
						copyTokens(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Cut Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Cut Tokens");					
						cutTokens(annotationId);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Delete Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Delete Tokens");					
						deleteTokens(annotationId);
					}
				});
				this.contextMenu.add(mi);
				
				//	add copy/paste functions if clipboard not empty
				if (clipboard != null) {
					this.contextMenu.addSeparator();
					
					//	add actions for doc part copy/paste
					if (clipboard instanceof MutableAnnotation) {
						
						//	end tag
						if ((span != null) && (span instanceof AnnotationTagSpan) && "E".equals(((AnnotationTagSpan) span).tagType)) {
							
							mi = new JMenuItem("Swap Clipboard");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Swap Clipboard");					
									swapClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
							mi = new JMenuItem("Paste");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Paste");					
									pasteClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Append");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Append");					
									appendTo(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Insert After");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert After");					
									insertAfter(annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
						
						//	start tag
						else if ((span != null) && (span instanceof AnnotationTagSpan) && "S".equals(((AnnotationTagSpan) span).tagType)) {
							
							mi = new JMenuItem("Insert Before");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert Before");					
									insertBefore(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Insert");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert");					
									insertTo(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Swap Clipboard");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Swap Clipboard");					
									swapClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Paste");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Paste");					
									pasteClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
						
						//	highlighted annotation
						else {
							mi = new JMenuItem("Insert Before");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert Before");					
									insertBefore(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Insert");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert");					
									insertTo(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							mi = new JMenuItem("Swap Clipboard");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Swap Clipboard");					
									swapClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
							mi = new JMenuItem("Paste");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Paste");					
									pasteClipboard(annotationId);
								}
							});
							this.contextMenu.add(mi);
							
							if (annotation instanceof MutableAnnotation) {
								mi = new JMenuItem("Append");
								mi.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent ae) {
										parent.writeLog("Context Menu --> Append");					
										appendTo(annotationId);
									}
								});
								this.contextMenu.add(mi);
							}
							mi = new JMenuItem("Insert After");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert After");					
									insertAfter(annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
						this.contextMenu.addSeparator();
					}
					
					//	add token functions
					//	end tag
					if ((span != null) && (span instanceof AnnotationTagSpan) && "E".equals(((AnnotationTagSpan) span).tagType)) {
						
						mi = new JMenuItem("Swap Clipboard Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Swap Clipboard Tokens");					
								swapClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
						mi = new JMenuItem("Paste Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Paste Tokens");					
								pasteClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						//	end tag of doc part
						if (annotation instanceof MutableAnnotation) {
							mi = new JMenuItem("Append Tokens");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Append Tokens");					
									appendTokensTo(annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
						mi = new JMenuItem("Insert Tokens After");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Insert Tokens After");					
								insertTokensAfter(annotationId);
							}
						});
						this.contextMenu.add(mi);
					}
					
					//	start tag
					else if ((span != null) && (span instanceof AnnotationTagSpan) && "S".equals(((AnnotationTagSpan) span).tagType)) {
						
						mi = new JMenuItem("Insert Tokens Before");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Insert Tokens Before");					
								insertTokensBefore(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						//	start tag of doc part
						if (annotation instanceof MutableAnnotation) {
							mi = new JMenuItem("Insert Tokens");
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.writeLog("Context Menu --> Insert Tokens");					
									insertTokensTo(annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
						
						mi = new JMenuItem("Swap Clipboard Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Swap Clipboard Tokens");					
								swapClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
						mi = new JMenuItem("Paste Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Paste Tokens");					
								pasteClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
					}
					
					//	highlighted annotation
					else {
						mi = new JMenuItem("Insert Tokens Before");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Insert Tokens Before");					
								insertTokensBefore(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						mi = new JMenuItem("Insert Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Insert Tokens");					
								insertTokensTo(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						mi = new JMenuItem("Swap Clipboard Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Swap Clipboard Tokens");					
								swapClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
						mi = new JMenuItem("Paste Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Paste Tokens");					
								pasteClipboardTokens(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						mi = new JMenuItem("Append Tokens");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Append Tokens");					
								appendTokensTo(annotationId);
							}
						});
						this.contextMenu.add(mi);
						
						mi = new JMenuItem("Insert Tokens After");
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> Insert Tokens After");					
								insertTokensAfter(annotationId);
							}
						});
						this.contextMenu.add(mi);
					}
				}
				
				//	collect custom functions for context menu
				CustomFunction[] customFunctions = this.host.getCustomFunctions();
				ArrayList cfList = new ArrayList();
				for (int c = 0; c < customFunctions.length; c++) {
					if (customFunctions[c].displayFor(annotation))
						cfList.add(customFunctions[c]);
				}
				customFunctions = ((CustomFunction[]) cfList.toArray(new CustomFunction[cfList.size()]));
				if (customFunctions.length != 0) {
					this.contextMenu.addSeparator();
					for (int c = 0; c < customFunctions.length; c++) {
						final CustomFunction cf = customFunctions[c];
						mi = new JMenuItem(cf.label);
						mi.setToolTipText(cf.toolTip);
						mi.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								parent.writeLog("Context Menu --> " + cf.label);					
								executeCustomFunction(cf, annotationId);
							}
						});
						this.contextMenu.add(mi);
					}
				}
				
				//	add document processor managers
				DocumentProcessorManager[] dpms = this.host.getDocumentProcessorProviders();
				if (dpms.length != 0) {
					this.contextMenu.addSeparator();
					for (int m = 0; m < dpms.length; m++) {
						String toolsMenuLabel = dpms[m].getToolsMenuLabel();
						if (toolsMenuLabel != null) {
							final String className = dpms[m].getClass().getName();
							mi = new JMenuItem(toolsMenuLabel + " " + dpms[m].getResourceTypeLabel());
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.applyDocumentProcessor(className, null, annotationId, null);
								}
							});
							this.contextMenu.add(mi);
						}
					}
				}
				
				//	add annotation source managers
				AnnotationSourceManager[] asms = this.host.getAnnotationSourceProviders();
				if (asms.length != 0) {
					this.contextMenu.addSeparator();
					for (int m = 0; m < asms.length; m++) {
						String toolsMenuLabel = asms[m].getToolsMenuLabel();
						if (toolsMenuLabel != null) {
							final String className = asms[m].getClass().getName();
							mi = new JMenuItem(toolsMenuLabel + " " + asms[m].getResourceTypeLabel());
							mi.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									parent.applyAnnotationSource(className, null, annotationId);
								}
							});
							this.contextMenu.add(mi);
						}
					}
				}
				
				//	show context menu
				this.contextMenu.show(this.displayPanel.display, me.getX(), me.getY());
			}
		}
		
		//	selected parts of more than one tag, but no text
		else if (selection == null) {
			this.contextMenu.removeAll();
			
			//	modify annotation items
			JMenuItem mi;
			mi = new JMenuItem("Merge Annotations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Merge Annotation");					
					mergeAnnotations();
				}
			});
			this.contextMenu.add(mi);
			mi = new JMenuItem("Swap Annotations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Swap Annotation");					
					swapAnnotations();
				}
			});
			this.contextMenu.add(mi);
			this.contextMenu.show(this.displayPanel.display, me.getX(), me.getY());
		}
		
		//	text selected
		else {
//			System.out.println("  --> selected tokens: " + selection.concatTokens());
			
			this.contextMenu.removeAll();
			
			//	create annotation items
			this.contextMenu.add(this.annotateMenuItem);
			this.contextMenu.add(this.annotateAllMenuItem);
			this.contextMenu.addSeparator();
			
			//	modify annotation items
			JMenuItem mi;
			mi = new JMenuItem("Merge Annotations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Merge Annotation");					
					mergeAnnotations();
				}
			});
			this.contextMenu.add(mi);
			mi = new JMenuItem("Swap Annotations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Swap Annotations");					
					swapAnnotations();
				}
			});
			this.contextMenu.add(mi);
			if (selection.size() == 1) {
				mi = new JMenuItem("Split Annotation before '" + selection.firstValue() + "'");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Split Before");					
						splitAnnotation(false, false);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Split Annotation around '" + selection.firstValue() + "'");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Split Around");					
						splitAnnotation(false, true);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Split Annotation before all '" + selection.firstValue() + "'");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Split Before All");					
						splitAnnotation(true, false);
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Split Annotation around all '" + selection.firstValue() + "'");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Split Around All");					
						splitAnnotation(true, true);
					}
				});
				this.contextMenu.add(mi);
			}
			
			//	functions for moving annotation borders
			if (selectedAnnotations.length != 0) {
				final MutableAnnotation affected = selectedAnnotations[selectedAnnotations.length - 1];
				if ((selection.getStartIndex() == affected.getStartIndex()) || (selection.getEndIndex() == affected.getEndIndex())) {
					mi = new JMenuItem("Exclude Tokens");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Exclude Tokens");					
							shrinkAnnotation(affected, selection);
						}
					});
					this.contextMenu.add(mi);
				}
				else {
					mi = new JMenuItem("Include Tokens");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Include Tokens");					
							extendAnnotation(affected, selection);
						}
					});
					this.contextMenu.add(mi);
				}
			}
			this.contextMenu.addSeparator();
			
			//	token actions
			mi = new JMenuItem("Edit Tokens");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Edit Tokens");					
					editSelection();
				}
			});
			this.contextMenu.add(mi);
			
			if (selection.size() == 1) {
				mi = new JMenuItem("Edit Token Attributes");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Edit Token Attributes");					
						editTokenAttributes(selection.tokenAt(0), selection.getStartIndex());
					}
				});
				this.contextMenu.add(mi);
			}
			
			mi = new JMenuItem("Copy Tokens");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Copy Tokens");					
					copyTokens();
				}
			});
			this.contextMenu.add(mi);
			
			mi = new JMenuItem("Cut Tokens");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Cut Tokens");					
					cutTokens();
				}
			});
			this.contextMenu.add(mi);
			
			if (clipboard != null) {
				if (clipboard instanceof MutableAnnotation) {
					mi = new JMenuItem("Insert Before");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Insert Before");					
							insertBefore();
						}
					});
					this.contextMenu.add(mi);
					mi = new JMenuItem("Swap Clipboard");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Swap Clipboard");					
							swapClipboard();
						}
					});
					this.contextMenu.add(mi);
					mi = new JMenuItem("Paste");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Paste");					
							pasteClipboard();
						}
					});
					this.contextMenu.add(mi);
					mi = new JMenuItem("Insert After");
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							parent.writeLog("Context Menu --> Insert After");					
							insertAfter();
						}
					});
					this.contextMenu.add(mi);
					this.contextMenu.addSeparator();
				}
				mi = new JMenuItem("Insert Tokens Before");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Insert Tokens Before");					
						insertTokensBefore();
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Swap Clipboard Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Swap Clipboard Tokens");					
						swapClipboardTokens();
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Paste Tokens");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Paste Tokens");					
						pasteClipboardTokens();
					}
				});
				this.contextMenu.add(mi);
				mi = new JMenuItem("Insert Tokens After");
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Insert Tokens After");					
						insertTokensAfter();
					}
				});
				this.contextMenu.add(mi);
			}
			mi = new JMenuItem("Delete Tokens");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.writeLog("Context Menu --> Delete Tokens");					
					deleteTokens();
				}
			});
			this.contextMenu.add(mi);
			
			this.contextMenu.show(this.displayPanel.display, me.getX(), me.getY());
		}
	}
	
	private void editAnnotation(String annotationID) {
		if (annotationID != null) {
			MutableAnnotation annotation = this.getAnnotationByID(annotationID);
			if (annotation != null) this.editAnnotation(annotation);
		}
	}
	
	private boolean editAnnotation(MutableAnnotation annotation) {
		if (annotation == null) return false;
		
		//	create dialog
		MutableAnnotationEditDialog dped = new MutableAnnotationEditDialog("Edit Annotation", annotation);
		
		//	save content for undo
		this.parent.enqueueRestoreContentUndoAction("Edit Annotation", annotation.getStartIndex(), annotation.size());
		this.parent.writeLog("Edit Annotation: opened '" + annotation.getType() + "' annotation for editing");
		
		//	show dialog
		dped.setVisible(true);
		
		//	remove Annotation
		if (dped.isRemoved()) {
			this.parent.writeLog("  --> annotation removed");
			this.parent.enqueueRestoreMarkupUndoAction(annotation.getType(), "Remove Annotation", annotation.getStartIndex(), annotation.size());
			
			this.content.removeAnnotation(annotation);
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			if (this.isShowingTags(annotation.getType())) this.refreshDisplay();
			else {
				this.layoutTypePanels();
				ArrayList spanList = new ArrayList();
				AnnotationSpan removedHighlight = null;
				for (int a = 0; a < this.annotationSpans.length; a++)
					if (annotation.getAnnotationID().equals(this.annotationSpans[a].annotation.getAnnotationID())) {
						if (this.annotationSpans[a] instanceof AnnotationHighlightSpan)
							removedHighlight = ((AnnotationHighlightSpan) this.annotationSpans[a]);
						this.annotationSpansByAnnotationID.remove(this.annotationSpans[a].annotation.getAnnotationID());
					}
					else spanList.add(this.annotationSpans[a]);
//					if (!annotation.getAnnotationID().equals(this.annotationSpans[a].annotation.getAnnotationID()))
//						spanList.add(this.annotationSpans[a]);
//					else if (this.annotationSpans[a] instanceof AnnotationHighlightSpan)
//						removedHighlight = ((AnnotationHighlightSpan) this.annotationSpans[a]);
				this.annotationSpans = ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
				if (removedHighlight != null) this.applySpans(removedHighlight.getStartOffset(), removedHighlight.getSize());
				else this.applySpans();
			}
			return true;
		}
		
		//	delete Annotation
		else if (dped.isDeleted()) {
			this.parent.writeLog("  --> annotation deleted");
			this.deleteTokens(annotation);
			return true;
		}
		
		//	update Annotation
		else if (dped.isDirty()) {
			boolean needRefresh = false;
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			
			if (dped.isContentModified()) {
				this.parent.notifyDocumentTextModified();
				needRefresh = true;
				
			}
			if (needRefresh || this.isShowingTags(annotation.getType())) this.refreshDisplay();
			return true;
		}
		else return false;
	}
	
	/**
	 * dialog for editing document parts a individual documents 
	 * 
	 * @author sautter
	 */
	private static Dimension partEditDialogSize = new Dimension(800, 600);
	private static Point partEditDialogLocation = null;
	private class MutableAnnotationEditDialog extends DocumentEditorDialog {
		
		boolean commit = false;
		boolean remove = false;
		boolean delete = false;
		
		MutableAnnotationEditDialog(String title, MutableAnnotation docPart) {
			super(AnnotationEditorPanel.this.host, AnnotationEditorPanel.this.parent, title, docPart);
			
			//	initialize main buttons
			JButton okButton = new JButton("OK");
			okButton.setBorder(BorderFactory.createRaisedBevelBorder());
			okButton.setPreferredSize(new Dimension(80, 21));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			this.mainButtonPanel.add(okButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
			cancelButton.setPreferredSize(new Dimension(100, 21));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancel();
				}
			});
			this.mainButtonPanel.add(cancelButton);
			
			JButton removeButton = new JButton("Remove");
			removeButton.setBorder(BorderFactory.createRaisedBevelBorder());
			removeButton.setPreferredSize(new Dimension(80, 21));
			removeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					remove();
				}
			});
			this.mainButtonPanel.add(removeButton);
			
			JButton deleteButton = new JButton("Delete");
			deleteButton.setBorder(BorderFactory.createRaisedBevelBorder());
			deleteButton.setPreferredSize(new Dimension(80, 21));
			deleteButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					delete();
				}
			});
			this.mainButtonPanel.add(deleteButton);
			
			//	create file menu
			JMenuItem mi = new JMenuItem("Make Separate Document");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					makeDocument();
				}
			});
			this.fileMenu.add(mi, 0);
			
			//	configure window
			this.setResizable(true);
			this.setSize(partEditDialogSize);
			if (partEditDialogLocation == null) this.setLocationRelativeTo(parent);
			else this.setLocation(partEditDialogLocation);
		}
		
		private void makeDocument() {
			parent.writeLog("Edit Dialog --> Make Document");
			
			//	display part in editor
			this.host.openDocument(Gamta.copyDocument(this.content), (AnnotationEditorPanel.this.parent.getContentName() + "." + this.content.getType()), this.parent.getContentFormat());
		}
		
		/** @see java.awt.Window#dispose()
		 */
		public void dispose() {
			partEditDialogSize = this.getSize();
			partEditDialogLocation = this.getLocation(partEditDialogLocation);
			super.dispose();
		}
		
		/**	@return	true if and only if something was changed
		 */
		boolean isDirty() {
			return (this.commit && this.documentEditor.isContentModified());
		}
		
		/**	@return	true if and only the dialog was closed with the remove button
		 */
		boolean isRemoved() {
			return this.remove;
		}
		
		/**	@return	true if and only the dialog was closed with the delete button
		 */
		boolean isDeleted() {
			return this.delete;
		}
		
		private void remove() {
			parent.writeLog("Edit Dialog --> Remove");
			this.remove = true;
			this.documentEditor.writeChanges();
			this.dispose();
		}
		
		private void delete() {
			parent.writeLog("Edit Dialog --> Delete");
			if (JOptionPane.showConfirmDialog(this, "Really delete MutableAnnotation and all contained Tokens?", "Confirm Delete MutableAnnotation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				parent.writeLog("Edit Dialog --> Delete Confirmed");
				this.delete = true;
				this.documentEditor.writeChanges();
				this.dispose();
			}
		}
		
		private void commit() {
			parent.writeLog("Edit Dialog --> Close");
			this.documentEditor.writeChanges();
			this.commit = true;
			this.dispose();
		}
		
		private void cancel() {
			parent.writeLog("Edit Dialog --> Cancelled");
			this.dispose();
		}
	}
	
	private void editAnnotationAttributes(String annotationID) {
		if (annotationID != null) {
			Annotation annotation = this.getAnnotationByID(annotationID);
			if (annotation != null) this.editAnnotationAttributes(annotation);
		}
	}
	
	private static Dimension attributeEditorDialogSize = new Dimension(400, 300);
	private static Point attributeEditorDialogLocation = null;
	
	private boolean editAnnotationAttributes(Annotation annotation) {
		if (annotation == null) return false;
		
		this.parent.enqueueRestoreMarkupUndoAction(annotation.getType(), "Edit Annotation Attributes", annotation.getStartIndex(), annotation.size());
		this.parent.writeLog("Edit Annotation Attributes: opened '" + annotation.getType() + "' annotation for editing");
		AttributeEditorDialog aed = new AttributeEditorDialog("Edit Annotation Attributes", annotation, content) {
			public void dispose() {
				attributeEditorDialogSize = this.getSize();
				attributeEditorDialogLocation = this.getLocation(attributeEditorDialogLocation);
				super.dispose();
			}
		};
		
		//	position and show dialog
		aed.setSize(attributeEditorDialogSize);
		if (attributeEditorDialogLocation == null) aed.setLocationRelativeTo(parent);
		else aed.setLocation(attributeEditorDialogLocation);
		aed.setVisible(true);
		
		if (aed.isDirty()) {
			this.parent.writeLog("Attribute Edit Dialog --> Close");
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			if (this.isShowingTags(annotation.getType())) this.updateAnnotationTags(annotation);
			return true;
		}
		else {
			this.parent.writeLog("Attribute Edit Dialog --> Cancel");
			return false;
		}
	}
	
	private boolean editTokenAttributes(Token token, int index) {
		if (token == null) return false;
		
		this.parent.enqueueRestoreMarkupUndoAction(Token.TOKEN_ANNOTATION_TYPE, "Edit Annotation Attributes", index, 1);
		this.parent.writeLog("Edit Token Attributes: opened '" + Token.TOKEN_ANNOTATION_TYPE + "' annotation for editing");
		AttributeEditorDialog aed = new AttributeEditorDialog("Edit Token Attributes", token, content) {
			public void dispose() {
				attributeEditorDialogSize = this.getSize();
				attributeEditorDialogLocation = this.getLocation(attributeEditorDialogLocation);
				super.dispose();
			}
		};
		
		//	position and show dialog
		aed.setSize(attributeEditorDialogSize);
		if (attributeEditorDialogLocation == null) aed.setLocationRelativeTo(parent);
		else aed.setLocation(attributeEditorDialogLocation);
		aed.setVisible(true);
		
		if (aed.isDirty()) {
			this.parent.writeLog("Attribute Edit Dialog --> Close");
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			return true;
		}
		else {
			this.parent.writeLog("Attribute Edit Dialog --> Cancel");
			return false;
		}
	}
	
	void annotate() {
		this.annotate(null, true);
	}
	
	void annotate(String type, boolean interactive) {
		Annotation preAnnotation =  this.annotateSelection(type, true);
		if (preAnnotation == null) return;
		
		//	open Annotation for editing if desired
		boolean create = true;
		if (interactive) {
//			String[] annotationTypes = this.parent.getAnnotationTypes(true);
//			Arrays.sort(annotationTypes, String.CASE_INSENSITIVE_ORDER);
			CreateAnnotationDialog cad = new CreateAnnotationDialog("Create Annotation", "Annotate", preAnnotation);
			cad.setVisible(true);
			if (cad.isCommitted()) preAnnotation.changeTypeTo(cad.getType());
			else create = false;
		}
		
		//	check if dialog confirmed
		if (create) {
			
			//	create undo action
			this.parent.enqueueRestoreMarkupUndoAction(preAnnotation.getType(), "Annotate", preAnnotation.getStartIndex(), preAnnotation.size());
			this.parent.storeUndoAction();
			this.parent.writeLog("Annotate: " + preAnnotation.size() + " tokens annotated as '" + preAnnotation.getType() + "'");
			
			//	annotate
			MutableAnnotation annotation = this.content.addAnnotation(preAnnotation);
			boolean needAllSpans = false;
			boolean needRefresh = false;
			if (structuralTypes.contains(preAnnotation.getType())) {
				
				if (highlightNewDocParts) needAllSpans = this.showHighlight(annotation.getType(), false);
				if (tagNewDocParts) {
					this.showTags(annotation.getType(), false);
					needRefresh = true;
				}
				
			} else {
				
				if (highlightNewAnnotations) needAllSpans = this.showHighlight(annotation.getType(), false);
				if (tagNewAnnotations) {
					this.showTags(annotation.getType(), false);
					needRefresh = true;
				}
			}
			
			this.parent.notifyDocumentMarkupModified();
			if (rememberAnnotationType(annotation.getType())) this.layoutSelectionActions();
			
			if (needRefresh || this.isShowingTags(annotation.getType())) this.refreshDisplay();
			else {
				AnnotationHighlightSpan span = new AnnotationHighlightSpan(this.tokenSpans[annotation.getStartIndex()], this.tokenSpans[annotation.getEndIndex() - 1], annotation, this.getHighlightLayoutForAnnotationType(annotation.getType()));
				span.isShowing = this.isHighlighted(annotation.getType());
				AnnotationSpan[] allSpans = new AnnotationSpan[this.annotationSpans.length + 1];
				System.arraycopy(this.annotationSpans, 0, allSpans, 0, this.annotationSpans.length);
				allSpans[this.annotationSpans.length] = span;
				Arrays.sort(allSpans);
				this.annotationSpans = allSpans;
				ArrayList spanList = new ArrayList();
				spanList.add(span);
				this.annotationSpansByAnnotationID.put(annotation.getAnnotationID(), spanList);
				
				this.annotationsByID.put(annotation.getAnnotationID(), annotation);
				this.layoutTypePanels();
				this.layoutSelectionActions();
				if (needAllSpans) this.applySpans();
				else this.applySpans(span.getStartOffset(), span.getSize());
			}
		}
	}
	
	void annotateAll() {
		this.annotateAll(null, true);
	}
	
	void annotateAll(String type, boolean interactive) {
		Annotation preAnnotation = this.annotateSelection(type, true);
		if (preAnnotation == null) return;
		
		//	open Annotation for editing if desired
		boolean create = true;
		if (interactive) {
			CreateAnnotationDialog cad = new CreateAnnotationDialog("Create Annotations", "Annotate All", preAnnotation);
			cad.setVisible(true);
			if (cad.isCommitted()) preAnnotation.changeTypeTo(cad.getType());
			else create = false;
		}
		
		//	check if dialog confirmed
		if (create) {
			
			//	create undo action
			this.parent.enqueueRestoreMarkupUndoAction(preAnnotation.getType(), "Annotate All");
			this.parent.storeUndoAction();
			
			//	create annotations
			ArrayList annotationList = new ArrayList();
			int startIndex = 0;
			while ((startIndex = TokenSequenceUtils.indexOf(this.content, preAnnotation, startIndex, annotateAllCaseSensitive)) != -1) {
				annotationList.add(this.content.addAnnotation(preAnnotation.getType(), startIndex, preAnnotation.size()));
				startIndex += preAnnotation.size();
			}
			this.parent.writeLog("Annotate All: " + annotationList.size() + " occurrences of '" + preAnnotation.getValue() + "' annotated as '" + preAnnotation.getType() + "'");
			MutableAnnotation[] annotations = ((MutableAnnotation[]) annotationList.toArray(new MutableAnnotation[annotationList.size()]));
			
			//	check if structural type
			boolean needRefresh = false;
			if (structuralTypes.contains(preAnnotation.getType())) {
				if (highlightNewDocParts) this.showHighlight(preAnnotation.getType(), false);
				if (tagNewDocParts) {
					this.showTags(preAnnotation.getType(), false);
					needRefresh = true;
				}
			}
			
			else {
				if (highlightNewAnnotations) this.showHighlight(preAnnotation.getType(), false);
				if (tagNewAnnotations) {
					this.showTags(preAnnotation.getType(), false);
					needRefresh = true;
				}
			}
			
			this.parent.notifyDocumentMarkupModified();
			if (rememberAnnotationType(preAnnotation.getType())) this.layoutSelectionActions();
			
			if (needRefresh || this.isShowingTags(preAnnotation.getType())) this.refreshDisplay();
			else {
				AnnotationSpan[] spans = new AnnotationSpan[annotations.length];
				for (int a = 0; a < annotations.length; a++) {
					this.annotationsByID.put(annotations[a].getAnnotationID(), annotations[a]);
					spans[a] = new AnnotationHighlightSpan(this.tokenSpans[annotations[a].getStartIndex()], this.tokenSpans[annotations[a].getEndIndex() - 1], annotations[a], this.getHighlightLayoutForAnnotationType(annotations[a].getType()));
					spans[a].isShowing = this.isHighlighted(preAnnotation.getType());
					ArrayList spanList = new ArrayList();
					spanList.add(spans[a]);
					this.annotationSpansByAnnotationID.put(annotations[a].getAnnotationID(), spanList);
				}
				AnnotationSpan[] allSpans = new AnnotationSpan[this.annotationSpans.length + spans.length];
				System.arraycopy(this.annotationSpans, 0, allSpans, 0, this.annotationSpans.length);
				System.arraycopy(spans, 0, allSpans, this.annotationSpans.length, spans.length);
				Arrays.sort(allSpans);
				this.annotationSpans = allSpans;
				
				this.layoutTypePanels();
				this.layoutSelectionActions();
				this.applySpans();
			}
		}
	}
	
	/**
	 * dialog for creating a new annotation
	 * 
	 * @author sautter
	 */
	private class CreateAnnotationDialog extends DialogPanel {
		
		JComboBox annotationTypeChooser;
		
		String type = null;
		String[] existingTypes;
		
		private boolean keyPressed = false;
		
		CreateAnnotationDialog(String title, String buttonText, Annotation annotation) {
			super(title, true);
			
			StringVector suggestions = new StringVector();
			suggestions.addContent(getAnnotationTypeSuggestions());
			suggestions.addContentIgnoreDuplicates(parent.getAnnotationTypes(true));
			this.existingTypes = suggestions.toStringArray();//(((existingTypes == null) || (existingTypes.length == 0)) ? EMPTY_TYPES : existingTypes);
			Arrays.sort(this.existingTypes, ANNOTATION_TYPE_ORDER);
			this.type = annotation.getType();
			if ((this.type == null) || (this.type.trim().length() == 0) || Annotation.DEFAULT_ANNOTATION_TYPE.equals(this.type))
				this.type = this.existingTypes[0];
			
			this.annotationTypeChooser = new JComboBox(this.existingTypes);
			this.annotationTypeChooser.setBorder(BorderFactory.createLoweredBevelBorder());
			this.annotationTypeChooser.setSelectedItem(this.type);
			this.annotationTypeChooser.setEditable(true);
			this.annotationTypeChooser.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					annotationTypeChanged();
				}
			});
			this.annotationTypeChooser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (keyPressed && isVisible() && !annotationTypeChooser.isPopupVisible()) commit();
				}
			});
			((JTextComponent) this.annotationTypeChooser.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					keyPressed = true;
				}
				public void keyReleased(KeyEvent ke) {
					keyPressed = false;
				}
			});
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.annotationTypeChooser, gbc.clone());
			
			//	initialize main buttons
			JPanel mainButtonPanel = new JPanel(new FlowLayout());
			JButton createButton = new JButton((buttonText == null) ? "Create" : buttonText);
			createButton.setBorder(BorderFactory.createRaisedBevelBorder());
			createButton.setPreferredSize(new Dimension(80, 21));
			createButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			mainButtonPanel.add(createButton);
			
			JButton cancelButton = new JButton("Cancel");
			cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
			cancelButton.setPreferredSize(new Dimension(80, 21));
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			mainButtonPanel.add(cancelButton);
			
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.NORTH);
			this.getContentPane().add(mainButtonPanel, BorderLayout.CENTER);
			
			this.setSize(300, 90);
			this.setResizable(true);
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		/**	@return	true if and only the dialog was closed with the commit button
		 */
		boolean isCommitted() {
			return ((this.type != null) && (this.type.trim().length() != 0));
		}
		
		private void annotationTypeChanged() {
			Object item = annotationTypeChooser.getSelectedItem();
			if (item != null) {
				String newType = item.toString();
				if (!this.type.equals(newType)) {
					if ((newType != null) && (newType.length() > 0)) {
						this.type = newType;
					} else annotationTypeChooser.setSelectedItem(this.type);
				}
			}
		}
		
		/**	@return	the new Annotation type
		 */
		String getType() {
			return this.type;
		}
		
		private void abort() {
			parent.writeLog("Create Dialog --> Cancel");
			this.type = null;
			this.dispose();
		}
		
		private void commit() {
			parent.writeLog("Create Dialog --> Create");
			this.dispose();
		}
	}
	
	void mergeAnnotations() {
		
		//	get and check selected Annotations
		Annotation[] selectedAnnotations = this.getAnnotationsForSelectedTags();
		if (selectedAnnotations.length == 0) {
			JOptionPane.showMessageDialog(this, "Please select Annotations to be merged.", "Invalid Annotations", JOptionPane.ERROR_MESSAGE);
		} else if (selectedAnnotations.length == 1) {
			JOptionPane.showMessageDialog(this, "Cannot merge Annotation with itself.", "Invalid Annotations", JOptionPane.ERROR_MESSAGE);
		} else {
			String type = selectedAnnotations[0].getType();
			boolean makeMutableAnnotation = (selectedAnnotations[0] instanceof MutableAnnotation);
			
			//	check type
			for (int a = 1; a < selectedAnnotations.length; a++)
				if (!type.equalsIgnoreCase(selectedAnnotations[a].getType())) {
					JOptionPane.showMessageDialog(this, "Cannot merge Annotations of different types.", "Invalid Annotations", JOptionPane.ERROR_MESSAGE);
					return;
				}
			
			//	compute indices
			int startIndex = this.content.size();
			int endIndex = 0;
			for (int a = 0; a < selectedAnnotations.length; a++) {
				if (selectedAnnotations[a].getStartIndex() < startIndex)
					startIndex = selectedAnnotations[a].getStartIndex();
				if (endIndex < selectedAnnotations[a].getEndIndex())
					endIndex = selectedAnnotations[a].getEndIndex();
			}
			
			//	create undo action
			this.parent.enqueueRestoreMarkupUndoAction(type, "Merge Annotations", startIndex, (endIndex - startIndex));
			this.parent.storeUndoAction();
			this.parent.writeLog("Merge Annotations: " + selectedAnnotations.length + " '" + selectedAnnotations[0].getType() + "' annotations merged");
			
			//	perform merge
			Annotation mergedAnnotation;
			if (makeMutableAnnotation)
				mergedAnnotation = this.content.addAnnotation(type, startIndex, (endIndex - startIndex));
			else mergedAnnotation = this.content.addAnnotation(type, startIndex, (endIndex - startIndex));
			
			//	remove mergerd Annotations & copy attributes
			for (int a = selectedAnnotations.length; a > 0; a--) {
				mergedAnnotation.copyAttributes(selectedAnnotations[a - 1]);
				this.content.removeAnnotation(selectedAnnotations[a - 1]);
			}
			
			//	propagate modification
			this.parent.notifyDocumentMarkupModified();
			this.refreshDisplay();
		}
	}
	
	private void swapAnnotations() {
		
		//	get and check selected Annotations
		MutableAnnotation[] selectedAnnotations = this.getAnnotationsForSelectedTags();
		if (selectedAnnotations.length != 2) {
			JOptionPane.showMessageDialog(this, "Please select two Annotations to be swapped.", "Invalid Annotations", JOptionPane.ERROR_MESSAGE);
		} else if (selectedAnnotations[0].getEndIndex() != selectedAnnotations[1].getStartIndex()) {
			JOptionPane.showMessageDialog(this, "Cannot swap Annotations that are not neighbours.", "Invalid Annotations", JOptionPane.ERROR_MESSAGE);
		} else {
			
			//	save content for undo
			this.parent.enqueueRestoreContentUndoAction("Swap Annotations", selectedAnnotations[0].getStartIndex(), (selectedAnnotations[1].getEndIndex() - selectedAnnotations[0].getStartIndex()));
			this.parent.writeLog("Swap Annotations: '" + selectedAnnotations[0].getType() + "' annotation swapped with '" + selectedAnnotations[1].getType() + "' annotations");
			
			//	copy tokens & markup
			MutableAnnotation cutDoc = this.copyMutableAnnotation(selectedAnnotations[1]);
			
			//	cut tokens
			this.content.removeTokens(selectedAnnotations[1]);
			
			//	insert tokens
			int insertIndex = selectedAnnotations[0].getStartIndex();
			this.insertAt(cutDoc, this.content, insertIndex);
			
			//	propagate changes
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	void splitAnnotation(boolean all, boolean around) {
		final Annotation selection = this.annotateSelection(null, false);
		
		if ((selection == null) || (selection.size() != 1)) {
			JOptionPane.showMessageDialog(this, "Mark the one Token to split the Annotation before or around.", "Invalid Spilt Point", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		//	get annotation to split
		Annotation splitAnnotation = this.getAnnotationSurrounding(selection.getStartIndex());
		if (splitAnnotation == null)
			return;
		
		//	get splitting point(s)
		Annotation[] splits = {Gamta.newAnnotation(splitAnnotation, null, (selection.getStartIndex() - splitAnnotation.getStartIndex()), selection.size())};
		if (all) {
			StringVector dict = new StringVector(annotateAllCaseSensitive);
			dict.addElement(selection.firstValue());
			splits = Gamta.extractAllContained(splitAnnotation, dict);
		}
		
		//	check splits
		int splitCount = 0;
		for (int s = 0; s < splits.length; s++) {
			if ((splits[s].getStartIndex() > 0) && (splits[s].getStartIndex() < (splitAnnotation.size() - (around ? 1 : 0))))
				splitCount++;
		}
		if (splitCount == 0)
			return;
		
		//	create undo action
		this.parent.enqueueRestoreMarkupUndoAction(splitAnnotation.getType(), "Split Annotation", splitAnnotation.getStartIndex(), splitAnnotation.size());
		this.parent.storeUndoAction();
		this.parent.writeLog("Split Annotations: '" + splitAnnotation.getType() + "' annotation split at '" + selection.getValue() + "'");
		
		//	perform splits
		for (int s = 0; s <= splits.length; s++) {
			if ((s < splits.length) && (splits[s].getStartIndex() == 0))
				continue;
			int start = (splitAnnotation.getStartIndex() + ((s == 0) ? 0 : (splits[s-1].getStartIndex() + (around ? 1 : 0))));
			int end = (splitAnnotation.getStartIndex() + ((s == splits.length) ? splitAnnotation.size() : splits[s].getStartIndex()));
			if (start < end) {
				Annotation annotation = this.content.addAnnotation(splitAnnotation.getType(), start, (end - start));
				annotation.copyAttributes(splitAnnotation);
			}
		}
		this.content.removeAnnotation(splitAnnotation);
		
		//	propagate modification
		this.parent.notifyDocumentMarkupModified();
		this.refreshDisplay();
	}
	
	void renameAnnotations() {
		AnnotationFilter[] filters = this.getFilters(false);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		
		RenameAnnotationsDialog rad = new RenameAnnotationsDialog(filters, initial);
		rad.setVisible(true);
		if (rad.typeChanged()) {
			String newType = rad.getTargetType();
			if (newType.trim().length() != 0)
				this.renameAnnotations(rad.getFilter(), newType);
		}
	}
	
	private void renameAnnotations(AnnotationFilter filter, String newType) {
		this.parent.enqueueRestoreMarkupUndoAction("Rename Annotations");
		Annotation[] annotations = filter.getMatches(this.content);
		boolean modified = AnnotationTools.renameAnnotations(this.content, annotations, newType);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Rename: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			actionLabel += (" -> " + newType);
			
			String actionTooltip = "Rename ";
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			actionTooltip += (" to '" + newType + "'");
			AnnotationAction aa = new AnnotationAction(RENAME_ANNOTATION_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					newType,
					null,
					null);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Rename Annotations: '" + ((AnnotationTypeFilter) filter).type + "' annotations renamed to '" + newType + "'");
			else this.parent.writeLog("Rename Annotations: annotations matching '" + filter.toString() + "' renamed to '" + newType + "'");
			this.parent.storeUndoAction();
			
			boolean needRefresh = false;
			if (filter instanceof AnnotationTypeFilter) {
				String annotationType = ((AnnotationTypeFilter) filter).type;
				if (this.isHighlighted(annotationType)) {
					this.showHighlight(newType, false);
				}
				if (this.isShowingTags(annotationType)) {
					if (this.showTags(newType, false)) {
						needRefresh = true;
					} else {
						for (int a = 0; a < annotations.length; a++)
							this.updateAnnotationTags(annotations[a]);
					}
				} else if (this.isShowingTags(newType)) needRefresh = true;
			} else {
				for (int a = 0; a < annotations.length; a++) {
					if (this.isHighlighted(annotations[a].getType()))
						this.showHighlight(newType, false);
					if (this.isShowingTags(annotations[a].getType())) {
						this.showTags(newType, false);
						needRefresh = true;
					}
				}
			}
			
			this.parent.notifyDocumentMarkupModified();
			if (needRefresh) this.refreshDisplay();
			else {
				this.layoutActions();
				this.layoutTypePanels();
				this.applySpans();
			}
		}
	}
	
	/**
	 * dialog for entering the parameters of a rename annotations operation
	 * @author sautter
	 */
	private class RenameAnnotationsDialog extends DialogPanel {
		
		private JComboBox sourceTypeSelector;
		private JComboBox targetTypeSelector;
		
		private boolean isCommitted = false;
		private boolean keyPressed = false;
		
		RenameAnnotationsDialog(AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Rename Annotations", true);
			
			//	initialize selector
			this.sourceTypeSelector = new JComboBox(filters);
			this.sourceTypeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.sourceTypeSelector.setEditable(false);
			this.sourceTypeSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					sourceTypeChanged();
				}
			});
			
			StringVector suggestions = new StringVector();
			suggestions.addContent(getAnnotationTypeSuggestions());
			suggestions.addContentIgnoreDuplicates(parent.getAnnotationTypes(true));
			String[] targetTypes = suggestions.toStringArray();
			Arrays.sort(targetTypes, ANNOTATION_TYPE_ORDER);
			
			this.targetTypeSelector = new JComboBox(targetTypes);
			this.targetTypeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.targetTypeSelector.setEditable(true);
			this.targetTypeSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (keyPressed && isVisible() && !targetTypeSelector.isPopupVisible()) commit();
				}
			});
			((JTextComponent) this.targetTypeSelector.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					keyPressed = true;
				}
				public void keyReleased(KeyEvent ke) {
					keyPressed = false;
				}
			});
			
			if (initial != null) this.sourceTypeSelector.setSelectedItem(initial);
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotations To Rename"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.sourceTypeSelector, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Rename Annotations To"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.targetTypeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Rename");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 120));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		private void sourceTypeChanged() {
			Object item = this.sourceTypeSelector.getSelectedItem();
			if (item != null) this.targetTypeSelector.setSelectedItem(item);
		}
		
		boolean typeChanged() {
			return this.isCommitted;
		}
		
		AnnotationFilter getFilter() {
			Object item = this.sourceTypeSelector.getSelectedItem();
			return ((AnnotationFilter) item);
		}
		
//		String getSourceType() {
//			Object item = this.sourceTypeSelector.getSelectedItem();
//			return ((item == null) ? "" : item.toString());
//		}
//		
		String getTargetType() {
			System.out.println("RenameAnnotationsDialog: retrieving target type ...");
			Object item = this.targetTypeSelector.getSelectedItem();
			System.out.println(" --> " + item);
			return ((item == null) ? "" : item.toString());
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
	
	void removeAnnotations() {
		AnnotationFilter[] filters = this.getFilters(true);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		RemoveAnnotationsDialog rad = new RemoveAnnotationsDialog(filters, initial);
		rad.setVisible(true);
		if (rad.isCommitted()) this.removeAnnotations(rad.getFilter());
	}
	
	private void removeAnnotations(AnnotationFilter filter) {
		if (filter instanceof AnnotationTypeFilter) this.parent.enqueueRestoreMarkupUndoAction(((AnnotationTypeFilter) filter).type, "Remove Annotations");
		else this.parent.enqueueRestoreMarkupUndoAction("Remove Annotations");
		
		Annotation[] annotations = filter.getMatches(this.content);
		boolean wasHighlighted = false;
		for (int a = 0; a < annotations.length; a++)
			wasHighlighted = (wasHighlighted || this.isHighlighted(annotations[a].getType()));
		boolean modified = AnnotationTools.removeAnnotations(this.content, annotations);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Remove: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			
			String actionTooltip = "Remove ";
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			AnnotationAction aa = new AnnotationAction(REMOVE_ANNOTATION_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					null,
					null,
					null);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Remove Annotations: '" + ((AnnotationTypeFilter) filter).type + "' annotations removed");
			else this.parent.writeLog("Remove Annotations: Annotations matching '" + filter.toString() + "' removed");
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			
			boolean needRefresh = false;
			if (filter instanceof AnnotationTypeFilter) {
				if (((AnnotationTypeFilter) filter).type == null) needRefresh = true;
				else if (this.isShowingTags(((AnnotationTypeFilter) filter).type)) needRefresh = true;
			} else {
				for (int a = 0; a < annotations.length; a++)
					if (this.isShowingTags(annotations[a].getType())) needRefresh = true;
			}
			
			if (needRefresh) this.refreshDisplay();
			else {
				this.layoutActions();
				this.layoutTypePanels();
				
				HashSet removed = new HashSet();
				for (int a = 0; a < annotations.length; a++) removed.add(annotations[a].getAnnotationID());
				
				ArrayList spanList = new ArrayList();
				for (int a = 0; a < this.annotationSpans.length; a++)
					if (removed.contains(this.annotationSpans[a].annotation.getAnnotationID()))
						this.annotationSpansByAnnotationID.remove(this.annotationSpans[a].annotation.getAnnotationID());
					else spanList.add(this.annotationSpans[a]);
				this.annotationSpans = ((AnnotationSpan[]) spanList.toArray(new AnnotationSpan[spanList.size()]));
				
				if (wasHighlighted) this.applySpans();
			}
		}
	}
	
	/**
	 * dialog for entering the parameters of an annotation removal operation
	 * 
	 * @author sautter
	 */
	private class RemoveAnnotationsDialog extends DialogPanel {
		
		private JComboBox typeSelector;
		private boolean isCommitted = false;
		
		RemoveAnnotationsDialog(AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Remove Annotation", true);
			
			//	initialize selector
			this.typeSelector = new JComboBox(filters);
			this.typeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.typeSelector.setEditable(false);
			if (initial != null) this.typeSelector.setSelectedItem(initial);
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.typeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Remove");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 90));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		boolean isCommitted() {
			return this.isCommitted;
		}
		
		AnnotationFilter getFilter() {
			Object item = this.typeSelector.getSelectedItem();
			return ((AnnotationFilter) item);
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
	
	void deleteAnnotations() {
		AnnotationFilter[] filters = this.getFilters(false);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		DeleteAnnotationsDialog dad = new DeleteAnnotationsDialog(filters, initial);
		dad.setVisible(true);
		if (dad.isCommitted()) this.deleteAnnotations(dad.getFilter());
	}
	
	private void deleteAnnotations(AnnotationFilter filter) {
		//	save content for undo
		this.parent.enqueueRestoreContentUndoAction("Delete Annotations");
		
		//	delete annotations
		boolean modified = AnnotationTools.deleteAnnotations(this.content, filter);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Delete: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			
			String actionTooltip = "Delete ";
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			AnnotationAction aa = new AnnotationAction(DELETE_ANNOTATION_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					null,
					null,
					null);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Delete Annotations: '" + ((AnnotationTypeFilter) filter).type + "' annotations deleted");
			else this.parent.writeLog("Delete Annotations: Annotations matching '" + filter.toString() + "' deleted");
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			this.parent.notifyDocumentTextModified();
			this.refreshDisplay();
		}
	}
	
	/**
	 * dialog for entering the parameters of a delete annotations operation
	 * 
	 * @author sautter
	 */
	private class DeleteAnnotationsDialog extends DialogPanel {
		
		private JComboBox typeSelector;
		private boolean isCommitted = false;
		
		DeleteAnnotationsDialog(AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Delete Annotation", true);
			
			//	initialize selector
			this.typeSelector = new JComboBox(filters);
			this.typeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			if (initial != null) this.typeSelector.setSelectedItem(initial);
			this.typeSelector.setEditable(false);
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.typeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Delete");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 90));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		boolean isCommitted() {
			return this.isCommitted;
		}
		
		AnnotationFilter getFilter() {
			Object item = this.typeSelector.getSelectedItem();
			return ((AnnotationFilter) item);
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
	
	void renameAnnotationAttribute() {
		AnnotationFilter[] filters = this.getFilters(true);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		RenameAttributeDialog rad = new RenameAttributeDialog(this.content, filters, initial);
		rad.setVisible(true);
		if (rad.attributeChanged()) {
			String oldName = rad.getSourceAttribute();
			String newName = rad.getTargetAttribute();
			if (newName.trim().length() != 0) this.renameAnnotationAttribute(rad.getFilter(), oldName, newName);
		}
	}
	
	private void renameAnnotationAttribute(AnnotationFilter filter, String attribute, String newAttribute) {
		
		if (filter instanceof AnnotationTypeFilter) this.parent.enqueueRestoreMarkupUndoAction(((AnnotationTypeFilter) filter).type, "Rename Annotation Attribute");
		else this.parent.enqueueRestoreMarkupUndoAction("Rename Annotation Attribute");
		Annotation[] annotations = filter.getMatches(this.content);
		boolean modified = AnnotationTools.renameAnnotationAttribute(annotations, attribute, newAttribute);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Rename: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			actionLabel += ("." + attribute + " -> " + newAttribute);
			
			String actionTooltip = "Rename attribute '" + attribute + "' of ";
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			actionTooltip += (" to '" + newAttribute + "'");
			AnnotationAction aa = new AnnotationAction(RENAME_ATTRIBUTE_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					null,
					attribute,
					newAttribute);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Rename Annotation Attribute: '" + attribute + "' attribute renamed to '" + newAttribute + "' in " + ((((AnnotationTypeFilter) filter).type == null) ? "all" : ("'" + ((AnnotationTypeFilter) filter).type + "'")) + " annotations");
			else this.parent.writeLog("Rename Annotation Attribute: '" + attribute + "' attribute renamed to '" + newAttribute + "' in all Annotations matching " + filter.toString());
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			
			boolean needRefresh = false;
			if (filter instanceof AnnotationTypeFilter) {
				if (this.isShowingTags(((AnnotationTypeFilter) filter).type)) needRefresh = true;
			} else {
				for (int a = 0; a < annotations.length; a++)
					if (this.isShowingTags(annotations[a].getType())) needRefresh = true;
			}
			
			if (needRefresh) this.refreshDisplay();
			else this.layoutActions();
		}
	}
	
	/**
	 * dialog for entering the parameters of an attribute renaming operation
	 * 
	 * @author sautter
	 */
	private class RenameAttributeDialog extends DialogPanel {
		
		private MutableAnnotation data;
		
		private JComboBox filterSelector;
		private JComboBox sourceAttributeSelector;
		private JComboBox targetAttributeSelector;
		
		private boolean isCommitted = false;
		private boolean keyPressed = false;
		
		RenameAttributeDialog(MutableAnnotation data, AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Rename Annotation Attributes", true);
			this.data = data;
			
			//	initialize selector
			this.filterSelector = new JComboBox(filters);
			this.filterSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.filterSelector.setEditable(false);
			this.filterSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					filterChanged();
				}
			});
			
			this.sourceAttributeSelector = new JComboBox();
			this.sourceAttributeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.sourceAttributeSelector.setEditable(false);
			this.sourceAttributeSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					sourceAttributeChanged();
				}
			});
			
			this.targetAttributeSelector = new JComboBox();
			this.targetAttributeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.targetAttributeSelector.setEditable(true);
			this.targetAttributeSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (keyPressed && isVisible() && !targetAttributeSelector.isPopupVisible()) commit();
				}
			});
			((JTextComponent) this.targetAttributeSelector.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					keyPressed = true;
				}
				public void keyReleased(KeyEvent ke) {
					keyPressed = false;
				}
			});
			
			Annotation[] annotations = this.data.getAnnotations(null);
			StringVector attributeNameCollector = new StringVector();
			for (int a = 0; a < annotations.length; a++)
				attributeNameCollector.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			attributeNameCollector.sortLexicographically(false, false);
			
			this.sourceAttributeSelector.removeAllItems();
			this.targetAttributeSelector.removeAllItems();
			for (int i = 0; i < attributeNameCollector.size(); i++) {
				this.sourceAttributeSelector.addItem(attributeNameCollector.get(i));
				this.targetAttributeSelector.addItem(attributeNameCollector.get(i));
			}
			
			if (initial != null) this.filterSelector.setSelectedItem(initial);
			
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.filterSelector, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Attribute To Rename"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.sourceAttributeSelector, gbc.clone());
			
			gbc.gridy = 2;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Rename Attribute To"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.targetAttributeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Rename");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 150));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		private void filterChanged() {
			Object filter = this.filterSelector.getSelectedItem();
			Annotation[] annotations = ((AnnotationFilter) filter).getMatches(this.data);
			StringVector matchTypes = new StringVector();
			StringVector attributeNames = new StringVector();
			for (int a = 0; a < annotations.length; a++) {
				matchTypes.addElementIgnoreDuplicates(annotations[a].getType());
				attributeNames.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			}
			
			attributeNames.sortLexicographically(false, false);
			this.sourceAttributeSelector.removeAllItems();
			for (int a = 0; a < attributeNames.size(); a++)
				this.sourceAttributeSelector.addItem(attributeNames.get(a));
			
			for (int t = 0; t < matchTypes.size(); t++)
				attributeNames.addContentIgnoreDuplicates(getAnnotationAttributeSuggestions(matchTypes.get(t)));
			attributeNames.sortLexicographically(false, false);
			this.targetAttributeSelector.removeAllItems();
			for (int a = 0; a < attributeNames.size(); a++)
				this.targetAttributeSelector.addItem(attributeNames.get(a));
		}
		
		private void sourceAttributeChanged() {
			Object item = this.sourceAttributeSelector.getSelectedItem();
			if (item != null) this.targetAttributeSelector.setSelectedItem(item);
		}
		
		boolean attributeChanged() {
			return (this.isCommitted && !this.getSourceAttribute().equals(this.getTargetAttribute()));
		}
		
		AnnotationFilter getFilter() {
			Object item = this.filterSelector.getSelectedItem();
			return ((AnnotationFilter) item);
		}
		
		String getSourceAttribute() {
			Object item = this.sourceAttributeSelector.getSelectedItem();
			return ((item == null) ? null : item.toString());
		}
		
		String getTargetAttribute() {
			Object item = this.targetAttributeSelector.getSelectedItem();
			return ((item == null) ? "" : item.toString());
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
	
	void modifyAnnotationAttribute() {
		AnnotationFilter[] filters = this.getFilters(true);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		ModifyAttributeDialog mad = new ModifyAttributeDialog(this.content, filters, initial);
		mad.setVisible(true);
		if (mad.isCommitted()) this.modifyAnnotationAttribute(mad.getFilter(), mad.getAttribute(), mad.getOldValue(), mad.getNewValue(), mad.getMode());
	}
	
	private void modifyAnnotationAttribute(AnnotationFilter filter, String attribute, Object oldValue, Object newValue, String mode) {
		if (mode == null) return;
		String editName = (mode + " Annotation Attribute");
		
		if (filter instanceof AnnotationTypeFilter) this.parent.enqueueRestoreMarkupUndoAction(((AnnotationTypeFilter) filter).type, editName);
		else this.parent.enqueueRestoreMarkupUndoAction(editName);
		Annotation[] annotations = filter.getMatches(this.content);
		boolean modified = false;
		if ("Add".equals(mode)) modified = AnnotationTools.addAnnotationAttribute(this.content, filter, attribute, newValue);
		if ("Set".equals(mode)) modified = AnnotationTools.setAnnotationAttribute(this.content, filter, attribute, newValue);
		if ("Change".equals(mode)) modified = AnnotationTools.changeAnnotationAttribute(this.content, filter, attribute, oldValue, newValue);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Remove: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			actionLabel += ("." + ((attribute == null) ? "<All Attributes>" : attribute));
			
			String actionTooltip = ("Remove " + ((attribute == null) ? "all attributes" : ("attribute '" + attribute + "'")) + " from ");
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			AnnotationAction aa = new AnnotationAction(REMOVE_ATTRIBUTE_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					null,
					attribute,
					null);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Remove Annotation Attribute: '" + attribute + "' attribute removed from " + ((((AnnotationTypeFilter) filter).type == null) ? "all" : ("'" + ((AnnotationTypeFilter) filter).type + "'")) + " annotations");
			else this.parent.writeLog("Remove Annotation Attribute: '" + attribute + "' attribute removed from all matches of '" + filter.toString() + "'");
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			
			boolean needRefresh = false;
			if (filter instanceof AnnotationTypeFilter) {
				if (((AnnotationTypeFilter) filter).type == null) needRefresh = true;
				else if (this.isShowingTags(((AnnotationTypeFilter) filter).type)) needRefresh = true;
			} else {
				for (int a = 0; a < annotations.length; a++)
					if (this.isShowingTags(annotations[a].getType())) needRefresh = true;
			}
			
			if (needRefresh) this.refreshDisplay();
			else this.layoutActions();
		}
	}
	
	/**
	 * dialog for entering the parameters of an attribute modification operation
	 * 
	 * @author sautter
	 */
	private class ModifyAttributeDialog extends DialogPanel {
		
		private static final String allValues = "<All Values>";
		
		private MutableAnnotation data;
		
		private JComboBox filterSelector;
		private JComboBox attributeSelector;
		private JComboBox oldValueSelector;
		private JComboBox newValueSelector;
		
		private JRadioButton addButton = new JRadioButton("Add where not set", true);
		private JRadioButton setButton = new JRadioButton("Set everywhere");
		private JRadioButton changeButton = new JRadioButton("Change value above");
		
		private boolean isCommitted = false;
		private boolean keyPressed = false;
		
		ModifyAttributeDialog(MutableAnnotation data, AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Modify Annotation Attributes", true);
			this.data = data;
			
			//	initialize selector
			this.filterSelector = new JComboBox(filters);
			this.filterSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.filterSelector.setEditable(false);
			this.filterSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					filterChanged();
				}
			});
			
			this.attributeSelector = new JComboBox();
			this.attributeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.attributeSelector.setEditable(true);
			this.attributeSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					attributeChanged();
				}
			});
			
			this.oldValueSelector = new JComboBox();
			this.oldValueSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.oldValueSelector.setEditable(false);
			this.oldValueSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					oldValueChanged();
				}
			});
			
			this.newValueSelector = new JComboBox();
			this.newValueSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.newValueSelector.setEditable(true);
			this.newValueSelector.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					if (keyPressed && isVisible() && !newValueSelector.isPopupVisible()) commit();
				}
			});
			((JTextComponent) this.newValueSelector.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent ke) {
					keyPressed = true;
				}
				public void keyReleased(KeyEvent ke) {
					keyPressed = false;
				}
			});
			
			
			Annotation[] annotations = this.data.getAnnotations(null);
			StringVector attributeNameCollector = new StringVector();
			for (int a = 0; a < annotations.length; a++)
				attributeNameCollector.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			attributeNameCollector.sortLexicographically(false, false);
			
			this.attributeSelector.removeAllItems();
			for (int i = 0; i < attributeNameCollector.size(); i++)
				this.attributeSelector.addItem(attributeNameCollector.get(i));
			
			if (initial != null) this.filterSelector.setSelectedItem(initial);
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.filterSelector, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Attribute To Modify"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.attributeSelector, gbc.clone());
			
			gbc.gridy = 2;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Old Attribute Value"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.oldValueSelector, gbc.clone());
			
			gbc.gridy = 3;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("New Attribute Value"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.newValueSelector, gbc.clone());
			
			ButtonGroup bg = new ButtonGroup();
			bg.add(this.addButton);
			bg.add(this.setButton);
			bg.add(this.changeButton);
			JPanel modePanel = new JPanel(new GridLayout(1,3));
			modePanel.add(this.addButton);
			modePanel.add(this.setButton);
			modePanel.add(this.changeButton);
			gbc.gridy = 4;
			gbc.gridx = 0;
			gbc.weightx = 1;
			gbc.gridwidth = 2;
			selectorPanel.add(modePanel, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Modify");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 200));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		private void filterChanged() {
			Object filter = this.filterSelector.getSelectedItem();
			Annotation[] annotations = ((AnnotationFilter) filter).getMatches(this.data);
			StringVector attributeNameCollector = new StringVector();
			for (int a = 0; a < annotations.length; a++)
				attributeNameCollector.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			attributeNameCollector.sortLexicographically(false, false);
			
			Object attribute = this.attributeSelector.getSelectedItem();
			this.attributeSelector.removeAllItems();
			for (int i = 0; i < attributeNameCollector.size(); i++)
				this.attributeSelector.addItem(attributeNameCollector.get(i));
			this.attributeSelector.setSelectedItem(attribute);
		}
		
		private void attributeChanged() {
			Object filter = this.filterSelector.getSelectedItem();
			Object attributeItem = this.attributeSelector.getSelectedItem();
			if (attributeItem != null) {
				String attribute = attributeItem.toString();
				Annotation[] annotations = ((AnnotationFilter) filter).getMatches(this.data);
				StringVector matchTypes = new StringVector();
				StringVector attributeNames = new StringVector();
				StringVector attributeValues = new StringVector();
				
				for (int a = 0; a < annotations.length; a++) {
					if (annotations[a].hasAttribute(attribute))
						attributeValues.addElementIgnoreDuplicates(annotations[a].getAttribute(attribute).toString());
				}
				
				attributeValues.sortLexicographically(false, false);
				this.oldValueSelector.removeAllItems();
				this.oldValueSelector.addItem(allValues);
				for (int i = 0; i < attributeValues.size(); i++)
					this.oldValueSelector.addItem(attributeValues.get(i));
				
				for (int t = 0; t < matchTypes.size(); t++) {
					String type = matchTypes.get(t);
					for (int a = 0; a < attributeNames.size(); a++)
						attributeValues.addContentIgnoreDuplicates(getAttributeValueSuggestions(type, attributeNames.get(a)));
				}
				
				attributeValues.sortLexicographically(false, false);
				this.newValueSelector.removeAllItems();
				for (int i = 0; i < attributeValues.size(); i++) {
					this.newValueSelector.addItem(attributeValues.get(i));
				}
			}
		}
		
		private void oldValueChanged() {
			Object item = this.oldValueSelector.getSelectedItem();
			if (item != null) this.newValueSelector.setSelectedItem(item);
		}
		
		boolean isCommitted() {
			return this.isCommitted;
		}
		
		AnnotationFilter getFilter() {
			return ((AnnotationFilter) this.filterSelector.getSelectedItem());
		}
		
		String getAttribute() {
			return this.attributeSelector.getSelectedItem().toString();
		}
		
		String getOldValue() {
			Object item = this.oldValueSelector.getSelectedItem();
			return (allValues.equals(item) ? null : item.toString());
		}
		
		String getNewValue() {
			Object item = this.newValueSelector.getSelectedItem();
			return ((item == null) ? "" : item.toString());
		}
		
		String getMode() {
			if (this.addButton.isSelected()) return "Add";
			else if (this.setButton.isSelected()) return "Set";
			else if (this.changeButton.isSelected()) return "Change";
			else return null;
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
		
	void removeAnnotationAttribute() {
		AnnotationFilter[] filters = this.getFilters(true);
		AnnotationFilter initial = null;
		Annotation selected = null;
		AnnotationSpan selectedSpan = this.getInmostAnnotationSpanForPosition(this.lastClickPosition);
		if (selectedSpan != null) selected = selectedSpan.annotation;
		if (selected == null)
			selected = this.getAnnotationSurrounding(this.getTokenIndexForPosition(this.lastClickPosition));
		if (selected != null) {
			for (int f = 0; f < filters.length; f++)
				if ((filters[f] instanceof AnnotationTypeFilter) && (((AnnotationTypeFilter) filters[f]).type != null) && ((AnnotationTypeFilter) filters[f]).type.equals(selected.getType()))
					initial = filters[f];
		}
		RemoveAttributeDialog rad = new RemoveAttributeDialog(this.content, filters, initial);
		rad.setVisible(true);
		if (rad.isCommitted()) this.removeAnnotationAttribute(rad.getFilter(), rad.getAttribute());
	}
	
	private void removeAnnotationAttribute(AnnotationFilter filter, String attribute) {
		if (filter instanceof AnnotationTypeFilter) this.parent.enqueueRestoreMarkupUndoAction(((AnnotationTypeFilter) filter).type, "Remove Annotation Attribute");
		else this.parent.enqueueRestoreMarkupUndoAction("Remove Annotation Attribute");
		Annotation[] annotations = filter.getMatches(this.content);
		boolean modified = AnnotationTools.removeAnnotationAttribute(annotations, attribute);
		
		if (!(filter instanceof GPathAnnotationFilter) || (((GPathAnnotationFilter) filter).gPath != null)) {
			String actionLabel = "Remove: ";
			if (filter instanceof AnnotationTypeFilter) actionLabel += ((AnnotationTypeFilter) filter).type;
			else if (acceptAllFilter.equals(filter)) actionLabel += "<All Annotations>";
			else actionLabel+= ("'" + filter.toString() + "'");
			actionLabel += ("." + ((attribute == null) ? "<All Attributes>" : attribute));
			
			String actionTooltip = ("Remove " + ((attribute == null) ? "all attributes" : ("attribute '" + attribute + "'")) + " from ");
			if (filter instanceof AnnotationTypeFilter) actionTooltip += ("all Annotations of type '" + ((AnnotationTypeFilter) filter).type + "'");
			else if (acceptAllFilter.equals(filter)) actionTooltip += "all Annotations";
			else actionTooltip+= ("all matches of '" + filter.toString() + "'");
			AnnotationAction aa = new AnnotationAction(REMOVE_ATTRIBUTE_ACTION,
					actionLabel,
					actionTooltip,
					filter.getName(),
					filter.getProviderClassName(),
					null,
					attribute,
					null);
			if (rememberAction(aa)) this.layoutActions();
		}
		
		if (modified) {
			if (filter instanceof AnnotationTypeFilter) this.parent.writeLog("Remove Annotation Attribute: '" + attribute + "' attribute removed from " + ((((AnnotationTypeFilter) filter).type == null) ? "all" : ("'" + ((AnnotationTypeFilter) filter).type + "'")) + " annotations");
			else this.parent.writeLog("Remove Annotation Attribute: '" + attribute + "' attribute removed from all matches of '" + filter.toString() + "'");
			
			this.parent.storeUndoAction();
			this.parent.notifyDocumentMarkupModified();
			
			boolean needRefresh = false;
			if (filter instanceof AnnotationTypeFilter) {
				if (((AnnotationTypeFilter) filter).type == null) needRefresh = true;
				else if (this.isShowingTags(((AnnotationTypeFilter) filter).type)) needRefresh = true;
			} else {
				for (int a = 0; a < annotations.length; a++)
					if (this.isShowingTags(annotations[a].getType())) needRefresh = true;
			}
			
			if (needRefresh) this.refreshDisplay();
			else this.layoutActions();
		}
	}
	
	/**
	 * dialog for entering the parameters of an attribute removal operation
	 * 
	 * @author sautter
	 */
	private class RemoveAttributeDialog extends DialogPanel {
		
		private static final String ALL_ATTRIBUTES_ATTRIBUTE = "<All Attributes>";
		
		private MutableAnnotation data;
		
		private JComboBox typeSelector;
		private JComboBox attributeSelector;
		
		private boolean isCommitted = false;
		
		RemoveAttributeDialog(MutableAnnotation data, AnnotationFilter[] filters, AnnotationFilter initial) {
			super("Remove Annotation Attributes", true);
			this.data = data;
			
			this.attributeSelector = new JComboBox();
			this.attributeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.attributeSelector.setEditable(false);
			
			Annotation[] annotations = this.data.getAnnotations(null);
			StringVector attributeNameCollector = new StringVector();
			for (int a = 0; a < annotations.length; a++)
				attributeNameCollector.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			attributeNameCollector.sortLexicographically(false, false);
			this.attributeSelector.removeAllItems();
			this.attributeSelector.addItem(ALL_ATTRIBUTES_ATTRIBUTE);
			for (int i = 0; i < attributeNameCollector.size(); i++)
				this.attributeSelector.addItem(attributeNameCollector.get(i));
			
			this.typeSelector = new JComboBox(filters);
			this.typeSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.typeSelector.setEditable(false);
			this.typeSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					sourceTypeChanged();
				}
			});
			if (initial != null) this.typeSelector.setSelectedItem(initial);
			
			JPanel selectorPanel = new JPanel(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.weighty = 1;
			gbc.gridwidth = 1;
			gbc.gridheight = 1;
			gbc.insets.top = 5;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.insets.bottom = 5;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Annotation Type"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.typeSelector, gbc.clone());
			
			gbc.gridy = 1;
			gbc.gridx = 0;
			gbc.weightx = 0;
			selectorPanel.add(new JLabel("Attribute Name"), gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 1;
			selectorPanel.add(this.attributeSelector, gbc.clone());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Remove");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(selectorPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(400, 120));
			this.setLocationRelativeTo(AnnotationEditorPanel.this);
		}
		
		private void sourceTypeChanged() {
			Object item = this.typeSelector.getSelectedItem();
			Annotation[] annotations = ((AnnotationFilter) item).getMatches(this.data);
			StringVector attributeNameCollector = new StringVector();
			for (int a = 0; a < annotations.length; a++)
				attributeNameCollector.addContentIgnoreDuplicates(annotations[a].getAttributeNames());
			attributeNameCollector.sortLexicographically(false, false);
			this.attributeSelector.removeAllItems();
			this.attributeSelector.addItem(ALL_ATTRIBUTES_ATTRIBUTE);
			for (int i = 0; i < attributeNameCollector.size(); i++)
				this.attributeSelector.addItem(attributeNameCollector.get(i));
		}
		
		boolean isCommitted() {
			return this.isCommitted;
		}
		
		AnnotationFilter getFilter() {
			Object item = this.typeSelector.getSelectedItem();
			return ((AnnotationFilter) item);
		}
		
		String getAttribute() {
			Object item = this.attributeSelector.getSelectedItem();
			return (((item == null) || ALL_ATTRIBUTES_ATTRIBUTE.equals(item)) ? null : item.toString());
		}
		
		private void abort() {
			this.dispose();
		}

		private void commit() {
			this.isCommitted = true;
			this.dispose();
		}
	}
	
	void tagSentences() {
		
		//	save content for undo
		this.parent.enqueueRestoreMarkupUndoAction(MutableAnnotation.SENTENCE_TYPE, "Tag Sentences");
		this.parent.writeLog("Tag Sentences: sentences marked up by builtin sentence tagger");
		
		Gamta.tagSentences(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentMarkupModified();
		this.showTags(MutableAnnotation.SENTENCE_TYPE, false);
		this.refreshDisplay();
	}
	
	void tagParagraphs() {
		
		//	save content for undo
		this.parent.enqueueRestoreMarkupUndoAction(MutableAnnotation.PARAGRAPH_TYPE, "Tag Paragraphs");
		this.parent.writeLog("Tag Paragraphs: paragraphs marked up by builtin paragraph tagger");
		
		Gamta.tagParagraphs(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentMarkupModified();
		this.showTags(MutableAnnotation.PARAGRAPH_TYPE, false);
		this.refreshDisplay();
	}
	
	void tagSections() {
		
		//	save content for undo
		this.parent.enqueueRestoreMarkupUndoAction(MutableAnnotation.SECTION_TYPE, "Tag Sections");
		this.parent.writeLog("Tag Sections: sections marked up by builtin section tagger");
		
		Gamta.tagSections(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentMarkupModified();
		this.showTags(MutableAnnotation.SECTION_TYPE, false);
		this.refreshDisplay();
	}
	
	void normalizeParagraphs() {
		
		//	save content for undo
		this.parent.enqueueRestoreContentUndoAction("Normalize Paragraphs");
		this.parent.writeLog("Normalize Paragraphs: paragraphs normalized by builtin paragraph normalizer");
		
		//	perform normalization
		Gamta.normalizeParagraphStructure(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentTextModified();
		this.showTags(MutableAnnotation.PARAGRAPH_TYPE, false);
		this.refreshDisplay();
	}
	
	void normalizeWhitespace() {
		
		//	save content for undo
		this.parent.enqueueRestoreContentUndoAction("Normalize Whitespace");
		this.parent.writeLog("Normalize Whitespace: token whitespace normalized by builtin whitespace normalizer");
		
		//	perform normalization
		Gamta.normalizeWhitespace(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentTextModified();
		this.showTags(MutableAnnotation.PARAGRAPH_TYPE, false);
		this.refreshDisplay();
	}
	
	void markLineEnds() {
		
		//	save content for undo
		this.parent.enqueueRestoreContentUndoAction("Mark Line Ends");
		this.parent.writeLog("Mark Line Ends: line ends marked by built-in analyzer");
		
		//	perform normalization
		Gamta.markLineEnds(this.content);
		
		this.parent.storeUndoAction();
		this.parent.notifyDocumentTextModified();
		this.refreshDisplay();
	}
	
	//	constants, registers and methods for REDO-management
	private static final String ACTION_PREFIX = "ACTION";
	private static final String RENAME_ANNOTATION_ACTION = "RENAME_ANNOTATION";
	private static final String REMOVE_ANNOTATION_ACTION = "REMOVE_ANNOTATION";
	private static final String DELETE_ANNOTATION_ACTION = "DELETE_ANNOTATION";
	private static final String RENAME_ATTRIBUTE_ACTION = "RENAME_ATTRIBUTE";
	private static final String REMOVE_ATTRIBUTE_ACTION = "REMOVE_ATTRIBUTE";
	private static final int MAX_ACTIONS = 10;
	private static Vector actions = new Vector();
	
	private static final String ANNOTATION_HISTORY_PREFIX = "ANNOTATION_TYPE";
	private static StringVector annotationTypeHistory = new StringVector();
	
	private void layoutActions() {
		this.actionPanel.removeAll();
		this.actionPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets.top = 2;
		gbc.insets.bottom = 2;
		gbc.insets.left = 3;
		gbc.insets.right = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		this.actionPanel.add(this.actionLabel, gbc.clone());
		
		AnnotationAction[] actions = getActions();
		for (int a = 0; a < actions.length; a++) {
			ActionButton ab = this.getActionButton(actions[a]);
			gbc.gridy ++;
			this.actionPanel.add(ab, gbc.clone());
		}
		
		gbc.gridy ++;
		gbc.weighty = 1;
		this.actionPanel.add(new JPanel(), gbc.clone());
		
		this.actionsModified = false;
		this.validate();
	}
	
	private ActionButton getActionButton(AnnotationAction action) {
		if (!this.actionButtons.containsKey(action)) {
			ActionButton ab = new ActionButton(this, action);
			this.actionButtons.put(action, ab);
		}
		return ((ActionButton) this.actionButtons.get(action));
	}
	
	/**
	 * button for recent actions
	 * 
	 * @author sautter
	 */
	private class ActionButton extends JButton {
		ActionButton(final AnnotationEditorPanel parent, final AnnotationAction action) {
			this.setText(action.label);
			this.setToolTipText(action.toolTip);
			this.setBorder(BorderFactory.createRaisedBevelBorder());
			this.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					parent.executeAction(action);
				}
			});
		}
	}
	
	JPanel getActionPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(this.actionPanelBox, BorderLayout.CENTER);
		return panel;
	}
	
	private void layoutSelectionActions() {
		
		ArrayList annotateActions = new ArrayList();
		ArrayList annotateAllActions = new ArrayList();
		
		for (int a = 0; a < annotationTypeHistory.size(); a++) {
			JMenuItem mi = this.getActionMenuItem(annotationTypeHistory.get(a), false);
			if (mi != null) annotateActions.add(mi);
			mi = this.getActionMenuItem(annotationTypeHistory.get(a), true);
			if (mi != null) annotateAllActions.add(mi);
		}
		
		if (annotateActions.isEmpty()) {
			this.annotateMenuItem = new JMenuItem("Annotate");
			this.annotateMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					annotate();
				}
			});
		} else {
			this.annotateMenuItem = new JMenu("Annotate ...");
			JMenuItem mi = new JMenuItem("Annotate");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					annotate();
				}
			});
			this.annotateMenuItem.add(mi);
			for (int a = 0; a < annotateActions.size(); a++)
				this.annotateMenuItem.add((JMenuItem) annotateActions.get(a));
		}
		
		if (annotateAllActions.isEmpty()) {
			this.annotateAllMenuItem = new JMenuItem("Annotate All");
			this.annotateAllMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					annotateAll();
				}
			});
		} else {
			this.annotateAllMenuItem = new JMenu("Annotate All ...");
			JMenuItem mi = new JMenuItem("Annotate All");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					annotateAll();
				}
			});
			this.annotateAllMenuItem.add(mi);
			for (int a = 0; a < annotateAllActions.size(); a++)
				this.annotateAllMenuItem.add((JMenuItem) annotateAllActions.get(a));
		}
	}
	
	private JMenuItem getActionMenuItem(final String type, boolean annotateAll) {
		String actionKey = ((annotateAll ? "AA-" : "A-") + type);
		if (!this.actionMenuItems.containsKey(actionKey)) {
			if (annotateAll) {
				JMenuItem mi = new JMenuItem("    - " + type);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Annotate All '" + type + "'");
						annotateAll(type, false);
					}
				});
				this.actionMenuItems.put(actionKey, mi);
				
			} else {
				JMenuItem mi = new JMenuItem("    - " + type);
				mi.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						parent.writeLog("Context Menu --> Annotate '" + type + "'");
						annotate(type, false);
					}
				});
				this.actionMenuItems.put(actionKey, mi);
			}
		}
		return ((JMenuItem) this.actionMenuItems.get(actionKey));
	}
	
	private void executeAction(AnnotationAction action) {
		AnnotationFilter filter = acceptAllFilter;
		if ("AdHocGPath".equals(action.filterProviderName) && (action.filterName != null)) {
			filter = new GPathAnnotationFilter(action.filterName);
		} else if (action.filterProviderName != null) {
			AnnotationFilterManager afm = this.host.getAnnotationFilterProvider(action.filterProviderName);
			if (afm != null) {
				AnnotationFilter af = afm.getAnnotationFilter(action.filterName);
				if (af != null) filter = af;
			}
		} else if (action.filterName != null)
			filter = new AnnotationTypeFilter(action.filterName);
		
		if (RENAME_ANNOTATION_ACTION.equals(action.actionName)) {
			if (action.newType != null) {
				this.parent.writeLog("Latest Actions --> Rename Annotations");
				this.renameAnnotations(filter, action.newType);
			}
			
		} else if (REMOVE_ANNOTATION_ACTION.equals(action.actionName)) {
			this.parent.writeLog("Latest Actions --> Remove Annotations");
			this.removeAnnotations(filter);
			
		} else if (DELETE_ANNOTATION_ACTION.equals(action.actionName)) {
			this.parent.writeLog("Latest Actions --> Delete Annotations");
			this.deleteAnnotations(filter);
			
		} else if (RENAME_ATTRIBUTE_ACTION.equals(action.actionName)) {
			if ((action.attribute != null) && (action.newAttribute != null)) {
				this.parent.writeLog("Latest Actions --> Rename Annotation Attribute");
				this.renameAnnotationAttribute(filter, action.attribute, action.newAttribute);
			}
			
		} else if (REMOVE_ATTRIBUTE_ACTION.equals(action.actionName)) {
			this.parent.writeLog("Latest Actions --> Remove Annotation Attribute");
			this.removeAnnotationAttribute(filter, action.attribute);
		}
	}
	
	private static boolean rememberAction(AnnotationAction aa) {
		//	if action is top action, there's nothing to do
		if ((aa != null) && (actions.indexOf(aa) != 0)) {
			actions.remove(aa);
			actions.insertElementAt(aa, 0);
			if (actions.size() > MAX_ACTIONS)
				actions.removeElement(actions.lastElement());
			notifyActionsModified();
			return true;
		} else return false;
	}
	
	private static boolean rememberAnnotationType(String type) {
		if (annotationTypeHistory.indexOf(type) == 0) return false;
		
		annotationTypeHistory.removeAll(type);
		annotationTypeHistory.insertElementAt(type, 0);
		while (annotationTypeHistory.size() > MAX_ACTIONS)
			annotationTypeHistory.removeAll(annotationTypeHistory.lastElement());
		return true;
	}
	
	private static Vector instances = new Vector();
	
	private static void notifyActionsModified() {
		for (int i = 0; i < instances.size(); i++)
			((AnnotationEditorPanel) instances.get(i)).actionsModified = true;
	}
	
	/**	notify all instances of AnnotationEditorPanel that the CustomFunctions have been modified
	 */
	static void notifyCustomFunctionsModified() {
		for (int i = 0; i < instances.size(); i++)
			((AnnotationEditorPanel) instances.get(i)).customFunctionsModified = true;
	}
	
	private static AnnotationAction[] getActions() {
		return ((AnnotationAction[]) actions.toArray(new AnnotationAction[actions.size()]));
	}
	
	//	methods for customFunction management
	private void layoutCustomFunctions() {
		this.customFunctionPanel.removeAll();
		this.customFunctionPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets.top = 2;
		gbc.insets.bottom = 2;
		gbc.insets.left = 3;
		gbc.insets.right = 3;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		this.customFunctionPanel.add(this.customFunctionLabel, gbc.clone());
		
		CustomFunction[] customFunctions = this.host.getCustomFunctions();
		for (int c = 0; c < customFunctions.length; c++) {
			final CustomFunction customFunction = customFunctions[c];
			JButton cfb = new JButton(customFunction.label);
			cfb.setToolTipText(customFunction.toolTip);
			cfb.setBorder(BorderFactory.createRaisedBevelBorder());
			cfb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					executeCustomFunction(customFunction, null);
				}
			});
			
			gbc.gridy ++;
			this.customFunctionPanel.add(cfb, gbc.clone());
		}
		
		gbc.gridy ++;
		gbc.weighty = 1;
		this.customFunctionPanel.add(new JPanel(), gbc.clone());
		this.customFunctionsModified = false;
		this.validate();
	}
	
	private void executeCustomFunction(CustomFunction customFunction, String annotationId) {
		if (annotationId == null) {
			String pe = customFunction.getPrecludingError(this.content);
			if (pe != null) {
				boolean isWarning = false;
				if (pe.startsWith("W:")) {
					pe = pe.substring("W:".length());
					isWarning = true;
				}
				int choice = JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), ("The document does not seem to be fit for " + customFunction.label + ":\n" + pe + "\n\nExecuting " + customFunction.label + " anyway might produce undesired results. Proceed?"), ("Document not Fit for '" + customFunction.label + "'"), JOptionPane.YES_NO_OPTION, (isWarning ? JOptionPane.WARNING_MESSAGE : JOptionPane.ERROR_MESSAGE));
				if (choice != JOptionPane.YES_OPTION)
					return;
			}
		}
		DocumentProcessor dp = customFunction.getDocumentProcessor();
		if (dp != null) {
			this.parent.writeLog("Custom Functions --> " + customFunction.label);					
			this.parent.applyDocumentProcessor(dp.getProviderClassName(), dp.getName(), annotationId, customFunction.label);
		}
	}
	
	private void executeCustomShortcut(final CustomShortcut customShortcut, final boolean isShiftDown) {
		final DocumentProcessor dp = customShortcut.getDocumentProcessor();
		
		if (customShortcut.annotationType != null) {
			this.parent.writeLog("Custom Shortcuts --> Annotate " + customShortcut.annotationType);
			
			//	create annotations only
			if (dp == null) {
				if (isShiftDown)
					this.annotateAll(customShortcut.annotationType, false);
				else this.annotate(customShortcut.annotationType, false);
			}
			
			else {
				
				//	get annotated value
				Annotation preAnnotation = this.annotateSelection(customShortcut.annotationType, false);
				if (preAnnotation == null)
					return;
				
				//	create undo action
				this.parent.enqueueRestoreContentUndoAction(customShortcut.annotationType, preAnnotation.getStartIndex(), preAnnotation.size());
				this.parent.writeLog("Custom Shortcuts --> Run " + dp.getName());					
				
				final MutableAnnotation[] annotations;
				
				//	annotate all occurrences of value
				if (isShiftDown) {
					ArrayList preAnnotationList = new ArrayList();
					int startIndex = 0;
					while ((startIndex = TokenSequenceUtils.indexOf(this.content, preAnnotation, startIndex, annotateAllCaseSensitive)) != -1) {
						preAnnotationList.add(this.content.addAnnotation(preAnnotation.getType(), startIndex, preAnnotation.size()));
						startIndex += preAnnotation.size();
					}
					annotations = ((MutableAnnotation[]) preAnnotationList.toArray(new MutableAnnotation[preAnnotationList.size()]));
				}
				
				//	annotate selection only
				else {
					annotations = new MutableAnnotation[1];
					annotations[0] = this.content.addAnnotation(preAnnotation);
				}
				
				//	TODO_maybe_later run processor only on first annotation, and copy attributes to others if nothing but attributes changed (use annotation listener to determine the latter)
				
				//	build splash screen
				final ResourceSplashScreen rss = new ResourceSplashScreen((dp.getTypeLabel() + " Running ..."), ("Please wait while '" + dp.getTypeLabel() + ": " + dp.getName() + "' is processing the Document ..."));
				
				//	apply processor
				Thread thread = new Thread(new Runnable() {
					public void run() {
						System.out.print("DocumentEditor: waiting for slpash screen ...");
						while (!rss.getDialog().isVisible()) try {
							Thread.sleep(100);
						} catch (InterruptedException ie) {}
						System.out.println(" done");
						
						try {
							System.out.print("DocumentEditor: start applying processor (" + dp.getName() + ") ...");
							Properties parameters = new Properties();
							parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
							for (int a = 0; a < annotations.length; a++) {
								rss.setBaseProgress((100 * a) / annotations.length);
								rss.setMaxProgress((100 * (a + 1)) / annotations.length);
								if (dp instanceof MonitorableDocumentProcessor)
									((MonitorableDocumentProcessor) dp).process(annotations[a], parameters, rss);
								else dp.process(annotations[a], parameters);
							}
							System.out.println(" finished");
						}
						catch (Throwable t) {
							System.out.println(" processor produced exception");
							JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("Error running " + dp.getName() + ":\n" + t.getMessage()), "Error Running DocumentProcessor", JOptionPane.ERROR_MESSAGE);
						}
						finally {
							rss.setPauseResumeEnabled(false);
							rss.setAbortEnabled(false);
							System.out.print("DocumentEditor: storing undo action ...");
							parent.storeUndoAction();
							parent.notifyDocumentMarkupModified();
							parent.notifyDocumentTextModified();
							System.out.println(" done");
							try {
								System.out.print("DocumentEditor: triggering refresh ...");
								refreshDisplay();
								System.out.println(" done");
							}
							finally {
								System.out.print("DocumentEditor: disposing slpash screen ...");
								rss.dispose();
								System.out.println(" done");
							}
						}
					}
				});
				
				//	show splash screen and start processing
				rss.popUp();
				thread.start();
			}
		}
		
		else if (dp != null) {
			this.parent.writeLog("Custom Shortcuts --> Run " + dp.getName());					
			this.parent.applyDocumentProcessor(dp.getProviderClassName(), dp.getName());
		}
	}
	
	static void init(Settings settings) {
		actions.clear();
		annotationTypeHistory.clear();
		
		for (int a = 0; a < (settings.size() / 3); a++) {
			Settings actionSettings = settings.getSubset(ACTION_PREFIX + a);
			AnnotationAction aa = AnnotationAction.getAction(actionSettings);
			if (aa != null) actions.add(aa);
		}
		
		for (int a = 0; a < settings.size(); a++)
			if (settings.containsKey(ANNOTATION_HISTORY_PREFIX + a))
				annotationTypeHistory.addElementIgnoreDuplicates(settings.getSetting(ANNOTATION_HISTORY_PREFIX + a));
		
		highlightNewAnnotations = HIGHLIGHT_NEW_ANNOTATIONS.equals(settings.getSetting(HIGHLIGHT_NEW_ANNOTATIONS, ""));
		tagNewAnnotations = TAG_NEW_ANNOTATIONS.equals(settings.getSetting(TAG_NEW_ANNOTATIONS, ""));
		highlightNewDocParts = HIGHLIGHT_NEW_DOCPARTS.equals(settings.getSetting(HIGHLIGHT_NEW_DOCPARTS, ""));
		tagNewDocParts = TAG_NEW_DOCPARTS.equals(settings.getSetting(TAG_NEW_DOCPARTS, ""));
		
		StringVector structuralTypeStringParser = new StringVector();
		structuralTypeStringParser.parseAndAddElements(settings.getSetting(DOCPART_TYPES, ""), " ");
		structuralTypeStringParser.removeAll("");
		setStructuralTypes(structuralTypeStringParser.toStringArray());
		
		annotateAllCaseSensitive = ANNOTATE_ALL_CASE_SENSITIVE.equals(settings.getSetting(ANNOTATE_ALL_CASE_SENSITIVE, ""));
		showAdvancedFilters = SHOW_ADVANCED_FILTERS.equals(settings.getSetting(SHOW_ADVANCED_FILTERS, ""));
		showRecentActions = SHOW_RECENT_ACTIONS.equals(settings.getSetting(SHOW_RECENT_ACTIONS, ""));
		showCustomFunctions = SHOW_CUSTOM_FUNCTIONS.equals(settings.getSetting(SHOW_CUSTOM_FUNCTIONS, ""));
		showDisplayControl = SHOW_DISPLAY_CONTROL.equals(settings.getSetting(SHOW_DISPLAY_CONTROL, ""));
		showFindReplace = SHOW_FIND_REPLACE.equals(settings.getSetting(SHOW_FIND_REPLACE, ""));
		
		initAnnotationSuggestions(settings.getSubset(ANNOTATION_SUGGESTION_SETTINGS_PREFIX));
		initDefaultAnnotationColors(settings.getSubset(ANNOTATION_COLOR_SETTINGS_PREFIX));
	}
	
	static void storeSettings(Settings settings) {
		settings.clear();
		
		for (int a = 0; a < actions.size(); a++) {
			Settings actionSettings = settings.getSubset(ACTION_PREFIX + a);
			((AnnotationAction) actions.get(a)).writeSettings(actionSettings);
		}
		
		for (int a = 0; a < annotationTypeHistory.size(); a++)
			settings.setSetting((ANNOTATION_HISTORY_PREFIX + a), annotationTypeHistory.get(a));
		
		if (highlightNewAnnotations) settings.setSetting(HIGHLIGHT_NEW_ANNOTATIONS, HIGHLIGHT_NEW_ANNOTATIONS);
		if (tagNewAnnotations) settings.setSetting(TAG_NEW_ANNOTATIONS, TAG_NEW_ANNOTATIONS);
		if (highlightNewDocParts) settings.setSetting(HIGHLIGHT_NEW_DOCPARTS, HIGHLIGHT_NEW_DOCPARTS);
		if (tagNewDocParts) settings.setSetting(TAG_NEW_DOCPARTS, TAG_NEW_DOCPARTS);
		
		StringVector structuralTypeStringBuilder = new StringVector();
		structuralTypeStringBuilder.addContentIgnoreDuplicates(getStructuralTypes());
		structuralTypeStringBuilder.sortLexicographically(false, false);
		settings.setSetting(DOCPART_TYPES, structuralTypeStringBuilder.concatStrings(" "));
		
		if (annotateAllCaseSensitive) settings.setSetting(ANNOTATE_ALL_CASE_SENSITIVE, ANNOTATE_ALL_CASE_SENSITIVE);
		if (showAdvancedFilters) settings.setSetting(SHOW_ADVANCED_FILTERS, SHOW_ADVANCED_FILTERS);
		if (showRecentActions) settings.setSetting(SHOW_RECENT_ACTIONS, SHOW_RECENT_ACTIONS);
		if (showCustomFunctions) settings.setSetting(SHOW_CUSTOM_FUNCTIONS, SHOW_CUSTOM_FUNCTIONS);
		if (showDisplayControl) settings.setSetting(SHOW_DISPLAY_CONTROL, SHOW_DISPLAY_CONTROL);
		if (showFindReplace) settings.setSetting(SHOW_FIND_REPLACE, SHOW_FIND_REPLACE);
		
		storeAnnotationSuggestions(settings.getSubset(ANNOTATION_SUGGESTION_SETTINGS_PREFIX));
		storeDefaultAnnotationColors(settings.getSubset(ANNOTATION_COLOR_SETTINGS_PREFIX));
	}
	
	/** get help
	 * @param	dataBaseUrl	the path where external help files are located
	 * @return a hierarchy of HelpChapters explaining the functionality of an AnnotationEditor
	 */
	static HelpChapter getHelp(String dataBaseUrl) {
		HelpChapter help = new DynamicHelpChapter("Annotation Editor", (dataBaseUrl + "AnnotationEditor.html"));
		//	TODO: explain more
		return help;
	}
	
	private static class AnnotationAction {
		
		private static final String ACTION_NAME = "ACTION";
		private static final String ACTION_LABEL = "LABEL";
		private static final String ACTION_TOOLTIP = "TOOLTIP";
		private static final String FILTER_NAME = "FILTER";
		private static final String FILTER_PROVIDER_NAME = "FILTER_PROVIDER";
		private static final String NEW_ANNOTATION_TYPE = "NEW_TYPE";
		private static final String ATTRIBUTE_NAME = "ATTRIBUTE";
		private static final String NEW_ATTRIBUTE_NAME = "NEW_ATTRIBUTE";
		
		final String actionName;
		final String label;
		final String toolTip;
		final String filterName;
		final String filterProviderName;
		final String newType;
		final String attribute;
		final String newAttribute;
		
		AnnotationAction(String actionName, String label, String toolTip, String filterName, String filterProviderName, String newType, String attribute, String newAttribute) {
			this.actionName = actionName;
			this.label = label;
			this.toolTip = toolTip;
			this.filterName = filterName;
			this.filterProviderName = filterProviderName;
			this.newType = newType;
			this.attribute = attribute;
			this.newAttribute = newAttribute;
		}
		
		private AnnotationAction(Settings set) {
			this.actionName = set.getSetting(ACTION_NAME);
			this.label = set.getSetting(ACTION_LABEL);
			this.toolTip = set.getSetting(ACTION_TOOLTIP);
			this.filterName = set.getSetting(FILTER_NAME);
			this.filterProviderName = set.getSetting(FILTER_PROVIDER_NAME);
			this.newType = set.getSetting(NEW_ANNOTATION_TYPE);
			this.attribute = set.getSetting(ATTRIBUTE_NAME);
			this.newAttribute = set.getSetting(NEW_ATTRIBUTE_NAME);
		}
		
//		Settings getSettings() {
//			Settings set = new Settings();
//			this.writeSettings(set);
//			return set;
//		}
//		
		void writeSettings(Settings set) {
			set.setSetting(ACTION_NAME, this.actionName);
			set.setSetting(ACTION_LABEL, this.label);
			set.setSetting(ACTION_TOOLTIP, this.toolTip);
			if (this.filterName != null) set.setSetting(FILTER_NAME, this.filterName);
			if (this.filterProviderName != null) set.setSetting(FILTER_PROVIDER_NAME, this.filterProviderName);
			if (this.newType != null) set.setSetting(NEW_ANNOTATION_TYPE, this.newType);
			if (this.attribute != null) set.setSetting(ATTRIBUTE_NAME, this.attribute);
			if (this.newAttribute != null) set.setSetting(NEW_ATTRIBUTE_NAME, this.newAttribute);
		}
		
		public String toString() {
			StringBuffer sb = new StringBuffer("AnnotationAction[");
			sb.append(ACTION_NAME + "=" + this.actionName);
			sb.append(";" + ACTION_LABEL + "=" + this.label);
			sb.append(";" + ACTION_TOOLTIP + "=" + this.toolTip);
			if (this.filterName != null) sb.append(";" + FILTER_NAME + "=" + this.filterName);
			if (this.filterProviderName != null) sb.append(";" + FILTER_PROVIDER_NAME + "=" + this.filterProviderName);
			if (this.newType != null) sb.append(";" + NEW_ANNOTATION_TYPE + "=" + this.newType);
			if (this.attribute != null) sb.append(";" + ATTRIBUTE_NAME + "=" + this.attribute);
			if (this.newAttribute != null) sb.append(";" + NEW_ATTRIBUTE_NAME + "=" + this.newAttribute);
			sb.append("]");
			return sb.toString();
		}
		
		public boolean equals(Object o) {
			if ((o == null) || !(o instanceof AnnotationAction)) return false;
			AnnotationAction aa = ((AnnotationAction) o);
			if (aa == this) return true;
			return (this.toString().equals(aa.toString()));
		}
		
		static AnnotationAction getAction(Settings set) {
			if ((set == null) || set.isEmpty()) return null;
			AnnotationAction aa = new AnnotationAction(set);
			if (aa.actionName == null) return null;
			if (aa.label == null) return null;
			if (aa.toolTip == null) return null;
			return aa;
		}
	}
}
