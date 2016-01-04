package Algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import java.util.List;

public class SimilarityList {
	//static Similarity staticWealth=new Similarity(0, 0);
	private long id;
	private List<SimilarityItem> list;
	public SimilarityList(long id){
		this.id = id;
		list = new ArrayList<SimilarityItem>();
	}
	public void add(long itemID,float data){
		list.add(new SimilarityItem(itemID,data));
	}
	public void resortAndSub(){
		Comparator<SimilarityItem> comparator = new Comparator<SimilarityItem>(){
			public int compare(SimilarityItem b1, SimilarityItem b2) {
				return b2.getData().compareTo(b1.getData());
			}
		};
		Collections.sort(list,comparator); 
		int size = list.size()>20? 20 :list.size();
		list = list.subList(0, size);
	}
	public List<SimilarityItem> getList(){
		return list;
	}
}
