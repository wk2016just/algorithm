package dfasfdsfd;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TreeTime {

 

    public static void main(String[] args) {
    	
    	AVLTree<Integer> avltree = new AVLTree<Integer>();
        RBTree<Integer> rbtree=new RBTree<Integer>();

        Set<Integer> set=new HashSet<Integer>(); 
        while(true){ set.add((int)(Math.random()*1000000+1)); 
        if(set.size()==1000000) break; } 
        
        int[] a = new int[set.size()];
        int[] b = new int[set.size()];
        int i=0;
        for (Integer str : set) {  
              a[i++]= str;
        }  
        int j=0;
        for (Integer str : set) {  
              b[j++]= str;
        }  
	  
        
      	long time_a1 =  System.currentTimeMillis();

        for(int i1=0; i1<a.length; i1++) {
        	rbtree.insert(a[i1]);
        }
    	long time_a2 = System.currentTimeMillis();
    	long time_a = time_a2-time_a1;
        System.out.println("红黑树时间是"+ time_a);
        
     	long time_a11 =  System.currentTimeMillis();

        for(int i1=0; i1<b.length; i1++) {
              avltree.insert(b[i1]);
          }
    	long time_a21 = System.currentTimeMillis();
    	long time_b = time_a21-time_a11;
        System.out.println("AVL树时间是"+ time_b);

        // 销毁二叉树
        rbtree.clear();
   
    }
}
