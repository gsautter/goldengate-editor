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
package de.uka.ipd.idaho.goldenGate.plugins;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;
import de.uka.ipd.idaho.goldenGate.GoldenGATE;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;
import de.uka.ipd.idaho.goldenGate.GoldenGateGUI;

/**
 * Standard GUI main JFrame for the GoldenGATE editor
 * 
 * @author sautter
 */
public class DefaultGoldenGateGUI extends JFrame implements GoldenGateGUI {
	
	private static final String START_X_NAME = "START_X";
	private static final String START_Y_NAME = "START_Y";
	
	private static final String START_WIDTH_NAME = "START_WIDTH";
	private static final String START_HEIGHT_NAME = "START_HEIGHT";
	
	private static final float START_SIZE_FACTOR = 0.8f;
	
	private int startX = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * ((1 - START_SIZE_FACTOR) / 2));
	private int startY = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * ((1 - START_SIZE_FACTOR) / 2));
	
	private int startWidth = (int) (Toolkit.getDefaultToolkit().getScreenSize().width * START_SIZE_FACTOR);
	private int startHeight = (int) (Toolkit.getDefaultToolkit().getScreenSize().height * START_SIZE_FACTOR);
	
	private GoldenGATE parent;
	private boolean exiting = false;
	
	private JMenuBar mainMenu = new JMenuBar();
	private JMenu fileMenu = null;
	private JMenu viewMenu = null;
	private JMenu editMenu = null;
	private JMenu toolsMenu = null;
	private JMenu windowMenu = null;
	private JMenu helpMenu = null;
	
	private boolean showPluginMenus = true;
	
	private JMenu[] pluginMenus = new JMenu[0];
	private JMenuItem[] documentDependentMenuItems = new JMenuItem[0];
	
	private JTabbedPane editorTabs = new JTabbedPane();
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#canOpenDocument()
	 */
	public boolean canOpenDocument() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#openDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor)
	 */
	public void openDocument(DocumentEditor document) {
		this.editorTabs.addTab(document.getTitle(), null, document, document.getTooltipText());
		this.editorTabs.setSelectedComponent(document);
		this.setMenusEnabled(true);
		this.validate();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#getOpenDocumentCount()
	 */
	public int getOpenDocumentCount() {
		return this.editorTabs.getTabCount();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#documentTitleChanged(de.uka.ipd.idaho.goldenGate.DocumentEditor)
	 */
	public void documentTitleChanged(DocumentEditor document) {
		int index = this.editorTabs.indexOfComponent(document);
		if (index != -1) this.editorTabs.setTitleAt(index, document.getTitle());
		this.validate();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#closeDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor)
	 */
	public void closeDocument(DocumentEditor document) {
		this.editorTabs.remove(document);
		this.setMenusEnabled(this.getOpenDocumentCount() != 0);
	}
	
	//	assemble main menu
	private void layoutMainMenu() {
		this.mainMenu.removeAll();
		
		this.mainMenu.add(this.fileMenu);
		this.mainMenu.add(this.viewMenu);
		this.mainMenu.add(this.editMenu);
		
		//	add Tools menu if not empty
		if (this.toolsMenu.getMenuComponentCount() != 0)
			this.mainMenu.add(this.toolsMenu);
		
		//	add plugin menus if required
		JMenu pluginMenu = new JMenu("Plugins");
		if (this.showPluginMenus) {
			for (int m = 0; m < this.pluginMenus.length; m++)
				if (this.pluginMenus[m].getMenuComponentCount() != 0)
					pluginMenu.add(this.pluginMenus[m]);
			if (pluginMenu.getItemCount() != 0)
				this.mainMenu.add(pluginMenu);
		}
		
		//	add window menu
		if (this.windowMenu != null)
			this.mainMenu.add(this.windowMenu);
		
		this.mainMenu.add(this.helpMenu);
		
		this.mainMenu.validate();
	}
	
	private void setMenusEnabled(boolean enabled) {
		this.viewMenu.setEnabled(enabled);
		this.editMenu.setEnabled(enabled);
		this.toolsMenu.setEnabled(enabled);
		for (int i = 0; i < this.documentDependentMenuItems.length; i++)
			this.documentDependentMenuItems[i].setEnabled(enabled);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#setParent(de.uka.ipd.idaho.goldenGate.GoldenGATE)
	 */
	public void setParent(GoldenGATE parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#init(de.easyIO.settings.Settings)
	 */
	public void init(Settings settings) {
		
		//	set window title
		this.setTitle(DEFAULT_WINDOW_TITLE + " - " + this.parent.getConfigurationName());
		
		//	set icon
		this.setIconImage(this.parent.getGoldenGateIcon());
		
		//	take control of window
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (!exiting) {
					exiting = true;
					parent.exitShutdown();
				}
			}
			public void windowClosed(WindowEvent we) {
				if (!exiting) {
					exiting = true;
					parent.exitShutdown();
				}
			}
		});
		
		ArrayList documentDependentMenuItemList = new ArrayList();
		
		this.fileMenu = this.parent.getFileMenu(this, true, this.parent.allowChangeConfiguration(), documentDependentMenuItemList);
		
		this.viewMenu = this.parent.getViewMenu(this, true);
		
		this.editMenu = new JMenu("Edit");
		JMenuItem[] editMenuItems = DocumentEditor.getEditMenuItems(this, true);
		for (int m = 0; m < editMenuItems.length; m++) {
			if (editMenuItems[m] == GoldenGateConstants.MENU_SEPARATOR_ITEM)
				this.editMenu.addSeparator();
			else this.editMenu.add(editMenuItems[m]);
		}
		
		this.toolsMenu = this.parent.getToolsMenu(this);
		
		this.helpMenu = this.parent.getHelpMenu();
		
		this.pluginMenus = this.parent.getPluginMenus(this, documentDependentMenuItemList);
		
		this.windowMenu = this.parent.getWindowMenu();
		if (this.windowMenu == null)
			this.showPluginMenus = false;
		else {
			final JCheckBoxMenuItem cmi = new JCheckBoxMenuItem("Plugin Menus");
			cmi.setSelected(this.showPluginMenus);
			cmi.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					showPluginMenus = cmi.isSelected();
					layoutMainMenu();
				}
			});
			this.windowMenu.add(cmi, 0);
		}
		
		this.documentDependentMenuItems = ((JMenuItem[]) documentDependentMenuItemList.toArray(new JMenuItem[documentDependentMenuItemList.size()]));
		
		//	layout menus
		this.layoutMainMenu();
		this.setMenusEnabled(false);
		
		//	load window position & size
		try {
			this.startWidth = Integer.parseInt(settings.getSetting(START_WIDTH_NAME, ("" + startWidth)));
		} catch (Exception e) {}
		try {
			this.startHeight = Integer.parseInt(settings.getSetting(START_HEIGHT_NAME, ("" + startHeight)));
		} catch (Exception e) {}
		
		try {
			this.startX = Integer.parseInt(settings.getSetting(START_X_NAME, ("" + this.startX)));
		} catch (Exception e) {}
		try {
			this.startY = Integer.parseInt(settings.getSetting(START_Y_NAME, ("" + this.startY)));
		} catch (Exception e) {}
		
		//	check if window fits screen
		Rectangle screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getBounds();
		
		//	check location
		if (this.startX < screen.x) this.startX = screen.x;
		else if (this.startX > (screen.x + screen.width)) this.startX = screen.x;
		if (this.startY < screen.y) this.startY = screen.y;
		else if (this.startY > (screen.y + screen.height)) this.startY = screen.y;
		
		//	check size
		if ((this.startX + this.startWidth) > screen.width) {
			if (this.startWidth < screen.width) this.startX = screen.x + ((screen.width - this.startWidth) / 2);
			else {
				this.startX = screen.x;
				this.startWidth = screen.width;
			}
		}
		if ((this.startY + this.startHeight) > screen.height) {
			if (this.startHeight < screen.height) this.startY = screen.y + ((screen.height - this.startHeight) / 2);
			else {
				this.startY = screen.y;
				this.startHeight = screen.height;
			}
		}
		
		
		//	build drop traget
		DropTarget dropTarget = new DropTarget(this.editorTabs, new DropTargetAdapter() {
			public void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(dtde.getDropAction());
				Transferable transfer = dtde.getTransferable();
				DataFlavor[] dataFlavors = transfer.getTransferDataFlavors();
				for (int d = 0; d < dataFlavors.length; d++) {
					System.out.println(dataFlavors[d].toString());
					System.out.println(dataFlavors[d].getRepresentationClass());
					try {
						Object transferData = transfer.getTransferData(dataFlavors[d]);
						System.out.println(transferData.getClass().getName());
						
						List transferList = ((List) transferData);
						if (transferList.isEmpty()) return;
						
						for (int t = 0; t < transferList.size(); t++) {
							File droppedFile = ((File) transferList.get(t));
							try {
								String fileName = droppedFile.getName();
								String dataType;
								if ((fileName.indexOf('.') == -1) || fileName.endsWith(".")) dataType = "xml";
								else dataType = fileName.substring(fileName.lastIndexOf('.') + 1);
								
								DocumentFormat format = DefaultGoldenGateGUI.this.parent.getDocumentFormatForFileExtension(dataType);
								if (format == null) {
									String formatName = DefaultGoldenGateGUI.this.parent.selectLoadFormat();
									if (formatName != null)
										format = DefaultGoldenGateGUI.this.parent.getDocumentFormatForName(formatName);
								}
								
								if (format == null) JOptionPane.showMessageDialog(DefaultGoldenGateGUI.this, ("GoldenGATE Editor cannot open the dropped file, sorry,\nthe data format in '" + droppedFile.getName() + "' is unknown."), "Unknown Document Format", JOptionPane.INFORMATION_MESSAGE);
								else {
									System.out.println("GoldenGateGUI: opening dropped file as '" + format.getDefaultSaveFileExtension() + "' (" + format.getDescription() + ") via " + format.getClass().getName());
									InputStream source = new FileInputStream(droppedFile);
									MutableAnnotation doc = format.loadDocument(source);
									source.close();
									if (doc != null)
										DefaultGoldenGateGUI.this.parent.openDocument(doc, fileName, format);
								}
							}
							catch (IOException ioe) {
								System.out.println("Error opening document '" + droppedFile.getAbsolutePath() + "':\n   " + ioe.getClass().getName() + " (" + ioe.getMessage() + ")");
								ioe.printStackTrace(System.out);
								JOptionPane.showMessageDialog(DefaultGoldenGateGUI.this, ("Could not open file '" + droppedFile.getAbsolutePath() + "':\n" + ioe.getMessage()), "Error Opening File", JOptionPane.ERROR_MESSAGE);
							}
							catch (SecurityException se) {
								System.out.println("Error opening document '" + droppedFile.getName() + "':\n   " + se.getClass().getName() + " (" + se.getMessage() + ")");
								se.printStackTrace(System.out);
								JOptionPane.showMessageDialog(DefaultGoldenGateGUI.this, ("Not allowed to open file '" + droppedFile.getName() + "':\n" + se.getMessage() + "\n\nIf you are currently running GoldenGATE Editor as an applet, your\nbrowser's security mechanisms might prevent reading files from your local disc."), "Not Allowed To Open File", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}
					catch (UnsupportedFlavorException ufe) {
						ufe.printStackTrace(System.out);
					}
					catch (IOException ioe) {
						ioe.printStackTrace(System.out);
					}
					catch (Exception e) {
						e.printStackTrace(System.out);
					}
				}
			}
		});
		dropTarget.setActive(true);
		
		
		//	add content to frame
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.mainMenu, BorderLayout.NORTH);
		this.getContentPane().add(this.editorTabs, BorderLayout.CENTER);
		
		//	make frame behave
		this.setResizable(true);
		this.setSize(new Dimension(this.startWidth, this.startHeight));
		this.setLocation(this.startX, this.startY);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#storeSettings(de.easyIO.settings.Settings)
	 */
	public void storeSettings(Settings settings) {
		
		//	get current editor window size and position
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension ownSize = this.getSize();
		if ((ownSize.width < screenSize.width) || (ownSize.height < screenSize.height)) {
			settings.setSetting(START_WIDTH_NAME, ("" + ownSize.width));
			settings.setSetting(START_HEIGHT_NAME, ("" + ownSize.height));
			settings.setSetting(START_X_NAME, ("" + this.getLocation().x));
			settings.setSetting(START_Y_NAME, ("" + this.getLocation().y));
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#exit()
	 */
	public void exit() {
		//	nothing to do here
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#open()
	 */
	public void open() {
		this.setVisible(true);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateGUI#close()
	 */
	public void close() {
		this.exiting = true;
		this.dispose();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConstants.InvokationTargetProvider#getFunctionTarget()
	 */
	public DocumentEditor getFunctionTarget() {
		Component selected = this.editorTabs.getSelectedComponent();
		return (((selected != null) && (selected instanceof DocumentEditor)) ? ((DocumentEditor) selected) : null);
	}
}
