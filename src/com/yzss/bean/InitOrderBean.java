package com.yzss.bean;

import java.util.List;

public class InitOrderBean {

	private String sn;
	private BnAddress address;
	private List<BnShopping> items;
	private int count;
	private double amount;
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public BnAddress getAddress() {
		return address;
	}
	public void setAddress(BnAddress address) {
		this.address = address;
	}

	public List<BnShopping> getItems() {
		return items;
	}
	public void setItems(List<BnShopping> items) {
		this.items = items;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	

}
