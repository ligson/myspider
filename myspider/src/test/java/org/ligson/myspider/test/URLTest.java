package org.ligson.myspider.test;

import java.io.File;
import java.net.URL;

public class URLTest {
	public static void main(String[] args) throws Exception{
		URL url = new URL("http://www.boful.com/successful");
		System.out.println(url.getPath());
		System.out.println(url.getRef());
		System.out.println(url.getQuery());
		System.out.println(new File(url.getPath()));
	}
}
