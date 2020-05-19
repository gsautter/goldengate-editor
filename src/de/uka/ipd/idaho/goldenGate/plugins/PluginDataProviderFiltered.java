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
package de.uka.ipd.idaho.goldenGate.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.TreeSet;

/**
 * Filter based implementation of a GoldenGatePluginDataProvider. This data
 * provider shows only data items of the underlying data provider that are
 * contained in its filter.
 * 
 * @author sautter
 */
public class PluginDataProviderFiltered implements GoldenGatePluginDataProvider {
	
	private final GoldenGatePluginDataProvider parent;
	private final TreeSet dataNames = new TreeSet();
	
	/**
	 * Constructor
	 * @param parent the data provider to build on
	 * @param dataList a listing of all data items available from this data
	 *            provider. Data items not listed there will not be available,
	 *            regardless if they exist physically or not.
	 */
	public PluginDataProviderFiltered(GoldenGatePluginDataProvider parent, String[] dataList) {
		this.parent = parent;
		
		//	extract data name list
		for (int d = 0; d < dataList.length; d++)
			this.dataNames.add(dataList[d]);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return this.parent.allowWebAccess();
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
	 */
	public String[] getDataNames() {
		TreeSet dataNames = new TreeSet(Arrays.asList(this.parent.getDataNames()));
		dataNames.retainAll(this.dataNames);
		return ((String[]) dataNames.toArray(new String[dataNames.size()]));
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return (this.dataNames.contains(dataName) && this.parent.isDataAvailable(dataName));
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		if (this.dataNames.contains(dataName))
			return this.parent.getInputStream(dataName);
		else throw new IOException("A data item named '" + dataName + "' does not exist.");
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		if (this.dataNames.contains(dataName) || (dataName.indexOf("://") != -1)  || dataName.toLowerCase().startsWith("file:/"))
			return this.parent.getURL(dataName);
		else throw new IOException("A data item named '" + dataName + "' does not exist.");
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable()
	 */
	public boolean isDataEditable() {
		return this.parent.isDataEditable();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		
		//	if data item exists, check if it's accessible
		if (this.parent.isDataAvailable(dataName))
			return this.dataNames.contains(dataName);
		
		//	otherwise, check if creating a new data item is allowed
		else return this.parent.isDataEditable(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		
		//	if data item exists, check if it's accessible
		if (this.parent.isDataAvailable(dataName)) {
			if (this.dataNames.contains(dataName))
				return this.parent.getOutputStream(dataName);
			else throw new IOException("A data item named '" + dataName + "' cannot be written to.");
		}
		
		//	otherwise, createa new data item and add it to the list
		else {
			this.dataNames.add(dataName);
			return this.parent.getOutputStream(dataName);
		}
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		
		//	if data item exists, check if it's accessible
		if (this.parent.isDataAvailable(dataName)) {
			if (this.dataNames.contains(dataName))
				return this.parent.deleteData(dataName);
			else return false;
		}
		
		//	otherwise, just tell it doesn't exist
		else return true;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return this.parent.getAbsolutePath() + "$" + this.dataNames.hashCode();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return this.getAbsolutePath().hashCode();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#equals(de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider)
	 */
	public boolean equals(GoldenGatePluginDataProvider dp) {
		return this.getAbsolutePath().equals(dp.getAbsolutePath());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		return ((obj instanceof GoldenGatePluginDataProvider) && this.equals((GoldenGatePluginDataProvider) obj));
	}
}
