package org.ligson.myspider.test;

import java.io.BufferedReader;
import java.io.FileReader;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class NekoHtmlDemo {
	public static String TextExtractor(Node root) {
		// 若是文本节点的话，直接返回
		if (root.getNodeType() == Node.TEXT_NODE) {
			return root.getNodeValue().trim();
		}
		if (root.getNodeType() == Node.ELEMENT_NODE) {
			Element elmt = (Element) root;
			// 抛弃脚本
			if (elmt.getTagName().equals("STYLE")
					|| elmt.getTagName().equals("SCRIPT"))
				return "";

			NodeList children = elmt.getChildNodes();
			StringBuilder text = new StringBuilder();
			for (int i = 0; i < children.getLength(); i++) {
				text.append(TextExtractor(children.item(i)));
			}
			return text.toString();
		}
		// 对其它类型的节点，返回空值
		return "";
	}

	public static void main(String[] args) throws Exception {
		// 生成html parser
		DOMParser parser = new DOMParser();
		// 设置网页的默认编码
		/*parser.setProperty(
				"http://www.iteye.com",
				"UTF-8");*/
		// input file
		BufferedReader in = new BufferedReader(new FileReader("D:/tt/www.iteye.com/_.html"));
		parser.parse(new InputSource(in));
		Document doc = parser.getDocument();
		// 获得body节点，以此为根，计算其文本内容
		NodeList nodeList = doc.getElementsByTagName("a");
		for(int i = 0;i<nodeList.getLength();i++){
			System.out.println(nodeList.item(i).getAttributes().getNamedItem("href").getNodeValue());
		}
	}
}
