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


import javax.swing.JMenuItem;

import de.uka.ipd.idaho.gamta.MutableAnnotation;

/**
 * Interface to be implemented by components that can load documents (from whatever source) 
 * 
 * @author sautter
 */
public interface DocumentLoader {
	
	/**	@return	a menu item to integrate this DocumentLoader in the file menu
	 */
	public abstract JMenuItem getLoadDocumentMenuItem();
	
	/**
	 * Container class for a document and associated data, namely the document's
	 * name and format, and (optionally) a save operation.
	 * 
	 * @author sautter
	 */
	public static class DocumentData {
		
		/** the actual document */
		public final MutableAnnotation docData;
		
		/** the the document's name */
		public final String name;
		
		/** the document's format */
		public final DocumentFormat format;
		
		/** a save operation for saving the document back to its origin (may be null) */
		public final DocumentSaveOperation saveOpertaion;
		
		/** Constructor
		 * @param docData the actual document
		 * @param name the document's name
		 * @param format the document's format
		 */
		public DocumentData(MutableAnnotation docData, String name, DocumentFormat format) {
			this(docData, name, format, null);
		}
		
		/** Constructor
		 * @param docData the actual document
		 * @param name the document's name
		 * @param format the document's format
		 * @param saveOpertaion a save operation for saving the document back to its origin
		 */
		public DocumentData(MutableAnnotation docData, String name, DocumentFormat format, DocumentSaveOperation saveOpertaion) {
			this.docData = docData;
			this.name = name;
			this.format = format;
			this.saveOpertaion = saveOpertaion;
		}
	}
	
	/**
	 * Load a document through the document loader. This method may return null,
	 * for instance if a user cancels the loading procedure somewhere in its
	 * process. If an exception occurs, however, it should be propagated.
	 * @return a DocumentData object, or null, if the loading process was
	 *         cancelled.
	 */
	public abstract DocumentData loadDocument() throws Exception;
}
