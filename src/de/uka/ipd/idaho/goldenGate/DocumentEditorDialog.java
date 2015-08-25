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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants.InvokationTargetProvider;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;

/**
 * DocumentEditDialog is a convenience class representing a dialog for editing a
 * document or a part of it. It offers the same editing menus as the GoldenGATE
 * main window. In order for editing having any effect on the document,
 * writeChanges() has to be invoked. This is best done by overwriting the
 * dispose() method accordingly.
 * 
 * @author sautter
 */
public abstract class DocumentEditorDialog extends DialogPanel {
	
	protected GoldenGATE host;
	protected DocumentEditor parent;
	
	protected JPanel mainButtonPanel = new JPanel(new FlowLayout());
	
	protected MutableAnnotation content;
	protected DocumentEditor documentEditor;
	
	protected JMenuBar mainMenu = new JMenuBar();
	protected JMenu fileMenu = new JMenu("File");
	protected JMenu viewMenu = new JMenu("View");
	protected JMenu editMenu = new JMenu("Edit");
	protected JMenu toolsMenu = new JMenu("Tools");
	
	/**
	 * Constructor
	 * @param host the GoldenGATE main window
	 * @param title the titel for the dialog
	 * @param content the MutableAnnotation to display for editing
	 */
	public DocumentEditorDialog(GoldenGATE host, String title, MutableAnnotation content) {
		this(host, null, title, content);
	}
//	
//	/**
//	 * Constructor
//	 * @param owner the JFrame this DocumentEditorDialog is modal to
//	 * @param host the GoldenGATE main window
//	 * @param title the titel for the dialog
//	 * @param content the MutableAnnotation to display for editing
//	 */
//	public DocumentEditorDialog(JFrame owner, GoldenGATE host, String title, MutableAnnotation content) {
//		this(owner, host, null, title, content);
//	}
//	
//	/**
//	 * Constructor
//	 * @param owner the JDialog this DocumentEditorDialog is modal to
//	 * @param host the GoldenGATE main window
//	 * @param title the titel for the dialog
//	 * @param content the MutableAnnotation to display for editing
//	 */
//	public DocumentEditorDialog(JDialog owner, GoldenGATE host, String title, MutableAnnotation content) {
//		this(owner, host, null, title, content);
//	}
	
	/**
	 * Constructor
	 * @param owner the window this DocumentEditorDialog is modal to
	 * @param host the GoldenGATE main window
	 * @param title the titel for the dialog
	 * @param content the MutableAnnotation to display for editing
	 */
	public DocumentEditorDialog(Window owner, GoldenGATE host, String title, MutableAnnotation content) {
		this(owner, host, null, title, content);
	}
	
	/**
	 * Constructor
	 * @param host the GoldenGATE main window
	 * @param parent the DocumentEditorPanel to use as a template for the layout
	 * @param title the titel for the dialog
	 * @param content the MutableAnnotation to display for editing
	 */
	public DocumentEditorDialog(GoldenGATE host, DocumentEditor parent, String title, MutableAnnotation content) {
		super(title, true);
		this.host = host;
		this.parent = parent;
		this.content = content;
		this.init();
	}
//	
//	/**
//	 * Constructor
//	 * @param owner the JFrame this DocumentEditorDialog is modal to
//	 * @param host the GoldenGATE main window
//	 * @param parent the DocumentEditorPanel to use as a template for the layout
//	 * @param title the titel for the dialog
//	 * @param content the MutableAnnotation to display for editing
//	 */
//	public DocumentEditorDialog(JFrame owner, GoldenGATE host, DocumentEditor parent, String title, MutableAnnotation content) {
//		super(owner, title, true);
//		this.host = host;
//		this.parent = parent;
//		this.content = content;
//		this.init();
//	}
//	
//	/**
//	 * Constructor
//	 * @param owner the JDialog this DocumentEditorDialog is modal to
//	 * @param host the GoldenGATE main window
//	 * @param parent the DocumentEditorPanel to use as a template for the layout
//	 * @param title the titel for the dialog
//	 * @param content the MutableAnnotation to display for editing
//	 */
//	public DocumentEditorDialog(JDialog owner, GoldenGATE host, DocumentEditor parent, String title, MutableAnnotation content) {
//		super(owner, title, true);
//		this.host = host;
//		this.parent = parent;
//		this.content = content;
//		this.init();
//	}
	
	/**
	 * Constructor
	 * @param owner the window this DocumentEditorDialog is modal to
	 * @param host the GoldenGATE main window
	 * @param parent the DocumentEditorPanel to use as a template for the layout
	 * @param title the titel for the dialog
	 * @param content the MutableAnnotation to display for editing
	 */
	public DocumentEditorDialog(Window owner, GoldenGATE host, DocumentEditor parent, String title, MutableAnnotation content) {
		super(owner, title, true);
		this.host = host;
		this.parent = parent;
		this.content = content;
		this.init();
	}
	
	private void init() {
		
		//	initialize main editor
//		this.documentEditor = new DocumentEditor(this.host, this.content, this.parent, ((this.parent == null) ?  this.getTitle() : this.parent.getTitle()), null);
		this.documentEditor = new DocumentEditor(this.host, this.parent);
		
		//	create invokation target provider
		InvokationTargetProvider targetProvider = new InvokationTargetProvider() {
			public DocumentEditor getFunctionTarget() {
				return documentEditor;
			}
		};
		
		//	create file menu
		this.host.buildFileMenu(this.fileMenu, targetProvider);
		
		//	create view menu
		this.host.buildViewMenu(this.viewMenu, targetProvider, false);
		
		//	create edit menu
		JMenuItem[] editMenuItems = this.documentEditor.getEditMenuItems(false);
		for (int m = 0; m < editMenuItems.length; m++) {
			if (editMenuItems[m] == GoldenGateConstants.MENU_SEPARATOR_ITEM)
				this.editMenu.addSeparator();
			else this.editMenu.add(editMenuItems[m]);
		}
		
		//	create tools menu
		this.host.buildToolsMenu(this.toolsMenu, targetProvider);
		
		//	assemble main menu
		this.mainMenu.add(this.fileMenu);
		this.mainMenu.add(this.viewMenu);
		this.mainMenu.add(this.editMenu);
		this.mainMenu.add(this.toolsMenu);
		
		//	show document
		this.documentEditor.setContent(this.content, ((this.parent == null) ?  this.getTitle() : this.parent.getTitle()), null, null);
		
		//	format editor dialog
		this.add(this.mainMenu, BorderLayout.NORTH);
		this.add(this.documentEditor, BorderLayout.CENTER);
		this.add(this.mainButtonPanel, BorderLayout.SOUTH);
		this.setResizable(true);
		this.setSize(new Dimension(800, 600));
	}
	
	/**
	 * @return true if and only if the document displayed in this
	 *         DocumentEditorDialog has been modified
	 */
	public boolean isContentModified() {
		return this.documentEditor.isContentModified();
	}
	
	/**
	 * Write the changes made to the content mutable annotation through to the
	 * backing document. This method simply loops through to the writeChanges()
	 * method of the nested DocumentEditor.
	 */
	protected void writeChanges() {
		this.documentEditor.writeChanges();
	}
	
	/**
	 * Set the content of this document editor dialog. If the specified
	 * MutableAnnotation is a DocumentRoot, all changes made to it will apply
	 * directly. If not, hanges apply to the specified MutableAnnotation only
	 * after writeChanges() has been invoked.
	 * @param content the mutable annotation to edit in this annotation editor
	 *            dialog
	 */
	public void setContent(MutableAnnotation content) {
		this.documentEditor.setContent(content);
	}
}
