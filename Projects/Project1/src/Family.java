public class Family {

    private Member boss; //boss of the family

    public String log = ""; //outputs are logged here until they are written into the file

    private static class Member{

        //properties of Member class
        public String fullName;
        public Double gms;
        public int height; //height is being held as a property instead of calculating it everytime because it makes more sense for this projects purposes
        public Member leftMember;
        public Member rightMember;

        Member(String fullName, Double gms){
            this(fullName, gms, null, null);
        }
        Member(String fullName, Double gms, Member leftMember, Member rightMember){
            this.fullName = fullName;
            this.gms = gms;
            this.leftMember = leftMember;
            this.rightMember = rightMember;
            this.height = 0;
        }
    }

    Family() { this.boss = null; }

    //height method returns the height of each member, for null members it return -1 for the leafs to have 0 height
    private int height(Member member){
        if(member == null){
            return -1;
        }
        return member.height;
    }

    //distance of the boss to the member
    private int rank(Member member){
        int rank = 0;
        Member currentMember = this.boss;
        while(!currentMember.gms.equals(member.gms)){
            if(currentMember.gms > member.gms){
                currentMember = currentMember.leftMember;
                rank++;
            }else{
                currentMember = currentMember.rightMember;
                rank++;
            }
        }
        return rank;
    }

    //finds minimum valued member in a subtree
    private Member findMin(Member member){
        while (member.leftMember != null){
            member = member.leftMember;
        }
        return member;
    }

    //classic right rotation on avl trees
    public Member rightRotate(Member member){
        Member x = member.leftMember;
        Member y = x.rightMember;

        x.rightMember = member;
        member.leftMember = y;

        //heights are being updated on every process that might change heights
        member.height = Math.max(height(member.leftMember), height(member.rightMember)) + 1;
        x.height = Math.max(height(x.leftMember), height(x.rightMember)) + 1;

        return x;
    }

    //classic left rotation on avl trees
    public Member leftRotate(Member member){
        Member x = member.rightMember;
        Member y = x.leftMember;

        x.leftMember = member;
        member.rightMember = y;

        //heights are being updated on every process that might change heights
        member.height = Math.max(height(member.leftMember), height(member.rightMember)) + 1;
        x.height = Math.max(height(x.leftMember), height(x.rightMember)) + 1;

        return x;
    }

    //gives the balance of a members subtree
    public int getBalance(Member member){
        if(member == null){
            return 0;
        }
        return height(member.leftMember) - height(member.rightMember);
    }

    //base insert method calling for the proper one with insertion to boss
    public void insert(String fullName, Double gms){ this.boss = this.insert(fullName, gms, this.boss); }

    //the insert method that returns a member and runs recursively
    private Member insert(String fullName, Double gms, Member member){

        //if we reach a null member, we create a new member node and assign the values to it.
        if (member == null) {
            return new Member(fullName, gms);
        }

        //this works as intended when we take the recursive process into consideration
        this.log += member.fullName + " welcomed " + fullName + "\n";

        //classic insertion reasoning to a BST
        if(gms > member.gms) {
            //recursively insert into the right subtree
            member.rightMember = this.insert(fullName, gms, member.rightMember);
        }else if(gms < member.gms) {
            member.leftMember = this.insert(fullName, gms, member.leftMember);
        }else {
            return member;
        }

        //if a member is inserted into a members subtree its height is updated
        member.height = 1 + Math.max(height(member.leftMember), height(member.rightMember));

        //recursively balancing every superior of the inserted members subtree
        int balance = getBalance(member);

        if (balance > 1 && gms < member.leftMember.gms){
            return rightRotate(member);
        }else if(balance < -1 && gms > member.rightMember.gms){
            return leftRotate(member);
        }else if(balance > 1 && gms > member.leftMember.gms){
            member.leftMember = leftRotate(member.leftMember);
            return rightRotate(member);
        }else if(balance < -1 && gms < member.rightMember.gms){
            member.rightMember = rightRotate(member.rightMember);
            return leftRotate(member);
        }

        return member;
    }

    //base remove method
    public void remove(Double gms){this.boss = remove(gms, this.boss);}

    //the recursive remove method that returns a member that will be assigned to be the boss of that subtree
    //because it returns the balanced subtree's boss
    private Member remove(Double gms, Member member){
        //recursion base step
        if(member == null){
            return null;
        }

        //searching for the member to be deleted
        if(gms > member.gms){
            member.rightMember = remove(gms, member.rightMember);
        }else if(gms < member.gms){
            member.leftMember = remove(gms, member.leftMember);

        //when the member is found there are four cases
        //has two children
        //replace the member to be deleted with the smallest member of the right subtree
        //also recursions are done with a seperate remove method that does not log the "left and replaced by" part
        }else if(member.leftMember != null && member.rightMember != null){
            Member memberToReplace = this.findMin(member.rightMember);
            this.log += member.fullName + " left the family, replaced by " + memberToReplace.fullName + "\n";
            member.fullName = memberToReplace.fullName;
            member.gms = memberToReplace.gms;
            member.rightMember = this.removeWithoutLog(memberToReplace.gms, member.rightMember);

        //has only left child
        }else if(member.leftMember != null){
            this.log += member.fullName + " left the family, replaced by " + member.leftMember.fullName + "\n";
            member = member.leftMember;

        //has only right child
        }else if(member.rightMember != null){
            this.log += member.fullName + " left the family, replaced by " + member.rightMember.fullName + "\n";
            member = member.rightMember;

        //has no child
        }else {
            this.log += member.fullName + " left the family, replaced by nobody" + "\n";
            member = null;
        }

        if (member == null){
            return member;
        }


        //balances the removed version of the subtree and returns the boss of that subtree
        member.height = Math.max(height(member.leftMember), height(member.rightMember)) + 1;

        int balance = getBalance(member);

        if (balance > 1 && getBalance(member.leftMember) >= 0){
            return rightRotate(member);
        }else if(balance < -1 && getBalance(member.rightMember) <= 0){
            return leftRotate(member);
        }else if(balance > 1 && getBalance(member.leftMember) < 0){
            member.leftMember = leftRotate(member.leftMember);
            return rightRotate(member);
        }else if(balance < -1 && getBalance(member.rightMember) > 0){
            member.rightMember = rightRotate(member.rightMember);
            return leftRotate(member);
        }

        return member;
    }

    //the remove method that does the exact same thing with the first one but it does not log
    private Member removeWithoutLog(Double gms, Member member){
        if(member == null){
            return null;
        }

        if(gms > member.gms){
            member.rightMember = removeWithoutLog(gms, member.rightMember);
        }else if(gms < member.gms){
            member.leftMember = removeWithoutLog(gms, member.leftMember);
        }else if(member.leftMember != null && member.rightMember != null){
            Member memberToReplace = this.findMin(member.rightMember);
            member.fullName = memberToReplace.fullName;
            member.gms = memberToReplace.gms;
            member.rightMember = this.removeWithoutLog(memberToReplace.gms, member.rightMember);
        }else if(member.leftMember != null){
            member = member.leftMember;
        }else {
            member = member.rightMember;
        }

        if (member == null){
            return member;
        }

        member.height = Math.max(height(member.leftMember), height(member.rightMember)) + 1;

        int balance = getBalance(member);

        if (balance > 1 && getBalance(member.leftMember) >= 0){
            return rightRotate(member);
        }else if(balance < -1 && getBalance(member.rightMember) <= 0){
            return leftRotate(member);
        }else if(balance > 1 && getBalance(member.leftMember) < 0){
            member.leftMember = leftRotate(member.leftMember);
            return rightRotate(member);
        }else if(balance < -1 && getBalance(member.rightMember) > 0){
            member.rightMember = rightRotate(member.rightMember);
            return leftRotate(member);
        }

        return member;
    }

    //the method that finds their first common superior
    public void findLowestCommonAncestor(Double gms1, Double gms2){
        Double gmsMax = Math.max(gms1,gms2);
        Double gmsMin = Math.min(gms1, gms2);
        Member currentMember = this.boss;
        while (true) {
            //if the member's gms is bigger than both of them its right child is also a ancestor of both of them
            if (currentMember.gms > gmsMax) {
                currentMember = currentMember.leftMember;
            }
            //if the member's gms is smaller than both of them its left child is also a ancestor of both of them
            else if (currentMember.gms < gmsMin) {
                currentMember = currentMember.rightMember;
            }
            //else two members are on one on the right one on the left subtree and it is the lowest common ancestor
            else {
                this.log += "Target Analysis Result: " + currentMember.fullName + " " + String.format("%.3f", currentMember.gms) + "\n";
                return;
            }
        }
    }

    public void findSameRankMembers(Double gms){
        Member currentMember = this.boss;
        while (!currentMember.gms.equals(gms)){
            if(currentMember.gms > gms){
                currentMember = currentMember.leftMember;
            }else{
                currentMember = currentMember.rightMember;
            }
        }
        //above part finds the rank of the wanted member
        Integer rank = rank(currentMember);
        this.log += "Rank Analysis Result:";
        logInOrderWithRank(this.boss, rank); //this method does all the hard work
        this.log += "\n";
    }

    public void printInOrder(){
        printInOrder(this.boss);
    }

    private void printInOrder(Member member){
        if (member != null){
            printInOrder(member.leftMember);
            System.out.println(member.fullName + ',' + member.gms + "," + member.height);
            printInOrder(member.rightMember);
        }
    }

    private void logInOrderWithRank(Member member, Integer rank){
        //this method recursively goes to a given depth and logs the member at that depth
        //it automatically sorts them because of its recursive nature and it starts from the left
        if (member != null){
            logInOrderWithRank(member.leftMember, rank);
            if (rank(member) == rank){
                this.log += " " + member.fullName + " " + String.format("%.3f", member.gms);
            }
            logInOrderWithRank(member.rightMember, rank);
        }
    }


    public void maxSubNodeSelection(){
        Integer[] maxSubNodeSelection = maxSubNodeSelection(this.boss);
        Integer result = Math.max(maxSubNodeSelection[0], maxSubNodeSelection[1]);
        this.log += "Division Analysis Result: " + result + "\n";
    }

    //this is a recursive method that finds the most number of members that can be selected with the constraint
    //of not being able to select two members that are direct superior or inferiors of each other
    //it does it by asking a member, "how many members can I select in your subtree if I select you and if I don't select you"
    //this question is being answered by the base case that a leaf node member gives 1 and 0 as an answer to those questions
    //then, each member answers this question by looking at its children's answers
    //it is one more than excluded max number of selections of their children
    //or it the possible selection combinations of their children when that node is excluded
    //it is more visible in the code below
    private Integer[] maxSubNodeSelection(Member member){
        if (member == null){
            return new Integer[]{0,0};
        }

        //maximum number of nodes that can be selected from the left subtree as a two integer array:
        //[including left subtrees boss, excluding it]
        Integer[] leftMaxSubNodeSelection = maxSubNodeSelection(member.leftMember);
        //maximum number of nodes that can be selected from the right subtree as a two integer array:
        //[including right subtrees boss, excluding it]
        Integer[] rightMaxSubNodeSelection = maxSubNodeSelection(member.rightMember);

        Integer includedMaxSubNodeSelection = 1 + leftMaxSubNodeSelection[1] + rightMaxSubNodeSelection[1];
        Integer excludedMaxSubNodeSelection = Math.max(leftMaxSubNodeSelection[0], leftMaxSubNodeSelection[1]) + Math.max(rightMaxSubNodeSelection[0], rightMaxSubNodeSelection[1]);

        //returns its value to its parent
        return new Integer[]{includedMaxSubNodeSelection,excludedMaxSubNodeSelection};
    }
}

