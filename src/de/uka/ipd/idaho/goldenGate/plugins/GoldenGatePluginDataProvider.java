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
import java.io.OutputStream;
import java.net.URL;

/**
 * A data provider gives GoldenGATE plugin components access to their data, no
 * matter where it is actually stored.
 * 
 * @author sautter
 */
public interface GoldenGatePluginDataProvider {
	
	/**
	 * Test if a data object is available.
	 * @param dataName the name of the data object to test
	 * @return true if the data object with the specified name exists and can be
	 *         read
	 */
	public abstract boolean isDataAvailable(String dataName);

	/**
	 * Obtain an input stream for an arbitrary data object.
	 * @param dataName the name of the data object
	 * @return an input stream the data object with the specified name can be
	 *         read from, or null, if there is no such data object
	 * @throws IOException if any occurs while establishing the stream
	 */
	public abstract InputStream getInputStream(String dataName) throws IOException;

	/**
	 * Obtain a URL for an arbitrary data object. If the specified data name
	 * represents an absolute URL (indicated by specifying a protocol), the data
	 * provider handles it as such. Otherwise, it returns a URL giving access to
	 * some data object it hosts. If allowWebAccess() returns false, however,
	 * the data provider may throw an IOException if the argument String is an
	 * absolute URL. The concrete behavior is implementation dependent.
	 * @param dataName the name of the data object
	 * @return a URL pointing to the data object with the specified name, or
	 *         null, if there is no such data object
	 * @throws IOException if any occurs while creating the url
	 */
	public abstract URL getURL(String dataName) throws IOException;

	/**
	 * Test if a data object is editable.
	 * @param dataName the name of the data object to test
	 * @return true if the data object with the specified name can be written,
	 *         i.e. it exists and can be modified, or it can be created
	 */
	public abstract boolean isDataEditable(String dataName);
	
	/**
	 * Obtain an output stream to an arbitrary data object.
	 * @param dataName the name of the data object
	 * @return an output stream the data object with the specified name can be
	 *         stored to, or null, if the data object cannot be written to
	 * @throws IOException if any occurs while establishing the stream
	 */
	public abstract OutputStream getOutputStream(String dataName) throws IOException;

	/**
	 * Delete a data object.
	 * @param dataName the name of the data object to delete
	 * @return true if the data object was deleted, or if it didn't exist at all
	 */
	public abstract boolean deleteData(String dataName);

	/**
	 * @return an array holding the names of the data objects available from
	 *         this data provider
	 */
	public abstract String[] getDataNames();

	/**
	 * Check if data objects are editable in general. Note: If this method
	 * returns true, it does not imply that every data object is editable. If it
	 * returns false, though, it does (should) imply that no data object is
	 * editable.
	 * @return true if data objects can (generally) be edited, created, and
	 *         deleted
	 */
	public abstract boolean isDataEditable();

	/**
	 * Ask if accessing the web is allowed.
	 * @return true if a GoldenGATE plugin is generally allowed to access the
	 *         network and web
	 */
	public abstract boolean allowWebAccess();
	
	/**
	 * Retrieve the absolute path of the physical storage location this data
	 * provider works on. The string returned should not end with a slash, even
	 * though it points to folders rather than files. This is due to the
	 * behavior of java.io.File objects, whose absolute path never includes the
	 * terminal slash, even if they point to folders. Further, it should contain
	 * forward slashes instead of backslashes to facilitate a unified handling.
	 * @return the absolute path of the data provider, to be used in comparison
	 */
	public abstract String getAbsolutePath();
	
	/**
	 * Test whether this data provider is the same as another data provider. Two
	 * data providers are only the same if they point to the same physical
	 * storage location. This method may return false even though the physical
	 * storage location of two data providers actually is the same, for instance
	 * if two different URLs point to the same physical storage location.
	 * @param dp the data provider to compare this one to
	 * @return true if the argument data provider surely points to the same
	 *         storage physical storage location as this data provider
	 */
	public abstract boolean equals(GoldenGatePluginDataProvider dp);
	
	/**
	 * Compute the hash code of the data provider, which should be the hash code
	 * of the string returned by getAbsolutePath().
	 * @return the hash code of the data provider
	 * @see java.lang.Object#hashCode()
	 */
	public abstract int hashCode();
}
