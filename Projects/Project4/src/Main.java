import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        long startTime = System.nanoTime(); //used to evaluate run time

        //checking args length
        if (args.length < 6) {
            System.out.println("Usage: java Main <airports-csv> <directions-csv> <weather-csv> <missions-in> <task1-out> <task2-out>");
            return;
        }

        Graph graph = new Graph();
        Misc misc = new Misc();
        Map<String, ArrayList<Integer>> weathers = new HashMap<>();

        //getting args
        String airportFile = args[0];
        String directionFile = args[1];
        String weatherFile = args[2];
        String missionFile = args[3];
        String task1File = args[4];
        String task2File = args[5];

        //handle the file reading and writing parts
        try {
            FileReader airportReader = new FileReader(airportFile);
            BufferedReader bufferedAirportReader = new BufferedReader(airportReader);

            FileReader directionReader = new FileReader(directionFile);
            BufferedReader bufferedDirectionReader = new BufferedReader(directionReader);

            FileReader weatherReader = new FileReader(weatherFile);
            BufferedReader bufferedWeatherReader = new BufferedReader(weatherReader);

            FileReader missionReader = new FileReader(missionFile);
            BufferedReader bufferedMissionReader = new BufferedReader(missionReader);

            FileWriter task1fileWriter = new FileWriter(task1File);
            PrintWriter task1Writer = new PrintWriter(task1fileWriter);

            FileWriter task2fileWriter = new FileWriter(task2File);
            PrintWriter task2Writer = new PrintWriter(task2fileWriter);

            String line= bufferedAirportReader.readLine();

            //handle every line of the input1 one by one
            while ((line = bufferedAirportReader.readLine()) != null) {
                String[] lineParts = line.split(",");
                Graph.Airport airport = new Graph.Airport(lineParts[0], lineParts[1], Double.parseDouble(lineParts[2]), Double.parseDouble(lineParts[3]), Integer.parseInt(lineParts[4]));
                graph.addAirport(airport);
            }

            line = bufferedDirectionReader.readLine();

            while((line = bufferedDirectionReader.readLine()) != null){
                String[] lineParts = line.split(",");
                Graph.Airport source = graph.getAirport(lineParts[0]);
                Graph.Airport sink = graph.getAirport(lineParts[1]);
                source.add(sink, misc.Distance(source.latitude, source.longitude, sink.latitude, sink.longitude));
            }

            line = bufferedWeatherReader.readLine();
            String previousAirfield = "";
            ArrayList<Integer> previousWeather = null;

            while((line = bufferedWeatherReader.readLine()) != null){
                String[] lineParts = line.split(",");
                if(!previousAirfield.equals(lineParts[0])){
                    if(previousWeather != null){
                        weathers.put(previousAirfield, previousWeather);
                    }
                    ArrayList<Integer> weather = new ArrayList<>();
                    weather.add(Integer.parseInt(lineParts[2]));
                    previousAirfield = lineParts[0];
                    previousWeather = weather;
                } else{
                    previousWeather.add(Integer.parseInt(lineParts[2]));
                }
            }

            solveTask1(bufferedMissionReader, task1fileWriter, weathers, graph, weatherFile);
            /*for (Map.Entry<String, Integer> entry : misc.giveTimeOriginWeathers(weathers, 1682370000, weatherFile).entrySet()) {
                String airfield = entry.getKey();
                Integer airfieldWeather = entry.getValue();
                System.out.println(airfield + airfieldWeather);
            }*/


            //close all IOs
            bufferedAirportReader.close();
            bufferedDirectionReader.close();
            bufferedWeatherReader.close();
            bufferedMissionReader.close();
            task1Writer.close();
            task2Writer.close();

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

    public static void solveTask1(BufferedReader bufferedMissionReader, FileWriter task1Writer, Map<String, ArrayList<Integer>> weathers, Graph graph, String weatherFile) throws IOException {
        Misc misc = new Misc();
        String line = bufferedMissionReader.readLine();

        while((line = bufferedMissionReader.readLine()) != null){
            String[] lineParts = line.split(" ");
            graph.computeShortestPathUsingWeather(lineParts[0], misc.giveTimeOriginWeathers(weathers, Integer.parseInt(lineParts[2]), weatherFile));
            Graph.Airport destination = graph.getAirport(lineParts[1]);
            for (Graph.Airport airport: destination.shortestPath){
                task1Writer.write(airport.airportCode + " ");
                //System.out.println(airport.airportCode);
            }
            task1Writer.write(destination.airportCode + " ");
            //System.out.println(graph.getAirport(lineParts[1]).cost);
            task1Writer.write(String.format("%.5f", destination.cost) + "\n");
            graph.resetGraph();
        }
    }
}