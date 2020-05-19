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
package de.uka.ipd.idaho.goldenGate.configuration;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.util.ProgressMonitor;
import de.uka.ipd.idaho.goldenGate.CustomFunction;
import de.uka.ipd.idaho.goldenGate.CustomShortcut;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;
import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration.ConfigurationDescriptor;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;
import de.uka.ipd.idaho.goldenGate.TokenizerManager;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilterManager;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSourceManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentEditorExtension;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentFormatProvider;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentLoader;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentSaver;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.htmlXmlUtil.Parser;
import de.uka.ipd.idaho.htmlXmlUtil.TreeNode;
import de.uka.ipd.idaho.htmlXmlUtil.grammars.Grammar;
import de.uka.ipd.idaho.htmlXmlUtil.grammars.StandardGrammar;
import de.uka.ipd.idaho.stringUtils.StringUtils;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Utility library for GoldenGATE configurations
 * 
 * @author sautter
 */
public class ConfigurationUtils implements GoldenGateConstants {
	
	/**Root element of an XML encoded GoldenGATE configuration*/
	public static final String configuration_NODE_TYPE = "configuration";
	
	/**The description of the configuration and what it is intended for*/
	public static final String description_NODE_TYPE = "description";
	
	/**The parent for all basic data specific to the configuration*/
	public static final String data_NODE_TYPE = "data";
	
	/**The actual path. This address can be either relative to the base path, or absolute. In the latter case, the path path must start with "http://"*/
	public static final String path_ATTRIBUTE = "path";
	
	/**The address of the help content, either relative to the base path, or absolute. In the latter case, the help base URL must start with "http://"*/
	public static final String helpBasePath_NODE_TYPE = "helpBasePath";
	
	/**The address of the settings of this configuration, either relative to the base path, or absolute. In the latter case, the settings URL must start with "http://"*/
	public static final String settingsPath_NODE_TYPE = "settingsPath";
	
	/**The address of the image icon, either relative to the base path, or absolute. In the latter case, the help image icon URL must start with "http://"*/
	public static final String iconImagePath_NODE_TYPE = "iconImagePath";
	
	/**The parent for all plugin components to load with this configuration*/
	public static final String plugins_NODE_TYPE = "plugins";
	
	/**The name of the configuration*/
	public static final String name_ATTRIBUTE = "name";
	
	/**The base path of the configuration, either relative to the host the configuration comes from, or absolute. In the latter case, the base path must start with "http://"*/
	public static final String basePath_ATTRIBUTE = "basePath";
	
	/**The timestamp when the configuration was created*/
	public static final String timestamp_ATTRIBUTE = "timestamp";
	
	/**Allow editing the data of this configuration?*/
	public static final String isEditable_ATTRIBUTE = "isEditable";
	
	/**Is this configuration a master configuration?*/
	public static final String isMasterConfiguration_ATTRIBUTE = "isMasterConfiguration";
	
	/**The path of an additional jar file required for the plugin to load.*/
	public static final String lib_NODE_TYPE = "lib";
	
	/**The actual path. This address can be either relative to the path component of the plugin's class path, or absolute. In the latter case, the lib path must start with "http://"*/
	public static final String libPath_ATTRIBUTE = "path";
	
	/**The timestamp when the lib was last modified*/
	public static final String libTimestamp_ATTRIBUTE = "timestamp";
	
	/**The path of an arbitrary data item required for the plugin to load. Resources provided by the plugin should not be represented as data item elements, but as resource elements.*/
	public static final String dataItem_NODE_TYPE = "dataItem";
	
	/**A nice name for the data item, useful if the path is a fully qualified URL*/
	public static final String dataItemName_ATTRIBUTE = "name";
	
	/**The path of the data item. This address can be either relative to the plugin's data path, or absolute. In the latter case, the path must start with "http://"*/
	public static final String dataItemPath_ATTRIBUTE = "path";
	
	/**The timestamp when the data item was last modified*/
	public static final String dataItemTimestamp_ATTRIBUTE = "timestamp";
	
	/**Descriptor for generic plugin to load*/
	public static final String plugin_NODE_TYPE = "plugin";
	
	/**A nice name for the plugin, useful if the class path is a fully qualified URL*/
	public static final String pluginName_ATTRIBUTE = "name";
	
	/**The class name of the plugin, to use by the class loader*/
	public static final String pluginClassName_ATTRIBUTE = "className";
	
	/**The class path of the plugin, in particular the address of the jar file to load it from. This address can be either relative to the base path, or absolute. In the latter case, the class path must start with "http://"*/
	public static final String pluginClassPath_ATTRIBUTE = "classPath";
	
	/**The timestamp when the codebase of the plugin was last modified*/
	public static final String pluginTimestamp_ATTRIBUTE = "timestamp";
	
	/**The data path of the plugin, in particular the address of the base address where it can load its data from. This address can be either relative to the base path, or absolute. In the latter case, the data path must start with "http://"*/
	public static final String pluginDataPath_ATTRIBUTE = "dataPath";
	
	/**Descriptor for plugin required by another plugin*/
	public static final String requiredPlugin_NODE_TYPE = "requiredPlugin";
	
	/**The class name of a plugin required by the plugin represented by the parent node*/
	public static final String reqPluginClassName_ATTRIBUTE = "className";
	
	/**Address of a specific resource descriptor to be available through plugin hosting resources. The latter may be either one of a ResourceManager, a DocumentFormatProvider, or a DocumentIO*/
	public static final String resource_NODE_TYPE = "resource";
	
	/**A nice name for the resource, useful if the path is a fully qualified URL*/
	public static final String resName_ATTRIBUTE = "name";
	
	/**The actual address of the the resource descriptor. This address can be either relative to the plugin's data path, or absolute. In the latter case, the path must start with "http://" */
	public static final String resPath_ATTRIBUTE = "path";
	
	/**The timestamp when the resource was last modified*/
	public static final String resTimestamp_ATTRIBUTE = "timestamp";
	
	/**The name of the required resources*/
	public static final String reqResName_ATTRIBUTE = "name";
	
	/**The class name of the resource manager providing the reqired resource*/
	public static final String reqResManagerClassName_ATTRIBUTE = "managerClassName";
	
	/**A resource another resource depends on*/
	public static final String requiredResource_NODE_TYPE = "requiredResource";
	
	/**Descriptor for the TokenizerManager to use*/
	public static final String tokenizerManager_NODE_TYPE = "tokenizerManager";
	
	/**Descriptor for the CustomFunctionManager to use*/
	public static final String customFunctionManager_NODE_TYPE = "customFunctionManager";
	
	/**Descriptor for the CustomShortcutManager to use*/
	public static final String customShortcutManager_NODE_TYPE = "customShortcutManager";
	
	
	/** Grammar for parsing XML configuration descriptors */
	public static final Grammar configurationGrammar = new ConfigurationGrammar();
	
	private static class ConfigurationGrammar extends StandardGrammar {
		private HashMap childTypesByParentTypes = new HashMap();
		
		public ConfigurationGrammar() {
			Set configurationChildSet = new HashSet();
			configurationChildSet.add(description_NODE_TYPE);
			configurationChildSet.add(data_NODE_TYPE);
			configurationChildSet.add(plugins_NODE_TYPE);
			this.childTypesByParentTypes.put(configuration_NODE_TYPE, configurationChildSet);
			
			Set dataChildSet = new HashSet();
			dataChildSet.add(helpBasePath_NODE_TYPE);
			dataChildSet.add(settingsPath_NODE_TYPE);
			dataChildSet.add(iconImagePath_NODE_TYPE);
			this.childTypesByParentTypes.put(data_NODE_TYPE, dataChildSet);
			
			Set pluginsChildSet = new HashSet();
			pluginsChildSet.add(tokenizerManager_NODE_TYPE);
			pluginsChildSet.add(customFunctionManager_NODE_TYPE);
			pluginsChildSet.add(customShortcutManager_NODE_TYPE);
			pluginsChildSet.add(plugin_NODE_TYPE);
			this.childTypesByParentTypes.put(plugins_NODE_TYPE, pluginsChildSet);
			
			Set pluginChildSet = new HashSet();
			pluginChildSet.add(lib_NODE_TYPE);
			pluginChildSet.add(dataItem_NODE_TYPE);
			pluginChildSet.add(requiredPlugin_NODE_TYPE);
			pluginChildSet.add(resource_NODE_TYPE);
			this.childTypesByParentTypes.put(plugin_NODE_TYPE, pluginChildSet);
			this.childTypesByParentTypes.put(tokenizerManager_NODE_TYPE, pluginChildSet);
			this.childTypesByParentTypes.put(customFunctionManager_NODE_TYPE, pluginChildSet);
			this.childTypesByParentTypes.put(customShortcutManager_NODE_TYPE, pluginChildSet);
			
			Set resourceChildSet = new HashSet();
			resourceChildSet.add(dataItem_NODE_TYPE);
			resourceChildSet.add(requiredResource_NODE_TYPE);
			this.childTypesByParentTypes.put(resource_NODE_TYPE, resourceChildSet);
		}
		public boolean correctErrors() {
			return false;
		}
		public boolean canBeChildOf(String child, String parent) {
			Set childSet = ((Set) this.childTypesByParentTypes.get(parent));
			return ((childSet != null) && childSet.contains(child));
		}
	}
	
	/**
	 * Descriptor of a configuration
	 * 
	 * @author sautter
	 */
	public static class Configuration {
		public final String name;
		
		public final String basePath; // base URL to load the configuration from
		public final long configTimestamp; // last update timestamp of master configuration
		
		public final String helpBasePath; // base URL to load the configuration from
		public final String settingsPath; // path of GoldenGATE.cnfg
		public final String iconImagePath; // base URL to load the configuration from
		
		public final TreeSet plugins = new TreeSet();
		public final TreeMap pluginsByName = new TreeMap();
		public final TreeMap pluginsByClassName = new TreeMap();
		
		public final TreeSet resources = new TreeSet();
		public final TreeMap resourcesByName = new TreeMap();
		
		public final TreeSet dataItems = new TreeSet();
		
		private Properties attributes = new Properties() {
			public String getProperty(String key, String defValue) {
				if (name_ATTRIBUTE.equals(key))
					return Configuration.this.name;
				else if (basePath_ATTRIBUTE.equals(key))
					return Configuration.this.basePath;
				else if (timestamp_ATTRIBUTE.equals(key))
					return ("" + Configuration.this.configTimestamp);
				else if (helpBasePath_NODE_TYPE.equals(key))
					return Configuration.this.helpBasePath;
				else if (settingsPath_NODE_TYPE.equals(key))
					return Configuration.this.settingsPath;
				else if (iconImagePath_NODE_TYPE.equals(key))
					return Configuration.this.iconImagePath;
				else return super.getProperty(key, defValue);
			}
		};
		
		//	used in GG editor core and ECS
		public Configuration(String name, String basePath, long configTimestamp, String helpBasePath, String settingsPath, String iconImagePath) {
			this.name = name;
			this.basePath = basePath;
			this.configTimestamp = configTimestamp;
			this.helpBasePath = helpBasePath;
			this.settingsPath = settingsPath;
			this.iconImagePath = iconImagePath;
		}
		
		//	used exclusively in ECS
		public Configuration(String name, Configuration model) {
			this.name = name;
			this.basePath = model.basePath;
			this.configTimestamp = model.configTimestamp;
			this.helpBasePath = model.helpBasePath;
			this.settingsPath = model.settingsPath;
			this.iconImagePath = model.iconImagePath;
			this.attributes.putAll(model.attributes);
		}
		
		public String[] getAttributeNames() {
			TreeSet ans = new TreeSet(this.attributes.keySet());
			return ((String[]) ans.toArray(new String[ans.size()]));
		}
		
		public String getAttribute(String name) {
			return this.getAttribute(name, null);
		}
		
		public String getAttribute(String name, String defValue) {
			return this.attributes.getProperty(name, defValue);
		}
		
		public String setAttribute(String name, String value) {
			return ((String) this.attributes.setProperty(name, value));
		}
		
		public String removeAttribute(String name) {
			return ((String) this.attributes.remove(name));
		}
		
		public void addPlugin(Plugin plugin) {
			this.plugins.add(plugin);
			this.pluginsByName.put(plugin.name, plugin);
			this.pluginsByClassName.put(plugin.className, plugin);
		}
		
		public void addResource(Resource resource) {
			this.resources.add(resource);
			this.resourcesByName.put(resource.name, resource);
		}
		
		public void addDataItem(DataItem dataItem) {
			this.dataItems.add(dataItem);
		}
		
		public void writeXml(BufferedWriter bw) throws IOException {
			bw.write("<" + configuration_NODE_TYPE + 
					" " + name_ATTRIBUTE + "=\"" + configurationGrammar.escape(this.name) + "\"" +
					" " + basePath_ATTRIBUTE + "=\"" + configurationGrammar.escape(this.basePath) + "\"" +
					" " + timestamp_ATTRIBUTE + "=\"" + this.configTimestamp + "\"" +
					" " + isEditable_ATTRIBUTE + "=\"false\"" +
					" " + isMasterConfiguration_ATTRIBUTE + "=\"false\"");
			String[] ans = this.getAttributeNames();
			for (int a = 0; a < ans.length; a++) {
				if (name_ATTRIBUTE.equals(ans[a]) || basePath_ATTRIBUTE.equals(ans[a]) || timestamp_ATTRIBUTE.equals(ans[a]) || isEditable_ATTRIBUTE.equals(ans[a]) || isMasterConfiguration_ATTRIBUTE.equals(ans[a]))
					continue;
				if (!ans[a].matches("[a-zA-Z][a-zA-Z0-9\\-\\_]*+"))
					continue;
				String value = this.getAttribute(ans[a]);
				if (value == null)
					continue;
				bw.write(" " + ans[a] + "=\"" + configurationGrammar.escape(value) + "\"");
			}
			bw.write(">");
			bw.newLine();
			
			bw.write("<" + description_NODE_TYPE + ">");
			bw.newLine();
			bw.write(configurationGrammar.escape(this.name) + " (auto-generated configuration)");
			bw.newLine();
			bw.write("</" + description_NODE_TYPE + ">");
			bw.newLine();
			
			bw.write("<" + data_NODE_TYPE + ">");
			bw.newLine();
			bw.write("<" + helpBasePath_NODE_TYPE + 
					" " + path_ATTRIBUTE + "=\"" + configurationGrammar.escape(this.helpBasePath) + "\"" +
					"/>");
			bw.newLine();
			bw.write("<" + settingsPath_NODE_TYPE + 
					" " + path_ATTRIBUTE + "=\"" + configurationGrammar.escape(this.settingsPath) + "\"" +
					"/>");
			bw.newLine();
			bw.write("<" + iconImagePath_NODE_TYPE + 
					" " + path_ATTRIBUTE + "=\"" + configurationGrammar.escape(this.iconImagePath) + "\"" +
					"/>");
			bw.newLine();
			for (Iterator dit = this.dataItems.iterator(); dit.hasNext();) {
				DataItem dataItem = ((DataItem) dit.next());
				bw.write("<" + dataItem_NODE_TYPE + 
						" " + dataItemPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(dataItem.path) + "\"" +
						" " + dataItemTimestamp_ATTRIBUTE + "=\"" + dataItem.timestamp + "\"" +
						"/>");
				bw.newLine();
			}
			bw.write("</" + data_NODE_TYPE + ">");
			bw.newLine();
			
			bw.write("<" + plugins_NODE_TYPE + ">");
			bw.newLine();
			
			for (Iterator pit = this.plugins.iterator(); pit.hasNext();)
				this.writePlugin(bw, ((Plugin) pit.next()));
			
			bw.write("</" + plugins_NODE_TYPE + ">");
			bw.newLine();
			
			bw.write("</" + configuration_NODE_TYPE + ">");
			bw.newLine();
			
			bw.flush();
		}
		
		private void writePlugin(BufferedWriter bw, Plugin plugin) throws IOException {
			bw.write("<" + plugin_NODE_TYPE + 
					" " + pluginClassName_ATTRIBUTE + "=\"" + plugin.className + "\"" +
					" " + pluginClassPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(plugin.classPath) + "\"" +
					" " + pluginTimestamp_ATTRIBUTE + "=\"" + plugin.timestamp + "\"" +
					" " + pluginDataPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(plugin.dataPath) + "\"" +
					">");
			bw.newLine();
			
			for (Iterator lit = plugin.libs.iterator(); lit.hasNext();) {
				Lib lib = ((Lib) lit.next());
				bw.write("<" + lib_NODE_TYPE + 
						" " + libPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(lib.path) + "\"" +
						" " + libTimestamp_ATTRIBUTE + "=\"" + lib.timestamp + "\"" +
						"/>");
				bw.newLine();
			}
			
			for (Iterator dit = plugin.dataItems.iterator(); dit.hasNext();) {
				DataItem dataItem = ((DataItem) dit.next());
				bw.write("<" + dataItem_NODE_TYPE + 
						" " + dataItemPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(dataItem.path) + "\"" +
						" " + dataItemTimestamp_ATTRIBUTE + "=\"" + dataItem.timestamp + "\"" +
						"/>");
				bw.newLine();
			}
			
			for (Iterator pit = plugin.requiredPluginClassNames.iterator(); pit.hasNext();) {
				String reqPluginClassName = ((String) pit.next());
				bw.write("<" + requiredPlugin_NODE_TYPE + 
						" " + reqPluginClassName_ATTRIBUTE + "=\"" + reqPluginClassName + "\"" +
						"/>");
				bw.newLine();
			}
			
			for (Iterator rit = plugin.resources.iterator(); rit.hasNext();) {
				Resource res = ((Resource) rit.next());
				if (this.resources.contains(res))
					this.writeResource(bw, res);
			}
			
			bw.write("</" + plugin_NODE_TYPE + ">");
			bw.newLine();
		}
		
		private void writeResource(BufferedWriter bw, Resource resource) throws IOException {
			if (resource.dataItems.isEmpty() && resource.requiredResourceNames.isEmpty()) {
				bw.write("<" + resource_NODE_TYPE + 
						" " + resPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(resource.path) + "\"" +
						" " + resTimestamp_ATTRIBUTE + "=\"" + resource.timestamp + "\"" +
						"/>");
				bw.newLine();
			}
			
			else {
				bw.write("<" + resource_NODE_TYPE + 
						" " + resPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(resource.path) + "\"" +
						" " + resTimestamp_ATTRIBUTE + "=\"" + resource.timestamp + "\"" +
						">");
				bw.newLine();
				
				for (Iterator dit = resource.dataItems.iterator(); dit.hasNext();) {
					DataItem dataItem = ((DataItem) dit.next());
					bw.write("<" + dataItem_NODE_TYPE + 
							" " + dataItemPath_ATTRIBUTE + "=\"" + configurationGrammar.escape(dataItem.path) + "\"" +
							" " + dataItemTimestamp_ATTRIBUTE + "=\"" + dataItem.timestamp + "\"" +
							"/>");
					bw.newLine();
				}
				
				for (Iterator rit = resource.requiredResourceNames.iterator(); rit.hasNext();) {
					Resource reqRes = ((Resource) resourcesByName.get(rit.next()));
					if (reqRes != null) {
						bw.write("<" + requiredResource_NODE_TYPE + 
								" " + reqResName_ATTRIBUTE + "=\"" + (reqRes.name.startsWith("<") ? "" : configurationGrammar.escape(reqRes.name)) + "\"" + // filter out names of built-in resources
								" " + reqResManagerClassName_ATTRIBUTE + "=\"" + reqRes.managerClassName + "\"" +
								"/>");
						bw.newLine();
					}
				}
				
				bw.write("</" + resource_NODE_TYPE + ">");
				bw.newLine();
			}
		}
		
		public static Configuration readConfiguration(InputStream source) throws IOException {
			return readConfiguration(new InputStreamReader(source));
		}
		
		private static final boolean DEBUG = false;
		public static Configuration readConfiguration(Reader source) throws IOException {
			Parser configParser = new Parser(configurationGrammar);
			TreeNode configRoot = configParser.parse(source);
			configRoot = (configuration_NODE_TYPE.equals(configRoot.getNodeType()) ? configRoot : configRoot.getChildNode(configuration_NODE_TYPE, 0));
			
			if (configRoot == null)
				throw new IOException("Invalid configuration data");
			
			else {
				String name = configRoot.getAttribute(name_ATTRIBUTE);
				String basePath = configRoot.getAttribute(basePath_ATTRIBUTE);
				String configTimestamp = configRoot.getAttribute(timestamp_ATTRIBUTE);
				
				String helpBasePath = ""; // base URL to load the configuration from
				String settingsPath = ""; // TODO: assemble settings dynamically
				String iconImagePath = ""; // base URL to load the configuration from
				
				TreeNode dataNode = configRoot.getChildNode(data_NODE_TYPE, 0);
				if (dataNode != null) {
					helpBasePath = dataNode.getChildNode(helpBasePath_NODE_TYPE, 0).getAttribute(path_ATTRIBUTE);
					settingsPath = dataNode.getChildNode(settingsPath_NODE_TYPE, 0).getAttribute(path_ATTRIBUTE);
					iconImagePath = dataNode.getChildNode(iconImagePath_NODE_TYPE, 0).getAttribute(path_ATTRIBUTE);
				}
				
				Configuration configuration = new Configuration(name, basePath, Long.parseLong(configTimestamp), helpBasePath, settingsPath, iconImagePath);
				
				String[] ans = configRoot.getAttributeNames();
				for (int a = 0; a < ans.length; a++) {
					if (configuration.getAttribute(ans[a]) != null) // skip built-in attributes
						continue;
					configuration.setAttribute(ans[a], configRoot.getAttribute(ans[a]));
				}
				
				if (dataNode != null) {
					TreeNode[] dataItemNodes = dataNode.getChildNodes(dataItem_NODE_TYPE);
					for (int d = 0; d < dataItemNodes.length; d++) {
						String dataItemName = dataItemNodes[d].getAttribute(dataItemName_ATTRIBUTE);
						String dataItemPath = dataItemNodes[d].getAttribute(dataItemPath_ATTRIBUTE);
						String dataItemTimestamp = dataItemNodes[d].getAttribute(dataItemTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
						if (dataItemPath != null)
							configuration.dataItems.add(new DataItem(dataItemName, dataItemPath, Long.parseLong(dataItemTimestamp)));
					}
				}
				
				TreeNode pluginsNode = configRoot.getChildNode(plugins_NODE_TYPE, 0);
				if (pluginsNode != null) {
					TreeNode[] pluginNodes = pluginsNode.getChildNodes(plugin_NODE_TYPE);
					for (int p = 0; p < pluginNodes.length; p++) {
						String pluginName = pluginNodes[p].getAttribute(pluginName_ATTRIBUTE);
						String pluginClassName = pluginNodes[p].getAttribute(pluginClassName_ATTRIBUTE);
						String pluginClassPath = pluginNodes[p].getAttribute(pluginClassPath_ATTRIBUTE);
						String pluginTimestamp = pluginNodes[p].getAttribute(pluginTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
						String pluginDataPath = pluginNodes[p].getAttribute(pluginDataPath_ATTRIBUTE);
						if (DEBUG) System.out.println("Got plugin: " + pluginClassName);
						
						Plugin plugin = new Plugin(pluginName, pluginClassName, pluginClassPath, Long.parseLong(pluginTimestamp), pluginDataPath);
						
						TreeNode[] libNodes = pluginNodes[p].getChildNodes(lib_NODE_TYPE);
						for (int l = 0; l < libNodes.length; l++) {
							String libPath = libNodes[l].getAttribute(libPath_ATTRIBUTE);
							String libTimestamp = libNodes[l].getAttribute(libTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
							if (libPath != null)
								plugin.libs.add(new Lib(libPath, Long.parseLong(libTimestamp)));
						}
						
						TreeNode[] dataItemNodes = pluginNodes[p].getChildNodes(dataItem_NODE_TYPE);
						for (int d = 0; d < dataItemNodes.length; d++) {
							String dataItemName = dataItemNodes[d].getAttribute(dataItemName_ATTRIBUTE);
							String dataItemPath = dataItemNodes[d].getAttribute(dataItemPath_ATTRIBUTE);
							String dataItemTimestamp = dataItemNodes[d].getAttribute(dataItemTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
							if (dataItemPath != null)
								plugin.dataItems.add(new DataItem(dataItemName, dataItemPath, Long.parseLong(dataItemTimestamp)));
						}
						
						TreeNode[] reqPluginNodes = pluginNodes[p].getChildNodes(requiredPlugin_NODE_TYPE);
						for (int d = 0; d < reqPluginNodes.length; d++) {
							String reqPluginClassName = reqPluginNodes[d].getAttribute(reqPluginClassName_ATTRIBUTE);
							if (reqPluginClassName != null)
								plugin.requiredPluginClassNames.add(reqPluginClassName);
						}
						
						TreeNode[] resourceNodes = pluginNodes[p].getChildNodes(resource_NODE_TYPE);
						for (int r = 0; r < resourceNodes.length; r++) {
							String resourceName = resourceNodes[r].getAttribute(resName_ATTRIBUTE);
							String resourcePath = resourceNodes[r].getAttribute(resPath_ATTRIBUTE);
							String resourceTimestamp = resourceNodes[r].getAttribute(resTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
							if (DEBUG) System.out.println("  got resource: " + resourcePath);
							if (resourcePath != null) {
								
								//	store resource name instead of path name if given
								Resource resource = new Resource(resourceName, resourcePath, Long.parseLong(resourceTimestamp), pluginClassName);
								
								//	get resource content
								TreeNode[] resourceDataItemNodes = resourceNodes[r].getChildNodes(dataItem_NODE_TYPE);
								for (int d = 0; d < resourceDataItemNodes.length; d++) {
									String dataItemName = resourceDataItemNodes[d].getAttribute(dataItemName_ATTRIBUTE);
									String dataItemPath = resourceDataItemNodes[d].getAttribute(dataItemPath_ATTRIBUTE);
									String dataItemTimestamp = resourceDataItemNodes[d].getAttribute(dataItemTimestamp_ATTRIBUTE, ("" + System.currentTimeMillis()));
									if (dataItemPath != null)
										resource.dataItems.add(new DataItem(dataItemName, dataItemPath, Long.parseLong(dataItemTimestamp)));
								}
								
								//	get required resources (needed for export to client later on)
								TreeNode[] requiredResourceNodes = resourceNodes[r].getChildNodes(requiredResource_NODE_TYPE);
								for (int rr = 0; rr < requiredResourceNodes.length; rr++) {
									String requiredResourceName = requiredResourceNodes[rr].getAttribute(reqResName_ATTRIBUTE);
									if (requiredResourceName != null)
										resource.requiredResourceNames.add(requiredResourceName);
								}
								
								plugin.resources.add(resource);
								configuration.addResource(resource);
							}
						}
						
						configuration.addPlugin(plugin);
					}
				}
				return configuration;
			}
		}
	}
	
	
	/**
	 * Descriptor of an individual plugin
	 * 
	 * @author sautter
	 */
	public static class Plugin implements Comparable {
		public final String name;
		public final String className;
		public final String classPath;
		public final long timestamp;
		public final String dataPath;
		
		public final TreeSet libs = new TreeSet();
		public final TreeSet dataItems = new TreeSet();
		public final TreeSet requiredPluginClassNames = new TreeSet();
		public final TreeSet resources = new TreeSet();
		
		public Plugin(String className, String classPath, long timestamp) {
			this(null, className, classPath, timestamp, null);
		}
		public Plugin(String name, String className, String classPath, long timestamp, String dataPath) {
			this.name = ((name == null) ? className.substring(className.lastIndexOf('.') + 1) : name);
			this.className = className;
			this.classPath = classPath;
			this.timestamp = timestamp;
			if (dataPath == null)
				this.dataPath = (classPath.substring(0, (classPath.length() - 4)) + GoldenGateConstants.JAR_DATA_FOLDER_SUFFIX);
			else this.dataPath = dataPath;
		}
		public boolean equals(Object obj) {
			return ((obj instanceof Plugin) && this.className.equals(((Plugin) obj).className));
		}
		public int hashCode() {
			return this.className.hashCode();
		}
		public int compareTo(Object obj) {
			return ((obj instanceof Plugin) ? this.className.compareTo(((Plugin) obj).className) : -1);
		}
	}
	
	
	/**
	 * Descriptor for a library (jar file) required by a plugin
	 * 
	 * @author sautter
	 */
	public static class Lib implements Comparable {
		public final String path;
		public final long timestamp;
		
		public Lib(String path, long timestamp) {
			this.path = path;
			this.timestamp = timestamp;
		}
		public boolean equals(Object obj) {
			return ((obj instanceof Lib) && this.path.equals(((Lib) obj).path));
		}
		public int hashCode() {
			return this.path.hashCode();
		}
		public int compareTo(Object obj) {
			return ((obj instanceof Lib) ? this.path.compareTo(((Lib) obj).path) : -1);
		}
	}
	
	
	/**
	 * Descriptor for a data item required by a plugin or resource
	 * 
	 * @author sautter
	 */
	public static class DataItem implements Comparable {
		public final String name;
		public final String path;
		public final long timestamp;
		
		public DataItem(String path, long timestamp) {
			this(null, path, timestamp);
		}
		public DataItem(String name, String path, long timestamp) {
			this.name = ((name == null) ? path.substring(path.lastIndexOf('/') + 1) : name);
			this.path = path;
			this.timestamp = timestamp;
		}
		public boolean equals(Object obj) {
			return ((obj instanceof DataItem) && this.path.equals(((DataItem) obj).path));
		}
		public int hashCode() {
			return this.path.hashCode();
		}
		public int compareTo(Object obj) {
			return ((obj instanceof DataItem) ? this.path.compareTo(((DataItem) obj).path) : -1);
		}
	}
	
	
	/**
	 * Descriptor of a resource managed by a plugin
	 * 
	 * @author sautter
	 */
	public static class Resource implements Comparable {
		public final String name;
		public final String path;
		public final long timestamp;
		public final String managerClassName;
		
		public final TreeSet dataItems = new TreeSet();
		public final TreeSet requiredResourceNames = new TreeSet();
		
		public Resource(String path, long timestamp, String managerClassName) {
			this(null, path, timestamp, managerClassName);
		}
		public Resource(String name, String path, long timestamp, String managerClassName) {
			this.name = ((name == null) ? path.substring(path.lastIndexOf('/') + 1) : name);
			this.path = path;
			this.timestamp = timestamp;
			this.managerClassName = managerClassName;
		}
		public boolean equals(Object obj) {
			return ((obj instanceof Resource) && this.path.equals(((Resource) obj).path));
		}
		public int hashCode() {
			return this.path.hashCode();
		}
		public int compareTo(Object obj) {
			return ((obj instanceof Resource) ? this.path.compareTo(((Resource) obj).path) : -1);
		}
	}
	
	/**
	 * Utility interface for export. This interface allows for supplying
	 * dedicated files from special sources other than the main data source.
	 * 
	 * @author sautter
	 */
	public static interface SpecialDataHandler {
		/**
		 * Retrieve an input stream for a specific data item to be exported. If
		 * this method returns null, exporters should retrieve the data item
		 * from the common source, i.e., from the local master configuration.
		 * @param dataName the name of the data item to obtain an input stream
		 *            for
		 * @return an input stream for the data item with the specified name, or
		 *         null, if the data item should be handled the common way
		 */
		public abstract InputStream getInputStream(String dataName) throws IOException;
	}
	
	/**
	 * Create descriptors for the configurations located relative to a given
	 * root folder on the local file system.
	 * @param ggRootPath the root folder of the local GoldenGATE installation
	 * @return descriptors for the configurations located in the specified root
	 *         folder on the local file system
	 */
	public static ConfigurationDescriptor[] getLocalConfigurations(File ggRootPath) {
		File configRoot = new File(ggRootPath, CONFIG_FOLDER_NAME);
		if (!configRoot.exists())
			return new ConfigurationDescriptor[0];
		
		ArrayList configurations = new ArrayList();
		
		File[] configPaths = configRoot.listFiles();
		for (int p = 0; p < configPaths.length; p++) {
			if (configPaths[p].isDirectory() // can be a configuration path
				&& !configPaths[p].getName().endsWith(".old") // is not an old configuration
				&& !configPaths[p].getName().endsWith(".updating") // is not an incomplete download or update attempt
				&& (new File(configPaths[p], GoldenGateConfiguration.TIMESTAMP_NAME).exists()) // timestamp exists
			) try {
				configurations.add(new ConfigurationDescriptor(null, configPaths[p].getName(), getTimestamp(configPaths[p])));
			}
			catch (IOException ioe) {
				System.out.println(" - Error: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
			}
		}
		
		return ((ConfigurationDescriptor[]) configurations.toArray(new ConfigurationDescriptor[configurations.size()]));
	}
	
	private static long getTimestamp(File configPath) throws IOException {
		BufferedReader timestampReader = new BufferedReader(new FileReader(new File(configPath, GoldenGateConfiguration.TIMESTAMP_NAME)));
		long timestamp = Long.parseLong(timestampReader.readLine());
		timestampReader.close();
		return timestamp;
	}
	
	/**
	 * Create descriptors for the configurations located relative to a given
	 * root folder on the local file system wrapped up in a zip file. The host
	 * name of the returned descriptors (if any) will be set to 'ZIP'.
	 * @param ggRootPath the root folder of the local GoldenGATE installation
	 * @return descriptors for the configurations located in the specified root
	 *         folder on the local file system wrapped up in a zip file
	 */
	public static ConfigurationDescriptor[] getZipConfigurations(File ggRootPath) {
		File configRoot = new File(ggRootPath, CONFIG_FOLDER_NAME);
		if (!configRoot.exists())
			return new ConfigurationDescriptor[0];
		
		ArrayList configurations = new ArrayList();
		
		File[] configZips = configRoot.listFiles();
		for (int z = 0; z < configZips.length; z++) {
			if (configZips[z].isFile() && configZips[z].getName().endsWith(".zip")) try {
				ZipFile configZip = new ZipFile(configZips[z]);
				
				ZipEntry timestampZipEntry = configZip.getEntry(GoldenGateConfiguration.TIMESTAMP_NAME);
				if (timestampZipEntry == null)
					continue;
				
				BufferedReader timestampReader = new BufferedReader(new InputStreamReader(configZip.getInputStream(timestampZipEntry)));
				long timestamp = Long.parseLong(timestampReader.readLine());
				timestampReader.close();
				
				String configName = configZips[z].getName();
				configName = configName.substring(0, (configName.length() - ".zip".length()));
				
				configurations.add(new ConfigurationDescriptor(zipConfigHostName, configName, timestamp));
				
				configZip.close();
			}
			catch (IOException ioe) {
				System.out.println(" - Error: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
			}
		}
		
		return ((ConfigurationDescriptor[]) configurations.toArray(new ConfigurationDescriptor[configurations.size()]));
	}
	
	private static final String zipConfigHostName = "Local ZIP";
	
	/**
	 * Create descriptors for the configurations located relative to a given URL
	 * on a remote server.
	 * @param configHost the root URL
	 * @return descriptors for the configurations located relative to the
	 *         specified URL on a remote server
	 */
	public static ConfigurationDescriptor[] getRemoteConfigurations(String configHost) {
		if (!configHost.endsWith("/")) configHost += "/";
		System.out.println("Start downloading configuration meta data from '" + configHost + "' ...");
		
		ArrayList configurations = new ArrayList();
		try {
			BufferedReader fileIndexReader = new BufferedReader(new InputStreamReader(new URL(configHost + GoldenGateConfiguration.FILE_INDEX_NAME).openStream()));
			String fileName;
			while ((fileName = fileIndexReader.readLine()) != null) try {
				configurations.add(new ConfigurationDescriptor(configHost, fileName, getTimestamp(configHost + fileName)));
				System.out.println(" - Found config: " + fileName);
			}
			catch (IOException ioe) {
				System.out.println(" - Error: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
			}
			fileIndexReader.close();
		}
		catch (Exception e) {
			System.out.println(" - Error: " + e.getClass().getName() + " (" + e.getMessage() + ")");
			e.printStackTrace(System.out);
		}
		
		return ((ConfigurationDescriptor[]) configurations.toArray(new ConfigurationDescriptor[configurations.size()]));
	}
	
	private static long getTimestamp(String configUrl) throws IOException {
		BufferedReader timestampReader = new BufferedReader(new InputStreamReader(new URL(configUrl + (configUrl.endsWith("/") ? "" : "/") + GoldenGateConfiguration.TIMESTAMP_NAME).openStream()));
		long timestamp = Long.parseLong(timestampReader.readLine());
		timestampReader.close();
		return timestamp;
	}

	/**
	 * Get the descriptor of the configuration with a given name from a list of
	 * configuration descriptors, installing updates from remote hosts in case
	 * they exist and a user authorizes the update. If the specified
	 * configuration name is null or the specified list of configuration
	 * descriptors does not contain a descriptor with the specified name, this
	 * method returns null.
	 * @param configurations the configuration descriptors to choose from
	 * @param configName the name of the configuration to obtain a descriptor
	 *            for
	 * @param dataBasePath the base path of the local GoldenGATE installation
	 * @param showStatusOnUpdate show the download/update progress in a dialog
	 *            in case of a download or update?
	 * @return a descriptor for the configuration with the specified name
	 */
	public static ConfigurationDescriptor getConfiguration(ConfigurationDescriptor[] configurations, String configName, File dataBasePath, boolean showStatusOnUpdate) {
		return getConfiguration(configurations, configName, dataBasePath, CACHE_UPDATE_POLICY_MOVE, true, showStatusOnUpdate);
	}
	
	/**
	 * Get the descriptor of the configuration with a given name from a list of
	 * configuration descriptors, installing updates from remote hosts in case
	 * they exist and a user authorizes the update. If the specified
	 * configuration name is null or the specified list of configuration
	 * descriptors does not contain a descriptor with the specified name, this
	 * method returns null.
	 * @param configurations the configuration descriptors to choose from
	 * @param configName the name of the configuration to obtain a descriptor
	 *            for
	 * @param dataBasePath the base path of the local GoldenGATE installation
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @param showStatusOnUpdate show the download/update progress in a dialog
	 *            in case of a download or update?
	 * @return a descriptor for the configuration with the specified name
	 */
	public static ConfigurationDescriptor getConfiguration(ConfigurationDescriptor[] configurations, String configName, File dataBasePath, int cachePolicy, boolean showStatusOnUpdate) {
		return getConfiguration(configurations, configName, dataBasePath, cachePolicy, true, showStatusOnUpdate);
	}
	
	/**
	 * Get the descriptor of the configuration with a given name from a list of
	 * configuration descriptors, installing updates from remote hosts in case
	 * they exist and a user authorizes the update. If the specified
	 * configuration name is null or the specified list of configuration
	 * descriptors does not contain a descriptor with the specified name, this
	 * method returns null.
	 * @param configurations the configuration descriptors to choose from
	 * @param configName the name of the configuration to obtain a descriptor
	 *            for
	 * @param dataBasePath the base path of the local GoldenGATE installation
	 * @param askUpdatePermission ask before updating a local configuration from
	 *            a more recent remote source?
	 * @param showStatusOnUpdate show the download/update progress in a dialog
	 *            in case of a download or update?
	 * @return a descriptor for the configuration with the specified name
	 */
	public static ConfigurationDescriptor getConfiguration(ConfigurationDescriptor[] configurations, String configName, File dataBasePath, boolean askUpdatePermission, boolean showStatusOnUpdate) {
		return getConfiguration(configurations, configName, dataBasePath, CACHE_UPDATE_POLICY_MOVE, askUpdatePermission, showStatusOnUpdate);
	}
	
	/**
	 * Get the descriptor of the configuration with a given name from a list of
	 * configuration descriptors, installing updates from remote hosts in case
	 * they exist and a user authorizes the update. If the specified
	 * configuration name is null or the specified list of configuration
	 * descriptors does not contain a descriptor with the specified name, this
	 * method returns null.
	 * @param configurations the configuration descriptors to choose from
	 * @param configName the name of the configuration to obtain a descriptor
	 *            for
	 * @param dataBasePath the base path of the local GoldenGATE installation
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @param askUpdatePermission ask before updating a local configuration from
	 *            a more recent remote source?
	 * @param showStatusOnUpdate show the download/update progress in a dialog
	 *            in case of a download or update?
	 * @return a descriptor for the configuration with the specified name
	 */
	public static ConfigurationDescriptor getConfiguration(ConfigurationDescriptor[] configurations, String configName, File dataBasePath, int cachePolicy, boolean askUpdatePermission, boolean showStatusOnUpdate) {
		if (configName == null)
			return null;
		
		if (GraphicsEnvironment.isHeadless())
			showStatusOnUpdate = false;
		
		//	find local and remote versions of selected configuration
		ConfigurationDescriptor localConfig = null;
		ConfigurationDescriptor zippedConfig = null;
		ConfigurationDescriptor remoteConfig = null;
		for (int c = 0; c < configurations.length; c++)
			if (configurations[c].name.equals(configName)) {
				if (configurations[c].host == null)
					localConfig = configurations[c];
				else if (zipConfigHostName.equals(configurations[c].host))
					zippedConfig = configurations[c];
				else if ((remoteConfig == null) || (remoteConfig.timestamp < configurations[c].timestamp))
					remoteConfig = configurations[c];
			}
		
		//	no configuration found with the specified name
		if ((localConfig == null) && (zippedConfig == null) && (remoteConfig == null))
			return null;
		
		/*
		 * do update from zipped configuration without asking (zip should be
		 * present for some purpose ...), still allowing instant online update
		 * to facilitate updating configurations that have been distributed on
		 * storage media rather than downloaded
		 */
		if (isMoreRecentThan(zippedConfig, localConfig)) try {
			localConfig = updateLocalConfiguration(localConfig, zippedConfig, dataBasePath, cachePolicy, askUpdatePermission, showStatusOnUpdate);
		}
		catch (IOException ioe) {
			System.out.println(" - Error extracting file of zipped configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
			ioe.printStackTrace(System.out);
			if (showStatusOnUpdate)
				JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("An error occurred while extracting the selected Configuration:\n  " + ioe.getMessage() + "."), "Configuration Extraction Error", JOptionPane.ERROR_MESSAGE);
//			no need to return immediately, we might still get something from the web
//			return (askPermission(("An error occurred while extracting the selected Configuration:\n  " + ioe.getMessage() + ".\nShould GoldenGATE start with the older local Configuration?"), "Configuration Extraction Error", JOptionPane.ERROR_MESSAGE) ? localConfig : null);
		}
		
		//	local configuration not found
		if (localConfig == null) {
			
			//	ask if download desired
//			if (!askUpdatePermission || askPermission(("The selected Configuration is not available locally.\nShould GoldenGATE download it and make it a local Configuration?"), "Download Configuration?", JOptionPane.QUESTION_MESSAGE)) {
//				try {
//					return updateLocalConfiguration(null, remoteConfig, dataBasePath, showStatusOnUpdate);
//				}
//				catch (IOException ioe) {
//					System.out.println(" - Error downloading remote configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
//					ioe.printStackTrace(System.out);
//					if (showStatusOnUpdate)
//						JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("An error occurred while downloading the selected Configuration:\n  " + ioe.getMessage() + "."), "Configuration Download Error", JOptionPane.ERROR_MESSAGE);
//					return null;
//				}
//			}
			boolean downloadConfig = true;
			if (askUpdatePermission)
				downloadConfig = askPermission(("The selected Configuration is not available locally.\nShould GoldenGATE download it and make it a local Configuration?"), "Download Configuration?", JOptionPane.QUESTION_MESSAGE);
			
			//	if not, use remote configuration
			if (!downloadConfig)
				return remoteConfig;
			
			//	download configuration
			try {
				return updateLocalConfiguration(null, remoteConfig, dataBasePath, cachePolicy, askUpdatePermission, showStatusOnUpdate);
			}
			catch (IOException ioe) {
				System.out.println(" - Error downloading remote configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
				ioe.printStackTrace(System.out);
				if (showStatusOnUpdate)
					JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), ("An error occurred while downloading the selected Configuration:\n  " + ioe.getMessage() + "."), "Configuration Download Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		}
		
		//	got local configuration, check if newer remote version of local configuration available
		else if (isMoreRecentThan(remoteConfig, localConfig)) {
			
			//	got remote configuration, ask if update desired
//			if ((!askUpdatePermission || askPermission(("There is a more recent version of the selected Configuration available at " + remoteConfig.host + ".\nShould GoldenGATE download the new version?"), "Download new Version of Configuration?", JOptionPane.QUESTION_MESSAGE))) {
//				try {
//					return updateLocalConfiguration(localConfig, remoteConfig, dataBasePath, showStatusOnUpdate);
//				}
//				catch (IOException ioe) {
//					System.out.println(" - Error downloading remote configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
//					ioe.printStackTrace(System.out);
//					return ((!askUpdatePermission || askPermission(("An error occurred while downloading the selected Configuration:\n  " + ioe.getMessage() + ".\nShould GoldenGATE start with the older local Configuration?"), "Configuration Download Error", JOptionPane.ERROR_MESSAGE)) ? localConfig : null);
//				}
//			}
			boolean updateConfig = true;
			if (askUpdatePermission)
				updateConfig = askPermission(("There is a more recent version of the selected Configuration available at " + remoteConfig.host + ".\nShould GoldenGATE download the new version?"), "Download new Version of Configuration?", JOptionPane.QUESTION_MESSAGE);
			
			//	retain old local configuration
			if (!updateConfig)
				return localConfig;
			
			//	update local configuration
			try {
				return updateLocalConfiguration(localConfig, remoteConfig, dataBasePath, cachePolicy, askUpdatePermission, showStatusOnUpdate);
			}
			catch (IOException ioe) {
				System.out.println(" - Error downloading remote configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
				ioe.printStackTrace(System.out);
				boolean useLocalConfig = true;
				if (askUpdatePermission)
					useLocalConfig = askPermission(("An error occurred while downloading the selected Configuration:\n  " + ioe.getMessage() + ".\nShould GoldenGATE start with the older local Configuration?"), "Configuration Download Error", JOptionPane.ERROR_MESSAGE);
				return (useLocalConfig ? localConfig : null);
			}
		}
		
		//	just use up to date local configuration
		else return localConfig;
	}
	
	/** cache update policy indicating to ignore all cached content in an existing local configuration on update */
	public static final int CACHE_UPDATE_POLICY_IGNORE = 0;
	
	/** cache update policy indicating to copy all cached content from an existing local configuration on update */
	public static final int CACHE_UPDATE_POLICY_COPY = 1;
	
	/** cache update policy indicating to scrub all cached content in an existing local configuration on update */
	public static final int CACHE_UPDATE_POLICY_SCRUB = 2;
	
	/** cache update policy indicating to move over all cached content from an existing local configuration on update */
	public static final int CACHE_UPDATE_POLICY_MOVE = (CACHE_UPDATE_POLICY_COPY | CACHE_UPDATE_POLICY_SCRUB);
	
	private static boolean askPermission(String message, String title, int messageType) {
		if (GraphicsEnvironment.isHeadless())
			return true; // if we're in a headless environment, there is probably no user waiting, so we can safely assume that updates are wanted, and that we are to start even if an update goes wrong
		return (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), message, title, JOptionPane.YES_NO_OPTION, messageType) == JOptionPane.YES_OPTION);
	}
//	private static int askPermission(String message, String title, int messageType, int cachePolicy) {
//		if (GraphicsEnvironment.isHeadless())
//			return cachePolicy; // if we're in a headless environment, there is probably no user waiting, so we can safely assume that updates are wanted, and that we are to start even if an update goes wrong
//		
//		JPanel askPanel = new JPanel(new BorderLayout(), true);
//		askPanel.add(new JLabel(("<HTML>" + message.replaceAll("\\r?\\n", "<BR>") + "</HTML>"), JLabel.CENTER), BorderLayout.CENTER);
//		
//		JComboBox cachePolicySelector;
//		if (cachePolicy < 0)
//			cachePolicySelector = null;
//		else {
//			cachePolicySelector = new JComboBox(cachePolicyOptions);
//			cachePolicySelector.setEditable(false);
//			cachePolicySelector.setSelectedItem(cachePolicyOptions[cachePolicy]);
//			
//			JPanel cachePolicyPanel = new JPanel(new BorderLayout(), true);
//			cachePolicyPanel.add(new JLabel("How to handle cached content in local configuration? ", JLabel.RIGHT), BorderLayout.CENTER);
//			cachePolicyPanel.add(cachePolicySelector, BorderLayout.EAST);
//			askPanel.add(cachePolicyPanel, BorderLayout.SOUTH);
//		}
//		
//		int choice = JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), askPanel, title, JOptionPane.YES_NO_OPTION, messageType);
//		if (choice != JOptionPane.YES_OPTION)
//			return -1;
//		
//		if (cachePolicy < 0)
//			return CACHE_UPDATE_POLICY_IGNORE; // we were not asked for a cache policy, so it does not matter
//		
//		return ((CachePolicyTray) cachePolicySelector.getSelectedItem()).policy; // return selected cache handling policy
//	}
	
	private static class CachePolicy {
		final int policy;
		final boolean copy;
		final boolean cleanup;
		CachePolicy(int policy) {
			this.policy = policy;
			this.copy = ((policy & CACHE_UPDATE_POLICY_COPY) != 0);
			this.cleanup = ((policy & CACHE_UPDATE_POLICY_SCRUB) != 0);
		}
		boolean move() {
			return (this.copy && this.cleanup);
		}
	}
	
	private static class CachePolicyTray {
		final int policy;
		final String label;
		CachePolicyTray(int policy, String label) {
			this.policy = policy;
			this.label = label;
		}
		public String toString() {
			return this.label;
		}
	}
	
	private static final CachePolicyTray[] cachePolicyOptions = {
		new CachePolicyTray(CACHE_UPDATE_POLICY_IGNORE, "Ignore Altogether"),
		new CachePolicyTray(CACHE_UPDATE_POLICY_COPY, "Copy Over"),
		new CachePolicyTray(CACHE_UPDATE_POLICY_SCRUB, "Clean Up"),
		new CachePolicyTray(CACHE_UPDATE_POLICY_IGNORE, "Move Along"),
	};
	
	private static class CachePolicySelector {
		final String path;
		final String label;
		final JComboBox policy = new JComboBox(cachePolicyOptions);
		CachePolicySelector(String path, String label, int policy) {
			this.path = path;
			this.label = label;
			this.policy.setEditable(false);
			this.policy.setSelectedItem(cachePolicyOptions[policy]);
		}
		CachePolicy getCachePolicy() {
			int policy = ((CachePolicyTray) this.policy.getSelectedItem()).policy;
			return new CachePolicy(policy);
		}
	}
	
	private static HashMap askCachePolicies(ArrayList cacheFolderNames, Settings exCachePolicies, int defaultCachePolicy) {
		CachePolicySelector[] cachePolicies = new CachePolicySelector[cacheFolderNames.size()];
		
		JPanel cachePolicyPanel = new JPanel(new GridLayout(0, 2), true);
		StringVector cacheFolderNameParser = new StringVector();
		for (int c = 0; c < cacheFolderNames.size(); c++) {
			String cacheFolderName = ((String) cacheFolderNames.get(c));
			String exCachePolicy = exCachePolicies.getSetting(getCachePolicyKey(cacheFolderName), ("" + defaultCachePolicy));
			cacheFolderNameParser.parseAndAddElements(cacheFolderName, "/");
			cacheFolderNameParser.remove(cacheFolderNameParser.size()-1);
			while (cacheFolderNameParser.size() > 2) // using last two path steps before '/cache' as label
				cacheFolderNameParser.remove(0);
			cachePolicies[c] = new CachePolicySelector(cacheFolderName, cacheFolderNameParser.concatStrings(" / "), Integer.parseInt(exCachePolicy));
			cacheFolderNameParser.clear();
			JLabel cachePolicyLabel = new JLabel((cachePolicies[c].label + "  "), JLabel.RIGHT);
			cachePolicyLabel.setToolTipText("Full path: " + cachePolicies[c].path);
			cachePolicyPanel.add(cachePolicyLabel);
			cachePolicyPanel.add(cachePolicies[c].policy);
		}
		
		JCheckBox rememberCachePolicy = new JCheckBox("Remember my Choices?", true);
		
		JPanel askCachePolicyPanel = new JPanel(new BorderLayout(), true);
		askCachePolicyPanel.add(new JLabel("<HTML><B>How to handle cached content in local configuration?</B></HTML>", JLabel.CENTER), BorderLayout.NORTH);
		askCachePolicyPanel.add(cachePolicyPanel, BorderLayout.CENTER);
		askCachePolicyPanel.add(rememberCachePolicy, BorderLayout.SOUTH);
		
		int choice = JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), askCachePolicyPanel, "How to Handle Cached Data?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if (choice != JOptionPane.OK_OPTION)
			return null;
		
		HashMap folderCachePolicies = new HashMap();
		for (int c = 0; c < cachePolicies.length; c++)
			folderCachePolicies.put(cachePolicies[c].path, cachePolicies[c].getCachePolicy());
		if (rememberCachePolicy.isSelected())
			folderCachePolicies.put(rememberCachePolicies, rememberCachePolicies);
		
		return folderCachePolicies;
	}
	
	private static final String rememberCachePolicies = "rememberCachePolicies";
	
	private static String getCachePolicyKey(String cacheFolderName) {
		return cacheFolderName.replaceAll("[^a-zA-Z0-9\\-\\_]+", "_");
	}
	
	/**
	 * The tolerance (in milliseconds) within which two configuration timestamps
	 * are considered equal. This fuzziness is necessary to alleviate gaps
	 * between exports of the same configuration to different locations, and
	 * system time differences between configuration hosts and client computers.
	 * The value is equal to 10 minutes, which alleviates rather vast time
	 * differences. On the other hand, it requires an update to a configuration
	 * to be at least 10 minutes more recent than the version installed locally
	 * to be recognized as an update.
	 */
	private static final int configTimestampComparisonFuzzienessMillis = (10 * 60 * 1000);
	
	/**
	 * Compare two configurations. this method returns true if the timestamp of
	 * the first configuration is more recent than that of the second one by at
	 * least the configuration timestamp comparison fuzziness. The equality
	 * relation established by this method through comparing two configurations
	 * both ways, returning false each way, is not necessarily transitive. Thus,
	 * this method should not be used for ordering.
	 * @param cd1 the first configuration
	 * @param cd2 the second configuration
	 * @return true if the first configuration is more recent than the second
	 *         one
	 */
	private static boolean isMoreRecentThan(ConfigurationDescriptor cd1, ConfigurationDescriptor cd2) {
		if (cd1 == null)
			return false;
		if (cd2 == null)
			return true;
		return ((cd1.timestamp - configTimestampComparisonFuzzienessMillis) > cd2.timestamp);
	}
	
	/* *
	 * Update a local configuration with one located on a remote server. If the
	 * local configuration descriptor is null, this method simply downloads the
	 * complete remote configuration and creates a local one from it.
	 * @param local the descriptor of the local configuration to update (may be
	 *            null for initial downloads)
	 * @param remote the descriptor of the remote configuration to download
	 *            updates from
	 * @param ggRootPath the root folder of the local GoldenGATE installation,
	 *            to store the local configuration
	 * @param cachePolicy how to handle cached content present in the local
	 *            configuration?
	 * @param showStatus show the download progress in a dialog?
	 * @return a descriptor for the downloaded/updated local configuration
	 */
	private static ConfigurationDescriptor updateLocalConfiguration(ConfigurationDescriptor local, ConfigurationDescriptor remote, File ggRootPath, int cachePolicy, boolean askCachePolicy, boolean showStatus) throws IOException {
		File configRoot = new File(ggRootPath, GoldenGateConstants.CONFIG_FOLDER_NAME);
		
		//	create local config folder
		File localConfigFolder = ((local == null) ? null : new File(configRoot, local.name));
		
		//	create update config folder
		File updateConfigFolder = new File(configRoot, (remote.name + "." + remote.timestamp + ".updating"));
		updateConfigFolder.mkdirs();
		
		//	open status dialog
		String title = ((zipConfigHostName.equals(remote.host) ? "Un-Zipping" : ((local == null) ? "Downloading" : "Updating")) + " Configuration " + remote.name);
		System.out.println(title);
		DownloadStatusDialog dsd = (showStatus ? new DownloadStatusDialog(Toolkit.getDefaultToolkit().getImage(new File(new File(ggRootPath, GoldenGateConstants.DATA_FOLDER_NAME), GoldenGateConstants.ICON_FILE_NAME).toString()), title) : null);
		if (dsd != null)
			dsd.popUp();
		
		try {
			StringVector fileList = new StringVector();
			StringVector dataFolders = new StringVector(); // collect parent folders of possible caches
			
			//	unzip configuration
			if (zipConfigHostName.equals(remote.host)) {
				File configZipBaseFile = new File(configRoot, (remote.name + ".zip"));
				ZipFile configZipFile = new ZipFile(configZipBaseFile);
				
				//	report status
				System.out.println(" - collecting data folders");
				if (dsd != null)
					dsd.setStepLabel("Collecting Data Folders");
				
				//	read file list
				ZipEntry configZipEntry = configZipFile.getEntry(GoldenGateConfiguration.FILE_INDEX_NAME);
				BufferedReader fileIndexReader = new BufferedReader(new InputStreamReader(configZipFile.getInputStream(configZipEntry)));
				String fileName;
				while ((fileName = fileIndexReader.readLine()) != null) {
					fileList.addElement(fileName);
					if (fileName.endsWith(".jar"))
						dataFolders.addElementIgnoreDuplicates(fileName.substring(0, (fileName.length() - ".jar".length())) + JAR_DATA_FOLDER_SUFFIX);
				}
				fileIndexReader.close();
				
				//	extract zipped files one by one
				if (dsd != null)
					dsd.setStepLabel("Un-Zipping Data");
				for (int f = 0; f < fileList.size(); f++) {
					fileName = fileList.get(f);
					configZipEntry = configZipFile.getEntry(fileName);
					
					//	report status
					System.out.println(" - un-zipping " + fileName);
					if (dsd != null) {
						dsd.setStatusLabel(" - un-zipping " + fileName);
						dsd.setProgressPercent((100 * f) / fileList.size());
					}
					
					//	get source and timestamp
					try {
						InputStream source = configZipFile.getInputStream(configZipEntry);
						long sourceLastModified = configZipEntry.getTime();
						System.out.println("   - last modified " + sourceLastModified);
						
						updateFile(localConfigFolder, updateConfigFolder, fileName, source, sourceLastModified);
					}
					catch (NullPointerException npe) {
						System.out.println("   - missing ZIP entry: " + fileName);
					}
				}
				
				configZipFile.close();
				configZipBaseFile.renameTo(new File(configRoot, (remote.name + ".zip." + System.currentTimeMillis() + ".installed")));
			}
			
			//	download remote configuration
			else {
				
				//	set up timestamp caching
				HashMap timestamps = new HashMap();
				
				//	report status
				if (dsd != null)
					dsd.setStepLabel("Extracting File List");
				
				//	download configuration descriptor
				try {
					BufferedReader configReader = new BufferedReader(new InputStreamReader(new URL(remote.host + (remote.host.endsWith("/") ? "" : "/") + remote.name + "/" + GoldenGateConfiguration.DESCRIPTOR_FILE_NAME).openStream(), "UTF-8"));
					Configuration config = Configuration.readConfiguration(configReader);
					System.out.println(" - extracting file list from XML descriptor");
					if (dsd != null)
						dsd.setStatusLabel(" - extracting file list from XML descriptor");
					
					//	extract file list and timestamps
					fileList.addElementIgnoreDuplicates(config.settingsPath);
					timestamps.put(config.settingsPath, new Long(config.configTimestamp));
					fileList.addElementIgnoreDuplicates(config.iconImagePath);
					timestamps.put(config.iconImagePath, new Long(config.configTimestamp));
					fileList.addElementIgnoreDuplicates(GoldenGateConfiguration.DESCRIPTOR_FILE_NAME);
					timestamps.put(GoldenGateConfiguration.DESCRIPTOR_FILE_NAME, new Long(config.configTimestamp));
					for (Iterator dit = config.dataItems.iterator(); dit.hasNext();) {
						DataItem di = ((DataItem) dit.next());
						fileList.addElementIgnoreDuplicates(di.path);
						timestamps.put(di.path, new Long(di.timestamp));
					}
					
					for (Iterator pit = config.plugins.iterator(); pit.hasNext();) {
						Plugin plugin = ((Plugin) pit.next());
						if (plugin.classPath.length() != 0) {
							fileList.addElementIgnoreDuplicates(plugin.classPath);
							timestamps.put(plugin.classPath, new Long(plugin.timestamp));
						}
						
						String libPath = ((plugin.classPath.lastIndexOf('/') == -1) ? "" : plugin.classPath.substring(0, plugin.classPath.lastIndexOf('/') + 1));
						for (Iterator lit = plugin.libs.iterator(); lit.hasNext();) {
							Lib lib = ((Lib) lit.next());
							fileList.addElementIgnoreDuplicates(libPath + lib.path);
							timestamps.put((libPath + lib.path), new Long(lib.timestamp));
						}
						
						String dataPath = (plugin.dataPath.endsWith("/") ? plugin.dataPath : (plugin.dataPath + "/"));
						dataFolders.addElementIgnoreDuplicates(plugin.dataPath.endsWith("/") ? plugin.dataPath.substring(0, (plugin.dataPath.length() - "/".length())) : plugin.dataPath);
						for (Iterator dit = plugin.dataItems.iterator(); dit.hasNext();) {
							DataItem di = ((DataItem) dit.next());
							fileList.addElementIgnoreDuplicates(dataPath + di.path);
							timestamps.put((dataPath + di.path), new Long(di.timestamp));
						}
						
						for (Iterator rit = plugin.resources.iterator(); rit.hasNext();) {
							Resource resource = ((Resource) rit.next());
							if (resource.path.length() != 0) {
								fileList.addElementIgnoreDuplicates(dataPath + resource.path);
								timestamps.put((dataPath + resource.path), new Long(resource.timestamp));
							}
							for (Iterator dit = resource.dataItems.iterator(); dit.hasNext();) {
								DataItem di = ((DataItem) dit.next());
								fileList.addElementIgnoreDuplicates(dataPath + di.path);
								timestamps.put((dataPath + di.path), new Long(di.timestamp));
							}
						}
					}
				}
				
				//	resort to classical file list if descriptor not found
				catch (IOException ioe) {
					System.out.println(" - XML descriptor not available, using plain file list");
					BufferedReader fileIndexReader = new BufferedReader(new InputStreamReader(new URL(remote.host + (remote.host.endsWith("/") ? "" : "/") + remote.name + "/" + GoldenGateConfiguration.FILE_INDEX_NAME).openStream()));
					String fileName;
					while ((fileName = fileIndexReader.readLine()) != null) {
						fileList.addElement(fileName);
						if (fileName.endsWith(".jar"))
							dataFolders.addElementIgnoreDuplicates(fileName.substring(0, (fileName.length() - ".jar".length())) + JAR_DATA_FOLDER_SUFFIX);
					}
					fileIndexReader.close();
				}
				
				
				//	copy remote files one by one
				if (dsd != null)
					dsd.setStepLabel("Downloading Data");
				for (int f = 0; f < fileList.size(); f++) {
					String fileName = fileList.get(f);
					
					//	report status
					System.out.println(" - downloading " + fileName);
					if (dsd != null) {
						dsd.setStatusLabel(" - downloading " + fileName);
						dsd.setProgressPercent((100 * f) / fileList.size());
					}
					
					//	get timestamp
					Long timestamp = ((Long) timestamps.get(fileName));
					
					//	create source connection
					InputStream source;
					long sourceLastModified;
					if (timestamp == null) {
						URLConnection sourceUrlConnection = new URL(remote.host + (remote.host.endsWith("/") ? "" : "/") + remote.name + "/" + fileName).openConnection();
						source = sourceUrlConnection.getInputStream();
						sourceLastModified = sourceUrlConnection.getLastModified();
					}
					else {
						final URLConnection sourceUrlConnection = new URL(remote.host + (remote.host.endsWith("/") ? "" : "/") + remote.name + "/" + fileName).openConnection();
						source = new InputStream() {
							InputStream iSource = null;
							public void close() throws IOException {
								if (this.iSource != null)
									this.iSource.close();
							}
							public int read() throws IOException {
								if (this.iSource == null)
									this.iSource = sourceUrlConnection.getInputStream();
								return this.iSource.read();
							}
							public int read(byte[] b, int off, int len) throws IOException {
								if (this.iSource == null)
									this.iSource = sourceUrlConnection.getInputStream();
								return this.iSource.read(b, off, len);
							}
						};
						sourceLastModified = timestamp.longValue();
					}
					System.out.println("   - last modified " + sourceLastModified);
					
					updateFile(localConfigFolder, updateConfigFolder, fileName, source, sourceLastModified);
				}
			}
			
			//	store file list
			fileList.sortLexicographically(false);
			fileList.storeContent(new File(updateConfigFolder, GoldenGateConfiguration.FILE_INDEX_NAME));
			
			//	write timestamp
			StringVector timestamper = new StringVector();
			timestamper.addElement("" + remote.timestamp);
			timestamper.storeContent(new File(updateConfigFolder, GoldenGateConfiguration.TIMESTAMP_NAME));
			
			//	transfer user defined resources (to prevent losing them on update by leaving them behind)
			transferLocalResources(localConfigFolder, updateConfigFolder, dsd);
			
			//	transfer caches from old local config (if given)
			handleCacheFolders(localConfigFolder, dataFolders, cachePolicy, askCachePolicy, updateConfigFolder, dsd);
			
			//	rename old local config
			if (localConfigFolder != null) {
				File oldLocalConfigFolder = new File(configRoot, (local.name + "." + local.timestamp + ".old"));
				for (int a = 0; a < 10; a++) {
					if (localConfigFolder.renameTo(oldLocalConfigFolder)) {
						oldLocalConfigFolder = null;
						break;
					}
					System.out.println("Failed to rename old configutation folder, retrying in " + (a+1) + " seconds.");
					if (dsd != null) {
						dsd.setStatusLabel("Failed to rename old configutation folder.");
						dsd.setStatusLabel("Will retry in " + (a+1) + " seconds.");
					}
					try {
						Thread.sleep(1000 * (a+1));
					} catch (InterruptedException ie) {}
				}
				if (oldLocalConfigFolder != null)
					throw new IOException("Failed to rename old configuration folder " + localConfigFolder.getAbsolutePath());
			}
			
			//	rename downloaded/updated config
			updateConfigFolder.renameTo(new File(configRoot, remote.name));
			
			//	return new local configuration pointing to the data just downloaded
			return new ConfigurationDescriptor(null, remote.name, remote.timestamp);
		}
		catch (IOException ioe) {
			System.out.println(" - Error copying remote configuration: " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
			ioe.printStackTrace(System.out);
			throw ioe;
		}
		
		//	close status dialog
		finally {
			if (dsd != null)
				dsd.dispose();
		}
	}
	
	private static void updateFile(File localSourceFolder, File targetFolder, String fileName, InputStream source, long sourceLastModified) throws IOException {
		
		//	check if more recent version of file available locally (not for cnfg files)
		if ((localSourceFolder != null) && !fileName.endsWith(".cnfg")) {
			File localFile = new File(localSourceFolder, fileName);
			
			//	check with a second tolerance to foster local reuse (for sake of faster downloads)
			if (localFile.exists() && (sourceLastModified < (localFile.lastModified() + 1000))) {
				System.out.println("   --> using local version " + fileName + " (" + localFile.lastModified() + ")");
				source.close();
				source = new FileInputStream(localFile);
				sourceLastModified = localFile.lastModified();
			}
		}
		
		//	create target file
		File targetFile = new File(targetFolder, fileName);
		
		//	check if target file exists (might have been created in earlier download attempt that failed on a later file)
		if (targetFile.exists() && (targetFile.lastModified() == sourceLastModified)) {
			System.out.println("   --> already downloaded " + fileName + " (" + targetFile.lastModified() + ")");
			source.close();
			return;
		}
		
		//	create update file
		File updateFile = new File(targetFolder, (fileName + "." + sourceLastModified + ".updating"));
		if (updateFile.exists()) // clean up possibly corrupted file from earlier download
			updateFile.delete();
		else updateFile.getParentFile().mkdirs();
		updateFile.createNewFile();
		OutputStream target = new BufferedOutputStream(new FileOutputStream(updateFile));
		
		//	make sure source is buffered
		source = ((source instanceof BufferedInputStream) ? source : new BufferedInputStream(source));
		
		//	copy file
		int count;
		int total = 0;
		byte[] data = new byte[1024];
		try {
			while ((count = source.read(data, 0, 1024)) != -1) {
				target.write(data, 0, count);
				total += count;
			}
			source.close();
		}
		
		//	catch omitted empty config, text, and explanation files
		catch (IOException ioe) {
			if (total != 0)
				throw ioe;
			fileName = fileName.toLowerCase();
			if (!fileName.endsWith(".cnfg") && !fileName.endsWith(".txt") && !fileName.endsWith(".html"))
				throw ioe;
		}
		
		//	close streams
		target.flush();
		target.close();
		
		//	set timestamp of copied file
		try {
			updateFile.setLastModified(sourceLastModified);
			System.out.println("   - last modified set to " + sourceLastModified);
		}
		catch (RuntimeException re) {
			System.out.println("   - Error setting file timestamp: " + re.getClass().getName() + " (" + re.getMessage() + ")");
			re.printStackTrace(System.out);
		}
		
		//	make file available
		updateFile.renameTo(targetFile);
	}
	
//	private static final void copyFolder(File sourceRoot, File targetRoot, Set copied) throws IOException {
//		
//		//	get files to copy
//		File[] toCopy = sourceRoot.listFiles(new FileFilter() {
//			public boolean accept(File file) {
//				return !file.getName().endsWith(".old");
//			}
//		});
//		
//		//	copy recursively through directories
//		for (int c = 0; c < toCopy.length; c++) {
//			if (toCopy[c].isFile()) {
//				if (copied.add(normalizePath(toCopy[c].getAbsolutePath())))
//					copyFile(toCopy[c].getName(), sourceRoot, targetRoot);
//			}
//			else {
//				File copy = new File(targetRoot, toCopy[c].getName());
//				copy.mkdirs();
//				copyFolder(toCopy[c], copy, copied);
//			}
//		}
//	}
	
	private static void transferLocalResources(File localConfigFolder, File updateConfigFolder, DownloadStatusDialog dsd) throws IOException {
		if (localConfigFolder == null)
			return;
		File localResourcesFile = new File(localConfigFolder, "localResources.cnfg");
		if (!localResourcesFile.exists())
			return;
		
		//	transfer local resources
		System.out.println(" - transferring local resources");
		if (dsd != null)
			dsd.setStepLabel("Transferring Local Resources");
		StringVector localResourceList = StringVector.loadList(localResourcesFile);
		for (int r = 0; r < localResourceList.size(); r++) try {
			String localResourceName = localResourceList.get(r);
			
			//	create and check source (clean up list if not found)
			File localResourceFile = new File(localConfigFolder, localResourceName);
			if (localResourceFile.exists()) {
				localResourceList.remove(r--);
				continue;
			}
			
			//	create and check target (might have come in with update)
			File updateResourceFile = new File(updateConfigFolder, localResourceName);
			if (updateResourceFile.exists())
				continue;
			
			//	perform transfer
			System.out.println("   - transferring " + localResourceName);
			if (dsd != null)
				dsd.setStepLabel(" - transferring " + localResourceName);
			copyFile(localResourceFile, updateResourceFile);
		}
		catch (Exception e) {
			if (dsd == null)
				continue;
			int choice = JOptionPane.showConfirmDialog(dsd, ("Failed to copy locally created file '" + localResourceList.get(r) + "'\r\nContinue with update?"), "Copying Local Resource Failed", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice != JOptionPane.NO_OPTION)
				continue;
			if (e instanceof IOException)
				throw ((IOException) e);
			else throw new IOException(e);
		}
		
		//	transfer resource list proper
		System.out.println("   - transferring local resource list");
		if (dsd != null)
			dsd.setStepLabel(" - transferring local resource list");
		File updateResourcesFile = new File(updateConfigFolder, "localResources.cnfg");
		localResourceList.storeContent(updateResourcesFile);
	}
	
	private static void handleCacheFolders(File localConfigFolder, StringVector dataFolders, int cachePolicy, boolean askCachePolicy, File updateConfigFolder, final DownloadStatusDialog dsd) throws IOException {
		if (localConfigFolder == null)
			return;
		if (cachePolicy == CACHE_UPDATE_POLICY_IGNORE)
			return;
		
		//	show status
		final String cacheActionLabel;
		if (cachePolicy == CACHE_UPDATE_POLICY_COPY)
			cacheActionLabel = "Copying";
		else if (cachePolicy == CACHE_UPDATE_POLICY_SCRUB)
			cacheActionLabel = "Cleaning up";
		else if (cachePolicy == CACHE_UPDATE_POLICY_MOVE)
			cacheActionLabel = "Transfering";
		else cacheActionLabel = "Handling";
		System.out.println(cacheActionLabel + " caches");
		if (dsd != null) {
			dsd.setStepLabel(cacheActionLabel + " Caches");
			dsd.setProgressPercent(100);
		}
		
		//	collect cache folders
		System.out.println(" - collecting cache folders");
		if (dsd != null)
			dsd.setStatusLabel(" - collecting cache folders");
		LinkedList possibleCacheParents = new LinkedList();
		for (int f = 0; f < dataFolders.size(); f++) {
			File dataFolder = new File(localConfigFolder, dataFolders.get(f));
			if (dataFolder.exists() && dataFolder.isDirectory())
				possibleCacheParents.addLast(dataFolder);
		}
		
		String localConfigFolderPrefix = normalizePath(localConfigFolder.getAbsolutePath());
		if (!localConfigFolderPrefix.endsWith("/"))
			localConfigFolderPrefix += "/";
		int localConfigFolderPrefixLength = localConfigFolderPrefix.length();
		
		TreeSet cacheFolderNameSet = new TreeSet();
		while (possibleCacheParents.size() != 0) {
			File folder = ((File) possibleCacheParents.removeFirst());
			if ("cache".equalsIgnoreCase(folder.getName())) {
				String cacheFolderName = normalizePath(folder.getAbsolutePath()).substring(localConfigFolderPrefixLength);
				cacheFolderNameSet.add(cacheFolderName);
				System.out.println("   - found cache folder: " + cacheFolderName);
			}
			else {
				File[] subFolders = folder.listFiles(new FileFilter() {
					public boolean accept(File file) {
						return file.isDirectory();
					}
				});
				for (int s = 0; s < subFolders.length; s++)
					possibleCacheParents.addLast(subFolders[s]);
			}
		}
		
		//	check which caches to actually update
		System.out.println(" - checking cache folders");
		if (dsd != null)
			dsd.setStatusLabel(" - checking cache folders");
		ArrayList cacheFolderNames = new ArrayList();
		for (Iterator cfnit = cacheFolderNameSet.iterator(); cfnit.hasNext();) {
			String cacheFolderName = ((String) cfnit.next());
			File sourceCacheFolder = new File(localConfigFolder, cacheFolderName);
			if (sourceCacheFolder.exists() && sourceCacheFolder.isDirectory() && (sourceCacheFolder.listFiles().length != 0))
				cacheFolderNames.add(cacheFolderName);
		}
		
		//	parse cache policy
		CachePolicy defaultCachePolicy = new CachePolicy(cachePolicy);
		
		//	ask for per-folder cache policies
		HashMap folderCachePolicies = null;
		if (askCachePolicy) {
			
			//	load any remembered cache policies
			Settings storedCachePolicies = Settings.loadSettings(new File(localConfigFolder, "cachePolicies.cnfg"));
			
			//	prompt user
			folderCachePolicies = askCachePolicies(cacheFolderNames, storedCachePolicies, cachePolicy);
			
			//	update cache settings from user response
			if ((folderCachePolicies != null) && folderCachePolicies.containsKey(rememberCachePolicies)) {
				storedCachePolicies.clear();
				for (Iterator cfnit = folderCachePolicies.keySet().iterator(); cfnit.hasNext();) {
					String cacheFolderName = ((String) cfnit.next());
					if (rememberCachePolicies.equals(cacheFolderName))
						continue;
					CachePolicy cacheFolderPolicy = ((CachePolicy) folderCachePolicies.get(cacheFolderName));
					if (cacheFolderPolicy == null)
						continue;
					storedCachePolicies.setSetting(getCachePolicyKey(cacheFolderName), ("" + cacheFolderPolicy.policy));
				}
			}
			
			//	store cache policies if asked to
			if ((folderCachePolicies == null) || folderCachePolicies.containsKey(rememberCachePolicies))
				storedCachePolicies.storeAsText(new File(updateConfigFolder, "cachePolicies.cnfg"));
		}
		
		//	transfer cached data
		for (int c = 0; c < cacheFolderNames.size(); c++) {
			String cacheFolderName = ((String) cacheFolderNames.get(c));
			System.out.println(" - " + cacheActionLabel.toLowerCase() + " cache folder " + cacheFolderName);
			if (dsd != null)
				dsd.setStatusLabel(" - " + cacheActionLabel.toLowerCase() + " cache folder " + cacheFolderName);
			File sourceCacheFolder = new File(localConfigFolder, cacheFolderName);
			File targetCacheFolder = new File(updateConfigFolder, cacheFolderName);
			
			//	get policy
			CachePolicy cacheFolderPolicy = ((folderCachePolicies == null) ? defaultCachePolicy : ((CachePolicy) folderCachePolicies.get(cacheFolderName)));
			if (cacheFolderPolicy == null)
				cacheFolderPolicy = defaultCachePolicy;
			
			//	try renaming first (way faster)
			if (cacheFolderPolicy.move() && !targetCacheFolder.exists() && targetCacheFolder.getParentFile().exists() && targetCacheFolder.getParentFile().isDirectory()) try {
				if (sourceCacheFolder.renameTo(targetCacheFolder)) {
					System.out.println(" ==> handled via folder path renaming");
					continue;
				}
				else System.out.println(" - folder path renaming failed, handling recursively");
			}
			catch (Exception e) {
				System.out.println("Could not rename cache folder " + cacheFolderName + ": " + e.getMessage());
				e.printStackTrace(System.out);
			}
			
			Set monitor = new HashSet() {
				public boolean add(Object o) {
					if (super.add(o)) {
						System.out.println(" - " + cacheActionLabel.toLowerCase() + " cached file " + ((String) o));
						if (dsd != null)
							dsd.setStatusLabel(" - " + cacheActionLabel.toLowerCase() + " cached file " + ((String) o));
						return true;
					}
					else return false;
				}
			};
			if (handleCacheFolder(sourceCacheFolder, targetCacheFolder, monitor, cacheFolderPolicy))
				sourceCacheFolder.delete();
		}
	}
	
	private static boolean handleCacheFolder(File sourceRoot, File targetRoot, Set copied, CachePolicy cachePolicy) throws IOException {
		
		//	keep track of whether or not directories are really empty
		boolean sourceRootEmpty = cachePolicy.cleanup;
		
		//	get files to handle (even the ones we don't need to copy, we might need to remove them)
		File[] toHandle = sourceRoot.listFiles();
		
		//	handle directory tree recursively through directories
		for (int c = 0; c < toHandle.length; c++) {
			if (toHandle[c].isFile()) {
				if (cachePolicy.copy && !toHandle[c].getName().endsWith(".old") && !toHandle[c].getName().endsWith(".new") && copied.add(normalizePath(toHandle[c].getAbsolutePath())))
					copyFile(toHandle[c].getName(), sourceRoot, targetRoot);
				if (cachePolicy.cleanup && !toHandle[c].delete())
					sourceRootEmpty = false;
			}
			else {
				File copyRoot = new File(targetRoot, toHandle[c].getName());
				if (cachePolicy.copy)
					copyRoot.mkdirs();
				if (handleCacheFolder(toHandle[c], copyRoot, copied, cachePolicy) && !toHandle[c].delete())
					sourceRootEmpty = false;
			}
		}
		
		//	did we completely empty source folder?
		return sourceRootEmpty;
	}
	
	private static void copyFile(String fileName, File sourceRoot, File targetRoot) throws IOException {
		copyFile(new File(sourceRoot, fileName), new File(targetRoot, fileName));
	}
	
	private static void copyFile(File sourceFile, File targetFile) throws IOException {
		
		//	open source file
		InputStream source = new BufferedInputStream(new FileInputStream(sourceFile));
		
		//	create target file
		targetFile.getParentFile().mkdirs();
		targetFile.createNewFile();
		
		//	open target file
		OutputStream target = new BufferedOutputStream(new FileOutputStream(targetFile));
		
		//	copy data
		byte[] buffer = new byte[1204];
		for (int r; (r = source.read(buffer, 0, buffer.length)) != -1;)
			target.write(buffer, 0, r);
		target.flush();
		target.close();
		source.close();
		
		//	set modification data of copy
		targetFile.setLastModified(sourceFile.lastModified());
	}
	
	/**
	 * status dialog for monitoring configuration downloads
	 * 
	 * @author sautter
	 */
	private static class DownloadStatusDialog extends StatusDialog {
		private JProgressBar progress = new JProgressBar(0, 100); 
		DownloadStatusDialog(Image icon, String title) {
			super(icon, title);
			this.progress.setStringPainted(true);
			this.setCustomComponent(this.progress);
		}
		void setProgressPercent(int percent) {
			percent = Math.max(0, percent);
			percent = Math.min(percent, 100);
			this.progress.setValue(percent);
		}
	}
	
	/**
	 * Utility panel for selecting GoldenGATE Plugins and Resources for export.
	 * 
	 * @author sautter
	 */
	public static class ExportPanel extends JPanel {
		private ArrayList pluginPanels = new ArrayList();
		
		/**
		 * Constructor
		 * @param goldenGate the GoldenGATE instance to retrieve plugins from
		 * @param selectablePlugins a list of the class names of the plugins
		 *            that are to be selectable for export
		 * @param selected a list of pre-selections, containing class names of
		 *            pre-selected plugins and names of pre-selected resources
		 *            (qualified or not, prefering the former)
		 */
		public ExportPanel(GoldenGATE goldenGate, StringVector selectablePlugins, StringVector selected) {
			super(new GridLayout(0, 1), true);
			this.setBorder(BorderFactory.createEtchedBorder());
			
			CustomFunction.Manager customFunctionManager = null;
			CustomShortcut.Manager customShortcutManager = null;
			TokenizerManager tokenizerManager = null;
			ArrayList plugins = new ArrayList();
			ArrayList docIOs = new ArrayList();
			ArrayList docFormats = new ArrayList();
			ArrayList docViewers = new ArrayList();
			ArrayList docEditorExts = new ArrayList();
			ArrayList resMgrs = new ArrayList();
			ArrayList dpMgrs = new ArrayList();
			ArrayList asMgrs = new ArrayList();
			ArrayList afMgrs = new ArrayList();
			
			GoldenGatePlugin[] ggPlugins = goldenGate.getPlugins();
			for (int p = 0; p < ggPlugins.length; p++) {
				if (ggPlugins[p] instanceof CustomFunction.Manager)
					customFunctionManager = ((CustomFunction.Manager) ggPlugins[p]);
				else if (ggPlugins[p] instanceof CustomShortcut.Manager)
					customShortcutManager = ((CustomShortcut.Manager) ggPlugins[p]);
				else if (ggPlugins[p] instanceof TokenizerManager)
					tokenizerManager = ((TokenizerManager) ggPlugins[p]);
				else if ((selectablePlugins == null) || selectablePlugins.contains(ggPlugins[p].getClass().getName())) {
					if (ggPlugins[p] instanceof ResourceManager) {
						if (ggPlugins[p] instanceof AnnotationSourceManager)
							asMgrs.add(ggPlugins[p]);
						else if (ggPlugins[p] instanceof AnnotationFilterManager)
							afMgrs.add(ggPlugins[p]);
						else if (ggPlugins[p] instanceof DocumentProcessorManager)
							dpMgrs.add(ggPlugins[p]);
						else if (ggPlugins[p] instanceof DocumentFormatProvider)
							docFormats.add(ggPlugins[p]);
						else resMgrs.add(ggPlugins[p]);
					}
					else if ((ggPlugins[p] instanceof DocumentLoader)|| (ggPlugins[p] instanceof DocumentSaver))
						docIOs.add(ggPlugins[p]);
					else if (ggPlugins[p] instanceof DocumentViewer)
						docViewers.add(ggPlugins[p]);
					else if (ggPlugins[p] instanceof DocumentEditorExtension)
						docEditorExts.add(ggPlugins[p]);
					else plugins.add(ggPlugins[p]);
				}
			}
			
			if (customFunctionManager != null)
				this.addPluginPanel(customFunctionManager, "Custom Functions", selected, true, false);
			if (customShortcutManager != null)
				this.addPluginPanel(customShortcutManager, "Custom Shortcuts", selected, true, false);
			if (tokenizerManager != null)
				this.addPluginPanel(tokenizerManager, "Tokenizers", selected, true, false);
			
			this.addPluginGroupPanels(docIOs, "Document IO", selected);
			this.addPluginGroupPanels(docFormats, "Document Formats", selected);
			this.addPluginGroupPanels(docViewers, "Document Viewers", selected);
			this.addPluginGroupPanels(docEditorExts, "Document Editor Extensions", selected);
			this.addPluginGroupPanels(afMgrs, "Annotation Filter Managers", selected);
			this.addPluginGroupPanels(dpMgrs, "Document Processor Managers", selected);
			this.addPluginGroupPanels(asMgrs, "Annotation Source Managers", selected);
			this.addPluginGroupPanels(resMgrs, "Generic Resource Managers", selected);
			this.addPluginGroupPanels(plugins, "Generic Plugins", selected);
		}
		
		private void addPluginGroupPanels(ArrayList plugins, String pluginGroupLabel, StringVector selected) {
			if (plugins.isEmpty()) return;
			
			this.add(new JLabel(("<HTML>&nbsp;<B>" + pluginGroupLabel + "</B></HTML>"), JLabel.LEFT));
			
			GoldenGatePlugin[] ggPlugins = ((GoldenGatePlugin[]) plugins.toArray(new GoldenGatePlugin[plugins.size()]));
			for (int p = 0; p < ggPlugins.length; p++)
				this.addPluginPanel(ggPlugins[p], ggPlugins[p].getPluginName(), selected, selected.contains(ggPlugins[p].getClass().getName()), true);
		}
		
		private void addPluginPanel(GoldenGatePlugin plugin, String pluginName, StringVector selectedResources, boolean selected, boolean enabled) {
			String pluginClassName = plugin.getClass().getName();
			String[] resourceNames = new String[0];
			if (plugin instanceof ResourceManager) {
				pluginName = ((ResourceManager) plugin).getResourceTypeLabel();
				resourceNames = ((ResourceManager) plugin).getResourceNames();
				if (resourceNames.length > 1)
					pluginName = pluginName + (pluginName.endsWith("s") ? "es" : "s");
			}
			if (pluginName == null) {
				pluginName = pluginClassName;
				pluginName = pluginName.substring(pluginName.lastIndexOf('.') + 1);
			}
			PluginPanel pp = new PluginPanel(pluginName, pluginClassName, resourceNames, selectedResources);
			pp.exportPlugin.setSelected(selected);
			pp.exportPlugin.setEnabled(enabled && pp.selectedResourceNames.isEmpty());
			this.pluginPanels.add(pp);
			this.add(pp);
		}
		
		/**
		 * Retrieve the class names of the plugins selected for export, and the
		 * qualified names of the resources selected for export. If a resource
		 * is selected, the respective manager plugin is automatically selected
		 * as well.
		 * @return an array holding the names of the plugins and resources
		 *         selected for export
		 */
		public String[] getSeleted() {
			StringVector selected = new StringVector();
			for (int p = 0; p < this.pluginPanels.size(); p++) {
				PluginPanel pp = ((PluginPanel) this.pluginPanels.get(p));
				if (pp.exportPlugin.isSelected() || (pp.selectedResourceNames.size() != 0)) {
					selected.addElementIgnoreDuplicates(pp.pluginClassName);
					for (int r = 0; r < pp.resourceNames.length; r++) {
						if (pp.resourceNames[r].startsWith("<") || pp.selectedResourceNames.contains(pp.resourceNames[r]))
							selected.addElementIgnoreDuplicates(pp.resourceNames[r] + "@" + pp.pluginClassName);
					}
				}
			}
			return selected.toStringArray();
		}
		
		private class PluginPanel extends JPanel {
			String pluginName;
			String pluginClassName;
			
			JCheckBox exportPlugin = new JCheckBox("Export");
			
			String[] resourceNames;
			StringVector selectedResourceNames = new StringVector();
			int builtinResourceCount = 0;
			JLabel resourceLabel = new JLabel();
			
			PluginPanel(String pluginName, String pluginClassName, String[] resourceNames, StringVector selected) {
				super(new BorderLayout(), true);
				this.pluginName = pluginName;
				this.pluginClassName = pluginClassName;
				this.resourceNames = resourceNames;
				for (int r = 0; r < this.resourceNames.length; r++) {
					if (this.resourceNames[r].startsWith("<"))
						this.builtinResourceCount++;
					else if (selected.contains(this.resourceNames[r]) || selected.contains(this.resourceNames[r] + "@" + this.pluginClassName))
						this.selectedResourceNames.addElementIgnoreDuplicates(this.resourceNames[r]);
				}
				
				this.exportPlugin.setHorizontalAlignment(JCheckBox.RIGHT);
				if ((this.resourceNames.length - this.builtinResourceCount) > 0)
					this.resourceLabel.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent me) {
							selectResources();
						}
					});
				this.resourceLabel.setPreferredSize(new Dimension(260, 19));
				this.resourceLabel.setHorizontalAlignment(JLabel.RIGHT);
				
				JLabel label = new JLabel(("<HTML>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" + pluginName + "</HTML>"), JLabel.LEFT);
				this.add(label, BorderLayout.WEST);
				this.add(this.exportPlugin, BorderLayout.CENTER);
				this.add(this.resourceLabel, BorderLayout.EAST);
				
				this.resourcesUpdated();
			}
			
			private void resourcesUpdated() {
				if (this.resourceNames.length == 0)
					this.resourceLabel.setText("");
				else {
					this.resourceLabel.setText("Export Resources (" + this.selectedResourceNames.size() + " of " + (this.resourceNames.length - this.builtinResourceCount) + " selected, " + this.builtinResourceCount + " built-in)");
					if (this.selectedResourceNames.isEmpty())
						this.exportPlugin.setEnabled(true);
					else {
						this.exportPlugin.setSelected(true);
						this.exportPlugin.setEnabled(false);
					}
				}
			}
			
			private void selectResources() {
				SelectResourcesDialog srd = new SelectResourcesDialog("Select Resources from " + this.pluginName + " for Export");
				srd.setVisible(true);
				this.resourcesUpdated();
			}
			
			private class SelectResourcesDialog extends DialogPanel {
				SelectResourcesDialog(String title) {
					super(title, true);
					
					JTable resourceTable = new JTable();
					resourceTable.setModel(new ResourceSelectionTableModel());
					resourceTable.getColumnModel().getColumn(0).setMaxWidth(60);
					
					//	initialize main buttons
					JButton commitButton = new JButton("OK");
					commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
					commitButton.setPreferredSize(new Dimension(100, 21));
					commitButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							dispose();
						}
					});
					
					JPanel mainButtonPanel = new JPanel(new FlowLayout());
					mainButtonPanel.add(commitButton);
					
					this.setLayout(new BorderLayout());
					this.add(new JScrollPane(resourceTable), BorderLayout.CENTER);
					this.add(mainButtonPanel, BorderLayout.SOUTH);
					
					this.setResizable(true);
					this.setSize(new Dimension(300, 500));
					this.setLocationRelativeTo(this.getOwner());
				}
				
				private class ResourceSelectionTableModel implements TableModel {
					
					/** @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
					 */
					public void addTableModelListener(TableModelListener l) {}
					
					/** @see javax.swing.table.TableModel#getColumnClass(int)
					 */
					public Class getColumnClass(int columnIndex) {
						if (columnIndex == 0) return Boolean.class;
						else return String.class;
					}
					
					/** @see javax.swing.table.TableModel#getColumnCount()
					 */
					public int getColumnCount() {
						return 2;
					}
					
					/** @see javax.swing.table.TableModel#getColumnName(int)
					 */
					public String getColumnName(int columnIndex) {
						if (columnIndex == 0) return "Export";
						if (columnIndex == 1) return "Resource Name";
						return null;
					}
					
					/** @see javax.swing.table.TableModel#getRowCount()
					 */
					public int getRowCount() {
						return resourceNames.length;
					}
					
					/** @see javax.swing.table.TableModel#getValueAt(int, int)
					 */
					public Object getValueAt(int rowIndex, int columnIndex) {
						if (columnIndex == 0) return new Boolean(resourceNames[rowIndex].startsWith("<") || selectedResourceNames.contains(resourceNames[rowIndex]));
						if (columnIndex == 1) return resourceNames[rowIndex];
						return null;
					}
					
					/** @see javax.swing.table.TableModel#isCellEditable(int, int)
					 */
					public boolean isCellEditable(int rowIndex, int columnIndex) {
						return ((columnIndex == 0) && !resourceNames[rowIndex].startsWith("<"));
					}
					
					/** @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
					 */
					public void removeTableModelListener(TableModelListener l) {}
					
					/** @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
					 */
					public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
						if (columnIndex == 0) {
							if (((Boolean) newValue).booleanValue())
								selectedResourceNames.addElementIgnoreDuplicates(resourceNames[rowIndex]);
							else selectedResourceNames.removeAll(resourceNames[rowIndex]);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Create a (duplicate-free) list of the data items that belong to a given
	 * configuration descriptor, relative to the specified root path. If the
	 * root path argument is null, the content of the configuration's data and
	 * help paths will not be included in the returned file list.
	 * @param rootPath the root path of the GoldenGATE installation
	 * @param config the configuration to analyze
	 * @return a list of the data items that belong to the specified
	 *         configuration descriptor
	 */
	public static String[] getDataNameList(File rootPath, Configuration config) {
		StringVector dataNameCollector = new StringVector();
		
		if (rootPath != null) {
			StringVector helpDataNames = listFilesRelative(new File(rootPath, config.helpBasePath));
			for (int h = 0; h < helpDataNames.size(); h++)
				dataNameCollector.addElementIgnoreDuplicates(config.helpBasePath + (config.helpBasePath.endsWith("/") ? "" : "/") + helpDataNames.get(h));
			StringVector dataDataNames = listFilesRelative(new File(rootPath, DATA_FOLDER_NAME));
			for (int d = 0; d < dataDataNames.size(); d++)
				dataNameCollector.addElementIgnoreDuplicates(DATA_FOLDER_NAME + "/" + dataDataNames.get(d));
		}
		dataNameCollector.addElementIgnoreDuplicates(config.settingsPath);
		for (Iterator dit = config.dataItems.iterator(); dit.hasNext();) {
			DataItem di = ((DataItem) dit.next());
			dataNameCollector.addElementIgnoreDuplicates(di.path);
		}
		
		for (Iterator pit = config.plugins.iterator(); pit.hasNext();) {
			Plugin plugin = ((Plugin) pit.next());
			
			if (plugin.classPath.length() != 0)
				dataNameCollector.addElementIgnoreDuplicates(plugin.classPath);
			
			String libBasePath = plugin.classPath.substring(0, (plugin.classPath.lastIndexOf('/') + 1));
			for (Iterator lit = plugin.libs.iterator(); lit.hasNext();) {
				Lib lib = ((Lib) lit.next());
				dataNameCollector.addElementIgnoreDuplicates(libBasePath + lib.path);
			}
			
			String dataBasePath = (plugin.dataPath + (plugin.dataPath.endsWith("/") ? "" : "/"));
			for (Iterator dit = plugin.dataItems.iterator(); dit.hasNext();) {
				DataItem di = ((DataItem) dit.next());
				dataNameCollector.addElementIgnoreDuplicates(dataBasePath + di.path);
			}
			
			for (Iterator rit = plugin.resources.iterator(); rit.hasNext();) {
				Resource res = ((Resource) rit.next());
				if (res.path.length() != 0)
					dataNameCollector.addElementIgnoreDuplicates(dataBasePath + res.path);
				
				for (Iterator dit = res.dataItems.iterator(); dit.hasNext();) {
					DataItem di = ((DataItem) dit.next());
					dataNameCollector.addElementIgnoreDuplicates(dataBasePath + di.path);
				}
			}
		}
		
		dataNameCollector.sortLexicographically();
		
		return dataNameCollector.toStringArray();
	}
	
	/**
	 * Create a configuration descriptor from a given GoldenGATE instance,
	 * restricted to specific plugins and resources. The specified list of
	 * plugins and resources to export is expected to contain class names for
	 * plugins and fully qualified names of resources. However, non-qualified
	 * resource names will be resolved to qualified ones on a best-effort basis.
	 * Dependencies are resolved transitively.
	 * @param name the name for the configuration descriptor
	 * @param rootPath the root path of the GoldenGATE installation
	 * @param goldenGate the GoldenGATE instance to obtain plugins and resources
	 *            from
	 * @param selected the list of the plugins and resources to export (see
	 *            above for details)
	 * @param pm a progress monitor through which to display progress
	 *            information
	 * @return a configuration descriptor including the specified plugins and
	 *         resources, and all plugins and resources the specified ones
	 *         depend on
	 * @throws IOException
	 */
	public static Configuration buildConfiguration(String name, File rootPath, GoldenGATE goldenGate, StringVector selected, ProgressMonitor pm) throws IOException {
		long ggTimestamp = new File(rootPath, "GoldenGATE.jar").lastModified();
		
		if (pm == null)
			pm = ProgressMonitor.dummy;
		
		pm.setStep("Basic settings");
		pm.setMaxProgress(10);
		
		Configuration configuration = new Configuration(name, "", System.currentTimeMillis(), DOCUMENTATION_FOLDER_NAME, CONFIG_FILE_NAME, (DATA_FOLDER_NAME + "/" + ICON_FILE_NAME));
		configuration.addDataItem(new DataItem(README_FILE_NAME, configuration.configTimestamp));
		
		pm.setStep("Plugin analysis");
		pm.setBaseProgress(10);
		pm.setMaxProgress(40);
		
		GoldenGatePlugin[] plugins = goldenGate.getPlugins();
		
		//	find configuration root
		File configRoot = new File(rootPath, goldenGate.getConfigurationPath());
		System.out.println("Config root path is " + configRoot.getAbsolutePath());
		
		//	find all plugin jar files
		File pluginFolder = new File(configRoot, PLUGIN_FOLDER_NAME);
		File[] jarFiles = pluginFolder.listFiles(jarFileFilter);
		
		//	index jar files by entries
		pm.setInfo("Indexing plugin jar files");
		System.out.println("Indexing jar files ...");
		Properties jarPathsByClassNamesIndex = new Properties(); 
		for (int j = 0; j < jarFiles.length; j++) {
			String jarPath = normalizePath(jarFiles[j].getAbsolutePath());
			System.out.println(" - " + jarPath);
			JarInputStream jis = new JarInputStream(new FileInputStream(jarFiles[j]));
			JarEntry je;
			while ((je = jis.getNextJarEntry()) != null) {
				String jarEntryName = je.getName();

				// new class file
				if (jarEntryName.endsWith(".class")) {
					String className = StringUtils.replaceAll(jarEntryName.substring(0, (jarEntryName.length() - 6)), "/", ".");
					jarPathsByClassNamesIndex.setProperty(className, jarPath);
				}
			}
			jis.close();
		}
		
		HashSet pluginClassNames = new HashSet();
		Properties jarPathsByClassNames = new Properties();
		Properties dataPathsByClassNames = new Properties();
		Properties resourceFullNames = new Properties();
		
		for (int p = 0; p < plugins.length; p++) {
			String pluginClassName = plugins[p].getClass().getName();
			pluginClassNames.add(pluginClassName);
			System.out.println("Analyzing plugin '" + plugins[p].getPluginName() + "' (" + pluginClassName + ")");
			pm.setInfo(" - " + plugins[p].getPluginName());
			
			String pluginSourcePath = jarPathsByClassNamesIndex.getProperty(pluginClassName);
			System.out.println("  - Plugin loaded from " + ((pluginSourcePath == null) ? "editor core JAR" : pluginSourcePath));
			
			//	plugin loaded from its own jar
			if (pluginSourcePath != null) {
				String pluginClassPath = pluginSourcePath;
				System.out.println("  - Class path is " + pluginClassPath);
				jarPathsByClassNames.setProperty(pluginClassName, pluginClassPath);
				
				String pluginDataPath = (pluginClassPath.substring(0, (pluginClassPath.length() - ".jar".length())) + JAR_DATA_FOLDER_SUFFIX);
				System.out.println("  - Data path is " + pluginDataPath);
				dataPathsByClassNames.setProperty(pluginClassName, pluginDataPath);
			}
			
			//	plugin loaded from core jar
			else {
				if (plugins[p] instanceof CustomFunction.Manager) {
					File dataPath = new File(configRoot.getAbsoluteFile(), CUSTOM_FUNCTIONS_FOLDER_NAME);
					String pluginDataPath = normalizePath(dataPath.getAbsolutePath());
					System.out.println("  - Data path is " + pluginDataPath);
					dataPathsByClassNames.setProperty(pluginClassName, pluginDataPath);
				}
				else if (plugins[p] instanceof CustomShortcut.Manager) {
					File dataPath = new File(configRoot.getAbsoluteFile(), CUSTOM_SHORTCUTS_FOLDER_NAME);
					String pluginDataPath = normalizePath(dataPath.getAbsolutePath());
					System.out.println("  - Data path is " + pluginDataPath);
					dataPathsByClassNames.setProperty(pluginClassName, pluginDataPath);
				}
				else if (plugins[p] instanceof TokenizerManager) {
					File dataPath = new File(configRoot.getAbsoluteFile(), TOKENIZER_FOLDER_NAME);
					String pluginDataPath = normalizePath(dataPath.getAbsolutePath());
					System.out.println("  - Data path is " + pluginDataPath);
					dataPathsByClassNames.setProperty(pluginClassName, pluginDataPath);
				}
			}
			
			if (plugins[p] instanceof ResourceManager) {
				String[] resNames = ((ResourceManager) plugins[p]).getResourceNames();
				for (int r = 0; r < resNames.length; r++)
					resourceFullNames.setProperty(resNames[r], (resNames[r] + "@" + plugins[p].getClass().getName()));
			}
			
			pm.setProgress(((p+1) * 100) / plugins.length);
		}
		
		File sharedPluginLibFolder = new File(pluginFolder, JAR_BIN_FOLDER_SUFFIX);
		if (sharedPluginLibFolder.exists() && sharedPluginLibFolder.isDirectory()) {
			File[] sharedPluginLibs = sharedPluginLibFolder.listFiles(jarFileFilter);
			String root = normalizePath(rootPath.getAbsolutePath());
			for (int l = 0; l < sharedPluginLibs.length; l++) {
				System.out.println("Adding shared plugin lib");
				String sharedPluginLib = normalizePath(sharedPluginLibs[l].getAbsolutePath());
				sharedPluginLib = normalizePath(sharedPluginLib.substring(root.length()));
				System.out.println("  - Lib path is " + sharedPluginLib);
				configuration.addDataItem(new DataItem(sharedPluginLib, sharedPluginLibs[l].lastModified()));
			}
		}
		
		//	resolve resource dependencies (aka transitive exports)
		pm.setStep("Resource & plugin resolution");
		pm.setBaseProgress(40);
		pm.setMaxProgress(60);
		StringVector toExport = new StringVector();
		toExport.addContentIgnoreDuplicates(selected);
		for (int e = 0; e < toExport.size(); e++) {
			String export = toExport.get(e);
			
			//	it's a plugin name, or a non-qualified resource name
			if (export.indexOf('@') == -1) {
				
				//	it's a resource name, qualify it
				if (resourceFullNames.containsKey(export)) {
					export = resourceFullNames.getProperty(export);
					toExport.setElementAt(export, e);
				}
				
				//	it's a plugin
				else {
					GoldenGatePlugin plugin = goldenGate.getPlugin(export);
					if (plugin != null) {
						Class pluginClass = plugin.getClass();
						while ((pluginClass != null) && !Object.class.equals(pluginClass)) {
							Field[] fields = pluginClass.getDeclaredFields();
							for (int f = 0; f < fields.length; f++) {
								Class fieldClass = fields[f].getType();
								if (pluginClassNames.contains(fieldClass.getName()))
									toExport.addElementIgnoreDuplicates(fieldClass.getName());
							}
							pluginClass = pluginClass.getSuperclass();
						}
					}
				}
			}
			
			//	it's a resource
			if (export.indexOf('@') != -1) {
				String providerClassName = export.substring(export.indexOf('@') + 1);
				toExport.addElementIgnoreDuplicates(providerClassName);
				String resName = export.substring(0, export.indexOf('@'));
				
				//	do not resolve built-in resources
				GoldenGatePlugin provider = goldenGate.getPlugin(providerClassName);
				if ((provider != null) && (provider instanceof ResourceManager))
					toExport.addContentIgnoreDuplicates(((ResourceManager) provider).getRequiredResourceNames(resName, false));
			}
			
			//	it's a plugin, add built-in resources
			else {
				GoldenGatePlugin plugin = goldenGate.getPlugin(export);
				if ((plugin != null) && (plugin instanceof ResourceManager)) {
					ResourceManager resManager = ((ResourceManager) plugin);
					System.out.println("Investigating resources of " + export);
					String[] resNames = resManager.getResourceNames();
					for (int r = 0; r < resNames.length; r++) {
						if (resNames[r].startsWith("<")) {
							System.out.println("  - added built-in resource " + resNames[r] + "@" + export);
							toExport.addElementIgnoreDuplicates(resNames[r] + "@" + export);
						}
					}
				}
			}
			
			pm.setProgress(((e+1) * 100) / toExport.size());
		}
		
		pm.setStep("Plugin export");
		pm.setBaseProgress(60);
		pm.setMaxProgress(100);
		
		for (int p = 0; p < plugins.length; p++) 
			if (toExport.contains(plugins[p].getClass().getName())) {
				pm.setInfo(" - " + plugins[p].getPluginName());
				addPlugin(configuration, normalizePath(configRoot.getAbsolutePath()), plugins[p], pluginClassNames, jarPathsByClassNames, dataPathsByClassNames, toExport, ggTimestamp);
				pm.setProgress(((p+1) * 100) / plugins.length);
			}
		
		pm.setProgress(100);
		return configuration;
	}
	
	private static void addPlugin(Configuration config, String basePath, GoldenGatePlugin ggPlugin, HashSet pluginClassNames, Properties jarPathsByClassNames, Properties dataPathsByClassNames, StringVector toExport, long ggTimestamp) throws IOException {
		System.out.println("Writing plugin '" + ggPlugin.getPluginName() + "'");
		String pluginClassPath = jarPathsByClassNames.getProperty(ggPlugin.getClass().getName());
		String pluginDataPath = dataPathsByClassNames.getProperty(ggPlugin.getClass().getName());
		
		String pluginLibPath = dataPathsByClassNames.getProperty(ggPlugin.getClass().getName());
		if (pluginClassPath != null)
			pluginLibPath = pluginLibPath.substring(0, (pluginLibPath.length() - JAR_DATA_FOLDER_SUFFIX.length())) + JAR_BIN_FOLDER_SUFFIX;
		
		Plugin plugin = new Plugin(
				ggPlugin.getPluginName(),
				ggPlugin.getClass().getName(),
				((pluginClassPath == null) ? "" : normalizePath(pluginClassPath.substring(basePath.length() + 1))),
				((pluginClassPath == null) ? ggTimestamp : (new File(pluginClassPath)).lastModified()),
				normalizePath(pluginDataPath.substring(basePath.length() + 1))
			);
		config.addPlugin(plugin);
		
		File pluginLibFolder = new File(pluginLibPath);
		if (pluginLibFolder.exists() && pluginLibFolder.isDirectory()) {
			File[] pluginLibs = pluginLibFolder.listFiles(jarFileFilter);
			for (int l = 0; l < pluginLibs.length; l++) {
				String pluginLib = normalizePath(pluginLibs[l].getAbsolutePath().substring(normalizePath(pluginClassPath).lastIndexOf('/') + 1));
				plugin.libs.add(new Lib(pluginLib, pluginLibs[l].lastModified()));
			}
		}
		
		StringVector dataNames = getNonResourceDataNames(new File(basePath), ggPlugin, dataPathsByClassNames);
		System.out.println(" - adding data items:");
		for (int d = 0; d < dataNames.size(); d++)
			plugin.dataItems.add(new DataItem(dataNames.get(d).substring(pluginDataPath.length() + 1), (new File(dataNames.get(d))).lastModified()));
		
		//	get required plugins
		Class pluginClass = ggPlugin.getClass();
		System.out.println(" - adding required plugin links:");
		while ((pluginClass != null) && !Object.class.equals(pluginClass)) {
			Field[] fields = pluginClass.getDeclaredFields();
			for (int f = 0; f < fields.length; f++) {
				Class fieldClass = fields[f].getType();
				if (pluginClassNames.contains(fieldClass.getName()))
					plugin.requiredPluginClassNames.add(fieldClass.getName());
			}
			pluginClass = pluginClass.getSuperclass();
		}
		
		if (ggPlugin instanceof ResourceManager) {
			ResourceManager rm = ((ResourceManager) ggPlugin);
			String[] resNames = rm.getResourceNames();
			Resource builtinResource = null;
			for (int r = 0; r < resNames.length; r++) {
				if (!toExport.contains(resNames[r] + "@" + ggPlugin.getClass().getName()))
					continue;
				
				//	built-in resource, only collect required data for now
				if (resNames[r].startsWith("<")) {
					String[] requiredResNames = rm.getRequiredResourceNames(resNames[r], false);
					if ((requiredResNames == null) || (requiredResNames.length == 0))
						continue;
					
					if (builtinResource == null)
						builtinResource = new Resource("", plugin.timestamp, ggPlugin.getClass().getName());
					for (int rr = 0; rr < requiredResNames.length; rr++) {
						int split = requiredResNames[rr].indexOf('@');
						if (split != -1) {
							String requiredResName = requiredResNames[rr].substring(0, split);
							builtinResource.requiredResourceNames.add(requiredResName);
						}
					}
				}
				
				//	'full' resource, write directly
				else {
					String[] resDataNames = rm.getDataNamesForResource(resNames[r]);
					StringVector resDataNameCollector = new StringVector();
					String resPath = null;
					for (int d = 0; d < resDataNames.length; d++) {
						if (resDataNames[d].endsWith("@" + rm.getClass().getName())) {
							if (resDataNames[d].startsWith(resNames[r] + "@"))
								resPath = resNames[r];
							else {
								String resDataName = resolve(pluginDataPath, resDataNames[d], dataPathsByClassNames);
								if (resDataName.endsWith("/")) {
									File wildcardFolder = new File(resDataName);
									if (wildcardFolder.exists() && wildcardFolder.isDirectory())
										resDataNameCollector.addContentIgnoreDuplicates(listFilesAbsolute(wildcardFolder));
								}
								else resDataNameCollector.addElementIgnoreDuplicates(resDataName);
							}
						}
					}
					
					String[] requiredResNames = rm.getRequiredResourceNames(resNames[r], false);
					for (int rr = 0; rr < requiredResNames.length; rr++) {
						String requiredResName = requiredResNames[rr];
						int split = requiredResName.indexOf('@');
						if (split != -1)
							requiredResName = requiredResName.substring(0, split);
						resDataNameCollector.removeAll(normalizePath((new File(pluginDataPath, requiredResName).getAbsolutePath())));
					}
					
					//	no data items required for resource, must be built in ==> ignore on export
					if (resDataNameCollector.isEmpty() && (resPath == null)) {}
					
					//	other data items & resources required
					else {
						
						//	resource must have path different from its name, substitute first data item
						if (resPath == null)
							resPath = resDataNameCollector.remove(0).substring(pluginDataPath.length() + 1);
						
						//	reset data names
						resDataNames = resDataNameCollector.toStringArray();
						
						//	create resource
						Resource resource = new Resource(resPath, (new File(pluginDataPath, resPath)).lastModified(), ggPlugin.getClass().getName());
						
						for (int d = 0; d < resDataNames.length; d++)
							resource.dataItems.add(new DataItem(resDataNames[d].substring(pluginDataPath.length() + 1), (new File(resDataNames[d])).lastModified()));
						
						for (int rr = 0; rr < requiredResNames.length; rr++) {
							int split = requiredResNames[rr].indexOf('@');
							if (split != -1) {
								String requiredResName = requiredResNames[rr].substring(0, split);
								resource.requiredResourceNames.add(requiredResName);
							}
						}
						
						plugin.resources.add(resource);
						config.addResource(resource);
					}
				}
				
				//	store built-in resources if any
				if (builtinResource != null) {
					plugin.resources.add(builtinResource);
					config.addResource(builtinResource);
				}
			}
		}
	}
	
	//	get the names of the files belonging to a selected resource, or to no resource at all
	private static StringVector getNonResourceDataNames(File basePath, GoldenGatePlugin ggp, Properties dataPathsByClassNames) {
		String dataPath = dataPathsByClassNames.getProperty(ggp.getClass().getName());
		StringVector resourceNameCollector = new StringVector();
		if (ggp instanceof ResourceManager)
			resourceNameCollector.addContentIgnoreDuplicates(((ResourceManager) ggp).getResourceNames());
		String[] resNames = resourceNameCollector.toStringArray();
		
		//	get and prepare folder content
		StringVector allFileNames = listFilesAbsolute(new File(dataPath));
		
		//	if all resources selected, there is no need for filtering, export entire folder content
		if (resNames.length != 0) {
			StringVector allResDataNames = new StringVector();
			
			//	get data names for resources
			for (int r = 0; r < resNames.length; r++) {
				String[] resDataNames = new String[0];
				
				if (ggp instanceof ResourceManager)
					resDataNames = ((ResourceManager) ggp).getDataNamesForResource(resNames[r]);
				
				for (int d = 0; d < resDataNames.length; d++) {
					String resDataName = resolve(dataPath, resDataNames[d], dataPathsByClassNames);
					if (resDataName.endsWith("/")) {
						File wildcardFolder = new File(resDataName);
						if (wildcardFolder.exists() && wildcardFolder.isDirectory())
							allResDataNames.addContentIgnoreDuplicates(listFilesAbsolute(wildcardFolder));
					}
					else allResDataNames.addElementIgnoreDuplicates(resDataName);
				}
			}
			
			//	remove data names for non-selected resources
			allFileNames = allFileNames.without(allResDataNames);
		}
		
		//	return non-excluded data names
		return allFileNames;
	}
	
	// transform a data name from a resource manager into the appropriate path relative to the configuration's base folder, and add the provider class names to the specified StringVector
	private static String resolve(String dataPath, String dataName, Properties dataPathsByClassNames) {
		int split = dataName.indexOf('@');
		if (split == -1) {
			return dataPath + "/" + dataName;
		}
		
		String plainDataName = dataName.substring(0, split);
		String providerClassName = dataName.substring(split + 1);
		
		return (dataPathsByClassNames.getProperty(providerClassName) + "/" + plainDataName);
	}
	
	private static final java.io.FileFilter jarFileFilter = new FileFilter() {
		public boolean accept(File file) {
			return ((file != null) && file.getName().toLowerCase().endsWith(".jar"));
		}
	};
	
	private static final String ATTIC_FOLDER_NAME = "_Attic";
	
	/**
	 * Recursively list the files in a folder, ignoring folders whose name
	 * starts with '_Attic' or equals 'cache', the latter case insensitively.
	 * All path separators are normalized to '/', and all local steps (folders
	 * named '.') are removed from the paths.
	 * @param rootPath the folder whose content to list
	 * @return a list of the absolute paths of the files contained in the
	 *         argument folder
	 */
	public static StringVector listFilesAbsolute(File rootPath) {
		return listFilesAbsolute(rootPath, null);
	}
	
	/**
	 * Recursively list the files in a folder, ignoring folders whose name
	 * starts with '_Attic' or equals 'cache', the latter case insensitively.
	 * All path separators are normalized to '/', and all local steps (folders
	 * named '.') are removed from the paths.
	 * @param rootPath the folder whose content to list
	 * @param filter a filter for for specifying which files enter the list
	 *            (specifying null deactivates filtering)
	 * @return a list of the absolute paths of the files contained in the
	 *         argument folder
	 */
	public static StringVector listFilesAbsolute(File rootPath, FileFilter filter) {
		StringVector list = new StringVector();
		
		File[] files = rootPath.listFiles();
		if (files == null) return list;
		
		for (int f = 0; f < files.length; f++)
			if ((filter == null) || filter.accept(files[f])) {
				if (files[f].isDirectory()) {
					if (!files[f].equals(rootPath) && !files[f].getName().startsWith(ATTIC_FOLDER_NAME) && !"cache".equalsIgnoreCase(files[f].getName()) && !files[f].getName().endsWith(".old"))
						list.addContentIgnoreDuplicates(listFilesAbsolute(files[f], filter));
				}
				else if (!files[f].getName().matches(".*\\.[0-9]{8,}\\.(old|new)"))
					list.addElementIgnoreDuplicates(normalizePath(files[f].getAbsolutePath()));
			}
		
		return list;
	}
	
	/**
	 * Recursively list the files in a folder, ignoring folders whose name
	 * starts with '_Attic' or equals 'cache', the latter case insensitively.
	 * All path separators are normalized to '/', and all local steps (folders
	 * named '.') are removed from the paths.
	 * @param rootPath the folder whose content to list
	 * @return a list of the paths of the files contained in the argument
	 *         folder, relative to the argument folder
	 */
	public static StringVector listFilesRelative(File rootPath) {
		return listFilesRelative(rootPath, null);
	}
	
	/**
	 * Recursively list the files in a folder, ignoring folders whose name
	 * starts with '_Attic' or equals 'cache', the latter case insensitively.
	 * All path separators are normalized to '/', and all local steps (folders
	 * named '.') are removed from the paths.
	 * @param rootPath the folder whose content to list
	 * @param filter a filter for for specifying which files enter the list
	 *            (specifying null deactivates filtering)
	 * @return a list of the paths of the files contained in the argument
	 *         folder, relative to the argument folder
	 */
	public static StringVector listFilesRelative(File rootPath, FileFilter filter) {
		String rootPathPrefix = normalizePath(rootPath.getAbsolutePath());
		if (!rootPathPrefix.endsWith("/")) rootPathPrefix += "/";
		int rootPathPrefixLength = rootPathPrefix.length();
		
		StringVector list = listFilesAbsolute(rootPath, filter);
		
		for (int l = 0; l < list.size(); l++)
			list.setElementAt(list.get(l).substring(rootPathPrefixLength), l);
		
		return list;
	}
	
	/**
	 * Normalize a file path. In particular, this method normalizes all
	 * separators to '/', and removes all local steps (folders named '.') from
	 * the paths. In addition, it cuts leading '/' and '.' characters.
	 * @param path the path to normalize
	 * @return the argument path, normalized according to the rules above
	 */
	public static String normalizePath(String path) {
		path = path.replace('\\', '/');
		while (path.startsWith("./"))
			path = path.substring(2);
		while (path.startsWith("/"))
			path = path.substring(1);
		while (path.endsWith("/."))
			path = path.substring(0, (path.length() - 2));
		int pathLength;
		do {
			pathLength = path.length();
			path = path.replaceAll("\\/\\.\\/", "/");
		} while (path.length() < pathLength);
		return path;
	}
	
	private static final SimpleDateFormat readmeTimestamper = new SimpleDateFormat("yyyy.MM.dd.HH.mm");
	private static final SimpleDateFormat yearTimestamper = new SimpleDateFormat("yyyy");
	
	/**
	 * Create a the content for a readme file to accompany a configuration.
	 * Basic configuration info, Jave and GoldenGATE version requirements, and
	 * GoldenGATE software license are added automatically. A dialog allows for
	 * entering a custom description text for the configuration, to be inserted
	 * right below the basic info. This method returns the individual lines of
	 * the readme file in an array of strings. If the dialog is cancelled, the
	 * array is empty, but this method never returns null.
	 * @param configName the name of the configuration to create a readme file
	 *            for
	 * @param timestamp the creation timestamp of the configuration
	 * @return the lines of the readme file in an array of strings, or an empty
	 *         array, if the input dialog was cancelled
	 */
	public static String[] createReadme(final String configName, long timestamp) {
		final StringVector readme = new StringVector();
		
		//	create configuration info lines
		final String[] configInfo = {
				"CONFIGURATION INFO",
				"",
				(configName + " configuration for GoldenGATE Document Markup System, created " + readmeTimestamper.format(new Date(timestamp))),
				"",
			};
		
		//	add Java(TM) version requirements
		final String[] javaVersionInfo = {
				"REQUIRED JAVA VERSION",
				"",
				"Sun (r) Java Runtime Environment (JRE) 1.4.2 or higher (recommended: JRE 1.5.0 or higher)",
			};
		
		//	add GoldenGATE version requirements
		final String[] ggVersionInfo = {
				"REQUIRED GOLDENGATE VERSION",
				"",
				GoldenGATE.VERSION_DATE,
			};
		
		//	add license
		final String[] ggLicenseInfo = {
				"LICENSE",
				"",
				"Copyright (c) 2006-" + yearTimestamper.format(new Date(timestamp)) + ", Guido Sautter, IPD Boehm, Universitaet Karlsruhe (TH)",
				"All rights reserved.",
				"Redistribution and use in source and binary forms, with or without",
				"modification, are permitted provided that the following conditions are met:",
				"",
				"    * Redistributions of source code must retain the above copyright",
				"      notice, this list of conditions and the following disclaimer.",
				"    * Redistributions in binary form must reproduce the above copyright",
				"      notice, this list of conditions and the following disclaimer in the",
				"      documentation and/or other materials provided with the distribution.",
				"    * Neither the name of the Universitaet Karlsruhe (TH) nor the",
				"      names of its contributors may be used to endorse or promote products",
				"      derived from this software without specific prior written permission.",
				"",
				"THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS 'AS IS' AND ANY",
				"EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED",
				"WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE",
				"DISCLAIMED. IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY",
				"DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES",
				"(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;",
				"LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND",
				"ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT",
				"(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS",
				"SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.",
			};
		
		//	create dialog
		final DialogPanel readmeDialog = new DialogPanel(("Create Readme File for Configuration '" + configName + "'"), true);
		
		JLabel label = new JLabel(("<HTML>Please fill in the description for the readme file of configuration '" + configName + "'.</HTML>"), JLabel.LEFT);
		label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, 12));
		label.setBorder(BorderFactory.createLineBorder(label.getBackground(), 5));
		
		JPanel readmePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		gbc.insets.left = 5;
		gbc.insets.right = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		
		StringVector builder = new StringVector();
		
		
		builder.addContent(configInfo);
		builder.addElement("");
		prepareLeadingSpace(builder);
		
		JLabel topLabel = new JLabel(("<HTML>" + builder.concatStrings("<BR>") + "</HTML>"), JLabel.LEFT);
		topLabel.setOpaque(true);
		topLabel.setBackground(uneditableReadmeColor);
		topLabel.setFont(readmeFont);
		
		readmePanel.add(topLabel, gbc.clone());
		gbc.gridy++;
		builder.clear();
		
		final JTextArea descriptionField = new JTextArea("<Enter Description>");
		descriptionField.setFont(readmeFont);
		descriptionField.setBorder(BorderFactory.createLineBorder(uneditableReadmeColor));
		
		readmePanel.add(descriptionField, gbc.clone());
		gbc.gridy++;
		
		
		builder.addElement("");
		builder.addElement("");
		builder.addContent(javaVersionInfo);
		builder.addElement("");
		builder.addElement("");
		builder.addContent(ggVersionInfo);
		builder.addElement("");
		builder.addElement("");
		builder.addContent(ggLicenseInfo);
		prepareLeadingSpace(builder);
		
		
		JLabel bottomLabel = new JLabel(("<HTML>" + builder.concatStrings("<BR>") + "</HTML>"), JLabel.LEFT);
		bottomLabel.setOpaque(true);
		bottomLabel.setBackground(uneditableReadmeColor);
		bottomLabel.setFont(readmeFont);
		
		readmePanel.add(bottomLabel, gbc.clone());
		gbc.gridy++;
		builder.clear();
		
		
		JScrollPane readmePanelBox = new JScrollPane(readmePanel);
		readmePanelBox.getVerticalScrollBar().setUnitIncrement(50);
		readmePanelBox.getVerticalScrollBar().setBlockIncrement(100);
		
		
		JButton okButton = new JButton("OK");
		okButton.setBorder(BorderFactory.createRaisedBevelBorder());
		okButton.setPreferredSize(new Dimension(70, 21));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String description = descriptionField.getText().trim();
				if ((description.length() == 0) || description.startsWith("<")) {
					JOptionPane.showMessageDialog(readmeDialog, ("Please enter a meaningful description text for configuration '" + configName + "'."), "Description Missing", JOptionPane.ERROR_MESSAGE);
					descriptionField.requestFocusInWindow();
					return;
				}
				
				readme.addContent(configInfo);
				readme.addContent(description.split("((\\n\\r)|(\\r\\n)|\\n|\\r)"));
				readme.addElement("");
				readme.addElement("");
				
				readme.addContent(javaVersionInfo);
				readme.addElement("");
				readme.addElement("");
				
				readme.addContent(ggVersionInfo);
				readme.addElement("");
				readme.addElement("");
				
				readme.addContent(ggLicenseInfo);
				
				readmeDialog.dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
		cancelButton.setPreferredSize(new Dimension(70, 21));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				readmeDialog.dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		
		readmeDialog.setLayout(new BorderLayout());
		readmeDialog.add(label, BorderLayout.NORTH);
		readmeDialog.add(readmePanelBox, BorderLayout.CENTER);
		readmeDialog.add(buttonPanel, BorderLayout.SOUTH);
		
		readmeDialog.setSize(700, 600);
		readmeDialog.setLocationRelativeTo(readmeDialog.getOwner());
		readmeDialog.setResizable(true);
		readmeDialog.setVisible(true);
		
		
		return readme.toStringArray();
	}
	
	/**
	 * Add a new entry to an existing readme file for a given configuration. The
	 * dialog offers a skipping option. This method returns true if a new entry
	 * was generated. If the new entry is the first, a change log header will be
	 * added to the argument readme file above the new entry. The new entry will
	 * be placed at the top of the change log, for easier access. Thus, if a
	 * readme file is extended exclusively by means of this method, the change
	 * log is in strict reverse order, i.e., newest entry atop.
	 * @param readme the lines of the readme file to extend
	 * @param configName the name of the configuration the readme file refers to
	 *            for
	 * @param timestamp the timestamp for the new entry
	 * @return true if an entry was added, i.e., if the argument string vector
	 *         was added new lines
	 */
	public static boolean extendReadme(StringVector readme, final String configName, final long timestamp) {
		final StringVector readmeTemp = new StringVector();
		
		//	create configuration info lines
		final String[] entryHeader = {
				("NEW IN VERSION " + readmeTimestamper.format(new Date(timestamp))),
				"",
			};
		
		//	parse existing readme
		final StringVector top = new StringVector();
		final StringVector bottom = new StringVector();
		
		//	read up to change log header
		int l = 0;
		while ((l < readme.size()) && !"CHANGE LOG".equals(readme.get(l)))
			top.addElement(readme.get(l++));
		
		//	no change log header so far
		if (l == readme.size()) {
			
			//	remove tailing empty lines
			while ((top.size() != 0) && "".equals(top.get(top.size() - 1)))
				top.removeElementAt(top.size() - 1);
			
			//	add change log header
			top.addElement("");
			top.addElement("");
			top.addElement("CHANGE LOG");
			top.addElement("");
			top.addElement("Changes to this configuration in reverse chronological order");
			
			//	add new entry header
			top.addElement("");
			top.addElement("");
			top.addContent(entryHeader);
		}
		
		//	add new entry below change log header
		else {
			
			//	read up to first entry
			while ((l < readme.size()) && !readme.get(l).startsWith("NEW IN VERSION"))
				top.addElement(readme.get(l++));
			
			//	remove tailing empty lines
			while ((top.size() != 0) && "".equals(top.get(top.size() - 1)))
				top.removeElementAt(top.size() - 1);
			
			//	add new entry header
			top.addElement("");
			top.addElement("");
			top.addContent(entryHeader);
			
			//	store older entries
			bottom.addElement("");
			bottom.addElement("");
			while (l < readme.size())
				bottom.addElement(readme.get(l++));
		}
		
		//	create dialog
		final DialogPanel readmeDialog = new DialogPanel(("Create Readme File Entry for Configuration '" + configName + "'"), true);
		
		JLabel label = new JLabel(("<HTML>Please create a new entry for the readme file of configuration '" + configName + "'.</HTML>"), JLabel.LEFT);
		label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, 12));
		label.setBorder(BorderFactory.createLineBorder(label.getBackground(), 5));
		
		final JPanel readmePanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets.top = 0;
		gbc.insets.bottom = 0;
		gbc.insets.left = 5;
		gbc.insets.right = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		
		final StringVector builder = new StringVector();
		
		
		builder.addContent(top);
		prepareLeadingSpace(builder);
		
		final JLabel topLabel = new JLabel(("<HTML>" + builder.concatStrings("<BR>") + "<BR></HTML>"), JLabel.LEFT);
		topLabel.setOpaque(true);
		topLabel.setBackground(uneditableReadmeColor);
		topLabel.setFont(readmeFont);
		
		readmePanel.add(topLabel, gbc.clone());
		gbc.gridy++;
		builder.clear();
		
		
		final JTextArea entryField = new JTextArea("<Enter New Entry>");
		entryField.setFont(readmeFont);
		entryField.setBorder(BorderFactory.createLineBorder(uneditableReadmeColor));
		
		ReadmeEntry[] res = ((ReadmeEntry[]) recentReadmeEntries.toArray(new ReadmeEntry[recentReadmeEntries.size()]));
		Arrays.sort(res);
		
		final JComboBox entrySelector = new JComboBox(res);
		entrySelector.setEditable(false);
		if (recentReadmeEntries.size() == 0)
			entrySelector.setEnabled(false);
		else entrySelector.setSelectedItem(null);
		entrySelector.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				ReadmeEntry re = ((ReadmeEntry) entrySelector.getSelectedItem());
				if (re != null)
					entryField.setText(re.entry);
			}
		});
		
		JLabel entrySelectorLabel = new JLabel("Previous entries: ");
		entrySelectorLabel.setFont(new Font(entrySelectorLabel.getFont().getFamily(), Font.BOLD, 12));
		entrySelectorLabel.setOpaque(true);
		entrySelectorLabel.setBackground(readmePanel.getBackground());
		
		JButton currentVersionButton = new JButton("Current Version");
		currentVersionButton.setToolTipText("Insert current GoldenGATE version number");
		currentVersionButton.setBackground(readmePanel.getBackground());
		currentVersionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				entryField.insert(GoldenGATE.VERSION_DATE, entryField.getCaretPosition());
			}
		});
		
		final JPanel entrySelectorPanel = new JPanel(new BorderLayout());
		entrySelectorPanel.add(entrySelectorLabel, BorderLayout.WEST);
		entrySelectorPanel.add(entrySelector, BorderLayout.CENTER);
		entrySelectorPanel.add(currentVersionButton, BorderLayout.EAST);
		
		final JPanel entryPanel = new JPanel(new BorderLayout());
		entryPanel.add(entryField, BorderLayout.CENTER);
		entryPanel.add(entrySelectorPanel, BorderLayout.SOUTH);
		
		readmePanel.add(entryPanel, gbc.clone());
		gbc.gridy++;
		
		if (bottom.size() != 0) {
			builder.addContent(bottom);
			prepareLeadingSpace(builder);
			
			JLabel bottomLabel = new JLabel(("<HTML>" + builder.concatStrings("<BR>") + "</HTML>"), JLabel.LEFT);
			bottomLabel.setOpaque(true);
			bottomLabel.setBackground(uneditableReadmeColor);
			bottomLabel.setFont(readmeFont);
			
			readmePanel.add(bottomLabel, gbc.clone());
			gbc.gridy++;
			builder.clear();
		}
		
		
		final JScrollPane readmePanelBox = new JScrollPane(readmePanel);
		readmePanelBox.getVerticalScrollBar().setUnitIncrement(50);
		readmePanelBox.getVerticalScrollBar().setBlockIncrement(100);
		
		
		JButton okButton = new JButton("OK");
		okButton.setBorder(BorderFactory.createRaisedBevelBorder());
		okButton.setPreferredSize(new Dimension(70, 21));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String entry = entryField.getText().trim();
				if ((entry.length() == 0) || entry.startsWith("<")) {
					JOptionPane.showMessageDialog(readmeDialog, ("Please enter a meaningful entry text for configuration '" + configName + "'."), "Description Missing", JOptionPane.ERROR_MESSAGE);
					entryField.requestFocusInWindow();
					return;
				}
				
				ReadmeEntry re = (recentReadmeEntries.isEmpty() ? null : ((ReadmeEntry) entrySelector.getSelectedItem()));
				if ((re != null) && re.entry.equals(entry))
					re.setLastUsed(timestamp);
				else recentReadmeEntries.add(new ReadmeEntry(entry, timestamp));
				
				readmeTemp.addContent(top);
				readmeTemp.addContent(entry.split("((\\n\\r)|(\\r\\n)|\\n|\\r)"));
				readmeTemp.addContent(bottom);
				
				readmeDialog.dispose();
			}
		});
		
		JButton skipButton = new JButton("Skip");
		skipButton.setBorder(BorderFactory.createRaisedBevelBorder());
		skipButton.setPreferredSize(new Dimension(70, 21));
		skipButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				readmeDialog.dispose();
			}
		});
		
		JButton requireCurrentVersionButton = new JButton("Current Version");
		requireCurrentVersionButton.setToolTipText("Set required GoldenGATE version number to current GoldenGATE version number");
		requireCurrentVersionButton.setBorder(BorderFactory.createRaisedBevelBorder());
		requireCurrentVersionButton.setPreferredSize(new Dimension(120, 21));
		requireCurrentVersionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				boolean changed = false;
				for (int t = 0; t < top.size(); t++) {
					String line = top.get(t);
					if (line.matches(".+[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}.*")) {
						line = line.replaceAll("[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}", GoldenGATE.VERSION_DATE);
						top.setElementAt(line, t);
						changed = true;
						t = top.size();
					}
					else if ("REQUIRED JAVA VERSION".equals(line))
						t = top.size();
				}
				
				if (changed) {
					builder.addContent(top);
					prepareLeadingSpace(builder);
					
					topLabel.setText("<HTML>" + builder.concatStrings("<BR>") + "<BR></HTML>");
					topLabel.setFont(readmeFont);
					topLabel.validate();
					
					builder.clear();
				}
			}
		});
		
		JButton editDescriptionButton = new JButton("Edit Description");
		editDescriptionButton.setToolTipText("Edit the description text of configuration '" + configName + "'");
		editDescriptionButton.setBorder(BorderFactory.createRaisedBevelBorder());
		editDescriptionButton.setPreferredSize(new Dimension(120, 21));
		editDescriptionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int dStart = 0;
				int dEnd = 0;
				for (int t = 0; t < top.size(); t++) {
					String line = top.get(t);
					
					//	info line
					if (line.matches(".+[0-9]{4}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}.*"))
						dStart = t+1;
					
					//	license header
					else if ("REQUIRED JAVA VERSION".equals(line)) {
						dEnd = t;
						t = top.size();
					}
				}
				
				while ((dStart < top.size()) && "".equals(top.get(dStart)))
					dStart++;
				while ((dEnd > 0) && "".equals(top.get(dEnd-1)))
					dEnd--;
				
				StringVector desc = new StringVector();
				for (int t = dStart; t < dEnd; t++)
					desc.addElement(top.get(t));
				
				if (editDescription(desc, configName)) {
					for (int d = 0; d < (dEnd - dStart); d++)
						top.removeElementAt(dStart);
					
					for (int d = 0; d < desc.size(); d++)
						top.insertElementAt(desc.get(d), (dStart + d));
					
					builder.addContent(top);
					prepareLeadingSpace(builder);
					
					topLabel.setText("<HTML>" + builder.concatStrings("<BR>") + "<BR></HTML>");
					topLabel.setFont(readmeFont);
					topLabel.validate();
					
					builder.clear();
				}
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		buttonPanel.add(skipButton);
		buttonPanel.add(new JLabel("    "));
		buttonPanel.add(requireCurrentVersionButton);
		buttonPanel.add(editDescriptionButton);
		
		
		readmeDialog.setLayout(new BorderLayout());
		readmeDialog.add(label, BorderLayout.NORTH);
		readmeDialog.add(readmePanelBox, BorderLayout.CENTER);
		readmeDialog.add(buttonPanel, BorderLayout.SOUTH);
		
		readmeDialog.setSize(700, 600);
		readmeDialog.setLocationRelativeTo(readmeDialog.getOwner());
		readmeDialog.setResizable(true);
		
		readmeDialog.getDialog().addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent we) {
				readmePanelBox.getViewport().scrollRectToVisible(entryPanel.getBounds());
			}
		});
		entryField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent de) {}
			public void insertUpdate(DocumentEvent de) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						Rectangle r = entryPanel.getBounds();
						if (r.height > readmePanelBox.getVisibleRect().height)
							return;
						Point p = r.getLocation();
						p = SwingUtilities.convertPoint(entryPanel.getParent(), p, readmePanelBox.getViewport());
						r = new Rectangle(p.x, p.y, r.width, r.height);
						readmePanelBox.getViewport().scrollRectToVisible(r);
					}
				});
			}
			public void removeUpdate(DocumentEvent de) {}
		});
		
		readmeDialog.setVisible(true);
		
		
		//	nothing changed
		if (readmeTemp.isEmpty())
			return false;
		
		//	perform changes
		readme.clear();
		readme.addContent(readmeTemp);
		
		//	indicate change
		return true;
	}
	
	private static final void prepareLeadingSpace(StringVector builder) {
		for (int b = 0; b < builder.size(); b++) {
			String s = builder.get(b);
			StringBuffer sb = new StringBuffer();
			int i = 0;
			while (s.startsWith(" ", i)) {
				sb.append("&nbsp;");
				i++;
			}
			if (i != 0) {
				sb.append(s.substring(i));
				builder.setElementAt(sb.toString(), b);
			}
		}
	}
	
	private static boolean editDescription(final StringVector description, final String configName) {
		final StringVector newDescription = new StringVector();
		
		//	create dialog
		final DialogPanel readmeDialog = new DialogPanel(("Edit Description of Configuration '" + configName + "'"), true);
		
		JLabel label = new JLabel(("<HTML>Edit the description text of the readme file of configuration '" + configName + "'.</HTML>"), JLabel.LEFT);
		label.setFont(new Font(label.getFont().getFamily(), Font.BOLD, 12));
		label.setBorder(BorderFactory.createLineBorder(label.getBackground(), 5));
		
		final JTextArea descriptionField = new JTextArea(description.concatStrings("\n"));
		descriptionField.setFont(readmeFont);
		
		JScrollPane descriptionFieldBox = new JScrollPane(descriptionField);
		descriptionFieldBox.getVerticalScrollBar().setUnitIncrement(50);
		descriptionFieldBox.getVerticalScrollBar().setBlockIncrement(100);
		
		JButton okButton = new JButton("OK");
		okButton.setBorder(BorderFactory.createRaisedBevelBorder());
		okButton.setPreferredSize(new Dimension(70, 21));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String description = descriptionField.getText().trim();
				if (description.length() == 0) {
					JOptionPane.showMessageDialog(readmeDialog, ("Please enter a meaningful description text for configuration '" + configName + "'."), "Description Missing", JOptionPane.ERROR_MESSAGE);
					descriptionField.requestFocusInWindow();
					return;
				}
				newDescription.addContent(description.split("((\\n\\r)|(\\r\\n)|\\n|\\r)"));
				readmeDialog.dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
		cancelButton.setPreferredSize(new Dimension(70, 21));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				readmeDialog.dispose();
			}
		});
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		buttonPanel.add(cancelButton);
		
		
		readmeDialog.setLayout(new BorderLayout());
		readmeDialog.add(label, BorderLayout.NORTH);
		readmeDialog.add(descriptionFieldBox, BorderLayout.CENTER);
		readmeDialog.add(buttonPanel, BorderLayout.SOUTH);
		
		readmeDialog.setSize(700, 300);
		readmeDialog.setLocationRelativeTo(readmeDialog.getOwner());
		readmeDialog.setResizable(true);
		readmeDialog.setVisible(true);
		
		if (newDescription.isEmpty())
			return false;
		
		description.clear();
		description.addContent(newDescription);
		return true;
	}
	
	private static final Font readmeFont = new Font("Verdana", Font.PLAIN, 12);
	
	private static final Color uneditableReadmeColor = new Color(224, 224, 224);
	
	//	cache for recent entries
	private static HashSet recentReadmeEntries = new HashSet();
	
	//	container for recent entries
	private static class ReadmeEntry implements Comparable {
		final String entry;
		final String entryPreview;
		long lastUsed = 0;
		
		ReadmeEntry(String entry, long lastUsed) {
			this.entry = entry;
			this.lastUsed = lastUsed;
			
			String preview = this.entry.replaceAll("((\\n\\r)|(\\r\\n)|\\n|\\r)", " ").trim();
			if (preview.length() < 80)
				this.entryPreview = preview;
			else this.entryPreview = (preview.substring(0, 76).trim() + " ...");
		}
		void setLastUsed(long lastUsed) {
			this.lastUsed = Math.max(this.lastUsed, lastUsed);
		}
		public boolean equals(Object obj) {
			return ((obj != null) && (this.compareTo(obj) == 0) && (this.hashCode() == obj.hashCode()));
		}
		public int compareTo(Object obj) {
			return ((int) (((ReadmeEntry) obj).lastUsed - this.lastUsed));
		}
		public int hashCode() {
			return this.entry.hashCode();
		}
		public String toString() {
			return (readmeTimestamper.format(new Date(this.lastUsed)) + ": " + this.entryPreview);
		}
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. The configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configHost, File baseFolder) throws IOException {
		return getConfiguration(configName, null, configHost, baseFolder, false, CACHE_UPDATE_POLICY_MOVE);
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. If the argument config path is not null, the configuration is
	 * loaded statically, i.e. without online updates, etc. If the config path
	 * starts with 'http://', the configuration is loaded from the web; if it
	 * starts with './', the path is interpreted relative to the argument base
	 * folder; otherwise, it is interpreted as an absolute path. If the config
	 * path is null, the configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configPath the path to load the configuration from
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configPath, String configHost, File baseFolder) throws IOException {
		return getConfiguration(configName, configPath, configHost, baseFolder, false, CACHE_UPDATE_POLICY_MOVE);
	} // older, more widely known
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. The configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param showStatusOnUpdate show a status dialog in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configHost, File baseFolder, boolean showStatusOnUpdate) throws IOException {
		return getConfiguration(configName, null, configHost, baseFolder, showStatusOnUpdate, CACHE_UPDATE_POLICY_MOVE);
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. If the argument config path is not null, the configuration is
	 * loaded statically, i.e. without online updates, etc. If the config path
	 * starts with 'http://', the configuration is loaded from the web; if it
	 * starts with './', the path is interpreted relative to the argument base
	 * folder; otherwise, it is interpreted as an absolute path. If the config
	 * path is null, the configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configPath the path to load the configuration from
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param showStatusOnUpdate show a status dialog in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configPath, String configHost, File baseFolder, boolean showStatusOnUpdate) throws IOException {
		return getConfiguration(configName, null, configHost, baseFolder, showStatusOnUpdate, CACHE_UPDATE_POLICY_MOVE);
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. The configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configHost, File baseFolder, int cachePolicy) throws IOException {
		return getConfiguration(configName, null, configHost, baseFolder, false, cachePolicy);
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. If the argument config path is not null, the configuration is
	 * loaded statically, i.e. without online updates, etc. If the config path
	 * starts with 'http://', the configuration is loaded from the web; if it
	 * starts with './', the path is interpreted relative to the argument base
	 * folder; otherwise, it is interpreted as an absolute path. If the config
	 * path is null, the configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configPath the path to load the configuration from
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configPath, String configHost, File baseFolder, int cachePolicy) throws IOException {
		return getConfiguration(configName, configPath, configHost, baseFolder, false, cachePolicy);
	} // older, more widely known
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. The configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param showStatusOnUpdate show a status dialog in case of an update?
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configHost, File baseFolder, boolean showStatusOnUpdate, int cachePolicy) throws IOException {
		return getConfiguration(configName, null, configHost, baseFolder, showStatusOnUpdate, cachePolicy);
	}
	
	/**
	 * Load a GoldenGATE Configuration with a given name from a choice of
	 * locations. If the argument config path is not null, the configuration is
	 * loaded statically, i.e. without online updates, etc. If the config path
	 * starts with 'http://', the configuration is loaded from the web; if it
	 * starts with './', the path is interpreted relative to the argument base
	 * folder; otherwise, it is interpreted as an absolute path. If the config
	 * path is null, the configuration is loaded from the base folder. If the
	 * config host is not null, it is checked for updates.
	 * @param configName the name of the configuration to load
	 * @param configPath the path to load the configuration from
	 * @param configHost the host to load the configuration from
	 * @param baseFolder the base folder of the GoldenGATE installation
	 * @param showStatusOnUpdate show a status dialog in case of an update?
	 * @param cachePolicy how to handle any cached content present in a local
	 *            configuration in case of an update?
	 * @return a GoldenGATE configuration loaded according to the specified
	 *         parameters
	 */
	public static GoldenGateConfiguration getConfiguration(String configName, String configPath, String configHost, File baseFolder, boolean showStatusOnUpdate, int cachePolicy) throws IOException {
		if (configName == null)
			return null;
		
		//	config path set, use it (likely for debugging)
		if (configPath != null) {
			
			//	URL based configuration, e.g. via ECS servlet
			if (configPath.startsWith("http://") || configPath.startsWith("https://")) {
				String ggConfigUrl = (configPath + (configPath.endsWith("/") ? "" : "/") + configName);
				return new UrlConfiguration(ggConfigUrl, configName);
			}
			
			//	file configuration in data folder
			else if (configPath.startsWith("./")) {
				configPath = configPath.substring(2);
				File ggConfigRoot = new File(baseFolder, configPath + (configPath.endsWith("/") ? "" : "/") + configName);
				return new FileConfiguration(configName, ggConfigRoot, false, true);
			}
			
			//	file configuration with absolute path, good for testing
			else {
				File ggConfigRoot = new File(configPath + (configPath.endsWith("/") ? "" : "/") + configName);
				return new FileConfiguration(configName, ggConfigRoot, false, true);
			}
		}
		
		//	use config host mechanism, with auto-update, etc.
		else {
			
			//	get available configurations
			ConfigurationDescriptor[] configurations = getConfigurations(configHost, baseFolder);
			
			//	find requested configuration
			ConfigurationDescriptor configuration = getConfiguration(configurations, configName, baseFolder, cachePolicy, false, showStatusOnUpdate);
			if (configuration == null)
				return null;
			
			//	local configuration
			if (configuration.host == null) {
				File configRoot = new File(new File(baseFolder, CONFIG_FOLDER_NAME), configuration.name);
				return new FileConfiguration(configuration.name, configRoot, false, true);
			}
			
			//	remote configuration
			String configRoot = (configuration.host + (configuration.host.endsWith("/") ? "" : "/") + configuration.name);
			return new UrlConfiguration(configRoot, configuration.name);
		}
	}
	
	private static ConfigurationDescriptor[] getConfigurations(String configHost, File dataFolder) {
		
		//	collect configurations
		ArrayList configList = new ArrayList();
		
		//	load local non-default configurations
		ConfigurationDescriptor[] configs = getLocalConfigurations(dataFolder);
		for (int c = 0; c < configs.length; c++)
			configList.add(configs[c]);
		
		//	get downloaded zip files
		configs = getZipConfigurations(dataFolder);
		for (int c = 0; c < configs.length; c++)
			configList.add(configs[c]);
		
		//	get available configurations from configuration hosts
		if ((configHost != null) && !configHost.startsWith("//")) {
			configs = getRemoteConfigurations(configHost);
			for (int c = 0; c < configs.length; c++)
				configList.add(configs[c]);
		}
		
		return ((ConfigurationDescriptor[]) configList.toArray(new ConfigurationDescriptor[configList.size()]));
	}
	
	public static void main(String[] args) throws Exception {
		GoldenGateConfiguration ggConfig = new FileConfiguration("Master", new File("E:/GoldenGATEv3"), true, true, null);
		GoldenGATE gg = GoldenGATE.openGoldenGATE(ggConfig, false);
		while (!gg.isStartupFinished())
			Thread.sleep(200);
		StringVector selected = new StringVector();
		selected.addElement("06.A.SelectTaxonNames.customFunction");
		selected.addElement("<ZooKeys XML Document Format>");
		Configuration config = buildConfiguration("Test", new File("E:/GoldenGATEv3"), gg, selected, null);
		config.writeXml(new BufferedWriter(new OutputStreamWriter(System.out)));
		String[] dataNames = getDataNameList(new File("E:/GoldenGATEv3"), config);
		for (int d = 0; d < dataNames.length; d++)
			System.out.println(dataNames[d]);
	}
}
