package DataModel;

import java.util.Date;


public class LongPair{
	public LongPair(long item,long date){
		this.item = item;
		this.date = new Date(date);
	}
	public LongPair(long item,long date,String mcc){
		this.item = item;
		this.date = new Date(date);
		this.mcc = mcc;
	}
	long item; 
	Date date; 
	String mcc;
	public long getItem(){
		return item;
	}
	public Date getDate(){
		return date;
	}
	public String getMcc(){
		return mcc;
	}
}
