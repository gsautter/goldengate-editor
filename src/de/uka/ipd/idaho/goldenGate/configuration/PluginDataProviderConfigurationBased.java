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
package de.uka.ipd.idaho.goldenGate.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.plugins.AbstractGoldenGatePluginDataProvider;

/**
 * Implementation of a GoldenGatePluginDataProvider working through the IO
 * facilities of a GoldenGATE configuration. This enables configurations to use
 * this data provider as a basis for all others.
 * 
 * @author sautter
 */
public class PluginDataProviderConfigurationBased extends AbstractGoldenGatePluginDataProvider {
	private GoldenGateConfiguration config;
	
	/**
	 * @param config
	 */
	public PluginDataProviderConfigurationBased(GoldenGateConfiguration config) {
		this.config = config;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return this.config.allowWebAccess();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.config.isDataAvailable(dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		return this.config.getInputStream(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		return this.config.getURL(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable()
	 */
	public boolean isDataEditable() {
		return this.config.isDataEditable();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		return this.config.isDataEditable(dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		return this.config.getOutputStream(dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		return this.config.deleteData(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
	 */
	public String[] getDataNames() {
		return this.config.getDataNames();
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return this.config.getAbsolutePath();
	}
}
