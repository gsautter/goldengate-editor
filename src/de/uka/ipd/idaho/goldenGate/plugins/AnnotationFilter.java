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
import de.uka.ipd.idaho.gamta.QueriableAnnotation;

/**
 * An annotation filter can extract annotations from a given parent annotation.
 * 
 * @author sautter
 */
public interface AnnotationFilter extends Resource {
	
	/**
	 * Retrieve all Annotations from some Annotation that match the filter
	 * @param data the MutableAnnotation to retrieve the Annotation from
	 * @return all Annotations of the specified QueriableAnnotation that match
	 *         the filter
	 */
	public abstract QueriableAnnotation[] getMatches(QueriableAnnotation data);

	/**
	 * Retrieve all MutabelAnnotations from some MutableAnnotation that match
	 * the filter
	 * @param data the MutableAnnotation to retrieve the Annotation from
	 * @return all Annotations of the specified MutableAnnotation that match the
	 *         filter
	 */
	public abstract MutableAnnotation[] getMutableMatches(MutableAnnotation data);

	/**
	 * Test if an Annotation passes the filter
	 * @param annotation the Annotation to test
	 * @return true if and only if the specified Annotation passes the filter
	 */
	public abstract boolean accept(Annotation annotation);
}
