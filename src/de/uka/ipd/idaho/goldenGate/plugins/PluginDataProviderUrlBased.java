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
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.TreeSet;


/**
 * URL based implementation of a GoldenGatePluginDataProvider. This data
 * provider maps all names relative to the base URL it is constructed on. The
 * InputStreams and OutputStreams returned bez this data provider close
 * automatically after 30 seconds of inactivity, preventing resource leaks if a
 * plugins does not close the strams itself. This data provider naturally allows
 * web access, but no data modification. Data items up to a maximum size of 2048
 * byte are cached as byte arrays for lowering network access rate for
 * frequently used small data items.
 * 
 * @author sautter
 */
public class PluginDataProviderUrlBased extends AbstractGoldenGatePluginDataProvider {
	
	private final String baseUrl;
	private final TreeSet dataNames = new TreeSet();
	
	private int cacheMaxBytes = 8192;
	private HashMap byteCache = new HashMap();
	
	/**
	 * Constructor
	 * @param baseUrl the URL to retrieve all data items from
	 * @param dataList a listing of all data items available from the base Url.
	 *            Data items not listed there will still be available, but will
	 *            not appear in the list returned by getDataNames(), and
	 *            isDataAvailable() will return false for data items not on the
	 *            list.
	 */
	public PluginDataProviderUrlBased(String baseUrl, String[] dataList) {
		this.baseUrl = (baseUrl.endsWith("/") ? baseUrl : (baseUrl + "/"));
		
		//	extract data name list
		for (int d = 0; d < dataList.length; d++)
			this.dataNames.add(dataList[d].startsWith(this.baseUrl) ? dataList[d].substring(baseUrl.length()) : dataList[d]);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return true;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getDataNames()
	 */
	public String[] getDataNames() {
		return ((String[]) this.dataNames.toArray(new String[this.dataNames.size()]));
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.dataNames.contains(dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getInputStream(java.lang.String)
	 */
	public InputStream getInputStream(String dataName) throws IOException {
		
		//	cache lookup
		if (this.byteCache.containsKey(dataName))
			return new ByteArrayInputStream((byte[]) this.byteCache.get(dataName));
		
		//	create stream
		BufferedInputStream bis = new BufferedInputStream(this.getUrl(dataName).openStream(), this.cacheMaxBytes);
		
		//	read up to cache limit or end of stream
		bis.mark(this.cacheMaxBytes + 1);
		byte[] testBytes = new byte[this.cacheMaxBytes];
		int read = 0;
		int tb;
		while (((tb = bis.read()) != -1) && (read < testBytes.length))
			testBytes[read++] = ((byte) tb);
		
		//	if we don't have an exception up to this point, data exists
		this.dataNames.add(dataName);
		
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
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		this.byteCache.remove(dataName);
		return this.getUrl(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable()
	 */
	public boolean isDataEditable() {
		// data is not editable in this configuration
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		// data is not editable in this configuration
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		// data is not editable in this configuration
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#deleteData(java.lang.String)
	 */
	public boolean deleteData(String name) {
		return false;
	}

	private URL getUrl(String name) throws IOException {
		if (name.indexOf("://") == -1)
			name = (this.baseUrl + (name.startsWith("/") ? name.substring(1) : name));
		return new URL(name);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return this.baseUrl.substring(0, (this.baseUrl.length() - 1));
	}
}
