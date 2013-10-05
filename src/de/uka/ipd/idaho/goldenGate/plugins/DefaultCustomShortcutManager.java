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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.goldenGate.CustomShortcut;
import de.uka.ipd.idaho.goldenGate.util.DataListListener;
import de.uka.ipd.idaho.goldenGate.util.DataListPanel;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.ResourceDialog;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Default implementation of the CustomShortcut.Manager interface
 * 
 * @author sautter
 */
public class DefaultCustomShortcutManager extends AbstractResourceManager implements CustomShortcut.Manager {
	
	private static final String ANNOTATION_TYPE_ATTRIBUTE = "ANNOTATION_TYPE";
	private static final String PROCESSOR_NAME_ATTRIBUTE = "PROCESSOR_NAME";
	private static final String PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE = "PROCESSOR_PROVIDER_CLASS";
	
	private static final String FILE_EXTENSION = ".customShortcut";
	
	public DefaultCustomShortcutManager() {}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getFileExtension()
	 */
	protected String getFileExtension() {
		return FILE_EXTENSION;
	}

	/** @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getDataNamesForResource(java.lang.String)
	 */
	public String[] getDataNamesForResource(String name) {
		StringVector nameCollector = new StringVector();
		nameCollector.addElementIgnoreDuplicates(name + "@" + this.getClass().getName());
		
		Settings settings = this.loadSettingsResource(name);
		String processorName = settings.getSetting(PROCESSOR_NAME_ATTRIBUTE);
		if (processorName != null) {
			String processorProviderClassName = settings.getSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE);
			if (processorName.indexOf('@') != -1) {
				if (processorProviderClassName == null)
					processorProviderClassName = processorName.substring(processorName.indexOf('@') + 1);
				processorName = processorName.substring(0, processorName.indexOf('@'));
			}
			ResourceManager rm = this.parent.getResourceProvider(processorProviderClassName);
			if (rm != null)
				nameCollector.addContentIgnoreDuplicates(rm.getDataNamesForResource(processorName));
		}
		
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
		return "Custom Shortcut";
	}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getSettingsPanel()
	 */
	public SettingsPanel getSettingsPanel() {
		if (this.settingsPanel == null)
			this.settingsPanel = new CustomShortcutSettingsPanel();
		return this.settingsPanel;
	}
	
	private SettingsPanel settingsPanel = null;
	private class CustomShortcutSettingsPanel extends SettingsPanel implements DataListListener {

		private DataListPanel resourceNameList;
		
		private CustomShortcutEditorPanel editor;
		
		private JPanel editButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		CustomShortcutSettingsPanel() {
			super("Custom Shortcuts", "Configure custom keyboard shortcuts for instant access to frequently used operations in Annotation Editor.");
			this.setLayout(new BorderLayout());
			this.setDoubleBuffered(true);
			
			this.resourceNameList = DefaultCustomShortcutManager.this.resourceNameList;
			
			JButton button;
			
			button = new JButton("Create");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (createCustomShortcut())
						resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			button = new JButton("Clone");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cloneCustomShortcut())
						resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			button = new JButton("Delete");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (deleteCustomShortcut()) resourceNameList.refresh();
				}
			});
			this.editButtons.add(button);
			
			this.add(this.editButtons, BorderLayout.NORTH);
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.resourceNameList.addDataListListener(this);
		}
		
		private boolean createCustomShortcut() {
			return this.createCustomShortcut(new Settings(), null);
		}
		
		private boolean cloneCustomShortcut() {
			String selectedName = this.resourceNameList.getSelectedName();
			if (selectedName == null)
				return this.createCustomShortcut();
			else {
				String name = "New " + selectedName;
				return this.createCustomShortcut(this.editor.getSettings(), name);
			}
		}
		
		private boolean createCustomShortcut(Settings modelCustomShortcut, String name) {
			CreateCustomShortcutDialog cpd = new CreateCustomShortcutDialog(name, getCustomShortcut(modelCustomShortcut));
			cpd.setVisible(true);
			
			if (cpd.isCommitted()) {
				Settings customShortcut = cpd.getCustomShortcut();
				String customShortcutName = cpd.getCustomShortcutName();
				if (!customShortcutName.endsWith(FILE_EXTENSION)) customShortcutName += FILE_EXTENSION;
				try {
					if (storeSettingsResource(customShortcutName, customShortcut)) {
						parent.notifyResourcesChanged(this.getClass().getName());
						return true;
					}
				}
				catch (IOException e) {}
			}
			return false;
		}
		
		private boolean deleteCustomShortcut() {
			String name = this.resourceNameList.getSelectedName();
			if ((name != null) && (JOptionPane.showConfirmDialog(this, ("Really delete " + name), "Confirm Delete CustomShortcut", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
				try {
					if (dataProvider.deleteData(name)) {
						this.resourceNameList.refresh();
						parent.notifyResourcesChanged(this.getClass().getName());
						return true;
					} else {
						JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				}
				catch (Exception ioe) {
					JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}
			else return false;
		}
		
		/** @see de.goldenGate.util.DataListListener#selected(java.lang.String)
		 */
		public void selected(String dataName) {
			if ((this.editor != null) && this.editor.isDirty()) {
				try {
					storeSettingsResource(this.editor.name, this.editor.getSettings());
					parent.notifyResourcesChanged(this.getClass().getName());
				}
				catch (IOException ioe) {
					if (JOptionPane.showConfirmDialog(this, (ioe.getClass().getName() + " (" + ioe.getMessage() + ")\nwhile saving file to " + this.editor.name + "\nProceed?"), "Could Not Save CustomShortcut", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
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
					this.editor = new CustomShortcutEditorPanel(dataName, getCustomShortcut(set));
					this.add(this.editor, BorderLayout.CENTER);
				}
			}
			this.add(this.editButtons, BorderLayout.NORTH);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.validate();
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			if ((this.editor != null) && this.editor.isDirty()) try {
				storeSettingsResource(this.editor.name, this.editor.getSettings());
				parent.notifyResourcesChanged(this.getClass().getName());
			}
			catch (IOException ioe) {}
		}
	}
	
	/**
	 * retrieve a CustomShortcut by its name
	 * @param name the name of the reqired CustomShortcut
	 * @return the CustomShortcut with the required name, or null, if there is
	 *         no such CustomShortcut
	 */
	public CustomShortcut getCustomShortcut(String name) {
		if (name == null) return null;
		if (!name.endsWith(FILE_EXTENSION)) name += FILE_EXTENSION;
		return this.getCustomShortcut(this.loadSettingsResource(name));
	}
	
	private CustomShortcut getCustomShortcut(Settings settings) {
		if (settings == null) return null;
		try {
			String annotationType = settings.getSetting(ANNOTATION_TYPE_ATTRIBUTE, "");
			
			String processorName = settings.getSetting(PROCESSOR_NAME_ATTRIBUTE);
			String processorProviderClassName = settings.getSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE);
			if ((processorName != null) && (processorName.indexOf('@') != -1)) {
				if (processorProviderClassName == null)
					processorProviderClassName = processorName.substring(processorName.indexOf('@') + 1);
				processorName = processorName.substring(0, processorName.indexOf('@'));
			}
			
			DocumentProcessorManager dpm = this.parent.getDocumentProcessorProvider(processorProviderClassName);
			return new CustomShortcut(annotationType, dpm, processorName);
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static final String[] chars = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	private class CreateCustomShortcutDialog extends DialogPanel {
		
		private JPanel namePanel = new JPanel(new GridBagLayout());
		private JLabel nameLabel = new JLabel("", JLabel.LEFT);
		private JComboBox charSelector = new JComboBox(chars);
		private JCheckBox altDown = new JCheckBox("Alt");
		private JCheckBox altGrDown = new JCheckBox("AltGr");
		
		private CustomShortcutEditorPanel editor;
		private String customShortcutName = null;
		
		public CreateCustomShortcutDialog(String name, CustomShortcut customShortcut) {
			super("Create Custom Shortcut", true);
			
			this.nameLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			this.charSelector.setBorder(BorderFactory.createLoweredBevelBorder());
			this.charSelector.setEditable(false);
			this.charSelector.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					updateName();
				}
			});
			this.altDown.setBorder(BorderFactory.createLoweredBevelBorder());
			this.altDown.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					if (altDown.isSelected())
						altGrDown.setSelected(false);
					updateName();
				}
			});
			this.altGrDown.setBorder(BorderFactory.createLoweredBevelBorder());
			this.altGrDown.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					if (altGrDown.isSelected())
						altDown.setSelected(false);
					updateName();
				}
			});
			this.updateName();
			
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 2;
			gbc.insets.bottom = 2;
			gbc.insets.left = 5;
			gbc.insets.right = 5;
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			
			gbc.weightx = 0;
			this.namePanel.add(new JLabel("Shortcut Name:"), gbc.clone());
			gbc.gridx ++;
			
			gbc.weightx = 1;
			this.namePanel.add(this.nameLabel, gbc.clone());
			gbc.gridx ++;
			
			gbc.weightx = 0;
			this.namePanel.add(new JLabel("Schortcut Char:"), gbc.clone());
			gbc.gridx ++;
			
			gbc.weightx = 0;
			this.namePanel.add(this.charSelector, gbc.clone());
			gbc.gridx ++;
			
			gbc.weightx = 0;
			this.namePanel.add(this.altDown, gbc.clone());
			gbc.gridx ++;
			
			gbc.weightx = 0;
			this.namePanel.add(this.altGrDown, gbc.clone());
			gbc.gridx ++;
			
			//	initialize main buttons
			JButton commitButton = new JButton("Create");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					customShortcutName = null;
					dispose();
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	initialize editor
			this.editor = new CustomShortcutEditorPanel(name, customShortcut);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.namePanel, BorderLayout.NORTH);
			this.getContentPane().add(this.editor, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(600, 400));
			this.setLocationRelativeTo(this.getOwner());
		}
		
		private void updateName() {
			Object item = this.charSelector.getSelectedItem();
			String name = ((item == null) ? "?" : item.toString());
			if (this.altDown.isSelected()) name = ("Alt-" + name);
			if (this.altGrDown.isSelected()) name = ("AltGr-" + name);
			this.nameLabel.setText(name);
			this.customShortcutName = name;
		}
		
		boolean isCommitted() {
			return (this.customShortcutName != null);
		}
		
		Settings getCustomShortcut() {
			return this.editor.getSettings();
		}
		
		String getCustomShortcutName() {
			return this.customShortcutName;
		}
	}

	private class CustomShortcutEditorPanel extends JPanel implements DocumentListener {
		
		private String name;
		private boolean dirty = false;
		
		private JTextField annotationType = new JTextField();
		
//		private DocumentProcessor processor;
		private DocumentProcessorManager processorProvider;
		private String processorName;
		private JLabel processorLabel = new JLabel("", JLabel.LEFT);
		private String processorTypeLabel = null;
		
		CustomShortcutEditorPanel(String name, CustomShortcut customShortcut) {
			super(new BorderLayout(), true);
			this.name = name;
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			
//			if (customShortcut != null) {
//				this.annotationType.setText(customShortcut.annotationType);
////				this.processor = customShortcut.processor;
//				this.processor = customShortcut.getDocumentProcessor();
//				if (this.processor != null) {
//					DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(this.processor.getProviderClassName());
//					if (dpm != null) this.processorTypeLabel = dpm.getResourceTypeLabel();
//				}
//			}
//			this.processorLabel.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent me) {
//					if ((me.getClickCount() > 1) && (processor != null)) {
//						DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(processor.getProviderClassName());
//						if (dpm != null) dpm.editDocumentProcessor(processor.getName());
//					}
//				}
//			});
			if (customShortcut != null) {
				this.annotationType.setText(customShortcut.annotationType);
				this.processorName = customShortcut.getDocumentProcessorName();
				this.processorProvider = customShortcut.getDocumentProcessorProvider();
				if (this.processorProvider != null)
					this.processorTypeLabel = this.processorProvider.getResourceTypeLabel();
			}
			this.processorLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if ((me.getClickCount() > 1) && (processorName != null) && (processorProvider != null))
						processorProvider.editDocumentProcessor(processorName);
				}
			});
			
			JButton clearDpButton = new JButton("Clear");
			clearDpButton.setBorder(BorderFactory.createRaisedBevelBorder());
			clearDpButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					clearProcessor();
				}
			});
			
			this.annotationType.getDocument().addDocumentListener(this);
			
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
			
			gbc.gridy ++;
			gbc.gridx = 0;
			gbc.gridwidth = 1;
			gbc.weightx = 0;
			functionPanel.add(new JLabel("Annotation Type", JLabel.LEFT), gbc.clone());
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			functionPanel.add(this.annotationType, gbc.clone());
			
//			gbc.gridy ++;
//			gbc.gridx = 0;
//			gbc.gridwidth = 3;
//			gbc.weightx = 3;
//			gbc.weighty = 5;
//			functionPanel.add(this.processorLabel, gbc.clone());
			gbc.gridy ++;
			
			gbc.gridx = 0;
			gbc.weightx = 1;
			gbc.gridwidth = 2;
			functionPanel.add(this.processorLabel, gbc.clone());
			gbc.gridx = 2;
			gbc.weightx = 0;
			gbc.gridwidth = 1;
			functionPanel.add(clearDpButton, gbc.clone());
			
			gbc.weighty = 1;
			DocumentProcessorManager[] dpms = parent.getDocumentProcessorProviders();
			for (int p = 0; p < dpms.length; p++) {
				gbc.gridy++;
				
				JButton button = new JButton("Use " + dpms[p].getResourceTypeLabel());
				button.setBorder(BorderFactory.createRaisedBevelBorder());
				final String className = dpms[p].getClass().getName();
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						selectProcessor(className);
					}
				});
				
				gbc.gridx = 0;
				gbc.weightx = 1;
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
		
		boolean isDirty() {
			return this.dirty;
		}
		
		private void updateLabels() {
			if ((this.processorName != null) && (this.processorProvider != null)) {
				this.processorTypeLabel = this.processorProvider.getResourceTypeLabel();
				this.processorLabel.setText(this.processorTypeLabel + " '" + this.processorName + "' (double click to edit)");
			}
			else this.processorLabel.setText("<No DocumentProcessor selected>");
		}
		
		private void clearProcessor() {
			this.processorName = null;
			this.processorProvider = null;
			this.updateLabels();
			this.dirty = true;
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
					this.dirty = true;
				}
			}
		}
		
		Settings getSettings() {
			Settings set = new Settings();
			
			String annotationType = this.annotationType.getText();
			if (annotationType.trim().length() == 0) {
				annotationType = JOptionPane.showInputDialog(this, "Please enter the type of Annotation to create by this custom shortcut,\nor leave the type empty, so the DocumentProcessor will be applied to the entire document.", "Enter Annotation Type", JOptionPane.QUESTION_MESSAGE);
				if (annotationType == null)
					annotationType = "";
			}
			if (annotationType.trim().length() != 0)
				set.setSetting(ANNOTATION_TYPE_ATTRIBUTE, annotationType);
			
			if ((this.processorName != null) && (this.processorProvider != null)) {
				set.setSetting(PROCESSOR_NAME_ATTRIBUTE, (this.processorName + "@" + this.processorProvider.getClass().getName()));
//				set.setSetting(PROCESSOR_NAME_ATTRIBUTE, this.processorName);
//				set.setSetting(PROCESSOR_PROVIDER_CLASS_NAME_ATTRIBUTE, this.processorProvider.getClass().getName());
			}
			
			return set;
		}
	}
}
