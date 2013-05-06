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

import de.uka.ipd.idaho.gamta.util.ProgressMonitor;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Document savers that allow monitoring the saving process should implement
 * this interface to indicate so. The document save operations returned by
 * document savers implementing this interface shoud be monitorable as well.
 * 
 * @author sautter
 */
public interface MonitorableDocumentSaver extends DocumentSaver {
	
	/**
	 * Save parts of a document in a DocumentEditor as individual documents.
	 * @param data the DocumentEditor holding the document to save parts of
	 * @param modelFormat the suggested format to save the parts in (may be
	 *            changed in used dialog)
	 * @param modelType the suggested type of the parts to save (max be changed
	 *            in user dialog)
	 * @param pm a progress monitor to observe the saving process
	 * @return the type of the document parts that have been saved, or null, if
	 *         the operation has been cancelled
	 */
	public abstract String saveDocumentParts(DocumentEditor data, DocumentFormat modelFormat, String modelType, ProgressMonitor pm);
}
