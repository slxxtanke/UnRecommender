package Algorithm;

public class SimilarityItem {
	private long id;
	private float data;
	public SimilarityItem(long id,float data){
		this.id = id;
		this.data = data;
	}
	public SimilarityItem setID(long value){
		this.id = value;
		return this;
	}
	public Float getData(){
		return data;
	}
	public Long getID(){
		return id;
	}
	@Override  
    public boolean equals(Object obj) {  
		//System.out.println(1);
        if (obj instanceof SimilarityItem) {  
            if (this.id==((SimilarityItem) obj).id) {  
                return true;  
            }  
            else {  
                return false;  
            }  
        }
        return false;  
   }
}
