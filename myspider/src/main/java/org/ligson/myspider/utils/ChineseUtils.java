package org.ligson.myspider.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class ChineseUtils {
	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	/**
	 * 汉字转换位汉语拼音，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(
							nameChar[i], defaultFormat)[0];
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

	public static String[]  sortChinese(String[] names) {
		for(int i = 0;i<names.length;i++){
			for(int j = i+1;j<names.length;j++){
				String py1 = converterToSpell(names[i]);
				String py2 = converterToSpell(names[j]);
				 
				if(py1.compareToIgnoreCase(py2)>0){
					String tmp = names[i];
					names[i] = names[j];
					names[j] = tmp;
				}
			}
			 
		}
		
		return names;
	}

	public static void main(String[] args) {
		// System.out.println(converterToFirstSpell("欢迎来到最棒的Java中文社区"));
		// System.out.println(converterToSpell("欢 迎 来 到 最 棒 的 Java 中 文 社 区"));
		String[] names = new String[] { "艾亦之", "商幻丝", "兰半蕾", "卿半蓉", "程幻霜",
				"霜紫容", "撒山蕾", "绍以枫", "揭海珍", "糜幼巧", "萨尔卉", "巨静翠", "逢雅柏", "阳翠雁",
				"陆雁卉", "广巧儿", "才碧春", "步乐蕊", "郁恨烟", "叶灵之", "操新易", "傅冷珊", "崇听晴",
				"徭绮柔", "盖翠秋", "莱翠琴", "敖念萍", "上官翠烟", "蒿秋蓉", "田慕文", "位书松", "声静薇",
				"朱寄筠", "辟映丝", "卞谷寒", "楚冷春", "滕易菡", "轩辕紫露", "鲜于雨旋", "潭梦梅",
				"栗采南", "殷初亦", "捷思雁", "宰绮之", "刘依菱", "贰恨云", "革小竹", "称春玉", "冷静云",
				"瓜尔佳访安" };
		
		System.out.println(Arrays.toString(sortChinese(names)));
		

	}
}
