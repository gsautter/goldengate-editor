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


import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants.InvokationTargetProvider;

/**
 * The root component for a graphical user interface for the GoldenGATE editor.
 * This interface provides methods for opening one or more documents, for
 * retrieving the document currently selected (required if a GUI can open more
 * than one document at a time), and for closing a document.
 * 
 * @author sautter
 */
public interface GoldenGateGUI extends GoldenGateConstants, InvokationTargetProvider {
	
	/**
	 * Test if the GUI can open one more document. Implementations capable of
	 * displaying several documents at the same time, eg using a JTabbedPane,
	 * should always return true on this method, at least if they have no
	 * specific limit regarding the number of documents open at the same time.
	 * With implementations designed for displaying at most one document at a
	 * time, this method should reflect whether there is a document opened or
	 * not. This method will never be invoked before both setParent() and init()
	 * have been, and never after storeSettings() and exit() have been.
	 * @return true if this GUI can open one more document, false otherwise
	 */
	public abstract boolean canOpenDocument();
	
	/**
	 * Open a document in the GUI (add the specified document editor to the
	 * container so it becomes visible). This method will never be invoked
	 * before both setParent() and init() have been, and never after
	 * storeSettings() and exit() have been.
	 * @param document the editor holding the document to show
	 */
	public abstract void openDocument(DocumentEditor document);
	
	/**
	 * Retrieve the number of documents currently open. Implementations capable
	 * of displaying several documents at the same time, eg using a JTabbedPane,
	 * should always return the actual number of document. With implementations
	 * designed for displaying at most one document at a time, this method will
	 * always return 1 or 0. This method will never be invoked before both
	 * setParent() and init() have been, and never after storeSettings() and
	 * exit() have been.
	 * @return the number of document currently open
	 */
	public abstract int getOpenDocumentCount();
	
	/**
	 * Receive notification that the title of some document has changed, eg for
	 * adjusting some respective label or title in the GUI. This method will
	 * never be invoked before both setParent() and init() have been, and never
	 * after storeSettings() and exit() have been.
	 * @param document the editor holding the document whose title has changed
	 *            (the actual title is available from the argument editor)
	 */
	public abstract void documentTitleChanged(DocumentEditor document);
	
	/**
	 * Close a document. The affected editor should simply be removed from the
	 * GUI, the backing GoldenGATE instance will take all further measures.
	 * This method will never be invoked before both setParent() and init() have
	 * been, and never after storeSettings() and exit() have been.
	 * @param document the editor holding the document to close
	 */
	public abstract void closeDocument(DocumentEditor document);
	
	/**
	 * Make the GUI know the backing GoldenGATE instance
	 * @param parent the backing GoldenGATE instance
	 */
	public abstract void setParent(GoldenGATE parent);
	
	/**
	 * Initialize the GUI instance. The argument settings will usually hold the
	 * same parameters and values that were stored by the last invokation of
	 * storeSettings(). This method will be invoked after the backing GoldenGATE
	 * instace was handed over via setParent().
	 * @param set a settings object holding the parameters
	 */
	public abstract void init(Settings set);
	
	/**
	 * Store the parameters of the GUI instance. All the parameters and values
	 * that are stored by this method will usually be contained in the settings
	 * object specified to the init() method when the GUI is opened for the next
	 * time.
	 * @param set a settings object for storing the parameters
	 */
	public abstract void storeSettings(Settings set);
	
	/**
	 * Exit the GUI instance, clear data structures, etc. After this method has
	 * been invoked, the GUI instance will not be used any more without a
	 * previous invokation of setParent() and init().
	 */
	public abstract void exit();
	
	/**
	 * Open the GUI, eg set a JFrame's visible property to true. If the specific
	 * GUI implementation does not support becoming visible and invisible, eg an
	 * applet embedded in a web page, it can ignore invokations of this method.
	 */
	public abstract void open();
	
	/**
	 * Close the GUI, eg set a JFrame's visible property to false. If the
	 * specific GUI implementation does not support becoming visible and
	 * invisible, eg an applet embedded in a web page, it can ignore invokations
	 * of this method.
	 */
	public abstract void close();
}
