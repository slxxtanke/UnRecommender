package Apriori;

import java.util.Date;


public class ItemPair{
	public ItemPair(String item,long date){
		this.item = item;
		this.date = new Date(date);
	}
	public ItemPair(String item,long date,String mcc){
		this.item = item;
		this.date = new Date(date);
		this.mcc = mcc;
	}
	public ItemPair(String item,long date,String mcc, long money){
		this.item = item;
		this.date = new Date(date);
		this.mcc = mcc;
		this.money = money;
	}
	String item; 
	long money;
	Date date; 
	String mcc;
	public String getItem(){
		return item;
	}
	public Date getDate(){
		return date;
	}
	public String getMcc(){
		return mcc;
	}
	public long getMoney(){
		return money;
	}
}
