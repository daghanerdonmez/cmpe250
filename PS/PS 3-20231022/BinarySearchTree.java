package W3;
import java.util.NoSuchElementException;


public class BinarySearchTree<AnyType extends Comparable<? super AnyType>> {
	
	private BinaryNode<AnyType> root;
	
	private static class BinaryNode<AnyType>{
		
		AnyType data;
		BinaryNode<AnyType> leftChild;
		BinaryNode<AnyType> rightChild;
		
		BinaryNode(AnyType data){
			this(data, null, null);
		}
		BinaryNode(AnyType data, BinaryNode<AnyType> leftChild, BinaryNode<AnyType> rightChild){
			this.data = data;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
		}
	}
	
	BinarySearchTree(){
		this.root = null;
	}
	
	public boolean isEmpty() {
		return this.root == null;
	}
	
	public void clear() {
		this.root = null;
	}
	
	public AnyType findMin() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		
		return this.findMin(this.root).data;
	}
	
	
	private BinaryNode<AnyType> findMin(BinaryNode<AnyType> node){
		while(node.leftChild != null) {
			node = node.leftChild;
		}
		return node;
	}
	
	public AnyType findMax() {
		if (this.isEmpty()) {
			throw new NoSuchElementException();
		}
		return this.findMax(this.root).data;
	}
	
	private BinaryNode<AnyType> findMax(BinaryNode<AnyType> node){
		while(node.rightChild != null) {
			node = node.rightChild;
		}
		return node;
	}
	
	public int height() {
		return this.height(this.root);
	}
	
	private int height(BinaryNode<AnyType> node) {
		if(node == null) {
			return -1;
		}
		return Math.max(this.height(node.leftChild), this.height(node.rightChild)) + 1;
	}
	
	public boolean contains(AnyType data) {
		return this.contains(data, this.root);
	}
	
	private boolean contains(AnyType data, BinaryNode<AnyType> node) {
		if (node == null) {
			return false;
		}
		int comparison = data.compareTo(node.data);
		if (comparison < 0) {
			return this.contains(data, node.leftChild);
		}else if(comparison > 0) {
			return this.contains(data, node.rightChild);
		}else {
			return true;
		}
	}
	
	
	public void insert(AnyType data) {
		this.root = this.insert(data, root);
	}
	
	
	private BinaryNode<AnyType> insert(AnyType data, BinaryNode<AnyType> node){
		if (node == null) {
			return new BinaryNode<AnyType>(data);
		}
		int comparison = data.compareTo(node.data);
		if(comparison < 0) {
			node.leftChild = this.insert(data, node.leftChild);
		}else if(comparison > 0) {
			node.rightChild = this.insert(data, node.rightChild);
		}
		
		return node;
	}
	
	public void remove(AnyType data) {
		this.remove(data, this.root);
	}
	private BinaryNode<AnyType> remove(AnyType data, BinaryNode<AnyType> node){
		if (node == null) {
			return node;
		}
		int comparison = data.compareTo(node.data);
		if(comparison < 0) {
			node.leftChild = this.remove(data, node.leftChild);
		}else if(comparison > 0) {
			node.rightChild = this.remove(data, node.rightChild);
		}else if(node.leftChild != null && node.rightChild != null) { //2 Children
			node.data = this.findMin(node.rightChild).data;
			node.rightChild = this.remove(node.data, node.rightChild);
		}else if(node.leftChild != null) { //1 Child
			node = node.leftChild;
		}else { //1 or 0 Child
			node = node.rightChild;
		}
		return node;
	}
	
	
	public void printInOrder() {
		this.printInOrder(this.root);
	}
	private void printInOrder(BinaryNode<AnyType> node) {
		if (node != null) {
			this.printInOrder(node.leftChild);
			System.out.println(node.data);
			this.printInOrder(node.rightChild);
		}
	}
	public void printPreOrder() {
		this.printPreOrder(this.root);
	}
	
	private void printPreOrder(BinaryNode<AnyType>node) {
		if(node != null) {
			System.out.println(node.data);
			this.printPreOrder(node.leftChild);
			this.printPreOrder(node.rightChild);
		}
	}
	public void printPostOrder() {
		this.printPostOrder(this.root);
	}
	
	private void printPostOrder(BinaryNode<AnyType>node) {
		if(node != null) {
			this.printPostOrder(node.leftChild);
			this.printPostOrder(node.rightChild);
			System.out.println(node.data);
		}
	}
	
	
	public static void main(String[] args) {
		BinarySearchTree<Integer> myBST = new BinarySearchTree<>();
		myBST.insert(25);
		myBST.insert(15);
		myBST.insert(50);
		myBST.insert(10);
		myBST.insert(22);
		myBST.insert(35);
		myBST.insert(4);
		myBST.insert(12);
		myBST.insert(18);
		myBST.insert(24);
		myBST.insert(31);
		myBST.insert(44);
		myBST.insert(66);
		myBST.insert(90);
		//System.out.println(myBST.findMax());
		myBST.printPostOrder();
	}

}
