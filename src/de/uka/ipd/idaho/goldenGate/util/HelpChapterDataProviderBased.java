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
 *     * Neither the name of the Universitaet Karlsruhe (TH) / KIT nor the
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

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import de.uka.ipd.idaho.easyIO.help.HelpChapter;
import de.uka.ipd.idaho.easyIO.streams.OnDemandReader;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePlugin;
import de.uka.ipd.idaho.goldenGate.plugins.GoldenGatePluginDataProvider;

/**
 * Help chapter implementation using a GoldenGatePluginDataProvider for loading
 * the help content on the fly. This is a GoldenGATE plugin specific version of
 * DynamicHelpChapter.
 * 
 * @author sautter
 */
public class HelpChapterDataProviderBased extends HelpChapter {
	private GoldenGatePluginDataProvider dataProvider;
	private String dataName;
	
	/**	Constructor
	 * @param 	title	the title for this HelpChapter
	 * @param 	dataProvider	the data provider to load the text of this HelpChapter from
	 */
	public HelpChapterDataProviderBased(String title, GoldenGatePluginDataProvider dataProvider) {
		this(title, dataProvider, GoldenGatePlugin.HELP_FILE_NAME);
	}
	
	/**	Constructor
	 * @param 	title	the title for this HelpChapter
	 * @param 	dataProvider	the data provider to load the text of this HelpChapter from
	 * @param	dataName	the name of the data item to load the text from
	 */
	public HelpChapterDataProviderBased(String title, GoldenGatePluginDataProvider dataProvider, String dataName) {
		super(title, "<HTML>The content of this chapter will be loaded from<BR><TT>" + dataName + "</TT><BR>on demand.</HTML>");
		this.dataProvider = dataProvider;
		this.dataName = dataName;
	}
	
	/* (non-Javadoc)
	 * @see de.uka.ipd.idaho.easyIO.help.HelpChapter#getTextReader()
	 */
	public Reader getTextReader() {
		return new OnDemandReader() {
			protected Reader getReader() throws IOException {
				if (dataProvider.isDataAvailable(dataName))
					return new InputStreamReader(dataProvider.getInputStream(dataName), "UTF-8");
				else return new StringReader("<html><body>" +
						"<h3><font face=\"Verdana\">" + getTitle() + "</font></h3>" +
						"<font face=\"Verdana\" size=\"2\">Help on " + getTitle() + " is not available, please contact your administrator.</font>" +
						"</body></html>");
			}
		};
	}
}