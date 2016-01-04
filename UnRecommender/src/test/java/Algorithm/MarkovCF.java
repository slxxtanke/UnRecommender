package Algorithm;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import DataModel.DataModel;
import DataModel.LongPair;

public class MarkovCF {
	private DataModel dataModel;
	private long userSize,itemSize;
	private HashMap<String, Float> threshold;
	private HashMap<Long, String> store_MccMap;
	private HashMap<Long, SimilarityList> martix;
	public MarkovCF(DataModel model){
		dataModel = model;
		userSize = dataModel.getUserNum();
		itemSize = dataModel.getItemNum();
		threshold = new HashMap<String, Float>();		//每个MCC下店铺的平均值---阀值
		store_MccMap = new HashMap<Long, String>();
		martix = new HashMap<Long, SimilarityList>();
		processThreshold();
		update();
		DataOut();
	}
	private void update(){
		CountList count = new CountList(); //i出现的次数 t(i)
		TransList trans = new TransList(); //t(i,j)
		for (long i=0;i<userSize;i++){
			ArrayList<LongPair> list = (ArrayList<LongPair>) dataModel.getUserHistoryWithTime(i);
			if (list.size()<2) continue;
			for (int j=list.size()-1;j>0;j--){
				int k =j-1;
				LongPair startPair = list.get(j);
				String mcc = startPair.getMcc();
				long storeID1 = startPair.getItem();
				count.add(storeID1);
				
				boolean flag = true;
				do{
					if (k<0){
						break;
					}
					LongPair endPair = list.get(k);
					if (mcc.equals(endPair.getMcc())){
						flag = false;
					}
					long storeID2 = endPair.getItem();
					trans.add(storeID1, storeID2);
					k--;
				}while (flag);
			}
		}
		Iterator<Long> countIterator = count.getIterator();
		while(countIterator.hasNext()){
			long storeID1 = countIterator.next();
			long Ti = count.get(storeID1);
			if (Ti ==0 ) continue;
			SimilarityList similarityList = new SimilarityList(storeID1);
			Iterator<Long> transIterator = trans.getUserIterator(storeID1);
			while(transIterator.hasNext()){
				long storeID2 = transIterator.next();
				
				long Tij = trans.get(storeID1, storeID2);
				if (Tij != 0){
					float value = (float)Tij / Ti;
					similarityList.add(storeID2, value);
				}
			}
			similarityList.resortAndSub();
			martix.put(storeID1, similarityList);
		}
		
	}
	private void  processThreshold(){
		HashMap<String, Long> mccTotal = new HashMap<String, Long>();
		HashMap<String, Long> mccRe = new HashMap<String, Long>(); //mcc 总次数 和 mcc自返次数
		for (long i=0;i<userSize;i++){
			ArrayList<LongPair> list = (ArrayList<LongPair>) dataModel.getUserHistoryWithTime(i);
			if (list.size()<2) continue;
			for (int j=list.size()-1;j>0;j--){
				int k =j-1;
				LongPair startPair = list.get(j);
				String mcc = startPair.getMcc();
				insertMccMap(startPair.getItem(), mcc);	//记录下每个店铺的MCC 
				boolean flag = true;
				do{
					if (k<0){
						break;
					}
					LongPair endPair = list.get(k);
					if (mcc.equals(endPair.getMcc())){
						if (mccTotal.containsKey(mcc)){
							mccTotal.put(mcc, mccTotal.get(mcc)+1);
						}else{
							mccTotal.put(mcc, 1L);
						}
						if (startPair.getItem() == endPair.getItem()){
							if (mccRe.containsKey(mcc)){
								mccRe.put(mcc, mccRe.get(mcc)+1);
							}else{
								mccRe.put(mcc, 1L);
							}
						}
						flag = false;
					}
					k--;
				}while (flag);
			}
		}
		
		Iterator<String> mccsIterator = mccTotal.keySet().iterator();
		while(mccsIterator.hasNext()){
			String mcc = mccsIterator.next();
			long total = mccTotal.get(mcc);
			long re;
			if (mccRe.containsKey(mcc)){
				re = mccRe.get(mcc);
			}else{
				re = 0;
			}
			threshold.put(mcc, (float)re/total);
			//处理
			System.out.println(mcc+"\t"+re+"\t"+total+"\t"+ (float)(1.0*re/total));
		}
	}
		
	private void insertMccMap(Long storeID,String MccCode){
		if (!store_MccMap.containsKey(storeID)){
			store_MccMap.put(storeID, MccCode);
		}
	}
	
	public ArrayList<Long> recommender(String userID, int length){
		return null;
	}
	
	public void DataOut(){
		try {
			FileOutputStream fos = new FileOutputStream("data_out.txt");
			OutputStreamWriter osw=new OutputStreamWriter(fos);
			BufferedWriter bw=new BufferedWriter(osw);
			Iterator<Long> martixIterator = martix.keySet().iterator();
			while(martixIterator.hasNext()){
				long storeID1 = martixIterator.next();
				List<SimilarityItem> list = martix.get(storeID1).getList();
				for (int i = 0; i < list.size(); i++){
					bw.write(storeID1+"\t"+list.get(i).getID()+"\t"+list.get(i).getData()+"\n");
				}
			}
	    	bw.close();
			osw.close();
			fos.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block·
			e.printStackTrace();
		}
		
		
		
	}
}
