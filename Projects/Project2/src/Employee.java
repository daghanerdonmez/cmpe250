public class Employee {

    public String name;
    public String position;
    public int promotionPoints;

    Employee(String name, String position){
        this.name = name;
        this.position = position;
    }

    public void addScore(int score){
        int promotionPoints = score/200;
        int bonusToBePaid = score%200;
    }

    public int hashCode() {
        return name.hashCode();
    }
}
