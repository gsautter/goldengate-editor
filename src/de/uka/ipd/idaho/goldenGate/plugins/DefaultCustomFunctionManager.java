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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.util.gPath.GPath;
import de.uka.ipd.idaho.gamta.util.gPath.GPathExpression;
import de.uka.ipd.idaho.gamta.util.gPath.GPathParser;
import de.uka.ipd.idaho.gamta.util.gPath.types.GPathObject;
import de.uka.ipd.idaho.gamta.util.swing.AnnotationDisplayDialog;
import de.uka.ipd.idaho.goldenGate.CustomFunction;
import de.uka.ipd.idaho.goldenGate.util.DataListListener;
import de.uka.ipd.idaho.goldenGate.util.DataListPanel;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.ResourceDialog;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Default implementation of the CustomFunction.Manager interface
 * 
 * @author sautter
 */
public class DefaultCustomFunctionManager extends AbstractResourceManager implements CustomFunction.Manager {
	
	private static final String LABEL_ATTRIBUTE = "LABEL";
	private static final String TOOLTIP_ATTRIBUTE = "TOOLTIP";
	
	private static final String PROCESSOR_NAME_ATTRIBUTE = "PROCESSOR_NAME";
	private static final String PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE = "PROCESSOR_PROVIDER_CLASS";
	
	private static final String LOCATION_ATTRIBUTE = "LOCATION";
	private static final String PANEL_LOCATION = "Panel";
	private static final String CONTEXT_MENU_LOCATION = "Context Menu";
	private static final String BOTH_LOCATION = "Panel & Context Menu";
	private static final String[] LOCATIONS = {
		PANEL_LOCATION,
		CONTEXT_MENU_LOCATION,
		BOTH_LOCATION,
	};
	
	private static final String FILTER_ATTRIBUTE = "FILTER";
	
	private static final String PRECLUSION_ATTRIBUTE_PREFIX = "PRECLUSION";
	private static final String PRECLUSION_FILTER_ATTRIBUTE_SUFFIX = "FILTER";
	private static final String PRECLUSION_MESSAGE_ATTRIBUTE_SUFFIX = "MESSAGE";
	
	private static final String FILE_EXTENSION = ".customFunction";
	
	public DefaultCustomFunctionManager() {}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getDataNamesForResource(java.lang.String)
	 */
	public String[] getDataNamesForResource(String name) {
		StringVector nameCollector = new StringVector();
		nameCollector.addElementIgnoreDuplicates(name + "@" + this.getClass().getName());
		
		Settings settings = this.loadSettingsResource(name);
		String processorName = settings.getSetting(PROCESSOR_NAME_ATTRIBUTE);
		String processorProviderClassName = settings.getSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE);
		if (processorName.indexOf('@') != -1) {
			if (processorProviderClassName == null)
				processorProviderClassName = processorName.substring(processorName.indexOf('@') + 1);
			processorName = processorName.substring(0, processorName.indexOf('@'));
		}
		
		ResourceManager rm = this.parent.getResourceProvider(processorProviderClassName);
		if (rm != null)
			nameCollector.addContentIgnoreDuplicates(rm.getDataNamesForResource(processorName));
		
		return nameCollector.toStringArray();
	}

	/** @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getRequiredResourceNames(java.lang.String, boolean)
	 */
	public String[] getRequiredResourceNames(String name, boolean recourse) {
		StringVector nameCollector = new StringVector();
		Settings settings = this.loadSettingsResource(name);
		
		String processorName = settings.getSetting(PROCESSOR_NAME_ATTRIBUTE);
		if (processorName != null) {
			String processorProviderClassName = settings.getSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE);
			if (processorName.indexOf('@') != -1) {
				if (processorProviderClassName == null)
					processorProviderClassName = processorName.substring(processorName.indexOf('@') + 1);
				processorName = processorName.substring(0, processorName.indexOf('@'));
			}
			nameCollector.addElement(processorName + "@" + processorProviderClassName);
		}
		
		int nameIndex = 0;
		while (recourse && (nameIndex < nameCollector.size())) {
			String resName = nameCollector.get(nameIndex);
			int split = resName.indexOf('@');
			if (split != -1) {
				String plainResName = resName.substring(0, split);
				String resProviderClassName = resName.substring(split + 1);
				
				ResourceManager rm = this.parent.getResourceProvider(resProviderClassName);
				if (rm != null)
					nameCollector.addContentIgnoreDuplicates(rm.getRequiredResourceNames(plainResName, recourse));
			}
			nameIndex++;
		}
		
		return nameCollector.toStringArray();
	}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getResourceTypeLabel()
	 */
	public String getResourceTypeLabel() {
		return "Custom Function";
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getFileExtension()
	 */
	protected String getFileExtension() {
		return FILE_EXTENSION;
	}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getSettingsPanel()
	 */
	public SettingsPanel getSettingsPanel() {
		if (this.settingsPanel == null)
			this.settingsPanel = new CustomFunctionSettingsPanel();
		return this.settingsPanel;
	}
	
	private SettingsPanel settingsPanel = null;
	private class CustomFunctionSettingsPanel extends SettingsPanel implements DataListListener {
		
		private DataListPanel resourceNameList;
		
		private CustomFunctionEditorPanel editor;
		
		private JPanel editButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		CustomFunctionSettingsPanel() {
			super("Custom Functions", "Configure custom functions for one click access to frequently used operations.");
			this.setLayout(new BorderLayout());
			this.setDoubleBuffered(true);
			
			this.resourceNameList = DefaultCustomFunctionManager.this.resourceNameList;
			
			JButton button;
			
			button = new JButton("Create");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (createCustomFunction())
						resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			button = new JButton("Clone");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cloneCustomFunction())
						resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			button = new JButton("Delete");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (deleteCustomFunction()) resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			
			this.add(this.editButtons, BorderLayout.NORTH);
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.resourceNameList.addDataListListener(this);
		}
		
		/** @see de.goldenGate.util.DataListListener#selected(java.lang.String)
		 */
		public void selected(String dataName) {
			if ((this.editor != null) && this.editor.isDirty()) {
				try {
					storeSettingsResource(this.editor.name, this.editor.getSettings());
					parent.notifyResourcesChanged(this.getClass().getName());
					CustomFunction.notifyModified();
				}
				catch (IOException ioe) {
					if (JOptionPane.showConfirmDialog(this, (ioe.getClass().getName() + " (" + ioe.getMessage() + ")\nwhile saving file to " + this.editor.name + "\nProceed?"), "Could Not Save CustomFunction", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
						this.resourceNameList.setSelectedName(this.editor.name);
						this.validate();
						return;
					}
				}
			}
			
			this.removeAll();
			if (dataName == null)
				this.add(getExplanationLabel(), BorderLayout.CENTER);
			
			else {
				Settings set = loadSettingsResource(dataName);
				if (set == null)
					this.add(getExplanationLabel(), BorderLayout.CENTER);
				
				else {
					this.editor = new CustomFunctionEditorPanel(dataName, getCustomFunction(set));
					this.add(this.editor, BorderLayout.CENTER);
				}
			}
			this.add(this.editButtons, BorderLayout.NORTH);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.validate();
		}
		
		private boolean createCustomFunction() {
			return this.createCustomFunction(new Settings(), null);
		}
		
		private boolean cloneCustomFunction() {
			String selectedName = this.resourceNameList.getSelectedName();
			if (selectedName == null)
				return this.createCustomFunction();
			else {
				String name = "New " + selectedName;
				return this.createCustomFunction(this.editor.getSettings(), name);
			}
		}
		
		private boolean createCustomFunction(Settings modelCustomFunction, String name) {
			CreateCustomFunctionDialog cpd = new CreateCustomFunctionDialog(name, getCustomFunction(modelCustomFunction));
			cpd.setVisible(true);
			
			if (cpd.isCommitted()) {
				Settings customFunction = cpd.getCustomFunction();
				String customFunctionName = cpd.getCustomFunctionName();
				if (!customFunctionName.endsWith(FILE_EXTENSION)) customFunctionName += FILE_EXTENSION;
				try {
					if (storeSettingsResource(customFunctionName, customFunction)) {
						parent.notifyResourcesChanged(this.getClass().getName());
						CustomFunction.notifyModified();
						return true;
					}
				} catch (IOException e) {}
			}
			return false;
		}
		
		private boolean deleteCustomFunction() {
			String name = this.resourceNameList.getSelectedName();
			if ((name != null) && (JOptionPane.showConfirmDialog(this, ("Really delete " + name), "Confirm Delete CustomFunction", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
				try {
					if (dataProvider.deleteData(name)) {
						this.resourceNameList.refresh();
						parent.notifyResourcesChanged(this.getClass().getName());
						CustomFunction.notifyModified();
						return true;
					} else {
						JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				} catch (Exception ioe) {
					JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			} else return false;
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			if ((this.editor != null) && this.editor.isDirty()) try {
				storeSettingsResource(this.editor.name, this.editor.getSettings());
				parent.notifyResourcesChanged(this.getClass().getName());
				CustomFunction.notifyModified();
			}
			catch (IOException ioe) {}
		}
	}
	
	/**	retrieve a customFunction by its name
	 * @param	name	the name of the reqired customFunction
	 * @return the customFunction with the required name, or null, if there is no such customFunction
	 */
	public CustomFunction getCustomFunction(String name) {
		if (name == null)
			return null;
		else return this.getCustomFunction(this.loadSettingsResource(name));
	}
	
	private CustomFunction getCustomFunction(Settings settings) {
		if (settings == null)
			return null;
		try {
			String label = settings.getSetting(LABEL_ATTRIBUTE, "");
			String toolTip = settings.getSetting(TOOLTIP_ATTRIBUTE, "");
			
			String processorName = settings.getSetting(PROCESSOR_NAME_ATTRIBUTE);
			String processorProviderClassName = settings.getSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE);
			if (processorName.indexOf('@') != -1) {
				if (processorProviderClassName == null)
					processorProviderClassName = processorName.substring(processorName.indexOf('@') + 1);
				processorName = processorName.substring(0, processorName.indexOf('@'));
			}
			
			String location = settings.getSetting(LOCATION_ATTRIBUTE, PANEL_LOCATION); // use panel-only as default
			
			DocumentProcessorManager dpm = this.parent.getDocumentProcessorProvider(processorProviderClassName);
			if (dpm == null)
				return null;
			
			LinkedHashMap preclusions = new LinkedHashMap();
			if (location.indexOf(PANEL_LOCATION) != -1)
				for (int f = 0; f < settings.size(); f++) {
					String filter = settings.getSetting(PRECLUSION_ATTRIBUTE_PREFIX + f + PRECLUSION_FILTER_ATTRIBUTE_SUFFIX);
					String message = settings.getSetting(PRECLUSION_ATTRIBUTE_PREFIX + f + PRECLUSION_MESSAGE_ATTRIBUTE_SUFFIX);
					if ((filter != null) && (message != null))
						preclusions.put(filter, message);
				}
			
			ArrayList filters = new ArrayList();
			if (location.indexOf(CONTEXT_MENU_LOCATION) != -1)
				for (int f = 0; f < settings.size(); f++) {
					String filter = settings.getSetting(FILTER_ATTRIBUTE + f);
					if (filter != null)
						filters.add(filter);
				}
			
			return new CustomFunction(label, toolTip, dpm, processorName, ((location.indexOf(PANEL_LOCATION) == -1) ? null : preclusions), (filters.isEmpty() ? ((location.indexOf(CONTEXT_MENU_LOCATION) == -1) ? new String[0] : null) : ((String[]) filters.toArray(new String[filters.size()]))));
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private class CreateCustomFunctionDialog extends DialogPanel {
		
		private JTextField nameField;
		
		private CustomFunctionEditorPanel editor;
		private String customFunctionName = null;
		
		public CreateCustomFunctionDialog(String name, CustomFunction customFunction) {
			super("Create CustomFunction", true);
			
			this.nameField = new JTextField((name == null) ? "New CustomFunction" : name);
			this.nameField.setBorder(BorderFactory.createLoweredBevelBorder());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Create");
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
			
			//	initialize editor
			this.editor = new CustomFunctionEditorPanel(name, customFunction);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.nameField, BorderLayout.NORTH);
			this.getContentPane().add(this.editor, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(600, 400));
			this.setLocationRelativeTo(this.getOwner());
		}
		
		boolean isCommitted() {
			return (this.customFunctionName != null);
		}
		
		Settings getCustomFunction() {
			return this.editor.getSettings();
		}
		
		String getCustomFunctionName() {
			return this.customFunctionName;
		}
		
		private void abort() {
			this.customFunctionName = null;
			this.dispose();
		}

		private void commit() {
			this.customFunctionName = this.nameField.getText();
			this.dispose();
		}
	}

	private class CustomFunctionEditorPanel extends JPanel implements DocumentListener, ItemListener {
		
		private String name;
		
		private boolean dirty = false;
		
		private JTextField label = new JTextField();
		private JTextField toolTip = new JTextField();
		private JComboBox location = new JComboBox(LOCATIONS);
		
		private LinkedHashMap preclusions = new LinkedHashMap();
		private String[] filters = new String[0];
//		private JButton editFilters = new JButton("GPath Filters");
		private JButton editPreclusionsAndFilters = new JButton("Preclusions (0) & Filters (0)");
		
		private DocumentProcessorManager processorProvider;
		private String processorName;
		private JLabel processorLabel = new JLabel("", JLabel.LEFT);
		private String processorTypeLabel = null;
		
		CustomFunctionEditorPanel(String name, CustomFunction customFunction) {
			super(new BorderLayout(), true);
			this.name = name;
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			
			this.location.setEditable(false);
			this.location.setSelectedItem(PANEL_LOCATION);
//			this.location.addItemListener(new ItemListener() {
//				public void itemStateChanged(ItemEvent ie) {
//					String l = ((String) location.getSelectedItem());
//					editFilters.setEnabled(l.indexOf(CONTEXT_MENU_LOCATION) != -1);
//				}
//			});
			
//			this.editFilters.setBorder(BorderFactory.createRaisedBevelBorder());
//			this.editFilters.setEnabled(false);
//			this.editFilters.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent ae) {
//					editFilters();
//				}
//			});
			this.editPreclusionsAndFilters.setBorder(BorderFactory.createRaisedBevelBorder());
			this.editPreclusionsAndFilters.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editPreclusionsAndFilters();
				}
			});
			
			if (customFunction != null) {
				this.label.setText(customFunction.label);
				this.toolTip.setText(customFunction.toolTip);
				this.processorName = customFunction.getDocumentProcessorName();
				this.processorProvider = customFunction.getDocumentProcessorProvider();
				if (this.processorProvider != null)
					this.processorTypeLabel = this.processorProvider.getResourceTypeLabel();
				this.location.setSelectedItem(customFunction.usePanel ? (customFunction.useContextMenu ? BOTH_LOCATION : PANEL_LOCATION) : CONTEXT_MENU_LOCATION);
				LinkedHashMap preclusions = customFunction.getPanelPreclusions();
				if (preclusions != null)
					this.preclusions.putAll(preclusions);
				String[] filters = customFunction.getContextMenuFilters();
				if (filters != null)
					this.filters = filters;
//				this.editFilters.setText("GPath Filters (" + this.filters.length + ")");
				this.editPreclusionsAndFilters.setText("Preclusions (" + this.preclusions.size() + ") & Filters (" + this.filters.length + ")");
			}
			this.processorLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if ((me.getClickCount() > 1) && (processorName != null) && (processorProvider != null))
						processorProvider.editDocumentProcessor(processorName);
				}
			});
			
			this.label.getDocument().addDocumentListener(this);
			this.toolTip.getDocument().addDocumentListener(this);
			this.location.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					dirty = true;
				}
			});
			
			this.updateLabels();
			
			JPanel functionPanel = new JPanel(new GridBagLayout(), true);
			functionPanel.setBorder(BorderFactory.createEtchedBorder());
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.weighty = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridy = 0;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.weightx = 0;
			functionPanel.add(new JLabel("Custom Function Label", JLabel.LEFT), gbc.clone());
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			functionPanel.add(this.label, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			functionPanel.add(new JLabel("Custom Function Tooltip", JLabel.LEFT), gbc.clone());
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			functionPanel.add(this.toolTip, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			functionPanel.add(new JLabel("Use Custom Function in ...", JLabel.LEFT), gbc.clone());
			gbc.gridx = 1;
			functionPanel.add(this.location, gbc.clone());
			gbc.gridx = 2;
//			functionPanel.add(this.editFilters, gbc.clone());
			functionPanel.add(this.editPreclusionsAndFilters, gbc.clone());
			
			gbc.gridy ++;
			gbc.gridx = 0;
			gbc.gridwidth = 3;
			gbc.weightx = 3;
			gbc.weighty = 1;
			functionPanel.add(this.processorLabel, gbc.clone());
			
			DocumentProcessorManager[] dpms = parent.getDocumentProcessorProviders();
			for (int p = 0; p < dpms.length; p++) {
				final String className = dpms[p].getClass().getName();
				gbc.gridy++;
				
				JButton button = new JButton("Use " + dpms[p].getResourceTypeLabel());
				button.setBorder(BorderFactory.createRaisedBevelBorder());
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						selectProcessor(className);
					}
				});
				
				gbc.gridx = 0;
				gbc.weightx = 2;
				gbc.gridwidth = 2;
				functionPanel.add(button, gbc.clone());
				
				button = new JButton("Create " + dpms[p].getResourceTypeLabel());
				button.setBorder(BorderFactory.createRaisedBevelBorder());
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						createProcessor(className);
					}
				});
				
				gbc.gridx = 2;
				gbc.weightx = 0;
				gbc.gridwidth = 1;
				functionPanel.add(button, gbc.clone());
			}
			
			this.add(functionPanel, BorderLayout.SOUTH);
		}
		
		public void changedUpdate(DocumentEvent de) {}
		
		public void insertUpdate(DocumentEvent de) {
			this.dirty = true;
		}
		
		public void removeUpdate(DocumentEvent de) {
			this.dirty = true;
		}
		
		public void itemStateChanged(ItemEvent ie) {
			this.dirty = true;
		}
		
		boolean isDirty() {
			return this.dirty;
		}
		
		private void updateLabels() {
			if ((this.processorName != null) && (this.processorProvider != null)) {
				this.processorTypeLabel = this.processorProvider.getResourceTypeLabel();
				this.processorLabel.setText(this.processorTypeLabel + " '" + this.processorName + "' (double click to edit)");
			}
			else this.processorLabel.setText("<No DocumentProcessor selected yet>");
		}
		
		private void editPreclusionsAndFilters() {
			PreclusionFilterEditorDialog pfed = new PreclusionFilterEditorDialog(this.name, this.preclusions, this.filters);
			pfed.setVisible(true);
			if (pfed.isCommitted()) {
				LinkedHashMap preclusions = pfed.getPreclusions();
				if (preclusions != null) {
					this.preclusions.clear();
					this.preclusions.putAll(preclusions);
				}
				String[] filters = pfed.getFilters();
				if (filters != null)
					this.filters = filters;
				this.editPreclusionsAndFilters.setText("Preclusions (" + this.preclusions.size() + ") & Filters (" + this.filters.length + ")");
				this.dirty = true;
			}
		}
		
		private void selectProcessor(String providerClassName) {
			DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(providerClassName);
			if (dpm != null) {
				ResourceDialog rd = ResourceDialog.getResourceDialog(dpm, ("Select " + dpm.getResourceTypeLabel()), "Select");
				rd.setLocationRelativeTo(rd.getOwner());
				rd.setVisible(true);
				
				//	get processor
				String dpn = rd.getSelectedResourceName();
				if (dpn != null) {
					this.processorName = dpn;
					this.processorProvider = dpm;
					this.updateLabels();
					if (this.label.getText().trim().length() == 0)
						this.label.setText(rd.getSelectedResourceName());
					if (this.toolTip.getText().trim().length() == 0) {
						String tml = this.processorProvider.getToolsMenuLabel();
						if (tml == null) tml = "Apply";
						this.toolTip.setText(tml + " " + this.processorProvider.getResourceTypeLabel() + " '" + this.processorName + "'");
					}
					this.dirty = true;
				}
			}
		}
		
		private void createProcessor(String providerClassName) {
			DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(providerClassName);
			if (dpm != null) {
				String dpn = dpm.createDocumentProcessor();
				
				//	get processor
				if (dpn != null) {
					this.processorName = dpn;
					this.processorProvider = dpm;
					this.updateLabels();
					if (this.label.getText().trim().length() == 0)
						this.label.setText(dpn);
					if (this.toolTip.getText().trim().length() == 0) {
						String tml = this.processorProvider.getToolsMenuLabel();
						if (tml == null) tml = "Apply";
						this.toolTip.setText(tml + " " + this.processorProvider.getResourceTypeLabel() + " '" + this.processorName + "'");
					}
					this.dirty = true;
				}
			}
		}
		
		Settings getSettings() {
			Settings set = new Settings();
			
			String label = this.label.getText();
			if (label.trim().length() == 0) {
				if (this.processorName != null) label += ((label.length() == 0) ? "" : ", ") + this.processorName;
			}
			if (label.trim().length() != 0)
				set.setSetting(LABEL_ATTRIBUTE, label);
			
			String toolTip = this.toolTip.getText();
			if (toolTip.trim().length() == 0) {
				if (this.processorName != null)
					toolTip += ((toolTip.length() == 0) ? "Apply " : " and apply ") + (this.processorTypeLabel + " '" + this.processorName + "'");
			}
			if (toolTip.trim().length() != 0)
				set.setSetting(TOOLTIP_ATTRIBUTE, toolTip);
			
			if ((this.processorName != null) && (this.processorProvider != null)) {
				set.setSetting(PROCESSOR_NAME_ATTRIBUTE, (this.processorName + "@" + this.processorProvider.getClass().getName()));
//				set.setSetting(PROCESSOR_NAME_ATTRIBUTE, (this.processorName);
//				set.setSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE, this.processorProvider.getClass().getName());
			}
			
			set.setSetting(LOCATION_ATTRIBUTE, this.location.getSelectedItem().toString());
			
			int preclusionCount = 0;
			for (Iterator fit = this.preclusions.keySet().iterator(); fit.hasNext();) {
				String filter = ((String) fit.next());
				set.setSetting((PRECLUSION_ATTRIBUTE_PREFIX + preclusionCount + PRECLUSION_FILTER_ATTRIBUTE_SUFFIX), filter);
				set.setSetting((PRECLUSION_ATTRIBUTE_PREFIX + preclusionCount + PRECLUSION_MESSAGE_ATTRIBUTE_SUFFIX), ((String) this.preclusions.get(filter)));
				preclusionCount++;
			}
			
			for (int f = 0; f < this.filters.length; f++)
				set.setSetting((FILTER_ATTRIBUTE + f), this.filters[f]);
			
			return set;
		}
	}
	
//	private class FilterEditorDialog extends DialogPanel {
//		
//		private FilterEditorPanel editor;
//		private String[] filters = null;
//		
//		FilterEditorDialog(String name, String[] filters) {
//			super(("Edit Filters for Custom Function '" + name + "'"), true);
//			
//			//	initialize main buttons
//			JButton commitButton = new JButton("OK");
//			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			commitButton.setPreferredSize(new Dimension(100, 21));
//			commitButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					FilterEditorDialog.this.filters = editor.getFilters();
//					dispose();
//				}
//			});
//			
//			JButton abortButton = new JButton("Cancel");
//			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			abortButton.setPreferredSize(new Dimension(100, 21));
//			abortButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					FilterEditorDialog.this.filters = null;
//					dispose();
//				}
//			});
//			
//			JPanel mainButtonPanel = new JPanel();
//			mainButtonPanel.setLayout(new FlowLayout());
//			mainButtonPanel.add(commitButton);
//			mainButtonPanel.add(abortButton);
//			
//			//	initialize editor
//			this.editor = new FilterEditorPanel(filters);
//			
//			//	put the whole stuff together
//			this.setLayout(new BorderLayout());
//			this.add(this.editor, BorderLayout.CENTER);
//			this.add(mainButtonPanel, BorderLayout.SOUTH);
//			
//			this.setResizable(true);
//			this.setSize(new Dimension(600, 400));
//			this.setLocationRelativeTo(this.getOwner());
//		}
//		
//		boolean isCommitted() {
//			return (this.filters != null);
//		}
//		
//		String[] getFilters() {
//			return this.filters;
//		}
//	}
//	
	private class PreclusionFilterEditorDialog extends DialogPanel {
		
		private PreclusionEditorPanel preclusionEditor;
		private LinkedHashMap preclusions = null;
		private FilterEditorPanel filterEditor;
		private String[] filters = null;
		
		PreclusionFilterEditorDialog(String name, LinkedHashMap preclusions, String[] filters) {
			super(("Edit Preclusions & Filters for '" + name + "'"), true);
			
			//	initialize main buttons
			JButton commitButton = new JButton("OK");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PreclusionFilterEditorDialog.this.preclusions = preclusionEditor.getPreclusions();
					PreclusionFilterEditorDialog.this.filters = filterEditor.getFilters();
					dispose();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PreclusionFilterEditorDialog.this.preclusions = null;
					PreclusionFilterEditorDialog.this.filters = null;
					dispose();
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	initialize editors
			this.preclusionEditor = new PreclusionEditorPanel(preclusions);
			this.filterEditor = new FilterEditorPanel(filters);
			
			//	put editor in tabs
			JTabbedPane tabs = new JTabbedPane();
			tabs.addTab("Preclusions", this.preclusionEditor);
			tabs.addTab("Filters", this.filterEditor);
			
			//	put the whole stuff together
			this.setLayout(new BorderLayout());
			this.add(tabs, BorderLayout.CENTER);
			this.add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(600, 400));
			this.setLocationRelativeTo(this.getOwner());
		}
		
		boolean isCommitted() {
			return ((this.filters != null) && (this.preclusions != null));
		}
		
		LinkedHashMap getPreclusions() {
			return this.preclusions;
		}
		
		String[] getFilters() {
			return this.filters;
		}
	}
	
	private class PreclusionEditorPanel extends JPanel {

		private ArrayList preclusions = new ArrayList();
		private int selectedPreclusion = -1;
		
		private JLabel filterLabel = new JLabel("GPath Filter Expression", JLabel.CENTER);
		private JLabel messageLabel = new JLabel("Error Message to Show", JLabel.CENTER);
		private JPanel linePanelSpacer = new JPanel();
		private JPanel linePanel = new JPanel(new GridBagLayout());
		
		PreclusionEditorPanel(LinkedHashMap data) {
			super(new BorderLayout(), true);
			
			this.filterLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			this.filterLabel.setPreferredSize(new Dimension(160, 21));
			this.filterLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectPreclusion(-1);
					linePanel.requestFocusInWindow();
				}
			});
			
			this.messageLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			this.messageLabel.setPreferredSize(new Dimension(160, 21));
			this.messageLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectPreclusion(-1);
					linePanel.requestFocusInWindow();
				}
			});
			
			this.linePanelSpacer.setBackground(Color.WHITE);
			this.linePanelSpacer.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectPreclusion(-1);
					linePanel.requestFocusInWindow();
				}
			});
			this.linePanel.setBorder(BorderFactory.createLineBorder(this.getBackground(), 3));
			this.linePanel.setFocusable(true);
			
	        final String upKey = "GO_UP";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), upKey);
	        this.linePanel.getActionMap().put(upKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(upKey);
	                if (selectedPreclusion > 0)
	                	selectPreclusion(selectedPreclusion - 1);
	                else if (selectedPreclusion == -1)
                		selectPreclusion(preclusions.size() - 1);
	            }
	        });
	        final String moveUpKey = "MOVE_UP";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, InputEvent.CTRL_DOWN_MASK, true), moveUpKey);
	        this.linePanel.getActionMap().put(moveUpKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(moveUpKey);
					moveUp();
	            }
	        });
	        
	        final String downKey = "GO_DOWN";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), downKey);
	        this.linePanel.getActionMap().put(downKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(downKey);
                	if (selectedPreclusion == -1)
                		selectPreclusion(0);
                	else if ((selectedPreclusion + 1) < preclusions.size())
	                	selectPreclusion(selectedPreclusion + 1);
	            }
	        });
	        final String moveDownKey = "MOVE_DOWN";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, InputEvent.CTRL_DOWN_MASK, true), moveDownKey);
	        this.linePanel.getActionMap().put(moveDownKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(moveDownKey);
					moveDown();
	            }
	        });

			JScrollPane linePanelBox = new JScrollPane(this.linePanel);
			
			JButton addPreclusionButton = new JButton("Add Preclusion");
			addPreclusionButton.setBorder(BorderFactory.createRaisedBevelBorder());
			addPreclusionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addPreclusion();
				}
			});
			
			JButton testPreclusionButton = new JButton("Test Filter");
			testPreclusionButton.setBorder(BorderFactory.createRaisedBevelBorder());
			testPreclusionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					testFilter();
				}
			});
			
			JButton removePreclusionButton = new JButton("Remove Preclusion");
			removePreclusionButton.setBorder(BorderFactory.createRaisedBevelBorder());
			removePreclusionButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removePreclusion();
				}
			});
			
			JPanel buttonPanel = new JPanel(new GridBagLayout(), true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 3;
			gbc.insets.bottom = 3;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.weighty = 0;
			gbc.weightx = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 0;
			gbc.gridx = 0;
			buttonPanel.add(addPreclusionButton, gbc.clone());
			gbc.gridx = 1;
			buttonPanel.add(testPreclusionButton, gbc.clone());
			gbc.gridx = 2;
			buttonPanel.add(removePreclusionButton, gbc.clone());
			
			JButton upButton = new JButton("Up");
			upButton.setBorder(BorderFactory.createRaisedBevelBorder());
			upButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveUp();
				}
			});
			JButton downButton = new JButton("Down");
			downButton.setBorder(BorderFactory.createRaisedBevelBorder());
			downButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					moveDown();
				}
			});
			
			JPanel reorderButtonPanel = new JPanel(new GridBagLayout());
			gbc = new GridBagConstraints();
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridheight = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			
			gbc.gridy = 0;
			reorderButtonPanel.add(upButton, gbc.clone());
			gbc.gridy = 1;
			reorderButtonPanel.add(downButton, gbc.clone());
			
			this.add(reorderButtonPanel, BorderLayout.WEST);
			this.add(linePanelBox, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.SOUTH);
			
			this.setContent(data);
		}
		
		void layoutPreclusions() {
			this.linePanel.removeAll();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			gbc.weighty = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			gbc.gridx = 0;
			gbc.weightx = 1;
			this.linePanel.add(this.filterLabel, gbc.clone());
			gbc.gridx = 1;
			gbc.weightx = 0;
			this.linePanel.add(this.messageLabel, gbc.clone());
			gbc.gridy++;
			
			for (int l = 0; l < this.preclusions.size(); l++) {
				CustomFunctionPreclusion line = ((CustomFunctionPreclusion) this.preclusions.get(l));
				line.index = l;
				
				gbc.gridx = 0;
				gbc.weightx = 1;
				this.linePanel.add(line.filterPanel, gbc.clone());
				gbc.gridx = 1;
				gbc.weightx = 0;
				this.linePanel.add(line.messagePanel, gbc.clone());
				gbc.gridy++;
			}
			
			gbc.gridx = 0;
			gbc.weightx = 1;
			gbc.weighty = 1;
			gbc.gridwidth = 2;
			this.linePanel.add(this.linePanelSpacer, gbc.clone());
			
			this.validate();
			this.repaint();
		}
		
		void selectPreclusion(int index) {
			if (this.selectedPreclusion == index) return;
			this.selectedPreclusion = index;
			
			for (int l = 0; l < this.preclusions.size(); l++) {
				CustomFunctionPreclusion line = ((CustomFunctionPreclusion) this.preclusions.get(l));
				if (l == this.selectedPreclusion) {
					line.filterPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
					line.messagePanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				}
				else {
					line.setEditing(false);
					line.filterPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
					line.messagePanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
				}
			}
			
			this.linePanel.validate();
			this.linePanel.repaint();
		}
		
		void moveUp() {
			if (this.selectedPreclusion < 1)
				return;
			this.preclusions.add(this.selectedPreclusion, this.preclusions.remove(this.selectedPreclusion - 1));
			this.selectedPreclusion--;
			this.layoutPreclusions();
		}
		
		void moveDown() {
			if ((this.selectedPreclusion == -1) || ((this.selectedPreclusion + 1) == this.preclusions.size()))
				return;		
			this.preclusions.add(this.selectedPreclusion, this.preclusions.remove(this.selectedPreclusion + 1));
			this.selectedPreclusion++;
			this.layoutPreclusions();
		}
		
		void addPreclusion() {
			AddPreclusionDialog acd = new AddPreclusionDialog();
			acd.setVisible(true);
			String filter = acd.getFilter();
			if (filter != null)
				this.addPreclusion(filter, acd.getMessage());
		}
		
		void addPreclusion(String filter, String message) {
			CustomFunctionPreclusion line = new CustomFunctionPreclusion(filter, message);
			line.filterPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
			line.messagePanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
			this.preclusions.add(line);
			this.layoutPreclusions();
			this.selectPreclusion(this.preclusions.size() - 1);
		}
		
		void removePreclusion() {
			if (this.selectedPreclusion == -1) return;
			int newSelectedPreclusion = this.selectedPreclusion;
			this.preclusions.remove(this.selectedPreclusion);
			this.selectedPreclusion = -1;
			this.layoutPreclusions();
			if (newSelectedPreclusion == this.preclusions.size())
				newSelectedPreclusion--;
			this.selectPreclusion(newSelectedPreclusion);
		}
		
		boolean validateFilter(String filter) {
			if (filter.length() != 0) try {
				GPathParser.parseExpression(filter);
				return true;
			} catch (Exception e) {}
			return false;
		}
		
		boolean validateMessage(String message) {
			return ((message != null) && (message.trim().length() != 0));
		}
		
		void testFilter() {
			if (this.selectedPreclusion == -1) return;
			this.testFilter(((CustomFunctionPreclusion) this.preclusions.get(this.selectedPreclusion)).getFilter());
		}
		void testFilter(String filter) {
			if (filter.length() == 0)
				return;
			
			QueriableAnnotation testDoc = Gamta.getTestDocument();
			if (testDoc == null)
				return;
			
			GPathExpression gpe;
			try {
				gpe = GPathParser.parseExpression(filter);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("The filter expression is invalid:\n" + e.getMessage()), "Filter Expression Invalid", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			GPathObject gpo = GPath.evaluateExpression(gpe, testDoc, null);
			JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("The filter expression " + (gpo.asBoolean().value ? "DOES" : " DOES NOT") + " preclude the current document."), "Filter Match Result", JOptionPane.INFORMATION_MESSAGE);
		}
		
		private class CustomFunctionPreclusion {
			int index = 0;
			private boolean isEditing = false;
			
			private String filter;
			private boolean filterDirty = false;
			private int filterInputPressedKey = -1;
			private String message;
			private boolean messageDirty = false;
			private int messageInputPressedKey = -1;
			
			JPanel filterPanel = new JPanel(new BorderLayout(), true);
			private JLabel filterDisplay = new JLabel("", JLabel.LEFT);
			private JPanel filterEditor = new JPanel(new BorderLayout(), true);
			private JTextField filterInput = new JTextField("");
			private JButton filterTest = new JButton("Test");
			
			JPanel messagePanel = new JPanel(new BorderLayout(), true);
			private JLabel messageDisplay = new JLabel("", JLabel.LEFT);
			private JTextField messageInput = new JTextField("");
			
			CustomFunctionPreclusion(String filter, String message) {
				this.filter = filter;
				this.message = message;
				
				this.filterDisplay.setText(this.filter);
				this.filterDisplay.setOpaque(true);
				this.filterDisplay.setBackground(Color.WHITE);
				this.filterDisplay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				this.filterDisplay.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getButton() != MouseEvent.BUTTON1) return;
						if (me.getClickCount() > 1)
							setEditing(true);
						select();
					}
				});
				
				this.filterInput.setText(this.filter);
				this.filterInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.filterInput.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {
						filterInputPressedKey = ke.getKeyCode();
					}
					public void keyReleased(KeyEvent ke) {
						filterInputPressedKey = -1;
					}
					public void keyTyped(KeyEvent ke) {
						if (filterInputPressedKey == KeyEvent.VK_ESCAPE) {
							revertFilter();
							setEditing(false);
						}
						filterDirty = true;
					}
				});
				this.filterInput.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent fe) {
						updateFilter();
					}
				});
				this.filterInput.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						setEditing(false);
					}
				});
				
				this.filterTest.setBorder(BorderFactory.createRaisedBevelBorder());
				this.filterTest.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						testFilter(getFilter());
					}
				});
				
				this.filterEditor.add(this.filterInput, BorderLayout.CENTER);
				this.filterEditor.add(this.filterTest, BorderLayout.EAST);
				
				this.messageDisplay.setText(this.message);
				this.messageDisplay.setOpaque(true);
				this.messageDisplay.setBackground(Color.WHITE);
				this.messageDisplay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				this.messageDisplay.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getButton() != MouseEvent.BUTTON1) return;
						if (me.getClickCount() > 1)
							setEditing(true);
						select();
					}
				});
				
				this.messageInput.setText(this.message);
				this.messageInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.messageInput.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {
						messageInputPressedKey = ke.getKeyCode();
					}
					public void keyReleased(KeyEvent ke) {
						messageInputPressedKey = -1;
					}
					public void keyTyped(KeyEvent ke) {
						if (messageInputPressedKey == KeyEvent.VK_ESCAPE) {
							revertMessage();
							setEditing(false);
						}
						messageDirty = true;
					}
				});
				this.messageInput.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent fe) {
						updateMessage();
					}
				});
				this.messageInput.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						setEditing(false);
					}
				});
				
				this.filterPanel.setPreferredSize(new Dimension(160, 21));
				this.messagePanel.setPreferredSize(new Dimension(160, 21));
				
				this.layoutParts(false);
			}
			String getFilter() {
				this.updateFilter();
				return this.filter;
			}
			private void updateFilter() {
				if (!this.filterDirty) return;
				
				this.filter = this.filterInput.getText().trim();
				this.filterDisplay.setText(this.filter);
				if (validateFilter(this.filter)) {
					this.filterDisplay.setBackground(Color.WHITE);
					this.filterInput.setBackground(Color.WHITE);
				}
				else {
					this.filterDisplay.setBackground(Color.ORANGE);
					this.filterInput.setBackground(Color.ORANGE);
				}
				
				this.filterDirty = false;
			}
			private void revertFilter() {
				this.filterInput.setText(this.filter);
				this.filterDirty = true;
				this.updateFilter();
			}
			String getMessage() {
				this.updateMessage();
				return this.message;
			}
			private void updateMessage() {
				if (!this.messageDirty) return;
				
				this.message = this.messageInput.getText().trim();
				this.messageDisplay.setText(this.message);
				if (validateFilter(this.message)) {
					this.messageDisplay.setBackground(Color.WHITE);
					this.messageInput.setBackground(Color.WHITE);
				}
				else {
					this.messageDisplay.setBackground(Color.ORANGE);
					this.messageInput.setBackground(Color.ORANGE);
				}
				
				this.messageDirty = false;
			}
			private void revertMessage() {
				this.messageInput.setText(this.message);
				this.messageDirty = true;
				this.updateMessage();
			}
			void setEditing(boolean editing) {
				if (this.isEditing == editing)
					return;
				if (this.isEditing) {
					this.updateFilter();
					this.updateMessage();
				}
				this.isEditing = editing;
				this.layoutParts(this.isEditing);
				if (!this.isEditing)
					linePanel.requestFocusInWindow();
			}
			void layoutParts(boolean editing) {
				this.filterPanel.removeAll();
				this.messagePanel.removeAll();
				if (editing) {
					this.filterPanel.add(this.filterEditor, BorderLayout.CENTER);
					this.messagePanel.add(this.messageInput, BorderLayout.CENTER);
				}
				else {
					this.filterPanel.add(this.filterDisplay, BorderLayout.CENTER);
					this.messagePanel.add(this.messageDisplay, BorderLayout.CENTER);
				}
				this.filterPanel.validate();
				this.filterPanel.repaint();
				this.messagePanel.validate();
				this.messagePanel.repaint();
			}
			void select() {
				selectPreclusion(this.index);
			}
		}
		
		private class AddPreclusionDialog extends DialogPanel {
			private boolean committed = true;
			private JTextField filterInput;
			private JTextField messageInput;
			
			AddPreclusionDialog() {
				super("Add Preclusion", true);
				
				this.filterInput = new JTextField();
				this.filterInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.filterInput.setEditable(true);
				
				this.messageInput = new JTextField();
				this.messageInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.messageInput.setEditable(true);
				
				JPanel inputPanel = new JPanel(new GridLayout(2, 2), true);
				inputPanel.add(new JLabel("Preclusion GPath Filter Expression", JLabel.CENTER));
				inputPanel.add(new JLabel("Error Message to Show on Match", JLabel.CENTER));
				inputPanel.add(this.filterInput);
				inputPanel.add(this.messageInput);
				
				JButton addPreclusionButton = new JButton("Add Preclusion");
				addPreclusionButton.setPreferredSize(new Dimension(100, 21));
				addPreclusionButton.setBorder(BorderFactory.createRaisedBevelBorder());
				addPreclusionButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String filter = getFilter();
						if (!validateFilter(filter)) {
							JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("'" + filter + "' is not a valid GPath expression or filter name."), "Invalid Filter", JOptionPane.ERROR_MESSAGE);
							return;
						}
						String message = getMessage();
						if (!validateMessage(message)) {
							JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("'" + message + "' is not a valid mapping message."), "Invalid Message", JOptionPane.ERROR_MESSAGE);
							return;
						}
						dispose();
					}
				});
				
				JButton testFilterButton = new JButton("Test Filter");
				testFilterButton.setPreferredSize(new Dimension(100, 21));
				testFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
				testFilterButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						testFilter(getFilter());
					}
				});
				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(100, 21));
				cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						committed = false;
						dispose();
					}
				});
				
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
				buttonPanel.add(addPreclusionButton);
				buttonPanel.add(testFilterButton);
				buttonPanel.add(cancelButton);
				
				this.setLayout(new BorderLayout());
				this.add(inputPanel, BorderLayout.NORTH);
				this.add(new JLabel("<HTML>" +
						"Enter the GPath filter to map to an error message to display if the custom function is applied to a document matching the filter." +
						"</HTML>"
						), BorderLayout.CENTER);
				this.add(buttonPanel, BorderLayout.SOUTH);
				this.setSize(500, 180);
				this.setResizable(true);
				this.setLocationRelativeTo(this.getOwner());
			}
			
			String getFilter() {
				return (this.committed ? this.filterInput.getText() : null);
			}
			
			String getMessage() {
				return (this.committed ? this.messageInput.getText() : null);
			}
		}
		void setContent(LinkedHashMap preclusions) {
			this.preclusions.clear();
			this.selectedPreclusion = -1;
			for (Iterator fit = preclusions.keySet().iterator(); fit.hasNext();) {
				String filter = ((String) fit.next());
				String message = ((String) preclusions.get(filter));
				if (message != null)
					this.addPreclusion(filter, message);
			}
			this.layoutPreclusions();
			this.selectPreclusion(0);
		}
		LinkedHashMap getPreclusions() {
			LinkedHashMap preclusions = new LinkedHashMap();
			for (int c = 0; c < this.preclusions.size(); c++) {
				CustomFunctionPreclusion mc = ((CustomFunctionPreclusion) this.preclusions.get(c));
				String filter = mc.getFilter();
				String message = mc.getMessage();
				if (this.validateFilter(filter) && this.validateMessage(message))
					preclusions.put(filter, message);
			}
			return preclusions;
		}
	}
	
	private class FilterEditorPanel extends JPanel {
		private ArrayList filters = new ArrayList();
		private int selectedFilter = -1;
		
		private JLabel filterLabel = new JLabel("Filter", JLabel.CENTER);
		private JPanel linePanelSpacer = new JPanel();
		private JPanel linePanel = new JPanel(new GridBagLayout());
		
		FilterEditorPanel(String[] filters) {
			super(new BorderLayout(), true);
			
			this.filterLabel.setBorder(BorderFactory.createRaisedBevelBorder());
			this.filterLabel.setPreferredSize(new Dimension(160, 21));
			this.filterLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectFilter(-1);
					linePanel.requestFocusInWindow();
				}
			});
			
			this.linePanelSpacer.setBackground(Color.WHITE);
			this.linePanelSpacer.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					selectFilter(-1);
					linePanel.requestFocusInWindow();
				}
			});
			this.linePanel.setBorder(BorderFactory.createLineBorder(this.getBackground(), 3));
			this.linePanel.setFocusable(true);
			
	        final String upKey = "GO_UP";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), upKey);
	        this.linePanel.getActionMap().put(upKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(upKey);
	                if (selectedFilter > 0)
	                	selectFilter(selectedFilter - 1);
	                else if (selectedFilter == -1)
                		selectFilter(FilterEditorPanel.this.filters.size() - 1);
	            }
	        });
	        
	        final String downKey = "GO_DOWN";
	        this.linePanel.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), downKey);
	        this.linePanel.getActionMap().put(downKey, new AbstractAction() {
	            public void actionPerformed(ActionEvent ae) {
	                System.out.println(downKey);
                	if (selectedFilter == -1)
                		selectFilter(0);
                	else if ((selectedFilter + 1) < FilterEditorPanel.this.filters.size())
	                	selectFilter(selectedFilter + 1);
	            }
	        });

			JScrollPane linePanelBox = new JScrollPane(this.linePanel);
			
			JButton addFilterButton = new JButton("Add Filter");
			addFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
			addFilterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					addFilter();
				}
			});
			
			JButton testFilterButton = new JButton("Test Filter");
			testFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
			testFilterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					testFilter();
				}
			});
			
			JButton removeFilterButton = new JButton("Remove Filter");
			removeFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
			removeFilterButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					removeFilter();
				}
			});
			
			JPanel buttonPanel = new JPanel(new GridBagLayout(), true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 3;
			gbc.insets.bottom = 3;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.weighty = 0;
			gbc.weightx = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 0;
			gbc.gridx = 0;
			buttonPanel.add(addFilterButton, gbc.clone());
			gbc.gridx = 1;
			buttonPanel.add(testFilterButton, gbc.clone());
			gbc.gridx = 2;
			buttonPanel.add(removeFilterButton, gbc.clone());
			
			this.add(linePanelBox, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.SOUTH);
			
			this.setContent(filters);
		}
		
		void layoutFilters() {
			this.linePanel.removeAll();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 0;
			gbc.insets.bottom = 0;
			gbc.insets.left = 0;
			gbc.insets.right = 0;
			gbc.weightx = 1;
			gbc.weighty = 0;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			
			this.linePanel.add(this.filterLabel, gbc.clone());
			gbc.gridy++;
			
			for (int l = 0; l < this.filters.size(); l++) {
				CustomFunctionFilter line = ((CustomFunctionFilter) this.filters.get(l));
				line.index = l;
				
				this.linePanel.add(line.filterPanel, gbc.clone());
				gbc.gridy++;
			}
			
			gbc.weighty = 1;
			this.linePanel.add(this.linePanelSpacer, gbc.clone());
			
			this.validate();
			this.repaint();
		}
		
		void selectFilter(int index) {
			if (this.selectedFilter == index)
				return;
			this.selectedFilter = index;
			
			for (int l = 0; l < this.filters.size(); l++) {
				CustomFunctionFilter line = ((CustomFunctionFilter) this.filters.get(l));
				if (l == this.selectedFilter)
					line.filterPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				else {
					line.setEditing(false);
					line.filterPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
				}
			}
			
			this.linePanel.validate();
			this.linePanel.repaint();
		}
		
		void addFilter() {
			AddFilterDialog acd = new AddFilterDialog();
			acd.setVisible(true);
			String filter = acd.getFilter();
			if (filter != null)
				this.addFilter(filter);
		}
		
		void addFilter(String filter) {
			CustomFunctionFilter line = new CustomFunctionFilter(filter);
			line.filterPanel.setBorder(BorderFactory.createLineBorder(this.getBackground()));
			this.filters.add(line);
			this.layoutFilters();
			this.selectFilter(this.filters.size() - 1);
		}
		
		void removeFilter() {
			if (this.selectedFilter == -1) return;
			int newSelectedFilter = this.selectedFilter;
			this.filters.remove(this.selectedFilter);
			this.selectedFilter = -1;
			this.layoutFilters();
			if (newSelectedFilter == this.filters.size())
				newSelectedFilter--;
			this.selectFilter(newSelectedFilter);
		}
		
		boolean validateFilter(String filter) {
			if (filter.length() != 0) try {
				GPathParser.parseExpression(filter);
				return true;
			} catch (Exception e) {}
			return false;
		}
		
		void testFilter() {
			if (this.selectedFilter == -1) return;
			this.testFilter(((CustomFunctionFilter) this.filters.get(this.selectedFilter)).getFilter());
		}
		void testFilter(String filter) {
			if (filter.length() == 0)
				return;
			
			QueriableAnnotation testDoc = Gamta.getTestDocument();
			if (testDoc == null)
				return;
			
			GPathExpression gpe;
			try {
				gpe = GPathParser.parseExpression(filter);
			}
			catch (Exception e) {
				JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("The filter expression is invalid:\n" + e.getMessage()), "Filter Expression Invalid", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			ArrayList matchAnnotList = new ArrayList();
			QueriableAnnotation[] testAnnots = testDoc.getAnnotations();
			for (int a = 0; a < testAnnots.length; a++) {
				GPathObject gpo = GPath.evaluateExpression(gpe, testAnnots[a], null);
				if ((gpo != null) && gpo.asBoolean().value)
					matchAnnotList.add(testAnnots[a]);
			}
			Annotation[] matchAnnots = ((Annotation[]) matchAnnotList.toArray(new Annotation[matchAnnotList.size()]));
			
			AnnotationDisplayDialog add;
			Window top = DialogPanel.getTopWindow();
			if (top instanceof JDialog)
				add = new AnnotationDisplayDialog(((JDialog) top), "Matches of Filter", matchAnnots, true);
			else if (top instanceof JFrame)
				add = new AnnotationDisplayDialog(((JFrame) top), "Matches of Filter", matchAnnots, true);
			else add = new AnnotationDisplayDialog(((JFrame) null), "Matches of Filter", matchAnnots, true);
			add.setLocationRelativeTo(top);
			add.setVisible(true);
		}
		
		private class CustomFunctionFilter {
			int index = 0;
			private boolean isEditing = false;
			
			private String filter;
			private boolean filterDirty = false;
			private int filterInputPressedKey = -1;
			
			JPanel filterPanel = new JPanel(new BorderLayout(), true);
			private JLabel filterDisplay = new JLabel("", JLabel.LEFT);
			private JPanel filterEditor = new JPanel(new BorderLayout(), true);
			private JTextField filterInput = new JTextField("");
			private JButton filterTest = new JButton("Test");
			
			CustomFunctionFilter(String filter) {
				this.filter = filter;
				
				this.filterDisplay.setText(this.filter);
				this.filterDisplay.setOpaque(true);
				this.filterDisplay.setBackground(Color.WHITE);
				this.filterDisplay.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
				this.filterDisplay.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (me.getButton() != MouseEvent.BUTTON1) return;
						if (me.getClickCount() > 1)
							setEditing(true);
						select();
					}
				});
				
				this.filterInput.setText(this.filter);
				this.filterInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.filterInput.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent ke) {
						filterInputPressedKey = ke.getKeyCode();
					}
					public void keyReleased(KeyEvent ke) {
						filterInputPressedKey = -1;
					}
					public void keyTyped(KeyEvent ke) {
						if (filterInputPressedKey == KeyEvent.VK_ESCAPE) {
							revertFilter();
							setEditing(false);
						}
						filterDirty = true;
					}
				});
				this.filterInput.addFocusListener(new FocusAdapter() {
					public void focusLost(FocusEvent fe) {
						updateFilter();
					}
				});
				this.filterInput.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						setEditing(false);
					}
				});
				
				this.filterTest.setBorder(BorderFactory.createRaisedBevelBorder());
				this.filterTest.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						testFilter(getFilter());
					}
				});
				
				this.filterEditor.add(this.filterInput, BorderLayout.CENTER);
				this.filterEditor.add(this.filterTest, BorderLayout.EAST);
				
				this.filterPanel.setPreferredSize(new Dimension(160, 21));
				
				this.layoutParts(false);
			}
			String getFilter() {
				this.updateFilter();
				return this.filter;
			}
			private void updateFilter() {
				if (!this.filterDirty) return;
				
				this.filter = this.filterInput.getText().trim();
				this.filterDisplay.setText(this.filter);
				if (validateFilter(this.filter)) {
					this.filterDisplay.setBackground(Color.WHITE);
					this.filterInput.setBackground(Color.WHITE);
				}
				else {
					this.filterDisplay.setBackground(Color.ORANGE);
					this.filterInput.setBackground(Color.ORANGE);
				}
				
				this.filterDirty = false;
			}
			private void revertFilter() {
				this.filterInput.setText(this.filter);
				this.filterDirty = true;
				this.updateFilter();
			}
			void setEditing(boolean editing) {
				if (this.isEditing == editing)
					return;
				if (this.isEditing)
					this.updateFilter();
				this.isEditing = editing;
				this.layoutParts(this.isEditing);
				if (!this.isEditing)
					linePanel.requestFocusInWindow();
			}
			void layoutParts(boolean editing) {
				this.filterPanel.removeAll();
				if (editing)
					this.filterPanel.add(this.filterEditor, BorderLayout.CENTER);
				else this.filterPanel.add(this.filterDisplay, BorderLayout.CENTER);
				this.filterPanel.validate();
				this.filterPanel.repaint();
			}
			void select() {
				selectFilter(this.index);
			}
		}
		
		private class AddFilterDialog extends DialogPanel {
			private boolean committed = true;
			private JTextField filterInput;
			
			AddFilterDialog() {
				super("Add Filter", true);
				
				this.filterInput = new JTextField();
				this.filterInput.setBorder(BorderFactory.createLoweredBevelBorder());
				this.filterInput.setEditable(true);
				
				JPanel inputPanel = new JPanel(new BorderLayout(), true);
				inputPanel.add(this.filterInput, BorderLayout.CENTER);
				
				JButton addFilterButton = new JButton("Add Filter");
				addFilterButton.setPreferredSize(new Dimension(100, 21));
				addFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
				addFilterButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						String filter = getFilter();
						if (!validateFilter(filter)) {
							JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("'" + filter + "' is not a valid GPath expression or filter name."), "Invalid Filter", JOptionPane.ERROR_MESSAGE);
							return;
						}
						dispose();
					}
				});
				
				JButton testFilterButton = new JButton("Test Filter");
				testFilterButton.setPreferredSize(new Dimension(100, 21));
				testFilterButton.setBorder(BorderFactory.createRaisedBevelBorder());
				testFilterButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						testFilter(getFilter());
					}
				});
				
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setPreferredSize(new Dimension(100, 21));
				cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						committed = false;
						dispose();
					}
				});
				
				JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER), true);
				buttonPanel.add(addFilterButton);
				buttonPanel.add(testFilterButton);
				buttonPanel.add(cancelButton);
				
				this.setLayout(new BorderLayout());
				this.add(inputPanel, BorderLayout.NORTH);
				this.add(new JLabel("<HTML>" +
						"Enter the filter to whose matches the custom function is applicable.<br>" +
						"The filter can be changed later in the table.</HTML>"
						), BorderLayout.CENTER);
				this.add(buttonPanel, BorderLayout.SOUTH);
				this.setSize(500, 180);
				this.setResizable(true);
				this.setLocationRelativeTo(this.getOwner());
			}
			
			String getFilter() {
				return (this.committed ? this.filterInput.getText().trim() : null);
			}
		}
		
		void setContent(String[] filters) {
			this.filters.clear();
			this.selectedFilter = -1;
			for (int f = 0; f < filters.length; f++)
				this.addFilter(filters[f]);
			this.layoutFilters();
			this.selectFilter(0);
		}
		
		String[] getFilters() {
			ArrayList filters = new ArrayList(this.filters.size());
			
			for (int c = 0; c < this.filters.size(); c++) {
				CustomFunctionFilter mc = ((CustomFunctionFilter) this.filters.get(c));
				String filter = mc.getFilter();
				if (this.validateFilter(filter))
					filters.add(filter);
			}
			
			return ((String[]) filters.toArray(new String[filters.size()]));
		}
	}
}
