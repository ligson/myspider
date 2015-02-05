package org.ligson.myspider.nutch.demo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.nutch.util.URLUtil;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/***
 * html工具
 * 
 * @author ligson
 *
 */
public class HtmlUtils {
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

	public static List<String> getAllHrefValues(String baseUrl, File htmlFile) {

		List<String> hrefTags = new ArrayList<String>();
		try {
			// 生成html parser
			DOMParser parser = new DOMParser();
			BufferedReader in = new BufferedReader(new FileReader(htmlFile));
			parser.parse(new InputSource(in));
			Document doc = parser.getDocument();
			// 获得body节点，以此为根，计算其文本内容
			NodeList nodeList = doc.getElementsByTagName("a");
			if (baseUrl.endsWith("/")) {
				baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
			}
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				NamedNodeMap namedNodeMap = node.getAttributes();
				Node hrefNode = namedNodeMap.getNamedItem("href");
				if (hrefNode != null) {
					if ((hrefNode.getNodeValue() != null)
							&& (!"".equals((hrefNode.getNodeValue() + "")
									.trim()))) {
						String nodeValue = hrefNode.getNodeValue();
						hrefTags.add(nodeValue);
					}
				}
			}
		} catch (IOException | SAXException exception) {
			exception.printStackTrace();
		}

		return getAbsPath(baseUrl, hrefTags);
	}

	private static List<String> getAbsPath(String baseUrl, List<String> hrefs) {

		List<String> result = new ArrayList<String>();
		URL url = null;
		try {
			url = new URL(baseUrl);
		} catch (MalformedURLException e) {
			return result;
		}
		String rootPath = null;

		try {
			rootPath = url.toURI().getScheme() + "://";
			rootPath += url.getHost();
			if (url.getPort() != -1 && url.getPort() != 80) {
				rootPath += ":" + url.getPort();
			}
		} catch (URISyntaxException e) {
			return result;
		}
		for (String href : hrefs) {
			String resultUrl = null;
			// 是否重定向

			// 过滤js代码
			if (href.toLowerCase().trim().startsWith("javascript:")) {
				continue;
			}

			// 处理./和../
			if (href.startsWith("./") || href.startsWith("../")) {
				resultUrl = baseUrl + "/" + href;
			}
			// 处理http://
			if (href.startsWith("http://")) {
				resultUrl = href;
			}

			// 处理/
			if (href.startsWith("/")) {
				if (href.equals("/")) {
					resultUrl = rootPath;
				} else {
					resultUrl = rootPath + href;
				}
			}

			// 处理index.html
			if ((!href.startsWith("http://")) && (!href.startsWith("./"))
					&& (!href.startsWith("../")) && (!href.startsWith("/"))) {
				if (baseUrl.endsWith("/")) {
					resultUrl = baseUrl + href;
				}
			}
			if (resultUrl != null) {
				result.add(resultUrl);
			}

		}
		Set<String> hashSet = new HashSet<String>();
		for (String href : result) {
			hashSet.add(href);
		}

		result.clear();
		for (String href : hashSet) {
			result.add(href);
		}

		return result;
	}

	public static String getHtmlFileContent(String inputUrl) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(inputUrl);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			if (!httpResponse.getLastHeader("Content-Type").getValue()
					.contains("text/html")) {
				return null;
			}

			HttpEntity entity = httpResponse.getEntity();
			Charset charset = ContentType.getOrDefault(entity).getCharset();
			if (charset == null) {
				charset = Charset.forName("UTF-8");
			}

			return EntityUtils.toString(entity, charset);
		} catch (Exception e) {
			return null;
		}
	}
}
