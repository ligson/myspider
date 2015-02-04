package org.ligson.myspider.nutch.demo;

import java.io.File;
import java.net.URL;

public class Store {
	private static Store store;
	private URL url;
	private File root;

	private Store(URL url) throws Exception {
		this.url = url;
		setRoot(new File(url.getFile()));
	}

	public void setRoot(File root) {
		this.root = root;
	}

	public File getRoot() {
		return this.root;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public synchronized static Store getInstance(String url) throws Exception {
		if (store == null) {
			store = new Store(new URL(url));
		}
		return store;
	}
}
