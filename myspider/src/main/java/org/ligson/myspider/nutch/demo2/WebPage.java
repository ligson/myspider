package org.ligson.myspider.nutch.demo2;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.ligson.myspider.utils.HtmlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebPage {

	private static Logger logger = LoggerFactory.getLogger(WebPage.class);
	private String title;
	private String url;
	private String guid = UUID.randomUUID().toString();
	private String text;
	private String html;
	private String domain;
	private List<String> allLinkList = new ArrayList<String>();
	private LinkPool linkPool = LinkPool.getInstance();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<String> getAllLinkList() {
		return allLinkList;
	}

	public void setAllLinkList(List<String> allLinkList) {
		this.allLinkList = allLinkList;
	}

	public WebPage() {
	}

	public WebPage(String inputUrl) throws Exception {

		if (!inputUrl.startsWith("http://")) {
			throw new Exception("url格式不正确");
		}

		URL url = null;
		try {
			url = new URL(inputUrl);
		} catch (MalformedURLException e1) {
			throw new Exception("url格式不正确");
		}

		logger.debug("抓取链接:" + inputUrl);

		String text = HtmlUtils.getHtmlFileContent(inputUrl);

		if (text != null) {
			this.html = text;
		} else {
			throw new Exception("解析失败");
		}
		
		linkPool.addUrl(inputUrl);

		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();

		InputStream inputStream = new ByteArrayInputStream(text.getBytes());
		ParseContext pcontext = new ParseContext();

		HtmlParser htmlparser = new HtmlParser();
		htmlparser.parse(inputStream, handler, metadata, pcontext);
		String textContent = handler.toString().replaceAll(" ", "");
		this.text = textContent;
		this.title = HtmlUtils.getFirstTagText(text, "title");
		this.domain = url.getHost();
		this.url = inputUrl;
		String baseUrl = "http://" + this.domain;
		if (url.getPort() != -1 && url.getPort() != 80) {
			baseUrl += ":" + url.getPort();
		}

		List<String> links = HtmlUtils.getAllHrefValues(baseUrl, text);
		for(String link : links) {
			allLinkList.add(link);
			links.add(link);
			linkPool.addUrl(link);
		}

	}
}
