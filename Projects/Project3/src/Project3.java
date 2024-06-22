import java.io.*;

public class Project3 {
    public static void main(String[] args) {
        long startTime = System.nanoTime(); //used to evaluate run time

        //checking args length
        if (args.length < 3) {
            System.out.println("Usage: java project2 <song_file> <test_case_file> <output_file>");
            return;
        }

        EpicBlend epicBlend = new EpicBlend();
        MaxHeap heartacheCandidates = new MaxHeap(100,1);
        MaxHeap roadtripCandidates = new MaxHeap(100, 2);
        MaxHeap blissfulCandidates = new MaxHeap(100, 3);

        //getting args
        String inputFile = args[0];
        String inputFile2 = args[1];
        String outputFile = args[2];

        //handle the file reading and writing parts
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileReader fileReader2 = new FileReader(inputFile2);
            BufferedReader bufferedReader2 = new BufferedReader(fileReader2);

            FileWriter fileWriter = new FileWriter(outputFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String line= bufferedReader.readLine();

            Song[] songs = new Song[Integer.parseInt(line)+1];

            //handle every line of the input1 one by one
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineParts = line.split(" ");
                Song song = new Song(lineParts);
                songs[Integer.parseInt(lineParts[0])] = song;
            }

            line = bufferedReader2.readLine();
            epicBlend.maxNumSongsFromEachPlaylist = Integer.parseInt(line.split(" ")[0]);
            epicBlend.maxHeartacheCount = Integer.parseInt(line.split(" ")[1]);
            epicBlend.maxRoadtripCount = Integer.parseInt(line.split(" ")[2]);
            epicBlend.maxBlissfulCount = Integer.parseInt(line.split(" ")[3]);

            line = bufferedReader2.readLine();
            epicBlend.numPlaylists = Integer.parseInt(line);
            epicBlend.heartacheFromEachPlaylist = new int[epicBlend.numPlaylists+1];
            epicBlend.roadtripFromEachPlaylist = new int[epicBlend.numPlaylists+1];
            epicBlend.blissfulFromEachPlaylist = new int[epicBlend.numPlaylists+1];
            epicBlend.initializeMinOfPlaylistsArray();

            for(int i = 1; i < epicBlend.numPlaylists+1; i++) {
                line = bufferedReader2.readLine();
                String[] lineSplit = line.split(" ");
                int songCount = Integer.parseInt(lineSplit[1]);
                line = bufferedReader2.readLine();
                String[] songlist = line.split(" ");
                for (int j = 0; j < songCount; j++) {
                    int songId = Integer.parseInt(songlist[j]);
                    songs[songId].inWhichPlaylist = i;
                    heartacheCandidates.insert(songs[songId]);
                    roadtripCandidates.insert(songs[songId]);
                    blissfulCandidates.insert(songs[songId]);
                }
            }

            /*for(Song song: blissfulCandidates.getHeap()){
                if (song == null) {
                    break;
                }
                System.out.println(song.songId);
            }*/

            /*while(heartacheCandidates.peek() != null){
                System.out.println(heartacheCandidates.extractMax().songId);
            }*/

            epicBlend.initializeEpicBlend(heartacheCandidates, roadtripCandidates, blissfulCandidates);

            int i = 0;
            while ((line = bufferedReader2.readLine()) != null){
                /*i++;
                if(i%100 == 0){
                    System.out.println(i);
                }*/
                //System.out.println(line);
                String[] lineParts = line.split(" ");
                int songId;
                switch(lineParts[0]){
                    case "ASK":
                        epicBlend.buildEpicBlend(printWriter);
                        printWriter.write("\n");
                        break;
                    case "ADD":

                        //System.out.println(line);
                        songId = Integer.parseInt(lineParts[1]);
                        int playlistId = Integer.parseInt(lineParts[2]);
                        epicBlend.checkAddModifications(printWriter, heartacheCandidates, roadtripCandidates, blissfulCandidates, songs[songId], playlistId);
                        break;
                    case "REM":
                        songId = Integer.parseInt(lineParts[1]);
                        epicBlend.remove(printWriter, heartacheCandidates, roadtripCandidates, blissfulCandidates, songs[songId]);
                        break;
                }
            }

            /*for(Song song: epicBlend.heartacheSongs.getHeap()){
                if (song == null) {
                    break;
                }
                System.out.println(song.songId);
            }*/


            /*while(epicBlend.blissfulSongs.peek() != null){
                System.out.println(epicBlend.blissfulSongs.extractMin().songId);
            }*/

            //close all IOs
            bufferedReader.close();
            bufferedReader2.close();
            printWriter.close();

            //exceptions catchers for the IO
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Elapsed time in milliseconds: " + duration / 1_000_000);

    }
}