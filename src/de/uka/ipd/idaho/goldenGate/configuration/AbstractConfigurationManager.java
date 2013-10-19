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
package de.uka.ipd.idaho.goldenGate.configuration;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorDialog;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Configuration;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.DataItem;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.ExportPanel;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.SpecialDataHandler;
import de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Manager for exporting GoldenGATE Editor configurations to arbitrary
 * destinations. The actual implementation of data transfer is up to sub
 * classes. Sub classes should also overwrite the getMainMenuTitle() method to
 * make themselves accessible.
 * 
 * @author sautter
 */
public abstract class AbstractConfigurationManager extends AbstractGoldenGatePlugin {
	
	/* TODO
  - provide protected getConfigurationAttributes() --> Properties method in abstract configuration exporter
    - loop through to attribute panel (check for null)
    - set inside abstract class code to gain access
  ==> add getAttributePanel() --> ConfigurationAttributePanel method in abstract configuration exporter
  - store exporter specific attributes for each export description in "<exportName>.exportAttributes"
	 */
	
	/**
	 * Retrieve the names of exports available through this configuration
	 * manager. This default implementation returns the names of all export
	 * descriptions available through the configuration manager's data provider.
	 * Sub classes wanting to generate export descriptions in a different way
	 * are welcome to overwrite it as needed.
	 * @return the names of exports available through this configuration manager
	 */
	protected String[] getExportNames() {
		String[] dataNames = this.dataProvider.getDataNames();
		StringVector exportNames = new StringVector();
		String exportName;
		for (int d = 0; d < dataNames.length; d++)
			if (dataNames[d].endsWith(EXPORT_DESCRIPTION_FILE_EXTENSION)) {
				exportName = dataNames[d];
				exportName = exportName.substring(0, (exportName.length() - EXPORT_DESCRIPTION_FILE_EXTENSION.length()));
				exportNames.addElementIgnoreDuplicates(exportName);
			}
		return exportNames.toStringArray();
	}
	
	private String[] getAvailableExportNames() {
		StringVector exportNames = new StringVector();
		exportNames.addContentIgnoreDuplicates(this.getExportNames());
		
		GoldenGatePlugin[] ggps = this.parent.getPlugins();
		for (int p = 0; p < ggps.length; p++) {
			if ((ggps[p] != this) && (ggps[p] instanceof AbstractConfigurationManager)) {
				String[] ggpExportNames = ((AbstractConfigurationManager) ggps[p]).getExportNames();
				for (int e = 0; e < ggpExportNames.length; e++)
					exportNames.addElementIgnoreDuplicates(ggpExportNames[e] + "@" + ggps[p].getClass().getName());
			}
		}
		
		return exportNames.toStringArray();
	}
	
	private static final String EXPORT_DESCRIPTION_FILE_EXTENSION = ".exportDesc";
	
	private StringVector loadExprortDescription(String exportName) {
		try {
			Reader r = new InputStreamReader(this.dataProvider.getInputStream(exportName + EXPORT_DESCRIPTION_FILE_EXTENSION), "UTF-8");
			StringVector exDesc = StringVector.loadList(r);
			r.close();
			return exDesc;
		}
		catch (IOException ioe) {
			return new StringVector();
		}
	}
	
	private void storeExportDescription(String exportName, StringVector exDesc) throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.dataProvider.getOutputStream(exportName + EXPORT_DESCRIPTION_FILE_EXTENSION), "UTF-8"));
		for (int l = 0; l < exDesc.size(); l++) {
			String exDescLine = exDesc.get(l);
			exDescLine = exDescLine.trim();
			if (exDescLine.startsWith("<"))
				continue;
			bw.write(exDescLine);
			bw.newLine();
		}
		bw.flush();
		bw.close();
	}
	
	/**
	 * Retrieve the description of a configuration export. This is basically a
	 * list of the fully qualified names of the plugins and resources to export.
	 * This list has to include only the names of the plugins and resources to
	 * export explicitly, as dependencies between plugins and resources are
	 * resolved automatically.
	 * @param exportName the local name of the export
	 * @return a description of a configuration export
	 */
	protected StringVector getExportDescription(String exportName) {
		StringVector exportDesc;
		if (exportName == null)
			exportDesc = new StringVector();
		else {
			int split = exportName.indexOf('@');
			if (split == -1)
				exportDesc = this.loadExprortDescription(exportName);
			else {
				String providerClassName = exportName.substring(split + 1);
				exportName = exportName.substring(0, split);
				GoldenGatePlugin ggp = this.parent.getPlugin(providerClassName);
				if ((ggp != null) && (ggp instanceof AbstractConfigurationManager))
					exportDesc = ((AbstractConfigurationManager) ggp).getExportDescription(exportName);
				else exportDesc = new StringVector();
			}
		}
		return exportDesc;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePlugin#getMainMenuItems()
	 */
	public JMenuItem[] getMainMenuItems() {
		ArrayList collector = new ArrayList();
		JMenuItem mi;
		
		mi = new JMenuItem("Manage Configurations");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				manageConfigurations();
			}
		});
		collector.add(mi);
		return ((JMenuItem[]) collector.toArray(new JMenuItem[collector.size()]));
	}
	
	/**
	 * Check if the configuration manager is ready to be used. Beside the
	 * conditions of abstract GoldenGATE plugin, this implementation checks
	 * whether or not the root path is accessible.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePlugin#isOperational()
	 * @see de.uka.ipd.idaho.goldenGate.configuration.AbstractConfigurationManager#getRootPath()
	 */
	public boolean isOperational() {
		return (super.isOperational() && (this.getRootPath() != null));
	}
	
	/**
	 * Specify which plugins should be selectable for export. The returned
	 * StringVector is to contain the class names of the selectable plugins.
	 * This default implementation returns null, allowing all plugins to be
	 * selected. Sub classes are welcome to overwrite this method and provide a
	 * more specific filter.
	 * @return a list of the class names of the plugins selectable for export
	 */
	protected StringVector getSelectablePluginClassNames() {
		return null;
	}
	
	/**
	 * Wrap the default special data handler in an implementation specific one,
	 * providing additional special data items. This default implementation
	 * simply returns the argument special data handler, sub classes are welcome
	 * to overwrite it as needed.
	 * @param exportName the name of the export to provide a special data
	 *            handler for
	 * @param sdh the default special data handler
	 * @return an implementation specific special data handler for the export
	 *         with the specified name
	 */
	protected SpecialDataHandler getSpecialDataHandler(String exportName, SpecialDataHandler sdh) {
		return sdh;
	}
	
	/**
	 * Adjust the list of the plugins and resources to include in a export with
	 * a given name. This is intended to accommodate the specific peculiarities
	 * of the configurations provided by this configuration manager. This
	 * default implementation does nothing, sub classes are welcome to overwrite
	 * it as needed.
	 * @param exportName the name of the export the argument selection list
	 *            belongs to
	 * @param selected the list of plugins and resources to extend
	 */
	protected void adjustSelection(String exportName, StringVector selected) {}
	
	/**
	 * Adjust the descriptor of a configuration to export to the specific
	 * peculiarities of the configurations provided by this configuration
	 * manager. This default implementation does nothing, sub classes are
	 * welcome to overwrite it as needed.
	 * @param exportName the name of the export the argument descriptor belongs
	 *            to
	 * @param config the configuration descriptor to extend
	 */
	protected void adjustConfiguration(String exportName, Configuration config) {}
	
	/**
	 * Retrieve the root path of the configuration to export from. This method
	 * should return null if the file system is not accessible, thus indicating
	 * that the data to export is not accessible.
	 * @return the root folder of the configuration to export from, or null, if
	 *         this folder is not accessible
	 */
	protected File getRootPath() {
		
		//	get root path
		File ggRoot;
		try {
			ggRoot = new File(".");
			
			//	check if we got the root path
			File ggJar = new File(ggRoot, "GoldenGATE.jar");
			if (ggJar.exists())
				return ggRoot;
			else {
				JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "Cannot export GoldenGATE configuration without GoldenGATE root folder.", "GoldenGATE Root Folder Not Found", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		//	we may not be allowed to access the file system ...
		catch (SecurityException se) {
			JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "Cannot export GoldenGATE configuration without access to GoldenGATE root folder.", "GoldenGATE Root Folder Not Accessible", JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	/**
	 * Perform the actual data export. The status dialog's step is set to
	 * "Export Data", its progress indicator to 0, so implementations have the
	 * opportunity of indicating their data export progress. The argument
	 * special data handler by default handles GoldenGATE.cnfg and README.txt.
	 * @param exportName the name of the configuration to export
	 * @param specialData the handler for special data
	 * @param config the descriptor of the configuration to export
	 * @param rootPath the root path of the configuration to export from
	 * @param statusDialog a status dialog that displays the export progress
	 * @return true if export succeeds, false otherwise
	 * @throws IOException
	 */
	protected abstract boolean doExport(String exportName, SpecialDataHandler specialData, Configuration config, File rootPath, ExportStatusDialog statusDialog) throws IOException;
	
	private void manageConfigurations() {
		File rootPath = this.getRootPath();
		if (rootPath == null) {
			JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "Without access to the root folder of the current configuration, an export is not possible.", "Root Folder Not Accessible", JOptionPane.ERROR_MESSAGE);
			return;
		}
		rootPath = new File(ConfigurationUtils.normalizePath(rootPath.getAbsolutePath()));
		GoldenGateConfigurationExporter exporter = new GoldenGateConfigurationExporter(rootPath);
		exporter.setVisible(true);
	}
	
	/**
	 * Do an export with a specific name. If the argument name identifies an
	 * export from another configuration manager than this one, the name has to
	 * be fully qualified.
	 * @param exportName the name of the export to do
	 * @return true if the export was successful, false if it was not, or if the
	 *         export was cancelled
	 */
	protected final boolean doExport(String exportName) {
		
		//	get root path
		File rootPath = this.getRootPath();
		if (rootPath == null) {
			JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "Without access to the root folder of the current configuration, an export is not possible.", "Root Folder Not Accessible", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//	get export host and load export description
		AbstractConfigurationManager exportHost = AbstractConfigurationManager.this;
		boolean isLocal;
		if ((exportName == null) || (exportName.indexOf('@') == -1))
			isLocal = true;
		else {
			String providerClassName = exportName.substring(exportName.indexOf('@') + 1);
			exportName = exportName.substring(0, exportName.indexOf('@'));
			GoldenGatePlugin ggp = this.parent.getPlugin(providerClassName);
			if ((ggp != null) && (ggp instanceof AbstractConfigurationManager))
				exportHost = ((AbstractConfigurationManager) ggp);
			isLocal = false;
		}
		
		//	get export description
		StringVector exportDesc = exportHost.getExportDescription(exportName);
		
		//	do local export
		if (isLocal) {
			
			//	open export dialog
			ExportDialog ed = new ExportDialog(exportName, exportDesc, rootPath);
			ed.setVisible(true);
			
			//	report result
			return ed.success;
		}
		
		//	do remote export
		else return this.doExport(exportName, exportDesc, false, rootPath, exportHost);
	}
	
	private boolean doExport(final String exportName, final StringVector selected, final boolean saveDesc, final File rootPath, final AbstractConfigurationManager exportHost) {
		
		System.out.println("AbstractConfigurationManager: doing export '" + exportName + "' in " + this.getClass().getName());
		System.out.println("  - root path is " + rootPath.getAbsolutePath());
		System.out.println("  - save descriptor is " + saveDesc);
		System.out.println("  - export host is " + exportHost.getClass().getName());
		System.out.println("  - selected plugins/resources are:\n    - " + selected.concatStrings("\n    - "));
		
		//	obtain settings
		final Settings ggSettings = this.parent.getSettings();
		
		//	extend README.txt
		final StringVector readmeLines = new StringVector();
		try {
			readmeLines.addContent(StringVector.loadList(exportHost.dataProvider.getInputStream(exportName + "." + README_FILE_NAME)));
			if (ConfigurationUtils.extendReadme(readmeLines, exportName, System.currentTimeMillis())) try {
				readmeLines.storeContent(exportHost.dataProvider.getOutputStream(exportName + "." + README_FILE_NAME));
			} catch (IOException ioe) {}
		}
		catch (FileNotFoundException fnfe) {
			readmeLines.addContent(ConfigurationUtils.createReadme(exportName, System.currentTimeMillis()));
			if (readmeLines.size() != 0) try {
				readmeLines.storeContent(exportHost.dataProvider.getOutputStream(exportName + "." + README_FILE_NAME));
			} catch (IOException ioe) {}
		}
		catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
		
		//	adjust selection (configuration to export might require specific resources to be included)
		exportHost.adjustSelection(exportName, selected);
		System.out.println("  - plugins/resources to export are:\n    - " + selected.concatStrings("\n    - "));
		
		//	create special handler
		final SpecialDataHandler specialData = exportHost.getSpecialDataHandler(exportName, new SpecialDataHandler() {
			public InputStream getInputStream(String dataName) throws IOException {
				if (CONFIG_FILE_NAME.equals(dataName)) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ggSettings.storeAsText(baos);
					return new ByteArrayInputStream(baos.toByteArray());
				}
				else if (README_FILE_NAME.equals(dataName)) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					readmeLines.storeContent(baos);
					return new ByteArrayInputStream(baos.toByteArray());
				}
				else return null;
			}
		});
		
		//	create status dialog
		final ExportStatusDialog statusDialog = new ExportStatusDialog();
		
		//	do export in separate thread so GUI updates are painted
		Thread exportThread = new Thread() {
			public void run() {
				try {
					
					//	wait for status dialog to show
					while (!statusDialog.isVisible()) try {
						Thread.sleep(100);
					} catch (InterruptedException ie) {}
					
					//	generate descriptor
					Configuration config = ConfigurationUtils.buildConfiguration(exportName, rootPath, parent, selected, statusDialog);
					config.addDataItem(new DataItem(README_FILE_NAME, config.configTimestamp));
					exportHost.adjustConfiguration(exportName, config);
					
					//	reset status dialog
					statusDialog.setStep("Exporting Data");
					statusDialog.setBaseProgress(0);
					statusDialog.setMaxProgress(100);
					statusDialog.setProgress(0);
					
					//	do export
					if (AbstractConfigurationManager.this.doExport(exportName, specialData, config, rootPath, statusDialog)) {
						JOptionPane.showMessageDialog(statusDialog, ("GoldenGATE Configuration '" + exportName + "' exported successfully."), "Export Successful", JOptionPane.INFORMATION_MESSAGE);
						
						//	store description on success
						if (saveDesc)
							storeExportDescription(exportName, selected);
					}
					else statusDialog.success = false;
				}
				catch (Throwable t) {
					statusDialog.success = false;
					t.printStackTrace(System.out);
					JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("An error occurred while exporting configuration '" + exportName + "':\n" + t.getMessage()), "Error Exporting Configuration", JOptionPane.ERROR_MESSAGE);
				}
				finally {
					statusDialog.close();
				}
			}
		};
		exportThread.start();
		
		//	block on status dialog
		statusDialog.popUp(true);
		
		//	report result
		return statusDialog.success;
	}
	
	private class GoldenGateConfigurationExporter extends DialogPanel {
		private JList exportList = new JList();
		
		GoldenGateConfigurationExporter(File rootPath) {
			super("GoldenGATE Configuration Exporter", true);
			
			this.setLayout(new BorderLayout());
			
			JLabel label = new JLabel("<HTML>Select an export configuration from the list,<BR>or click <B>New Export</B> for a blank export dialog.</HTML>", JLabel.CENTER);
			this.add(label, BorderLayout.NORTH);
			
			this.exportList.setModel(new DefaultComboBoxModel(getAvailableExportNames()));
			this.exportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			this.exportList.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent me) {
					if ((me.getButton() == MouseEvent.BUTTON1) && (me.getClickCount() > 1)) {
						Object selected = exportList.getSelectedValue();
						if (selected != null)
							doExport((String) selected);
					}
				}
			});
			JScrollPane exportListBox = new JScrollPane(this.exportList);
			this.add(exportListBox, BorderLayout.CENTER);
			
			JButton newExportButton = new JButton("New Export");
			newExportButton.setBorder(BorderFactory.createRaisedBevelBorder());
			newExportButton.setPreferredSize(new Dimension(120, 21));
			newExportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					doExport(null);
				}
			});
			JButton doExportButton = new JButton("Do Selected Export");
			doExportButton.setBorder(BorderFactory.createRaisedBevelBorder());
			doExportButton.setPreferredSize(new Dimension(120, 21));
			doExportButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					Object selected = exportList.getSelectedValue();
					doExport(((selected == null) ? null : ((String) selected)));
				}
			});
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(newExportButton);
			buttonPanel.add(doExportButton);
			this.add(buttonPanel, BorderLayout.SOUTH);
			
			this.setSize(400, 400);
			this.setLocationRelativeTo(this.getOwner());
		}
	}
	
	private class ExportDialog extends DialogPanel {
		private File rootPath;
		private boolean success = false;
		private ExportPanel exportPanel;
		private JTextField exportNameField = new JTextField("<Enter export name here>");
		
		ExportDialog(String exName, StringVector exDesc, File rootPath) {
			super("Export GoldenGATE Configuration", true);
			this.rootPath = rootPath;
			
			this.exportPanel = new ExportPanel(parent, getSelectablePluginClassNames(), exDesc);
			JScrollPane epBox = new JScrollPane(this.exportPanel);
			epBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			epBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			epBox.getVerticalScrollBar().setUnitIncrement(50);
			epBox.getVerticalScrollBar().setBlockIncrement(100);
			this.getContentPane().setLayout(new BorderLayout());
			
			JButton okButton = new JButton("Do Export");
			okButton.setBorder(BorderFactory.createRaisedBevelBorder());
			okButton.setPreferredSize(new Dimension(100, 21));
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String exportName = exportNameField.getText().trim();
					StringVector selected = new StringVector();
					selected.addContentIgnoreDuplicates(exportPanel.getSeleted());
					if (doExport(exportName, selected, true, AbstractConfigurationManager.this)) {
						success = true;
						dispose();
					}
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
			
			JPanel namePanel = new JPanel(new BorderLayout());
			namePanel.add(new JLabel("Export Name:"), BorderLayout.WEST);
			if (exName != null)
				this.exportNameField.setText(exName);
			namePanel.add(this.exportNameField, BorderLayout.CENTER);
			
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			buttonPanel.add(okButton);
			buttonPanel.add(cancelButton);
			
			this.getContentPane().add(namePanel, BorderLayout.NORTH);
			this.getContentPane().add(epBox, BorderLayout.CENTER);
			this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
			
			//	ensure dialog is closed with button
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			
			//	get feedback
			this.setSize(600, 500);
			this.setLocationRelativeTo(this.getOwner());
		}
		
		private boolean doExport(String exportName, StringVector selected, boolean saveDesc, AbstractConfigurationManager exportHost) {
			if ((exportName.length() == 0) || exportName.startsWith("<")) {
				JOptionPane.showMessageDialog(this, "Please specify a name for the configuration to export.", " Export Name Missing", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return AbstractConfigurationManager.this.doExport(exportName, selected, saveDesc, this.rootPath, exportHost);
		}
	}
	
	public static class ExportStatusDialog extends ProgressMonitorDialog {
		
		/** boolean for transporting export success out of a thread */
		public boolean success = true;
		
		/**
		 * Constructor
		 */
		public ExportStatusDialog() {
			super(true, false, DialogPanel.getTopWindow(), "Exporting GoldenGATE Configuration");
			
			this.setSize(500, 170);
			this.setLocationRelativeTo(this.getWindow().getOwner());
		}
	}
//	public static class ExportStatusDialog extends DialogPanel implements StatusMonitor {
//		private JLabel exportStepLabel = new JLabel("", JLabel.LEFT);
//		
//		private JLabel label = new JLabel("", JLabel.CENTER);
//		private ArrayList labelLines = new ArrayList();
//		
//		private int baseProgress = 0;
//		private int maxProgress = 100;
//		private JProgressBar progress = new JProgressBar();
//		
//		/**
//		 * boolean for transporting export success out of a thread
//		 */
//		public boolean success = true;
//		
//		/**
//		 * Constructor
//		 */
//		public ExportStatusDialog() {
//			super("Exporting GoldenGATE Configuration", true);
//			
//			this.progress.setStringPainted(true);
//			
//			this.getContentPane().setLayout(new BorderLayout());
//			this.getContentPane().add(this.exportStepLabel, BorderLayout.NORTH);
//			this.getContentPane().add(this.label, BorderLayout.CENTER);
//			this.getContentPane().add(this.progress, BorderLayout.SOUTH);
//			
//			this.setSize(500, 170);
//			this.setLocationRelativeTo(this.getOwner());
//			this.getDialog().setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
//		}
//		
//		public void setExportStep(String exportStep) {
//			System.out.println(exportStep);
//			try {
//				this.exportStepLabel.setText(exportStep);
//				this.exportStepLabel.validate();
//			} catch (RuntimeException re) {}
//		}
//		
//		public void setLabel(String text) {
//			System.out.println(text);
//			this.labelLines.add(text);
//			while (this.labelLines.size() > 3)
//				this.labelLines.remove(0);
//			
//			StringBuffer labelText = new StringBuffer("<HTML>" + this.labelLines.get(0));
//			for (int l = 1; l < this.labelLines.size(); l++)
//				labelText.append("<BR>" + this.labelLines.get(l));
//			labelText.append("</HTML>");
//			
//			try {
//				this.label.setText(labelText.toString());
//				this.label.validate();
//			} catch (RuntimeException re) {}
//		}
//		
//		public void setBaseProgress(int baseProgress) {
//			this.baseProgress = baseProgress;
//		}
//		
//		public void setMaxProgress(int maxProgress) {
//			this.maxProgress = maxProgress;
//		}
//		
//		public void setProgress(int progress) {
//			this.progress.setValue(this.baseProgress + (((this.maxProgress - this.baseProgress) * progress) / 100));
//		}
//	}
}
