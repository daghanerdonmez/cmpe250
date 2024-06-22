import java.util.ArrayList;
import java.util.Iterator;



public class myArrayList {
	
	
	
	public static void main (String[] args) {
		
		int [] myArray = new int[20];
		myArray[0] = 20;
		myArray[1] = 30;
		myArray[2] = 10;
				
		System.out.println("myArray[0]: " + myArray[0]);
		System.out.println("myArray[1]: " + myArray[1]);
		System.out.println("myArray[2]: " + myArray[2]);
		
		ArrayList<Integer> myArrayList = new ArrayList<>();
		System.out.println("myArrayList has the size of " + myArrayList.size());
		myArrayList.add(20);
		myArrayList.add(10);
		myArrayList.add(30);
		
		System.out.println("myArrayList has the size of " + myArrayList.size());
		System.out.println(myArrayList.toString());
		
		Iterator<Integer> it = myArrayList.iterator();
		
		
        while(it.hasNext()){
            System.out.println(it.next());
        }
		
		
	}

}
