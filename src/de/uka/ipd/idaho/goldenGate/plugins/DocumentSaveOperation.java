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


import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Ready to use save operation using a specific DocumentSaver and DocumentFormat
 */
public interface DocumentSaveOperation {
	
	/**
	 * Save the content of a DocumentEditor using the settings of this
	 * DocumentSaveOperation.
	 * @param data the document to save
	 * @return a String representing the new name of the document in the editor,
	 *         or null, if this SaveOperation cannot be used again (for instance
	 *         if a session can be used only once) and thus should not become
	 *         the default used for save
	 */
	public abstract String saveDocument(DocumentEditor data);
	
	/**
	 * Save a document.
	 * @param data the document to save
	 * @return a String representing the new name of the document, or null, if
	 *         this SaveOperation cannot be used again (for instance if a
	 *         session can be used only once) and thus should not become the
	 *         default used for save
	 */
	public abstract String saveDocument(QueriableAnnotation data);
	
	/**
	 * @return the name of a document after it has been saved through this
	 *         DocumentSaveOperation
	 */
	public abstract String getDocumentName();
	
	/**
	 * @return the format a document will be saved in by this
	 *         DocumentSaveOperation
	 */
	public abstract DocumentFormat getDocumentFormat();
	
	/**
	 * notify this SaveOpretaion that the document it is responsible for was
	 * closed, so it can free remote resources, for instance
	 */
	public abstract void documentClosed();
}
