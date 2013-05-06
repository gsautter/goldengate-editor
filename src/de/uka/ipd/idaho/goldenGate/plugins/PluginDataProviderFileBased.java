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

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;

import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * File based implementation of a GoldenGatePluginDataProvider. This data
 * provider maps all names to the file system, relative to the base path it is
 * constructed on. The InputStreams and OutputStreams returned bez this data
 * provider close automatically after 30 seconds of inactivity, preventing
 * resource leaks if a plugins does not close the strams itself. Data items up
 * to a maximum size of 2048 byte are cached as byte arrays for lowering disc
 * access rate for frequently used small data items.
 * 
 * @author sautter
 */
public class PluginDataProviderFileBased extends AbstractGoldenGatePluginDataProvider {
	
	private final File basePath;
	private final int basePathLength;
	private String[] fileList = null;
	
	private final boolean dataEditable;
	private final boolean allowWebAccess;
	
	private int cacheMaxBytes = 2048;
	private HashMap byteCache = new HashMap();
	
	/**
	 * Constructor creatig a data provider with data editable, and allowing web access
	 * @param basePath the folder to use as the basis for this data provider
	 */
	public PluginDataProviderFileBased(File basePath) {
		this(basePath, true, true);
	}

	/**
	 * Constructor creatig a data provider with data editable
	 * @param basePath the folder to use as the basis for this data provider
	 * @param allowWebAccess allow plugins access the web?
	 */
	public PluginDataProviderFileBased(File basePath, boolean allowWebAccess) {
		this(basePath, true, allowWebAccess);
	}

	/**
	 * Constructor
	 * @param basePath the folder to use as the basis for this data provider
	 * @param dataEditable allow the plugin using the data provider for modifying data?
	 * @param allowWebAccess allow plugins access the web?
	 */
	public PluginDataProviderFileBased(File basePath, boolean dataEditable, boolean allowWebAccess) {
		this.basePath = basePath;
		if (!this.basePath.exists()) this.basePath.mkdirs();
		this.basePathLength = this.basePath.getAbsolutePath().length();
		this.dataEditable = dataEditable;
		this.allowWebAccess = allowWebAccess;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
	 */
	public String[] getDataNames() {
		this.fileList = this.readFileList(this.basePath);
		return this.fileList;
	}
	
	//	list files recursively
	private String[] readFileList(File directory) {
		StringVector resultFiles = new StringVector();
		File[] files = directory.listFiles();
		for (int f = 0; f < files.length; f++) {
			if (files[f].isDirectory() && !files[f].equals(directory))
				resultFiles.addContent(this.readFileList(files[f]));
			else resultFiles.addElement(files[f].getAbsolutePath().substring(this.basePathLength + 1).replaceAll("\\\\", "\\/"));
		}
		return resultFiles.toStringArray();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		
		//	cache lookup
		if (this.byteCache.containsKey(dataName))
			return new ByteArrayInputStream((byte[]) this.byteCache.get(dataName));
		
		//	create stream
		BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this.getFile(dataName)), this.cacheMaxBytes);
		
		//	read up to cache limit or end of stream
		bis.mark(this.cacheMaxBytes + 1);
		byte[] testBytes = new byte[this.cacheMaxBytes];
		int read = 0;
		int tb;
		while (((tb = bis.read()) != -1) && (read < testBytes.length))
			testBytes[read++] = ((byte) tb);
		
		//	put file in cache if small enough
		if ((0 < read) && (read < this.cacheMaxBytes)) {
			byte[] bytes = new byte[read];
			System.arraycopy(testBytes, 0, bytes, 0, read);
			this.byteCache.put(dataName, bytes);
			bis.close();
			return new ByteArrayInputStream(bytes);
		}
		
		//	return reader for original stream otherwise
		else {
			bis.reset();
			return new TimeoutInputStream(bis, dataName);
		}
	}

	/**
	 * wrapper for input streams to automatically close the wrapped stream if inactive for more than 30 seconds
	 * 
	 * @author sautter
	 */
	private class TimeoutInputStream extends FilterInputStream {
		private String dataName;
		
		private long lastRead = System.currentTimeMillis();
		
		TimeoutInputStream(InputStream in, String dn) {
			super(in);
			this.dataName = dn;
			
			//	start watchdog
			new Thread() {
				public void run() {
					
					//	watch out if stream closed externally
					while (lastRead != -1) {
						
						//	close stream if timeout expired
						if ((System.currentTimeMillis() - lastRead) > 30000) {
							try {
								System.out.println("Auto-Closing InputStream for '" + dataName + "'");
								close();
							} catch (IOException ioe) {}
							return;
						}
						
						//	wait 1 second before checking again
						try {
							sleep(1000);
						} catch (InterruptedException ie) {}
						
					}
				}
			}.start();
		}
		
		public void close() throws IOException {
			this.lastRead = -1;
			super.close();
		}

		public int read() throws IOException {
			this.lastRead = System.currentTimeMillis();
			return super.read();
		}

		protected void finalize() throws Throwable {
			if (this.lastRead != -1) {
				System.out.println("Auto-Closing InputStream on Finalization for '" + this.dataName + "'");
				this.close();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.getFile(dataName).exists();
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		if ((dataName.indexOf("://") == -1) && !dataName.toLowerCase().startsWith("file:/")) {
			this.byteCache.remove(dataName);
			return this.getFile(dataName).toURI().toURL();
		}
		else return new URL(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable()
	 */
	public boolean isDataEditable() {
		return this.dataEditable;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		return this.dataEditable;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return this.allowWebAccess;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		if (this.dataEditable) {
			File dataFile = this.getFile(dataName);
			if (dataFile.exists()) {
				dataFile.renameTo(new File(dataFile.getPath() + "." + System.currentTimeMillis() + ".old"));
				dataFile = this.getFile(dataName);
			}
			else {
				dataFile.getParentFile().mkdirs();
				dataFile.createNewFile();
			}
			this.byteCache.remove(dataName);
			return new TimeoutOutputStream(new FileOutputStream(dataFile), dataName);
		}
		else throw new IOException("Write access denied, this data provider is read-only.");
	}
	
	/**
	 * wrapper for output streams to automatically flush and close the wrapped stream if inactive for more than 30 seconds
	 * 
	 * @author sautter
	 */
	private class TimeoutOutputStream extends FilterOutputStream {
		private String dataName; 
		
		private long lastWritten = System.currentTimeMillis();
		
		TimeoutOutputStream(OutputStream out, String dn) {
			super(out);
			this.dataName = dn;
			
			//	start watchdog
			new Thread() {
				public void run() {
					
					//	watch out if stream closed externally
					while (lastWritten != -1) {
						
						//	close stream if timeout expired
						if ((System.currentTimeMillis() - lastWritten) > 30000) {
							try {
								System.out.println("Auto-Closing OutputStream for '" + dataName + "'");
								flush();
								close();
							} catch (IOException ioe) {}
							return;
						}
						
						//	wait 1 second before checking again
						try {
							sleep(1000);
						} catch (InterruptedException ie) {}
						
					}
				}
			}.start();
		}
		
		public void close() throws IOException {
			this.lastWritten = -1;
			fileList = null; // set file list to null in order to force refresh
			super.close();
		}

		public void write(int b) throws IOException {
			this.lastWritten = System.currentTimeMillis();
			super.write(b);
		}
		
		protected void finalize() throws Throwable {
			if (this.lastWritten != -1) {
				System.out.println("Auto-Closing OutputStream on Finalization for '" + this.dataName + "'");
				this.flush();
				this.close();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		if (this.dataEditable) {
			File file = this.getFile(dataName);
			this.byteCache.remove(dataName);
			this.fileList = null; // set file list to null in order to force refresh
			return (!file.exists() || file.delete());
		}
		else return false;
	}

	private File getFile(String dataName) {
		return new File(this.basePath, dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		if (this.absolutePath == null) {
			String path = this.basePath.getAbsolutePath().replaceAll("\\\\", "/");
			StringBuffer cleanPath = new StringBuffer();
			for (int c = 0; c < path.length(); c++) {
				char ch = path.charAt(c);
				if (ch == '/') {
					if (path.startsWith("./", (c+1)))
						c++; // ignore current slash and jump dot
					else cleanPath.append(ch);
				}
				else if (ch == '\\') {
					if (path.startsWith("./", (c+1)) || path.startsWith(".\\", (c+1)))
						c++; // ignore current slash and jump dot
					else cleanPath.append('/');
				}
				else cleanPath.append(ch);
			}
			this.absolutePath = cleanPath.toString();
		}
		return this.absolutePath;
	}
	private String absolutePath = null;
}
