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


import javax.swing.JMenuItem;

import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;

public interface GoldenGatePlugin extends GoldenGateConstants {
	
	/** the default name for the help html page */
	public static final String HELP_FILE_NAME = "help.html";

	/**
	 * Make this GoldenGATE plugin know the GoldenGATE instance it belongs to
	 * @param parent the GoldenGATE instance this plugin belongs to
	 */
	public abstract void setParent(GoldenGATE parent);
	
	/**
	 * Make this GoldenGATE plugin know the path where its data is located
	 * @param dataProvider the data provider holding this plugin's data
	 */
	public void setDataProvider(GoldenGatePluginDataProvider dataProvider);
	
	/**
	 * Initialize the GoldenGATE plugin (load data, establish references to
	 * other plugins, etc.) Note: This method is called after parent and
	 * dataProvider are set.
	 */
	public void init();
	
	/**
	 * check if this GoldenGATE plugin is fully initialized and ready for
	 * operation
	 * @return true if and only if this ResourceManager is fully initialized and
	 *         ready for operation
	 */
	public boolean isOperational();
	
	/**
	 * Shut down this GoldenGATE plugin (store data, etc.)
	 */
	public abstract void exit();
	
	/**
	 * Retrieve a name for the plugin. This method MUST NOT return null.
	 * @return the name of this plugin
	 */
	public abstract String getPluginName();
	
	/**
	 * Retrieve a title for this plugin's entry in the main menu. A return value
	 * of null will result in the plugin having no entry in the main menu.
	 * @return the name of the menu of this GoldenGATE plugin in the main menu
	 *         bar, or null, if this GoldenGATE plugin does not want to be
	 *         accessible through the main menu
	 */
	public abstract String getMainMenuTitle();
	
	/**
	 * Retrieve the menu items for using the plugin through the main menu.
	 * @return an array of JMenuItems to add to the menu of this
	 *         GoldenGATE plugin in the main menu bar
	 */
	public abstract JMenuItem[] getMainMenuItems();
	
	/**
	 * Retrieve the menu items for accessing this GoldenGATE plugin's document
	 * editing functionality in the Tools menu of the main window
	 * @param targetProvider the provider of the target DocumentEditor for the
	 *            functions returned
	 * @return the menu items for accessing this GoldenGATE plugin's document
	 *         editing functionality in the Tools menu of the main window, or
	 *         null, if this GoldenGATE plugin does not want to be accessible
	 *         through the Tools menu, or does not provide any functionality
	 *         directly applicable for editing documents
	 */
	public abstract JMenuItem[] getToolsMenuFunctionItems(InvokationTargetProvider targetProvider);
	
	/**
	 * Retrieve a SettingsPanel containing input fields for preferences to be
	 * included in the main window's preferences dialog.
	 * @return a SettingsPanel containing input fields for this
	 *         GoldenGATE plugin's preferences
	 */
	public SettingsPanel getSettingsPanel();
	
	/**
	 * Retrieve a menu item to directly open the help page for this plugin. The
	 * easiest way of achieving this is adding an ActionListener to the menu
	 * item invoking help('help-title') on the parent GoldenGATE object, where
	 * 'help-title' is the String returned by the getTitle() method of the
	 * HelpChapter object returned by the getHelp() method.
	 * @return a JMenuItem to be part of the help menu
	 */
	public JMenuItem getHelpMenuItem();
	
	/**
	 * Retrieve a help chapter to explain this GoldenGATE plugin's functions in
	 * the main help
	 * @return a help chapter to offer help on this plugin in the help menu
	 */
	public abstract HelpChapter getHelp();
	
	/**
	 * Retrieve a plugin specific extension for the &quot;About&quot; box in the
	 * GoldenGATE main window. This is, for instance, to enable plugins that
	 * make use of third party libraries to properly credit the sources of the
	 * latter. The returned String may contain line breaks, but <b>no</b> HTML
	 * markup. If this method returns null, no entry is included in the
	 * &quot;About&quot; box for this plugin.
	 * @return an extension for the &quot;About&quot; box
	 */
	public abstract String getAboutBoxExtension();
}
