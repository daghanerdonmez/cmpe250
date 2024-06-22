public class Song {
    public int songId;
    public String songName;
    public int playCount;
    public int heartacheScore;
    public int roadtripScore;
    public int blissfulScore;

    public int inWhichPlaylist;

    public boolean addedToEpicBlendArray = false;

    public int indexAtHeartacheSongs;
    public int indexAtRoadtripSongs;
    public int indexAtBlissfulSongs;
    public int indexAtHeartacheMaxHeap;
    public int indexAtRoadtripMaxHeap;
    public int indexAtBlissfulMaxHeap;


    Song(String[] songInfo){
        this.songId = Integer.parseInt(songInfo[0]);
        this.songName = songInfo[1];
        this.playCount = Integer.parseInt(songInfo[2]);
        this.heartacheScore = Integer.parseInt(songInfo[3]);
        this.roadtripScore = Integer.parseInt(songInfo[4]);
        this.blissfulScore = Integer.parseInt(songInfo[5]);
    }

    public int playCountCompare(Song otherSong){
        if (playCount == otherSong.playCount){
            return -this.songName.compareTo(otherSong.songName);
        }
        return Integer.compare(playCount, otherSong.playCount);
    }

    public int heartacheCompare(Song otherSong){
        if (heartacheScore == otherSong.heartacheScore){
            return -this.songName.compareTo(otherSong.songName);
        }
        return Integer.compare(heartacheScore, otherSong.heartacheScore);
    }

    public int roadtripCompare(Song otherSong){
        if (roadtripScore == otherSong.roadtripScore){
            return -this.songName.compareTo(otherSong.songName);
        }
        return Integer.compare(roadtripScore, otherSong.roadtripScore);
    }

    public int blissfulCompare(Song otherSong){
        if (blissfulScore == otherSong.blissfulScore){
            return -this.songName.compareTo(otherSong.songName);
        }
        return Integer.compare(blissfulScore, otherSong.blissfulScore);
    }
}




