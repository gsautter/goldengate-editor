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


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

/**
 * @author sautter
 *
 * TODO document this class
 */
public class EditFontsButton extends JButton {
	
	private final FontEditable parentPanel;
	
	/** Constructor
	 * @param	parent	the GontEditable to modify through this button
	 */
	public EditFontsButton(FontEditable parent) {
		this(parent, null, null, null);
	}
	
	/** Constructor
	 * @param	parent	the GontEditable to modify through this button
	 * @param	text	the button text
	 */
	public EditFontsButton(FontEditable parent, String text) {
		this(parent, text, null, null);
	}
	
	/** Constructor
	 * @param	parent		the GontEditable to modify through this button
	 * @param	dimension	the size of the button
	 */
	public EditFontsButton(FontEditable parent, Dimension dimension) {
		this(parent, null, dimension, null);
	}
	
	/** Constructor
	 * @param	parent	the GontEditable to modify through this button
	 * @param	border	the border for the button
	 */
	public EditFontsButton(FontEditable parent, Border border) {
		this(parent, null, null, border);
	}
	
	/** Constructor
	 * @param	parent		the GontEditable to modify through this button
	 * @param	text		the button text
	 * @param	dimension	the size of the button
	 * @param	border		the border for the button
	 */
	public EditFontsButton(FontEditable parent, String text, Dimension dimension, Border border) {
		super((text == null) ? "Edit Fonts" : text);
		this.parentPanel = parent;
		this.setPreferredSize((dimension == null) ? (new Dimension(150, 21)) : dimension);
		this.setBorder((border == null) ? BorderFactory.createRaisedBevelBorder() : border);
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				parentPanel.editFonts();
			}
		});
	}
}
