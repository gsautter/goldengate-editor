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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;

import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.util.ProgressMonitor;
import de.uka.ipd.idaho.goldenGate.DocumentEditor;

/**
 * Document format that allows monitoring loading and saving processes. This
 * class is mainly intended to be used for document formats whose loading and/or
 * saving processes involve lengthy data processing. It implements the abstract
 * document IO methods from DocumentFormat to loop through to the abstract ones
 * of this class using a dummy progress monitor.
 * 
 * @author sautter
 */
public abstract class MonitorableDocumentFormat extends DocumentFormat {
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat#loadDocument(java.io.InputStream)
	 */
	public MutableAnnotation loadDocument(InputStream source) throws IOException {
		return this.loadDocument(source, ProgressMonitor.dummy);
	}
	
	/** load a document from an InputStream, the encoding of which is determined automatically
	 * @param	source	the InputStream to read the document from
	 * @param 	pm		the progress monitor to inform about the loading process
	 * @return the document that was just read
	 * @throws IOException if any occurs while reading the specified InputStream
	 */
	public MutableAnnotation loadDocument(InputStream source, ProgressMonitor pm) throws IOException {
		InputStreamReader isr = this.getInputStreamReader(source);
		MutableAnnotation doc = this.loadDocument(isr, pm);
		if (doc != null) doc.setAttribute(ENCODING_ATTRIBUTE, isr.getEncoding());
		return doc;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat#loadDocument(java.io.Reader)
	 */
	public MutableAnnotation loadDocument(Reader source) throws IOException {
		return this.loadDocument(source, ProgressMonitor.dummy);
	}
	
	/** load a document from a Reader
	 * @param	source	the Reader to read the document from
	 * @param 	pm		the progress monitor to inform about the loading process
	 * @return the document that was just read
	 * @throws IOException if any occurs while reading the specified Reader
	 */
	public abstract MutableAnnotation loadDocument(Reader source, ProgressMonitor pm) throws IOException;
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat#saveDocument(de.uka.ipd.idaho.gamta.QueriableAnnotation, java.io.Writer)
	 */
	public boolean saveDocument(QueriableAnnotation data, Writer out) throws IOException {
		return this.saveDocument(data, out, ProgressMonitor.dummy);
	}
	
	/** save a document to a Writer (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the document to save
	 * @param	out		the Writer to write the document to
	 * @param 	pm		the progress monitor to inform about the saving process
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(QueriableAnnotation data, Writer out, ProgressMonitor pm) throws IOException;
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat#saveDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor, java.io.Writer)
	 */
	public boolean saveDocument(DocumentEditor data, Writer out) throws IOException {
		return this.saveDocument(data, out, ProgressMonitor.dummy);
	}
	
	/** save the document in a DocumentEditor to a Writer (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the DocumentEditor whose content to save
	 * @param	out		the Writer to write the document to
	 * @param 	pm		the progress monitor to inform about the saving process
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(DocumentEditor data, Writer out, ProgressMonitor pm) throws IOException;
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.DocumentFormat#saveDocument(de.uka.ipd.idaho.goldenGate.DocumentEditor, de.uka.ipd.idaho.gamta.QueriableAnnotation, java.io.Writer)
	 */
	public boolean saveDocument(DocumentEditor data, QueriableAnnotation doc, Writer out) throws IOException {
		return this.saveDocument(data, doc, out, ProgressMonitor.dummy);
	}
	
	/** save a document to a Writer, using the settings of a DocumentEditor (Note: using this method will leave you with making sure that the appropriate encoding is used)
	 * @param	data	the DocumentEditor providing settings
	 * @param	doc		the document to write
	 * @param	out		the Writer to write the document to
	 * @param 	pm		the progress monitor to inform about the saving process
	 * @return true if and only if the document was saved successfully
	 * @throws IOException if any occurs while writing the document to the specified Writer
	 */
	public abstract boolean saveDocument(DocumentEditor data, QueriableAnnotation doc, Writer out, ProgressMonitor pm) throws IOException;
}
