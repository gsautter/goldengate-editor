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


import javax.swing.JPanel;

import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Document editor extensions provide additional views, which can be added to
 * the document editor. Such views can, for instance, provide advanced document
 * navigation functionality or display additional information on the document.
 * Note, however, that the views are not intended to edit a document directly.
 * 
 * @author sautter
 */
public interface DocumentEditorExtension extends GoldenGatePlugin {
	
//	/**
//	 * This class represents the actual extensions to be added to individual
//	 * DocumentEditor instances.
//	 * 
//	 * @author sautter
//	 */
//	public static abstract class DocumentEditorExtensionPanel extends JPanel {
//		
//		/** Constructor using a BorderLayout
//		 */
//		public DocumentEditorExtensionPanel() {
//			this(new BorderLayout());
//		}
//		
//		/** Constructor with custom layout manager
//		 * @param	layout	the LayoutManager to use
//		 */
//		public DocumentEditorExtensionPanel(LayoutManager layout) {
//			super(layout, true);
//		}
//		
//		/**	
//		 * @return the constant indication the preferred orientation of this extension panel, one of
//		 * HORIZONTAL,
//		 * VERTICAL,
//		 * (to be extended)
//		 */
//		public abstract String getPreferredOrientation();
//		
//		/** the constant for indicating a preference for a horizontal layout, significantly wider than high */
//		public static final String HORIZONTAL_PREFERRED_ORIENTATION = "HORIZONTAL";
//		
//		/** the constant for indicating a preference for a vertical layout, significantly higher than wide */
//		public static final String VERTICAL_PREFERRED_ORIENTATION = "VERTICAL";
//	}
//	
//	/**
//	 * obtain an extension for a specific DocumentEditor
//	 * @param	editor	the DocumentEditor to be extended, at the same time the target of the extension panel's method invokation
//	 * @return a JPanel holding an extension for the specified document editor
//	 */
//	public abstract DocumentEditorExtensionPanel getExtensionPanel(DocumentEditor editor);
	
	/**
	 * obtain an extension for a specific DocumentEditor
	 * @param	editor	the DocumentEditor to be extended, at the same time the target of the extension panel's method invokation
	 * @return a JPanel holding an extension for the specified document editor
	 */
	public abstract JPanel getExtensionPanel(DocumentEditor editor);
}
