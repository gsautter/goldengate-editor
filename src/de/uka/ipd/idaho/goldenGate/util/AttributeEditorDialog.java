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
package de.uka.ipd.idaho.goldenGate.util;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.Attributed;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.Token;
import de.uka.ipd.idaho.gamta.TokenSequence;
import de.uka.ipd.idaho.gamta.util.swing.AttributeEditor;

/**
 * A small editor dialog for the attributes of Attributed objects, basically
 * for Annotations and Tokens.
 * 
 * @author sautter
 */
public class AttributeEditorDialog extends DialogPanel {
	
	private AttributeEditor attributeEditor;
	private boolean dirty = false;
	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(String title, Annotation annotation, QueriableAnnotation context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Annotation annotation, QueriableAnnotation context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Annotation annotation, QueriableAnnotation context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(String title, Token token, TokenSequence context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Token token, TokenSequence context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Token token, TokenSequence context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	private void init() {
		
		//	initialize buttons
		JButton commitButton = new JButton("OK");
		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		commitButton.setPreferredSize(new Dimension(80, 21));
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				dirty = attributeEditor.writeChanges();
				AttributeEditorDialog.this.dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
		cancelButton.setPreferredSize(new Dimension(80, 21));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				AttributeEditorDialog.this.dispose();
			}
		});
		
		JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainButtonPanel.add(commitButton);
		mainButtonPanel.add(cancelButton);
		
		//	put the whole stuff together
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.attributeEditor, BorderLayout.CENTER);
		this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
		
		//	configure window
		this.setResizable(true);
	}
	
	/**	@return	true if and only if something was changed
	 */
	public boolean isDirty() {
		return this.dirty;
	}	
}