import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;

public class risingCity {
	
	static int GLOBAL_COUNTER = 0;
	MinHeap minheap = new MinHeap();
	RedBlackTree redblacktree = new RedBlackTree();
	Queue<String> queue = new LinkedList<String>();
	Node currentBuilding;
	static int constructionDays = 5;
	File fout = new File("output_file.txt");
	FileOutputStream fos;  
	BufferedWriter bw;
	
	public static void main(String[] args) {
		if(args.length!=1 ) {
			System.err.println("Invalid Argument: Enter the Input file");
			System.exit(1);
		}
		ArrayList<String> inputList = new ArrayList<String>();
		//read input text file
		try {
			Scanner scanner = new Scanner(new FileInputStream(args[0]));
			while(scanner.hasNext()) {
				inputList.add(scanner.next());
			}
			scanner.close();
		}	catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		//start construction
		(new risingCity()).startConstruction(inputList);
	}
	
    void startConstruction(ArrayList<String> inputList)  {
    	try {
        	fos = new FileOutputStream(fout);
        	bw = new BufferedWriter(new OutputStreamWriter(fos));
    	} catch(FileNotFoundException e) {
    		e.printStackTrace();
    	}
    	//read and process the input list
    	for(int i=0; i<inputList.size(); i=i+2) {
    		String inTimeStr = inputList.get(i); //in time of the input
    		String dataStr = inputList.get(i+1); //input string
			int inTime = Integer.parseInt(inTimeStr.substring(0, inTimeStr.indexOf(":")));
			//if input string is an 'Insert and in time matches global time, then insert into minheap and RBT'
			if(dataStr.contains("Insert")) {
    			if(inTime==GLOBAL_COUNTER) {
    				//parse building no. from the input strin
    				int buildNum = Integer.parseInt(dataStr.substring(dataStr.indexOf("(")+1, dataStr.indexOf(",")));
    				//parse total time from the input string
    				int total_time = Integer.parseInt(dataStr.substring(dataStr.indexOf(",")+1, dataStr.indexOf(")")));
    				Node node = new Node(buildNum, total_time);
    				//insert into minheap
    				minheap.insert(node);
    				//insert into RBT
    				RedBlackTree.RedBlackNode redblacknode = redblacktree.new RedBlackNode(node);
    				redblacktree.insert(redblacknode);
    			}
    		}
			//if global time < in time then continue the building construction 
			while(inTime>GLOBAL_COUNTER) {
				if(minheap.size!=0) {
					//currentBuilding is the building with the minimum executed time
	    			currentBuilding = getMin();
	    			constructionDays=5;
	    			//construct for 5 days or till completion of construction
	    			while(constructionDays>0&&currentBuilding.executed_time!=currentBuilding.total_time) {
	    				//increment the executed time of the current building in minheap and RBT
	        			currentBuilding.executed_time = currentBuilding.executed_time+1;
	        			redblacktree.findNode(currentBuilding.buildingNum, redblacktree.root).key.executed_time = currentBuilding.executed_time;
	            		GLOBAL_COUNTER++;
	            		constructionDays--;
	            		//check if there is an input in between construction
	            		if(GLOBAL_COUNTER==inTime) {
	            			if(dataStr.contains("Print")) 
	            				print(dataStr);
	            			//if there is an insert, insert the node into RBT, and store it in a queue to insert into heap 
	            			//after construction is complete(for 5 days)
	            			if(dataStr.contains("Insert")) {
	            					insertRBT(dataStr);
	            					queue.add(dataStr);
	            			}
	            			//update the index of the input list if the next input already read in between construction
	            			if(i<inputList.size()-2) {
	            				i=i+2;
	            				inTime = Integer.parseInt((inputList.get(i)).substring(0, (inputList.get(i)).indexOf(":")));
	            				dataStr = inputList.get(i+1);
	            			}
	            		}
	            		
	        		}
	    			//insert the elements of the queue(the elements that were to be inserted in between construction)
	    			if(!queue.isEmpty()) {
	    				String removed = queue.remove();
	    				insertHeap(removed);
	    			}
	    			//if the construction of a building is complete, remove it from the minheap and RBT
	    			if(currentBuilding.executed_time==currentBuilding.total_time) {
	    				//remove from minheap
	    	        	minheap.remove();
	    	        	//remove from RBT
	        			RedBlackTree.RedBlackNode delBuilding = redblacktree.findNode(currentBuilding.buildingNum, redblacktree.root);
	        			redblacktree.delete(delBuilding);
	        			//print to the output file
	        			try {
		        			bw.write("("+currentBuilding.buildingNum+","+GLOBAL_COUNTER+")");
		        			bw.newLine();
	        			} catch(IOException e) {
	        				e.printStackTrace();
	        			}
	        		} 
	    			//after 5 days of construction, if the building is not complete, then heapify
	    			else {
	        			minheap.heapify(1);
	        		} 	 
				}
				//if minheap is empty and global counter is less than in time, then increase 
				//global counter till its equal to in time (no work to do on an empty heap)
				else
					GLOBAL_COUNTER=inTime;
			}
    	}    	
    	//after reading all inputs, if the heap still has buildings to be completed
    	//then construct them in the same way as above
    	while(minheap.size!=0) {
    		currentBuilding = getMin();
    		constructionDays = 5;
    		while(constructionDays>0&&currentBuilding.executed_time!=currentBuilding.total_time) {
    			currentBuilding.executed_time = currentBuilding.executed_time+1;
    			redblacktree.findNode(currentBuilding.buildingNum, redblacktree.root).key.executed_time = currentBuilding.executed_time;
        		GLOBAL_COUNTER++;
        		constructionDays--;
    		}
    		if(currentBuilding.executed_time==currentBuilding.total_time) {
    			minheap.remove();
    			RedBlackTree.RedBlackNode delBuilding = redblacktree.findNode(currentBuilding.buildingNum, redblacktree.root);
    			redblacktree.delete(delBuilding);
    			try {
        			bw.write("("+currentBuilding.buildingNum+","+GLOBAL_COUNTER+")");
        			bw.newLine();
    			} catch(IOException e) {
    				e.printStackTrace();
    			}
    		} else  minheap.heapify(1);
    	}
    	//close the buffered writer
    	try {
        	bw.close();
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }
    
    //method for print logic
    void print(String dataStr) {
    	//if have to print range
    	if(dataStr.contains(",")) {
			int buildingNum1 = Integer.parseInt(dataStr.substring(dataStr.indexOf("(")+1, dataStr.indexOf(",")));
			int buildingNum2 = Integer.parseInt(dataStr.substring(dataStr.indexOf(",")+1, dataStr.indexOf(")")));
	    	Queue<Node> queue = new LinkedList<Node>();
	    	//method to print the range of buildings
	    	redblacktree.findInRange(buildingNum1, buildingNum2, redblacktree.root, queue);
	    	//print the range to the output file
	    	Iterator itr=queue.iterator();  
	    	while(itr.hasNext()){  
	    	  Node node = (Node)itr.next(); 
	    	  try {
		    	  bw.write("("+node.buildingNum + ", " + node.executed_time + ", " + node.total_time + ") ");
	    	  } catch(IOException e) {
	    		  e.printStackTrace();
	    	  }
	    	}  
	    	try {
		    	bw.newLine();
	    	} catch(IOException e) {
	    		e.printStackTrace();
	    	}
		}
    	//if have to print single building
		else {
			int buildingNum = Integer.parseInt(dataStr.substring(dataStr.indexOf("(")+1, dataStr.indexOf(")")));
			RedBlackTree.RedBlackNode searchnode = redblacktree.findNode(buildingNum, redblacktree.root);
			//if node not found then print (0,0,0)
			if(searchnode==null) {
				try {
					//write to the output file
					bw.write("(0,0,0)");
					bw.newLine();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			//if node is found in the RBT
			else {
				try {
					bw.write("("+searchnode.key.buildingNum+","+searchnode.key.executed_time+","+searchnode.key.total_time+")");
					bw.newLine();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}				
		}
    }
    
    //method to insert into heap
    void insertHeap(String dataStr) {
    	int buildNum = Integer.parseInt(dataStr.substring(dataStr.indexOf("(")+1, dataStr.indexOf(",")));
		int total_time = Integer.parseInt(dataStr.substring(dataStr.indexOf(",")+1, dataStr.indexOf(")")));
		Node node = new Node(buildNum, total_time);
		minheap.insert(node);	
    }
    
    //method to insert into RBT
    void insertRBT(String dataStr) {
    	int buildNum = Integer.parseInt(dataStr.substring(dataStr.indexOf("(")+1, dataStr.indexOf(",")));
		int total_time = Integer.parseInt(dataStr.substring(dataStr.indexOf(",")+1, dataStr.indexOf(")")));
		Node node = new Node(buildNum, total_time);
		RedBlackTree.RedBlackNode redblacknode = redblacktree.new RedBlackNode(node);
		redblacktree.insert(redblacknode);
    }
    
    //method to get the node with the minimum execution time from the minheap
    Node getMin() {
    	Node min = minheap.minheap[1];
    	//if the execution time of root is the same as that of its children
    	//then choose the node with the minimum building number
    	Node leftchild = minheap.minheap[minheap.leftChild(1)];
		Node rightchild = minheap.minheap[minheap.rightChild(1)];
		if(leftchild!=null&&min.executed_time==leftchild.executed_time) {
			int bn_left = leftchild.buildingNum;
			int bn_min = min.buildingNum;
			if(bn_left<bn_min) {
				minheap.swap(1, minheap.leftChild(1));
				min = minheap.minheap[1];
			}
		}
		if(rightchild!=null&&min.executed_time==rightchild.executed_time) {
			int bn_right = rightchild.buildingNum;
			int bn_min = min.buildingNum;
			if(bn_right<bn_min) {
				minheap.swap(1, minheap.rightChild(1));
			}
		}
	    return minheap.minheap[1];
    }
}
