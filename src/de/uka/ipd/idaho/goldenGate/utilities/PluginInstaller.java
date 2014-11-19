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
package de.uka.ipd.idaho.goldenGate.utilities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import de.uka.ipd.idaho.goldenGate.GoldenGateConstants;

/**
 * Installer tool for zipped GoldenGATE Editor plugins and updates thereof,
 * retaining files that were modified locally on updates.
 * 
 * @author sautter
 */
public class PluginInstaller {
	public static void main(String[] args) {
		
		//	get base folder
		File basePath = (new File(".").getAbsoluteFile());
		if (".".equals(basePath.getName()))
			basePath = basePath.getParentFile();
		
		//	check where we are
		File ggServerJar = new File(basePath, "GoldenGATE.jar");
		if (!ggServerJar.exists()) {
			System.out.println("Please run the installer in the root folder of GoldenGATE Editor.");
			return;
		}
		
		//	get plugin folder
		File pluginPath = new File(basePath, GoldenGateConstants.PLUGIN_FOLDER_NAME);
		
		//	check arguments
		if ((args.length == 0) || (args[0] == null) || (args[0].trim().length() == 0)) {
			System.out.println("Please specify the name of the zip file to install or update from.");
			return;
		}
		
		//	find archive to extract (check update case first)
		String zipName = args[0];
		File zipFile = new File(pluginPath, zipName);
		if (!zipFile.exists())
			zipFile = new File(basePath, zipName);
		if (!zipFile.exists()) {
			System.out.println(zipName + " not found.");
			return;
		}
		
		//	get extension data
		String extName = zipName.substring(0, (zipName.length() - ".zip".length()));
		
		//	load file name filter
		ArrayList ignoreFileNames = new ArrayList();
		try {
			File ifnFile = new File(pluginPath, ("update." + extName + ".cnfg"));
			BufferedReader ifnBr = new BufferedReader(new InputStreamReader(new FileInputStream(ifnFile)));
			for (String ifn; (ifn = ifnBr.readLine()) != null;) {
				ifn = ifn.trim();
				if ((ifn.length() == 0) || ifn.startsWith("//"))
					continue;
				
				//	simple string
				if (ifn.indexOf('*') == -1) {
					ignoreFileNames.add(ifn);
					continue;
				}
				
				//	pattern
				StringBuffer ifnRegEx = new StringBuffer();
				for (int c = 0; c < ifn.length(); c++) {
					char ch = ifn.charAt(c);
					if (Character.isLetterOrDigit(ch))
						ifnRegEx.append(ch);
					else if (ch == '*')
						ifnRegEx.append(".*");
					else {
						ifnRegEx.append('\\');
						ifnRegEx.append(ch);
					}
				}
				ignoreFileNames.add(Pattern.compile(ifnRegEx.toString(), Pattern.CASE_INSENSITIVE));
			}
			ifnBr.close();
		} catch (IOException ioe) {}
		
		//	unzip components
		try {
			ZipInputStream webAppZip = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
			for (ZipEntry ze; (ze = webAppZip.getNextEntry()) != null;) {
				
				//	test for folders
				if (ze.isDirectory())
					continue;
				
				//	get and check name
				String zeName = ze.getName();
				if (zeName.startsWith(GoldenGateConstants.PLUGIN_FOLDER_NAME + "/") || zeName.startsWith(GoldenGateConstants.PLUGIN_FOLDER_NAME + "\\"))
					zeName = zeName.substring(GoldenGateConstants.PLUGIN_FOLDER_NAME.length() + 1);
				
				//	check ignore patterns
				for (int i = 0; i < ignoreFileNames.size(); i++) {
					Object io = ignoreFileNames.get(i);
					if (((io instanceof Pattern) && ((Pattern) io).matcher(zeName).matches()) || ((io instanceof String) && ((String) io).equalsIgnoreCase(ze.getName()))) {
						System.out.println(" - ignoring " + zeName);
						ze = null;
						break;
					}
				}
				if (ze == null)
					continue;
				
				//	get timestamp and unpack file
				long zipLastModified = ze.getTime();
				updateFile(pluginPath, zeName, webAppZip, zipLastModified);
				
				//	close current entry
				webAppZip.closeEntry();
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace(System.out);
		}
	}
	
	private static void updateFile(File baseFolderPath, String fileName, InputStream source, long sourceLastModified) throws IOException {
		
		//	create target file
		File targetFile = new File(baseFolderPath, fileName);
		boolean targetFileExists = targetFile.exists();
		
		//	check if more recent version of file available in file system
		if (targetFile.exists() && (sourceLastModified < (targetFile.lastModified() + 1000))) {
			System.out.println(" - retaining " + fileName);
			return;
		}
		
		//	make sure folders exist
		targetFile.getParentFile().mkdirs();
		
		//	create target file
		targetFile.createNewFile();
		
		//	report status
		System.out.println(" - " + (targetFileExists ? "updating" : "installing") + " " + fileName);
		
		//	copy file
		OutputStream target = new BufferedOutputStream(new FileOutputStream(targetFile));
		int count;
		byte[] data = new byte[1024];
		while ((count = source.read(data, 0, 1024)) != -1)
			target.write(data, 0, count);
		
		//	close streams
		target.flush();
		target.close();
		
		//	set timestamp of copied file
		try {
			targetFile.setLastModified(sourceLastModified);
		}
		catch (RuntimeException re) {
			re.printStackTrace(System.out);
		}
	}
}