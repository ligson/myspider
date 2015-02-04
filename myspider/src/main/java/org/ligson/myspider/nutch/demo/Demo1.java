package org.ligson.myspider.nutch.demo;


public class Demo1 {

	public static void main(String[] args) throws Exception{
		Store store = Store.getInstance("file:///d:/tt");
		//^http://.*boful\\.com.*
		Crawler crawler = new Crawler(store, 10, "http://tianlang519241.blog.163.com", "^http://tianlang519241\\.blog\\.163\\.com.*");
		crawler.start();
	}

}
