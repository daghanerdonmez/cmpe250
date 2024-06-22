import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Misc {
    public double Distance(double lat1, double long1, double lat2, double long2){
        return 2 * 6371 * Math.asin(
                Math.sqrt(
                        (Math.pow(
                                Math.sin(
                                        (Math.toRadians(lat2)-Math.toRadians(lat1))/2
                                ),2
                        ) + Math.cos(
                                Math.toRadians(lat1)
                        ) * Math.cos(
                                Math.toRadians(lat2)
                        ) * Math.pow(
                                Math.sin(
                                        (Math.toRadians(long2) - Math.toRadians(long1))/2
                                ),2
                        ))
                )
        );
    }

    public int[] weatherCodeToBinary(int weatherCode){
        int wind = weatherCode/16;
        weatherCode -= wind*16;
        int rain = weatherCode/8;
        weatherCode -= rain*8;
        int snow = weatherCode/4;
        weatherCode -= snow*4;
        int hail = weatherCode/2;
        weatherCode -= hail*2;
        int bolt = weatherCode;

        return new int[]{wind,rain,snow,hail,bolt};
    }

    public double weatherCodeToMultiplier(int weatherCode){
        int[] weatherBinary = weatherCodeToBinary(weatherCode);
        return (weatherBinary[0]*1.05+(1-weatherBinary[0])) *
                (weatherBinary[1]*1.05+(1-weatherBinary[1])) *
                (weatherBinary[2]*1.10+(1-weatherBinary[2])) *
                (weatherBinary[3]*1.15+(1-weatherBinary[3])) *
                (weatherBinary[4]*1.20+(1-weatherBinary[4]));
    }

    public int[] giveT0andTf(String weatherFile) throws IOException {
        FileReader weatherReader = new FileReader(weatherFile);
        BufferedReader bufferedWeatherReader = new BufferedReader(weatherReader);

        String line = bufferedWeatherReader.readLine();
        int t0 = -1;
        int tf = -1;

        while ((line = bufferedWeatherReader.readLine()) != null){
            if (t0 == -1){
                t0 = Integer.parseInt(line.split(",")[1]);
            }
            tf = Integer.parseInt(line.split(",")[1]);
        }

        return new int[]{t0,tf};

    }

    public int timestampToIndex(int t0, int timestamp){
        return (timestamp - t0)/21600;
    }

    public Map<String, Integer> giveTimeOriginWeathers(Map<String, ArrayList<Integer>> weathers, int timeOrigin, String weatherFile) throws IOException {
        Map<String,Integer> timeOriginWeathers = new HashMap<>();
        int index = timestampToIndex(giveT0andTf(weatherFile)[0],timeOrigin);
        for (Map.Entry<String, ArrayList<Integer>> entry : weathers.entrySet()) {
            String airfield = entry.getKey();
            ArrayList<Integer> airfieldWeathers = entry.getValue();
            timeOriginWeathers.put(airfield, airfieldWeathers.get(index));
        }

        return timeOriginWeathers;
    }

    public static void main(String[] args) {
        Misc misc = new Misc();
        System.out.println(Arrays.toString(misc.weatherCodeToBinary(18)));
    }
}
