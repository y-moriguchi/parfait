/*
 * Copyright 2009-2014 Yuichiro Moriguchi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.morilib.parfait.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public final class DeserializeParfaitXML {

	/**
	 * 
	 * @param np
	 * @return
	 */
	public static String readTextNode(Node np) {
		StringBuffer b = new StringBuffer();
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeType() == Node.TEXT_NODE) {
				b.append(nd.getTextContent());
			} else if(nd.getNodeType() == Node.CDATA_SECTION_NODE) {
				b.append(nd.getTextContent());
			}
		}
		return b.toString();
	}

	//
	private Node getnode(NamedNodeMap mp, String x) {
		Node nr;

		if((nr = mp.getNamedItem(x)) == null) {
			throw new RuntimeException();
		}
		return nr;
	}

	//
	String getnodestrOpt(NamedNodeMap mp, String x, String d) {
		Node nr;

		if((nr = mp.getNamedItem(x)) == null) {
			return d;
		}
		return nr.getNodeValue();
	}

	//
	int getnodeint(NamedNodeMap mp, String x) {
		return Integer.parseInt(getnode(mp, x).getNodeValue());
	}

	//
	boolean getnodebool(NamedNodeMap mp, String x) {
		Node nr;

		if((nr = mp.getNamedItem(x)) == null) {
			return false;
		}
		return "true".equalsIgnoreCase(nr.getNodeValue());
	}

	//
	KeywordBean readKeyword(Node np) {
		KeywordBean db = new KeywordBean();
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("name")) {
				db.setKeyword(readTextNode(nd).trim());
			} else if(nd.getNodeName().equals("action")) {
				db.setAction(readTextNode(nd).trim());
			}
		}
		return db;
	}

	//
	KeywordsBean readKeywords(Node np) {
		KeywordsBean db = new KeywordsBean();
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("keyword")) {
				db.add(readKeyword(nd));
			}
		}
		return db;
	}

	//
	DescriptionBean readDesciption(Node np) {
		DescriptionBean db = new DescriptionBean();
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("license")) {
				db.license = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("description")) {
				db.description = readTextNode(nd).trim();
			}
		}
		return db;
	}

	//
	AuxiliaryCodeBean readAuxiliary(Node np) {
		AuxiliaryCodeBean db = new AuxiliaryCodeBean();
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("definition")) {
				db.definition = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("auxiliary")) {
				db.auxiliary = readTextNode(nd).trim();
			}
		}
		return db;
	}

	//
	void readHashComp(ParfaitBean db, Node np) {
		NodeList nl;
		Node nd;

		nl = np.getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("columns")) {
				db.columns = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("pluslength")) {
				db.plusLength = readTextNode(nd).trim().equals("true");
			} else if(nd.getNodeName().equals("auto")) {
				db.automatically =
						readTextNode(nd).trim().equals("true");
			}
		}
	}

	//
	ParfaitBean read(Document doc) {
		ParfaitBean mb = new ParfaitBean();
		NodeList nl;
		Node nd;

		nl = doc.getChildNodes().item(0).getChildNodes();
		for(int k = 0; k < nl.getLength(); k++) {
			nd = nl.item(k);
			if(nd.getNodeName().equals("target-language")) {
				mb.language = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("function-type")) {
				mb.functionType = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("default-action")) {
				mb.defaultAction = readTextNode(nd).trim();
			} else if(nd.getNodeName().equals("ignore-case")) {
				mb.ignoreCase = "true".equals(readTextNode(nd).trim());
			} else if(nd.getNodeName().equals("compute-hash")) {
				readHashComp(mb, nd);
			} else if(nd.getNodeName().equals("keywords")) {
				mb.keywords = readKeywords(nd);
			} else if(nd.getNodeName().equals("description")) {
				mb.description = readDesciption(nd);
			} else if(nd.getNodeName().equals("auxiliary")) {
				mb.auxiliary = readAuxiliary(nd);
			}
		}
		return mb;
	}

	/**
	 * 
	 * @param ins
	 * @return
	 * @throws IOException 
	 */
	public static ParfaitBean read(
			InputStream ins) throws IOException {
		DocumentBuilderFactory bf;
		PushbackInputStream pin;
		DocumentBuilder db;
		int c;

		pin = new PushbackInputStream(ins);
		if((c = pin.read()) < 0) {
			return null;
		}

		try {
			pin.unread(c);
			bf = DocumentBuilderFactory.newInstance();
			db = bf.newDocumentBuilder();
			return new DeserializeParfaitXML().read(db.parse(pin));
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch(SAXException e) {
			e.printStackTrace();
			return null;
		}
	}

}
