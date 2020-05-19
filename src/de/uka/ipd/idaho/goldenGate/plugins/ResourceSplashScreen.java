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


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import de.uka.ipd.idaho.gamta.util.ControllingProgressMonitor;
import de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorPanel;
import de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorWindow;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;

public class ResourceSplashScreen extends DialogPanel implements ControllingProgressMonitor, ProgressMonitorWindow {
	private JLabel textLabel = new JLabel("Please wait while GoldenGATE Resource is running ...", JLabel.LEFT);
	private ProgressMonitorPanel pmp;
	
	/**
	 * Constructor
	 * @param title the title for the splash screen
	 * @param text the initial text to be displayed on the label of the splash
	 *            screen
	 */
	public ResourceSplashScreen(String title, String text) {
		this(DialogPanel.getTopWindow(), title, text, false, false);
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this splash screen is modal to
	 * @param title the title for the splash screen
	 * @param text the initial text to be displayed on the label of the splash
	 *            screen
	 */
	public ResourceSplashScreen(JFrame owner, String title, String text) {
		this(owner, title, text, false, false);
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this splash screen is modal to
	 * @param title the title for the splash screen
	 * @param text the initial text to be displayed on the label of the splash
	 *            screen
	 */
	public ResourceSplashScreen(JDialog owner, String title, String text) {
		this(owner, title, text, false, false);
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this splash screen is modal to
	 * @param title the title for the splash screen
	 * @param text the initial text to be displayed on the label of the splash
	 *            screen
	 * @param supportPauseResume support pausing/resuming resource application?
	 * @param supportAbort support aborting resource application?
	 */
	public ResourceSplashScreen(Window owner, String title, String text, boolean supportPauseResume, boolean supportAbort) {
		super(owner, title, true);
		
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.pmp = new ProgressMonitorPanel(supportPauseResume, supportAbort);
		
		this.setLayout(new BorderLayout());
		this.add(this.textLabel, BorderLayout.NORTH);
		this.add(this.pmp, BorderLayout.CENTER);
		
		this.setSize(new Dimension(400, ((supportPauseResume || supportAbort) ? 150 : 130)));
		this.setLocationRelativeTo(owner);
		if (text != null) this.setText(text);
	}
	
	public void setAbortExceptionMessage(String aem) {
		this.pmp.setAbortExceptionMessage(aem);
	}
	
	public boolean supportsAbort() {
		return this.pmp.supportsAbort();
	}
	
	public void setAbortEnabled(boolean ae) {
		this.pmp.setAbortEnabled(ae);
	}
	
	public boolean supportsPauseResume() {
		return this.pmp.supportsPauseResume();
	}
	
	public void setPauseResumeEnabled(boolean pre) {
		this.pmp.setPauseResumeEnabled(pre);
	}
	
	public void setStep(String step) {
		this.pmp.setStep(step);
	}
	
	public void setInfo(String info) {
		this.pmp.setInfo(info);
	}
	
	public void setBaseProgress(int baseProgress) {
		this.pmp.setBaseProgress(baseProgress);
	}
	
	public void setMaxProgress(int maxProgress) {
		this.pmp.setMaxProgress(maxProgress);
	}
	
	public void setProgress(int progress) {
		this.pmp.setProgress(progress);
	}
	
	/**
	 * Set the text displayed on the label of the splash screen
	 * @param text the new text
	 */
	public void setText(String text) {
		this.textLabel.setText((text == null) ? "" : text);
	}
	
	/**
	 * Make the splash screen appear. It is not a good idea to invoke this
	 * method from the Swing event dispatch thread and then wait for the splash
	 * screen to show, since setting the splash screen visible actually has to
	 * be done by the event dispatch thread.
	 */
	public void popUp() {
//		Thread t = new SplashScreenThread(this);
//		t.start();
		this.popUp(false);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorWindow#getWindow()
	 */
	public Window getWindow() {
		return this.getDialog();
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorWindow#getSubWindow(java.awt.Window, java.lang.String, boolean, boolean)
	 */
	public ProgressMonitorWindow getSubWindow(Window topWindow, String title, boolean supportPauseResume, boolean supportAbort) {
		return new ResourceSplashScreen(topWindow, title, "", supportPauseResume, supportAbort);
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorWindow#popUp(boolean)
	 */
	public void popUp(boolean block) {
		if (this.isVisible())
			return;
		if (block)
			this.setVisible(true);
		else {
			Thread put = new Thread() {
				public void run() {
					setVisible(true);
				}
			};
			put.start();
			if (SwingUtilities.isEventDispatchThread())
				return;
			while (!this.isVisible()) try {
				Thread.sleep(100);
			} catch (InterruptedException ie) {}
		}
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.gamta.util.swing.ProgressMonitorWindow#close()
	 */
	public void close() {
		this.dispose();
	}
//	
//	/**
//	 * Thread for displaying the modal splash screen without blocking program
//	 * execution.
//	 */
//	private static class SplashScreenThread extends Thread {
//		private ResourceSplashScreen splashScreen;
//		private SplashScreenThread(ResourceSplashScreen splashScreen) {
//			this.splashScreen = splashScreen;
//		}
//		public void run() {
//			System.out.println("SplashScreenThread: showing splash screen");
//			this.splashScreen.setVisible(true);
//		}
//	}
}
