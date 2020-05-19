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


import javax.swing.JMenuItem;

import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Interface to be implemented by components that can save documents (to
 * whatever source)
 * 
 * @author sautter
 */
public interface DocumentSaver {
	
	/**
	 * @return a menu item to integrate this DocumentSaver in the file menu
	 */
	public abstract JMenuItem getSaveDocumentMenuItem();
	
	/**
	 * @return a menu item to integrate this DocumentSaver in the file menu for
	 *         saving parts of a document
	 */
	public abstract JMenuItem getSaveDocumentPartsMenuItem();
	
	/**
	 * Obtain a DocumentSaveOperation.
	 * @param documentName an initial name for the document to save (eg a file
	 *            name)
	 * @param format a suggested DocumentFormat to use
	 * @return a DocumentSaveOperation that can save a document
	 */
	public abstract DocumentSaveOperation getSaveOperation(String documentName, DocumentFormat format);
	
	/**
	 * Obtain a DocumentSaveOperation.
	 * @param model a DocumentSaveOperation to use as a model for the one to
	 *            return
	 * @return a DocumentSaveOperation that can save a document
	 */
	public abstract DocumentSaveOperation getSaveOperation(DocumentSaveOperation model);
	
	/**
	 * Save parts of a document in a DocumentEditor as individual documents.
	 * @param data the DocumentEditor holding the document to save parts of
	 * @param modelFormat the suggested format to save the parts in (may be
	 *            changed in used dialog)
	 * @param modelType the suggested type of the parts to save (max be changed
	 *            in user dialog)
	 * @return the type of the document parts that have been saved, or null, if
	 *         the operation has been cancelled
	 */
	public abstract String saveDocumentParts(DocumentEditor data, DocumentFormat modelFormat, String modelType);
}
