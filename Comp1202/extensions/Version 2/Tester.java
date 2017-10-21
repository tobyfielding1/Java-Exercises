/**
 * Test harness for COMP1202 Lab 3.
 * @author systjeh
 */
import java.util.*;
import java.io.*;

public class Tester {
    // Uncomment each part as you go.
    // Remember to test often!
    public static void main(String... args) {
        //testPart1a();
        //testPart1b();
        //testPart1c();
        //testPart2a();
        //testPart2b();
        //testPart2c();
        //testPart3();
        sendPeopleThrough();
    }
 
    public static void testPart1a() {
        System.out.println("Part 1 - Accessor methods");
        System.out.println("======");
 
        System.out.println("--- Part 1a ---");
        System.out.println();
 
        System.out.println("* Creating a new SmartCard for student Anna Undergrad...");
        SmartCard card = new SmartCard("Anna Undergrad");
        System.out.println("Owner is: " + card.getOwner());
        System.out.println();
    }
 

    public static void testPart1b() {
        System.out.println("--- Part 1b ---");
        System.out.println();
 
        SmartCard card = new SmartCard("Anna Undergrad");
 
        System.out.println("Is " + card.getOwner() + " staff? " + card.isStaff());
        System.out.println();
    }

 

    public static void testPart1c() {
        System.out.println("--- Part 1c ---");
        System.out.println();
 
        System.out.println("* Creating a new SmartCard for staff member Dr. Bob Lecturer...");
        SmartCard card = new SmartCard("Dr. Bob Lecturer");
        card.setStaff(true);
        System.out.println("Is " + card.getOwner() + " staff? " + card.isStaff());
        System.out.println();
    }

 

    public static void testPart2a() {
        System.out.println("Part 2 - Object interactions");
        System.out.println("======");
 
        System.out.println("--- Part 2a ---");
        System.out.println();
 
        System.out.println("* Creating a new CardLock...");
        CardLock lock = new CardLock();
        System.out.println();
 
        SmartCard cardA = new SmartCard("Anna Undergrad");
        SmartCard cardB = new SmartCard("Dr. Bob Lecturer");
        cardB.setStaff(true);
 
        System.out.println("* Swiping " + cardA.getOwner() + "'s card");
        lock.swipeCard(cardA);
        System.out.println("Last card seen: " + lock.getLastCardSeen().getOwner() + "'s card");
        System.out.println();
 
        System.out.println("* Swiping " + cardB.getOwner() + "'s card");
        lock.swipeCard(cardB);
        System.out.println("Last card seen: " + lock.getLastCardSeen().getOwner() + "'s card");
        System.out.println();
    }
 

    public static void testPart2b() {
        System.out.println("--- Part 2b ---");
        System.out.println();
 
        CardLock lock = new CardLock();
        SmartCard cardA = new SmartCard("Anna Undergrad");
        SmartCard cardB = new SmartCard("Dr. Bob Lecturer");
        cardB.setStaff(true);
 
        System.out.println("* Swiping some cards on the lock...");
        System.out.println("(This lock should only let staff in)");
        System.out.println();
 
        System.out.println("* Swiping " + cardA.getOwner() + "'s card");
        lock.swipeCard(cardA);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
 
        System.out.println("* Swiping " + cardB.getOwner() + "'s card");
        lock.swipeCard(cardB);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
    }

 

    public static void testPart2c() {
        System.out.println("--- Part 2c ---");
        System.out.println();
 
        CardLock lock = new CardLock();
        SmartCard cardA = new SmartCard("Anna Undergrad");
        SmartCard cardB = new SmartCard("Dr. Bob Lecturer");
        cardB.setStaff(true);
 
        System.out.println("* Toggling the lock to allow both students and staff...");
        lock.toggleStudentAccess();
        System.out.println();
 
        System.out.println("* Swiping " + cardA.getOwner() + "'s card");
        lock.swipeCard(cardA);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
 
        System.out.println("* Swiping " + cardB.getOwner() + "'s card");
        lock.swipeCard(cardB);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
 
        System.out.println("* Toggling the lock to allow only staff...");
        lock.toggleStudentAccess();
        System.out.println();
 
        System.out.println("* Swiping " + cardA.getOwner() + "'s card");
        lock.swipeCard(cardA);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
 
        System.out.println("* Swiping " + cardB.getOwner() + "'s card");
        lock.swipeCard(cardB);
        System.out.println("Is the card lock unlocked? " + lock.isUnlocked());
        System.out.println();
    }

 

    public static void testPart3() {
        System.out.println("Part 3 - Writing tests");
        System.out.println("======");
 
        Door myDoor = new Door();
        myDoor.setRoomName("Research Labs");
        CardLock myCardLock = new CardLock();
        myDoor.attachLock(myCardLock);
        SmartCard mySmartCard = new SmartCard("Mr. Bean");     
        myCardLock.swipeCard(mySmartCard);
        myDoor.openDoor();
        myDoor.closeDoor();
        
        System.out.println("Allowing student access");
        myCardLock.toggleStudentAccess();
        myCardLock.swipeCard(mySmartCard);
        myDoor.openDoor();
        myDoor.closeDoor();
        
        
    }

    public static void sendPeopleThrough() {
        System.out.println("Part 3 - Writing tests");
        System.out.println("======");
 
        Door myDoor = new Door();
        myDoor.setRoomName("Research Labs");
        CardLock myCardLock = new CardLock();
        myDoor.attachLock(myCardLock);
        SmartCard mySmartCard = new SmartCard("Mr. Bean");
        //creates hashmap to store all created swipe card objects (people)
        HashMap<String, SmartCard> map = new HashMap<String, SmartCard>();
        int i;
        //asks for names of five users to create five swipecard objects and put them through the door
        //they should all be declined.
        for (i=1;i<=5;i++) {
            
            System.out.println("Enter person to put through door");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String name;
            name = null;
 
            try
            {
                name = br.readLine();
            }
            catch (IOException ioe)
            { 
                System.err.println("There was an input error");
            }
            
            map.put(name, new SmartCard(name));
            myCardLock.swipeCard(map.get(name));
            myDoor.openDoor();
            myDoor.closeDoor();
            
        }
        
        //asks for person (swipe card object) who has already been created to convert to staff and retry entry 
        System.out.println("Enter person to turn to staff and put through door");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String name;
        name = null;
 
        try
        {
            name = br.readLine();
        }
        catch (IOException ioe)
        { 
            System.err.println("There was an input error");
        }
            
        System.out.println(map.get(name)); 
        map.get(name).setStaff(true);
        myCardLock.swipeCard(map.get(name));
        myDoor.openDoor();
        myDoor.closeDoor();
        
        //displays history of door users and their success/failure
        CardLock.swipeHistory();
    }
}