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
package de.uka.ipd.idaho.goldenGate.util;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.MutableAnnotation;
import de.uka.ipd.idaho.goldenGate.plugins.AnnotationFilter;

/**
 * Utility library for renaming, modifying, removing, and deleting annotations
 * and annotation attributes. For details, please refer to the documentation of
 * the individual methods.
 * 
 * @author sautter
 */
public class AnnotationTools extends de.uka.ipd.idaho.gamta.util.AnnotationFilter {
	
	/**
	 * clone the Annotations matching a filter
	 * @param data the MutableAnnotation to process
	 * @param type the type of the Annotations to clone
	 * @param cloneType the type to assign to the cloned annotations
	 * @return true if and only if the MutableAnnotation was modified by this method
	 */
	public static boolean cloneAnnotations(MutableAnnotation data, String type, String cloneType) {
		return cloneAnnotations(data, data.getAnnotations(type), cloneType);
	}
	
	/**
	 * clone the Annotations matching a filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to clone
	 * @param cloneType the type to assign to the cloned annotations
	 * @return true if and only if the MutableAnnotation was modified by this method
	 */
	public static boolean cloneAnnotations(MutableAnnotation data, AnnotationFilter filter, String cloneType) {
		return cloneAnnotations(data, filter.getMatches(data), cloneType);
	}
	
	/**
	 * clone a given set of Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to check for duplicates
	 * @param cloneType the type to assign to the cloned annotations
	 * @return true if and only if the MutableAnnotation was modified by this method
	 */
	public static boolean cloneAnnotations(MutableAnnotation data, Annotation[] annotations, String cloneType) {
		
		//	duplicate annotations
		for (int a = 0; a < annotations.length; a++)
			data.addAnnotation(
					cloneType, 
					annotations[a].getStartIndex(), 
					annotations[a].size()
				).copyAttributes(annotations[a]);
		
		//	indicate modifications
		return (annotations.length != 0);
	}
	
	/**
	 * remove duplicates among the Annotations matching a filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to check for duplicates
	 * @return true if and only if the MutableAnnotation was modified by this method
	 */
	public static boolean removeDuplicates(MutableAnnotation data, AnnotationFilter filter) {
		return removeDuplicates(data, filter.getMatches(data));
	}
	
	/**
	 * remove duplicates among a given set of Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to check for duplicates
	 * @return true if and only if the MutableAnnotation was modified by this method
	 */
	public static boolean removeDuplicates(MutableAnnotation data, Annotation[] annotations) {
		boolean modified = false;
		
		//	sort Annotations in equivalence classes (by type and indices)
		HashMap duplicateClasses = new HashMap();
		for (int a = 0; a < annotations.length; a++) {
			String key = (annotations[a].getType() + " " + annotations[a].getStartIndex() + " " + annotations[a].size());
			LinkedList duplicates = ((LinkedList) duplicateClasses.get(key));
			if (duplicates == null) {
				duplicates = new LinkedList();
				duplicateClasses.put(key, duplicates);
			}
			duplicates.addLast(annotations[a]);
		}
		
		//	process equivalence classes
		for (Iterator dcit = duplicateClasses.values().iterator(); dcit.hasNext();) {
			
			//	get equivalence classe
			LinkedList duplicates = ((LinkedList) dcit.next());
			
			//	get lead Annnotation
			Annotation annotation = ((Annotation) duplicates.removeFirst());
			
			//	process duplicates
			while (duplicates.size() > 0) {
				Annotation duplicate = ((Annotation) duplicates.removeFirst());
				
				//	copy attributes, give priority to Annnotation not removed
				duplicate.copyAttributes(annotation);
				annotation.copyAttributes(duplicate);
				
				//	remove duplicate annotation
				data.removeAnnotation(duplicate);
				
				//	remember modifications
				modified = true;
			}
		}
		
		//	indicate modifications
		return modified;
	}
	
	/**
	 * remove Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to remove (specifying null will
	 *            result in all Annotations being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotations(MutableAnnotation data, String type) {
		return renameAnnotations(data, data.getAnnotations(type), null);
	}

	/**
	 * remove Annotations matching a specific Filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to remove
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotations(MutableAnnotation data, AnnotationFilter filter) {
		return renameAnnotations(data, filter.getMatches(data), null);
	}

	/**
	 * remove actual Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to remove
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotations(MutableAnnotation data, Annotation[] annotations) {
		return renameAnnotations(data, annotations, null);
	}

	/**
	 * rename Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to rename (specifying null will
	 *            result in all Annotations being removed)
	 * @param newType the type to rename the Annotations to (specifying null
	 *            will result in the annotations of the specified type being
	 *            removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotations(MutableAnnotation data, String type, String newType) {
		if ((type != null) && type.equals(newType)) return false;
		return renameAnnotations(data, data.getAnnotations(type), newType);
	}

	/**
	 * rename Annotations matching a specific Filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to rename
	 * @param newType the type to rename the Annotations to (specifying null
	 *            will result in the annotations of the specified type being
	 *            removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotations(MutableAnnotation data, AnnotationFilter filter, String newType) {
		return renameAnnotations(data, filter.getMatches(data), newType);
	}

	/**
	 * rename actual Annotations
	 * @param data the MutableAnnotation to process
	 * @param newType the type to rename the Annotations to (specifying null
	 *            will result in the annotations of the specified type being
	 *            removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotations(MutableAnnotation data, Annotation[] annotations, String newType) {
		
		//	check parameters
		if ((data == null) || ((newType != null) && (newType.trim().length() == 0))) return false;
		
		//	get and process Annotations
		for (int a = 0; a < annotations.length; a++)
			if (newType == null) data.removeAnnotation(annotations[a]);
			else annotations[a].changeTypeTo(newType);
		
		return (annotations.length != 0);
	}
	
	/**
	 * duplicate Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to duplicate
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean duplicateAnnotations(MutableAnnotation data, String type) {
		if (type == null) return false;
		return duplicateAnnotations(data, data.getAnnotations(type));
	}

	/**
	 * duplicate Annotations matching a specific Filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to duplicate
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean duplicateAnnotations(MutableAnnotation data, AnnotationFilter filter) {
		return duplicateAnnotations(data, filter.getMatches(data));
	}

	/**
	 * duplicate actual Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to duplicate
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean duplicateAnnotations(MutableAnnotation data, Annotation[] annotations) {
		return annotateAnnotations(data, annotations, null);
	}

	/**
	 * double-annotate the Tokens marked by Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to double-annotate
	 * @param annotationType the type for the new Annotations (specifying null
	 *            will result in the newAnnotations having the type of their
	 *            originals)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean annotateAnnotations(MutableAnnotation data, String type, String annotationType) {
		if (type == null) return false;
		return annotateAnnotations(data, data.getAnnotations(type), annotationType);
	}

	/**
	 * double-annotate the Tokens marked by Annotations matching a specific
	 * Filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to double-annotate
	 * @param annotationType the type for the new Annotations (specifying null
	 *            will result in the newAnnotations having the type of their
	 *            originals)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean annotateAnnotations(MutableAnnotation data, AnnotationFilter filter, String annotationType) {
		return annotateAnnotations(data, filter.getMatches(data), annotationType);
	}

	/**
	 * double-annotate the Tokens marked by actual Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to double-annotate
	 * @param annotationType the type for the new Annotations (specifying null
	 *            will result in the newAnnotations having the type of their
	 *            originals)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean annotateAnnotations(MutableAnnotation data, Annotation[] annotations, String annotationType) {
		boolean modified = false;
		for (int a = 0; a < annotations.length; a++) {
			String type = ((annotationType == null) ? annotations[a].getType() : annotationType);
			if (annotations[a] instanceof MutableAnnotation)
				data.addAnnotation(type, annotations[a].getStartIndex(), annotations[a].size()).copyAttributes(annotations[a]);
			else data.addAnnotation(type, annotations[a].getStartIndex(), annotations[a].size()).copyAttributes(annotations[a]);
			modified = true;
		}
		return modified;
	}
	
	/**
	 * delete Annotations of a specific type, including the Tokens contained in
	 * them
	 * @param data the MutableAnnotation to process
	 * @param type the type of the Annotations process
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean deleteAnnotations(MutableAnnotation data, String type) {
		if (type == null) return false;
		return deleteAnnotations(data, data.getAnnotations(type));
	}

	/**
	 * delete Annotations matching a specific Filter, including the Tokens
	 * contained in them
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to delete
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean deleteAnnotations(MutableAnnotation data, AnnotationFilter filter) {
		return deleteAnnotations(data, filter.getMatches(data));
	}

	/**
	 * delete actual Annotations
	 * @param data the MutableAnnotation to process
	 * @param annotations the Annotations to process
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean deleteAnnotations(MutableAnnotation data, Annotation[] annotations) {
		
		//	check parameters
		if (data == null) return false;
		if (annotations.length == 0) return false;
		
		//	determine token ranges to remove, and remove Annotations from document
		int start = annotations[0].getStartIndex();
		int end = annotations[0].getEndIndex();
		data.removeAnnotation(annotations[0]);
		
		ArrayList annotationList = new ArrayList();
		for (int a = 1; a < annotations.length; a++) {
			if (annotations[a].getStartIndex() > end) {
				annotationList.add(Gamta.newAnnotation(data, "delete", start, (end - start)));
				start = annotations[a].getStartIndex();
				end = annotations[a].getEndIndex();
			}
			else end = annotations[a].getEndIndex();
			data.removeAnnotation(annotations[a]);
		}
		annotationList.add(Gamta.newAnnotation(data, "delete", start, (end - start)));
		
		//	delete Annotation Tokens
		boolean modified = false;
		for (int a = (annotationList.size() - 1); a != -1; a--) {
			Annotation annotation = ((Annotation) annotationList.get(a));
			data.removeTokensAt(annotation.getStartIndex(), annotation.size());
			modified = true;
		}
		
		return modified;
	}
	
	/**
	 * remove an attribute of Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to process (specifying null will
	 *            result in all Annotations being processed)
	 * @param attribute the attribute to remove (specifying null will result in
	 *            all attributes being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotationAttribute(MutableAnnotation data, String type, String attribute) {
		return renameAnnotationAttribute(data.getAnnotations(type), attribute, null);
	}

	/**
	 * remove an attribute of Annotations matching a filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter the matches of which to process
	 * @param attribute the attribute to remove (specifying null will result in
	 *            all attributes being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotationAttribute(MutableAnnotation data, AnnotationFilter filter, String attribute) {
		return renameAnnotationAttribute(filter.getMatches(data), attribute, null);
	}

	/**
	 * remove an attribute from specific Annotations
	 * @param annotations the Annotations to process
	 * @param attribute the attribute to remove (specifying null will result in
	 *            all attributes being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean removeAnnotationAttribute(Annotation[] annotations, String attribute) {
		return renameAnnotationAttribute(annotations, attribute, null);
	}

	/**
	 * rename an attribute of Annotations of a specific type
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to process (specifying null will
	 *            result in all Annotations being processed)
	 * @param attribute the attribute to rename (specifying null will result in
	 *            all attributes being removed)
	 * @param newAttribute the new name for the attribute (specifying null will
	 *            result in the specified attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotationAttribute(MutableAnnotation data, String type, String attribute, String newAttribute) {
		return renameAnnotationAttribute(data.getAnnotations(type), attribute, newAttribute);
	}

	/**
	 * rename an attribute of Annotations matching a filter
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter the matches of which to process
	 * @param attribute the attribute to rename (specifying null will result in
	 *            all attributes being removed)
	 * @param newAttribute the new name for the attribute (specifying null will
	 *            result in the specified attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotationAttribute(MutableAnnotation data, AnnotationFilter filter, String attribute, String newAttribute) {
		return renameAnnotationAttribute(filter.getMatches(data), attribute, newAttribute);
	}

	/**
	 * rename an attribute of Annotations of a specific type
	 * @param annotations the Annotations to process
	 * @param attribute the attribute to rename (specifying null will result in
	 *            all attributes being removed)
	 * @param newAttribute the new name for the attribute (specifying null will
	 *            result in the specified attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean renameAnnotationAttribute(Annotation[] annotations, String attribute, String newAttribute) {
		
		//	check parameters
		if (((attribute != null) && (attribute.equals(newAttribute) || (attribute.trim().length() == 0))) || ((newAttribute != null) && (newAttribute.length() == 0))) return false;
		
		//	process Annotations
		boolean modified = false;
		for (int a = 0; a < annotations.length; a++) {
			if (attribute == null) {
				modified = (modified || (annotations[a].getAttributeNames().length != 0));
				annotations[a].clearAttributes();
			}
			else if (annotations[a].hasAttribute(attribute)) {
				Object attributeValue = annotations[a].removeAttribute(attribute);
				if (newAttribute != null) annotations[a].setAttribute(newAttribute, attributeValue);
				modified = true;
			}
		}
		
		return modified;
	}
	
	/**
	 * add an attribute to Annotations of a specific type (will not overwrite
	 * existing, to force overwriting, use the setAnnotationAttribute() methods)
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to process (specifying null will
	 *            result in all Annotations being processed)
	 * @param attributeName the name for the attribute to add
	 * @param attributeValue the value of the attribute to add
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean addAnnotationAttribute(MutableAnnotation data, String type, String attributeName, Object attributeValue) {
		return addAnnotationAttribute(data.getAnnotations(type), attributeName, attributeValue);
	}

	/**
	 * add an attribute to Annotations matching a filter (will not overwrite
	 * existing, to force overwriting, use the setAnnotationAttribute() methods)
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to process
	 * @param attributeName the name for the attribute to add
	 * @param attributeValue the value of the attribute to add
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean addAnnotationAttribute(MutableAnnotation data, AnnotationFilter filter, String attributeName, Object attributeValue) {
		return addAnnotationAttribute(filter.getMatches(data), attributeName, attributeValue);
	}

	/**
	 * add an attribute to specific Annotations (will not overwrite existing, to
	 * force overwriting, use the setAnnotationAttribute() methods)
	 * @param annotations the Annotations to process
	 * @param attributeName the name for the attribute to add
	 * @param attributeValue the value of the attribute to add
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean addAnnotationAttribute(Annotation[] annotations, String attributeName, Object attributeValue) {
		
		//	check parameters
		if ((attributeName == null) || (attributeName.trim().length() == 0) || (attributeValue == null)) return false;
		
		//	process Annotations
		boolean modified = false;
		for (int a = 0; a < annotations.length; a++) {
			if (!annotations[a].hasAttribute(attributeName)) {
				annotations[a].setAttribute(attributeName, attributeValue);
				modified = true;
			}
		}
		
		return modified;
	}
	
	/**
	 * set an attribute to Annotations of a specific type (no matter if it was
	 * set before or not)
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to process (specifying null will
	 *            result in all Annotations being processed)
	 * @param attributeName the name of the attribute to set
	 * @param attributeValue the value to set the attribute to
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean setAnnotationAttribute(MutableAnnotation data, String type, String attributeName, Object attributeValue) {
		return setAnnotationAttribute(data.getAnnotations(type), attributeName, attributeValue);
	}

	/**
	 * set an attribute to Annotations matching a filter (no matter if it was
	 * set before or not)
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to process
	 * @param attributeName the name of the attribute to set
	 * @param attributeValue the value to set the attribute to
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean setAnnotationAttribute(MutableAnnotation data, AnnotationFilter filter, String attributeName, Object attributeValue) {
		return setAnnotationAttribute(filter.getMatches(data), attributeName, attributeValue);
	}

	/**
	 * set an attribute to specific Annotations (no matter if it was set before
	 * or not)
	 * @param annotations the Annotations to process
	 * @param attributeName the name of the attribute to set
	 * @param attributeValue the value to set the attribute to
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean setAnnotationAttribute(Annotation[] annotations, String attributeName, Object attributeValue) {
		
		//	check parameters
		if ((attributeName == null) || (attributeName.trim().length() == 0) || (attributeValue == null)) return false;
		
		//	process Annotations
		boolean modified = false;
		for (int a = 0; a < annotations.length; a++) {
			annotations[a].setAttribute(attributeName, attributeValue);
			modified = true;
		}
		
		return modified;
	}
	
	/**
	 * change an attribute of Annotations of a specific type (will not be added
	 * if not present)
	 * @param data the MutableAnnotation to process
	 * @param type the type of Annotations to process (specifying null will
	 *            result in all Annotations being processed)
	 * @param attributeName the name of the attribute to change
	 * @param oldValue the attribute value to replace (specifying null will
	 *            result in all values being replaced with newValue)
	 * @param newValue the value to set the attribute to (specifying null will
	 *            result in the attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean changeAnnotationAttribute(MutableAnnotation data, String type, String attributeName, Object oldValue, Object newValue) {
		return changeAnnotationAttribute(data.getAnnotations(type), attributeName, oldValue, newValue);
	}

	/**
	 * change an attribute of Annotations matching a specific filter (will not
	 * be added if not present)
	 * @param data the MutableAnnotation to process
	 * @param filter the AnnotationFilter whose matches to process
	 * @param attributeName the name of the attribute to change
	 * @param oldValue the attribute value to replace (specifying null will
	 *            result in all values being replaced with newValue)
	 * @param newValue the value to set the attribute to (specifying null will
	 *            result in the attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean changeAnnotationAttribute(MutableAnnotation data, AnnotationFilter filter, String attributeName, Object oldValue, Object newValue) {
		return changeAnnotationAttribute(filter.getMatches(data), attributeName, oldValue, newValue);
	}

	/**
	 * change an attribute specific Annotations (will not be added if not
	 * present)
	 * @param annotations the Annotations to process
	 * @param attributeName the name of the attribute to change
	 * @param oldValue the attribute value to replace (specifying null will
	 *            result in all values being replaced with newValue)
	 * @param newValue the value to set the attribute to (specifying null will
	 *            result in the attribute being removed)
	 * @return true if and only if the MutableAnnotation was modified by this
	 *         method
	 */
	public static boolean changeAnnotationAttribute(Annotation[] annotations, String attributeName, Object oldValue, Object newValue) {
		
		//	check parameters
		if ((attributeName == null) || (attributeName.trim().length() == 0)) return false;
		
		//	process Annotations
		boolean modified = false;
		for (int a = 0; a < annotations.length; a++) {
			if (annotations[a].hasAttribute(attributeName)) {
				Object value = annotations[a].getAttribute(attributeName, null);
				if ((oldValue == null) || oldValue.equals(value)) {
					if (newValue == null) annotations[a].removeAttribute(attributeName);
					else annotations[a].setAttribute(attributeName, newValue);
					modified = true;
				}
			}
		}
		
		return modified;
	}
}
