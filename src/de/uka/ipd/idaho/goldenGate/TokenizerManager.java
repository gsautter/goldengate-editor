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
package de.uka.ipd.idaho.goldenGate;


import de.uka.ipd.idaho.gamta.Tokenizer;
import de.uka.ipd.idaho.goldenGate.plugins.ResourceManager;

/**
 * Manager for the regular expressions used for tokenizing document text
 * 
 * @author sautter
 */
public interface TokenizerManager extends ResourceManager {
	
	/** the name of the Gamta default Tokenizer that allows in-word and in-number punctuation */
	public static final String INNER_PUNCTUATION_TOKENIZER_NAME = "<In-Word Punctuation>";
	
	/** the name of the Gamta default Tokenizer that does not allow in-word and in-number punctuation */
	public static final String NO_INNER_PUNCTUATION_TOKENIZER_NAME = "<No In-Word Punctuation>";
	
	/**	retrieve a Tokenizer by its name
	 * @param	name	the name of the required Tokenizer
	 * @return the Tokenizer with the specified name, or null if there is no such Tokenizer
	 */
	public abstract Tokenizer getTokenizer(String name);
}
