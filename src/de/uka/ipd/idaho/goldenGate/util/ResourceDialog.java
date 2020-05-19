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
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;

/**
 * Dialog for selecting a specific resource from some resource manager. The
 * dialog can be closed either via one of the two buttons ('Select' and
 * 'Cancel'), or with a double click in the resource list, which counts as a
 * click on the 'Select' button.
 * 
 * @author sautter
 */
public class ResourceDialog extends JDialog {
	
	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 */
	public static ResourceDialog getResourceDialog(ResourceManager parent, String title) {
		return getResourceDialog(null, parent, title, null);
	}

	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 */
	public static ResourceDialog getResourceDialog(String[] resourceNames, String title) {
		return getResourceDialog(null, resourceNames, title, null);
	}

	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 */
	public static ResourceDialog getResourceDialog(ResourceManager parent, String title, String commitButtonLabel) {
		return getResourceDialog(null, parent, title, commitButtonLabel, null);
	}

	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 */
	public static ResourceDialog getResourceDialog(String[] resourceNames, String title, String commitButtonLabel) {
		return getResourceDialog(null, resourceNames, title, commitButtonLabel, null);
	}

	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	public static ResourceDialog getResourceDialog(ResourceManager parent, String title, String commitButtonLabel, JPanel customFields) {
		return getResourceDialog(null, parent.getResourceNames(), title, commitButtonLabel, customFields);
	}

	/**
	 * Create a new resource dialog modal to the currently focused window
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	public static ResourceDialog getResourceDialog(String[] resourceNames, String title, String commitButtonLabel, JPanel customFields) {
		return getResourceDialog(null, resourceNames, title, commitButtonLabel, customFields);
	}

	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 */
	public static ResourceDialog getResourceDialog(Window owner, ResourceManager parent, String title) {
		return getResourceDialog(owner, parent, title, null);
	}
	
	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 */
	public static ResourceDialog getResourceDialog(Window owner, String[] resourceNames, String title) {
		return getResourceDialog(owner, resourceNames, title, null);
	}

	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 */
	public static ResourceDialog getResourceDialog(Window owner, ResourceManager parent, String title, String commitButtonLabel) {
		return getResourceDialog(owner, parent, title, commitButtonLabel, null);
	}

	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 */
	public static ResourceDialog getResourceDialog(Window owner, String[] resourceNames, String title, String commitButtonLabel) {
		return getResourceDialog(owner, resourceNames, title, commitButtonLabel, null);
	}

	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param parent the ResourceManager providing the DocumentProcessors to be
	 *            selectable for application
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	public static ResourceDialog getResourceDialog(Window owner, ResourceManager parent, String title, String commitButtonLabel, JPanel customFields) {
		return getResourceDialog(owner, parent.getResourceNames(), title, commitButtonLabel, customFields);
	}

	/**
	 * Create a new resource dialog modal to a specific window
	 * @param owner the owner dialog for the dialog
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	public static ResourceDialog getResourceDialog(Window owner, String[] resourceNames, String title, String commitButtonLabel, JPanel customFields) {
		if (owner == null)
			owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
		
		if (owner instanceof Dialog)
			return new ResourceDialog(((Dialog) owner), resourceNames, title, commitButtonLabel, customFields);
		
		else if (owner instanceof Frame)
			return new ResourceDialog(((Frame) owner), resourceNames, title, commitButtonLabel, customFields);
		
		else return new ResourceDialog(((Frame) null), resourceNames, title, commitButtonLabel, customFields);
	}
	
	private ResourceSelectorPanel resourceSelector;
	private String resourceName = null;
	
	/**
	 * Constructor
	 * @param owner the owner dialog for the dialog
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	private ResourceDialog(Dialog owner, String[] resourceNames, String title, String commitButtonLabel, JPanel customFields) {
		super(owner, title, true);
		this.init(owner, resourceNames, commitButtonLabel, customFields);
	}

	/**
	 * Constructor
	 * @param owner the owner frame for the dialog
	 * @param resourceNames the resource names to offer for selection
	 * @param title the title for the dialog window
	 * @param commitButtonLabel the label String for the apply button
	 * @param customFields a JPanel containing custom inputs to be displayed
	 *            between the selector list and the buttons
	 */
	private ResourceDialog(Frame owner, String[] resourceNames, String title, String commitButtonLabel, JPanel customFields) {
		super(owner, title, true);
		this.init(owner, resourceNames, commitButtonLabel, customFields);
	}
	
	private void init(Window owner, String[] resourceNames, String commitButtonLabel, JPanel customFields) {	
		this.resourceSelector = new ResourceSelectorPanel(resourceNames, null);
		this.resourceSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				commit();
			}
		});
		
		//	initialize main buttons
		JButton commitButton = new JButton((commitButtonLabel == null) ? ((customFields == null) ? "Select" : "Apply") : commitButtonLabel);
		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		commitButton.setPreferredSize(new Dimension(100, 21));
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				commit();
			}
		});
		JButton abortButton = new JButton("Cancel");
		abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
		abortButton.setPreferredSize(new Dimension(100, 21));
		abortButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				resourceName = null;
				dispose();
			}
		});
		
		JPanel mainButtonPanel = new JPanel(new FlowLayout());
		mainButtonPanel.add(commitButton);
		mainButtonPanel.add(abortButton);
		
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(this.resourceSelector, BorderLayout.CENTER);
		
		if (customFields == null)
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
		else {
			JPanel functionPanel = new JPanel(new BorderLayout());
			functionPanel.add(customFields, BorderLayout.CENTER);
			functionPanel.add(mainButtonPanel, BorderLayout.SOUTH);
			this.getContentPane().add(functionPanel, BorderLayout.SOUTH);
		}
		
		this.setResizable(true);
		this.setSize(new Dimension(300, Math.min(Math.max(200, (60 + (resourceNames.length * 25))), 500)));
		this.setLocationRelativeTo(owner);
	}
	
	private void commit() {
		this.resourceName = this.resourceSelector.getSelectedResourceName();
		this.dispose();
	}
	
	/**
	 * @return true if and only if the dialog was committed
	 */
	public boolean isCommitted() {
		return (this.resourceName != null);
	}

	/**
	 * @return the name of the selected DocumentProcessor, or null if none is
	 *         selected
	 */
	public String getSelectedResourceName() {
		return this.resourceName;
	}
}
