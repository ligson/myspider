package org.ligson.myspider.nutch.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrawlerQueueMonitor implements Runnable {
	private boolean isRunning = false;
	private int threadNum = 1;
	private List<Thread> threads = new ArrayList<Thread>();
	private List<Spider> spiders = new ArrayList<Spider>();
	private Map<Thread, Spider> spiderThreadMap = new HashMap<Thread, Spider>();

	private Thread monitorThread = new Thread();
	private List<Spider> waitSpiders = new ArrayList<Spider>();

	public CrawlerQueueMonitor(boolean isRunning, int threadNum) {
		this.isRunning = isRunning;
		this.threadNum = threadNum;
		monitorThread = new Thread(this);
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

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

	public Map<Thread, Spider> getSpiderThreadMap() {
		return spiderThreadMap;
	}

	public void setSpiderThreadMap(Map<Thread, Spider> spiderThreadMap) {
		this.spiderThreadMap = spiderThreadMap;
	}

	public Thread getMonitorThread() {
		return monitorThread;
	}

	public void setMonitorThread(Thread monitorThread) {
		this.monitorThread = monitorThread;
	}

	public List<Spider> getWaitSpiders() {
		return waitSpiders;
	}

	public void setWaitSpiders(List<Spider> waitSpiders) {
		this.waitSpiders = waitSpiders;
	}

	public void start() {
		monitorThread.start();
	}

	public void stop() {
		monitorThread.interrupt();
	}

	public synchronized boolean addSpider(Spider spider) {
		if (spiders.size() <= threadNum) {
			Thread thread = null;
			for (Thread thread2 : spiderThreadMap.keySet()) {
				if (spiderThreadMap.get(thread2) != spider) {
					thread = thread2;
					break;
				}
			}
			if (thread == null) {
				thread = new Thread(spider);
			}
			for (Spider spider2 : spiders) {
				if (spider2.getInputUrl().equals(spider.getInputUrl())) {
					return false;
				}
			}
			spiders.add(spider);
			threads.add(thread);
			spiderThreadMap.put(thread, spider);

			return true;
		} else {
			waitSpiders.add(spider);
		}
		return true;

	}

	public synchronized boolean removeSpider(Spider spider) {
		return false;
	}

	@Override
	public void run() {
		while (isRunning) {
			System.out.println(threads.size());
			for (Thread thread : threads) {
				System.out.println(thread);
				if (!thread.isAlive()) {
					thread.start();
				}
			}
			if (spiders.size() <= threadNum) {
				int cha = threadNum - spiders.size();
				int minIndex = Math.min(cha, waitSpiders.size());
				for (int i = 0; i < minIndex; i++) {
					Spider spider = waitSpiders.get(i);
					if (addSpider(spider)) {
						waitSpiders.remove(spider);
					}
				}

			}
		}
	}
}
