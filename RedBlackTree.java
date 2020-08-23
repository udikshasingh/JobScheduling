import java.util.Queue;
public class RedBlackTree {

	public final int RED = 0;
	public final int BLACK = 1;
    public final RedBlackNode nil = new RedBlackNode(null); 
    public RedBlackNode root = nil;

    class RedBlackNode {
	 	private final int RED = 0;
	    private final int BLACK = 1;
        Node key = null;
        int color = BLACK;
        RedBlackNode left = nil, right = nil, parent = nil;
        RedBlackNode(Node key) {
            this.key = key;
        } 
    }
    
    public RedBlackNode findNode(int findval, RedBlackNode node) {
        if (root == nil) {
            return null;
        }
        //System.out.println("findval = "+findval);
        if (findval < node.key.buildingNum) {
            if (node.left != nil) {
                return findNode(findval, node.left);
            }
        } else if (findval > node.key.buildingNum) {
            if (node.right != nil) {
                return findNode(findval, node.right);
            }
        } else if (findval == node.key.buildingNum) {
            return node;
        }
        return null;
    }
    
    public void findInRange(int low, int high, RedBlackNode node, Queue<Node> queue) {
    	if(node==nil) return;
    	if(low<node.key.buildingNum)
    		findInRange(low, high, node.left, queue);
    	if(low<=node.key.buildingNum&&high>=node.key.buildingNum)  {
    		queue.add(node.key);
    		//System.out.println("buildingnum enqueued:" + node.key.buildingNum);
    	}
    	if(high>node.key.buildingNum)
    		findInRange(low, high, node.right, queue);
    }
    public void inorder(RedBlackNode node) {
    	if(node!=nil) {
    		inorder(node.left);
    		System.out.print(node.key.buildingNum+" -  ");
    		inorder(node.right);
    	}
    }
    public void insert(RedBlackNode node) {
    	RedBlackNode temp = root;
        if (root == nil) {
            root = node;
            node.color = BLACK;
            node.parent = nil;
        } else {
            node.color = RED;
            while (true) {
                if (node.key.buildingNum < temp.key.buildingNum) {
                    if (temp.left == nil) {
                        temp.left = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.left;
                    }
                } else if (node.key.buildingNum >= temp.key.buildingNum) {
                    if (temp.right == nil) {
                        temp.right = node;
                        node.parent = temp;
                        break;
                    } else {
                        temp = temp.right;
                    }
                }
            }
            fixTree(node);
        }
    }
    
    //Takes as argument the newly inserted node
    public void fixTree(RedBlackNode node) {
        while (node.parent.color == RED) {
        	RedBlackNode uncle = nil;
            if (node.parent == node.parent.parent.left) {
                uncle = node.parent.parent.right;

                if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                } 
                if (node == node.parent.right) {
                    //Double rotation needed
                    node = node.parent;
                    rotateLeft(node);
                } 
                node.parent.color = BLACK;
                node.parent.parent.color = RED;
                //if the "else if" code hasn't executed, this
                //is a case where we only need a single rotation 
                rotateRight(node.parent.parent);
            } else {
                uncle = node.parent.parent.left;
                 if (uncle != nil && uncle.color == RED) {
                    node.parent.color = BLACK;
                    uncle.color = BLACK;
                    node.parent.parent.color = RED;
                    node = node.parent.parent;
                    continue;
                }
                 if (node == node.parent.left) {
                     //Double rotation needed
                     node = node.parent;
                     rotateRight(node);
                 }
                 node.parent.color = BLACK;
                 node.parent.parent.color = RED;
                 //if the "else if" code hasn't executed, this
                 //is a case where we only need a single rotation
                 rotateLeft(node.parent.parent);
             }
         }
         root.color = BLACK;
     }
    
    void rotateLeft(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.right;
            } else {
                node.parent.right = node.right;
            }
            node.right.parent = node.parent;
            node.parent = node.right;
            if (node.right.left != nil) {
                node.right.left.parent = node;
            }
            node.right = node.right.left;
            node.parent.left = node;
        } else {//Need to rotate root
        	RedBlackNode right = root.right;
            root.right = right.left;
            right.left.parent = root;
            root.parent = right;
            right.left = root;
            right.parent = nil;
            root = right;
        }
    }
    
    void rotateRight(RedBlackNode node) {
        if (node.parent != nil) {
            if (node == node.parent.left) {
                node.parent.left = node.left;
            } else {
                node.parent.right = node.left;
            }

            node.left.parent = node.parent;
            node.parent = node.left;
            if (node.left.right != nil) {
                node.left.right.parent = node;
            }
            node.left = node.left.right;
            node.parent.right = node;
        } else {//Need to rotate root
        	RedBlackNode left = root.left;
            root.left = root.left.right;
            left.right.parent = root;
            root.parent = left;
            left.right = root;
            left.parent = nil;
            root = left;
        }
    }

    //Deletes whole tree
    void deleteTree(){
        root = nil;
    }
    
 //Deletion Code .
    
    //This operation doesn't care about the new Node's connections
    //with previous node's left and right. The caller has to take care
    //of that.
    void transplant(RedBlackNode target, RedBlackNode with){ 
          if(target.parent == nil){
              root = with;
          }else if(target == target.parent.left){
              target.parent.left = with;
          }else
              target.parent.right = with;
          with.parent = target.parent;
    }
    
    void delete(RedBlackNode z) {
    	//if(z==null) System.out.println("z is null");
        if(z==null) return;
        RedBlackNode x;
        RedBlackNode y = z; // temporary reference y
        int y_original_color = y.color;
        
        if(z.left == nil){
            x = z.right;  
            transplant(z, z.right);  
        }else if(z.right == nil){
            x = z.left;
            transplant(z, z.left); 
        }else{
            y = treeMinimum(z.right);
            y_original_color = y.color;
            x = y.right;
            if(y.parent == z)
                x.parent = y;
            else{
                transplant(y, y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            transplant(z, y);
            y.left = z.left;
            y.left.parent = y;
            y.color = z.color; 
        }
        if(y_original_color==BLACK)
            deleteFixup(x);  
        return;
    }
    
    void deleteFixup(RedBlackNode x){
        while(x!=root && x.color == BLACK){ 
            if(x == x.parent.left){
            	RedBlackNode w = x.parent.right;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateLeft(x.parent);
                    w = x.parent.right;
                }
                if(w.left.color == BLACK && w.right.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.right.color == BLACK){
                    w.left.color = BLACK;
                    w.color = RED;
                    rotateRight(w);
                    w = x.parent.right;
                }
                if(w.right.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.right.color = BLACK;
                    rotateLeft(x.parent);
                    x = root;
                }
            }else{
            	RedBlackNode w = x.parent.left;
                if(w.color == RED){
                    w.color = BLACK;
                    x.parent.color = RED;
                    rotateRight(x.parent);
                    w = x.parent.left;
                }
                if(w.right.color == BLACK && w.left.color == BLACK){
                    w.color = RED;
                    x = x.parent;
                    continue;
                }
                else if(w.left.color == BLACK){
                    w.right.color = BLACK;
                    w.color = RED;
                    rotateLeft(w);
                    w = x.parent.left;
                }
                if(w.left.color == RED){
                    w.color = x.parent.color;
                    x.parent.color = BLACK;
                    w.left.color = BLACK;
                    rotateRight(x.parent);
                    x = root;
                }
            }
        }
        x.color = BLACK; 
    }
    
    RedBlackNode treeMinimum(RedBlackNode subTreeRoot){
        while(subTreeRoot.left!=nil){
            subTreeRoot = subTreeRoot.left;
        }
        return subTreeRoot;
    }
}