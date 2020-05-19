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


import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.security.AccessControlException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.util.GamtaClassLoader;
import de.uka.ipd.idaho.goldenGate.CustomFunction;
import de.uka.ipd.idaho.goldenGate.CustomShortcut;
import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.TokenizerManager;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Configuration;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.DataItem;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Lib;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Plugin;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Resource;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultCustomFunctionManager;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultCustomShortcutManager;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultTokenizerManager;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider;
import de.uka.ipd.idaho.goldenGate.plugins.PluginDataProviderPrefixBased;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * GoldenGATE configuration wrapped around an XML descriptor. The implementation
 * of the actual IO facilities is up to sub classes. Implementations may assume
 * that access to requested streams and URLs is permissible in terms of the
 * backing configuration descriptor. Thus, they need only provide implementation
 * specific access checks, if any.
 * 
 * @author sautter
 */
public abstract class XmlConfiguration implements GoldenGateConfiguration  {
	
	/**
	 * the configuration descriptor backing this configuration
	 */
	protected final Configuration descriptor;
	
//	/**
//	 * the GoldenGATE instance running on top of this configuration
//	 */
//	protected GoldenGATE ggInstance = null;
//	
	private TreeMap dataItems = new TreeMap();
	
	private int cacheMaxBytes = 2048;
	private HashMap byteCache = new HashMap();
	
	private BufferedWriter logWriter = null;
	
	private GoldenGatePlugin[] plugins;
	
	/**
	 * data provider acting relative to the configuration's root path, using the
	 * configuration's IO facilities; basis for plugin data providers
	 */
	protected GoldenGatePluginDataProvider baseDataProvider;
	
	/**
	 * Constructor
	 * @param descriptor the configuration descriptor to back this configuration
	 * @param logFile the file to write log entries to (specifying null disables
	 *            logging)
	 */
	public XmlConfiguration(Configuration descriptor, File logFile) {
		this.descriptor = descriptor;
		
		//	add configuration's data items
		for (Iterator dit = this.descriptor.dataItems.iterator(); dit.hasNext();) {
			DataItem dataItem = ((DataItem) dit.next());
			this.dataItems.put(dataItem.path, dataItem);
		}
		
		if (logFile != null) try {
			if (!logFile.exists()) {
				logFile.getParentFile().mkdirs();
				logFile.createNewFile();
			}
			logWriter = new BufferedWriter(new FileWriter(logFile, true));
		} catch (Exception e) {}
		
		this.baseDataProvider = new PluginDataProviderConfigurationBased(this);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws Throwable {
		if (logWriter != null) try {
			logWriter.flush();
			logWriter.close();
		}
		catch (Exception e) {}
	}
	
	/* (non-Javadoc)
	 * @see de.goldenGate.GoldenGateConfiguration#getName()
	 */
	public String getName() {
		return this.descriptor.name;
	}
	
	/**
	 * Retrieve the path of this configuration, relative to the root path of the
	 * surrounding GoldenGATE installation. If a configuration does no have a
	 * path, this method should return null. If the path is equal to the root
	 * path, the return value should be '.'. This default implementation returns
	 * null. Sub classes are welcome to overwrite it as needed.
	 * @return the relative path of this configuration
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getPath()
	 */
	public String getPath() {
		return null;
	}
//	
//	/* (non-Javadoc)
//	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#setGoldenGateInstance(de.uka.ipd.idaho.goldenGate.GoldenGATE)
//	 */
//	public void setGoldenGateInstance(GoldenGATE gg) throws IOException {
//		if (this.ggInstance != null)
//			throw new IOException("There can be only one GoldenGATE instance per configuration object.");
//		this.ggInstance = gg;
//	}
	
	/**
	 * Ask if acccessing the web is allowed for this configuration. This default
	 * implementation returns true. Sub classes are welcome to overwrite it as
	 * needed.
	 * @return true if GoldenGATE plugin componentes are generally allowed to
	 *         access the network and web when working with this configuration
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return true;
	}
	
	/**
	 * Test if this configuration is editable. This default implementation
	 * returns false. Sub classes are welcome to overwrite it as needed.
	 * @return true if this configuration object can be edited
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isDataEditable()
	 */
	public boolean isDataEditable() {
		return false;
	}
	
	/**
	 * Ask if this configuration is a master configuration. Starting with a
	 * master configuration will cause GoldenGATE to show the main menu entries
	 * of resource managers and the Window menu, effectively facilitating
	 * configuring the editor. This default implementation returns false. Sub
	 * classes are welcome to overwrite it as needed.
	 * @return true if this configuration is a master configuration
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isMasterConfiguration()
	 */
	public boolean isMasterConfiguration() {
		return false;
	}
	
	/**
	 * Retrieve additional items for the File menu that allow for accessing
	 * implementation-specific functions of the configuration. This default
	 * implementation returns an empty array. Sub classes are welcome to
	 * overwrite it as needed.
	 * @return an array holding implementation specific entries for the File
	 *         menu
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getFileMenuItems()
	 */
	public JMenuItem[] getFileMenuItems() {
		return new JMenuItem[0];
	}
	
	/**
	 * Retrieve additional items for the Window menu that allow for accessing
	 * implementation-specific functions of the configuration. The items
	 * returned by this method are only included in the GUI if
	 * isMasterConfiguration() returns true. This default implementation returns
	 * an empty array. Sub classes are welcome to overwrite it as needed.
	 * @return an array holding implementation specific entries for the Window
	 *         menu
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getWindowMenuItems()
	 */
	public JMenuItem[] getWindowMenuItems() {
		return new JMenuItem[0];
	}
	
	/**
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getPlugins()
	 */
	public final GoldenGatePlugin[] getPlugins() {
		
		//	load plugins on demand
		if (this.plugins == null) {
			this.plugins = loadPlugins();
			
			//	test if specific managers given
			CustomFunction.Manager customFunctionManager = null;
			CustomShortcut.Manager customShortcutManager = null;
			TokenizerManager tokenizerManager = null;
			ArrayList pluginList = new ArrayList();
			for (int p = 0; p < this.plugins.length; p++) {
				if (this.plugins[p] instanceof CustomFunction.Manager)
					customFunctionManager = ((CustomFunction.Manager) this.plugins[p]);
				if (this.plugins[p] instanceof CustomShortcut.Manager)
					customShortcutManager = ((CustomShortcut.Manager) this.plugins[p]);
				if (this.plugins[p] instanceof TokenizerManager)
					tokenizerManager = ((TokenizerManager) this.plugins[p]);
				pluginList.add(this.plugins[p]);
			}
			
			//	use default implementations if no custom implementations present
			if (customFunctionManager == null) {
				Plugin cfm = ((Plugin) this.descriptor.pluginsByClassName.get(DefaultCustomFunctionManager.class.getName()));
				if (cfm != null) {
					customFunctionManager = new DefaultCustomFunctionManager();
					customFunctionManager.setDataProvider(this.getPluginDataProvider(cfm));
					pluginList.add(customFunctionManager);
				}
			}
			if (customShortcutManager == null) {
				Plugin csm = ((Plugin) this.descriptor.pluginsByClassName.get(DefaultCustomShortcutManager.class.getName()));
				if (csm != null) {
					customShortcutManager = new DefaultCustomShortcutManager();
					customShortcutManager.setDataProvider(this.getPluginDataProvider(csm));
					pluginList.add(customShortcutManager);
				}
			}
			if (tokenizerManager == null) {
				Plugin tm = ((Plugin) this.descriptor.pluginsByClassName.get(DefaultTokenizerManager.class.getName()));
				if (tm != null) {
					tokenizerManager = new DefaultTokenizerManager();
					tokenizerManager.setDataProvider(this.getPluginDataProvider(tm));
					pluginList.add(tokenizerManager);
				}
			}
			
			//	had to use one of the default implementations?
			if (this.plugins.length < pluginList.size())
				this.plugins = ((GoldenGatePlugin[]) pluginList.toArray(new GoldenGatePlugin[pluginList.size()]));
		}
		
		GoldenGatePlugin[] plugins = new GoldenGatePlugin[this.plugins.length];
		for (int d = 0; d < this.plugins.length; d++)
			plugins[d] = this.plugins[d];
		return plugins;
	}
	
	/**
	 * Write an entry to the log file. This default implementation writes to the
	 * log file that was specified to the constructor - if it is not null.
	 * @param entry the entry to write
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#writeLog(java.lang.String)
	 */
	public void writeLog(String entry) {
		if (this.logWriter != null) {
			try {
				logWriter.write(entry);
				logWriter.newLine();
				logWriter.flush();
			}
			catch (IOException ioe) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getSettings()
	 */
	public Settings getSettings() {
		InputStream sis = null;
		try {
			sis = this.getInputStream(this.descriptor.settingsPath, this.descriptor.configTimestamp);
			return Settings.loadSettings(sis);
		}
		catch (IOException ioe) {
			System.out.println("Exception getting settings: " + ioe.getMessage());
			ioe.printStackTrace(System.out);
			return new Settings(); // return empty settings so defaults are user in main program
		}
		finally {
			if (sis != null) try {
				sis.close();
			} catch (IOException ioe) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#storeSettings(de.easyIO.settings.Settings)
	 */
	public void storeSettings(Settings settings) throws IOException {
		OutputStream sos = null;
		try {
			sos = this.getOutputStream(this.descriptor.settingsPath);
			settings.storeAsText(sos);
			sos.flush();
		}
		catch (IOException ioe) {
			System.out.println("Exception storing settings: " + ioe.getMessage());
			ioe.printStackTrace(System.out);
			throw ioe;
		}
		finally {
			if (sos != null)
				sos.close();
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getIconImage()
	 */
	public Image getIconImage() {
		if (this.iconImage == null) {
			InputStream iis = null;
			try {
				iis = this.getInputStream(this.descriptor.iconImagePath, this.descriptor.configTimestamp);
				this.iconImage = ImageIO.read(iis);
			}
			catch (IOException ioe) {
				System.out.println("Exception getting icon image: " + ioe.getMessage());
				ioe.printStackTrace(System.out);
			}
			finally {
				if (iis != null) try {
					iis.close();
				} catch (IOException e) {}
			}
		}
		return this.iconImage;
	}
	private Image iconImage = null; // cache icon image
	
	private GoldenGatePlugin[] loadPlugins() {
		
		//	collect class names and jar files to load
		StringVector jarNames = new StringVector();
		HashMap jarTimestamps = new HashMap();
		for (Iterator pit = this.descriptor.plugins.iterator(); pit.hasNext();) {
			Plugin plugin = ((Plugin) pit.next());
			
			//	read main attributes
			String pClassPath = plugin.classPath;
			
			//	check empty classPath (indicates using default implementation resident in the base jars - may happen for CFM, CSM, and TM)
			if (pClassPath.length() != 0) {
				
				//	complete & store class path
				jarNames.addElementIgnoreDuplicates(pClassPath);
				jarTimestamps.put(pClassPath, new Long(plugin.timestamp));
				
				//	get libraries (for non-native plugins, native ones are the only ones with no '/' in their class path)
				if (pClassPath.lastIndexOf('/') != -1) {
					String pLibBasePath = pClassPath.substring(0, pClassPath.lastIndexOf('/'));
					for (Iterator lit = plugin.libs.iterator(); lit.hasNext();) {
						Lib lib = ((Lib) lit.next());
						
						//	complete & store library path
						jarNames.addElementIgnoreDuplicates(pLibBasePath + "/" + lib.path);
						jarTimestamps.put((pLibBasePath + "/" + lib.path), new Long(lib.timestamp));
					}
				}
			}
		}
		
		//	create class loader
		GamtaClassLoader pluginLoader = GamtaClassLoader.createClassLoader(GoldenGatePlugin.class);
		for (int u = 0; u < jarNames.size(); u++) try {
			String jarName = jarNames.get(u);
			pluginLoader.addJar(this.getInputStream(jarName, ((Long) jarTimestamps.get(jarName)).longValue()));
		}
		catch (IOException ioe) {
			System.out.println("Exception adding jar '" + jarNames.get(u) + "': " + ioe.getMessage());
			ioe.printStackTrace(System.out);
		}
		
		//	instantiate plugins
		ArrayList pluginList = new ArrayList();
		for (Iterator pit = this.descriptor.plugins.iterator(); pit.hasNext();) {
			Plugin plugin = ((Plugin) pit.next());
			System.out.println("EcsConfiguration:  trying to load plugin class '" + plugin.className + "'");
				
			//	load and instantiate class
			Class pluginClass = null;
			
			try {
				//	could not create class loader, try system class path
				if (pluginLoader == null)
					pluginClass = Class.forName(plugin.className);
				
				//	load class through specific class loader if given
				else pluginClass = pluginLoader.loadClass(plugin.className);
			}
			catch (ClassNotFoundException cnfe) {
				System.out.println("  could not find plugin class: " + cnfe.getMessage());
			}
			catch (NoClassDefFoundError ncdfe) {
				System.out.println("  could not find some part of plugin class: " + ncdfe.getMessage());
			}
			catch (SecurityException se) {
				System.out.println("  not allowed to load plugin class: " + se.getMessage());
			} // may happen due to jar signatures
			
			//	Analyzer class loaded successfully
			if ((pluginClass != null) && !Modifier.isAbstract(pluginClass.getModifiers()) && Modifier.isPublic(pluginClass.getModifiers()) && !Modifier.isInterface(pluginClass.getModifiers()) && GoldenGatePlugin.class.isAssignableFrom(pluginClass)) {
				System.out.println("  got plugin class - " + plugin.className);
				
				Object loadedPlugin = null;
				try {
					loadedPlugin = pluginClass.newInstance();
					System.out.println("  plugin class successfully instantiated.");
				}
				catch (InstantiationException e) {
					System.out.println("  could not instantiate plugin class.");
				}
				catch (IllegalAccessException e) {
					System.out.println("  illegal acces to plugin class.");
				}
				catch (AccessControlException ace) {
					Permission per = ace.getPermission();
					if (per == null) System.out.println("  plugin violated security constraint.");
					else System.out.println("  plugin violated security constraint, permission '" + per.getActions() + "' was denied for '" + per.getName() + "' by runtime environment.");
				}
				catch (NoClassDefFoundError ncdfe) {
					System.out.println("  could not find some part of plugin class: " + ncdfe.getMessage());
				}
				
				//	set data provider & store plugin
				if (loadedPlugin != null) {
					((GoldenGatePlugin) loadedPlugin).setDataProvider(this.getPluginDataProvider(plugin));
					pluginList.add((GoldenGatePlugin) loadedPlugin);
				}
			}
		}
		
		//	return plugins
		return ((GoldenGatePlugin[]) pluginList.toArray(new GoldenGatePlugin[pluginList.size()]));
	}
	
	private GoldenGatePluginDataProvider getPluginDataProvider(Plugin plugin) {
		System.out.println("Building data provider for " + plugin.name + " ...");
		System.out.println("  data path is " + plugin.dataPath);
		
		//	collect plugin's data names relative to local base folder (for filtering in base data provider)
		HashMap dataItems = new HashMap();
		
		//	add plugin's data items
		for (Iterator dit = plugin.dataItems.iterator(); dit.hasNext();) {
			DataItem dataItem = ((DataItem) dit.next());
			DataItem resolvedDataItem = new DataItem((plugin.dataPath + "/" + dataItem.path), dataItem.timestamp);
			dataItems.put(resolvedDataItem.path, resolvedDataItem);
//			System.out.println("  got data item in " + resolvedDataItem.path);
		}
		
		//	add data items for plugin's resources
		for (Iterator rit = plugin.resources.iterator(); rit.hasNext();) {
			Resource resource = ((Resource) rit.next());
			if (resource.path.length() != 0) {
				DataItem resolvedResourceDataItem = new DataItem((plugin.dataPath + "/" + resource.path), resource.timestamp);
				dataItems.put(resolvedResourceDataItem.path, resolvedResourceDataItem);
			}
//			System.out.println("  got resource in " + resolvedResourceDataItem.path);
			
			//	add resource's data items
			for (Iterator dit = resource.dataItems.iterator(); dit.hasNext();) {
				DataItem dataItem = ((DataItem) dit.next());
				DataItem resolvedDataItem = new DataItem((plugin.dataPath + "/" + dataItem.path), dataItem.timestamp);
				dataItems.put(resolvedDataItem.path, resolvedDataItem);
//				System.out.println("    got data item in " + resolvedDataItem.path);
			}
		}
//		
//		//	create base data provider on demand
//		if (this.baseDataProvider == null)
//			this.baseDataProvider = new ExtensibleDataProvider();
		
		//	add resolved data items to base data provider
		this.dataItems.putAll(dataItems);
//		this.baseDataProvider.addData(dataItems);
		
		//	create & return prefix based data provider for plugin
		return new PluginDataProviderPrefixBased(this.baseDataProvider, plugin.dataPath);
	}
	
//	/*
//	 * The base data provider does all the actual IO. Prefix-based data
//	 * providers are used for all other purposes
//	 */
//	private ExtensibleDataProvider baseDataProvider = null;
//	
//	/**
//	 * Abstract implementation of a data provider with an extensible filter,
//	 * used as base data provider
//	 * 
//	 * @author sautter
//	 */
//	private class ExtensibleDataProvider implements GoldenGatePluginDataProvider {
//		
//		protected TreeMap dataItems = new TreeMap();
//		
//		protected int cacheMaxBytes = 2048;
//		protected HashMap byteCache = new HashMap();
//		
//		void addData(Map dataItems) {
//			this.dataItems.putAll(dataItems);
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
//		 */
//		public boolean allowWebAccess() {
//			return XmlConfiguration.this.allowWebAccess();
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
//		 */
//		public boolean isDataAvailable(String dataName) {
//			return this.dataItems.containsKey(dataName);
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
//		 */
//		public String[] getDataNames() {
//			return ((String[]) this.dataItems.keySet().toArray(new String[this.dataItems.size()]));
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
//		 */
//		public InputStream getInputStream(String dataName) throws IOException {
//			
//			//	cache lookup
//			if (this.byteCache.containsKey(dataName))
//				return new ByteArrayInputStream((byte[]) this.byteCache.get(dataName));
//			
//			//	get data itme
//			DataItem dataItem = ((DataItem) this.dataItems.get(dataName));
//			
//			//	requested data item not in filter
//			if (dataItem == null)
//				throw new IOException("A data item named '" + dataName + "' does not exist.");
//			
//			//	localize data if possible and return stream
//			else {
//				
//				//	create stream
//				InputStream is = XmlConfiguration.this.getInputStream(dataName, dataItem.timestamp);
//				
//				//	read up to cache limit or end of stream
//				BufferedInputStream bis = new BufferedInputStream(is, this.cacheMaxBytes);
//				bis.mark(this.cacheMaxBytes + 1);
//				byte[] testBytes = new byte[this.cacheMaxBytes];
//				int read = 0;
//				int tb;
//				while (((tb = bis.read()) != -1) && (read < testBytes.length))
//					testBytes[read++] = ((byte) tb);
//				
//				//	put file in cache if small enough
//				if ((0 < read) && (read < this.cacheMaxBytes)) {
//					byte[] bytes = new byte[read];
//					System.arraycopy(testBytes, 0, bytes, 0, read);
//					this.byteCache.put(dataName, bytes);
//					bis.close();
//					return new ByteArrayInputStream(bytes);
//				}
//				
//				//	return reader for original stream otherwise
//				else {
//					bis.reset();
//					return new TimeoutInputStream(bis, dataName);
//				}
//			}
//		}
//		
//		/**
//		 * wrapper for input streams to automatically close the wrapped stream
//		 * if inactive for more than 30 seconds
//		 * 
//		 * @author sautter
//		 */
//		private class TimeoutInputStream extends FilterInputStream {
//			private String dataName;
//			
//			private long lastRead = System.currentTimeMillis();
//			
//			TimeoutInputStream(InputStream in, String dn) {
//				super(in);
//				this.dataName = dn;
//				
//				//	start watchdog
//				new Thread() {
//					public void run() {
//						
//						//	watch out if stream closed externally
//						while (lastRead != -1) {
//							
//							//	close stream if timeout expired
//							if ((System.currentTimeMillis() - lastRead) > 30000) {
//								try {
//									System.out.println("Auto-Closing InputStream for '" + dataName + "'");
//									close();
//								} catch (IOException ioe) {}
//								return;
//							}
//							
//							//	wait 1 second before checking again
//							try {
//								sleep(1000);
//							} catch (InterruptedException ie) {}
//						}
//					}
//				}.start();
//			}
//			
//			public void close() throws IOException {
//				this.lastRead = -1;
//				super.close();
//			}
//
//			public int read() throws IOException {
//				this.lastRead = System.currentTimeMillis();
//				return super.read();
//			}
//
//			protected void finalize() throws Throwable {
//				if (this.lastRead != -1) {
//					System.out.println("Auto-Closing InputStream on Finalization for '" + this.dataName + "'");
//					this.close();
//				}
//			}
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable()
//		 */
//		public boolean isDataEditable() {
//			return XmlConfiguration.this.isEditable();
//		}
//
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable(java.lang.String)
//		 */
//		public boolean isDataEditable(String dataName) {
//			/*
//			 * Allow editing data visible through the filter and creating new
//			 * data locally if not existing locally behind the filter.
//			 * Theoretically, this is a security gap, since non-localized
//			 * existing data items might be created locally. But in practice,
//			 * each plugin has its own data folder, and re-creating a resource
//			 * by chance is quite improbable. Even more so since non-admin users
//			 * won't have the respective menu options available.
//			 */
//			return (this.isDataEditable() && (this.dataItems.containsKey(dataName) || isMasterConfiguration()));
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
//		 */
//		public OutputStream getOutputStream(String dataName) throws IOException {
//			if (this.isDataEditable(dataName)) {
//				OutputStream os = XmlConfiguration.this.getOutputStream(dataName);
//				this.byteCache.remove(dataName);
//				this.dataItems.put(dataName, new DataItem(dataName, System.currentTimeMillis()));
//				return new TimeoutOutputStream(os, dataName);
//			}
//			else throw new IOException("Write access denied, '" + dataName + "' cannot be written to.");
//		}
//		
//		/**
//		 * wrapper for output streams to automatically flush and close the
//		 * wrapped stream if inactive for more than 30 seconds
//		 * 
//		 * @author sautter
//		 */
//		private class TimeoutOutputStream extends FilterOutputStream {
//			private String dataName; 
//			
//			private long lastWritten = System.currentTimeMillis();
//			
//			TimeoutOutputStream(OutputStream out, String dn) {
//				super(out);
//				this.dataName = dn;
//				
//				//	start watchdog
//				new Thread() {
//					public void run() {
//						
//						//	watch out if stream closed externally
//						while (lastWritten != -1) {
//							
//							//	close stream if timeout expired
//							if ((System.currentTimeMillis() - lastWritten) > 30000) {
//								try {
//									System.out.println("Auto-Closing OutputStream for '" + dataName + "'");
//									flush();
//									close();
//								} catch (IOException ioe) {}
//								return;
//							}
//							
//							//	wait 1 second before checking again
//							try {
//								sleep(1000);
//							} catch (InterruptedException ie) {}
//						}
//					}
//				}.start();
//			}
//			
//			public void close() throws IOException {
//				this.lastWritten = -1;
//				super.close();
//			}
//
//			public void write(int b) throws IOException {
//				this.lastWritten = System.currentTimeMillis();
//				super.write(b);
//			}
//			
//			protected void finalize() throws Throwable {
//				if (this.lastWritten != -1) {
//					System.out.println("Auto-Closing OutputStream on Finalization for '" + this.dataName + "'");
//					this.flush();
//					this.close();
//				}
//			}
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
//		 */
//		public boolean deleteData(String dataName) {
//			
//			//	delete data item
////			try {
//				if (!XmlConfiguration.this.deleteData(dataName))
//					return false;
////			}
////			catch (IOException e) {
////				e.printStackTrace();
////			}
//			
//			//	remove data item from cache
//			this.byteCache.remove(dataName);
//			
//			//	remove data item from filter
//			this.dataItems.remove(dataName);
//			
//			//	check success
//			return !this.dataItems.containsKey(dataName);
//		}
//		
//		/* (non-Javadoc)
//		 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
//		 */
//		public URL getURL(String dataName) throws IOException {
//			
//			URL url;
//			
//			//	handle absolute URL
//			if (dataName.indexOf("://")  != -1)
//				url = XmlConfiguration.this.getURL(dataName, 0);
//			
//			//	handle relative data URL
//			else {
//				
//				//	get data item
//				DataItem dataItem = ((DataItem) this.dataItems.get(dataName));
//				
//				//	requested data item not in filter
//				if (dataItem == null)
//					throw new IOException("A data item named '" + dataName + "' does not exist.");
//				
//				//	remove data item from cache
//				this.byteCache.remove(dataName);
//				
//				//	produce URL
//				url = XmlConfiguration.this.getURL(dataName, dataItem.timestamp);
//			}
//			
//			//	return URL
//			System.out.println("Mapped data name '" + dataName + "' to " + url.toString());
//			return url;
//		}
//	}
//	
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getDataNames()
	 */
	public String[] getDataNames() {
		return ((String[]) this.dataItems.keySet().toArray(new String[this.dataItems.size()]));
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.dataItems.containsKey(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		
		//	cache lookup
		if (this.byteCache.containsKey(dataName))
			return new ByteArrayInputStream((byte[]) this.byteCache.get(dataName));
		
		//	get data itme
		DataItem dataItem = ((DataItem) this.dataItems.get(dataName));
		
		//	requested data item not in filter
		if (dataItem == null)
			throw new IOException("A data item named '" + dataName + "' does not exist.");
		
		//	localize data if possible and return stream
		else {
			
			//	create stream
			InputStream is = this.getInputStream(dataName, dataItem.timestamp);
			
			//	read up to cache limit or end of stream
			BufferedInputStream bis = new BufferedInputStream(is, this.cacheMaxBytes);
			bis.mark(this.cacheMaxBytes + 1);
			byte[] testBytes = new byte[this.cacheMaxBytes];
			int read = 0;
			int tb;
			while (((tb = bis.read()) != -1) && (read < testBytes.length))
				testBytes[read++] = ((byte) tb);
			
			//	put file in cache if small enough
			if ((0 < read) && (read < this.cacheMaxBytes)) {
				byte[] bytes = new byte[read];
				System.arraycopy(testBytes, 0, bytes, 0, read);
				this.byteCache.put(dataName, bytes);
				bis.close();
				return new ByteArrayInputStream(bytes);
			}
			
			//	return reader for original stream otherwise
			else {
				bis.reset();
				return new TimeoutInputStream(bis, dataName);
			}
		}
	}
	
	/**
	 * wrapper for input streams to automatically close the wrapped stream
	 * if inactive for more than 30 seconds
	 * 
	 * @author sautter
	 */
	private class TimeoutInputStream extends FilterInputStream {
		private String dataName;
		
		private long lastRead = System.currentTimeMillis();
		
		TimeoutInputStream(InputStream in, String dn) {
			super(in);
			this.dataName = dn;
			
			//	start watchdog
			new Thread() {
				public void run() {
					
					//	watch out if stream closed externally
					while (lastRead != -1) {
						
						//	close stream if timeout expired
						if ((System.currentTimeMillis() - lastRead) > 30000) {
							try {
								System.out.println("Auto-Closing InputStream for '" + dataName + "'");
								close();
							} catch (IOException ioe) {}
							return;
						}
						
						//	wait 1 second before checking again
						try {
							sleep(1000);
						} catch (InterruptedException ie) {}
					}
				}
			}.start();
		}
		
		public void close() throws IOException {
			this.lastRead = -1;
			super.close();
		}

		public int read() throws IOException {
			this.lastRead = System.currentTimeMillis();
			return super.read();
		}

		protected void finalize() throws Throwable {
			if (this.lastRead != -1) {
				System.out.println("Auto-Closing InputStream on Finalization for '" + this.dataName + "'");
				this.close();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		/*
		 * Allow editing data visible through the filter and creating new
		 * data locally if not existing locally behind the filter.
		 * Theoretically, this is a security gap, since non-localized
		 * existing data items might be created locally. But in practice,
		 * each plugin has its own data folder, and re-creating a resource
		 * by chance is quite improbable. Even more so since non-admin users
		 * won't have the respective menu options available.
		 */
		return (this.isDataEditable() && (this.dataItems.containsKey(dataName) || isMasterConfiguration()));
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		if (this.isDataEditable(dataName)) {
			OutputStream os = this.doGetOutputStream(dataName);
			this.byteCache.remove(dataName);
			this.dataItems.put(dataName, new DataItem(dataName, System.currentTimeMillis()));
			return new TimeoutOutputStream(os, dataName);
		}
		else throw new IOException("Write access denied, '" + dataName + "' cannot be written to.");
	}
	
	/**
	 * wrapper for output streams to automatically flush and close the
	 * wrapped stream if inactive for more than 30 seconds
	 * 
	 * @author sautter
	 */
	private class TimeoutOutputStream extends FilterOutputStream {
		private String dataName; 
		
		private long lastWritten = System.currentTimeMillis();
		
		TimeoutOutputStream(OutputStream out, String dn) {
			super(out);
			this.dataName = dn;
			
			//	start watchdog
			new Thread() {
				public void run() {
					
					//	watch out if stream closed externally
					while (lastWritten != -1) {
						
						//	close stream if timeout expired
						if ((System.currentTimeMillis() - lastWritten) > 30000) {
							try {
								System.out.println("Auto-Closing OutputStream for '" + dataName + "'");
								flush();
								close();
							} catch (IOException ioe) {}
							return;
						}
						
						//	wait 1 second before checking again
						try {
							sleep(1000);
						} catch (InterruptedException ie) {}
					}
				}
			}.start();
		}
		
		public void close() throws IOException {
			this.lastWritten = -1;
			super.close();
		}
		
		public void write(int b) throws IOException {
			this.lastWritten = System.currentTimeMillis();
			super.write(b);
		}
		
		protected void finalize() throws Throwable {
			if (this.lastWritten != -1) {
				System.out.println("Auto-Closing OutputStream on Finalization for '" + this.dataName + "'");
				this.flush();
				this.close();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		
		//	delete data item
		try {
			if (!this.doDeleteData(dataName))
				return false;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		//	remove data item from cache
		this.byteCache.remove(dataName);
		
		//	remove data item from filter
		this.dataItems.remove(dataName);
		
		//	check success
		return !this.dataItems.containsKey(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		
		URL url;
		
		//	handle absolute URL
		if (dataName.indexOf("://")  != -1)
			url = XmlConfiguration.this.getURL(dataName, 0);
		
		//	handle relative data URL
		else {
			
			//	get data item
			DataItem dataItem = ((DataItem) this.dataItems.get(dataName));
			
			//	requested data item not in filter
			if (dataItem == null)
				throw new IOException("A data item named '" + dataName + "' does not exist.");
			
			//	remove data item from cache
			this.byteCache.remove(dataName);
			
			//	produce URL
			url = XmlConfiguration.this.getURL(dataName, dataItem.timestamp);
		}
		
		//	return URL
		System.out.println("Mapped data name '" + dataName + "' to " + url.toString());
		return url;
	}
	
	/**
	 * Provide a URL for accessing a given data item. If the argument specifies
	 * a protocol, it should be treated as an absolute URL. The nested
	 * implementetion of GoldenGateConfiguration loops through to this
	 * method. If the argument URL is absolute, the time stamp is 0.
	 * @param dataName the name of the data item to obtain a URL for
	 * @param timestamp the time stamp of the data item in the configuration
	 *            descriptor backing this configuration
	 * @return a URL for accessing the data item with the specified name
	 * @throws IOException
	 */
	protected abstract URL getURL(String dataName, long timestamp) throws IOException;
	
	/**
	 * Provide an InputStream for reading a given data item. The nested
	 * implementetion of GoldenGateConfiguration loops through to this
	 * method.
	 * @param dataName the name of the data item to obtain an InputStream for
	 * @param timestamp the time stamp of the data item in the configuration
	 *            descriptor backing this configuration
	 * @return an InputStream for reading the data item with the specified name
	 * @throws IOException
	 */
	protected abstract InputStream getInputStream(String dataName, long timestamp) throws IOException;
	
	/**
	 * Provide an OutputStream for actually writing a given data item. The
	 * implementetion of getOutputStream() loops through to this method after
	 * accessibility checks.
	 * @param dataName the name of the data item to obtain an OutputStream for
	 * @return an OutputStream for writing the data item with the specified name
	 * @throws IOException
	 */
	protected abstract OutputStream doGetOutputStream(String dataName) throws IOException;
	
	/**
	 * Delete a given data item. The implementetion deleteData() loops through
	 * to this method after cleanup operations.
	 * @param dataName the name of the data item to delete
	 * @return true if the data item with the specified name was deleted
	 *         successfully, false otherwise
	 * @throws IOException
	 */
	protected abstract boolean doDeleteData(String dataName) throws IOException;
}
