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


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataListener;

import de.uka.ipd.idaho.gamta.Gamta;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.TokenSequence;
import de.uka.ipd.idaho.gamta.Tokenizer;
import de.uka.ipd.idaho.gamta.defaultImplementation.PlainTokenSequence;
import de.uka.ipd.idaho.gamta.defaultImplementation.RegExTokenizer;
import de.uka.ipd.idaho.goldenGate.TokenizerManager;
import de.uka.ipd.idaho.goldenGate.util.DataListListener;
import de.uka.ipd.idaho.goldenGate.util.DataListPanel;
import de.uka.ipd.idaho.goldenGate.util.DialogPanel;
import de.uka.ipd.idaho.goldenGate.util.EditFontsButton;
import de.uka.ipd.idaho.goldenGate.util.FontEditable;
import de.uka.ipd.idaho.goldenGate.util.FontEditorDialog;
import de.uka.ipd.idaho.goldenGate.util.FontEditorPanel;
import de.uka.ipd.idaho.stringUtils.StringVector;
import de.uka.ipd.idaho.stringUtils.regExUtils.RegExUtils;

/**
 * Manager for the regular expressions used for tokenizing document text
 * 
 * @author sautter
 */
public class DefaultTokenizerManager extends AbstractResourceManager/*SettingsPanel*/ implements /*DataListListener,*/ TokenizerManager {
	
	private static final String FILE_EXTENSION = ".tokenizer";
	
	private static final String[] FIX_REGEX_NAMES = {NO_INNER_PUNCTUATION_TOKENIZER_NAME, INNER_PUNCTUATION_TOKENIZER_NAME};
	private static final String[] FIX_REGEXES = {Gamta.NO_INNER_PUNCTUATION_TOKENIZER_REGEX, Gamta.INNER_PUNCTUATION_TOKENIZER_REGEX};
	
	public DefaultTokenizerManager() {}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.goldenGate.plugins.AbstractResourceManager#getFileExtension()
	 */
	protected String getFileExtension() {
		return FILE_EXTENSION;
	}

	/** @see de.uka.ipd.idaho.goldenGate.plugins.AbstractDocumentProcessorManager#getResourceNames()
	 */
	public String[] getResourceNames() {
		StringVector regExes = new StringVector();
		regExes.addContentIgnoreDuplicates(FIX_REGEX_NAMES);
		regExes.addContentIgnoreDuplicates(this.resourceNameList.getNames());
		return regExes.toStringArray();
	}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.ResourceManager#getResourceTypeLabel()
	 */
	public String getResourceTypeLabel() {
		return "Tokenizer";
	}
	
	/** @see de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin#getSettingsPanel()
	 */
	public SettingsPanel getSettingsPanel() {
		if (this.settingsPanel == null)
			this.settingsPanel = new TokenizerSettingsPanel();
		return this.settingsPanel;
	}
	
	private SettingsPanel settingsPanel = null;
	private class TokenizerSettingsPanel extends SettingsPanel implements DataListListener {
		
		private DataListPanel resourceNameList;
		
		private JPanel editButtons = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		private TokenizerEditorPanel editor;
		
		TokenizerSettingsPanel() {
			super("Tokenizers", "Edit the regular expressions used for tokenizing document text");
			this.editor = new TokenizerEditorPanel();
			
			this.resourceNameList = DefaultTokenizerManager.this.resourceNameList;
			
			this.setLayout(new BorderLayout());
			this.setDoubleBuffered(true);
			
			JButton button;
			
			button = new JButton("Create");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (createRegEx())
						resourceNameList.refresh();
				}
			});
			editButtons.add(button);
			button = new JButton("Clone");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (cloneRegEx())
						resourceNameList.refresh();
				}
			});
			editButtons.add(button);
			button = new JButton("Delete");
			button.setBorder(BorderFactory.createRaisedBevelBorder());
			button.setPreferredSize(new Dimension(100, 21));
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (deleteRegEx()) resourceNameList.refresh();
				}
			});
			editButtons.add(button);
			
			this.add(editButtons, BorderLayout.NORTH);
			this.add(getExplanationLabel(), BorderLayout.CENTER);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.resourceNameList.addDataListListener(this);
		}
		
		private boolean createRegEx() {
			return this.createRegEx(null, null);
		}
		
		private boolean cloneRegEx() {
			String selectedName = this.resourceNameList.getSelectedName();
			if (selectedName == null)
				return this.createRegEx();
			
			else {
				String name = "New " + selectedName;
				return this.createRegEx(this.editor.getContent(), name);
			}
		}
		
		private boolean createRegEx(String modelRegEx, String name) {
			CreateTokenizerDialog cred = new CreateTokenizerDialog(name, modelRegEx);
			cred.setVisible(true);
			if (cred.isCommitted()) {
				String regEx = cred.getRegEx();
				String regExName = cred.getRegExName();
				if (!regExName.endsWith(FILE_EXTENSION)) regExName += FILE_EXTENSION;
				try {
					if (storeStringResource(regExName, regEx)) {
						parent.notifyResourcesChanged(this.getClass().getName());
						return true;
					}
				} catch (IOException e) {}
			}
			return false;
		}
		
		private boolean deleteRegEx() {
			String name = this.resourceNameList.getSelectedName();
			if ((name != null) && (JOptionPane.showConfirmDialog(this, ("Really delete " + name), "Confirm Delete Tokenizer RegEx", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION)) {
				try {
					if (dataProvider.deleteData(name)) {
						this.resourceNameList.refresh();
						parent.notifyResourcesChanged(this.getClass().getName());
						return true;
					}
					else {
						JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
						return false;
					}
				}
				catch (Exception ioe) {
					JOptionPane.showMessageDialog(this, ("Could not delete " + name), "Delete Failed", JOptionPane.INFORMATION_MESSAGE);
					return false;
				}
			}
			else return false;
		}
		
		/** @see de.goldenGate.util.DataListListener#selected(java.lang.String)
		 */
		public void selected(String dataName) {
			if ((this.editor != null) && this.editor.isDirty()) {
				try {
					storeStringResource(this.editor.name, this.editor.getContent());
					parent.notifyResourcesChanged(this.getClass().getName());
				}
				catch (IOException ioe) {
					if (JOptionPane.showConfirmDialog(this, (ioe.getClass().getName() + " (" + ioe.getMessage() + ")\nwhile saving file to " + this.editor.name + "\nProceed?"), "Could Not Save Tokenizer", JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION) {
						this.resourceNameList.setSelectedName(this.editor.name);
						this.validate();
						return;
					}
				}
			}
			
			this.removeAll();
			if (dataName == null)
				this.add(getExplanationLabel(), BorderLayout.CENTER);
			
			else {
				String regEx = loadStringResource(dataName);
				if (regEx == null) regEx = "";
				this.editor.setContent(dataName, regEx);
				this.add(this.editor, BorderLayout.CENTER);
			}
			this.add(this.editButtons, BorderLayout.NORTH);
			this.add(this.resourceNameList, BorderLayout.EAST);
			this.validate();
		}
		
		/** @see de.uka.ipd.idaho.goldenGate.plugins.SettingsPanel#commitChanges()
		 */
		public void commitChanges() {
			if ((this.editor != null) && this.editor.isDirty()) try {
				storeStringResource(this.editor.name, this.editor.getContent());
				parent.notifyResourcesChanged(this.getClass().getName());
			}
			catch (IOException ioe) {}
		}
	}
	
	/**	retrieve a Tokenizer by its name
	 * @param	name	the name of the required Tokenizer
	 * @return the Tokenizer with the specified name, or null if there is no such Tokenizer
	 */
	public Tokenizer getTokenizer(String name) {
		String tokenizerRegEx = this.getRegEx(name);
		return ((tokenizerRegEx == null) ? null : new RegExTokenizer(tokenizerRegEx));
	}
	
	private String getRegEx(String name) {
		if (name == null) return null;
		
		for (int r = 0; r < FIX_REGEX_NAMES.length; r++) 
			if (name.equals(FIX_REGEX_NAMES[r])) return FIX_REGEXES[r];
		
		return this.loadStringResource(name);
	}
	
	private class CreateTokenizerDialog extends DialogPanel {
		
		private JTextField nameField;
		
		private TokenizerEditorPanel editor;
		private String regEx = null;
		private String regExName = null;
		
		CreateTokenizerDialog(String name, String regEx) {
			super("Create Tokenizer RegEx", true);
			
			this.nameField = new JTextField((name == null) ? "New TokenizerRegEx" : name);
			this.nameField.setBorder(BorderFactory.createLoweredBevelBorder());
			
			//	initialize main buttons
			JButton commitButton = new JButton("Create");
			commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
			commitButton.setPreferredSize(new Dimension(100, 21));
			commitButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					commit();
				}
			});
			
			JButton abortButton = new JButton("Cancel");
			abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
			abortButton.setPreferredSize(new Dimension(100, 21));
			abortButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					abort();
				}
			});
			
			JPanel mainButtonPanel = new JPanel(new FlowLayout());
			mainButtonPanel.add(commitButton);
			mainButtonPanel.add(abortButton);
			
			//	initialize editor
			this.editor = new TokenizerEditorPanel();
			this.editor.setContent(name, ((regEx == null) ? "" : regEx));
			
			//	put the whole stuff together
			this.getContentPane().setLayout(new BorderLayout());
			this.getContentPane().add(this.nameField, BorderLayout.NORTH);
			this.getContentPane().add(this.editor, BorderLayout.CENTER);
			this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
			
			this.setResizable(true);
			this.setSize(new Dimension(600, 400));
			this.setLocationRelativeTo(this.getOwner());
		}
		
		private boolean isCommitted() {
			return (this.regEx != null);
		}
		
		private String getRegEx() {
			return this.regEx;
		}
		
		private String getRegExName() {
			return this.regExName;
		}
		
		private void abort() {
			this.regEx = null;
			this.regExName = null;
			this.dispose();
		}

		private void commit() {
			this.regEx = this.editor.getContent();
			this.regExName = this.nameField.getText();
			this.dispose();
		}
	}

	private class TokenizerEditorPanel extends JPanel implements FontEditable, DocumentListener {
		
		private static final String VALIDATOR = "";
		private static final int MAX_SCROLLBAR_WAIT = 200;
		
		private JTextArea editor;
		private JScrollPane editorBox;
		
		private String content = "";
		
		private String fontName = "Verdana";
		private int fontSize = 12;
		private Color fontColor = Color.BLACK;
		
		private String name;
		private boolean dirty = false;
		
		private TokenizerEditorPanel() {
			super(new BorderLayout(), true);
			
			//	initialize editor
			this.editor = new JTextArea();
			this.editor.setEditable(true);
			
			//	wrap editor in scroll pane
			this.editorBox = new JScrollPane(this.editor);
			
			//	initialize buttons
			JButton refreshButton = new JButton("Refresh");
			refreshButton.setBorder(BorderFactory.createRaisedBevelBorder());
			refreshButton.setPreferredSize(new Dimension(115, 21));
			refreshButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshRegEx();
				}
			});
			
			JButton validateButton = new JButton("Validate");
			validateButton.setBorder(BorderFactory.createRaisedBevelBorder());
			validateButton.setPreferredSize(new Dimension(115, 21));
			validateButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					validateRegEx();
				}
			});
			
			JButton testButton = new JButton("Test");
			testButton.setBorder(BorderFactory.createRaisedBevelBorder());
			testButton.setPreferredSize(new Dimension(115, 21));
			testButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					testRegEx();
				}
			});
			
			JPanel buttonPanel = new JPanel(new GridBagLayout(), true);
			GridBagConstraints gbc = new GridBagConstraints();
			gbc.insets.top = 3;
			gbc.insets.bottom = 3;
			gbc.insets.left = 3;
			gbc.insets.right = 3;
			gbc.weighty = 0;
			gbc.weightx = 1;
			gbc.gridheight = 1;
			gbc.gridwidth = 1;
			gbc.fill = GridBagConstraints.BOTH;
			gbc.gridy = 0;
			gbc.gridx = 0;
			buttonPanel.add(this.getEditFontsButton(new Dimension(100, 21)), gbc.clone());
			gbc.gridx ++;
			buttonPanel.add(refreshButton, gbc.clone());
			gbc.gridx ++;
			buttonPanel.add(validateButton, gbc.clone());
			gbc.gridx ++;
			buttonPanel.add(testButton, gbc.clone());
			
			//	put the whole stuff together
			this.add(this.editorBox, BorderLayout.CENTER);
			this.add(buttonPanel, BorderLayout.SOUTH);
			this.refreshDisplay();
		}
		
		private String getContent() {
			if (this.isDirty()) this.content = RegExUtils.normalizeRegEx(this.editor.getText());
			return this.content;
		}
		
		private void setContent(String name, String regEx) {
			this.name = name;
			this.content = RegExUtils.normalizeRegEx(regEx);
			this.refreshDisplay();
			this.dirty = false;
		}
		
		private boolean isDirty() {
			return this.dirty;
		}
		
		private void refreshRegEx() {
			String regEx = this.editor.getText();
			if ((regEx != null) && (regEx.length() != 0)) {
				
				final Point viewPosition = this.editorBox.getViewport().getViewPosition();
				
				String normalizedRegEx = RegExUtils.normalizeRegEx(regEx);
				this.editor.getDocument().removeDocumentListener(this);
				this.editor.setText(RegExUtils.explodeRegEx(normalizedRegEx, "  "));
				this.editor.getDocument().addDocumentListener(this);
				
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						try {
							editorBox.getViewport().setViewPosition(viewPosition);
							validate();
						} catch (RuntimeException re) {}
					}
				});
			}
		}
		
		private void validateRegEx() {
			boolean selected = true;
			String regEx = this.editor.getSelectedText();
			if ((regEx == null) || (regEx.length() == 0)) {
				regEx = this.editor.getText();
				selected = false;
			}
			if (!this.validateRegEx(regEx)) JOptionPane.showMessageDialog(this, "The " + (selected ? "selected pattern part" : "pattern") + " is not a valid pattern.", "RegEx Validation", JOptionPane.ERROR_MESSAGE);
			else JOptionPane.showMessageDialog(this, "The " + (selected ? "selected pattern part" : "pattern") + " is a valid pattern.", "RegEx Validation", JOptionPane.INFORMATION_MESSAGE);
		}
		
		private void testRegEx() {
			boolean selected = true;
			String regEx = this.editor.getSelectedText();
			if ((regEx == null) || (regEx.length() == 0)) {
				regEx = this.editor.getText();
				selected = false;
			}
			regEx = RegExUtils.normalizeRegEx(regEx);
			if (!this.validateRegEx(regEx)) JOptionPane.showMessageDialog(this, "The " + (selected ? "selected pattern part" : "pattern") + " is not a valid pattern.", "RegEx Validation", JOptionPane.ERROR_MESSAGE);
			else {
				final TokenSequence tokenized = testRegEx(regEx);
				if (tokenized != null) {
					final DialogPanel tokenDisplay = new DialogPanel("Tokenized Text", true);
					tokenDisplay.setLayout(new BorderLayout());
					
					JList tokenList = new JList(new ListModel() {
						public void addListDataListener(ListDataListener l) {}
						public void removeListDataListener(ListDataListener l) {}
						public Object getElementAt(int index) {
							return tokenized.tokenAt(index);
						}
						public int getSize() {
							return tokenized.size();
						}
					}); 
					JScrollPane tokenListBox = new JScrollPane(tokenList); 
					
					JButton abortButton = new JButton("OK");
					abortButton.setBorder(BorderFactory.createRaisedBevelBorder());
					abortButton.setPreferredSize(new Dimension(100, 21));
					abortButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							tokenDisplay.dispose();
						}
					});
					
					tokenDisplay.add(new JLabel("This are the tokens your Tokenizer decomposed the text to:"), BorderLayout.NORTH);
					tokenDisplay.add(tokenListBox, BorderLayout.CENTER);
					tokenDisplay.add(abortButton, BorderLayout.SOUTH);
					
					tokenDisplay.setSize(300, 500);
					tokenDisplay.setLocationRelativeTo(this);
					tokenDisplay.setVisible(true);
				}
			}
		}
		
		private TokenSequence testRegEx(String regEx) {
			QueriableAnnotation data = parent.getActiveDocument();
			return ((data == null) ? null : new PlainTokenSequence(data, new RegExTokenizer(regEx)));
		}
		

		private boolean validateRegEx(String regEx) {
			try {
				VALIDATOR.matches(RegExUtils.normalizeRegEx(regEx));
				return true;
			} catch (PatternSyntaxException pse) {
				return false;
			}
		}
		
		private void refreshDisplay() {
			final JScrollBar scroller = this.editorBox.getVerticalScrollBar();
			final int scrollPosition = scroller.getValue();
			
			this.editor.getDocument().removeDocumentListener(this);
			this.editor.setFont(new Font(this.fontName, Font.PLAIN, this.fontSize));
			this.editor.setText(RegExUtils.explodeRegEx(this.content, "  "));
			this.editor.getDocument().addDocumentListener(this);
			
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					int scrollbarWaitCounter = 0;
					while (scroller.getValueIsAdjusting() && (scrollbarWaitCounter < MAX_SCROLLBAR_WAIT)) try {
						Thread.sleep(10);
						scrollbarWaitCounter ++;
					} catch (Exception e) {}
					
					if (scrollbarWaitCounter < MAX_SCROLLBAR_WAIT) {
						scroller.setValueIsAdjusting(true);
						scroller.setValue(scrollPosition);
						scroller.setValueIsAdjusting(false);
					}
					validate();
				}
			});
		}
		
		/** @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
		 */
		public void changedUpdate(DocumentEvent e) {
			//	attribute changes are not of interest for now
		}
		
		/** @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
		 */
		public void insertUpdate(DocumentEvent e) {
			this.dirty = true;
		}
		
		/** @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
		 */
		public void removeUpdate(DocumentEvent e) {
			this.dirty = true;
		}
		
		/**	@see de.goldenGate.util.FontEditable#getEditFontsButton()
		 */
		public JButton getEditFontsButton() {
			return this.getEditFontsButton(null, null, null);
		}
		
		/**	@see de.goldenGate.util.FontEditable#getEditFontsButton(String)
		 */
		public JButton getEditFontsButton(String text) {
			return this.getEditFontsButton(text, null, null);
		}
		
		/**	@see de.goldenGate.util.FontEditable#getEditFontsButton(Dimension)
		 */
		public JButton getEditFontsButton(Dimension dimension) {
			return this.getEditFontsButton(null, dimension, null);
		}
		
		/**	@see de.goldenGate.util.FontEditable#getEditFontsButton(Border)
		 */
		public JButton getEditFontsButton(Border border) {
			return this.getEditFontsButton(null, null, border);
		}
		
		/**	@see de.goldenGate.util.FontEditable#getEditFontsButton(String, Dimension, Border)
		 */
		public JButton getEditFontsButton(String text, Dimension dimension, Border border) {
			return new EditFontsButton(this, text, dimension, border);
		}
		
		/**	@see de.goldenGate.util.FontEditable#editFonts()
		 */
		public boolean editFonts() {
			FontEditorDialog fed = new FontEditorDialog(((JFrame) null), this.fontName, this.fontSize, this.fontColor);
			fed.setVisible(true);
			if (fed.isCommitted()) {
				FontEditorPanel font = fed.getFontEditor();
				if (font.isDirty()) {
					this.fontName = font.getFontName();
					this.fontSize = font.getFontSize();
					this.fontColor = font.getFontColor();
					dirty = true;
				}
				this.refreshDisplay();
				return true;
			}
			return false;
		}
	}
}
