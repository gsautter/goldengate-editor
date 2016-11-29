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


import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.text.html.HTMLDocument;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.goldenGate.util.DataListPanel;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.ResourceSelector;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Abstract implementetion of a resource manager. This class provides
 * convenience IO methods, namely the loadXyzResource() and storeXyzResource()
 * methods. In addition, it provides a DataListPanel for displaying the names of
 * the available resources, and a default implementation for the getSelector()
 * methods.
 * 
 * @author sautter
 */
public abstract class AbstractResourceManager extends AbstractGoldenGatePlugin implements ResourceManager {
	
	/** default size for resource manager edit dialogs */
	protected static final Dimension DEFAULT_EDIT_DIALOG_SIZE = new Dimension(800, 600);
	
	/** a data list panel containing the resource manager's resource names */
	protected DataListPanel resourceNameList = null;
	
	/**
	 * void Constructor for generic loading
	 */
	public AbstractResourceManager() {}
	
	/*
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#setDataProvider(de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider)
	 */
	public void setDataProvider(GoldenGatePluginDataProvider dataProvider) {
		super.setDataProvider(dataProvider);
		
		final String fileExtension = this.getFileExtension();
		this.resourceNameList = new DataListPanel(this.dataProvider, new DataListPanel.StringFilter() {
			public boolean accept(String name) {
				return ((fileExtension == null) ? false : ((name != null) && name.endsWith(fileExtension)));
			}
		});
	}
	
	/**
	 * Retrieve the default file extension this ResourceManager uses for its
	 * Resource description files. If this method returns null, this class
	 * assumes that the resource name list is not used by a concrete sub class.
	 * This may be useful for resource managers that exclusively provide
	 * built-in resources.
	 * @return the default file extension this ResourceManager uses for its
	 *         Resource description files.
	 */
	protected abstract String getFileExtension();
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getPluginName()
	 */
	public String getPluginName() {
		return (this.getResourceTypeLabel() + " Manager");
	}
	
	/**
	 * Retrieve the label for this ResourceManager's entry in the Tools menu of
	 * the main window. <br>
	 * Note: null is the default return value. Subclasses may overwrite this
	 * method to provide another return value
	 * @return the label for this ResourceManager's entry in the Tools menu of
	 *         the main window, or null, if this ResourceManager does not want
	 *         to be accessible through the Tools menu
	 */
	public String getToolsMenuLabel() {
		return null;
	}
	
	/**	@return	the names of all Resources managed by this ResourceManager
	 */
	public String[] getResourceNames() {
		return this.resourceNameList.getNames();
	}
	
	/**
	 * This default implementation simply returns an array holding the specified
	 * resource name, which is sufficient if each resource is represented as one
	 * data object with the same name as the resource itself
	 * @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getDataNamesForResource(java.lang.String)
	 */
	public String[] getDataNamesForResource(String name) {
		String[] names = {name + "@" + this.getClass().getName()};
		return names;
	}
	
	/**
	 * This default implementation simply returns an empty array, which is
	 * sufficient if the resource does not depend on other resources
	 * @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getRequiredResourceNames(java.lang.String,
	 *      boolean)
	 */
	public String[] getRequiredResourceNames(String name, boolean recourse) {
		return new String[0];
	}
	
	/**
	 * Retrieve a panel offering some explanation regarding the resources
	 * provided by the manager. Note: This is a convenience method, which
	 * retrieves the help content via the getHelp() method, reads it into a
	 * JEditorPane and wraps the latter in a JScrollPane.
	 * @return a JScrollPane with an embedded JEdotorPane displaying the
	 *         resource manager's help entry
	 */
	protected JScrollPane getExplanationLabel() {
		JEditorPane text = new JEditorPane();
		text.setContentType("text/html");
		try {
			text.read(this.getHelp().getTextReader(), new HTMLDocument());
		}
		catch (IOException e) {
			text.setText("<html><body>" +
			"<h3><font face=\"Verdana\">The " + this.getResourceTypeLabel() + "</font></h3>" +
			"<font face=\"Verdana\" size=\"2\">Help on " + this.getResourceTypeLabel() + " is not available, please contact your administrator.</font>" +
			"</body></html>");
		}
		return new JScrollPane(text);
	}
	
	/**
	 * Produce a selector JPanel to place in the GUI
	 * @param label the label String for the panel
	 * @return a selector for the type of Resource managed by this
	 *         DocumentProcessorManager
	 */
	public ResourceSelector getSelector(String label) {
		return this.getSelector(label, null);
	}
	
	/**
	 * Produce a selector JPanel to place in the GUI
	 * @param label the label String for the panel
	 * @param initialSelection the name of the DocumentProcessor initially
	 *            selected
	 * @return a selector for the type of Resource managed by this
	 *         ResourceManager
	 */
	public ResourceSelector getSelector(String label, String initialSelection) {
		return new ResourceSelector(this, initialSelection, label);
	}
	
	/**
	 * Load a resource represented as a string
	 * @param resourceName the name of the resource to load
	 * @return the resource with the specified name, or null, if no such resource exists
	 */
	protected String loadStringResource(String resourceName) {
		try {
			StringWriter sw = new StringWriter();
			BufferedReader br = new BufferedReader(new InputStreamReader(this.dataProvider.getInputStream(resourceName), "UTF-8"));
			for (int c; (c = br.read()) != -1;)
				sw.write((char) c);
			br.close();
			return sw.toString();
		}
		catch (IOException ioe) {
			return null;
		}
	}
	
	/**
	 * Store a resource represented as a string. This method will also notify
	 * the GoldenGATE main instance that the resources of this resource manager
	 * have changed.
	 * @param resourceName the name to store the specified resource with
	 * @param resource the resource to store
	 * @return true if the specified resource was store, false otherwise
	 * @throws IOException if any occurs in the course of storing the resource
	 */
	protected boolean storeStringResource(String resourceName, String resource) throws IOException {
		if (this.dataProvider.isDataEditable(resourceName)) {
			StringReader sr = new StringReader(resource);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.dataProvider.getOutputStream(resourceName), "UTF-8"));
			for (int c; (c = sr.read()) != -1;)
				bw.write((char) c);
			bw.flush();
			bw.close();
			this.parent.notifyResourcesChanged(this.getClass().getName());
			return true;
		}
		else return false;
	}
	
	/**
	 * Load a resource represented as a string vector
	 * @param resourceName the name of the resource to load
	 * @return the resource with the specified name, or null, if no such resource exists
	 */
	protected StringVector loadListResource(String resourceName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.dataProvider.getInputStream(resourceName), "UTF-8"));
			StringVector resource = StringVector.loadList(br);
			br.close();
			return resource;
		}
		catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Store a resource represented as a string vector. This method will also
	 * notify the GoldenGATE main instance that the resources of this resource
	 * manager have changed.
	 * @param resourceName the name to store the specified resource with
	 * @param resource the resource to store
	 * @return true if the specified resource was store, false otherwise
	 * @throws IOException if any occurs in the course of storing the resource
	 */
	protected boolean storeListResource(String resourceName, StringVector resource) throws IOException {
		if (this.dataProvider.isDataEditable(resourceName)) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.dataProvider.getOutputStream(resourceName), "UTF-8"));
			resource.storeContent(bw);
			bw.flush();
			bw.close();
			this.parent.notifyResourcesChanged(this.getClass().getName());
			return true;
		}
		else return false;
	}
	
	/**
	 * Load a resource represented as a Settings object
	 * @param resourceName the name of the resource to load
	 * @return the resource with the specified name, or null, if no such resource exists
	 */
	protected Settings loadSettingsResource(String resourceName) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(this.dataProvider.getInputStream(resourceName), "UTF-8"));
			Settings set = Settings.loadSettings(br);
			br.close();
			return set;
		}
		catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Store a resource represented as a Settings object. This method will also
	 * notify the GoldenGATE main instance that the resources of this resource
	 * manager have changed.
	 * @param resourceName the name to store the specified resource with
	 * @param resource the resource to store
	 * @return true if the specified resource was store, false otherwise
	 * @throws IOException if any occurs in the course of storing the resource
	 */
	protected boolean storeSettingsResource(String resourceName, Settings resource) throws IOException {
		if (this.dataProvider.isDataEditable(resourceName)) {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.dataProvider.getOutputStream(resourceName), "UTF-8"));
			resource.storeAsText(bw);
			bw.flush();
			bw.close();
			this.parent.notifyResourcesChanged(this.getClass().getName());
			return true;
		}
		else return false;
	}
	
	/**
	 * Delete a resource. This method will also notify the GoldenGATE main
	 * instance that the resources of this resource manager have changed.
	 * @param resourceName the name of the resource to delete
	 * @return true if the resource with the specified name was deleted, false
	 *         otherwise (false will also be returned if an exception occurs)
	 */
	protected boolean deleteResource(String resourceName) {
		if ((resourceName != null) && (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), ("Really delete " + resourceName), ("Confirm Delete " + this.getResourceTypeLabel()), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
			try {
				if (this.dataProvider.deleteData(resourceName)) {
					this.parent.notifyResourcesChanged(this.getClass().getName());
					return true;
				}
				else {
					JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("Could not delete " + resourceName), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}
			catch (Exception ioe) {
				JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("Could not delete " + resourceName), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
		}
		else return false;
	}
}
