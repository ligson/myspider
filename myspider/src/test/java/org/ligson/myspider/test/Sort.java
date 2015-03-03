package org.ligson.myspider.test;

import java.util.Arrays;

public class Sort {
	public static void main(String[] args) {
		int[] arr = new int[]{10,55,8854,6654,22,44,9996, 33};
		for(int i = 0;i<arr.length;i++){
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
