import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class HashTable<AnyType> {

    private static final int DEFAULT_SIZE = 101; // default table size to start the hash table with

    private HashEntry<AnyType>[] array; // the hash table
    private int occupied; // number of occupied spots on the hash table (includes deleted)
    private int size; // number of active spots on the hash table

    private static class HashEntry<AnyType>{ // the class of entries of the hash table
        public AnyType value; // where the real value is being held
        public boolean isActive; // indicates if the spot is active or deleted before

        HashEntry(AnyType value){ this(value, true); }

        HashEntry(AnyType value, boolean isActive){
            this.value = value;
            this.isActive = isActive;
        }
    }

    HashTable(){
        this(DEFAULT_SIZE);
    }

    HashTable(int tableSize){
        allocateArray(tableSize);
        clear();
    }

    private void allocateArray(int arraySize){ // allocate a prime sized array for the hash table
        array = new HashEntry[nextPrime(arraySize)];
    }


    private void clear(){ // reset the hash table
        occupied = 0;
        Arrays.fill(array, null);
    }

    //inserts a new value to the hash table
    //uses the PrintWriter to create the output file
    public boolean insert(AnyType value, PrintWriter printWriter) {
        if (value instanceof Employee) {
            Employee employee = (Employee) value;
            if (containsByName(employee.name)) {
                printWriter.write("Existing employee cannot be added again.\n");
                return false;
            }
        }

        int currentPos = findPos(value);
        if(isActive(currentPos)) {
            return false;
        }

        if(array[currentPos] == null) {
            occupied++;
        }

        array[currentPos] = new HashEntry<>(value);
        size++;

        if(occupied > array.length / 2) { // rehashing when load factor > 0.5, because of quadratic probing
            rehash(printWriter);
        }

        return true;
    }

    public boolean remove(AnyType value) { // removes an entry if it is in the hash table
        int currentPos = findPos(value);
        if(isActive(currentPos)) {
            array[currentPos].isActive = false;
            size--;
            return true;
        }else {
            return false;
        }
    }

    public void endOfMonthForShops(){ // reset all monthly bonus values
        for(int i = 0; i < array.length; i++){
            if (array[i] != null && array[i].isActive){
                LahmacunShop shop = (LahmacunShop) array[i].value;
                shop.thisMonthBonus = 0;
            }
        }
    }

    public LahmacunShop getBranch(String name) { // finds and returns a branch with a given name
        // calculate and find the position of the branch and travel further if needed.
        int hashVal = name.hashCode();
        hashVal = Math.abs(hashVal % array.length);

        int offset = 1;
        int currentPos = hashVal;

        while (array[currentPos] != null) {
            if (array[currentPos].isActive && array[currentPos].value instanceof LahmacunShop) {
                LahmacunShop shop = (LahmacunShop) array[currentPos].value;
                if (shop.name.equals(name)) {
                    return shop;
                }
            }

            currentPos += offset;
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }

        return null;
    }

    public Employee getEmployee(String name) { // finds and returns an employee with a given name
        // calculate and find the position of the employee and travel further if needed.
        int hashVal = name.hashCode();
        hashVal = Math.abs(hashVal % array.length);

        int offset = 1;
        int currentPos = hashVal;

        while (array[currentPos] != null) {
            if (array[currentPos].isActive && array[currentPos].value instanceof Employee) {
                Employee employee = (Employee) array[currentPos].value;
                if (employee.name.equals(name)) {
                    return employee;
                }
            }

            currentPos += offset;
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }

        return null;
    }

    /*public boolean contains(AnyType value) { //check if a value is in the hash table
        int currentPos = findPos(value);
        return isActive(currentPos);
    }*/

    public boolean containsByName(String name) { // checks if a branch or employee is in their respective hash tables
        //works for both branches and employees
        //searches for branches among all branches
        //searches for employees in the called branch
        int hashVal = name.hashCode();
        hashVal = Math.abs(hashVal % array.length);

        int offset = 1;
        int currentPos = hashVal;

        while (array[currentPos] != null) {
            if (array[currentPos].isActive) {
                if (array[currentPos].value instanceof LahmacunShop) {
                    LahmacunShop shop = (LahmacunShop) array[currentPos].value;
                    if (shop.name.equals(name)) {
                        return true;
                    }
                } else if (array[currentPos].value instanceof Employee) {
                    Employee employee = (Employee) array[currentPos].value;
                    if (employee.name.equals(name)) {
                        return true;
                    }
                }
            }

            currentPos += offset;
            offset += 2;
            if (currentPos >= array.length) {
                currentPos -= array.length;
            }
        }

        return false;
    }


    private void rehash(PrintWriter printWriter) { // rehashing process with a larger array
        //enlarges the array and inserts all the old entries
        HashEntry<AnyType> [] oldArray = array;

        allocateArray(2 * oldArray.length);
        occupied = 0;
        size = 0;
        for(HashEntry<AnyType> entry : oldArray) {
            if(entry != null && entry.isActive) {
                insert(entry.value, printWriter);
            }
        }
    }

    private int findPos(AnyType value) {  // finds the position of an element in the hash tables array
        // input : value
        // output : index in the hash table array
        int offset = 1;
        int currentPos = hashFunction(value);

        while(array[currentPos] != null && !array[currentPos].value.equals(value)) {
            currentPos += offset;
            offset += 2;
            if(currentPos >= array.length) {
                currentPos -= array.length;
            }
        }

        return currentPos;
    }

    //checks if the given position is active (meaning if the value there [if there is] not deleted before)
    private boolean isActive(int currentPos) {
        return array[currentPos] != null && array[currentPos].isActive;
    }

    //hash function of the hash table
    private int hashFunction(AnyType value) {
        int hashVal = value.hashCode(); //uses the hashcode method of lahmacun shop and employee classes
        hashVal = Math.abs(hashVal % array.length);
        if(hashVal < 0) {
            hashVal += array.length;
        }
        return hashVal;
    }

    private static int nextPrime(int currentPrime) {
        if(currentPrime % 2 == 0) {
            currentPrime++;
        }
        while(!isPrime(currentPrime)) {
            currentPrime += 2;
        }
        return currentPrime;
    }

    private static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        //HashTable<LahmacunShop> business = new HashTable<>();
    }

}
