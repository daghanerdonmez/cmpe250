import java.io.PrintWriter;
import java.util.LinkedList;

public class LahmacunShop {


    public String name;
    public Employee manager; // tracking the manager here
    public int numCouriers; // number of couriers
    public int numCashiers; // num of cashiers
    public int numCooks; // num of cooks
    public int totalBonus;
    public int thisMonthBonus;
    public LinkedList<Employee> potentialCooks; // list where potential cooks are stored in order (points >=3)
    public LinkedList<Employee> potentialManagers;
    public LinkedList<Employee> couriersToBeDismissed; // list where couriers whose points are <= -5
    public LinkedList<Employee> cashiersToBeDismissed;
    public LinkedList<Employee> cooksToBeDismissed;
    public HashTable<Employee> employeeHashTable; // hash table of all employees

    LahmacunShop(String name){
        this.name = name;
        this.potentialCooks = new LinkedList<>();
        this.potentialManagers = new LinkedList<>();
        this.couriersToBeDismissed = new LinkedList<>();
        this.cashiersToBeDismissed = new LinkedList<>();
        this.cooksToBeDismissed = new LinkedList<>();
        this.employeeHashTable = new HashTable<>();
    }

    public void addScoreToEmployee(String name, int score, PrintWriter printWriter){
        if(score < 0){ // if the score is negative, remainder has no purpose, so I round it to the greater 200k where k€Z
            score = score - (score%200);
        }

        if(employeeHashTable.getEmployee(name) == null){ // no such employee case
            printWriter.write("There is no such employee.\n" );
        }
        else if (employeeHashTable.getEmployee(name).position.equals("CASHIER")){
            // check if the cashier was to be dismissed or in potential cooks list before the score update
            boolean wasPotentialCook = employeeHashTable.getEmployee(name).promotionPoints >= 3;
            boolean wasToBeDismissed = employeeHashTable.getEmployee(name).promotionPoints <= -5;
            // update the score
            employeeHashTable.getEmployee(name).promotionPoints += score/200;
            totalBonus += score%200;
            thisMonthBonus += score%200;
            if(employeeHashTable.getEmployee(name).promotionPoints >= 3 && !wasPotentialCook ){
                // if it was not a potential cook, but now it is, add it to the list
                potentialCooks.add(employeeHashTable.getEmployee(name));
            }
            if(employeeHashTable.getEmployee(name).promotionPoints < 3 && wasPotentialCook ){
                // if it was a potential cook, but not now, remove it from the list
                potentialCooks.remove(employeeHashTable.getEmployee(name));
            }
            if(wasToBeDismissed && employeeHashTable.getEmployee(name).promotionPoints > -5){
                // if it was to be dismissed, but not anymore remove it from the list
                cashiersToBeDismissed.remove(employeeHashTable.getEmployee(name));
            }
            if(employeeHashTable.getEmployee(name).promotionPoints <= -5){
                // if score is now <-5, try to dismiss it
                employeeWantsToLeave(name, printWriter,true);
            }else if(employeeHashTable.getEmployee(name).promotionPoints >= 3){
                // try to promote it
                if(numCashiers>1){ // this is the promotion case
                    numCashiers--;
                    numCooks++;
                    employeeHashTable.getEmployee(name).promotionPoints-=3;
                    employeeHashTable.getEmployee(name).position = "COOK";
                    potentialCooks.remove(employeeHashTable.getEmployee(name));
                    if (employeeHashTable.getEmployee(name).promotionPoints >= 10){
                        // if it also is eligible for being a manager add it to the list
                        potentialManagers.add(employeeHashTable.getEmployee(name));
                    }
                    printWriter.write(name + " is promoted from Cashier to Cook.\n");
                    if(cooksToBeDismissed.peekFirst() != null){
                        // num of cooks increased, check if there are any to be dismissed
                        employeeWantsToLeave(cooksToBeDismissed.peekFirst().name, printWriter, true);
                    }
                }
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("COOK")){
            // almost identical procedure with cashier
            boolean wasPotentialManager = employeeHashTable.getEmployee(name).promotionPoints >= 10;
            boolean wasToBeDismissed = employeeHashTable.getEmployee(name).promotionPoints <= -5;
            employeeHashTable.getEmployee(name).promotionPoints += score/200;
            totalBonus += score%200;
            thisMonthBonus += score%200;
            if(employeeHashTable.getEmployee(name).promotionPoints >= 10 && !wasPotentialManager ){
                potentialManagers.add(employeeHashTable.getEmployee(name));
                if (manager.promotionPoints <= -5){
                    employeeWantsToLeave(manager.name, printWriter, true);
                }
            }
            if(employeeHashTable.getEmployee(name).promotionPoints < 10 && wasPotentialManager ){
                potentialManagers.remove(employeeHashTable.getEmployee(name));
            }
            if(wasToBeDismissed && employeeHashTable.getEmployee(name).promotionPoints > -5){
                cooksToBeDismissed.remove(employeeHashTable.getEmployee(name));
            }
            if(employeeHashTable.getEmployee(name).promotionPoints <= -5){
                employeeWantsToLeave(name, printWriter,true);
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("MANAGER")){
            // update the score
            employeeHashTable.getEmployee(name).promotionPoints += score/200;
            totalBonus += score%200;
            thisMonthBonus += score%200;
            if(employeeHashTable.getEmployee(name).promotionPoints <= -5){
                // if it needs to be dismissed, send it to hell! >:D
                employeeWantsToLeave(name, printWriter,true);
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("COURIER")) {
            // almost identicel procedure with cashier
            boolean wasToBeDismissed = employeeHashTable.getEmployee(name).promotionPoints <= -5;
            employeeHashTable.getEmployee(name).promotionPoints += score / 200;
            totalBonus += score % 200;
            thisMonthBonus += score % 200;
            if (wasToBeDismissed && employeeHashTable.getEmployee(name).promotionPoints > -5){
                couriersToBeDismissed.remove(employeeHashTable.getEmployee(name));
            }
            if(employeeHashTable.getEmployee(name).promotionPoints <= -5){
                employeeWantsToLeave(name, printWriter,true);
            }
        }
    }

    public void employeeWantsToLeave(String name, PrintWriter printWriter, boolean dismiss){
        if(employeeHashTable.getEmployee(name) == null){
            printWriter.write("There is no such employee.\n" );
        }
        else if (employeeHashTable.getEmployee(name).position.equals("COURIER")){
            // if it is a courier
            if(numCouriers == 1 && employeeHashTable.getEmployee(name).promotionPoints > -5){
                // if it wants to leave but numcouriers == 1 give it money
                totalBonus += 200;
                thisMonthBonus += 200;
            }else if(numCouriers == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && !couriersToBeDismissed.contains(employeeHashTable.getEmployee(name))){
                // if it is being dismissed but numcouriers == 1 add it to the to be dismissed list
                couriersToBeDismissed.add(employeeHashTable.getEmployee((name)));
            }else if(numCouriers == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && couriersToBeDismissed.contains(employeeHashTable.getEmployee(name))){
                // if it is the above case, but it is already in the list, do nothing allahından bulsun
                return;
            } else{
                // else dismiss it
                numCouriers--;
                couriersToBeDismissed.remove(employeeHashTable.getEmployee(name));
                employeeHashTable.remove(employeeHashTable.getEmployee(name));
                if (dismiss){
                    printWriter.write(name + " is dismissed from branch: " + this.name.split(" ")[1] + ".\n");
                }else{
                    printWriter.write(name + " is leaving from branch: " + this.name.split(" ")[1] + ".\n");
                }
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("CASHIER")){
            // almost identical to courier case
            if(numCashiers == 1 && employeeHashTable.getEmployee(name).promotionPoints > -5){
                totalBonus += 200;
                thisMonthBonus += 200;
            }else if(numCashiers == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && !cashiersToBeDismissed.contains(employeeHashTable.getEmployee(name))) {
                cashiersToBeDismissed.add(employeeHashTable.getEmployee(name));
            } else if(numCashiers == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && cashiersToBeDismissed.contains(employeeHashTable.getEmployee(name))){
                return;
            } else{
                numCashiers--;
                potentialCooks.remove(employeeHashTable.getEmployee(name));
                cashiersToBeDismissed.remove(employeeHashTable.getEmployee(name));
                potentialManagers.remove(employeeHashTable.getEmployee(name));
                employeeHashTable.remove(employeeHashTable.getEmployee(name));
                if (dismiss){
                    printWriter.write(name + " is dismissed from branch: " + this.name.split(" ")[1] + ".\n");
                }else{
                    printWriter.write(name + " is leaving from branch: " + this.name.split(" ")[1] + ".\n");
                }
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("COOK")){
            //almost identical to courier case
            if(numCooks == 1 && employeeHashTable.getEmployee(name).promotionPoints > -5){
                totalBonus += 200;
                thisMonthBonus += 200;
            }else if(numCooks == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && !cooksToBeDismissed.contains(employeeHashTable.getEmployee(name))) {
                cooksToBeDismissed.add(employeeHashTable.getEmployee(name));
            }else if(numCooks == 1 && employeeHashTable.getEmployee(name).promotionPoints <= -5 && cooksToBeDismissed.contains(employeeHashTable.getEmployee(name))) {
                return;
            } else{
                numCooks--;
                couriersToBeDismissed.remove(employeeHashTable.getEmployee(name));
                potentialManagers.remove(employeeHashTable.getEmployee(name));
                employeeHashTable.remove(employeeHashTable.getEmployee(name));
                if (dismiss){
                    printWriter.write(name + " is dismissed from branch: " + this.name.split(" ")[1] + ".\n");
                }else{
                    printWriter.write(name + " is leaving from branch: " + this.name.split(" ")[1] + ".\n");
                }
            }
        }
        else if (employeeHashTable.getEmployee(name).position.equals("MANAGER")){

            if(employeeHashTable.getEmployee(name).promotionPoints > -5){
                if(potentialManagers.peekFirst() != null && potentialManagers.peekFirst().position.equals("COOK")){
                    numCooks--;
                    this.manager = potentialManagers.pollFirst();
                    this.manager.position = "MANAGER";
                    this.manager.promotionPoints -= 10;
                    employeeHashTable.remove(employeeHashTable.getEmployee(name));
                    printWriter.write(name + " is leaving from branch: " + this.name.split(" ")[1] + ".\n");
                    printWriter.write(this.manager.name + " is promoted from Cook to Manager.\n");
                }else{
                    totalBonus += 200;
                    thisMonthBonus += 200;
                }
            } else{
                if(potentialManagers.peekFirst() != null && potentialManagers.peekFirst().position.equals("COOK")){
                    numCooks--;
                    this.manager = potentialManagers.pollFirst();
                    this.manager.position = "MANAGER";
                    this.manager.promotionPoints -= 10;
                    employeeHashTable.remove(employeeHashTable.getEmployee(name));
                    printWriter.write(name + " is dismissed from branch: " + this.name.split(" ")[1] + ".\n");
                    printWriter.write(this.manager.name + " is promoted from Cook to Manager.\n");
                }
            }
        }
    }

    public void promotionCheck(String position, PrintWriter printWriter){
        if (position.equals("CASHIER")) {
            if (numCashiers > 1 && potentialCooks.peekFirst() != null) {
                addScoreToEmployee(potentialCooks.peekFirst().name,0,printWriter);
            }
        }
    }

    public void dismissCheck(String position, PrintWriter printWriter){
        if(position.equals("CASHIER")){
            if(numCashiers > 1 && cashiersToBeDismissed.peekFirst() != null){
                employeeWantsToLeave(cashiersToBeDismissed.pollFirst().name, printWriter, true);
            }
        } else if (position.equals("COOK")){
            if(numCooks > 1 && cooksToBeDismissed.peekFirst() != null){
                employeeWantsToLeave(cooksToBeDismissed.pollFirst().name, printWriter, true);
            }
        }
    }

    public void endOfMonth(){
        thisMonthBonus = 0;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        LahmacunShop other = (LahmacunShop) obj;
        return name != null ? name.equals(other.name) : other.name == null;
    }

}


