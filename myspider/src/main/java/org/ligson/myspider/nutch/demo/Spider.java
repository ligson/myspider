package org.ligson.myspider.nutch.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Spider implements Runnable {
	private String id;
	private String urlPattern;
	private String inputUrl;
	private Store store;
	private List<Spider> sonSpiders = new ArrayList<Spider>();
	private Crawler crawler;
	private static Logger logger = LoggerFactory.getLogger(Spider.class);

	public List<Spider> getSonSpiders() {
		return sonSpiders;
	}

	public void setSonSpiders(List<Spider> sonSpiders) {
		this.sonSpiders = sonSpiders;
	}

	public Crawler getCrawler() {
		return crawler;
	}

	public void setCrawler(Crawler crawler) {
		this.crawler = crawler;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Spider(String id, String urlPattern, String inputUrl, Store store,
			Crawler crawler) {
		super();
		this.id = id;
		this.urlPattern = urlPattern;
		this.store = store;
		this.inputUrl = inputUrl;
		this.crawler = crawler;
	}

	@Override
	public void run() {
		if (!inputUrl.startsWith("http://")) {
			return;
		}

		URL url;
		try {
			url = new URL(inputUrl);
		} catch (MalformedURLException e1) {
			return;
		}
		File dir = new File(store.getRoot(), url.getHost());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File targetFile = null;
		if (url.getFile() == null || "".equals(url.getFile())) {
			targetFile = new File(dir, "_.html");
		} else if (url.getFile().equals("/") || url.getFile().endsWith("/")) {
			if (url.getFile().endsWith("/") && (!url.getFile().equals("/"))) {
				targetFile = new File(dir, url.getFile() + "_.html");
			} else {
				targetFile = new File(dir, "_.html");
			}
		} else {
			targetFile = new File(dir, url.getFile());
		}

		if (!targetFile.getParentFile().exists() || targetFile.getParentFile().isFile()) {
			targetFile.getParentFile().mkdirs();
		}
		logger.debug("抓取链接:" + inputUrl + ",存储位置:" + targetFile.getAbsolutePath());

		String locationHeader = null;
		if (!targetFile.exists()) {
			try {
				if (!targetFile.getParentFile().exists()) {
					targetFile.getParentFile().mkdirs();
				}
				if (!targetFile.getParentFile().exists()) {
					logger.error("父文件夹创建失败!" + targetFile.getAbsolutePath());
				}
				if (targetFile.createNewFile()) {
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(inputUrl);
					HttpResponse httpResponse = httpClient.execute(httpGet);
					if (!httpResponse.getLastHeader("Content-Type").getValue()
							.equals("text/html")) {
						targetFile.delete();
						return;
					}
					if (httpResponse.getLastHeader("Location") != null) {
						locationHeader = httpResponse.getLastHeader("Location")
								.getValue();
					}
					HttpEntity entity = httpResponse.getEntity();
					Charset charset = ContentType.getOrDefault(entity)
							.getCharset();
					if (charset == null) {
						charset = Charset.forName("UTF-8");
					}
					String text = EntityUtils.toString(entity, charset);

					FileUtils.write(targetFile, text);

					// detecting the file type
					BodyContentHandler handler = new BodyContentHandler();
					Metadata metadata = new Metadata();
					FileInputStream inputstream = new FileInputStream(targetFile);
					ParseContext pcontext = new ParseContext();

					// Html parser
					HtmlParser htmlparser = new HtmlParser();
					htmlparser.parse(inputstream, handler, metadata, pcontext);
					/*
					 * System.out.println("Contents of the document:" +
					 * handler.toString());
					 * System.out.println("Metadata of the document:"); String[]
					 * metadataNames = metadata.names();
					 * 
					 * for (String name : metadataNames) {
					 * System.out.println(name + ":   " + metadata.get(name)); }
					 */

				}
				
				logger.debug("抓取URL成功!"+inputUrl);
			} catch (UnknownHostException e) {
				logger.error("链接打开失败:" + inputUrl);
			} catch (Exception e) {
				logger.error("抓取网页(" + inputUrl + ")失败,存储位置："
						+ targetFile.getAbsolutePath() + ";错误信息:" + e.getMessage());
			}
		}
		if (!targetFile.exists() || targetFile.isDirectory()) {
			return;
		}
		String baseUrl = inputUrl;
		if (locationHeader != null) {
			baseUrl = locationHeader;
		}
		List<String> hrefList = HtmlUtils.getAllHrefValues(baseUrl, targetFile);
		// Pattern pattern = Pattern.compile(urlPattern);
		for (String href : hrefList) {
			if (href.matches(urlPattern)) {
				Spider spider = new Spider(UUID.randomUUID().toString(),
						urlPattern, href, store, getCrawler());
				Thread thread = new Thread(spider);

				if (getCrawler().addSpider(spider, thread)) {
					thread.start();
				} else {
					thread = null;
					spider = null;
				}
			}
		}

	}
}
