package Apriori;

import java.io.BufferedReader;  
import java.io.BufferedWriter;
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;
import java.io.FileReader;  
import java.io.IOException;  
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;  
import java.util.HashSet;  
import java.util.Iterator;
import java.util.Map;  
import java.util.Set;  
import java.util.TreeSet;  

import Tool.DataSource;
  
import junit.framework.TestCase;  
/** 
* <B>Apriori算法测试类</B> 
*  
* @author king 
* @date 2013/07/28  
*/  
public class AprioriTest extends TestCase {  
  
    private Apriori apriori;  
    private Map<String, Set<String>> txDatabase;  
    private Float minSup = new Float("0.001");  
    private Float minConf = new Float("0.05");  
	private HashMap<String, Long> Item_ID2ItemMap;  
	private HashMap<Long, String> Item2Item_IDMap;
    public AprioriTest(){
		Item_ID2ItemMap = new HashMap<String, Long>(); //物品ID--物品重编�?的键值对
		Item2Item_IDMap = new HashMap<Long, String>(); //物品重编�?-物品ID 的键值对
    }
    public static void main(String []args) throws Exception {  
    	
        AprioriTest at = new AprioriTest();  
        at.setUp();  
          
        long from = System.currentTimeMillis();  
        at.testGetFreqItemSet();  
        long to = System.currentTimeMillis();  
        System.out.println("耗时：" + (to-from));  
      
    }  
      
    @Override  
    protected void setUp() throws Exception {  
//      create(); // 构造事务数据库  
        this.buildData(Integer.MAX_VALUE);  
        apriori = new Apriori(txDatabase, minSup, minConf);  
    }  
      
    /** 
     * 构造数据集 
     * @param fileName 存储事务数据的文件名 
     * @param totalcount 获取的事务数 
     */  
    public void buildData(int totalCount) {  
        txDatabase = new HashMap<String, Set<String>>();   
        try{
        	Connection conn = DataSource.getConn();		
			Statement stmt = conn.createStatement();
			String sql ="SELECT t1.USERID,t1.StoreID,UNIX_TIMESTAMP(t1.Time),t1.MCC,t1.Money  FROM data_train t1 ORDER BY t1.USERID,t1.Time";
			ResultSet rs = stmt.executeQuery(sql);//创建数据对象	   
			String userID,itemID;
            String mccType;
            long timeStamp;
            int count = 0;  
            long money;
            while (rs.next()){
            	userID=rs.getString(1);//传入String获取用户ID
            	itemID=getItemHashID(rs.getString(2));//传入String获取物品ID
            	timeStamp=rs.getLong(3);
	            mccType=rs.getString(4);
	            money = rs.getLong(5);
	            if (txDatabase.containsKey(userID)){
		        	Set<String> UserBuyList = txDatabase.get(userID);
		            UserBuyList.add(itemID);
		            txDatabase.put(userID, UserBuyList);
		        }else{
		            Set<String> UserBuyList = new HashSet<String>();
		            UserBuyList.add(itemID);
		            txDatabase.put(userID, UserBuyList);
		        }
            }
            
        } catch (Exception e) {  
                e.printStackTrace();  
        } 

    }  
      
    /** 
    * 测试挖掘频繁1-项集 
    */  
    public void testFreq1ItemSet() {  
       System.out.println("挖掘频繁1-项集 : " + apriori.getFreq1ItemSet());  
    }  
      
    /** 
    * 测试aprioriGen方法，生成候选频繁项集 
    */  
    public void testAprioriGen() {  
       System.out.println(  
         "候选频繁2-项集 ： " +  
         this.apriori.aprioriGen(1, this.apriori.getFreq1ItemSet().keySet())  
         );  
    }  
      
    /** 
    * 测试挖掘频繁2-项集 
    */  
    public void testGetFreq2ItemSet() {  
       System.out.println(  
         "挖掘频繁2-项集 ：" +  
         this.apriori.getFreqKItemSet(2, this.apriori.getFreq1ItemSet().keySet())  
         );  
    }  
      
    /** 
    * 测试挖掘频繁3-项集 
    */  
    public void testGetFreq3ItemSet() {  
       System.out.println(  
         "挖掘频繁3-项集 ：" +  
         this.apriori.getFreqKItemSet(  
           3,   
           this.apriori.getFreqKItemSet(2, this.apriori.getFreq1ItemSet().keySet()).keySet()  
           )  
         );  
    }  
      
    /** 
    * 测试挖掘全部频繁项集 
    */  
    public void testGetFreqItemSet() {  
       this.apriori.mineFreqItemSet(); // 挖掘频繁项集  
       Map<Integer, Set<Set<String>>> freqSet = this.apriori.getFreqItemSet();
       //System.out.println("挖掘频繁项集 ：" );  
       Iterator<Integer>  iter = freqSet.keySet().iterator();
       try {
			FileOutputStream fos = new FileOutputStream("data_out.txt");
			OutputStreamWriter osw=new OutputStreamWriter(fos);
			BufferedWriter bw=new BufferedWriter(osw);
			while(iter.hasNext()){
		    	   Integer key = iter.next();
		    	   if (key>0){
		    		   System.out.println(key+"=:");
		    		   Set<Set<String>> collections = freqSet.get(key);
		    		   Iterator<Set<String>> values = collections.iterator();
		    		   while(values.hasNext()){
		    			   Set<String>set =values.next();
		    			   Iterator<String> value = set.iterator();
		    			   int count =0;
		    			   while (value.hasNext()){
			    			   count++;
			    			   Long item = Long.parseLong(value.next());
			    			   if (count<set.size()){
			    				   bw.write(mapItem2CONTEN_ID(item)+",");
			    			   }else{
			    				   bw.write(mapItem2CONTEN_ID(item)+"\n");
			    			   }
		    				   
		    				   
		    			   }
		    		   }
		    		   
		    	   }
			}
			bw.close();
			osw.close();
			fos.close();
       }catch (Exception e) {
			// TODO Auto-generated catch block·
			e.printStackTrace();
		}
       
       
    }  
      
    /** 
    * 测试挖掘全部频繁关联规则 
    */  
    public void testMineAssociationRules() {  
       this.apriori.mineFreqItemSet(); // 挖掘频繁项集  
       this.apriori.mineAssociationRules();  
       System.out.println("挖掘频繁关联规则 ：" + this.apriori.getAssiciationRules());  
    }  
    
    
    public String getItemHashID(String Item_ID){
		if (Item_ID2ItemMap.containsKey(Item_ID)){
			return Item_ID2ItemMap.get(Item_ID).toString();
		}else{
			Long id = (long) Item_ID2ItemMap.size();
			Item_ID2ItemMap.put(Item_ID, id);
			Item2Item_IDMap.put(id, Item_ID);
			return id.toString();
		}
	}
    
    public String mapItem2CONTEN_ID(long item) {
		return Item2Item_IDMap.get(item);
	}
    public String mapItem2CONTEN_ID(String item) {
    	long i = Long.parseLong(item);
		return Item2Item_IDMap.get(i);
	}
	/*public long mapCONTENT_ID2item(String item) {
		return getItemHashID(item);
	}*/
	
}  