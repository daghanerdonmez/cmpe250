import java.util.LinkedList;

public class linkedList {
	public static void main(String[] args) {
		LinkedList<Integer> myLinkedList = new LinkedList<>();
		myLinkedList.add(3);
		myLinkedList.add(5);
		System.out.println(myLinkedList);
		myLinkedList.clear();
		System.out.println(myLinkedList);
	}
}
