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
package de.uka.ipd.idaho.goldenGate.utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * @author sautter
 *
 */
public class ConfigurationPacker {
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {}
		
		try {
			buildConfigurations();
		}
		catch (Exception e) {
			System.out.println("An error occurred creating the configuration zip file:\n" + e.getMessage());
			e.printStackTrace(System.out);
			JOptionPane.showMessageDialog(null, ("An error occurred creating the configuration zip file:\n" + e.getMessage()), "Configuration Creation Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void buildConfigurations() {
		File rootFolder = new File(PackerUtils.normalizePath(new File(".").getAbsolutePath()));
		System.out.println("Root folder is '" + rootFolder.getAbsolutePath() + "'");
		
		String[] configNames = PackerUtils.getConfigNames(rootFolder);
		Set preSelectedConfigNames = new TreeSet();
		long now = System.currentTimeMillis();
		for (int c = 0; c < configNames.length; c++) {
			File versionZipFile = new File(rootFolder, ("_Zips/" + getConfigZipName(configNames[c])));
			File configFolder = PackerUtils.getConfigFolder(rootFolder, configNames[c]);
			if ((versionZipFile.lastModified() < configFolder.lastModified()) && ((now - configFolder.lastModified()) < (1000 * 60 * 60 * 24 * 90)))
				preSelectedConfigNames.add(configNames[c]);
		}
		configNames = PackerUtils.selectConfigurationNames(rootFolder, "Please select the configuration(s) to zip.", false, preSelectedConfigNames);
		
		for (int c = 0; c < configNames.length; c++) try {
			buildConfiguration(rootFolder, configNames[c]);
		}
		catch (Exception e) {
			System.out.println("An error occurred creating the configuration zip file:\n" + e.getMessage());
			e.printStackTrace(System.out);
			JOptionPane.showMessageDialog(null, ("An error occurred creating the configuration zip file:\n" + e.getMessage()), "Configuration Creation Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private static void buildConfiguration(File rootFolder, String configName) throws Exception {
		System.out.println("Zipping " + ("Local Master Configuration".equals(configName) ? configName : ("Configuration '" + configName + "'")) + ".");
		
		File configFolder = PackerUtils.getConfigFolder(rootFolder, configName);
		
		String configZipName = getConfigZipName(configName);
		System.out.println("Building GoldenGATE configuration '" + configZipName + "'");
		
		String[] configFileNames = PackerUtils.getConfigFileNames(rootFolder, configName);
		
		File configZipFile = new File(rootFolder, ("_Zips/" + configZipName));
		if (configZipFile.exists()) {
			configZipFile.renameTo(new File(rootFolder, ("_Zips/" + configZipName + "." + System.currentTimeMillis() + ".old")));
			configZipFile = new File(rootFolder, ("_Zips/" + configZipName));
		}
		System.out.println("Creating configuration zip file '" + configZipFile.getAbsolutePath() + "'");
		
		configZipFile.getParentFile().mkdirs();
		configZipFile.createNewFile();
		ZipOutputStream configZipper = new ZipOutputStream(new FileOutputStream(configZipFile));
		
		PackerUtils.writeZipFileEntries(configFolder, configZipper, configFileNames);
		
		configZipper.flush();
		configZipper.close();
		
		System.out.println("Configuration zip file '" + configZipFile.getAbsolutePath() + "' created successfully.");
		JOptionPane.showMessageDialog(null, ("Configuration '" + configZipName + "' created successfully."), "Configuration Created Successfully", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private static final String getConfigZipName(String configName) {
		return ((PackerUtils.LOCAL_MASTER_CONFIGURATION.equals(configName) ? "LocalMasterConfiguration" : configName) + ".zip");
	}
//	private static void buildConfiguration() throws Exception {	
//		File rootFolder = new File(PackerUtils.normalizePath(new File(".").getAbsolutePath()));
//		System.out.println("Root folder is '" + rootFolder.getAbsolutePath() + "'");
//		
//		String configName = PackerUtils.selectConfigurationName(rootFolder, "Please select the configuration to zip.", false);
//		if (configName == null)
//			return;
//		System.out.println(("Local Master Configuration".equals(configName) ? configName : ("Configuration '" + configName + "'")) + " selected for zipping.");
//		
//		File configFolder = PackerUtils.getConfigFolder(rootFolder, configName);
//		
//		String configZipName = ("Local Master Configuration".equals(configName) ? "LocalMasterConfiguration" : configName) + ".zip";
//		System.out.println("Building GoldenGATE configuration '" + configZipName + "'");
//		
//		String[] configFileNames = PackerUtils.getConfigFileNames(rootFolder, configName);
//		
//		File configZipFile = new File(rootFolder, ("_Zips/" + configZipName));
//		if (configZipFile.exists()) {
//			configZipFile.renameTo(new File(rootFolder, ("_Zips/" + configZipName + "." + System.currentTimeMillis() + ".old")));
//			configZipFile = new File(rootFolder, ("_Zips/" + configZipName));
//		}
//		System.out.println("Creating configuration zip file '" + configZipFile.getAbsolutePath() + "'");
//		
//		configZipFile.getParentFile().mkdirs();
//		configZipFile.createNewFile();
//		ZipOutputStream configZipper = new ZipOutputStream(new FileOutputStream(configZipFile));
//		
//		PackerUtils.writeZipFileEntries(configFolder, configZipper, configFileNames);
//		
//		configZipper.flush();
//		configZipper.close();
//		
//		System.out.println("Configuration zip file '" + configZipFile.getAbsolutePath() + "' created successfully.");
//		JOptionPane.showMessageDialog(null, ("Configuration '" + configZipName + "' created successfully."), "Configuration Created Successfully", JOptionPane.INFORMATION_MESSAGE);
//	}
}
