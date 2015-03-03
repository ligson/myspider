package org.ligson.myspider.nutch.demo2;

import java.util.HashSet;
import java.util.Set;

public class LinkPool {
	private static Set<String> links = new HashSet<String>();
	private static LinkPool linkPool = null;
	private int linkNum = 10;

	private LinkPool() {

	}

	public synchronized static LinkPool getInstance() {
		if (linkPool == null) {
			linkPool = new LinkPool();
		}
		return linkPool;
	}

	public synchronized void addUrl(String url) {
		links.add(url);
	}

	public synchronized void remove(String url) {
		links.remove(url);
	}
}
