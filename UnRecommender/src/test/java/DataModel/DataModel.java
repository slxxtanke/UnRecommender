package DataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import java.sql.*;

import Tool.DataSource;


/**
 * @author tk
 *
 */

public class DataModel{

	private HashMap<Long,ArrayList<LongPair>> Users_BuyListHashMap;
	private HashMap<String, Long> User_ID2UserMap,Item_ID2ItemMap;  
	private HashMap<Long, String> User2User_IDMap,Item2Item_IDMap;
	//private HashMap<String, Double> UserWeight;
	private HashMap<String, Double> StoreWeight;
	
	/**
	 * 初始�?
	 */
	public DataModel(){
		User_ID2UserMap = new HashMap<String, Long>(); //用户ID--用户重编�?的键值对
		Item_ID2ItemMap = new HashMap<String, Long>(); //物品ID--物品重编�?的键值对
		User2User_IDMap = new HashMap<Long, String>(); //用户重编�?-用户ID 的键值对
		Item2Item_IDMap = new HashMap<Long, String>(); //物品重编�?-物品ID 的键值对
		Users_BuyListHashMap = new HashMap<Long,ArrayList<LongPair>>(); //用户-Item 列表
		//UserWeight = map;
		update();
	}
	
	
	/**
	 * @param User_ID 用户ID
	 * @return 用户重编号的ID �?�?��计数
	 */
	public long getUserHashID(String User_ID){
		if (User_ID2UserMap.containsKey(User_ID)){
			return User_ID2UserMap.get(User_ID).longValue();
		}else{
			long id = User_ID2UserMap.size();
			User_ID2UserMap.put(User_ID, id);
			User2User_IDMap.put(id, User_ID);
			return id;
		}
	}
	
	
	/**
	 * @param Item_ID 物品ID
	 * @return 物品重编�?�?�?��计数
	 */
	public long getItemHashID(String Item_ID){
		if (Item_ID2ItemMap.containsKey(Item_ID)){
			return Item_ID2ItemMap.get(Item_ID).longValue();
		}else{
			long id = Item_ID2ItemMap.size();
			Item_ID2ItemMap.put(Item_ID, id);
			Item2Item_IDMap.put(id, Item_ID);
			return id;
		}
	}

	/** 获取用户总数
	 * @return 用户总数�?
	 */
	public long getUserNum(){
		return Users_BuyListHashMap.size();
	}
	
	public long getItemNum(){
		return Item_ID2ItemMap.size();
	}

	
	public void update(){
		try{
			Connection conn = getConn();		
			Statement stmt = conn.createStatement();
			String sql ="SELECT t1.USERID,t1.StoreID,UNIX_TIMESTAMP(t1.Time),t1.MCC,t1.Money  FROM data_train t1 ORDER BY t1.USERID,t1.Time";
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象	   
			long userID,itemID;
            String mccType;
            long timeStamp;
            long money;
            while (rs.next()){
            	userID=getUserHashID(rs.getString(1));//传入String获取用户ID
            	itemID=getItemHashID(rs.getString(2));//传入String获取物品ID
            	timeStamp=rs.getLong(3);
	            mccType=rs.getString(4);
	            money = rs.getLong(5);
		        if (Users_BuyListHashMap.containsKey(userID)){
		        	ArrayList<LongPair> UserBuyList = Users_BuyListHashMap.get(userID);
		            UserBuyList.add(new LongPair(itemID, timeStamp*1000, mccType, money));
		        }else{
		            ArrayList<LongPair> UserBuyList = new ArrayList<LongPair>();
		            UserBuyList.add(new LongPair(itemID, timeStamp*1000, mccType, money));
		            Users_BuyListHashMap.put(userID, UserBuyList);
		        }
            }
    		rs.close();
    		stmt.close();
    		conn.close();
   
		}catch(Exception e){
			e.printStackTrace();
        }
		
		Comparator<LongPair> comparator = new Comparator<LongPair>(){
			public int compare(LongPair b1, LongPair b2) {
				return b2.getDate().compareTo(b1.getDate());
			}
		};
        Iterator<Map.Entry<Long, ArrayList<LongPair>>> entries = Users_BuyListHashMap.entrySet().iterator();  
		while (entries.hasNext()) {  
		    Map.Entry<Long, ArrayList<LongPair>> entry = entries.next();
		    
		    Collections.sort(entry.getValue(),comparator); 
		}
	}

	


	public int userListHas(long user,long item){
		int k=0;
		if (Users_BuyListHashMap.containsKey(user)){
			ArrayList<LongPair> pair = Users_BuyListHashMap.get(user);
			
			for (int i=0;i<pair.size();i++){
				if (pair.get(i).getItem()==item){
					k=1;
				}
			}
		}
		return k;
	}

	public List<LongPair> getUserHistoryWithTime(long user) {
		if (Users_BuyListHashMap.containsKey(user)){
			return Users_BuyListHashMap.get(user);
		}
		return new ArrayList<LongPair>();
	}
	
	public double getUserWeight(long user){
		return 1.0;
	}
	
	public List<LongPair> getUserHistoryWithTime(String userID) {
		ArrayList<LongPair> list =  new ArrayList<LongPair>();
		try{
			Connection conn = getConn();		
			Statement stmt = conn.createStatement();
			String sql ="SELECT t1.StoreID,UNIX_TIMESTAMP(t1.Time),t1.MCC  FROM data_train t1 WHERE t1.USERID = '"+userID+"'  ORDER BY t1.Time DESC";
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象	   
			long v2;
            String v4;
            long v3;
            while (rs.next()){
	            v2=getItemHashID(rs.getString(1));//传入String获取物品ID
	            v3=rs.getLong(2);
		        v4=rs.getString(3);
		        list.add(new LongPair(v2, v3*1000,v4));
            }
    		rs.close();
    		stmt.close();
    		conn.close();
   
		}catch(Exception e){
			e.printStackTrace();
        }
		return list;
	}
	

	private Connection getConn() throws Exception{		
		return DataSource.getConn();
	}	
	
	public long mapUser_ID2User(String UserID) {
		if (User_ID2UserMap.containsKey(UserID))
			return User_ID2UserMap.get(UserID);
		else return -1;
	}
	
	
	public String mapUser2USER_ID(long user) {
		return User2User_IDMap.get(user);
	}

	public String mapItem2CONTEN_ID(long item) {
		return Item2Item_IDMap.get(item);
	}
	public long mapCONTENT_ID2item(String item) {
		return getItemHashID(item);
	}
	
}
