package org.ligson.myspider.nutch.demo;

/**
 * 爬虫入口
 * @author ligson
 *
 */
public class Demo1 {

	public static void main(String[] args) throws Exception{
		Store store = Store.getInstance("file:///d:/tt");
		//^http://.*boful\\.com.*
		Crawler crawler = new Crawler(store, 1, "http://www.boful.com", "^http://.*boful\\.com.*");
		crawler.start();
	}

}
