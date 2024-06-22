public class MinHeapForPlaylists {
    private Song[] heap;
    private int size;
    private int maxsize;
    private int category;

    // Constructor
    public MinHeapForPlaylists(int maxsize, int category) {
        this.maxsize = maxsize;
        this.size = 0;
        this.category = category;
        heap = new Song[this.maxsize + 1];
        heap[0] = new Song(new String[]{
                "0",
                "",
                Integer.toString(-1),
                Integer.toString(-1),
                Integer.toString(-1),
                Integer.toString(-1)
        });
    }

    // Get the index of the parent
    private int parent(int pos) {
        return pos / 2;
    }

    // Get the index of the left child
    private int leftChild(int pos) {
        return (2 * pos);
    }

    // Get the index of the right child
    private int rightChild(int pos) {
        return (2 * pos) + 1;
    }

    // Swap two nodes of the heap
    private void swap(int fpos, int spos) {
        Song tmp = heap[fpos];
        heap[fpos] = heap[spos];
        heap[spos] = tmp;
    }

    // Heapify the node at pos
    private void minHeapify(int pos) {
        if (!isLeaf(pos)) {
            // Check if right child exists
            boolean hasRightChild = rightChild(pos) <= size;

            if (compare(heap[pos], heap[leftChild(pos)]) > 0 ||
                    (hasRightChild && compare(heap[pos], heap[rightChild(pos)]) > 0)) {

                if (!hasRightChild || compare(heap[leftChild(pos)], heap[rightChild(pos)]) < 0) {
                    swap(pos, leftChild(pos));
                    minHeapify(leftChild(pos));
                } else {
                    swap(pos, rightChild(pos));
                    minHeapify(rightChild(pos));
                }
            }
        }
    }


    // Insert a node into the heap
    public void insert(Song element) {
        if (size >= maxsize) {
            // Resize the heap if necessary
            resizeHeap();
        }
        heap[++size] = element;
        int current = size;

        while (compare(heap[current], heap[parent(current)]) < 0) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Remove and return the minimum element from the heap
    public Song extractMin() {
        if (size == 0) {
            return null;
        }else if(size == 1){
            Song popped = heap[1];
            heap[1] = null;
            size--;
            return popped;
        }
        Song popped = heap[1];
        heap[1] = heap[size--];
        minHeapify(1);
        return popped;
    }

    // Check if the node is a leaf
    private boolean isLeaf(int pos) {
        return pos > (size / 2) && pos <= size;
    }

    // Compare two songs based on the category
    private int compare(Song a, Song b) {
        switch (category) {
            case 0: return a.playCountCompare(b);
            case 1: return a.heartacheCompare(b);
            case 2: return a.roadtripCompare(b);
            case 3: return a.blissfulCompare(b);
            default: return 0;
        }
    }

    // Resize the heap when it's full
    private void resizeHeap() {
        maxsize *= 2;
        Song[] newHeap = new Song[maxsize + 1];
        System.arraycopy(heap, 0, newHeap, 0, heap.length);
        heap = newHeap;
    }

    // Return the current size of the heap
    public int getSize() {
        return this.size;
    }

    // Return the array representing the heap
    public Song[] getHeap() {
        return this.heap;
    }

    // Return the maximum element without removing it
    public Song peek() {
        if (size == 0) {
            return null;
        }
        return heap[1];
    }

    public boolean remove(Song element) {
        // Find the element
        int index = -1;
        for (int i = 1; i <= size; i++) {
            if (heap[i].equals(element)) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return false; // Element not found
        }

        // Replace the element with the last in the heap
        heap[index] = heap[size];
        heap[size] = null;
        size--;

        // Reheapify
        if (index <= size) {
            minHeapify(index);
            /*while (index > 1 && compare(heap[index], heap[parent(index)]) < 0) {
                swap(index, parent(index));
                index = parent(index);
            }*/
        }

        /*if(minOfEachPlaylist[element.inWhichPlaylist] == element){
            Song minSong = null;
            for(Song song: heap){
                if (song.inWhichPlaylist == element.inWhichPlaylist && compare(song,minSong)<0){
                    minSong = song;
                }
            }
            minOfEachPlaylist[element.inWhichPlaylist] = minSong;
        }*/

        return true;
    }


    // Main method for testing
    public static void main(String[] args) {
        // Create a MaxHeap with a capacity of 10 and a category (which is not used in this version)
        MaxHeap maxHeap = new MaxHeap(10, 1);

        // Insert some integers into the heap
        maxHeap.insert(new Song(new String[]{
                "1",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(86),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));
        maxHeap.insert(new Song(new String[]{
                "2",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(91),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));
        maxHeap.insert(new Song(new String[]{
                "3",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(100),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));
        maxHeap.insert(new Song(new String[]{
                "4",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(13),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));
        maxHeap.insert(new Song(new String[]{
                "5",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(26),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));
        maxHeap.insert(new Song(new String[]{
                "6",
                "",
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(65),
                Integer.toString(Integer.MAX_VALUE),
                Integer.toString(Integer.MAX_VALUE)
        }));

        for (Song song: maxHeap.getHeap()){
            if(song == null){break;}
            System.out.println(song.songId);
        }

        // Print the size of the heap
        System.out.println("Size of the heap: " + maxHeap.getSize());

        // Extract and print the maximum element
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);
        System.out.println("Max element extracted: " + maxHeap.extractMax().songId);



        // Print the size of the heap after extraction
        System.out.println("Size of the heap after extraction: " + maxHeap.getSize());
    }
}
