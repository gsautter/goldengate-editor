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
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author sautter
 *
 * TODO document this class
 */
public class FontEditorPanel extends JPanel {
	
	private static final String[] FONT_NAMES = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	private static final String[] FONT_SIZE_STRINGS = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
	
	private String fontName;
	private int fontSize;
	private Color fontColor;
	
	private JComboBox fontNameChooser = new JComboBox(FONT_NAMES);
	private JComboBox fontSizeChooser = new JComboBox(FONT_SIZE_STRINGS);
	
	private boolean dirty = false;
	
	public FontEditorPanel(String title, String selectedFontName, int selectedFontSize, Color selectedColor) {
		super(new FlowLayout());
		
		this.fontName = selectedFontName;
		this.fontSize = selectedFontSize;
		this.fontColor = selectedColor;
		
		this.add(new JLabel(title, JLabel.LEFT));
		
		this.fontNameChooser.setBorder(BorderFactory.createLoweredBevelBorder());
		this.fontNameChooser.setMaximumRowCount(10);
		this.fontNameChooser.setSelectedItem(selectedFontName);
		this.fontNameChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					fontName = fontNameChooser.getSelectedItem().toString();
					dirty = true;
				} catch (Exception ex) {
					fontNameChooser.setSelectedItem(getFontName());
				}
			}
		});
		this.add(fontNameChooser);
		
		this.fontSizeChooser.setBorder(BorderFactory.createLoweredBevelBorder());
		this.fontSizeChooser.setMaximumRowCount(10);
		this.fontSizeChooser.setSelectedItem("" + selectedFontSize);
		this.fontSizeChooser.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				try {
					fontSize = Integer.parseInt(fontSizeChooser.getSelectedItem().toString());
					dirty = true;
				} catch (Exception ex) {
					fontSizeChooser.setSelectedItem("" + fontSize);
				}
			}
		});
		this.add(fontSizeChooser);
		
		JButton colorButton = new JButton("Change Color");
		colorButton.setBorder(BorderFactory.createRaisedBevelBorder());
		colorButton.setPreferredSize(new Dimension(100, 21));
		colorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeColor();
			}
		});
		this.add(colorButton);
	}
	
	private void changeColor() {
		Color newColor = JColorChooser.showDialog(this, "Change Font Color", this.fontColor);
		if (newColor != null) {
			fontColor = newColor;
			this.dirty = true;
		}
	}
	
	/**	@return the name of the selected font
	 */
	public String getFontName() {
		return this.fontName;
	}
	
	/**	@return the selected font size
	 */
	public int getFontSize() {
		return this.fontSize;
	}
	
	/**	@return the selected font color
	 */
	public Color getFontColor() {
		return this.fontColor;
	}
	
	/**	@return true if and only if at least one of font name, size, or color was changed
	 */
	public boolean isDirty() {
		return this.dirty;
	}
}
