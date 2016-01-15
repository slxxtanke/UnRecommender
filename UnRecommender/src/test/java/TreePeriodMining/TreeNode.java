package TreePeriodMining;

import java.util.HashMap;

public class TreeNode {
	int count;
	boolean isLeaf;
	HashMap<Long, TreeNode> child;
	public TreeNode(){
		count = 0;
		isLeaf = true;
		child = new HashMap<Long,TreeNode>();
	}
}
