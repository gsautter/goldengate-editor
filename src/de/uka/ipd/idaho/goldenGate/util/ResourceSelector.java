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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;


/**
 * A ResourceSelector is a small widget that allows for selecting a resource
 * from its parent resource manager. In particular, it's a JButton that displays
 * the name of the selected resource, or an explanation label, if no resource is
 * selected.
 * 
 * @author sautter
 */
public class ResourceSelector extends JPanel {
	
	private final String NONE;
	
	private ResourceManager parent;
	
	private JButton selectButton;
	
	private String selectedName;
	private final String initialSelection;
	private String selectorTitle;
	
	/**
	 * Constructor
	 * @param parent the DocumentProcessorManager whose DocumentProcessors are
	 *            selectable by this panel
	 * @param labelString the String to use as the lable for this selector panel
	 *            (will also be used as the title for the selector dialog)
	 */
	public ResourceSelector(ResourceManager parent, String labelString) {
		this(parent, null, labelString);
	}

	/**
	 * Constructor
	 * @param parent the DocumentProcessorManager whose DocumentProcessors are
	 *            selectable by this panel
	 * @param initialSelection the name of the initially selected
	 *            DocumentProcessor
	 * @param labelString the String to use as the lable for this selector panel
	 *            (will also be used as the title for the selector dialog)
	 */
	public ResourceSelector(ResourceManager parent, String initialSelection, String labelString) {
		this(parent, initialSelection, labelString, labelString);
	}

	/**
	 * Constructor
	 * @param parent the DocumentProcessorManager whose DocumentProcessors are
	 *            selectable by this panel
	 * @param initialSelection the name of the initially selected
	 *            DocumentProcessor
	 * @param labelString the String to use as the lable for this selector panel
	 * @param selectorTitle the title for the selector dialog
	 */
	public ResourceSelector(ResourceManager parent, String initialSelection, String labelString, String selectorTitle) {
		super(new GridBagLayout(), true);
		this.parent = parent;
		this.initialSelection = initialSelection;
		this.selectedName = initialSelection;
		this.selectorTitle = selectorTitle;
		this.NONE = ("<No " + this.parent.getResourceTypeLabel() + ">");
		
		JLabel label = new JLabel(labelString);
		label.setPreferredSize(new Dimension(100, 25));
		
		this.selectButton = new JButton((this.selectedName == null) ? ("<Select a " + this.parent.getResourceTypeLabel() + ">") : this.selectedName);
		this.selectButton.setBorder(BorderFactory.createRaisedBevelBorder());
		this.selectButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openSelector();
			}
		});
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets.top = 2;
		gbc.insets.bottom = 2;
		gbc.insets.left = 4;
		gbc.insets.right = 4;
		gbc.weighty = 0;
		gbc.weightx = 1;
		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		gbc.gridx = 0;
		this.add(label, gbc.clone());
		gbc.gridx = 1;
		this.add(this.selectButton, gbc.clone());
	}
	
	/**
	 * opens a selector dialog to change the selection
	 */
	private void openSelector() {
		String[] resourceNames = this.parent.getResourceNames();
		if (resourceNames != null) {
			String[] noneResourceNames = new String[resourceNames.length + 1];
			System.arraycopy(resourceNames, 0, noneResourceNames, 0, resourceNames.length);
			noneResourceNames[resourceNames.length] = this.NONE;
			
			ResourceDialog rd = ResourceDialog.getResourceDialog(noneResourceNames, this.selectorTitle, "Select");
			rd.setVisible(true);
			if (rd.isCommitted()) {
				String selectedResourceName = rd.getSelectedResourceName();
				this.selectedName = (this.NONE.equals(selectedResourceName) ? null : selectedResourceName);
				this.selectButton.setText((this.selectedName == null) ? ("<Select a " + this.parent.getResourceTypeLabel() + ">") : this.selectedName);
			}
		}
	}

	/**
	 * @return the name of the selected DocumentProcessor, or null if none was
	 *         selected
	 */
	public String getSelectedResourceName() {
		return this.selectedName;
	}

	/**
	 * @return true if and only id the selection was changed
	 */
	public boolean isDirty() {
		return ((this.initialSelection == null) ? (this.selectedName != null) : !this.initialSelection.equals(this.selectedName));
	}
}
