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


import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JMenuItem;

import de.uka.ipd.idaho.easyIO.settings.Settings;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;

/**
 * A configuration object provides all the plug-in extensions and settings for
 * the GoldenGATE editor, abstracting from where these resources are actually
 * loaded from. Further, it abstracts IO operations from the backing data store,
 * facilitation GoldenGATE to run the same way in different environments.
 * 
 * @author sautter
 */
public interface GoldenGateConfiguration extends GoldenGateConstants {
	
	/**
	 * Basic descriptor for a GoldenGATE Configuration, located either on the
	 * local file system, or at a given URL.
	 * 
	 * @author sautter
	 */
	public static class ConfigurationDescriptor {
		
		/** the root URL of the configuration, null for configurations on the local file system */
		public final String host;
		
		/** the name of the configuration */
		public final String name;
		
		/** the timestamp of the configuration, i.e., the time of the most recent update to either of the data items it contains */
		public final long timestamp;
		
		/**
		 * Constructor
		 * @param host the root URL of the configuration, null for
		 *            configurations on the local file system
		 * @param name the name of the configuration
		 * @param timestamp the timestamp of the configuration
		 */
		public ConfigurationDescriptor(String host, String name, long timestamp) {
			this.host = host;
			this.name = name;
			this.timestamp = timestamp;
		}
	}
	
	/**
	 * the name of the file that contains an XML descriptor of a configuration
	 */
	public static final String DESCRIPTOR_FILE_NAME = "configuration.xml";
	
	/**
	 * the name of the one file in a folder that contains a listing of the other
	 * files and sub folders in the same folder (to use if a web server does not
	 * list folder contents)
	 */
	public static final String FILE_INDEX_NAME = "files.txt";

	/**
	 * the name of the file in a configuration base folder that contains the
	 * timestamp of the configuration
	 */
	public static final String TIMESTAMP_NAME = "timestamp.txt";

	/**
	 * @return the name of this configuration
	 */
	public abstract String getName();

	/**
	 * Retrieve the path of this configuration, relative to the root path of the
	 * surrounding GoldenGATE installation. If a configuration does no have a
	 * path, this method should return null. If the path is equal to the root
	 * path, the return value should be '.'.
	 * @return the relative path of this configuration
	 */
	public abstract String getPath();
	
	/**
	 * Retrieve the absolute root path of the physical storage location this
	 * configuration works on. The string returned should not end with a slash,
	 * even though it points to folders rather than files. This is due to the
	 * behavior of java.io.File objects, whose absolute path never includes the
	 * terminal slash, even if they point to folders. Further, it should contain
	 * forward slashes instead of backslashes to facilitate a unified handling.
	 * @return the absolute path of the configuration, to be used in comparison
	 */
	public abstract String getAbsolutePath();
	
//	/**
//	 * Make the configuration object know the GoldenGATE instance running on top
//	 * of it.
//	 * @param gg the GoldenGATE instance running on top of this configuration
//	 * @throws IOException if invoked more than once
//	 */
//	public abstract void setGoldenGateInstance(GoldenGATE gg) throws IOException;
//	
//	/**
//	 * test if this configuration is editable
//	 * @return true if this configuration object can be edited
//	 */
//	public abstract boolean isEditable();
//
	/**
	 * Test if the underlying data of this configuration is editable
	 * @return true if this configuration object can be edited
	 */
	public abstract boolean isDataEditable();

	/**
	 * Ask if acccessing the web is allowed for this configuration
	 * @return true if GoldenGATE plugin componentes are generally allowed to
	 *         access the network and web when working with this configuration
	 */
	public abstract boolean allowWebAccess();
	
	/**
	 * Ask if this configuration is a master configuration. Starting with a
	 * master configuration will cause GoldenGATE to show the main menu entries
	 * of resource managers and the Window menu, effectively facilitating
	 * configuring the editor.
	 * @return true if this configuration is a master configuration
	 */
	public abstract boolean isMasterConfiguration();
	
	/**
	 * Retrieve additional items for the File menu that allow for accessing
	 * implementation-specific functions of the configuration.
	 * @return an array holding implementation specific entries for the File
	 *         menu
	 */
	public abstract JMenuItem[] getFileMenuItems();
	
	/**
	 * Retrieve additional items for the Window menu that allow for accessing
	 * implementation-specific functions of the configuration. The items
	 * returned by this method are only included in the GUI if
	 * isMasterConfiguration() returns true.
	 * @return an array holding implementation specific entries for the Window
	 *         menu
	 */
	public abstract JMenuItem[] getWindowMenuItems();
	
	/**
	 * Get the plugins available in this configuration, instantiated and with
	 * their data path set
	 * @return an array holding the operational plugins available in this
	 *         configuration
	 */
	public abstract GoldenGatePlugin[] getPlugins();

	/*
	 * TODO: establish local settings able to reflect individual users' editing
	 * preferences (fonts, show tags for new Annotations, etc)
	 */
	/**
	 * @return the detail settings for the main editor
	 */
	public abstract Settings getSettings();

	/**
	 * Store the detail settings for the main editor (may do nothing if
	 * isEditable() returns false)
	 * @param settings the Settings to store
	 * @throws IOException if any occurs storing the settings
	 */
	public abstract void storeSettings(Settings settings) throws IOException;
	
	/**
	 * @return the base URL for the GoldenGATE help
	 */
	public abstract String getHelpBaseURL();
	
	/**
	 * Write an entry to the log file (this method may be implemented to do
	 * nothing if logging is not desired or not possible)
	 * @param entry the entry to write
	 */
	public abstract void writeLog(String entry);
	
	/**
	 * @return the icon to use for window decoration
	 */
	public abstract Image getIconImage();
	
	/**
	 * Finalize the configuration (clean up data, close open streams, etc)
	 * @throws Throwable
	 */
	public abstract void finalize() throws Throwable;
	
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
	 * represents an absolute URL (indicated by specifying a protocol), it is
	 * handled as such. Otherwise, it returns a URL giving access to some data
	 * object it hosts. If allowWebAccess() returns false and the argument data
	 * name is an absolute URL, however, an IOException should be thrown. The
	 * concrete behavior is implementation dependent.
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
}
