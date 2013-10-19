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
package de.uka.ipd.idaho.goldenGate;


import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;


/**
 * A CustomShortcut adds a custom key combination to the AnnotationEditor, to be
 * invoked when a piece of text is selected. The custom shortcut will annotate
 * this text with the configured type, and apply a DocumentProcessor to the
 * newly created annotation (if a DocumentProcessor is specified).
 * 
 * @author sautter
 */
public class CustomShortcut {
	
	/** the combination of keys triggering the shortcut */
	public final String keyCombination;
	
	/** the type of annotation to create */
	public final String annotationType;
	
	private final DocumentProcessorManager processorProvider;
	private final String processorName;
	
	/**
	 * Constructor
	 * @param keyCombination the combination of keys triggering the shortcut
	 * @param annotationType the type of the Annotation to create
	 * @param processorProvider the document processor manager to load the
	 *            document processor from
	 * @param processorName the name of the document processor to use
	 */
	public CustomShortcut(String keyCombination, String annotationType, DocumentProcessorManager processorProvider, String processorName) {
		this.annotationType = ((annotationType == null) ? "" : annotationType.trim());
		this.keyCombination = keyCombination;
		this.processorProvider = processorProvider;
		this.processorName = processorName;
	}
	
	/**
	 * Retrieve the document processor this custom shortcut uses, if any is
	 * configured.
	 * @return the document processor the custom function uses
	 */
	public DocumentProcessor getDocumentProcessor() {
		return ((this.processorProvider == null) ? null : this.processorProvider.getDocumentProcessor(this.processorName));
	}
	
	/**
	 * Retrieve the document processor manager responsible for the document
	 * processor this custom shortcut uses.
	 * @return the document processor manager
	 */
	public DocumentProcessorManager getDocumentProcessorProvider() {
		return this.processorProvider;
	}
	
	/**
	 * Retrieve the name of the document processor this custom shortcut uses.
	 * @return the document processor name
	 */
	public String getDocumentProcessorName() {
		return this.processorName;
	}
	
	/**
	 * Retrieve a label for the help text explaining what the custom shortcut
	 * does. If GoldenGATE Document Editor is started with a non-master
	 * configuration, the explanation text becomes part of the help, using the
	 * return value of this method as the title. The returned string must be
	 * plain text. This default implementation returns a generic text built
	 * from the annotation type and selected document processor. Implementations
	 * of the <code>Manager</code> interface are welcome to provide an
	 * implementation with more meaningful return values.
	 * @return the help label
	 */
	public String getHelpLabel() {
		if (this.processorName == null) {
			if ("".equals(this.annotationType))
				return this.keyCombination;
			else return ("Annotate '" + this.annotationType + "' (" + this.keyCombination + ")");
		}
		else {
			if ("".equals(this.annotationType))
				return ("Run '" + this.processorName + "' (" + this.keyCombination + ")");
			else return ("Annotate and process '" + this.annotationType + "' (" + this.keyCombination + ")");
		}
	}
	
	/**
	 * Retrieve a help text explaining what the custom shortcut does. If
	 * GoldenGATE Document Editor is started with a non-master configuration,
	 * this explanation text becomes part of the help, using the help label
	 * as the title. This default implementation
	 * returns null, providing no help text. Implementations of the
	 * <code>Manager</code> interface are welcome to provide an implementation
	 * with more meaningful return values.
	 * @return the help text
	 */
	public String getHelpText() {
		return null;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("customShortcut[");
		sb.append(";" + this.annotationType);
		sb.append(";" + this.processorName);
		sb.append("@" + this.processorProvider.getClass().getName());
		sb.append("]");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof CustomShortcut))
			return false;
		CustomShortcut cs = ((CustomShortcut) o);
		if (cs == this)
			return true;
		return (this.toString().equals(cs.toString()));
	}
	
	/**
	 * Manager component allowing for creating, editing, and deleting CustomShortcuts
	 * 
	 * @author sautter
	 */
	public static interface Manager extends ResourceManager {
		
		/**	retrieve a CustomShortcut by its name (Control and Alt keys will be indicated by "Control-" and "Alt-" prefixes to the custom shortcut's key character)
		 * @param	name	the name of the required CustomShortcut
		 * @return the CustomShortcut with the required name, or null, if there is no such CustomShortcut
		 */
		public abstract CustomShortcut getCustomShortcut(String name);
	}
}