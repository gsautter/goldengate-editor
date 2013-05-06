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


import java.util.Properties;

import de.uka.ipd.idaho.goldenGate.DocumentEditor;
import de.uka.ipd.idaho.goldenGate.util.ResourceDialog;

/**
 * Abstract implementation of the AbstractResourceManager interface,
 * implementing the applyDocumentProcessor() method, but leaving all other
 * methods abstract.
 * 
 * @author sautter
 */
public abstract class AbstractDocumentProcessorManager extends AbstractResourceManager implements DocumentProcessorManager {
	
	/**	Constructor
	 */
	public AbstractDocumentProcessorManager() {}
	
	/**
	 * apply a DocumentProcessor provided by this DocumentProcessorManager to a
	 * DocumentPart. The idea of this method is to give the DocumentProcessor
	 * the chance of being run under circumstances which make use of the special
	 * knowledge only the provider has about the habbits of its
	 * DocumentProcessors
	 * @param processorName the name of the DocumentProcessor to be applied (if
	 *            null, a selector should be presented to the user)
	 * @param data the DocumentEditor containing the document to apply the
	 *            DocumentProcessor to
	 * @param parameters processing parameters (eg if to allow intermediate
	 *            dialogs or user interaction in general)?
	 */
	public void applyDocumentProcessor(String processorName, DocumentEditor data, Properties parameters) {
		String pn = processorName;
		
		//	select processor if not specified
		if (pn == null) {
			ResourceDialog rd = ResourceDialog.getResourceDialog(this, (this.getToolsMenuLabel() + " " + this.getResourceTypeLabel()), this.getToolsMenuLabel());
			rd.setLocationRelativeTo(rd.getOwner());
			rd.setVisible(true);
			if (rd.isCommitted()) pn = rd.getSelectedResourceName();
		}
		
		//	get processor
		DocumentProcessor dp = this.getDocumentProcessor(pn);
		if (dp != null) {
			
			//	apply processor
			ResourceSplashScreen splashScreen = new ResourceSplashScreen((this.getResourceTypeLabel() + " Running ..."), ("Please wait while '" + this.getResourceTypeLabel() + ": " + dp.getName() + "' is processing the Document ..."));
			data.applyDocumentProcessor(dp, splashScreen, parameters);
		}
	}
}
