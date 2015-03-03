package org.ligson.myspider.test;

import java.util.Arrays;
import java.util.Comparator;
import net.sourceforge.pinyin4j.PinyinHelper;

public class Sort2 {
	
	public static void main(String[] args) {
		
	String[] arr = new String[] { "艾亦之", "商幻丝", "兰半蕾", "卿半蓉", "程幻霜",
				"霜紫容", "撒山蕾", "绍以枫", "揭海珍", "糜幼巧", "萨尔卉", "巨静翠", "逢雅柏", "阳翠雁",
				"陆雁卉", "广巧儿", "才碧春", "步乐蕊", "郁恨烟", "叶灵之", "操新易", "傅冷珊", "崇听晴",
				"徭绮柔", "盖翠秋", "莱翠琴", "敖念萍", "上官翠烟", "蒿秋蓉", "田慕文", "位书松", "声静薇",
				"朱寄筠", "辟映丝", "卞谷寒", "楚冷春", "滕易菡", "轩辕紫露", "鲜于雨旋", "潭梦梅",
				"栗采南", "殷初亦", "捷思雁", "宰绮之", "刘依菱", "贰恨云", "革小竹", "称春玉", "冷静云",
				"瓜尔佳访安" };;
		Arrays.sort(arr,new ComparatorPinYin());
		for (String string : arr) {
			System.out.println(string);
		}
	}
	/**
	 * 功能：实现汉语拼音序比较
	 *
	 */
	static  class ComparatorPinYin implements Comparator<String>{
		@Override
		public int compare(String o1, String o2) {
			return ToPinYinString(o1).compareTo(ToPinYinString(o2));
		}
		
		private String ToPinYinString(String str){
			
			StringBuilder sb=new StringBuilder();
			String[] arr=null;
			
			for(int i=0;i<str.length();i++){
				arr=PinyinHelper.toHanyuPinyinStringArray(str.charAt(i));
				if(arr!=null && arr.length>0){
					for (String string : arr) {
						sb.append(string);
					}
				}
			}
			
			return sb.toString();
		}
		
	}
}
