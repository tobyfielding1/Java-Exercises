
import java.io.*;

public class Atm {
	
	int balance;
	
	public static void main(String [ ] args){
		Atm myAtm = new Atm();
		myAtm.go();
	}

	void display(String value){
		System.out.println("*****************************************\n" + value + "\n*****************************************");
	}

	//ask function always takes string question argument and returns integer answer, it will repeat question until valid answer given
	int ask(String question){
		Boolean complete = false;
		int answer = -1;

		//while loop runs until valid entry provided
		while (!complete){
			System.out.println(question);
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String number;
			number = null;
		 
			try
			{
				number = br.readLine();
			}
			catch (IOException ioe)
			{
				System.err.println("There was an input error");
			}
			
			//parse string value into integer method to cast as int
			try
			{
				answer =  new Integer(number);
			}
			catch (NumberFormatException e)
			{
				System.err.println("There is something wrong with the number you entered");
			}
				
			if (answer >= 0)
				complete = true;
			else
				display("you must enter a integer zero and above; please repeat...");
		}
		return answer;
	}

	void go() {
		int actionChoice;
		//action choice constants make code more readable
		final int withdraw = 1;
		final int deposit = 2;
		final int inquire = 3;
		final int quit = 4;

		display("Welcome to online ATM banking ");
		balance = ask("How much do you want in your account? \nEnter your number: ");
		
		while (true) {
			actionChoice = ask("What do you want to do?"+
					"\n '1' : Withdraw"+
					"\n '2' : Deposit"+
					"\n '3' : Inquire"+
					"\n '4' : Quit"+
					"\n Enter your number: ");

			if (actionChoice == withdraw)
				withdraw();
			else if(actionChoice == deposit)
				deposit();
			else if(actionChoice == inquire)
				display("Your Balance is " + balance);
			else if(actionChoice == quit) {
				display("goodbye");
				System.exit(0);
			}
			else 
				display("enty invalid, please enter a number between 1 and 4..");
		}
	}
	
	void withdraw() {
		Boolean complete = false;

		while (!complete){
			display("withdrawl");
			int withdrawAmount = ask("How much would you like to withdraw? \n (Enter '0' to go back): ");
			//tests for valid and possible entry
			if (withdrawAmount <= balance){
				balance = balance - withdrawAmount;
				complete = true;
				display("your new Balance is " + balance);
			}
			else 
				display("Insufficient funds, you only have " + balance + " in your account");
		}
	}
	
	void deposit() {
		display("deposit");
		int depositAmount = ask("How much would you like to deposit? \nEnter your number (Enter '0' to go back): ");
		balance = balance + depositAmount;
		display("your new Balance is " + balance);
	}
}

