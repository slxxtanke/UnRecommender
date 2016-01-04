package Algorithm;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;

public class TransList {
	Hashtable<Long,HashMap<Long, Long>> list;
	public TransList(){
		list = new Hashtable<Long,HashMap<Long, Long>>();
	}
	public void add(Long itemID1,Long itemID2){
		this.add(itemID1, itemID2, 1);
	}
	public void add(Long itemID1,Long itemID2,long data){
		HashMap<Long, Long> userTrans;
		if (list.containsKey(itemID1)){
			userTrans = list.get(itemID1);
		}else{
			userTrans = new HashMap<Long, Long>();
		}
		if (userTrans.containsKey(itemID2)){
			userTrans.put(itemID2, userTrans.get(itemID2)+data);
		}else{
			userTrans.put(itemID2, data);
		}
		list.put(itemID1, userTrans);
	}
	public long get(Long itemID1,Long itemID2){
		if (list.containsKey(itemID1)){
			HashMap<Long, Long> userTrans = list.get(itemID1);
			if (userTrans.containsKey(itemID2)){
				return userTrans.get(itemID2);
			}
		}
		return 0;
	}
	public Iterator<Long> getUserIterator(Long itemID1){
		if (list.containsKey(itemID1)){
			return list.get(itemID1).keySet().iterator();
		}
		return new HashMap<Long, Long>().keySet().iterator();
	}
}
