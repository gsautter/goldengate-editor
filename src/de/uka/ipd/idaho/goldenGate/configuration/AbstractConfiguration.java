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

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JMenuItem;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.util.GamtaClassLoader;
import de.uka.ipd.idaho.gamta.util.GamtaClassLoader.ComponentInitializer;
import de.uka.ipd.idaho.gamta.util.GamtaClassLoader.InputStreamProvider;
import de.uka.ipd.idaho.goldenGate.CustomFunction;
import de.uka.ipd.idaho.goldenGate.CustomShortcut;
import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.TokenizerManager;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultCustomFunctionManager;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultCustomShortcutManager;
import de.uka.ipd.idaho.goldenGate.plugins.DefaultTokenizerManager;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider;
import de.uka.ipd.idaho.goldenGate.plugins.PluginDataProviderPrefixBased;

/**
 * Abstract implementation of a GoldenGATE configuration. This class provides
 * basic methods that are not overwritten frequently, plus the plugin loading
 * facilities. Sub classes have to implement IO facilities.
 * 
 * @author sautter
 */
public abstract class AbstractConfiguration implements GoldenGateConfiguration {
	
	/**
	 * the name of the configuration
	 */
	protected final String name;
	
//	private BufferedWriter logWriter = null;
//	
	private GoldenGatePlugin[] plugins = null;
	
	/**
	 * data provider acting relative to the configuration's root path, using the
	 * configuration's IO facilities; basis for plugin data providers
	 */
	protected GoldenGatePluginDataProvider baseDataProvider;
	
	/**
	 * Constructor
	 * @param name the name of the configuration
	 */
	protected AbstractConfiguration(String name) {
		this(name, null);
	}
	
	/**
	 * Constructor
	 * @param name the name of the configuration
	 * @param logFile the file to write log entries to (specifying null turns
	 *            off logging)
	 */
	protected AbstractConfiguration(String name, File logFile) {
		this.name = name;
		this.baseDataProvider = new PluginDataProviderConfigurationBased(this);
//		if (logFile != null) try {
//			if (!logFile.exists()) {
//				logFile.getParentFile().mkdirs();
//				logFile.createNewFile();
//			}
//			this.logWriter = new BufferedWriter(new FileWriter(logFile, true));
//		} catch (Exception e) {}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */
	public void finalize() throws Throwable {
		super.finalize();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#writeLog(java.lang.String)
	 */
	public void writeLog(String entry) {
		System.out.println(entry);
//		if (this.logWriter != null) {
//			try {
//				this.logWriter.write(entry);
//				this.logWriter.newLine();
//				this.logWriter.flush();
//			} catch (IOException e) {}
//		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getName()
	 */
	public String getName() {
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isMasterConfiguration()
	 */
	public boolean isMasterConfiguration() {
		return false;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getFileMenuItems()
	 */
	public JMenuItem[] getFileMenuItems() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getWindowMenuItems()
	 */
	public JMenuItem[] getWindowMenuItems() {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getPlugins()
	 */
	public GoldenGatePlugin[] getPlugins() {
		
		//	load plugins on demand
		if (this.plugins == null) {
			this.plugins = this.loadPlugins();
			
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
				customFunctionManager = new DefaultCustomFunctionManager();
				customFunctionManager.setDataProvider(new PluginDataProviderPrefixBased(this.baseDataProvider, CUSTOM_FUNCTIONS_FOLDER_NAME));
				pluginList.add(customFunctionManager);
			}
			if (customShortcutManager == null) {
				customShortcutManager = new DefaultCustomShortcutManager();
				customShortcutManager.setDataProvider(new PluginDataProviderPrefixBased(this.baseDataProvider, CUSTOM_SHORTCUTS_FOLDER_NAME));
				pluginList.add(customShortcutManager);
			}
			if (tokenizerManager == null) {
				tokenizerManager = new DefaultTokenizerManager();
				tokenizerManager.setDataProvider(new PluginDataProviderPrefixBased(this.baseDataProvider, TOKENIZER_FOLDER_NAME));
				pluginList.add(tokenizerManager);
			}
			
			//	had to use one of the default implementations?
			if (this.plugins.length < pluginList.size())
				this.plugins = ((GoldenGatePlugin[]) pluginList.toArray(new GoldenGatePlugin[pluginList.size()]));
		}
		
		GoldenGatePlugin[] plugins = new GoldenGatePlugin[this.plugins.length];
		for (int p = 0; p < this.plugins.length; p++)
			plugins[p] = this.plugins[p];
		return plugins;
	}
	
	private GoldenGatePlugin[] loadPlugins() {
		
		//	load plugins
		Object[] pluginObjects = GamtaClassLoader.loadComponents(
				this.getDataNames(), 
				PLUGIN_FOLDER_NAME, 
				new InputStreamProvider() {
					public InputStream getInputStream(String dataName) throws IOException {
						return AbstractConfiguration.this.getInputStream(dataName);
					}
				}, 
				GoldenGatePlugin.class, 
				new ComponentInitializer() {
					public void initialize(Object component, String componentJarName) throws Throwable {
						String dataPath = (componentJarName.substring(0, (componentJarName.length() - 4)) + JAR_DATA_FOLDER_SUFFIX);
						((GoldenGatePlugin) component).setDataProvider(new PluginDataProviderPrefixBased(AbstractConfiguration.this.baseDataProvider, dataPath));
					}
				});
		
		//	store & return plugins
		GoldenGatePlugin[] plugins = new GoldenGatePlugin[pluginObjects.length];
		for (int c = 0; c < pluginObjects.length; c++)
			plugins[c] = ((GoldenGatePlugin) pluginObjects[c]);
		return plugins;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getSettings()
	 */
	public Settings getSettings() {
		InputStream sis = null;
		try {
			sis = this.getInputStream(CONFIG_FILE_NAME);
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
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#storeSettings(de.uka.ipd.idaho.easyIO.settings.Settings)
	 */
	public void storeSettings(Settings settings) throws IOException {
		OutputStream sos = null;
		try {
			sos = this.getOutputStream(CONFIG_FILE_NAME);
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
				iis = this.getInputStream(DATA_FOLDER_NAME + "/" + ICON_FILE_NAME);
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
}