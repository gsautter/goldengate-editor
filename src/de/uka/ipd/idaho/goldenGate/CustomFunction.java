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


import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.util.gPath.GPath;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessor;
import de.uka.ipd.idaho.goldenGate.plugins.DocumentProcessorManager;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;

/**
 * A Custom Function makes a Document Processor directly accessible through a
 * button in the respective panel of the document editor, and/or through an
 * option in the context menu the annotation editor displays for a right click
 * on an annotation tag.
 * 
 * @author sautter
 */
public class CustomFunction {
	
	/* TODO
	 * - add getter for help text
	 *   - leave it to manager implementation where to get that text from
	 * - display that in help menu instead of current help in non-master configurations
	 * - also facilitate adding a markup manual to configurations
	 *   - allow adding custom files to export descriptions
	 *   - store in configurations as data items
	 */
	
	/** the label to show on the function button and in the context menu */
	public final String label;
	
	/** the tooltip text for the button, giving a deeper explanation */
	public final String toolTip;
	
	private final DocumentProcessorManager processorProvider;
	private final String processorName;
	
	/**
	 * Indicates whether or not to display this custom function in the custom
	 * functions panel. Appearing in this panel makes sense for custom functions
	 * applicable to a document as a whole.
	 */
	public final boolean usePanel;
	
	/**
	 * Indicates whether or not to display this custom function in the
	 * annotation editor's context menu for annotation tags. Appearing in this
	 * context menu makes sense for custom functions applicable to individual
	 * annotations, e.g. paragraphs.
	 */
	public final boolean useContextMenu;
	
	private final LinkedHashMap panelPreclusions;
	
	private final String[] contextMenuFilters;
	
	/**
	 * Constructor
	 * @param label the label to display on the button or in the context menu
	 * @param toolTip the tooltip to show
	 * @param processorProvider the document processor manager to load the
	 *            document processor from
	 * @param processorName the name of the document processor to use
	 * @param usePanel use the custom function in the respective panel in the
	 *            UI?
	 * @param useContextMenu use the custom function in the the UI context menu?
	 */
	public CustomFunction(String label, String toolTip, DocumentProcessorManager processorProvider, String processorName, boolean usePanel, boolean useContextMenu) {
		this.label = label;
		this.toolTip = toolTip;
		this.processorProvider = processorProvider;
		this.processorName = processorName;
		this.usePanel = usePanel;
		this.panelPreclusions = null;
		this.useContextMenu = useContextMenu;
		this.contextMenuFilters = null;
	}
	
	/**
	 * Constructor
	 * @param label the label to display on the button or in the context menu
	 * @param toolTip the tooltip to show
	 * @param processorProvider the document processor manager to load the
	 *            document processor from
	 * @param processorName the name of the document processor to use
	 * @param usePanel use the custom function in the respective panel in the
	 *            UI?
	 * @param contextMenuFilters an array of GPath expressions for whose matches
	 *            to use the custom function in the the UI context menu
	 */
	public CustomFunction(String label, String toolTip, DocumentProcessorManager processorProvider, String processorName, boolean usePanel, String[] contextMenuFilters) {
		this.label = label;
		this.toolTip = toolTip;
		this.processorProvider = processorProvider;
		this.processorName = processorName;
		this.usePanel = usePanel;
		this.panelPreclusions = null;
		this.useContextMenu = ((contextMenuFilters == null) || (contextMenuFilters.length != 0));
		this.contextMenuFilters = contextMenuFilters;
	}
	
	/**
	 * Constructor
	 * @param label the label to display on the button or in the context menu
	 * @param toolTip the tooltip to show
	 * @param processorProvider the document processor manager to load the
	 *            document processor from
	 * @param processorName the name of the document processor to use
	 * @param panelPreclusions a map of GPath expressions to error messages to
	 *            display instead of applying the document processor if a
	 *            document matches the expression; the GPath expression will be
	 *            evaluated in the order they come from the key set iterator of
	 *            the specified map
	 * @param useContextMenu use the custom function in the the UI context menu?
	 */
	public CustomFunction(String label, String toolTip, DocumentProcessorManager processorProvider, String processorName, Map panelPreclusions, boolean useContextMenu) {
		this.label = label;
		this.toolTip = toolTip;
		this.processorProvider = processorProvider;
		this.processorName = processorName;
		this.usePanel = (panelPreclusions != null);
		this.panelPreclusions = new LinkedHashMap();
		if (panelPreclusions != null)
			this.panelPreclusions.putAll(panelPreclusions);
		this.useContextMenu = useContextMenu;
		this.contextMenuFilters = null;
	}
	
	/**
	 * Constructor
	 * @param label the label to display on the button or in the context menu
	 * @param toolTip the tooltip to show
	 * @param processorProvider the document processor manager to load the
	 *            document processor from
	 * @param processorName the name of the document processor to use
	 * @param panelPreclusions a map of GPath expressions to error messages to
	 *            display instead of applying the document processor if a
	 *            document matches the expression; the GPath expression will be
	 *            evaluated in the order they come from the key set iterator of
	 *            the specified map
	 * @param contextMenuFilters an array of GPath expressions for whose matches
	 *            to use the custom function in the the UI context menu
	 */
	public CustomFunction(String label, String toolTip, DocumentProcessorManager processorProvider, String processorName, Map panelPreclusions, String[] contextMenuFilters) {
		this.label = label;
		this.toolTip = toolTip;
		this.processorProvider = processorProvider;
		this.processorName = processorName;
		this.usePanel = (panelPreclusions != null);
		this.panelPreclusions = new LinkedHashMap();
		if (panelPreclusions != null)
			this.panelPreclusions.putAll(panelPreclusions);
		this.useContextMenu = ((contextMenuFilters == null) || (contextMenuFilters.length != 0));
		this.contextMenuFilters = contextMenuFilters;
	}
	
	/**
	 * Retrieve a help text explaining what the custom function does in more
	 * detail than possible in a tooltip text. If GoldenGATE Document Editor is
	 * started with a non-master configuration, explanation text becomes part
	 * of the help, using the custom function label as the title. The returned
	 * string may be plain text or HTML; in the latter case, it has to start
	 * with '&lt;html&gt;' to indicate so. This default implementation returns
	 * null, providing no help text. Implementations of the <code>Manager</code>
	 * interface are welcome to provide an implementation with more meaningful
	 * return values.
	 * @return the help text
	 */
	public String getHelpText() {
		return null;
	}
	
	/**
	 * Check if the custom function in is applicable to a document. If usePanel
	 * is false, this method returns false. Otherwise, this method returns true
	 * if the argument document matches none of the GPath expressions handed to
	 * the respective constructor. Note that this method is a shorthand for
	 * getPrecludingError() == null;
	 * @param doc the document to test
	 * @return true if the custom function is applicable, false otherwise
	 */
	public boolean isApplicableTo(QueriableAnnotation doc) {
		return (this.getPrecludingError(doc) == null);
	}
	
	/**
	 * Retrieve an error message describing the condition preventing the custom
	 * function from being applicable to a document. If usePanel is false, this
	 * method returns '&lt;customFunctionName&gt; is not applicable to whole
	 * documents'. Otherwise, this method checks the preclusion GPath
	 * expressions one by one and returns the first error reported, or null, if
	 * none of the preclusions reports an error.
	 * @param doc the document to test
	 * @return a message describing the reason this custom function is not
	 *         applicable, or null to indicate it is applicable
	 */
	public String getPrecludingError(QueriableAnnotation doc) {
		if (!this.usePanel)
			return (this.label + " is not applicable to whole documents");
		if (this.panelPreclusions == null)
			return null;
		for (Iterator pit = this.panelPreclusions.keySet().iterator(); pit.hasNext();) {
			String gpe = ((String) pit.next());
			if (GPath.evaluateExpression(gpe, doc, null).asBoolean().value)
				return ((String) this.panelPreclusions.get(gpe));
		}
		return null;
	}
	
	/**
	 * Display the custom function in the context menu for some queriable
	 * annotation? If useContextMenu is false, this method returns false.
	 * Otherwise, this method returns true if the argument annotation matches
	 * any of the GPath expressions handed to the respective constructor.
	 * @param annot the annotation to test
	 * @return true if the custom function should be displayed, false otherwise
	 */
	public boolean displayFor(QueriableAnnotation annot) {
		if (!this.useContextMenu)
			return false;
		if (this.contextMenuFilters == null)
			return true;
		for (int f = 0; f < this.contextMenuFilters.length; f++) {
			if (GPath.evaluateExpression(this.contextMenuFilters[f], annot, null).asBoolean().value)
				return true;
		}
		return false;
	}
	
	/**
	 * Retrieve the document processor this custom function executes.
	 * @return the document processor the custom function executes
	 */
	public DocumentProcessor getDocumentProcessor() {
		return ((this.processorProvider == null) ? null : this.processorProvider.getDocumentProcessor(this.processorName));
	}
	
	/**
	 * Retrieve the document processor manager responsible for the document
	 * processor this custom function executes.
	 * @return the document processor manager
	 */
	public DocumentProcessorManager getDocumentProcessorProvider() {
		return this.processorProvider;
	}
	
	/**
	 * Retrieve the name of the document processor this custom function
	 * executes.
	 * @return the document processor name
	 */
	public String getDocumentProcessorName() {
		return this.processorName;
	}
	
	/**
	 * Retrieve a map relating the preclusion expressions specifying when this
	 * custom function is not applicable to a document to respective error
	 * messages.
	 * @return a map relating the preclusion expressions to error messages
	 */
	public LinkedHashMap getPanelPreclusions() {
		if (this.panelPreclusions == null)
			return null;
		return new LinkedHashMap(this.panelPreclusions);
	}
	
	/**
	 * Retrieve the filter expressions whose matches this custom function is
	 * applicable to.
	 * @return an array holding the filter expressions
	 */
	public String[] getContextMenuFilters() {
		if (this.contextMenuFilters == null)
			return null;
		String[] cmfs = new String[this.contextMenuFilters.length];
		System.arraycopy(this.contextMenuFilters, 0, cmfs, 0, this.contextMenuFilters.length);
		return cmfs;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("customFunction[");
		sb.append(this.label);
		sb.append(";" + this.toolTip);
		sb.append(";" + this.processorName);
		sb.append("@" + this.processorProvider.getClass().getName());
		sb.append("]");
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if ((o == null) || !(o instanceof CustomFunction))
			return false;
		CustomFunction cf = ((CustomFunction) o);
		if (cf == this)
			return true;
		return (this.toString().equals(cf.toString()));
	}
	
	/**
	 * Spread notification that some CustomFunction has been modified, so the
	 * GUI can adjust itself.
	 */
	public static void notifyModified() {
		AnnotationEditorPanel.notifyCustomFunctionsModified();
	}
	
	/**
	 * Manager component allowing for creating, editing, and deleting
	 * CustomFunctions
	 * 
	 * @author sautter
	 */
	public static interface Manager extends ResourceManager {
		
		/**
		 * retrieve a customFunction by its name
		 * @param name the name of the reqired customFunction
		 * @return the customFunction with the required name, or null, if there
		 *         is no such custom function
		 */
		public abstract CustomFunction getCustomFunction(String name);
	}
}
