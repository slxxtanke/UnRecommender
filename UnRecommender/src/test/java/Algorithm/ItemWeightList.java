package Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ItemWeightList {
	private HashMap<Long, Float> weightedList;
	public ItemWeightList(){
		weightedList = new HashMap<Long, Float>();
	}
	public void mergeList(List<SimilarityItem> list){
		for (int i = 0; i<list.size();i++){
			long storeID = list.get(i).getID();
			float value = list.get(i).getData();
			if (weightedList.containsKey(storeID)){
				weightedList.put(storeID, weightedList.get(storeID)+value);
			}else{
				weightedList.put(storeID, value);
			}
		}
	}
	public ArrayList<Long> toRecommendList(int length){
		ArrayList<SimilarityItem> recommendList = new ArrayList<SimilarityItem>();
		Iterator<Long> weightIterator = weightedList.keySet().iterator();
		while(weightIterator.hasNext()){
			long storeID = weightIterator.next();
			recommendList.add(new SimilarityItem(storeID, weightedList.get(storeID)));
		}
		Comparator<SimilarityItem> comparator = new Comparator<SimilarityItem>(){
			public int compare(SimilarityItem b1, SimilarityItem b2) {
				return b2.getData().compareTo(b1.getData());
			}
		};
		Collections.sort(recommendList,comparator); 
		ArrayList<Long> returnList = new ArrayList<Long>();
		int size = recommendList.size()>length? length :recommendList.size();
		for (int i=0;i<size;i++){
			returnList.add(recommendList.get(i).getID());
		}
		return returnList;
	}
	
}
