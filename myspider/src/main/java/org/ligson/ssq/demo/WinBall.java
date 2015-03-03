package org.ligson.ssq.demo;

import java.util.ArrayList;
import java.util.List;

public class WinBall {
	private String id;
	private int serial;
	private String serialNum;
	private String createDate;
	private List<Integer> redBallList = new ArrayList<Integer>();
	private int blueBall;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getSerial() {
		return serial;
	}

	public void setSerial(int serial) {
		this.serial = serial;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public List<Integer> getRedBallList() {
		return redBallList;
	}

	public void setRedBallList(List<Integer> redBallList) {
		this.redBallList = redBallList;
	}

	public int getBlueBall() {
		return blueBall;
	}

	public void setBlueBall(int blueBall) {
		this.blueBall = blueBall;
	}

	@Override
	public String toString() {
		StringBuffer sb  = new StringBuffer();
		sb.append(" 第"+getSerialNum()+"期");
		sb.append(" 开奖日期:"+getCreateDate());
		sb.append(" 开奖序号："+getSerialNum());
		
		sb.append(" 红球：");
		for(int red:redBallList){
			if(red<10){
				sb.append(" 0"+red);
			}else{
				sb.append(" "+red);
			}
		}
		sb.append(" 篮球:");
		
		if(blueBall<10){
			sb.append(" 0"+blueBall);
		}else{
			sb.append(" "+blueBall);
		}
		
		return sb.toString();
	}
	
	

}
