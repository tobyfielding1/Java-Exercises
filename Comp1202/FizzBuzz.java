public class FizzBuzz { 
	public static void main(String[] args) { //main function, where program starts
		for(Integer i = new Integer(1); i < 61; i++){ //defining i as the incrimenting variable, initialising it as 1, incrimenting by 1 per cycle up to 61.
			if(i % 3 != 0 && i % 5 != 0){ //if the incrimenting variable leaves a remeinder when divided by 3 or 5, then it is not a fizz or buzz
				System.out.print(i);	
			}
			if(i % 3 == 0){ // if i is a multiple of three it will leave no remainder, making the test true and initiating this 'if' section
				System.out.print("Fizz");
			}
			if(i % 5 == 0){ // if i is a multiple of five it will leave no remainder, making the test true and initiating this 'if' section
				System.out.print("Buzz");
			}
			System.out.println(); // this simply moves to a new line without printing anything
		}
	}
}









