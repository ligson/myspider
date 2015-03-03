package org.ligson.ssq.demo;

import java.util.Arrays;

public class Test {
	public static void main(String[] args) {
		int[] arr = new int[]{10,585,215,84,5,1,5,858,52,5584,4,1441};
		for(int i = 0 ;i<arr.length;i++){
			for(int j = i+1;j<arr.length;j++){
				if(arr[j]>arr[i]){
					int tmp = arr[i];
					arr[i] = arr[j];
					arr[j] = tmp;
				}
			}
		}
		System.out.println(Arrays.toString(arr));
	}
}
