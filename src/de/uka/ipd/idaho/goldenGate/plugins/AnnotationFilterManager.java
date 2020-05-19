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


import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.MutableAnnotation;

public interface AnnotationFilterManager extends ResourceManager {
	
	/**
	 * retrieve a AnnotationFilter provided by this AnnotationFilterManager
	 * @param name the name of the desired AnnotationFilter
	 * @return the AnnotationFilter with the specified name, or null if there is
	 *         no such AnnotationFilter
	 */
	public abstract AnnotationFilter getAnnotationFilter(String name);

	/**
	 * tell this AnnotationFilterManager to create an AnnotationFilter
	 * @return the name of the newly created AnnotationFilter, or null if no
	 *         AnnotationFilter was created
	 */
	public abstract String createAnnotationFilter();

	/**
	 * tell this AnnotationFilterManager to open an AnnotationFilter for editing
	 * @param name the name of the AnnotationFilter to edit
	 */
	public abstract void editAnnotationFilter(String name);

	/**
	 * tell this AnnotationFilterManager to open for editing
	 */
	public abstract void editAnnotationFilters();

	/**
	 * apply a AnnotationFilter provided by this AnnotationFilterManager to a
	 * MutableAnnotation. The idea of this method is to give the
	 * AnnotationFilter the chance of being run under circumstances which make
	 * use of the special knowledge only the provider has about the habbits of
	 * its AnnotationFilter
	 * @param filterName the name of the AnnotationFilter to be applied (if
	 *            null, a selector should be presented to the user)
	 * @param data the MutableAnnotation to apply the AnnotationFilter to
	 * @param interactive do interactive processing (allow intermediate dialogs
	 *            or user interaction in general)?
	 * @return an array containing the Annotations that passed the filter
	 */
	public abstract Annotation[] applyAnnotationFilter(String filterName, MutableAnnotation data, boolean interactive);
}
