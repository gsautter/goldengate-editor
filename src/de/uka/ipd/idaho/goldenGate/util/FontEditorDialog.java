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
package de.uka.ipd.idaho.goldenGate.util;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * dialog for editing the font style, size, and color of some text component 
 * 
 * @author sautter
 */
public class FontEditorDialog extends JDialog {
	
	private FontEditorPanel font;
	
	private boolean committed = false;
	
	public FontEditorDialog(JDialog owner, String textFontName, int textFontSize, Color textFontColor) {
		super(owner, "Edit Fonts", true);
		this.init(textFontName, textFontSize, textFontColor);
	}
	
	public FontEditorDialog(JFrame owner, String textFontName, int textFontSize, Color textFontColor) {
		super(owner, "Edit Fonts", true);
		this.init(textFontName, textFontSize, textFontColor);
	}
	
	private void init(String textFontName, int textFontSize, Color textFontColor) {
		
		//	initialize main buttons
		JButton commitButton = new JButton("OK");
		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		commitButton.setPreferredSize(new Dimension(100, 25));
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				committed = true;
				dispose();
			}
		});
		
		JButton abortButton = new JButton("Cancel");
		abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
		abortButton.setPreferredSize(new Dimension(100, 25));
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
		JPanel mainButtonPanel = new JPanel();
		mainButtonPanel.setLayout(new FlowLayout());
		mainButtonPanel.add(commitButton);
		mainButtonPanel.add(abortButton);
		
		//	initialize editors
		this.font = new FontEditorPanel("Editor Font", textFontName, textFontSize, textFontColor);
		
		//	put the whole stuff together
		this.getContentPane().setLayout(new GridLayout(0, 1));
		this.getContentPane().add(this.font);
		this.getContentPane().add(mainButtonPanel);
		
		this.setResizable(false);
		this.setSize(new Dimension(500, 120));
	}
	
	/**	@return	true if and only if the dialog was committed
	 */
	public boolean isCommitted() {
		return this.committed;
	}
	
	/**	@return	the FopntEditorPanel holding the font data
	 */
	public FontEditorPanel getFontEditor() {
		return this.font;
	}
}
