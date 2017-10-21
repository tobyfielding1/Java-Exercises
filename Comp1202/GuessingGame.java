public class GuessingGame {
	
	public static void main(String[] args) {
		System.out.println("Welcome to the game");
		Toolbox myToolbox = new Toolbox();
		Integer numberToGuess;
		Integer guessedNumber;
		numberToGuess = myToolbox.getRandomInteger(10);
		guessedNumber = myToolbox.readIntegerFromCmd ();
		if (numberToGuess.equals(guessedNumber)) {
		System.out.println("Well done u win");
		}
		else if (guessedNumber>numberToGuess) {
		System.out.println("Try Again, I guessed: " + numberToGuess);
		
		
		}
		else {
		System.out.println("Try Again, I guessed: " + numberToGuess);
		
		}
		
		
	}
}