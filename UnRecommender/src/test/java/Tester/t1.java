package Tester;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

import Tool.DataSource;
import Algorithm.MarkovCF;
import DataModel.DataModel;

public class t1 {

	/**
	 * @param args
	 */
	private static int N = 5;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HashSet<Long> covSet = new HashSet<>();
		DataModel dataModel = new DataModel();
		MarkovCF cf = new MarkovCF(dataModel);
		int hit=0,total=0,userCount=16962;
		try{
			Connection conn = DataSource.getConn();
			Statement st2=conn.createStatement();
			String sql2="SELECT DISTINCT t1.USERID FROM data_test t1";
			ResultSet rs2= st2.executeQuery(sql2);
			while(rs2.next()){
				String UserID=rs2.getString(1);
				String sql = "SELECT t1.StoreID,t1.Money FROM data_test t1 WHERE t1.USERID='"+UserID+"' ORDER BY t1.time";
				ArrayList<Long> recommenderList= (ArrayList<Long>) cf.recommender(UserID, N);
				if (recommenderList.size() ==0) continue;
				
				
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
	            long v1;
	            
	            while (rs.next()){
	                v1 = dataModel.mapCONTENT_ID2item(rs.getString(1));
	                for (int i=0;i<recommenderList.size();i++){
	                	if (recommenderList.get(i)==v1){
		                	hit++;
		                }
	                }
	                total++;
	            }
	            for (int i=0;i<recommenderList.size();i++){
	            	if (!covSet.contains(recommenderList.get(i))){
						covSet.add(recommenderList.get(i));
					}
                }
	            
	            rs.close();
	    		stmt.close();
			}
			rs2.close();
			st2.close();
			conn.close();
		}catch(Exception e){
			e.printStackTrace();
        }
		double Recall=1.0*hit/total,Precision=1.0*hit/userCount/N;
		double F1=Recall*Precision*2/(Recall+Precision);
		System.out.println("recall:"+Recall+"\n"+"precision:"+Precision);
		System.out.println("F1: "+F1);
		System.out.println("total Hit:"+hit);
		System.out.println(covSet.size());
	}

}
