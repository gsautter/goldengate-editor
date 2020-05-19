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


import java.util.Properties;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Components providing specialized views on documents or parts of them should
 * implment this interface
 * 
 * @author sautter
 */
public interface DocumentViewer extends GoldenGatePlugin {
	
	/**
	 * @return the name of this DocumentViewer to display in the View menu
	 */
	public abstract String getViewMenuName();
	
	/**
	 * Displays the document from the specified DocumentEditor in the way
	 * specific to this DocumentViewer.
	 * @param editor the DocumentEditor whose content to show
	 */
	public abstract void showDocument(DocumentEditor editor);
	
	/**
	 * Displays the document from the specified DocumentEditor in the way
	 * specific to this DocumentViewer. This method enables other plugins to
	 * specify parameters when asking the DocumentViewer to open a document
	 * view.
	 * @param editor the DocumentEditor whose content to show
	 * @param parameters parameters specifying details on how to show the
	 *            document.
	 */
	public abstract void showDocument(DocumentEditor editor, Properties parameters);
	
	/**
	 * Displays a document. This method is intended for document processors to
	 * invoke, from a scope where they already have write access to a document.
	 * If any layout information is required from a DocumentEditor instance, it
	 * can be obtained from the parent GoldenGATE via the getActiveDocument()
	 * method.
	 * @param doc the document to show
	 * @param parameters parameters specifying details on how to show the
	 *            document.
	 */
	public abstract void showDocument(MutableAnnotation doc, Properties parameters);
	
	/**
	 * Obtain a panel for specifying additional parameters for a document view
	 * @return a panel for specifying additional parameters for a document view
	 */
	public abstract DocumentViewParameterPanel getDocumentViewParameterPanel();

	/**
	 * Obtain a panel for specifying additional parameters for a document view
	 * @param settings the initial settings for the fields in the parameter
	 *            panel
	 * @return a panel for specifying additional parameters for a document view
	 */
	public abstract DocumentViewParameterPanel getDocumentViewParameterPanel(Settings settings);
}
