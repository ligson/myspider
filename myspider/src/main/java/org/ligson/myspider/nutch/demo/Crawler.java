package org.ligson.myspider.nutch.demo;

import java.util.ArrayList;
import java.util.List;

/***
 * 抓取控制
 * 
 * @author ligson
 *
 */
public class Crawler {
	private Store store;
	private int threadNum;
	private String inputUrl;
	private String urlPattern;
	private List<Thread> threads = new ArrayList<Thread>();
	private List<Spider> spiders = new ArrayList<Spider>();
	private CrawlerQueueMonitor monitor;

	public List<Thread> getThreads() {
		return threads;
	}

	public void setThreads(List<Thread> threads) {
		this.threads = threads;
	}

	public List<Spider> getSpiders() {
		return spiders;
	}

	public void setSpiders(List<Spider> spiders) {
		this.spiders = spiders;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public CrawlerQueueMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(CrawlerQueueMonitor monitor) {
		this.monitor = monitor;
	}

	public Crawler(Store store, int threadNum, String inputUrl,
			String urlPattern) {
		super();
		this.store = store;
		this.threadNum = threadNum;
		this.inputUrl = inputUrl;
		this.urlPattern = urlPattern;
		monitor = new CrawlerQueueMonitor(true, threadNum);
	}

	public void start() {
		for (int i = 0; i < threadNum; i++) {
			Spider spider = new Spider(threadNum + "", urlPattern, inputUrl,
					store, this);
			if (!monitor.addSpider(spider)) {
				spider = null;
			}
		}

		monitor.setRunning(true);
		monitor.start();
	}

	public void stop() {
		monitor.setRunning(false);
		monitor.stop();

	}

}
