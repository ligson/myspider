package org.ligson.ssq.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/*
 * http://www.17500.cn/getData/ssq.TXT
 */
public class DataUtils {
	private static final String DATA_URL = "http://www.17500.cn/getData/ssq.TXT";
	public static List<WinBall> winBalls = new ArrayList<WinBall>();

	private static String getTxtData() {
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(DATA_URL);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static void fillValue(String txt) {
		String[] lines = txt.split("\n");
		for (String line : lines) {
			String[] items = line.split(" ");
			String serialNum = items[0];
			String createDate = items[1];
			String num1 = items[2];
			String num2 = items[3];
			String num3 = items[4];
			String num4 = items[5];
			String num5 = items[6];
			String num6 = items[7];
			String num7 = items[8];
			WinBall winBall = new WinBall();
			winBall.setId(UUID.randomUUID().toString().replaceAll("-", "")
					.toUpperCase());
			winBall.setSerial(winBalls.size() + 1);
			winBall.setCreateDate(createDate);
			winBall.setSerialNum(serialNum);
			List<Integer> redList = winBall.getRedBallList();
			redList.add(Integer.parseInt(num1));
			redList.add(Integer.parseInt(num2));
			redList.add(Integer.parseInt(num3));
			redList.add(Integer.parseInt(num4));
			redList.add(Integer.parseInt(num5));
			redList.add(Integer.parseInt(num6));
			winBall.setBlueBall(Integer.parseInt(num7));

			winBalls.add(winBall);
		}
	}

	static {
		String txt = getTxtData();
		fillValue(txt);
		System.out.println("最近一期开奖数据：" + winBalls.get(winBalls.size() - 1));
	}

	// 统计每位上数字出现的次数
	public static void calcDemo1() {
		List<Map<Integer, Integer>> countList = new ArrayList<Map<Integer, Integer>>();
		for (int i = 0; i < 7; i++) {
			Map<Integer, Integer> map = new TreeMap<Integer, Integer>(
					new Comparator<Integer>() {
						public int compare(Integer obj1, Integer obj2) {
							// 降序排序
							return obj1 - obj2;
						}
					});
			countList.add(map);

		}
		for (int i = 1; i < 34; i++) {
			for (WinBall winBall : winBalls) {
				List<Integer> redBalls = winBall.getRedBallList();
				// red

				for (int j = 0; j < 6; j++) {
					Map<Integer, Integer> map = countList.get(j);
					if (redBalls.get(j) == i) {
						if (map.containsKey(i)) {
							map.put(i, map.get(i) + 1);
						} else {
							map.put(i, 1);
						}
					}

					for (int k = 1; k < 34; k++) {
						if (!map.containsKey(k)) {
							map.put(k, 0);
						}
					}
				}

			}
		}

		for (int i = 1; i < 17; i++) {
			for (WinBall winBall : winBalls) {
				Map<Integer, Integer> map = countList.get(6);
				if (winBall.getBlueBall() == i) {
					if (map.containsKey(i)) {
						map.put(i, map.get(i) + 1);
					} else {
						map.put(i, 1);
					}
				}

				for (int k = 1; k < 17; k++) {
					if (!map.containsKey(k)) {
						map.put(k, 0);
					}
				}
			}
		}

		for (Map<Integer, Integer> map : countList) {
			System.out.println(map);
		}

		System.out.println("值排序");

		
		List<Integer> result = new ArrayList<Integer>();
		List<Integer> redPool = new ArrayList<Integer>();
		List<Integer> bluePool = new ArrayList<Integer>();
		
		for(int i = 1;i<34;i++){
			redPool.add(i);
			if(i<17){
				bluePool.add(i);
			}
		}
		
		for (Map<Integer, Integer> map : countList) {
			Set<Integer> keySet = map.keySet();
			List<List<Integer>> list = new ArrayList<List<Integer>>();
			
			for (Integer key : keySet) {
				List<Integer> tmp = new ArrayList<Integer>();
				tmp.add(key);
				tmp.add(map.get(key));
				list.add(tmp);
			}
			
			for(int i = 0;i<list.size();i++){
				
				for(int j = i+1;j<list.size();j++){
					List<Integer> tmp1 = list.get(i);
					List<Integer> tmp2 = list.get(j);
					if(tmp2.get(1)>tmp1.get(1)){
						list.set(i,tmp2);
						list.set(j,tmp1);
						
					}
				}
			}
			
			List<Integer> zeroList = new ArrayList<Integer>();
			for(int i = 0;i<list.size();i++){
				if(list.get(i).get(1)==0){
					zeroList.add(list.get(i).get(0));
				}
			}
			
			zeroList.removeAll(result);
			if(zeroList.size()==0){
				result.add(list.get(list.size()-1).get(0));
			}else {
				int index = (int)(Math.random()*zeroList.size());
				result.add(zeroList.get(index));
			}

		}
		System.out.println("预测开奖");
		System.out.println(result);

	}

	public static void main(String[] args) {
		calcDemo1();
	}
}
