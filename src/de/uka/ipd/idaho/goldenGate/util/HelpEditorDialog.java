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
 *     * Neither the name of the Universität Karlsruhe (TH) / KIT nor the
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
package de.uka.ipd.idaho.goldenGate.util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * As an editor widget for HTML empowered help texts, this class combines a
 * plain text text editing area for raw HTML input with an HTML rendered
 * preview that shows what the help page will look like for users. This widget
 * is intended for use in resource managers that wish to provide HTML based
 * help pages for the resources they provide.
 * 
 * @author sautter
 */
public class HelpEditorDialog extends DialogPanel {
	private JTextArea editor = new JTextArea();
	private JEditorPane preview = new JEditorPane();
	
	/** Constructor
	 * @param title the dialog title
	 */
	public HelpEditorDialog(String title) {
		this(null, title, null);
	}
	
	/** Constructor
	 * @param title the dialog title
	 * @param helpText the initial help text to be edited
	 */
	public HelpEditorDialog(String title, String helpText) {
		this(null, title, helpText);
	}
	
	/** Constructor
	 * @param owner the window owning this dialog
	 * @param title the dialog title
	 */
	public HelpEditorDialog(Window owner, String title) {
		this(owner, title, null);
	}
	
	/** Constructor
	 * @param owner the window owning this dialog
	 * @param title the dialog title
	 * @param helpText the initial help text to be edited
	 */
	public HelpEditorDialog(Window owner, String title, String helpText) {
		super(owner, title, true);
		
		//	initialize main buttons
		JButton commitButton = new JButton("OK");
		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		commitButton.setPreferredSize(new Dimension(100, 21));
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dispose();
			}
		});
		
		JButton abortButton = new JButton("Cancel");
		abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
		abortButton.setPreferredSize(new Dimension(100, 21));
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HelpEditorDialog.this.editor = null;
				dispose();
			}
		});
		
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new FlowLayout());
		mainButtonPanel.add(commitButton);
		mainButtonPanel.add(abortButton);
		
		//	initialize editors
		this.editor.setText((helpText == null) ? "" : helpText);
		
		//	make editor scrollable
		final JScrollPane editorBox = new JScrollPane(this.editor);
		editorBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		editorBox.getVerticalScrollBar().setUnitIncrement(25);
		editorBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		editorBox.getHorizontalScrollBar().setUnitIncrement(25);
		final JScrollPane previewBox = new JScrollPane(this.preview);
		previewBox.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		previewBox.getVerticalScrollBar().setUnitIncrement(25);
		
		//	put editor in tabs
		final JTabbedPane tabs = new JTabbedPane();
		tabs.addTab("Editor", editorBox);
		tabs.addTab("Preview", previewBox);
		tabs.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent ce) {
				if (tabs.getSelectedComponent() == previewBox) {
					String ht = editor.getText();
					if ((ht.length() > 6) && ("<html>".equals(ht.substring(0, 6).toLowerCase()) || "<html ".equals(ht.substring(0, 6).toLowerCase())))
						preview.setContentType("text/html");
					else preview.setContentType("text/plain");
					preview.setText(ht);
				}
			}
		});
		
		//	put the whole stuff together
		this.setLayout(new BorderLayout());
		this.add(tabs, BorderLayout.CENTER);
		this.add(mainButtonPanel, BorderLayout.SOUTH);
		
		this.setResizable(true);
		this.setSize(new Dimension(600, 400));
		this.setLocationRelativeTo(this.getOwner());
	}
	
	/**
	 * Test if the editing dialog was committed, i.e., closed via the 'OK' button.
	 * @return true if the dialog was committed, false otherwise
	 */
	public boolean isCommitted() {
		return (this.editor != null);
	}
	
	/**
	 * Retrieve the help text committed with the editing dialog, i.e., the last
	 * status of the text before the 'OK' button was clicked. If the dialog was
	 * closed in another way than the 'OK' button, this method returns null.
	 * @return the help text
	 */
	public String getHelpText() {
		return ((this.editor == null) ? null : this.editor.getText().trim());
	}
}