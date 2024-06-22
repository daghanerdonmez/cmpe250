import java.io.PrintWriter;
import java.util.ArrayList;

public class EpicBlend {
    public int maxNumSongsFromEachPlaylist;
    public int maxHeartacheCount;
    public int maxRoadtripCount;
    public int maxBlissfulCount;
    public int numPlaylists;

    public MinHeap heartacheSongs = new MinHeap(100, 1);
    public MinHeap roadtripSongs = new MinHeap(100, 2);
    public MinHeap blissfulSongs = new MinHeap(100, 3);

    public int[] heartacheFromEachPlaylist;
    public int[] roadtripFromEachPlaylist;
    public int[] blissfulFromEachPlaylist;

    EpicBlend(){}

    public void initializeMinOfPlaylistsArray(){
        heartacheSongs.heapOfEachPlaylist = new MinHeapForPlaylists[numPlaylists+1];
        roadtripSongs.heapOfEachPlaylist = new MinHeapForPlaylists[numPlaylists+1];
        blissfulSongs.heapOfEachPlaylist = new MinHeapForPlaylists[numPlaylists+1];
    }

    public void initializeEpicBlend(MaxHeap heartacheMaxHeap, MaxHeap roadtripMaxHeap, MaxHeap blissfulMaxHeap){
        fillHeartacheSongs(heartacheMaxHeap);
        fillRoadtripSongs(roadtripMaxHeap);
        fillBlissfulSongs(blissfulMaxHeap);
    }

    public void fillHeartacheSongs(MaxHeap heartacheMaxHeap){
        ArrayList<Song> heldOutSongs = new ArrayList<>();
        Song currentHeartacheSong;

        while (heartacheSongs.getSize() < maxHeartacheCount){
            currentHeartacheSong = heartacheMaxHeap.peek();
            if(currentHeartacheSong == null){
                break;
            }
            if (heartacheFromEachPlaylist[currentHeartacheSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                heartacheSongs.insert(currentHeartacheSong);
                heartacheFromEachPlaylist[currentHeartacheSong.inWhichPlaylist]++;
                heartacheMaxHeap.extractMax();
            } else {
                heldOutSongs.add(heartacheMaxHeap.extractMax());
            }
        }

        for (Song heldOutSong : heldOutSongs) {
            heartacheMaxHeap.insert(heldOutSong);
        }
    }

    public void fillRoadtripSongs(MaxHeap roadtripMaxHeap){
        ArrayList<Song> heldOutSongs = new ArrayList<>();
        Song currentRoadtripSong;

        while (roadtripSongs.getSize() < maxRoadtripCount){
            currentRoadtripSong = roadtripMaxHeap.peek();
            if(currentRoadtripSong == null){
                break;
            }
            if (roadtripFromEachPlaylist[currentRoadtripSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                roadtripSongs.insert(currentRoadtripSong);
                roadtripFromEachPlaylist[currentRoadtripSong.inWhichPlaylist]++;
                roadtripMaxHeap.extractMax();
            } else {
                heldOutSongs.add(roadtripMaxHeap.extractMax());
            }
        }

        for (Song heldOutSong : heldOutSongs) {
            roadtripMaxHeap.insert(heldOutSong);
        }
    }

    public void fillBlissfulSongs(MaxHeap blissfulMaxHeap){
        ArrayList<Song> heldOutSongs = new ArrayList<>();
        Song currentBlissfulSong;

        while (blissfulSongs.getSize() < maxBlissfulCount){
            currentBlissfulSong = blissfulMaxHeap.peek();
            if (currentBlissfulSong == null){
                break;
            }
            if (blissfulFromEachPlaylist[currentBlissfulSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                blissfulSongs.insert(currentBlissfulSong);
                blissfulFromEachPlaylist[currentBlissfulSong.inWhichPlaylist]++;
                blissfulMaxHeap.extractMax();
            } else {
                heldOutSongs.add(blissfulMaxHeap.extractMax());
            }
        }

        for (Song heldOutSong : heldOutSongs) {
            blissfulMaxHeap.insert(heldOutSong);
        }
        heldOutSongs.clear();

    }

    public void checkAddModifications(PrintWriter printWriter, MaxHeap heartacheMaxHeap, MaxHeap roadtripMaxHeap, MaxHeap blissfulMaxHeap, Song addedSong, int playlistId){
        addedSong.inWhichPlaylist = playlistId;
        Song minHeartacheSong = heartacheSongs.peek();
        Song minRoadtripSong = roadtripSongs.peek();
        Song minBlissfulSong = blissfulSongs.peek();

        String[] heartacheChange = new String[2];
        String[] roadtripChange = new String[2];
        String[] blissfulChange = new String[2];

        /*if(addedSong.songId == 97){
            System.out.println("97 geldi");
            System.out.println(blissfulSongs.getSize());
            System.out.println(maxBlissfulCount);
            System.out.println(minBlissfulSong.songId);
            System.out.println(minBlissfulSong.blissfulScore);
        }*/

        if(heartacheSongs.getSize() < maxHeartacheCount){
            if(heartacheFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){

                //System.out.println("a");

                heartacheSongs.insert(addedSong);
                heartacheFromEachPlaylist[addedSong.inWhichPlaylist]++;
                heartacheChange[0] = Integer.toString(addedSong.songId);
                heartacheChange[1] = "0";
            }else{

                //System.out.println("b");

                if(addedSong.heartacheCompare(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    heartacheChange[0] = Integer.toString(addedSong.songId);
                    heartacheChange[1] = Integer.toString(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    heartacheMaxHeap.insert(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    heartacheSongs.remove(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    heartacheSongs.insert(addedSong);
                }else{
                    heartacheMaxHeap.insert(addedSong);
                    heartacheChange[0] = "0";
                    heartacheChange[1] = "0";
                }
            }
        }else if (addedSong.heartacheCompare(minHeartacheSong) > 0){
            if(heartacheFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){

                //System.out.println("c");

                heartacheSongs.extractMin();
                heartacheSongs.insert(addedSong);
                heartacheFromEachPlaylist[minHeartacheSong.inWhichPlaylist]--;
                heartacheMaxHeap.insert(minHeartacheSong);
                heartacheChange[0] = Integer.toString(addedSong.songId);
                heartacheChange[1] = Integer.toString(minHeartacheSong.songId);
            } else{

                //System.out.println("d");

                if(addedSong.heartacheCompare(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    heartacheChange[0] = Integer.toString(addedSong.songId);
                    heartacheChange[1] = Integer.toString(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    heartacheMaxHeap.insert(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    heartacheSongs.remove(heartacheSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    heartacheSongs.insert(addedSong);
                }else{
                    heartacheMaxHeap.insert(addedSong);
                    heartacheChange[0] = "0";
                    heartacheChange[1] = "0";
                }
            }
        } else{

            //System.out.println("e");

            heartacheMaxHeap.insert(addedSong);
            heartacheChange[0] = "0";
            heartacheChange[1] = "0";
        }

        if(roadtripSongs.getSize() < maxRoadtripCount){
            if(roadtripFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                roadtripSongs.insert(addedSong);
                roadtripFromEachPlaylist[addedSong.inWhichPlaylist]++;
                roadtripChange[0] = Integer.toString(addedSong.songId);
                roadtripChange[1] = "0";
            }else{
                if(addedSong.roadtripCompare(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    roadtripChange[0] = Integer.toString(addedSong.songId);
                    roadtripChange[1] = Integer.toString(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    roadtripMaxHeap.insert(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    roadtripSongs.remove(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    roadtripSongs.insert(addedSong);
                }else{
                    roadtripMaxHeap.insert(addedSong);
                    roadtripChange[0] = "0";
                    roadtripChange[1] = "0";
                }
            }
        }else if (addedSong.roadtripCompare(minRoadtripSong) > 0){
            if(roadtripFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                roadtripSongs.extractMin();
                roadtripSongs.insert(addedSong);
                roadtripFromEachPlaylist[minRoadtripSong.inWhichPlaylist]--;
                roadtripMaxHeap.insert(minRoadtripSong);
                roadtripChange[0] = Integer.toString(addedSong.songId);
                roadtripChange[1] = Integer.toString(minRoadtripSong.songId);
            } else{
                if(addedSong.roadtripCompare(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    roadtripChange[0] = Integer.toString(addedSong.songId);
                    roadtripChange[1] = Integer.toString(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    roadtripMaxHeap.insert(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    roadtripSongs.remove(roadtripSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    roadtripSongs.insert(addedSong);
                }else{
                    roadtripMaxHeap.insert(addedSong);
                    roadtripChange[0] = "0";
                    roadtripChange[1] = "0";
                }

            }
        } else{
            roadtripMaxHeap.insert(addedSong);
            roadtripChange[0] = "0";
            roadtripChange[1] = "0";
        }

        if(blissfulSongs.getSize() < maxBlissfulCount){
            if(blissfulFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){

                //System.out.println("a");

                blissfulSongs.insert(addedSong);
                blissfulFromEachPlaylist[addedSong.inWhichPlaylist]++;
                blissfulChange[0] = Integer.toString(addedSong.songId);
                blissfulChange[1] = "0";
            }else {

                //System.out.println("b");

                if(addedSong.blissfulCompare(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    //System.out.println("aaaa");
                    //System.out.println(Integer.toString(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId));
                    blissfulChange[0] = Integer.toString(addedSong.songId);
                    blissfulChange[1] = Integer.toString(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    blissfulMaxHeap.insert(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    blissfulSongs.remove(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    blissfulSongs.insert(addedSong);
                }else{
                    blissfulMaxHeap.insert(addedSong);
                    blissfulChange[0] = "0";
                    blissfulChange[1] = "0";
                }
            }
        }else if (addedSong.blissfulCompare(minBlissfulSong) > 0){
            if(blissfulFromEachPlaylist[addedSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){

                //System.out.println("c");

                blissfulSongs.extractMin();
                blissfulSongs.insert(addedSong);
                blissfulFromEachPlaylist[minBlissfulSong.inWhichPlaylist]--;
                blissfulMaxHeap.insert(minBlissfulSong);
                blissfulChange[0] = Integer.toString(addedSong.songId);
                blissfulChange[1] = Integer.toString(minBlissfulSong.songId);
            } else{

                //System.out.println("d");

                if(addedSong.blissfulCompare(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek()) > 0){
                    blissfulChange[0] = Integer.toString(addedSong.songId);
                    blissfulChange[1] = Integer.toString(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek().songId);
                    blissfulMaxHeap.insert(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    blissfulSongs.remove(blissfulSongs.heapOfEachPlaylist[addedSong.inWhichPlaylist].peek());
                    blissfulSongs.insert(addedSong);
                }else{
                    blissfulMaxHeap.insert(addedSong);
                    blissfulChange[0] = "0";
                    blissfulChange[1] = "0";
                }
            }
        } else{

            //System.out.println("e");

            blissfulMaxHeap.insert(addedSong);
            blissfulChange[0] = "0";
            blissfulChange[1] = "0";
        }

        String change0 = heartacheChange[0] + " " + roadtripChange[0] + " " + blissfulChange[0];
        String change1 = heartacheChange[1] + " " + roadtripChange[1] + " " + blissfulChange[1];

        printWriter.write(change0 + "\n");
        printWriter.write(change1 + "\n");
    }

    public void buildEpicBlend(PrintWriter printWriter){

        MaxHeap epicBlendMaxHeap = new MaxHeap(maxHeartacheCount+maxBlissfulCount+maxRoadtripCount,0);
        Song[] songsInEpicBlend = new Song[maxHeartacheCount+maxBlissfulCount+maxRoadtripCount];
        int i = 0;
        for(int j = 1; j < heartacheSongs.getSize()+1; j++){
            if(heartacheSongs.getHeap()[j] == null){
                break;
            }
            if(!heartacheSongs.getHeap()[j].addedToEpicBlendArray){
                songsInEpicBlend[i] = heartacheSongs.getHeap()[j];
                heartacheSongs.getHeap()[j].addedToEpicBlendArray = true;
                i++;
            }
        }
        for(int j = 1; j < roadtripSongs.getSize()+1; j++){
            if(roadtripSongs.getHeap()[j] == null){
                break;
            }
            if(!roadtripSongs.getHeap()[j].addedToEpicBlendArray){
                songsInEpicBlend[i] = roadtripSongs.getHeap()[j];
                roadtripSongs.getHeap()[j].addedToEpicBlendArray = true;
                i++;
            }
        }
        for(int j = 1; j < blissfulSongs.getSize()+1; j++){
            if(blissfulSongs.getHeap()[j] == null){
                break;
            }
            if(!blissfulSongs.getHeap()[j].addedToEpicBlendArray){
                songsInEpicBlend[i] = blissfulSongs.getHeap()[j];
                blissfulSongs.getHeap()[j].addedToEpicBlendArray = true;
                i++;
            }
        }
        for(Song song: songsInEpicBlend){
            if(song==null){break;}
            song.addedToEpicBlendArray = false;
            epicBlendMaxHeap.insert(song);
        }
        Song[] sortedEpicBlend = new Song[epicBlendMaxHeap.getSize()];
        i = 0;
        while(epicBlendMaxHeap.peek() != null){
            sortedEpicBlend[i] = epicBlendMaxHeap.extractMax();
            i++;
        }
        for (Song song: sortedEpicBlend){
            printWriter.write(song.songId + " ");
        }
    }

    public void remove(PrintWriter printWriter, MaxHeap heartacheMaxHeap, MaxHeap roadtripMaxHeap, MaxHeap blissfulMaxHeap, Song removedSong){

        String[] heartacheChange = new String[]{"0","0"};
        String[] roadtripChange = new String[]{"0","0"};
        String[] blissfulChange = new String[]{"0","0"};

        if(removedSong.songId == 48){
            System.out.println("48 geldi");
            System.out.println(heartacheMaxHeap.peek());
        }

        if(heartacheSongs.remove(removedSong)){
            heartacheChange[1] = Integer.toString(removedSong.songId);
            heartacheFromEachPlaylist[removedSong.inWhichPlaylist]--;

            //fillHeartacheSongs with print
            ArrayList<Song> heldOutSongs = new ArrayList<>();
            Song currentHeartacheSong;

            while (heartacheSongs.getSize() < maxHeartacheCount){
                currentHeartacheSong = heartacheMaxHeap.peek();
                if(currentHeartacheSong == null){
                    break;
                }
                if (heartacheFromEachPlaylist[currentHeartacheSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                    heartacheSongs.insert(currentHeartacheSong);
                    heartacheChange[0] = Integer.toString(currentHeartacheSong.songId);
                    heartacheFromEachPlaylist[currentHeartacheSong.inWhichPlaylist]++;
                    heartacheMaxHeap.extractMax();
                } else {
                    heldOutSongs.add(heartacheMaxHeap.extractMax());
                }
            }

            for (Song heldOutSong : heldOutSongs) {
                heartacheMaxHeap.insert(heldOutSong);
            }
        } else{
            heartacheMaxHeap.remove(removedSong);
        }

        if(roadtripSongs.remove(removedSong)){
            roadtripChange[1] = Integer.toString(removedSong.songId);
            roadtripFromEachPlaylist[removedSong.inWhichPlaylist]--;

            //fillRoadtripSongs with print
            ArrayList<Song> heldOutSongs = new ArrayList<>();
            Song currentRoadtripSong;

            while (roadtripSongs.getSize() < maxRoadtripCount){
                currentRoadtripSong = roadtripMaxHeap.peek();
                if(currentRoadtripSong == null){
                    break;
                }
                if (roadtripFromEachPlaylist[currentRoadtripSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                    roadtripSongs.insert(currentRoadtripSong);
                    roadtripChange[0] = Integer.toString(currentRoadtripSong.songId);
                    roadtripFromEachPlaylist[currentRoadtripSong.inWhichPlaylist]++;
                    roadtripMaxHeap.extractMax();
                } else {
                    heldOutSongs.add(roadtripMaxHeap.extractMax());
                }
            }

            for (Song heldOutSong : heldOutSongs) {
                roadtripMaxHeap.insert(heldOutSong);
            }
        }else{
            roadtripMaxHeap.remove(removedSong);
        }

        if(blissfulSongs.remove(removedSong)){
            blissfulChange[1] = Integer.toString(removedSong.songId);
            blissfulFromEachPlaylist[removedSong.inWhichPlaylist]--;

            //fillBlissfulSongs with print
            ArrayList<Song> heldOutSongs = new ArrayList<>();
            Song currentBlissfulSong;

            while (blissfulSongs.getSize() < maxBlissfulCount){
                currentBlissfulSong = blissfulMaxHeap.peek();
                if (currentBlissfulSong == null){
                    break;
                }
                if (blissfulFromEachPlaylist[currentBlissfulSong.inWhichPlaylist] < maxNumSongsFromEachPlaylist){
                    blissfulSongs.insert(currentBlissfulSong);
                    blissfulChange[0] = Integer.toString(currentBlissfulSong.songId);
                    blissfulFromEachPlaylist[currentBlissfulSong.inWhichPlaylist]++;
                    blissfulMaxHeap.extractMax();
                } else {
                    heldOutSongs.add(blissfulMaxHeap.extractMax());
                }
            }

            for (Song heldOutSong : heldOutSongs) {
                blissfulMaxHeap.insert(heldOutSong);
            }
            heldOutSongs.clear();
        } else{
            blissfulMaxHeap.remove(removedSong);
        }

        removedSong.inWhichPlaylist = 0;

        String change0 = heartacheChange[0] + " " + roadtripChange[0] + " " + blissfulChange[0];
        String change1 = heartacheChange[1] + " " + roadtripChange[1] + " " + blissfulChange[1];

        printWriter.write(change0 + "\n");
        printWriter.write(change1 + "\n");
    }
}
