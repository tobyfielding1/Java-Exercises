import java.util.Arrays;

public class MinTree {

    

    public static void main(String[] args){
	MinTree mt = new MinTree();
	Tree tree = new Tree( 24, 
                 new Tree( 45, 
                     null , 
                     new Tree(8, null , null) ) , 
                 new Tree ( 17, 
                     new Tree (74 , null , null ) , 
                     null ) );
	System.out.println("Minimum is :" + mt.findMin(tree));
    }

    public int findMin(Tree tree){
    	int newSmall;
    	int val = tree.getVal();
    	
    	if (tree.left() == null && tree.right() == null)
 		   return val;
    	else if (tree.left() == null)
    		newSmall = findMin(tree.right());
    	else if (tree.right() == null)
    		newSmall = findMin(tree.left());
    	else{
    		newSmall = findMin(tree.right());
    		int leftSmall = findMin(tree.left());
    		if (leftSmall < newSmall)
    			newSmall = leftSmall;
    	}
    		
 	   if (newSmall >  val)
 		   return  val;
 	   else
 		   return newSmall;
    }

}

class Tree {

   private int val;
   private Tree left, right;

   public Tree(int val, Tree left, Tree right){
     this.val = val;
     this.left = left;
     this.right = right;
   }

   public int getVal(){
      return val;
   }

   public Tree left(){
      return left;
   }

   public Tree right(){
      return right;
   }
}
