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
package de.uka.ipd.idaho.goldenGate.plugins;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Prefix based implementation of a GoldenGatePluginDataProvider. This data
 * provider maps to the underlying data provider, adding the path prefix it is
 * constructed with.
 * 
 * @author sautter
 */
public class PluginDataProviderPrefixBased extends AbstractGoldenGatePluginDataProvider {
	
	private final GoldenGatePluginDataProvider parent;
	private final String pathPrefix;
	
	/**
	 * Constructor
	 * @param parent the data provider to build on
	 * @param pathPrefix the prefix to add to all data names before passing them
	 *            to the parent data provider
	 */
	public PluginDataProviderPrefixBased(GoldenGatePluginDataProvider parent, String pathPrefix) {
		this.parent = parent;
		if (!pathPrefix.endsWith("/")) pathPrefix += "/";
		this.pathPrefix = pathPrefix;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
	 */
	public String[] getDataNames() {
		String[] dataNames = this.parent.getDataNames();
		StringVector localDataNames = new StringVector();
		for (int d = 0; d < dataNames.length; d++)
			if (dataNames[d].startsWith(this.pathPrefix))
				localDataNames.addElement(dataNames[d].substring(this.pathPrefix.length()));
		return localDataNames.toStringArray();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		return this.parent.getInputStream(this.pathPrefix + dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.parent.isDataAvailable(this.pathPrefix + dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		return this.parent.getURL(((dataName.indexOf("://") == -1) && !dataName.toLowerCase().startsWith("file:/")) ? (this.pathPrefix + dataName) : dataName);
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
		return this.parent.isDataEditable(this.pathPrefix + dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return this.parent.allowWebAccess();
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		return this.parent.getOutputStream(this.pathPrefix + dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		return this.parent.deleteData(this.pathPrefix + dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return this.parent.getAbsolutePath() + "/" + this.pathPrefix.substring(0, (this.pathPrefix.length() - 1));
	}
}
