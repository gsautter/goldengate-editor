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

import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * Abstract implementation of the DocumentFormatProvider interface, implementing
 * the getDataNamesForDocumentFormat() method in a way returning an empty array,
 * but leaving all the other methods abstract.
 * 
 * @author sautter
 */
public abstract class AbstractDocumentFormatProvider extends AbstractResourceManager implements DocumentFormatProvider {
	
	/**
	 * Note: this default implementation simply returns an empty array, which is
	 * sufficient for hard-coded document formats
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getDataNamesForResource(java.lang.String)
	 */
	public String[] getDataNamesForResource(String name) {
		return new String[0];
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getToolsMenuLabel()
	 */
	public final String getToolsMenuLabel() {
		return null; // we're not in the Tools menu
	}
	
	/**
	 * This implementation returns the same as the getPluginName() method, which
	 * is convenient for document format providers that implement one specific
	 * document format rather than multiple custom ones. The latter should
	 * overwrite this method to provide a more appropriate label for the
	 * document formats they provide.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getResourceTypeLabel()
	 */
	public String getResourceTypeLabel() {
		return this.getPluginName();
	}
	
	/**
	 * Note: this default implementation returns null and thus deactivates the
	 * resource name list, which is sufficient for hard-coded document formats
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getFileExtension()
	 */
	protected String getFileExtension() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getResourceNames()
	 */
	public String[] getResourceNames() {
		StringVector collector = new StringVector();
		collector.addContentIgnoreDuplicates(this.getLoadFormatNames());
		collector.addContentIgnoreDuplicates(this.getSaveFormatNames());
		return collector.toStringArray();
	}
}
