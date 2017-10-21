
import java.util.*;
import java.io.*;

public class CardLock {

    SmartCard lastCard;
    Boolean isUnlocked;
    Boolean studentAccess;
    //static hashmaps store swipe card objects to record users of door and their success/failure
    public static HashMap<String, SmartCard> doorUsers = new HashMap<String, SmartCard>();
    public static HashMap<String, SmartCard> usersOpenedDoor = new HashMap<String, SmartCard>();
    public static HashMap<String, SmartCard> usersFailedDoor = new HashMap<String, SmartCard>();
    
    public CardLock() {
        studentAccess = false;
        isUnlocked = false;
    }

    public void swipeCard (SmartCard person) {
        lastCard = person;
        if (lastCard.isStaff() || (!lastCard.isStaff() && studentAccess) ) {
            isUnlocked = true;
            usersOpenedDoor.put(person.getOwner(), person);//adds swipe card object to hasmap of successful users
        }
        else { 
            isUnlocked = false;
            usersFailedDoor.put(person.getOwner(), person);//adds swipe card object to hasmap of unsuccessful users
        }
        doorUsers.put(person.getOwner(), person);//adds swipe card object to hasmap of all users
    }
    
    //method displays array of past users grouped by success or failure
    public static void swipeHistory() {
        System.out.println("door swipe history: ");
        String swipeHistory[] = {"users of cardlock:\n" + doorUsers.keySet()  +"\n","users opened:\n" + usersOpenedDoor.keySet() +"\n","users failed:\n" + usersFailedDoor.keySet() };
        System.out.println(Arrays.toString(swipeHistory));
    }
    
    public SmartCard getLastCardSeen() {
        return lastCard;
    }
    
    public Boolean isUnlocked() {
        return isUnlocked;
    }
    
    public void lockCardLock() {
        isUnlocked = false;
    }
    
    public void toggleStudentAccess() {
        studentAccess = !studentAccess;
    }
}