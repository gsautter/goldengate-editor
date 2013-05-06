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

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Components providing specialized views on documents or parts of them may
 * extend this class instead of implementing DocumentViewer themselves
 * 
 * @author sautter
 */
public abstract class AbstractDocumentViewer extends AbstractGoldenGatePlugin implements DocumentViewer {
	
	/* (non-Javadoc)
	 * @see de.goldenGate.resourceManagement.AbstractGoldenGatePlugin#getPluginName()
	 */
	public String getPluginName() {
		return this.getViewMenuName();
	}
	
	/**
	 * Display the document from the specified DocumentEditor in the way
	 * specific to this document view. This implementation makes the specified
	 * DocumentEditor apply a DocumentProcessor that calls the
	 * showDocument(MutableAnnotation, DocumentEditor) method. Using the
	 * applyDocumentProcessor() method for accessing the document allows for
	 * using the existing undo management, and the synchronization and security
	 * mechanisms.
	 * @param editor the DocumentEditor whose content to show
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer#showDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor)
	 */
	public final void showDocument(DocumentEditor editor) {
		Properties parameters = new Properties();
		parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
		this.showDocument(editor, parameters);
	}
	
	/**
	 * Display the document from the specified DocumentEditor in the way
	 * specific to this document view. This implementation makes the specified
	 * DocumentEditor apply a DocumentProcessor that calls the
	 * showDocument(MutableAnnotation, DocumentEditor) method. Using the
	 * applyDocumentProcessor() method for accessing the document allows for
	 * using the existing undo management, and the synchronization and security
	 * mechanisms. This method enables other plugins to specify parameters that
	 * cause an actual DocumentViewer implementation to display a document in a
	 * way intended by the plugin without prompting a user for viewing
	 * parameters.
	 * @param editor the DocumentEditor whose content to show
	 * @param parameters parameters specifying details on how to show the
	 *            document.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer#showDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor,java.util.Properties)
	 */
	public void showDocument(DocumentEditor editor, Properties parameters) {
		parameters = new Properties(parameters);
		parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
		editor.applyDocumentProcessor(new DocumentViewerProcessor(editor), null, parameters);
	}
	
	/**
	 * DocumentProcessor implementation for showing specialized views of a
	 * document. Using the applyDocumentProcessor() method for accessing the
	 * document allows for using the existing undo management, and the
	 * synchronization and security mechanisms.
	 * 
	 * @author sautter
	 */
	private class DocumentViewerProcessor implements DocumentProcessor {
		private DocumentEditor editor; 
		private DocumentViewerProcessor(DocumentEditor editor) {
			this.editor = editor;
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor#process(de.uka.ipd.idaho.gamta.MutableAnnotation)
		 */
		public void process(MutableAnnotation data) {
			Properties parameters = new Properties();
			parameters.setProperty(Resource.INTERACTIVE_PARAMETER, Resource.INTERACTIVE_PARAMETER);
			this.process(data, parameters);
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor#process(de.uka.ipd.idaho.gamta.MutableAnnotation, boolean)
		 */
		public void process(MutableAnnotation data, Properties parameters) {
			if (!parameters.containsKey(INTERACTIVE_PARAMETER)) return;
			try {
				showDocument(data, this.editor, parameters);
			}
			catch (Exception e) {
				e.printStackTrace(System.out);
			}
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.Resource#getName()
		 */
		public String getName() {
			return getViewMenuName();
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.Resource#getProviderClassName()
		 */
		public String getProviderClassName() {
			return ""; // we don't have an actual provider, since this DP is not accessible externally
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.Resource#getTypeLabel()
		 */
		public String getTypeLabel() {
			return "Document Viewer";
		}
	}
	
	/**
	 * Actually display the document (open a respective dialog, or the like)
	 * @param doc the document to show
	 * @param editor the DocumentEdior displaying the specified document (to
	 *            obtain layout information from, may be null)
	 * @param parameters parameters specifying details on how to show the
	 *            document.
	 */
	protected abstract void showDocument(MutableAnnotation doc, DocumentEditor editor, Properties parameters);
	
	/**
	 * Display a document (open a respective dialog, or the like). This method
	 * is intended for other document processors to invoke. This convenience
	 * implementation fetches the active DocumentEditor from the parent
	 * GoldenGATE via the getActiveDocument() method. Then invokation the loops
	 * through to the three parameter version of this method. Sub classes are
	 * welcome to overwrite this convenience implementation as needed.
	 * @param doc the document to show
	 * @param parameters parameters specifying details on how to show the
	 *            document.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer#showDocument(de.uka.ipd.idaho.gamta.MutableAnnotation,
	 *      java.util.Properties)
	 */
	public void showDocument(MutableAnnotation doc, Properties parameters) {
		this.showDocument(doc, this.parent.getActivePanel(), parameters);
	}
	
	/**
	 * This default implementation loops through to the one-argument version of
	 * the method, with null as the argument. Sub classes are welcome to
	 * overwrite it as needed.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer#getDocumentViewParameterPanel()
	 */
	public DocumentViewParameterPanel getDocumentViewParameterPanel() {
		return this.getDocumentViewParameterPanel(null);
	}
	
	/**
	 * This default implementation returns null, sub classes are welcome to
	 * overwrite it as needed.
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentViewer#getDocumentViewParameterPanel(de.uka.ipd.idaho.easyIO.settings.Settings)
	 */
	public DocumentViewParameterPanel getDocumentViewParameterPanel(Settings settings) {
		return null;
	}
}
