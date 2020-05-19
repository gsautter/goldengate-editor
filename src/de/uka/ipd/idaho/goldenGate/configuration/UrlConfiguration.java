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
package de.uka.ipd.idaho.goldenGate.configuration;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Properties;
import java.util.TreeSet;

import de.uka.ipd.idaho.htmlXmlUtil.TreeNode;
import de.uka.ipd.idaho.htmlXmlUtil.accessories.IoTools;
import de.uka.ipd.idaho.htmlXmlUtil.xPath.XPath;
import de.uka.ipd.idaho.htmlXmlUtil.xPath.types.XPathNodeSet;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * GoldenGATE configuration loading plugin components and data from URLs
 * relative to some base URL
 * 
 * @author sautter
 */
public class UrlConfiguration extends AbstractConfiguration {
	
	private String baseUrl; // the base URL
	private String basePath; // the path after authority and port
	
	private final TreeSet dataNames = new TreeSet();
	
	private int cacheMaxBytes = 8192;
	private HashMap byteCache = new HashMap();
	
	/**
	 * Constructor
	 * @param baseUrl the url that all data lies relative to
	 * @throws IOException if any happens while loading the configuration from
	 *             the specified URL
	 */
	public UrlConfiguration(String baseUrl) throws IOException {
		this(baseUrl, null);
	}
	
	/**
	 * Constructor
	 * @param baseUrl the url that all data lies relative to
	 * @param name the name of the configuration
	 * @throws IOException if any happens while loading the configuration from
	 *             the specified URL
	 */
	public UrlConfiguration(String baseUrl, String name) throws IOException {
		super((name == null) ? baseUrl : name);
		this.baseUrl = (baseUrl + (baseUrl.endsWith("/") ? "" : "/"));
		
		//	parse authority:port and path from base URL
		String url = this.baseUrl;
		
		//	remove protocol
		if (url.indexOf("://") != -1)
			url = url.substring(url.indexOf("://") + 3);
		
		//	prune authority:port and path
		this.basePath = url.substring(url.indexOf('/'));
		
		//	create file list
		StringVector fileNameCollector = new StringVector();
		this.loadFileNames(this.baseUrl, fileNameCollector);
		this.dataNames.addAll(fileNameCollector.asList());
		
		//	create master data provider
		System.out.println("UrlConfiguration: base URL is " + this.baseUrl);
	}
	
	private void loadFileNames(String url, StringVector fileNameCollector) throws IOException {
		
		//	try to use index file first
		try {
			BufferedReader fileIndexReader = new BufferedReader(new InputStreamReader(new URL(url + FILE_INDEX_NAME).openStream()));
			String fileName;
			while ((fileName = fileIndexReader.readLine()) != null) {
				
				//	recurse through directory
				if (fileName.endsWith("/") && !fileName.equals("/")) 
					this.loadFileNames((this.baseUrl + fileName), fileNameCollector);
				
				//	collect file names
				else fileNameCollector.addElementIgnoreDuplicates(fileName);
			}
			fileIndexReader.close();
		}
		catch (IOException ioe) {
			
			//	try to parse file names from server's generic folder content listing
			try {
				TreeNode root = IoTools.getAndParsePage(url);
				
				//	extract and process listing hrefs
				XPathNodeSet hrefs = new XPath("//td/a/@href").evaluate(root, new Properties());
				for (int h = 0; h < hrefs.size(); h++) {
					String href = hrefs.get(h).getNodeValue();
					
					//	cut base path of configuration
					if (href.startsWith(this.basePath))
						href = href.substring(this.basePath.length());
					
					//	recurse through directory
					if (href.endsWith("/") && !href.equals("/")) 
						this.loadFileNames((this.baseUrl + href), fileNameCollector);
					
					//	collect file names
					else fileNameCollector.addElementIgnoreDuplicates(href);
				}
			}
			catch (Exception e) {
				throw new IOException("Could not load configuration from " + url);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getHelpBaseURL()
	 */
	public String getHelpBaseURL() {
		return (this.baseUrl + DOCUMENTATION_FOLDER_NAME);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getPath()
	 */
	public String getPath() {
		return null; // we don't have a path ...
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return this.baseUrl.substring(0, (this.baseUrl.length() - 1));
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getDataNames()
	 */
	public String[] getDataNames() {
		return ((String[]) this.dataNames.toArray(new String[this.dataNames.size()]));
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.dataNames.contains(dataName);
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getInputStream(java.lang.String)
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
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		this.byteCache.remove(dataName);
		return this.getUrl(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#isDataEditable()
	 */
	public boolean isDataEditable() {
		// data is not editable in this configuration
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		// data is not editable in this configuration
		return false;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		// data is not editable in this configuration
		return null;
	}

	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGateConfiguration#deleteData(java.lang.String)
	 */
	public boolean deleteData(String name) {
		return false;
	}
	
	private URL getUrl(String name) throws IOException {
		if (name.indexOf("://") == -1)
			name = (this.baseUrl + (name.startsWith("/") ? name.substring(1) : name));
		return new URL(name);
	}
}
