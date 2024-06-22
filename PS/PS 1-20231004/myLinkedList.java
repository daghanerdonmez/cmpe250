import java.util.Iterator;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

public class myLinkedList<AnyType> implements Iterable<AnyType>{
	
	private int size;
	private int modCount = 0;
	
	private Node<AnyType> beginMarker;
	private Node<AnyType> endMarker;
	
	
	private static class Node<AnyType>{
		public AnyType data;
		public Node<AnyType> next;
		public Node<AnyType> prev;
		
		public Node(AnyType data, Node<AnyType> prev, Node<AnyType> next) {
			this.data = data;
			this.prev = prev;
			this.next = next;
			
		}
	}
	
	public myLinkedList(){
		this.initilaze();
	}
	
	public void clear() {
		this.initilaze();
	}
	
	private void initilaze() {
		this.beginMarker = new Node<AnyType>(null, null, null);
		this.endMarker = new Node<AnyType>(null, beginMarker, null);
		beginMarker.next = endMarker; //Garbage Collection
		this.size = 0;
		this.modCount++;
	}
	
	public void add(AnyType x) {
		this.add(this.size(), x);
	}
	
	public boolean isEmpty() {
		return this.size() == 0;
	}
	
	public void add(int idx, AnyType x) {
		this.addBefore(getNode(idx, 0, this.size()), x);
	}
	
	private void addBefore(Node<AnyType> node, AnyType x) {
		Node<AnyType> newNode = new Node<>(x, node.prev, node);
		node.prev.next = newNode;
		node.prev = newNode;
		this.size++;
		this.modCount++;
	}
	
	public int size() {
		return this.size;
	}
	
	
	public AnyType get(int idx) {
		return this.getNode(idx).data;
	}
	
	//Added this method
	private Node<AnyType> getNode(int idx){
		return this.getNode(idx, 0, this.size()-1);
	}
	
	private Node<AnyType> getNode(int idx, int lower, int upper){
		Node <AnyType> node;
		if (idx < lower || idx > upper) {
			throw new IndexOutOfBoundsException();
		}
		
		if (idx < this.size()/2) {
			node = beginMarker.next;
			for(int i = 0; i < idx; i++) {
				node = node.next;
			}
		}else {
			node = endMarker;
			for (int i = this.size(); i > idx; i--) {
				node = node.prev;
			}
		}
		return node;
	}
	
	public AnyType set(int idx, AnyType newData) {
		Node <AnyType> node = this.getNode(idx);
		AnyType oldData = node.data;
		node.data = newData;
		return oldData;
	}
	
	public AnyType remove(int idx) {
		return this.remove(this.getNode(idx));
	}
	
	private AnyType remove(Node<AnyType> node) {
		Node <AnyType> prevNode = node.prev;
		Node <AnyType> nextNode = node.next;
		prevNode.next = nextNode;
		nextNode.prev = prevNode;
		this.size--;
		this.modCount++;
		return node.data;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder("[ ");
		for (AnyType x: this) {
			sb.append(x + " ");
		}
		sb.append("]");
		return new String(sb);
	}
	
	@Override
	public Iterator<AnyType> iterator(){
		return new LinkedListIterator();
	}
	private class LinkedListIterator implements Iterator<AnyType>{
		private Node<AnyType> current = myLinkedList.this.beginMarker.next;
		private int expectedModCount = myLinkedList.this.modCount;
		private boolean okToRemove = false;
		
		@Override
		public boolean hasNext() {
			return this.current != myLinkedList.this.endMarker;
		}
		@Override
		public AnyType next() {
			if (this.expectedModCount != myLinkedList.this.modCount) {
				throw new ConcurrentModificationException();
			}
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			AnyType nextItem = this.current.data;
			this.current = this.current.next;
			this.okToRemove = true;
			return nextItem;
		}
		@Override
		public void remove() {
			if (this.expectedModCount != myLinkedList.this.modCount) {
				throw new ConcurrentModificationException();
			}
			if (!this.okToRemove) {
				throw new IllegalStateException();
			}
			myLinkedList.this.remove(current.prev);
			this.expectedModCount++;
			this.okToRemove = false;
		}
	}
	
	public static void main(String[] args) {
		myLinkedList<Integer> myLinkedList1 = new myLinkedList<>();
		myLinkedList<Integer> myLinkedList2 = new myLinkedList<>();
		for(int i = 0; i < 20; i++) {
			myLinkedList1.add(i);
			myLinkedList2.add(20 - i * 2);
		}
		
		System.out.println("myLinkedList size: " + myLinkedList1.size());
		System.out.println("myLinkedList: " + myLinkedList1);
		
		myLinkedList1.remove(3);
		System.out.println("myLinkedList size: " + myLinkedList1.size());
		System.out.println("myLinkedList: " + myLinkedList1);
		
		myLinkedList1.add(3, 20);
		for (Integer data : myLinkedList1) {
			System.out.println(data);
		}
		
		
	}
}
