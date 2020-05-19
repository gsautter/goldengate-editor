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
package de.uka.ipd.idaho.goldenGate.observers;


import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.goldenGate.plugins.Resource;

/**
 * An observer for Annotations being added, removed, and renamed in a document.
 * All arguments handed over to methods of this interface are strictly
 * immutable. That means any attempt of changing the type, the value, or an
 * attribute of an Annotation will result in an exception being thrown.
 * 
 * @author sautter
 */
public interface AnnotationObserver {
	
	/**
	 * Receive notification that an Annotation was added to a document. The
	 * argument resource indicates the origin of the change, with null
	 * indicating that the user made the change in the main editor window.
	 * @param doc the document the Annotation was added to
	 * @param annotation the Annotation that was added to the document
	 * @param source the GoldenGATE Resource that performed the modification
	 */
	public abstract void annotationAdded(QueriableAnnotation doc, Annotation annotation, Resource source);
	
	/**
	 * Receive notification that an Annotation was removed from a document. The
	 * argument resource indicates the origin of the change, with null
	 * indicating that the user made the change in the main editor window.
	 * @param doc the document the Annotation was removed from
	 * @param annotation the Annotation that was removed from the document
	 * @param source the GoldenGATE Resource that performed the modification
	 */
	public abstract void annotationRemoved(QueriableAnnotation doc, Annotation annotation, Resource source);
	
	/**
	 * Receive notification that the type of an Annotation has changed. The
	 * argument resource indicates the origin of the change, with null
	 * indicating that the user made the change in the main editor window.
	 * @param doc the document the Annotation belongs to
	 * @param annotation the Annotation whose type changed
	 * @param oldType the type of the Annotation before the change
	 * @param source the GoldenGATE Resource that performed the modification
	 */
	public abstract void annotationTypeChanged(QueriableAnnotation doc, Annotation annotation, String oldType, Resource source);
	
	/**
	 * Receive notification that the an attribute of an Annotation has changed.
	 * The argument resource indicates the origin of the change, with null
	 * indicating that the user made the change in the main editor window.
	 * @param doc the document the Annotation belongs to
	 * @param annotation the Annotation whose type changed
	 * @param attributeName the name of the attribute that changed (null
	 *            indicates that multiple attributes have changed at once, e.g.
	 *            through clearAttributes() or copyAttributes(), oldValue will
	 *            be null as well in these cases)
	 * @param oldValue the value of the attribute before the change (the new
	 *            value is available from the Annotation)
	 * @param source the GoldenGATE Resource that performed the modification
	 */
	public abstract void annotationAttributeChanged(QueriableAnnotation doc, Annotation annotation, String attributeName, Object oldValue, Resource source);
}
