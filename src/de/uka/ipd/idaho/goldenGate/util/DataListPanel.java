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


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * a JPanel for listing and selectin the data objects provided by some plugin
 * data provider.
 * 
 * @author sautter
 */
public class DataListPanel extends JPanel {
	
	private GoldenGatePluginDataProvider dataProvider;
	private StringFilter[] filters;
	
	private StringVector dataNames = new StringVector();
	private ArrayList listeners = null;
	
	private JList nameList; 
	private JScrollPane nameListBox;
	
	/**
	 * a filter for the data names to display in the list
	 * 
	 * @author sautter
	 */
	public static interface StringFilter {
		
		/**	test if a name should appear in the list
		 * @param	name	the name to test
		 * @return true if the specified name should appear in the list 
		 */
		public abstract boolean accept(String name);
	}
	
	/**	Constructor
	 * @param	dataProvider	the data provider whose data to display
	 */
	public DataListPanel(GoldenGatePluginDataProvider dataProvider) {
		this.dataProvider = dataProvider;
		this.init();
	}
	
	/**	Constructor
	 * @param	dataProvider	the data provider whose data to display
	 * @param	filter			the filter to determine which data object names show
	 */
	public DataListPanel(GoldenGatePluginDataProvider dataProvider, StringFilter filter) {
		this.dataProvider = dataProvider;
		if (filter != null) {
			this.filters = new StringFilter[1];
			this.filters[0] = filter;
		}
		this.init();
	}
	
	/**	Constructor
	 * @param	dataProvider	the data provider whose data to display
	 * @param	filters			the filters to determine which data object names show
	 */
	public DataListPanel(GoldenGatePluginDataProvider dataProvider, StringFilter[] filters) {
		this.dataProvider = dataProvider;
		this.filters = filters;
		this.init();
	}
	
	private void init() {
		
		//	 initialize file list
		this.nameList = new JList(new DataListModel());
		this.nameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.nameList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				notifySelected();
			}
		});
		this.nameListBox = new JScrollPane(this.nameList);
		
		//	initialize buttons
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		JButton refreshButton = new JButton("Refresh List");
		refreshButton.setBorder(BorderFactory.createRaisedBevelBorder());
		refreshButton.setPreferredSize(new Dimension(115, 21));
		refreshButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refresh();
			}
		});
		buttonPanel.add(refreshButton);
		
		this.setLayout(new BorderLayout());
		this.add(this.nameListBox, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		this.refresh();
	}
	
	/**	@return	the name of the data object currently selected
	 */
	public String getSelectedName() {
		int index = this.nameList.getSelectedIndex();
		return ((index == -1) ? null : this.dataNames.get(index));
	}
	
	/**	set the selected data object
	 * @param	name	the data object name to set selected
	 */
	public void setSelectedName(String name) {
		int index = this.dataNames.indexOf(name);
		this.nameList.setSelectedIndex(index);
	}
	
	/**	notify the listeners that a file has been selected
	 */
	public void notifySelected() {
		this.notifySelected(this.getSelectedName());
	}
	
	private void notifySelected(String name) {
		if (this.listeners != null)
			for (int l = 0; l < this.listeners.size(); l++)
				((DataListListener) this.listeners.get(l)).selected(name);
	}
	
	/**	register a listener to be notified when the selected file changes
	 * @param	dll		the listener to register
	 */
	public void addDataListListener(DataListListener dll) {
		if (this.listeners == null) this.listeners = new ArrayList();
		if (dll != null) this.listeners.add(dll);
	}
	
	/**	unregister a listener
	 * @param	dll		the listener to unregister
	 */
	public void removeDataListListener(DataListListener dll) {
		if (this.listeners != null) this.listeners.remove(dll);
	}
	
	/**	synchronize display with content of the backing data provider
	 */
	public void refresh() {
		this.dataNames.clear();
		
		String[] dataNames = this.dataProvider.getDataNames();
		for (int n = 0; n < dataNames.length; n++) {
			if ((this.filters == null) || (this.filters.length == 0)) this.dataNames.addElementIgnoreDuplicates(dataNames[n]);
			else for (int f = 0; f < this.filters.length; f++)
				if (this.filters[f].accept(dataNames[n])) {
					this.dataNames.addElementIgnoreDuplicates(dataNames[n]);
					continue;
				}
		}
		
		this.dataNames.sortLexicographically(false, false);
		this.nameList.setModel(new DataListModel());
		this.nameList.validate();
	}
	
	/**	obtain a data object name at a certain position
	 * @param	index	the position of the desired name
	 * @return the name at the specified index
	 */
	public String dataNameAt(int index) {
		return ((this.dataNames == null) ? null : this.dataNames.get(index));
	}
	
	/**	@return	the number of data object names currently displayed
	 */
	public int getNameCount() {
		return ((this.dataNames == null) ? 0 : this.dataNames.size());
	}
	
	/**	@return	the names listed in this data list
	 */
	public String[] getNames() {
		return this.dataNames.toStringArray();
	}
	
	private class DataListModel implements ListModel {
		
		public DataListModel() {}
		
		/** @see javax.swing.ListModel#getElementAt(int)
		 */
		public Object getElementAt(int index) {
			return dataNameAt(index);
		}
		
		/** @see javax.swing.ListModel#getSize()
		 */
		public int getSize() {
			return getNameCount();
		}
		
		/** @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
		 */
		public void addListDataListener(ListDataListener l) {}
		
		/** @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
		 */
		public void removeListDataListener(ListDataListener l) {}
	}
}
