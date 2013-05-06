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
package de.uka.ipd.idaho.goldenGate.util;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import de.uka.ipd.idaho.gamta.Annotation;
import de.uka.ipd.idaho.gamta.Attributed;
import de.uka.ipd.idaho.gamta.QueriableAnnotation;
import de.uka.ipd.idaho.gamta.Token;
import de.uka.ipd.idaho.gamta.TokenSequence;
import de.uka.ipd.idaho.gamta.util.swing.AttributeEditor;

/**
 * A small editor dialog for the attributes of Attributed objects, basicallly
 * for Annotations and Tokens.
 * 
 * @author sautter
 */
public class AttributeEditorDialog extends DialogPanel {
	
	private AttributeEditor attributeEditor;
	private boolean dirty = false;
	
//	/**	Constructor
//	 * @param	title		the title for the dialog
//	 * @param	annotation	the Annotations whose attributes to edit
//	 * @param	context		the context document (used for extracting suggestions)
//	 */
//	public AttributeEditorDialog(String title, Annotation annotation, MutableAnnotation context) {
//		super(title, true);
//		this.init(annotation, context);
//	}
//	
//	/**	Constructor
//	 * @param	owner		the JFrame this AttributeEditorDialog is modal to
//	 * @param	title		the title for the dialog
//	 * @param	annotation	the Annotations whose attributes to edit
//	 * @param	context		the context document (used for extracting suggestions)
//	 */
//	public AttributeEditorDialog(JFrame owner, String title, Annotation annotation, MutableAnnotation context) {
//		super(owner, title, true);
//		this.init(annotation, context);
//	}
//	
//	/**	Constructor
//	 * @param	owner		the JDialog this AttributeEditorDialog is modal to
//	 * @param	title		the title for the dialog
//	 * @param	annotation	the Annotations whose attributes to edit
//	 * @param	context		the context document (used for extracting suggestions)
//	 */
//	public AttributeEditorDialog(JDialog owner, String title, Annotation annotation, MutableAnnotation context) {
//		super(owner, title, true);
//		this.init(annotation, context);
//	}
//	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(String title, Annotation annotation, QueriableAnnotation context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Annotation annotation, QueriableAnnotation context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param annotation the Annotations whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Annotation annotation, QueriableAnnotation context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(annotation, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(String title, Token token, TokenSequence context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Token token, TokenSequence context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param token the Token whose attributes to edit
	 * @param context the context document (used for extracting suggestions)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Token token, TokenSequence context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(token, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JFrame this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(JFrame owner, String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	/**
	 * Constructor
	 * @param owner the JDialog this AttributeEditorDialog is modal to
	 * @param title the title for the dialog
	 * @param attributed the Attributed object whose attributes to edit
	 * @param type the type of the attributed object (may be null)
	 * @param value the string value of the attributed object (may be null)
	 * @param context the context, eg attributed objects similar to the one
	 *            whose attributes to edit (will be used to provide attribute
	 *            name and value suggestions, may be null)
	 */
	public AttributeEditorDialog(JDialog owner, String title, Attributed attributed, String type, String value, Attributed[] context) {
		super(owner, title, true);
		this.attributeEditor = new AttributeEditor(attributed, type, value, context);
		this.init();
	}
	
	private void init() {
		
		//	initialize buttons
		JButton commitButton = new JButton("OK");
		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
		commitButton.setPreferredSize(new Dimension(80, 21));
		commitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dirty = attributeEditor.writeChanges();
				AttributeEditorDialog.this.dispose();
			}
		});
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
		cancelButton.setPreferredSize(new Dimension(80, 21));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AttributeEditorDialog.this.dispose();
			}
		});
		
		JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		mainButtonPanel.add(commitButton);
		mainButtonPanel.add(cancelButton);
		
		//	put the whole stuff together
		this.getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(attributeEditor, BorderLayout.CENTER);
		this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
		
		//	configure window
		this.setResizable(true);
	}
	
//	private void init(Annotation annotation, QueriableAnnotation context) {
//		
//		//	initialize editor
////		final AttributeEditor attributeEditor = new AttributeEditor(annotation, context);
//		this.attributeEditor = new AttributeEditor(annotation, context);
//		
//		//	initialize buttons
//		JButton commitButton = new JButton("OK");
//		commitButton.setBorder(BorderFactory.createRaisedBevelBorder());
//		commitButton.setPreferredSize(new Dimension(80, 21));
//		commitButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				dirty = attributeEditor.writeChanges();
//				AttributeEditorDialog.this.dispose();
//			}
//		});
//		
//		JButton cancelButton = new JButton("Cancel");
//		cancelButton.setBorder(BorderFactory.createRaisedBevelBorder());
//		cancelButton.setPreferredSize(new Dimension(80, 21));
//		cancelButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				AttributeEditorDialog.this.dispose();
//			}
//		});
//		
//		JPanel mainButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//		mainButtonPanel.add(commitButton);
//		mainButtonPanel.add(cancelButton);
//		
//		//	put the whole stuff together
//		this.getContentPane().setLayout(new BorderLayout());
//		this.getContentPane().add(attributeEditor, BorderLayout.CENTER);
//		this.getContentPane().add(mainButtonPanel, BorderLayout.SOUTH);
//		
//		//	configure window
//		this.setResizable(true);
//	}
//	
	/**	@return	true if and only if something was changed
	 */
	public boolean isDirty() {
		return this.dirty;
	}
	
//	/**
//	 * editor for annotation attributes
//	 * 
//	 * @author sautter
//	 */
//	private class AttributeEditor extends JPanel {
//		
//		private static final String DUMMY_ATTRIBUTE_NAME = "Attribute Name";
//		private static final String DUMMY_ATTRIBUTE_VALUE = "Attribute Value";
//		
//		private JTable attributeTable = new JTable();
//		
//		private JComboBox attributeNameField = new JComboBox();
//		private JComboBox attributeValueField = new JComboBox();
//		
//		private StringVector contextAttributeNames = new StringVector();
//		private HashMap contextAttributeValuesByNames = new HashMap();
//		private HashMap contextAttributeValueFrequenciesByNames = new HashMap();
//		
//		private Annotation annotation;
//		private StringVector annotationAttributeNames = new StringVector();
//		private HashMap annotationAttributeValues = new HashMap();
//		
//		private boolean nameFieldKeyPressed = false;
//		private boolean valueFieldKeyPressed = false;
//		
//		AttributeEditor(Annotation annotation, MutableAnnotation context) {
//			super(new BorderLayout(), true);
//			this.annotation = annotation;
//			
//			//	store attributes of annotation being edited
//			String[] attributeNames = annotation.getAttributeNames();
//			Arrays.sort(attributeNames, String.CASE_INSENSITIVE_ORDER);
//			for (int n = 0; n < attributeNames.length; n++) {
//				this.annotationAttributeNames.addElement(attributeNames[n]);
//				this.annotationAttributeValues.put(attributeNames[n], this.annotation.getAttribute(attributeNames[n]));
//			}
//			
//			//	get and index attributes and values of all annotations of same type as the edited one (for offering suggestions)
//			Annotation[] annotations = ((context == null) ? new Annotation[0] : context.getAnnotations(this.annotation.getType()));
//			for (int a = 0; a < annotations.length; a++) {
//				attributeNames = annotations[a].getAttributeNames();
//				for (int n = 0; n < attributeNames.length; n++) {
//					this.contextAttributeNames.addElementIgnoreDuplicates(attributeNames[n]);
//					Object attributeValue = annotations[a].getAttribute(attributeNames[n]);
//					if (attributeValue != null) {
//						this.getValueList(attributeNames[n]).addElementIgnoreDuplicates(attributeValue.toString());
//						this.getValueFrequencyIndex(attributeNames[n]).add(attributeValue.toString());
//					}
//				}
//			}
//			//	get global suggestions
//			this.contextAttributeNames.addContentIgnoreDuplicates(DocumentEditor.getAnnotationAttributeSuggestions(this.annotation.getType()));
//			for (int n = 0; n < this.contextAttributeNames.size(); n++) {
//				String attributeName = this.contextAttributeNames.get(n);
//				String[] suggestions = DocumentEditor.getAttributeValueSuggestions(this.annotation.getType(), attributeName);
//				this.getValueList(attributeName).addContentIgnoreDuplicates(suggestions);
//				for (int s = 0; s < suggestions.length; s++)
//					this.getValueFrequencyIndex(attributeName).add(suggestions[s], (Integer.MAX_VALUE / 2));
//			}
//			
//			//	initialize attribute editor fields
//			this.attributeNameField.setBorder(BorderFactory.createLoweredBevelBorder());
//			this.attributeNameField.setEditable(true);
//			this.resetAttributeNameField();
//			
//			this.attributeNameField.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
//				public void focusGained(FocusEvent fe) {
//					if (DUMMY_ATTRIBUTE_NAME.equals(attributeNameField.getSelectedItem()))
//						attributeNameField.setSelectedItem("");
//				}
//			});
//			((JTextComponent) this.attributeNameField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
//				public void keyPressed(KeyEvent ke) {
//					nameFieldKeyPressed = true;
//				}
//				public void keyReleased(KeyEvent ke) {
//					nameFieldKeyPressed = false;
//				}
//			});
//			this.attributeNameField.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent ae) {
//					if (nameFieldKeyPressed && isVisible() && !attributeNameField.isPopupVisible())
//						attributeValueField.requestFocusInWindow();
//				}
//			});
//			this.attributeNameField.addItemListener(new ItemListener() {
//				public void itemStateChanged(ItemEvent ie) {
//					resetAttributeValueField();
//				}
//			});
//			
//			
//			this.attributeValueField.setBorder(BorderFactory.createLoweredBevelBorder());
//			this.attributeValueField.setEditable(true);
//			this.resetAttributeValueField();
//			
//			this.attributeValueField.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
//				public void focusGained(FocusEvent e) {
//					if (DUMMY_ATTRIBUTE_NAME.equals(attributeValueField.getSelectedItem())) {
//						attributeValueField.setSelectedItem("");
//					}
//				}
//			});
//			((JTextComponent) this.attributeValueField.getEditor().getEditorComponent()).addKeyListener(new KeyAdapter() {
//				public void keyPressed(KeyEvent ke) {
//					valueFieldKeyPressed = true;
//				}
//				public void keyReleased(KeyEvent ke) {
//					valueFieldKeyPressed = false;
//				}
//			});
//			this.attributeValueField.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent ae) {
//					if (valueFieldKeyPressed && isVisible() && !attributeValueField.isPopupVisible())
//						setAttribute();
//				}
//			});
//			
//			//	initialize buttons
//			JButton setAttributeButton = new JButton("Add / Set Attribute");
//			setAttributeButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			setAttributeButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					setAttribute();
//				}
//			});
//			
//			JButton removeAttributeButton = new JButton("Remove Attribute");
//			removeAttributeButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			removeAttributeButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					removeAttribute();
//				}
//			});
//			
//			JButton clearAttributesButton = new JButton("Clear Attributes");
//			clearAttributesButton.setBorder(BorderFactory.createRaisedBevelBorder());
//			clearAttributesButton.addActionListener(new ActionListener() {
//				public void actionPerformed(ActionEvent e) {
//					clearAttributes();
//				}
//			});
//			
//			JPanel attributeButtonPanel = new JPanel(new GridBagLayout(), true);
//			GridBagConstraints gbc = new GridBagConstraints();
//			gbc.weightx = 1;
//			gbc.weighty = 0;
//			gbc.gridwidth = 1;
//			gbc.gridheight = 1;
//			gbc.gridx = 0;
//			gbc.insets.top = 5;
//			gbc.insets.left = 5;
//			gbc.insets.right = 5;
//			gbc.insets.bottom = 5;
//			gbc.fill = GridBagConstraints.HORIZONTAL;
//			gbc.anchor = GridBagConstraints.NORTH;
//			
//			gbc.gridy = 0;
//			attributeButtonPanel.add(this.attributeNameField, gbc.clone());
//			gbc.gridy++;
//			attributeButtonPanel.add(this.attributeValueField, gbc.clone());
//			gbc.gridy++;
//			attributeButtonPanel.add(setAttributeButton, gbc.clone());
//			gbc.gridy++;
//			attributeButtonPanel.add(removeAttributeButton, gbc.clone());
//			gbc.gridy++;
//			attributeButtonPanel.add(clearAttributesButton, gbc.clone());
//			gbc.gridy++;
//			gbc.weighty = 1;
//			attributeButtonPanel.add(new JPanel(), gbc.clone());
//			
//			
//			//	display annotation's data (for giving users the context)
//			JLabel annotationTypeField = new JLabel(this.annotation.getType());
//			annotationTypeField.setBorder(BorderFactory.createLoweredBevelBorder());
//			
//			JLabel startIndexField = new JLabel("Start: " + this.annotation.getStartIndex());
//			startIndexField.setBorder(BorderFactory.createLoweredBevelBorder());
//			
//			JLabel lengthField = new JLabel("Size: " + this.annotation.size());
//			lengthField.setBorder(BorderFactory.createLoweredBevelBorder());
//			
//			JLabel annotationValueField = new JLabel(this.annotation.getValue());
//			annotationValueField.setBorder(BorderFactory.createLoweredBevelBorder());
//			annotationValueField.setToolTipText(this.produceTooltipText(annotation));
//			
//			JPanel annotationDataPanel = new JPanel(new GridBagLayout());
//			gbc = new GridBagConstraints();
//			gbc.weightx = 1;
//			gbc.weighty = 0;
//			gbc.gridwidth = 1;
//			gbc.gridheight = 1;
//			gbc.insets.top = 5;
//			gbc.insets.left = 5;
//			gbc.insets.right = 5;
//			gbc.insets.bottom = 5;
//			gbc.fill = GridBagConstraints.BOTH;
//			gbc.gridy = 0;
//			gbc.gridx = 0;
//			annotationDataPanel.add(annotationTypeField, gbc.clone());
//			gbc.gridx++;
//			annotationDataPanel.add(startIndexField, gbc.clone());
//			gbc.gridx++;
//			annotationDataPanel.add(lengthField, gbc.clone());
//			gbc.gridy = 1;
//			gbc.gridx = 0;
//			gbc.gridwidth = 3;
//			annotationDataPanel.add(annotationValueField, gbc.clone());
//			
//			//	set up attribute table
////			this.attributeTable.setModel(new AttributeEditorTableModel(this.annotationAttributeNames, this.annotation));
//			this.attributeTable.setModel(new AttributeEditorTableModel());
//			this.attributeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//			this.attributeTable.addMouseListener(new MouseAdapter() {
//				public void mouseClicked(MouseEvent me) {
//					if (me.getClickCount() > 1) {
//						int rowIndex = attributeTable.getSelectedRow();
//						if (rowIndex != -1) attributeNameField.setSelectedItem(annotationAttributeNames.get(rowIndex));
//					}
//				}
//			});
//			JScrollPane attributeTableBox = new JScrollPane(this.attributeTable);
//			attributeTableBox.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//			attributeTableBox.setViewportBorder(BorderFactory.createLoweredBevelBorder());
//			
//			//	put the whole stuff together
//			this.setLayout(new BorderLayout());
//			this.add(annotationDataPanel, BorderLayout.NORTH);
//			this.add(attributeTableBox, BorderLayout.CENTER);
//			this.add(attributeButtonPanel, BorderLayout.EAST);
//		}
//		
//		private void resetAttributeNameField() {
//			this.contextAttributeNames.sortLexicographically(false, false);
//			this.attributeNameField.setModel(new DefaultComboBoxModel(this.contextAttributeNames.toStringArray()));
//			this.attributeNameField.setSelectedItem(DUMMY_ATTRIBUTE_NAME);
//		}
//		
//		private void resetAttributeValueField() {
//			Object nameItem = this.attributeNameField.getSelectedItem();
//			if ((nameItem == null) || DUMMY_ATTRIBUTE_NAME.equals(nameItem)) {
//				this.attributeValueField.setModel(new DefaultComboBoxModel(new String[0]));
//				this.attributeValueField.setSelectedItem(DUMMY_ATTRIBUTE_VALUE);
//			}
//			else {
//				StringVector values = this.getValueList(nameItem.toString());
//				this.attributeValueField.setModel(new DefaultComboBoxModel(values.toStringArray()));
//				Object value = this.annotationAttributeValues.get(nameItem.toString());
//				this.attributeValueField.setSelectedItem((value == null) ? DUMMY_ATTRIBUTE_VALUE : value);
//			}
//		}
//		
//		private StringVector getValueList(String attributeName) {
//			StringVector values = ((StringVector) this.contextAttributeValuesByNames.get(attributeName));
//			if (values == null) {
//				values = new StringVector();
//				this.contextAttributeValuesByNames.put(attributeName, values);
//			}
//			return values;
//		}
//		
//		private StringIndex getValueFrequencyIndex(String attributeName) {
//			StringIndex valueIndex = ((StringIndex) this.contextAttributeValueFrequenciesByNames.get(attributeName));
//			if (valueIndex == null) {
//				valueIndex = new StringIndex(true);
//				this.contextAttributeValueFrequenciesByNames.put(attributeName, valueIndex);
//			}
//			return valueIndex;
//		}
//		
//		private String produceTooltipText(Annotation annotation) {
//			if (annotation.length() < 100) return TokenSequenceUtils.concatTokens(annotation);
//			
//			StringVector lines = new StringVector();
//			int startToken = 0;
//			int lineLength = 0;
//			Token lastToken = null;
//			
//			for (int t = 0; t < annotation.size(); t++) {
//				Token token = annotation.tokenAt(t);
//				lineLength += token.length();
//				if (lineLength > 100) {
//					lines.addElement(TokenSequenceUtils.concatTokens(annotation, startToken, (t - startToken + 1)));
//					startToken = (t + 1);
//					lineLength = 0;
//				}
//				else if (Gamta.insertSpace(lastToken, token)) lineLength++;
//			}
//			if (startToken < annotation.size())
//				lines.addElement(TokenSequenceUtils.concatTokens(annotation, startToken, (annotation.size() - startToken)));
//			
//			return ("<HTML>" + lines.concatStrings("<BR>") + "</HTML>");
//		}
//		
//		private void setAttribute() {
//			Object item;
//			
//			String name = null;
//			item = attributeNameField.getSelectedItem();
//			if (item != null) name = item.toString();
//			
//			String value = null;
//			item = attributeValueField.getSelectedItem();
//			if (item != null) value = item.toString();
//			
//			if ((name != null) && (name.length() != 0) && (value != null) && !DUMMY_ATTRIBUTE_NAME.equals(name) && !DUMMY_ATTRIBUTE_VALUE.equals(value)) {
//				
//				//	get old value
//				String oldValue = null;
//				item = this.annotationAttributeValues.get(name);
//				if (item != null) oldValue = item.toString();
//				
//				//	name OK
//				if (AttributeUtils.isValidAttributeName(name)) {
//					
//					/*
//					 * not checking the value any more ... all values are valid,
//					 * only require appropriate escaping
//					 */
//					
//					//	set attribute
//					this.annotationAttributeNames.addElementIgnoreDuplicates(name);
//					this.annotationAttributeValues.put(name, value);
//					
//					//	update value lists
//					this.getValueList(name).addElementIgnoreDuplicates(value);
//					this.getValueFrequencyIndex(name).add(value);
//					if (oldValue != null) {
//						if (this.getValueFrequencyIndex(name).remove(oldValue))
//							this.getValueList(name).removeAll(oldValue);
//					}
//					
//					//	refresh attribute table
//					this.attributeTable.revalidate();
//					this.attributeTable.repaint();
//					
//					//	refresh input fields
//					this.resetAttributeNameField();
//					
////					String modificationString = (this.annotationAttributeNames.contains(name) ? "set" : "add");
////					
////					//	check value
////					if (AttributeUtils.isValidAttributeValue(value)) {
////						
////						//	set attribute
////						this.annotationAttributeNames.addElementIgnoreDuplicates(name);
////						this.annotationAttributeValues.put(name, value);
////						
////						//	update value lists
////						this.getValueList(name).addElementIgnoreDuplicates(value);
////						this.getValueFrequencyIndex(name).add(value);
////						if (oldValue != null) {
////							if (this.getValueFrequencyIndex(name).remove(oldValue))
////								this.getValueList(name).removeAll(oldValue);
////						}
////						
////						//	refresh attribute table
////						this.attributeTable.revalidate();
////						this.attributeTable.repaint();
////						
////						//	refresh input fields
////						this.resetAttributeNameField();
////					}
////					
////					//	show error message
////					else JOptionPane.showMessageDialog(this, ("Cannot " + modificationString + " attribute. The specified value is invalid."), "Invalid Attribute Value", JOptionPane.ERROR_MESSAGE);
//				}
//				
//				//	show error message
//				else JOptionPane.showMessageDialog(this, "Cannot add attribute. The specified name is invalid.", "Invalid Attribute Name", JOptionPane.ERROR_MESSAGE);
//			}
//		}
//
//		private void removeAttribute() {
//			int rowIndex = attributeTable.getSelectedRow();
//			if (rowIndex != -1) {
//				
//				//	get name to remove
//				String name = this.annotationAttributeNames.get(rowIndex);
//				
//				//	get value
//				String value = null;
//				Object valueItem = this.annotationAttributeValues.get(name);
//				if (valueItem != null) value = valueItem.toString();
//				
//				//	update data
//				this.annotationAttributeNames.removeAll(name);
//				this.annotationAttributeValues.remove(name);
//				
//				//	update value lists
//				if (value != null) {
//					if (this.getValueFrequencyIndex(name).remove(value))
//						this.getValueList(name).removeAll(value);
//				}
//				
//				//	refresh attribute table
//				this.attributeTable.revalidate();
//				this.attributeTable.repaint();
//				
//				//	refresh input fields
//				this.resetAttributeNameField();
//			}
//		}
//
//		private void clearAttributes() {
//			if (JOptionPane.showConfirmDialog(this, "Really remove all attributes?", "Confirm Clear Attributes", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
//				while (!this.annotationAttributeNames.isEmpty()) {
//					
//					//	get name to remove
//					String name = this.annotationAttributeNames.lastElement();
//					
//					//	get value
//					String value = null;
//					Object valueItem = this.annotationAttributeValues.get(name);
//					if (valueItem != null) value = valueItem.toString();
//					
//					//	update data
//					this.annotationAttributeNames.removeAll(name);
//					this.annotationAttributeValues.remove(name);
//					
//					//	update value lists
//					if (value != null) {
//						if (this.getValueFrequencyIndex(name).remove(value))
//							this.getValueList(name).removeAll(value);
//					}
//				}
//				
//				//	refresh attribute table
//				this.attributeTable.revalidate();
//				this.attributeTable.repaint();
//				
//				//	refresh input fields
//				this.resetAttributeNameField();
//			}
//		}
//		
//		private boolean writeChanges() {
//			boolean modified = false;
//			
//			StringVector oldAnnotationAttributeNames = new StringVector();
//			oldAnnotationAttributeNames.addContentIgnoreDuplicates(this.annotation.getAttributeNames());
//			
//			StringVector toRemove = oldAnnotationAttributeNames.without(this.annotationAttributeNames);
//			for (int r = 0; r < toRemove.size(); r++) {
//				modified = true;
//				this.annotation.removeAttribute(toRemove.get(r));
//			}
//			
//			StringVector toCheck = oldAnnotationAttributeNames.intersect(this.annotationAttributeNames);
//			for (int c = 0; c < toCheck.size(); c++) {
//				String name = toCheck.get(c);
//				Object oldValue = this.annotation.getAttribute(name);
//				Object newValue = this.annotationAttributeValues.get(name);
//				
//				if (newValue == null) {
//					if (oldValue != null) {
//						modified = true;
//						this.annotation.removeAttribute(name);
//					}
//				}
//				else if ((oldValue == null) || !newValue.equals(oldValue)) {
//					modified = true;
//					this.annotation.setAttribute(name, newValue);
//				}
//			}
//			
//			StringVector toAdd = this.annotationAttributeNames.without(oldAnnotationAttributeNames);
//			for (int a = 0; a < toAdd.size(); a++) {
//				String name = toAdd.get(a);
//				Object value = this.annotationAttributeValues.get(name);
//				if (value != null) {
//					modified = true;
//					this.annotation.setAttribute(name, value);
//				}
//			}
//			
//			return modified;
//		}
//		
//		/**
//		 * table model for displaying annotation attributes
//		 * 
//		 * @author sautter
//		 */
//		private class AttributeEditorTableModel implements TableModel {
//			
//			/** @see javax.swing.table.TableModel#getColumnCount()
//			 */
//			public int getColumnCount() {
//				return 2;
//			}
//
//			/** @see javax.swing.table.TableModel#getRowCount()
//			 */
//			public int getRowCount() {
//				return annotationAttributeNames.size();
//			}
//
//			/** @see javax.swing.table.TableModel#isCellEditable(int, int)
//			 */
//			public boolean isCellEditable(int rowIndex, int columnIndex) {
//				return false;
//			}
//
//			/** @see javax.swing.table.TableModel#getColumnClass(int)
//			 */
//			public Class getColumnClass(int columnIndex) {
//				return String.class;
//			}
//
//			/** @see javax.swing.table.TableModel#getValueAt(int, int)
//			 */
//			public Object getValueAt(int rowIndex, int columnIndex) {
//				if ((rowIndex >= 0) && (rowIndex < annotationAttributeNames.size())) {
//					String aName = annotationAttributeNames.get(rowIndex);
//					return ((columnIndex == 0) ? aName : annotationAttributeValues.get(aName));
//				} else return null;
//			}
//
//			/** @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
//			 */
//			public void setValueAt(Object newValue, int rowIndex, int columnIndex) {}
//
//			/** @see javax.swing.table.TableModel#getColumnName(int)
//			 */
//			public String getColumnName(int columnIndex) {
//				switch (columnIndex) {
//					case 0:	return "Name";
//					case 1:	return "Value";
//				}
//				return null;
//			}
//
//			/** @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
//			 */
//			public void addTableModelListener(TableModelListener l) {}
//
//			/** @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
//			 */
//			public void removeTableModelListener(TableModelListener l) {}
//		}
//	}
}
