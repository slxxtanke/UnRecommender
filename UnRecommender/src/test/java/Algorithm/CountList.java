package Algorithm;

import java.util.Hashtable;
import java.util.Iterator;
public class CountList {
	Hashtable<Long,Long> list;
	public CountList(){
		list = new Hashtable<Long,Long>();
	}
	public void add(long itemID){
		this.add(itemID, 1);
	}
	public void add(Long itemID,long data){
		if (!list.containsKey(itemID)){
			list.put(itemID, data);
		}else{
			list.put(itemID,list.get(itemID) + data);
		}
	}

	public long get(Long itemID){
		return list.get(itemID);
	}
	
	public Iterator<Long> getIterator(){
		return list.keySet().iterator();
	}
}
