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
package de.uka.ipd.idaho.goldenGate.util;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;

import javax.swing.JDialog;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Substitute for JDialog, which can be constructed with an arbitrary instance
 * of java.awt.Window as it's owner. This class holds the actual JDialog
 * internally and loops respective methods through to it. The JPanel this class
 * actually is adds itself as the only child to the JDialog's content pane, in
 * BorderLayout.CENTER position. When using one of the constructors that do not
 * take a Window as an agrument, the dialog's owner will be the currently
 * top-most (i.e. active) frame or dialog. This panel itself will act as the
 * dialog's content pane.
 * 
 * @author sautter
 */
public class DialogPanel extends JPanel {
	
	/**
	 * Retrieve the window on top of the hierarchy of (modal or non-modal)
	 * dialogs and frames. This is useful, for instance, for setting the
	 * location of the JOptionPane dialogs relative to the current window,
	 * instead of centering it on the screen, and at the same time saves
	 * tracking which window is currently on top.
	 * @return the window on top of the hierarchy, or null, if there is no such
	 *         window
	 */
	public static Window getTopWindow() {
		return KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
	}
	
	private final JDialog dialog;
	private boolean closeWithOwner = false;
	
	/**
	 */
	public DialogPanel() {
		this((Window) null);
	}
	
	/**
	 * @param title
	 */
	public DialogPanel(String title) {
		this(null, title);
	}

	/**
	 * @param modal
	 */
	public DialogPanel(boolean modal) {
		this(((Window) null), modal);
	}

	/**
	 * @param title
	 * @param modal
	 */
	public DialogPanel(String title, boolean modal) {
		this(null, title, modal);
	}

	/**
	 * @param owner
	 */
	public DialogPanel(Window owner) {
		this(owner, "", true);
	}
	
	/**
	 * @param owner
	 * @param title
	 */
	public DialogPanel(Window owner, String title) {
		this(owner, title, true);
	}

	/**
	 * @param owner
	 * @param modal
	 */
	public DialogPanel(Window owner, boolean modal) {
		this(owner, "", modal);
	}

	/**
	 * @param owner
	 * @param title
	 * @param modal
	 */
	public DialogPanel(Window owner, String title, boolean modal) {
		super(new BorderLayout(), true);
		if (owner == null)
			owner = getTopWindow();
		
		if (owner instanceof Dialog)
			this.dialog = new JDialog(((Dialog) owner), title, modal);
		
		else this.dialog = new JDialog(((Frame) owner), title, modal);
		
		if (owner != null) owner.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				if (DialogPanel.this.closeWithOwner)
					dispose();
			}
		});
		
		this.dialog.getContentPane().add(this, BorderLayout.CENTER);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JDialog#getContentPane()
	 */
	public Container getContentPane() {
		return this;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#setContentPane(java.awt.Container)
	 */
	public void setContentPane(Container contentPane) {
		//	we ARE the content pane ...
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#getDefaultCloseOperation()
	 */
	public int getDefaultCloseOperation() {
		return this.dialog.getDefaultCloseOperation();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#setDefaultCloseOperation(int)
	 */
	public void setDefaultCloseOperation(int operation) {
		this.dialog.setDefaultCloseOperation(operation);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#addWindowListener(java.awt.event.WindowListener)
	 */
	public synchronized void addWindowListener(WindowListener wl) {
		this.dialog.addWindowListener(wl);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#getWindowListeners()
	 */
	public synchronized WindowListener[] getWindowListeners() {
		return this.dialog.getWindowListeners();
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#removeWindowListener(java.awt.event.WindowListener)
	 */
	public synchronized void removeWindowListener(WindowListener wl) {
		this.dialog.removeWindowListener(wl);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#addWindowFocusListener(java.awt.event.WindowFocusListener)
	 */
	public synchronized void addWindowFocusListener(WindowFocusListener wfl) {
		this.dialog.addWindowFocusListener(wfl);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#getWindowFocusListeners()
	 */
	public synchronized WindowFocusListener[] getWindowFocusListeners() {
		return this.dialog.getWindowFocusListeners();
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#removeWindowFocusListener(java.awt.event.WindowFocusListener)
	 */
	public synchronized void removeWindowFocusListener(WindowFocusListener wfl) {
		this.dialog.removeWindowFocusListener(wfl);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#addWindowStateListener(java.awt.event.WindowStateListener)
	 */
	public synchronized void addWindowStateListener(WindowStateListener wsl) {
		this.dialog.addWindowStateListener(wsl);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#getWindowStateListeners()
	 */
	public synchronized WindowStateListener[] getWindowStateListeners() {
		return this.dialog.getWindowStateListeners();
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#removeWindowStateListener(java.awt.event.WindowStateListener)
	 */
	public synchronized void removeWindowStateListener(WindowStateListener wsl) {
		this.dialog.removeWindowStateListener(wsl);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#getJMenuBar()
	 */
	public JMenuBar getJMenuBar() {
		return this.dialog.getJMenuBar();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JDialog#setJMenuBar(javax.swing.JMenuBar)
	 */
	public void setJMenuBar(JMenuBar menu) {
		this.dialog.setJMenuBar(menu);
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#getTitle()
	 */
	public String getTitle() {
		return this.dialog.getTitle();
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.dialog.setTitle(title);
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#isModal()
	 */
	public boolean isModal() {
		return this.dialog.isModal();
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setModal(boolean)
	 */
	public void setModal(boolean modal) {
		this.dialog.setModal(modal);
	}
	
	/**	@return	is this dialog closing when the owner closes?
	 */
	public boolean isClosingWithOwner() {
		return this.closeWithOwner;
	}
	
	/**	@param	closeWithOwner	close with owner dialog or frame?
	 */
	public void setClosingWithOwner(boolean closeWithOwner) {
		this.closeWithOwner = closeWithOwner;
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#isResizable()
	 */
	public boolean isResizable() {
		return this.dialog.isResizable();
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setResizable(boolean)
	 */
	public void setResizable(boolean resizable) {
		this.dialog.setResizable(resizable);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getSize()
	 */
	public Dimension getSize() {
		return this.dialog.getSize();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getSize(java.awt.Dimension)
	 */
	public Dimension getSize(Dimension rv) {
		return this.dialog.getSize(rv);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension size) {
		this.dialog.setSize(size);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(int, int)
	 */
	public void setSize(int width, int height) {
		this.dialog.setSize(width, height);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#setLocationRelativeTo(java.awt.Component)
	 */
	public void setLocationRelativeTo(Component c) {
		this.dialog.setLocationRelativeTo(c);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getLocation()
	 */
	public Point getLocation() {
		return this.dialog.getLocation();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getLocation(java.awt.Point)
	 */
	public Point getLocation(Point rv) {
		return this.dialog.getLocation(rv);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setLocation(java.awt.Point)
	 */
	public void setLocation(Point p) {
		this.dialog.setLocation(p);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setLocation(int, int)
	 */
	public void setLocation(int x, int y) {
		this.dialog.setLocation(x, y);
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#getOwner()
	 */
	public Window getOwner() {
		return this.dialog.getOwner();
	}
	
	/** get the actual dialog this panel resides in (necessary for producing other windows with this one as their owner)
	 * @return	the dialog this panel resides in
	 */
	public JDialog getDialog() {
		return this.dialog;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (visible && !SwingUtilities.isEventDispatchThread()) {
			final Object layoutLock = new Object();
			synchronized (layoutLock) {
				System.out.println("DialogPanel: doing Swing EDT handshake");
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						synchronized (layoutLock) {
							System.out.print("DialogPanel in SwingEDT: releasing dialog layout lock ... ");
							layoutLock.notify();
							System.out.println("done");
						}
					}
				});
				try {
					System.out.println("  ==> waiting on dialog layout lock");
					layoutLock.wait();
				}
				catch (InterruptedException ie) {
					System.out.println("  ==> interrupted while waiting on dialog layout lock");
				}
				System.out.println("DialogPanel: proceeding after Swing EDT handshake");
			}
		}
		this.dialog.setVisible(visible);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.Component#isVisible()
	 */
	public boolean isVisible() {
		return (this.dialog.isVisible() && super.isVisible());
	}

	/* (non-Javadoc)
	 * @see java.awt.Window#dispose()
	 */
	public void dispose() {
		if (this.dialog.isVisible()) //	avoid endless loop in window listeners
			this.dialog.dispose();
	}
}
