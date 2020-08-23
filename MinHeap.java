
public class MinHeap {
Node[] minheap;
int size;
public MinHeap() {
	minheap = new Node[2001];
	minheap[0] = new Node(-1, Integer.MIN_VALUE);
	this.size = 0;
}
int parent(int pos) {
	return pos/2;
}
int leftChild(int pos) {
	return (2 * pos); 
}
int rightChild(int pos) 
{ 
    return (2 * pos) + 1; 
} 
boolean isLeaf(int pos) 
{ 
    if (pos >= (size / 2) && pos <= size) { 
        return true; 
    } 
    return false; 
} 
void swap(int fpos, int spos) 
{ 
    Node tmp; 
    tmp = minheap[fpos]; 
    minheap[fpos] = minheap[spos]; 
    minheap[spos] = tmp; 
} 
/*void heapify(int pos) 
{ 
    if (!isLeaf(pos)) { 
        if ((minheap[leftChild(pos)]!=null)&&(minheap[pos].executed_time > minheap[leftChild(pos)].executed_time) ||((minheap[rightChild(pos)]!=null)&&(minheap[pos].executed_time > minheap[rightChild(pos)].executed_time))) { 
            if (minheap[leftChild(pos)].executed_time < minheap[rightChild(pos)].executed_time) { 
                swap(pos, leftChild(pos)); 
                heapify(leftChild(pos)); 
            } 
            else { 
                swap(pos, rightChild(pos)); 
                heapify(rightChild(pos)); 
            } 
        } 
    } 
}*/

void heapify(int pos) {
	int left = leftChild(pos);
	int right = rightChild(pos);
	int smallest = -1;
	//System.out.println("left = " + minheap[left].executed_time);
	if(left<=size&&minheap[left].executed_time<minheap[pos].executed_time) {
		smallest = left;
	} else smallest = pos;
	if(right<=size&&minheap[right].executed_time<minheap[smallest].executed_time) {
		smallest = right;
	}
	if(smallest!=pos) {
		swap(pos, smallest);
		heapify(smallest);
	}
}


void insert(Node node) {
	minheap[++size] = node; 
	int current = size;
	while (minheap[current].executed_time < minheap[parent(current)].executed_time) { 
        swap(current, parent(current)); 
        current = parent(current); 
    } 
}
Node remove() 
{ 
    Node popped = minheap[1]; 
    minheap[1] = minheap[size--]; 
    heapify(1); 
    return popped; 
} 
}
