import java.io.*;
import java.util.*;
/**
 * Write a description of class Main here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Main
{
    
    /**
     * Method askInt
     *
     * @param question (the user prompt)
     * @return a number (Integer) input by the user
     */
    public static Integer askInt(String question){   
        String number = null;
        Integer num = null;
        System.out.println(question);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            number = br.readLine();
        }
        catch (IOException ioe)
        {
            System.err.println("There was an input error");
        }
        try
        {
            num = new Integer(number);
        }
        catch (NumberFormatException e)
        {
            System.err
                        .println("There is something wrong with the number you entered");
        }
        return num;
    }
    
    public static void main(String [ ] args) {
        Integer num = askInt("Enter a number: ");  // see method in Main class
        
        /*
        * this part provides a list the first 20 natural numbers multiplied by the user input value
        */
        int i;
        int result;
        for (i=0; i<=20; i++) {
            result = num*i; 
            System.out.println(result);
            }
        
        /*
        * this part provides the last value of a 
        * consecutive summation of natural numbers, whereby the summation exceeds 500
        */    
        int total = 0;
        i = 0;
        while (total <500){
            i++;
            total = i + total;
        }
        System.out.println("500 was exceeded on the " + i + "th integer");
       
        /**
         * Part 2
         */
        //creating a usergroup and filling it with 10 user objects
        UserGroup myUserGroup = new UserGroup();
        myUserGroup.addSampleData();
        
        //printing contents of usergroup
        System.out.println("printing usernames");
        myUserGroup.printUsernames();
        
        /*
         * identifying and copying reference to user objects of "admin" userType into new Administators usergroup class
         */
        UserGroup Administrators = new UserGroup();
        //creating iterator to cycle through users
        Iterator<User> itr = myUserGroup.getUserIterator();
        
        User lastAdmin = null;  //variable stores last admin to be found by loop.
        //loop iterates through userGroup to copy admin users to administrators UserGroup
        while (itr.hasNext()){
            User thisUser = itr.next();
            if (thisUser.getUserType().equals("admin")){
                Administrators.addUser(thisUser);
                lastAdmin = thisUser;
            }
        }
        System.out.println("printing administrator usernames");
        Administrators.printUsernames();
        
        System.out.println("setting last admin to user");
        lastAdmin.setUserType("user");
        Administrators.removeLastUser();
        
        System.out.println("printing administrator usernames");
        Administrators.printUsernames();
        System.out.println("printing usernames");
        myUserGroup.printUsernames();

    }
}
