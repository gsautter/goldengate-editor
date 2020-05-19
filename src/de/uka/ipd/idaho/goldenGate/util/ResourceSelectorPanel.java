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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * Selector list widget for the resources provided by some resource manager. Any
 * registered notification listener will be notified on a double click in the
 * list.
 * 
 * @author sautter
 */
public class ResourceSelectorPanel extends JPanel {
	
	private JList resourceList;

	/**
	 * Constructor
	 * @param resourceNames an array of Strings containing the names selectable
	 *            in this ResourceSelectorPanel
	 * @param initialSelection the name initially selected (null will select
	 *            nothing)
	 */
	public ResourceSelectorPanel(String[] resourceNames, String initialSelection) {
		super(new BorderLayout(), true);
		
		Arrays.sort(resourceNames, String.CASE_INSENSITIVE_ORDER);
		this.resourceList = new JList(resourceNames);
		this.resourceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		if (initialSelection != null)
			this.resourceList.setSelectedValue(initialSelection, true);
		
		this.resourceList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() > 1) {
					int index = resourceList.getSelectedIndex();
					if (index != -1) notifyActionListeners(resourceList.getSelectedValue().toString());
				}
			}
		});
		
		this.add(new JScrollPane(this.resourceList), BorderLayout.CENTER);
	}
	
	/**
	 * @return the name of the selected DocumentProcessor, or null if none is
	 *         selected
	 */
	public String getSelectedResourceName() {
		return ((String) this.resourceList.getSelectedValue());
	}

	private ArrayList listeners = new ArrayList();
	private void notifyActionListeners(String resourceName) {
		for (int l = 0; l < this.listeners.size(); l++)
			((ActionListener) this.listeners.get(l)).actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, resourceName));
	}

	/**
	 * add an ActionListener to this resource list panel to catch double clicks
	 * in the list
	 * @param al the ActionListener to add
	 */
	public void addActionListener(ActionListener al) {
		if (al != null) this.listeners.add(al);
	}

	/**
	 * remove an ActionListener from this resource list panel
	 * @param al the ActionListener to remove
	 */
	public void removeActionListener(ActionListener al) {
		if (al != null) this.listeners.remove(al);
	}
}
