import java.io.*;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        long startTime = System.nanoTime(); //used to evaluate run time

        if (args.length < 2) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            return;
        }

        //getting args
        String inputFile = args[0];
        String outputFile = args[1];

        Family family = new Family(); //create a new family


        //handle the file reading and writing parts
        try {
            FileReader fileReader = new FileReader(inputFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            FileWriter fileWriter = new FileWriter(outputFile);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            String line = bufferedReader.readLine();
            line = "MEMBER_IN " + line; //first line is also technically a member in line
            handleLine(line, family); //handle it


            //handle every line of the input one by one
            while ((line = bufferedReader.readLine()) != null) {
                handleLine(line, family);
                printWriter.write(family.log); //write the logs into the file then reset the log
                family.log = "";
            }

            bufferedReader.close();
            printWriter.close();


        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        //System.out.println("Elapsed time in milliseconds: " + duration / 1_000_000);


    }

    private static void handleLine(String line, Family family){
        String[] inputSplit = line.split(" ");
        String inputType = inputSplit[0];
        //first word of the line is the operation type, the rest are the parameters related to that


        //handle each type of input appropriately
        switch (inputType){
            case "MEMBER_IN":
                memberIn(Arrays.copyOfRange(inputSplit,1,inputSplit.length), family);
                break;
            case "MEMBER_OUT":
                memberOut(Arrays.copyOfRange(inputSplit,1,inputSplit.length), family);
                break;
            case "INTEL_TARGET":
                intelTarget(Arrays.copyOfRange(inputSplit,1,inputSplit.length), family);
                break;
            case "INTEL_DIVIDE":
                intelDivide(family);
                break;
            case "INTEL_RANK":
                intelRank(Arrays.copyOfRange(inputSplit,1,inputSplit.length), family);
                break;
        }
    }

    //5 methods for 5 different types of input lines, each of them are basically splitting the input
    //according to their needs and calling the families method related to that input, these are just an interface
    //between the family and the main classes.
    private static void memberIn(String[] input, Family family){
        String fullName = input[0];
        Double gms = Double.parseDouble(input[1]);

        family.insert(fullName, gms);
    }

    private static void memberOut(String[] input, Family family){
        Double gms = Double.parseDouble(input[1]);

        family.remove(gms);
    }

    private static void intelTarget(String[] input, Family family){
        Double gms1 = Double.parseDouble(input[1]);
        Double gms2 = Double.parseDouble(input[3]);

        family.findLowestCommonAncestor(gms1, gms2);
    }

    private static void intelDivide(Family family){
        family.maxSubNodeSelection();
    }

    private static void intelRank(String[] input, Family family){
        Double gms = Double.parseDouble(input[1]);

        family.findSameRankMembers(gms);
    }
}