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
package de.uka.ipd.idaho.goldenGate;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import de.uka.ipd.idaho.easyIO.help.DynamicHelpChapter;
import de.uka.ipd.idaho.easyIO.help.Help;
import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.AnnotationListener;
import de.uka.ipd.idaho.gamta.CharSequenceListener;
import de.uka.ipd.idaho.gamta.DocumentRoot;
import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.gamta.MutableCharSequence.CharSequenceEvent;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.Tokenizer;
import de.uka.ipd.idaho.gamta.util.ImmutableAnnotation;
import de.uka.ipd.idaho.gamta.util.TestDocumentProvider;
import de.uka.ipd.idaho.goldenGate.observers.AnnotationObserver;
import de.uka.ipd.idaho.goldenGate.observers.ResourceObserver;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilter;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilterManager;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSource;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationSourceManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentEditorExtension;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentFormatProvider;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentLoader;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentLoader.DocumentData;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentSaveOperation;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentSaver;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.Resource;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;
import de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.ResourceDialog;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * the GoldenGATE editor main frame
 * 
 * @author sautter
 */
public class GoldenGATE implements GoldenGateConstants, TestDocumentProvider {

	/*
	 * notTODO separate GG core (plugin loading & registry, maybe menu assembly)
	 * from document editor and GUI specific functionality (opening documents,
	 * editing settings, etc.; these all go to GoldenGateEditor)
	 * 
	 * ==> DO NOT DO THIS: provides no advantage because GG has to remain a
	 * singleton anyways due to static fields plugins
	 */
	
	private static final SimpleDateFormat yearTimestamper = new SimpleDateFormat("yyyy");
	private static final String ABOUT_TEXT = 
		"GoldenGATE Document Editor " + VERSION_STRING + "\n" +
		"The easy way to mark up Documents\n" +
		"Version Date: " + VERSION_DATE + "\n" +
		"\n" +
		"© by Guido Sautter 2006-" + yearTimestamper.format(new Date()) + "\n" +
		"IPD Boehm\n" +
		"Karlsruhe Institute of Technology (KIT)";
	
	private CustomFunction.Manager customFunctionManager;
	private CustomShortcut.Manager customShortcutManager;
	
	private TokenizerManager tokenizerManager;
	private String tokenizerName;
	
	private Help help;
	private HelpChapter helpContent;
	
	private Image iconImage;
	
	private GoldenGateGUI gui;
	private LinkedHashSet openDocuments = new LinkedHashSet();
	
	/** Constructor
	 * @param	the GoldenGateConfiguration to use
	 */
	private GoldenGATE(GoldenGateConfiguration configuration, boolean allowChangeConfiguration, StartupStatusMonitor ssm) throws IOException {
		this.configuration = configuration;
		this.allowChangeConfiguration = allowChangeConfiguration;
		this.iconImage = this.configuration.getIconImage();
		System.out.println("GoldenGATE (" + VERSION_DATE + ") started, configuration is " + this.configuration.getName());
		
		//	initialize settings
		ssm.addStatusLine("Loading settings ...");
		Settings settings = this.configuration.getSettings();
		
		//	read tokenizer name
		this.tokenizerName = settings.getSetting(TOKENIZER_NAME_SETTING_NAME, TokenizerManager.INNER_PUNCTUATION_TOKENIZER_NAME);
		
		DocumentEditor.init(settings.getSubset(DOCUMENT_DISPLAY_PANEL_SETTINGS_PREFIX));
		AnnotationEditorPanel.init(settings.getSubset(ANNOTATION_EDITOR_PANEL_SETTINGS_PREFIX));
		DocumentFormat.init(settings.getSubset(DOCUMENT_FORMAT_SETTINGS_PREFIX));
		
		Gamta.addTestDocumentProvider(this);
		
		
		//	load plugins
		ssm.addStatusLine("Loading plugins ...");
		GoldenGatePlugin[] plugins = this.configuration.getPlugins();
		
		//	register plugins
		ssm.addStatusLine("Registering plugins ...");
		for (int p = 0; p < plugins.length; p++)
			this.registerPlugin(plugins[p]);
		
		//	set parent & initialize plugins
		ssm.addStatusLine("Initializing plugins ...");
		for (int p = 0; p < plugins.length; p++) {
			try {
				plugins[p].setParent(this);
				plugins[p].init();
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while initializing " + plugins[p].getClass().getName());
				t.printStackTrace(System.out);
				plugins[p] = null;
			}
		}
		
		//	collect operational plugins
		ssm.addStatusLine("Checking plugins ...");
		ArrayList operationalPlugins = new ArrayList();
		for (int p = 0; p < plugins.length; p++) {
			if (plugins[p] == null)
				continue;
			ssm.addStatusLine(" - " + plugins[p].getPluginName());
			if (plugins[p].isOperational())
				operationalPlugins.add(plugins[p]);
		}
		
		
		final ArrayList lazyLoadRunnables = new ArrayList();
		
		//	do not pre-load in master configuration, as this likely is unnecessarily costly
		if (!this.configuration.isMasterConfiguration()) {
			
			ssm.addStatusLine("Initializing Custom Functions ...");
			if ((this.customFunctionManager != null)) {
				
				// retrieve custom functions so opening first document is faster
				lazyLoadRunnables.add(new Runnable() {
					public void run() {
						CustomFunction[] customFunctions = getCustomFunctions();
						for (int c = 0; c < customFunctions.length; c++)
							customFunctions[c].getDocumentProcessor();
					}
				});
			}
			
			ssm.addStatusLine("Initializing Custom Shortcuts ...");
			if (this.customShortcutManager != null) {
				
				// retrieve custom shortcuts so opening first document is faster
				lazyLoadRunnables.add(new Runnable() {
					public void run() {
						String[] customShortcutNames = customShortcutManager.getResourceNames();
						for (int c = 0; c < customShortcutNames.length; c++) {
							CustomShortcut cs = customShortcutManager.getCustomShortcut(customShortcutNames[c]);
							if (cs != null)
								cs.getDocumentProcessor();
						}
					}
				});
			}
		}
		
		ssm.addStatusLine("Initializing Tokenizers ...");
//		
//		/*
//		 * run lazy loading processes (if available mememory is sufficient (128
//		 * MB or more) - if memory is scarce, we cannot afford pre-loading stuff
//		 * that might not even be used later on)
//		 */
//		if (Runtime.getRuntime().maxMemory() > (128 * 1024 * 1024)) {
//			new Thread() {
//				public void run() {
//					for (int r = 0; r < lazyLoadRunnables.size(); r++)
//						try {
//							((Runnable) lazyLoadRunnables.get(r)).run();
//						} catch (Throwable t) {
//							System.out.println(t.getClass().getName() + " (" + t.getMessage() + "):");
//							t.printStackTrace(System.out);
//						}
//					lazyLoadingDone = true;
//				}
//			}.start();
//		}
//		else this.lazyLoadingDone = true;
		//	memory limit is no good, for without pre-loading, opening first document takes forever
		new Thread() {
			public void run() {
				for (int r = 0; r < lazyLoadRunnables.size(); r++)
					try {
						((Runnable) lazyLoadRunnables.get(r)).run();
					} catch (Throwable t) {
						System.out.println(t.getClass().getName() + " (" + t.getMessage() + "):");
						t.printStackTrace(System.out);
					}
				lazyLoadingDone = true;
			}
		}.start();
		
		//	build helb only if not headless
		if (GraphicsEnvironment.isHeadless())
			ssm.addStatusLine("Cannot create help due to headless environment");
		
		else {
			
			//	create help menu & content
			ssm.addStatusLine("Creating help menu ...");
			this.helpContent = this.buildHelp(this.configuration.getHelpBaseURL());
			
			//	initialize help window 
			ssm.addStatusLine("Creating help window ...");
			this.help = new Help("GoldenGATE", this.helpContent, this.configuration.getHelpBaseURL());
			this.help.setIconImage(this.iconImage);
		}
		
		//	set icon
		ssm.addStatusLine("GoldenGATE Editor Initialized");
		this.status = RUNNING;
	}
	
	/**
	 * Retrieve a 'File' menu with the options available from the current
	 * configuration. This method is a convenience signature for its
	 * eight-argument sibling, designed for retrieving only the saving options
	 * in a one-document, non-main window, eg some sub dialog.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 */
	public JMenu getFileMenu(InvokationTargetProvider targetProvider) {
		JMenu fileMenu = new JMenu("File");
		this.buildFileMenu(fileMenu, targetProvider);
		return fileMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'File' menu with the options available from the
	 * current configuration. This method is a convenience signature for its
	 * nine-argument sibling, designed for retrieving only the saving options in
	 * a one-document, non-main window, eg some sub dialog.
	 * @param fileMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 */
	public void buildFileMenu(JMenu fileMenu, InvokationTargetProvider targetProvider) {
		this.buildFileMenu(fileMenu, targetProvider, false, true, false, false, false, false, null);
	}
	
	/**
	 * Have GoldenGATE fill a 'File' menu with the options available from the
	 * current configuration. This method is a convenience signature for its
	 * eight-argument sibling, designed for setting all flags to true or false
	 * with one argument.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param isMainWindow are the items intended for the main window? (will
	 *            include loading, closing and exitting items if set to true)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public JMenu getFileMenu(InvokationTargetProvider targetProvider, boolean isMainWindow, ArrayList documentDependentMenuItems) {
		JMenu fileMenu = new JMenu("File");
		this.buildFileMenu(fileMenu, targetProvider, isMainWindow, documentDependentMenuItems);
		return fileMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'File' menu with the options available from the
	 * current configuration. This method is a convenience signature for its
	 * nine-argument sibling, designed for setting all flags to true or false
	 * with one argument.
	 * @param fileMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param isMainWindow are the items intended for the main window? (will
	 *            include loading, closing and exitting items if set to true)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public void buildFileMenu(JMenu fileMenu, InvokationTargetProvider targetProvider, boolean isMainWindow, ArrayList documentDependentMenuItems) {
		this.buildFileMenu(fileMenu, targetProvider, isMainWindow, true, isMainWindow, isMainWindow, isMainWindow, isMainWindow, documentDependentMenuItems);
	}
	
	/**
	 * Retrieve a 'File' menu with the options available from the current
	 * configuration.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param isMainWindow are the items intended for the main window? (will
	 *            include loading, closing and exitting items if set to true)
	 * @param isMultiDocument is the main window capable of having multiple
	 *            documents open at the same time? (if set to true, will include
	 *            the menu items for closing all documents at once?)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public JMenu getFileMenu(InvokationTargetProvider targetProvider, boolean isMainWindow, boolean isMultiDocument, ArrayList documentDependentMenuItems) {
		JMenu fileMenu = new JMenu("File");
		this.buildFileMenu(fileMenu, targetProvider, isMainWindow, isMultiDocument, documentDependentMenuItems);
		return fileMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'File' menu with the options available from the
	 * current configuration.
	 * @param fileMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param isMainWindow are the items intended for the main window? (will
	 *            include loading, closing and exitting items if set to true)
	 * @param isMultiDocument is the main window capable of having multiple
	 *            documents open at the same time? (if set to true, will include
	 *            the menu items for closing all documents at once?)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public void buildFileMenu(JMenu fileMenu, InvokationTargetProvider targetProvider, boolean isMainWindow, boolean isMultiDocument, ArrayList documentDependentMenuItems) {
		this.buildFileMenu(fileMenu, targetProvider, isMainWindow, true, isMainWindow, isMultiDocument, isMainWindow, isMainWindow, documentDependentMenuItems);
	}
	
	/**
	 * Retrieve a 'File' menu with the options available from the current
	 * configuration.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param includeLoad include the menu items for loading documents?
	 * @param includeSave include the menu items for saving documents or parts
	 *            of them?
	 * @param includeClose include the menu items for closing documents?
	 * @param includeCloseAll include the menu items for closing all documents
	 *            at once? This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 * @param includeChangeConfiguration include the menu items for exitting
	 *            GoldenGATE and restarting it with a newly selected
	 *            configuration? (this is recommendet only in the editor main
	 *            window)
	 * @param includeExit include the menu items for exitting GoldenGATE? (this
	 *            is recommendet only in the editor main window)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public JMenu getFileMenu(InvokationTargetProvider targetProvider, boolean includeLoad, boolean includeSave, boolean includeClose, boolean includeCloseAll, boolean includeChangeConfiguration, boolean includeExit, ArrayList documentDependentMenuItems) {
		JMenu fileMenu = new JMenu("File");
		this.buildFileMenu(fileMenu, targetProvider, includeLoad, includeSave, includeClose, includeCloseAll, includeChangeConfiguration, includeExit, documentDependentMenuItems);
		return fileMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'File' menu with the options available from the
	 * current configuration.
	 * @param fileMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param includeLoad include the menu items for loading documents?
	 * @param includeSave include the menu items for saving documents or parts
	 *            of them?
	 * @param includeClose include the menu items for closing documents?
	 * @param includeCloseAll include the menu items for closing all documents
	 *            at once? This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 * @param includeChangeConfiguration include the menu items for exitting
	 *            GoldenGATE and restarting it with a newly selected
	 *            configuration? (this is recommendet only in the editor main
	 *            window)
	 * @param includeExit include the menu items for exitting GoldenGATE? (this
	 *            is recommendet only in the editor main window)
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 */
	public void buildFileMenu(JMenu fileMenu, final InvokationTargetProvider targetProvider, boolean includeLoad, boolean includeSave, boolean includeClose, boolean includeCloseAll, boolean includeChangeConfiguration, boolean includeExit, ArrayList documentDependentMenuItems) {
		JMenuItem mi;
		
		//	generic load option
		if (includeLoad) {
			mi = new JMenuItem("Load Document");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					loadDocument();
				}
			});
			fileMenu.add(mi);
		}
		
		//	generic save option
		if (includeSave) {
			mi = new JMenuItem("Save Document");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					DocumentEditor target = targetProvider.getFunctionTarget();
					if (target != null) target.saveContent();
				}
			});
			fileMenu.add(mi);
			if (documentDependentMenuItems != null)
				documentDependentMenuItems.add(mi);
		}
		
		if (includeLoad || includeSave)
			fileMenu.addSeparator();
		
		//	add plugin menu items for document IO
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) try {
			
			//	integrate in specific menus
			JMenuItem loadMenuItem = null;
			JMenuItem saveMenuItem = null;
			JMenuItem savePartsMenuItem = null;
			
			//	get load item
			if (plugins[p] instanceof DocumentLoader) {
				loadMenuItem = ((DocumentLoader) plugins[p]).getLoadDocumentMenuItem();
				if (loadMenuItem != null) {
					final DocumentLoader loader = ((DocumentLoader) plugins[p]);
					loadMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							try {
								DocumentData dd = loader.loadDocument();
								if (dd != null)
									openDocument(dd.docData, dd.name, dd.format, dd.saveOpertaion);
							}
							catch (Exception e) {
								JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), e.getMessage(), "Exception Loading Document", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace(System.out);
							}
						}
					});
				}
			}
			
			//	get save items
			if (plugins[p] instanceof DocumentSaver) {
				
				//	integrate in save section of file menu
				saveMenuItem = ((DocumentSaver) plugins[p]).getSaveDocumentMenuItem();
				if (saveMenuItem != null) {
					final DocumentSaver saver = ((DocumentSaver) plugins[p]);
					saveMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							DocumentEditor target = targetProvider.getFunctionTarget();
							if (target != null)
								target.saveContentAs(saver);
						}
					});
				}
				
				//	integrate in save parts section of file menu
				savePartsMenuItem = ((DocumentSaver) plugins[p]).getSaveDocumentPartsMenuItem();
				if (savePartsMenuItem != null) {
					final DocumentSaver saver = ((DocumentSaver) plugins[p]);
					savePartsMenuItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							DocumentEditor target = targetProvider.getFunctionTarget();
							if (target != null)
								target.saveContentParts(saver);
						}
					});
				}
			}
			
			boolean itemAdded = false;
			if (includeLoad && (loadMenuItem != null)) {
				fileMenu.add(loadMenuItem);
				itemAdded = true;
			}
			
			if (includeSave) {
				if (saveMenuItem != null) {
					fileMenu.add(saveMenuItem);
					if (documentDependentMenuItems != null)
						documentDependentMenuItems.add(saveMenuItem);
					itemAdded = true;
				}
				if (savePartsMenuItem != null) {
					fileMenu.add(savePartsMenuItem);
					if (documentDependentMenuItems != null)
						documentDependentMenuItems.add(savePartsMenuItem);
					itemAdded = true;
				}
			}
			if (itemAdded) fileMenu.addSeparator();
		}
		catch (Throwable t) {
			System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting file menu items from " + plugins[p].getClass().getName());
			t.printStackTrace(System.out);
		}
		
		//	close options
		if (includeClose) {
			mi = new JMenuItem("Close");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					close(targetProvider.getFunctionTarget());
				}
			});
			fileMenu.add(mi);
			if (documentDependentMenuItems != null)
				documentDependentMenuItems.add(mi);
		}
		if (includeCloseAll) {
			mi = new JMenuItem("Close All");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					closeAll();
				}
			});
			fileMenu.add(mi);
			if (documentDependentMenuItems != null)
				documentDependentMenuItems.add(mi);
		}
		if (includeClose || includeCloseAll)
			fileMenu.addSeparator();
		
		JMenuItem[] configItems = this.configuration.getFileMenuItems();
		if ((configItems != null) && (configItems.length != 0)) {
			for (int i = 0; i < configItems.length; i++)
				fileMenu.add(configItems[i]);
			
			if (includeClose || includeCloseAll)
				fileMenu.addSeparator();
		}
		
		//	exit options
		if (includeChangeConfiguration) {
			mi = new JMenuItem("Change Configuration");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					exit(EXIT_CHANGE_CONFIGURATION);
				}
			});
			fileMenu.add(mi);
		}
		if (includeExit) {
			mi = new JMenuItem("Exit");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					exit(EXIT_SHUTDOWN);
				}
			});
			fileMenu.add(mi);
		}
	}
	
	/**
	 * Retrieve a 'View' menu with the options available from the current
	 * configuration.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param includeEditOptions include the menu item for changing the editor's
	 *            default behavior?
	 */
	public JMenu getViewMenu(InvokationTargetProvider targetProvider, boolean includeEditOptions) {
		JMenu viewMenu = new JMenu("View");
		this.buildViewMenu(viewMenu, targetProvider, includeEditOptions);
		return viewMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'View' menu with the options available from the
	 * current configuration.
	 * @param viewMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param includeEditOptions include the menu item for changing the editor's
	 *            default behavior?
	 */
	public void buildViewMenu(JMenu viewMenu, final InvokationTargetProvider targetProvider, boolean includeEditOptions) {
		JMenuItem mi;
		
		//	create view menu
		mi = new JMenuItem("Edit Fonts");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.editFonts();
			}
		});
		viewMenu.add(mi);
		
		//	add access to editor behavior if window menu not present
		if (includeEditOptions && !this.configuration.isMasterConfiguration()) {
			mi = new JMenuItem("Editor Options");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					editOptions();
				}
			});
			viewMenu.add(mi);
		}
		
		mi = new JMenuItem("Refresh");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.refreshDisplay();
			}
		});
		viewMenu.add(mi);
		mi = new JMenuItem("Display Control");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.configureDisplay();
			}
		});
		viewMenu.add(mi);
		mi = new JMenuItem("Output Preview");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				DocumentEditor target = targetProvider.getFunctionTarget();
				if (target != null) target.preview();
			}
		});
		viewMenu.add(mi);
		
		DocumentViewer[] dvs = this.getDocumentViewers();
		if (dvs.length != 0) {
			viewMenu.addSeparator();
			for (int d = 0; d < dvs.length; d++) {
				String viewMenuName = dvs[d].getViewMenuName();
				if (viewMenuName != null) {
					final String className = dvs[d].getClass().getName();
					mi = new JMenuItem(viewMenuName);
					mi.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent ae) {
							DocumentEditor target = targetProvider.getFunctionTarget();
							if (target != null) target.showDocumentView(className);
						}
					});
					viewMenu.add(mi);
				}
			}
		}
	}
	/**
	 * Retrieve a 'Tools' menu with the options available from the current
	 * configuration.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 */
	public JMenu getToolsMenu(InvokationTargetProvider targetProvider) {
		JMenu toolsMenu = new JMenu("Tools");
		this.buildToolsMenu(toolsMenu, targetProvider);
		return toolsMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'Tools' menu with the options available from the
	 * current configuration.
	 * @param toolsMenu the JMenu to fill
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 */
	public void buildToolsMenu(JMenu toolsMenu, final InvokationTargetProvider targetProvider) {
		
		//	TODO put plugin specific function items at bottom of menu, under separator
		//	==> 'Apply' / 'Run' / etc menu item alwasy stays in main menu
		//	==> plugins can exist only in order to extend tools menu, without providing any resources
		
		ArrayList annotationSourceToolsMenuItems = new ArrayList();
		ArrayList documentProcessorToolsMenuItems = new ArrayList();
		ArrayList pluginToolsMenuItems = new ArrayList();
		
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) try {
			
			JMenuItem[] functionMenuItems = plugins[p].getToolsMenuFunctionItems(targetProvider);
			JMenuItem resourceMenuItem = null;
			
			if (plugins[p] instanceof ResourceManager) {
				String toolsMenuLabel = ((ResourceManager) plugins[p]).getToolsMenuLabel();
				if (toolsMenuLabel != null) {
					
					//	create menu item
					final String className = plugins[p].getClass().getName();
					resourceMenuItem = new JMenuItem(toolsMenuLabel + " " + ((ResourceManager) plugins[p]).getResourceTypeLabel());
					
					//	create sub class specific action listener
					if (plugins[p] instanceof AnnotationSourceManager)
						resourceMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								DocumentEditor target = targetProvider.getFunctionTarget();
								if (target != null) target.applyAnnotationSource(className, null);
							}
						});
					else if (plugins[p] instanceof DocumentProcessorManager)
						resourceMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent ae) {
								DocumentEditor target = targetProvider.getFunctionTarget();
								if (target != null) target.applyDocumentProcessor(className, null);
							}
						});
					else resourceMenuItem = null;
					
					//	got resource menu item, but not functions, add menu item directly
					if ((resourceMenuItem != null) && ((functionMenuItems == null) || (functionMenuItems.length == 0))) {
						if (plugins[p] instanceof AnnotationSourceManager) {
							annotationSourceToolsMenuItems.add(resourceMenuItem);
							resourceMenuItem = null;
						}
						else if (plugins[p] instanceof DocumentProcessorManager) {
							documentProcessorToolsMenuItems.add(resourceMenuItem);
							resourceMenuItem = null;
						}
					}
				}
			}
			
			//	got function menu items, create sub menu
			if ((functionMenuItems != null) && (functionMenuItems.length != 0)) {
				
				//	add menu items
				JMenu pluginMenu = new JMenu(plugins[p].getPluginName());
				boolean lastWasSeparator = true;
				int menuItemCount = 0;
				for (int f = 0; f < functionMenuItems.length; f++) {
					if (functionMenuItems[f] == GoldenGateConstants.MENU_SEPARATOR_ITEM) {
						if (menuItemCount != 0) pluginMenu.addSeparator();
						lastWasSeparator = true;
					}
					else {
						pluginMenu.add(functionMenuItems[f]);
						lastWasSeparator = false;
						menuItemCount ++;
					}
				}
				
				//	add resource menu if given
				if (resourceMenuItem != null) {
					if (!lastWasSeparator) pluginMenu.addSeparator();
					pluginMenu.add(resourceMenuItem);
				}
				
				//	sort menu into appropriate list
				if (plugins[p] instanceof AnnotationSourceManager)
					annotationSourceToolsMenuItems.add(pluginMenu);
				else if (plugins[p] instanceof DocumentProcessorManager)
					documentProcessorToolsMenuItems.add(pluginMenu);
				else pluginToolsMenuItems.add(pluginMenu);
			}
		}
		catch (Throwable t) {
			System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting tools menu items from " + plugins[p].getClass().getName());
			t.printStackTrace(System.out);
		}
		
		//	sort menu entries
		Collections.sort(annotationSourceToolsMenuItems, new Comparator() {
			public int compare(Object o1, Object o2) {
				JMenuItem mi1 = ((JMenuItem) o1);
				JMenuItem mi2 = ((JMenuItem) o2);
				return mi1.getText().compareTo(mi2.getText());
			}
		});
		Collections.sort(documentProcessorToolsMenuItems, new Comparator() {
			public int compare(Object o1, Object o2) {
				JMenuItem mi1 = ((JMenuItem) o1);
				JMenuItem mi2 = ((JMenuItem) o2);
				return mi1.getText().compareTo(mi2.getText());
			}
		});
		Collections.sort(pluginToolsMenuItems, new Comparator() {
			public int compare(Object o1, Object o2) {
				JMenuItem mi1 = ((JMenuItem) o1);
				JMenuItem mi2 = ((JMenuItem) o2);
				return mi1.getText().compareTo(mi2.getText());
			}
		});
		
		//	add items to menu
		for (int i = 0; i < annotationSourceToolsMenuItems.size(); i++)
			toolsMenu.add((JMenuItem) annotationSourceToolsMenuItems.get(i));
		
		//	add separator between annotation source providers and document processor providers if both given
		if (!annotationSourceToolsMenuItems.isEmpty() && !documentProcessorToolsMenuItems.isEmpty())
			toolsMenu.addSeparator();
		for (int i = 0; i < documentProcessorToolsMenuItems.size(); i++)
			toolsMenu.add((JMenuItem) documentProcessorToolsMenuItems.get(i));
		
		//	add separator between annotation source / document processor providers and items from other plugins if both given
		if ((!annotationSourceToolsMenuItems.isEmpty() || !documentProcessorToolsMenuItems.isEmpty()) && !pluginToolsMenuItems.isEmpty())
			toolsMenu.addSeparator();
		for (int i = 0; i < pluginToolsMenuItems.size(); i++)
			toolsMenu.add((JMenuItem) pluginToolsMenuItems.get(i));
	}
	
	/**
	 * Retrieve a 'Help' menu with the options available from the current
	 * configuration.
	 */
	public JMenu getHelpMenu() {
		JMenu helpMenu = new JMenu("Help");
		this.buildHelpMenu(helpMenu);
		return helpMenu;
	}
	
	/**
	 * Have GoldenGATE fill a 'Help' menu with the options available from the
	 * current configuration.
	 * @param helpMenu the JMenu to fill
	 */
	public void buildHelpMenu(JMenu helpMenu) {
		JMenuItem mi;
		
		//	create help menu and further content
		mi = new JMenuItem("Help on GoldenGATE");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help(null);
			}
		});
		helpMenu.add(mi);
		mi = new JMenuItem("Help on File Menu");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help("File");
			}
		});
		helpMenu.add(mi);
		mi = new JMenuItem("Help on View Menu");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help("View");
			}
		});
		helpMenu.add(mi);
		mi = new JMenuItem("Help on Edit Menu");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help("Edit");
			}
		});
		helpMenu.add(mi);
		mi = new JMenuItem("Help on Tools Menu");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help("Tools");
			}
		});
		helpMenu.add(mi);
		
		GoldenGatePlugin[] plugins = this.getPlugins();
		if (plugins.length != 0) {
			helpMenu.addSeparator();
			ArrayList pluginHelpMi = new ArrayList();
			for (int p = 0; p < plugins.length; p++) {
				try {
					mi = plugins[p].getHelpMenuItem();
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting help menu item from " + plugins[p].getClass().getName());
					t.printStackTrace(System.out);
					mi = null;
				}
				if (mi != null) pluginHelpMi.add(mi);
			}
			Collections.sort(pluginHelpMi, new Comparator() {
				public int compare(Object o1, Object o2) {
					JMenuItem mi1 = ((JMenuItem) o1);
					JMenuItem mi2 = ((JMenuItem) o2);
					return mi1.getText().compareTo(mi2.getText());
				}
			});
			for (int p = 0; p < pluginHelpMi.size(); p++)
				helpMenu.add((JMenuItem) pluginHelpMi.get(p));
		}
		
		helpMenu.addSeparator();
		mi = new JMenuItem("Help");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				help("");
			}
		});
		helpMenu.add(mi);
		mi = new JMenuItem("About");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				about();
			}
		});
		helpMenu.add(mi);
		
		if (this.configuration.isDataAvailable(README_FILE_NAME)) {
			mi = new JMenuItem("View Readme");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					showReadme();
				}
			});
			helpMenu.add(mi);
		}
	}
	
	private HelpChapter buildHelp(String dataBaseUrl) {
		if (!dataBaseUrl.endsWith("/")) dataBaseUrl += "/";
		
		HelpChapter helpRoot = new DynamicHelpChapter("GoldenGATE", (dataBaseUrl + "GoldenGATE.html"));
		helpRoot.addSubChapter(new DynamicHelpChapter("Glossary",(dataBaseUrl + "Glossary.html")));
		
		HelpChapter menuHelp = new DynamicHelpChapter("Main Menus", (dataBaseUrl + "MainMenus.html"));
		menuHelp.addSubChapter(new DynamicHelpChapter("File", (dataBaseUrl + "FileMenu.html")));
		menuHelp.addSubChapter(new DynamicHelpChapter("View", (dataBaseUrl + "ViewMenu.html")));
		menuHelp.addSubChapter(new DynamicHelpChapter("Edit", (dataBaseUrl + "EditMenu.html")));
		menuHelp.addSubChapter(new DynamicHelpChapter("Tools", (dataBaseUrl + "ToolsMenu.html")));
		menuHelp.addSubChapter(new DynamicHelpChapter("Window", (dataBaseUrl + "WindowMenu.html")));
		menuHelp.addSubChapter(new DynamicHelpChapter("Help", (dataBaseUrl + "HelpMenu.html")));
		helpRoot.addSubChapter(menuHelp);
		
		helpRoot.addSubChapter(DocumentEditor.getHelp(dataBaseUrl));
		
		HelpChapter helpDocumentIo = new DynamicHelpChapter("Document IO", (dataBaseUrl + "DocumentIO.html"));
		helpRoot.addSubChapter(helpDocumentIo);
		
		HelpChapter helpDocumentFormats = new DynamicHelpChapter("Document Formats", (dataBaseUrl + "DocumentFormats.html"));
		helpRoot.addSubChapter(helpDocumentFormats);
		
		HelpChapter helpDocumentViewers = new DynamicHelpChapter("Document Viewers", (dataBaseUrl + "DocumentViewers.html"));
		helpRoot.addSubChapter(helpDocumentViewers);
		
		HelpChapter helpEditorExtensions = new DynamicHelpChapter("Editor Extensions", (dataBaseUrl + "EditorExtensions.html"));
		helpRoot.addSubChapter(helpEditorExtensions);
		
		HelpChapter helpResourceManagers = null;
		HelpChapter helpPlugins = null;
		HelpChapter helpCustomFunctions = null;
		HelpChapter helpCustomShortcuts = null;
		if (this.configuration.isMasterConfiguration()) {
			helpResourceManagers = new DynamicHelpChapter("Plug-In Resources", (dataBaseUrl + "PlugInResources.html"));
			helpRoot.addSubChapter(helpResourceManagers);
			helpPlugins = new DynamicHelpChapter("Other Plug-Ins", (dataBaseUrl + "PlugIns.html"));
			helpRoot.addSubChapter(helpPlugins);
		}
		else {
			helpCustomFunctions = new DynamicHelpChapter("Markup Funktions", (dataBaseUrl + "PlugInResources.html"));
			helpRoot.addSubChapter(helpResourceManagers);
			helpCustomShortcuts = new DynamicHelpChapter("Editor Shortcuts", (dataBaseUrl + "PlugIns.html"));
			helpRoot.addSubChapter(helpPlugins);
		}
		
		
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) {
			HelpChapter chapter = null;
			try {
				chapter = plugins[p].getHelp();
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting help from " + plugins[p].getClass().getName());
				t.printStackTrace(System.out);
			}
			if (chapter != null) {
				boolean integrated = false;
				
				if ((plugins[p] instanceof DocumentLoader) || (plugins[p] instanceof DocumentSaver)) {
					helpDocumentIo.addSubChapter(chapter);
					integrated = true;
				}
				
				if (plugins[p] instanceof DocumentFormatProvider) {
					helpDocumentFormats.addSubChapter(chapter);
					integrated = true;
				}
				else if (plugins[p] instanceof ResourceManager) {
					if (helpResourceManagers != null)
						helpResourceManagers.addSubChapter(chapter);
					integrated = true;
				}
				
				if (plugins[p] instanceof DocumentViewer) {
					helpDocumentViewers.addSubChapter(chapter);
					integrated = true;
				}
				
				if (plugins[p] instanceof DocumentEditorExtension) {
					helpEditorExtensions.addSubChapter(chapter);
					integrated = true;
				}
				
				if (plugins[p] instanceof CustomFunction.Manager)
					integrated = true;
				
				if (plugins[p] instanceof CustomShortcut.Manager)
					integrated = true;
				
				if (plugins[p] instanceof TokenizerManager)
					integrated = true;
				
				if (!integrated) {
					if (helpPlugins != null)
						helpPlugins.addSubChapter(chapter);
				}
			}
		}
		
		if (this.customFunctionManager != null) {
			if (this.configuration.isMasterConfiguration()) {
				HelpChapter chapter = this.customFunctionManager.getHelp();
				if (chapter != null)
					helpRoot.addSubChapter(chapter);
			}
			else {
				String[] customFunctionNames = this.customFunctionManager.getResourceNames();
				Arrays.sort(customFunctionNames);
				for (int n = 0; n < customFunctionNames.length; n++) {
					CustomFunction customFunction = this.customFunctionManager.getCustomFunction(customFunctionNames[n]);
					if (customFunction != null) {
						String helpText = customFunction.getHelpText();
						if (helpText != null)
							helpCustomFunctions.addSubChapter(new HelpChapter(customFunction.label, helpText));
					}
				}
			}
		}
		
		if (this.customShortcutManager != null) {
			if (this.configuration.isMasterConfiguration()) {
				HelpChapter chapter = this.customShortcutManager.getHelp();
				if (chapter != null)
					helpRoot.addSubChapter(chapter);
			}
			else {
				String[] customShortcutNames = this.customShortcutManager.getResourceNames();
				Arrays.sort(customShortcutNames);
				for (int n = 0; n < customShortcutNames.length; n++) {
					CustomShortcut customShortcut = this.customShortcutManager.getCustomShortcut(customShortcutNames[n]);
					if (customShortcut != null) {
						String helpText = customShortcut.getHelpText();
						if (helpText != null)
							helpCustomShortcuts.addSubChapter(new HelpChapter(customShortcutNames[n], helpText));
					}
				}
			}
		}
		
		if ((this.tokenizerManager != null) && this.configuration.isMasterConfiguration()) {
			HelpChapter chapter = this.tokenizerManager.getHelp();
			if (chapter != null)
				helpRoot.addSubChapter(chapter);
		}
		
		return helpRoot;
	}
	
	private SettingsPanel[] getSettingsPanels() {
		ArrayList settingsPanelList = new ArrayList();
		
		settingsPanelList.add(DocumentEditor.getSettingsPanel(this));
		settingsPanelList.add(AnnotationEditorPanel.getAnnotationSuggestionSettingsPanel(this));
		settingsPanelList.add(AnnotationEditorPanel.getAnnotationColorSettingsPanel(this));
		
		ArrayList pluginSettingsPanelList = new ArrayList();
		ArrayList documentFormatSettingsPanelList = new ArrayList();
		
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) try {
			SettingsPanel settingsPanel = plugins[p].getSettingsPanel();
			if (settingsPanel != null) {
				
				if (plugins[p] instanceof DocumentFormatProvider)
					documentFormatSettingsPanelList.add(settingsPanel);
				else pluginSettingsPanelList.add(settingsPanel);
			}
		}
		catch (Throwable t) {
			System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting settings panel from " + plugins[p].getClass().getName());
			t.printStackTrace(System.out);
		}
		
		final SettingsPanel[] documentFormatSettingsPanels = ((SettingsPanel[]) documentFormatSettingsPanelList.toArray(new SettingsPanel[documentFormatSettingsPanelList.size()]));
		final SettingsPanel documentFormatGlobalSettingsPanel = DocumentFormat.getSettingsPanel(this);
		SettingsPanel documentFormatSettingsPanel = new SettingsPanel(documentFormatGlobalSettingsPanel.getTitle(), documentFormatGlobalSettingsPanel.getToolTip()) {
			public void commitChanges() {
				documentFormatGlobalSettingsPanel.commitChanges();
				for (int s = 0; s < documentFormatSettingsPanels.length; s++)
					documentFormatSettingsPanels[s].commitChanges();
			}
		};
		JTabbedPane documentFormatTabs = new JTabbedPane();
		for (int s = 0; s < documentFormatSettingsPanels.length; s++)
			documentFormatTabs.addTab(documentFormatSettingsPanels[s].getTitle(), null, documentFormatSettingsPanels[s], documentFormatSettingsPanels[s].getToolTip());
		documentFormatSettingsPanel.setLayout(new BorderLayout());
		documentFormatSettingsPanel.add(documentFormatGlobalSettingsPanel, BorderLayout.NORTH);
		documentFormatSettingsPanel.add(documentFormatTabs, BorderLayout.CENTER);
		
		settingsPanelList.add(documentFormatSettingsPanel);
		
		settingsPanelList.addAll(pluginSettingsPanelList);
		
		return ((SettingsPanel[]) settingsPanelList.toArray(new SettingsPanel[settingsPanelList.size()]));
	}
	
	/**
	 * Retrieve the menus for the plugins currently installed in GoldenGATE.
	 * @param targetProvider the provider to retrieve the target DocumentEditor
	 *            from when invoking actions that are directed at a specific
	 *            document
	 * @param documentDependentMenuItems a list for adding menu items to that
	 *            depend on at least one document being open, eg for activating
	 *            or deactivating them depending on whether a document is open
	 *            or not. This is recommendet in the editor main window only,
	 *            and sensible only if the window can hold multiple open
	 *            documents at once.
	 * @return an array holding the menus for the plugins currently installed in GoldenGATE
	 */
	public JMenu[] getPluginMenus(final InvokationTargetProvider targetProvider, ArrayList documentDependentMenuItems) {
		ArrayList menuList = new ArrayList();
		
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) try {
			
			String mainMenuTitle = plugins[p].getMainMenuTitle();
			JMenuItem[] mainMenuItems = plugins[p].getMainMenuItems();
			if ((mainMenuTitle != null) && (mainMenuItems != null)) {
				JMenu menu = new JMenu(mainMenuTitle);
				
				boolean lastWasSeparator = true;
				int menuItemCount = 0;
				for (int m = 0; m < mainMenuItems.length; m++) {
					if (mainMenuItems[m] == GoldenGateConstants.MENU_SEPARATOR_ITEM) {
						if (menuItemCount != 0) menu.addSeparator();
						lastWasSeparator = true;
					}
					else {
						menu.add(mainMenuItems[m]);
						lastWasSeparator = false;
						menuItemCount ++;
					}
				}
				
				//	add tools menu entry for resource managers
				if (plugins[p] instanceof ResourceManager) {
					String toolsMenuLabel = ((ResourceManager) plugins[p]).getToolsMenuLabel();
					if (toolsMenuLabel != null) {
						
						//	create menu item
						final String className = plugins[p].getClass().getName();
						JMenuItem resourceMenuItem = new JMenuItem(toolsMenuLabel + " " + ((ResourceManager) plugins[p]).getResourceTypeLabel());
						
						//	create sub class specific action listener
						if (plugins[p] instanceof AnnotationSourceManager)
							resourceMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									DocumentEditor target = targetProvider.getFunctionTarget();
									if (target != null) target.applyAnnotationSource(className, null);
								}
							});
						else if (plugins[p] instanceof DocumentProcessorManager)
							resourceMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent ae) {
									DocumentEditor target = targetProvider.getFunctionTarget();
									if (target != null) target.applyDocumentProcessor(className, null);
								}
							});
						else resourceMenuItem = null;
						
						//	got resource menu item, add it to menu
						if (resourceMenuItem != null) {
							if (!lastWasSeparator) menu.addSeparator();
							menu.add(resourceMenuItem);
							if (documentDependentMenuItems != null)
								documentDependentMenuItems.add(resourceMenuItem);
						}
					}
				}
				
				//	store menu if not empty
				if (menu.getMenuComponentCount() != 0)
					menuList.add(menu);
			}
		}
		catch (Throwable t) {
			System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while getting main menu items from " + plugins[p].getClass().getName());
			t.printStackTrace(System.out);
		}
		
		return ((JMenu[]) menuList.toArray(new JMenu[menuList.size()]));
	}
	
	/**
	 * Retrieve the 'Window' menu for GoldenGATE. If the current configuration
	 * is not a master configuration, this method returns null.
	 * @return the 'Window' menu for GoldenGATE
	 */
	public JMenu getWindowMenu() {
		if (this.configuration.isMasterConfiguration()) {
			JMenu windowMenu = new JMenu("Window");
			this.buildWindowMenu(windowMenu);
			return windowMenu;
		}
		else return null;
	}
	
	private void buildWindowMenu(JMenu windowMenu) {
		JMenuItem mi;
		
		mi = new JMenuItem("Preferences");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				editSettings();
			}
		});
		windowMenu.add(mi);
		
		JMenuItem[] configItems = this.configuration.getWindowMenuItems();
		if ((configItems != null) && (configItems.length != 0)) {
			windowMenu.addSeparator();
			for (int i = 0; i < configItems.length; i++)
				windowMenu.add(configItems[i]);
		}
	}
	
//	private class DocIoMenuItemTray {
//		private JMenuItem loadMenuItem;
//		private JMenuItem saveMenuItem;
//		private JMenuItem savePartsMenuItem;
//		private DocIoMenuItemTray() {}
//	}
//	
	private void loadDocument() {
		DocumentLoader[] loaders = this.getDocumentLoaders();
		String[] loaderNames = new String[loaders.length];
		StringVector choosableLoaderNames = new StringVector();
		for (int l = 0; l < loaders.length; l++) {
			JMenuItem mi = loaders[l].getLoadDocumentMenuItem();
			if (mi == null) loaderNames[l] = "";
			else {
				loaderNames[l] = mi.getText();
				choosableLoaderNames.addElement(loaderNames[l]);
			}
		}
		Object o = JOptionPane.showInputDialog(DialogPanel.getTopWindow(), "Please select where to load the document from.", "Select Document Source", JOptionPane.QUESTION_MESSAGE, null, choosableLoaderNames.toStringArray(), null);
		for (int l = 0; l < loaders.length; l++)
			if (loaderNames[l].equals(o)) {
				try {
					DocumentData dd = loaders[l].loadDocument();
					if (dd != null) {
						openDocument(dd.docData, dd.name, dd.format, dd.saveOpertaion);
						return;
					}
				}
				catch (Exception e) {
					JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), e.getMessage(), "Exception Loading Document", JOptionPane.ERROR_MESSAGE);
				}
			}
	}
	
	private static class Logger implements CharSequenceListener, AnnotationListener {
		private String docName = null;
		private File logFile;
		private PrintWriter logWriter;
		private Logger(String docName) throws Exception { // IOException is not enough, attempt to create file might throw a SecurityException
			this.init(docName);
		}
		private void init(String docName) throws Exception { // IOException is not enough, attempt to create file might throw a SecurityException
			this.docName = docName;
			this.logFile = new File("./DocLog/" + this.docName + ".docLog");
			this.logFile.getParentFile().mkdirs();
			if (this.logFile.exists()) this.logFile.delete();
			this.logFile.createNewFile();
			this.logWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(logFile, true), "UTF-8"), true);
		}
		private void logChange(String change) {
			this.logWriter.println(change);
		}
		private void close() {
			this.logWriter.close();
			this.logFile.delete();
		}
		
		private static final String CHARS_SET = "CHS ";
		
		private static final String ANNOTATION_ADDED = "ANA ";
		private static final String ANNOTATION_REMOVED = "ANR ";
		private static final String ANNOTATION_TYPE_CHANGED = "ATC ";
		
		private static final String ANNOTATION_ATTRIBUTES_CLEARED = "AAC ";
		private static final String ANNOTATION_ATTRIBUTE_REMOVED = "AAR ";
		private static final String ANNOTATION_ATTRIBUTE_SET = "AAS ";
		
		private static final String DOCUMENT_SAVED = "SVD ";
		
		public void charSequenceChanged(CharSequenceEvent cse) {
			this.logChange(CHARS_SET + cse.offset + " " + cse.removed.length() + " " + cse.inserted);
		}
		
		public void annotationAdded(QueriableAnnotation doc, Annotation annotation) {
			this.logChange(ANNOTATION_ADDED + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size());
		}
		public void annotationAttributeChanged(QueriableAnnotation doc, Annotation annotation, String attributeName, Object oldValue) {
			//	bulk attribute copy, or attributes cleared
			if (attributeName == null) {
				String[] attributeNames = annotation.getAttributeNames();
				if (attributeNames.length == 0) 
					this.logChange(ANNOTATION_ATTRIBUTES_CLEARED + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size());
				else for (int a = 0; a < attributeNames.length; a++) {
						Object attributeValue = annotation.getAttribute(attributeNames[a]);
						if ((attributeValue != null) && (attributeValue instanceof CharSequence))
							this.logChange(ANNOTATION_ATTRIBUTE_SET + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size() + " " + attributeNames[a] + " " + attributeValue.toString());
					}
			}
			//	single attribute changed
			else {
				Object attributeValue = annotation.getAttribute(attributeName);
				if ((attributeValue != null) && (attributeValue instanceof CharSequence))
					this.logChange(ANNOTATION_ATTRIBUTE_SET + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size() + " " + attributeName + " " + attributeValue.toString());
				else this.logChange(ANNOTATION_ATTRIBUTE_REMOVED + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size() + " " + attributeName);
			}
		}
		public void annotationRemoved(QueriableAnnotation doc, Annotation annotation) {
			this.logChange(ANNOTATION_REMOVED + annotation.getType() + " " + annotation.getStartIndex() + " " + annotation.size());
		}
		public void annotationTypeChanged(QueriableAnnotation doc, Annotation annotation, String oldType) {
			this.logChange(ANNOTATION_TYPE_CHANGED + oldType + " " + annotation.getStartIndex() + " " + annotation.size() + " " + annotation.getType());
		}
		
		public void documentSaved(String newName) {
			//	document saved with unrepeatable save operation, just add log entry
			if (newName == null)
				this.logChange(DOCUMENT_SAVED);
			
			//	document saved with repeatable save operation
			else  {
				
				//	document saved with new name, add link to current log
				if (!newName.equals(this.docName))
					this.logChange(DOCUMENT_SAVED + newName);
				
				//	otherwise (document saved without name change), init() will discard current log file
				
				//	close writer in order to release current file
				this.logWriter.close();
				
				//	initialize logging to new file
				try {
					this.init(newName);
				}
				catch (Exception e) {
					System.out.println(e.getClass().getName() + " (" + e.getMessage() + ") while switching change logger to new file.");
					e.printStackTrace(System.out);
				}
			}
		}
		
		private static String[] readChangeLog(String logName) {
			try {
				File logFile = new File("./DocLog/" + logName + ".docLog");
				if (logFile.exists()) {
					BufferedReader logReader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "UTF-8"));
					StringVector logLines = new StringVector();
					String logLine = null;
					String line;
					while ((line = logReader.readLine()) != null) {
						//	start of new log entry
						if (line.startsWith(CHARS_SET)
								||
								line.startsWith(ANNOTATION_ADDED) || line.startsWith(ANNOTATION_REMOVED) || line.startsWith(ANNOTATION_TYPE_CHANGED)
								||
								line.startsWith(ANNOTATION_ATTRIBUTES_CLEARED) || line.startsWith(ANNOTATION_ATTRIBUTE_SET) || line.startsWith(ANNOTATION_ATTRIBUTE_REMOVED)
								||
								line.startsWith(DOCUMENT_SAVED)
							) {
							if (logLine != null)
								logLines.addElement(logLine);
							logLine = line;
						}
						
						//	log entry continues after line break
						else if (logLine != null)
							logLine = (logLine + "\n" + line);
					}
					if (logLine != null)
						logLines.addElement(logLine);
					logReader.close();
					logFile.delete();
					return logLines.toStringArray();
				}
				else return null;
			}
			catch (Exception e) {
				System.out.println(e.getClass().getName() + " (" + e.getMessage() + ") while reading change log for '" + logName + "'");
				e.printStackTrace(System.out);
				return null;
			}
		}
		
		private static boolean repeatChanges(String[] changesToRepeat, DocumentRoot content) {
			if (changesToRepeat == null) return false;
			
			System.out.println("Got " + changesToRepeat.length + " unsaved changes to repeat");
			boolean changed = false;
			for (int c = 0; (changesToRepeat != null) && (c < changesToRepeat.length); c++) {
				System.out.println("  - repeating " + changesToRepeat[c]);
				String changeType = changesToRepeat[c].substring(0, 4);
				String parameters = changesToRepeat[c].substring(4);
				if (changeType.startsWith(CHARS_SET)) {
					int offset = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int length = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					String chars = parameters.substring(parameters.indexOf(' ') + 1);
					content.setChars(chars, offset, length);
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_ADDED)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters);
					content.addAnnotation(type, startIndex, size);
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_REMOVED)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters);
					Annotation[] annots = content.getAnnotations(type);
					for (int a = 0; a < annots.length; a++)
						if ((annots[a].getStartIndex() == startIndex) && (annots[a].size() == size)) {
							content.removeAnnotation(annots[a]);
							a = annots.length;
						}
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_TYPE_CHANGED)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					String newType = parameters.substring(parameters.indexOf(' ') + 1);
					Annotation[] annots = content.getAnnotations(type);
					for (int a = 0; a < annots.length; a++)
						if ((annots[a].getStartIndex() == startIndex) && (annots[a].size() == size)) {
							annots[a].changeTypeTo(newType);
							a = annots.length;
						}
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_ATTRIBUTES_CLEARED)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters);
					Annotation[] annots = content.getAnnotations(type);
					for (int a = 0; a < annots.length; a++)
						if ((annots[a].getStartIndex() == startIndex) && (annots[a].size() == size)) {
							annots[a].clearAttributes();
							a = annots.length;
						}
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_ATTRIBUTE_SET)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					String attributeName = parameters.substring(0, parameters.indexOf(' '));
					String attributeValue = parameters.substring(parameters.indexOf(' ') + 1);
					Annotation[] annots = content.getAnnotations(type);
					for (int a = 0; a < annots.length; a++)
						if ((annots[a].getStartIndex() == startIndex) && (annots[a].size() == size)) {
							annots[a].setAttribute(attributeName, attributeValue);
							a = annots.length;
						}
					changed = true;
				}
				else if (changeType.startsWith(ANNOTATION_ATTRIBUTE_REMOVED)) {
					String type = parameters.substring(0, parameters.indexOf(' '));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int startIndex = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					parameters = parameters.substring(parameters.indexOf(' ') + 1);
					int size = Integer.parseInt(parameters.substring(0, parameters.indexOf(' ')));
					String attributeName = parameters.substring(parameters.indexOf(' ') + 1);
					Annotation[] annots = content.getAnnotations(type);
					for (int a = 0; a < annots.length; a++)
						if ((annots[a].getStartIndex() == startIndex) && (annots[a].size() == size)) {
							annots[a].removeAttribute(attributeName);
							a = annots.length;
						}
					changed = true;
				}
				else if (changeType.startsWith(DOCUMENT_SAVED)) {
					String newName = parameters;
					if (newName.length() != 0) {
						System.out.println("  - switching change log to " + newName);
						changesToRepeat = readChangeLog(newName);
						System.out.println("  ==> got " + ((changesToRepeat == null) ? -1 : changesToRepeat.length) + " more changes to repeat");
						c = -1;
					}
				}
			}
			return changed;
		}
	}
	
	/**
	 * Open a document in the GoldenGATE editor.
	 * @param doc the document to open
	 * @param docName the name of the document to open (will be displayed in
	 *            tab)
	 * @param format the format of the document
	 */
	public void openDocument(MutableAnnotation doc, String docName, DocumentFormat format) {
		this.openDocument(doc, docName, format, null);
	}
	
	/**
	 * Open a document in the GoldenGATE editor
	 * @param doc the document to open
	 * @param docName the name of the document to open (will be displayed in
	 *            tab)
	 * @param format the format of the document
	 * @param saveOperation the save operation to save the document through
	 *            without using the "Save As" options
	 */
	public void openDocument(MutableAnnotation doc, String docName, DocumentFormat format, DocumentSaveOperation saveOperation) {
		if (doc != null) {
//			DocumentEditor dep = new DocumentEditor(this, doc, null, docName, format, saveOperation);
			/*
			 * notTODO: wrap document in User Permission Enforcing Envelope once
			 * user permissions become real
			 * 
			 * ==> MAKES NO SENSE with current developments of the project
			 */
			
			//	experimental: read in logged changes
			String[] changesToRepeat = ((doc instanceof DocumentRoot) ? Logger.readChangeLog((docName == null) ? doc.getAnnotationID() : docName) : null);
			
			//	experimental: log changes on document root
			Logger logger = null;
			try {
				logger = new Logger((docName == null) ? doc.getAnnotationID() : docName);
				doc.addCharSequenceListener(logger);
				doc.addAnnotationListener(logger);
			}
			catch (Exception e) { // IOException is not enough, attempt to create file might throw a SecurityException
				System.out.println(e.getClass().getName() + " (" + e.getMessage() + ") while creating change logger.");
				e.printStackTrace(System.out);
			}
			
			//	experimental: repeat logged changes
			if (
					(changesToRepeat != null)
					&&
					(changesToRepeat.length != 0)
					&&
					!GraphicsEnvironment.isHeadless()
					&&
					(JOptionPane.showConfirmDialog(
							DialogPanel.getTopWindow(), 
							("Document " + ((docName == null) ? doc.getAnnotationID() : docName) + " has been edited before, but some changes have not been saved. Restore these changes?"), 
							"Repeat Changes", 
							JOptionPane.OK_CANCEL_OPTION, 
							JOptionPane.QUESTION_MESSAGE
							) 
						== 
						JOptionPane.OK_OPTION
					)
				)
				Logger.repeatChanges(changesToRepeat, ((DocumentRoot) doc));
			
			//	create editor for document
			DocumentEditor dep = new DocumentEditor(this, null);
			dep.setContent(doc, docName, format, saveOperation);
			dep.setShowExtensions(true);
			
			//	experimental: store logger
			if (logger != null)
				this.storeLogger(dep, logger);
			
			//	open the editor
			this.openDocument(dep);
		}
	}
	
	private void openDocument(DocumentEditor dep) {
		if (dep != null) {
			writeLog("Document Opened: " + dep.getTitle());
			this.openDocuments.add(dep); // remember document opened, display it when GUI is set
			if (this.gui != null) {
				if (this.gui.canOpenDocument())
					this.gui.openDocument(dep);
				
				else {
					DocumentEditor open = this.gui.getFunctionTarget();
					int openCount = this.gui.getOpenDocumentCount();
					if (open == null)
						JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), "With this GUI, GoldenGATE cannot open documents.", "Cannot Open Document", JOptionPane.ERROR_MESSAGE);
					else {
						if (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), ("With this GUI, GoldenGATE can have only " + ((openCount == 1) ? "one document" : (openCount + " documents")) + " open at the same time. Close current document?"), "Cannot Open Document", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
							if (this.close(open, false))
								this.gui.openDocument(dep);
						}
					}
				}
			}
		}
	}
	
	private HashMap loggers = new HashMap();
	private void storeLogger(DocumentEditor editor, Logger logger) {
		this.loggers.put(editor, logger);
	}
	private Logger getLogger(DocumentEditor editor) {
		return ((Logger) this.loggers.get(editor));
	}
	
	void documentSaved(DocumentEditor editor, String newName) {
		if ((this.gui != null) && (newName != null)) 
			this.gui.documentTitleChanged(editor);
		
		//	experimental: notify logger
		Logger logger = this.getLogger(editor);
		if (logger != null)
			logger.documentSaved(newName);
	}
	
	private boolean close(DocumentEditor target) {
		return this.close(target, true);
	}
	
	private boolean close(DocumentEditor target, boolean refill) {
		if (target == null) return true;
		else if (target.close()) {
			this.openDocuments.remove(target);
			
			//	experimental: close logger
			Logger logger = this.getLogger(target);
			if (logger != null)
				logger.close();
			
			if (this.gui != null) {
				this.gui.closeDocument(target);
				
				//	number of documents in GUI is limited, so show one of the ones not on display
				if (refill && (this.openDocuments.size() > this.gui.getOpenDocumentCount())) {
					Iterator it = this.openDocuments.iterator();
					while (it.hasNext() && this.gui.canOpenDocument())
						this.gui.openDocument((DocumentEditor) it.next());
				}
			}
			return true;
		}
		else return false;
	}
	
	private boolean closeAll() {
		LinkedList openDocuments = new LinkedList(this.openDocuments);
		while (openDocuments.size() != 0) {
			if (!this.close(((DocumentEditor) openDocuments.removeFirst()), false))
				return false;
		}
		return true;
	}
	
	/**
	 * Close GoldenGATE indicating that a restart with a newly selected
	 * configuration is desired.
	 */
	public void exitChangeConfiguration() {
		this.exit(EXIT_CHANGE_CONFIGURATION);
	}
	
	/**
	 * Close GoldenGATE indicating to the surrounding application that it should
	 * close down completely.
	 */
	public void exitShutdown() {
		this.exit(EXIT_SHUTDOWN);
	}
	
	private void exit(int status) {
		System.out.println("GoldenGATE exiting with status " + status);
		
		//	if all editors closed successfully
		if (this.closeAll()) {
			System.out.println(" - documents closed");
			
			//	exit resource providers
			GoldenGatePlugin[] plugins = this.getPlugins();
			for (int p = 0; p < plugins.length; p++)
				try {
					plugins[p].exit();
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while exitting " + plugins[p].getPluginName());
					t.printStackTrace(System.out);
				}
			System.out.println(" - plugins shut down");
			
			//	dispose help dialog
			if (this.help != null)
				this.help.dispose();
			System.out.println(" - help closed");
			
			//	gather and store settings
			if (this.configuration.isDataEditable()) {
				Settings settings = this.getSettings();
				try {
					this.configuration.storeSettings(settings);
					System.out.println(" - settings stored");
				}
				catch (Exception e) {
					System.out.println(e.getClass().getName() + " (" + e.getMessage() + ") while storing settings");
					e.printStackTrace(System.out);
				}
			}
			
			if (this.gui != null) try { // catch exceptions that might occur when disposing dependent dialogs
				this.gui.close();
				this.gui.exit();
				this.gui = null;
				System.out.println(" - GUI closed");
			}
			catch (Throwable t) {
				System.out.println(t.getClass().getName() + " while exitting GUI: " + t.getMessage());
				t.printStackTrace(System.out);
			}
			
			//	set singleton instance to null
//			GOLDEN_GATE = null;
			if (GUI_INSTANCE == this)
				GUI_INSTANCE = null;
			INSTANCES_BY_CONFIG_PATH.remove(this.configuration.getAbsolutePath());
			System.out.println(" - instance disposed");
			
			//	set exit status
			this.status = status;
			System.out.println(" - status set");
		}
		else System.out.println(" - could not close at least one document");
	}
	
	/**
	 * Retrieve the current settings of the editor, including the ones for
	 * DocumentEditor, AnnotationEditorPanel, and DocumentFormat. This method
	 * creates a copy, so modification has no effect. The main use of this
	 * method is for configuration export.
	 * @return the current settings of this GoldenGATE instance
	 */
	public Settings getSettings() {
		Settings settings = new Settings();
		
		//	add tokenizer name
		if (this.tokenizerName != null)
			settings.setSetting(TOKENIZER_NAME_SETTING_NAME, this.tokenizerName);
		
		//	get editor settings
		DocumentEditor.storeSettings(settings.getSubset(DOCUMENT_DISPLAY_PANEL_SETTINGS_PREFIX));
		AnnotationEditorPanel.storeSettings(settings.getSubset(ANNOTATION_EDITOR_PANEL_SETTINGS_PREFIX));
		DocumentFormat.storeSettings(settings.getSubset(DOCUMENT_FORMAT_SETTINGS_PREFIX));
		
		//	get GUI settings
		if (this.gui != null)
			this.gui.storeSettings(settings.getSubset(GUI_SETTINGS_PREFIX));
		
		return settings;
	}
	
	private void editOptions() {
		OptionsDialog od = new OptionsDialog();
		od.setLocationRelativeTo(od.getOwner());
		od.setVisible(true);
	}
	
	private class OptionsDialog extends DialogPanel {
		
		private OptionsDialog() {
			super("GoldenGATE Editing Options", true);
			final SettingsPanel annotationEditorSettingsPanel = AnnotationEditorPanel.getSettingsPanel(GoldenGATE.this);
			
			//	initialize main buttons
			JButton commitButton = new JButton("OK");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					annotationEditorSettingsPanel.commitChanges();
					dispose();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(annotationEditorSettingsPanel, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			//	set dialog size
			this.setSize(new Dimension(500, 150));
			this.setResizable(true);
		}
	}
	
	/**
	 * @return the DocumentEditor currently selected, or null, if there is no
	 *         such DocumentEditor
	 */
	public DocumentEditor getActivePanel() {
		if (this.gui == null) return null;
		
		DocumentEditor selected = this.gui.getFunctionTarget();
		if (selected != null)
			return ((DocumentEditor) selected);
		
		else if (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), "This function is avaliable only if a Document id opened.\nOpen a Document now?", "Option Not Avalable", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
			this.loadDocument();
			return this.getActivePanel();
		}
		
		else return null;
	}
	
	/**
	 * @return a read only view on the content of the DocumentEditor currently
	 *         selected, or null, if there is no such DocumentEditor
	 */
	public QueriableAnnotation getActiveDocument() {
		DocumentEditor activePanel = this.getActivePanel();
		return ((activePanel == null) ? null : activePanel.getContent());
//		if (this.gui == null) return null;
//		
//		DocumentEditor selected = this.gui.getFunctionTarget();
//		if (selected != null)
//			return ((DocumentEditor) selected).getContent();
//		
//		else if (JOptionPane.showConfirmDialog(DialogPanel.getTopWindow(), "This function is avalaible only if a Document is opened.\nOpen a Document now?", "Option Not Available", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
//			this.loadDocument();
//			return this.getActiveDocument();
//		}
//		
//		else return null;
	}
	
	/**
	 * @see de.uka.ipd.idaho.gamta.util.TestDocumentProvider#getTestDocument()
	 */
	public QueriableAnnotation getTestDocument() {
		return this.getActiveDocument();
	}
	
	/**
	 * Show a custom view of the document in the DocumentEditor currently
	 * selected.
	 * @param viewerClassName the class name of the DocumentViewer to call
	 */
	public void showDocumentView(String viewerClassName) {
		DocumentEditor de = this.getActivePanel();
		if (de != null)
			de.showDocumentView(viewerClassName);
	}
	
	CustomFunction[] getCustomFunctions() {
		return this.getCustomFunctions(null);
	}
	
	private CustomFunction[] getCustomFunctions(StartupStatusDialog ssd) {
		if (this.customFunctionManager == null)
			return new CustomFunction[0];
		
		String[] customFunctionNames = this.customFunctionManager.getResourceNames();
		Arrays.sort(customFunctionNames);
		
		ArrayList customFunctions = new ArrayList();
		for (int n = 0; n < customFunctionNames.length; n++) {
			if (ssd != null)
				ssd.addStatusLine(" - " + customFunctionNames[n]);
			CustomFunction customFunction = this.customFunctionManager.getCustomFunction(customFunctionNames[n]);
			if (customFunction != null)
				customFunctions.add(customFunction);
		}
		return ((CustomFunction[]) customFunctions.toArray(new CustomFunction[customFunctions.size()]));
	}
	
	CustomShortcut getCustomShortcut(String key, boolean isAltDown, boolean isAltGrDown) {
		if (this.customShortcutManager == null)
			return null;
		System.out.println("Getting CS: " + (isAltDown ? "Alt-" : (isAltGrDown ? "AltGr-" : "")) + key);
		return this.customShortcutManager.getCustomShortcut((isAltDown ? "Alt-" : (isAltGrDown ? "AltGr-" : "")) + key);
	}
	
	/**
	 * Show some help information.
	 * @param on the subject of the desired help information
	 */
	public void help(String on) {
		if (this.help != null)
			this.help.showHelp(on);
	}
	
	private void about() {
		StringVector aboutExtensions = new StringVector();
		GoldenGatePlugin[] plugins = this.getPlugins();
		for (int p = 0; p < plugins.length; p++) {
			String aboutPlugin = plugins[p].getAboutBoxExtension();
			if (aboutPlugin == null)
				continue;
			aboutPlugin = aboutPlugin.trim();
			if (aboutPlugin.length() == 0)
				continue;
			aboutExtensions.addElement("\n-------- " + plugins[p].getPluginName() + " --------");
			aboutExtensions.addElement(aboutPlugin);
		}
		JOptionPane.showMessageDialog(DialogPanel.getTopWindow(), (ABOUT_TEXT + (aboutExtensions.isEmpty() ? "" : ("\n" + aboutExtensions.concatStrings("\n")))), "About GoldenGATE Document Editor", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getGoldenGateIcon()));
	}
	
	private void showReadme() {
		StringVector readme;
		try {
			InputStream ris = this.configuration.getInputStream(README_FILE_NAME);
			readme = StringVector.loadList(ris);
			ris.close();
		}
		catch (IOException ioe) {
			readme = new StringVector();
			readme.addElement("An error occurred loading the readme file: " + ioe.getMessage());
			StackTraceElement[] stes = ioe.getStackTrace();
			for (int s = 0; s < stes.length; s++)
				readme.addElement(stes[s].toString());
		}
		
		final DialogPanel readmeDialog = new DialogPanel(("GoldenGATE - " + this.configuration.getName() + " - " + README_FILE_NAME), true);
		
		final JTextArea readmeDisplay = new JTextArea();
		readmeDisplay.setEditable(false);
		readmeDisplay.setLineWrap(true);
		readmeDisplay.setWrapStyleWord(true);
		readmeDisplay.setFont(new Font(DocumentEditor.getDefaultTextFontName(), Font.PLAIN, DocumentEditor.getDefaultTextFontSize()));
		readmeDisplay.setText(readme.concatStrings("\n"));
		
		final JScrollPane readmeDisplayBox = new JScrollPane(readmeDisplay);
		
		JButton okButton = new JButton("OK");
		okButton.setBorder(BorderFactory.createRaisedBevelBorder());
		okButton.setPreferredSize(new Dimension(70, 21));
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				readmeDialog.dispose();
			}
		});
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.add(okButton);
		
		readmeDialog.add(readmeDisplayBox, BorderLayout.CENTER);
		readmeDialog.add(buttonPanel, BorderLayout.SOUTH);
		
		readmeDialog.setSize(800, 600);
		readmeDialog.setLocationRelativeTo(readmeDialog.getOwner());
		readmeDialog.setResizable(true);
		
		readmeDialog.getDialog().addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent we) {
				readmeDisplayBox.getVerticalScrollBar().setValue(0);
			}
		});
		
		readmeDialog.setVisible(true);
	}
	
	private void registerPlugin(GoldenGatePlugin plugin) {
		if (plugin  != null)
			this.pluginsByClassName.put(plugin.getClass().getName(), plugin);
		
		if (plugin instanceof ResourceManager) {
			this.registerResourceProvider((ResourceManager) plugin);
			if (plugin instanceof AnnotationSourceManager)
				this.registerAnnotationSourceProvider((AnnotationSourceManager) plugin);
			if (plugin instanceof AnnotationFilterManager)
				this.registerAnnotationFilterProvider((AnnotationFilterManager) plugin);
			if (plugin instanceof DocumentProcessorManager)
				this.registerDocumentProcessorProvider((DocumentProcessorManager) plugin);
			if (plugin instanceof DocumentFormatProvider)
				this.registerDocumentFormatProvider((DocumentFormatProvider) plugin);
		}
		
		if (plugin instanceof DocumentLoader)
			this.registerDocumentLoader((DocumentLoader) plugin);
		if (plugin instanceof DocumentSaver)
			this.registerDocumentSaver((DocumentSaver) plugin);
		
		if (plugin instanceof DocumentViewer)
			this.registerDocumentViewer((DocumentViewer) plugin);
		
		if (plugin instanceof DocumentEditorExtension)
			this.registerDocumentEditorExtension((DocumentEditorExtension) plugin);
		
		if (plugin instanceof CustomFunction.Manager)
			this.customFunctionManager = ((CustomFunction.Manager) plugin);
		
		if (plugin instanceof CustomShortcut.Manager)
			this.customShortcutManager = ((CustomShortcut.Manager) plugin);
		
		if (plugin instanceof TokenizerManager)
			this.tokenizerManager = ((TokenizerManager) plugin);
	}
	
	//	register and lookup method for generic plugin access
	private HashMap pluginsByClassName = new LinkedHashMap();
	
	/**
	 * Find a GoldenGatePlugin by its class name.
	 * @param pluginClassName the class name of the desired GoldenGatePlugin
	 * @return the GoldenGatePlugin with the specified class name
	 */
	public GoldenGatePlugin getPlugin(String pluginClassName) {
		return ((GoldenGatePlugin) this.pluginsByClassName.get(pluginClassName));
	}
	
	/**
	 * Get all GoldenGatePlugins that are currently available.
	 * @return an array holding all GoldenGatePlugins registered
	 */
	public GoldenGatePlugin[] getPlugins() {
		ArrayList extensions = new ArrayList(this.pluginsByClassName.values());
		return ((GoldenGatePlugin[]) extensions.toArray(new GoldenGatePlugin[extensions.size()]));
	}
	
	
	
	//	register and lookup method for generic Resource access
	private HashMap editorExtensionsByClassName = new LinkedHashMap();
	
	private void registerDocumentEditorExtension(DocumentEditorExtension editorExtension) {
		if (editorExtension  != null)
			this.editorExtensionsByClassName.put(editorExtension.getClass().getName(), editorExtension);
	}
	
	/**
	 * Find a DocumentEditorExtension by its class name.
	 * @param extensionClassName the class name of the desired
	 *            DocumentEditorExtension
	 * @return the DocumentEditorExtension with the specified class name
	 */
	public DocumentEditorExtension getDocumentEditorExtension(String extensionClassName) {
		return ((DocumentEditorExtension) this.editorExtensionsByClassName.get(extensionClassName));
	}
	
	/**
	 * Get all DocumentEditorExtensions that are currently available.
	 * @return an array holding all DocumentEditorExtensions registered
	 */
	public DocumentEditorExtension[] getDocumentEditorExtensions() {
		ArrayList extensions = new ArrayList(this.editorExtensionsByClassName.values());
		return ((DocumentEditorExtension[]) extensions.toArray(new DocumentEditorExtension[extensions.size()]));
	}
	
	
	//	register and lookup method for generic Resource access
	private HashMap resourceProvidersByClassName = new LinkedHashMap();
	
	private void registerResourceProvider(ResourceManager resourceProvider) {
		if (resourceProvider  != null)
			this.resourceProvidersByClassName.put(resourceProvider.getClass().getName(), resourceProvider);
	}
	
	/**
	 * Find a ResourceManager by its class name (exspecially useful to find the
	 * provider of a Resource).
	 * @param providerClassName the class name of the desired ResourceProvider
	 * @return the ResourceManager with the specified class name
	 */
	public ResourceManager getResourceProvider(String providerClassName) {
		return ((ResourceManager) this.resourceProvidersByClassName.get(providerClassName));
	}
	
	/**
	 * Get all ResourceManagers that are currently available.
	 * @return an array holding all ResourceManager registered
	 */
	public ResourceManager[] getResourceProviders() {
		ArrayList managers = new ArrayList(this.resourceProvidersByClassName.values());
		return ((ResourceManager[]) managers.toArray(new ResourceManager[managers.size()]));
	}
	
	
	
	//	register and lookup method for generic DocumentProcessor access 
	private HashMap documentProcessorProvidersByClassName = new LinkedHashMap();
	
	private void registerDocumentProcessorProvider(DocumentProcessorManager documentProcessorProvider) {
		if (documentProcessorProvider != null)
			this.documentProcessorProvidersByClassName.put(documentProcessorProvider.getClass().getName(), documentProcessorProvider);
	}
	
	/**
	 * Find a DocumentProcessorManager by its class name (exspecially useful to
	 * find the provider of a DocumentProcessor).
	 * @param providerClassName the class name of the desired
	 *            DocumentProcessorProvider
	 * @return the DocumentProcessorManager with the specified class name
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor#getProviderClassName()
	 */
	public DocumentProcessorManager getDocumentProcessorProvider(String providerClassName) {
		return ((DocumentProcessorManager) this.documentProcessorProvidersByClassName.get(providerClassName));
	}
	
	/**
	 * Get all DocumentProcessorManagers currently available.
	 * @return an array holding all DocumentProcessorManagers registered
	 */
	public DocumentProcessorManager[] getDocumentProcessorProviders() {
		ArrayList managers = new ArrayList(this.documentProcessorProvidersByClassName.values());
		return ((DocumentProcessorManager[]) managers.toArray(new DocumentProcessorManager[managers.size()]));
	}
	
	
	/**
	 * Retrieve a document processor by its name. The name may be fully
	 * qualified, i.e., include the providerClassName, but need not. In the
	 * latter case, all document processor managers will be asked for a document
	 * processor with the specified name, and the first one found will be
	 * returned.
	 * @param name the name of the document processor
	 * @return the document processor with the specified name, or null, if there
	 *         is no such document processor
	 */
	public DocumentProcessor getDocumentProcessorForName(String name) {
		int nameSplit = ((name == null) ? -1 : name.indexOf('@'));
		if ((nameSplit == -1) || ((nameSplit + 1) == name.length()))
			return this.getDocumentProcessorForName(name, null);
		else return this.getDocumentProcessorForName(name.substring(0, nameSplit), name.substring(nameSplit + 1));
	}
	
	/**
	 * Retrieve a document processor by its name. The providerClassName may be
	 * null. In this latter case, all document processor managers will be asked
	 * for a document processor with the specified name, and the first one found
	 * will be returned.
	 * @param name the name of the document processor
	 * @param providerClassName the class name of the desired document processor
	 *            manager to ask for the document processor
	 * @return the document processor with the specified name, or null, if there
	 *         is no such document processor
	 */
	public DocumentProcessor getDocumentProcessorForName(String name, String providerClassName) {
		if (providerClassName == null) {
			DocumentProcessorManager[] dpms = this.getDocumentProcessorProviders();
			for (int m = 0; m < dpms.length; m++) {
				DocumentProcessor dp = dpms[m].getDocumentProcessor(name);
				if (dp != null) return dp;
			}
			return null;
		}
		else {
			DocumentProcessorManager dpm = this.getDocumentProcessorProvider(providerClassName);
			return ((dpm == null) ? null : dpm.getDocumentProcessor(name));
		}
	}
	
	
	
	//	register and lookup method for generic DocumentProcessor access 
	private HashMap documentAnnotatorProvidersByClassName = new LinkedHashMap();
	
	private void registerAnnotationSourceProvider(AnnotationSourceManager snnotationSourceProvider) {
		if (snnotationSourceProvider  != null)
			this.documentAnnotatorProvidersByClassName.put(snnotationSourceProvider.getClass().getName(), snnotationSourceProvider);
	}
	
	/**
	 * Find a AnnotationSourceManager by its class name (exspecially useful to
	 * find the provider of a AnnotationSource).
	 * @param providerClassName the class name of the desired
	 *            AnnotationSourceProvider
	 * @return the AnnotationSourceManager with the specified class name
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AnnotationSource#getProviderClassName()
	 */
	public AnnotationSourceManager getAnnotationSourceProvider(String providerClassName) {
		return ((AnnotationSourceManager) this.documentAnnotatorProvidersByClassName.get(providerClassName));
	}
	
	/**
	 * Get all AnnotationSourceManagers currently available.
	 * @return an array holding all AnnotationSourceManagers registered
	 */
	public AnnotationSourceManager[] getAnnotationSourceProviders() {
		ArrayList managers = new ArrayList(this.documentAnnotatorProvidersByClassName.values());
		return ((AnnotationSourceManager[]) managers.toArray(new AnnotationSourceManager[managers.size()]));
	}
	
	/**
	 * Retrieve an annotation source by its name. The name may be fully
	 * qualified, i.e., include the providerClassName, but need not. In the
	 * latter case, all annotation source managers will be asked for a
	 * annotation source with the specified name, and the first one found will
	 * be returned.
	 * @param name the name of the annotation source
	 * @return the annotation source with the specified name, or null, if there
	 *         is no such annotation source
	 */
	public AnnotationSource getAnnotationSourceForName(String name) {
		int nameSplit = ((name == null) ? -1 : name.indexOf('@'));
		if ((nameSplit == -1) || ((nameSplit + 1) == name.length()))
			return this.getAnnotationSourceForName(name, null);
		else return this.getAnnotationSourceForName(name.substring(0, nameSplit), name.substring(nameSplit + 1));
	}
	
	/**
	 * Retrieve a annotation source by its name. The providerClassName may be
	 * null. In this latter case, all annotation source managers will be asked
	 * for a annotation source with the specified name, and the first one found
	 * will be returned.
	 * @param name the name of the annotation source
	 * @param providerClassName the class name of the desired annotation source
	 *            manager to ask for the annotation source
	 * @return the annotation source with the specified name, or null, if there
	 *         is no such annotation source
	 */
	public AnnotationSource getAnnotationSourceForName(String name, String providerClassName) {
		if (providerClassName == null) {
			AnnotationSourceManager[] asms = this.getAnnotationSourceProviders();
			for (int m = 0; m < asms.length; m++) {
				AnnotationSource as = asms[m].getAnnotationSource(name);
				if (as != null) return as;
			}
			return null;
		}
		else {
			AnnotationSourceManager asm = this.getAnnotationSourceProvider(providerClassName);
			return ((asm == null) ? null : asm.getAnnotationSource(name));
		}
	}
	
	
	
	//	register and lookup method for generic DocumentProcessor access 
	private HashMap annotationFilterProvidersByClassName = new LinkedHashMap();
	
	private void registerAnnotationFilterProvider(AnnotationFilterManager annotationFilterProvider) {
		if (annotationFilterProvider  != null)
			this.annotationFilterProvidersByClassName.put(annotationFilterProvider.getClass().getName(), annotationFilterProvider);
	}
	
	/**
	 * Find a AnnotationFilterManager by its class name (exspecially useful to
	 * find the provider of a AnnotationFilters).
	 * @param providerClassName the class name of the desired
	 *            AnnotationFilterManager
	 * @return the AnnotationFilterManager with the specified class name
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AnnotationSource#getProviderClassName()
	 */
	public AnnotationFilterManager getAnnotationFilterProvider(String providerClassName) {
		return ((AnnotationFilterManager) this.annotationFilterProvidersByClassName.get(providerClassName));
	}
	
	/**
	 * Get all AnnotationFilterManagers currently available.
	 * @return an array holding all AnnotationFilterManagers registered
	 */
	public AnnotationFilterManager[] getAnnotationFilterProviders() {
		ArrayList managers = new ArrayList(this.annotationFilterProvidersByClassName.values());
		return ((AnnotationFilterManager[]) managers.toArray(new AnnotationFilterManager[managers.size()]));
	}
	
	/**
	 * Retrieve an annotation filter by its name. The name may be fully
	 * qualified, i.e., include the providerClassName, but need not. In the
	 * latter case, all annotation filter managers will be asked for a
	 * annotation filter with the specified name, and the first one found will
	 * be returned.
	 * @param name the name of the annotation filter
	 * @return the annotation filter with the specified name, or null, if there
	 *         is no such annotation filter
	 */
	public AnnotationFilter getAnnotationFilterForName(String name) {
		int nameSplit = ((name == null) ? -1 : name.indexOf('@'));
		if ((nameSplit == -1) || ((nameSplit + 1) == name.length()))
			return this.getAnnotationFilterForName(name, null);
		else return this.getAnnotationFilterForName(name.substring(0, nameSplit), name.substring(nameSplit + 1));
	}
	
	/**
	 * Retrieve a annotation filter by its name. The providerClassName may be
	 * null. In this latter case, all annotation filter managers will be asked
	 * for a annotation filter with the specified name, and the first one found
	 * will be returned.
	 * @param name the name of the annotation filter
	 * @param providerClassName the class name of the desired annotation source
	 *            manager to ask for the annotation filter
	 * @return the annotation filter with the specified name, or null, if there
	 *         is no such annotation filter
	 */
	public AnnotationFilter getAnnotationFilterForName(String name, String providerClassName) {
		if (providerClassName == null) {
			AnnotationFilterManager[] afms = this.getAnnotationFilterProviders();
			for (int m = 0; m < afms.length; m++) {
				AnnotationFilter af = afms[m].getAnnotationFilter(name);
				if (af != null) return af;
			}
			return null;
		}
		else {
			AnnotationFilterManager afm = this.getAnnotationFilterProvider(providerClassName);
			return ((afm == null) ? null : afm.getAnnotationFilter(name));
		}
	}
	
	
	
	//	register and lookup method for generic DocumentLoader access 
	private HashMap documentLoadersByClassName = new LinkedHashMap();
	
	private void registerDocumentLoader(DocumentLoader documentLoader) {
		if (documentLoader  != null)
			this.documentLoadersByClassName.put(documentLoader.getClass().getName(), documentLoader);
	}
	
	/**
	 * Find a DocumentLoader by its class name (exspecially useful to find a
	 * DocumentLoader).
	 * @param loaderClassName the class name of the desired DocumentLoader
	 * @return the DocumentLoader with the specified class name
	 */
	public DocumentLoader getDocumentLoader(String loaderClassName) {
		return ((DocumentLoader) this.documentLoadersByClassName.get(loaderClassName));
	}
	
	/**
	 * Get all DocumentLoader currently available.
	 * @return an array holding all DocumentLoaders registered
	 */
	public DocumentLoader[] getDocumentLoaders() {
		ArrayList damps = new ArrayList(this.documentLoadersByClassName.values());
		return ((DocumentLoader[]) damps.toArray(new DocumentLoader[damps.size()]));
	}
	
	
	
	//	register and lookup method for generic DocumentSaver access 
	private HashMap documentSaversByClassName = new LinkedHashMap();
	
	private void registerDocumentSaver(DocumentSaver documentSaver) {
		if (documentSaver  != null)
			this.documentSaversByClassName.put(documentSaver.getClass().getName(), documentSaver);
	}
	
	/**
	 * Find a DocumentSaver by its class name (exspecially useful to find a
	 * DocumentSaver).
	 * @param saverClassName the class name of the desired DocumentSaver
	 * @return the DocumentSaver with the specified class name
	 */
	public DocumentSaver getDocumentSaver(String saverClassName) {
		return ((DocumentSaver) this.documentSaversByClassName.get(saverClassName));
	}
	
	/**
	 * Get all DocumentSaver currently available.
	 * @return an array holding all DocumentSavers registered
	 */
	public DocumentSaver[] getDocumentSavers() {
		ArrayList damps = new ArrayList(this.documentSaversByClassName.values());
		return ((DocumentSaver[]) damps.toArray(new DocumentSaver[damps.size()]));
	}
	
	
	
	//	register and lookup method for generic DocumentFormat access 
	private HashMap documentFormatProvidersByClassName = new LinkedHashMap();
	
	private void registerDocumentFormatProvider(DocumentFormatProvider documentFormatter) {
		if (documentFormatter  != null)
			this.documentFormatProvidersByClassName.put(documentFormatter.getClass().getName(), documentFormatter);
	}
	
	/**
	 * Find a DocumentFormatProvider by its class name (exspecially useful to
	 * find a DocumentFormatProvider).
	 * @param formatterClassName the class name of the desired
	 *            DocumentFormatProvider
	 * @return the DocumentFormatProvider with the specified class name
	 */
	public DocumentFormatProvider getDocumentFormatProvider(String formatterClassName) {
		return ((DocumentFormatProvider) this.documentFormatProvidersByClassName.get(formatterClassName));
	}
	
	/**
	 * Get all DocumentFormatProvider currently available.
	 * @return an array holding all DocumentFormatProviders registered
	 */
	public DocumentFormatProvider[] getDocumentFormatProviders() {
		ArrayList damps = new ArrayList(this.documentFormatProvidersByClassName.values());
		return ((DocumentFormatProvider[]) damps.toArray(new DocumentFormatProvider[damps.size()]));
	}
	
	/**
	 * Obtain the format for some specific file extension (search all available
	 * DocumentFormatProviders).
	 * @param fileExtension the file extension to obtain the DocumentFormat for
	 * @return the DocumentFormat for the specified file extension, or null, if
	 *         this DocumentFormatProvider does not provide a format for the
	 *         specified file extension
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormatProvider#getFormatForFileExtension(java.lang.String)
	 */
	public DocumentFormat getDocumentFormatForFileExtension(String fileExtension) {
		DocumentFormatProvider[] formatProviders = this.getDocumentFormatProviders();
		for (int f = 0; f < formatProviders.length; f++) {
			DocumentFormat format = formatProviders[f].getFormatForFileExtension(fileExtension);
			if (format != null) return format;
		}
		return null;
	}
	
	/**
	 * Obtain a DocumentFormat by its name. The name may be fully
	 * qualified, i.e., include the providerClassName, but need not. In the
	 * latter case, all document format providers will be asked for a
	 * document format with the specified name, and the first one found will
	 * be returned.
	 * @param formatName the name of the desired DocumentFormat
	 * @return the DocumentFormat with the specified name, or null, if there is
	 *         no such DocumentFormat
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormatProvider#getFormatForName(java.lang.String)
	 */
	public DocumentFormat getDocumentFormatForName(String formatName) {
//		DocumentFormatProvider[] formatProviders = this.getDocumentFormatProviders();
//		for (int f = 0; f < formatProviders.length; f++) {
//			DocumentFormat format = formatProviders[f].getFormatForName(formatName);
//			if (format != null) return format;
//		}
//		return null;
		int nameSplit = ((formatName == null) ? -1 : formatName.indexOf('@'));
		if ((nameSplit == -1) || ((nameSplit + 1) == formatName.length()))
			return this.getDocumentFormatForName(formatName, null);
		else return this.getDocumentFormatForName(formatName.substring(0, nameSplit), formatName.substring(nameSplit + 1));
	}
	
	/**
	 * Obtain a DocumentFormat by its name. The providerClassName may be
	 * null. In this latter case, all document format providers will be asked for a
	 * document format with the specified name, and the first one found will
	 * be returned.
	 * @param formatName the name of the desired DocumentFormat
	 * @param providerClassName the class name of the desired document format provider to ask for the document format
	 * @return the document format with the specified name, or null, if there
	 *         is no such document format
	 */
	public DocumentFormat getDocumentFormatForName(String formatName, String providerClassName) {
		if (providerClassName == null) {
			DocumentFormatProvider[] dfps = this.getDocumentFormatProviders();
			for (int p = 0; p < dfps.length; p++) {
				DocumentFormat df = dfps[p].getFormatForName(formatName);
				if (df != null) return df;
			}
			return null;
		}
		else {
			DocumentFormatProvider dfp = this.getDocumentFormatProvider(providerClassName);
			return ((dfp == null) ? null : dfp.getFormatForName(formatName));
		}
	}
	
	/**
	 * Offer all available load DocumentFormats for selection.
	 * @return the name of the selected DocumentFormat, or null, if the
	 *         selection was cancelled
	 */
	public String selectLoadFormat() {
		DocumentFormatProvider[] formatProviders = this.getDocumentFormatProviders();
		StringVector formatNames = new StringVector();
		for (int f = 0; f < formatProviders.length; f++)
			formatNames.addContent(formatProviders[f].getLoadFormatNames());
		Object o = JOptionPane.showInputDialog(DialogPanel.getTopWindow(), "Please select a format for loading documents.", "Select Load Format", JOptionPane.QUESTION_MESSAGE, null, formatNames.toStringArray(), null);
		return ((o == null) ? null : o.toString());
	}
	
	/**
	 * Offer all available save DocumentFormats for selection.
	 * @return the name of the selected DocumentFormat, or null, if the
	 *         selection was cancelled
	 */
	public String selectSaveFormat() {
		DocumentFormatProvider[] formatProviders = this.getDocumentFormatProviders();
		StringVector formatNames = new StringVector();
		for (int f = 0; f < formatProviders.length; f++)
			formatNames.addContent(formatProviders[f].getSaveFormatNames());
		Object o = JOptionPane.showInputDialog(DialogPanel.getTopWindow(), "Please select a format to save documents in.", "Select Save Format", JOptionPane.QUESTION_MESSAGE, null, formatNames.toStringArray(), null);
		return ((o == null) ? null : o.toString());
	}
	
	
	
	//	register and lookup method for generic DocumentLoader access 
	private HashMap documentViewersByClassName = new LinkedHashMap();
	
	private void registerDocumentViewer(DocumentViewer documentViewer) {
		if (documentViewer  != null)
			this.documentViewersByClassName.put(documentViewer.getClass().getName(), documentViewer);
	}
	
	/**
	 * Find a DocumentViewer by its class name (exspecially useful to find a
	 * DocumentViewer).
	 * @param viewerClassName the class name of the desired DocumentViewer
	 * @return the DocumentViewer with the specified class name
	 */
	public DocumentViewer getDocumentViewer(String viewerClassName) {
		return ((DocumentViewer) this.documentViewersByClassName.get(viewerClassName));
	}
	
	/**
	 * Get all DocumentViewer currently available.
	 * @return the DocumentViewer with the specified class name
	 */
	public DocumentViewer[] getDocumentViewers() {
		ArrayList damps = new ArrayList(this.documentViewersByClassName.values());
		return ((DocumentViewer[]) damps.toArray(new DocumentViewer[damps.size()]));
	}
	
	
	private Vector resourceObservers = new Vector();
	
	/**
	 * Register a ResourceObserver so it is notified when
	 * resourceChanged(String) is called.
	 * @param observer the ResourceObserver to register
	 */
	public void registerResourceObserver(ResourceObserver observer) {
		if (observer != null) this.resourceObservers.add(observer);
	}
	
	/**
	 * Unregister a ResourceObserver so it is not notified any more when
	 * resourceChanged(String) is called.
	 * @param observer the ResourceObserver to unregister
	 */
	public void unregisterResourceObserver(ResourceObserver observer) {
		this.resourceObservers.remove(observer);
	}
	
	/**
	 * Notify all registered ResourceObservers that the resources provided by
	 * the ResourceManager with the specified name have changed.
	 * @param resourceProviderClassName the class name of the ResourceManager
	 *            issuing the cahnge
	 */
	public void notifyResourcesChanged(String resourceProviderClassName) {
		for (int o = 0; o < this.resourceObservers.size(); o++)
			((ResourceObserver) this.resourceObservers.get(o)).resourcesChanged(resourceProviderClassName);
	}
	
	
	private Vector annotationObservers = new Vector();
	
	/**
	 * Register an AnnotationObserver so it is notified whenever an Annotation
	 * is modified manually in a document.
	 * @param ao the AnnotationObserver to register
	 */
	public void registerAnnotationObserver(AnnotationObserver ao) {
		if (ao != null) this.annotationObservers.add(ao);
	}
	
	/**
	 * Unregister an AnnotationObserver so it is not notified any more whenever
	 * an Annotation is modified manually in a document.
	 * @param ao the AnnotationObserver to unregister
	 */
	public void unregisterAnnotationObserver(AnnotationObserver ao) {
		this.annotationObservers.remove(ao);
	}
	
	void notifyAnnotationAdded(QueriableAnnotation doc, Annotation added, Resource source) {
//		System.out.println("Annotation added (" + added.getType() + " - '" + added.getValue() + "') " + ((source == null) ? "manually" : "by " + source.getName()));
		if (this.annotationObservers != null) {
			QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
			Annotation addedAnnotation = ((added instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) added) : added);
			for (int l = 0; l < this.annotationObservers.size(); l++) {
				try {
					((AnnotationObserver) this.annotationObservers.get(l)).annotationAdded(docAnnotation, addedAnnotation, source);
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation added.");
					t.printStackTrace(System.out);
				}
			}
		}
	}
	
	void notifyAnnotationRemoved(QueriableAnnotation doc, Annotation removed, Resource source) {
//		System.out.println("Annotation removed (" + removed.getType() + " - '" + removed.getValue() + "') " + ((source == null) ? "manually" : "by " + source.getName()));
		if (this.annotationObservers != null) {
			QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
			Annotation removedAnnotation = ((removed instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) removed) : removed);
			for (int l = 0; l < this.annotationObservers.size(); l++) {
				try {
					((AnnotationObserver) this.annotationObservers.get(l)).annotationRemoved(docAnnotation, removedAnnotation, source);
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation removed.");
					t.printStackTrace(System.out);
				}
			}
		}
	}
	
	void notifyAnnotationTypeChanged(QueriableAnnotation doc, Annotation reTyped, String oldType, Resource source) {
//		System.out.println("Annotation type changed (" + oldType + " --> " + reTyped.getType() + " - '" + reTyped.getValue() + "') " + ((source == null) ? "manually" : "by " + source.getName()));
		if (this.annotationObservers != null) {
			QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
			Annotation reTypedAnnotation = ((reTyped instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) reTyped) : reTyped);
			for (int l = 0; l < this.annotationObservers.size(); l++) {
				try {
					((AnnotationObserver) this.annotationObservers.get(l)).annotationTypeChanged(docAnnotation, reTypedAnnotation, oldType, source);
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation type change.");
					t.printStackTrace(System.out);
				}
			}
		}
	}
	
	void notifyAnnotationAttributeChanged(QueriableAnnotation doc, Annotation target, String attributeName, Object oldValue, Resource source) {
//		System.out.println("Annotation type changed (" + oldType + " --> " + reTyped.getType() + " - '" + reTyped.getValue() + "') " + ((source == null) ? "manually" : "by " + source.getName()));
		if (this.annotationObservers != null) {
			QueriableAnnotation docAnnotation = new ImmutableAnnotation(doc);
			Annotation targetAnnotation = ((target instanceof QueriableAnnotation) ? new ImmutableAnnotation((QueriableAnnotation) target) : target);
			for (int l = 0; l < this.annotationObservers.size(); l++) {
				try {
					((AnnotationObserver) this.annotationObservers.get(l)).annotationAttributeChanged(docAnnotation, targetAnnotation, attributeName, oldValue, source);
				}
				catch (Throwable t) {
					System.out.println(t.getClass().getName() + " (" + t.getMessage() + ") while notifying '" + this.annotationObservers.get(l).getClass().getName() + "' of annotation attribute change.");
					t.printStackTrace(System.out);
				}
			}
		}
	}
	
	
	/**	@return	the tokenizer to use
	 */
	public Tokenizer getTokenizer() {
		if (this.tokenizerManager == null) return Gamta.INNER_PUNCTUATION_TOKENIZER;
		Tokenizer tokenizer = this.tokenizerManager.getTokenizer(this.tokenizerName);
		return ((tokenizer == null) ? Gamta.INNER_PUNCTUATION_TOKENIZER : tokenizer);
	}
	
	/**
	 * Set the GUI component for this GoldenGATE instance. If there is already a
	 * GUI instance registered with this GoldenGATE instance, it will be shut
	 * down. It will not get a chance to store its settings.
	 * @param gui the new GUI to use
	 */
	public void setGui(GoldenGateGUI gui) {
		
		//	exit old GUI on replacements at runtime
		if (this.gui != null) {
			Iterator it = this.openDocuments.iterator();
			while (it.hasNext())
				this.gui.closeDocument((DocumentEditor) it.next());
			this.gui.close();
			this.gui.exit();
			this.gui = null;
		}
		
		//	remove GUI
		if (gui == null) {
			if (GUI_INSTANCE == this)
				GUI_INSTANCE = null;
		}
		
		//	trying to set non-null GUI, but other instance already has a GUI
		else if (GUI_INSTANCE != null)
			throw new RuntimeException("Only one GoldenGATE instance may have a GUI at the same time.");
		
		//	set GUI
		else {
			GUI_INSTANCE = this;
			this.gui = gui;
			this.gui.setParent(this);
			this.gui.init(this.configuration.getSettings().getSubset(GUI_SETTINGS_PREFIX));
			Iterator it = this.openDocuments.iterator();
			while (it.hasNext() && this.gui.canOpenDocument())
				this.gui.openDocument((DocumentEditor) it.next());
		}
	}
	
	private GoldenGateConfiguration configuration;
	private boolean allowChangeConfiguration;
	
	private void editSettings() {
		SettingsDialog sd = new SettingsDialog(this.getSettingsPanels());
		sd.setLocationRelativeTo(sd.getOwner());
		sd.setVisible(true);
	}
	
	private class SettingsDialog extends DialogPanel {
		
		private String settingsTokenizerName = tokenizerName;
		
		private SettingsDialog(final SettingsPanel[] settingsPanels) {
			super("GoldenGATE Settings", true);
			
			//	initialize main buttons
			JButton commitButton = new JButton("OK");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					tokenizerName = settingsTokenizerName;
					for (int s = 0; s < settingsPanels.length; s++)
						try {
							settingsPanels[s].commitChanges();
						}
						catch (Throwable t) {
							System.out.println(t.getClass().getName() + " while committings changes in " + settingsPanels[s].getClass().getName() + ": " + t.getMessage());
							t.printStackTrace(System.out);
						}
					dispose();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			
			final JButton tokenizerButton = new JButton("Tokenizer" + ((this.settingsTokenizerName == null) ? "" : (": (" + this.settingsTokenizerName + ")")));
			tokenizerButton.setBorder(BorderFactory.createRaisedBevelBorder());
			tokenizerButton.setPreferredSize(new Dimension(200, 21));
			tokenizerButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					ResourceDialog rd = ResourceDialog.getResourceDialog(tokenizerManager, "Select Tokenizer", "Select");
					rd.setLocationRelativeTo(SettingsDialog.this);
					rd.setVisible(true);
					
					//	get tokenizer name
					String newTokenizerName = rd.getSelectedResourceName();
					Tokenizer newTokenizer = tokenizerManager.getTokenizer(newTokenizerName);
					if (newTokenizer != null) {
						settingsTokenizerName = newTokenizerName;
						tokenizerButton.setText("Tokenizer" + ((settingsTokenizerName == null) ? "" : (": (" + settingsTokenizerName + ")")));
					}
				}
			});
			
			JPanel mainButtonPanel = new JPanel();
			mainButtonPanel.setLayout(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			mainButtonPanel.add(tokenizerButton);
			
			//	create tabs
			JTabbedPane tabs = new JTabbedPane();
			for (int s = 0; s < settingsPanels.length; s++)
				tabs.addTab(settingsPanels[s].getTitle(), null, settingsPanels[s], settingsPanels[s].getToolTip());
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(tabs, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			//	set dialog size
			this.setSize(new Dimension(650, 500));
			this.setResizable(true);
		}
	}
	
	private static final String GUI_SETTINGS_PREFIX = "GUI";
	private static final String DOCUMENT_DISPLAY_PANEL_SETTINGS_PREFIX = "DDP";
	private static final String ANNOTATION_EDITOR_PANEL_SETTINGS_PREFIX = "AEP";
	private static final String DOCUMENT_FORMAT_SETTINGS_PREFIX = "DFM";
	
	private static final String TOKENIZER_NAME_SETTING_NAME = "TOKENIZER";
	
	private static HashMap INSTANCES_BY_CONFIG_PATH = new HashMap();
	private static GoldenGATE GUI_INSTANCE = null;
//	private static GoldenGATE GOLDEN_GATE = null;
	
	/**
	 * @return the GoldenGATE icon as provided by the current configuration
	 */
	public Image getGoldenGateIcon() {
		return this.iconImage;
	}
	
	/**
	 * @return the name of the configuration wrapped in this GoldenGATE instace
	 */
	public String getConfigurationName() {
		return ((this.configuration == null) ? null : this.configuration.getName());
	}
	
	/**
	 * @return the path of the configuration wrapped in this GoldenGATE instace,
	 *         relative to the root path of the surrounding GoldenGATE
	 *         installation
	 */
	public String getConfigurationPath() {
		return ((this.configuration == null) ? null : this.configuration.getPath());
	}
	
	/**
	 * @return true if GoldenGATE may exit in order to restart with a new
	 *         configuration, false otherwise
	 */
	public boolean allowChangeConfiguration() {
		return this.allowChangeConfiguration;
	}
	
	/** the status value indicating that GoldenGATE is starting up */
	public static final int STARTING = -1;
	
	/** the status value indicating that GoldenGATE is running */
	public static final int RUNNING = 0;
	
	/** the status value indicating that GoldenGATE should be closed */
	public static final int EXIT_SHUTDOWN = 1;
	
	/** the status value indicating that GoldenGATE was closed and should be restarted with a newly selected GoldenGateConfiguration */
	public static final int EXIT_CHANGE_CONFIGURATION = 2;
	
	private int status = STARTING;
	private boolean lazyLoadingDone = false;
	
	/**
	 * @return true if GoldenGATE has finished the startup process for the
	 *         current configuration, i.e. if the loading phase has finished and
	 *         all the leazy loading processes have run
	 */
	public boolean isStartupFinished() {
		return ((this.status == RUNNING) && this.lazyLoadingDone);
	}
	
	/**
	 * @return the status of this GoldenGATE editor window, one of STARING,
	 *         RUNNING, EXIT_SHUTDOWN, EXIT_CHANGE_CONFIGURATION
	 */
	public int getStatus() {
		return this.status;
	}
	
//	/**
//	 * Retrieve the current GoldenGATE instance. This is the GoldenGATE instance
//	 * returned by the last invocation of the openGoldenGate() method.
//	 * @return the current GoldenGATE instance, or null, if no instance has been
//	 *         created so far
//	 */
//	public static GoldenGATE getCurrentInstance() {
//		return GOLDEN_GATE;
//		
//	- provide static method getGuiInstance(), returning respective variable
//	- provide static getInstanceForConfiguration(String configName) --> GG method to avoid double loading
//	  ==> implement based on static registry of GG instances by config name
//	- facilitate switching configurations without restart
//	  - switch GUI instance
//	  - set display settings of open documents
//
//		//	TODOne figure out how to abandon singleton property
//		
//		//	TODOne maybe this works: give every (writeable) configuration an identifier and keep static list of configurations in use, preventing two instances to be started on top of same configuration 
//		
//		//	TODOne OR use marker file (similar to Eclipse Workspace lock) to indicate file configurations in use, e.g. 'lock.cnfg'
//		
//		//	TODOne to give configurations access to the GG instance running on top of them, inject GG instance into configuration in openGoldenGATE()
//		
//		//	TODOne figure out how to make sure that only one GG instance per JVM can have (1) a GUI and (2) open DocumentEditor instances (due to the static settings in the latter)
//		
//	/*
//	 * DO NOT DO THIS, STICK TO SINGLETON (ONE GG INSTANCE PER JVM), AS ANYTHING
//	 * ELSE WILL CAUSE INTRACTABLE TROUBLE WITH STATIC FIELDS IN PLUGINS, ETC.
//	 */
//	 }
//	
	/**
	 * Create an instance of the GoldenGATE editor core with a specific
	 * configuration (this will automatically call exit() on a running
	 * GoldenGATE instance)
	 * @param configuration the GoldenGateConfiguration to use
	 * @param allowChangeConfig allow returning to configuration selection?
	 *            (setting this parameter to true makes sense if GoldenGATE is
	 *            started with a configuration selected out of at least two of
	 *            them)
	 * @return a new GoldenGATE editor window to work with the specified
	 *         configuration
	 */
	public static GoldenGATE openGoldenGATE(GoldenGateConfiguration configuration, boolean allowChangeConfig) throws IOException {
		return openGoldenGATE(configuration, allowChangeConfig, true);
	}
	
	/**
	 * Create an instance of the GoldenGATE editor core with a specific
	 * configuration (this will automatically call exit() on a running
	 * GoldenGATE instance)
	 * @param configuration the GoldenGateConfiguration to use
	 * @param allowChangeConfig allow returning to configuration selection?
	 *            (setting this parameter to true makes sense if GoldenGATE is
	 *            started with a configuration selected out of at least two of
	 *            them)
	 * @param showStatus use a splash screen for monitoring startup status? If
	 *            set to false, or if the JVM is headless, the status
	 *            information will go to System.out instead.
	 * @return a new GoldenGATE editor window to work with the specified
	 *         configuration
	 */
	public static synchronized GoldenGATE openGoldenGATE(GoldenGateConfiguration configuration, boolean allowChangeConfig, boolean showStatus) throws IOException {
		StartupStatusMonitor ssm = null;
		if (showStatus && !GraphicsEnvironment.isHeadless())
			ssm = new StartupStatusDialog(configuration.getName(), configuration.getIconImage());
		return openGoldenGATE(configuration, allowChangeConfig, ssm);
	}
	
	/**
	 * Create an instance of the GoldenGATE editor core with a specific
	 * configuration (this will automatically call exit() on a running
	 * GoldenGATE instance)
	 * @param configuration the GoldenGateConfiguration to use
	 * @param allowChangeConfig allow returning to configuration selection?
	 *            (setting this parameter to true makes sense if GoldenGATE is
	 *            started with a configuration selected out of at least two of
	 *            them)
	 * @param ssm a status monitor receiving information on the startup process.
	 *            If set to null, the status information will go to System.out
	 *            instead.
	 * @return a new GoldenGATE editor window to work with the specified
	 *         configuration
	 */
	public static synchronized GoldenGATE openGoldenGATE(GoldenGateConfiguration configuration, boolean allowChangeConfig, StartupStatusMonitor ssm) throws IOException {
//		if (GOLDEN_GATE != null)
//			GOLDEN_GATE.exit(EXIT_SHUTDOWN);
		GoldenGATE egg = ((GoldenGATE) INSTANCES_BY_CONFIG_PATH.get(configuration.getAbsolutePath()));
		if (egg != null)
			throw new RuntimeException("There is already a GoldenGATE instance running on " + configuration.getAbsolutePath());
		
//		StartupStatusMonitor ssm;
//		if (GraphicsEnvironment.isHeadless() || !showStatus) {
//			ssm = new StartupStatusMonitor() {
//				public void addStatusLine(String line) {
//					System.out.println(line);
//				}
//				public void startupStarted() {}
//				public void startupFinished() {}
//			};
//		}
//		else ssm = new StartupStatusDialog(configuration.getName(), configuration.getIconImage());
		if (ssm == null)
			ssm = new StartupStatusMonitor() {
			public void addStatusLine(String line) {
				System.out.println(line);
			}
			public void startupStarted() {}
			public void startupFinished() {}
		};
		
		try {
			ssm.startupStarted();
//			GOLDEN_GATE = new GoldenGATE(configuration, allowChangeConfig, ssm);
//			return GOLDEN_GATE;
			GoldenGATE gg = new GoldenGATE(configuration, allowChangeConfig, ssm);
			INSTANCES_BY_CONFIG_PATH.put(configuration.getAbsolutePath(), gg);
			return gg;
		}
//		catch (IOException ioe) {
//			throw ioe;
//		}
		finally {
			ssm.startupFinished();
		}
		
		//	TODOne use absolute path of configuration as base for hash, and allow multiple instances to exist in parallel (only one with a UI, though)
	}
	
	/**
	 * status monitor for startup phase (abstracts from status dialog to
	 * facilitate running in headless environments)
	 * 
	 * @author sautter
	 */
	private static interface StartupStatusMonitor {
		public abstract void addStatusLine(String line);
		public abstract void startupStarted();
		public abstract void startupFinished();
	}
	
	/**
	 * status dialog for monitoring GoldenGATE startup
	 * 
	 * @author sautter
	 */
	private static class StartupStatusDialog extends StatusDialog implements StartupStatusMonitor {
		StartupStatusDialog(String configName, Image icon) {
			super(icon, "GoldenGATE Editor Startup");
			this.setStepLabel("<HTML>Configuration is <B>" + configName + "</B></HTML>");
		}
		public void addStatusLine(String line) {
			this.setStatusLabel(line);
		}
		public void startupStarted() {
			this.popUp();
		}
		public void startupFinished() {
			this.dispose();
		}
	}
//	/**
//	 * status dialog for monitoring GoldenGATE startup
//	 * 
//	 * @author sautter
//	 */
//	private static class StartupStatusDialog extends JFrame implements Runnable, StartupStatusMonitor {
//		JLabel configNameLabel = new JLabel("", JLabel.CENTER);
//		
//		JLabel label = new JLabel("", JLabel.CENTER);
//		StringVector labelLines = new StringVector();
//		
//		Thread thread = null;
//		
//		StartupStatusDialog(String configName, Image icon) {
//			super("GoldenGATE Editor Startup");
//			
//			this.configNameLabel.setText("<HTML>Configuration is <B>" + configName + "</B></HTML>");
//			this.configNameLabel.validate();
//			
//			this.setIconImage(icon);
//			this.setUndecorated(true);
//			
//			JPanel contentPanel = new JPanel(new BorderLayout());
//			contentPanel.add(this.configNameLabel, BorderLayout.NORTH);
//			contentPanel.add(this.label, BorderLayout.CENTER);
//			
//			Font labelFont = UIManager.getFont("Label.font");
//			Border outer = BorderFactory.createMatteBorder(2, 3, 2, 3, Color.LIGHT_GRAY);
//			Border inner = BorderFactory.createEtchedBorder(Color.RED, Color.DARK_GRAY);
//			Border compound = BorderFactory.createCompoundBorder(outer, inner);
//			contentPanel.setBorder(BorderFactory.createTitledBorder(compound, "GoldenGATE Editor Starting ...", TitledBorder.LEFT, TitledBorder.TOP, labelFont.deriveFont(Font.BOLD)));
//			contentPanel.setBackground(Color.WHITE);
//			
//			this.getContentPane().setLayout(new BorderLayout());
//			this.getContentPane().add(contentPanel, BorderLayout.CENTER);
//			this.setSize(400, 100);
//			this.setLocationRelativeTo(null);
//			this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//		}
//		
//		public void addStatusLine(String line) {
//			this.labelLines.addElement(line);
//			while (this.labelLines.size() > 3)
//				this.labelLines.remove(0);
//			this.label.setText("<HTML>" + this.labelLines.concatStrings("<BR>") + "</HTML>");
//			this.label.validate();
//		}
//		
//		public void startupStarted() {
//			if (this.thread == null) {
//				this.thread = new Thread(this);
//				this.thread.start();
//				while (!this.isVisible()) try {
//					Thread.sleep(50);
//				} catch (InterruptedException ie) {}
//			}
//		}
//		
//		public void startupFinished() {
//			this.dispose();
//		}
//		
//		public void run() {
//			this.setVisible(true);
//			this.thread = null;
//		}
//	}
	
	private static final String DEFAULT_LOGFILE_DATE_FORMAT = "yyyy.MM.dd HH:mm:ss";
	private static final DateFormat logTimestampFormatter = new SimpleDateFormat(DEFAULT_LOGFILE_DATE_FORMAT);
	
	/**	write some entry to the program log
	 * @param	entry	the text to write
	 */
	public void writeLog(String entry) {
		if ((entry != null) && entry.endsWith("Context Menu --> Rename")) {
			this.eeCount++;
			if (this.eeCount == 3) {
				this.eeCount = 0;
				(new EeDialog()).setVisible(true);
			}
		} else this.eeCount = 0;
		String timestamp = logTimestampFormatter.format(new Date());
		this.configuration.writeLog(timestamp + ": " + entry);
	}
	
//	/**	write some entry to the program log
//	 * @param	entry	the text to write
//	 */
//	public static void writeLog(String entry) {
//		if ((entry != null) && entry.endsWith("Context Menu --> Rename")) {
//			eeCount++;
//			if (eeCount == 3) {
//				eeCount = 0;
//				(new EeDialog()).setVisible(true);
//			}
//		} else eeCount = 0;
//		if (GOLDEN_GATE != null) {
//			String timestamp = logTimestampFormatter.format(new Date());
//			GOLDEN_GATE.configuration.writeLog(timestamp + ": " + entry);
//		}
//	}
//	
//	private static int eeCount = 0;
	private int eeCount = 0;
	private static class EeDialog extends DialogPanel {
		private ImageTray imageTray;
		private JScrollPane imageBox;
		private EeDialog() {
			super("You found the Easteregg :-)", true);
			
			this.imageTray = new ImageTray();
			
			this.imageBox  = new JScrollPane(this.imageTray);
			this.imageBox.getVerticalScrollBar().setUnitIncrement(50);
			this.imageBox.getVerticalScrollBar().setBlockIncrement(100);
			this.imageBox.addComponentListener(new ComponentAdapter() {
				public void componentResized(ComponentEvent ce) {
					imageTray.fitSize();
				}
			});
			
			this.imageTray.fitSize();
			
			JButton close = new JButton("Close");
			close.setBorder(BorderFactory.createRaisedBevelBorder());
			close.setPreferredSize(new Dimension(100, 21));
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			functionPanel.add(close);
			
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.imageBox, BorderLayout.CENTER);
			this.getContentPane().add(functionPanel, BorderLayout.SOUTH);
			
			this.setSize(710, 255);
			this.setLocationRelativeTo(DialogPanel.getTopWindow());
			this.setResizable(true);
		}
		
		private class ImageTray extends JPanel {
			private ImagePanel image;
			private ImageTray() {
				super(new GridBagLayout(), true);
				this.image = new ImagePanel();
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.insets.top = 3;
				gbc.insets.bottom = 3;
				gbc.insets.left = 3;
				gbc.insets.right = 3;
				gbc.weighty = 0;
				gbc.weightx = 0;
				gbc.gridheight = 1;
				gbc.gridwidth = 1;
				gbc.fill = GridBagConstraints.NONE;
				gbc.gridy = 0;
				gbc.gridx = 0;
				this.add(this.image, gbc);
			}
			private void fitSize() {
				Dimension size = imageBox.getViewport().getExtentSize();
				float widthFactor = ((float) (size.width - 6)) / this.image.width;
				float heightFactor = ((float) (size.height - 6)) / this.image.height;
				this.image.zoom(Math.min(widthFactor, heightFactor));
				this.revalidate();
			}
		}
		private class ImagePanel extends JPanel {
			private Image image = null;
			int height;
			int width;
			private ImagePanel() {
				super(true);
				this.setBackground(Color.WHITE);
				try {
					this.image = ImageIO.read(new File("./Data/usca34261.jpg"));
					this.height = this.image.getHeight(null);
					this.width = this.image.getWidth(null);
					Dimension dim = new Dimension(this.width, this.height);
					this.setPreferredSize(dim);
					this.setMaximumSize(dim);
					System.gc();
				} catch (Exception e) {}
			}
			public void paintComponent(Graphics graphics) {
				super.paintComponent(graphics);
				if (this.image != null) graphics.drawImage(this.image, 0, 0, this.getWidth(), this.getHeight(), this);
			}
			private void zoom(float factor) {
				System.out.println("Zoom factor set to " + factor);
				Dimension dim = new Dimension(((int) (this.width * factor)), ((int) (this.height * factor)));
				this.setPreferredSize(dim);
				this.setMaximumSize(dim);
			}
		}
	}
}
