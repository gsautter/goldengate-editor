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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.util.ResourceDialog;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Active document formats are document formats that can automatically run a
 * single specific document processor from an arbitrary manager on a document
 * after loading or before saving. This can range from a parser for specific
 * numeric values like dates to run after loading to an XML schema validator to
 * run before saving. This abstract class provides the facilities for
 * configuring which document processor to use (a settings panel), for exporting
 * that document processor along with the specific document format, and the
 * applyDocumentProcessor() for running the document processor on a document.
 * 
 * @author sautter
 */
public abstract class ActiveDocumentFormatProvider extends AbstractDocumentFormatProvider {
	
	private static final String DP_CONFIG_FILE_NAME = "dpConfig.cnfg";
	private static final String DP_NAME_SETTING = "DpName";
	private static final String DP_PROVIDER_CLASS_NAME_SETTING = "DpProviderClassName";
	private static final String DP_INTERACTIVE_SETTING = "DpInteractive";
	
	private String dpName = null;
	private String dpProviderClassName = null;
	private boolean dpInteractive = false;
	
	/**
	 * Apply the configured document processor to a document. This method may
	 * throw a runtime exception (that the invocing code has to react to) if the
	 * configured document processor wants to prevent loading or saving a given
	 * document, e.g. due to validation errors.
	 * @param doc the document to process
	 * @throws RuntimeException
	 */
	protected final void applyDocumentProcessor(MutableAnnotation doc) throws RuntimeException {
		if ((this.dpName == null) || (this.dpProviderClassName == null))
			return;
		DocumentProcessorManager dpm = this.parent.getDocumentProcessorProvider(this.dpProviderClassName);
		if (dpm ==  null)
			return;
		DocumentProcessor dp = dpm.getDocumentProcessor(this.dpName);
		if (dp == null)
			return;
		
		Properties parameters = new Properties();
		if (this.dpInteractive)
			parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
		dp.process(doc, parameters);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getRequiredResourceNames(java.lang.String, boolean)
	 */
	public String[] getRequiredResourceNames(String name, boolean recourse) {
		if ((this.dpName == null) || (this.dpProviderClassName == null))
			return super.getRequiredResourceNames(name, recourse);
		
		StringVector nameCollector = new StringVector();
		nameCollector.addElementIgnoreDuplicates(this.dpName + "@" + this.dpProviderClassName);
		
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

	/**
	 * Initialize the active document format. This implementation loads the
	 * descriptor data for the document processor to use in the
	 * applyDocumentProcessor() method. Thus subclasses overwriting this method
	 * for their own initialization have to make the super call.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePlugin#init()
	 */
	public void init() {
		Settings config = this.loadSettingsResource(DP_CONFIG_FILE_NAME);
		if (config == null)
			return;
		this.dpName = config.getSetting(DP_NAME_SETTING);
		this.dpProviderClassName = config.getSetting(DP_PROVIDER_CLASS_NAME_SETTING);
		this.dpInteractive = "true".equals(config.getSetting(DP_INTERACTIVE_SETTING));
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePlugin#getSettingsPanel()
	 */
	public SettingsPanel getSettingsPanel() {
		if (this.settingsPanel == null)
			this.settingsPanel = new AdfSettingsPanel(this.getPluginName(), ("Configure " + this.getPluginName()));
		return this.settingsPanel;
	}
	
	private SettingsPanel settingsPanel = null;
	private class AdfSettingsPanel extends SettingsPanel {
		
		private DocumentProcessor processor;
		private JLabel processorLabel = new JLabel("", JLabel.LEFT);
		private JCheckBox processorInteractive = new JCheckBox("Interactive");
		private String processorTypeLabel = null;
		
		AdfSettingsPanel(String title, String toolTip) {
			super(title, toolTip);
			this.setLayout(new BorderLayout());
			this.setDoubleBuffered(true);
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			
			JButton clearDpButton = new JButton("Clear");
			clearDpButton.setBorder(BorderFactory.createRaisedBevelBorder());
			clearDpButton.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					processor = null;
					processorLabel.setText("<No Document Processor Selected>");
					processorTypeLabel = null;
				}
			});
			
			if ((ActiveDocumentFormatProvider.this.dpName != null) && (ActiveDocumentFormatProvider.this.dpProviderClassName != null)) {
				DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(ActiveDocumentFormatProvider.this.dpProviderClassName);
				if (dpm != null) {
					this.processor = dpm.getDocumentProcessor(ActiveDocumentFormatProvider.this.dpName);
					if (this.processor != null) {
						this.processorTypeLabel = dpm.getResourceTypeLabel();
						this.processorInteractive.setSelected(ActiveDocumentFormatProvider.this.dpInteractive);
					}
				}
			}
			this.processorLabel.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if ((me.getClickCount() > 1) && (processor != null)) {
						DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(processor.getProviderClassName());
						if (dpm != null)
							dpm.editDocumentProcessor(processor.getName());
					}
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
			functionPanel.add(clearDpButton, gbc.clone());
			gbc.gridx = 1;
			gbc.gridwidth = 2;
			gbc.weightx = 2;
			functionPanel.add(this.processorLabel, gbc.clone());
			gbc.gridx = 3;
			gbc.gridwidth = 1;
			gbc.weightx = 0;
			functionPanel.add(this.processorInteractive, gbc.clone());
			
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
				gbc.weightx = 2;
				gbc.gridwidth = 2;
				functionPanel.add(button, gbc.clone());
			}
			
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			this.add(functionPanel, BorderLayout.SOUTH);
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			boolean dirty = false;
			if (this.processor == null)
				dirty = (ActiveDocumentFormatProvider.this.dpName != null);
			else dirty = (!this.processor.getProviderClassName().equals(ActiveDocumentFormatProvider.this.dpProviderClassName) || !this.processor.getName().equals(ActiveDocumentFormatProvider.this.dpName) || (this.processorInteractive.isSelected() != ActiveDocumentFormatProvider.this.dpInteractive));
			if (dirty) try {
				Settings config = new Settings();
				if (this.processor == null) { 
					ActiveDocumentFormatProvider.this.dpName = null;
					ActiveDocumentFormatProvider.this.dpProviderClassName = null;
					ActiveDocumentFormatProvider.this.dpInteractive = false;
				}
				else {
					config.setSetting(DP_NAME_SETTING, this.processor.getName());
					ActiveDocumentFormatProvider.this.dpName = this.processor.getName();
					config.setSetting(DP_PROVIDER_CLASS_NAME_SETTING, this.processor.getProviderClassName());
					ActiveDocumentFormatProvider.this.dpProviderClassName = this.processor.getProviderClassName();
					config.setSetting(DP_INTERACTIVE_SETTING, String.valueOf(this.processorInteractive.isSelected()));
					ActiveDocumentFormatProvider.this.dpInteractive = this.processorInteractive.isSelected();
				}
				storeSettingsResource(DP_CONFIG_FILE_NAME, config);
				parent.notifyResourcesChanged(this.getClass().getName());
			}
			catch (IOException ioe) {}
		}
		
		private void updateLabels() {
			if (this.processor != null) {
				DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(this.processor.getProviderClassName());
				if (dpm != null)
					this.processorTypeLabel = dpm.getResourceTypeLabel();
				this.processorLabel.setText(this.processorTypeLabel + " '" + this.processor.getName() + "' (double click to edit)");
			}
			else this.processorLabel.setText("<No DocumentProcessor selected yet>");
		}
		
		private void selectProcessor(String providerClassName) {
			DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(providerClassName);
			if (dpm != null) {
				ResourceDialog rd = ResourceDialog.getResourceDialog(dpm, ("Select " + dpm.getResourceTypeLabel()), "Select");
				rd.setLocationRelativeTo(rd.getOwner());
				rd.setVisible(true);
				
				//	get processor
				DocumentProcessor dp = dpm.getDocumentProcessor(rd.getSelectedResourceName());
				if (dp != null) {
					this.processor = dp;
					this.updateLabels();
				}
			}
		}
		
		private void createProcessor(String providerClassName) {
			DocumentProcessorManager dpm = parent.getDocumentProcessorProvider(providerClassName);
			if (dpm != null) {
				String dpName = dpm.createDocumentProcessor();
				DocumentProcessor dp = dpm.getDocumentProcessor(dpName);
				if (dp != null) {
					this.processor = dp;
					this.updateLabels();
				}
			}
		}
	}
}
