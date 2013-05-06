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


import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;

/**
 * Abstract implementetion of a GoldenGATE plugin. This class provides
 * implementations of the basic infrastructure like the setDataProvider(),
 * setParent() and isOperational() methods. In addition, it offers dummy
 * implementations of the GUI integration methods, whose return values indicate
 * 'no integration in this point', usually null. Sub classes are welcome to
 * overwrite any of these methods as needed. The getHelp() and getHelpMenuItem()
 * methods, though, are operational and need not be overwritten if it is
 * convenient to provide help information in the way outlined in these method's
 * implementation's comments. It also provides empty dummy implementations for
 * the init() and exit() methods.
 * 
 * @author sautter
 */
public abstract class AbstractGoldenGatePlugin implements GoldenGatePlugin {
	
	/**
	 * A cloneable JMenuItem that can be constructed once and then cloned,
	 * retaining the same text, layout, listeners, etc. This class is for cases
	 * plaugins keep internal references to the items they return from the
	 * getToolsMenuFunctionItems() method. In particular, this class enables
	 * them to return clones instead of the actual menu items, so adding the
	 * menu item to the 'Tools' menu of a sub window of a multi-window GUI does
	 * not remove these very menu items from the main window.
	 * 
	 * @author sautter
	 */
	protected class ToolsMenuItem extends JMenuItem {
		private LinkedList clones = new LinkedList();
		
		/**
		 * Constructor
		 * @param text the menu item text
		 */
		public ToolsMenuItem(String text) {
			super(text);
		}

		/* (non-Javadoc)
		 * @see javax.swing.JMenuItem#setEnabled(boolean)
		 */
		public void setEnabled(boolean b) {
			super.setEnabled(b);
			if (this.clones == null) // have to check this, as this method might be called by super class constructor
				return;
			for (Iterator cit = this.clones.iterator(); cit.hasNext();)
				((JMenuItem) cit.next()).setEnabled(b);
		}
		
		/* (non-Javadoc)
		 * @see javax.swing.AbstractButton#setText(java.lang.String)
		 */
		public void setText(String text) {
			super.setText(text);
			if (this.clones == null) // have to check this, as this method might be called by super class constructor
				return;
			for (Iterator cit = this.clones.iterator(); cit.hasNext();)
				((JMenuItem) cit.next()).setText(text);
		}
		
		/**
		 * Retrieve a clone of the menu item, with the same text, layout,
		 * listeners, etc.
		 * @return the clone
		 */
		public JMenuItem getClone() {
			for (Iterator cit = this.clones.iterator(); cit.hasNext();) {
				JMenuItem clone = ((JMenuItem) cit.next());
				Component parent = clone.getParent();
				System.out.println("Checking tools menu item in " + AbstractGoldenGatePlugin.this.getClass().getName());
				while (parent != null) {
					System.out.println(" - parent is " + parent.getClass().getName());
					if (parent instanceof Window) {
						System.out.println(" - got window");
						if (!parent.isVisible()) {
							cit.remove();
							System.out.println(" - display clone from disposed window discarded");
						}
						break;
					}
					else if (parent instanceof JPopupMenu)
						parent = ((JPopupMenu) parent).getInvoker();
					else parent = parent.getParent();
				}
			}
			JMenuItem clone = new JMenuItem(this.getText());
			clone.setEnabled(this.isEnabled());
			ActionListener[] als = this.getActionListeners();
			for (int l = 0; l < als.length; l++)
				clone.addActionListener(als[l]);
			this.clones.addLast(clone);
			return clone;
		}
	}
	
	/** The plugin's data provider */
	protected GoldenGatePluginDataProvider dataProvider;
	
	/** The GoldenGATE instance this plugin instance belongs to */
	protected GoldenGATE parent;
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#setDataProvider(de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider)
	 */
	public void setDataProvider(GoldenGatePluginDataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#setParent(de.goldenGate.GoldenGATE)
	 */
	public void setParent(GoldenGATE parent) {
		this.parent = parent;
	}
	
	/**
	 * Initialize the GoldenGATE plugin (load data, establish references to
	 * other plugins, etc.) Note: This method is called after parent and
	 * dataProvider are set. Note 2: This default implementation does nothing by
	 * default, sub classes are welcome to overwrite it as needed.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#init()
	 */
	public void init() {}
	
	/**
	 * Check if this ResourceManager is fully initialized and ready for
	 * operation. Note: This default implementation checks if parent and
	 * dataProvider are set to valid values. Subclasses overwriting this method
	 * to check additional properties (e.g. references to other subclasses)
	 * should include a super call in theit implementation of this method, or at
	 * least check parent and dataProvider for themselves.
	 * @return true if and only if this GoldenGATE plugin is fully initialized
	 *         and ready for operation
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#isOperational()
	 */
	public boolean isOperational() {
		return ((this.dataProvider != null) && (this.parent != null));
	}
	
	/**
	 * Shut down this GoldenGATE plugin (store data, etc.) Note: This default
	 * implementation does nothing by default, sub classes are welcome to
	 * overwrite it as needed.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#exit()
	 */
	public void exit() {}
	
	/**
	 * Retrieve a name for the plugin. This method MUST NOT return null. Note:
	 * This default implementation returns the class name after the last dot.
	 * Sub classes are welcone to overwrite this method in order to provide a
	 * informative and/or more user friendly name.
	 * @return the name of this plugin
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getPluginName()
	 */
	public String getPluginName() {
		String className = this.getClass().getName();
		return className.substring(className.lastIndexOf('.') + 1);
	}
	
	/**
	 * Retrieve a menu item to directly open the help page for this plugin. The
	 * menu item returned by this implementation has an ActionListener added to
	 * it, which invokes help('help-title') on the parent GoldenGATE object,
	 * where 'help-title' is the String returned by the getTitle() method of the
	 * HelpChapter object returned by the getHelp() method, thus the String
	 * returned by hte getPluginName() method of the plugin.
	 * @return a JMenuItem to be part of the help menu
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getHelpMenuItem()
	 */
	public JMenuItem getHelpMenuItem() {
		JMenuItem mi = new JMenuItem("Help on " + this.getPluginName());
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				parent.help(getPluginName());
			}
		});
		return mi;
	}
	
	/**
	 * Retrieve a help chapter to explain this GoldenGATE plugin's functions in
	 * the main help. Note: This default implementation tries to load a HTML
	 * help page from the file 'help.html' provided by the plugin's data
	 * provider. Thus, the easiest way of providing a help page on a plugin
	 * extending this class is to deposit said file in the plugin's data path.
	 * Note 2: This implementation uses the plugin name returned by the
	 * getPluginName() method as the title for the help chapter.
	 * @return a help chapter to offer help on this plugin in the help menu
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getHelp()
	 */
	public HelpChapter getHelp() {
		//	check if help file exists
		if (this.dataProvider.isDataAvailable(HELP_FILE_NAME)) {
			
			//	if so, use dynamic help loaded from file
			return new HelpChapter(this.getPluginName(), "<HTML>The content of this chapter will be loaded from<BR><TT>" + HELP_FILE_NAME.toString() + "</TT><BR>on demand.</HTML>") {
				public Reader getTextReader() {
					try {
						return new InputStreamReader(dataProvider.getInputStream(HELP_FILE_NAME));
					}
					catch (IOException e) {
						return super.getTextReader();
					}
				}
			};
		}
		
		//	help file does not exist, use fallback error text
		else {
			String help = ("<html><body>" +
					"<h3><font face=\"Verdana\">" + this.getPluginName() + "</font></h3>" +
					"<font face=\"Verdana\" size=\"2\">Help on " + this.getPluginName() + " is not available, please contact your administrator.</font>" +
					"</body></html>");
			return new HelpChapter(this.getPluginName(), help);
		}
	}
	
	/**
	 * Retrieve a title for this plugin's entry in the main menu. A return value
	 * of null will result in the plugin having no entry in the main menu. Note:
	 * this default implementation returns null, sub classes are welcome to
	 * overwrite this method as needed in order to integrate themselves in the
	 * main menu.
	 * @return the name of the menu of this GoldenGATE plugin in the main menu
	 *         bar
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getMainMenuTitle()
	 */
	public String getMainMenuTitle() {
		return null;
	}
	
	/**
	 * Retrieve the menu items for using the plugin through the main menu. Note:
	 * this default implementation returns null, sub classes are welcome to
	 * overwrite this method as needed in order to integrate themselves in the
	 * main menu.
	 * @return an array of JMenuItems to add to the menu of this GoldenGATE
	 *         Plugin in the main menu bar
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getMainMenuItems()
	 */
	public JMenuItem[] getMainMenuItems() {
		return null;
	}
	
	/**
	 * Retrieve a SettingsPanel containing input fields for preferences to be
	 * included in the main window's preferences dialog. Note: this default
	 * implementation returns null, resulting in the plugin not being visible in
	 * the preferences dialog. Sub classes are welcome to overwrite this method
	 * as needed in order to integrate themselves in the preferences dialog.
	 * @return a SettingsPanel containing input fields for this GoldenGATE
	 *         plugin's preferences
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getSettingsPanel()
	 */
	public SettingsPanel getSettingsPanel() {
		return null;
	}
	
	/**
	 * Retrieve the menu items for accessing this GoldenGATE plugin's document
	 * editing functionality in the Tools menu of the main window. Note: this
	 * default implementation returns null, resulting in the plugin not being
	 * visible in the Tools menu. Sub classes are welcome to overwrite this
	 * method as needed in order to integrate themselves in the Tools menu.
	 * @param targetProvider the provider of the target DocumentEditor for the
	 *            functions returned
	 * @return the menu items for accessing this GoldenGATE plugin's document
	 *         editing functionality in the Tools menu of the main window
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getToolsMenuFunctionItems(de.uka.ipd.idaho.goldenGate.GoldenGateConstants.InvokationTargetProvider)
	 */
	public JMenuItem[] getToolsMenuFunctionItems(InvokationTargetProvider targetProvider) {
		return null;
	}

	/**
	 * Retrieve a plugin specific extension for the &quot;About&quot; box in the
	 * GoldenGATE main window. This is, for instance, to enable plugins that
	 * make use of third party libraries to properly credit the sources of the
	 * latter. The returned String may contain line breaks, but <b>no</b> HTML
	 * markup. If this method returns null, no entry is included in the
	 * &quot;About&quot; box for this plugin. This default implementation does
	 * return null, sub classes are welcome to overwrite it as needed.
	 * @return an extension for the &quot;About&quot; box
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getAboutBoxExtension()
	 */
	public String getAboutBoxExtension() {
		return null;
	}
}
