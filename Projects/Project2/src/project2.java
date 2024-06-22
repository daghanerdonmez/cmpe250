import java.io.*;

public class project2 {

    public static HashTable<LahmacunShop> business = new HashTable<>(); //create a new hash table of shops

    public static void main(String[] args) {

        long startTime = System.nanoTime(); //used to evaluate run time

        //checking args length
        if (args.length < 3) {
            System.out.println("Usage: java project2 <input_file> <input_file2> <output_file>");
            return;
        }


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

            String line;

            //handle every line of the input1 one by one
            while ((line = bufferedReader.readLine()) != null) {
                //splitting and naming different parts of the line
                String[] lineContent = line.split(",");
                String branchName = lineContent[0] + lineContent[1];
                String employeeName = lineContent[2].strip();
                String position = lineContent[3].strip();
                if (business.containsByName(branchName)) { // the branch already exists
                    Employee newEmployee = new Employee(employeeName, position);
                    business.getBranch(branchName).employeeHashTable.insert(newEmployee, printWriter);
                    //updating position counts
                    switch (position){
                        case "COURIER":
                            business.getBranch(branchName).numCouriers++;
                            break;
                        case "CASHIER":
                            business.getBranch(branchName).numCashiers++;
                            break;
                        case "COOK":
                            business.getBranch(branchName).numCooks++;
                            break;
                        case "MANAGER":
                            business.getBranch(branchName).manager = newEmployee;
                    }
                }
                else{ // it is a new branch
                    Employee newEmployee = new Employee(employeeName, position);
                    business.insert(new LahmacunShop(branchName),printWriter); //first create the new branch
                    business.getBranch(branchName).employeeHashTable.insert(newEmployee,printWriter);
                    switch (position){
                        case "COURIER":
                            business.getBranch(branchName).numCouriers++;
                            break;
                        case "CASHIER":
                            business.getBranch(branchName).numCashiers++;
                            break;
                        case "COOK":
                            business.getBranch(branchName).numCooks++;
                            break;
                        case "MANAGER":
                            business.getBranch(branchName).manager = newEmployee;
                    }
                }
            }

            //handle the second file line by line
            while((line = bufferedReader2.readLine()) != null){
                //System.out.println(line);
                handleLine(line, printWriter);
            }

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

    public static void handleLine(String line, PrintWriter printWriter){

        if (line.isEmpty()){ //handles the empty lines between months
            return;
        }
        String[] lineParts = line.split(":"); //again splitting the line to get meaningful words
        switch (lineParts[0]){
            case "PERFORMANCE_UPDATE":
                String[] lineContent = lineParts[1].split(",");
                String branchName = lineContent[0].strip() + lineContent[1];
                String employeeName = lineContent[2].strip();
                int score = Integer.parseInt(lineContent[3].strip());
                //seperate the line and do the perfomance update task
                business.getBranch(branchName).addScoreToEmployee(employeeName, score, printWriter);
                break;
            case "ADD":
                lineContent = lineParts[1].split(",");
                branchName = lineContent[0].strip() + lineContent[1];
                employeeName = lineContent[2].strip();
                String position = lineContent[3].strip();
                //seperating the line and doing inserting
                if (business.containsByName(branchName)) { // the branch already exists
                    business.getBranch(branchName).employeeHashTable.insert(new Employee(employeeName, position),printWriter);
                    switch (position){ //again updating the position counters
                        case "COURIER":
                            business.getBranch(branchName).numCouriers++;
                            break;
                        case "CASHIER":
                            business.getBranch(branchName).numCashiers++;
                            break;
                        case "COOK":
                            business.getBranch(branchName).numCooks++;
                            break;
                    }
                    business.getBranch(branchName).promotionCheck(position, printWriter);
                    business.getBranch(branchName).dismissCheck(position,printWriter);
                }
                else{ // that's a new branch
                    business.insert(new LahmacunShop(branchName),printWriter);
                    System.out.println(branchName);
                    business.getBranch(branchName).employeeHashTable.insert(new Employee(employeeName, position),printWriter);
                }
                break;
            case "LEAVE":
                lineContent = lineParts[1].split(",");
                branchName = lineContent[0].strip() + lineContent[1];
                employeeName = lineContent[2].strip();
                //seperate the line and do the leaving task
                business.getBranch(branchName).employeeWantsToLeave(employeeName, printWriter,false);
                break;
            case "PRINT_MONTHLY_BONUSES":
                lineContent = lineParts[1].split(",");
                branchName = lineContent[0].strip() + lineContent[1];
                //just getting the monthly bonus number and printing it to the output file
                printWriter.write("Total bonuses for the " + lineContent[1].strip() + " branch this month are: " + business.getBranch(branchName).thisMonthBonus + "\n");
                break;
            case "PRINT_OVERALL_BONUSES":
                lineContent = lineParts[1].split(",");
                branchName = lineContent[0].strip() + lineContent[1];
                //just getting the total bonus number and printing it to the output file
                printWriter.write("Total bonuses for the " + lineContent[1].strip() + " branch are: " + business.getBranch(branchName).totalBonus + "\n");
                break;
            case "PRINT_MANAGER":
                lineContent = lineParts[1].split(",");
                branchName = lineContent[0].strip() + lineContent[1];
                //just getting the manager name and printing it to the output file
                printWriter.write("Manager of the " + lineContent[1].strip() + " branch is " + business.getBranch(branchName).manager.name + ".\n");
                break;
            default:
                //reset monthly bonus for all branches one by one
                business.endOfMonthForShops();
                break;
        }
    }
}