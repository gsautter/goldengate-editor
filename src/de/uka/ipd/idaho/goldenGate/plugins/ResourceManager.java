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


import de.uka.ipd.idaho.goldenGate.util.ResourceSelector;

/**
 * @author sautter
 *
 */
public interface ResourceManager extends GoldenGatePlugin {
	
	/**
	 * @return the names of all Resources managed by this ResourceManager
	 */
	public abstract String[] getResourceNames();
	
	/**
	 * Get the names of the data objects required for a resource (necessary for
	 * configuration export).<br>
	 * Note: Use a terminal slash with some data object name to enable prefix
	 * matches. In other words, appending a slash ('/') to a name is a wildcard
	 * for all data object in a folder with that name. <br>
	 * Note: Resource managers should identify themselves by appending '@' plus
	 * their fully qualified class name to the names they return. This allows
	 * for resolving resources from dependent resource managers more easily.
	 * @param name the name of the resource to obtain a data name list for
	 * @return the names of all data objects necessary for providing the
	 *         resource with the specified name
	 */
	public abstract String[] getDataNamesForResource(String name);
	
	/**
	 * Get the names of the resources a resource depends on (necessary for
	 * configuration export). <br>
	 * Note: Resource managers should identify themselves by appending '@' plus
	 * their fully qualified class name to the resource names they return. This
	 * allows for resolving resources from dependent resource managers more
	 * easily.
	 * @param name the name of the resource to obtain required resources for
	 * @param recourse get the required resource names recoursively?
	 * @return the names of all resources necessary for providing the resource
	 *         with the specified name
	 */
	public abstract String[] getRequiredResourceNames(String name, boolean recourse);
	
	/**
	 * @return a nice name for the type of resources provided by this
	 *         ResourceManager
	 */
	public abstract String getResourceTypeLabel();
	
	/**
	 * Retrieve the label for this resource manager's entry in the Tools menu of
	 * the main window through which the resources are accessible.
	 * @return the label for this resource manager's entry in the Tools menu of
	 *         the main window through which the resources are accessible, or
	 *         null, if this resource manager does not want its resources to to
	 *         be accessible through the Tools menu
	 */
	public abstract String getToolsMenuLabel();
	
	/**
	 * Produce a selector JPanel to place in the GUI.
	 * @param label the label String for the panel
	 * @return a selector for the type of Resource managed by this
	 *         ResourceManager
	 */
	public abstract ResourceSelector getSelector(String label);
	
	/**
	 * Produce a selector JPanel to place in the GUI.
	 * @param label the label String for the panel
	 * @param initialSelection the name of the Resource initially selected
	 * @return a selector for the type of Resource managed by this
	 *         ResourceManager
	 */
	public abstract ResourceSelector getSelector(String label, String initialSelection);
}
