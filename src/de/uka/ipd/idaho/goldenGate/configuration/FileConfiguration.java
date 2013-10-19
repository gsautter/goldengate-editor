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
package de.uka.ipd.idaho.goldenGate.configuration;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

import de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration;
import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;
import de.uka.ipd.idaho.goldenGate.configuration.AbstractConfigurationManager.ExportStatusDialog;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.Configuration;
import de.uka.ipd.idaho.goldenGate.configuration.ConfigurationUtils.SpecialDataHandler;
import de.uka.ipd.idaho.stringUtils.StringVector;

/**
 * GoldenGATE configuration loading plugin components and data from the local
 * file system
 * 
 * @author sautter
 */
public class FileConfiguration extends AbstractConfiguration {
	
	private File basePath;
	
	private boolean isMaster;
	private boolean allowWebAccess;
	
	private StringVector fileList = new StringVector();
	private int cacheMaxBytes = 2048;
	private Map byteCache = Collections.synchronizedMap(new HashMap());
	
	/**
	 * Constructor
	 * @param basePath the base path of the configuration
	 * @param master are we a master configuration?
	 * @param allowWebAccess allow components accessing the web?
	 */
	public FileConfiguration(File basePath, boolean master, boolean allowWebAccess) {
		this(null, basePath, master, allowWebAccess, null);
	}
	
	/**
	 * Constructor
	 * @param basePath the base path of the configuration
	 * @param master are we a master configuration?
	 * @param allowWebAccess allow components accessing the web?
	 * @param logFile the file to write log entries to (specifying null disables
	 *            logging)
	 */
	public FileConfiguration(File basePath, boolean master, boolean allowWebAccess, File logFile) {
		this(null, basePath, master, allowWebAccess, logFile);
	}
	
	/**
	 * Constructor
	 * @param name the name of the configuration (null value will be substituted
	 *            with name of folder)
	 * @param basePath the base path of the configuration
	 * @param master are we a master configuration?
	 * @param allowWebAccess allow components accessing the web?
	 */
	public FileConfiguration(String name, File basePath, boolean master, boolean allowWebAccess) {
		this(name, basePath, master, allowWebAccess, null);
	}
	
	/**
	 * Constructor
	 * @param name the name of the configuration (null value will be substituted
	 *            with name of folder)
	 * @param basePath the base path of the configuration
	 * @param master are we a master configuration?
	 * @param allowWebAccess allow components accessing the web?
	 * @param logFile the file to write log entries to (specifying null disables
	 *            logging)
	 */
	public FileConfiguration(String name, File basePath, boolean master, boolean allowWebAccess, File logFile) {
		super(((name == null) ? basePath.getAbsoluteFile().getName() : name), logFile);
		this.basePath = basePath;
		this.isMaster = master;
		this.allowWebAccess = allowWebAccess;
		
		this.fileList = ConfigurationUtils.listFilesRelative(this.basePath, new FileFilter() {
			public boolean accept(File file) {
				return ((!file.isDirectory() && !file.getName().matches(".*\\.[0-9]{8,}\\.(old|new)"))
						||
						!FileConfiguration.this.basePath.equals(file.getParentFile())
						|| (
							!file.getName().startsWith("_")
							&&
							!CONFIG_FOLDER_NAME.equals(file.getName())
							&&
							!UPDATE_FOLDER_NAME.equals(file.getName())
						)
					);
			}
		});
		this.fileList.sortLexicographically(false, false);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getPath()
	 */
	public String getPath() {
		String absolutePath = this.basePath.getAbsolutePath().replaceAll("\\\\", "/");
		int pathStart = absolutePath.indexOf(GoldenGateConstants.CONFIG_FOLDER_NAME + "/");
		if (pathStart == -1) return ".";
		else return (absolutePath.substring(pathStart));
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getAbsolutePath()
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
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#allowWebAccess()
	 */
	public boolean allowWebAccess() {
		return this.allowWebAccess;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isMasterConfiguration()
	 */
	public boolean isMasterConfiguration() {
		return this.isMaster;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.configuration.AbstractConfiguration#getWindowMenuItems()
	 */
	public JMenuItem[] getWindowMenuItems() {
		ArrayList collector = new ArrayList();
		JMenuItem mi;
		
		mi = new JMenuItem("Clear Byte Cache");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				byteCache.clear();
			}
		});
		collector.add(mi);
		
		if (!GraphicsEnvironment.isHeadless()) {
			mi = new JMenuItem("Monitor System Log");
			mi.addActionListener(new ActionListener() {
				SystemLogMonitor slm = null;
				public void actionPerformed(ActionEvent ae) {
					if (this.slm == null) {
						this.slm = new SystemLogMonitor();
						this.slm.setIconImage(getIconImage());
					}
					this.slm.setVisible(true);
				}
			});
			collector.add(mi);
		}
		
		return ((JMenuItem[]) collector.toArray(new JMenuItem[collector.size()]));
	}
	
	private static class SystemLogMonitor extends JFrame {
		private boolean active = true;
		private JCheckBox activeSwitch = new JCheckBox("Active", true);
		
		private PrintStream systemOut;
		private JTextArea systemOutDisplay = new JTextArea();
		private JScrollPane systemOutDisplayBox = new JScrollPane(this.systemOutDisplay);
		
		private PrintStream systemErr;
		private JTextArea systemErrDisplay = new JTextArea();
		private JScrollPane systemErrDisplayBox = new JScrollPane(this.systemErrDisplay);
		
		private JSplitPane displaySplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.systemOutDisplayBox, this.systemErrDisplayBox);
		
		SystemLogMonitor() {
			super("System Output Monitor");
			
			this.systemOutDisplay.setWrapStyleWord(false);
			this.systemOutDisplay.setLineWrap(false);
			this.systemOutDisplay.setFont(new Font("Courier", Font.PLAIN, 12));
			
			this.systemErrDisplay.setWrapStyleWord(false);
			this.systemErrDisplay.setLineWrap(false);
			this.systemErrDisplay.setFont(new Font("Courier", Font.PLAIN, 12));
			this.systemErrDisplay.setForeground(Color.RED);
			
			this.displaySplit.setDividerLocation(0.8);
			this.displaySplit.setResizeWeight(0.8);
			
			this.activeSwitch.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent ie) {
					active = activeSwitch.isSelected();
				}
			});
			
			JButton clearOutButton = new JButton("Clear System.out");
			clearOutButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					systemOutDisplay.setText("");
				}
			});
			
			JButton clearErrButton = new JButton("Clear System.err");
			clearErrButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					systemErrDisplay.setText("");
				}
			});
			
			JButton closeButton = new JButton("Close");
			closeButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					dispose();
				}
			});
			
			JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
			functionPanel.add(this.activeSwitch);
			functionPanel.add(new JLabel(" "));
			functionPanel.add(clearOutButton);
			functionPanel.add(new JLabel(" "));
			functionPanel.add(clearErrButton);
			functionPanel.add(new JLabel(" "));
			functionPanel.add(closeButton);
			
			
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.displaySplit, BorderLayout.CENTER);
			this.getContentPane().add(functionPanel, BorderLayout.SOUTH);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLocation(10, 10);
			this.setSize(500, 800);
			this.setResizable(true);
			
			//	divert System.out
			this.systemOut = System.out;
			System.setOut(new PrintStream(this.systemOut) {
				/*
				 * not the most elegant solution, but way faster than extending
				 * FilteredOutputStream and diverting every single invocation of
				 * write(byte b)
				 */
				public void print(boolean b) {
					super.print(b);
					outPrint(String.valueOf(b), false);
				}
				public void print(char c) {
					super.print(c);
					outPrint("" + c, false);
				}
				public void print(char[] s) {
					super.print(s);
					outPrint(new String(s), false);
				}
				public void print(double d) {
					super.print(d);
					outPrint(String.valueOf(d), false);
				}
				public void print(float f) {
					super.print(f);
					outPrint(String.valueOf(f), false);
				}
				public void print(int i) {
					super.print(i);
					outPrint(String.valueOf(i), false);
				}
				public void print(long l) {
					super.print(l);
					outPrint(String.valueOf(l), false);
				}
				public void print(Object obj) {
					super.print(obj);
					outPrint(String.valueOf(obj), false);
				}
				public void print(String s) {
					super.print(s);
					outPrint(s, false);
				}
				public void println() {
					super.println();
					outPrint("", true);
				}
				public void println(boolean x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(char x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(char[] x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(double x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(float x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(int x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(long x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(Object x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
				public void println(String x) {
					super.print(x);
					super.println();
					outPrint(String.valueOf(x), true);
				}
			});
			
			//	divert System.err
			this.systemErr = System.err;
			System.setErr(new PrintStream(this.systemErr) {
				/*
				 * not the most elegant solution, but way faster than extending
				 * FilteredOutputStream and diverting every single invocation of
				 * write(byte b)
				 */
				public void print(boolean b) {
					super.print(b);
					errPrint(String.valueOf(b), false);
				}
				public void print(char c) {
					super.print(c);
					errPrint("" + c, false);
				}
				public void print(char[] s) {
					super.print(s);
					errPrint(new String(s), false);
				}
				public void print(double d) {
					super.print(d);
					errPrint(String.valueOf(d), false);
				}
				public void print(float f) {
					super.print(f);
					errPrint(String.valueOf(f), false);
				}
				public void print(int i) {
					super.print(i);
					errPrint(String.valueOf(i), false);
				}
				public void print(long l) {
					super.print(l);
					errPrint(String.valueOf(l), false);
				}
				public void print(Object obj) {
					super.print(obj);
					errPrint(String.valueOf(obj), false);
				}
				public void print(String s) {
					super.print(s);
					errPrint(s, false);
				}
				public void println() {
					super.println();
					errPrint("", true);
				}
				public void println(boolean x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(char x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(char[] x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(double x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(float x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(int x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(long x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(Object x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
				public void println(String x) {
					super.print(x);
					super.println();
					errPrint(String.valueOf(x), true);
				}
			});
			
			this.addWindowListener(new WindowAdapter() {
				public void windowOpened(WindowEvent we) {
					active = activeSwitch.isSelected();
				}
				public void windowClosed(WindowEvent we) {
					active = false;
					systemOutDisplay.setText("");
					systemErrDisplay.setText("");
				}
			});
		}
		
		void outPrint(String str, boolean isLine) {
			if (!this.active)
				return;
			this.systemOutDisplay.append(str);
			if (isLine) this.systemOutDisplay.append("\n");
			this.systemOutDisplay.setCaretPosition(this.systemOutDisplay.getDocument().getLength());
		}
		
		void errPrint(String str, boolean isLine) {
			if (!this.active)
				return;
			this.systemErrDisplay.append(str);
			if (isLine)
				this.systemErrDisplay.append("\n");
			this.systemErrDisplay.setCaretPosition(this.systemErrDisplay.getDocument().getLength());
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getHelpBaseURL()
	 */
	public String getHelpBaseURL() {
		try {
			return new File(this.basePath, DOCUMENTATION_FOLDER_NAME).toURL().toString();
		}
		catch (MalformedURLException e) {
			return new File(this.basePath, DOCUMENTATION_FOLDER_NAME).toString();
		} 
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getDataNames()
	 */
	public String[] getDataNames() {
		return this.fileList.toStringArray();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isDataAvailable(java.lang.String)
	 */
	public boolean isDataAvailable(String dataName) {
		return this.getFile(dataName).exists();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getInputStream(java.lang.String)
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
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getURL(java.lang.String)
	 */
	public URL getURL(String dataName) throws IOException {
		if ((dataName.indexOf("://") == -1) && !dataName.toLowerCase().startsWith("file:/")) {
			this.byteCache.remove(dataName);
			return this.getFile(dataName).toURI().toURL();
		}
		else return new URL(dataName);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isDataEditable()
	 */
	public boolean isDataEditable() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#isDataEditable(java.lang.String)
	 */
	public boolean isDataEditable(String dataName) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#getOutputStream(java.lang.String)
	 */
	public OutputStream getOutputStream(String dataName) throws IOException {
		
		//	get data file and parent folder
		File dataFile = this.getFile(dataName);
		File dataFileParent = dataFile.getParentFile();
		
		//	make sure parent file exists and is directory
		if (dataFileParent.exists()) {
			if (!dataFileParent.isDirectory())
				throw new IOException("Cannot write '" + dataFile.getAbsolutePath() + "' because '" + dataFileParent.getAbsolutePath() + "' is not a directory.");
		}
		else {
			if (!dataFileParent.mkdirs())
				throw new IOException("Cannot write '" + dataFile.getAbsolutePath() + "' because directory '" + dataFileParent.getAbsolutePath() + "' does not exist and could not be created.");
		}
		
		//	return output stream
		return new TimeoutOutputStream(dataName, dataFile.getName(), dataFileParent);
	}
	
	/**
	 * wrapper for output streams to automatically flush and close the wrapped stream if inactive for more than 30 seconds
	 * 
	 * @author sautter
	 */
	private class TimeoutOutputStream extends OutputStream {
		private String dataName;
		private long dataTime;
		private String dataFileName;
		private File dataFileParent;
		
		private File dataOutFile;
		private OutputStream dataOut;
		private long lastWritten = System.currentTimeMillis();
		
		TimeoutOutputStream(String dataName, String dataFileName, File dataFileParent) throws IOException {
			
			//	store basic data
			this.dataName = dataName;
			this.dataTime = System.currentTimeMillis();
			this.dataFileName = dataFileName;
			this.dataFileParent = dataFileParent;
			
			//	create writing file and output stream
			this.dataOutFile = new File(this.dataFileParent, (this.dataFileName + "." + this.dataTime + ".new"));
			try {
				this.dataOutFile.createNewFile();
			}
			catch (RuntimeException re) {
				this.dataOutFile = new File(this.dataFileParent, this.dataFileName);
				throw new IOException("Cannot write '" + this.dataOutFile.getAbsolutePath() + "': " + re.getMessage());
			}
			this.dataOut = new FileOutputStream(this.dataOutFile);
			
			//	start watchdog
			new Thread() {
				public void run() {
					
					//	watch out if stream closed externally
					while (lastWritten != -1) {
						
						//	close stream if timeout expired
						if ((System.currentTimeMillis() - lastWritten) > (30 * 1000)) {
							try {
								System.out.println("Auto-Closing OutputStream for '" + TimeoutOutputStream.this.dataName + "'");
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
		
		public void flush() throws IOException {
			this.dataOut.flush();
		}
		
		public void close() throws IOException {
			this.lastWritten = -1;
			this.dataOut.close();
			File existingDataFile = new File(this.dataFileParent, this.dataFileName);
			if (existingDataFile.exists())
				existingDataFile.renameTo(new File(this.dataFileParent, (this.dataFileName + "." + this.dataTime + ".old")));
			this.dataOutFile.renameTo(new File(this.dataFileParent, this.dataFileName));
			byteCache.remove(this.dataName);
			fileList.addElementIgnoreDuplicates(this.dataName);
			fileList.sortLexicographically(false, false);
		}
		
		public void write(int b) throws IOException {
			this.lastWritten = System.currentTimeMillis();
			this.dataOut.write(b);
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
	 * @see de.uka.ipd.idaho.goldenGate.GoldenGateConfiguration#deleteData(java.lang.String)
	 */
	public boolean deleteData(String dataName) {
		File file = this.getFile(dataName);
		this.byteCache.remove(dataName);
		this.fileList.removeAll(dataName);
		return (!file.exists() || file.delete());
	}
	
	private File getFile(String dataName) {
		return new File(this.basePath, dataName);
	}
	
	public static boolean createFileConfiguration(String exportName, SpecialDataHandler specialData, Configuration config, File rootPath, File targetPath, ExportStatusDialog statusDialog) throws IOException {
		
		//	make way
		if (targetPath.exists()) {
			String exportFolderName = targetPath.toString();
			File oldExportFolder = new File(exportFolderName + "." + System.currentTimeMillis() + ".old");
			targetPath.renameTo(oldExportFolder);
			targetPath = new File(exportFolderName);
		}
		targetPath.mkdir();
		
		//	get data names
		String[] dataNames = ConfigurationUtils.getDataNameList(rootPath, config);
		
		//	copy data
		System.out.println("FileConfigurationManager: copying files");
		for (int d = 0; d < dataNames.length; d++) {
			statusDialog.setInfo(dataNames[d]);
			
			InputStream specialSource = specialData.getInputStream(dataNames[d]);
			if (specialSource == null)
				copyFile(dataNames[d], rootPath, targetPath);
			else copyFile(dataNames[d], specialSource, config.configTimestamp, targetPath);
			
			statusDialog.setProgress((98 * (d+1)) / dataNames.length);
		}
		
		//	write configuration.xml
		statusDialog.setInfo(DESCRIPTOR_FILE_NAME);
		File configFile = new File(targetPath, DESCRIPTOR_FILE_NAME);
		BufferedWriter configWriter = new BufferedWriter(new FileWriter(configFile, true));
		config.writeXml(configWriter);
		configWriter.flush();
		configWriter.close();
		statusDialog.setProgress(98);
		
		//	write files.txt
		statusDialog.setInfo(GoldenGateConfiguration.FILE_INDEX_NAME);
		StringVector fileList = new StringVector();
		fileList.addContentIgnoreDuplicates(dataNames);
		fileList.addElementIgnoreDuplicates(DESCRIPTOR_FILE_NAME);
		fileList.storeContent(new File(targetPath, GoldenGateConfiguration.FILE_INDEX_NAME));
		statusDialog.setProgress(99);
		
		//	create timestamp
		statusDialog.setInfo(GoldenGateConfiguration.TIMESTAMP_NAME);
		StringVector timestamper = new StringVector();
		timestamper.addElement("" + config.configTimestamp);
		timestamper.storeContent(new File(targetPath, GoldenGateConfiguration.TIMESTAMP_NAME));
		statusDialog.setProgress(100);
		
		//	indicate success
		return true;
	}
	
	private static final int BUFFER_SIZE = 1024;
	
	private static final void copyFile(String fileName, File sourceRoot, File targetRoot) throws IOException {
		
		//	open source file
		File sourceFile = new File(sourceRoot, fileName);
		InputStream source = new BufferedInputStream(new FileInputStream(sourceFile), BUFFER_SIZE);
		
		//	copy data
		copyFile(fileName, source, sourceFile.lastModified(), targetRoot);
	}
	
	private static final void copyFile(String fileName, InputStream source, long sourceTimestamp, File targetRoot) throws IOException {
		
		//	set up target file
		File targetFile = null;
		OutputStream target = null;
		
		//	copy data
		int count;
		byte[] data = new byte[BUFFER_SIZE];
		while ((count = source.read(data, 0, BUFFER_SIZE)) != -1) {
			
			//	create target file only after first bytes are read from source to prevent creating empty files
			if (target == null) {
				targetFile = new File(targetRoot, fileName);
				targetFile.getParentFile().mkdirs();
				targetFile.createNewFile();
				target = new BufferedOutputStream(new FileOutputStream(targetFile));
			}
			
			//	write data
			target.write(data, 0, count);
		}
		
		//	close source
		source.close();
		
		//	close target file if any was created
		if (target != null) {
			target.flush();
			target.close();
			try { // set modification data of copy
				targetFile.setLastModified(sourceTimestamp);
			} catch (RuntimeException re) {}
		}
	}
}
