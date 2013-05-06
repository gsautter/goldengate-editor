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


public interface DocumentFormatProvider extends ResourceManager {
	
	/**	@return	the file extensions indicating that this DocumentFormat is given 
	 */
	public abstract String[] getFileExtensions();
	
	/** obtain the format for some specific file extension
	 * @param	fileExtension	the file extension to obtain the DocumentFormat for
	 * @return the DocumentFormat for the specified file extension, or null, if this DocumentFormatProvider does not provide a format for the specified file extension
	 */
	public abstract DocumentFormat getFormatForFileExtension(String fileExtension);
	
	/**	@return	the names of the DocumentFormats provided by this DocumentFormatProvider suited for loading documents
	 */
	public abstract String[] getLoadFormatNames();
	
	/**	@return	the names of the DocumentFormats provided by this DocumentFormatProvider suited for saving documents
	 */
	public abstract String[] getSaveFormatNames();
	
	/** obtain a DocumentFormat by its name
	 * @param	formatName	the name of the desired DocumentFormat
	 * @return the DocumentFormat with the specified name, or null, if there is no such DocumentFormat
	 */
	public abstract DocumentFormat getFormatForName(String formatName);
	
	/**	@return	an array containing the file filters to appear in a load dialog
	 */
	public abstract DocumentFormat[] getLoadFileFilters();
	
	/**	@return	an array containing the file filters to appear in a save dialog
	 */
	public abstract DocumentFormat[] getSaveFileFilters();
}
