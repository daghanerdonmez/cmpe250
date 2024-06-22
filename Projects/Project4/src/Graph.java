import java.util.*;

public class Graph {

    Misc misc = new Misc();

    public static class Airport implements Comparable<Airport>{
        public String airportCode;
        public String airfieldName;
        public double latitude;
        public double longitude;
        public int parkingCost;
        public Map<Airport, Double> adjacentAirports; // node to cost
        public LinkedList<Airport> shortestPath;

        public void setCost(double cost) {
            this.cost = cost;
        }

        public double cost;

        public Airport(String airportCode, String airfieldName, double latitude, double longitude, int parkingCost){
            this.airportCode = airportCode;
            this.airfieldName = airfieldName;
            this.latitude = latitude;
            this.longitude = longitude;
            this.parkingCost = parkingCost;
            shortestPath = new LinkedList<>();
            adjacentAirports = new HashMap<>();
            cost = Double.MAX_VALUE;
        }

        public void add(Airport neighborAirport, double cost){
            adjacentAirports.put(neighborAirport, cost);
        }


        @Override
        public int compareTo(Airport otherAirport) {
            return Double.compare(this.cost, otherAirport.cost);
        }
    }

    private Map<String,Airport> airports;
    private PriorityQueue<Airport> heap;


    public Graph(){
        airports = new HashMap<>();
        heap = new PriorityQueue<>();
    }

    public void addAirport(Airport newAirport){
        airports.put(newAirport.airportCode, newAirport);
    }

    public Airport getAirport(String airportCode){
        return airports.get(airportCode);
    }

    /*public void computeShortestPath(String sourceAirportCode){
        Airport sourceAirport = airports.get(sourceAirportCode);
        sourceAirport.setCost(0);

        Set<Airport> settledAirports = new HashSet<>();

        heap.add(sourceAirport);
        while (settledAirports.size() != airports.size()){
            if (heap.isEmpty()){
                return;
            }

            // Removing the minimum distance node
            // from the priority queue
            Airport minDistanceAirport = heap.poll();
            // Adding the node whose distance is
            // finalized
            if (settledAirports.contains(minDistanceAirport))
                // Continue keyword skips execution for
                // following check
                continue;

            settledAirports.add(minDistanceAirport);

            for(Map.Entry<Airport, Double> adjacentPair: minDistanceAirport.adjacentAirports.entrySet()){
                Airport adjacentAirport = adjacentPair.getKey();
                Double weight = adjacentPair.getValue();
                if (!settledAirports.contains(adjacentAirport)){

                    double costOfMinDistanceNode = minDistanceAirport.cost;
                    if(costOfMinDistanceNode + weight < adjacentAirport.cost){
                        adjacentAirport.setCost(costOfMinDistanceNode + weight);

                        LinkedList<Airport> shortestPath = new LinkedList<>(sourceAirport.shortestPath);
                        shortestPath.add(sourceAirport);
                        adjacentAirport.shortestPath = shortestPath;
                    }

                    heap.add(adjacentAirport);

                }
            }


        }

    }*/

    public void resetGraph() {
        // Iterate through each airport and reset their cost and shortest path
        for (Airport airport : airports.values()) {
            airport.cost = Double.MAX_VALUE;
            airport.shortestPath.clear();
        }

        // Clear the priority queue
        heap.clear();
    }

    public void computeShortestPath(String sourceAirportCode){
        Airport sourceAirport = airports.get(sourceAirportCode);
        sourceAirport.cost = 0; // Set the source airport cost to 0

        heap.add(sourceAirport);

        Set<Airport> settledAirports = new HashSet<>();

        while (!heap.isEmpty()){
            Airport minDistanceAirport = heap.poll();

            if (settledAirports.contains(minDistanceAirport)){
                continue;
            }

            settledAirports.add(minDistanceAirport);

            for (Map.Entry<Airport, Double> adjacentPair: minDistanceAirport.adjacentAirports.entrySet()) {
                Airport adjacentAirport = adjacentPair.getKey();
                Double edgeWeight = adjacentPair.getValue();

                if (!settledAirports.contains(adjacentAirport)) {
                    double newCost = minDistanceAirport.cost + edgeWeight;

                    if (newCost < adjacentAirport.cost) {
                        adjacentAirport.cost = newCost;

                        LinkedList<Airport> shortestPath = new LinkedList<>(minDistanceAirport.shortestPath);
                        shortestPath.add(minDistanceAirport);
                        adjacentAirport.shortestPath = shortestPath;

                        heap.add(adjacentAirport);
                    }
                }
            }
        }
    }

    public void computeShortestPathUsingWeather(String sourceAirportCode, Map<String, Integer> timeOriginWeathers){
        Airport sourceAirport = airports.get(sourceAirportCode);
        if (sourceAirport == null) {
            // Handle error: source airport not found
            return;
        }

        sourceAirport.cost = 0; // Set the source airport cost to 0
        heap.add(sourceAirport);

        Set<Airport> settledAirports = new HashSet<>();

        while (!heap.isEmpty()){
            Airport minDistanceAirport = heap.poll();

            if (settledAirports.contains(minDistanceAirport)){
                continue;
            }

            settledAirports.add(minDistanceAirport);

            for (Map.Entry<Airport, Double> adjacentPair: minDistanceAirport.adjacentAirports.entrySet()) {
                Airport adjacentAirport = adjacentPair.getKey();
                Double rawEdgeWeight = adjacentPair.getValue();
                Double edgeWeight = 300.0 * misc.weatherCodeToMultiplier(timeOriginWeathers.get(minDistanceAirport.airfieldName)) * misc.weatherCodeToMultiplier(timeOriginWeathers.get(adjacentAirport.airfieldName)) + rawEdgeWeight;

                if (!settledAirports.contains(adjacentAirport)) {
                    double newCost = minDistanceAirport.cost + edgeWeight;

                    if (newCost < adjacentAirport.cost) {
                        adjacentAirport.cost = newCost;

                        LinkedList<Airport> shortestPath = new LinkedList<>(minDistanceAirport.shortestPath);
                        shortestPath.add(minDistanceAirport);
                        adjacentAirport.shortestPath = shortestPath;

                        heap.add(adjacentAirport);
                    }
                }
            }
        }
    }

    /*public void computeShortestPathUsingWeather(String sourceAirportCode, Map<String, Integer> timeOriginWeathers){
        Airport sourceAirport = airports.get(sourceAirportCode);
        sourceAirport.setCost(0);
        if (sourceAirport == null) {
            // Handle error: source airport not found
            return;
        }

        Set<Airport> settledAirports = new HashSet<>();

        heap.add(sourceAirport);
        while (settledAirports.size() != airports.size()){
            if (heap.isEmpty()){
                return;
            }

            // Removing the minimum distance node
            // from the priority queue
            Airport minDistanceAirport = heap.poll();
            // Adding the node whose distance is
            // finalized
            if (settledAirports.contains(minDistanceAirport))
                // Continue keyword skips execution for
                // following check
                continue;

            settledAirports.add(minDistanceAirport);

            for(Map.Entry<Airport, Double> adjacentPair: minDistanceAirport.adjacentAirports.entrySet()){
                Airport adjacentAirport = adjacentPair.getKey();
                Double rawWeight = adjacentPair.getValue();
                Double weight = 300.0 * timeOriginWeathers.get(minDistanceAirport.airfieldName) * timeOriginWeathers.get(adjacentAirport.airfieldName) + rawWeight;
                if (!settledAirports.contains(adjacentAirport)){

                    double costOfMinDistanceNode = minDistanceAirport.cost;
                    if(costOfMinDistanceNode + weight < adjacentAirport.cost){
                        adjacentAirport.setCost(costOfMinDistanceNode + weight);

                        LinkedList<Airport> shortestPath = new LinkedList<>(sourceAirport.shortestPath);
                        shortestPath.add(sourceAirport);
                        adjacentAirport.shortestPath = shortestPath;
                    }

                    heap.add(adjacentAirport);

                }
            }


        }

    }*/

    public static void main(String[] args) {
        // Create airports with the new constructor parameters
        Airport airportA = new Airport("A", "Airfield A", 0.0, 0.0, 10);
        Airport airportB = new Airport("B", "Airfield B", 0.0, 0.0, 10);
        Airport airportC = new Airport("C", "Airfield C", 0.0, 0.0, 10);
        Airport airportD = new Airport("D", "Airfield D", 0.0, 0.0, 10);
        Airport airportE = new Airport("E", "Airfield E", 0.0, 0.0, 10);
        Airport airportF = new Airport("F", "Airfield F", 0.0, 0.0, 10);

        // Add connections between airports
        airportA.add(airportB, 7);
        airportA.add(airportC, 12);
        airportB.add(airportC, 2);
        airportB.add(airportD, 9);
        airportC.add(airportE, 10);
        airportD.add(airportF, 1);
        airportE.add(airportD, 4);
        airportE.add(airportF, 5);

        // Create the graph and add airports to it
        Graph graph = new Graph();
        graph.addAirport(airportA);
        graph.addAirport(airportB);
        graph.addAirport(airportC);
        graph.addAirport(airportD);
        graph.addAirport(airportE);
        graph.addAirport(airportF);

        // Compute the shortest path from airport A
        graph.computeShortestPath("A");

        // Print the cost to get to airport F from A
        System.out.println(airportC.cost);

        for(Airport airport: airportC.shortestPath){
            System.out.println(airport.airfieldName);
        }
    }


}
